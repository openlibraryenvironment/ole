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
package org.kuali.rice.kew.documentlink.dao;

import java.util.List;

import org.kuali.rice.kew.documentlink.DocumentLink;

/**
 * This is a description of what this class does - g1zhang don't forget to fill this in. 
 * 
 * @author Kuali Rice Team (kuali-rice@googlegroups.com)
 *
 */
public interface DocumentLinkDAO {
	
	//get all docs linked to orgn doc
    public List<DocumentLink> getLinkedDocumentsByDocId(String docId);
    
    public List<DocumentLink> getOutgoingLinkedDocumentsByDocId(String docId);
    
    //get a link between 2 docs
    public DocumentLink getLinkedDocument(DocumentLink link);
    
    //save a link for 2 docs
    public void saveDocumentLink(DocumentLink link);

    //delete a link between 2 docs
    public void deleteDocumentLink(DocumentLink link); 
    
    //delete all links to orgn doc
    public void deleteDocmentLinksByDocId(String docId);
    
    public DocumentLink getDocumentLink(Long documentLinkId);

}
