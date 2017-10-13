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
package org.kuali.rice.krad.datadictionary;

/**
 * This interface provides read-only metadata about inactivation blocking.  This metadata object is associated with a
 * business object.
 * The source of this information often comes from the data dictionary file.
 *
 * @author Kuali Rice Team (rice.collab@kuali.org)
 */
public interface InactivationBlockingMetadata {

    /**
     * The property name of the reference that is blocked
     */
    public String getBlockedReferencePropertyName();

    /**
     * The type of the object that is blocked
     *
     * @return
     */
    public Class getBlockedBusinessObjectClass();

    /**
     * The bean name of the service that is responsible for determining whether there are any records that block
     * inactivation
     */
    public String getInactivationBlockingDetectionServiceBeanName();

    /**
     * The type of the object that is blocking another record
     */
    public Class getBlockingReferenceBusinessObjectClass();

    /**
     * Returns the human-meaningful name of the relationship
     *
     * @return
     */
    public String getRelationshipLabel();
}
