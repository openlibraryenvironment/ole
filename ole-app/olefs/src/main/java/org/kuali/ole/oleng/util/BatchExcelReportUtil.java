package org.kuali.ole.oleng.util;

import org.apache.commons.io.output.ByteArrayOutputStream;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.kuali.ole.constants.OleNGConstants;
import org.kuali.ole.docstore.common.document.PHoldings;
import org.kuali.ole.docstore.common.response.*;
import org.kuali.ole.spring.batch.BatchUtil;

import java.io.IOException;
import java.util.*;

/**
 * Created by SheikS on 3/17/2016.
 */
public class BatchExcelReportUtil extends BatchUtil{

    private int GREEN_COLOR = HSSFColor.GREEN.index;

    private int addHeaders(XSSFWorkbook workbook, XSSFSheet spreadsheet, int rowId, String[] headerArray, Integer color) {
        CellStyle style = workbook.createCellStyle();
        Font font = workbook.createFont();
        font.setBoldweight(Font.BOLDWEIGHT_BOLD);
        if(null != color) {
            font.setColor(color.shortValue());
        }
        style.setFont(font);
        XSSFRow mainSectionHeaderRow = spreadsheet.createRow(rowId++);
        int cellId = 0;
        for (String header : headerArray)
        {
            Cell cell = mainSectionHeaderRow.createCell(cellId++);
            cell.setCellStyle(style);
            cell.setCellValue(header);
        }
        return rowId;
    }

    private int addSection(XSSFWorkbook workbook, XSSFSheet spreadsheet, int rowId,String[] header, List<List<String>> contents,
                            String sectionTitle) {
        spreadsheet.createRow(rowId++);
        spreadsheet.createRow(rowId++);
        rowId = addHeaders(workbook, spreadsheet, rowId, new String[]{sectionTitle}, GREEN_COLOR);
        rowId = addHeaders(workbook, spreadsheet, rowId, header, null);
        for (Iterator<List<String>> iterator = contents.iterator(); iterator.hasNext(); ) {
            List<String> row = iterator.next();
            XSSFRow holdingsRow = spreadsheet.createRow(rowId++);
            int cellId = 0;
            for (Iterator<String> stringIterator = row.iterator(); stringIterator.hasNext(); ) {
                String column = stringIterator.next();
                Cell cell = holdingsRow.createCell(cellId++);
                cell.setCellValue(column);
            }
        }

        return rowId;
    }

    public byte[] getExcelSheetForBibImport(String fileContent) {
        ByteArrayOutputStream byteArrayInputStream = new ByteArrayOutputStream();
        OleNGBibImportResponse oleNGBibImportResponse = null;
        try {
            oleNGBibImportResponse = getObjectMapper().readValue(fileContent, OleNGBibImportResponse.class);
            int rowId = 0;
            if(oleNGBibImportResponse != null) {
                XSSFWorkbook workbook = new XSSFWorkbook();
                XSSFSheet spreadsheet = workbook.createSheet("Bib Import Response");
                String[] headerArray = {"Job Name", "Job Execution Id", "Matched Bibs Count", "Unmatched Bibs Count",
                        "Multiple Matched Bibs Count", "Matched Holdings Count" , "Unmatched Holdings Count", "Multiple Matched Holdings Count",
                        "Matched Items Count", "Unmatched Items Count", "Multiple Matched Items Count", "Matched EHoldings Count", "Multiple Matched EHoldings Count"};
                rowId = addHeaders(workbook, spreadsheet, rowId, new String[]{"Main Section"}, GREEN_COLOR);
                rowId = addHeaders(workbook, spreadsheet, rowId, headerArray, null);
                int cellId;

                XSSFRow mainSectionValueRow = spreadsheet.createRow(rowId++);
                List<String> mainSectionValueList = getBibMainSectionValues(oleNGBibImportResponse);

                cellId = 0;
                for (Iterator<String> iterator = mainSectionValueList.iterator(); iterator.hasNext(); ) {
                    String value = iterator.next();
                    Cell cell = mainSectionValueRow.createCell(cellId++);
                    cell.setCellValue(value);
                }

                // Bib Section
                String[] bibSectionHeader = {"Bib Id", "Operation", "Record Index"};
                Map<String, List<List<String>>> contents = getBibContent(oleNGBibImportResponse.getBibResponses());
                List<List<String>> bibContent = contents.get(OleNGConstants.BIB);
                rowId = addSection(workbook, spreadsheet, rowId, bibSectionHeader,  bibContent, "Bib Section");

                //Holdings Section
                String[] holdingsSectionHeader = {"Holdings Id", "Bib Id", "Operation", "Record Index"};
                List<List<String>> holdingsContent = contents.get(OleNGConstants.HOLDINGS);
                rowId = addSection(workbook, spreadsheet, rowId,holdingsSectionHeader, holdingsContent, "Holdings Section");

                //Item Section
                String[] itemSectionHeader = {"Item Id","Holdings Id", "Bib Id", "Operation", "Record Index"};
                List<List<String>> itemContent = contents.get(OleNGConstants.ITEM);
                rowId = addSection(workbook, spreadsheet, rowId,itemSectionHeader, itemContent, "Item Section");


                //Holdings Section
                String[] eholdingsSectionHeader = {"Holdings Id", "Bib Id", "Operation", "Record Index"};
                List<List<String>> eholdingsContent = contents.get(OleNGConstants.EHOLDINGS);
                rowId = addSection(workbook, spreadsheet, rowId,eholdingsSectionHeader, eholdingsContent, "Eholdings Section");

                workbook.write(byteArrayInputStream);

            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return byteArrayInputStream.toByteArray();
    }

    private List<String> getBibMainSectionValues(OleNGBibImportResponse oleNGBibImportResponse) {
        List<String> values = new ArrayList<>();
        values.add(oleNGBibImportResponse.getJobName());
        values.add(oleNGBibImportResponse.getJobDetailId());
        values.add(String.valueOf(oleNGBibImportResponse.getMatchedBibsCount()));
        values.add(String.valueOf(oleNGBibImportResponse.getUnmatchedBibsCount()));
        values.add(String.valueOf(oleNGBibImportResponse.getMultipleMatchedBibsCount()));

        values.add(String.valueOf(oleNGBibImportResponse.getMatchedHoldingsCount()));
        values.add(String.valueOf(oleNGBibImportResponse.getUnmatchedHoldingsCount()));
        values.add(String.valueOf(oleNGBibImportResponse.getMultipleMatchedHoldingsCount()));

        values.add(String.valueOf(oleNGBibImportResponse.getMatchedItemsCount()));
        values.add(String.valueOf(oleNGBibImportResponse.getUnmatchedItemsCount()));
        values.add(String.valueOf(oleNGBibImportResponse.getMultipleMatchedItemsCount()));

        values.add(String.valueOf(oleNGBibImportResponse.getMatchedEHoldingsCount()));
        values.add(String.valueOf(oleNGBibImportResponse.getUnmatchedEHoldingsCount()));
        values.add(String.valueOf(oleNGBibImportResponse.getMultipleMatchedEHoldingsCount()));
        return values;
    }

    public Map<String, List<List<String>>> getBibContent(List<BibResponse> bibResponses) {
        Map<String, List<List<String>>> valueMap = new HashMap<>();
        List<List<String>> bibValues = new ArrayList<>();
        List<List<String>> holdingsValues = new ArrayList<>();
        List<List<String>> itemsValues = new ArrayList<>();
        List<List<String>> eholdingsValues = new ArrayList<>();
        for (Iterator<BibResponse> iterator = bibResponses.iterator(); iterator.hasNext(); ) {
            List<String> rowValue = new ArrayList<>();
            BibResponse bibResponse = iterator.next();
            String bibId = bibResponse.getBibId();
            rowValue.add(bibId);
            rowValue.add(bibResponse.getOperation());
            String index = String.valueOf(bibResponse.getRecordIndex());
            rowValue.add(index);
            bibValues.add(rowValue);
            Map<String, List<List<String>>>holdingsContent  = getHoldingsContent(bibResponse.getHoldingsResponses(), bibId, index);
            holdingsValues.addAll(holdingsContent.get(OleNGConstants.HOLDINGS));
            eholdingsValues.addAll(holdingsContent.get(OleNGConstants.EHOLDINGS));
            itemsValues.addAll(holdingsContent.get(OleNGConstants.ITEM));
        }

        valueMap.put(OleNGConstants.BIB, bibValues);
        valueMap.put(OleNGConstants.HOLDINGS, holdingsValues);
        valueMap.put(OleNGConstants.EHOLDINGS, eholdingsValues);
        valueMap.put(OleNGConstants.ITEM, itemsValues);

        return valueMap;

    }

    public Map<String, List<List<String>>> getHoldingsContent(List<HoldingsResponse> holdingsResponses, String bibUUID, String index) {
        Map<String, List<List<String>>> valueMap = new HashMap<>();
        List<List<String>> holdingsValues = new ArrayList<>();
        List<List<String>> eholdingsValues = new ArrayList<>();
        List<List<String>> itemValues = new ArrayList<>();
        for (Iterator<HoldingsResponse> iterator = holdingsResponses.iterator(); iterator.hasNext(); ) {
            List<String> rowValue = new ArrayList<>();
            HoldingsResponse holdingsResponse = iterator.next();
            String holdingsId = holdingsResponse.getHoldingsId();
            rowValue.add(holdingsId);
            rowValue.add(bibUUID);
            rowValue.add(holdingsResponse.getOperation());
            rowValue.add(index);
            if(StringUtils.equals(holdingsResponse.getHoldingsType(), PHoldings.PRINT)) {
                holdingsValues.add(rowValue);
                itemValues.addAll(getItemContent(holdingsResponse.getItemResponses(), bibUUID, holdingsId, index));
            } else {
                eholdingsValues.add(rowValue);
            }
        }
        valueMap.put(OleNGConstants.HOLDINGS, holdingsValues);
        valueMap.put(OleNGConstants.EHOLDINGS, eholdingsValues);
        valueMap.put(OleNGConstants.ITEM, itemValues);
        return valueMap;
    }

    private List<List<String>> getItemContent(List<ItemResponse> itemResponses, String bibUUID, String holdingUUID, String index) {
        List<List<String>> itemValues = new ArrayList<>();
        for (Iterator<ItemResponse> iterator = itemResponses.iterator(); iterator.hasNext(); ) {
            List<String> rowValue = new ArrayList<>();
            ItemResponse itemResponse = iterator.next();
            rowValue.add(itemResponse.getItemId());
            rowValue.add(holdingUUID);
            rowValue.add(bibUUID);
            rowValue.add(itemResponse.getOperation());
            rowValue.add(index);
            itemValues.add(rowValue);
        }
        return itemValues;
    }

    public byte[] getExcelSheetForOrderImport(String fileContent) throws IOException {
        ByteArrayOutputStream byteArrayInputStream = new ByteArrayOutputStream();
        OleNGOrderImportResponse oleNGOrderImportResponse = null;
        try {
            oleNGOrderImportResponse = getObjectMapper().readValue(fileContent, OleNGOrderImportResponse.class);
            int rowId = 0;
            if(oleNGOrderImportResponse != null) {
                XSSFWorkbook workbook = new XSSFWorkbook();
                XSSFSheet spreadsheet = workbook.createSheet("Order Import Response");
                String[] headerArray = {"Job Name", "Job Execution Id", "Matched Count", "Unmatched Count",
                        "Multiple Matched Count"};
                rowId = addHeaders(workbook, spreadsheet, rowId, new String[]{"Main Section"}, GREEN_COLOR);
                rowId = addHeaders(workbook, spreadsheet, rowId, headerArray, null);
                int cellId;

                XSSFRow mainSectionValueRow = spreadsheet.createRow(rowId++);
                List<String> mainSectionValueList = getOrderMainSectionValues(oleNGOrderImportResponse);

                cellId = 0;
                for (Iterator<String> iterator = mainSectionValueList.iterator(); iterator.hasNext(); ) {
                    String value = iterator.next();
                    Cell cell = mainSectionValueRow.createCell(cellId++);
                    cell.setCellValue(value);
                }

                // Requisition Only Section
                String[] orderSectionReportHeader = {"Document Number", "Record Number", "Title", "Successful Match Points"};
                List<List<String>> requisitionOnlyContent = getOrderResponseContent(oleNGOrderImportResponse.getReqOnlyResponses());
                rowId = addSection(workbook, spreadsheet, rowId, orderSectionReportHeader,  requisitionOnlyContent, "Requisition Only");

                //Req and PO Section
                List<List<String>> requisitionAndPOContent = getOrderResponseContent(oleNGOrderImportResponse.getReqAndPOResponses());
                rowId = addSection(workbook, spreadsheet, rowId, orderSectionReportHeader,  requisitionAndPOContent, "Requisition and Purchase Order");

                //Neither Req Nor PO Section
                List<List<String>> neitherReqNorPOContent = getOrderResponseContent(oleNGOrderImportResponse.getNoReqNorPOResponses());
                rowId = addSection(workbook, spreadsheet, rowId, orderSectionReportHeader,  neitherReqNorPOContent, "Neither Requisition Nor Purchase Order");

                workbook.write(byteArrayInputStream);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
        return byteArrayInputStream.toByteArray();
    }

    private List<List<String>> getOrderResponseContent(List<OrderResponse> reqOnlyResponses) {
        List<List<String>> itemValues = new ArrayList<>();
        for (Iterator<OrderResponse> iterator = reqOnlyResponses.iterator(); iterator.hasNext(); ) {
            OrderResponse orderResponse = iterator.next();
            List<OrderData> orderDatas = orderResponse.getOrderDatas();
            for (Iterator<OrderData> orderDataIterator = orderDatas.iterator(); orderDataIterator.hasNext(); ) {
                OrderData orderData = orderDataIterator.next();
                List<String> rowValue = new ArrayList<>();
                Integer reqDocumentNumber = orderData.getReqDocumentNumber();
                rowValue.add(null != reqDocumentNumber ? String.valueOf(reqDocumentNumber) : "");
                rowValue.add(String.valueOf(orderData.getRecordNumber()));
                rowValue.add(orderData.getTitle());
                rowValue.add(orderData.getSuccessfulMatchPoints());
                itemValues.add(rowValue);
            }
        }
        return itemValues;
    }


    private List<String> getOrderMainSectionValues(OleNGOrderImportResponse oleNGOrderImportResponse) {
        List<String> values = new ArrayList<>();
        values.add(oleNGOrderImportResponse.getJobName());
        values.add(oleNGOrderImportResponse.getJobDetailId());
        values.add(String.valueOf(oleNGOrderImportResponse.getMatchedCount()));
        values.add(String.valueOf(oleNGOrderImportResponse.getUnmatchedCount()));
        values.add(String.valueOf(oleNGOrderImportResponse.getMultiMatchedCount()));
        return values;
    }

    public byte[] getExcelSheetForInvoiceImport(String fileContent)  {
        ByteArrayOutputStream byteArrayInputStream = new ByteArrayOutputStream();
        OleNGInvoiceImportResponse oleNGInvoiceImportResponse = null;
        try {
            oleNGInvoiceImportResponse = getObjectMapper().readValue(fileContent, OleNGInvoiceImportResponse.class);
            int rowId = 0;
            if(oleNGInvoiceImportResponse != null) {
                XSSFWorkbook workbook = new XSSFWorkbook();
                XSSFSheet spreadsheet = workbook.createSheet("Invoice Import Response");
                String[] headerArray = {"Job Name", "Job Execution Id", "Matched Count", "Unmatched Count",
                        "Multiple Matched Count"};
                rowId = addHeaders(workbook, spreadsheet, rowId, new String[]{"Main Section"}, GREEN_COLOR);
                rowId = addHeaders(workbook, spreadsheet, rowId, headerArray, null);
                int cellId;

                XSSFRow mainSectionValueRow = spreadsheet.createRow(rowId++);
                List<String> mainSectionValueList = getInvoiceMainSectionValues(oleNGInvoiceImportResponse);

                cellId = 0;
                for (Iterator<String> iterator = mainSectionValueList.iterator(); iterator.hasNext(); ) {
                    String value = iterator.next();
                    Cell cell = mainSectionValueRow.createCell(cellId++);
                    cell.setCellValue(value);
                }

                // Requisition Only Section
                String[] orderSectionReportHeader = {"Document Number", "Number Of Records Unlinked", "Number Of Records Linked"};
                List<List<String>> requisitionOnlyContent = getInvoiceResponseContent(oleNGInvoiceImportResponse.getInvoiceResponses());
                rowId = addSection(workbook, spreadsheet, rowId, orderSectionReportHeader,  requisitionOnlyContent, "Invoice");

                workbook.write(byteArrayInputStream);

            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return byteArrayInputStream.toByteArray();
    }

    public byte[] getExcelSheetForBatchExport(String fileContent)  {
        ByteArrayOutputStream byteArrayInputStream = new ByteArrayOutputStream();
        OleNGBatchExportResponse oleNGBatchExportResponse = null;
        try {
            oleNGBatchExportResponse = getObjectMapper().readValue(fileContent, OleNGBatchExportResponse.class);
            int rowId = 0;
            if(oleNGBatchExportResponse != null) {
                XSSFWorkbook workbook = new XSSFWorkbook();
                XSSFSheet spreadsheet = workbook.createSheet("Batch Export Response");
                String[] headerArray = {"Job Name", "Job Execution Id", "Total Bib Count", "Success Bib Count",
                        "Failed Bib Count"};
                rowId = addHeaders(workbook, spreadsheet, rowId, new String[]{"Main Section"}, GREEN_COLOR);
                rowId = addHeaders(workbook, spreadsheet, rowId, headerArray, null);
                int cellId;

                XSSFRow mainSectionValueRow = spreadsheet.createRow(rowId++);
                List<String> mainSectionValueList = getBatchExportMainSectionValues(oleNGBatchExportResponse);

                cellId = 0;
                for (Iterator<String> iterator = mainSectionValueList.iterator(); iterator.hasNext(); ) {
                    String value = iterator.next();
                    Cell cell = mainSectionValueRow.createCell(cellId++);
                    cell.setCellValue(value);
                }

                // Success Section
                String[] successSectionReportHeader = {"Bib Id", "Message"};
                List<List<String>> batchExportSuccessContent = getBatchExportSuccessResponseContent(oleNGBatchExportResponse.getBatchExportSuccessResponseList());
                rowId = addSection(workbook, spreadsheet, rowId, successSectionReportHeader,  batchExportSuccessContent, "Export Success Section ");

                // Failure Section
                String[] failureSectionReportHeader = {"Bib Id", "Message"};
                List<List<String>> batchExportFailureContent = getBatchExportFailureResponseContent(oleNGBatchExportResponse.getBatchExportFailureResponseList());
                rowId = addSection(workbook, spreadsheet, rowId, failureSectionReportHeader,  batchExportFailureContent, "Export Failure Section ");

                workbook.write(byteArrayInputStream);

            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return byteArrayInputStream.toByteArray();
    }

    private List<List<String>> getInvoiceResponseContent(List<InvoiceResponse> invoiceResponses) {
        List<List<String>> itemValues = new ArrayList<>();
        for (Iterator<InvoiceResponse> iterator = invoiceResponses.iterator(); iterator.hasNext(); ) {
            InvoiceResponse invoiceResponse = iterator.next();
            List<String> rowValue = new ArrayList<>();
            rowValue.add(invoiceResponse.getDocumentNumber());
            rowValue.add(String.valueOf(invoiceResponse.getNoOfRecordUnlinked()));
            rowValue.add(String.valueOf(invoiceResponse.getNoOfRecordLinked()));
            itemValues.add(rowValue);
        }
        return itemValues;
    }


    private List<String> getInvoiceMainSectionValues(OleNGInvoiceImportResponse oleNGInvoiceImportResponse) {
        List<String> values = new ArrayList<>();
        values.add(oleNGInvoiceImportResponse.getJobName());
        values.add(oleNGInvoiceImportResponse.getJobDetailId());
        values.add(String.valueOf(oleNGInvoiceImportResponse.getMatchedCount()));
        values.add(String.valueOf(oleNGInvoiceImportResponse.getUnmatchedCount()));
        values.add(String.valueOf(oleNGInvoiceImportResponse.getMultiMatchedCount()));
        return values;
    }

    private List<String> getBatchExportMainSectionValues(OleNGBatchExportResponse oleNGBatchExportResponse) {
        List<String> values = new ArrayList<>();
        values.add(oleNGBatchExportResponse.getJobName());
        values.add(oleNGBatchExportResponse.getJobDetailId());
        values.add(String.valueOf(oleNGBatchExportResponse.getTotalNumberOfRecords()));
        values.add(String.valueOf(oleNGBatchExportResponse.getNoOfSuccessRecords()));
        values.add(String.valueOf(oleNGBatchExportResponse.getNoOfFailureRecords()));
        return values;
    }



    private List<List<String>> getBatchExportSuccessResponseContent(List<BatchExportSuccessResponse> exportSuccessResponses) {
        List<List<String>> itemValues = new ArrayList<>();
        for (Iterator<BatchExportSuccessResponse> iterator = exportSuccessResponses.iterator(); iterator.hasNext(); ) {
            BatchExportSuccessResponse batchExportSuccessResponse = iterator.next();
            List<String> rowValue = new ArrayList<>();
            rowValue.add(batchExportSuccessResponse.getBibId());
            rowValue.add(batchExportSuccessResponse.getMessage());
            itemValues.add(rowValue);
        }
        return itemValues;
    }


    private List<List<String>> getBatchExportFailureResponseContent(List<BatchExportFailureResponse> batchExportFailureResponses) {
        List<List<String>> itemValues = new ArrayList<>();
        for (Iterator<BatchExportFailureResponse> iterator = batchExportFailureResponses.iterator(); iterator.hasNext(); ) {
            BatchExportFailureResponse batchExportFailureResponse = iterator.next();
            List<String> rowValue = new ArrayList<>();
            rowValue.add(batchExportFailureResponse.getBibId());
            rowValue.add(batchExportFailureResponse.getMessage());
            itemValues.add(rowValue);
        }
        return itemValues;
    }

    public byte[] getExcelSheetForBatchDelete(String fileContent)  {
        ByteArrayOutputStream byteArrayInputStream = new ByteArrayOutputStream();
        OleNGBatchDeleteResponse oleNGBatchDeleteResponse = null;
        try {
            oleNGBatchDeleteResponse = getObjectMapper().readValue(fileContent, OleNGBatchDeleteResponse.class);
            int rowId = 0;
            if(oleNGBatchDeleteResponse != null) {
                XSSFWorkbook workbook = new XSSFWorkbook();
                XSSFSheet spreadsheet = workbook.createSheet("Batch Delete Response");
                String[] headerArray = {"Job Name", "Job Execution Id", "Deleted Bibs Count", "Failed Bibs Count"};
                rowId = addHeaders(workbook, spreadsheet, rowId, new String[]{"Main Section"}, GREEN_COLOR);
                rowId = addHeaders(workbook, spreadsheet, rowId, headerArray, null);
                int cellId;
                XSSFRow mainSectionValueRow = spreadsheet.createRow(rowId++);
                List<String> mainSectionValueList = getBatchDeleteMainSectionValues(oleNGBatchDeleteResponse);
                cellId = 0;
                for (Iterator<String> iterator = mainSectionValueList.iterator(); iterator.hasNext(); ) {
                    String value = iterator.next();
                    Cell cell = mainSectionValueRow.createCell(cellId++);
                    cell.setCellValue(value);
                }
                // Delete Success Section
                String[] deleteSuccessSectionReportHeader = {"Match Point", "Match Point Value", "Matched Bib Id", "Message"};
                List<List<String>> deleteSuccessSectionContent = getDeleteSuccessResponseContent(oleNGBatchDeleteResponse.getBatchDeleteSuccessResponseList());
                rowId = addSection(workbook, spreadsheet, rowId, deleteSuccessSectionReportHeader,  deleteSuccessSectionContent, "Delete Success Section");

                // Delete Success Section
                String[] deleteFailureSectionReportHeader = {"Match Point", "Match Point Value", "Matched Bib Id", "Message"};
                List<List<String>> deleteFailureSectionContent = getDeleteFailureResponseContent(oleNGBatchDeleteResponse.getBatchDeleteFailureResponseList());
                rowId = addSection(workbook, spreadsheet, rowId, deleteFailureSectionReportHeader,  deleteFailureSectionContent, "Delete Failure Section");

                workbook.write(byteArrayInputStream);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return byteArrayInputStream.toByteArray();
    }

    private List<List<String>> getDeleteSuccessResponseContent(List<BatchDeleteSuccessResponse> batchDeleteSuccessResponseList) {
        List<List<String>> itemValues = new ArrayList<>();
        for (Iterator<BatchDeleteSuccessResponse> iterator = batchDeleteSuccessResponseList.iterator(); iterator.hasNext(); ) {
            BatchDeleteSuccessResponse batchDeleteSuccessResponse = iterator.next();
            List<String> rowValue = new ArrayList<>();
            rowValue.add(batchDeleteSuccessResponse.getMatchPoint());
            rowValue.add(batchDeleteSuccessResponse.getMatchPointValue());
            rowValue.add(batchDeleteSuccessResponse.getBibId());
            rowValue.add(OleNGConstants.SUCCESS.equalsIgnoreCase(batchDeleteSuccessResponse.getMessage()) ? "Deleted" : batchDeleteSuccessResponse.getMessage());
            itemValues.add(rowValue);
        }
        return itemValues;
    }

    private List<List<String>> getDeleteFailureResponseContent(List<BatchDeleteFailureResponse> batchDeleteFailureResponseList) {
        List<List<String>> itemValues = new ArrayList<>();
        for (Iterator<BatchDeleteFailureResponse> iterator = batchDeleteFailureResponseList.iterator(); iterator.hasNext(); ) {
            BatchDeleteFailureResponse batchDeleteFailureResponse = iterator.next();
            List<String> rowValue = new ArrayList<>();
            rowValue.add(batchDeleteFailureResponse.getMatchPoint());
            rowValue.add(batchDeleteFailureResponse.getMatchPointValue());
            rowValue.add(batchDeleteFailureResponse.getBibId());
            rowValue.add(batchDeleteFailureResponse.getMessage());
            itemValues.add(rowValue);
        }
        return itemValues;
    }

    private List<String> getBatchDeleteMainSectionValues(OleNGBatchDeleteResponse oleNGBatchDeleteResponse) {
        List<String> values = new ArrayList<>();
        values.add(oleNGBatchDeleteResponse.getJobName());
        values.add(oleNGBatchDeleteResponse.getJobDetailId());
        values.add(String.valueOf(oleNGBatchDeleteResponse.getNoOfSuccessRecords()));
        values.add(String.valueOf(oleNGBatchDeleteResponse.getNoOfFailureRecords()));
        return values;
    }

}
