/*
 * Copyright 2011 The Kuali Foundation.
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
package org.kuali.ole.select.service.impl;

import org.kuali.ole.select.OleSelectConstant;
import org.kuali.ole.select.businessobject.BibInfoBean;
import org.kuali.ole.select.service.BibInfoWrapperService;
import org.kuali.ole.select.service.DocStoreService;
import org.kuali.ole.sys.context.SpringContext;

import java.util.HashMap;

public class DocStoreServiceImpl implements DocStoreService {
    protected static org.apache.log4j.Logger LOG = org.apache.log4j.Logger.getLogger(DocStoreServiceImpl.class);
    private BibInfoBean bibInfoBean;
    private BibInfoWrapperService bibInfoWrapperService;
    private BibInfoServiceImpl bibInfoServiceImpl;

    private BibInfoBean getBibInfoBean() {
        bibInfoBean = new BibInfoBean();
        return bibInfoBean;
    }

    public String search(String title, String author, String typeOfStandardNumber, String standardNumber) throws Exception {
        String response = null;
        BibInfoBean bibInfoBean = getBibInfoBean();
        HashMap dataMap = new HashMap();
        bibInfoBean = buildBibInfoBean(title, author, typeOfStandardNumber, standardNumber);
        BibInfoWrapperService docStore = getBibInfoWrapperService();
        response = docStore.getDocSearchResponse(bibInfoBean);
        if (LOG.isDebugEnabled())
            LOG.debug("docstore search response------------->" + response);
        return response;
    }

    public String ingest(String title, String author, String typeOfStandardNumber, String standardNumber) throws Exception {
        String response = null;
        BibInfoBean bibInfoBean = getBibInfoBean();
        HashMap dataMap = new HashMap();
        bibInfoBean = buildBibInfoBean(title, author, typeOfStandardNumber, standardNumber);
        BibInfoWrapperService docStore = getBibInfoWrapperService();
        String xmlString = docStore.generateXMLStringForIngest(bibInfoBean, dataMap);
        dataMap.put(OleSelectConstant.DOCSTORE_REQUEST_XMLSTRING, xmlString);
        response = docStore.getDocStoreResponse(dataMap);
        if (LOG.isDebugEnabled())
            LOG.debug("docstore ingest response------------->" + response);
        return response;
    }

    public String save(String bibMarcXmlString) throws Exception {
        BibInfoBean bibInfoBean = getBibInfoBean();
        bibInfoBean.setDocStoreOperation(OleSelectConstant.DOCSTORE_OPERATION_WEBFORM);
        HashMap<String, String> dataMap = new HashMap<String, String>();
        dataMap.put(OleSelectConstant.BIB_MARC_XMLSTRING, bibMarcXmlString);
        BibInfoWrapperService docStore = getBibInfoWrapperService();
        String titleId = docStore.generateXMLStringForIngest(bibInfoBean, dataMap);
        return titleId;
    }

    private BibInfoBean buildBibInfoBean(String title, String author, String typeOfStandardNumber, String standardNumber) throws Exception {
        bibInfoBean.setTitle(title);
        bibInfoBean.setAuthor(author);
        bibInfoBean.setTypeOfStandardNumber(typeOfStandardNumber);
        bibInfoBean.setStandardNumber(standardNumber);
        return bibInfoBean;
    }

    private BibInfoWrapperService getBibInfoWrapperService() {
        if (null == bibInfoWrapperService) {
            bibInfoWrapperService = SpringContext.getBean(BibInfoWrapperServiceImpl.class);
        }
        return bibInfoWrapperService;
    }
}
