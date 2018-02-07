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
package org.kuali.rice.kew.api.rule;


/**
 * This is an interface to define a Role Name for a role assigned to a RoleAttribute.
 *
 * @author Kuali Rice Team (rice.collab@kuali.org)
 */
public interface RoleNameContract {
    /**
     * This is the composite name value for the Role on an attribute.  It consists of
     * of the roleAttribute's class name + '!' + roleBaseName
     *
     * @return name
     */
    String getName();

    /**
     * This is the base name value for the Role on an attribute.  It consists of
     * of the name of the Role
     *
     * @return baseName
     */
    String getBaseName();

    /**
     * This is the return URL for the given Role for a role attribute
     *
     * @return returnUrl
     */
    String getReturnUrl();

    /**
     * A label for the Role on an attribute.
     *
     * @return label
     */
    String getLabel();
}
