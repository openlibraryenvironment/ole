package org.kuali.ole.util;

import org.junit.Test;
import org.kuali.ole.BaseTestCase;
import org.kuali.ole.repo.SearchFacetPageRepository;
import org.springframework.data.jpa.repository.JpaRepository;

import static org.junit.Assert.*;

/**
 * Created by sheiks on 28/10/16.
 */
public class HelperUtilTest extends BaseTestCase{
    @Test
    public void getRepository() throws Exception {

        JpaRepository repository = HelperUtil.getRepository(SearchFacetPageRepository.class);
        assertTrue(repository instanceof SearchFacetPageRepository);

    }

}