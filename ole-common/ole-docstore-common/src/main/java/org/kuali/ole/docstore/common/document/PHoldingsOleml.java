package org.kuali.ole.docstore.common.document;

import org.apache.log4j.Logger;
import org.kuali.ole.docstore.common.document.content.enums.DocCategory;
import org.kuali.ole.docstore.common.document.content.enums.DocFormat;
import org.kuali.ole.docstore.common.document.content.enums.DocType;
import org.kuali.ole.docstore.common.document.content.instance.OleHoldings;
import org.kuali.ole.docstore.common.document.content.instance.xstream.HoldingOlemlRecordProcessor;
import org.kuali.ole.docstore.common.document.factory.JAXBContextFactory;

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
@XmlRootElement(name = "holdingsDoc")
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
        PHoldingsOleml holdings = (PHoldingsOleml) object;
        try {
        StringWriter sw = new StringWriter();
            Marshaller jaxbMarshaller = JAXBContextFactory.getInstance().getMarshaller(PHoldingsOleml.class);
            synchronized (jaxbMarshaller) {
                jaxbMarshaller.marshal(holdings, sw);
            }
            result = sw.toString();
        } catch (Exception e) {
            LOG.error("Exception ", e);
        }
        return result;
    }

    @Override
    public Object deserialize(String content) {
        PHoldingsOleml holdings = new PHoldingsOleml();
        try {
            Unmarshaller unmarshaller = JAXBContextFactory.getInstance().getUnMarshaller(PHoldingsOleml.class);
            ByteArrayInputStream input = new ByteArrayInputStream(content.getBytes("UTF-8"));
            synchronized (unmarshaller) {
                holdings = unmarshaller.unmarshal(new StreamSource(input), PHoldingsOleml.class).getValue();
            }
        } catch (Exception e) {
            LOG.error("Exception ", e);
        }
        return holdings;
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
