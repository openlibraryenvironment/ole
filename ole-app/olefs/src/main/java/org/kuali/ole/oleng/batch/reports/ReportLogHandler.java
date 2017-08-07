package org.kuali.ole.oleng.batch.reports;

import org.apache.commons.collections.CollectionUtils;
import org.kuali.ole.oleng.batch.reports.processors.OleNGReportProcessor;

import java.util.Iterator;
import java.util.List;

/**
 * Created by SheikS on 2/18/2016.
 */
public class ReportLogHandler {

    protected List<OleNGReportProcessor> processors;

    public void logMessage(Object response, String directoryToWrite) {
        if(CollectionUtils.isNotEmpty(getProcessors())) {
            for (Iterator<OleNGReportProcessor> iterator = getProcessors().iterator(); iterator.hasNext(); ) {
                OleNGReportProcessor oleNGReportProcessor = iterator.next();
                try {
                    oleNGReportProcessor.process(response,directoryToWrite);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }

    }

    public List<OleNGReportProcessor> getProcessors() {
        return processors;
    }

    public void setProcessors(List<OleNGReportProcessor> processors) {
        this.processors = processors;
    }
}
