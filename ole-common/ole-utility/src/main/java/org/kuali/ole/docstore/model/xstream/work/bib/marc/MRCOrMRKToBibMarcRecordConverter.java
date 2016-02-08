package org.kuali.ole.docstore.model.xstream.work.bib.marc;

import org.kuali.ole.docstore.common.document.content.bib.marc.BibMarcRecords;
import org.kuali.ole.docstore.common.document.content.bib.marc.xstream.BibMarcRecordProcessor;

/**
 * Created by SheikS on 12/4/2015.
 */
public class MRCOrMRKToBibMarcRecordConverter {

    public BibMarcRecords convert(String content) {
        MRKToMARCXMLConverter mrkToMARCXMLConverter = new MRKToMARCXMLConverter();
        StringBuffer sb = new StringBuffer(
                "<?xml version=\"1.0\" encoding=\"UTF-8\"?><collection xmlns=\"http://www.loc.gov/MARC21/slim\">");
        String marcXML = mrkToMARCXMLConverter.convert(content);
        sb.append(marcXML.substring(marcXML.indexOf("<record>"), marcXML.indexOf("</collection>")));
        sb.append("</collection>");
        String marcXmlContent = sb.toString();
        return new BibMarcRecordProcessor().fromXML(marcXmlContent);
    }
}
