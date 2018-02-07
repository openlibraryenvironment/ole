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
package org.kuali.rice.kew.quicklinks;

/**
 * Stores statistics information about an initiated DocumentType
 *
 * @author Kuali Rice Team (rice.collab@kuali.org)
 */
public class InitiatedDocumentType {

    private String documentTypeName;
    private String documentTypeLabelText;
    public InitiatedDocumentType(String documentTypeName, String documentTypeLabelText) {
        this.documentTypeName = documentTypeName;
        this.documentTypeLabelText = documentTypeLabelText;
    }
    public String getDocumentTypeLabelText() {
        return documentTypeLabelText;
    }
    public void setDocumentTypeLabelText(String documentTypeLabelText) {
        this.documentTypeLabelText = documentTypeLabelText;
    }
    public String getDocumentTypeName() {
        return documentTypeName;
    }
    public void setDocumentTypeName(String documentTypeName) {
        this.documentTypeName = documentTypeName;
    }
}
