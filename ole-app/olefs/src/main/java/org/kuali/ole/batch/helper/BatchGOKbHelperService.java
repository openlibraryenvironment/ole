package org.kuali.ole.batch.helper;

import org.kuali.ole.batch.bo.*;
import org.kuali.ole.batch.util.BatchBibImportUtil;
import org.kuali.ole.docstore.common.document.DocstoreDocument;
import org.kuali.ole.docstore.common.document.Holdings;
import org.kuali.ole.docstore.common.document.HoldingsTree;
import org.kuali.ole.docstore.common.document.content.bib.marc.BibMarcRecord;
import org.kuali.ole.docstore.common.document.content.bib.marc.DataField;
import org.kuali.ole.docstore.common.search.SearchParams;
import org.kuali.ole.docstore.model.enums.DocType;

import java.util.*;

/**
 * Created by sambasivam on 4/12/14.
 */
public class BatchGOKbHelperService extends BatchBibImportHelper {


    protected void applyBibDataMappingOverlay(BibMarcRecord bibRecord, OLEBatchProcessProfileBo profile) {

        List<OLEBatchProcessBibDataMappingOverlay> oleBatchProcessBibDataMappingOverlays = profile.getOleBatchProcessBibDataMappingOverlayList();

        Iterator<DataField> iterator = bibRecord.getDataFields().iterator();
        while (iterator.hasNext()) {

            DataField dataField = iterator.next();

            OLEBatchProcessBibDataMappingOverlay matchedBibDataMappingOverlayField = getMatchedBibDataMappingOverlayField(dataField.getTag(), oleBatchProcessBibDataMappingOverlays);
            if (matchedBibDataMappingOverlayField == null) {
                iterator.remove();
            }
        }


        List<String> addFieldsInOverlay = getAddDataFieldTags(profile.getOleBatchProcessBibDataMappingOverlayList());

        for (String tag : addFieldsInOverlay) {
            OLEBatchGloballyProtectedField oleBatchGloballyProtectedField = new OLEBatchGloballyProtectedField();
            oleBatchGloballyProtectedField.setTag(tag);
            profile.getOleBatchGloballyProtectedFieldList().add(oleBatchGloballyProtectedField);
        }

    }

    private List<String> getAddDataFieldTags(List<OLEBatchProcessBibDataMappingOverlay> oleBatchProcessBibDataMappingOverlays) {
        List<String> addFieldsInOverlay = new ArrayList<>();

        for(OLEBatchProcessBibDataMappingOverlay oleBatchProcessBibDataMappingOverlay : oleBatchProcessBibDataMappingOverlays) {
            if(oleBatchProcessBibDataMappingOverlay.getAddOrReplace().equalsIgnoreCase("add")) {
                addFieldsInOverlay.add(oleBatchProcessBibDataMappingOverlay.getTag());
            }
        }
        return addFieldsInOverlay;
    }

    protected void applyBibDataMapping(BibMarcRecord bibRecord, OLEBatchProcessProfileBo profile) {

        Iterator<DataField> iterator =  bibRecord.getDataFields().iterator();

        List<String> tags = new ArrayList<>();

        for(OLEBatchProcessBibDataMappingNew oleBatchProcessBibDataMappingNew : profile.getOleBatchProcessBibDataMappingNewList()) {
            tags.add(oleBatchProcessBibDataMappingNew.getTag());
        }

        while(iterator.hasNext()) {

            DataField dataField = iterator.next();
            if(!tags.contains(dataField.getTag())) {
                iterator.remove();
            }
        }

    }

    protected void addSearchCondition(String fieldValue, SearchParams searchParams, OLEBatchProcessProfileMatchPoint matchPoint) {
        searchParams.getSearchConditions().add(searchParams.buildSearchCondition("phrase", searchParams.buildSearchField(DocType.BIB.getCode(), BatchBibImportUtil.getDataFieldWithout$(matchPoint.getMatchPoint()), fieldValue), "AND"));
    }

    private OLEBatchProcessBibDataMappingOverlay getMatchedBibDataMappingOverlayField(String tag, List<OLEBatchProcessBibDataMappingOverlay> oleBatchProcessBibDataMappingOverlayList) {

        for (OLEBatchProcessBibDataMappingOverlay batchProcessBibDataMappingOverlay : oleBatchProcessBibDataMappingOverlayList) {
            if(batchProcessBibDataMappingOverlay.getTag().equals(tag)) {
                return batchProcessBibDataMappingOverlay;
            }
        }

        return null;

    }

    protected List<HoldingsTree> processMatchedEHoldings(BibMarcRecord bibRecord, OLEBatchProcessProfileBo profile, OLEBatchBibImportDataObjects oleBatchBibImportDataObjects, List<HoldingsTree> holdingsTrees, MatchingProfile matchingProfile, Holdings matchedHoldings, DataField eHoldingsDataField) {

        holdingsTrees.addAll(buildEHoldingsTreesForBib(bibRecord, profile, false));
        // Setting Operation type as create
        setHoldingsTreeOperations(holdingsTrees, DocstoreDocument.OperationType.CREATE, DocstoreDocument.OperationType.CREATE);


        return holdingsTrees;
    }


}
