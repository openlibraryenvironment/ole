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

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public interface BibInfoWrapperService {

    public boolean isBibInfoExists(BibInfoBean bibInfoBean) throws Exception;

    public boolean isDuplicateRecord(BibInfoBean bibInfoBean) throws Exception;

    public String insertBibInfo(BibInfoBean bibInfoBean) throws Exception;

    public String insertBibInfo(BibInfoBean bibInfoBean, HashMap dataMap) throws Exception;

    //public BibInfoBean getBibInfo(String titleId)throws Exception;
    public BibInfoBean getBibInfo(HashMap<String, String> dataMap) throws Exception;

    public List searchBibInfo(Map map) throws Exception;

    public List searchBibInfo(BibInfoBean bibInfoBean) throws Exception;

    public List<String> searchBibInfo(BibInfoBean bibInfoBean, int noOfRecords) throws Exception;

    public String getDocStoreResponse(HashMap<String, String> dataMap) throws Exception;

    public String generateXMLStringForIngest(BibInfoBean bibInfoBean, HashMap dataMap) throws Exception;

    public String getDocSearchResponse(BibInfoBean bibInfoBean) throws Exception;

    public String getTitleIdByMarcXMLFileProcessing(BibInfoBean bibInfoBean, HashMap<String, String> dataMap) throws Exception;
}
