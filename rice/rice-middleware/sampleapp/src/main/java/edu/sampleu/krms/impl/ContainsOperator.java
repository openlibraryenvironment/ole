/**
 * Copyright 2005-2014 The Kuali Foundation
 *
 * Licensed under the Educational Community License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.opensource.org/licenses/ecl2.php
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package edu.sampleu.krms.impl;

import org.kuali.rice.core.api.resourceloader.GlobalResourceLoader;
import org.kuali.rice.core.api.uif.RemotableAttributeError;
import org.kuali.rice.krms.api.KrmsApiServiceLocator;
import org.kuali.rice.krms.api.repository.function.FunctionDefinition;
import org.kuali.rice.krms.api.repository.function.FunctionRepositoryService;
import org.kuali.rice.krms.api.repository.operator.CustomOperator;
import org.kuali.rice.krms.api.repository.type.KrmsTypeDefinition;
import org.kuali.rice.krms.api.repository.type.KrmsTypeRepositoryService;
import org.kuali.rice.krms.framework.engine.Function;
import org.kuali.rice.krms.framework.type.FunctionTypeService;
import org.kuali.rice.krms.impl.repository.FunctionBoService;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

/**
 * An example CustomOperator to demonstrate this functionality in of the KRMS UI and engine.
 *
 * <p>This one also does the FunctionTypeService duties to produce the engine executable, though that responsibility
 * could be extracted to a separate class if desired, though that would require more wiring and database configuration.
 * </p>
 *
 * <p>This implementation cheats a bit for easy configuration by persisting the FunctionDefinition on first access.
 * The only prerequisite configuration to use it: 1) add a KRMS type with namespace "KR-SAP", name "contains operator",
 * and the serviceName "sampleAppContainsOperatorService".  2) add a type relation of type 'A' (usage allowed) from
 * your KRMS type to the context type you want to use it with.</p>
 *
 * <p>This service implementation is wired up in Spring and exported to the service bus.</p>
 *
 * <!-- NOTE: all ID and FK columns in the example SQL below should be replaced appropriately, but you knew that. -->
 *
 * <!-- sample SQL for creating the KRMS type:
 * insert into krms_typ_t (typ_id, nm, nmspc_cd, srvc_nm, actv, ver_nbr)
 * values ('OPERATOR-KRMS-TYPE-ID', 'contains operator', 'KR-SAP', 'sampleAppContainsOperatorService', 'Y', 1);
 * -->
 *
 * <!-- sample SQL for creating the type relation:
 * insert into krms_typ_reln_t (typ_reln_id, from_typ_id, to_typ_id, reln_typ, seq_no)
 * values ('A-UNIQUE-ID', 'CONTEXT-TYPE-ID', 'OPERATOR-KRMS-TYPE-ID', 'A', 1);
 * -->
 *
 * @author Kuali Rice Team (rice.collab@kuali.org)
 */
public class ContainsOperator implements CustomOperator, FunctionTypeService {

    /**
     * Returns the FunctionDefinition for the custom function the executable portion of this CustomOperator will
     * call.
     *
     * <p>If the FunctionDefinition hasn't been persisted yet, this method will persist it and then return it.</p>
     *
     * <p>Note that having the KRMS type for this </p>
     *
     * @return
     */
    @Override
    public FunctionDefinition getOperatorFunctionDefinition() {
        FunctionBoService functionBoService =
                (FunctionBoService) GlobalResourceLoader.getService("functionRepositoryService");

        KrmsTypeRepositoryService typeRepository = KrmsApiServiceLocator.getKrmsTypeRepositoryService();
        KrmsTypeDefinition containsOperatorType = typeRepository.getTypeByName("KR-SAP", "contains operator");

        if (containsOperatorType == null) {
            throw new IllegalStateException("There must be a persisted KRMS type with namespace 'KR-SAP', "
                    + "name 'contains operator', and serviceName 'sampleAppContainsOperatorService'");
        }

        FunctionDefinition containsFunction = functionBoService.getFunctionByNameAndNamespace("contains", "KR-SAP");

        if (containsFunction == null) {
            FunctionDefinition.Builder functionBuilder =
                    FunctionDefinition.Builder.create("KR-SAP", "contains", "java.lang.Boolean", containsOperatorType.getId());

            containsFunction = functionBoService.createFunction(functionBuilder.build());
        }

        return containsFunction;
    }

    /**
     * Validate the argument types.  This operator supports (String, String), and (Collection, Object).  For other
     * classes, errors will be produced.  Note that at the current time (Rice 2.3.1), the rhsClassName will always be
     * "java.lang.String" since the UI is constrained to set the second arg as a constant value.
     *
     * @param lhsClassName the class name for the left hand side operand
     * @param rhsClassName the class name for the right hand side operand
     * @return errors
     */
    @Override
    public List<RemotableAttributeError> validateOperandClasses(String lhsClassName, String rhsClassName) {
        List<RemotableAttributeError> errors = new ArrayList<RemotableAttributeError>();

        if (String.class.getName().equals(lhsClassName)) {
            if (!String.class.getName().equals(rhsClassName)) {
                RemotableAttributeError.Builder errorBuilder =
                        RemotableAttributeError.Builder.create("operator", "right hand operand is not a String");
                errors.add(errorBuilder.build());
            }
        } else {
            try {
                Class<?> clazz = getClass().getClassLoader().loadClass(lhsClassName);

                if (!Collection.class.isAssignableFrom(clazz)) {
                    RemotableAttributeError.Builder errorBuilder =
                            RemotableAttributeError.Builder.create("operator", "left hand operand is not a Collection");
                    errors.add(errorBuilder.build());
                }
            } catch (ClassNotFoundException e) {
                // it is not a class available to the class loader so we can't determine if it extends Collection or not
            }
        }

        return errors;
    }

    /**
     * Loads the Function object that the KRMS engine can execute during rule evaluation
     *
     * @param functionDefinition {@link FunctionDefinition} to create the {@link Function} from.
     * @return
     */
    @Override
    public Function loadFunction(FunctionDefinition functionDefinition) {
        if (!"contains".equals(functionDefinition.getName()) || "KR-SAP".equals(functionDefinition.getNamespace())) {
            throw new IllegalArgumentException("oops, you have the wrong type service, I can't load this function");
        }

        // return our KRMS engine executable operator function:
        return new Function() {
            @Override
            public Object invoke(Object... arguments) {
                if (arguments.length != 2) {
                    throw new IllegalArgumentException("contains operator expects 2 arguments");
                }

                if (arguments[0] instanceof String) {
                    if (arguments[1] instanceof String) {
                        return Boolean.valueOf(((String)arguments[0]).contains((String)arguments[1]));
                    } else {
                        throw new IllegalArgumentException("if the first argument is a String, the second argument "
                                + "must be as well");
                    }
                } else if (arguments[0] instanceof Collection) {
                    return Boolean.valueOf(((Collection)arguments[0]).contains(arguments[1]));
                }

                throw new IllegalArgumentException("argument types must be either (String, String) or (Collection, Object)");
            }
        };
    }
}
