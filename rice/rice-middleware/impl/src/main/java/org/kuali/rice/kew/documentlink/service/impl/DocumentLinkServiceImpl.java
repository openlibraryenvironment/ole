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
package org.kuali.rice.kew.documentlink.service.impl;

import java.util.List;

import org.kuali.rice.kew.documentlink.DocumentLink;
import org.kuali.rice.kew.documentlink.dao.DocumentLinkDAO;
import org.kuali.rice.kew.documentlink.service.DocumentLinkService;

/**
 * This is a description of what this class does - g1zhang don't forget to fill this in. 
 * 
 * @author Kuali Rice Team (kuali-rice@googlegroups.com)
 *
 */
public class DocumentLinkServiceImpl implements DocumentLinkService {

	private DocumentLinkDAO docLinkDao;
	/**
	 * @return the docLinkDao
	 */
	public DocumentLinkDAO getDocumentLinkDAO() {
		return this.docLinkDao;
	}

	/**
	 * @param docLinkDao the docLinkDao to set
	 */
	public void setDocumentLinkDAO(DocumentLinkDAO docLinkDao) {
		this.docLinkDao = docLinkDao;
	}

	/**
	 * This overridden method ...
	 * 
	 * @see org.kuali.rice.kew.documentlink.service.DocumentLinkService#deleteDocumentLink(org.kuali.rice.kew.documentlink.DocumentLink)
	 */
	public void deleteDocumentLink(DocumentLink link) {
		getDocumentLinkDAO().deleteDocumentLink(link);
	}

	
	/**
	 * This overridden method ...
	 * 
	 * @see org.kuali.rice.kew.documentlink.service.DocumentLinkService#saveDocumentLink(org.kuali.rice.kew.documentlink.DocumentLink)
	 */
	public void saveDocumentLink(DocumentLink link) {
		getDocumentLinkDAO().saveDocumentLink(link);

	}
	/**
	 * This overridden method ...
	 * 
	 * @see org.kuali.rice.kew.documentlink.service.DocumentLinkService#getLinkedDocumentByDocId(java.lang.Long)
	 */
	public List<DocumentLink> getLinkedDocumentsByDocId(String docId) {
		return getDocumentLinkDAO().getLinkedDocumentsByDocId(docId);
	}
	
	public List<DocumentLink> getOutgoingLinkedDocumentsByDocId(String docId) {
		return getDocumentLinkDAO().getOutgoingLinkedDocumentsByDocId(docId);
	}

	/**
	 * This overridden method ...
	 * 
	 * @see org.kuali.rice.kew.documentlink.service.DocumentLinkService#deleteDocumentLinksByDocId(java.lang.Long)
	 */
	public void deleteDocumentLinksByDocId(String docId) {
		getDocumentLinkDAO().deleteDocmentLinksByDocId(docId);
		
	}

	/**
	 * This overridden method ...
	 * 
	 * @see org.kuali.rice.kew.documentlink.service.DocumentLinkService#getLinkedDocument(org.kuali.rice.kew.documentlink.DocumentLink)
	 */
	public DocumentLink getLinkedDocument(DocumentLink link) {
		return getDocumentLinkDAO().getLinkedDocument(link);
	}
	
	public DocumentLink getDocumentLink(Long documentLinkId) {
		return getDocumentLinkDAO().getDocumentLink(documentLinkId);
	}


}
