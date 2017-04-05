package org.kuali.ole.repo;

import org.junit.Test;
import org.kuali.ole.BaseTestCase;
import org.kuali.ole.model.jpa.AccessLocation;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

import static org.junit.Assert.*;

/**
 * Created by sheiks on 10/11/16.
 */
public class AccessLocationRepositoryTest extends BaseTestCase{

    @Autowired
    AccessLocationRepository accessLocationRepository;

    @Test
    public void testFetch() {
        assertNotNull(accessLocationRepository);
        List<AccessLocation> all = accessLocationRepository.findAll();
        assertNotNull(all);
        System.out.println("Size : " + all);
    }

}