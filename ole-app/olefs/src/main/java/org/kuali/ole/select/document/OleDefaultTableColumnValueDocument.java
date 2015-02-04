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
package org.kuali.ole.select.document;

import org.kuali.ole.select.OleSelectConstant;
import org.kuali.ole.select.businessobject.OleDefaultTableColumn;
import org.kuali.ole.select.businessobject.OleDefaultValue;
import org.kuali.ole.sys.context.SpringContext;
import org.kuali.rice.core.api.mo.common.active.Inactivatable;
import org.kuali.rice.kew.api.exception.WorkflowException;
import org.kuali.rice.kew.doctype.bo.DocumentType;
import org.kuali.rice.kns.document.MaintenanceDocument;
import org.kuali.rice.kns.service.MaintenanceDocumentDictionaryService;
import org.kuali.rice.krad.document.TransactionalDocumentBase;
import org.kuali.rice.krad.service.BusinessObjectService;
import org.kuali.rice.krad.service.DocumentService;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.Collection;
import java.util.Collections;
import java.util.LinkedHashMap;

public class OleDefaultTableColumnValueDocument extends TransactionalDocumentBase implements Inactivatable {
    private static final org.apache.log4j.Logger LOG = org.apache.log4j.Logger.getLogger(OleDefaultTableColumnValueDocument.class);

    protected String documentNumber;
    protected BigDecimal defaultTableColumnId;
    protected String documentTypeId;
    protected String documentColumn;
    protected DocumentType documentTypes;
    protected boolean active;
    protected Timestamp createdDate;
    protected Timestamp modifiedDate;
    protected String defaultValue;

    public String getDefaultValue() {
        return defaultValue;
    }

    public void setDefaultValue(String defaultValue) {
        this.defaultValue = defaultValue;
    }

    public String getDocumentNumber() {
        return documentNumber;
    }

    public void setDocumentNumber(String documentNumber) {
        this.documentNumber = documentNumber;
    }

    public Timestamp getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Timestamp createdDate) {
        this.createdDate = createdDate;
    }

    public Timestamp getModifiedDate() {
        return modifiedDate;
    }

    public void setModifiedDate(Timestamp modifiedDate) {
        this.modifiedDate = modifiedDate;
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

    /**
     * @see org.kuali.rice.krad.bo.BusinessObjectBase#toStringMapper()
     */
    @SuppressWarnings("rawtypes")

    protected LinkedHashMap toStringMapper_RICE20_REFACTORME() {
        LinkedHashMap m = new LinkedHashMap();
        return m;
    }

    public void valueSubmit() {
        try {
            boolean flag = true;
            boolean flag1 = true;
            if (!(this.getDocumentTypeId() == null) || !(this.getDocumentColumn() == null)) {
                if (LOG.isDebugEnabled())
                    LOG.debug(" this.getDocumentTypeId() --------------------------> " + this.getDocumentTypeId());
                Collection<OleDefaultTableColumn> defaultTableColumnList = SpringContext.getBean(BusinessObjectService.class).findMatching(OleDefaultTableColumn.class, Collections.singletonMap("documentTypeId", getDocumentTypeId()));
                for (OleDefaultTableColumn col : defaultTableColumnList) {
                    if (col.getDocumentColumn().equalsIgnoreCase(this.getDocumentColumn())) {
                        this.setDefaultTableColumnId(col.getDefaultTableColumnId());
                        flag = false;
                    }
                }
                if (flag) {
                    MaintenanceDocument document = (MaintenanceDocument) SpringContext.getBean(DocumentService.class).getNewDocument(SpringContext.getBean(MaintenanceDocumentDictionaryService.class).getDocumentTypeName(OleDefaultTableColumn.class));
                    OleDefaultTableColumn oleDefaultTableColumn = (OleDefaultTableColumn) document.getNewMaintainableObject().getBusinessObject();
                    oleDefaultTableColumn.setDocumentTypeId(this.getDocumentTypeId());
                    oleDefaultTableColumn.setDocumentColumn(this.getDocumentColumn());
                    oleDefaultTableColumn.setActive(this.isActive());
                    SpringContext.getBean(BusinessObjectService.class).save(oleDefaultTableColumn);
                    this.setDefaultTableColumnId(oleDefaultTableColumn.getDefaultTableColumnId());
                }
            }

            Collection<OleDefaultValue> defaultValueList = SpringContext.getBean(BusinessObjectService.class).findMatching(OleDefaultValue.class, Collections.singletonMap("defaultTableColumnId", this.getDefaultTableColumnId()));
            for (OleDefaultValue def : defaultValueList) {
                if (def.getDefaultValueFor().equalsIgnoreCase(OleSelectConstant.DEFAULT_VALUE_SYSTEM)) {
                    flag1 = false;
                }
            }
            if (flag1) {
                if (!(this.getDefaultValue() == null) && !(this.getDefaultTableColumnId() == null)) {
                    MaintenanceDocument document = (MaintenanceDocument) SpringContext.getBean(DocumentService.class).getNewDocument(SpringContext.getBean(MaintenanceDocumentDictionaryService.class).getDocumentTypeName(OleDefaultValue.class));
                    OleDefaultValue oleDefaultValue = (OleDefaultValue) document.getNewMaintainableObject().getBusinessObject();
                    oleDefaultValue.setDefaultValue(this.getDefaultValue());
                    oleDefaultValue.setDefaultTableColumnId(this.getDefaultTableColumnId());
                    oleDefaultValue.setActive(this.isActive());
                    oleDefaultValue.setDefaultValueFor("System");
                    SpringContext.getBean(BusinessObjectService.class).save(oleDefaultValue);

                }
            }
        } catch (WorkflowException e) {
            LOG.error("Received WorkflowException trying to get route header id from workflow document", e);
            throw new RuntimeException(e);
        }
    }

}
