/**
 * Created by SheikS on 2/11/2016.
 */
var batchReportViewerApp = angular.module('batchReportViewerApp', ['datatables', 'customDirectives', 'ngAnimate', 'ngSanitize', 'mgcrea.ngStrap', 'ui.bootstrap']);

batchReportViewerApp.controller('batchReportViewerController', ['$scope', '$http', function ($scope, $http) {

    initializePanels($scope);
    $scope.fileList = [];
    $scope.batchReportViewer = {};
    $scope.batchReportViewer.reportContent = {};

    $scope.init = function() {
        $scope.initializeReportList();
    };


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
                initializePanels($scope);
                var fileContent = data.fileContent;
                if(fileName.indexOf('BatchExport')  != -1 && fileName.indexOf('_DeletedBibIds')  != -1) {
                    fileContent = data.fileContent;
                } else {
                    fileContent = JSON.parse(data.fileContent);
                    if (fileName.indexOf('BibImport') != -1) {
                        populateBibImportReportFromContent(fileContent, $scope);
                    } else if (fileName.indexOf('OrderImport') != -1) {
                        populateOrderImportReportFromContent(fileContent, $scope);
                    } else if (fileName.indexOf('Invoice') != -1) {
                        populateInvoiceImportReportFromContent(fileContent, $scope);
                    } else if (fileName.indexOf('BatchDelete') != -1) {
                        populateBatchDeleteReportFromContent(fileContent, $scope);
                    } else if (fileName.indexOf('BatchExport') != -1) {
                        populateBatchExportReportFromContent(fileContent, $scope);
                    }
                }

            }
            $scope.batchReportViewer.showModal = true;
        });
    };

    $scope.downloadReport = function(file) {
        var url = OLENG_CONSTANTS.DOWNLOAD_REPORT_FILE + "?fileName=" + file.name + "&parent=" + file.parent;
        window.location.href = url;
    };

    $scope.closeModal = function() {
        $scope.reportContent = null;
        $scope.batchReportViewer.showModal = false;
    };

}]);
