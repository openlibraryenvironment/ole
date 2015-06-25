package org.kuali.ole;

import org.apache.log4j.Logger;
import org.junit.Before;
import org.junit.Test;
import org.kuali.ole.docstore.engine.service.storage.rdbms.RdbmsItemDocumentManager;
import org.mockito.MockitoAnnotations;

import java.sql.Timestamp;
import java.util.Calendar;
import java.util.GregorianCalendar;

import static org.junit.Assert.assertEquals;

/**
 * Created by aurojyotit on 5/7/15.
 */
public class RdbmsItemDocumentManager_IT extends DocstoreTestCaseBase {
    private static final Logger LOG = Logger.getLogger(RdbmsItemDocumentManager_IT.class);

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testConvertToStringTimeStamp(){
        RdbmsItemDocumentManager rdbmsItemDocumentManager=new RdbmsItemDocumentManager();
        String timestamp1="05/15/2015 11:59:00PM";
        String timestamp2="04/27/2015 00:02:59";
        String timestamp3="05/15/2015 00:59:00PM";

        Timestamp timestampExpected1= buildTimestamp(2015, 05, 15, 23, 59, 0, 0);
        Timestamp timestampExpected2= buildTimestamp(2015, 04, 27, 00, 02, 59, 0);
        Timestamp timestampExpected3= buildTimestamp(2015, 05, 15, 12, 59, 0, 0);

        Timestamp convertedTimestamp1=rdbmsItemDocumentManager.convertDateToTimeStamp(timestamp1);
        LOG.info("Converted Date : "+convertedTimestamp1+" Date Input : "+timestamp1);
        Timestamp convertedTimestamp2=rdbmsItemDocumentManager.convertDateToTimeStamp(timestamp2);
        LOG.info("Converted Date : "+convertedTimestamp2+" Date Input : "+timestamp2);
        Timestamp convertedTimestamp3=rdbmsItemDocumentManager.convertDateToTimeStamp(timestamp3);
        LOG.info("Converted Date : "+convertedTimestamp3+" Date Input : "+timestamp3);

        assertEquals(timestampExpected1,convertedTimestamp1);
        assertEquals(timestampExpected2,convertedTimestamp2);
        assertEquals(timestampExpected3,convertedTimestamp3);
    }

    public static Timestamp buildTimestamp(int year, int month, int day, int hour, int minute,
                                           int second, int millisecond) {
        Calendar cal = new GregorianCalendar();
        cal.set(Calendar.YEAR, year);
        cal.set(Calendar.MONTH, month-1);
        cal.set(Calendar.DATE, day);
        cal.set(Calendar.HOUR_OF_DAY, hour);
        cal.set(Calendar.MINUTE, minute);
        cal.set(Calendar.SECOND, second);
        cal.set(Calendar.MILLISECOND, millisecond);

        // now convert GregorianCalendar object to Timestamp object
        return new Timestamp(cal.getTimeInMillis());
    }
}
