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
import org.kuali.ole.select.service.ItemMarcXMLGenerationService;

import java.util.HashMap;

public class ItemMarcXMLGenerationServiceImpl implements ItemMarcXMLGenerationService {

    private static ItemMarcXMLGenerationServiceImpl itemMarcXMLGenerationServiceImpl;
    protected static org.apache.log4j.Logger LOG = org.apache.log4j.Logger.getLogger(ItemMarcXMLGenerationServiceImpl.class);


    public String getMarcXML(BibInfoBean bibInfoBean, HashMap dataMap) throws Exception {
        return buildXmlString(bibInfoBean, dataMap);
    }

    private String buildXmlString(BibInfoBean bibInfoBean, HashMap dataMap) throws Exception {
        StringBuilder xmlString = new StringBuilder();
        xmlString.append("<collection xmlns=\"http://www.loc.gov/MARC21/slim\">");
        xmlString.append("<record>");
        xmlString.append("<leader></leader>");
        xmlString.append("<controlfield tag=\"001\"></controlfield>");
        xmlString.append("<datafield tag=\"999\" ind1=\"\" ind2=\"\" >");
        xmlString.append("<subfield code=\"a\"></subfield>");
        xmlString.append("<subfield code=\"b\">").append(bibInfoBean.getVolumeNumber() != null ? bibInfoBean.getVolumeNumber() : "").append("</subfield>");
        xmlString.append("<subfield code=\"i\"></subfield>");
        xmlString.append("<subfield code=\"l\">").append(bibInfoBean.getLocation() != null ? bibInfoBean.getLocation() : "").append("</subfield>");
        xmlString.append("<subfield code=\"m\"></subfield>");
        xmlString.append("<subfield code=\"p\">").append(bibInfoBean.getListprice() != null ? bibInfoBean.getListprice() : "").append("</subfield>");
        xmlString.append("</datafield>");
        xmlString.append("</record>");
        xmlString.append("</collection>");
        if (LOG.isDebugEnabled())
            LOG.debug("itemxmlString----------->" + xmlString.toString());
        return xmlString.toString();
    }
}
