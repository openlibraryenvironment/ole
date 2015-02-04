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

import org.apache.log4j.Logger;
import org.kuali.ole.select.businessobject.BibInfoBean;

import java.util.HashMap;
import java.util.StringTokenizer;

public class BuildOpenUrlBibInfoBean {
    private Logger LOG = org.apache.log4j.Logger.getLogger(BuildOpenUrlBibInfoBean.class);
    private BibInfoBean bibInfoBean;
    private HashMap<String, String> openUrlMap;
    private String openUrlString;

    public BibInfoBean getBean(BibInfoBean bibInfoBean, String openUrl) throws Exception {
        this.bibInfoBean = bibInfoBean;
        this.openUrlString = openUrl;
        if (openUrlString != null) {
            replaceEncodedValues();
            parseOpenUrl();
            populateBibInfoBean();
        }
        return bibInfoBean;
    }

    private void replaceEncodedValues() {
        openUrlString = openUrlString.replaceAll("\\+", " ");
        openUrlString = openUrlString.replaceAll("%20", " ");
        openUrlString = openUrlString.replaceAll("%23", "#");
        openUrlString = openUrlString.replaceAll("%25", "%");
        openUrlString = openUrlString.replaceAll("%26", "&");
        openUrlString = openUrlString.replaceAll("%2B", "+");
        openUrlString = openUrlString.replaceAll("%2F", "/");
        openUrlString = openUrlString.replaceAll("%3C", "<");
        openUrlString = openUrlString.replaceAll("%3D", "=");
        openUrlString = openUrlString.replaceAll("%3E", ">");
        openUrlString = openUrlString.replaceAll("%3F", "?");
        openUrlString = openUrlString.replaceAll("%3A", ":");
    }

    private void parseOpenUrl() {
        openUrlMap = new HashMap();
        String[] url = openUrlString.split("\\?");
        if (url.length > 1) {
            StringTokenizer st = new StringTokenizer(url[1], "&");
            while (st.hasMoreElements()) {
                String[] values = ((String) st.nextElement()).split("=");
                if (values.length > 1)
                    openUrlMap.put(values[0], values[1]);
            }
        }
    }

    private void populateBibInfoBean() {
        if (openUrlMap.get("rft.btitle") != null)
            bibInfoBean.setTitle(openUrlMap.get("rft.btitle"));

        if (bibInfoBean.getTitle() == null) {
            if (openUrlMap.get("rft.title") != null)
                bibInfoBean.setTitle(openUrlMap.get("rft.title"));
        }

        if (openUrlMap.get("rft.isbn") != null) {
            bibInfoBean.setStandardNumber(openUrlMap.get("rft.isbn"));
            bibInfoBean.setTypeOfStandardNumber("ISBN");
        }

        if (openUrlMap.get("rft.issn") != null) {
            bibInfoBean.setStandardNumber(openUrlMap.get("rft.issn"));
            bibInfoBean.setTypeOfStandardNumber("ISSN");
        }

        if (openUrlMap.get("rft.epage") != null)
            bibInfoBean.setEndPage(new Long(openUrlMap.get("rft.epage")));

        if (openUrlMap.get("rft.spage") != null)
            bibInfoBean.setStartPage(new Long(openUrlMap.get("rft.spage")));

        if (openUrlMap.get("rft.genre") != null)
            bibInfoBean.setCategory(openUrlMap.get("rft.genre"));

        if (openUrlMap.get("rft.edition") != null)
            bibInfoBean.setEdition(openUrlMap.get("rft.edition"));

        if (openUrlMap.get("rft.series") != null)
            bibInfoBean.setSeries(openUrlMap.get("rft.series"));

        if (openUrlMap.get("rft.place") != null)
            bibInfoBean.setPlaceOfPublication(openUrlMap.get("rft.place"));

        if (openUrlMap.get("rft.date") != null)
            bibInfoBean.setYearOfPublication(openUrlMap.get("rft.date"));

        if (openUrlMap.get("rft.pub") != null)
            bibInfoBean.setPublisher(openUrlMap.get("rft.pub"));

        if (openUrlMap.get("rft.au") != null)
            bibInfoBean.setAuthor(openUrlMap.get("rft.au"));
    }
}
