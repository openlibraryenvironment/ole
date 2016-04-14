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

    public  void logMessage(String directoryName, String fileName, String fileExtension, String content, boolean append) throws Exception {
        String reportDirectory = getReportDirectoryPath();
        String date = OleNGConstants.DATE_FORMAT.format(new Date());
        String file = getFile(reportDirectory,directoryName, fileName, fileExtension, date);
        FileUtils.write(new File(reportDirectory, directoryName + File.separator + file), content, append);
    }

    private String getFile(String reportDirectory, String directoryName, String fileName, String fileExtension, String date) {
        String fileStartingString = fileName + "-";
        File file = new File(reportDirectory, directoryName);
        if(file.exists() && file.isDirectory()) {
            File[] files = file.listFiles();
            if (null != files) {
                for (File subFiles : files) {
                    if (subFiles.isFile() && subFiles.getName().contains(fileStartingString)) {
                        return subFiles.getName();
                    }
                }
            }
        }
        return fileStartingString + date + "." + fileExtension;
    }

    public String getReportDirectoryPath() {
        return ConfigContext.getCurrentContextConfig().getProperty("report.directory");
    }
}
