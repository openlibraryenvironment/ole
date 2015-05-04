package org.kuali.ole.docstore.common.document;

import org.apache.log4j.Logger;
import org.kuali.ole.docstore.common.document.content.bib.dc.unqualified.*;
import org.kuali.ole.docstore.common.document.content.bib.dc.unqualified.xstream.BibDublinUnQualifiedRecordProcessor;
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
 * Date: 1/21/14
 * Time: 3:39 PM
 * To change this template use File | Settings | File Templates.
 */
@XmlRootElement(name = "bibDoc")

public class BibDcUnqualified extends Bib {

    private static final Logger LOG = Logger.getLogger(BibDcUnqualified.class);
    public BibDcUnqualified () {
        category=DocCategory.WORK.getCode();
        type=DocType.BIB.getCode();
        format=DocFormat.DUBLIN_UNQUALIFIED.getCode();
    }

    @Override
    public String serialize(Object object) {
        String result = null;
        BibDcUnqualified bib = (BibDcUnqualified) object;
        try {
            StringWriter sw = new StringWriter();
            Marshaller jaxbMarshaller = JAXBContextFactory.getInstance().getMarshaller(BibDcUnqualified.class);
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
        BibDcUnqualified bib = new BibDcUnqualified();
        try {
            Unmarshaller unmarshaller = JAXBContextFactory.getInstance().getUnMarshaller(BibDcUnqualified.class);
            ByteArrayInputStream input = new ByteArrayInputStream(content.getBytes("UTF-8"));
            synchronized (unmarshaller) {
                bib = unmarshaller.unmarshal(new StreamSource(input), BibDcUnqualified.class).getValue();
            }
        } catch (Exception e) {
            LOG.error("Exception :", e);
            throw new DocstoreDeserializeException(DocstoreResources.BIB_CREATION_FAILED, DocstoreResources.BIB_CREATION_FAILED);
        }
        return bib;
    }


    @Override
    public Object deserializeContent(Object object) {
        BibDcUnqualified bibDcUnqualified= (BibDcUnqualified)object;
        BibDublinUnQualifiedRecordProcessor bibDublinUnQualifiedRecordProcessor=new BibDublinUnQualifiedRecordProcessor();
        BibDublinUnQualifiedRecord bibDcUnqualifiedRecord=bibDublinUnQualifiedRecordProcessor.fromXML(bibDcUnqualified.getContent());
        ListRecords listRecords=bibDcUnqualifiedRecord.getListRecords();
        for(Record recordlist:listRecords.getRecords()){
            MetaData metaData=recordlist.getMetadata();
            for(OaiDcDoc oaiDcDoc:metaData.getOaiDcDocs()){
                for (Tag tag:oaiDcDoc.getAllTags()){
                   if(tag.getName().equalsIgnoreCase("dc:title")){
                       bibDcUnqualified.setTitle(tag.getValue());
                   }
                }
            }
        }
        return bibDcUnqualified;
    }
}
