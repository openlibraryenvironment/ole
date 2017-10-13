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
package org.kuali.rice.krms.impl.ui;

import org.apache.commons.lang.StringUtils;
import org.kuali.rice.core.api.resourceloader.GlobalResourceLoader;
import org.kuali.rice.core.api.util.ConcreteKeyValue;
import org.kuali.rice.core.api.util.KeyValue;
import org.kuali.rice.krad.uif.control.UifKeyValuesFinderBase;
import org.kuali.rice.krad.uif.view.ViewModel;
import org.kuali.rice.krad.web.form.MaintenanceDocumentForm;
import org.kuali.rice.krms.api.KrmsApiServiceLocator;
import org.kuali.rice.krms.api.repository.RuleManagementService;
import org.kuali.rice.krms.api.repository.context.ContextDefinition;
import org.kuali.rice.krms.api.repository.function.FunctionDefinition;
import org.kuali.rice.krms.api.repository.operator.CustomOperator;
import org.kuali.rice.krms.api.repository.type.KrmsTypeDefinition;
import org.kuali.rice.krms.api.repository.type.KrmsTypeRepositoryService;
import org.kuali.rice.krms.api.repository.typerelation.RelationshipType;
import org.kuali.rice.krms.api.repository.typerelation.TypeTypeRelation;
import org.kuali.rice.krms.framework.engine.expression.ComparisonOperator;
import org.kuali.rice.krms.impl.util.KrmsImplConstants;
import org.kuali.rice.krms.impl.util.KrmsServiceLocatorInternal;

import javax.xml.namespace.QName;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * ValueFinder for the operators available while editing a proposition.
 *
 * <p>Fetches the KeyValues for the operators available for use in propositions within the current context.</p>
 *
 * @author Kuali Rice Team (rice.collab@kuali.org)
 */
public class PropositionOpCodeValuesFinder extends UifKeyValuesFinderBase {

    private static final org.apache.log4j.Logger LOG = org.apache.log4j.Logger.getLogger(PropositionOpCodeValuesFinder.class);

    private static final List<KeyValue> LABELS;

    static {
        final List<KeyValue> labels = new ArrayList<KeyValue>( ComparisonOperator.values().length );
        for (ComparisonOperator operator : ComparisonOperator.values()) {
            labels.add(new ConcreteKeyValue(operator.getCode(), operator.getCode()));
        }

        LABELS = Collections.unmodifiableList(labels);
    }

    /**
     * @see UifKeyValuesFinderBase#getKeyValues(org.kuali.rice.krad.uif.view.ViewModel)
     */
    @Override
    public List<KeyValue> getKeyValues(ViewModel model) {

        List<KeyValue> keyValues = new ArrayList<KeyValue>(LABELS);

        MaintenanceDocumentForm maintenanceForm = (MaintenanceDocumentForm) model;
        AgendaEditor agendaEditor =
                (AgendaEditor) maintenanceForm.getDocument().getNewMaintainableObject().getDataObject();

        // context should never be null as otherwise rule editing would not be allowed
        ContextDefinition context = getRuleManagementService().getContext(agendaEditor.getAgenda().getContextId());

        keyValues.addAll(getCustomOperatorsKeyValuesForContextType(context.getTypeId()));

        return keyValues;
    }

    /**
     * Gets the {@link KeyValue}s for the {@link CustomOperator}s allowed to be used by the given context type.
     *
     * @param contextTypeId the context's type id
     * @return the KeyValue list for the allowed custom operator types
     */
    private List<KeyValue> getCustomOperatorsKeyValuesForContextType(String contextTypeId) {
        List<KeyValue> keyValues = new ArrayList<KeyValue>();

        if (contextTypeId == null) {
            return keyValues;
        }

        // Runtime checking for the CustomOperator service interface needs to be done
        List<TypeTypeRelation> typeRelations =
                getTypeRepositoryService().findTypeTypeRelationsByFromType(contextTypeId);

        for (TypeTypeRelation typeRelation : typeRelations) {
            if (typeRelation.getRelationshipType().equals(RelationshipType.USAGE_ALLOWED)) {
                KrmsTypeDefinition krmsType = getTypeRepositoryService().getTypeById(typeRelation.getToTypeId());

                Object service = getTypeServiceImplementation(krmsType);

                if (service != null && service instanceof CustomOperator) {
                    // Bingo, we have a custom operator to add to the list
                    keyValues.add(getKeyValueForCustomOperator(krmsType, (CustomOperator)service));
                }
            }
        }

        return keyValues;
    }

    /**
     * Gets the KeyValue for the given custom operator and corresponding KRMS type.
     *
     * <p>A special convention is used for the key values: {@code customOperator:<nameSpace>:<serviceName>}</p>
     * <p>Values are the function name, which is what will be displayed in the operator dropdown.</p>
     *
     * @param krmsType the krms type for the given customOperator
     * @param customOperator the custom operator, assumed to be not null
     * @return a KeyValue
     */
    private ConcreteKeyValue getKeyValueForCustomOperator(KrmsTypeDefinition krmsType, CustomOperator customOperator) {
        FunctionDefinition operatorFunctionDefinition =
                customOperator.getOperatorFunctionDefinition();
        String key = KrmsImplConstants.CUSTOM_OPERATOR_PREFIX
                + krmsType.getNamespace() +
                ":" + krmsType.getServiceName();

        return new ConcreteKeyValue(key, operatorFunctionDefinition.getName());
    }

    /**
     * Returns the service for the given KRMS type, or null if none can be found.
     *
     * @param krmsType the type to return the service for
     * @return the service or null if none can be found
     */
    private Object getTypeServiceImplementation(KrmsTypeDefinition krmsType) {
        Object service = null;

        if (krmsType != null && !StringUtils.isEmpty(krmsType.getServiceName())) {
            QName serviceQName = new QName(krmsType.getNamespace(), krmsType.getServiceName());
            service = GlobalResourceLoader.getService(serviceQName);
        }

        return service;
    }

    /**
     * lazy initialization holder class idiom, see Effective Java 2nd Ed. item # 71, J. Bloch
     */
    private static class TypeRepositoryServiceHolder {
        static final KrmsTypeRepositoryService typeRepositoryService =
                KrmsApiServiceLocator.getKrmsTypeRepositoryService();
    }

    // getter for lazy init service
    private KrmsTypeRepositoryService getTypeRepositoryService() {
        return TypeRepositoryServiceHolder.typeRepositoryService;
    }

    /**
     * lazy initialization holder class idiom, see Effective Java 2nd Ed. item # 71, J. Bloch
     */
    private static class RuleManagementServiceHolder {
        static final RuleManagementService ruleManagementService = KrmsServiceLocatorInternal.getService(
                "ruleManagementService");
    }

    // getter for lazy init service
    private RuleManagementService getRuleManagementService() {
        return RuleManagementServiceHolder.ruleManagementService;
    }

}
