'use strict';

angular.module("oleSolrClient.report", ['ui.bootstrap', 'ngStorage', 'ui.bootstrap.datetimepicker'])
    .controller('reportCtrl', ["$scope", "$location", "$routeParams", "oleSolrClientAPIService", "$localStorage", '$filter',
        function ($scope, $location, $routeParams, oleSolrClientAPIService, $localStorage, $filter) {
            console.log("Generate Report");

            $scope.currentPage = 1;
            $scope.pageSize = 10;

            $scope.inlineOptions = {
                customClass: getDayClass,
                minDate: new Date(),
                showWeeks: true
            };

            $scope.dateOptions = {
                //dateDisabled: disabled,
                formatYear: 'yy',
                maxDate: new Date(2020, 5, 22),
                minDate: new Date(),
                startingDay: 1
            };

            $scope.toggleMin = function() {
                $scope.inlineOptions.minDate = $scope.inlineOptions.minDate ? null : new Date();
                $scope.dateOptions.minDate = $scope.inlineOptions.minDate;
            };

            $scope.toggleMin();

            $scope.openCreatedDateFrom = function() {
                $scope.createdDateFromPopup.opened = true;
            };

            $scope.format = 'dd-MM-yyyy';

            $scope.createdDateFromPopup = {
                opened: false
            };

            function getDayClass(data) {
                var date = data.date,
                    mode = data.mode;
                if (mode === 'day') {
                    var dayToCheck = new Date(date).setHours(0,0,0,0);

                    for (var i = 0; i < $scope.events.length; i++) {
                        var currentDay = new Date($scope.events[i].date).setHours(0,0,0,0);

                        if (dayToCheck === currentDay) {
                            return $scope.events[i].status;
                        }
                    }
                }

                return '';
            }

            $scope.initializeReportList = function() {
                oleSolrClientAPIService.getRestCall(OLESOLRCLIENT_CONSTANTS.GET_REPORT_FILES, {}).then(function (response) {
                    var data = response.data;
                    console.log(data);
                    $scope.fileList = data;

                }, function (response) {
                    console.log("Loading initial files failed.");
                    console.log(response);

                });
            };


            $scope.initializeReportList();

            $scope.generateReport = function () {
                oleSolrClientAPIService.postRestCall(OLESOLRCLIENT_CONSTANTS.GENERATE_REPORT, {}, $scope.reportRequest).then(function (response) {
                    var data = response.data;

                    $scope.message = data.status;
                    $scope.title = "Success";
                    $scope.showDialog = true;
                    console.log(data);

                    $scope.initializeReportList();

                }, function (response) {
                    console.log("Report generation failed");
                    console.log(response);
                    $scope.message = "Submit failed!";
                    $scope.title = "Error";
                    $scope.showDialog = true;
                });
            };

            $scope.downloadReport = function(file) {
                var url = OLESOLRCLIENT_CONSTANTS.DOWNLOAD_REPORT_FILE + "?fileName=" + file.name + "&parent=" + file.parent;
                window.location.href = url;
            };

        }]);