/*
 * Copyright 2012 The Kuali Foundation.
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
package org.kuali.ole.docstore.discovery.solr.work.license.onixpl;

import org.apache.solr.common.SolrInputDocument;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.kuali.ole.docstore.discovery.BaseTestCase;
import org.kuali.ole.docstore.discovery.solr.work.license.WorkLicenseCommonFields;
import org.kuali.ole.docstore.model.xmlpojo.ingest.Request;
import org.kuali.ole.docstore.model.xmlpojo.ingest.RequestDocument;
import org.kuali.ole.docstore.model.xstream.ingest.RequestHandler;

import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Class to WorkLicenseOnixplDocBuilder.
 *
 * @author Sreekanth
 */
public class WorkLicenseOnixplDocBuilder_UT
        extends BaseTestCase {

    WorkLicenseOnixplDocBuilder workLicenseOnixplDocBuilder = new WorkLicenseOnixplDocBuilder();
    List<SolrInputDocument> solrInputDocuments = null;
    List<RequestDocument> requestDocumentList = null;

    @Before
    public void setUp() throws Exception {
    }

    @After
    public void tearDown() throws Exception {
        //To change body of created methods use File | Settings | File Templates.
    }

    @Test
    public void testLicenseInputFiles() throws Exception {
        testLicenseInputFile1();
        testLicenseInputFile2();
        testLicenseInputFile3();
        testLicenseInputFile4();
        testLicenseInputFile5();
    }

    public void testLicenseInputFile1() throws Exception {
        try {
            solrInputDocuments = new ArrayList<SolrInputDocument>();
            String licenseRequestFile = "/work/license/onixpl/OLE-License-ONIXPL.xml";
            indexONIXPLDocument(licenseRequestFile);
            verifyFields(solrInputDocuments, requestDocumentList, "SpringerLink US Consortium License",
                    "[Name of licensee]", "[Name of licensor]", "0104-2007", "supplemental, trial, regular");
        } catch (Exception e) {
            LOG.error(e.getMessage() , e);
        }
    }


    public void testLicenseInputFile2() throws Exception {
        try {
            solrInputDocuments = new ArrayList<SolrInputDocument>();
            String licenseRequestFile = "/work/license/onixpl/JISC_Collections_NESLi2_Licence_2011.xml";
            indexONIXPLDocument(licenseRequestFile);
            verifyFields(solrInputDocuments, requestDocumentList, "JISC Collections NESLi2 Licence 2011",
                    "JISC_Collections", "NESLi2_Licence_2011", "0104-2008", "supplemental");
        } catch (Exception e) {
            LOG.error(e.getMessage() , e);
        }
    }

    public void testLicenseInputFile3() throws Exception {
        try {
            solrInputDocuments = new ArrayList<SolrInputDocument>();
            String licenseRequestFile = "/work/license/onixpl/JISC_Collections_NESLi2_Licence_2012.xml";
            indexONIXPLDocument(licenseRequestFile);
            verifyFields(solrInputDocuments, requestDocumentList, "JISC Collections NESLi2 Licence 2012",
                    "JISC_Collections", "NESLi2_Licence_2012", "0104-2009", "trial");
        } catch (Exception e) {
            LOG.error(e.getMessage() , e);
        }
    }


    public void testLicenseInputFile4() throws Exception {
        try {
            solrInputDocuments = new ArrayList<SolrInputDocument>();
            String licenseRequestFile
                    = "/work/license/onixpl/Springer ESALC consortium license 081028 onix-pl-i-1223936166640.xml";
            indexONIXPLDocument(licenseRequestFile);
            verifyFields(solrInputDocuments, requestDocumentList, "Springer ESALC consortium license",
                    "Participating members of Euphoria State Electronic Library Consortium",
                    "Springer Science+Business Media LLC", "0104-2010", "regular");
        } catch (Exception e) {
            LOG.error(e.getMessage() , e);
        }
    }


    public void testLicenseInputFile5() throws Exception {
        try {
            solrInputDocuments = new ArrayList<SolrInputDocument>();
            String licenseRequestFile
                    = "/work/license/onixpl/Springer US consortium template 081028 onix-pl-t-1222116235968.xml";
            indexONIXPLDocument(licenseRequestFile);
            verifyFields(solrInputDocuments, requestDocumentList, "Springer US consortium template",
                    "Participating members of US Consortium", "Springer Science+Business Media LLC", "0104-2011",
                    "local");
        } catch (Exception e) {
            LOG.error(e.getMessage() , e);
        }
    }

    public void indexONIXPLDocument(String inputFile) throws Exception {
        URL resource = getClass().getResource(inputFile);
        File file = new File(resource.toURI());
        String requestContent = readFile(file);
        RequestHandler requestHandler = new RequestHandler();
        Request request = requestHandler.toObject(requestContent);
        requestDocumentList = request.getRequestDocuments();
        for (RequestDocument requestDocument : requestDocumentList) {
            requestDocument.setUuid(String.valueOf(Math.round(Math.random())));
            workLicenseOnixplDocBuilder.buildSolrInputDocument(requestDocument, solrInputDocuments);
            Assert.assertNotNull(solrInputDocuments);
            LOG.info("uuid-->" + requestDocument.getUuid());
        }
    }


    public void verifyFields(List<SolrInputDocument> solrInputDocuments, List<RequestDocument> requestDocumentList,
                             String inputTitle, String inputLicensee, String inputLicensor, String inputContractNum,
                             String inputType) {
        Object title = solrInputDocuments.get(0).getField(WorkLicenseCommonFields.TITLE_SEARCH).getValue();
        LOG.info("title-->" + title);
        Assert.assertTrue(title.toString().contains(inputTitle));
        Object lisensee = solrInputDocuments.get(0).getField(WorkLicenseCommonFields.LICENSEE_SEARCH).getValue();
        LOG.info("licensee-->" + lisensee);
        Assert.assertEquals(inputLicensee, lisensee);
        Object licesor = solrInputDocuments.get(0).getField(WorkLicenseCommonFields.LICENSOR_SEARCH).getValue();
        LOG.info("licensor-->" + licesor);
        Assert.assertEquals(inputLicensor, licesor);
        Object contractNumber = solrInputDocuments.get(0).getField(WorkLicenseCommonFields.CONTRACT_NUMBER_SEARCH)
                .getValue();
        LOG.info("contractNumber-->" + contractNumber);
        Assert.assertEquals(inputContractNum, contractNumber);
        for (RequestDocument requestDocument : requestDocumentList) {
            String type = requestDocument.getAdditionalAttributes().getAttribute("type");
            LOG.info("type-->" + type);
            Assert.assertEquals(inputType, type);
        }
    }
}


