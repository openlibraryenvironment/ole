package org.kuali.ole.deliver.service;

import org.apache.commons.lang3.StringUtils;
import org.junit.Test;
import org.kuali.ole.OLEConstants;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by pvsubrah on 8/12/15.
 */
public class DateFormatHelperTest {

    @Test
    public void generateDateStringsForMySQL() {
        String generateDateStringsForMySQL = DateFormatHelper.getInstance().generateDateStringsForMySQL("3/3/2015");
        System.out.println(generateDateStringsForMySQL);
    }

    @Test
    public void generateDateStringsForMySQLFromTimeStamp() throws Exception {
        Timestamp timeStamp = getTimeStamp(new SimpleDateFormat("MM/dd/yyyy").parse("08/20/2015"), "02:36:00");

        String generateDateStringsForMySQL = DateFormatHelper.getInstance().generateDateStringsForMySQL(timeStamp);
        System.out.println(generateDateStringsForMySQL);
    }
    @Test
    public void generateDateStringsForMyOracleFromTimeStamp() throws Exception {
        Timestamp timeStamp = getTimeStamp(new SimpleDateFormat("MM/dd/yyyy").parse("08/20/2015"), "02:36:00");

        String generateDateStringsForMySQL = DateFormatHelper.getInstance().generateDateStringsForOracle(timeStamp);
        System.out.println(generateDateStringsForMySQL);
    }

    @Test
    public void generateDateStringsForOracle() {
        String generateDateStringsForOracle = DateFormatHelper.getInstance().generateDateStringsForOracle("3/3/2015");
        System.out.println(generateDateStringsForOracle);
    }

    @Test
    public void generateTimeStampBasedOnDateString() throws Exception {
        Timestamp timeStamp = getTimeStamp(new SimpleDateFormat("MM/dd/yyyy").parse("08/20/2015"), "23:36:00");
        System.out.println(timeStamp);
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