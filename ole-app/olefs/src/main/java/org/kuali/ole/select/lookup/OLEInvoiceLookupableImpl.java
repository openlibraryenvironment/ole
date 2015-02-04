package org.kuali.ole.select.lookup;

import org.kuali.ole.select.bo.OLEInvoiceBo;
import org.kuali.ole.select.document.OleInvoiceDocument;
import org.kuali.ole.sys.context.SpringContext;
import org.kuali.rice.core.api.datetime.DateTimeService;
import org.kuali.rice.krad.lookup.LookupableImpl;
import org.kuali.rice.krad.service.KRADServiceLocator;
import org.kuali.rice.krad.web.form.LookupForm;

import java.sql.*;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.Date;

public class OLEInvoiceLookupableImpl extends LookupableImpl {
    /*@Override
    protected List<?> getSearchResults(LookupForm form, Map<String, String> searchCriteria, boolean unbounded) {
        List<OleInvoiceDocument> oleInvoiceDocuments=new ArrayList<OleInvoiceDocument>();
        if(searchCriteria.size()==0){
            oleInvoiceDocuments =(List<OleInvoiceDocument>) KRADServiceLocator.getBusinessObjectService().findAll(OleInvoiceDocument.class);
        } else{
            oleInvoiceDocuments=(List<OleInvoiceDocument>) KRADServiceLocator.getBusinessObjectService().findMatching(OleInvoiceDocument.class, searchCriteria);
        }
        List<OLEInvoiceBo> searchResults=new ArrayList<OLEInvoiceBo>();
        for(OleInvoiceDocument oleInvoiceDocument:oleInvoiceDocuments){
           OLEInvoiceBo oleInvoiceBo=new OLEInvoiceBo();
            oleInvoiceBo.setInvoiceDate(oleInvoiceDocument.getInvoiceDate());
            oleInvoiceBo.setInvoiceNumber(oleInvoiceDocument.getInvoiceNumber());
            searchResults.add(oleInvoiceBo);
        }
        sortSearchResults(form, searchResults);
        return searchResults;
    }*/
    @Override
    public Collection<?> performSearch(LookupForm form, Map<String, String> searchCriteria, boolean bounded) {
        Map<String, Object> map = new HashMap<String, Object>();

        for (Map.Entry<String, String> entry : searchCriteria.entrySet()) {
            if (!entry.getValue().equals("") && entry.getValue() != null) {

                if (entry.getKey().equals("invoiceDate")) {
                    DateFormat sourceFormat = new SimpleDateFormat("MM/dd/yyyy");
                    try {
                        java.util.Date date = sourceFormat.parse(entry.getValue().toString());
                        Timestamp timestamp = new Timestamp(date.getTime());
                        map.put(entry.getKey(), timestamp);
                    } catch (ParseException e) {
                        e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
                    }
                } else {
                    map.put(entry.getKey(), entry.getValue());
                }
            }

        }
        List<OleInvoiceDocument> oleInvoiceDocuments = new ArrayList<OleInvoiceDocument>();
        if (map.size() == 0) {
            oleInvoiceDocuments = (List<OleInvoiceDocument>) KRADServiceLocator.getBusinessObjectService().findAll(OleInvoiceDocument.class);
        } else {
            oleInvoiceDocuments = (List<OleInvoiceDocument>) KRADServiceLocator.getBusinessObjectService().findMatching(OleInvoiceDocument.class, map);
        }
        List<OLEInvoiceBo> searchResults = new ArrayList<OLEInvoiceBo>();
        for (OleInvoiceDocument oleInvoiceDocument : oleInvoiceDocuments) {
            if (oleInvoiceDocument.getIsSaved()) {
                OLEInvoiceBo oleInvoiceBo = new OLEInvoiceBo();
                oleInvoiceBo.setInvoiceDate(oleInvoiceDocument.getInvoiceDate());
                oleInvoiceBo.setInvoiceNbr(oleInvoiceDocument.getPurapDocumentIdentifier().toString());
                oleInvoiceBo.setInvoiceNumber(oleInvoiceDocument.getInvoiceNumber());
                oleInvoiceBo.setDocumentNumber(oleInvoiceDocument.getDocumentNumber());
                searchResults.add(oleInvoiceBo);
            }
        }
        sortSearchResults(form, searchResults);
        return searchResults;
    }
}
