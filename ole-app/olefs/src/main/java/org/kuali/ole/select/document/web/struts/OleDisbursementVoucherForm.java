/*
 * Copyright 2013 The Kuali Foundation.
 * 
 * Licensed under the Educational Community License, Version 1.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 * http://www.opensource.org/licenses/ecl1.php
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.kuali.ole.select.document.web.struts;

import org.kuali.ole.fp.document.service.DisbursementVoucherCoverSheetService;
import org.kuali.ole.fp.document.web.struts.DisbursementVoucherForm;
import org.kuali.ole.select.businessobject.OleDisbursementVoucherPayeeDetail;
import org.kuali.ole.select.document.OleDisbursementVoucherDocument;
import org.kuali.ole.sys.context.SpringContext;
import org.kuali.rice.kns.service.BusinessObjectDictionaryService;

import javax.servlet.http.HttpServletRequest;

public class OleDisbursementVoucherForm extends DisbursementVoucherForm {

    /**
     * Constructs a OleDisbursementVoucherForm.java.
     */
    public OleDisbursementVoucherForm() {
        super();
    }

    /**
     * @see org.kuali.rice.kns.web.struts.form.KualiTransactionalDocumentFormBase#populate(javax.servlet.http.HttpServletRequest)
     */
    @Override
    public void populate(HttpServletRequest request) {
        super.populate(request);
        OleDisbursementVoucherPayeeDetail payeeDetail = getDisbursementVoucherDocument().getDvPayeeDetail();
        SpringContext.getBean(BusinessObjectDictionaryService.class).performForceUppercase(payeeDetail);

    }

    /**
     * determines if the DV document is in a state that allows printing of the cover sheet
     *
     * @return true if the DV document is in a state that allows printing of the cover sheet; otherwise, return false
     */
    @Override
    public boolean getCanPrintCoverSheet() {
        OleDisbursementVoucherDocument oleDisbursementVoucherDocument = (OleDisbursementVoucherDocument) this.getDocument();
        return SpringContext.getBean(DisbursementVoucherCoverSheetService.class).isCoverSheetPrintable(oleDisbursementVoucherDocument);
    }

    /**
     * Returns the instance of OleDisbursementVoucherDocument
     */
    @Override
    public OleDisbursementVoucherDocument getDisbursementVoucherDocument() {
        return (OleDisbursementVoucherDocument) getDocument();

    }

    /**
     * determine whether the selected payee is an employee
     */
    @Override
    public boolean isEmployee() {
        OleDisbursementVoucherDocument oleDisbursementVoucherDocument = (OleDisbursementVoucherDocument) this.getDocument();
        return oleDisbursementVoucherDocument.getDvPayeeDetail().isEmployee();
    }

    /**
     * determine whether the selected payee is a vendor
     */
    @Override
    public boolean isVendor() {
        OleDisbursementVoucherDocument oleDisbursementVoucherDocument = (OleDisbursementVoucherDocument) this.getDocument();
        return oleDisbursementVoucherDocument.getDvPayeeDetail().isVendor();
    }
}
