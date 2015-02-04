package org.kuali.ole.docstore.common.document;

import org.apache.log4j.Logger;
import org.kuali.ole.docstore.common.document.content.enums.DocCategory;
import org.kuali.ole.docstore.common.document.content.enums.DocFormat;
import org.kuali.ole.docstore.common.document.content.enums.DocType;
import org.kuali.ole.docstore.common.document.content.instance.OleHoldings;
import org.kuali.ole.docstore.common.document.content.instance.xstream.HoldingOlemlRecordProcessor;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import javax.xml.transform.stream.StreamSource;
import java.io.ByteArrayInputStream;
import java.io.StringWriter;

/**
 * Created with IntelliJ IDEA.
 * User: sambasivam
 * Date: 12/13/13
 * Time: 3:32 PM
 * To change this template use File | Settings | File Templates.
 */
public class EHoldingsOleml extends EHoldings {

    private static final Logger LOG = Logger.getLogger(EHoldingsOleml.class);
    protected String accessStatus;
    protected String platForm;
    protected String imprint;
    protected String statisticalCode;
    protected String location;
    protected String url;
    protected String eResourceName;
    protected String subscriptionStatus;

    public EHoldingsOleml() {
        holdingsType = "electronic";
        category=DocCategory.WORK.getCode();
        type=DocType.EHOLDINGS.getCode();
        format=DocFormat.OLEML.getCode();
    }

    public String getAccessStatus() {
        return accessStatus;
    }

    public void setAccessStatus(String accessStatus) {
        this.accessStatus = accessStatus;
    }

    public String getPlatForm() {
        return platForm;
    }

    public void setPlatForm(String platForm) {
        this.platForm = platForm;
    }

    public String getImprint() {
        return imprint;
    }

    public void setImprint(String imprint) {
        this.imprint = imprint;
    }

    public String getStatisticalCode() {
        return statisticalCode;
    }

    public void setStatisticalCode(String statisticalCode) {
        this.statisticalCode = statisticalCode;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String geteResourceName() {
        return eResourceName;
    }

    public void seteResourceName(String eResourceName) {
        this.eResourceName = eResourceName;
    }

    public String getSubscriptionStatus() {
        return subscriptionStatus;
    }

    public void setSubscriptionStatus(String subscriptionStatus) {
        this.subscriptionStatus = subscriptionStatus;
    }

    @Override
    public String serialize(Object object) {
        String result = null;
        StringWriter sw = new StringWriter();
        EHoldingsOleml holdings = (EHoldingsOleml) object;
        try {
            JAXBContext jaxbContext = JAXBContext.newInstance(EHoldingsOleml.class);
            Marshaller jaxbMarshaller = jaxbContext.createMarshaller();
            jaxbMarshaller.marshal(holdings, sw);
            result = sw.toString();
        } catch (Exception e) {
            LOG.error("Exception ", e);
        }
        return result;
    }

    @Override
    public Object deserialize(String content) {
        JAXBElement<EHoldingsOleml> holdingsElement = null;
        try {
            JAXBContext jaxbContext = JAXBContext.newInstance(EHoldingsOleml.class);
            Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
            ByteArrayInputStream input = new ByteArrayInputStream(content.getBytes("UTF-8"));
            holdingsElement = jaxbUnmarshaller.unmarshal(new StreamSource(input), EHoldingsOleml.class);
        } catch (Exception e) {
            LOG.error("Exception ", e);
        }
        return holdingsElement.getValue();
    }

    @Override
    public Object deserializeContent(String content) {
        HoldingOlemlRecordProcessor recordProcessor = new HoldingOlemlRecordProcessor();
        OleHoldings oleHoldings = recordProcessor.fromXML(content);
        return oleHoldings;
    }

    @Override
    public String serializeContent(Object object) {
        OleHoldings oleHoldings = (OleHoldings) object;
        HoldingOlemlRecordProcessor recordProcessor = new HoldingOlemlRecordProcessor();
        String content = recordProcessor.toXML(oleHoldings);
        return content;
    }

}
