package org.kuali.ole.pojo.edi;

/**
 * Created with IntelliJ IDEA.
 * User: palanivel
 * Date: 7/26/13
 * Time: 4:22 PM
 * To change this template use File | Settings | File Templates.
 */
public class DateTimeInformation {
    private String periodQualifier;
    private String period;
    private String periodFormat;

    public String getPeriodQualifier() {
        return periodQualifier;
    }

    public void setPeriodQualifier(String periodQualifier) {
        this.periodQualifier = periodQualifier;
    }

    public String getPeriod() {
        return period;
    }

    public void setPeriod(String period) {
        this.period = period;
    }

    public String getPeriodFormat() {
        return periodFormat;
    }

    public void setPeriodFormat(String periodFormat) {
        this.periodFormat = periodFormat;
    }
}
