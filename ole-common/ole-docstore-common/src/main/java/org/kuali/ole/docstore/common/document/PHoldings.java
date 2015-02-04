package org.kuali.ole.docstore.common.document;

import org.apache.log4j.Logger;
import org.kuali.ole.docstore.common.document.content.enums.DocCategory;
import org.kuali.ole.docstore.common.document.content.enums.DocFormat;
import org.kuali.ole.docstore.common.document.content.enums.DocType;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;
import javax.xml.transform.stream.StreamSource;
import java.io.ByteArrayInputStream;
import java.io.StringWriter;


/**
 * <p>Java class for pHoldings complex type.
 * <p/>
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p/>
 * <pre>
 * &lt;complexType name="pHoldings">
 *   &lt;complexContent>
 *     &lt;extension base="{}holdings">
 *       &lt;sequence>
 *       &lt;/sequence>
 *     &lt;/extension>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "pHoldings")

@XmlRootElement(name = "holdingsDoc")
public class PHoldings
        extends Holdings {

    private static final Logger LOG = Logger.getLogger(PHoldings.class);
    public static final String PRINT="print";

    public PHoldings() {
        holdingsType = PRINT;
        category=DocCategory.WORK.getCode();
        type=DocType.HOLDINGS.getCode();
        format=DocFormat.OLEML.getCode();
    }

    @Override
    public String serialize(Object object) {
        String result = null;
        StringWriter sw = new StringWriter();
        PHoldings pHoldings = (PHoldings) object;
        try {
            JAXBContext jaxbContext = JAXBContext.newInstance(PHoldings.class);
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

        JAXBElement<PHoldings> pHoldingsElement = null;
        try {
            JAXBContext jaxbContext = JAXBContext.newInstance(PHoldings.class);
            Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
            ByteArrayInputStream input = new ByteArrayInputStream(content.getBytes("UTF-8"));
            pHoldingsElement = jaxbUnmarshaller.unmarshal(new StreamSource(input), PHoldings.class);
        } catch (Exception e) {
            LOG.error("Exception ", e);
        }
        return pHoldingsElement.getValue();
    }

    @Override
    public Object deserializeContent(Object object) {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }
}
