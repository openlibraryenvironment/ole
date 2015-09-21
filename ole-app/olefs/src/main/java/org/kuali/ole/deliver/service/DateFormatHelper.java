package org.kuali.ole.deliver.service;

import java.sql.Timestamp;
import java.util.HashMap;
import java.util.Map;
import java.util.StringTokenizer;

/**
 * Created by pvsubrah on 4/3/15.
 */
public class DateFormatHelper {

    private static DateFormatHelper dateFormatHelper;
    private static Map map;

    private DateFormatHelper() {
        map = new HashMap();
        map.put("1", "Jan");
        map.put("01", "Jan");
        map.put("2", "Feb");
        map.put("02", "Feb");
        map.put("3", "Mar");
        map.put("03", "Mar");
        map.put("4", "Apr");
        map.put("04", "Apr");
        map.put("5", "May");
        map.put("05", "May");
        map.put("6", "Jun");
        map.put("06", "Jun");
        map.put("7", "Jul");
        map.put("07", "Jul");
        map.put("8", "Aug");
        map.put("08", "Aug");
        map.put("9", "Sep");
        map.put("09", "Sep");
        map.put("10", "Oct");
        map.put("11", "Nov");
        map.put("12", "Dec");
    }

    public static DateFormatHelper getInstance() {
        if (null == dateFormatHelper) {
            dateFormatHelper = new DateFormatHelper();
        }
        return dateFormatHelper;
    }

    public String generateDateStringsForMySQL(String dateString) {
        StringBuilder formattedDate = new StringBuilder();

        StringTokenizer stringTokenizer = new StringTokenizer(dateString, "/");
        while (stringTokenizer.hasMoreTokens()) {
            String month = stringTokenizer.nextToken();
            String day = stringTokenizer.nextToken();
            String year = stringTokenizer.nextToken();
            formattedDate.append("'").append(month).append(",").append(day).append(",").append(year).append("'").append(",'").append
                    ("%m,%d,%Y").append("'");
        }
        return formattedDate.toString();
    }

    public String generateDateStringsForMySQL(Timestamp timestamp) {

        StringBuilder formattedDate = new StringBuilder();

        String dateTimeString = timestamp.toString();

        StringTokenizer dateTimeTokenizer = new StringTokenizer(dateTimeString, " ");
        String date = dateTimeTokenizer.nextToken();
        String time = dateTimeTokenizer.nextToken();

        StringTokenizer dateTokenizer = new StringTokenizer(date, "-");

        String year = dateTokenizer.nextToken();
        String month = dateTokenizer.nextToken();
        String day = dateTokenizer.nextToken();

        StringTokenizer timeTokenizer = new StringTokenizer(time, ":");
        String hours = timeTokenizer.nextToken();
        String minutes = timeTokenizer.nextToken();
        String seconds = timeTokenizer.nextToken();

        formattedDate.append("'").append(month).append(",").append(day).append(",").append(year).append(",").append(hours+":"+minutes+":"+seconds.substring(0,2)).append("'").append(",'").append
                ("%m,%d,%Y,%H:%i:%s").append("'");


        return formattedDate.toString();
    }


    public String generateDateStringsForOracle(String dateString) {
        StringBuilder formattedDate = new StringBuilder();

        StringTokenizer stringTokenizer = new StringTokenizer(dateString, "/");
        while (stringTokenizer.hasMoreTokens()) {
            String month = stringTokenizer.nextToken();
            String day = stringTokenizer.nextToken();
            String year = stringTokenizer.nextToken();
            formattedDate.append(day).append("-").append(getOracleMonth(month)).append("-").append(year);
        }
        return formattedDate.toString();
    }


    public String generateDateStringsForOracle(Timestamp timestamp) {
        StringBuilder formattedDate = new StringBuilder();

        String dateTimeString = timestamp.toString();

        StringTokenizer dateTimeTokenizer = new StringTokenizer(dateTimeString, " ");
        String date = dateTimeTokenizer.nextToken();
        String time = dateTimeTokenizer.nextToken();

        StringTokenizer dateTokenizer = new StringTokenizer(date, "-");

        String year = dateTokenizer.nextToken();
        String month = dateTokenizer.nextToken();
        String day = dateTokenizer.nextToken();

        StringTokenizer timeTokenizer = new StringTokenizer(time, ":");
        String hours = timeTokenizer.nextToken();
        String minutes = timeTokenizer.nextToken();
        String seconds = timeTokenizer.nextToken().substring(0, 2);

        formattedDate.append("'").append(year).append("/").append(month).append("/").append(day).append(" ").append(hours+":"+minutes+":"+seconds).append("'").append(",'").append
                ("YYYY/MM/DD HH24:MI:SS").append("'");

        return formattedDate.toString();

    }


    private String getOracleMonth(String month) {

        return (String) map.get(month);
    }

}
