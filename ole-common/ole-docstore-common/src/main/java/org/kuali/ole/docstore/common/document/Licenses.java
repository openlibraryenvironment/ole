package org.kuali.ole.docstore.common.document;

import org.apache.log4j.Logger;
import org.kuali.ole.docstore.common.document.factory.JAXBContextFactory;

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
 * User: chenchulakshmig
 * Date: 2/25/14
 * Time: 3:41 PM
 * To change this template use File | Settings | File Templates.
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "licenses", propOrder = {
        "licenses"
})
@XmlRootElement
public class Licenses {

    private static final Logger LOG = Logger.getLogger(Licenses.class);

    @XmlElement(name = "license")
    protected List<License> licenses;

    public List<License> getLicenses() {
        if (licenses == null) {
            licenses = new ArrayList<>();
        }
        return licenses;
    }
    public static String serialize(Object object) {
        String result = null;
        Licenses licenses = (Licenses) object;
        try {
            StringWriter sw = new StringWriter();
            Marshaller jaxbMarshaller = JAXBContextFactory.getInstance().getMarshaller(Licenses.class);
            synchronized (jaxbMarshaller) {
                jaxbMarshaller.marshal(licenses, sw);
            }
            result = sw.toString();
        } catch (Exception e) {
            LOG.error("Exception :", e);
        }
        return result;
    }

    public static Object deserialize(String licensesXml) {
        Licenses licenses = new Licenses();
        try {
            Unmarshaller unmarshaller = JAXBContextFactory.getInstance().getUnMarshaller(Licenses.class);
            ByteArrayInputStream input = new ByteArrayInputStream(licensesXml.getBytes("UTF-8"));
            synchronized (unmarshaller) {
                licenses = unmarshaller.unmarshal(new StreamSource(input), Licenses.class).getValue();
            }
        } catch (Exception e) {
            LOG.error("Exception :", e);
        }
        return licenses;
    }


}
