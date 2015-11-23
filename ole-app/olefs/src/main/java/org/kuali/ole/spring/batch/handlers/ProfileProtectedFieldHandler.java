package org.kuali.ole.spring.batch.handlers;

import org.apache.commons.collections.CollectionUtils;
import org.kuali.ole.batch.bo.OLEBatchProcessProfileBo;
import org.kuali.ole.batch.bo.OLEBatchProcessProfileProtectedField;

import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * Created by SheikS on 11/19/2015.
 */
public class ProfileProtectedFieldHandler extends BatchProcessProfileHandler {

    @Override
    public boolean isInterested(OLEBatchProcessProfileBo oleBatchProcessProfileBo) {
        return CollectionUtils.isNotEmpty(oleBatchProcessProfileBo.getOleBatchProcessProfileProtectedFieldList());
    }

    @Override
    public Map process(Map parameterMap) {
        OLEBatchProcessProfileBo oleBatchProcessProfileBo = (OLEBatchProcessProfileBo) parameterMap.get("profile");
        List<OLEBatchProcessProfileProtectedField> oleBatchProcessProfileProtectedFieldList = oleBatchProcessProfileBo.getOleBatchProcessProfileProtectedFieldList();
        if(CollectionUtils.isNotEmpty(oleBatchProcessProfileProtectedFieldList)) {
            for (Iterator<OLEBatchProcessProfileProtectedField> iterator = oleBatchProcessProfileProtectedFieldList.iterator(); iterator.hasNext(); ) {
                OLEBatchProcessProfileProtectedField oleBatchProcessProfileProtectedField = iterator.next();
                // TODO : Need to Process.
            }
        }
        return null;
    }
}

