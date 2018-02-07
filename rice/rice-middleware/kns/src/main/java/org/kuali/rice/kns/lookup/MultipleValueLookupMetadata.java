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
package org.kuali.rice.kns.lookup;

import org.kuali.rice.krad.bo.PersistableBusinessObjectBase;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import java.sql.Timestamp;

@MappedSuperclass
public abstract class MultipleValueLookupMetadata extends PersistableBusinessObjectBase {
    @Id
    @Column(name="LOOKUP_RSLT_ID")
    private String lookupResultsSequenceNumber;
    @Column(name="PRNCPL_ID")
    private String lookupPersonId;
    /**
     * the time the lookup data was persisted, used by a batch purge job
     */
    //@Transient
    @Column(name="LOOKUP_DT")
    private Timestamp lookupDate;
    
    public String getLookupResultsSequenceNumber() {
        return lookupResultsSequenceNumber;
    }

    public void setLookupResultsSequenceNumber(String lookupResultsSequenceNumber) {
        this.lookupResultsSequenceNumber = lookupResultsSequenceNumber;
    }

    public String getLookupPersonId() {
        return lookupPersonId;
    }

    public void setLookupPersonId(String lookupPersonId) {
        this.lookupPersonId = lookupPersonId;
    }

    /**
     * @return the time the lookup data was persisted, used by a batch purge job
     */
    public Timestamp getLookupDate() {
        return lookupDate;
    }

    /**
     * @param lookupDate the time the lookup data was persisted, used by a batch purge job
     */
    public void setLookupDate(Timestamp lookupDate) {
        this.lookupDate = lookupDate;
    }
}

