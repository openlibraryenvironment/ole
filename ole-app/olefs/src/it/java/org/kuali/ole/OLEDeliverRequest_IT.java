package org.kuali.ole;

import org.junit.Test;
import org.kuali.ole.deliver.bo.OlePatronDocument;
import org.kuali.ole.deliver.service.OleDeliverRequestDocumentHelperServiceImpl;
import org.kuali.ole.ncip.service.OLECirculationService;
import org.kuali.ole.ncip.service.impl.OLECirculationServiceImpl;
import org.kuali.rice.krad.service.KRADServiceLocator;

import java.sql.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by angelind on 4/17/15.
 */
public class OLEDeliverRequest_IT extends OLETestCaseBase {

    @Test
    public void convertObjectToXml(){
        Map<String, String> patronMap = new HashMap<String, String>();
        patronMap.put(OLEConstants.BARCODE, "OLE-ASR");
        OlePatronDocument olePatronDocument = KRADServiceLocator.getBusinessObjectService().findByPrimaryKey(OlePatronDocument.class, patronMap);
        if(olePatronDocument != null){
            try{
                String xmlContent = KRADServiceLocator.getXmlObjectSerializerService().toXml(olePatronDocument);
                System.out.println("XmlContent : " + xmlContent);
            }catch (Exception e){
                e.printStackTrace();
            }

        }

    }

    @Test
    public void testAcceptItem(){
        OLECirculationService oleCirculationService=new OLECirculationServiceImpl();
        String content = "";
        try{
            content = oleCirculationService.acceptItem("OLE-ASR","10002","2020","Call Number", "Title", "Author","bordirc",
                    "UC/JRL/BorDirc","","Hold/Hold Request","API");
            System.out.print("Content : " + content);
        }catch(Exception e){
            e.printStackTrace();
        }
    }

    @Test
    public void testPlaceRequest(){
        OleDeliverRequestDocumentHelperServiceImpl oleDeliverRequestDocumentHelperService = new OleDeliverRequestDocumentHelperServiceImpl();
        oleDeliverRequestDocumentHelperService.placeRequest("OLE-ASR","10002","5001","Copy Request","API","5001","API","stks","test","test","X",false,"5001","1",new Date(new java.util.Date().getTime()));
    }
}
