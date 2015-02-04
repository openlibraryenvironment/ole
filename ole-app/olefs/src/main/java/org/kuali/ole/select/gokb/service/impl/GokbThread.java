package org.kuali.ole.select.gokb.service.impl;

import org.kuali.ole.module.purap.document.dataaccess.impl.ThresholdDaoOjb;
import org.kuali.ole.select.gokb.service.GokbLocalService;
import org.kuali.ole.select.gokb.service.GokbRdbmsService;
import org.kuali.ole.select.gokb.util.OleGokbXmlUtil;

import java.sql.Timestamp;

/**
 * Created by premkumarv on 12/24/14.
 */
public class GokbThread extends Thread {


    private GokbRdbmsService gokbRdbmsService;
    private GokbLocalService gokbLocalService;

    public GokbRdbmsService getGokbRdbmsService() {
        if (null == gokbRdbmsService) {
            return new GokbRdbmsServiceImpl();
        }
        return gokbRdbmsService;
    }

    public GokbLocalService getGokbLocalService() {
        if (null == gokbLocalService) {
            return new GokbLocalServiceImpl();
        }
        return gokbLocalService;
    }
    @Override
    public void run() {
        Timestamp lastUpdatedTime = getGokbRdbmsService().getUpdatedDate();
        if(lastUpdatedTime == null) {
            getGokbLocalService().initLocalGokb();
        } else {
            String stringFromTimeStamp = OleGokbXmlUtil.getStringFromTimeStamp(lastUpdatedTime);
            getGokbLocalService().updateLocalGokb(stringFromTimeStamp+"Z");
        }
    }
}
