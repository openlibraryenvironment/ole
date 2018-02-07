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
package org.kuali.rice.coreservice.web.component;

import org.kuali.rice.coreservice.impl.component.ComponentBo;
import org.kuali.rice.coreservice.impl.component.DerivedComponentBo;
import org.kuali.rice.kns.inquiry.KualiInquirableImpl;
import org.kuali.rice.krad.bo.BusinessObject;
import org.kuali.rice.krad.service.KRADServiceLocator;

import java.util.HashMap;
import java.util.Map;

/**
 * Since ParameterDetailType can be either DataDictionary or DB based, we need a custom {@link org.kuali.rice.krad.inquiry.Inquirable} to
 * check both.
 * 
 * @author Kuali Rice Team (rice.collab@kuali.org)
 *
 */
public class ComponentInquirableImpl extends KualiInquirableImpl {

	private static final String COMPONENT_CODE = "code";
	private static final String NAMESPACE_CODE = "namespaceCode";
	
	@Override
	public Object retrieveDataObject(Map fieldValues){
		BusinessObject result = (BusinessObject)super.retrieveDataObject(fieldValues);
		if (result == null) {
            result = loadDerivedComponent(fieldValues);
        }
		return result; 
    }
	
	@Override
	public BusinessObject getBusinessObject(Map fieldValues) {
		BusinessObject result = super.getBusinessObject(fieldValues);
		if (result == null) {
			result = loadDerivedComponent(fieldValues);
		}
		return result; 
	}

    protected ComponentBo loadDerivedComponent(Map fieldValues) {
        String componentCode = (String)fieldValues.get(COMPONENT_CODE);
	    String namespaceCode = (String)fieldValues.get(NAMESPACE_CODE);
        if (componentCode == null) {
            throw new RuntimeException(COMPONENT_CODE + " is a required key for this inquiry");
        }
	    if (namespaceCode == null) {
            throw new RuntimeException(NAMESPACE_CODE + " is a required key for this inquiry");
        }
        Map<String, Object> primaryKeys = new HashMap<String, Object>();
        primaryKeys.put(COMPONENT_CODE, componentCode);
        primaryKeys.put(NAMESPACE_CODE, namespaceCode);

        DerivedComponentBo derivedComponentBo = KRADServiceLocator.getBusinessObjectService().findByPrimaryKey(
                DerivedComponentBo.class, primaryKeys);
        if (derivedComponentBo != null) {
            return DerivedComponentBo.toComponentBo(derivedComponentBo);
        }
        return null;
    }
	
}
