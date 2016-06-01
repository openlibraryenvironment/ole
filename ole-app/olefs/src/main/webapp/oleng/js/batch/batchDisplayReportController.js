/**
 * Created by angelind on 3/10/16.
 */
var batchDisplayReportApp = angular.module('batchDisplayReportApp', ['datatables', 'customDirectives', 'ngAnimate', 'ngSanitize', 'mgcrea.ngStrap', 'ui.bootstrap']);

batchDisplayReportApp.controller('batchDisplayReportController', ['$scope', '$http', function ($scope, $http) {

    initializePanels($scope);
    $scope.batchReportViewer = {};

    $scope.init = function() {
        $scope.batchReportViewer.profileTypeValues = BATCH_CONSTANTS.PROFILE_TYPES;
    };

    $scope.showReportPopUp = function() {
        $scope.batchReportViewer.showModal = false;
        document.getElementById('modalContentId').style.width = '950px';
        document.getElementById('modalContentId').style.height = '450px';
        document.getElementById('modalContentId').style.overflowX = 'auto';
        document.getElementById('modalContentId').style.overflowY = 'auto';
        document.getElementById('modalContentId').style.left = '-150px';

        var fileName = $scope.batchReportViewer.selectedFile.name;
        var file = $scope.batchReportViewer.selectedFile;
        var profileType = $scope.batchReportViewer.profileType;
        console.log(profileType);

        $scope.batchReportViewer.fileName = fileName;

        var fileContent = null;
        var reader = new FileReader();
        reader.readAsText(file, "UTF-8");
        reader.onload = function (evt) {
            fileContent = evt.target.result;
            $scope.batchReportViewer.reportContent = fileContent;
            if (fileName.indexOf('txt') != -1) {
                initializePanels($scope);
                fileContent = JSON.parse(fileContent);
                if (profileType.indexOf('Bib') != -1) {
                    populateBibImportReportFromContent(fileContent, $scope);
                } else if (profileType.indexOf('Order') != -1) {
                    populateOrderImportReportFromContent(fileContent, $scope);
                } else if (profileType.indexOf('Invoice') != -1) {
                    populateInvoiceImportReportFromContent(fileContent, $scope);
                } else if (profileType.indexOf('Batch Delete') != -1) {
                    populateBatchDeleteReportFromContent(fileContent, $scope);
                } else if (profileType.indexOf('Batch Export') != -1) {
                    populateBatchExportReportFromContent(fileContent, $scope);
                }
            }
        };

        $scope.batchReportViewer.showModal = true;
    };

    $scope.closeModal = function() {
        $scope.reportContent = null;
        $scope.batchReportViewer.showModal = false;
    };

}]);
