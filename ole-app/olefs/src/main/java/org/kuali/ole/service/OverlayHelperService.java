package org.kuali.ole.service;

import org.kuali.ole.docstore.common.document.content.bib.marc.BibMarcRecord;
import org.kuali.ole.docstore.model.xmlpojo.ingest.Response;
import org.kuali.ole.docstore.common.document.content.instance.InstanceCollection;
import org.kuali.ole.ingest.pojo.OverlayOption;

import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: premkb
 * Date: 12/2/12
 * Time: 8:54 PM
 * To change this template use File | Settings | File Templates.
 */
public interface OverlayHelperService {


    public String updateInstanceToDocstore(String instanceUUID, InstanceCollection oldInstanceCollection, InstanceCollection newInstanceCollection)throws Exception;

    public String getUUID(Response response, String docType) throws Exception;

    public BibMarcRecord updateBibMarcRecordExcludingGPF(BibMarcRecord oldBibMarcRecord, BibMarcRecord newBibMarcRecord, List<String> gpfFieldList, List<OverlayOption> overlayOptionList) throws Exception;

    public BibMarcRecord updateBibMarcRecordIncludingGPF(BibMarcRecord oldBibMarcRecord, BibMarcRecord newBibMarcRecord, List<String> gpfFieldList, List<OverlayOption> overlayOptionList) throws Exception;


}
