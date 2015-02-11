package org.kuali.ole.select;

import org.junit.Test;
import org.kuali.ole.SpringBaseTestCase;
import org.kuali.ole.sys.batch.OLEReEncumberRecurringOrdersJob;
import org.kuali.ole.sys.context.SpringContext;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

/**
 * Created by arunag on 12/12/14.
 */
@Transactional
public class OLEReEncumberRecuringOrdersJob_IT extends SpringBaseTestCase {

    @Test
    @Transactional
    public void generateFileforReEncumberrecuringOrders () throws InterruptedException {
        SpringContext.getBean(OLEReEncumberRecurringOrdersJob.class).execute("reencumberRecurringOrdersJob", new Date(System.currentTimeMillis()));
    }
}
