package org.kuali.ole.oleng.service.impl;

import org.kuali.ole.OLEConstants;
import org.kuali.ole.module.purap.document.service.OlePurapService;
import org.kuali.ole.oleng.service.InvoiceService;
import org.kuali.ole.pojo.OleInvoiceRecord;
import org.kuali.ole.select.document.OleInvoiceDocument;
import org.kuali.ole.sys.businessobject.Bank;
import org.kuali.ole.sys.context.SpringContext;
import org.kuali.ole.sys.service.BankService;
import org.kuali.ole.sys.service.UniversityDateService;
import org.kuali.rice.kew.api.exception.WorkflowException;
import org.kuali.rice.kim.api.identity.Person;
import org.kuali.rice.krad.UserSession;
import org.kuali.rice.krad.service.BusinessObjectService;
import org.kuali.rice.krad.service.DocumentService;
import org.kuali.rice.krad.util.GlobalVariables;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

/**
 * Created by SheikS on 12/17/2015.
 */
@Service("invoiceService")
public class InvoiceServiceImpl implements InvoiceService {

    private static final Logger LOG = LoggerFactory.getLogger(InvoiceServiceImpl.class);
    private BusinessObjectService businessObjectService;
    private OlePurapService olePurapService;

    @Override
    public OleInvoiceDocument createInvoiceDocument(OleInvoiceRecord oleInvoiceRecord) throws Exception {
        UserSession userSession = new UserSession("ole-quickstart");
        Person person = userSession.getPerson();
        GlobalVariables.setUserSession(userSession);

        OleInvoiceDocument oleInvoiceDocument = initiateInvoiceDocument(person);
        //todo : nextprocess.
        return oleInvoiceDocument;
    }

    private OleInvoiceDocument initiateInvoiceDocument(Person currentUser) throws Exception {
        OleInvoiceDocument invoiceDocument = null;
        try {
            invoiceDocument = (OleInvoiceDocument) SpringContext.getBean(DocumentService.class).getNewDocument("OLE_PRQS");
        } catch (WorkflowException e) {
            LOG.error(e.getMessage());
        }
        invoiceDocument.initiateDocument();

        UniversityDateService universityDateService = SpringContext.getBean(UniversityDateService.class);
        invoiceDocument.setPostingYear(universityDateService.getCurrentUniversityDate().getUniversityFiscalYear());
        Bank defaultBank = SpringContext.getBean(BankService.class).getDefaultBankByDocType(invoiceDocument.getClass());
        if (defaultBank != null) {
            invoiceDocument.setBankCode(defaultBank.getBankCode());
            invoiceDocument.setBank(defaultBank);

        }
        String description = getOlePurapService().getParameter(OLEConstants.INVOICE_IMPORT_INV_DESC);
        if (LOG.isDebugEnabled()){
            LOG.debug("Description for invoice import ingest is "+description);
        }
        description = getOlePurapService().setDocumentDescription(description,null);
        invoiceDocument.getDocumentHeader().setDocumentDescription(description);
        invoiceDocument.setAccountsPayableProcessorIdentifier(currentUser.getPrincipalId());
        invoiceDocument.setProcessingCampusCode(currentUser.getCampusCode());
        return invoiceDocument;
    }

    public OlePurapService getOlePurapService() {
        if (olePurapService == null) {
            olePurapService = SpringContext.getBean(OlePurapService.class);
        }
        return olePurapService;
    }

    public BusinessObjectService getBusinessObjectService() {
        if (businessObjectService == null) {
            businessObjectService = SpringContext.getBean(BusinessObjectService.class);
        }
        return businessObjectService;
    }
}
