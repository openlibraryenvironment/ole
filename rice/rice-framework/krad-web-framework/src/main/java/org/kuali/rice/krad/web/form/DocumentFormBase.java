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
package org.kuali.rice.krad.web.form;

import org.apache.commons.lang.StringUtils;
import org.kuali.rice.core.api.CoreApiServiceLocator;
import org.kuali.rice.kew.api.WorkflowDocument;
import org.kuali.rice.kim.api.identity.Person;
import org.kuali.rice.kim.api.services.KimApiServiceLocator;
import org.kuali.rice.krad.document.Document;
import org.kuali.rice.krad.service.KRADServiceLocatorWeb;

/**
 * Base form for all <code>DocumentView</code> screens
 *
 * @author Kuali Rice Team (rice.collab@kuali.org)
 */
public class DocumentFormBase extends UifFormBase {
	private static final long serialVersionUID = 2190268505427404480L;

	private static final org.apache.log4j.Logger LOG = org.apache.log4j.Logger.getLogger(DocumentFormBase.class);

	private String annotation = "";
	private String command;

	private String docId;
	private String docTypeName;

	protected Document document;

	public DocumentFormBase() {
	    super();

	    instantiateDocument();
	}

	public String getAnnotation() {
		return this.annotation;
	}

	public void setAnnotation(String annotation) {
		this.annotation = annotation;
	}

	public Document getDocument() {
		return this.document;
	}

	public void setDocument(Document document) {
		this.document = document;
	}

	public String getDocTypeName() {
        if(this.docTypeName == null && !this.getDefaultDocumentTypeName().isEmpty())
        {
            return this.getDefaultDocumentTypeName();
        }
		return this.docTypeName;
	}

	public void setDocTypeName(String docTypeName) {
		this.docTypeName = docTypeName;
	}

	public String getCommand() {
		return this.command;
	}

	public void setCommand(String command) {
		this.command = command;
	}

	public String getDocId() {
		return this.docId;
	}

	public void setDocId(String docId) {
		this.docId = docId;
	}

    protected String getDefaultDocumentTypeName() {
        return "";
    }

    protected void instantiateDocument() {
        if (document == null && StringUtils.isNotBlank(getDefaultDocumentTypeName())) {
            Class<? extends Document> documentClass = KRADServiceLocatorWeb.getDataDictionaryService()
                    .getDocumentClassByTypeName(getDefaultDocumentTypeName());
            try {
                Document newDocument = documentClass.newInstance();
                setDocument(newDocument);
            } catch (Exception e) {
                LOG.error("Unable to instantiate document class " + documentClass.getName() + " document type "
                        + getDefaultDocumentTypeName());
                throw new RuntimeException(e);
            }
        }
    }

	/**
	 * Retrieves the principal name (network id) for the document's initiator
	 *
	 * @return String initiator name
	 */
	public String getDocumentInitiatorNetworkId() {
		String initiatorNetworkId = "";
		if (getWorkflowDocument() != null) {
			String initiatorPrincipalId = getWorkflowDocument().getInitiatorPrincipalId();
			Person initiator = KimApiServiceLocator.getPersonService().getPerson(initiatorPrincipalId);
			if (initiator != null) {
				initiatorNetworkId = initiator.getPrincipalName();
			}
		}

		return initiatorNetworkId;
	}

	/**
	 * Retrieves the create date for the forms document and formats for
	 * presentation
	 *
	 * @return String formatted document create date
	 */
    public String getDocumentCreateDate() {
        String createDateStr = "";
        if (getWorkflowDocument() != null && getWorkflowDocument().getDateCreated() != null) {
            createDateStr = CoreApiServiceLocator.getDateTimeService().toString(
                    getWorkflowDocument().getDateCreated().toDate(), "hh:mm a MM/dd/yyyy");
        }

        return createDateStr;
    }

	/**
	 * Retrieves the <code>WorkflowDocument</code> instance from the forms
	 * document instance
	 *
	 * @return WorkflowDocument for the forms document
	 */
	public WorkflowDocument getWorkflowDocument() {
		return getDocument().getDocumentHeader().getWorkflowDocument();
	}

}
