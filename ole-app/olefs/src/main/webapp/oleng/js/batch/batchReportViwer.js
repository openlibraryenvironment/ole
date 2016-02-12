
/**
 * Created by SheikS on 2/11/2016.
 */
var batchReportViewerApp = angular.module('batchReportViewerApp', ['datatables', 'customDirectives']);

batchReportViewerApp.controller('batchReportViewerController', ['$scope', '$http', function ($scope, $http) {
    $scope.fileList = [];
    $scope.batchReportViewer = {};
    $scope.initalizeReportList = function() {
        doGetRequest($scope, $http, OLENG_CONSTANTS.REPORT_FILES, null ,function(response) {
            var data = response.data;
            $scope.fileList = data;
        });
    };

    $scope.showReport = function(fileName) {
        doGetRequest($scope, $http, OLENG_CONSTANTS.GET_FILE_CONTENT, {params: {"fileName": fileName}},function(response) {
            var data = response.data;
            $scope.batchReportViewer.fileName = fileName;
            $scope.batchReportViewer.reportContent = data.fileContent;
            $scope.batchReportViewer.showModal = true;
        });
    };

    $scope.downloadReport = function(fileName) {
        doGetRequest($scope, $http, OLENG_CONSTANTS.GET_FILE_CONTENT, {params: {"fileName": fileName}},function(response) {
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
        $scope.initalizeReportList();
    };


}]);