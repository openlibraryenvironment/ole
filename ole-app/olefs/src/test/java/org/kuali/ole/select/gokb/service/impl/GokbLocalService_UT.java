package org.kuali.ole.select.gokb.service.impl;

import org.apache.commons.lang.StringUtils;
import org.apache.xerces.dom.DeferredElementImpl;
import org.junit.Test;
import org.kuali.ole.OLEConstants;
import org.kuali.ole.select.gokb.*;
import org.kuali.ole.select.gokb.util.OleGokbXmlUtil;
import org.w3c.dom.Attr;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.util.ArrayList;
import java.util.List;

import static junit.framework.Assert.assertNotNull;

/**
 * Created by jayabharathreddy on 3/5/15.
 */
public class GokbLocalService_UT {

    private static String URL = "https://test-gokb.kuali.org/gokb/oai/";

    @Test
    public void initPackages() throws Exception {
        GokbLocalServiceImpl gokbLocalService = new GokbLocalServiceImpl();
        OleGokbXmlUtil.setGokbUrl(URL);
        List<OleGokbPackage> oleGokbPackageList = new ArrayList<>();
        String responseXml = OleGokbXmlUtil.getPackageResponseXmlFromGokb(6);
        NodeList packageNodeList = OleGokbXmlUtil.getElementNodeList(responseXml, OLEConstants.OleGokb.PACKAGE_XPATH_EXP);
        List<String> updatedDates = OleGokbXmlUtil.getUpdatedDates(responseXml);
        for (int i = 0; i < packageNodeList.getLength(); i++) {
            Node packageNode = packageNodeList.item(i);
            OleGokbPackage oleGokbPack = null;
            if (null != updatedDates.get(i)) {
                oleGokbPack = gokbLocalService.buildPackageFromPackageNode(packageNode, updatedDates.get(i));
                assertNotNull(oleGokbPack.getGokbPackageId());
                assertNotNull(oleGokbPack.getPackageName());
                //assertNotNull(oleGokbPack.getVariantName());
                assertNotNull(oleGokbPack.getStatus());
                assertNotNull(oleGokbPack.getPackageScope());
                assertNotNull(oleGokbPack.getBreakable());
                assertNotNull(oleGokbPack.getFixed());
//                assertNotNull(oleGokbPack.getAvailability());
                assertNotNull(oleGokbPack.getDateCreated());
                assertNotNull(oleGokbPack.getDateUpdated());

                List<OleGokbTipp> oleGokbTipps = processTipps(oleGokbPack, packageNode.getChildNodes());
                for (OleGokbTipp oleGokbTipp : oleGokbTipps) {
                    assertNotNull(oleGokbTipp.getGokbTippId());
                    assertNotNull(oleGokbTipp.getGokbPackageId());
//                    assertNotNull(oleGokbTipp.getGokbTitleId());
                    assertNotNull(oleGokbTipp.getGokbPlatformId());
                    assertNotNull(oleGokbTipp.getStatus());
//                    assertNotNull(oleGokbTipp.getStatusReason());
                    assertNotNull(oleGokbTipp.getStartdate());
                    assertNotNull(oleGokbTipp.getStartVolume());
                    assertNotNull(oleGokbTipp.getStartIssue());
//                    assertNotNull(oleGokbTipp.getEndDate());
                    assertNotNull(oleGokbTipp.getEndVolume());
                    assertNotNull(oleGokbTipp.getEndIssue());
//                    assertNotNull(oleGokbTipp.getEmbarco());
                    assertNotNull(oleGokbTipp.getPlatformHostUrl());
                    assertNotNull(oleGokbTipp.getDateCreated());
                    assertNotNull(oleGokbTipp.getDateUpdated());
                }

                oleGokbPackageList.add(oleGokbPack);
            }
        }
    }

    @Test
    public void initTitles() throws Exception {
        GokbLocalServiceImpl gokbLocalService = new GokbLocalServiceImpl();
        OleGokbXmlUtil.setGokbUrl(URL);
        List<OleGokbTitle> oleGokbTitleList = new ArrayList<>();
        String responseXml = OleGokbXmlUtil.getTitleResponseXmlFromGokb(10);
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
                assertNotNull(oleGokbTitle.getGokbTitleId());
                assertNotNull(oleGokbTitle.getTitleName());
//                assertNotNull(oleGokbTitle.getVariantName());
                assertNotNull(oleGokbTitle.getMedium());
//                assertNotNull(oleGokbTitle.getPureQa());
                assertNotNull(oleGokbTitle.getIssnOnline());
//                assertNotNull(oleGokbTitle.getIssnPrint());
//                assertNotNull(oleGokbTitle.getIssnL());
                assertNotNull(oleGokbTitle.getOclcNumber());
//                assertNotNull(oleGokbTitle.getDoi());
                assertNotNull(oleGokbTitle.getProprietaryId());
//                assertNotNull(oleGokbTitle.getSuncat());
//                assertNotNull(oleGokbTitle.getLccn());
                assertNotNull(oleGokbTitle.getPublisherId());
                assertNotNull(oleGokbTitle.getImprint());
                assertNotNull(oleGokbTitle.getDateUpdated());//oleGokbTitle.getDateCreated()
                assertNotNull(oleGokbTitle.getDateUpdated());
                oleGokbTitleList.add(oleGokbTitle);
            }
        }
    }


    @Test
    public void initPlatform() throws Exception {
        GokbLocalServiceImpl gokbLocalService = new GokbLocalServiceImpl();
        OleGokbXmlUtil.setGokbUrl(URL);
        List<OleGokbPlatform> oleGokbPlatformList = new ArrayList<>();
        String responseXml = OleGokbXmlUtil.getPlatformResponseXmlFromGokb(10);
        NodeList platformNodeList = OleGokbXmlUtil.getElementNodeList(responseXml, OLEConstants.OleGokb.PLATFORM_XPATH_EXP);
        List<String> updatedDates = OleGokbXmlUtil.getUpdatedDates(responseXml);
        for (int i = 0; i < platformNodeList.getLength(); i++) {
            Node platformNode = platformNodeList.item(i);
            OleGokbPlatform oleGokbPlatform = null;
            if (null != updatedDates.get(i)) {
                oleGokbPlatform = gokbLocalService.buildPlatformFromPlatformNode(platformNode, updatedDates.get(i));
                assertNotNull(oleGokbPlatform.getGokbPlatformId());
                assertNotNull(oleGokbPlatform.getPlatformName());
//                assertNotNull(oleGokbPlatform.getStatus());
                assertNotNull(oleGokbPlatform.getPlatformProviderId());
                assertNotNull(oleGokbPlatform.getAuthentication());
                assertNotNull(oleGokbPlatform.getSoftwarePlatform());
//                assertNotNull(oleGokbPlatform.getDateCreated());
                assertNotNull(oleGokbPlatform.getDateUpdated());
                oleGokbPlatformList.add(oleGokbPlatform);
            }
        }
    }

    @Test
    public void initVendors() throws Exception {
        List<OleGokbOrganization> oleGokbOrganizationList = new ArrayList<>();
        OleGokbXmlUtil.setGokbUrl(URL);
        GokbLocalServiceImpl gokbLocalService = new GokbLocalServiceImpl();
        String responseXml = OleGokbXmlUtil.getOrgsResponseXmlFromGokb(10);
        NodeList orgsNodeList = OleGokbXmlUtil.getElementNodeList(responseXml, OLEConstants.OleGokb.ORG_XPATH_EXP);
        List<String> updatedDates = OleGokbXmlUtil.getUpdatedDates(responseXml);
        for (int i = 0; i < orgsNodeList.getLength(); i++) {
            Node orgNode = orgsNodeList.item(i);
            OleGokbOrganization oleGokbOrganization = null;
            if (null != updatedDates.get(i)) {
                oleGokbOrganization = gokbLocalService.buildOrgFromOrgNode(orgNode, updatedDates.get(i));
                assertNotNull(oleGokbOrganization.getGokbOrganizationId());
                assertNotNull(oleGokbOrganization.getOrganizationName());
//                assertNotNull(oleGokbOrganization.getVariantName());
//                assertNotNull(oleGokbOrganization.getDateCreated());
                assertNotNull(oleGokbOrganization.getDateUpdated());
                oleGokbOrganizationList.add(oleGokbOrganization);
            }
        }
    }


    public List<OleGokbTipp> processTipps(OleGokbPackage oleGokbPackage, NodeList packageChildNodeList) {
        List<OleGokbTipp> oleGokbTippList = new ArrayList<>();
        for (int i = 0; i < packageChildNodeList.getLength(); i++) {
            if (!packageChildNodeList.item(i).getNodeName().equalsIgnoreCase(OLEConstants.OleGokb.TIPPS))
                continue;
            NodeList tippsNodeList = packageChildNodeList.item(i).getChildNodes();
            for (int j = 0; j < tippsNodeList.getLength(); j++) {
                OleGokbTipp oleGokbTipp = new OleGokbTipp();
                Node tippNode = tippsNodeList.item(j);
                if (tippNode.getAttributes() == null)
                    continue;
                String tippId = ((DeferredElementImpl) tippNode).getAttribute(OLEConstants.OleGokb.ID);
                if (!tippId.isEmpty()) {
                    try {
                        oleGokbTipp.setGokbTippId(Integer.parseInt(tippId));
                    } catch (Exception e) {
                        System.err.println("Exception while parsing int for tipp id : " + tippId + " " + e);
                    }
                }
                NodeList tippChildNodes = tippNode.getChildNodes();
                for (int k = 0; k < tippChildNodes.getLength(); k++) {
                    String nodeName = tippChildNodes.item(k).getNodeName();
                    Node tippChildNode = tippChildNodes.item(k);
                    if (nodeName.equalsIgnoreCase(OLEConstants.OleGokb.STATUS)) {
                        oleGokbTipp.setStatus(tippChildNode.getTextContent());
                    } else if (nodeName.equalsIgnoreCase(OLEConstants.OleGokb.TITLE)) {
                        String titleId = StringUtils.substringAfter(((DeferredElementImpl) tippChildNode).getAttribute(OLEConstants.OleGokb.ID), OLEConstants.OleGokb.TITLE + OLEConstants.SLASH);
                        if (!titleId.isEmpty()) {
                            try {
                                oleGokbTipp.setGokbTitleId(Integer.parseInt(titleId));
                            } catch (Exception e) {
                                System.err.println("Exception while parsing int of title id for tipp with id : " + oleGokbTipp.getGokbTippId() + " " + e);
                            }
                        }
                    } else if (nodeName.equalsIgnoreCase(OLEConstants.OleGokb.PLATFORM)) {
                        String platformId = ((DeferredElementImpl) tippChildNode).getAttribute(OLEConstants.OleGokb.ID);
                        if (!platformId.isEmpty()) {
                            try {
                                oleGokbTipp.setGokbPlatformId(Integer.parseInt(platformId));
                            } catch (Exception e) {
                                System.err.println("Exception while parsing int of platform id for tipp with id : " + oleGokbTipp.getGokbTippId() + " " + e);
                            }
                        }
                    } else if (nodeName.equalsIgnoreCase(OLEConstants.OleGokb.COVERAGE)) {
                        NamedNodeMap namedNodeMap = tippChildNode.getAttributes();
                        for (int l = 0; l < namedNodeMap.getLength(); l++) {
                            Attr attribute = (Attr) namedNodeMap.item(l);
                            if (attribute.getName().equalsIgnoreCase(OLEConstants.OleGokb.START_DATE)) {
                                oleGokbTipp.setStartdate(OleGokbXmlUtil.getTimeStampFromString(attribute.getTextContent()));
                            } else if (attribute.getName().equalsIgnoreCase(OLEConstants.OleGokb.START_VOLUME)) {
                                oleGokbTipp.setStartVolume(attribute.getTextContent());
                            } else if (attribute.getName().equalsIgnoreCase(OLEConstants.OleGokb.START_ISSUE)) {
                                oleGokbTipp.setStartIssue(attribute.getTextContent());
                            } else if (attribute.getName().equalsIgnoreCase(OLEConstants.OleGokb.END_DATE)) {
                                oleGokbTipp.setEndDate(OleGokbXmlUtil.getTimeStampFromString(attribute.getTextContent()));
                            } else if (attribute.getName().equalsIgnoreCase(OLEConstants.OleGokb.END_VOLUME)) {
                                oleGokbTipp.setEndVolume(attribute.getTextContent());
                            } else if (attribute.getName().equalsIgnoreCase(OLEConstants.OleGokb.END_ISSUE)) {
                                oleGokbTipp.setEndIssue(attribute.getTextContent());
                            }
                        }
                    } else if (nodeName.equalsIgnoreCase(OLEConstants.OleGokb.URL)) {
                        oleGokbTipp.setPlatformHostUrl(tippChildNode.getTextContent());
                    }
                }
                oleGokbTipp.setGokbPackageId(oleGokbPackage.getGokbPackageId());
                oleGokbTipp.setDateCreated(oleGokbPackage.getDateCreated());
                oleGokbTipp.setDateUpdated(oleGokbPackage.getDateUpdated());
                oleGokbTippList.add(oleGokbTipp);

            }
        }
        return oleGokbTippList;
    }

}
