package org.kuali.ole.repo;

import org.kuali.ole.callable.BibFullIndexCallable;
import org.kuali.ole.model.jpa.BibRecord;
import org.kuali.ole.util.HelperUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.util.StopWatch;

import java.util.concurrent.Callable;

/**
 * Created by sheiks on 10/11/16.
 */
public class LoadTestCallable implements Callable {

    Logger logger = LoggerFactory.getLogger(BibFullIndexCallable.class);
    private final int pageNum;
    private final int docsPerPage;

    public LoadTestCallable(int pageNum, int docsPerPage) {
        this.pageNum = pageNum;
        this.docsPerPage = docsPerPage;
    }

    @Override
    public Object call() throws Exception {
        StopWatch mainStopWatch = new StopWatch();
        mainStopWatch.start();
        Page<BibRecord> pageableBibRecords = HelperUtil.getRepository(BibRecordRepository.class).findAll(new PageRequest(pageNum, docsPerPage));
        mainStopWatch.stop();
        logger.info("Page Num : " + pageNum + "    Num Bibs Fetched : " + pageableBibRecords.getNumberOfElements() + "   and time taken to fetch : " + mainStopWatch.getTotalTimeSeconds() + " Secs");
        return 0;
    }
}
