package org.kuali.ole.repo;

import org.junit.Test;
import org.kuali.ole.BaseTestCase;
import org.kuali.ole.model.jpa.ExtentOfOwnerShipTypeRecord;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

import static org.junit.Assert.assertNotNull;

/**
 * Created by sheiks on 10/11/16.
 */
public class ExtentOfOwnerShipTypeRecordRepositoryTest extends BaseTestCase{

    @Autowired
    ExtentOfOwnerShipTypeRecordRepository ExtentOfOwnerShipTypeRecordRepository;

    @Test
    public void testFetch() {
        assertNotNull(ExtentOfOwnerShipTypeRecordRepository);
        List<ExtentOfOwnerShipTypeRecord> all = ExtentOfOwnerShipTypeRecordRepository.findAll();
        assertNotNull(all);
        System.out.println("Size : " + all);
    }

}