package org.kuali.ole.dsng.util;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.codehaus.jackson.map.ObjectMapper;
import org.junit.Test;
import org.kuali.ole.DocstoreTestCaseBase;
import org.kuali.ole.docstore.engine.service.storage.rdbms.pojo.BibRecord;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

import static org.junit.Assert.*;

/**
 * Created by SheikS on 12/3/2015.
 */
public class OleDsNGSearchUtil_IT extends DocstoreTestCaseBase {

    @Autowired
    OleDsNGSearchUtil oleDsNGSearchUtil;


    @Test
    public void testRetrieveBibBasedOnMatchPoints() throws Exception {
        BibRecord bibRecord = oleDsNGSearchUtil.retrieveBibBasedOnMatchPoints("mdf_035a:\"Search Tag\"");
        assertNotNull(bibRecord);

        ObjectMapper objectMapper = new ObjectMapper();
        String retrievedObjectString = objectMapper.defaultPrettyPrintingWriter().writeValueAsString(bibRecord);
        assertTrue(StringUtils.isNotBlank(retrievedObjectString));
        System.out.println(retrievedObjectString);
    }
}