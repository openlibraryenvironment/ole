package org.kuali.ole.spring.batch.handlers;

import org.kuali.ole.batch.bo.OLEBatchProcessProfileBo;
import org.apache.log4j.Logger;

import java.util.Map;

/**
 * Created by SheikS on 11/19/2015.
 */
public abstract class BatchProcessProfileHandler {
    private final static Logger LOG = Logger.getLogger(BatchProcessProfileHandler.class.getName());

    protected OLEBatchProcessProfileBo oleBatchProcessProfileBo;

    public abstract boolean isInterested(OLEBatchProcessProfileBo oleBatchProcessProfileBo);

    public abstract Map process();

    public OLEBatchProcessProfileBo getOleBatchProcessProfileBo() {
        return oleBatchProcessProfileBo;
    }

    public void setOleBatchProcessProfileBo(OLEBatchProcessProfileBo oleBatchProcessProfileBo) {
        this.oleBatchProcessProfileBo = oleBatchProcessProfileBo;
    }
}
