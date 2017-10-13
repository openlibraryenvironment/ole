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
package org.kuali.rice.kew.doctype.service;

import java.util.Collection;

import org.kuali.rice.core.framework.impex.xml.XmlLoader;
import org.kuali.rice.kew.doctype.bo.DocumentType;
import org.springframework.cache.annotation.Cacheable;

/**
 * A service for querying document type stuff for plugins without exposing the document type service.
 * 
 * @author Kuali Rice Team (rice.collab@kuali.org)
 */
public interface DocumentTypeQueryService extends XmlLoader {

    @Cacheable(value= org.kuali.rice.kew.api.doctype.DocumentType.Cache.NAME, key="'{BO}' + 'documentTypeId=' + #p0")
    public DocumentType findById(String documentTypeId);

    @Cacheable(value= org.kuali.rice.kew.api.doctype.DocumentType.Cache.NAME, key="'{BO}' + 'name=' + #p0")
    public DocumentType findByName(String name);

    @Cacheable(value= org.kuali.rice.kew.api.doctype.DocumentType.Cache.NAME,
            key="'{BO}' + 'documentTypeId=' + #p0.getId() + '|' + 'name=' + #p0.getName() + '|' + 'label=' + #p0.getLabel() + '|' +'docGroupName=' + #p1 + '|' + 'climbHierarchy=' + #p2")
    public Collection<DocumentType> find(DocumentType documentType, String docGroupName, boolean climbHierarchy);

    @Cacheable(value= org.kuali.rice.kew.api.doctype.DocumentType.Cache.NAME, key="'{BO}{root}' + 'documentTypeId=' + #p0.getId()")
    public DocumentType findRootDocumentType(DocumentType docType);
    
    /**
     * Returns the DocumentType of the Document with the given ID. 
     * 
     * @since 2.3
     */
    @Cacheable(value= org.kuali.rice.kew.api.doctype.DocumentType.Cache.NAME, key="'{BO}' + 'documentId=' + #p0")
    public DocumentType findByDocumentId(String documentId);
    
}
