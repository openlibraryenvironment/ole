package org.kuali.ole.docstore.common.document;

import org.apache.log4j.Logger;
import org.kuali.ole.docstore.common.document.content.bib.dc.unqualified.*;
import org.kuali.ole.docstore.common.document.content.bib.dc.unqualified.xstream.BibDublinUnQualifiedRecordProcessor;
import org.kuali.ole.docstore.common.document.content.enums.DocCategory;
import org.kuali.ole.docstore.common.document.content.enums.DocFormat;
import org.kuali.ole.docstore.common.document.content.enums.DocType;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
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
        StringWriter sw = new StringWriter();
        BibDcUnqualified bib = (BibDcUnqualified) object;
        try {
            JAXBContext jaxbContext = JAXBContext.newInstance(BibDcUnqualified.class);
            Marshaller jaxbMarshaller = jaxbContext.createMarshaller();
            jaxbMarshaller.setProperty(Marshaller.JAXB_FRAGMENT, Boolean.TRUE);
            jaxbMarshaller.marshal(bib, sw);
            result = sw.toString();
        } catch (Exception e) {
            LOG.error("Exception :", e);
        }
        return result;
    }

    @Override
    public Object deserialize(String content) {

        JAXBElement<BibDcUnqualified> bibElement = null;
        try {
            JAXBContext jaxbContext = JAXBContext.newInstance(BibDcUnqualified.class);
            Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
            ByteArrayInputStream input = new ByteArrayInputStream(content.getBytes("UTF-8"));
            bibElement = jaxbUnmarshaller.unmarshal(new StreamSource(input), BibDcUnqualified.class);
        } catch (Exception e) {
            LOG.error("Exception :", e);
        }
        return bibElement.getValue();
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
