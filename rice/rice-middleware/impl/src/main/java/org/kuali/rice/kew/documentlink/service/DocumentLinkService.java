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
package org.kuali.rice.kew.documentlink.service;

import java.util.List;

import org.kuali.rice.kew.documentlink.DocumentLink;

/**
 * This is a description of what this class does - g1zhang don't forget to fill this in. 
 * 
 * @author Kuali Rice Team (kuali-rice@googlegroups.com)
 *
 */
public interface DocumentLinkService {

    public List<DocumentLink> getLinkedDocumentsByDocId(String docId);
    
    public List<DocumentLink> getOutgoingLinkedDocumentsByDocId(String docId);
    
    public DocumentLink getLinkedDocument(DocumentLink link);
    
    public void saveDocumentLink(DocumentLink link);

    public void deleteDocumentLink(DocumentLink link); 
    
    public void deleteDocumentLinksByDocId(String docId); 
    
    public DocumentLink getDocumentLink(Long documentLinkId);

}
