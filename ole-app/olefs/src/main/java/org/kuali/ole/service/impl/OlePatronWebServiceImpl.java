package org.kuali.ole.service.impl;

import org.kuali.ole.ingest.OlePatronRecordHandler;
import org.kuali.ole.deliver.bo.OlePatronDocument;
import org.kuali.ole.deliver.bo.OlePatronDocuments;
import org.kuali.ole.service.OlePatronWebService;
import org.kuali.rice.krad.service.BusinessObjectService;
import org.kuali.rice.krad.service.KRADServiceLocator;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: ?
 * Date: 2/19/13
 * Time: 6:02 PM
 * To change this template use File | Settings | File Templates.
 */
public class OlePatronWebServiceImpl implements OlePatronWebService {

    private BusinessObjectService businessObjectService;
    private OlePatronRecordHandler olePatronRecordHandler;

    /**
     *   Returns the businessObjectService instance.
     *   If it is not null  returns existing businessObjectService else creates new
     * @return  businessObjectService
     */
    protected BusinessObjectService getBusinessObjectService() {
        if (businessObjectService == null) {
            businessObjectService = KRADServiceLocator.getBusinessObjectService();
        }
        return businessObjectService;
    }

    public OlePatronRecordHandler getOlePatronRecordHandler() {
        if (null == olePatronRecordHandler) {
            olePatronRecordHandler = new OlePatronRecordHandler();
        }
        return olePatronRecordHandler;
    }

    /**
     * Sets the olePatronRecordHandler which is of type OlePatronRecordHandler
     * @param olePatronRecordHandler(OlePatronRecordHandler)
     */
    public void setOlePatronRecordHandler(OlePatronRecordHandler olePatronRecordHandler) {
        this.olePatronRecordHandler = olePatronRecordHandler;
    }

    @Override
    public String getPatronRecords() {

        String patronXML = new String();
        StringBuffer patronRecords = new StringBuffer();
        OlePatronDocuments olePatronDocuments = new OlePatronDocuments();

        List<OlePatronDocument> olePatronDocumentList = new ArrayList<OlePatronDocument>();
        Collection<OlePatronDocument> olePatronDocumentRecords = getBusinessObjectService().findAll(OlePatronDocument.class);
        for (OlePatronDocument olePatronDocument : olePatronDocumentRecords) {
           olePatronDocument.setFirstName(olePatronDocument.getEntity().getNames().get(0).getFirstName());
           olePatronDocument.setLastName(olePatronDocument.getEntity().getNames().get(0).getLastName());
           olePatronDocument.setBorrowerType(olePatronDocument.getOleBorrowerType().getBorrowerTypeName());
           olePatronDocumentList.add(olePatronDocument);
           olePatronDocuments.setOlePatronDocuments(olePatronDocumentList);
        }
        patronXML = getOlePatronRecordHandler().generatePatronXML(olePatronDocuments);
        return patronXML;
    }

}
