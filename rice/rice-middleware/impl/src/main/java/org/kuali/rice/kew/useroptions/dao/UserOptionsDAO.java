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
package org.kuali.rice.kew.useroptions.dao;

import java.util.Collection;
import java.util.List;

import org.kuali.rice.kew.useroptions.UserOptions;


public interface UserOptionsDAO {

	public Collection<UserOptions> findByWorkflowUser(String principalId);
	public List<UserOptions> findByUserQualified(String principalId, String likeString);
    public void deleteByUserQualified(String principalId, String likeString);
	public void save(UserOptions userOptions);
	public void save(Collection<UserOptions> userOptions);
	public void deleteUserOptions(UserOptions userOptions);
	public UserOptions findByOptionId(String optionId, String principalId);
	public Collection<UserOptions> findByOptionValue(String optionId, String optionValue);
	public Long getNewOptionIdForActionList();
	
	/**
	 * 
	 * This method fetches all of the user options which are used for email preferences
	 * and are set to the given email setting.
	 * 
	 * @param emailSetting
	 * @return
	 */
	public List<UserOptions> findEmailUserOptionsByType(String emailSetting);
}
