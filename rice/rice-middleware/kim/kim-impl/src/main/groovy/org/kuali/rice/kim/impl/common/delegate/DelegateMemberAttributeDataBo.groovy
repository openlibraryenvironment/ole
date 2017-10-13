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
package org.kuali.rice.kim.impl.common.delegate;


import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.Table
import org.kuali.rice.kim.impl.common.attribute.KimAttributeDataBo

/**
 * @author Kuali Rice Team (rice.collab@kuali.org)
 */
@Entity
@Table(name = "KRIM_DLGN_MBR_ATTR_DATA_T")
public class DelegateMemberAttributeDataBo extends KimAttributeDataBo {
    @Column(name = "DLGN_MBR_ID")
    String assignedToId;
}
