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
package org.kuali.rice.krad.dao.impl;

import org.apache.ojb.broker.query.Criteria;
import org.apache.ojb.broker.query.QueryFactory;
import org.kuali.rice.core.framework.persistence.ojb.dao.PlatformAwareDaoBaseOjb;
import org.kuali.rice.krad.bo.DocumentHeader;
import org.kuali.rice.krad.dao.DocumentHeaderDao;
import org.kuali.rice.krad.util.KRADPropertyConstants;

/**
 * This class is the OJB implementation of the DocumentHeaderDao interface.
 * 
 */
public class DocumentHeaderDaoOjb extends PlatformAwareDaoBaseOjb implements DocumentHeaderDao {
	private static org.apache.log4j.Logger LOG = org.apache.log4j.Logger.getLogger(DocumentHeaderDaoOjb.class);

	private Class documentHeaderBaseClass = DocumentHeader.class;

	/**
	 * Default constructor
	 */
	public DocumentHeaderDaoOjb() {
		super();
	}

	/**
	 * @see org.kuali.rice.krad.dao.DocumentHeaderDao#getByDocumentHeaderId(java.lang.String)
	 */
	public DocumentHeader getByDocumentHeaderId(String id) {
		Criteria criteria = new Criteria();
		criteria.addEqualTo(KRADPropertyConstants.DOCUMENT_NUMBER, id);

		return (DocumentHeader) getPersistenceBrokerTemplate().getObjectByQuery(QueryFactory.newQuery(getDocumentHeaderBaseClass(), criteria));
	}

	/**
	 * Method used to define the {@link DocumentHeader} object to use in case clients need to override the class.  Default value is {@link DocumentHeader}.
	 * 
	 * @see org.kuali.rice.krad.dao.DocumentHeaderDao#getDocumentHeaderBaseClass()
	 */
	public Class getDocumentHeaderBaseClass() {
		return this.documentHeaderBaseClass;
	}

	/**
	 * @param documentHeaderBaseClass the documentHeaderBaseClass to set
	 */
	public void setDocumentHeaderBaseClass(Class documentHeaderBaseClass) {
		this.documentHeaderBaseClass = documentHeaderBaseClass;
	}

}
