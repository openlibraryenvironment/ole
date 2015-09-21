package org.kuali.ole.batch;

import org.junit.Test;
import org.kuali.ole.deliver.service.OleBatchJobService;
import org.kuali.ole.sys.exception.ParseException;
import org.quartz.CronExpression;

import java.util.Date;

import static junit.framework.TestCase.assertNotNull;
import static junit.framework.TestCase.assertNull;

/**
 * Created by sheiksalahudeenm on 9/15/15.
 */
public class CronExpression_UT {

    @Test
    public void expressionTestForFutureDate() throws java.text.ParseException {
        String expression = "0 10 21 15 9 ? 2025";
        Date nextValidTimeToRunJobFromCronExpression = OleBatchJobService.getNextValidTimeToRunJobFromCronExpression(expression);
        assertNotNull(nextValidTimeToRunJobFromCronExpression);
        System.out.println(nextValidTimeToRunJobFromCronExpression);
    }

    @Test
    public void expressionTestForPastDate() throws java.text.ParseException {
        String expression = "0 10 21 15 9 ? 2010";
        Date nextValidTimeToRunJobFromCronExpression = OleBatchJobService.getNextValidTimeToRunJobFromCronExpression(expression);
        assertNull(nextValidTimeToRunJobFromCronExpression);
    }
}
