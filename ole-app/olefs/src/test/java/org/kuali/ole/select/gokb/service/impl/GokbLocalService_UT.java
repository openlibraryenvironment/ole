package org.kuali.ole.select.gokb.service.impl;

import org.junit.Test;
import org.kuali.ole.select.gokb.util.OleGokbXmlUtil;

/**
 * Created by jayabharathreddy on 3/5/15.
 */
public class GokbLocalService_UT {

    @Test
    public void initPackages() throws Exception {
        String responseXml = OleGokbXmlUtil.getPackageResponseXmlFromGokb(0);
        System.out.println(responseXml);
    }

    @Test
    public void initTitles()  throws Exception {
        String responseXml = OleGokbXmlUtil.getTitleResponseXmlFromGokb(0);
        System.out.println(responseXml);
     }


    @Test
    public void initPlatform() throws Exception {
        String responseXml = OleGokbXmlUtil.getPlatformResponseXmlFromGokb(0);
        System.out.println(responseXml);
    }

    @Test
    public void initOrgs() throws Exception {
        String responseXml = OleGokbXmlUtil.getOrgsResponseXmlFromGokb(0);
        System.out.println(responseXml);
    }





}
