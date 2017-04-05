package org.kuali.ole.repo.jpa;

import org.junit.Test;
import org.kuali.ole.BaseTestCase;
import org.kuali.ole.model.jpa.ItemRecord;

import static org.junit.Assert.*;

/**
 * Created by sheiks on 07/12/16.
 */
public class ItemRecordRepositoryTest extends BaseTestCase {

    @Test
    public void testItemFetch() {
        assertNotNull(itemRecordRepository);
        ItemRecord one = itemRecordRepository.getOne(1);
        assertNotNull(one);
    }

}