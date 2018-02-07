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

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Map.Entry;

import org.kuali.rice.kew.useroptions.dao.UserOptionsDAO;
import org.kuali.rice.kew.api.KewApiConstants;
import org.springframework.transaction.annotation.Transactional;

@Transactional
public class UserOptionsServiceImpl implements UserOptionsService {

    private UserOptionsDAO userOptionsDAO;

    private static final Properties defaultProperties = new Properties();

    static {
        defaultProperties.setProperty(KewApiConstants.EMAIL_RMNDR_KEY, KewApiConstants.EMAIL_RMNDR_WEEK_VAL);
    }

    private Long getNewOptionIdForActionList() {
		return getUserOptionsDAO().getNewOptionIdForActionList();
	}

    public List<UserOptions> findByUserQualified(String principalId, String likeString) {
        if ((principalId == null)) {
            return new ArrayList<UserOptions>(0);
        }
        return this.getUserOptionsDAO().findByUserQualified(principalId, likeString);
    }

    public UserOptions findByOptionId(String optionId, String principalId) {
        if (optionId == null || "".equals(optionId) || principalId == null || "".equals(principalId)) {
            return null;
        }
        return this.getUserOptionsDAO().findByOptionId(optionId, principalId);
    }

    public Collection<UserOptions> findByOptionValue(String optionId, String optionValue){
        return this.getUserOptionsDAO().findByOptionValue(optionId, optionValue);
    }

    public Collection<UserOptions> findByWorkflowUser(String principalId) {
        return this.getUserOptionsDAO().findByWorkflowUser(principalId);
    }

    public void save(UserOptions userOptions) {
        this.getUserOptionsDAO().save(userOptions);
    }
    
    /**
     * This overridden method saves an option for each optionsMap entry, all for the given principalId
     * 
     * @see org.kuali.rice.kew.useroptions.UserOptionsService#save(java.lang.String, java.util.Map)
     */
    public void save(String principalId, Map<String,String> optionsMap) {
    	// create a collection of UserOptions and save it
    	if (optionsMap != null && optionsMap.size() > 0) {
    		List<UserOptions> toSave = new ArrayList<UserOptions>();
    		for (Entry<String, String> entry : optionsMap.entrySet()) {
    			UserOptions option = findByOptionId(entry.getKey(), principalId);
    			if (option == null) {
    				option = new UserOptions();
    				option.setWorkflowId(principalId);
    			}
    			option.setOptionId(entry.getKey());
    			option.setOptionVal(entry.getValue());
    			toSave.add(option);
    		}
			getUserOptionsDAO().save(toSave);
    	}
    }
    
    public void deleteUserOptions(UserOptions userOptions) {
        this.getUserOptionsDAO().deleteUserOptions(userOptions);
    }

    public void save(String principalId, String optionId, String optionValue) {
        //KULRICE-7796 Don't save where val is greater than field length
        if(optionValue.length() <= 2000)
        {
            UserOptions option = findByOptionId(optionId, principalId);
            if (option == null) {
                option = new UserOptions();
                option.setWorkflowId(principalId);
            }
            option.setOptionId(optionId);
            option.setOptionVal(optionValue);
            getUserOptionsDAO().save(option);
        }
    }

    public UserOptionsDAO getUserOptionsDAO() {
        return userOptionsDAO;
    }

    public void setUserOptionsDAO(UserOptionsDAO optionsDAO) {
        userOptionsDAO = optionsDAO;
    }
    
    @Override
    public List<UserOptions> retrieveEmailPreferenceUserOptions(String emailSetting) {
        return this.getUserOptionsDAO().findEmailUserOptionsByType(emailSetting);
    }
}
