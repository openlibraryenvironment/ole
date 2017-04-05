package org.kuali.ole.repo;

import org.junit.Test;
import org.kuali.ole.BaseTestCase;
import org.kuali.ole.model.jpa.AuthenticationTypeRecord;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

import static org.junit.Assert.assertNotNull;

/**
 * Created by sheiks on 10/11/16.
 */
public class AuthenticationTypeRecordRepositoryTest extends BaseTestCase{

    @Autowired
    AuthenticationTypeRecordRepository AuthenticationTypeRecordRepository;

    @Test
    public void testFetch() {
        assertNotNull(AuthenticationTypeRecordRepository);
        List<AuthenticationTypeRecord> all = AuthenticationTypeRecordRepository.findAll();
        assertNotNull(all);
        System.out.println("Size : " + all);
    }

}