package org.kuali.ole;

import org.kuali.ole.deliver.bo.OlePatronDocument;
import org.kuali.ole.deliver.bo.OlePatronIngestSummaryRecord;
import org.kuali.ole.ingest.FileUtil;
import org.kuali.ole.ingest.OlePatronXMLSchemaValidator;
import org.kuali.ole.ingest.pojo.OlePatron;
import org.kuali.ole.service.OlePatronConverterService;
import org.kuali.rice.core.api.impex.xml.XmlDocCollection;
import org.kuali.rice.core.api.impex.xml.XmlIngesterService;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.InputStream;
import java.util.*;

/**
 * Created by pvsubrah on 12/9/13.
 */
public class PatronsIngesterService implements XmlIngesterService {
    private OlePatronConverterService olePatronConverterService;
    private FileUtil fileUtil;
    private OlePatronXMLSchemaValidator olePatronXMLSchemaValidator;

    @Override
    public Collection<XmlDocCollection> ingest(List<XmlDocCollection> xmlDocCollections) throws Exception {
        List<XmlDocCollection> failedRecords = new ArrayList<>();

        for (Iterator<XmlDocCollection> iterator = xmlDocCollections.iterator(); iterator.hasNext(); ) {
            StringBuffer reportContent = new StringBuffer();
            XmlDocCollection xmlDocCollection = iterator.next();
            File file = xmlDocCollection.getFile();
            String fileContent = getFileUtil().readFile(file);

            InputStream inputStream = getByteArrayInputStream(fileContent);
            //boolean validContent = getOlePatronXMLSchemaValidator().validateContentsAgainstSchema(inputStream);
            Map validateResultMap = getOlePatronXMLSchemaValidator().validateContentsAgainstSchema(inputStream);
            boolean validContent = (boolean)validateResultMap.get(OLEConstants.OlePatron.PATRON_XML_ISVALID);
            String errorMessage = (String)validateResultMap.get(OLEConstants.OlePatron.PATRON_POLLERSERVICE_ERROR_MESSAGE);
            OlePatronIngestSummaryRecord olePatronIngestSummaryRecord = getOlePatronIngestSummaryRecord();
            if (validContent) {
                List<OlePatronDocument> successPatronDocuments = getOlePatronConverterService().persistPatronFromFileContent(fileContent, true, file.getName(), olePatronIngestSummaryRecord, null, "");
                for (OlePatronDocument olePatronDocument : successPatronDocuments) {
                    buildReportContent(olePatronDocument.getOlePatronId(), file.getName(), reportContent, "Success", null);
                }
                for (OlePatron olePatron : olePatronIngestSummaryRecord.getFailurePatronRecords()) {
                    buildReportContent(olePatron.getPatronID(), file.getName(), reportContent, "Failure", olePatron.getErrorMessage());
                }
            } else {
                failedRecords.add(xmlDocCollection);
                buildReportContent(null, file.getName(), reportContent, "Failure", errorMessage);
            }
            if (olePatronIngestSummaryRecord.getPatronFailedCount() > 0) {
                failedRecords.add(xmlDocCollection);
            }
            if (xmlDocCollection.getXmlDocs() != null && xmlDocCollection.getXmlDocs().size() > 0) {
                xmlDocCollection.getXmlDocs().get(0).setProcessingMessage(reportContent.toString());
            }
        }
        if (failedRecords.size() == 0) {
            return new LinkedList<>();
        } else {
            return failedRecords;
        }
    }

    protected OlePatronIngestSummaryRecord getOlePatronIngestSummaryRecord() {
        return new OlePatronIngestSummaryRecord();
    }

    protected ByteArrayInputStream getByteArrayInputStream(String fileContent) {
        return new ByteArrayInputStream(fileContent.getBytes());
    }

    private OlePatronXMLSchemaValidator getOlePatronXMLSchemaValidator() {
        if (null == olePatronXMLSchemaValidator) {
            olePatronXMLSchemaValidator = new OlePatronXMLSchemaValidator();
        }
        return olePatronXMLSchemaValidator;
    }

    public void setOlePatronXMLSchemaValidator(OlePatronXMLSchemaValidator olePatronXMLSchemaValidator) {
        this.olePatronXMLSchemaValidator = olePatronXMLSchemaValidator;
    }

    private FileUtil getFileUtil() {
        if (null == fileUtil) {
            fileUtil = new FileUtil();
        }
        return fileUtil;
    }

    public void setFileUtil(FileUtil fileUtil) {
        this.fileUtil = fileUtil;
    }

    @Override
    public Collection<XmlDocCollection> ingest(List<XmlDocCollection> xmlDocCollections, String principalId) throws Exception {
        return null;
    }

    public void setOlePatronConverterService(OlePatronConverterService olePatronConverterService) {
        this.olePatronConverterService = olePatronConverterService;
    }

    public OlePatronConverterService getOlePatronConverterService() {
        if (null == olePatronConverterService) {
            olePatronConverterService = new OlePatronConverterService();
        }
        return olePatronConverterService;
    }

    private void buildReportContent(String patronId, String importFileName, StringBuffer reportContent, String importStatus, String errorMessage) {
        reportContent.append(importStatus);
        reportContent.append(OLEConstants.COMMA);
        reportContent.append(OLEConstants.SPACE);
        reportContent.append(importFileName);
        if (patronId!=null){
            reportContent.append(OLEConstants.COMMA);
            reportContent.append(OLEConstants.SPACE);
            reportContent.append(patronId);
        }
        if (errorMessage!=null){
            reportContent.append(OLEConstants.COMMA);
            reportContent.append(OLEConstants.SPACE);
            reportContent.append(errorMessage);
        }
        reportContent.append("\n");
    }

}
