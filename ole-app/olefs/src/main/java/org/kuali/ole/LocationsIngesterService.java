package org.kuali.ole;

import org.kuali.ole.describe.defaultload.LoadDefaultLocationsBean;
import org.kuali.ole.ingest.FileUtil;
import org.kuali.ole.service.OleLocationConverterService;
import org.kuali.rice.core.api.impex.xml.XmlDocCollection;
import org.kuali.rice.core.api.impex.xml.XmlIngesterService;

import java.io.File;
import java.util.*;

/**
 * Created by pvsubrah on 12/5/13.
 */
public class LocationsIngesterService implements XmlIngesterService {
    OleLocationConverterService oleLocationConverterService;

    @Override
    public Collection<XmlDocCollection> ingest(List<XmlDocCollection> xmlDocCollections) throws Exception {
        boolean failedRecords = false;
        for (Iterator<XmlDocCollection> iterator = xmlDocCollections.iterator(); iterator.hasNext(); ) {
            XmlDocCollection xmlDocCollection = iterator.next();
            File file = xmlDocCollection.getFile();
            String fileContent = new FileUtil().readFile(file);

            oleLocationConverterService.persistLocationFromFileContent(fileContent, file.getName());
            failedRecords = oleLocationConverterService.getRejectLocationList().size() > 0;

        }
        if (!failedRecords) {
            return new LinkedList<>();
        }
        return xmlDocCollections;
    }


//    TODO:Refactor to make use the principal id.
    @Override
    public Collection<XmlDocCollection> ingest(List<XmlDocCollection> xmlDocCollections, String principalId) throws Exception {
        return ingest(xmlDocCollections);
    }

    public OleLocationConverterService getOleLocationConverterService() {
        return oleLocationConverterService;
    }

    public void setOleLocationConverterService(OleLocationConverterService oleLocationConverterService) {
        this.oleLocationConverterService = oleLocationConverterService;
    }
}
