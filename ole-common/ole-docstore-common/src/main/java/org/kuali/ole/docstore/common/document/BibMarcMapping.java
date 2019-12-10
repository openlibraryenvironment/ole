package org.kuali.ole.docstore.common.document;

import org.kuali.ole.docstore.common.document.content.bib.marc.BibMarcRecords;
import org.kuali.ole.docstore.common.document.content.bib.marc.xstream.BibMarcRecordProcessor;

/**
 * Created with IntelliJ IDEA.
 * User: jayabharathreddy
 * Date: 12/27/13
 * Time: 4:35 PM
 * To change this template use File | Settings | File Templates.
 */
public class BibMarcMapping {

    public Object getDocument(Bib bib) {

        String content = bib.getContent();
        if(content != null && content.contains("marc:")) {
            content = content.replaceAll("marc:leader","leader");
            if(content.contains("<controlfield")) {
                content = content.replaceAll("<controlfield","<marc:controlfield");
            }
            if(content.contains("<datafield")) {
                content = content.replaceAll("<datafield","<marc:datafield");
            }
            if(content.contains("<subfield")) {
                content = content.replaceAll("<subfield","<marc:subfield");
            }

            if(content.contains("</controlfield")) {
                content = content.replaceAll("</controlfield","</marc:controlfield");
            }
            if(content.contains("</datafield")) {
                content = content.replaceAll("</datafield","</marc:datafield");
            }
            if(content.contains("</subfield")) {
                content = content.replaceAll("</subfield","</marc:subfield");
            }
            if(content.contains("xmlns=")) {
                content = content.replaceAll("xmlns=", "xmlns:marc=");
            }
        }
        else if(content != null && !content.contains("marc:")) {
            content = content.replaceAll("collection","marc:collection");
            content = content.replaceAll("record","marc:record");
            content = content.replaceAll("controlfield","marc:controlfield");
            content = content.replaceAll("datafield","marc:datafield");
            content = content.replaceAll("subfield","marc:subfield");
            content = content.replaceAll("xmlns=","xmlns:marc=");
        }
        bib.setContent(content);
        BibMarcRecordProcessor bibMarcRecordProcessor = new BibMarcRecordProcessor();

        BibMarcRecords bibMarcRecords = bibMarcRecordProcessor.fromXML(content);

        BibMarcMapper bibMarcMapper = BibMarcMapper.getInstance();

        bibMarcMapper.extractFields(bibMarcRecords.getRecords().get(0), bib);

        return bib;
    }

}
