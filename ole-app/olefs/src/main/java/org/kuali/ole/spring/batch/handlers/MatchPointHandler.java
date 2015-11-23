package org.kuali.ole.spring.batch.handlers;

import org.apache.commons.collections.CollectionUtils;
import org.kuali.ole.batch.bo.OLEBatchProcessProfileBo;
import org.kuali.ole.batch.bo.OLEBatchProcessProfileMatchPoint;
import org.kuali.ole.batch.util.BatchBibImportUtil;


import java.util.*;

/**
 * Created by SheikS on 11/19/2015.
 */
public class MatchPointHandler extends BatchProcessProfileHandler {

    @Override
    public boolean isInterested(OLEBatchProcessProfileBo oleBatchProcessProfileBo) {
        return CollectionUtils.isNotEmpty(oleBatchProcessProfileBo.getOleBatchProcessProfileMatchPointList());
    }

    @Override
    public Map process(Map parameterMap) {
        OLEBatchProcessProfileBo oleBatchProcessProfileBo = (OLEBatchProcessProfileBo) parameterMap.get("profile");
        List<OLEBatchProcessProfileMatchPoint> oleBatchProcessProfileMatchPointList = oleBatchProcessProfileBo.getOleBatchProcessProfileMatchPointList();
        if(CollectionUtils.isNotEmpty(oleBatchProcessProfileMatchPointList)) {
            for (Iterator<OLEBatchProcessProfileMatchPoint> iterator = oleBatchProcessProfileMatchPointList.iterator(); iterator.hasNext(); ) {
                OLEBatchProcessProfileMatchPoint oleBatchProcessProfileMatchPoint = iterator.next();
                // TODO : Need to process.
            }
        }
        return null;
    }
}

