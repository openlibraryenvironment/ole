package org.kuali.ole.docstore.common.document.content.bib.dc.xstream;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.DomDriver;
import com.thoughtworks.xstream.io.xml.XmlFriendlyReplacer;
import org.kuali.ole.docstore.common.document.content.bib.dc.BibDcRecord;
import org.kuali.ole.docstore.common.document.content.bib.dc.DCValue;

/**
 * Created with IntelliJ IDEA.
 * User: sambasivam
 * Date: 12/13/13
 * Time: 3:54 PM
 * To change this template use File | Settings | File Templates.
 */
public class BibDcRecordProcessor {
    /**
     * Method to covert xml content to BibDcRecord.
     *
     * @param fileContent
     * @return
     */
    public BibDcRecord fromXML(String fileContent) {
        //throws ParserConfigurationException, IOException, SAXException, XPathExpressionException {
        XStream xStream = new XStream();
        xStream.alias("dublin_core", BibDcRecord.class);
        xStream.alias("dcvalue", DCValue.class);
        xStream.addImplicitCollection(BibDcRecord.class, "dcValues", DCValue.class);
        xStream.registerConverter(new BibDublinRecordConverter());
        xStream.registerConverter(new DCValueConverter());
//        fileContent=fileContent.replaceAll("&","&amp;");
//        fileContent=fileContent.replaceAll("< ","&lt; ");
        return (BibDcRecord) xStream.fromXML(fileContent);
    }

    /**
     * Method to covert WorkBibDublinRecord to XML Format.
     *
     * @param rec
     * @return
     */
    public String toXml(BibDcRecord rec) {
        XmlFriendlyReplacer replacer = new XmlFriendlyReplacer("ddd", "_");
        XStream xStream = new XStream(new DomDriver("UTF-8", replacer));
        xStream.alias("dublin_core", BibDcRecord.class);
        xStream.alias("dcvalue", DCValue.class);
        xStream.addImplicitCollection(BibDcRecord.class, "dcValues", DCValue.class);
        xStream.registerConverter(new BibDublinRecordConverter());
        xStream.registerConverter(new DCValueConverter());
        return xStream.toXML(rec);
    }

}
