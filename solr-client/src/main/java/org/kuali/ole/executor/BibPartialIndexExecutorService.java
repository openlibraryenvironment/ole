package org.kuali.ole.executor;

import com.google.common.collect.Lists;
import org.kuali.ole.callable.BibPartialIndexCallable;
import org.kuali.ole.common.DocumentSearchConfig;
import org.kuali.ole.response.PartialIndexStatus;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.util.StopWatch;

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.*;

/**
 * Created by sheiks on 27/03/17.
 */
@Service
public class BibPartialIndexExecutorService extends BibIndexExecutorService{

    @Async
    public Integer indexDocument(List<Integer> bibIds, int chunkSize, int numThreads) {

        StopWatch mainStopWatch = new StopWatch();
        mainStopWatch.start();

        DocumentSearchConfig documentSearchConfig = DocumentSearchConfig.getDocumentSearchConfig();
        List<Future<Integer>> futures = new ArrayList<>();

        List<List<Integer>> partition = Lists.partition(bibIds, chunkSize);

        Integer totalBibsProcessed = 0;
        try {
            ThreadPoolExecutor threadPoolExecutor = (ThreadPoolExecutor) Executors.newFixedThreadPool(numThreads);

            for (int index = 0; index < partition.size(); index++) {

                Callable callable = new BibPartialIndexCallable(solrServerProtocol + solrUrl, solrCore, partition.get(index),
                        getBibMarcRecordProcessor(),
                        documentSearchConfig.FIELDS_TO_TAGS_2_INCLUDE_MAP,
                        documentSearchConfig.FIELDS_TO_TAGS_2_EXCLUDE_MAP,
                        producerTemplate,solrTemplate, oleMemorizeService);
                Future submit = threadPoolExecutor.submit(callable);
                futures.add(submit);
            }

            int numOfBibsProcessed = 0;
            for (Iterator<Future<Integer>> iterator = futures.iterator(); iterator.hasNext(); ) {
                Future future = iterator.next();
                try {
                    Integer entitiesCount = (Integer) future.get();
                    numOfBibsProcessed += entitiesCount;
                    totalBibsProcessed += entitiesCount;
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (ExecutionException e) {
                    e.printStackTrace();
                }
            }

            logger.info("Num of Bibs Processed and indexed by partial index to core " + solrCore + " on commit interval : " + numOfBibsProcessed);
            logger.info("Total Num of Bibs Processed and indexed  by partial index to core " + solrCore + " : " + totalBibsProcessed);

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            PartialIndexStatus instance = PartialIndexStatus.getInstance();
            instance.setEndTime(new Date());
            instance.setRunning(false);
        }

        mainStopWatch.stop();
        logger.info("Total time taken:" + mainStopWatch.getTotalTimeSeconds() + " secs");

        return totalBibsProcessed;

    }
}
