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

import javax.persistence.FetchType;
import javax.persistence.Basic;
import javax.persistence.Lob;
import javax.persistence.Column;
import javax.persistence.Table;
import javax.persistence.Entity;

@Entity
@Table(name="KRNS_LOOKUP_RSLT_T")
public class LookupResults extends MultipleValueLookupMetadata {
    @Lob
	@Basic(fetch=FetchType.LAZY)
	@Column(name="SERIALZD_RSLTS")
	private String serializedLookupResults;

    public String getSerializedLookupResults() {
        return serializedLookupResults;
    }

    public void setSerializedLookupResults(String serializedLookupResults) {
        this.serializedLookupResults = serializedLookupResults;
    }
}

