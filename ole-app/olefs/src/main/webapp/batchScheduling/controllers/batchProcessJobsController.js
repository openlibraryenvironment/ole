var batchProcessJobApp = angular.module('batchScheduling', ['datatables']);

var jobTypes = [
    {id: 'adhoc', name: 'Adhoc'},
    {id: 'scheduled', name: 'Scheduled'}
];

var scheduleOptions = [
    {id: 'cron', name: 'Provide Cron Expression'},
    {id: 'schedule', name: 'Schedule'}
];

var scheduleTypes = [
    {id: 'once', name: 'Once'},
    {id: 'daily', name: 'Daily'},
    {id: 'everyWeek', name: 'Every Week'},
    {id: 'everyMonth', name: 'Every Month'}
];

var weekDays = [
    {id: 'monday', name: 'Monday'},
    {id: 'tuesday', name: 'Tuesday'},
    {id: 'wednesday', name: 'Wednesday'},
    {id: 'thursday', name: 'Thursday'},
    {id: 'friday', name: 'Friday'},
    {id: 'saturday', name: 'Saturday'},
    {id: 'sunday', name: 'Sunday'}
];

batchProcessJobApp.controller('batchSchedulingController', ['$scope', '$http', function ($scope, $http) {
    $scope.quickLaunch = {};

    $scope.init = function() {
        $scope.initializeCreateJob();
    };

    $scope.initializeCreateJob = function() {
        $scope.batchProcessCreateTab = {};
        $scope.selected = 1;
        $scope.page = 'batchScheduling/views/batchCreateJob.html';
        $scope.batchProcessTypeValues = batchProcessTypeValues;
        $scope.jobTypes = jobTypes;
        $scope.scheduleOptions = scheduleOptions;
        $scope.scheduleTypes = scheduleTypes;
        $scope.weekDays = weekDays;
        $scope.monthDays = [];
        for (var i = 1; i <= 31; i++) {
            $scope.monthDays.push({id: i,name: i});
        }
    };

    $scope.initializeJobs = function() {
        $scope.selected = 2;
        $scope.page = 'batchScheduling/views/batchJobs.html';
        $scope.batchProcessJobs = getBatchProcessJobs();
    };

    $scope.initializeExecutions = function() {
        $scope.selected = 3;
        $scope.page = 'batchScheduling/views/batchJobExecutions.html';
        $scope.batchJobs = getBatchJobs();
    };

    $scope.populateProfileNames = function() {
        $http.get(OLENG_CONSTANTS.PROFILE_GET_NAMES, {params: {"batchType": $scope.batchProcessCreateTab.batchProcessType}}).success(function(data) {
            $scope.profileNames = data;
        });
    };

    $scope.onChangeJobType = function() {
        $scope.batchProcessCreateTab.scheduleOption = 'Provide Cron Expression';
        $scope.batchProcessCreateTab.cronExpression = null;
        $scope.batchProcessCreateTab.scheduleType = null;
        $scope.batchProcessCreateTab.scheduleDate = null;
        $scope.batchProcessCreateTab.weekDay = null;
        $scope.batchProcessCreateTab.monthDay = null;
        $scope.batchProcessCreateTab.monthFrequency = null;
        $scope.batchProcessCreateTab.scheduleTime = null;
    };

    $scope.onChangeScheduleOption = function() {
        $scope.batchProcessCreateTab.cronExpression = null;
        $scope.batchProcessCreateTab.scheduleType = null;
        $scope.batchProcessCreateTab.scheduleDate = null;
        $scope.batchProcessCreateTab.weekDay = null;
        $scope.batchProcessCreateTab.monthDay = null;
        $scope.batchProcessCreateTab.monthFrequency = null;
        $scope.batchProcessCreateTab.scheduleTime = null;
    };

    $scope.submit = function () {
        getProfileNameById();
        var batchProcessJob = {
            "processId": $scope.batchProcessCreateTab.processId,
            "batchProcessName": $scope.batchProcessCreateTab.batchProcessName,
            "batchProcessType": $scope.batchProcessCreateTab.batchProcessType,
            "profileId": $scope.batchProcessCreateTab.profileId,
            "profileName": $scope.batchProcessCreateTab.profileName,
            "jobType": $scope.batchProcessCreateTab.jobType,
            "cronExpression": $scope.batchProcessCreateTab.cronExpression
        };

        $http.post(OLENG_CONSTANTS.PROCESS_SUBMIT, batchProcessJob).success(function (data) {
            $scope.batchProcessJob = data;
            $scope.initializeJobs();
            /*if ($scope.batchProcessJob.jobType === 'Adhoc') {
                $scope.initializeExecutions();
            } else if ($scope.batchProcessJob.jobType === 'Scheduled') {
                $scope.initializeJobs();
            }*/
        });
    };

    function getProfileNameById() {
        angular.forEach($scope.profileNames, function(value, key) {
            if ($scope.batchProcessCreateTab.profileId == value.profileId) {
                $scope.batchProcessCreateTab.profileName = value.profileName;
            }
        })
    }

    function getBatchProcessJobs() {
        $http.get(OLENG_CONSTANTS.PROCESS_JOBS).success(function(data) {
            $scope.batchProcessJobs = data;
        });
    }

    function getBatchJobs() {
        $http.get(OLENG_CONSTANTS.BATCH_JOBS).success(function(data) {
            $scope.batchJobs = data;
        });
    }

    $scope.launchJob = function (jobId) {
        $http.get(OLENG_CONSTANTS.PROCESS_LAUNCH, {params: {"jobId": jobId}}).success(function (data) {
            $scope.message = "Job Launched";
            $scope.initializeExecutions();
        });
    };

    $scope.quickLaunchModal = function (jobId) {
        $scope.jobId = jobId;
        $scope.showModal = !$scope.showModal;
    };

    $scope.closeModal = function() {
        $scope.quickLaunch.selectedFile = null;
        $scope.jobId = null;
        $scope.showModal = false;
    };

    $scope.destroyJob = function(jobId) {
        $http.get(OLENG_CONSTANTS.DESTROY_PROCESS, {params: {"jobId": jobId}}).success(function(data) {
            $scope.message = "Job Destroyed";
        });
    };

    $scope.submitQuickLaunch = function() {
        var fd = new FormData();
        fd.append('jobId', $scope.jobId);
        fd.append('file', $scope.quickLaunch.selectedFile);
        console.log($scope.quickLaunch.selectedFile);
        $http.post(OLENG_CONSTANTS.PROCESS_QUICK_LAUNCH, fd, {
            transformRequest: angular.identity,
            headers: {'Content-Type': undefined}
        }).success(function (data) {
            $scope.message = "Job Launched";
            $scope.showModal = false;
            //$scope.initializeExecutions();
        });
        $scope.showModal = false;
        $scope.closeModal();
    };

}]);

batchProcessJobApp.directive('modal', function () {
    return {
        template: '<div class="modal fade">' +
        '<div class="modal-dialog">' +
        '<div class="modal-content">' +
        '<div class="modal-header">' +
        '<button type="button" class="close" data-dismiss="modal" aria-hidden="true" ng-click="closeModal()">&times;</button>' +
        '<h4 class="modal-title">{{ title }}</h4>' +
        '</div>' +
        '<div class="modal-body" ng-transclude></div>' +
        '</div>' +
        '</div>' +
        '</div>',
        restrict: 'E',
        transclude: true,
        replace:true,
        scope:true,
        link: function postLink(scope, element, attrs) {
            scope.title = attrs.title;

            scope.$watch(attrs.visible, function(value){
                if(value == true)
                    $(element).modal('show');
                else
                    $(element).modal('hide');
            });

            $(element).on('shown.bs.modal', function(){
                scope.$apply(function(){
                    scope.$parent[attrs.visible] = true;
                });
            });

            $(element).on('hidden.bs.modal', function(){
                scope.$apply(function(){
                    scope.$parent[attrs.visible] = false;
                });
            });
        }
    };
});

batchProcessJobApp.directive('fileModel', ['$parse', function ($parse) {
    return {
        restrict: 'A',
        link: function(scope, element, attrs) {
            var model = $parse(attrs.fileModel);
            var modelSetter = model.assign;

            element.bind('change', function(){
                scope.$apply(function(){
                    modelSetter(scope, element[0].files[0]);
                });
            });
        }
    };
}]);