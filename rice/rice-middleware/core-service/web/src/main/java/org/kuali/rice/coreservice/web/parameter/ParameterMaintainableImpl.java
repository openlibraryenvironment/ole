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
   package org.kuali.rice.coreservice.web.parameter;

import org.kuali.rice.coreservice.api.CoreServiceApiServiceLocator;
import org.kuali.rice.coreservice.api.parameter.Parameter;
import org.kuali.rice.coreservice.api.parameter.ParameterKey;
import org.kuali.rice.coreservice.api.parameter.ParameterRepositoryService;
import org.kuali.rice.coreservice.api.parameter.ParameterType;
import org.kuali.rice.coreservice.impl.parameter.ParameterBo;
import org.kuali.rice.kns.maintenance.KualiMaintainableImpl;

public class ParameterMaintainableImpl extends KualiMaintainableImpl {
    private static final long serialVersionUID = 4914145799502207182L;

    @Override
    public void saveDataObject() {
            if (super.getDataObject() instanceof ParameterBo) {
                ParameterBo object  = (ParameterBo)getDataObject();
                Parameter param = null;

                // construct a ParameterType.Builder for when we need to set the type
                // (since object may not have a ParameterType set)
                ParameterType.Builder parameterTypeBuilder =
                        ParameterType.Builder.create(object.getParameterTypeCode());

                ParameterRepositoryService parameterRepository = CoreServiceApiServiceLocator.getParameterRepositoryService();
                
                param = parameterRepository.getParameter(
                        ParameterKey.create(object.getApplicationId(),
                        object.getNamespaceCode(), 
                        object.getComponentCode(), 
                        object.getName()));

                // Note that the parameter repository service will try to get back a parameter with the Rice application
                // ID if it can't find it with the give application ID.  If the given application ID is not Rice, and a
                // parameter for Rice comes back, we have to be careful and not update it as that would effectively
                // erase the Rice parameter.
                
                if(param == null || !object.getApplicationId().equals(param.getApplicationId())) { // create new
                    param = parameterRepository.createParameter(Parameter.Builder.create(object.getApplicationId(),
                            object.getNamespaceCode(), object.getComponentCode(), object.getName(), parameterTypeBuilder
                    /* set the type using our builder from above */).build());

                }

                Parameter.Builder b = Parameter.Builder.create(param);
                b.setValue(object.getValue());
                b.setDescription(object.getDescription());
                b.setEvaluationOperator(object.getEvaluationOperator());
                b.setParameterType(parameterTypeBuilder); // set the type using our builder from above

                parameterRepository.updateParameter(b.build());

            } else {
                throw new RuntimeException(
                        "Cannot update object of type: " + this.getDataObjectClass() + " with Parameter repository service");
            }
        }

}
