package org.kuali.ole.docstore.common.document;

import org.apache.log4j.Logger;
import org.kuali.ole.docstore.common.document.factory.JAXBContextFactory;

import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.transform.stream.StreamSource;
import java.io.ByteArrayInputStream;
import java.io.StringWriter;

/**
 * Created with IntelliJ IDEA.
 * User: chenchulakshmig
 * Date: 2/25/14
 * Time: 2:53 PM
 * To change this template use File | Settings | File Templates.
 */
@XmlRootElement(name = "license")
public class License extends DocstoreDocument {

    private static final Logger LOG = Logger.getLogger(License.class);

    @Override
    public String serialize(Object object) {
        String result = null;
        License license = (License) object;
        try {
            StringWriter sw = new StringWriter();
            JAXBContextFactory jaxbContextFactory = JAXBContextFactory.getInstance();
            Marshaller jaxbMarshaller = jaxbContextFactory.getMarshaller(License.class);
            synchronized (jaxbMarshaller) {
                jaxbMarshaller.marshal(license, sw);
            }

            result = sw.toString();
        } catch (Exception e) {
            LOG.error("Exception :", e);
        }
        return result;
    }

    @Override
    public Object deserialize(String content) {
        License license = new License();
        try {
            Unmarshaller unmarshaller = JAXBContextFactory.getInstance().getUnMarshaller(License.class);
            ByteArrayInputStream input = new ByteArrayInputStream(content.getBytes("UTF-8"));
            synchronized (unmarshaller) {
                license = unmarshaller.unmarshal(new StreamSource(input), License.class).getValue();
            }

        } catch (Exception e) {
            LOG.error("Exception :", e);
        }
        return license;
    }

    @Override
    public Object deserializeContent(Object object) {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public Object deserializeContent(String content) {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public String serializeContent(Object object) {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }
}
