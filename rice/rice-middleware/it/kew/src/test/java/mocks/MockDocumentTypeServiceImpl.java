/**
 * Copyright 2005-2013 The Kuali Foundation
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
package mocks;

import java.io.InputStream;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jdom.Element;
import org.kuali.rice.core.api.impex.ExportDataSet;
import org.kuali.rice.kew.doctype.bo.DocumentType;
import org.kuali.rice.kew.doctype.service.DocumentTypeService;
import org.kuali.rice.kew.framework.postprocessor.PostProcessor;
import org.kuali.rice.kew.rule.bo.RuleAttribute;


public class MockDocumentTypeServiceImpl implements DocumentTypeService {

    public DocumentType setDocumentTypeVersion(DocumentType documentType, boolean currentInd) {
        return null;
    }
    public DocumentType getMostRecentDocType(String docTypeName) {
        return null;
    }
    public boolean isLockedForRouting(DocumentType documentType) {
        return false;
    }
    private Map<String, DocumentType> documentsById = new HashMap<String, DocumentType>();
    private Map<String, DocumentType> documentsByName = new HashMap<String, DocumentType>();
    private Map<String, PostProcessor> postProcessors = new HashMap<String, PostProcessor>();

    public void makeCurrent(List documentTypes) {
        throw new UnsupportedOperationException("not yet implmeneted");
    }

    public void addDocumentType(DocumentType documentType, PostProcessor postProcessor) {
        documentsById.put(documentType.getDocumentTypeId(), documentType);
        documentsByName.put(documentType.getName(), documentType);
        postProcessors.put(documentType.getDocumentTypeId(), postProcessor);
    }

    public DocumentType findByDocumentId(String documentId) {
		throw new UnsupportedOperationException("not yet implemented");
	}

    public Integer getMaxVersionNumber(String name){
        return new Integer(0);
    }
    public DocumentType findById(String documentTypeId) {
        return (DocumentType) documentsById.get(documentTypeId);
    }

    public DocumentType findByName(String name) {
        return (DocumentType) documentsByName.get(name);
    }

    public DocumentType findByNameCaseInsensitive(String name) {
        return (DocumentType) documentsByName.get(name);
    }

    public void versionAndSave(DocumentType documentType) {
        addDocumentType(documentType, new MockPostProcessor(true));
    }

    public PostProcessor getPostProcessor(String documentTypeId) {
        return (PostProcessor) postProcessors.get(documentTypeId);
    }

    public Collection findRouteLevels(String documentTypeId) {
        return (Collection) ((DocumentType)documentsById.get(documentTypeId)).getRouteLevels();
    }

    public Collection find(DocumentType documentType, String docGroupName, boolean climbHiearchy) {
        throw new UnsupportedOperationException("not implemented in MockDocumentTypeServiceImpl");
    }

    public void delete(DocumentType documentType) {
        documentsById.remove(documentType.getDocumentTypeId());
        documentsByName.remove(documentType.getName());
    }

    public List findByRouteHeaderId (String documentId) {
        throw new UnsupportedOperationException("not implemented in MockDocumentTypeServiceImpl");
    }
    public void makeCurrent(String documentId) {
        throw new UnsupportedOperationException("not implemented in MockDocumentTypeServiceImpl");
    }

    public List findAllCurrentRootDocuments() {
        return null;
    }

    /* (non-Javadoc)
     * @see org.kuali.rice.kew.doctype.DocumentTypeService#getRootDocumentType(org.kuali.rice.kew.doctype.DocumentType)
     */
    public DocumentType findRootDocumentType(DocumentType docType) {
        return null;
    }
    public void loadXml(InputStream inputStream, String principalId) {
        throw new UnsupportedOperationException("Mock document type service can't load xml");
    }
    public Element export(ExportDataSet dataSet) {
        return null;
    }
	@Override
	public boolean supportPrettyPrint() {
		return true;
	}
	public List findAllCurrent() {
        return null;
    }
	public List getChildDocumentTypes(String documentTypeId) {
		return null;
	}
	public DocumentType findByNameIgnoreCache(String documentTypeId) {
		return null;
	}
	public void save(DocumentType documentType) {

	}
    public void save(DocumentType documentType, boolean flushCache) {

    }
    public void flushCache() {

    }
	public void clearCacheForAttributeUpdate(RuleAttribute ruleAttribute) {

	}
	public Integer getDocumentTypeCount() {
		return null;
	}
    public List<DocumentType> findPreviousInstances(String documentTypeName) {
        return null;
    }

}
