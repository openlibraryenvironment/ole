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
package org.kuali.rice.krad.uif.container;

import org.kuali.rice.krad.datadictionary.parse.BeanTag;
import org.kuali.rice.krad.datadictionary.parse.BeanTagAttribute;
import org.kuali.rice.krad.uif.component.ComponentSecurity;

/**
 * Collection Group security is used to flag that permissions exist for the associated {@link CollectionGroup}
 * in KIM and should be checked to determine the associated group, line, and field state. In particular this adds
 * the edit line and view line flags
 *
 * <p>
 * In addition, properties such as additional role and permission details can be configured to use when
 * checking the KIM permissions
 * </p>
 *
 * @author Kuali Rice Team (rice.collab@kuali.org)
 */
@BeanTag(name = "collectionGroupSecurity-bean")
public class CollectionGroupSecurity extends ComponentSecurity {
    private static final long serialVersionUID = 1134455196763917062L;

    private boolean editLineAuthz;
    private boolean viewLineAuthz;

    public CollectionGroupSecurity() {
        super();

        editLineAuthz = false;
        viewLineAuthz = false;
    }

    /**
     * Indicates whether the collection group line has edit authorization and KIM should be consulted
     *
     * @return true if the line has edit authorization, false if not
     */
    @BeanTagAttribute(name="editLineAuthz")
    public boolean isEditLineAuthz() {
        return editLineAuthz;
    }

    /**
     * Setter for the edit line authorization flag
     *
     * @param editLineAuthz
     */
    public void setEditLineAuthz(boolean editLineAuthz) {
        this.editLineAuthz = editLineAuthz;
    }

    /**
     * Indicates whether the collection group line has view authorization and KIM should be consulted
     *
     * @return true if the line has view authorization, false if not
     */
    @BeanTagAttribute(name="viewLineAuthz")
    public boolean isViewLineAuthz() {
        return viewLineAuthz;
    }

    /**
     * Setter for the view line authorization flag
     *
     * @param viewLineAuthz
     */
    public void setViewLineAuthz(boolean viewLineAuthz) {
        this.viewLineAuthz = viewLineAuthz;
    }

    /**
     * @see org.kuali.rice.krad.uif.component.ComponentSecurity#copy()
     */
    @Override
    protected <T> void copyProperties(T componentSecurity) {
        super.copyProperties(componentSecurity);
        CollectionGroupSecurity collectionGroupSecurityCopy = (CollectionGroupSecurity) componentSecurity;
        collectionGroupSecurityCopy.setEditLineAuthz(this.editLineAuthz);
        collectionGroupSecurityCopy.setViewLineAuthz(this.viewLineAuthz);
    }
}
