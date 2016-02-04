package org.kuali.ole.deliver.util;

import org.apache.commons.io.FileUtils;
import org.apache.log4j.Logger;
import org.junit.Test;
import org.kuali.ole.OLETestCaseBase;
import org.kuali.ole.docstore.common.util.CallNumberMigration;

import java.io.File;
import java.sql.SQLException;

/**
 * Created by jayabharathreddy on 12/17/15.
 */
public class CallNumberMigration_IT  extends OLETestCaseBase {

    private static final Logger LOG = Logger.getLogger(CallNumberMigration_IT.class);

    @Test
    public void calculateAndUpdateShelvingOrder() throws Exception {
        CallNumberMigration callNumberMigration = new CallNumberMigration();
        callNumberMigration.init();
        callNumberMigration.getHoldingsDetails();
        callNumberMigration.calculateAndUpdateHoldingsShelvingOrder();
        callNumberMigration.getItemDetails();
        callNumberMigration.calculateAndUpdateItemShelvingOrder();
        callNumberMigration.closeConnections();
    }



}
