/*
 * Copyright 2013 The Kuali Foundation.
 *
 * Licensed under the Educational Community License, Version 1.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.opensource.org/licenses/ecl1.php
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.kuali.ole.select.businessobject.inquiry;

import org.kuali.ole.OLEConstants;
import org.kuali.ole.deliver.bo.OLEPatronEntityViewBo;
import org.kuali.ole.deliver.bo.OleBorrowerType;
import org.kuali.ole.deliver.bo.OLEPatronEntityViewBo;
import org.kuali.ole.sys.businessobject.inquiry.KfsInquirableImpl;
import org.kuali.rice.kim.impl.identity.name.EntityNameBo;
import org.kuali.rice.kns.lookup.HtmlData;
import org.kuali.rice.kns.lookup.HtmlData.AnchorHtmlData;
import org.kuali.rice.krad.bo.BusinessObject;
import java.util.*;

public class OlePatronDocumentInquirableImpl extends KfsInquirableImpl {

    @Override
    public HtmlData getInquiryUrl(BusinessObject businessObject, String attributeName, boolean forceInquiry) {
        AnchorHtmlData htmlData = new AnchorHtmlData();
        if (businessObject instanceof OLEPatronEntityViewBo && attributeName.equals(OLEConstants.REQUESTOR_PATRON_ID) ) {
            OLEPatronEntityViewBo olePatronEntityViewBo = (OLEPatronEntityViewBo)businessObject;
            String patronId = olePatronEntityViewBo.getPatronId();
            olePatronEntityViewBo.setRequestorPatronId(patronId);
            String href = OLEConstants.ASSIGN_REQUESTOR_INQUIRY + patronId;
            href = "kr/" + href;
            htmlData.setHref(href);
            return htmlData;
        }
        else {
            OLEPatronEntityViewBo oleRequestorPatronDocument = (OLEPatronEntityViewBo)businessObject;
            String patronId = oleRequestorPatronDocument.getPatronId();
            oleRequestorPatronDocument.setRequestorPatronId(patronId);
        }
        return htmlData;
    }

}
