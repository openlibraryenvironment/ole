package org.kuali.ole.repo;

import org.junit.Test;
import org.kuali.ole.BaseTestCase;
import org.kuali.ole.model.jpa.ReceiptStatusRecord;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

import static org.junit.Assert.assertNotNull;

/**
 * Created by sheiks on 10/11/16.
 */
public class ReceiptStatusRecordRepositoryTest extends BaseTestCase{

    @Autowired
    ReceiptStatusRecordRepository ReceiptStatusRecordRepository;

    @Test
    public void testFetch() {
        assertNotNull(ReceiptStatusRecordRepository);
        List<ReceiptStatusRecord> all = ReceiptStatusRecordRepository.findAll();
        assertNotNull(all);
        System.out.println("Size : " + all);
    }

}