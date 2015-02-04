package org.kuali.ole;

import org.kuali.ole.deliver.bo.OlePatronDocument;
import org.kuali.ole.deliver.bo.OlePatronIngestSummaryRecord;
import org.kuali.ole.ingest.FileUtil;
import org.kuali.ole.service.OlePatronConverterService;
import org.kuali.rice.core.api.impex.xml.XmlDocCollection;
import org.kuali.rice.core.api.impex.xml.XmlIngesterService;

import java.io.File;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by pvsubrah on 12/9/13.
 */
public class PatronsIngesterService implements XmlIngesterService {
    private OlePatronConverterService olePatronConverterService;

    @Override
    public Collection<XmlDocCollection> ingest(List<XmlDocCollection> xmlDocCollections) throws Exception {
        boolean failedRecords = false;
        for (Iterator<XmlDocCollection> iterator = xmlDocCollections.iterator(); iterator.hasNext(); ) {
            XmlDocCollection xmlDocCollection = iterator.next();
            File file = xmlDocCollection.getFile();
            String fileContent = new FileUtil().readFile(file);

            OlePatronIngestSummaryRecord olePatronIngestSummaryRecord = new OlePatronIngestSummaryRecord();
            List<OlePatronDocument> olePatronDocuments = olePatronConverterService.persistPatronFromFileContent(fileContent,
                    true,
                    file.getName(),
                    olePatronIngestSummaryRecord, null, "");
            failedRecords = !(olePatronIngestSummaryRecord.getPatronRejectCount() > 0);

        }
        if (!failedRecords) {
            return new LinkedList<>();
        }
        return xmlDocCollections;
    }

    @Override
    public Collection<XmlDocCollection> ingest(List<XmlDocCollection> xmlDocCollections, String principalId) throws Exception {
        return null;
    }

    public void setOlePatronConverterService(OlePatronConverterService olePatronConverterService) {
        this.olePatronConverterService = olePatronConverterService;
    }

    public OlePatronConverterService getOlePatronConverterService() {
        return olePatronConverterService;
    }
}
