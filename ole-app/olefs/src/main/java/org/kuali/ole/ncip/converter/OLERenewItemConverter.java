package org.kuali.ole.ncip.converter;

import com.thoughtworks.xstream.XStream;
import org.apache.log4j.Logger;
import org.kuali.ole.ncip.bo.OLERenewItem;

/**
 * Created with IntelliJ IDEA.
 * User: sheiksalahudeenm
 * Date: 9/3/13
 * Time: 8:52 PM
 * To change this template use File | Settings | File Templates.
 */
public class OLERenewItemConverter {
    final Logger LOG = Logger.getLogger(OLEItemFineConverter.class);
    public String generateRenewItemXml(OLERenewItem olePlaceRequest){
        XStream xStream = new XStream();
        xStream.alias("renewItem",OLERenewItem.class);
        return xStream.toXML(olePlaceRequest);
    }
    public Object generateRenewItemObject(String xml){
        XStream xStream = new XStream();
        xStream.alias("renewItem",OLERenewItem.class);
        return xStream.fromXML(xml);
    }
    public String generateRenewItemJson(String xml) {
        OLERenewItem oleRenewItem = (OLERenewItem)generateRenewItemObject(xml);
        OleCirculationHandler xmlContentHandler = new OleCirculationHandler();
        if(oleRenewItem==null){
            oleRenewItem=new OLERenewItem();
        }
        try{
            return xmlContentHandler.marshalToJSON(oleRenewItem);
        }catch(Exception e){
            LOG.error(e,e);
        }
        return null;
    }
}
