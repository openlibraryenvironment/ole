package org.kuali.ole.pojo.edi;

import java.util.ArrayList;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: palanivel
 * Date: 7/26/13
 * Time: 4:58 PM
 * To change this template use File | Settings | File Templates.
 */
public class MonetarySummary {

    private List<MonetarySummaryInformation> monetarySummaryInformation = new ArrayList<>();


    public void addMonetarySummaryInformation(MonetarySummaryInformation monetarySummaryInformation) {
        if (!this.monetarySummaryInformation.contains(monetarySummaryInformation)) {
            this.monetarySummaryInformation.add(monetarySummaryInformation);
        }
    }

    public List<MonetarySummaryInformation> getMonetarySummaryInformation() {
        return monetarySummaryInformation;
    }

    public void setMonetarySummaryInformation(List<MonetarySummaryInformation> monetarySummaryInformation) {
        this.monetarySummaryInformation = monetarySummaryInformation;
    }
}
