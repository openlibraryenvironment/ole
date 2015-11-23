package org.kuali.ole.spring.batch.handlers;

import org.apache.commons.collections.CollectionUtils;
import org.kuali.ole.batch.bo.OLEBatchProcessProfileBo;
import org.kuali.ole.batch.bo.OLEBatchProcessProfileDataMappingOptionsBo;
import org.kuali.ole.batch.bo.OLEBatchProcessProfileMappingOptionsBo;


import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * Created by SheikS on 11/19/2015.
 */
public class DataMappingHandler extends BatchProcessProfileHandler {

    @Override
    public boolean isInterested(OLEBatchProcessProfileBo oleBatchProcessProfileBo) {
        return CollectionUtils.isNotEmpty(oleBatchProcessProfileBo.getDeleteBatchProcessBibDataMappingNewList());
    }

    @Override
    public Map process(Map parameterMap) {
        OLEBatchProcessProfileBo oleBatchProcessProfileBo = (OLEBatchProcessProfileBo) parameterMap.get("profile");
        List<OLEBatchProcessProfileMappingOptionsBo> oleBatchProcessProfileMappingOptionsList = oleBatchProcessProfileBo.getOleBatchProcessProfileMappingOptionsList();
        if(CollectionUtils.isNotEmpty(oleBatchProcessProfileMappingOptionsList)) {
            for (Iterator<OLEBatchProcessProfileMappingOptionsBo> iterator = oleBatchProcessProfileMappingOptionsList.iterator(); iterator.hasNext(); ) {
                OLEBatchProcessProfileMappingOptionsBo oleBatchProcessProfileMappingOptionsBo = iterator.next();
                List<OLEBatchProcessProfileDataMappingOptionsBo> oleBatchProcessProfileDataMappingOptionsBoList = oleBatchProcessProfileMappingOptionsBo.getOleBatchProcessProfileDataMappingOptionsBoList();
                if(CollectionUtils.isNotEmpty(oleBatchProcessProfileDataMappingOptionsBoList)) {
                    for (Iterator<OLEBatchProcessProfileDataMappingOptionsBo> oleBatchProcessProfileDataMappingOptionsBoIterator = oleBatchProcessProfileDataMappingOptionsBoList.iterator(); oleBatchProcessProfileDataMappingOptionsBoIterator.hasNext(); ) {
                        OLEBatchProcessProfileDataMappingOptionsBo oleBatchProcessProfileDataMappingOptionsBo = oleBatchProcessProfileDataMappingOptionsBoIterator.next();
                        //TODO : Need to process.
                    }
                }
            }
        }
        return null;
    }
}

