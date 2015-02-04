package org.kuali.ole.docstore.common.document;

import org.apache.log4j.Logger;
import org.kuali.ole.docstore.common.document.content.enums.DocCategory;
import org.kuali.ole.docstore.common.document.content.enums.DocFormat;
import org.kuali.ole.docstore.common.document.content.enums.DocType;

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
 * Time: 3:31 PM
 * To change this template use File | Settings | File Templates.
 */
@XmlRootElement(name = "bibDoc")
public class BibDc extends Bib {

    private static final Logger LOG = Logger.getLogger(BibDc.class);
    public BibDc() {
        category=DocCategory.WORK.getCode();
        type=DocType.BIB.getCode();
        format=DocFormat.DUBLIN_CORE.getCode();
    }

    @Override
    public String serialize(Object object) {
        String result = null;
        StringWriter sw = new StringWriter();
        BibDc bib = (BibDc) object;
        try {
            JAXBContext jaxbContext = JAXBContext.newInstance(BibDc.class);
            Marshaller jaxbMarshaller = jaxbContext.createMarshaller();
            jaxbMarshaller.setProperty(Marshaller.JAXB_FRAGMENT, Boolean.TRUE);
            jaxbMarshaller.marshal(bib, sw);
            result = sw.toString();
        } catch (Exception e) {
            LOG.error("Exception :", e);
        }
        return result;
    }

    @Override
    public Object deserialize(String content) {

        JAXBElement<BibDc> bibElement = null;
        try {
            JAXBContext jaxbContext = JAXBContext.newInstance(BibDc.class);
            Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
            ByteArrayInputStream input = new ByteArrayInputStream(content.getBytes("UTF-8"));
            bibElement = jaxbUnmarshaller.unmarshal(new StreamSource(input), BibDc.class);
        } catch (Exception e) {
            LOG.error("Exception :", e);
        }
        return bibElement.getValue();
    }
}
