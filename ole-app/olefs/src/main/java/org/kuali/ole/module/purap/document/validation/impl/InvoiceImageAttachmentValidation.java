/*
 * Copyright 2008-2009 The Kuali Foundation
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
package org.kuali.ole.module.purap.document.validation.impl;

import org.kuali.ole.module.purap.PurapConstants;
import org.kuali.ole.module.purap.PurapKeyConstants;
import org.kuali.ole.module.purap.document.InvoiceDocument;
import org.kuali.ole.sys.document.validation.GenericValidation;
import org.kuali.ole.sys.document.validation.event.AttributedDocumentEvent;
import org.kuali.rice.kew.api.WorkflowDocument;
import org.kuali.rice.krad.bo.Note;
import org.kuali.rice.krad.util.GlobalVariables;
import org.kuali.rice.krad.util.KRADConstants;
import org.kuali.rice.krad.util.ObjectUtils;

import java.util.List;
import java.util.Set;

public class InvoiceImageAttachmentValidation extends GenericValidation {

    public boolean validate(AttributedDocumentEvent event) {
        boolean valid = true;
        InvoiceDocument document = (InvoiceDocument) event.getDocument();
        GlobalVariables.getMessageMap().clearErrorPath();

        if (isDocumentStoppedInRouteNode(document, "ImageAttachment")) {
            //assume false if we're in the correct node
            valid = false;

            //loop through notes looking for a invoice image
            List boNotes = document.getNotes();
            if (ObjectUtils.isNotNull(boNotes)) {
                for (Object obj : boNotes) {
                    Note note = (Note) obj;

                    note.refreshReferenceObject("attachment");
                    if (ObjectUtils.isNotNull(note.getAttachment()) && PurapConstants.AttachmentTypeCodes.ATTACHMENT_TYPE_INVOICE_IMAGE.equals(note.getAttachment().getAttachmentTypeCode())) {
                        valid = true;
                        break;
                    }
                }
            }

            if (valid == false) {
                GlobalVariables.getMessageMap().putError(KRADConstants.NEW_DOCUMENT_NOTE_PROPERTY_NAME, PurapKeyConstants.ERROR_PAYMENT_REQUEST_INVOICE_REQUIRED);
            }
        }

        GlobalVariables.getMessageMap().clearErrorPath();

        return valid;
    }

    /**
     * @param document
     * @param nodeName
     * @return
     */
    protected boolean isDocumentStoppedInRouteNode(InvoiceDocument document, String nodeName) {
        WorkflowDocument workflowDoc = document.getDocumentHeader().getWorkflowDocument();
        Set<String> currentRouteLevels = workflowDoc.getCurrentNodeNames();
        if (currentRouteLevels.contains(nodeName) && workflowDoc.isApprovalRequested()) {
            return true;
        }
        return false;
    }
}
