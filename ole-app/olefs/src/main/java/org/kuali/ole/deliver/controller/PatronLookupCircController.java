package org.kuali.ole.deliver.controller;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.kuali.ole.deliver.bo.OlePatronDocument;
import org.kuali.ole.deliver.bo.OleProxyPatronDocument;
import org.kuali.ole.deliver.controller.checkout.CircUtilController;
import org.kuali.ole.deliver.drools.DroolsConstants;
import org.kuali.ole.deliver.form.CircForm;
import org.kuali.ole.deliver.form.OleLoanForm;
import org.kuali.ole.deliver.util.ErrorMessage;
import org.kuali.ole.deliver.util.OlePatronRecordUtil;
import org.kuali.ole.sys.context.SpringContext;
import org.kuali.ole.utility.OleStopWatch;
import org.kuali.rice.krad.web.form.UifFormBase;

import java.util.Iterator;
import java.util.List;

/**
 * Created by pvsubrah on 6/4/15.
 */

public class PatronLookupCircController extends CircUtilController {

    private static final Logger LOG = Logger.getLogger(PatronLookupCircController.class);
    private OlePatronRecordUtil olePatronRecordUtil;

    public ErrorMessage searchPatron(UifFormBase form) {
        ErrorMessage errorMessage = null;
        OleStopWatch oleStopWatch = new OleStopWatch();
        oleStopWatch.start();
        CircForm circForm = (CircForm) form;
        try {
            OlePatronDocument patronDocument = circForm.getPatronDocument();
            if (null != patronDocument && StringUtils.isBlank(patronDocument.getBarcode())) {
                patronDocument = getOlePatronRecordUtil().getPatronRecordByBarcode(circForm
                        .getPatronBarcode());
                circForm.setPatronDocument(patronDocument);
            }
            String[] expectedRules = {};
            errorMessage = processRules(circForm, patronDocument, expectedRules);
        } catch (Exception e) {
            errorMessage = new ErrorMessage();
            errorMessage.setErrorMessage("Invalid Patron Barcode. Please try again!");
            errorMessage.setErrorCode(DroolsConstants.GENERAL_MESSAGE_FLAG);
            LOG.error("Exception while search patron time", e);
            return errorMessage;
        }
        oleStopWatch.end();
        LOG.info("Time taken to look up a patron:" + oleStopWatch.getTotalTime() + " ms");
        return errorMessage;
    }

    public ErrorMessage processPatronSearchPostProxyHandling(UifFormBase form) {
        CircForm circForm = (CircForm) form;
        ErrorMessage errorMessage = null;
        circForm.setProxyCheckDone(true);

        OlePatronDocument patronDocument = circForm.getPatronDocument();
        Boolean selfCheckOut = patronDocument.isCheckoutForSelf();
        if (!selfCheckOut) {
            List<OleProxyPatronDocument> oleProxyPatronDocumentList = patronDocument.getOleProxyPatronDocumentList();
            if (!CollectionUtils.isEmpty(oleProxyPatronDocumentList)) {
                OlePatronDocument realOlePatronDocument = identifyOlePatronDocumentForCheckout(oleProxyPatronDocumentList);
                if (null != realOlePatronDocument) {
                    patronDocument.setSelectedProxyForPatron(realOlePatronDocument);
                    String[] expectedRules = {};
                    errorMessage = processRules(circForm,realOlePatronDocument, expectedRules);
                }
            }
        }

        return errorMessage;
    }

    /**
     *
     * @param circForm
     * @param olePatronDocument
     * @param expectedRules
     * @return
     *
     * OlePatronDocument is passed because it could be either the borrower whose barcode was scanned
     * or the Proxy Borrower; Hence we cannot retrieve it from the form.
     */
    public ErrorMessage processRules(CircForm circForm, OlePatronDocument olePatronDocument, String[] expectedRules) {
        ErrorMessage errorMessage = getOlePatronRecordUtil().fireRules(olePatronDocument, expectedRules);
        circForm.setErrorMessage(errorMessage);
        return errorMessage;

    }

    private OlePatronDocument identifyOlePatronDocumentForCheckout(List<OleProxyPatronDocument> oleProxyPatronDocuments) {
        for (Iterator<OleProxyPatronDocument> iterator = oleProxyPatronDocuments.iterator(); iterator.hasNext(); ) {
            OleProxyPatronDocument proxyPatronDocument = iterator.next();
            OlePatronDocument olePatronDocument = proxyPatronDocument.getOlePatronDocument();
            if (olePatronDocument.isCheckoutForSelf()) {
                return olePatronDocument;
            }
        }
        return null;
    }

    public boolean hasProxyPatrons(UifFormBase form) {
        CircForm circForm = (CircForm) form;
        return CollectionUtils.isNotEmpty(circForm.getPatronDocument().getOleProxyPatronDocumentList());
    }

    //TODO: Do we need to handle lost Patron?
    private void handleLostPatron(OleLoanForm oleLoanForm) {
        if (oleLoanForm.getOlePatronDocument().isLostPatron()) {
            oleLoanForm.setBlockUser(true);
        } else {
            oleLoanForm.setBlockUser(false);
        }
    }

    public OlePatronRecordUtil getOlePatronRecordUtil() {
        if (null == olePatronRecordUtil) {
            olePatronRecordUtil = (OlePatronRecordUtil) SpringContext.getBean("olePatronRecordUtil");
        }
        return olePatronRecordUtil;
    }


}
