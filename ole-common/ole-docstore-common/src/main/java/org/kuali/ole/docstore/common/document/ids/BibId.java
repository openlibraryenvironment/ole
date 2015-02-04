package org.kuali.ole.docstore.common.document.ids;

import org.apache.log4j.Logger;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import javax.xml.bind.annotation.*;
import javax.xml.transform.stream.StreamSource;
import java.io.ByteArrayInputStream;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: sambasivam
 * Date: 6/6/14
 * Time: 12:54 PM
 * To change this template use File | Settings | File Templates.
 */

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "bib", propOrder = {
        "id",
        "holdingsIds"
})
@XmlRootElement(name = "bib")

public class BibId {
    private static final Logger LOG = Logger.getLogger(BibId.class);
    @XmlElement(name = "id")
    protected String id;

    @XmlElementWrapper(name = "holdingsIds")
    @XmlElement(name = "holdingsId")
    protected List<HoldingsId> holdingsIds;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public List<HoldingsId> getHoldingsIds() {
        if(holdingsIds == null) {
            holdingsIds = new ArrayList<>();
        }
        return holdingsIds;
    }

    public static String serialize(Object object) {
        String result = null;
        StringWriter sw = new StringWriter();
        BibId bibId = (BibId) object;
        try {
            JAXBContext jaxbContext = JAXBContext.newInstance(BibId.class);
            Marshaller jaxbMarshaller = jaxbContext.createMarshaller();
            jaxbMarshaller.setProperty(Marshaller.JAXB_FRAGMENT, Boolean.TRUE);
            jaxbMarshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
            jaxbMarshaller.setProperty(Marshaller.JAXB_ENCODING, "UTF-8");
            jaxbMarshaller.marshal(bibId, sw);
            result = sw.toString();
        } catch (Exception e) {
            LOG.error("Exception :", e);
        }
        return result;
    }

    public static Object deserialize(String content) {

        JAXBElement<BibId> bibIdJAXBElement = null;
        try {
            JAXBContext jaxbContext = JAXBContext.newInstance(BibId.class);
            Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
            ByteArrayInputStream input = new ByteArrayInputStream(content.getBytes("UTF-8"));
            bibIdJAXBElement = jaxbUnmarshaller.unmarshal(new StreamSource(input), BibId.class);
        } catch (Exception e) {
            LOG.error("Exception :", e);
        }
        return bibIdJAXBElement.getValue();
    }

}
