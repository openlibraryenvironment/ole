'use strict';

angular.module("oleSolrClient.partialIndex", ['ui.bootstrap', 'ngStorage', 'ui.bootstrap.datetimepicker'])
    .controller('partialIndexCtrl', ["$scope", "$location", "$routeParams", "oleSolrClientAPIService", "$localStorage", '$filter',
        function ($scope, $location, $routeParams, oleSolrClientAPIService, $localStorage, $filter) {
            console.log("Partial Index");

            $scope.getPartialIndexStatus = function () {
                oleSolrClientAPIService.getRestCall(OLESOLRCLIENT_CONSTANTS.PARTIAL_INDEX_STATUS, {}).then(function (response) {
                    var data = response.data;
                    $scope.partialIndexStatus = data;

                    $scope.partialIndexRequest.noOfDbThreads = $scope.noOfDbThreads;
                    $scope.partialIndexRequest.docsPerThread = $scope.docsPerThread;
                    $scope.partialIndexRequest.type = $scope.type;
                    $scope.partialIndexRequest.fromDate = $scope.fromDate;
                    $scope.partialIndexRequest.fromBibId = $scope.fromBibId;
                    $scope.partialIndexRequest.toBibId = $scope.toBibId;

                }, function (response) {
                    console.log("loading full index status failed");
                    console.log(response);
                });
            };
            $scope.getPartialIndexStatus();
            
            $scope.openCalendar = openCalendar;
            $scope.datePickerOpenStatus = {};

            $scope.datePickerOpenStatus.fromDate = false;
            $scope.datePickerOpenStatus.toDate = false;

            function openCalendar (date) {
                $scope.datePickerOpenStatus[date] = true;
            }

            $scope.partialIndex = function () {
                var config = {
                    transformRequest: angular.identity,
                    headers:  {
                        "Content-Type": undefined
                    }
                };

                var fd = new FormData();
                if ($scope.partialIndexRequest.type === 'indexByRange') {
                    var fromBibId = $scope.partialIndexRequest.fromBibId;
                    var toBibId = $scope.partialIndexRequest.toBibId;
                    var numberOfThreads = $scope.partialIndexRequest.noOfDbThreads;
                    var docPerThread = $scope.partialIndexRequest.docsPerThread;
                    fd.append('fromBibId', fromBibId);
                    fd.append('toBibId', toBibId);
                    fd.append('numberOfThreads', numberOfThreads);
                    fd.append('docPerThread', docPerThread);
                    oleSolrClientAPIService.postRestCall(OLESOLRCLIENT_CONSTANTS.PARTIAL_INDEX_BIB_ID, config, fd).then(function (response) {
                        var data = response.data;
                        $scope.partialIndexStatus = data;
                        console.log($scope.partialIndexStatus);
                        $scope.message = "Successfully submitted";
                        $scope.title = "Success Message";
                        $scope.showDialog = true;
                    }, function (response) {
                        console.log("Partial index initiation failed");
                        console.log(response);
                        $scope.message = "Submit failed!";
                        $scope.title = "Error";
                        $scope.showDialog = true;
                    });
                }else if ($scope.partialIndexRequest.type === 'indexByDate') {

                    var fromDate = $scope.partialIndexRequest.fromDate;
                    var numberOfThreads = $scope.partialIndexRequest.noOfDbThreads;
                    var docPerThread = $scope.partialIndexRequest.docsPerThread;
                    fd.append('fromDate', fromDate);
                    fd.append('numberOfThreads', numberOfThreads);
                    fd.append('docPerThread', docPerThread);
                    oleSolrClientAPIService.postRestCall(OLESOLRCLIENT_CONSTANTS.PARTIAL_INDEX_DATE, config, fd).then(function (response) {
                        var data = response.data;
                        $scope.partialIndexStatus = data;
                        console.log($scope.partialIndexStatus);
                        $scope.message = "Successfully submitted";
                        $scope.title = "Success Message";
                        $scope.showDialog = true;
                    }, function (response) {
                        console.log("Partial index initiation failed");
                        console.log(response);
                        $scope.message = "Submit failed!";
                        $scope.title = "Error";
                        $scope.showDialog = true;
                    });


                }else if ($scope.partialIndexRequest.type === 'indexByFile') {
                    var file = $scope.partialIndexRequest.fileToProcess;
                    var numberOfThreads = $scope.partialIndexRequest.noOfDbThreads;
                    var docPerThread = $scope.partialIndexRequest.docsPerThread;
                    fd.append('file', file);
                    fd.append('numberOfThreads', numberOfThreads);
                    fd.append('docPerThread', docPerThread);
                    oleSolrClientAPIService.postRestCall(OLESOLRCLIENT_CONSTANTS.PARTIAL_INDEX_FILE, config, fd).then(function (response) {
                        var data = response.data;
                        $scope.partialIndexStatus = data;
                        console.log($scope.partialIndexStatus);
                        $scope.message = "Successfully submitted";
                        $scope.title = "Success Message";
                        $scope.showDialog = true;
                    }, function (response) {
                        console.log("Partial index initiation failed");
                        console.log(response);
                        $scope.message = "Submit failed!";
                        $scope.title = "Error";
                        $scope.showDialog = true;
                    });
                }

            }

            


        }]);