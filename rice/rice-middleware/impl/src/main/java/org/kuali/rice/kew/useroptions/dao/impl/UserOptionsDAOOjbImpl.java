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
package org.kuali.rice.kew.useroptions.dao.impl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.apache.ojb.broker.PersistenceBroker;
import org.apache.ojb.broker.query.Criteria;
import org.apache.ojb.broker.query.QueryByCriteria;
import org.kuali.rice.core.api.resourceloader.GlobalResourceLoader;
import org.kuali.rice.core.api.util.RiceConstants;
import org.kuali.rice.core.framework.persistence.platform.DatabasePlatform;
import org.kuali.rice.kew.api.KewApiConstants;
import org.kuali.rice.kew.useroptions.UserOptions;
import org.kuali.rice.kew.useroptions.dao.UserOptionsDAO;
import org.springmodules.orm.ojb.PersistenceBrokerCallback;
import org.springmodules.orm.ojb.support.PersistenceBrokerDaoSupport;

import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;


public class UserOptionsDAOOjbImpl extends PersistenceBrokerDaoSupport implements UserOptionsDAO {

	public Long getNewOptionIdForActionList() {
        return (Long)this.getPersistenceBrokerTemplate().execute(new PersistenceBrokerCallback() {
            public Object doInPersistenceBroker(PersistenceBroker broker) {
            	return getPlatform().getNextValSQL("KREW_ACTN_LIST_OPTN_S", broker);
            }
        });
    }

	protected DatabasePlatform getPlatform() {
    	return (DatabasePlatform)GlobalResourceLoader.getService(RiceConstants.DB_PLATFORM);
    }

    public List<UserOptions> findByUserQualified(String principalId, String likeString) {
        Criteria criteria = new Criteria();
        criteria.addEqualTo("workflowId", principalId);
        criteria.addLike("optionId", likeString);
        return new ArrayList<UserOptions>(this.getPersistenceBrokerTemplate().getCollectionByQuery(new QueryByCriteria(UserOptions.class, criteria)));
    }

    public void deleteByUserQualified(String principalId, String likeString) {
        Criteria criteria = new Criteria();
        criteria.addEqualTo("workflowId", principalId);
        criteria.addLike("optionId", likeString);
        this.getPersistenceBrokerTemplate().deleteByQuery(new QueryByCriteria(UserOptions.class, criteria));
    }

    public Collection<UserOptions> findByWorkflowUser(String principalId) {
        UserOptions userOptions = new UserOptions();
        userOptions.setWorkflowId(principalId);
        return this.getPersistenceBrokerTemplate().getCollectionByQuery(new QueryByCriteria(userOptions));
    }

    public void save(UserOptions userOptions) {
    	this.getPersistenceBrokerTemplate().store(userOptions);
    }
    
    public void save(Collection<UserOptions> userOptions) {
    	if (userOptions != null) for (UserOptions option : userOptions) {
    		this.getPersistenceBrokerTemplate().store(option);
    	}
    }
    
    public void deleteUserOptions(UserOptions userOptions) {
    	this.getPersistenceBrokerTemplate().delete(userOptions);
    }

    public UserOptions findByOptionId(String optionId, String principalId) {
        UserOptions userOptions = new UserOptions();
        userOptions.setOptionId(optionId);
        userOptions.setWorkflowId(principalId);
        return (UserOptions) this.getPersistenceBrokerTemplate().getObjectByQuery(new QueryByCriteria(userOptions));
    }

    public Collection<UserOptions> findByOptionValue(String optionId, String optionValue) {
        UserOptions userOptions = new UserOptions();
        userOptions.setOptionId(optionId);
        userOptions.setOptionVal(optionValue);
        return this.getPersistenceBrokerTemplate().getCollectionByQuery(new QueryByCriteria(userOptions));
    }

    @Override
    public List<UserOptions> findEmailUserOptionsByType(String emailSetting) {
        Criteria optionIDCriteria = new Criteria();
        optionIDCriteria.addEqualTo("optionId", KewApiConstants.EMAIL_RMNDR_KEY);

        Criteria documentTypeNotificationCriteria = new Criteria();
        documentTypeNotificationCriteria.addLike("optionId", "%" + KewApiConstants.DOCUMENT_TYPE_NOTIFICATION_PREFERENCE_SUFFIX);
        optionIDCriteria.addOrCriteria(documentTypeNotificationCriteria);

        Criteria criteria = new Criteria();
        criteria.addEqualTo("optionVal", emailSetting);
        criteria.addAndCriteria(optionIDCriteria);
        return Lists.newArrayList(Iterables.filter(this.getPersistenceBrokerTemplate().getCollectionByQuery(new QueryByCriteria(UserOptions.class, criteria)), UserOptions.class));
    }
}
