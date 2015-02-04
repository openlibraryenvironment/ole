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
 * Time: 2:54 PM
 * To change this template use File | Settings | File Templates.
 */

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "itemIds", propOrder = {
        "ids"
})
@XmlRootElement(name = "itemIds")
public class ItemIds {

    private static final Logger LOG = Logger.getLogger(ItemIds.class);
    @XmlElementWrapper(name = "itemIds")
    @XmlElement(name = "itemId")
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
        ItemIds itemIds = (ItemIds) object;
        try {
            JAXBContext jaxbContext = JAXBContext.newInstance(ItemIds.class);
            Marshaller jaxbMarshaller = jaxbContext.createMarshaller();
            jaxbMarshaller.setProperty(Marshaller.JAXB_FRAGMENT, Boolean.TRUE);
            jaxbMarshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
            jaxbMarshaller.setProperty(Marshaller.JAXB_ENCODING, "UTF-8");
            jaxbMarshaller.marshal(itemIds, sw);
            result = sw.toString();
        } catch (Exception e) {
            LOG.error("Exception :", e);
        }
        return result;
    }

    public Object deserialize(String content) {

        JAXBElement<ItemIds> itemIdsJAXBElement = null;
        try {
            JAXBContext jaxbContext = JAXBContext.newInstance(ItemIds.class);
            Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
            ByteArrayInputStream input = new ByteArrayInputStream(content.getBytes("UTF-8"));
            itemIdsJAXBElement = jaxbUnmarshaller.unmarshal(new StreamSource(input), ItemIds.class);
        } catch (Exception e) {
            LOG.error("Exception :", e);
        }
        return itemIdsJAXBElement.getValue();
    }

}
