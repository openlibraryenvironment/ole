package org.kuali.ole.deliver.bo;

import org.apache.commons.lang3.StringUtils;
import org.junit.Test;
import org.kuali.ole.OLEConstants;
import org.kuali.ole.OLETestCaseBase;
import org.kuali.ole.deliver.service.OleLoanDocumentPlatformAwareDao;
import org.kuali.ole.deliver.util.BulkUpdateDataObject;
import org.kuali.ole.sys.context.SpringContext;

import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static junit.framework.TestCase.assertNotNull;

/**
 * Created by sheiksalahudeenm on 8/12/15.
 */
public class BatchUpdate_IT extends OLETestCaseBase {
    @Test
    public void batchUpdateTests() throws Exception {
        OleLoanDocumentPlatformAwareDao oleLoanDocumentPlatformAwareDao = (OleLoanDocumentPlatformAwareDao) SpringContext.getBean("OleLoanDocumentPlatformAwareDao");
        assertNotNull(oleLoanDocumentPlatformAwareDao);
        List<BulkUpdateDataObject> bulkUpdateDataObjects = new ArrayList<>();

        BulkUpdateDataObject bulkUpdateDataObject = new BulkUpdateDataObject();
        HashMap setClauseMap = new HashMap();
        setClauseMap.put("CURR_DUE_DT_TIME",getTimeStamp(new SimpleDateFormat("MM/dd/yyyy").parse("08/20/2015"), "23:36:00"));
        bulkUpdateDataObject.setSetClauseMap(setClauseMap);
        HashMap whereClauseMap = new HashMap();
        whereClauseMap.put("ITEM_UUID", "wio-457");
        bulkUpdateDataObject.setWhereClauseMap(whereClauseMap);
        bulkUpdateDataObjects.add(bulkUpdateDataObject);

        BulkUpdateDataObject bulkUpdateDataObject1 = new BulkUpdateDataObject();
        HashMap setClauseMap1 = new HashMap();
        setClauseMap1.put("CURR_DUE_DT_TIME",getTimeStamp(new SimpleDateFormat("MM/dd/yyyy").parse("08/20/2015"), "23:36:00"));
        bulkUpdateDataObject1.setSetClauseMap(setClauseMap1);
        HashMap whereClauseMap1 = new HashMap();
        whereClauseMap1.put("ITEM_UUID", "wio-1292");
        bulkUpdateDataObject1.setWhereClauseMap(whereClauseMap1);
        bulkUpdateDataObjects.add(bulkUpdateDataObject1);

        int[] resultCount = oleLoanDocumentPlatformAwareDao.updateLoanDocument(bulkUpdateDataObjects);
        System.out.println(Arrays.toString(resultCount));
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
