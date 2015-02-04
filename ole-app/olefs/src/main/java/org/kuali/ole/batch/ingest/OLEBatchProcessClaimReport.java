package org.kuali.ole.batch.ingest;

import org.kuali.ole.OLEConstants;
import org.kuali.ole.batch.impl.AbstractBatchProcess;
import org.kuali.ole.batch.service.OLEClaimNoticeService;
import org.kuali.ole.sys.context.SpringContext;
import org.kuali.rice.core.api.resourceloader.GlobalResourceLoader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created with IntelliJ IDEA.
 * User: vivekb
 * Date: 12/30/13
 * Time: 4:58 PM
 * To change this template use File | Settings | File Templates.
 */
public class OLEBatchProcessClaimReport extends AbstractBatchProcess {
    private static final Logger LOG = LoggerFactory.getLogger(OLEBatchProcessClaimReport.class);


    @Override
    protected void prepareForRead() throws Exception {
       LOG.info("Inside prepareForRead of OLEBatchProcessClaimReport ");
    }

    @Override
    protected void prepareForWrite() throws Exception {
        LOG.info("Inside prepareForWrite of OLEBatchProcessClaimReport ");
    }

    @Override
    protected void getNextBatch() throws Exception {
        LOG.info("Inside getNextBatch of OLEBatchProcessClaimReport ");
    }

    @Override
    protected void processBatch() throws Exception {
        LOG.info("Inside processBatch of OLEBatchProcessClaimReport ");
        OLEClaimNoticeService oleClaimNoticeService  = GlobalResourceLoader.getService("oleClaimNoticeService");
        oleClaimNoticeService.generateClaimReports(job);

    }
}
