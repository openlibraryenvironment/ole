package org.kuali.ole.select.gokb.service.impl;

import org.apache.commons.lang.StringUtils;
import org.apache.xerces.dom.DeferredElementImpl;
import org.kuali.ole.OLEConstants;
import org.kuali.ole.select.gokb.*;
import org.kuali.ole.select.gokb.service.GokbLocalService;
import org.kuali.ole.select.gokb.service.GokbRdbmsService;
import org.kuali.ole.select.gokb.util.OleGokbXmlUtil;
import org.kuali.ole.sys.OLEPropertyConstants;
import org.kuali.rice.krad.service.BusinessObjectService;
import org.kuali.rice.krad.service.KRADServiceLocator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Attr;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by rajeshbabuk on 12/18/14.
 */
public class GokbLocalServiceImpl implements GokbLocalService {

    private static final Logger LOG = LoggerFactory.getLogger(GokbLocalServiceImpl.class);

    private GokbRdbmsService gokbRdbmsService = null;
    private BusinessObjectService businessObjectService;

    public BusinessObjectService getBusinessObjectService() {
        if(businessObjectService == null){
            this.businessObjectService = KRADServiceLocator.getBusinessObjectService();
        }
        return businessObjectService;
    }

    public void setBusinessObjectService(BusinessObjectService businessObjectService) {
        this.businessObjectService = businessObjectService;
    }

    /**
     * This method returns GokbRdbmsService.
     *
     * @return
     */
    private GokbRdbmsService getGokbRdbmsService() {
        if (gokbRdbmsService == null) {
            gokbRdbmsService = new GokbRdbmsServiceImpl();
        }
        return gokbRdbmsService;
    }

    /**
     * This method is used to initialize local copy of gokb.
     */
    @Override
    public void initLocalGokb() {
        getGokbRdbmsService().truncateTables();
        int updateId = getGokbRdbmsService().insertStatus();
        initPackages(updateId);
        initVendors(updateId);
        initPlatforms(updateId);
        initTitles(updateId);
        getGokbRdbmsService().insertLogEndTime(updateId);
    }

    /**
     * This method is used to update local copy of gokb.
     *
     * @param lastUpdatedTime
     */
    @Override
    public void updateLocalGokb(String lastUpdatedTime) {
        int updateId = getGokbRdbmsService().insertStatus();
        updatePackages(lastUpdatedTime, updateId);
        updateVendors(lastUpdatedTime, updateId);
        updatePlatforms(lastUpdatedTime, updateId);
        updateTitles(lastUpdatedTime, updateId);
        getGokbRdbmsService().insertLogEndTime(updateId);
    }

    /**
     * This method is used to initialize Packages and Tipps.
     *
     * @param updateId
     */
    private void initPackages(int updateId) {
        List<OleGokbPackage> oleGokbPackageList = new ArrayList<>();
        int endIndex = 0;
        int pageSize = 0;
        int noOfRecordsInserted = 0;
        int noOfTippRecordsInserted = 0;

        while (true) {
            String responseXml = OleGokbXmlUtil.getPackageResponseXmlFromGokb(endIndex);

            NodeList packageNodeList = OleGokbXmlUtil.getElementNodeList(responseXml, OLEConstants.OleGokb.PACKAGE_XPATH_EXP);

            if (packageNodeList.getLength() == 0)
                break;

            List<String> updatedDates = OleGokbXmlUtil.getUpdatedDates(responseXml);

            for (int i = 0; i < packageNodeList.getLength(); i++) {
                Node packageNode = packageNodeList.item(i);
                OleGokbPackage oleGokbPackage = null;
                if (null != updatedDates.get(i)) {
                    oleGokbPackage = buildPackageFromPackageNode(packageNode, updatedDates.get(i));
                    oleGokbPackageList.add(oleGokbPackage);
                }
                if (oleGokbPackageList.size() == OLEConstants.OleGokb.BATCH_SIZE) {
                    getGokbRdbmsService().insertPackages(oleGokbPackageList);
                    noOfRecordsInserted = noOfRecordsInserted + oleGokbPackageList.size();
                    getGokbRdbmsService().updateStatus(updateId, OLEConstants.OleGokb.NUM_PKGS + noOfRecordsInserted);
                    oleGokbPackageList.clear();
                }
                noOfTippRecordsInserted = processTipps(updateId, oleGokbPackage, packageNode.getChildNodes(), noOfTippRecordsInserted);
            }
            if (endIndex == 0) {
                pageSize = OleGokbXmlUtil.getPageSizeFromResponse(responseXml);
            }
            endIndex = endIndex + pageSize;
        }

        if (oleGokbPackageList.size() > 0) {
            getGokbRdbmsService().insertPackages(oleGokbPackageList);
            noOfRecordsInserted = noOfRecordsInserted + oleGokbPackageList.size();
            getGokbRdbmsService().updateStatus(updateId, OLEConstants.OleGokb.NUM_PKGS + noOfRecordsInserted);
        }
    }

    /**
     * This method is used to update Packages and Tipps.
     *
     * @param lastUpdatedTime
     * @param updateId
     */
    private void updatePackages(String lastUpdatedTime, int updateId) {
        List<OleGokbPackage> oleGokbPackageList = new ArrayList<>();
        int endIndex = 0;
        int pageSize = 0;
        int noOfRecordsInserted = 0;
        int noOfTippRecordsInserted = 0;

        while (true) {
            String responseXml = OleGokbXmlUtil.getPackageResponseXmlFromGokb(lastUpdatedTime, endIndex);

            NodeList headerNodeList = OleGokbXmlUtil.getElementNodeList(responseXml, OLEConstants.OleGokb.HEADER_XPATH_EXP);

            if (headerNodeList.getLength() == 0)
                break;

            for (int i = 0; i < headerNodeList.getLength(); i++) {
                Node headerNode = headerNodeList.item(i);
                String identifier = OleGokbXmlUtil.getIdentifierFromHeader(headerNode);
                String recordXml = OleGokbXmlUtil.getPackageResponseXmlFromGokb(identifier);

                List<String> updatedDates = OleGokbXmlUtil.getUpdatedDates(recordXml);

                NodeList packageNodeList = OleGokbXmlUtil.getElementNodeList(recordXml, OLEConstants.OleGokb.PACKAGE_XPATH_EXP);
                Node packageNode = packageNodeList.item(0);
                OleGokbPackage oleGokbPackage = buildPackageFromPackageNode(packageNode, updatedDates.get(0));
                oleGokbPackageList.add(oleGokbPackage);
                if (oleGokbPackageList.size() == OLEConstants.OleGokb.BATCH_SIZE) {
                    getGokbRdbmsService().insertOrUpdatePackages(oleGokbPackageList);
                    noOfRecordsInserted = noOfRecordsInserted + oleGokbPackageList.size();
                    getGokbRdbmsService().updateStatus(updateId, OLEConstants.OleGokb.NUM_PKGS + noOfRecordsInserted);
                    oleGokbPackageList.clear();
                }
                noOfTippRecordsInserted = processTipps(updateId, oleGokbPackage, packageNode.getChildNodes(), noOfTippRecordsInserted);
            }
            if (endIndex == 0) {
                pageSize = OleGokbXmlUtil.getPageSizeFromResponse(responseXml);
            }
            if (pageSize == 0)
                break;
            endIndex = endIndex + pageSize;
        }

        if (oleGokbPackageList.size() > 0) {
            getGokbRdbmsService().insertOrUpdatePackages(oleGokbPackageList);
            noOfRecordsInserted = noOfRecordsInserted + oleGokbPackageList.size();
            getGokbRdbmsService().updateStatus(updateId, OLEConstants.OleGokb.NUM_PKGS + noOfRecordsInserted);
        }
    }

    /**
     * This method is used to initialize Titles.
     *
     * @param updateId
     */
    private void initTitles(int updateId) {
        try {
            List<OleGokbTitle> oleGokbTitleList = new ArrayList<>();
            int endIndex = 0;
            int pageSize = 0;
            int noOfRecordsInserted = 0;

            while (true) {
                String responseXml = OleGokbXmlUtil.getTitleResponseXmlFromGokb(endIndex);
                NodeList titleNodeList = OleGokbXmlUtil.getElementNodeList(responseXml, OLEConstants.OleGokb.TITLE_XPATH_EXP);
                int count = 0;
                if (titleNodeList.getLength() == 0)
                    break;

                List<String> updatedDates = OleGokbXmlUtil.getUpdatedDates(responseXml);
                for (int i = 0; i < titleNodeList.getLength(); i++) {
                    Node titleNode = titleNodeList.item(i);
                    if (titleNode.getAttributes().getLength() == 0) {
                        count = count + 1;
                        continue;
                    }
                    OleGokbTitle oleGokbTitle = null;
                    if (null != updatedDates.get(i - count)) {
                        oleGokbTitle = buildTitleFromTitleNode(titleNode, updatedDates.get(i - count));
                        oleGokbTitleList.add(oleGokbTitle);
                    }
                    if (oleGokbTitleList.size() == OLEConstants.OleGokb.BATCH_SIZE) {
                        getGokbRdbmsService().insertTitles(oleGokbTitleList);
                        noOfRecordsInserted = noOfRecordsInserted + oleGokbTitleList.size();
                        getGokbRdbmsService().updateStatus(updateId, OLEConstants.OleGokb.NUM_TITLES + noOfRecordsInserted);
                        oleGokbTitleList.clear();
                    }
                }
                if (endIndex == 0) {
                    pageSize = OleGokbXmlUtil.getPageSizeFromResponse(responseXml);
                }
                endIndex = endIndex + pageSize;
            }

            if (oleGokbTitleList.size() > 0) {
                getGokbRdbmsService().insertTitles(oleGokbTitleList);
                noOfRecordsInserted = noOfRecordsInserted + oleGokbTitleList.size();
                getGokbRdbmsService().updateStatus(updateId, OLEConstants.OleGokb.NUM_TITLES + noOfRecordsInserted);
            }
        } catch (Exception e) {
            LOG.error("Exception While Initializing Titles: " + e);
        }
    }

    /**
     * This method is used to update Titles.
     *
     * @param lastUpdatedTime
     * @param updateId
     */
    private void updateTitles(String lastUpdatedTime, int updateId) {
        List<OleGokbTitle> oleGokbTitleList = new ArrayList<>();
        int endIndex = 0;
        int pageSize = 0;
        int noOfRecordsInserted = 0;

        while (true) {
            String responseXml = OleGokbXmlUtil.getTitleResponseXmlFromGokb(lastUpdatedTime, endIndex);

            NodeList headerNodeList = OleGokbXmlUtil.getElementNodeList(responseXml, OLEConstants.OleGokb.HEADER_XPATH_EXP);

            if (headerNodeList.getLength() == 0)
                break;

            for (int i = 0; i < headerNodeList.getLength(); i++) {
                Node headerNode = headerNodeList.item(i);
                String identifier = OleGokbXmlUtil.getIdentifierFromHeader(headerNode);
                String recordXml = OleGokbXmlUtil.getTitleResponseXmlFromGokb(identifier);

                List<String> updatedDates = OleGokbXmlUtil.getUpdatedDates(recordXml);

                NodeList titleNodeList = OleGokbXmlUtil.getElementNodeList(recordXml, OLEConstants.OleGokb.TITLE_XPATH_EXP);
                Node titleNode = titleNodeList.item(0);
                OleGokbTitle oleGokbTitle = buildTitleFromTitleNode(titleNode, updatedDates.get(0));
                oleGokbTitleList.add(oleGokbTitle);
                if (oleGokbTitleList.size() == OLEConstants.OleGokb.BATCH_SIZE) {
                    getGokbRdbmsService().insertOrUpdateTitles(oleGokbTitleList);
                    noOfRecordsInserted = noOfRecordsInserted + oleGokbTitleList.size();
                    getGokbRdbmsService().updateStatus(updateId, OLEConstants.OleGokb.NUM_TITLES + noOfRecordsInserted);
                    oleGokbTitleList.clear();
                }
            }
            if (endIndex == 0) {
                pageSize = OleGokbXmlUtil.getPageSizeFromResponse(responseXml);
            }
            if (pageSize == 0)
                break;
            endIndex = endIndex + pageSize;
        }

        if (oleGokbTitleList.size() > 0) {
            getGokbRdbmsService().insertOrUpdateTitles(oleGokbTitleList);
            noOfRecordsInserted = noOfRecordsInserted + oleGokbTitleList.size();
            getGokbRdbmsService().updateStatus(updateId, OLEConstants.OleGokb.NUM_TITLES + noOfRecordsInserted);
        }
    }

    /**
     * This method is used to initialize Platforms.
     *
     * @param updateId
     */
    private void initPlatforms(int updateId) {
        List<OleGokbPlatform> oleGokbPlatformList = new ArrayList<>();
        int endIndex = 0;
        int pageSize = 0;
        int noOfRecordsInserted = 0;

        while (true) {
            String responseXml = OleGokbXmlUtil.getPlatformResponseXmlFromGokb(endIndex);

            NodeList platformNodeList = OleGokbXmlUtil.getElementNodeList(responseXml, OLEConstants.OleGokb.PLATFORM_XPATH_EXP);

            if (platformNodeList.getLength() == 0)
                break;

            List<String> updatedDates = OleGokbXmlUtil.getUpdatedDates(responseXml);

            for (int i = 0; i < platformNodeList.getLength(); i++) {
                Node platformNode = platformNodeList.item(i);
                OleGokbPlatform oleGokbPlatform = null;
                if (null != updatedDates.get(i)) {
                    oleGokbPlatform = buildPlatformFromPlatformNode(platformNode, updatedDates.get(i));
                    oleGokbPlatformList.add(oleGokbPlatform);
                }
                if (oleGokbPlatformList.size() == OLEConstants.OleGokb.BATCH_SIZE) {
                    getGokbRdbmsService().insertPlatforms(oleGokbPlatformList);
                    noOfRecordsInserted = noOfRecordsInserted + oleGokbPlatformList.size();
                    getGokbRdbmsService().updateStatus(updateId, OLEConstants.OleGokb.NUM_PLTFRMS + noOfRecordsInserted);
                    oleGokbPlatformList.clear();
                }
            }
            if (endIndex == 0) {
                pageSize = OleGokbXmlUtil.getPageSizeFromResponse(responseXml);
            }
            endIndex = endIndex + pageSize;
        }

        if (oleGokbPlatformList.size() > 0) {
            getGokbRdbmsService().insertPlatforms(oleGokbPlatformList);
            noOfRecordsInserted = noOfRecordsInserted + oleGokbPlatformList.size();
            getGokbRdbmsService().updateStatus(updateId, OLEConstants.OleGokb.NUM_PLTFRMS + noOfRecordsInserted);
        }
    }

    /**
     * This method is used to update Platforms.
     *
     * @param lastUpdatedTime
     * @param updateId
     */
    private void updatePlatforms(String lastUpdatedTime, int updateId) {
        List<OleGokbPlatform> oleGokbPlatformList = new ArrayList<>();
        int endIndex = 0;
        int pageSize = 0;
        int noOfRecordsInserted = 0;

        while (true) {
            String responseXml = OleGokbXmlUtil.getPlatformResponseXmlFromGokb(lastUpdatedTime, endIndex);

            NodeList headerNodeList = OleGokbXmlUtil.getElementNodeList(responseXml, OLEConstants.OleGokb.HEADER_XPATH_EXP);

            if (headerNodeList.getLength() == 0)
                break;

            for (int i = 0; i < headerNodeList.getLength(); i++) {
                Node headerNode = headerNodeList.item(i);
                String identifier = OleGokbXmlUtil.getIdentifierFromHeader(headerNode);
                String recordXml = OleGokbXmlUtil.getPlatformResponseXmlFromGokb(identifier);

                List<String> updatedDates = OleGokbXmlUtil.getUpdatedDates(recordXml);

                NodeList platformNodeList = OleGokbXmlUtil.getElementNodeList(recordXml, OLEConstants.OleGokb.PLATFORM_XPATH_EXP);
                Node platformNode = platformNodeList.item(0);
                OleGokbPlatform oleGokbPlatform = buildPlatformFromPlatformNode(platformNode, updatedDates.get(0));
                oleGokbPlatformList.add(oleGokbPlatform);
                if (oleGokbPlatformList.size() == OLEConstants.OleGokb.BATCH_SIZE) {
                    getGokbRdbmsService().insertOrUpdatePlatforms(oleGokbPlatformList);
                    noOfRecordsInserted = noOfRecordsInserted + oleGokbPlatformList.size();
                    getGokbRdbmsService().updateStatus(updateId, OLEConstants.OleGokb.NUM_PLTFRMS + noOfRecordsInserted);
                    oleGokbPlatformList.clear();
                }
            }
            if (endIndex == 0) {
                pageSize = OleGokbXmlUtil.getPageSizeFromResponse(responseXml);
            }
            if (pageSize == 0)
                break;
            endIndex = endIndex + pageSize;
        }

        if (oleGokbPlatformList.size() > 0) {
            getGokbRdbmsService().insertOrUpdatePlatforms(oleGokbPlatformList);
            noOfRecordsInserted = noOfRecordsInserted + oleGokbPlatformList.size();
            getGokbRdbmsService().updateStatus(updateId, OLEConstants.OleGokb.NUM_PLTFRMS + noOfRecordsInserted);
        }
    }

    /**
     * This method is used to initialize Organizations and Roles.
     *
     * @param updateId
     */
    public void initVendors(int updateId) {
        List<OleGokbOrganization> oleGokbOrganizationList = new ArrayList<>();
        int endIndex = 0;
        int pageSize = 0;
        int noOfRecordsInserted = 0;

        while (true) {
            String responseXml = OleGokbXmlUtil.getOrgsResponseXmlFromGokb(endIndex);

            NodeList orgsNodeList = OleGokbXmlUtil.getElementNodeList(responseXml, OLEConstants.OleGokb.ORG_XPATH_EXP);

            if (orgsNodeList.getLength() == 0)
                break;

            List<String> updatedDates = OleGokbXmlUtil.getUpdatedDates(responseXml);

            for (int i = 0; i < orgsNodeList.getLength(); i++) {
                Node orgNode = orgsNodeList.item(i);
                OleGokbOrganization oleGokbOrganization = null;
                if (null != updatedDates.get(i)) {
                    oleGokbOrganization = buildOrgFromOrgNode(orgNode, updatedDates.get(i));
                    oleGokbOrganizationList.add(oleGokbOrganization);
                }
                if (oleGokbOrganizationList.size() == OLEConstants.OleGokb.BATCH_SIZE) {
                    getGokbRdbmsService().insertOrganizations(oleGokbOrganizationList);
                    noOfRecordsInserted = noOfRecordsInserted + oleGokbOrganizationList.size();
                    getGokbRdbmsService().updateStatus(updateId, OLEConstants.OleGokb.NUM_ORGS + noOfRecordsInserted);
                    oleGokbOrganizationList.clear();
                }
                processRoles(oleGokbOrganization, orgNode.getChildNodes());
            }
            if (endIndex == 0) {
                pageSize = OleGokbXmlUtil.getPageSizeFromResponse(responseXml);
            }
            endIndex = endIndex + pageSize;
        }

        if (oleGokbOrganizationList.size() > 0) {
            getGokbRdbmsService().insertOrganizations(oleGokbOrganizationList);
            noOfRecordsInserted = noOfRecordsInserted + oleGokbOrganizationList.size();
            getGokbRdbmsService().updateStatus(updateId, OLEConstants.OleGokb.NUM_ORGS + noOfRecordsInserted);
        }
    }

    /**
     * This method is used to update Organizations and Roles.
     *
     * @param lastUpdatedTime
     * @param updateId
     */
    public void updateVendors(String lastUpdatedTime, int updateId) {
        List<OleGokbOrganization> oleGokbOrganizationList = new ArrayList<>();
        int endIndex = 0;
        int pageSize = 0;
        int noOfRecordsInserted = 0;

        while (true) {
            String responseXml = OleGokbXmlUtil.getOrgsResponseXmlFromGokb(lastUpdatedTime, endIndex);

            NodeList headerNodeList = OleGokbXmlUtil.getElementNodeList(responseXml, OLEConstants.OleGokb.HEADER_XPATH_EXP);

            if (headerNodeList.getLength() == 0)
                break;

            for (int i = 0; i < headerNodeList.getLength(); i++) {
                Node headerNode = headerNodeList.item(i);
                String identifier = OleGokbXmlUtil.getIdentifierFromHeader(headerNode);
                String recordXml = OleGokbXmlUtil.getOrgsResponseXmlFromGokb(identifier);

                List<String> updatedDates = OleGokbXmlUtil.getUpdatedDates(recordXml);

                NodeList orgsNodeList = OleGokbXmlUtil.getElementNodeList(recordXml, OLEConstants.OleGokb.ORG_XPATH_EXP);
                Node orgNode = orgsNodeList.item(0);
                OleGokbOrganization oleGokbOrganization = buildOrgFromOrgNode(orgNode, updatedDates.get(0));
                oleGokbOrganizationList.add(oleGokbOrganization);
                if (oleGokbOrganizationList.size() == OLEConstants.OleGokb.BATCH_SIZE) {
                    getGokbRdbmsService().insertOrUpdateOrganizations(oleGokbOrganizationList);
                    noOfRecordsInserted = noOfRecordsInserted + oleGokbOrganizationList.size();
                    getGokbRdbmsService().updateStatus(updateId, OLEConstants.OleGokb.NUM_ORGS + noOfRecordsInserted);
                    oleGokbOrganizationList.clear();
                }
                processRoles(oleGokbOrganization, orgNode.getChildNodes());
            }
            if (endIndex == 0) {
                pageSize = OleGokbXmlUtil.getPageSizeFromResponse(responseXml);
            }
            if (pageSize == 0)
                break;
            endIndex = endIndex + pageSize;
        }

        if (oleGokbOrganizationList.size() > 0) {
            getGokbRdbmsService().insertOrUpdateOrganizations(oleGokbOrganizationList);
            noOfRecordsInserted = noOfRecordsInserted + oleGokbOrganizationList.size();
            getGokbRdbmsService().updateStatus(updateId, OLEConstants.OleGokb.NUM_ORGS + noOfRecordsInserted);
        }
    }

    /**
     * This method reads the package node and builds the package object.
     *
     * @param packageNode
     * @param updatedDate
     * @return
     */
    public OleGokbPackage buildPackageFromPackageNode(Node packageNode, String updatedDate) {
        OleGokbPackage oleGokbPackage = new OleGokbPackage();
        String packageId = ((DeferredElementImpl) packageNode).getAttribute(OLEConstants.OleGokb.ID);
        if (!packageId.isEmpty()) {
            try {
                oleGokbPackage.setGokbPackageId(Integer.parseInt(packageId));
            } catch (Exception e) {
                LOG.error("Exception while parsing int for package id : " + packageId + " " + e);
            }
        }
        oleGokbPackage.setDateUpdated(OleGokbXmlUtil.getTimeStampFromString(updatedDate));
        String nodeName = null;
        Node packageChildNode = null;
        NodeList childNodes = packageNode.getChildNodes();
        for (int j = 0; j < childNodes.getLength(); j++) {
            nodeName = childNodes.item(j).getNodeName();
            packageChildNode = childNodes.item(j);
            if (nodeName.equalsIgnoreCase(OLEConstants.OleGokb.SCOPE)) {
                oleGokbPackage.setPackageScope(packageChildNode.getTextContent());
            } else if (nodeName.equalsIgnoreCase(OLEConstants.OleGokb.STATUS)) {
                oleGokbPackage.setStatus(packageChildNode.getTextContent());
            } else if (nodeName.equalsIgnoreCase(OLEConstants.OleGokb.BREAKABLE)) {
                oleGokbPackage.setBreakable(packageChildNode.getTextContent());
            } else if (nodeName.equalsIgnoreCase(OLEConstants.OleGokb.FIXED)) {
                oleGokbPackage.setFixed(packageChildNode.getTextContent());
            } else if (nodeName.equalsIgnoreCase(OLEConstants.OleGokb.NAME)) {
                oleGokbPackage.setPackageName(packageChildNode.getTextContent());
            } else if (nodeName.equalsIgnoreCase(OLEConstants.OleGokb.VARIANT_NAMES)) {
                oleGokbPackage.setVariantName(getVariantNames(packageChildNode));
            } else if (nodeName.equalsIgnoreCase(OLEConstants.OleGokb.DATE_CREATED)) {
                oleGokbPackage.setDateCreated(OleGokbXmlUtil.getTimeStampFromString(packageChildNode.getTextContent()));
            }
        }
        return oleGokbPackage;
    }

    /**
     * This method reads the Tipp nodes and initializes Tipps.
     *
     * @param updateId
     * @param oleGokbPackage
     * @param packageChildNodeList
     * @param noOfTippRecordsInserted
     * @return
     */
    public int processTipps(int updateId, OleGokbPackage oleGokbPackage, NodeList packageChildNodeList, int noOfTippRecordsInserted) {
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
                        LOG.error("Exception while parsing int for tipp id : " + tippId + " " + e);
                    }
                }
                NodeList tippChildNodes = tippNode.getChildNodes();
                for (int k = 0; k < tippChildNodes.getLength(); k++) {
                    String nodeName = tippChildNodes.item(k).getNodeName();
                    Node tippChildNode = tippChildNodes.item(k);
                    if (nodeName.equalsIgnoreCase(OLEConstants.OleGokb.STATUS)) {
                        oleGokbTipp.setStatus(tippChildNode.getTextContent());
                    } else if (nodeName.equalsIgnoreCase(OLEConstants.OleGokb.TITLE)) {
                        String titleId = ((DeferredElementImpl) tippChildNode).getAttribute("id");// String titleId = StringUtils.substringAfter(((DeferredElementImpl) tippChildNode).getAttribute(OLEConstants.OleGokb.ID), OLEConstants.OleGokb.TITLE + OLEConstants.SLASH);
                        if (!titleId.isEmpty()) {
                            try {
                                oleGokbTipp.setGokbTitleId(Integer.parseInt(titleId));
                            } catch (Exception e) {
                                LOG.error("Exception while parsing int of title id for tipp with id : " + oleGokbTipp.getGokbTippId() + " " + e);
                            }
                        }
                    } else if (nodeName.equalsIgnoreCase(OLEConstants.OleGokb.PLATFORM)) {
                        String platformId = ((DeferredElementImpl) tippChildNode).getAttribute(OLEConstants.OleGokb.ID);
                        if (!platformId.isEmpty()) {
                            try {
                                oleGokbTipp.setGokbPlatformId(Integer.parseInt(platformId));
                            } catch (Exception e) {
                                LOG.error("Exception while parsing int of platform id for tipp with id : " + oleGokbTipp.getGokbTippId() + " " + e);
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
                if (oleGokbTippList.size() == OLEConstants.OleGokb.BATCH_SIZE) {
                    getGokbRdbmsService().insertOrUpdateTipps(oleGokbTippList);
                    noOfTippRecordsInserted = noOfTippRecordsInserted + oleGokbTippList.size();
                    getGokbRdbmsService().updateStatus(updateId, OLEConstants.OleGokb.NUM_TIPPS + noOfTippRecordsInserted);
                    oleGokbTippList.clear();
                }
            }
        }
        if (oleGokbTippList.size() > 0) {
            getGokbRdbmsService().insertOrUpdateTipps(oleGokbTippList);
            noOfTippRecordsInserted = noOfTippRecordsInserted + oleGokbTippList.size();
            getGokbRdbmsService().updateStatus(updateId, OLEConstants.OleGokb.NUM_TIPPS + noOfTippRecordsInserted);
        }
        return noOfTippRecordsInserted;
    }

    /**
     * This method reads the Organization role nodes and initializes roles.
     *
     * @param oleGokbOrganization
     * @param orgChildNodeList
     */
    private void processRoles(OleGokbOrganization oleGokbOrganization, NodeList orgChildNodeList) {
        List<OleGokbOrganizationRole> oleGokbOrganizationRoles = new ArrayList<>();
        for (int i = 0; i < orgChildNodeList.getLength(); i++) {
            if (!orgChildNodeList.item(i).getNodeName().equalsIgnoreCase(OLEConstants.OleGokb.ROLES))
                continue;
            NodeList rolesNodeList = orgChildNodeList.item(i).getChildNodes();
            for (int j = 0; j < rolesNodeList.getLength(); j++) {
                Node roleNode = rolesNodeList.item(j);
                if (roleNode.getAttributes() == null)
                    continue;
                OleGokbOrganizationRole oleGokbOrganizationRole = new OleGokbOrganizationRole();
                oleGokbOrganizationRole.setGokbOrganizationId(oleGokbOrganization.getGokbOrganizationId());
                oleGokbOrganizationRole.setRole(roleNode.getTextContent());
                oleGokbOrganizationRoles.add(oleGokbOrganizationRole);
            }
        }
        if (oleGokbOrganizationRoles.size() > 0) {
            getGokbRdbmsService().insertOrUpdateOrganizationRoles(oleGokbOrganizationRoles);
        }
    }

    /**
     * This method reads the title node and builds the title object.
     *
     * @param titleNode
     * @param updatedDate
     * @return
     */
    public OleGokbTitle buildTitleFromTitleNode(Node titleNode, String updatedDate) {
        OleGokbTitle oleGokbTitle = new OleGokbTitle();
        String titleId = ((DeferredElementImpl) titleNode).getAttribute(OLEConstants.OleGokb.ID);
        if (!titleId.isEmpty()) {
            try {
                oleGokbTitle.setGokbTitleId(Integer.parseInt(titleId));
            } catch (Exception e) {
                LOG.error("Exception while parsing int for title id : " + titleId + " " + e);
            }
        }
        oleGokbTitle.setDateUpdated(OleGokbXmlUtil.getTimeStampFromString(updatedDate));
        String nodeName = null;
        Node titleChildNode = null;
        NodeList titleChildNodes = titleNode.getChildNodes();
        for (int i = 0; i < titleChildNodes.getLength(); i++) {
            nodeName = titleChildNodes.item(i).getNodeName();
            titleChildNode = titleChildNodes.item(i);
            if (nodeName.equalsIgnoreCase(OLEConstants.OleGokb.NAME)) {
                oleGokbTitle.setTitleName(titleChildNode.getTextContent());
            } else if (nodeName.equalsIgnoreCase(OLEConstants.OleGokb.IMPRINT)) {
                if (StringUtils.isNotEmpty(titleChildNode.getTextContent())) {
                    oleGokbTitle.setImprint(titleChildNode.getTextContent());
                }
            } else if (nodeName.equalsIgnoreCase(OLEConstants.OleGokb.MEDIUM)) {
                oleGokbTitle.setMedium(titleChildNode.getTextContent());
            } else if (nodeName.equalsIgnoreCase(OLEConstants.OleGokb.IDENTIFIERS)) {
                NodeList identifierNodes = titleChildNode.getChildNodes();
                for (int j = 0; j < identifierNodes.getLength(); j++) {
                    NamedNodeMap namedNodeMap = identifierNodes.item(j).getAttributes();
                    if (namedNodeMap == null)
                        continue;
                    for (int k = 0; k < namedNodeMap.getLength(); k++) {
                        Attr attribute = (Attr) namedNodeMap.item(k);
                        if (attribute.getNodeValue().equalsIgnoreCase(OLEConstants.OleGokb.ISSN)) {
                            k++;
                            oleGokbTitle.setIssnPrint(namedNodeMap.item(k).getNodeValue());
                        } else if (attribute.getNodeValue().equalsIgnoreCase(OLEConstants.OleGokb.EISSN)) {
                            k++;
                            oleGokbTitle.setIssnOnline(namedNodeMap.item(k).getNodeValue());
                        } else if (attribute.getNodeValue().equalsIgnoreCase(OLEConstants.OleGokb.DOI)) {
                            k++;
                            oleGokbTitle.setDoi(namedNodeMap.item(k).getNodeValue());
                        } else if (attribute.getNodeValue().equalsIgnoreCase(OLEConstants.OleGokb.PROPRIETARY_ID)) {
                            k++;
                            try {
                                oleGokbTitle.setProprietaryId(Integer.parseInt(namedNodeMap.item(k).getNodeValue()));
                            } catch (Exception e) {
                                LOG.error("Exception while parsing int of proprietary id for title with id : " + oleGokbTitle.getGokbTitleId() + " " + e);
                            }
                        } else if (attribute.getNodeValue().equalsIgnoreCase(OLEConstants.OleGokb.OCLC_NUM)) {
                            k++;
                            try {
                                oleGokbTitle.setOclcNumber(Integer.parseInt(namedNodeMap.item(k).getNodeValue()));
                            } catch (Exception e) {
                                LOG.error("Exception while parsing int of oclc number for title with id : " + oleGokbTitle.getGokbTitleId() + " " + e);
                            }
                        }
                    }
                }
            } else if (nodeName.equalsIgnoreCase(OLEConstants.OleGokb.PUBLISHER)) {
                String publisherId = ((DeferredElementImpl) titleChildNode).getAttribute(OLEConstants.OleGokb.ID);
                if (!publisherId.isEmpty()) {
                    try {
                        oleGokbTitle.setPublisherId(Integer.parseInt(publisherId));
                    } catch (Exception e) {
                        LOG.error("Exception while parsing int of publisher for title with id : " + oleGokbTitle.getGokbTitleId() + " " + e);
                    }
                }
            } else if (nodeName.equalsIgnoreCase(OLEConstants.OleGokb.DATE_CREATED)) {
                oleGokbTitle.setDateCreated(OleGokbXmlUtil.getTimeStampFromString(titleChildNode.getTextContent()));
            }
        }
        return oleGokbTitle;
    }

    /**
     * This method reads the platform node and builds the platform object.
     *
     * @param platformNode
     * @param updatedDate
     * @return
     */
    public OleGokbPlatform buildPlatformFromPlatformNode(Node platformNode, String updatedDate) {
        OleGokbPlatform oleGokbPlatform = new OleGokbPlatform();
        String platformId = ((DeferredElementImpl) platformNode).getAttribute(OLEConstants.OleGokb.ID);
        if (!platformId.isEmpty()) {
            try {
                oleGokbPlatform.setGokbPlatformId(Integer.parseInt(platformId));
            } catch (Exception e) {
                LOG.error("Exception while parsing int for platform id : " + platformId + " " + e);
            }
        }
        oleGokbPlatform.setDateUpdated(OleGokbXmlUtil.getTimeStampFromString(updatedDate));
        String nodeName = null;
        Node platformChildNode = null;
        NodeList platformChildNodes = platformNode.getChildNodes();
        for (int i = 0; i < platformChildNodes.getLength(); i++) {
            nodeName = platformChildNodes.item(i).getNodeName();
            platformChildNode = platformChildNodes.item(i);
            if (nodeName.equalsIgnoreCase(OLEConstants.OleGokb.NAME)) {
                oleGokbPlatform.setPlatformName(platformChildNode.getTextContent());
            } else if (nodeName.equalsIgnoreCase(OLEConstants.OleGokb.AUTHENTICATION)) {
                oleGokbPlatform.setAuthentication(platformChildNode.getTextContent());
            } else if (nodeName.equalsIgnoreCase(OLEConstants.OleGokb.SOFTWARE)) {
                oleGokbPlatform.setSoftwarePlatform(platformChildNode.getTextContent());
            } else if (nodeName.equalsIgnoreCase(OLEConstants.OleGokb.DATE_CREATED)) {
                oleGokbPlatform.setDateCreated(OleGokbXmlUtil.getTimeStampFromString(platformChildNode.getTextContent()));
            } else if (nodeName.equalsIgnoreCase(OLEConstants.OleGokb.STATUS)) {
                oleGokbPlatform.setStatus(platformChildNode.getTextContent());
            }  else if (nodeName.equalsIgnoreCase(OLEConstants.OleGokb.SERVICE)) {
                if (StringUtils.isNotBlank(platformChildNode.getTextContent())) {
                    Map<String, String> orgMap = new HashMap<>();
                    orgMap.put(OLEPropertyConstants.ORGANIZATION_NAME, platformChildNode.getTextContent());
                    List<OleGokbOrganization> oleGokbOrganizations = (List<OleGokbOrganization>) getBusinessObjectService().findMatching(OleGokbOrganization.class, orgMap);
                    if (oleGokbOrganizations.size() > 0) {
                        oleGokbPlatform.setPlatformProviderId(oleGokbOrganizations.get(0).getGokbOrganizationId());
                    }
                }
            }
        }
        return oleGokbPlatform;
    }

    /**
     * This method reads the organization node and builds the organization object.
     *
     * @param orgNode
     * @param updatedDate
     * @return
     */
    public OleGokbOrganization buildOrgFromOrgNode(Node orgNode, String updatedDate) {
        OleGokbOrganization oleGokbOrganization = new OleGokbOrganization();
        String orgId = ((DeferredElementImpl) orgNode).getAttribute(OLEConstants.OleGokb.ID);
        if (!orgId.isEmpty()) {
            try {
                oleGokbOrganization.setGokbOrganizationId(Integer.parseInt(orgId));
            } catch (Exception e) {
                LOG.error("Exception while parsing int for organization id : " + orgId + " " + e);
            }
        }
        oleGokbOrganization.setDateUpdated(OleGokbXmlUtil.getTimeStampFromString(updatedDate));
        String nodeName = null;
        Node orgChildNode = null;
        NodeList orgChildNodes = orgNode.getChildNodes();
        for (int i = 0; i < orgChildNodes.getLength(); i++) {
            nodeName = orgChildNodes.item(i).getNodeName();
            orgChildNode = orgChildNodes.item(i);
            if (nodeName.equalsIgnoreCase(OLEConstants.OleGokb.NAME)) {
                oleGokbOrganization.setOrganizationName(orgChildNode.getTextContent());
            } else if (nodeName.equalsIgnoreCase(OLEConstants.OleGokb.VARIANT_NAMES)) {
                oleGokbOrganization.setVariantName(getVariantNames(orgChildNode));
            } else if (nodeName.equalsIgnoreCase(OLEConstants.OleGokb.DATE_CREATED)) {
                oleGokbOrganization.setDateCreated(OleGokbXmlUtil.getTimeStampFromString(orgChildNode.getTextContent()));
            }
        }
        return oleGokbOrganization;
    }

    /**
     * This methods builds the variant names separated by pipe symbol.
     *
     * @param orgChildNode
     * @return
     */
    private String getVariantNames(Node orgChildNode) {
        StringBuilder variantNameBuilder = new StringBuilder();
        NodeList variantNameNodeList = orgChildNode.getChildNodes();
        for (int j = 0; j < variantNameNodeList.getLength(); j++) {
            variantNameBuilder.append(variantNameNodeList.item(j).getTextContent());
            if (j < variantNameNodeList.getLength() - 1) {
                variantNameBuilder.append(OLEConstants.OleGokb.PIPE);
            }
        }
        return variantNameBuilder.toString();
    }

}
