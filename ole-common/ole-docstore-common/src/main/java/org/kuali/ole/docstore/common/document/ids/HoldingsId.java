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
@XmlType(name = "holdings", propOrder = {
        "id",
        "itemIds"
})
@XmlRootElement(name = "holdings")

public class HoldingsId {

    private static final Logger LOG = Logger.getLogger(HoldingsId.class);
    @XmlElement(name = "id")
    String id;
    @XmlElementWrapper(name = "itemIds")
    @XmlElement(name = "itemId")
    List<String> itemIds;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public List<String> getItems() {
        if(itemIds == null) {
            itemIds = new ArrayList<>();
        }
        return itemIds;
    }


    public String serialize(Object object) {
        String result = null;
        StringWriter sw = new StringWriter();
        HoldingsId holdingsId = (HoldingsId) object;
        try {
            JAXBContext jaxbContext = JAXBContext.newInstance(HoldingsId.class);
            Marshaller jaxbMarshaller = jaxbContext.createMarshaller();
            jaxbMarshaller.setProperty(Marshaller.JAXB_FRAGMENT, Boolean.TRUE);
            jaxbMarshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
            jaxbMarshaller.setProperty(Marshaller.JAXB_ENCODING, "UTF-8");
            jaxbMarshaller.marshal(holdingsId, sw);
            result = sw.toString();
        } catch (Exception e) {
            LOG.error("Exception :", e);
        }
        return result;
    }

    public Object deserialize(String content) {

        JAXBElement<HoldingsId> holdingsIdJAXBElement = null;
        try {
            JAXBContext jaxbContext = JAXBContext.newInstance(HoldingsId.class);
            Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
            ByteArrayInputStream input = new ByteArrayInputStream(content.getBytes("UTF-8"));
            holdingsIdJAXBElement = jaxbUnmarshaller.unmarshal(new StreamSource(input), HoldingsId.class);
        } catch (Exception e) {
            LOG.error("Exception :", e);
        }
        return holdingsIdJAXBElement.getValue();
    }




}
