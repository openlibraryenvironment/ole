package org.kuali.ole.spring.batch.handlers;

import org.apache.commons.collections.CollectionUtils;
import org.kuali.ole.batch.bo.OLEBatchProcessProfileBo;
import org.kuali.ole.batch.bo.OLEBatchProcessProfileRenameField;
import org.kuali.ole.docstore.common.document.content.bib.marc.Collection;


import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * Created by SheikS on 11/19/2015.
 */
public class RenameFieldHandler extends BatchProcessProfileHandler {

    @Override
    public boolean isInterested(OLEBatchProcessProfileBo oleBatchProcessProfileBo) {
        return CollectionUtils.isNotEmpty(oleBatchProcessProfileBo.getOleBatchProcessProfileRenameFieldsList());
    }

    @Override
    public Map process() {
        List<OLEBatchProcessProfileRenameField> oleBatchProcessProfileRenameFieldsList = oleBatchProcessProfileBo.getOleBatchProcessProfileRenameFieldsList();
        if(CollectionUtils.isNotEmpty(oleBatchProcessProfileRenameFieldsList)) {
            for (Iterator<OLEBatchProcessProfileRenameField> iterator = oleBatchProcessProfileRenameFieldsList.iterator(); iterator.hasNext(); ) {
                OLEBatchProcessProfileRenameField oleBatchProcessProfileRenameField = iterator.next();
                // TODO : Need to process.
            }
        }
        return null;
    }
}

