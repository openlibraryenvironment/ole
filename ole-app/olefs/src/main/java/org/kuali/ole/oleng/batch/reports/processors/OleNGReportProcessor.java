package org.kuali.ole.oleng.batch.reports.processors;

import org.apache.commons.io.FileUtils;
import org.kuali.ole.constants.OleNGConstants;
import org.kuali.rice.core.api.config.property.ConfigContext;

import java.io.File;
import java.util.Date;

/**
 * Created by SheikS on 2/22/2016.
 */
public abstract class OleNGReportProcessor {

    public abstract void process(Object object, String directoryToWrite) throws Exception;

    public void logMessage(String directoryName, String fileName, String fileExtension, String content) throws Exception {
        String reportDirectory = getReportDirectoryPath();
        String date = OleNGConstants.DATE_FORMAT.format(new Date());
        FileUtils.write(new File(reportDirectory, directoryName + File.separator + fileName + "-" + date + "." +  fileExtension), content);
    }

    public String getReportDirectoryPath() {
        return ConfigContext.getCurrentContextConfig().getProperty("report.directory");
    }
}
