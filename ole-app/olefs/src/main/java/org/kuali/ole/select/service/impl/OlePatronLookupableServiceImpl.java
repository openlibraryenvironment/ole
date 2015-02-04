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
package org.kuali.ole.select.service.impl;

import org.apache.commons.lang.StringUtils;
import org.kuali.ole.deliver.bo.OLEPatronEntityViewBo;
import org.kuali.ole.deliver.bo.OlePatronDocument;
import org.kuali.ole.select.businessobject.OLERequestorPatronDocument;
import org.kuali.ole.select.businessobject.OlePatronDocuments;
import org.kuali.ole.select.businessobject.OlePatronRecordHandler;
import org.kuali.ole.select.service.OleUrlResolver;
import org.kuali.ole.service.impl.OlePatronWebServiceImpl;
import org.kuali.ole.sys.OLEConstants;
import org.kuali.rice.kns.lookup.AbstractLookupableHelperServiceImpl;
import org.kuali.rice.krad.bo.BusinessObject;

import java.util.*;

public class OlePatronLookupableServiceImpl extends AbstractLookupableHelperServiceImpl {

    private transient OleUrlResolver oleUrlResolver;
    private OlePatronRecordHandler olePatronRecordHandler;


    public OlePatronRecordHandler getOlePatronRecordHandler() {
        if (null == olePatronRecordHandler) {
            olePatronRecordHandler = new OlePatronRecordHandler();
        }
        return olePatronRecordHandler;
    }

    public void setOlePatronRecordHandler(OlePatronRecordHandler olePatronRecordHandler) {
        this.olePatronRecordHandler = olePatronRecordHandler;
    }

    @Override
    public List<? extends BusinessObject> getSearchResults(Map<String, String> fieldValues) {
        return (List<OLEPatronEntityViewBo>)getBusinessObjectService().findAll(OLEPatronEntityViewBo.class);
    }

}
