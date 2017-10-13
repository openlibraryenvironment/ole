/**
 * Copyright 2005-2013 The Kuali Foundation
 *
 * Licensed under the Educational Community License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.opensource.org/licenses/ecl2.php
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.kuali.rice.kew.batch;

import org.apache.commons.io.FileUtils;
import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;
import org.kuali.rice.core.api.CoreApiServiceLocator;
import org.kuali.rice.core.api.impex.xml.FileXmlDocCollection;
import org.kuali.rice.core.api.impex.xml.XmlDocCollection;
import org.kuali.rice.edl.impl.bo.EDocLiteAssociation;
import org.kuali.rice.edl.impl.service.EdlServiceLocator;
import org.kuali.rice.edl.impl.xml.export.EdlExportDataSet;
import org.kuali.rice.kew.test.KEWTestCase;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;
import org.springframework.util.FileCopyUtils;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * Tests XML "ingestion" pipeline
 *
 * @author Kuali Rice Team (rice.collab@kuali.org)
 */
public class XmlIngestionTest extends KEWTestCase {

    private static final org.apache.log4j.Logger LOG = org.apache.log4j.Logger.getLogger(XmlIngestionTest.class);

    private static final File TMP_DIR = new File(System.getProperty("java.io.tmpdir"), "XmlIngestionTest_dir");
    private static final File PENDING_DIR = new File(TMP_DIR, "pending");
    private static final File LOADED_DIR = new File(TMP_DIR, "loaded");
    private static final File PROBLEM_DIR = new File(TMP_DIR, "problem");

    public void setUp() throws Exception {
        super.setUp();
        deleteDirectories();
        TMP_DIR.mkdirs();
        PENDING_DIR.mkdirs();
        LOADED_DIR.mkdirs();
        PROBLEM_DIR.mkdirs();
    }

    private void deleteContentsOfDir(File dir, int depth) {
        File[] files = dir.listFiles();
        if (files == null) return;
        for (File file : files) {
            if (file.isDirectory() && depth > 0) {
                // decrement depth
                // to avoid the possibility of inadvertent
                // recursive delete!
                deleteContentsOfDir(file, depth - 1);
            }
            boolean success = file.delete();
            LOG.info("deleting: " + file + "..." + (success ? "succeeded" : "failed"));
        }
    }

    public void tearDown() throws Exception {
        try {
            deleteDirectories();
        } finally {
            super.tearDown();
        }
    }

    protected void deleteDirectories() {
        deleteContentsOfDir(PENDING_DIR, 0);
        deleteContentsOfDir(LOADED_DIR, 2);
        deleteContentsOfDir(PROBLEM_DIR, 2);
        deleteContentsOfDir(TMP_DIR, 0);
        TMP_DIR.delete();
    }

    protected boolean verifyFileExists(File dir, File file) throws IOException {
        ResourcePatternResolver resolver = new PathMatchingResourcePatternResolver();
        Resource[] resources = resolver.getResources(dir.toURL() + "/**/" + file.getName());
        if (resources == null) {
            return false;
        }
        for (int i = 0; i < resources.length; i++) {
            if (resources[i].exists()) {
                return true;
            }
        }
        return false;
    }
    

    @Ignore
    public void testXmlReIngestion() throws Exception {

        // Define the path for the test environment
        String relativeFolder = "/src/test/resources/org/kuali/rice/kew/batch/data/";
        String filePath = getBaseDir() + relativeFolder + "widgetsTest.xml";
        File ingestedFile = new File(filePath);
        List<XmlDocCollection> collections = new ArrayList<XmlDocCollection>();
        XmlDocCollection fileDoc = new FileXmlDocCollection(ingestedFile);
        collections.add(fileDoc);
        // ingest the collection and save it to the database
        Collection<XmlDocCollection> ingestedXmlFile = null;
        try {
            ingestedXmlFile = CoreApiServiceLocator.getXmlIngesterService().ingest(collections);
        } catch (Exception e) {
            LOG.error("Error ingesting data", e);
            //throw new RuntimeException(e);
        }

        EdlExportDataSet dataSet = new EdlExportDataSet();

        //Cast this for now
        List<EDocLiteAssociation> edla = EdlServiceLocator.getEDocLiteService().getEDocLiteAssociations();     
        String style = null;
        for (EDocLiteAssociation edl : edla) {
            if (edl != null) {
                style = edl.getStyle();
                if ("widgetsTest".equals(style)) {
                    dataSet.getEdocLites().add(edl);
                }
            }
        }
        
        byte[] xmlBytes = CoreApiServiceLocator.getXmlExporterService().export(dataSet.createExportDataSet());
        // now export that xml into a file
        File reingestFile = File.createTempFile("widgetsTestOutput", ".xml");		
        FileUtils.writeByteArrayToFile(reingestFile, xmlBytes);
        String ingestedString = FileUtils.readFileToString(ingestedFile);
        String reingestedString = FileUtils.readFileToString(reingestFile);
        //assertTrue(FileUtils.contentEquals(ingestedFile, reingestFile));
    }


    /**
     * TODO: beef this up
     * need a reliable way to test if the file arrived in the right date-stamped
     * subdirectory (maybe just pick the last, or first directory?)
     *
     * @throws java.io.IOException
     */
    @Test
    public void testXmlIngestion() throws IOException {
        XmlPollerServiceImpl poller = new XmlPollerServiceImpl();
        poller.setPollIntervalSecs(1);
        poller.setXmlParentDirectory(TMP_DIR.toString());
        poller.setXmlPendingLocation(PENDING_DIR.toString());
        poller.setXmlCompletedLocation(LOADED_DIR.toString());
        poller.setXmlProblemLocation(PROBLEM_DIR.toString());

        Properties filesToIngest = new Properties();
        filesToIngest.load(getClass().getResourceAsStream("XmlIngestionTest.txt"));
        List<File> pendingFiles = new LinkedList<File>();
        List<File> shouldPass = new LinkedList<File>();
        List<File> shouldFail = new LinkedList<File>();
        Iterator<Map.Entry<Object, Object>> entries = filesToIngest.entrySet().iterator();
        int i = 0;
        while (entries.hasNext()) {
            Map.Entry<?, ?> entry = entries.next();
            String filePath = entry.getKey().toString();
            filePath = filePath.replace("${basedir}", getBaseDir());
            File testFile = new File(filePath);
            File pendingDir = new File(PENDING_DIR + "/TestDoc-" + i);
            Assert.assertTrue(pendingDir.mkdirs());
            assertTrue(pendingDir.isDirectory());
            File pending = new File(pendingDir, testFile.getName());
            pendingFiles.add(pending);
            if (Boolean.valueOf(entry.getValue().toString())) {
                shouldPass.add(pending);
            } else {
                shouldFail.add(pending);
            }
            FileCopyUtils.copy(testFile, pending);
            LOG.info("created: " + pending);
            i++;
        }

        // poller should not throw exceptions
        poller.run();

        // check that all files have been processed
        Iterator<File> it = pendingFiles.iterator();
        while (it.hasNext()) {
            File pending = it.next();
            assertTrue(!pending.isFile());
        }

        // check that they landed in the appropriate location

        // loaded files should be in the loaded dir...
        it = shouldPass.iterator();
        while (it.hasNext()) {
            File file = it.next();
            assertTrue("Loaded file " + file + " was not moved to loaded directory " + LOADED_DIR, verifyFileExists(LOADED_DIR, file));
            assertFalse("Loaded file " + file + " was moved to problem directory " + PROBLEM_DIR, verifyFileExists(PROBLEM_DIR, file));
        }
        // and problem files should be in the problem dir...
        it = shouldFail.iterator();
        while (it.hasNext()) {
            File file = it.next();
            assertTrue("Problem file " + file + " was not moved to problem directory" + PROBLEM_DIR, verifyFileExists(PROBLEM_DIR, file));
            assertFalse("Problem file " + file + " was moved to loaded directory" + LOADED_DIR, verifyFileExists(LOADED_DIR, file));
        }
    }
}
