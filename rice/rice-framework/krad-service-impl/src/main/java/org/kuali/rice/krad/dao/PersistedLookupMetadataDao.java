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
package org.kuali.rice.krad.dao;

import java.sql.Timestamp;

public interface PersistedLookupMetadataDao {
    
    /**
     * removes all LookupResults BO where the lookup date attribute is older than
     * the parameter
     * 
     * @param expirationDate all LookupResults having a lookup date before this date 
     * will be removed
     */
    public void deleteOldLookupResults(Timestamp expirationDate);
    
    /**
     * removes all LookupResults BO where the lookup date attribute is older than
     * the parameter
     * 
     * @param expirationDate all LookupResults having a lookup date before this date 
     * will be removed
     */
    public void deleteOldSelectedObjectIds(Timestamp expirationDate);
}
