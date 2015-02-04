package org.kuali.ole.deliver.service;

import org.apache.log4j.Logger;
import org.kuali.ole.deliver.api.OleDeliverRequestDefinition;
import org.kuali.ole.deliver.api.OlePatronDefinition;
import org.kuali.ole.deliver.bo.OleDeliverRequestBo;
import org.kuali.ole.deliver.bo.OlePatronLoanDocument;
import org.kuali.ole.deliver.bo.OlePatronLoanDocuments;
import org.kuali.ole.deliver.bo.OleRenewalLoanDocument;
import org.kuali.ole.service.OlePatronService;
import org.kuali.ole.service.OlePatronServiceImpl;

import java.util.ArrayList;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: ?
 * Date: 10/26/12
 * Time: 7:47 PM
 * To change this template use File | Settings | File Templates.
 */
public class OleMyAccountProcess {

    private static final Logger LOG = Logger.getLogger(OleMyAccountProcess.class);


    public List<OleRenewalLoanDocument> oleRenewalLoanDocumentList;
    private OlePatronService olePatronService;
    OleDeliverRequestDocumentHelperServiceImpl service = new OleDeliverRequestDocumentHelperServiceImpl();

    public OlePatronService getOlePatronService() {

        if (olePatronService == null)
            olePatronService = new OlePatronServiceImpl();
        return olePatronService;
    }

    public OleMyAccountProcess() {
        oleRenewalLoanDocumentList = new ArrayList<OleRenewalLoanDocument>();
    }

    /**
     * this method performs to find the  patron information using by patron id
     *
     * @param patronId
     * @return
     */
    public OlePatronDefinition getPatronInfo(String patronId) {
        LOG.debug("Inside the getPatronInfo method");
        OlePatronDefinition olePatronDefinition = getOlePatronService().getPatron(patronId);
        return olePatronDefinition;
    }

    /**
     * this method performs   to find the patron checkout items by using patron barcode
     *
     * @param patronBarcode
     * @return
     */
    public List<OleRenewalLoanDocument> getPatronLoanedItems(String patronBarcode) {
        LOG.debug("Inside the getPatronLoanedItems method");
        OlePatronLoanDocuments olePatronLoanDocuments = getOlePatronService().getPatronLoanedItems(patronBarcode);
        List<OleRenewalLoanDocument> oleRenewalLoanDocumentList = convertRenewalLoanDocuments(olePatronLoanDocuments);
        return oleRenewalLoanDocumentList;

    }

    public List<OleDeliverRequestDefinition> getPatronRequestItems(String patronId) {
        LOG.debug("Inside the getPatronRequestItems method");
        List<OleDeliverRequestDefinition> oleDeliverRequestDefinition = getOlePatronService().getPatronRequestItems(patronId);
        return oleDeliverRequestDefinition;

    }

    public void cancelRequest(List<OleDeliverRequestDefinition> oleDeliverRequestDefinition) {
        LOG.debug("Inside the cancelRequest method");
        for (OleDeliverRequestDefinition oleDeliverRequestDefinitions : oleDeliverRequestDefinition) {
            OleDeliverRequestBo oleDeliverRequestBo = OleDeliverRequestBo.from(oleDeliverRequestDefinitions);
            service.cancelDocument(oleDeliverRequestBo);
        }
    }

    /**
     * this method performs to renewal the itmes
     *
     * @param oleRenewalLoanDocumentList
     * @return
     */
    public List<OleRenewalLoanDocument> performRenewalItem(List<OleRenewalLoanDocument> oleRenewalLoanDocumentList) {
        LOG.debug("Inside the performRenewalItem method");
        OlePatronLoanDocuments olePatronLoanDocuments = convertOlePatronLoanDocuments(oleRenewalLoanDocumentList);
        olePatronLoanDocuments = getOlePatronService().performRenewalItems(olePatronLoanDocuments);
        oleRenewalLoanDocumentList = null;
        oleRenewalLoanDocumentList = convertRenewalLoanDocuments(olePatronLoanDocuments);
        return oleRenewalLoanDocumentList;
    }

    /**
     * this method performs to convert list of OleRenewalLoanDocument to  OlePatronLoanDocuments
     *
     * @param oleRenewalLoanDocumentList
     * @return
     */
    private OlePatronLoanDocuments convertOlePatronLoanDocuments(List<OleRenewalLoanDocument> oleRenewalLoanDocumentList) {
        LOG.debug("Inside the convertOlePatronLoanDocuments method");
        List<OlePatronLoanDocument> olePatronLoanItemList = new ArrayList<OlePatronLoanDocument>();
        for (int i = 0; i < oleRenewalLoanDocumentList.size(); i++) {
            OleRenewalLoanDocument oleRenewalLoanDocument = oleRenewalLoanDocumentList.get(i);
            OlePatronLoanDocument olePatronLoanDocument = OlePatronLoanDocuments.to(oleRenewalLoanDocument);
            olePatronLoanItemList.add(olePatronLoanDocument);
        }
        OlePatronLoanDocuments olePatronLoanDocuments = getOlePatronLoanDocuments(olePatronLoanItemList);
        return olePatronLoanDocuments;
    }

    /**
     * this method performs to convert  OlePatronLoanDocuments  to  list of OleRenewalLoanDocument
     *
     * @param olePatronLoanDocuments
     * @return
     */
    private List<OleRenewalLoanDocument> convertRenewalLoanDocuments(OlePatronLoanDocuments olePatronLoanDocuments) {
        LOG.debug("Inside the convertRenewalLoanDocuments method");
        List<OleRenewalLoanDocument> oleRenewalLoanDocumentList = new ArrayList<OleRenewalLoanDocument>();
        if (olePatronLoanDocuments != null)
            for (int i = 0; i < olePatronLoanDocuments.getOlePatronLoanDocuments().size(); i++) {
                OlePatronLoanDocument olePatronLoanDocument = (OlePatronLoanDocument) olePatronLoanDocuments.getOlePatronLoanDocuments().get(i);
                OleRenewalLoanDocument oleRenewalLoanDocument = new OleRenewalLoanDocument();
                oleRenewalLoanDocument.setItemBarcode(olePatronLoanDocument.getItemBarcode());
                oleRenewalLoanDocument.setCallNumber(olePatronLoanDocument.getCallNumber());
                oleRenewalLoanDocument.setDueDate(olePatronLoanDocument.getDueDate());
                oleRenewalLoanDocument.setLocation(olePatronLoanDocument.getLocation());
                oleRenewalLoanDocument.setTitle(olePatronLoanDocument.getTitle());
                oleRenewalLoanDocument.setAuthor(olePatronLoanDocument.getAuthor());
                oleRenewalLoanDocument.setMessageInfo(olePatronLoanDocument.getMessageInfo());
                oleRenewalLoanDocumentList.add(oleRenewalLoanDocument);
            }
        return oleRenewalLoanDocumentList;
    }

    /**
     * this method performs to convert  OlePatronLoanDocument  to  list of OleRenewalLoanDocument
     *
     * @param olePatronLoanItemList
     * @return
     */
    private OlePatronLoanDocuments getOlePatronLoanDocuments(List<OlePatronLoanDocument> olePatronLoanItemList) {
        LOG.debug("Inside the getOlePatronLoanDocuments method");
        OleRenewalLoanDocument oleRenewalLoanDocument = new OleRenewalLoanDocument();
        if (olePatronLoanItemList.size() != 0) {
            oleRenewalLoanDocument.setOlePatronLoanDocuments(olePatronLoanItemList);
            OlePatronLoanDocuments olePatronLoanDocuments = OlePatronLoanDocuments.Builder.create(oleRenewalLoanDocument).build();
            return olePatronLoanDocuments;
        }

        return null;

    }

}
