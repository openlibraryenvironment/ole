package org.kuali.ole.repo;

import org.junit.Test;
import org.kuali.ole.BaseTestCase;
import org.kuali.ole.model.jpa.StatisticalSearchRecord;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

import static org.junit.Assert.assertNotNull;

/**
 * Created by sheiks on 10/11/16.
 */
public class StatisticalSearchRecordRepositoryTest extends BaseTestCase{

    @Autowired
    StatisticalSearchRecordRepository StatisticalSearchRecordRepository;

    @Test
    public void testFetch() {
        assertNotNull(StatisticalSearchRecordRepository);
        List<StatisticalSearchRecord> all = StatisticalSearchRecordRepository.findAll();
        assertNotNull(all);
        System.out.println("Size : " + all);
    }

}