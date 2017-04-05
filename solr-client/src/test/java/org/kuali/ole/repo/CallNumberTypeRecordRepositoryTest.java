package org.kuali.ole.repo;

import org.junit.Test;
import org.kuali.ole.BaseTestCase;
import org.kuali.ole.model.jpa.CallNumberTypeRecord;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

import static org.junit.Assert.*;

/**
 * Created by sheiks on 10/11/16.
 */
public class CallNumberTypeRecordRepositoryTest extends BaseTestCase{

    @Autowired
    CallNumberTypeRecordRepository callNumberTypeRecordRepository;

    @Test
    public void testCallNumberTypeRecordFetch() {
        assertNotNull(callNumberTypeRecordRepository);
        List<CallNumberTypeRecord> all = callNumberTypeRecordRepository.findAll();
        assertNotNull(all);
    }

}