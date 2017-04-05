package org.kuali.ole.dao.impl;

import com.google.common.collect.Lists;
import org.aspectj.weaver.ast.Call;
import org.junit.Test;
import org.kuali.ole.BaseTestCase;
import org.kuali.ole.OleStopWatch;
import org.kuali.ole.dao.OleMemorizeService;
import org.kuali.ole.model.jpa.CallNumberTypeRecord;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.*;

import static org.junit.Assert.*;

/**
 * Created by sheiks on 10/11/16.
 */
public class OleMemorizeServiceImplTest extends BaseTestCase{

    @Autowired
    OleMemorizeService memorizeService;

    @Test
    public void getCallNumberTypeRecordById() throws Exception {
        assertNotNull(memorizeService);
        OleStopWatch oleStopWatch = new OleStopWatch();

        ThreadPoolExecutor threadPoolExecutor = (ThreadPoolExecutor) Executors.newFixedThreadPool(75);
        List<Callable<Integer>> callables = new ArrayList<>();
        for(int index = 0; index < 10000; index++) {
            callables.add(new Callable<Integer>() {
                @Override
                public Integer call() throws Exception {
                    CallNumberTypeRecord callNumberTypeRecordById = memorizeService.getCallNumberTypeRecordById(1L);
                    return 0;
                }
            });
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
        }
        oleStopWatch.log("Total time taken : ");
    }

    @Test
    public void testItemTypeAndStatusFetch() throws Exception {
        assertNotNull(memorizeService);
        for(int index = 0; index < 10000; index++) {
            memorizeService.getItemStatusById("1");
            memorizeService.getItemTypeById("1");
        }
    }

}