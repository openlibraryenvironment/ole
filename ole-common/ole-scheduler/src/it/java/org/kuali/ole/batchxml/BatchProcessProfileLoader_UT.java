package org.kuali.ole.batchxml;

import org.junit.Test;

import org.kuali.ole.batchxml.BatchProcessProfileLoader;

public class BatchProcessProfileLoader_UT {
    @Test
    public void load() throws Exception {
        new BatchProcessProfileLoader().load();
    }
}
