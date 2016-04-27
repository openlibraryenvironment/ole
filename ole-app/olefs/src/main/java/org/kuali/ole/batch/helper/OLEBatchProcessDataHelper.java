package org.kuali.ole.batch.helper;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.kuali.ole.OLEConstants;
import org.kuali.ole.batch.bo.*;
import org.kuali.ole.docstore.common.document.content.bib.marc.BibMarcRecord;
import org.kuali.ole.docstore.model.xmlpojo.work.bib.marc.ControlField;
import org.kuali.ole.docstore.common.document.content.bib.marc.DataField;
import org.kuali.ole.docstore.common.document.content.bib.marc.SubField;
import org.kuali.ole.pojo.bib.BibliographicRecord;
import org.kuali.rice.core.api.config.property.ConfigContext;
import org.marc4j.MarcStreamWriter;
import org.marc4j.MarcWriter;
import org.marc4j.MarcXmlReader;
import org.marc4j.marc.Record;

import java.io.*;
import java.nio.file.FileSystems;
import java.util.*;

/**
 * Created with IntelliJ IDEA.
 * User: meenrajd
 * Date: 2/20/13
 * Time: 12:16 PM
 * To change this template use File | Settings | File Templates.
 */
public class OLEBatchProcessDataHelper {

    private String statingDirectory = ConfigContext.getCurrentContextConfig().getProperty("staging.directory");
    private static final Logger LOG = Logger.getLogger(OLEBatchProcessDataHelper.class);

    private static volatile OLEBatchProcessDataHelper exportDataHelper;


    private static final String applicationUrl = ConfigContext.getCurrentContextConfig().getProperty(OLEConstants.OLEBatchProcess.BATCH_EXPORT_PATH_APP_URL);
    private static final String homeDirectory = ConfigContext.getCurrentContextConfig().getProperty(OLEConstants.USER_HOME_DIRECTORY);

    private OLEBatchProcessDataHelper() {
    }

    public static synchronized OLEBatchProcessDataHelper getInstance() {
        if (exportDataHelper == null) {
            exportDataHelper = new OLEBatchProcessDataHelper();
        }
        return exportDataHelper;
    }

    /**
     * deletes the Marc Fields and subfileds of the given BibRecord for the given Batch process profile
     *
     * @param oleBatchProcessProfileBo
     * @param record
     */
    public void deleteFieldsSubfields(OLEBatchProcessProfileBo oleBatchProcessProfileBo, BibMarcRecord record) {
        List<OLEBatchProcessProfileDeleteField> deleteFieldList = oleBatchProcessProfileBo.getOleBatchProcessProfileDeleteFieldsList();
        for (OLEBatchProcessProfileDeleteField deleteField : deleteFieldList) {
            if (StringUtils.isNotEmpty(deleteField.getSubField())) {
                deleteMarcSubFields(record, deleteField);
            } else if (StringUtils.isNotEmpty(deleteField.getTag())) {
                deleteMarcFields(record, deleteField);
            }
        }
    }

    /**
     * deletes marc fields
     *
     * @param record
     * @param deleteField
     */
    public void deleteMarcFields(BibMarcRecord record, OLEBatchProcessProfileDeleteField deleteField) {
        List<DataField> dataFieldList = record.getDataFields();
        List<Integer> indices = findDataFieldIndex(dataFieldList, deleteField.getTag(), deleteField.getFirstIndicator(), deleteField.getSecondIndicator());
        for (int i = indices.size() - 1; i >= 0; i--) {
            dataFieldList.remove(indices.get(i).intValue());
        }
    }

    public void addMarcFields(BibMarcRecord record, OLEBatchProcessProfileRenameField renameField) {
        List<DataField> dataFieldList = record.getDataFields();
        DataField dataField = new DataField();
        dataField.setTag(renameField.getRenamedTag());
        dataField.setInd1(renameField.getRenamedFirstIndicator());
        dataField.setInd2(renameField.getRenamedSecondIndicator());
        SubField subField = new SubField();
        if (renameField.getRenamedSubField().contains("$")) {
            renameField.setRenamedSubField(renameField.getRenamedSubField().substring(1, renameField.getRenamedSubField().length()));
        }
        subField.setCode(renameField.getRenamedSubField());
        subField.setValue(renameField.getRenamedSubFieldContains());
        ListIterator<DataField> iterator = dataFieldList.listIterator();
        if (renameField.getOriginalSubField().contains("$")) {
            renameField.setOriginalSubField(renameField.getOriginalSubField().substring(1, renameField.getOriginalSubField().length()));
        }
        while (iterator.hasNext()) {
            DataField dField = iterator.next();
            if (dField.getTag().equals(renameField.getOriginalTag())) {
                Iterator<SubField> subFieldIterator = dField.getSubFields().iterator();
                while (subFieldIterator.hasNext()) {
                    SubField sField = subFieldIterator.next();
                    if (sField.getCode().equals(renameField.getOriginalSubField())) {
                        if (StringUtils.isEmpty(renameField.getRenamedSubFieldContains())) {
                            subField.setValue(sField.getValue());
                        }
                        dataField.addSubField(subField);
                    }
                }
            }
        }
        dataFieldList.add(dataField);
    }


    /**
     * deletes marc subfields
     *
     * @param record
     * @param deleteField
     */
    public void deleteMarcSubFields(BibMarcRecord record, OLEBatchProcessProfileDeleteField deleteField) {
        List<DataField> dataFieldList = record.getDataFields();
        boolean hasEmptySubFields = false;
        for (DataField dataField : dataFieldList) {
            if (!dataField.getTag().equals(deleteField.getTag())) continue;

            List<Integer> indices = findSubFieldIndex(dataField.getSubFields(), deleteField.getSubField(), deleteField.getSubFieldContains());
            for (int i = indices.size() - 1; i >= 0; i--) {
                dataField.getSubFields().remove(indices.get(i).intValue());
            }
            if(dataField.getSubFields().isEmpty()){
                hasEmptySubFields=true;
            }
        }
        if(hasEmptySubFields)
            deleteEmptyFields(dataFieldList);
    }

    /**
     * deletes emply dataFields
     * @param dataFieldList
     */
    private void deleteEmptyFields(List<DataField>dataFieldList){
        Iterator<DataField> itr = dataFieldList.iterator();
        while(itr.hasNext()){
            DataField dataField = itr.next();
            if(dataField.getSubFields().isEmpty()){
                itr.remove();
            }
        }

    }

    /**
     * renames the marc fields ans subfields
     *
     * @param oleBatchProcessProfileBo
     * @param record
     */
    public void renameMarcFieldsSubFields(OLEBatchProcessProfileBo oleBatchProcessProfileBo, BibMarcRecord record) {
        List<OLEBatchProcessProfileRenameField> renameList = oleBatchProcessProfileBo.getOleBatchProcessProfileRenameFieldsList();
        for (OLEBatchProcessProfileRenameField renameField : renameList) {
            if (StringUtils.isNotEmpty(renameField.getOriginalSubField()) && StringUtils.isNotEmpty(renameField.getRenamedSubField())
                    && !renameField.getOriginalSubField().equals(renameField.getRenamedSubField())) {
                renameMarcSubFields(record, renameField);
            }
            if (StringUtils.isNotEmpty(renameField.getOriginalTag()) && StringUtils.isNotEmpty(renameField.getRenamedTag())
                    && !renameField.getOriginalTag().equals(renameField.getRenamedSubField())) {
                renameMarcFields(record, renameField);
            }
        }
    }

    /**
     * renames marc fields and indicators
     *
     * @param record
     * @param renameField
     */
    public void renameMarcFields(BibMarcRecord record, OLEBatchProcessProfileRenameField renameField) {
        List<DataField> dataFieldList = record.getDataFields();
        List<Integer> indices = findDataFieldIndex(dataFieldList, renameField.getOriginalTag(), renameField.getOriginalFirstIndicator(), renameField.getOriginalSecondIndicator());
        for (int i = 0; i < indices.size(); i++) {
            DataField dataField = dataFieldList.get(indices.get(i));
            if (StringUtils.isNotEmpty(renameField.getRenamedFirstIndicator()))
                dataField.setInd1(renameField.getRenamedFirstIndicator());
            if (StringUtils.isNotEmpty(renameField.getRenamedSecondIndicator()))
                dataField.setInd2(renameField.getRenamedSecondIndicator());
            if (StringUtils.isNotEmpty(renameField.getRenamedTag())) dataField.setTag(renameField.getRenamedTag());
        }


    }

    /**
     * renames marc subfields
     *
     * @param record
     * @param renameField
     */
    public void renameMarcSubFields(BibMarcRecord record, OLEBatchProcessProfileRenameField renameField) {
        List<DataField> dataFieldList = record.getDataFields();
        for (DataField dataField : dataFieldList) {
            if (!dataField.getTag().equals(renameField.getOriginalTag())) continue;
            List<Integer> indices = findSubFieldIndex(dataField.getSubFields(), renameField.getOriginalSubField(), renameField.getOriginalSubFieldContains());
            for (int i = 0; i < indices.size(); i++) {
                SubField subField = dataField.getSubFields().get(indices.get(i));
                if (StringUtils.isNotEmpty(renameField.getRenamedSubField()))
                    subField.setCode(renameField.getRenamedSubField());
                if (StringUtils.isNotEmpty(renameField.getRenamedSubFieldContains()))
                    subField.setValue(renameField.getRenamedSubFieldContains());
            }
        }
    }

    /**
     * Method will return the index of the matchin tag , indicator 1 and indicator 2 for the giving dataFieldList
     * for null indicators only tag is matched
     *
     * @param dataFieldList
     * @param tag
     * @param ind1
     * @param ind2
     * @return
     */
    public List<Integer> findDataFieldIndex(List<DataField> dataFieldList, String tag, String ind1, String ind2) {
        if (StringUtils.isEmpty(tag)) return Collections.emptyList();
        List<Integer> indices = new ArrayList<Integer>();
        for (int i = 0; i < dataFieldList.size(); i++) {
            if (!tag.equals(dataFieldList.get(i).getTag())) continue;
            if (StringUtils.isNotEmpty(ind1) ? !ind1.equals(dataFieldList.get(i).getInd1()) : false) continue;
            if (StringUtils.isNotEmpty(ind2) ? !ind2.equals(dataFieldList.get(i).getInd2()) : false) continue;
            indices.add(i);
        }
        return indices;
    }

    /**
     * Method will return the index of the matching subfield and subfield content for the given subFieldList
     * For null content only the subfield will be matched
     *
     * @param subFieldList
     * @param subField
     * @param content
     * @return
     */
    public List<Integer> findSubFieldIndex(List<SubField> subFieldList, String subField, String content) {
        if (StringUtils.isEmpty(subField)) return Collections.emptyList();
        List<Integer> indices = new ArrayList<Integer>();
        for (int i = 0; i < subFieldList.size(); i++) {
            if(subField.contains("$")){
                subField = subField.substring(1);
            }
            if (StringUtils.isNotEmpty(subField) ? !subField.equals(subFieldList.get(i).getCode()) : true) continue;
            if (StringUtils.isNotEmpty(content) ? !subFieldList.get(i).getValue().contains(content) : false) continue;
            indices.add(i);
        }
        return indices;
    }

    /**
     * create and return the dirctory based on batch process type in the server file system for batch delete
     * @param batchProceesType
     * @param jobId
     * @return
     */
    public String getBatchProcessFilePath(String batchProceesType, String jobId) {
        String batchProcessLocation = getBatchProcessFilePath(batchProceesType);
        if (batchProcessLocation != null) {
            batchProcessLocation = batchProcessLocation + jobId + FileSystems.getDefault().getSeparator();
            File file = new File(batchProcessLocation);
            if (!file.isDirectory()) {
                file.mkdir();
            }
        }
        return batchProcessLocation;
    }


    /**
     * create and return the dirctory based on batch process type in the server file system
     * @param batchProceesType
     * @return
     */
    public String getBatchProcessFilePath(String batchProceesType) {

        String batchProcessLocation = null;
        if (batchProceesType.equals(OLEConstants.OLEBatchProcess.BATCH_DELETE)) {
            batchProcessLocation = ConfigContext.getCurrentContextConfig().getProperty(OLEConstants.OLEBatchProcess.BATCH_DELETE_DIR_PATH) + "/";
        } else if (batchProceesType.equalsIgnoreCase(OLEConstants.OLEBatchProcess.BATCH_BIB_IMPORT)) {
            batchProcessLocation = ConfigContext.getCurrentContextConfig().getProperty(OLEConstants.OLEBatchProcess.BATCH_BIB_IMPORT_DIR_PATH) + "/";
        } else if (batchProceesType.equals(OLEConstants.OLEBatchProcess.BATCH_INVOICE)) {
            batchProcessLocation = ConfigContext.getCurrentContextConfig().getProperty(OLEConstants.OLEBatchProcess.BATCH_INVOICE_DIR_PATH) + "/";
        } else if (batchProceesType.equals(OLEConstants.OLEBatchProcess.ORDER_RECORD_IMPORT)) {
            batchProcessLocation = ConfigContext.getCurrentContextConfig().getProperty(OLEConstants.OLEBatchProcess.BATCH_ORDER_RECORD_IMPORT_DIR_PATH) + "/";
        } else if (batchProceesType.equals(OLEConstants.OLEBatchProcess.PATRON_IMPORT)) {
            batchProcessLocation = ConfigContext.getCurrentContextConfig().getProperty(OLEConstants.OLEBatchProcess.BATCH_PATRON_IMPORT_DIR_PATH) + "/";
        } else if (batchProceesType.equals(OLEConstants.OLEBatchProcess.LOCATION_IMPORT)) {
            batchProcessLocation = ConfigContext.getCurrentContextConfig().getProperty(OLEConstants.OLEBatchProcess.BATCH_LOCATION_IMPORT_DIR_PATH) + "/";
        } else if (batchProceesType.equals(OLEConstants.OLEBatchProcess.BATCH_EXPORT)) {
            batchProcessLocation = ConfigContext.getCurrentContextConfig().getProperty(OLEConstants.OLEBatchProcess.BATCH_EXPORT_DIR_PATH) + "/";
        } else if (batchProceesType.equals(OLEConstants.OLEBatchProcess.SERIAL_RECORD_IMPORT)) {
            batchProcessLocation = ConfigContext.getCurrentContextConfig().getProperty(OLEConstants.OLEBatchProcess.SERIAL_IMPORT_DIR_PATH) + "/";
        } else if (batchProceesType.equals(OLEConstants.OLEBatchProcess.FUND_RECORD_IMPORT)) {
            batchProcessLocation = ConfigContext.getCurrentContextConfig().getProperty(OLEConstants.OLEBatchProcess.FUND_CODE_IMPORT_DIR_PATH) + "/";
        } else if (batchProceesType.equals(OLEConstants.OLEBatchProcess.GOKB_IMPORT)) {
            batchProcessLocation = ConfigContext.getCurrentContextConfig().getProperty(OLEConstants.OLEBatchProcess.BATCH_GOKB_IMPORT_DIR_PATH) + "/";
        }


        if (batchProcessLocation != null) {
            File file = new File(batchProcessLocation);
            if (!file.isDirectory()) {
                file.mkdir();
            }
        }
        return batchProcessLocation;
    }

    /**
     * Create the failure record file in the batch process type dirctory
     *
     * @param failureRecordData
     * @param batchProcessType
     * @param batchFileName
     * @throws Exception
     */
    public void createBatchFailureFile(String failureRecordData, String batchProcessType, String batchFileName, String jobId) throws Exception {
        String fileLocation = getBatchProcessFilePath(batchProcessType , jobId);
        //if (batchProcessType.equals(OLEConstants.OLEBatchProcess.BATCH_DELETE) || batchProcessType.equals(OLEConstants.OLEBatchProcess.SERIAL_RECORD_IMPORT)) {
        //    fileLocation = getBatchProcessFilePath(batchProcessType, jobId);
       // } else {
       //     fileLocation = getBatchProcessFilePath(batchProcessType);
      //  }
        BufferedWriter out = new BufferedWriter(new FileWriter(fileLocation + batchFileName));
        out.write(failureRecordData);
        out.close();

    }

    /**
     * Create the failure record file in the batch process type directory for batch delete
     *
     * @param successRecordData
     * @param batchProcessType
     * @param batchFileName
     * @throws Exception
     */
    public void createBatchSuccessFile(String successRecordData, String batchProcessType, String batchFileName, String jobId) throws Exception {
        String fileLocation =  fileLocation = getBatchProcessFilePath(batchProcessType, jobId);
      //  if (batchProcessType.equals(OLEConstants.OLEBatchProcess.BATCH_DELETE)) {
      //      fileLocation = getBatchProcessFilePath(batchProcessType, jobId);
     //   }
        BufferedWriter out = new BufferedWriter(new FileWriter(fileLocation + batchFileName));
        out.write(successRecordData);
        out.close();

    }

    /**
     * Create the failure record file in the batch process type directory for batch delete
     *
     * @param failureReportData
     * @param batchProcessType
     * @param batchFileName
     * @throws Exception
     */
    public void createBatchDeleteFailureReportFile(String failureReportData, String batchProcessType, String batchFileName, String jobId) throws Exception {
        String fileLocation = getBatchProcessFilePath(batchProcessType, jobId);
        //if (batchProcessType.equals(OLEConstants.OLEBatchProcess.BATCH_DELETE)) {
        //    fileLocation = getBatchProcessFilePath(batchProcessType, jobId);
       // }
        BufferedWriter out = new BufferedWriter(new FileWriter(fileLocation + batchFileName));
        out.write(failureReportData);
        out.close();

    }


    /**
     * Delete the failure record file in the batch process type dirctory
     */
    public void deleteBatchFailureFile(String batchProcessType, String batchFileName) throws Exception {

        File file = new File(getBatchProcessFilePath(batchProcessType) + batchFileName);
        if (file.exists()) {
            file.delete();
        }


    }


    /**
     * Delete the upload file in the batch process type dirctory
     */
    public void deleteBatchFile(String batchProcessType, String batchFileName, String jobId) throws Exception {

        String fileLocation = getBatchProcessFilePath(batchProcessType, jobId);
       // if (batchProcessType.equals(OLEConstants.OLEBatchProcess.BATCH_DELETE)) {
        //    fileLocation = getBatchProcessFilePath(batchProcessType, jobId);
        //} else {
        //    fileLocation = getBatchProcessFilePath(batchProcessType);
        //}
        File file = new File(fileLocation + batchFileName);
        if (file.exists()) {
            file.delete();
        }
    }

    /**
     * Get the upload file content from the batch process type dirctory by using job id an upload file name
     */
    public String getBatchProcessFileContent(String batchProcessType, String batchFileName, String jobId) throws Exception {

        String fileLocation = getBatchProcessFilePath(batchProcessType, jobId);
        /*if (batchProcessType.equals(OLEConstants.OLEBatchProcess.BATCH_DELETE)) {*/
        //    fileLocation = getBatchProcessFilePath(batchProcessType, jobId);
        /*} else {
            fileLocation = getBatchProcessFilePath(batchProcessType);
        }*/
        File file = new File(fileLocation + batchFileName);
        if (file.exists()) {
            BufferedReader br = new BufferedReader(new FileReader(file));
            StringBuilder sb = new StringBuilder();
            String line = br.readLine();
            while (line != null) {
                sb.append(line);
                line = br.readLine();
                if (line != null) {
                    sb.append("\n");
                }
            }
            return sb.toString();
        }
        return null;
    }

    /**
     * Copy the upload batch file in to the batch process file directory
     */
    public void createBatchProcessFile(String batchProcessType, String batchFileName, String fileContent, String jobId) throws Exception {
        String  fileLocation = getBatchProcessFilePath(batchProcessType, jobId);
        //if (batchProcessType.equals(OLEConstants.OLEBatchProcess.BATCH_DELETE)) {
        // fileLocation = getBatchProcessFilePath(batchProcessType, jobId);
        /*} else {
            fileLocation = getBatchProcessFilePath(batchProcessType);
        }*/

        if (fileLocation != null) {
            BufferedWriter out = new BufferedWriter(new FileWriter(fileLocation + batchFileName));
            out.write(fileContent);
            out.close();
        }
    }


    /**
     * Copy the upload batch file in to the batch process file directory
     */
    public void createBatchProcessFile(String batchProcessType, String mrcFileName, String ediFileName, String mrcfileContent, String ediFileContent , String jobId) throws Exception {
        String fileLocation = getBatchProcessFilePath(batchProcessType , jobId);
        if (fileLocation != null) {
            BufferedWriter mrcOut = new BufferedWriter(new FileWriter(fileLocation + mrcFileName));
            mrcOut.write(mrcfileContent);
            mrcOut.close();
            BufferedWriter ediOut = new BufferedWriter(new FileWriter(fileLocation + ediFileName));
            ediOut.write(ediFileContent);
            ediOut.close();
        }
    }

    public void createBatchProcessFile(String batchProcessType, String documentFileName, String typeFileName, String historyFileName, String documentFileContent, String typeFileContent, String historyFileContent , String jobId) throws Exception {
        String fileLocation = getBatchProcessFilePath(batchProcessType ,jobId);
        if (fileLocation != null) {
            if (documentFileName != null) {
                BufferedWriter documentOut = new BufferedWriter(new FileWriter(fileLocation + documentFileName));
                documentOut.write(documentFileContent);
                documentOut.close();
            }
            if (typeFileName != null) {
                BufferedWriter typeOut = new BufferedWriter(new FileWriter(fileLocation + typeFileName));
                typeOut.write(typeFileContent);
                typeOut.close();
            }
            if (historyFileName != null) {
                BufferedWriter historyOut = new BufferedWriter(new FileWriter(fileLocation + historyFileName));
                historyOut.write(historyFileContent);
                historyOut.close();
            }
        }
    }

    public void createFundCodeBatchProcessFile(String batchProcessType, String documentFileName, String accountFileName, String documentFileContent, String accountFileContent, String jobId) throws Exception {
        String fileLocation = getBatchProcessFilePath(batchProcessType ,jobId);
        if (fileLocation != null) {
            if (documentFileName != null) {
                BufferedWriter documentOut = new BufferedWriter(new FileWriter(fileLocation + documentFileName));
                documentOut.write(documentFileContent);
                documentOut.close();
            }
            if (accountFileName != null) {
                BufferedWriter accountOut = new BufferedWriter(new FileWriter(fileLocation + accountFileName));
                accountOut.write(accountFileContent);
                accountOut.close();
            }
        }
    }

    public void createBatchBibImportFailureFile(String failureRecordData, String batchProcessType, String batchFileName, String jobId) throws Exception {
        String fileLocation = getBatchProcessFilePath(batchProcessType,jobId);
        String filePath = fileLocation + FileSystems.getDefault().getSeparator() + batchFileName;
        createMarcRecord(failureRecordData, filePath);
    }

    public void createFile(String[] content, String batchProcessType, String batchFileName, String jobId) throws Exception {
        String fileLocation = getBatchProcessFilePath(batchProcessType,jobId);
        String filePath = fileLocation + FileSystems.getDefault().getSeparator() + batchFileName;
        File fileToWrite = new File(filePath);
        if (!fileToWrite.exists()) {
            fileToWrite.getParentFile().mkdirs();
            fileToWrite.createNewFile();
        }
        BufferedWriter out = new BufferedWriter(new FileWriter(filePath ,true));
        for (String id : content) {
            out.write(id);
            out.newLine();
        }
        out.close();
    }


    public void createMarcRecord(String marcRecordContent, String filePath) throws Exception {
        File fileToWrite = new File(filePath);
        FileOutputStream fileOutputStream = new FileOutputStream(fileToWrite);
        //String bibContent = StringUtils.join(bibDocList, "");
        InputStream input = new ByteArrayInputStream(marcRecordContent.getBytes("UTF-8"));
        if (!fileToWrite.exists()) {
            fileToWrite.getParentFile().mkdirs();
            fileToWrite.createNewFile();
        }
        try {
            MarcXmlReader marcXmlReader = new MarcXmlReader(input);
            MarcWriter writer = new MarcStreamWriter(fileOutputStream, "UTF-8");

            while (marcXmlReader.hasNext()) {
                Record record = marcXmlReader.next();
                writer.write(record);
            }
            writer.close();
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    public String getExportPathUrl(OLEBatchProcessJobDetailsBo job) {
        //String filePath = StringUtils.substringAfter(job.getUploadFileName(), homeDirectory);
        //return applicationUrl + FileSystems.getDefault().getSeparator() + OLEConstants.OLEBatchProcess.HOME + filePath;
        return applicationUrl + OLEConstants.OLEBatchProcess.BATCH_CHANNEL_STRING + applicationUrl +
                OLEConstants.OLEBatchProcess.DIRECTORY_LIST_LOCATION + job.getUploadFileName().replace(statingDirectory,"");
    }

    public String getBibPathUrl(OLEBatchProcessJobDetailsBo job) {
        return applicationUrl + OLEConstants.OLEBatchProcess.BATCH_CHANNEL_STRING + applicationUrl
                + OLEConstants.OLEBatchProcess.DIRECTORY_LIST_LOCATION  + getBatchProcessFilePath(job.getBatchProcessType(), job.getJobId()).replace(statingDirectory, "");
    }

    public String getDeletePathUrl(OLEBatchProcessJobDetailsBo job) {
        return applicationUrl + OLEConstants.OLEBatchProcess.BATCH_CHANNEL_STRING + applicationUrl
                + OLEConstants.OLEBatchProcess.DIRECTORY_LIST_LOCATION + getBatchProcessFilePath(job.getBatchProcessType(), job.getJobId()).replace(statingDirectory, "");
    }

    public String getSerialCSVPathUrl(OLEBatchProcessJobDetailsBo job) {
        return applicationUrl + OLEConstants.OLEBatchProcess.BATCH_CHANNEL_STRING + applicationUrl
                + OLEConstants.OLEBatchProcess.DIRECTORY_LIST_LOCATION + getBatchProcessFilePath(job.getBatchProcessType(), job.getJobId()).replace(statingDirectory, "");
    }

    public String getFundCodeCSVPathUrl(OLEBatchProcessJobDetailsBo job) {
        return applicationUrl + OLEConstants.OLEBatchProcess.BATCH_CHANNEL_STRING + applicationUrl
                + OLEConstants.OLEBatchProcess.DIRECTORY_LIST_LOCATION + getBatchProcessFilePath(job.getBatchProcessType(), job.getJobId()).replace(statingDirectory, "");
    }
}
