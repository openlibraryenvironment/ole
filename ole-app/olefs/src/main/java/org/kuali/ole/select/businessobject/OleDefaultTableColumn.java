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
package org.kuali.ole.select.businessobject;

import org.kuali.rice.core.api.mo.common.active.MutableInactivatable;
import org.kuali.rice.kew.doctype.bo.DocumentType;
import org.kuali.rice.krad.bo.PersistableBusinessObjectBase;

import java.math.BigDecimal;
import java.util.LinkedHashMap;

public class OleDefaultTableColumn extends PersistableBusinessObjectBase implements MutableInactivatable {

    private BigDecimal defaultTableColumnId;
    private String documentTypeId;
    private String documentColumn;
    //private String defaultValue; 
    private boolean active;
    private DocumentType documentTypes;

    public String getDocumentTypeId() {
        return documentTypeId;
    }

    public void setDocumentTypeId(String documentTypeId) {
        this.documentTypeId = documentTypeId;
    }

    public DocumentType getDocumentTypes() {
        return documentTypes;
    }

    public void setDocumentTypes(DocumentType documentTypes) {
        this.documentTypes = documentTypes;
    }


    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public BigDecimal getDefaultTableColumnId() {
        return defaultTableColumnId;
    }

    public void setDefaultTableColumnId(BigDecimal defaultTableColumnId) {
        this.defaultTableColumnId = defaultTableColumnId;
    }

    public String getDocumentColumn() {
        return documentColumn;
    }

    public void setDocumentColumn(String documentColumn) {
        this.documentColumn = documentColumn;
    }


    protected LinkedHashMap toStringMapper_RICE20_REFACTORME() {
        // TODO Auto-generated method stub
        LinkedHashMap m = new LinkedHashMap();
        m.put("defaultTableColumnId", defaultTableColumnId);
        m.put("documentTypeId", documentTypeId);
        m.put("documentColumn", documentColumn);
        m.put("active", active);
        m.put("documentTypes", documentTypes);
        return m;
    }


}
