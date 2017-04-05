package org.kuali.ole.util;

import org.junit.Test;
import org.kuali.ole.BaseTestCase;

import static org.junit.Assert.*;

/**
 * Created by sheiks on 28/10/16.
 */
public class SolrCommitSchedulerTest extends BaseTestCase{

    @Test
    public void testCommit() {
        new SolrCommitScheduler();
        try {
            Thread.sleep(100000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

}