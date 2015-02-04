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
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: sambasivam
 * Date: 6/6/14
 * Time: 2:25 PM
 * To change this template use File | Settings | File Templates.
 */

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "bibIds", propOrder = {
        "bibIds"
})
@XmlRootElement(name = "bibIds")
public class BibIds {
    private static final Logger LOG = Logger.getLogger(BibIds.class);
    @XmlElementWrapper(name = "bibIds")
    @XmlElement(name = "bibId")
    public List<BibId> bibIds;


    public List<BibId> getBibIds() {
        if(bibIds == null) {
            bibIds = new ArrayList<>();
        }
        return bibIds;
    }


    public static String serialize(Object object) {
        String result = null;
        StringWriter sw = new StringWriter();
        BibIds bibIds = (BibIds) object;
        try {
            JAXBContext jaxbContext = JAXBContext.newInstance(BibIds.class);
            Marshaller jaxbMarshaller = jaxbContext.createMarshaller();
            jaxbMarshaller.setProperty(Marshaller.JAXB_FRAGMENT, Boolean.TRUE);
            jaxbMarshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
            jaxbMarshaller.setProperty(Marshaller.JAXB_ENCODING, "UTF-8");
            jaxbMarshaller.marshal(bibIds, sw);
            result = sw.toString();
        } catch (Exception e) {
            LOG.error("Exception :", e);
        }
        return result;
    }

    public static Object deserialize(String content) {

        JAXBElement<BibIds> bibIdsJAXBElement = null;
        try {
            JAXBContext jaxbContext = JAXBContext.newInstance(BibIds.class);
            Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
            ByteArrayInputStream input = new ByteArrayInputStream(content.getBytes("UTF-8"));
            bibIdsJAXBElement = jaxbUnmarshaller.unmarshal(new StreamSource(input), BibIds.class);
        } catch (Exception e) {
            LOG.error("Exception :", e);
        }
        return bibIdsJAXBElement.getValue();
    }


}
