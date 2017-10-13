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
package org.kuali.rice.kew.impl.document.security;

import org.apache.commons.lang.StringUtils;
import org.kuali.rice.core.api.exception.RiceIllegalArgumentException;
import org.kuali.rice.kew.api.document.Document;
import org.kuali.rice.kew.api.extension.ExtensionDefinition;
import org.kuali.rice.kew.api.extension.ExtensionRepositoryService;
import org.kuali.rice.kew.api.extension.ExtensionUtils;
import org.kuali.rice.kew.framework.document.security.DocumentSecurityDirective;
import org.kuali.rice.kew.framework.document.security.DocumentSecurityHandlerService;
import org.kuali.rice.kew.framework.document.security.DocumentSecurityAttribute;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Reference implementation of the DocumentSecurityHandlerService.
 *
 * @author Kuali Rice Team (rice.collab@kuali.org)
 */
public class DocumentSecurityHandlerServiceImpl implements DocumentSecurityHandlerService {

    private ExtensionRepositoryService extensionRepositoryService;

    @Override
    public List<String> getAuthorizedDocumentIds(String principalId, List<DocumentSecurityDirective> documentSecurityDirectives ) {
        if (StringUtils.isBlank(principalId)) {
            throw new RiceIllegalArgumentException("principalId was null or blank");
        }
        if (documentSecurityDirectives == null) {
            documentSecurityDirectives = Collections.emptyList();
        }
        List<String> authorizedDocumentIds = new ArrayList<String>();
        Map<String, DocumentSecurityAttribute> securityAttributeCache = new HashMap<String, DocumentSecurityAttribute>();
        for (DocumentSecurityDirective documentSecurityDirective : documentSecurityDirectives) {
            List<DocumentSecurityAttribute> attributesToApply = loadSecurityAttributes(documentSecurityDirective, securityAttributeCache);
            for (Document document : documentSecurityDirective.getDocuments()) {
                // if it's already authorized, we don't need to do anything
                if (!authorizedDocumentIds.contains(document.getDocumentId())) {
                    for (DocumentSecurityAttribute securityAttribute : attributesToApply) {
                        if (securityAttribute.isAuthorizedForDocument(principalId, document)) {
                            authorizedDocumentIds.add(document.getDocumentId());
                            break;
                        }
                    }
                }
            }
        }
        return authorizedDocumentIds;
    }

    protected List<DocumentSecurityAttribute> loadSecurityAttributes(DocumentSecurityDirective documentSecurityDirective,
            Map<String, DocumentSecurityAttribute> securityAttributeCache) {
        List<DocumentSecurityAttribute> securityAttributes = new ArrayList<DocumentSecurityAttribute>();
        for (String documentSecurityAttributeName : documentSecurityDirective.getDocumentSecurityAttributeNames()) {
            securityAttributes.add(loadAndCacheSecurityAttribute(documentSecurityAttributeName, securityAttributeCache));
        }
        return securityAttributes;
    }

    protected DocumentSecurityAttribute loadAndCacheSecurityAttribute(String securityAttributeName, Map<String, DocumentSecurityAttribute> securityAttributeCache) {
        if (securityAttributeCache.containsKey(securityAttributeName)) {
            return securityAttributeCache.get(securityAttributeName);
        }
        ExtensionDefinition extensionDefinition = extensionRepositoryService.getExtensionByName(securityAttributeName);
        if (extensionDefinition == null) {
            throw new RiceIllegalArgumentException("Failed to locate a SecurityAttribute with the given name: " + securityAttributeName);
        }
        DocumentSecurityAttribute securityAttribute = loadSecurityAttribute(extensionDefinition);
        securityAttributeCache.put(securityAttributeName, securityAttribute);
        return securityAttribute;
    }

    protected DocumentSecurityAttribute loadSecurityAttribute(ExtensionDefinition extensionDefinition) {
        Object securityAttribute = ExtensionUtils.loadExtension(extensionDefinition);
        if (securityAttribute == null) {
            throw new RiceIllegalArgumentException("Failed to load SecurityAttribute for: " + extensionDefinition);
        }
        return (DocumentSecurityAttribute)securityAttribute;
    }

    public ExtensionRepositoryService getExtensionRepositoryService() {
        return extensionRepositoryService;
    }

    public void setExtensionRepositoryService(ExtensionRepositoryService extensionRepositoryService) {
        this.extensionRepositoryService = extensionRepositoryService;
    }

}
