package org.kuali.ole;

import org.kuali.ole.pojo.edi.*;

import java.util.Arrays;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: htcuser
 * Date: 4/5/12
 * Time: 2:41 AM
 * To change this template use File | Settings | File Templates.
 */
public enum SummaryFixture {
    SUMMARYSECTION("S", null, null, null, null),
    CONTROLINFORMATION(null, "1", "00033", null, null),
    UNT(null, null, null, "00057", "34"),;

    private String separatorInformation;
    private String controlQualifier;
    private String totalQuantitySegments;
    private String segmentNumber;
    private String linSegmentTotal;

    private SummaryFixture(String separatorInformation, String controlQualifier,
                           String totalQuantitySegments, String segmentNumber,
                           String linSegmentTotal) {
        this.separatorInformation = separatorInformation;
        this.controlQualifier = controlQualifier;
        this.totalQuantitySegments = totalQuantitySegments;
        this.segmentNumber = segmentNumber;
        this.linSegmentTotal = linSegmentTotal;
    }

    public SummarySection createSummarySectionPojo(Class clazz) {
        SummarySection summarySection = null;
        try {
            summarySection = (SummarySection) clazz.newInstance();
        } catch (InstantiationException e) {
            throw new RuntimeException("SummarySection creation failed. class = " + clazz);
        } catch (IllegalAccessException e) {
            throw new RuntimeException("SummarySection creation failed. class = " + clazz);
        }


        summarySection.setSeparatorInformation(separatorInformation);

        return summarySection;
    }

    public ControlInfomation createControlInfomationPojo(Class clazz) {
        ControlInfomation controlInfomation = null;
        try {
            controlInfomation = (ControlInfomation) clazz.newInstance();
        } catch (InstantiationException e) {
            throw new RuntimeException("ControlInfomation creation failed. class = " + clazz);
        } catch (IllegalAccessException e) {
            throw new RuntimeException("ControlInfomation creation failed. class = " + clazz);
        }
        Control control = new Control();
        control.setControlQualifier(controlQualifier);
        control.setTotalQuantitySegments(totalQuantitySegments);
        controlInfomation.addControlField(control);
        // controlInfomation.setControl(Arrays.asList(control));
        return controlInfomation;
    }

    public UNTSummary createUNTSummaryPojo(Class clazz) {
        UNTSummary untSummary = null;
        try {
            untSummary = (UNTSummary) clazz.newInstance();
        } catch (InstantiationException e) {
            throw new RuntimeException("UNTSummary creation failed. class = " + clazz);
        } catch (IllegalAccessException e) {
            throw new RuntimeException("UNTSummary creation failed. class = " + clazz);
        }
        untSummary.setSegmentNumber(segmentNumber);
        untSummary.setLinSegmentTotal(linSegmentTotal);
        return untSummary;
    }

}
