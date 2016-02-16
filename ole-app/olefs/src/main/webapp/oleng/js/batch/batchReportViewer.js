
/**
 * Created by SheikS on 2/11/2016.
 */
var batchReportViewerApp = angular.module('batchReportViewerApp', ['datatables', 'customDirectives', 'ngAnimate', 'ngSanitize', 'mgcrea.ngStrap', 'ui.bootstrap']);

batchReportViewerApp.controller('batchReportViewerController', ['$scope', '$http', function ($scope, $http) {
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
    $scope.fileList = [];
    $scope.batchReportViewer = {};
    $scope.batchReportViewer.reportContent = {};
    $scope.initializeReportList = function() {
        doGetRequest($scope, $http, OLENG_CONSTANTS.REPORT_FILES, null ,function(response) {
            var data = response.data;
            $scope.fileList = data;
        });
    };

    $scope.showReport = function(fileName) {
        document.getElementById('modalContentId').style.width = '950px';
        document.getElementById('modalContentId').style.height = '450px';
        document.getElementById('modalContentId').style.overflowX = 'auto';
        document.getElementById('modalContentId').style.overflowY = 'auto';
        document.getElementById('modalContentId').style.left = '-150px';
        doGetRequest($scope, $http, OLENG_CONSTANTS.GET_FILE_CONTENT, {"fileName": fileName},function(response) {
            var data = response.data;
            $scope.batchReportViewer.fileName = fileName;
            $scope.batchReportViewer.reportContent = data.fileContent;
            if (fileName.indexOf('txt') != -1) {
                var fileContent = JSON.parse(data.fileContent);
                populateReportFromContent(fileContent);
            }
            $scope.batchReportViewer.showModal = true;
        });
    };

    $scope.downloadReport = function(fileName) {
        doGetRequest($scope, $http, OLENG_CONSTANTS.GET_FILE_CONTENT, {"fileName": fileName},function(response) {
            var data = response.data;
            var blob = new Blob([data.fileContent], {
                type: "application/json;charset=utf-8"
            });
            saveAs(blob, fileName);
        });
    };

    $scope.closeModal = function() {
        $scope.reportContent = null;
        $scope.batchReportViewer.showModal = false;
    };

    $scope.init = function() {
        $scope.initializeReportList();
    };

    function populateReportFromContent(fileContent) {
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
        var bibSectionContent = [];
        var holdingsSectionContent = [];
        var itemSectionContent = [];
        var eHoldingsSectionContent = [];

        var bibResponses = fileContent["bibResponses"];
        if (bibResponses != null && bibResponses != undefined) {
            for (var i = 0; i < bibResponses.length; i++) {
                var bib = {
                    "bibId" : bibResponses[i]["bibId"],
                    "operation" : bibResponses[i]["operation"],
                    "recordIndex" : bibResponses[i]["recordIndex"]
                };
                bibSectionContent.push(bib);
                var holdingsResponses = bibResponses[i]["holdingsResponses"];
                if (holdingsResponses != null && holdingsResponses != undefined) {
                    for (var j = 0; j < holdingsResponses.length; j++) {
                        var holdings = {
                            "holdingsId" : holdingsResponses[j]["holdingsId"],
                            "bibId" : bib.bibId,
                            "operation" : holdingsResponses[j]["operation"],
                            "recordIndex" : holdingsResponses[j]["recordIndex"]
                        };
                        if (holdingsResponses[j]["holdingsType"] == 'print') {
                            holdingsSectionContent.push(holdings);
                        } else if (holdingsResponses[j]["holdingsType"] == 'electronic') {
                            eHoldingsSectionContent.push(holdings);
                        }
                        var itemResponses = holdingsResponses[j]["itemResponses"];
                        if (itemResponses != null && itemResponses != undefined) {
                            for (var k = 0; k < itemResponses.length; k++) {
                                var item = {
                                    "itemId" : itemResponses[k]["itemId"],
                                    "holdingsId" : holdings.holdingsId,
                                    "bibId" : bib.bibId,
                                    "operation" : itemResponses[k]["operation"],
                                    "recordIndex" : itemResponses[k]["recordIndex"]
                                };
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

}]);