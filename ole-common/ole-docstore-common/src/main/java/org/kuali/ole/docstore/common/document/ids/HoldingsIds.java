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
 * User: rajeshbabuk
 * Date: 7/4/14
 * Time: 4:05 PM
 * To change this template use File | Settings | File Templates.
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "holdingsIds", propOrder = {
        "ids"
})
@XmlRootElement(name = "holdingsIds")
public class HoldingsIds {

    private static final Logger LOG = Logger.getLogger(HoldingsIds.class);
    @XmlElementWrapper(name = "holdingsIds")
    @XmlElement(name = "ids")
    List<String> ids;

    public List<String> getIds() {
        if(ids == null) {
            ids = new ArrayList<>();
        }
        return ids;
    }

    public String serialize(Object object) {
        String result = null;
        StringWriter sw = new StringWriter();
        HoldingsIds holdingsIds = (HoldingsIds) object;
        try {
            JAXBContext jaxbContext = JAXBContext.newInstance(HoldingsIds.class);
            Marshaller jaxbMarshaller = jaxbContext.createMarshaller();
            jaxbMarshaller.setProperty(Marshaller.JAXB_FRAGMENT, Boolean.TRUE);
            jaxbMarshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
            jaxbMarshaller.setProperty(Marshaller.JAXB_ENCODING, "UTF-8");
            jaxbMarshaller.marshal(holdingsIds, sw);
            result = sw.toString();
        } catch (Exception e) {
            LOG.error("Exception :", e);
        }
        return result;
    }

    public Object deserialize(String content) {

        JAXBElement<HoldingsIds> holdingsIdsJAXBElement = null;
        try {
            JAXBContext jaxbContext = JAXBContext.newInstance(HoldingsIds.class);
            Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
            ByteArrayInputStream input = new ByteArrayInputStream(content.getBytes("UTF-8"));
            holdingsIdsJAXBElement = jaxbUnmarshaller.unmarshal(new StreamSource(input), HoldingsIds.class);
        } catch (Exception e) {
            LOG.error("Exception :", e);
        }
        return holdingsIdsJAXBElement.getValue();
    }
}
