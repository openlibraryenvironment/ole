package org.kuali.ole.repo;

import com.google.common.collect.Lists;
import org.junit.Test;
import org.kuali.ole.BaseTestCase;
import org.kuali.ole.OleStopWatch;
import org.kuali.ole.model.jpa.BibRecord;
import org.kuali.ole.model.jpa.CallNumberTypeRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.util.StopWatch;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.*;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

/**
 * Created by sNheiks on 27/10/16.
 */
public class BibRecordRepositoryTest extends BaseTestCase{
    Logger logger = LoggerFactory.getLogger(BibRecordRepositoryTest.class);

    @Test
    public void testFetchCount() {
        assertNotNull(bibRecordRepository);
        long count = bibRecordRepository.count();
        assertTrue(count != 0);
        System.out.println("total bib count : " + count);
    }

    @Test
    public void fetchBib() {
        assertNotNull(bibRecordRepository);
        Page<BibRecord> all = bibRecordRepository.findAll(new PageRequest(0, 1));
        assertNotNull(all);
        System.out.println("total bib count : " + all.getSize());
    }

    @Test
    public void loadTest() {
        try {
            OleStopWatch oleStopWatch = new OleStopWatch();
            Integer totalDocCount = 100000;
            Integer numThreads = 75;
            Integer docsPerThread = 1000;
            Integer commitIndexesInterval = 100000;
            ThreadPoolExecutor threadPoolExecutor = (ThreadPoolExecutor) Executors.newFixedThreadPool(numThreads);
            int quotient = totalDocCount / (docsPerThread);
            int remainder = totalDocCount % (docsPerThread);
            Integer loopCount = remainder == 0 ? quotient : quotient + 1;
            logger.info("Loop Count Value : " + loopCount);
            logger.info("Commit Indexes Interval : " + commitIndexesInterval);

            Integer callableCountByCommitInterval = commitIndexesInterval / (docsPerThread);
            if (callableCountByCommitInterval == 0) {
                callableCountByCommitInterval = 1;
            }
            callableCountByCommitInterval = 1000;
            logger.info("Number of callables to execute to commit indexes : " + callableCountByCommitInterval);

            StopWatch stopWatch = new StopWatch();
            stopWatch.start();

            List<Callable<Integer>> callables = new ArrayList<>();
            for (int pageNum = 0; pageNum < loopCount; pageNum++) {
                callables.add(new LoadTestCallable(pageNum, docsPerThread));
            }

            List<List<Callable<Integer>>> partitions = Lists.partition(new ArrayList<Callable<Integer>>(callables), 1000);

            for (Iterator<List<Callable<Integer>>> iterator = partitions.iterator(); iterator.hasNext(); ) {
                List<Callable<Integer>> callableList = iterator.next();
                List<Future<Integer>> futures = threadPoolExecutor.invokeAll(callableList);
                futures
                        .stream()
                        .map(future -> {
                            try {
                                return future.get();
                            } catch (Exception e) {
                                throw new IllegalStateException(e);
                            }
                        });

                for (Iterator<Future<Integer>> iterator1 = futures.iterator(); iterator1.hasNext(); ) {
                    Future future = iterator1.next();
                    try {
                        future.get();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    } catch (ExecutionException e) {
                        e.printStackTrace();
                    }
                }
            }
            oleStopWatch.log("Total time taken : ");
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }


}