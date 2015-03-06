package org.kuali.ole.select.gokb.service.impl;

import org.junit.Test;
import org.kuali.ole.OLEConstants;
import org.kuali.ole.select.gokb.OleGokbOrganization;
import org.kuali.ole.select.gokb.OleGokbPackage;
import org.kuali.ole.select.gokb.OleGokbPlatform;
import org.kuali.ole.select.gokb.OleGokbTitle;
import org.kuali.ole.select.gokb.util.OleGokbXmlUtil;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by jayabharathreddy on 3/5/15.
 */
public class GokbLocalService_UT {

    @Test
    public void initPackages() throws Exception {
        GokbLocalServiceImpl gokbLocalService = new GokbLocalServiceImpl();
        List<OleGokbPackage> oleGokbPackageList = new ArrayList<>();
        String responseXml = OleGokbXmlUtil.getPackageResponseXmlFromGokb(0);
        NodeList packageNodeList = OleGokbXmlUtil.getElementNodeList(responseXml, OLEConstants.OleGokb.PACKAGE_XPATH_EXP);
        List<String> updatedDates = OleGokbXmlUtil.getUpdatedDates(responseXml);
        for (int i = 0; i < packageNodeList.getLength(); i++) {
            Node packageNode = packageNodeList.item(i);
            OleGokbPackage oleGokbPackage = null;
            if (null != updatedDates.get(i)) {
                oleGokbPackage = gokbLocalService.buildPackageFromPackageNode(packageNode, updatedDates.get(i));
                System.out.println(oleGokbPackage.getGokbPackageId());
                oleGokbPackageList.add(oleGokbPackage);
            }
        }
    }

    @Test
    public void initTitles() throws Exception {
        GokbLocalServiceImpl gokbLocalService = new GokbLocalServiceImpl();
        List<OleGokbTitle> oleGokbTitleList = new ArrayList<>();
        String responseXml = OleGokbXmlUtil.getTitleResponseXmlFromGokb(0);
        NodeList titleNodeList = OleGokbXmlUtil.getElementNodeList(responseXml, OLEConstants.OleGokb.TITLE_XPATH_EXP);
        int count = 0;
        List<String> updatedDates = OleGokbXmlUtil.getUpdatedDates(responseXml);
        for (int i = 0; i < titleNodeList.getLength(); i++) {
            Node titleNode = titleNodeList.item(i);
            if (titleNode.getAttributes().getLength() == 0) {
                count = count + 1;
                continue;
            }
            OleGokbTitle oleGokbTitle = null;
            if (null != updatedDates.get(i - count)) {
                oleGokbTitle = gokbLocalService.buildTitleFromTitleNode(titleNode, updatedDates.get(i - count));
                System.out.println(oleGokbTitle.getTitleName());
                oleGokbTitleList.add(oleGokbTitle);
            }
        }
    }


    @Test
    public void initPlatform() throws Exception {
        GokbLocalServiceImpl gokbLocalService = new GokbLocalServiceImpl();
        List<OleGokbPlatform> oleGokbPlatformList = new ArrayList<>();
        String responseXml = OleGokbXmlUtil.getPlatformResponseXmlFromGokb(0);
        NodeList platformNodeList = OleGokbXmlUtil.getElementNodeList(responseXml, OLEConstants.OleGokb.PLATFORM_XPATH_EXP);
        List<String> updatedDates = OleGokbXmlUtil.getUpdatedDates(responseXml);
        for (int i = 0; i < platformNodeList.getLength(); i++) {
            Node platformNode = platformNodeList.item(i);
            OleGokbPlatform oleGokbPlatform = null;
            if (null != updatedDates.get(i)) {
                oleGokbPlatform = gokbLocalService.buildPlatformFromPlatformNode(platformNode, updatedDates.get(i));
                System.out.println(oleGokbPlatform.getPlatformName());
                oleGokbPlatformList.add(oleGokbPlatform);
            }
        }
    }

    @Test
    public void initVendors() throws Exception {
        List<OleGokbOrganization> oleGokbOrganizationList = new ArrayList<>();
        GokbLocalServiceImpl gokbLocalService = new GokbLocalServiceImpl();
        String responseXml = OleGokbXmlUtil.getOrgsResponseXmlFromGokb(0);
        NodeList orgsNodeList = OleGokbXmlUtil.getElementNodeList(responseXml, OLEConstants.OleGokb.ORG_XPATH_EXP);
        List<String> updatedDates = OleGokbXmlUtil.getUpdatedDates(responseXml);
        for (int i = 0; i < orgsNodeList.getLength(); i++) {
            Node orgNode = orgsNodeList.item(i);
            OleGokbOrganization oleGokbOrganization = null;
            if (null != updatedDates.get(i)) {
                oleGokbOrganization = gokbLocalService.buildOrgFromOrgNode(orgNode, updatedDates.get(i));
                System.out.println(oleGokbOrganization.getOrganizationName());
                oleGokbOrganizationList.add(oleGokbOrganization);
            }
        }
    }


}
