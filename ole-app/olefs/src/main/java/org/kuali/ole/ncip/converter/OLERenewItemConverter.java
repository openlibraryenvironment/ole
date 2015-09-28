package org.kuali.ole.ncip.converter;

import com.thoughtworks.xstream.XStream;
import org.apache.log4j.Logger;
import org.kuali.ole.bo.OLERenewItem;
import org.kuali.ole.converter.OleCirculationHandler;
import org.kuali.ole.ncip.bo.OLERenewItemList;

/**
 * Created with IntelliJ IDEA.
 * User: sheiksalahudeenm
 * Date: 9/3/13
 * Time: 8:52 PM
 * To change this template use File | Settings | File Templates.
 */
public class OLERenewItemConverter {
    final Logger LOG = Logger.getLogger(OLEItemFineConverter.class);

    private static XStream xStream = getXstream();
    private static XStream xStream1 = getXstream1();
    private static XStream xStreamList = getXstreamList();
    private static XStream xStreamList1 = getXstreamList1();

    public String generateRenewItemXml(OLERenewItem olePlaceRequest) {
        return xStream.toXML(olePlaceRequest);
    }

    public String generateRenewItemXmlForSIP2(OLERenewItem olePlaceRequest) {
        return xStream1.toXML(olePlaceRequest);
    }

    public Object generateRenewItemObject(String xml) {
        return xStream1.fromXML(xml);
    }

    public String generateRenewItemListXml(OLERenewItemList olePlaceRequest) {
        return xStreamList.toXML(olePlaceRequest);
    }

    public String generateRenewItemListXmlForSip2(OLERenewItemList olePlaceRequest) {
        return xStreamList1.toXML(olePlaceRequest);
    }

    public Object generateRenewItemListObject(String xml) {
        return xStreamList1.fromXML(xml);
    }

    public Object generateRenewItemListObjectForSip2(String xml) {
        return xStreamList1.fromXML(xml);
    }

    public String generateRenewItemListJson(String xml) {
        OLERenewItemList oleRenewItemList = (OLERenewItemList) generateRenewItemListObject(xml);
        OleCirculationHandler xmlContentHandler = new OleCirculationHandler();
        if (oleRenewItemList == null) {
            oleRenewItemList = new OLERenewItemList();
        }
        try {
            return xmlContentHandler.marshalToJSON(oleRenewItemList);
        } catch (Exception e) {
            LOG.error(e, e);
        }
        return null;
    }
    public String generateRenewItemJson(String xml) {
        OLERenewItem oleRenewItem = (OLERenewItem) generateRenewItemObject(xml);
        OleCirculationHandler xmlContentHandler = new OleCirculationHandler();
        if (oleRenewItem == null) {
            oleRenewItem = new OLERenewItem();
        }
        try {
            return xmlContentHandler.marshalToJSON(oleRenewItem);
        } catch (Exception e) {
            LOG.error(e, e);
        }
        return null;
    }
    private static XStream getXstreamList() {
        XStream xStream = new XStream();
        xStream.alias("renewItem", OLERenewItem.class);
        xStream.alias("renewItemList", OLERenewItemList.class);
        xStream.omitField(OLERenewItem.class, "patronBarcode");
        xStream.omitField(OLERenewItem.class, "titleIdentifier");
        xStream.omitField(OLERenewItem.class, "itemBarcode");
        xStream.omitField(OLERenewItem.class, "feeAmount");
        xStream.omitField(OLERenewItem.class, "feeType");
        xStream.omitField(OLERenewItem.class, "mediaType");
        xStream.omitField(OLERenewItem.class, "transactionId");
        xStream.omitField(OLERenewItem.class, "itemProperties");
        xStream.addImplicitCollection(OLERenewItemList.class, "renewItemList", OLERenewItem.class);
        return xStream;
    }

    private static XStream getXstreamList1() {
        XStream xStream = new XStream();
        xStream.alias("renewItem", OLERenewItem.class);
        xStream.alias("renewItemList", OLERenewItemList.class);
        xStream.addImplicitCollection(OLERenewItemList.class, "renewItemList", OLERenewItem.class);
        return xStream;
    }

    private static XStream getXstream() {
        XStream xStream = new XStream();
        xStream.alias("renewItem", OLERenewItem.class);
        xStream.omitField(OLERenewItem.class, "patronBarcode");
        xStream.omitField(OLERenewItem.class, "titleIdentifier");
        xStream.omitField(OLERenewItem.class, "itemBarcode");
        xStream.omitField(OLERenewItem.class, "feeAmount");
        xStream.omitField(OLERenewItem.class, "feeType");
        xStream.omitField(OLERenewItem.class, "mediaType");
        xStream.omitField(OLERenewItem.class, "transactionId");
        xStream.omitField(OLERenewItem.class, "itemProperties");
        return xStream;
    }

    private static XStream getXstream1() {
        XStream xStream = new XStream();
        xStream.alias("renewItem", OLERenewItem.class);
        return xStream;
    }


}
