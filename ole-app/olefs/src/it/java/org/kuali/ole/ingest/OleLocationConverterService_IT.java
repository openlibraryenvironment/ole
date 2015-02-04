package org.kuali.ole.ingest;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.kuali.ole.SpringBaseTestCase;
import org.kuali.ole.ingest.pojo.OleLocationGroup;
import org.kuali.ole.ingest.pojo.OleLocationIngest;
import org.kuali.ole.service.OleLocationConverterService;
import org.kuali.rice.core.api.resourceloader.GlobalResourceLoader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.transaction.TransactionConfiguration;
import org.springframework.transaction.annotation.Transactional;

import java.io.File;
import java.net.URL;
import java.util.Iterator;
import java.util.List;

import static junit.framework.Assert.assertNotNull;
import static junit.framework.Assert.assertTrue;

/**
 * Created by IntelliJ IDEA.
 * User: Gopalp
 * Date: 5/30/12
 * Time: 12:39 PM
 * To change this template use File | Settings | File Templates.
 */

public class OleLocationConverterService_IT extends SpringBaseTestCase {
   private OleLocationConverterService oleLocationConverterService;
    @Autowired
    private ApplicationContext applicationContext;

    @Before
    public void setUp() throws Exception {
        super.setUp();
        oleLocationConverterService = new OleLocationConverterService();
    }


    @Test
    @Transactional
    public void testCreateLocationRecord() throws Exception {
        URL resource = getClass().getResource("DefaultLibraryLocations.xml");
        File file = new File(resource.toURI());
        String fileContent = new FileUtil().readFile(file);
        OleLocationConverterService oleLocationRecordService = GlobalResourceLoader.getService("oleLocationConverterService");
        String  oleLocations =  oleLocationRecordService.persistLocationFromFileContent(fileContent,"DefaultLibraryLocations.xml");
        assertNotNull(oleLocations);
        }

 @Test
    @Transactional
    public void testBuildLocationFromFileContent() throws Exception {
        URL resource = getClass().getResource("DefaultLibraryLocations.xml");
        File file = new File(resource.toURI());
        String fileContent = new FileUtil().readFile(file);
        OleLocationGroup oleLocationGroup = oleLocationConverterService.buildLocationFromFileContent(fileContent);
        assertNotNull(oleLocationGroup);
        assertNotNull(oleLocationGroup.getLocationGroup());
        List<OleLocationIngest> locations = oleLocationGroup.getLocationGroup();
        assertTrue(!locations.isEmpty());
        for (Iterator<OleLocationIngest> iterator = locations.iterator(); iterator.hasNext(); ) {
            OleLocationIngest location = iterator.next();
            assertNotNull(location);
            assertNotNull(location.getLocationCode());
        }
 }



}