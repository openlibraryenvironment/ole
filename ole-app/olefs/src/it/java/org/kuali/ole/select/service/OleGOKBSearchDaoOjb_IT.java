package org.kuali.ole.select.service;

import junit.framework.Assert;
import org.apache.log4j.Logger;
import org.junit.Test;
import org.kuali.ole.OLETestCaseBase;
import org.kuali.ole.select.gokb.*;
import org.kuali.ole.service.OLEGOKBSearchDaoOjb;
import org.kuali.ole.sys.context.SpringContext;
import org.kuali.rice.krad.service.BusinessObjectService;
import org.kuali.rice.krad.service.KRADServiceLocator;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by maheswarang on 4/15/15.
 */
public class OleGOKBSearchDaoOjb_IT extends OLETestCaseBase {
    Logger LOG = Logger.getLogger(OleGOKBSearchDaoOjb_IT.class);

        private BusinessObjectService businessObjectService;
        private OLEGOKBSearchDaoOjb olegokbSearchDaoOjb;


    public BusinessObjectService getBusinessObjectService() {
        if (null == businessObjectService) {
            businessObjectService = KRADServiceLocator.getBusinessObjectService();
        }
        return businessObjectService;
    }

    public void setBusinessObjectService(BusinessObjectService businessObjectService) {
        this.businessObjectService = businessObjectService;
    }


    public OLEGOKBSearchDaoOjb getOlegokbSearchDaoOjb() {
        if(null == olegokbSearchDaoOjb){
            olegokbSearchDaoOjb = (OLEGOKBSearchDaoOjb) SpringContext.getService("oleGOKBSearchDaoOjb");
        }
        return olegokbSearchDaoOjb;
    }

    public void setOlegokbSearchDaoOjb(OLEGOKBSearchDaoOjb olegokbSearchDaoOjb) {
        this.olegokbSearchDaoOjb = olegokbSearchDaoOjb;
    }

    @Test
    public void testForExistingTippRetrieval() {
        Map<String, String> gokbTippMap = new HashMap<String, String>();
        // gokbTippMap.put("gokbTitleId","111");
        gokbTippMap.put("oleGokbPlatform.gokbPlatformId", "111");
        List<OleGokbTipp> oleGokbTipps = (List<OleGokbTipp>) KRADServiceLocator.getBusinessObjectService().findMatching(OleGokbTipp.class, gokbTippMap);
        Assert.assertNotNull(oleGokbTipps);
        for (OleGokbTipp oleGokbTipp : oleGokbTipps) {
            LOG.info("TIPP ID :" + oleGokbTipp.getGokbTippId());
            LOG.info("Package Name :" + oleGokbTipp.getOleGokbPackage().getPackageName());
            LOG.info("Platform Name :" + oleGokbTipp.getOleGokbPlatform().getPlatformName());
            LOG.info("Title  Name :" + oleGokbTipp.getOleGokbTitle().getTitleName());
        }

    }

    @Test
    public void testSavePackage() {
        OleGokbPackage oleGokbPackage = new OleGokbPackage();
        oleGokbPackage.setPackageName("Test Package");
        oleGokbPackage.setAvailability("AVAILABLE");
        oleGokbPackage.setBreakable("TRUE");
        oleGokbPackage.setFixed("FIXED");
        oleGokbPackage.setPackageScope("CURRENT");
        oleGokbPackage.setStatus("AVAILABLE");
        oleGokbPackage.setVariantName("Test Package");
        oleGokbPackage.setDateCreated(new Timestamp(System.currentTimeMillis()));
        oleGokbPackage.setDateUpdated(new Timestamp(System.currentTimeMillis()));
        oleGokbPackage = getBusinessObjectService().save(oleGokbPackage);
        LOG.info("Package has been successfully saved . Package Id : " + oleGokbPackage.getGokbPackageId());
        Assert.assertNotNull(oleGokbPackage.getGokbPackageId());
    }

    @Test
    public void testSaveOrganization() {
        OleGokbOrganization oleGokbOrganization = new OleGokbOrganization();
        oleGokbOrganization.setOrganizationName("Test Organization");
        oleGokbOrganization.setVariantName("Test organization");
        oleGokbOrganization.setDateCreated(new Timestamp(System.currentTimeMillis()));
        oleGokbOrganization = getBusinessObjectService().save(oleGokbOrganization);
        LOG.info("Organization Record has been successfully saved . Organization Id : " + oleGokbOrganization.getGokbOrganizationId());
        Assert.assertNotNull(oleGokbOrganization.getGokbOrganizationId());
    }


    @Test
    public void testSavePlatform() {
        OleGokbOrganization oleGokbOrganization = new OleGokbOrganization();
        oleGokbOrganization.setOrganizationName("Test Organization");
        oleGokbOrganization.setVariantName("Test organization");
        oleGokbOrganization.setDateCreated(new Timestamp(System.currentTimeMillis()));
        oleGokbOrganization = getBusinessObjectService().save(oleGokbOrganization);
        LOG.info("Organization Record has been successfully saved . Organization Id : " + oleGokbOrganization.getGokbOrganizationId());
        OleGokbPlatform oleGokbPlatform = new OleGokbPlatform();
        oleGokbPlatform.setPlatformName("Test Platform Name");
        oleGokbPlatform.setPlatformProviderId(oleGokbOrganization.getGokbOrganizationId());
        oleGokbPlatform.setSoftwarePlatform("Test SoftWare Platform");
        oleGokbPlatform.setAuthentication("TRUE");
        oleGokbPlatform.setStatus("Status");
        oleGokbPlatform.setStatusId("1");
        oleGokbPlatform.setDateCreated(new Timestamp(System.currentTimeMillis()));
        oleGokbPlatform.setDateUpdated(new Timestamp(System.currentTimeMillis()));
        oleGokbPlatform = getBusinessObjectService().save(oleGokbPlatform);
        LOG.info("Platform Record has been successfully saved . Platform Id : " + oleGokbPlatform.getGokbPlatformId());
        Assert.assertNotNull(oleGokbOrganization.getGokbOrganizationId());
    }


    @Test
    public void testSaveTitle() {
        OleGokbTitle oleGokbTitle = new OleGokbTitle();
        oleGokbTitle.setTitleName("Test Title");
        oleGokbTitle.setVariantName("Test Variant");
        oleGokbTitle.setDoi("DOI");
        oleGokbTitle.setDateCreated(new Timestamp(System.currentTimeMillis()));
        oleGokbTitle.setDateUpdated(new Timestamp(System.currentTimeMillis()));
        oleGokbTitle.setPublisherId(1);
        oleGokbTitle = getBusinessObjectService().save(oleGokbTitle);
        LOG.info("Title Record has been successfully saved . Title Id : " + oleGokbTitle.getGokbTitleId());
        Assert.assertNotNull(oleGokbTitle.getGokbTitleId());
    }


    @Test
    public void testSaveTipp() {
        OleGokbTitle oleGokbTitle = new OleGokbTitle();
        oleGokbTitle.setGokbTitleId(100);
        oleGokbTitle.setTitleName("Test Title");
        oleGokbTitle.setVariantName("Test Variant");
        oleGokbTitle.setDoi("DOI");
        oleGokbTitle.setDateCreated(new Timestamp(System.currentTimeMillis()));
        oleGokbTitle.setDateUpdated(new Timestamp(System.currentTimeMillis()));
        oleGokbTitle.setPublisherId(1);
        oleGokbTitle = getBusinessObjectService().save(oleGokbTitle);
        LOG.info("Title Record has been successfully saved . Title Id : " + oleGokbTitle.getGokbTitleId());
        OleGokbPackage oleGokbPackage = new OleGokbPackage();
        oleGokbPackage.setGokbPackageId(100);
        oleGokbPackage.setPackageName("Test Package");
        oleGokbPackage.setAvailability("AVAILABLE");
        oleGokbPackage.setBreakable("TRUE");
        oleGokbPackage.setFixed("FIXED");
        oleGokbPackage.setPackageScope("CURRENT");
        oleGokbPackage.setStatus("AVAILABLE");
        oleGokbPackage.setVariantName("Test Package");
        oleGokbPackage.setDateCreated(new Timestamp(System.currentTimeMillis()));
        oleGokbPackage.setDateUpdated(new Timestamp(System.currentTimeMillis()));
        oleGokbPackage = getBusinessObjectService().save(oleGokbPackage);
        LOG.info("Package has been successfully saved . Package Id : " + oleGokbPackage.getGokbPackageId());
        OleGokbOrganization oleGokbOrganization = new OleGokbOrganization();
        oleGokbOrganization.setGokbOrganizationId(100);
        oleGokbOrganization.setOrganizationName("Test Organization");
        oleGokbOrganization.setVariantName("Test organization");
        oleGokbOrganization.setDateCreated(new Timestamp(System.currentTimeMillis()));
        oleGokbOrganization = getBusinessObjectService().save(oleGokbOrganization);
        LOG.info("Organization Record has been successfully saved . Organization Id : " + oleGokbOrganization.getGokbOrganizationId());
        OleGokbPlatform oleGokbPlatform = new OleGokbPlatform();
        oleGokbPlatform.setGokbPlatformId(100);
        oleGokbPlatform.setPlatformName("Test Platform Name");
        oleGokbPlatform.setPlatformProviderId(oleGokbOrganization.getGokbOrganizationId());
        oleGokbPlatform.setSoftwarePlatform("Test SoftWare Platform");
        oleGokbPlatform.setAuthentication("TRUE");
        oleGokbPlatform.setStatus("Status");
        oleGokbPlatform.setStatusId("1");
        oleGokbPlatform.setDateCreated(new Timestamp(System.currentTimeMillis()));
        oleGokbPlatform.setDateUpdated(new Timestamp(System.currentTimeMillis()));
        oleGokbPlatform = getBusinessObjectService().save(oleGokbPlatform);
        LOG.info("Platform Record has been successfully saved . Platform Id : " + oleGokbPlatform.getGokbPlatformId());
        List<OleGokbTipp> oleGokbTippList = new ArrayList<OleGokbTipp>();
        OleGokbTipp oleGokbTipp;
        for (int i = 10000; i < 10005; i++) {
            oleGokbTipp = new OleGokbTipp();
            oleGokbTipp.setGokbTippId(i);
            oleGokbTipp.setGokbPackageId(oleGokbPackage.getGokbPackageId());
            oleGokbTipp.setGokbPlatformId(oleGokbPlatform.getGokbPlatformId());
            oleGokbTipp.setGokbTitleId(oleGokbTitle.getGokbTitleId());
            oleGokbTipp.setStartdate(new Timestamp(System.currentTimeMillis()));
            oleGokbTipp.setEndDate(new Timestamp(System.currentTimeMillis()));
            oleGokbTipp.setEndIssue("End Issue");
            oleGokbTipp.setDateCreated(new Timestamp(System.currentTimeMillis()));
            oleGokbTipp.setDateUpdated(new Timestamp(System.currentTimeMillis()));
            getBusinessObjectService().save(oleGokbTipp);
            LOG.info("Tipp Record has been successfully saved . Tipp Id : " + oleGokbTipp.getGokbTippId());
        }

        List<OleGokbTipp> oleGokbTipps;
       // Map<String, String> tippMap = new HashMap<String, String>();
     //   tippMap.put("oleGokbTitle.gokbTitleId", String.valueOf(oleGokbTitle.getGokbTitleId()));/* Retrieving tipp based on title id */
        //tippMap.put("oleGokbPackage.gokbPackageId",String.valueOf(oleGokbPackage.getGokbPackageId()));/* Retrieving tipp based on package id */
        //tippMap.put("oleGokbPlatform.gokbPlatformId",String.valueOf(oleGokbPlatform.getGokbPlatformId()));/* Retrieving tipp based on platform  id */

        oleGokbTipps = (List<OleGokbTipp>) getOlegokbSearchDaoOjb().packageSearch(oleGokbPackage.getPackageName(),oleGokbPlatform.getPlatformName(),null,null,null,null,null,null,null);
        for (OleGokbTipp oleGokbTip : oleGokbTipps) {
            LOG.info("TIPP ID :" + oleGokbTip.getGokbTippId());
            LOG.info("Package Name :" + oleGokbTip.getOleGokbPackage().getPackageName());
            LOG.info("Platform Name :" + oleGokbTip.getOleGokbPlatform().getPlatformName());
            LOG.info("Title  Name :" + oleGokbTip.getOleGokbTitle().getTitleName());
            Assert.assertNotNull(oleGokbTipps);
        }

          getBusinessObjectService().delete(oleGokbTippList);
        getBusinessObjectService().delete(oleGokbPlatform);
        getBusinessObjectService().delete(oleGokbOrganization);
        getBusinessObjectService().delete(oleGokbPackage);
    }
}
