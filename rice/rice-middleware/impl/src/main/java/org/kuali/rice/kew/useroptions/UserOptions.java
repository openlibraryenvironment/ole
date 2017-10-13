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
package org.kuali.rice.kew.useroptions;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Version;

import org.kuali.rice.kew.api.preferences.Preferences;


/**
 * An option defined for a user.  These are used to store user {@link Preferences}.
 *
 * @author Kuali Rice Team (rice.collab@kuali.org)
 */
@IdClass(org.kuali.rice.kew.useroptions.UserOptionsId.class)
@Entity
@Table(name="KREW_USR_OPTN_T")
@NamedQueries({
  @NamedQuery(name="UserOptions.FindByUserQualified", query="select uo from UserOptions uo where uo.workflowId = :workflowId and uo.optionId like :optionId"), 
  @NamedQuery(name="UserOptions.FindByWorkflowId",  query="select uo from UserOptions uo where uo.workflowId = :workflowId"),
  @NamedQuery(name="UserOptions.FindByOptionValue", query="select uo from UserOptions uo where uo.optionId = :optionId and uo.optionVal = :optionValue"),
  @NamedQuery(name="UserOptions.FindByOptionId", query="select uo from UserOptions uo where uo.optionId = :optionId and uo.workflowId = :workflowId"),
  @NamedQuery(name="UserOptions.FindEmailUserOptionsByType", query="select uo from UserOptions uo where (uo.optionId = :optionId or uo.optionId like :optionIdLike) and uo.optionVal = :optionValue")
})
public class UserOptions implements Comparable {

	@Id
	@Column(name="PRNCPL_ID",insertable=false,updatable=false)
	private String workflowId;
	@Id
	@Column(name="PRSN_OPTN_ID",insertable=false,updatable=false)
	private String optionId;
	@Column(name="VAL")
	private String optionVal;
	@Version
	@Column(name="VER_NBR")
	private Integer lockVerNbr;

	/**
	 * @return
	 */
	public Integer getLockVerNbr() {
		return lockVerNbr;
	}

	/**
	 * @return
	 */
	public String getOptionId() {
		return optionId;
	}

	/**
	 * @return
	 */
	public String getOptionVal() {
		return optionVal;
	}

	/**
	 * @return
	 */
	public String getWorkflowId() {
		return workflowId;
	}

	/**
	 * @param integer
	 */
	public void setLockVerNbr(Integer integer) {
		lockVerNbr = integer;
	}

	/**
	 * @param string
	 */
	public void setOptionId(String string) {
		optionId = string;
	}

	/**
	 * @param string
	 */
	public void setOptionVal(String string) {
		optionVal = string;
	}

	/**
	 * @param string
	 */
	public void setWorkflowId(String string) {
	    workflowId = string;
	}

	
    public int compareTo(Object o) {
        if (o instanceof UserOptions) {
            return this.getOptionId().compareTo(((UserOptions)o).getOptionId());
        }
        return 0;
    }
}

