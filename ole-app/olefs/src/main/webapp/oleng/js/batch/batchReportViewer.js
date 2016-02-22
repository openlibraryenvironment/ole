/**
 * Created by SheikS on 2/11/2016.
 */
var batchReportViewerApp = angular.module('batchReportViewerApp', ['datatables', 'customDirectives', 'ngAnimate', 'ngSanitize', 'mgcrea.ngStrap', 'ui.bootstrap']);

batchReportViewerApp.controller('batchReportViewerController', ['$scope', '$http', function ($scope, $http) {

    initializePanels();
    $scope.fileList = [];
    $scope.batchReportViewer = {};
    $scope.batchReportViewer.reportContent = {};
    $scope.initializeReportList = function() {
        var urlVars = getUrlVars();
        var jobDetailsId = urlVars['jobDetailsId'];
        if(jobDetailsId !== null && jobDetailsId !== undefined) {
            doGetRequest($scope, $http, OLENG_CONSTANTS.SPECIFIC_REPORT_FILES, {"jobDetailsId": jobDetailsId}, function(response) {
                var data = response.data;
                $scope.fileList = data;
            });
        } else {
            doGetRequest($scope, $http, OLENG_CONSTANTS.REPORT_FILES, null, function(response) {
                var data = response.data;
                $scope.fileList = data;
            });
        }

    };

    $scope.showReport = function(file) {
        $scope.reportContent = null;
        $scope.batchReportViewer.showModal = false;
        document.getElementById('modalContentId').style.width = '950px';
        document.getElementById('modalContentId').style.height = '450px';
        document.getElementById('modalContentId').style.overflowX = 'auto';
        document.getElementById('modalContentId').style.overflowY = 'auto';
        document.getElementById('modalContentId').style.left = '-150px';
        var fileName = file.name;
        doGetRequest($scope, $http, OLENG_CONSTANTS.GET_FILE_CONTENT, {"fileName": fileName,"parent": file.parent},function(response) {
            var data = response.data;
            $scope.batchReportViewer.fileName = fileName;
            $scope.batchReportViewer.reportContent = data.fileContent;
            if (fileName.indexOf('txt') != -1) {
                initializePanels();
                var fileContent = JSON.parse(data.fileContent);
                if (fileName.indexOf('BibImport') != -1) {
                    populateBibImportReportFromContent(fileContent);
                } else if (fileName.indexOf('OrderImport') != -1) {
                    populateOrderImportReportFromContent(fileContent);
                } else if (fileName.indexOf('Invoice') != -1) {
                    populateInvoiceImportReportFromContent(fileContent);
                }
            }
            $scope.batchReportViewer.showModal = true;
        });
    };

    $scope.downloadReport = function(file) {
        doGetRequest($scope, $http, OLENG_CONSTANTS.GET_FILE_CONTENT, {"fileName": file.name,"parent": file.parent},function(response) {
            var data = response.data;
            var blob = new Blob([data.fileContent], {
                type: "application/json;charset=utf-8"
            });
            saveAs(blob, file.name);
        });
    };

    $scope.closeModal = function() {
        $scope.reportContent = null;
        $scope.batchReportViewer.showModal = false;
    };

    $scope.init = function() {
        $scope.initializeReportList();
    };

    function initializePanels() {
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

        $scope.reportReqSectionActivePanel = [0];
        $scope.reportReqAndPoSectionActivePanel = [];
        $scope.reportNeitherReqNorPoSectionActivePanel = [];
        $scope.reportReqSectionPanel = [];
        $scope.reportReqAndPoSectionPanel = [];
        $scope.reportNeitherReqNorPoSectionPanel = [];
        $scope.reportReqSectionPanel.collapsed = false;
        $scope.reportReqAndPoSectionPanel.collapsed = false;
        $scope.reportNeitherReqNorPoSectionPanel.collapsed = false;

        $scope.reportInvoiceSectionActivePanel = [0];
        $scope.reportInvoiceSectionPanel = [];
        $scope.reportInvoiceSectionPanel.collapsed = false;
    }

    function populateBibImportReportFromContent(fileContent) {
        var mainSectionContent = getMainSectionContent(fileContent);
        var bibSectionContent = [];
        var holdingsSectionContent = [];
        var itemSectionContent = [];
        var eHoldingsSectionContent = [];

        var bibResponses = fileContent["bibResponses"];
        if (bibResponses != null && bibResponses != undefined) {
            for (var i = 0; i < bibResponses.length; i++) {
                var bib = getBibFromBibResponse(bibResponses[i]);
                bibSectionContent.push(bib);
                var holdingsResponses = bibResponses[i]["holdingsResponses"];
                if (holdingsResponses != null && holdingsResponses != undefined) {
                    for (var j = 0; j < holdingsResponses.length; j++) {
                        var holdings = getHoldingsFromHoldingsResponse(holdingsResponses[j], bib);
                        if (holdingsResponses[j]["holdingsType"] == 'print') {
                            holdingsSectionContent.push(holdings);
                        } else if (holdingsResponses[j]["holdingsType"] == 'electronic') {
                            eHoldingsSectionContent.push(holdings);
                        }
                        var itemResponses = holdingsResponses[j]["itemResponses"];
                        if (itemResponses != null && itemResponses != undefined) {
                            for (var k = 0; k < itemResponses.length; k++) {
                                var item = getItemFromItemResponse(itemResponses[k], holdings, bib);
                                itemSectionContent.push(item);
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


    function populateOrderImportReportFromContent(fileContent) {
        $scope.reportReqSectionPanel = getReqOnlySectionContent(fileContent);
        $scope.reportReqAndPoSectionPanel = getReqAndPoSectionContent(fileContent);
        $scope.reportNeitherReqNorPoSectionPanel = getNeitherReqNorPoSectionContent(fileContent);
    }

    function populateInvoiceImportReportFromContent(fileContent) {
        $scope.reportInvoiceSectionPanel = getInvoiceSectionContent(fileContent);
    }

    function getMainSectionContent(fileContent) {
        var mainSectionContent = {
            "bibImportProfileName": fileContent["bibImportProfileName"],
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

    function getBibFromBibResponse(bibResponse) {
        var bib = {
            "bibId" : bibResponse["bibId"],
            "operation" : bibResponse["operation"]
        };
        return bib;
    }

    function getHoldingsFromHoldingsResponse(holdingsResponse, bib) {
        var holdings = {
            "holdingsId" : holdingsResponse["holdingsId"],
            "bibId" : bib.bibId,
            "operation" : holdingsResponse["operation"]
        };
        return holdings;
    }

    function getItemFromItemResponse(itemResponse, holdings, bib) {
        var item = {
            "itemId" : itemResponse["itemId"],
            "holdingsId" : holdings.holdingsId,
            "bibId" : bib.bibId,
            "operation" : itemResponse["operation"]
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
            "recordNumber" : reqResponse["recordNumber"]
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

}]);
