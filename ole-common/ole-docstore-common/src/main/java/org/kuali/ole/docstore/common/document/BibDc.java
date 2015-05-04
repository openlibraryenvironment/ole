package org.kuali.ole.docstore.common.document;

import org.apache.log4j.Logger;
import org.kuali.ole.docstore.common.document.content.enums.DocCategory;
import org.kuali.ole.docstore.common.document.content.enums.DocFormat;
import org.kuali.ole.docstore.common.document.content.enums.DocType;
import org.kuali.ole.docstore.common.document.factory.JAXBContextFactory;
import org.kuali.ole.docstore.common.exception.DocstoreDeserializeException;
import org.kuali.ole.docstore.common.exception.DocstoreResources;

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
        BibDc bib = (BibDc) object;
        try {
            StringWriter sw = new StringWriter();
            Marshaller jaxbMarshaller = JAXBContextFactory.getInstance().getMarshaller(BibDc.class);
            synchronized (jaxbMarshaller) {
            jaxbMarshaller.marshal(bib, sw);
            }
            result = sw.toString();
        } catch (Exception e) {
            LOG.error("Exception :", e);
        }
        return result;
    }

    @Override
    public Object deserialize(String content) {
        BibDc bib = new BibDc();
        try {
            Unmarshaller unmarshaller = JAXBContextFactory.getInstance().getUnMarshaller(BibDc.class);
            ByteArrayInputStream input = new ByteArrayInputStream(content.getBytes("UTF-8"));
            synchronized (unmarshaller) {
                bib = unmarshaller.unmarshal(new StreamSource(input), BibDc.class).getValue();
            }
        } catch (Exception e) {
            LOG.error("Exception :", e);
            throw new DocstoreDeserializeException(DocstoreResources.BIB_CREATION_FAILED, DocstoreResources.BIB_CREATION_FAILED);
        }
        return bib;
    }

}
