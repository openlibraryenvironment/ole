package org.kuali.ole.executor;

import org.junit.Test;
import org.kuali.ole.BaseTestCase;
import org.kuali.ole.common.DocumentSearchConfig;
import org.kuali.ole.request.FullIndexRequest;
import org.springframework.beans.factory.annotation.Autowired;

import static org.junit.Assert.*;

/**
 * Created by sheiks on 28/10/16.
 */
public class BibFullIndexExecutorServiceTest extends BaseTestCase{

    @Autowired
    BibFullIndexExecutorService bibFullIndexExecutorService;

    @Test
    public void indexDocument() throws Exception {
        DocumentSearchConfig.getDocumentSearchConfig();
        Integer integer = bibFullIndexExecutorService.indexDocument(new FullIndexRequest());
        assertNotNull(integer);
        System.out.println(integer);
    }

}