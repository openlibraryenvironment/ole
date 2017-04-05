package org.kuali.ole.service;

import org.kuali.ole.executor.BibPartialIndexExecutorService;
import org.kuali.ole.repo.BibRecordRepository;
import org.kuali.ole.response.PartialIndexStatus;
import org.kuali.ole.util.HelperUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Date;
import java.util.List;

/**
 * Created by sheiks on 05/04/17.
 */
@Service
public class PartialIndexService {

    @Autowired
    BibPartialIndexExecutorService bibPartialIndexExecutorService;

    @Autowired
    BibRecordRepository bibRecordRepository;

    private PartialIndexStatus partialIndexStatus = PartialIndexStatus.getInstance();

    public PartialIndexStatus getPartialIndexStatus(@RequestParam("fromDate") Date date, @RequestParam("docPerThread") Integer docPerThread, @RequestParam("numberOfThreads") Integer numberOfThreads) {
        this.partialIndexStatus.resetStatus();
        this.partialIndexStatus.setType("indexByDate");
        this.partialIndexStatus.setDocsPerThread(docPerThread);
        this.partialIndexStatus.setNoOfDbThreads(numberOfThreads);
        Date fromDate = HelperUtil.getFromDate(date);
        Date toDate = HelperUtil.getToDate(new Date());
        this.partialIndexStatus.setFromDate(fromDate);
        this.partialIndexStatus.setToDate(toDate);
        List<Integer> bibIdByDate = bibRecordRepository.getBibIdByDate(fromDate, toDate);
        bibPartialIndexExecutorService.indexDocument(bibIdByDate,docPerThread, numberOfThreads);
        return this.partialIndexStatus;
    }

}
