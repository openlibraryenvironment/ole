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
package org.kuali.rice.krad.bo;

import javax.persistence.Entity;
import javax.persistence.IdClass;
import javax.persistence.Table;
import javax.persistence.Transient;


/**
 * Ad Hoc Route Workgroup Business Object
 *
 * @author Kuali Rice Team (rice.collab@kuali.org)
 */
@IdClass(org.kuali.rice.krad.bo.AdHocRouteWorkgroupId.class)
@Entity
@Table(name="KRNS_ADHOC_RTE_ACTN_RECIP_T")
public class AdHocRouteWorkgroup extends AdHocRouteRecipient {
    private static final long serialVersionUID = 1L;

    @Transient
    private String recipientNamespaceCode;

    @Transient
    private String recipientName;

    public AdHocRouteWorkgroup() {
        setType(WORKGROUP_TYPE);
    }

    @Override
    public void setType(Integer type) {
        if (!WORKGROUP_TYPE.equals(type)) {
            throw new IllegalArgumentException("cannot change type to " + type);
        }
        super.setType(type);
    }

    @Override
    public String getName() {
        return "";
    }

    public String getRecipientNamespaceCode() {
        return this.recipientNamespaceCode;
    }

    public String getRecipientName() {
        return this.recipientName;
    }

    public void setRecipientNamespaceCode(String recipientNamespaceCode) {
        this.recipientNamespaceCode = recipientNamespaceCode;
    }

    public void setRecipientName(String recipientName) {
        this.recipientName = recipientName;
    }
}
