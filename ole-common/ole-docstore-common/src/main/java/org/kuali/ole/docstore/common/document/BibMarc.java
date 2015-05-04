package org.kuali.ole.docstore.common.document;

import org.apache.log4j.Logger;
import org.kuali.ole.docstore.common.document.content.bib.marc.BibMarcRecords;
import org.kuali.ole.docstore.common.document.content.bib.marc.xstream.BibMarcRecordProcessor;
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
public class BibMarc extends Bib {

    private static final Logger LOG = Logger.getLogger(BibMarc.class);

    public BibMarc() {
        category=DocCategory.WORK.getCode();
        type=DocType.BIB.getCode();
        format=DocFormat.MARC.getCode();
    }

    @Override
    public String serialize(Object object) {
        String result = null;
        BibMarc bib = (BibMarc) object;
        try {
            StringWriter sw = new StringWriter();
            Marshaller jaxbMarshaller = JAXBContextFactory.getInstance().getMarshaller(BibMarc.class);
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
        BibMarc bib = new BibMarc();
        try {
            Unmarshaller unmarshaller = JAXBContextFactory.getInstance().getUnMarshaller(BibMarc.class);
            ByteArrayInputStream input = new ByteArrayInputStream(content.getBytes("UTF-8"));
            synchronized (unmarshaller) {
                bib = unmarshaller.unmarshal(new StreamSource(input), BibMarc.class).getValue();
            }
        } catch (Exception e) {
            LOG.error("Exception :", e);
            throw new DocstoreDeserializeException(DocstoreResources.BIB_CREATION_FAILED, DocstoreResources.BIB_CREATION_FAILED);
        }
        return bib;
    }


    @Override
    public Object deserializeContent(Object object) {
        Bib bib = (Bib) object;
        BibMarcMapping bibMarcMapping = new BibMarcMapping();
        return bibMarcMapping.getDocument(bib);
    }

    @Override
    public Object deserializeContent(String content) {
        BibMarcRecordProcessor recordProcessor = new BibMarcRecordProcessor();
        BibMarcRecords bibMarcRecords = recordProcessor.fromXML(content);
        return bibMarcRecords;
    }

    @Override
    public String serializeContent(Object object) {
        BibMarcRecordProcessor recordProcessor = new BibMarcRecordProcessor();
        BibMarcRecords bibMarc = (BibMarcRecords) object;
        String content = recordProcessor.toXml(bibMarc);
        return content;
    }

}
