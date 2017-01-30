var batchProcessJobsApp = angular.module('batchProcessJobs', [
    'ui.bootstrap',
    'datatables', 'customDirectives', 'ui.date',
    'ole.app-filter'
]);

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
    {id: 'everyWeek', name: 'Weekly'},
    {id: 'everyMonth', name: 'Monthly'}
];

var weekDays = [
    {id: 'mon', name: 'MON'},
    {id: 'tue', name: 'TUE'},
    {id: 'wed', name: 'WED'},
    {id: 'thu', name: 'THU'},
    {id: 'fri', name: 'FRI'},
    {id: 'sat', name: 'SAT'},
    {id: 'sun', name: 'SUN'}
];

var monthDays = [];
frequency = [];



function enableDatePicker(){
    $( "#scheduleDate" ).datepicker({
        changeMonth: true,
        changeYear: true
    });
    $('#scheduleDate').keydown(function(event) {
        return false;
    });
}

batchProcessJobsApp.controller('batchProcessJobsController', ['$scope', '$http', '$interval', 'DTOptionsBuilder', function ($scope, $http, $interval, DTOptionsBuilder) {


    $scope.currentPage = 1;
    $scope.rowsToShow = [10,25, 50, 75, 100];
    $scope.pageSize = 10;

    for (var i = 1; i <= 31; i++) {
        monthDays.push({id: i,name: i});
    }
    for (var i = 1; i <= 12; i++) {
        frequency.push({id: i,name: i});
    }

    $scope.dateOptions = {
        changeYear: true,
        changeMonth: true,
        yearRange: '1900:-0'
    };

    $scope.quickLaunch = {
        outputFormats: BATCH_CONSTANTS.OUTPUT_FORMATS,

        outputFormat: 'Marc'
    };

    $scope.batchSchedule = {
        outputFormats: BATCH_CONSTANTS.OUTPUT_FORMATS,
        outputFormat: 'Marc',
        scheduleOption: 'Provide Cron Expression',
        scheduleOptions: scheduleOptions,
        scheduleTypes: scheduleTypes,
        weekDays: weekDays,
        monthDays: monthDays,
        frequency: frequency
    };

    $scope.init = function() {
        $scope.initializeJobs();
        $scope.dtOptions = DTOptionsBuilder.newOptions().withOption('aaSorting', [[0, 'desc']]);
    };

    $scope.initializeCreateJob = function() {
        $scope.batchProcessCreateTab = {};
        $scope.selected = 1;
        $scope.page = 'batchScheduling/views/batchCreateJob.html';
        $scope.profileTypeValues = BATCH_CONSTANTS.PROFILE_TYPES;
        //$scope.jobTypes = jobTypes;
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
        doGetRequest($scope, $http, OLENG_CONSTANTS.PROFILE_GET_NAMES, {"batchType": $scope.batchProcessCreateTab.profileType},function(response) {
            var data = response.data;
            $scope.profileNames = data;
        });
    };

    $scope.onChangeJobType = function() {
        clearScheduleValues();
    };

    $scope.onChangeScheduleOption = function() {
        $scope.batchSchedule.cronExpression = null;
        $scope.batchSchedule.scheduleType = null;
        $scope.batchSchedule.scheduleDate = null;
        $scope.batchSchedule.weekDay = null;
        $scope.batchSchedule.monthDay = null;
        $scope.batchSchedule.monthFrequency = null;
        $scope.batchSchedule.scheduleTime = null;
    };


    function clearScheduleValues() {
        $scope.batchSchedule.scheduleOption = 'Provide Cron Expression';
        $scope.batchSchedule.cronExpression = null;
        $scope.batchSchedule.scheduleType = null;
        $scope.batchSchedule.scheduleDate = null;
        $scope.batchSchedule.weekDay = null;
        $scope.batchSchedule.monthDay = null;
        $scope.batchSchedule.monthFrequency = null;
        $scope.batchSchedule.scheduleTime = null;
    }

    $scope.submit = function () {
        getProfileNameById();
        var batchProcessJob = {
            "jobId": $scope.batchProcessCreateTab.jobId,
            "jobName": $scope.batchProcessCreateTab.jobName,
            "profileType": $scope.batchProcessCreateTab.profileType,
            "profileId": $scope.batchProcessCreateTab.profileId,
            "profileName": $scope.batchProcessCreateTab.profileName,
            //"jobType": $scope.batchProcessCreateTab.jobType,
            "schedule": getBatchSchedule()
        };

        angular.element(document.getElementById('submit'))[0].disabled = true;
        //angular.element(document.getElementById('jobType'))[0].disabled = true;
        angular.element(document.getElementById('profileId'))[0].disabled = true;
        angular.element(document.getElementById('profileType'))[0].disabled = true;
        angular.element(document.getElementById('jobName'))[0].disabled = true;
        doPostRequest($scope, $http, OLENG_CONSTANTS.PROCESS_SUBMIT, batchProcessJob, function (response) {
            var data = response.data;
            $scope.batchProcessJob = data;
            $scope.initializeJobs();
        });
    };

    function getProfileNameById() {
        angular.forEach($scope.profileNames, function(value, key) {
            if ($scope.batchProcessCreateTab.profileId == value.profileId) {
                $scope.batchProcessCreateTab.profileName = value.profileName;
            }
        })
    }

    function getBatchSchedule() {
        var batchSchedule = {
            jobId: $scope.jobId,
            scheduleOption: $scope.batchSchedule.scheduleOption,
            scheduleType: $scope.batchSchedule.scheduleType,
            scheduleDate: $scope.batchSchedule.scheduleDate,
            scheduleTime: $scope.batchSchedule.scheduleTime,
            weekDay: $scope.batchSchedule.weekDay,
            monthDay: $scope.batchSchedule.monthDay,
            monthFrequency: $scope.batchSchedule.monthFrequency,
            cronExpression: $scope.batchSchedule.cronExpression
        };
        return batchSchedule;
    }

    function getBatchProcessJobs() {
        doGetRequest($scope, $http, OLENG_CONSTANTS.PROCESS_JOBS, null, function(response) {
            var data = response.data;
            $scope.batchProcessJobs = data;
        });
    }

    function getBatchJobs() {
        doGetRequest($scope, $http, OLENG_CONSTANTS.BATCH_JOBS, null, function(response) {
            var data = response.data;
            $scope.batchJobs = data;
        });
    }

    $scope.quickLaunchPopUp = function (batchProcessJob) {
        document.getElementById('modalContentId').style.width = '550px';
        if (batchProcessJob.profileType == 'Batch Export') {
            document.getElementById('modalContentId').style.height = '280px';
        } else {
            document.getElementById('modalContentId').style.height = '190px';
        }
        $scope.jobId = batchProcessJob.jobId;
        $scope.profileType = batchProcessJob.profileType;
        $scope.exportInputFile = batchProcessJob.exportInputFile;
        $scope.quickLaunch.showModal = !$scope.quickLaunch.showModal;
    };

    $scope.schedulePopUp = function (index, batchProcessJob) {
        clearScheduleValues();
        $scope.clearValidationMessages();
        document.getElementById('scheduleJobPopUpId').firstElementChild.firstElementChild.style.width = '750px';
        if (batchProcessJob.profileType == 'Batch Export') {
            if(batchProcessJob.exportInputFile) {
                document.getElementById('scheduleJobPopUpId').firstElementChild.firstElementChild.style.height = '400px';
            } else {
                document.getElementById('scheduleJobPopUpId').firstElementChild.firstElementChild.style.height = '350px';
            }
        } else {
            document.getElementById('scheduleJobPopUpId').firstElementChild.firstElementChild.style.height = '300px';
        }
        $scope.jobId = batchProcessJob.jobId;
        $scope.profileType = batchProcessJob.profileType;
        $scope.exportInputFile = batchProcessJob.exportInputFile;
        $scope.index = index;
        $scope.batchSchedule.showModal = !$scope.batchSchedule.showModal;
    };

    $scope.closeModal = function() {
        $scope.jobId = null;
        if ($scope.quickLaunch.showModal) {
            $scope.quickLaunch.selectedFile = null;
            document.getElementById('selectedFile').value = null;
            $scope.quickLaunch.showModal = false;
        } else if ($scope.batchSchedule.showModal) {
            $scope.batchSchedule.scheduleJobFile = null;
            document.getElementById('scheduleJobFile').value = null;
            $scope.batchSchedule.showModal = false;
        }
    };

    $scope.scheduleJob = function() {
        $scope.clearValidationMessages();
        var fd = new FormData();
        var isValid = $scope.validateSchedulerOptions($scope);
        if(isValid) {
            fd.append('jobId', $scope.jobId);
            fd.append('numOfRecordsInFile', $scope.batchSchedule.numOfRecordsInFile);
            fd.append('extension', $scope.batchSchedule.outputFormat);
            fd.append('file', $scope.batchSchedule.scheduleJobFile);
            fd.append('scheduleJob', JSON.stringify(getBatchSchedule()));
            doPostRequestWithMultiPartData($scope, $http, OLENG_CONSTANTS.PROCESS_SCHEDULE, fd, function(response) {
                var data = response.data;
                var index = $scope.index;
                var batchProcessJob = $scope.batchProcessJobs[index];
                batchProcessJob["jobType"] = data["jobType"];
                batchProcessJob["cronExpression"] = data["cronExpression"];
                batchProcessJob["nextRunTime"] = data["nextRunTime"];
                $scope.batchProcessJobs[index] = batchProcessJob;
                $scope.index = null;
                $scope.message = "Job Scheduled";
                $scope.batchSchedule.showModal = false;
                $scope.closeModal();
            });
            document.getElementById('scheduleJobFile').value = null;
            $scope.batchSchedule.showModal = false;
            $scope.closeModal();
        }
    };

    $scope.submitQuickLaunchJob = function() {
        var fd = new FormData();
        fd.append('jobId', $scope.jobId);
        fd.append('numOfRecordsInFile', $scope.quickLaunch.numOfRecordsInFile);
        fd.append('extension', $scope.quickLaunch.outputFormat);
        fd.append('file', $scope.quickLaunch.selectedFile);
        doPostRequestWithMultiPartData($scope, $http, OLENG_CONSTANTS.PROCESS_QUICK_LAUNCH, fd, function(response) {
            var data = response.data;
            $scope.message = "Job Launched";
            $scope.quickLaunch.showModal = false;
            $scope.closeModal();
            //$scope.initializeExecutions();
        });
        document.getElementById('selectedFile').value = null;
        $scope.quickLaunch.showModal = false;
        $scope.closeModal();
    };

    $scope.destroyJob = function(index,jobId) {
        var jobIdToDelete = Number(jobId);
        doGetRequest($scope, $http, OLENG_CONSTANTS.DESTROY_PROCESS, {"jobId": jobIdToDelete}, function(response) {
            var data = response.data;
            $scope.message = "Job Destroyed";
            $scope.batchProcessJobs.splice(index, 1);
        });
    };

    $scope.unSchedule = function(index,jobId) {
        var jobIdToUnschedule = Number(jobId);
        doGetRequest($scope, $http, OLENG_CONSTANTS.UNSCHEDULE_PROCESS, {"jobId": jobIdToUnschedule}, function(response) {
            var data = response.data;
            var batchProcessJob = $scope.batchProcessJobs[index];
            batchProcessJob["jobType"] = data["jobType"];
            batchProcessJob["cronExpression"] = data["cronExpression"];
            batchProcessJob["nextRunTime"] = data["nextRunTime"];
            $scope.batchProcessJobs[index] = batchProcessJob;
            $scope.message = "Job Unscheduled";
        });
    };

    $scope.pauseJob = function(index,jobId) {
        var jobIdToPause = Number(jobId);
        doGetRequest($scope, $http, OLENG_CONSTANTS.PAUSE_JOB, {"jobId": jobIdToPause}, function(response) {
            var data = response.data;
            var batchProcessJob = $scope.batchProcessJobs[index];
            batchProcessJob["jobType"] = data["jobType"];
            batchProcessJob["cronExpression"] = data["cronExpression"];
            batchProcessJob["nextRunTime"] = data["nextRunTime"];
            $scope.batchProcessJobs[index] = batchProcessJob;
            $scope.message = "Job Unscheduled";
        });
    };

    $scope.resumeJob = function(index,jobId) {
        var jobIdToResume = Number(jobId);
        doGetRequest($scope, $http, OLENG_CONSTANTS.RESUME_JOB, {"jobId": jobIdToResume}, function(response) {
            var data = response.data;
            var batchProcessJob = $scope.batchProcessJobs[index];
            batchProcessJob["jobType"] = data["jobType"];
            batchProcessJob["cronExpression"] = data["cronExpression"];
            batchProcessJob["nextRunTime"] = data["nextRunTime"];
            $scope.batchProcessJobs[index] = batchProcessJob;
            $scope.message = "Job Unscheduled";
        });
    };

    $scope.enableAutoRefresh = function() {
        $interval(function() {
            if ($scope.selected == 3) {
                $scope.initializeExecutions()
            }
        }, 5000);
    };

    $scope.validateSchedulerOptions = function() {
        var isValid = true;
        if($scope.batchSchedule.scheduleOption === 'Provide Cron Expression') {
            if($scope.isFieldEmpty($scope.batchSchedule.cronExpression)) {
                $scope.batchProcessJobsForm['cronExpression'].$dirty = true;
                $scope.batchProcessJobsForm['cronExpression'].$invalid = true;
                isValid = false;
            }
        } else if($scope.batchSchedule.scheduleOption === 'Schedule'){
            if($scope.isFieldEmpty($scope.batchSchedule.scheduleType)) {
                $scope.batchProcessJobsForm['scheduleType'].$dirty = true;
                $scope.batchProcessJobsForm['scheduleType'].$invalid = true;
                isValid = false;
            }

            if($scope.batchSchedule.scheduleType === 'Once') {
                if($scope.isFieldEmpty($scope.batchSchedule.scheduleDate)) {
                    $scope.batchProcessJobsForm['scheduleDate'].$dirty = true;
                    $scope.batchProcessJobsForm['scheduleDate'].$invalid = true;
                    isValid = false;
                }
                if($scope.isFieldEmpty($scope.batchSchedule.scheduleTime)) {
                    $scope.batchProcessJobsForm['scheduleTime'].$dirty = true;
                    $scope.batchProcessJobsForm['scheduleTime'].$invalid = true;
                    isValid = false;
                }
                isValid = isValid && $scope.validateHhMm()
            } else if($scope.batchSchedule.scheduleType === 'Daily') {
                if($scope.isFieldEmpty($scope.batchSchedule.scheduleTime)) {
                    $scope.batchProcessJobsForm['scheduleTime'].$dirty = true;
                    $scope.batchProcessJobsForm['scheduleTime'].$invalid = true;
                    isValid = false;
                }
                isValid = isValid && $scope.validateHhMm()
            } else if($scope.batchSchedule.scheduleType === 'Weekly') {
                if($scope.isFieldEmpty($scope.batchSchedule.weekDay)) {
                    $scope.batchProcessJobsForm['weekDay'].$dirty = true;
                    $scope.batchProcessJobsForm['weekDay'].$invalid = true;
                    isValid = false;
                }
                if($scope.isFieldEmpty($scope.batchSchedule.scheduleTime)) {
                    $scope.batchProcessJobsForm['scheduleTime'].$dirty = true;
                    $scope.batchProcessJobsForm['scheduleTime'].$invalid = true;
                    isValid = false;
                }
                isValid = isValid && $scope.validateHhMm()

            } else if($scope.batchSchedule.scheduleType === 'Monthly') {
                if($scope.isFieldEmpty($scope.batchSchedule.monthDay)) {
                    $scope.batchProcessJobsForm['monthDay'].$dirty = true;
                    $scope.batchProcessJobsForm['monthDay'].$invalid = true;
                    isValid = false;
                }
                if($scope.isFieldEmpty($scope.batchSchedule.scheduleTime)) {
                    $scope.batchProcessJobsForm['monthFrequency'].$dirty = true;
                    $scope.batchProcessJobsForm['monthFrequency'].$invalid = true;
                    isValid = false;
                }
                if($scope.isFieldEmpty($scope.batchSchedule.scheduleTime)) {
                    $scope.batchProcessJobsForm['scheduleTime'].$dirty = true;
                    $scope.batchProcessJobsForm['scheduleTime'].$invalid = true;
                    isValid = false;
                }
                isValid = isValid && $scope.validateHhMm()

            }

        }
        console.log("valid : " + isValid);
        return isValid;
    };

    $scope.clearValidationMessages = function() {
        $scope.batchProcessJobsForm['cronExpression'].$dirty = false;
        $scope.batchProcessJobsForm['cronExpression'].$invalid = false;
        $scope.batchProcessJobsForm['scheduleType'].$dirty = false;
        $scope.batchProcessJobsForm['scheduleType'].$invalid = false;
        $scope.batchProcessJobsForm['scheduleDate'].$dirty = false;
        $scope.batchProcessJobsForm['scheduleDate'].$invalid = false;
        $scope.batchProcessJobsForm['scheduleTime'].$dirty = false;
        $scope.batchProcessJobsForm['scheduleTime'].$invalid = false;
        $scope.batchProcessJobsForm['weekDay'].$dirty = false;
        $scope.batchProcessJobsForm['weekDay'].$invalid = false;
        $scope.batchProcessJobsForm['monthDay'].$dirty = false;
        $scope.batchProcessJobsForm['monthDay'].$invalid = false;
        $scope.batchProcessJobsForm['monthFrequency'].$dirty = false;
        $scope.batchProcessJobsForm['monthFrequency'].$invalid = false;
        document.getElementById("scheduleTime").style.backgroundColor = '#ffffff';
    };

    $scope.validateHhMm = function() {
        var time = $scope.batchSchedule.scheduleTime;
        var isValid = /^([0-1]?[0-9]|2[0-4]):([0-5][0-9])(:[0-5][0-9])?$/.test(time);
        if (isValid) {
            document.getElementById("scheduleTime").style.backgroundColor = '#bfa';
        } else {
            document.getElementById("scheduleTime").style.backgroundColor = '#fba';
        }
        return isValid;
    }


    $scope.isFieldEmpty = function (field) {
        if (field == undefined || field == null || field == '') {
            return true;
        }
        return false;
    };



    $scope.deleteJobDetails = function(index, jobExecutionId) {
        var jobDetailsId = Number(jobExecutionId);
        doGetRequest($scope, $http, OLENG_CONSTANTS.DELETE_JOB_EXECUTION, {"jobDetailsId": jobDetailsId}, function(response) {
            $scope.batchJobs.splice(index, 1);
        });
    };

    $scope.stopJobExecution = function(index, jobExecutionId) {
        var jobDetailsId = Number(jobExecutionId);
        doGetRequest($scope, $http, OLENG_CONSTANTS.STOP_JOB_EXECUTION, {"jobDetailsId": jobDetailsId}, function(response) {
            var data = response.data;
            var batchJob = $scope.batchJobs[index];
            batchJob["status"] = data["status"];
            $scope.batchJobs[index] = batchJob;
        });
    };

}]);