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

import org.kuali.rice.core.framework.impex.xml.XmlExporter;
import org.kuali.rice.kew.api.rule.Rule;
import org.kuali.rice.kew.doctype.bo.DocumentType;
import org.kuali.rice.kim.api.permission.Permission;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;

import java.util.List;


/**
 * Service for data access of document types.
 *
 * @author Kuali Rice Team (rice.collab@kuali.org)
 */
public interface DocumentTypeService extends DocumentTypeQueryService, XmlExporter {
    @CacheEvict(value={Rule.Cache.NAME, org.kuali.rice.kew.api.doctype.DocumentType.Cache.NAME, Permission.Cache.NAME}, allEntries = true)
    void versionAndSave(DocumentType documentType);

    @CacheEvict(value={Rule.Cache.NAME, org.kuali.rice.kew.api.doctype.DocumentType.Cache.NAME, Permission.Cache.NAME}, allEntries = true)
    void save(DocumentType documentType);

    @Cacheable(value= org.kuali.rice.kew.api.doctype.DocumentType.Cache.NAME, key="'{BO}allCurrentRootDocuments'")
    List<DocumentType> findAllCurrentRootDocuments();

    @Cacheable(value= org.kuali.rice.kew.api.doctype.DocumentType.Cache.NAME, key="'{BO}allCurrent'")
    List<DocumentType> findAllCurrent();

    @Cacheable(value= org.kuali.rice.kew.api.doctype.DocumentType.Cache.NAME, key="'{BO}{previousInstances}' + 'documentTypeName=' + #p0")
    List<DocumentType> findPreviousInstances(String documentTypeName);

    @Cacheable(value= org.kuali.rice.kew.api.doctype.DocumentType.Cache.NAME, key="'{BO}{childDocumentTypes}' + 'documentTypeId=' + #p0")
    List<DocumentType> getChildDocumentTypes(String documentTypeId);

    /**
     *
     * This method is similar to the findByName method except it is case insensitive.
     *
     * @param name
     * @return
     */
    DocumentType findByNameCaseInsensitive(String name);
}
