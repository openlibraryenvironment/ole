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
import javax.xml.bind.annotation.XmlRootElement;
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
@XmlRootElement(name = "holdings")
public class PHoldingsOleml extends PHoldings {

    private static final Logger LOG = Logger.getLogger(PHoldingsOleml.class);

    public PHoldingsOleml() {
        holdingsType = "print";
        category=DocCategory.WORK.getCode();
        type=DocType.HOLDINGS.getCode();
        format=DocFormat.OLEML.getCode();
    }

    @Override
    public String serialize(Object object) {
        String result = null;
        StringWriter sw = new StringWriter();
        PHoldingsOleml pHoldings = (PHoldingsOleml) object;
        try {
            JAXBContext jaxbContext = JAXBContext.newInstance(PHoldingsOleml.class);
            Marshaller jaxbMarshaller = jaxbContext.createMarshaller();
            jaxbMarshaller.marshal(pHoldings, sw);
            result = sw.toString();
        } catch (Exception e) {
            LOG.error("Exception ", e);
        }
        return result;
    }

    @Override
    public Object deserialize(String content) {

        JAXBElement<PHoldingsOleml> pHoldingsElement = null;
        try {
            JAXBContext jaxbContext = JAXBContext.newInstance(PHoldingsOleml.class);
            Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
            ByteArrayInputStream input = new ByteArrayInputStream(content.getBytes("UTF-8"));
            pHoldingsElement = jaxbUnmarshaller.unmarshal(new StreamSource(input), PHoldingsOleml.class);
        } catch (Exception e) {
            LOG.error("Exception ", e);
        }
        return pHoldingsElement.getValue();
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
