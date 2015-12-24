package org.kuali.ole.deliver.util;

import org.apache.log4j.Logger;
import org.junit.Test;
import org.kuali.ole.OLETestCaseBase;
import org.kuali.ole.docstore.common.dao.CallNumberMigrationDao;
import org.kuali.ole.sys.context.SpringContext;

/**
 * Created by jayabharathreddy on 12/17/15.
 */
public class CallNumberMigration_IT  extends OLETestCaseBase {

    private static final Logger LOG = Logger.getLogger(CallNumberMigration_IT.class);

    @Test
    public void calculateAndUpdateShelvingOrder() throws Exception {
        CallNumberMigrationDao callNumberMigration = (CallNumberMigrationDao) SpringContext.getBean("callNumberMigrationDao");
        callNumberMigration.init();
    }



}
