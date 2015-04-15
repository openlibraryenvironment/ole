package org.kuali.ole;

import org.apache.commons.collections.CollectionUtils;
import org.apache.log4j.Logger;
import org.junit.Assert;
import org.junit.Test;
import org.kuali.ole.describe.bo.OleInstanceItemType;
import org.kuali.rice.krad.service.KRADServiceLocator;

import java.util.List;

/**
 * Created by sheiksalahudeenm on 8/4/15.
 */
public class OleInstanceItemType_UT extends OLETestCaseBase{
    Logger LOG = Logger.getLogger(OleInstanceItemType_UT.class);

    @Test
    public void getAllOleInstanceItemType(){
        LOG.info("Inside getAllOleInstanceItemType Method");
        List<OleInstanceItemType> oleInstanceItemTypeList = (List<OleInstanceItemType>) KRADServiceLocator.getBusinessObjectService().findAll(OleInstanceItemType.class);
        Assert.assertTrue("Collection is Empty", CollectionUtils.isNotEmpty(oleInstanceItemTypeList));
        LOG.info("GetAllOleInstanceItemType Method End");

    }
}
