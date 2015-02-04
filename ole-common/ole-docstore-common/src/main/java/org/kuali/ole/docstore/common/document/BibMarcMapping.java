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

        BibMarcRecordProcessor bibMarcRecordProcessor = new BibMarcRecordProcessor();

        BibMarcRecords bibMarcRecords = bibMarcRecordProcessor.fromXML(bib.getContent());

        BibMarcMapper bibMarcMapper = BibMarcMapper.getInstance();

        bibMarcMapper.extractFields(bibMarcRecords.getRecords().get(0), bib);

        return bib;
    }

}
