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

import org.kuali.ole.select.businessobject.BibInfoBean;
import org.kuali.ole.select.service.BibInfoService;
import org.kuali.ole.select.service.BibInfoWrapperService;
import org.kuali.ole.sys.context.SpringContext;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BibInfoWrapperServiceImpl implements BibInfoWrapperService {

    private static org.apache.log4j.Logger LOG = org.apache.log4j.Logger.getLogger(BibInfoWrapperServiceImpl.class);
    protected BibInfoService bibInfoService;

    @Override
    public boolean isBibInfoExists(BibInfoBean bibInfoBean) throws Exception {
        HashMap map = convertBibInfoBeanToMap(bibInfoBean);
        boolean isExists = bibInfoService.isExists(map);

        return isExists;
    }

    @Override
    public String insertBibInfo(BibInfoBean bibInfoBean) throws Exception {
        LOG.debug("###########inside BibInfoWrapperServiceImpl insertBibInfo###########");
        String titleId = bibInfoService.save(bibInfoBean);
        return titleId;
    }

    @Override
    public String insertBibInfo(BibInfoBean bibInfoBean, HashMap dataMap) throws Exception {
        LOG.debug("###########inside BibInfoWrapperServiceImpl insertBibInfo###########");
        String titleId = bibInfoService.save(bibInfoBean, dataMap);
        return titleId;
    }

    @Override
    public BibInfoBean getBibInfo(HashMap<String, String> dataMap) throws Exception {
        BibInfoBean bibInfoBean = bibInfoService.retrieveFromDocStore(dataMap);
        return bibInfoBean;
    }

    @Override
    public List<String> searchBibInfo(BibInfoBean bibInfoBean, int noOfRecords) throws Exception {
        HashMap map = convertBibInfoBeanToMap(bibInfoBean);
        List<BibInfoBean> bibInfoList = bibInfoService.search(map, noOfRecords);
        List<String> titleIdList = new ArrayList<String>();
        for (int i = 0; i < bibInfoList.size(); i++) {
            titleIdList.add(bibInfoList.get(i).getTitleId());
        }
        return titleIdList;
    }

    @Override
    public String getDocStoreResponse(HashMap<String, String> dataMap) throws Exception {
        return bibInfoService.getDocStoreResponse(dataMap);
    }

    @Override
    public List searchBibInfo(Map map) throws Exception {
        return bibInfoService.search(map);
    }

    @Override
    public List<BibInfoBean> searchBibInfo(BibInfoBean bibInfoBean) throws Exception {
        return bibInfoService.searchBibInfo(bibInfoBean);
    }

    @Override
    public String generateXMLStringForIngest(BibInfoBean bibInfoBean, HashMap dataMap) throws Exception {
        return bibInfoService.generateXMLStringForIngest(bibInfoBean, dataMap);
    }

    @Override
    public String getDocSearchResponse(BibInfoBean bibInfoBean) throws Exception {
        return bibInfoService.getDocSearchResponse(bibInfoBean);
    }

    @Override
    public String getTitleIdByMarcXMLFileProcessing(BibInfoBean bibInfoBean, HashMap<String, String> dataMap) throws Exception {
        return bibInfoService.getTitleIdByMarcXMLFileProcessing(bibInfoBean, dataMap);
    }

    private HashMap convertBibInfoBeanToMap(BibInfoBean bibInfoBean) {
        HashMap map = new HashMap<String, String>();
        if (bibInfoBean.getTitleId() != null) {
            map.put("titleId", bibInfoBean.getTitleId());
        }
        if (bibInfoBean.getTitle() != null) {
            map.put("title", bibInfoBean.getTitle());
        }
        if (bibInfoBean.getAuthor() != null) {
            map.put("author", bibInfoBean.getAuthor());
        }
        if (bibInfoBean.getPublisher() != null) {
            map.put("publisher", bibInfoBean.getPublisher());
        }
        if (bibInfoBean.getSubjects() != null) {
            map.put("subjects", bibInfoBean.getSubjects());
        }
        if (bibInfoBean.getCategory() != null) {
            map.put("category", bibInfoBean.getCategory());
        }
        if (bibInfoBean.getPlaceOfPublication() != null) {
            map.put("placeOfPublication", bibInfoBean.getPlaceOfPublication());
        }
        if (bibInfoBean.getYearOfPublication() != null) {
            map.put("yearOfPublication", bibInfoBean.getYearOfPublication());
        }
        if (bibInfoBean.getStandardNumber() != null) {
            map.put("standardNumber", bibInfoBean.getStandardNumber());
        }
        return map;
    }

    @Override
    public boolean isDuplicateRecord(BibInfoBean bibInfoBean) throws Exception {
        boolean isExists = bibInfoService.isDuplicateRecord(bibInfoBean);
        return isExists;
    }

    public BibInfoService getBibInfoService() {
        if (bibInfoService == null) {
            bibInfoService = SpringContext.getBean(BibInfoService.class);
        }
        return bibInfoService;
    }

    public void setBibInfoService(BibInfoService bibInfoService) {
        this.bibInfoService = bibInfoService;
    }
}
