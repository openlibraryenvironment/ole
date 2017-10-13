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

import org.joda.time.DateTime;
import org.kuali.rice.core.api.mo.common.active.InactivatableFromToUtils;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;
import javax.persistence.Transient;
import java.sql.Timestamp;

/**
 * @author Kuali Rice Team (rice.collab@kuali.org)
 */
@MappedSuperclass
public abstract class InactivatableFromToImpl extends PersistableBusinessObjectBase implements InactivatableFromTo {

	private static final long serialVersionUID = 1L;

	@Column(name = "ACTV_FRM_DT")
	protected Timestamp activeFromDate;
	@Column(name = "ACTV_TO_DT")
	protected Timestamp activeToDate;
	@Transient
	protected Timestamp activeAsOfDate;
	@Transient
	protected boolean current;

	/**
	 * Returns active if the {@link #getActiveAsOfDate()} (current time used if not set) is between
	 * the from and to dates. Null dates are considered to indicate an open range.
	 */
	public boolean isActive() {
        return InactivatableFromToUtils.isActive(new DateTime(activeFromDate), new DateTime(activeToDate), new DateTime(activeAsOfDate));
	}
	
	public void setActive(boolean active) {
		// do nothing
	}

	public void setActiveFromDate(Timestamp from) {
		this.activeFromDate = from;
	}

	public void setActiveToDate(Timestamp to) {
		this.activeToDate = to;
	}

	public Timestamp getActiveFromDate() {
		return this.activeFromDate;
	}

	public Timestamp getActiveToDate() {
		return this.activeToDate;
	}

	public Timestamp getActiveAsOfDate() {
		return this.activeAsOfDate;
	}

	public void setActiveAsOfDate(Timestamp activeAsOfDate) {
		this.activeAsOfDate = activeAsOfDate;
	}

	public boolean isCurrent() {
		return this.current;
	}

	public void setCurrent(boolean current) {
		this.current = current;
	}
}
