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

public interface PopulateBibInfoService {

    public String processBibInfoForCitation(String citation, BibInfoBean bibInfoBean) throws Exception;

    public String processBibInfoForOperURL(String openUrl, BibInfoBean bibInfoBean) throws Exception;

    public String processBibInfoForForm(BibInfoBean bibInfoBean, String title, String author, String edition, String series, String publisher, String placeOfPublication, String yearOfPublication, String standardNumber, String typeOfStandardNumber, String routeRequesterReceipt) throws Exception;
    
/*    public BibInfoBean populateBibInfoToXML(BibInfoBean formBibInfoBeanFrom) throws Exception;
    
    public BibInfoBean retriveBibInfo(String titleId)throws Exception;*/
}
