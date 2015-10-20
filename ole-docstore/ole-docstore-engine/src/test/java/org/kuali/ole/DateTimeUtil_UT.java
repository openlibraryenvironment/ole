/*
 * Copyright 2011 The Kuali Foundation.
 * 
 * Licensed under the Educational Community License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 * http://www.opensource.org/licenses/ecl2.php
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.kuali.ole;

import org.apache.commons.lang.time.DateUtils;
import org.apache.solr.common.util.DateUtil;
import org.junit.Test;
import org.kuali.ole.utility.DateTimeUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import static junit.framework.Assert.assertNotNull;

/**
 * Created by IntelliJ IDEA.
 * User: peris
 * Date: 7/22/11
 * Time: 2:37 PM
 * To change this template use File | Settings | File Templates.
 */
public class DateTimeUtil_UT {
    private static Logger LOG = LoggerFactory.getLogger(DateTimeUtil_UT.class);

    @Test
    public void testFormatTime() throws Exception {
        long startTime = System.currentTimeMillis();
        Thread.sleep(20);
        long endTime = System.currentTimeMillis();
        String formattedTime = DateTimeUtil.formatTime(endTime - startTime);
        assertNotNull(formattedTime);
        System.out.println(formattedTime);
    }

    @Test
    public void testFormatTime1() throws Exception {
        long startTime = System.currentTimeMillis();
        Thread.sleep(10);
        long endTime = System.currentTimeMillis();

        String formattedTime = DateTimeUtil.formatTime(endTime, startTime);
        assertNotNull(formattedTime);
        System.out.println(formattedTime);
    }

    @Test
    public void dateAndTimeFormatTest() throws Exception {
        Date date = DateUtils.addDays(new Date(), 1);
        Date dateWithStartTimeOfTheDay = DateTimeUtil.formateDateWithStartTimeOfTheDay(date);
        assertNotNull(dateWithStartTimeOfTheDay);
        System.out.println("Date with start time of the day : " + dateWithStartTimeOfTheDay);

        Date dateWithEndTimeOfTheDay = DateTimeUtil.formateDateWithEndTimeOfTheDay(date);
        assertNotNull(dateWithEndTimeOfTheDay);
        System.out.println("Date with end time of the day : " + dateWithEndTimeOfTheDay);
    }

    @Test
    public void stringToDateTest() throws Exception {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("MM/dd/yyyy");
        Date parsedDate = simpleDateFormat.parse("10/20/2015");

        Date dateWithStartTimeOfTheDay = DateTimeUtil.formateDateWithStartTimeOfTheDay(parsedDate);
        assertNotNull(dateWithStartTimeOfTheDay);
        System.out.println("Date with start time of the day : " + dateWithStartTimeOfTheDay);

        Date dateWithEndTimeOfTheDay = DateTimeUtil.formateDateWithEndTimeOfTheDay(parsedDate);
        assertNotNull(dateWithEndTimeOfTheDay);
        System.out.println("Date with end time of the day : " + dateWithEndTimeOfTheDay);

    }

    @Test
    public void stringToDateWithSolrDateFormat() throws Exception {
        DateFormat threadLocalDateFormat = DateUtil.getThreadLocalDateFormat();

        System.out.println("Solr Date Format : " + threadLocalDateFormat.format(new Date()));

    }



    @Test
    public void addOneHour() throws Exception {

    }
}
