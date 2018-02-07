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
package org.kuali.rice.kew.rule.web;

import java.util.ArrayList;
import java.util.List;

import org.kuali.rice.kew.rule.web.RuleQuickLinksAction.DocumentTypeQuickLinksStructure;
import org.kuali.rice.kns.web.struts.form.KualiForm;

/**
 * A Struts ActionForm for the {@link RuleQuickLinksAction}.
 * 
 * @see RuleQuickLinksAction
 *
 * @author Kuali Rice Team (rice.collab@kuali.org)
 */
public class RuleQuickLinksForm extends KualiForm {

    private static final long serialVersionUID = 3632283509506923869L;
    private String rootDocTypeName;
    protected boolean canInitiateDocumentTypeDocument = false;
    private List<DocumentTypeQuickLinksStructure> documentTypeQuickLinksStructures = new ArrayList<DocumentTypeQuickLinksStructure>();
    
    public String getRootDocTypeName() {
        return rootDocTypeName;
    }
    public void setRootDocTypeName(String rootDocTypeName) {
        this.rootDocTypeName = rootDocTypeName;
    }
    public List<DocumentTypeQuickLinksStructure> getDocumentTypeQuickLinksStructures() {
        return this.documentTypeQuickLinksStructures;
    }
	public void setDocumentTypeQuickLinksStructures(
			List<DocumentTypeQuickLinksStructure> documentTypeQuickLinksStructures) {
		this.documentTypeQuickLinksStructures = documentTypeQuickLinksStructures;
	}
	public boolean isCanInitiateDocumentTypeDocument() {
		return this.canInitiateDocumentTypeDocument;
	}
	public void setCanInitiateDocumentTypeDocument(
			boolean canInitiateDocumentTypeDocument) {
		this.canInitiateDocumentTypeDocument = canInitiateDocumentTypeDocument;
	}
    

}
