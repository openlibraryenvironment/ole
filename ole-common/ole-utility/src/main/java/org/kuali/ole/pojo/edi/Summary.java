package org.kuali.ole.pojo.edi;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: palanivel
 * Date: 3/2/12
 * Time: 3:01 PM
 * To change this template use File | Settings | File Templates.
 */
public class Summary {
    private SummarySection summarySection;
    private List<ControlInfomation> controlInfomation = new ArrayList<ControlInfomation>();
    private List<MonetarySummary> monetarySummary = new ArrayList<>();
    private UNTSummary untSummary;


    public void addControlInfomation(ControlInfomation controlInfomation) {
        if (!this.controlInfomation.contains(controlInfomation)) {
            this.controlInfomation.add(controlInfomation);
        }
    }

    public void addMonetarySummary(MonetarySummary monetarySummary) {
        if (!this.monetarySummary.contains(monetarySummary)) {
            this.monetarySummary.add(monetarySummary);
        }
    }

    public SummarySection getSummarySection() {
        return summarySection;
    }

    public void setSummarySection(SummarySection summarySection) {
        this.summarySection = summarySection;
    }

    public List<ControlInfomation> getControlInfomation() {
        return controlInfomation;
    }

    public void setControlInfomation(List<ControlInfomation> controlInfomation) {
        this.controlInfomation = controlInfomation;
    }

    public UNTSummary getUntSummary() {
        return untSummary;
    }

    public void setUntSummary(UNTSummary untSummary) {
        this.untSummary = untSummary;
    }

    public List<MonetarySummary> getMonetarySummary() {
        return monetarySummary;
    }

    public void setMonetarySummary(List<MonetarySummary> monetarySummary) {
        this.monetarySummary = monetarySummary;
    }
}
