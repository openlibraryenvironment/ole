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
package org.kuali.rice.krad.uif.component;

import org.kuali.rice.krad.datadictionary.parse.BeanTag;
import org.kuali.rice.krad.datadictionary.parse.BeanTagAttribute;
import org.kuali.rice.krad.datadictionary.uif.UifDictionaryBeanBase;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * Component security is used to flag permissions that exist in KIM for various component state (like edit and view)
 *
 * <p>
 * In addition, properties such as additional role and permission details can be configured to use when
 * checking the KIM permissions
 * </p>
 *
 * <p>
 * Security subclasses exist adding on flags apporiate for that component
 * </p>
 *
 * @author Kuali Rice Team (rice.collab@kuali.org)
 */
@BeanTag(name = "componentSecurity-bean")
public class ComponentSecurity extends UifDictionaryBeanBase implements Serializable {
    private static final long serialVersionUID = 726347449984853891L;

    private boolean editAuthz;
    private boolean viewAuthz;

    private String namespaceAttribute;
    private String componentAttribute;
    private String idAttribute;

    private Map<String, String> additionalPermissionDetails;
    private Map<String, String> additionalRoleQualifiers;

    public ComponentSecurity() {
        editAuthz = false;
        viewAuthz = false;

        additionalPermissionDetails = new HashMap<String, String>();
        additionalRoleQualifiers = new HashMap<String, String>();
    }

    /**
     * Indicates whether the component has edit authorization and KIM should be consulted
     *
     * @return true if the component has edit authorization, false if not
     */
    @BeanTagAttribute(name="editAuthz")
    public boolean isEditAuthz() {
        return editAuthz;
    }

    /**
     * Setter for the edit authorization flag
     *
     * @param editAuthz
     */
    public void setEditAuthz(boolean editAuthz) {
        this.editAuthz = editAuthz;
    }

    /**
     * Indicates whether the component has view authorization and KIM should be consulted
     *
     * @return true if the component has view authorization, false if not
     */
    @BeanTagAttribute(name="viewAuthz")
    public boolean isViewAuthz() {
        return viewAuthz;
    }

    /**
     * Setter for the view authorization flag
     *
     * @param viewAuthz
     */
    public void setViewAuthz(boolean viewAuthz) {
        this.viewAuthz = viewAuthz;
    }

    /**
     * Namespace code that should be sent as permission detail when doing a permission check on this field
     *
     * <p>
     * When the namespace code is a detail for a permission check, this property can be configured to override the
     * namespace derived by the system
     * </p>
     *
     * @return namespace code
     */
    @BeanTagAttribute(name="namespaceAttribute")
    public String getNamespaceAttribute() {
        return namespaceAttribute;
    }

    /**
     * Setter for the namespace code to use for details
     *
     * @param namespaceAttribute
     */
    public void setNamespaceAttribute(String namespaceAttribute) {
        this.namespaceAttribute = namespaceAttribute;
    }

    /**
     * Component code that should be sent as permission detail when doing a permission check on this field
     *
     * <p>
     * When the component code is a detail for a permission check, this property can be configured to override the
     * component code derived by the system
     * </p>
     *
     * @return component code
     */
    @BeanTagAttribute(name="componentAttribute")
    public String getComponentAttribute() {
        return componentAttribute;
    }

    /**
     * Setter for the component code to use for details
     *
     * @param componentAttribute
     */
    public void setComponentAttribute(String componentAttribute) {
        this.componentAttribute = componentAttribute;
    }

    /**
     * Id that should be sent as permission detail when doing a permission check on this field
     *
     * <p>
     * By default they system will send the component id as a permission detail, this property can be configured to
     * send a different id for the permission check
     * </p>
     *
     * @return id
     */
    @BeanTagAttribute(name="idAttribute")
    public String getIdAttribute() {
        return idAttribute;
    }

    /**
     * Setter for the id to use for details
     *
     * @param idAttribute
     */
    public void setIdAttribute(String idAttribute) {
        this.idAttribute = idAttribute;
    }

    /**
     * Map of key value pairs that should be added as permission details when doing KIM permission checks for this
     * component
     *
     * <p>
     * Any details given here that will override details with the same key that were derived by the system
     * </p>
     *
     * @return Map<String, String>
     */
    @BeanTagAttribute(name="additionalPermissionDetails",type= BeanTagAttribute.AttributeType.MAPVALUE)
    public Map<String, String> getAdditionalPermissionDetails() {
        return additionalPermissionDetails;
    }

    /**
     * Setter for the map of additional permission details
     *
     * @param additionalPermissionDetails
     */
    public void setAdditionalPermissionDetails(Map<String, String> additionalPermissionDetails) {
        this.additionalPermissionDetails = additionalPermissionDetails;
    }

    /**
     * Map of key value pairs that should be added as role qualifiers when doing KIM permission checks for this
     * component
     *
     * <p>
     * Any qualifiers given here that will override qualifiers with the same key that were derived by the system
     * </p>
     *
     * @return Map<String, String>
     */
    @BeanTagAttribute(name="additionalRoleQualifiers",type= BeanTagAttribute.AttributeType.MAPVALUE)
    public Map<String, String> getAdditionalRoleQualifiers() {
        return additionalRoleQualifiers;
    }

    /**
     * Setter for the map of additional role qualifiers
     *
     * @param additionalRoleQualifiers
     */
    public void setAdditionalRoleQualifiers(Map<String, String> additionalRoleQualifiers) {
        this.additionalRoleQualifiers = additionalRoleQualifiers;
    }

    @Override
    protected void finalize() throws Throwable {
        try {
            idAttribute = null;
            componentAttribute = null;
            namespaceAttribute = null;
            additionalRoleQualifiers = null;
            additionalPermissionDetails = null;
        } finally {
            // don't call super.finalize() in attempt to avoid loop between maps.
        }
    }

    /**
     * Returns a copy of the component security.
     *
     * @return ComponentSecurity copy of the component
     */
    public <T> T copy() {
        T copiedClass = null;
        try {
            copiedClass = (T)this.getClass().newInstance();
        }
        catch(Exception exception) {
            throw new RuntimeException();
        }

        copyProperties(copiedClass);

        return copiedClass;
    }

    protected <T> void copyProperties(T componentSecurity) {
        super.copyProperties(componentSecurity);
        ComponentSecurity componentSecurityCopy = (ComponentSecurity) componentSecurity;

        if (this.additionalPermissionDetails != null) {
            componentSecurityCopy.setAdditionalPermissionDetails(new HashMap<String, String>(this.additionalPermissionDetails));
        }

        if (this.additionalRoleQualifiers != null) {
            componentSecurityCopy.setAdditionalRoleQualifiers(new HashMap<String, String>(this.additionalRoleQualifiers));
        }

        componentSecurityCopy.setComponentAttribute(this.componentAttribute);
        componentSecurityCopy.setEditAuthz(this.editAuthz);
        componentSecurityCopy.setIdAttribute(this.idAttribute);
        componentSecurityCopy.setNamespaceAttribute(this.namespaceAttribute);
        componentSecurityCopy.setViewAuthz(this.viewAuthz);
    }
}
