package org.kuali.ole;

import org.apache.camel.CamelContext;
import org.apache.camel.ProducerTemplate;

/**
 * Created by pvsubrah on 1/10/15.
 */
public class ReportLogHandler {

    private ProducerTemplate sedaProducer;
    static ReportLogHandler reportLogHandler;
    private String filePath;
    private String fileName;

    private ReportLogHandler() {
    }

    public static ReportLogHandler getInstance() {
        if(null == reportLogHandler){
            reportLogHandler = new ReportLogHandler();
        }
        return reportLogHandler;
    }

    public void logMessage(Object message) throws Exception {
        if(null != filePath && null != fileName) {
            CamelContext context = OleCamelContext.getInstance().getContext();
            if (null == sedaProducer) {
                sedaProducer = context.createProducerTemplate();
            }
            sedaProducer.sendBody("seda:messages", message);
        } else {
            throw new Exception("File Path and File Name required.");
        }
    }

    public void setFileNameAndPath(String filePath, String fileName) {
        this.filePath = filePath;
        this.fileName = fileName;
        OleCamelContext.getInstance().setFileNameAndPath(filePath, fileName);
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }
}
