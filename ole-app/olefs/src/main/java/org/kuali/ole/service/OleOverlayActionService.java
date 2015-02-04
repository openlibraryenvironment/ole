package org.kuali.ole.service;

import org.kuali.ole.docstore.common.document.content.bib.marc.BibMarcRecord;
import org.kuali.ole.ingest.pojo.OverlayOption;
import org.kuali.ole.pojo.OleOrderRecord;
import org.kuali.ole.pojo.edi.LineItemOrder;

import java.util.HashMap;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: vivekb
 * Date: 3/2/13
 * Time: 4:16 PM
 * To change this template use File | Settings | File Templates.
 */
public interface OleOverlayActionService {

    public String performOverlayLookupAction(String profileName, HashMap<String, Object> objects, String instanceUUID, OleOrderRecord oleOrderRecord)throws Exception;

    void updateRecordIncludingGPF(HashMap<String, String> uuids, BibMarcRecord oldBibliographicRecord, BibMarcRecord newBibliographicRecord, LineItemOrder lineItemOrder, List<String> gpfFieldList, List<OverlayOption> overlayOptionList, String profileName)throws Exception;

    void updateRecordExcludingGPF(HashMap<String, String> uuids, BibMarcRecord oldBibMarcRecord, BibMarcRecord newBibMarcRecord, LineItemOrder lineItemOrder, List<String> gpfFieldList, List<OverlayOption> overlayOptionList, String profileName)throws Exception;
}
