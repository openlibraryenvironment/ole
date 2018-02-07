/**
 * Copyright 2005-2013 The Kuali Foundation
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
package edu.samplu.admin.test;

import java.util.List;

import edu.samplu.common.Failable;
import edu.samplu.common.ITUtil;

/**
 * @author Kuali Rice Team (rice.collab@kuali.org)
 */
public abstract class ParameterTypeAbstractSmokeTestBase extends AdminTmplMthdSTNavBase{

    /**
     * ITUtil.PORTAL + "?channelTitle=Parameter%20Type&channelUrl=" 
     * + ITUtil.getBaseUrlString() + ITUtil.KNS_LOOKUP_METHOD + "org.kuali.rice.coreservice.impl.parameter.ParameterTypeBo&docFormKey=88888888&returnLocation=" +
     * ITUtil.PORTAL_URL + ITUtil.HIDE_RETURN_LINK;
     */
    public static final String BOOKMARK_URL = ITUtil.PORTAL + "?channelTitle=Parameter%20Type&channelUrl=" 
            + ITUtil.getBaseUrlString() + ITUtil.KNS_LOOKUP_METHOD +
            "org.kuali.rice.coreservice.impl.parameter.ParameterTypeBo&docFormKey=88888888&returnLocation=" +
            ITUtil.PORTAL_URL + ITUtil.HIDE_RETURN_LINK ;

    private String docId;
    private String parameterType;
    private String parameterCode;
    
    /**
     * {@inheritDoc}
     * Parameter Type
     * @return
     */
    @Override
    protected String getLinkLocator() {
        return "Parameter Type";
    }
   
    public void testParameterTypeBookmark(Failable failable) throws Exception {
     // Create New
        selectFrame("iframeportlet");
        waitAndCreateNew();
        List<String> params;
        params=testCreateNewParameterType(docId, parameterType,parameterCode);
        
        //Lookup
        open(ITUtil.getBaseUrlString()+BOOKMARK_URL);
        selectFrame("iframeportlet");
        params=testLookUpParameterType(params.get(0), params.get(1),params.get(2));
        
        //edit
        params=testEditParameterType(params.get(0), params.get(1),params.get(2));

        //Verify if its edited
        open(ITUtil.getBaseUrlString()+BOOKMARK_URL);
        selectFrame("iframeportlet");
        params=testLookUpParameterType(params.get(0), params.get(1),params.get(2));

        //copy
        params=testCopyParameterType(params.get(0), params.get(1),params.get(2));

        //Verify if its copied
        open(ITUtil.getBaseUrlString()+BOOKMARK_URL);
        selectFrame("iframeportlet");
        testVerifyCopyParameterType(params.get(0), params.get(1),params.get(2));
        passed();
    }

    public void testParameterTypeNav(Failable failable) throws Exception {
      //Create New
        gotoCreateNew();
        List<String> params;
        params=testCreateNewParameterType(docId, parameterType,parameterCode);
    
        //Lookup
        navigate();
        selectFrame("iframeportlet");
        params=testLookUpParameterType(params.get(0), params.get(1),params.get(2));

        //edit
        params=testEditParameterType(params.get(0), params.get(1),params.get(2));
        
        //Verify if its edited
        navigate();
        params=testLookUpParameterType(params.get(0), params.get(1),params.get(2));

        //copy
        params=testCopyParameterType(params.get(0), params.get(1),params.get(2));
        
        //Verify if its copied
        navigate();
        testVerifyCopyParameterType(params.get(0), params.get(1),params.get(2));
        passed();
    }
}
