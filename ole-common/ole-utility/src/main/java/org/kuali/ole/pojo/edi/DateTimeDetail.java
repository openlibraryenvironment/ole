package org.kuali.ole.pojo.edi;


import java.util.ArrayList;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: palanivel
 * Date: 7/26/13
 * Time: 4:16 PM
 * To change this template use File | Settings | File Templates.
 */
public class DateTimeDetail {

    private List<DateTimeInformation> dateTimeInformationList = new ArrayList<>();

    public void addDateTimeInformation(DateTimeInformation dateTimeInformation) {
        if (!this.dateTimeInformationList.contains(dateTimeInformation)) {
            this.dateTimeInformationList.add(dateTimeInformation);
        }
    }

    public List<DateTimeInformation> getDateTimeInformationList() {
        return dateTimeInformationList;
    }

    public void setDateTimeInformationList(List<DateTimeInformation> dateTimeInformationList) {
        this.dateTimeInformationList = dateTimeInformationList;
    }
}
