package org.kuali.ole.spring.batch.handlers;

import org.apache.commons.collections.CollectionUtils;
import org.kuali.ole.batch.bo.OLEBatchGloballyProtectedField;
import org.kuali.ole.batch.bo.OLEBatchProcessProfileBo;


import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * Created by SheikS on 11/19/2015.
 */
public class GloballyProtectedFieldHandler extends BatchProcessProfileHandler {

    @Override
    public boolean isInterested(OLEBatchProcessProfileBo oleBatchProcessProfileBo) {
        return CollectionUtils.isNotEmpty(oleBatchProcessProfileBo.getOleBatchGloballyProtectedFieldList());
    }

    @Override
    public Map process(Map parameterMap) {
        OLEBatchProcessProfileBo oleBatchProcessProfileBo = (OLEBatchProcessProfileBo) parameterMap.get("profile");
        List<OLEBatchGloballyProtectedField> oleBatchGloballyProtectedFieldList = oleBatchProcessProfileBo.getOleBatchGloballyProtectedFieldList();
        if(CollectionUtils.isNotEmpty(oleBatchGloballyProtectedFieldList)) {
            for (Iterator<OLEBatchGloballyProtectedField> iterator = oleBatchGloballyProtectedFieldList.iterator(); iterator.hasNext(); ) {
                OLEBatchGloballyProtectedField oleBatchGloballyProtectedField = iterator.next();
                // TODO : Need to process.
            }
        }
        return null;
    }
}

