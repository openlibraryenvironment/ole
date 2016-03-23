package org.kuali.ole.deliver.controller;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.kuali.ole.OLEConstants;
import org.kuali.ole.deliver.bo.OlePatronDocument;
import org.kuali.ole.deliver.bo.OleProxyPatronDocument;
import org.kuali.ole.deliver.controller.checkout.CircUtilController;
import org.kuali.ole.deliver.drools.DroolsConstants;
import org.kuali.ole.deliver.drools.DroolsExchange;
import org.kuali.ole.deliver.form.OleLoanForm;
import org.kuali.ole.deliver.util.DroolsResponse;
import org.kuali.ole.deliver.util.ErrorMessage;
import org.kuali.ole.deliver.util.OlePatronRecordUtil;
import org.kuali.ole.sys.context.SpringContext;
import org.kuali.ole.utility.OleStopWatch;
import org.kuali.rice.core.api.config.property.ConfigContext;

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

/**
 * Created by pvsubrah on 6/4/15.
 */

public abstract class PatronLookupCircBaseController extends CircUtilController {

    private static final Logger LOG = Logger.getLogger(PatronLookupCircBaseController.class);

    public abstract OlePatronDocument getPatronDocument(DroolsExchange droolsExchange);

    public abstract void setPatronDocument(DroolsExchange droolsExchange, OlePatronDocument patronDocument);

    public abstract String getPatronBarcode(DroolsExchange droolsExchange);

    public abstract void setErrorMessage(DroolsExchange droolsExchange, ErrorMessage errorMessage);

    public abstract void setProxyCheckDone(DroolsExchange droolsExchange, boolean proxyCheckDone);

    private OlePatronRecordUtil olePatronRecordUtil;

    public DroolsResponse searchPatron(DroolsExchange droolsExchange) {
        DroolsResponse droolsResponse = null;
        OleStopWatch oleStopWatch = new OleStopWatch();
        oleStopWatch.start();
        try {
            OlePatronDocument patronDocument = getPatronDocument(droolsExchange);
            if (null != patronDocument && StringUtils.isBlank(patronDocument.getBarcode())) {
                patronDocument = getOlePatronRecordUtil().getPatronRecordByBarcode(getPatronBarcode(droolsExchange));
                patronDocument.setRequestedItemRecordsCount(patronDocument.getOleDeliverRequestBos().size());
                patronDocument.setTempCirculationHistoryCount(patronDocument.getOleTemporaryCirculationHistoryRecords().size());
                if(StringUtils.isBlank(patronDocument.getPhoneNumber())){
                    patronDocument.setPhoneNumber(patronDocument.getOlePatronEntityViewBo() != null ? patronDocument.getOlePatronEntityViewBo().getPhoneNumber() : "");
                }
                setPatronDocument(droolsExchange, patronDocument);
            }
        } catch (Exception e) {
            droolsResponse = new DroolsResponse();
            if(e.getMessage().equalsIgnoreCase(OLEConstants.PTRN_BARCD_NOT_EXT)){
                String createNewPatronLink = "patronMaintenance?viewTypeName=MAINTENANCE&amp;returnLocation=" + ConfigContext.getCurrentContextConfig().getProperty("ole.fs.url.base") + "/portal.do&amp;methodToCall=start&amp;dataObjectClassName=org.kuali.ole.deliver.bo.OlePatronDocument";
                droolsExchange.addToContext("createNewPatronLink", createNewPatronLink);
                droolsResponse.addErrorMessage(OLEConstants.PTRN_BARCD_NOT_EXT);
                droolsResponse.addErrorMessageCode(DroolsConstants.GENERAL_MESSAGE_FLAG);
            } else {
                droolsResponse.addErrorMessage(e.getMessage());
                droolsResponse.addErrorMessageCode(DroolsConstants.GENERAL_MESSAGE_FLAG);
            }
            LOG.error("Exception while search patron time", e);
            return droolsResponse;
        }

        oleStopWatch.end();
        LOG.info("Time taken to look up a patron:" + oleStopWatch.getTotalTime() + " ms");
        return droolsResponse;
    }

    public DroolsResponse processPatronSearchPostProxyHandling(DroolsExchange droolsExchange) {
        DroolsResponse droolsResponse = null;
        setProxyCheckDone(droolsExchange, true);

        OlePatronDocument patronDocument = getPatronDocument(droolsExchange);
        Boolean selfCheckOut = patronDocument.isCheckoutForSelf();
        if (!selfCheckOut) {
            List<OleProxyPatronDocument> oleProxyPatronDocumentList = patronDocument.getOleProxyPatronDocumentList();
            if (!CollectionUtils.isEmpty(oleProxyPatronDocumentList)) {
                OlePatronDocument realOlePatronDocument = identifyOlePatronDocumentForCheckout(oleProxyPatronDocumentList);
                if (null != realOlePatronDocument) {
                    patronDocument.setSelectedProxyForPatron(realOlePatronDocument);
                    String[] expectedRules = {};
                    droolsResponse = processRules(droolsExchange,realOlePatronDocument, expectedRules);
                }
            }
        } else {
            String[] expectedRules = {};
            droolsResponse = processRules(droolsExchange,patronDocument, expectedRules);
        }

        return droolsResponse;
    }

    public DroolsResponse processPatronValidation(DroolsExchange droolsExchange) {
        DroolsResponse droolsResponse = null;
        OlePatronDocument patronDocument = getPatronDocument(droolsExchange);
        String[] expectedRules = {};
        droolsResponse = processRules(droolsExchange, patronDocument, expectedRules);
        return droolsResponse;
    }

    /**
     *
     *
     * @param droolsExchange
     * @param olePatronDocument
     * @param expectedRules
     * @return
     *
     * OlePatronDocument is passed because it could be either the borrower whose barcode was scanned
     * or the Proxy Borrower; Hence we cannot retrieve it from the form.
     */
    public DroolsResponse processRules(DroolsExchange droolsExchange, OlePatronDocument olePatronDocument, String[] expectedRules) {
        DroolsResponse droolsResponse = getOlePatronRecordUtil().fireRules(olePatronDocument, expectedRules);
        setErrorMessage(droolsExchange, droolsResponse.getErrorMessage());
        return droolsResponse;

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

    public boolean hasProxyPatrons(DroolsExchange droolsExchange) {
        List<OleProxyPatronDocument> expiredOleProxyPatronDocuments = new ArrayList<>();
        if(CollectionUtils.isNotEmpty(getPatronDocument(droolsExchange).getOleProxyPatronDocumentList())) {
            for(OleProxyPatronDocument oleProxyPatronDocument : getPatronDocument(droolsExchange).getOleProxyPatronDocumentList()) {
                if(oleProxyPatronDocument.getProxyPatronExpirationDate() != null && oleProxyPatronDocument.getProxyPatronExpirationDate().before(new Date())) {
                    expiredOleProxyPatronDocuments.add(oleProxyPatronDocument);
                }
            }
        }
        getPatronDocument(droolsExchange).getOleProxyPatronDocumentList().removeAll(expiredOleProxyPatronDocuments);
        return CollectionUtils.isNotEmpty(getPatronDocument(droolsExchange).getOleProxyPatronDocumentList());
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
