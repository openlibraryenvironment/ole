package org.kuali.ole.docstore.common.document;

import org.apache.log4j.Logger;
import org.kuali.ole.docstore.common.document.content.enums.DocCategory;
import org.kuali.ole.docstore.common.document.content.enums.DocFormat;
import org.kuali.ole.docstore.common.document.content.enums.DocType;
import org.kuali.ole.docstore.common.document.factory.JAXBContextFactory;

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
        PHoldings holdings = (PHoldings) object;
        try {
        StringWriter sw = new StringWriter();
            Marshaller jaxbMarshaller = JAXBContextFactory.getInstance().getMarshaller(PHoldings.class);
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
        PHoldings holdings = new PHoldings();
        try {
            Unmarshaller unmarshaller = JAXBContextFactory.getInstance().getUnMarshaller(PHoldings.class);
            ByteArrayInputStream input = new ByteArrayInputStream(content.getBytes("UTF-8"));
            synchronized (unmarshaller) {
                holdings = unmarshaller.unmarshal(new StreamSource(input), PHoldings.class).getValue();
            }
        } catch (Exception e) {
            LOG.error("Exception ", e);
        }
        return holdings;
    }


    @Override
    public Object deserializeContent(Object object) {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }
}
