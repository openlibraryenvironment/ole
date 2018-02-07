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
package org.kuali.rice.kim.impl.common.active

import java.sql.Timestamp
import javax.persistence.Column
import org.kuali.rice.core.api.mo.common.active.InactivatableFromToUtils
import org.kuali.rice.krad.bo.PersistableBusinessObjectBase
import org.joda.time.DateTime

public abstract class ActiveFromToBo extends PersistableBusinessObjectBase {
    @Column(name = "ACTV_FRM_DT")
	Timestamp activeFromDateValue

	@Column(name = "ACTV_TO_DT")
	Timestamp activeToDateValue

    boolean isActive(Timestamp activeAsOfDate) {
        return InactivatableFromToUtils.isActive(getActiveFromDate(), getActiveToDate(), new DateTime(activeAsOfDate.getTime()))
    }

    boolean isActive(DateTime activeAsOfDate) {
        return InactivatableFromToUtils.isActive(getActiveFromDate(), getActiveToDate(), activeAsOfDate)
    }

    boolean isActive() {
        return InactivatableFromToUtils.isActive(getActiveFromDate(), getActiveToDate(), null)
    }

    DateTime getActiveFromDate() {
        return this.activeFromDateValue == null ? null : new DateTime(this.activeFromDateValue.getTime())
    }

    DateTime getActiveToDate() {
        return this.activeToDateValue == null ? null : new DateTime(this.activeToDateValue.getTime())
    }
}
