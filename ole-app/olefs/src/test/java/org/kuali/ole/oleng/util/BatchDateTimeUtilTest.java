package org.kuali.ole.oleng.util;

import org.junit.Test;

import java.util.Date;

import static org.junit.Assert.*;

/**
 * Created by SheikS on 3/1/2016.
 */
public class BatchDateTimeUtilTest {

    @Test
    public void testConvertToDate() throws Exception {
        BatchDateTimeUtil batchDateTimeUtil = new BatchDateTimeUtil();
        Date date1 = batchDateTimeUtil.convertToDate("201609");
        assertNotNull(date1);
        System.out.println(date1);

        Date date2 = batchDateTimeUtil.convertToDate("20160203");
        assertNotNull(date2);
        System.out.println(date2);

        Date date3 = batchDateTimeUtil.convertToDate("04.Feb.2016");
        assertNotNull(date3);
        System.out.println(date3);

    }
}