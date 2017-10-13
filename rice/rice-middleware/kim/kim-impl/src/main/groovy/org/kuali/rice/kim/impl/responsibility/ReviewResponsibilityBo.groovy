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
package org.kuali.rice.kim.impl.responsibility



import org.kuali.rice.kim.api.KimConstants

//strange - hacky non-PBO
class ReviewResponsibilityBo extends ResponsibilityBo {

    private static final long serialVersionUID = 1L

    public static final String ACTION_DETAILS_AT_ROLE_MEMBER_LEVEL_FIELD_NAME = "actionDetailsAtRoleMemberLevel"

    String documentTypeName
    String routeNodeName
    boolean actionDetailsAtRoleMemberLevel
    boolean required
    String qualifierResolverProvidedIdentifier

    public ReviewResponsibilityBo() {
    }

    public ReviewResponsibilityBo(ResponsibilityBo resp) {
        loadFromKimResponsibility(resp)
    }

    public void loadFromKimResponsibility(ResponsibilityBo resp) {
        resp.metaClass.properties.each {
            if (this.metaClass.respondsTo(this, MetaProperty.getSetterName(it.name))) {
                try {
                    if (resp.getProperty(it.name) != null) { // if it.name is null then the object is of type org.codehaus.groovy.runtime.NullObject
                        // which is not an expected class type and throws an IllegalArgumentException see KULRICE-6880
                        this.setProperty(it.name, resp.getProperty(it.name))
                    }
                } catch (IllegalArgumentException iae) {
                    throw new IllegalArgumentException("setProperty exception. type=" + it.type + " name=" + it.name + " resp.getProperty(it.name)=" + resp.getProperty(it.name) + " resp.getProperty(it.name) Class=" + resp.getProperty(it.name).getClass() + " : " + iae.getMessage(), iae);
                }
            }
        }

        Map<String,String> respDetails = resp.getAttributes()
        documentTypeName = respDetails.get(KimConstants.AttributeConstants.DOCUMENT_TYPE_NAME)
        routeNodeName = respDetails.get(KimConstants.AttributeConstants.ROUTE_NODE_NAME)
        actionDetailsAtRoleMemberLevel = Boolean.valueOf(respDetails.get(KimConstants.AttributeConstants.ACTION_DETAILS_AT_ROLE_MEMBER_LEVEL))
        required = Boolean.valueOf(respDetails.get(KimConstants.AttributeConstants.REQUIRED))
        qualifierResolverProvidedIdentifier = respDetails.get(KimConstants.AttributeConstants.QUALIFIER_RESOLVER_PROVIDED_IDENTIFIER)
    }
}
