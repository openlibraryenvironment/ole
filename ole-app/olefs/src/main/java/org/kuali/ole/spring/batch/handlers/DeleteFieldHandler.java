package org.kuali.ole.spring.batch.handlers;

import org.apache.commons.collections.CollectionUtils;
import org.kuali.ole.batch.bo.OLEBatchProcessProfileBo;
import org.kuali.ole.batch.bo.OLEBatchProcessProfileDeleteField;


import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * Created by SheikS on 11/19/2015.
 */
public class DeleteFieldHandler extends BatchProcessProfileHandler {

    @Override
    public boolean isInterested(OLEBatchProcessProfileBo oleBatchProcessProfileBo) {
        return CollectionUtils.isNotEmpty(oleBatchProcessProfileBo.getOleBatchProcessProfileDeleteFieldsList());
    }

    @Override
    public Map process(Map parameterMap) {
        OLEBatchProcessProfileBo oleBatchProcessProfileBo = (OLEBatchProcessProfileBo) parameterMap.get("profile");
        List<OLEBatchProcessProfileDeleteField> oleBatchProcessProfileDeleteFieldsList = oleBatchProcessProfileBo.getOleBatchProcessProfileDeleteFieldsList();
        if(CollectionUtils.isNotEmpty(oleBatchProcessProfileDeleteFieldsList)) {
            for (Iterator<OLEBatchProcessProfileDeleteField> iterator = oleBatchProcessProfileDeleteFieldsList.iterator(); iterator.hasNext(); ) {
                OLEBatchProcessProfileDeleteField oleBatchProcessProfileDeleteField = iterator.next();
                //TODO : Need to Process
            }
        }
        return null;
    }
}

