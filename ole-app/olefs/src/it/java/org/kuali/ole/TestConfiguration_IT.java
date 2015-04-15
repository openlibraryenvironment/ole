package org.kuali.ole;

import org.apache.commons.collections.CollectionUtils;
import org.apache.log4j.Logger;
import org.junit.Assert;
import org.junit.Test;
import org.kuali.ole.describe.bo.OleInstanceItemType;
import org.kuali.ole.ingest.krms.action.OleDeliverNoticeTypeService;
import org.kuali.ole.sys.context.SpringContext;
import org.kuali.rice.core.api.config.property.ConfigContext;
import org.kuali.rice.krad.service.KRADServiceLocator;

import java.util.List;

/**
 * Created by sheiksalahudeenm on 8/4/15.
 */
public class TestConfiguration_IT extends OLETestCaseBase{
    Logger LOG = Logger.getLogger(TestConfiguration_IT.class);

    @Test
    public void testConfigurations(){
        LOG.info("Inside testConfigurations Method");
        String dbVendor = ConfigContext.getCurrentContextConfig().getProperty("db.vendor");
        LOG.info("Fetching the value from olefs-config-defaults.xml file -->  Db.Vendor : " + dbVendor);
        Assert.assertNotNull(dbVendor);

        String applicationUrl = ConfigContext.getCurrentContextConfig().getProperty("application.url");
        LOG.info("Fetching the value from olefs-config-defaults.xml file -->  Application Url : " + applicationUrl);
        Assert.assertNotNull(applicationUrl);

        OleDeliverNoticeTypeService oleDeliverNoticeTypeService = (OleDeliverNoticeTypeService) SpringContext.getService("deliverNoticeTypeService");
        LOG.info("Fetching the service from spring-deliver.xml file -->  deliverNoticeTypeService : " + oleDeliverNoticeTypeService);
        Assert.assertNotNull(oleDeliverNoticeTypeService);

        List<OleInstanceItemType> oleInstanceItemTypeList = (List<OleInstanceItemType>) KRADServiceLocator.getBusinessObjectService().findAll(OleInstanceItemType.class);
        Assert.assertTrue("Collection is Empty", CollectionUtils.isNotEmpty(oleInstanceItemTypeList));
        LOG.info("Fetching the list of object from databases using OJB -->  List size : " + oleInstanceItemTypeList.size());
        LOG.info("testConfigurations Method End");

    }
}
