package org.kuali.ole.sys.batch;

import org.kuali.ole.select.document.service.OLEReEncumberRecurringOrdersJobService;
import org.kuali.ole.sys.context.SpringContext;

import java.util.Date;

/**
 * Created by arunag on 12/3/14.
 */
public class OLEReEncumberRecurringOrdersJob extends AbstractStep {

    /**
     * @see org.kuali.ole.sys.batch.Step#execute(String, java.util.Date)
     */
    public boolean execute(String jobName, Date jobRunDate) throws InterruptedException {
        SpringContext.getBean(OLEReEncumberRecurringOrdersJobService.class).retrieveReEncumberRecuringOrders();
        return true;
    }

}
