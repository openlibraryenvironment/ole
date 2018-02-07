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
package org.kuali.rice.kew.doctype.dao;

import java.util.Collection;
import java.util.List;

import org.kuali.rice.kew.doctype.bo.DocumentType;
import org.kuali.rice.kew.rule.bo.RuleAttribute;


/**
 * @author Kuali Rice Team (rice.collab@kuali.org)
 */
public interface DocumentTypeDAO {

    public DocumentType findById(String docTypeId);

    public DocumentType findByName(String name); // docType is case sensitive by default

    public DocumentType findByName(String name, boolean caseSensitive);

    public void save(DocumentType documentType);

    public Collection<DocumentType> find(DocumentType documentType, DocumentType docTypeParentName, boolean climbHierarchy);

    public void delete(DocumentType documentType);

    public List findByDocumentId(String documentId);

    public Integer getMaxVersionNumber(String docTypeName);

    public List findAllCurrentRootDocuments();

    public List findAllCurrent();

    public List findAllCurrentByName(String name);

    public List<DocumentType> findPreviousInstances(String documentTypeName);

    public List<String> getChildDocumentTypeIds(String parentDocumentTypeId);

    public List findDocumentTypeAttributes(RuleAttribute ruleAttribute);

    public String findDocumentTypeIdByDocumentId(String documentId);
    
    public String findDocumentTypeIdByName(String documentTypeName);
    
    public String findDocumentTypeNameById(String documentTypeId);
}
