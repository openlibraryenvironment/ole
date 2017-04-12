/**
 * Created by angelind on 3/10/16.
 */

function initializePanels($scope) {
    $scope.reportMainSectionActivePanel = [0];
    $scope.reportBibSectionActivePanel = [];
    $scope.reportHoldingsSectionActivePanel = [];
    $scope.reportItemSectionActivePanel = [];
    $scope.reportEHoldingsSectionActivePanel = [];
    $scope.reportMainSectionPanel = [];
    $scope.reportBibSectionPanel = [];
    $scope.reportHoldingsSectionPanel = [];
    $scope.reportItemSectionPanel = [];
    $scope.reportEHoldingsSectionPanel = [];
    $scope.reportMainSectionPanel.collapsed = false;
    $scope.reportBibSectionPanel.collapsed = false;
    $scope.reportHoldingsSectionPanel.collapsed = false;
    $scope.reportItemSectionPanel.collapsed = false;
    $scope.reportEHoldingsSectionPanel.collapsed = false;

    $scope.orderImportReportMainSectionActivePanel = [0];
    $scope.orderImportReportMainSectionPanel = [];
    $scope.reportReqSectionActivePanel = [];
    $scope.reportReqAndPoSectionActivePanel = [];
    $scope.reportNeitherReqNorPoSectionActivePanel = [];
    $scope.reportReqSectionPanel = [];
    $scope.reportReqAndPoSectionPanel = [];
    $scope.reportNeitherReqNorPoSectionPanel = [];
    $scope.orderImportReportMainSectionPanel.collapsed = false;
    $scope.reportReqSectionPanel.collapsed = false;
    $scope.reportReqAndPoSectionPanel.collapsed = false;
    $scope.reportNeitherReqNorPoSectionPanel.collapsed = false;

    $scope.invoiceImportReportMainSectionActivePanel = [0];
    $scope.invoiceImportReportMainSectionPanel = [];
    $scope.reportInvoiceSectionActivePanel = [];
    $scope.reportInvoiceSectionPanel = [];
    $scope.invoiceImportReportMainSectionPanel.collapsed = false;
    $scope.reportInvoiceSectionPanel.collapsed = false;

    $scope.batchDeleteReportMainSectionActivePanel = [0];
    $scope.batchDeleteReportSuccessSectionActivePanel = [];
    $scope.batchDeleteReportFailureSectionActivePanel = [];
    $scope.batchDeleteReportMainSectionPanel = [];
    $scope.batchDeleteReportSuccessSectionPanel = [];
    $scope.batchDeleteReportFailureSectionPanel = [];
    $scope.batchDeleteReportMainSectionPanel.collapsed = false;
    $scope.batchDeleteReportSuccessSectionPanel.collapsed = false;
    $scope.batchDeleteReportFailureSectionPanel.collapsed = false;
}

function populateBibImportReportFromContent(fileContent, $scope) {
    var mainSectionContent = getMainSectionContent(fileContent);
    var bibSectionContent = [];
    var holdingsSectionContent = [];
    var itemSectionContent = [];
    var eHoldingsSectionContent = [];

    $scope.bibCreatedCount = 0;
    $scope.bibUpdatedCount = 0;
    $scope.bibDiscardedCount = 0;
    $scope.holdingsCreatedCount = 0;
    $scope.holdingsUpdatedCount = 0;
    $scope.holdingsDiscardedCount = 0;
    $scope.itemCreatedCount = 0;
    $scope.itemUpdatedCount = 0;
    $scope.itemDiscardedCount = 0;
    $scope.eholdingsCreatedCount = 0;
    $scope.eholdingsUpdatedCount = 0;
    $scope.eholdingsDiscardedCount = 0;

    var bibResponses = fileContent["bibResponses"];
    if (bibResponses != null && bibResponses != undefined) {
        for (var i = 0; i < bibResponses.length; i++) {
            var bib = getBibFromBibResponse(bibResponses[i]);
            bibSectionContent.push(bib);
            if(bib.operation === 'Created') {
                $scope.bibCreatedCount++;
            } else if(bib.operation === 'Updated') {
                $scope.bibUpdatedCount++;
            } else {
                $scope.bibDiscardedCount++;
            }
            var holdingsResponses = bibResponses[i]["holdingsResponses"];
            if (holdingsResponses != null && holdingsResponses != undefined) {
                for (var j = 0; j < holdingsResponses.length; j++) {
                    var holdings = getHoldingsFromHoldingsResponse(holdingsResponses[j], bib);
                    if (holdingsResponses[j]["holdingsType"] == 'print') {
                        holdingsSectionContent.push(holdings);
                        if(holdings.operation === 'Created') {
                            $scope.holdingsCreatedCount++;
                        } else if(holdings.operation === 'Updated') {
                            $scope.holdingsUpdatedCount++;
                        } else {
                            $scope.holdingsDiscardedCount++;
                        }
                    } else if (holdingsResponses[j]["holdingsType"] == 'electronic') {
                        eHoldingsSectionContent.push(holdings);
                        if(holdings.operation === 'Created') {
                            $scope.eholdingsCreatedCount++;
                        } else if(holdings.operation === 'Updated') {
                            $scope.eholdingsUpdatedCount++;
                        } else {
                            $scope.eholdingsDiscardedCount++;
                        }
                    }
                    var itemResponses = holdingsResponses[j]["itemResponses"];
                    if (itemResponses != null && itemResponses != undefined) {
                        for (var k = 0; k < itemResponses.length; k++) {
                            var item = getItemFromItemResponse(itemResponses[k], holdings, bib);
                            itemSectionContent.push(item);
                            if(item.operation === 'Created') {
                                $scope.itemCreatedCount++;
                            } else if(item.operation === 'Updated') {
                                $scope.itemUpdatedCount++;
                            } else {
                                $scope.itemDiscardedCount++;
                            }
                        }
                    }
                }
            }
        }
    }
    $scope.reportMainSectionPanel = mainSectionContent;
    $scope.reportBibSectionPanel = bibSectionContent;
    $scope.reportHoldingsSectionPanel = holdingsSectionContent;
    $scope.reportItemSectionPanel = itemSectionContent;
    $scope.reportEHoldingsSectionPanel = eHoldingsSectionContent;
}


function populateOrderImportReportFromContent(fileContent, $scope) {
    var mainSectionContent = getMainSectionContentForOrderAndInvoiceImport(fileContent);
    $scope.orderImportReportMainSectionPanel = mainSectionContent;
    $scope.reportReqSectionPanel = getReqOnlySectionContent(fileContent);
    $scope.reportReqAndPoSectionPanel = getReqAndPoSectionContent(fileContent);
    $scope.reportNeitherReqNorPoSectionPanel = getNeitherReqNorPoSectionContent(fileContent);
}

function populateInvoiceImportReportFromContent(fileContent, $scope) {
    var mainSectionContent = getMainSectionContentForOrderAndInvoiceImport(fileContent);
    $scope.invoiceImportReportMainSectionPanel = mainSectionContent;
    $scope.reportInvoiceSectionPanel = getInvoiceSectionContent(fileContent);
}

function populateBatchDeleteReportFromContent(fileContent, $scope) {
    var mainSectionContent = getMainSectionContentForBatchDelete(fileContent);
    $scope.batchDeleteReportMainSectionPanel = mainSectionContent;
    $scope.batchDeleteReportSuccessSectionPanel = getDeleteSuccessSectionContent(fileContent);
    $scope.batchDeleteReportFailureSectionPanel = getDeleteFailureSectionContent(fileContent);
}

function populateBatchExportReportFromContent(fileContent, $scope) {
    var mainSectionContent = getMainSectionContentForBatchExport(fileContent);
    $scope.batchExportReportMainSectionPanel = mainSectionContent;
    $scope.batchExportReportSuccessSectionPanel = getExportSuccessSectionContent(fileContent);
    $scope.batchExportReportFailureSectionPanel = getExportFailureSectionContent(fileContent);
}

function getMainSectionContent(fileContent) {
    var mainSectionContent = {
        "bibImportProfileName": fileContent["bibImportProfileName"],
        "jobName": fileContent["jobName"],
        "jobDetailId": fileContent["jobDetailId"],
        "matchedBibsCount": fileContent["matchedBibsCount"],
        "unmatchedBibsCount": fileContent["unmatchedBibsCount"],
        "multipleMatchedBibsCount": fileContent["multipleMatchedBibsCount"],
        "matchedHoldingsCount": fileContent["matchedHoldingsCount"],
        "unmatchedHoldingsCount": fileContent["unmatchedHoldingsCount"],
        "multipleMatchedHoldingsCount": fileContent["multipleMatchedHoldingsCount"],
        "matchedItemsCount": fileContent["matchedItemsCount"],
        "unmatchedItemsCount": fileContent["unmatchedItemsCount"],
        "multipleMatchedItemsCount": fileContent["multipleMatchedItemsCount"],
        "matchedEHoldingsCount": fileContent["matchedEHoldingsCount"],
        "unmatchedEHoldingsCount": fileContent["unmatchedEHoldingsCount"],
        "multipleMatchedEHoldingsCount": fileContent["multipleMatchedEHoldingsCount"]
    };
    return mainSectionContent;
}


function getMainSectionContentForBatchDelete(fileContent) {
    var mainSectionContent = {
        "jobName": fileContent["jobName"],
        "jobDetailId": fileContent["jobDetailId"],
        "deletedBibsCount": fileContent["deletedBibsCount"],
        "failedBibsCount": fileContent["failedBibsCount"],
        "totalRecordsCount": fileContent["totalRecordsCount"]
    };
    return mainSectionContent;
}

function getMainSectionContentForBatchExport(fileContent) {
    var mainSectionContent = {
        "jobName": fileContent["jobName"],
        "jobDetailId": fileContent["jobDetailId"],
        "successBibsCount": fileContent["successBibsCount"],
        "failedBibsCount": fileContent["failedBibsCount"],
        "totalRecordsCount": fileContent["totalRecordsCount"]
    };
    return mainSectionContent;
}

function getMainSectionContentForOrderAndInvoiceImport(fileContent) {
    var mainSectionContent = {
        "profileName": fileContent["profileName"],
        "jobName": fileContent["jobName"],
        "jobDetailId": fileContent["jobDetailId"],
        "matchedCount": fileContent["matchedCount"],
        "unmatchedCount": fileContent["unmatchedCount"],
        "multiMatchedCount": fileContent["multiMatchedCount"]
    };
    return mainSectionContent;
}

function getBibFromBibResponse(bibResponse) {
    var bib = {
        "bibId" : bibResponse["bibId"],
        "operation" : bibResponse["operation"],
        "recordIndex" : bibResponse["recordIndex"]
    };
    return bib;
}

function getHoldingsFromHoldingsResponse(holdingsResponse, bib) {
    var holdings = {
        "holdingsId" : holdingsResponse["holdingsId"],
        "bibId" : bib.bibId,
        "operation" : holdingsResponse["operation"],
        "recordIndex" : bib["recordIndex"]
    };
    return holdings;
}

function getItemFromItemResponse(itemResponse, holdings, bib) {
    var item = {
        "itemId" : itemResponse["itemId"],
        "holdingsId" : holdings.holdingsId,
        "bibId" : bib.bibId,
        "operation" : itemResponse["operation"],
        "recordIndex" : bib["recordIndex"]
    };
    return item;
}

function getReqOnlySectionContent(fileContent) {
    var reqOnlySectionContent = [];
    var reqOnlyResponses = fileContent["reqOnlyResponses"];
    if (reqOnlyResponses != null && reqOnlyResponses != undefined) {
        for (var i = 0; i < reqOnlyResponses.length; i++) {
            var reqOnlyOrderDatas = reqOnlyResponses[i]["orderDatas"];
            if (reqOnlyOrderDatas != null && reqOnlyOrderDatas != undefined) {
                for (var j = 0; j < reqOnlyOrderDatas.length; j++) {
                    var requisition = getReqFromResponse(reqOnlyOrderDatas[j]);
                    reqOnlySectionContent.push(requisition);
                }
            }
        }
    }
    return reqOnlySectionContent;
}

function getReqAndPoSectionContent(fileContent) {
    var reqAndPoSectionContent = [];
    var reqAndPOResponses = fileContent["reqAndPOResponses"];
    if (reqAndPOResponses != null && reqAndPOResponses != undefined) {
        for (var k = 0; k < reqAndPOResponses.length; k++) {
            var reqAndPOOrderDatas = reqAndPOResponses[k]["orderDatas"];
            if (reqAndPOOrderDatas != null && reqAndPOOrderDatas != undefined) {
                for (var l = 0; l < reqAndPOOrderDatas.length; l++) {
                    var requisitionAndPo = getReqFromResponse(reqAndPOOrderDatas[l]);
                    reqAndPoSectionContent.push(requisitionAndPo);
                }
            }
        }
    }
    return reqAndPoSectionContent;
}

function getNeitherReqNorPoSectionContent(fileContent) {
    var neitherReqNorPoSectionContent = [];
    var neitherReqNorPoResponses = fileContent["noReqNorPOResponses"];
    if (neitherReqNorPoResponses != null && neitherReqNorPoResponses != undefined) {
        for (var m = 0; m < neitherReqNorPoResponses.length; m++) {
            var neitherReqNorPoOrderDatas = neitherReqNorPoResponses[m]["orderDatas"];
            if (neitherReqNorPoOrderDatas != null && neitherReqNorPoOrderDatas != undefined) {
                for (var n = 0; n < neitherReqNorPoOrderDatas.length; n++) {
                    var neitherReqNorPo = getReqFromResponse(neitherReqNorPoOrderDatas[n]);
                    neitherReqNorPoSectionContent.push(neitherReqNorPo);
                }
            }
        }
    }
    return neitherReqNorPoSectionContent;
}

function getReqFromResponse(reqResponse) {
    var requisition = {
        "title" : reqResponse["title"],
        "successfulMatchPoints" : reqResponse["successfulMatchPoints"],
        "recordNumber" : reqResponse["recordNumber"],
        "reqDocumentNumber" : reqResponse["reqDocumentNumber"]
    };
    return requisition;
}

function getInvoiceSectionContent(fileContent) {
    var invoiceSectionContent = [];
    var invoiceResponses = fileContent["invoiceResponses"];
    if (invoiceResponses != null && invoiceResponses != undefined) {
        for (var p = 0; p < invoiceResponses.length; p++) {
            var invoice = getInvoiceFromResponse(invoiceResponses[p]);
            invoiceSectionContent.push(invoice);
        }
    }
    return invoiceSectionContent;
}

function getInvoiceFromResponse(invoiceResponse) {
    var invoice = {
        "documentNumber" : invoiceResponse["documentNumber"],
        "noOfRecordUnlinked" : invoiceResponse["noOfRecordUnlinked"],
        "noOfRecordLinked" : invoiceResponse["noOfRecordLinked"]
    };
    return invoice;
}

function getDeleteSuccessSectionContent(fileContent) {
    var deleteSuccessSectionContent = [];
    var deleteSuccessResponses = fileContent["deleteSuccessResponses"];
    if (deleteSuccessResponses != null && deleteSuccessResponses != undefined) {
        for (var q = 0; q < deleteSuccessResponses.length; q++) {
            var deleteSuccessMessage = getDeleteMessageFromResponse(deleteSuccessResponses[q]);
            deleteSuccessSectionContent.push(deleteSuccessMessage);
        }
    }
    return deleteSuccessSectionContent;
}

function getDeleteMessageFromResponse(deleteMessage) {
    var deleteMessage = {
        "matchPoint" : deleteMessage["matchPoint"],
        "matchPointValue" : deleteMessage["matchPointValue"],
        "bibId" : deleteMessage["bibId"],
        "message" : deleteMessage["message"]
    };
    return deleteMessage;
}

function getDeleteFailureSectionContent(fileContent) {
    var deleteFailureSectionContent = [];
    var deleteFailureResponses = fileContent["deleteFailureResponses"];
    if (deleteFailureResponses != null && deleteFailureResponses != undefined) {
        for (var q = 0; q < deleteFailureResponses.length; q++) {
            var deleteFailureMessage = getDeleteMessageFromResponse(deleteFailureResponses[q]);
            deleteFailureSectionContent.push(deleteFailureMessage);
        }
    }
    return deleteFailureSectionContent;
}

function getExportSuccessSectionContent(fileContent) {
    var exportSuccessSectionContent = [];
    var exportSuccessResponses = fileContent["exportSuccessResponses"];
    if (exportSuccessResponses != null && exportSuccessResponses != undefined) {
        for (var q = 0; q < exportSuccessResponses.length; q++) {
            var exportSuccessMessage = getExportMessageFromResponse(exportSuccessResponses[q]);
            exportSuccessSectionContent.push(exportSuccessMessage);
        }
    }
    return exportSuccessSectionContent;
}

function getExportMessageFromResponse(exportMessage) {
    var exportMessage = {
        "bibLocalId" : exportMessage["bibLocalId"],
        "message" : exportMessage["message"]
    };
    return exportMessage;
}

function getExportFailureSectionContent(fileContent) {
    var exportFailureSectionContent = [];
    var exportFailureResponses = fileContent["exportFailureResponses"];
    if (exportFailureResponses != null && exportFailureResponses != undefined) {
        for (var q = 0; q < exportFailureResponses.length; q++) {
            var exportFailureMessage = getExportMessageFromResponse(exportFailureResponses[q]);
            exportFailureSectionContent.push(exportFailureMessage);
        }
    }
    return exportFailureSectionContent;
}

