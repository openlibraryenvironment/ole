package org.kuali.ole.spring.batch.handlers;

import org.kuali.ole.batch.bo.OLEBatchProcessProfileBo;


import java.util.Map;

/**
 * Created by SheikS on 11/19/2015.
 */
public class ImportBibliographicRecordHandler extends BatchProcessProfileHandler {

    @Override
    public boolean isInterested(OLEBatchProcessProfileBo oleBatchProcessProfileBo) {
        return true;
    }

    @Override
    public Map process(Map parameterMap) {
        OLEBatchProcessProfileBo oleBatchProcessProfileBo = (OLEBatchProcessProfileBo) parameterMap.get("profile");
        //TODO : Need to process
        return null;
    }
}

