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
package org.kuali.ole.select.service;

import org.kuali.ole.select.businessobject.BibInfoBean;
import org.kuali.ole.select.businessobject.DocInfoBean;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public interface BibInfoService {

    public boolean isExists(HashMap map) throws Exception;

    public String save(BibInfoBean bibInfoBean) throws Exception;

    public String save(BibInfoBean bibInfoBean, HashMap dataMap) throws Exception;

    public List<BibInfoBean> getUUID(List<BibInfoBean> bibInfoBeanList, HashMap dataMap) throws Exception;

    //public BibInfoBean retrieveFromDocStore(String titleId) throws Exception;
    public BibInfoBean retrieveFromDocStore(HashMap<String, String> dataMap) throws Exception;

    public List search(HashMap map, int noOfRecords) throws Exception;

    public String getDocStoreResponse(HashMap<String, String> dataMap) throws Exception;

    public List search(Map map) throws Exception;

    public List<BibInfoBean> searchBibInfo(BibInfoBean bibInfoBean) throws Exception;

    public String convertBibInfoBeanToMarcXMLString(BibInfoBean bibInfoBean, HashMap<String, String> dataMap) throws Exception;

    public String generateItemMarcXMLString(BibInfoBean bibInfoBean, HashMap<String, String> dataMap) throws Exception;

    public String generateRequestXMLString(BibInfoBean bibInfoBean, HashMap<String, String> dataMap) throws Exception;

    public String generateXMLStringForIngest(BibInfoBean bibInfoBean, HashMap<String, String> dataMap) throws Exception;

    public String getDocSearchResponse(BibInfoBean bibInfoBean) throws Exception;

    public boolean isDuplicateRecord(BibInfoBean bibInfoBean) throws Exception;

    public List<DocInfoBean> getResult(List isbnList) throws Exception;

    public String getTitleIdByMarcXMLFileProcessing(BibInfoBean bibInfoBean, HashMap<String, String> dataMap) throws Exception;

/*    public BibInfoBean retrieveFromSolrQuery(Map map) throws Exception;

    public BibInfoBean retrieveFromSolrQueryNew(Map map) throws Exception;*/
}
