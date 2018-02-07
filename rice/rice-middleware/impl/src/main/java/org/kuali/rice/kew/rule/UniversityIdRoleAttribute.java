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
package org.kuali.rice.kew.rule;

import java.util.ArrayList;
import java.util.List;

import org.kuali.rice.kew.api.identity.EmployeeId;
import org.kuali.rice.kew.api.identity.Id;
import org.kuali.rice.kew.api.rule.RoleName;

/**
 * A generic Role Attribute that can be used to route to an Empl ID.  Can take as configuration the
 * label to use for the element name in the XML.  This allows for re-use of this component in different
 * contexts.
 * 
 * @author Kuali Rice Team (rice.collab@kuali.org)
 */
public class UniversityIdRoleAttribute extends AbstractIdRoleAttribute {

    private static final long serialVersionUID = -7258860156654681708L;
    
    private static final String UNIVERSITY_ID_ROLE_NAME = "universityId";
    private static final String ATTRIBUTE_ELEMENT = "UniversityIdRoleAttribute";
    private static final String UNIVERSITY_ID_ELEMENT = "universityId";
    
    public List<RoleName> getRoleNames() {
	List<RoleName> roleNames = new ArrayList<RoleName>();
	roleNames.add(new RoleName(getClass().getName(), UNIVERSITY_ID_ROLE_NAME, "University ID"));
	return roleNames;
    }
    
    protected String getAttributeElementName() {
	return ATTRIBUTE_ELEMENT;
    }
    
    protected Id resolveId(String id) {
	return id == null ? null : new EmployeeId(id);
    }

    protected String getIdName() {
	return UNIVERSITY_ID_ELEMENT;
    }
    
    public String getUniversityId() {
        return getIdValue();
    }

    public void setUniversityId(String universityId) {
        setIdValue(universityId);
    }
    
}
