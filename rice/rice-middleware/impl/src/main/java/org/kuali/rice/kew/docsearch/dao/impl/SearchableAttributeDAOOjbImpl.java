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
package org.kuali.rice.kew.docsearch.dao.impl;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.apache.ojb.broker.query.Criteria;
import org.apache.ojb.broker.query.QueryByCriteria;
import org.kuali.rice.kew.docsearch.SearchableAttributeDateTimeValue;
import org.kuali.rice.kew.docsearch.SearchableAttributeFloatValue;
import org.kuali.rice.kew.docsearch.SearchableAttributeLongValue;
import org.kuali.rice.kew.docsearch.SearchableAttributeStringValue;
import org.kuali.rice.kew.docsearch.dao.SearchableAttributeDAO;
import org.springmodules.orm.ojb.support.PersistenceBrokerDaoSupport;

/**
 * OJB implementation of SearchableAttributeDAO
 *
 * @author Kuali Rice Team (rice.collab@kuali.org)
 *
 */
public class SearchableAttributeDAOOjbImpl extends PersistenceBrokerDaoSupport implements SearchableAttributeDAO {

	public static final org.apache.log4j.Logger LOG = org.apache.log4j.Logger.getLogger(SearchableAttributeDAOOjbImpl.class);

	/**
	 * This overridden method queries the SearchableAttributeDateTimeValue persistence class
	 *
	 * @see org.kuali.rice.kew.docsearch.dao.SearchableAttributeDAO#getSearchableAttributeDateTimeValuesByKey(java.lang.Long, java.lang.String)
	 */
	public List<Timestamp> getSearchableAttributeDateTimeValuesByKey(
			String documentId, String key) {

		List<Timestamp> lRet = null;

		Criteria crit = new Criteria();
		crit.addEqualTo("documentId", documentId);
		crit.addEqualTo("searchableAttributeKey", key);
		Collection<SearchableAttributeDateTimeValue> collection = getPersistenceBrokerTemplate().getCollectionByQuery(new QueryByCriteria(SearchableAttributeDateTimeValue.class, crit));

		if(collection != null && !collection.isEmpty()){
			lRet = new ArrayList<Timestamp>();
			for(SearchableAttributeDateTimeValue value:collection){
				lRet.add(value.getSearchableAttributeValue());
			}
		}

		return lRet;
	}

	/**
	 * This overridden method queries the SearchableAttributeFloatValue persistence class
	 *
	 * @see org.kuali.rice.kew.docsearch.dao.SearchableAttributeDAO#getSearchableAttributeFloatValuesByKey(java.lang.Long, java.lang.String)
	 */
	public List<BigDecimal> getSearchableAttributeFloatValuesByKey(
			String documentId, String key) {
		List<BigDecimal> lRet = null;

		Criteria crit = new Criteria();
		crit.addEqualTo("documentId", documentId);
		crit.addEqualTo("searchableAttributeKey", key);
		Collection<SearchableAttributeFloatValue> collection = getPersistenceBrokerTemplate().getCollectionByQuery(new QueryByCriteria(SearchableAttributeFloatValue.class, crit));

		if(collection != null && !collection.isEmpty()){
			lRet = new ArrayList<BigDecimal>();
			for(SearchableAttributeFloatValue value:collection){
				lRet.add(value.getSearchableAttributeValue());
			}
		}

		return lRet;
	}

	/**
	 * This overridden method queries the SearchableAttributeStringValue persistence class
	 *
	 * @see org.kuali.rice.kew.docsearch.dao.SearchableAttributeDAO#getSearchableAttributeStringValuesByKey(java.lang.Long, java.lang.String)
	 */
	public List<String> getSearchableAttributeStringValuesByKey(
			String documentId, String key) {

		List<String> lRet = null;

		Criteria crit = new Criteria();
		crit.addEqualTo("documentId", documentId);
		crit.addEqualTo("searchableAttributeKey", key);
		Collection<SearchableAttributeStringValue> collection = getPersistenceBrokerTemplate().getCollectionByQuery(new QueryByCriteria(SearchableAttributeStringValue.class, crit));

		if(collection != null && !collection.isEmpty()){
			lRet = new ArrayList<String>();
			for(SearchableAttributeStringValue value:collection){
				lRet.add(value.getSearchableAttributeValue());
			}
		}

		return lRet;
	}

	/**
	 * This overridden method queries the SearchableAttributeLongValue persistence class
	 *
	 * @see org.kuali.rice.kew.docsearch.dao.SearchableAttributeDAO#getSearchableAttributeValuesByKey(java.lang.Long, java.lang.String)
	 */
	public List<Long> getSearchableAttributeLongValuesByKey(String documentId,
			String key) {
		List<Long> lRet = null;

			Criteria crit = new Criteria();
			crit.addEqualTo("documentId", documentId);
			crit.addEqualTo("searchableAttributeKey", key);
			Collection<SearchableAttributeLongValue> collection = getPersistenceBrokerTemplate().getCollectionByQuery(new QueryByCriteria(SearchableAttributeLongValue.class, crit));

			if(collection != null && !collection.isEmpty()){
				lRet = new ArrayList<Long>();
				for(SearchableAttributeLongValue value:collection){
					lRet.add(value.getSearchableAttributeValue());
				}
			}

			return lRet;
	}

}
