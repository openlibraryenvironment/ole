/*
 * Copyright 2011 The Kuali Foundation.
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
package org.kuali.ole.select.document.web;

import org.apache.struts.action.ActionMapping;
import org.kuali.ole.select.document.OleDefaultTableColumnValueDocument;
import org.kuali.rice.kew.doctype.bo.DocumentType;
import org.kuali.rice.kns.web.struts.form.KualiTransactionalDocumentFormBase;

import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;

public class OleDefaultTableColumnValueForm extends KualiTransactionalDocumentFormBase {

    private BigDecimal defaultTableColumnId;

    private String documentTypeId;

    private String documentColumn;

    private String defaultValue;

    private boolean active;

    private DocumentType documentTypes;

    /* //private String principalName;


     public String getPrincipalName() {
         return principalName;
     }

     public void setPrincipalName(String principalName) {
         this.principalName = principalName;
     }*/
    public String getDefaultValue() {
        return defaultValue;
    }

    public void setDefaultValue(String defaultValue) {
        this.defaultValue = defaultValue;
    }

    public BigDecimal getDefaultTableColumnId() {
        return defaultTableColumnId;
    }

    public void setDefaultTableColumnId(BigDecimal defaultTableColumnId) {
        this.defaultTableColumnId = defaultTableColumnId;
    }

    public String getDocumentTypeId() {
        return documentTypeId;
    }

    public void setDocumentTypeId(String documentTypeId) {
        this.documentTypeId = documentTypeId;
    }

    public String getDocumentColumn() {
        return documentColumn;
    }

    public void setDocumentColumn(String documentColumn) {
        this.documentColumn = documentColumn;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public DocumentType getDocumentTypes() {
        return documentTypes;
    }

    public void setDocumentTypes(DocumentType documentTypes) {
        this.documentTypes = documentTypes;
    }

    public OleDefaultTableColumnValueForm() {
        super();

        setDocument(new OleDefaultTableColumnValueDocument());
        setDocTypeName("OLE_DFTTABCL");
    }

    public void populate(HttpServletRequest req) {
        super.populate(req);
    }

    @Override
    public String getRefreshCaller() {
        return "refreshCaller";
    }

    @Override
    public void reset(ActionMapping mapping, HttpServletRequest request) {
        super.reset(mapping, request);
    }

}
