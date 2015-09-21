package org.kuali.ole.deliver.service;

import org.apache.commons.lang3.StringUtils;
import org.junit.Before;
import org.junit.Test;
import org.kuali.ole.OLEConstants;
import org.kuali.ole.deliver.util.BulkUpdateDataObject;
import org.kuali.rice.core.api.config.property.Config;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by sheiksalahudeenm on 8/11/15.
 */
public class OleLoanDocumentPlatformAwareDaoTest {

    @Mock
    private Config mockConfig;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void getSQLsForBatch() throws Exception {
        Mockito.when(mockConfig.getProperty("db.vendor")).thenReturn("mysql");
        OleLoanDocumentPlatformAwareDao oleLoanDocumentPlatformAwareDao  = new OleLoanDocumentPlatformAwareDao();
        oleLoanDocumentPlatformAwareDao.setCurrentContextConfig(mockConfig);
        List<BulkUpdateDataObject> bulkUpdateDataObjects = new ArrayList<>();
        BulkUpdateDataObject bulkUpdateDataObject = new BulkUpdateDataObject();
        HashMap setClauseMap = new HashMap();
        setClauseMap.put("CURR_DUE_DT_TIME",getTimeStamp(new SimpleDateFormat("MM/dd/yyyy").parse("06/20/2015"), "11:36:00"));
        bulkUpdateDataObject.setSetClauseMap(setClauseMap);
        HashMap whereClauseMap = new HashMap();
        whereClauseMap.put("ITEM_UUID", "wio-1292");
        bulkUpdateDataObject.setWhereClauseMap(whereClauseMap);
        bulkUpdateDataObjects.add(bulkUpdateDataObject);

        BulkUpdateDataObject bulkUpdateDataObject1 = new BulkUpdateDataObject();
        HashMap setClauseMap1 = new HashMap();
        setClauseMap1.put("CURR_DUE_DT_TIME",getTimeStamp(new SimpleDateFormat("MM/dd/yyyy").parse("03/21/2015"), "10:36:00"));
        bulkUpdateDataObject1.setSetClauseMap(setClauseMap1);
        HashMap whereClauseMap1 = new HashMap();
        whereClauseMap1.put("ITEM_UUID", "wio-457");
        bulkUpdateDataObject1.setWhereClauseMap(whereClauseMap1);
        bulkUpdateDataObjects.add(bulkUpdateDataObject1);

        List<String> updateQueries = oleLoanDocumentPlatformAwareDao.getUpdateQueriesForDate(bulkUpdateDataObjects);
        for (Iterator<String> iterator = updateQueries.iterator(); iterator.hasNext(); ) {
            String sql = iterator.next();
            System.out.println(sql);
        }
    }

    private Timestamp getTimeStamp(Date loanDueDateToAlter, String loanDueDateTimeToAlter) throws Exception {
        boolean timeFlag = false;
        Timestamp timestamp;
        Pattern pattern;
        Matcher matcher;
        SimpleDateFormat fmt = new SimpleDateFormat(OLEConstants.OlePatron.PATRON_MAINTENANCE_DATE_FORMAT);

        if (StringUtils.isNotBlank(loanDueDateTimeToAlter)) {
            String[] str = loanDueDateTimeToAlter.split(":");
            pattern = Pattern.compile(OLEConstants.TIME_24_HR_PATTERN);
            matcher = pattern.matcher(loanDueDateTimeToAlter);
            timeFlag = matcher.matches();
            if (timeFlag) {
                if (str != null && str.length <= 2) {
                    loanDueDateTimeToAlter = loanDueDateTimeToAlter + OLEConstants.CHECK_IN_TIME_MS;
                }
                timestamp = Timestamp.valueOf(new SimpleDateFormat(OLEConstants.CHECK_IN_DATE_TIME_FORMAT).format(loanDueDateToAlter).concat(" ").concat(loanDueDateTimeToAlter));
            } else {
                throw new Exception();
            }
        } else if (fmt.format(loanDueDateToAlter).compareTo(fmt.format(new Date())) == 0) {
            timestamp = new Timestamp(new Date().getTime());
        } else {
            timestamp = Timestamp.valueOf(new SimpleDateFormat(OLEConstants.CHECK_IN_DATE_TIME_FORMAT).format(loanDueDateToAlter).concat(" ").concat(new SimpleDateFormat("HH:mm:ss").format(new Date())));
        }
        return timestamp;
    }
}