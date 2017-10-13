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
public abstract class PermissionAbstractSmokeTestBase extends AdminTmplMthdSTNavBase{

    /**
     * ITUtil.PORTAL + "?channelTitle=Permission&channelUrl=" 
     * + ITUtil.getBaseUrlString() + ITUtil.KNS_LOOKUP_METHOD + "org.kuali.rice.kim.impl.permission.UberPermissionBo&docFormKey=88888888&returnLocation=" +
     * ITUtil.PORTAL_URL + ITUtil.HIDE_RETURN_LINK;
     */
    public static final String BOOKMARK_URL = ITUtil.PORTAL + "?channelTitle=Permission&channelUrl=" 
            + ITUtil.getBaseUrlString() + ITUtil.KNS_LOOKUP_METHOD +
            "org.kuali.rice.kim.impl.permission.UberPermissionBo&docFormKey=88888888&returnLocation=" +
            ITUtil.PORTAL_URL + ITUtil.HIDE_RETURN_LINK ;

    private String docId;
    private String permissionName;
    
    /**
     * {@inheritDoc}
     * Permission
     * @return
     */
    @Override
    protected String getLinkLocator() {
        return "Permission";
    }
   
    public void testPermissionBookmark(Failable failable) throws Exception {
      //Create New Permission
        selectFrame("iframeportlet");
        waitAndCreateNew();
        List<String> params;
        params=testCreateNewPermission(docId, permissionName);
       
        //LookUp Permission
        selectTopFrame();
        open(ITUtil.getBaseUrlString()+BOOKMARK_URL);
        selectFrame("iframeportlet");
        params=testLookUpPermission(params.get(0), params.get(1));

        //Edit Permission
        params=testEditPermission(params.get(0), params.get(1));
        
        //Verify Permission
        selectTopFrame();
        open(ITUtil.getBaseUrlString()+BOOKMARK_URL);
        selectFrame("iframeportlet");
        params=testVerifyPermission(params.get(0), params.get(1));
        passed();
    }

    public void testPermissionNav(Failable failable) throws Exception {
        //Create New
        gotoCreateNew();
        List<String> params;
        params = testCreateNewPermission(docId, permissionName);

        //LookUp Permission
        selectTopFrame();
        navigate();
        params = testLookUpPermission(params.get(0), params.get(1));

        //Edit Permission
        params = testEditPermission(params.get(0), params.get(1));

        //Verify Permisstion
        selectTopFrame();
        navigate();
        params = testVerifyPermission(params.get(0), params.get(1));
        passed();
    }
}
