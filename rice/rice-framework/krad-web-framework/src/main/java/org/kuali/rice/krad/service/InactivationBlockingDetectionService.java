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
package org.kuali.rice.krad.service;

import org.kuali.rice.krad.bo.BusinessObject;
import org.kuali.rice.krad.datadictionary.InactivationBlockingMetadata;

import java.util.Collection;

/**
 * This service detects whether there are any records that block the inactivation of a particular record
 *
 * @author Kuali Rice Team (rice.collab@kuali.org)
 */
public interface InactivationBlockingDetectionService {
    /**
     * Determines whether there is ANY record in the relationship defined by the inactivationBlockingMetadata that prevents inactivation of blockedBo
     *
     * @param blockedBo a BO that is potentially inactivation blocked
     * @param inactivationBlockingMetadata
     * @return true iff there was a record that blocks the blockedBo using the metadata in inactivationBlockingMetadata
     */
    public boolean hasABlockingRecord(BusinessObject blockedBo, InactivationBlockingMetadata inactivationBlockingMetadata);

    /**
     * Lists all records in the relationship defined by the inactivationBlockingMetadata that prevents inactivation of blockedBo
     *
     * @param blockedBo a BO that is potentially inactivation blocked
     * @param inactivationBlockingMetadata
     * @return true iff there was a record that blocks the blockedBo using the metadata in inactivationBlockingMetadata
     */
    public Collection<BusinessObject> listAllBlockerRecords(BusinessObject blockedBo, InactivationBlockingMetadata inactivationBlockingMetadata);
}
