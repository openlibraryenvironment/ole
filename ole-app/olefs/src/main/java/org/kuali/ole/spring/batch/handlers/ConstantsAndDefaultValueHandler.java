package org.kuali.ole.spring.batch.handlers;

import org.apache.commons.collections.CollectionUtils;
import org.kuali.ole.batch.bo.OLEBatchProcessProfileBo;
import org.kuali.ole.batch.bo.OLEBatchProcessProfileConstantsBo;


import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * Created by SheikS on 11/19/2015.
 */
public class ConstantsAndDefaultValueHandler extends BatchProcessProfileHandler {

    @Override
    public boolean isInterested(OLEBatchProcessProfileBo oleBatchProcessProfileBo) {
        return CollectionUtils.isNotEmpty(oleBatchProcessProfileBo.getOleBatchProcessProfileConstantsList());
    }

    @Override
    public Map process() {
        List<OLEBatchProcessProfileConstantsBo> oleBatchProcessProfileConstantsList = oleBatchProcessProfileBo.getOleBatchProcessProfileConstantsList();
        if(CollectionUtils.isNotEmpty(oleBatchProcessProfileConstantsList)) {
            for (Iterator<OLEBatchProcessProfileConstantsBo> iterator = oleBatchProcessProfileConstantsList.iterator(); iterator.hasNext(); ) {
                OLEBatchProcessProfileConstantsBo oleBatchProcessProfileConstantsBo = iterator.next();
                //TODO : Need to process
            }
        }
        return null;
    }
}

