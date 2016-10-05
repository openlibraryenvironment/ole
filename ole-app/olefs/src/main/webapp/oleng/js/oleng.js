/**
 * Created by SheikS on 12/20/2015.
 */

var OLENG_CONSTANTS = {
    PROFILE_GET_BIB_STATUS : "rest/describe/getBibStatus",
    PROFILE_GET_CALLNUMBER_TYPES : "rest/describe/getCallNumberTypes",
    PROFILE_GET_ITEM_TYPES : "rest/describe/getItemTypes",
    PROFILE_GET_ITEM_STATUS : "rest/describe/getItemStatus",
    PROFILE_GET_DONOR_CODES : "rest/describe/getDonorCodes",
    PROFILE_GET_LOCATIONS : "rest/describe/getLocations",
    PROFILE_GET_NAMES : "rest/describe/getProfileNames",
    PROFILE_GET_GLOBALLY_PROTECTED_FIELDS : "rest/describe/getGloballyProtectedFields",
    PROFILE_GET_ORDER_FIELD_VALUES : "rest/describe/getOrderImportFieldValues",
    PROFILE_GET_DROP_DOWN_VALUES : "rest/describe/getValuesForDropDown",
    PROFILE_GET_FILTER_NAMES : "rest/describe/getFilterNames",

    /*Profile Actions*/
    PROFILE_SEARCH : "../../rest/describe/profile/search",
    PROFILE_EDIT : "rest/describe/profile/edit",
    PROFILE_SUBMIT : "rest/describe/profile/submit",
    PROFILE_DELETE : "../../rest/describe/profile/delete",
    PROFILE_CANCEL : "portal.jsp",
    PROFILE_IMPORT : "../../rest/describe/profile/import",

    PROCESS_SUBMIT : "rest/batch/job/create",
    PROCESS_LAUNCH : "rest/batch/job/launch",
    PROCESS_QUICK_LAUNCH : "rest/batch/job/quickLaunch",
    PROCESS_SCHEDULE : "rest/batch/job/schedule",
    PROCESS_JOBS : "rest/describe/getBatchProcessJobs",
    BATCH_JOBS : "rest/describe/getBatchJobs",
    DESTROY_PROCESS : "rest/batch/job/destroy",
    UNSCHEDULE_PROCESS : "rest/batch/job/unShedule",
    RESUME_JOB: "rest/batch/job/resumeJob",
    PAUSE_JOB: "rest/batch/job/pauseJob",
    DELETE_JOB_EXECUTION: "rest/batch/job/deleteJobExecution",
    STOP_JOB_EXECUTION: "rest/batch/job/stopJobExecution",
    REPORT_FILES : "rest/batch/job/getReportsFiles",
    SPECIFIC_REPORT_FILES : "rest/batch/job/getSpecificReportsFiles",
    GET_FILE_CONTENT : "rest/batch/job/getFileContent",
    DOWNLOAD_REPORT_FILE : "rest/batch/job/downloadReportFile",
    BATCH_STATUS : "rest/batch/job/getBatchStatus",
    CAN_SHOW_LOCAL_DATA_MAPPING : "rest/describe/canShowLocalDataMapping"

};

var BATCH_CONSTANTS = {
    PROFILE_TYPES : [
        {id: 'orderRecordImport', value: 'Order Record Import'},
        {id: 'invoiceImport', value: 'Invoice Import'},
        {id: 'bibImport', value: 'Bib Import'},
        {id: 'batchExport' , value: 'Batch Export'},
        {id: 'batchDelete', value: 'Batch Delete'}
    ],
    OUTPUT_FORMATS : [
        {id: 'marc', name: 'Marc'},
        {id: 'marcXml', name: 'MarcXml'}
    ]
};



var getUrlVars = function () {
    var vars = {}, hash;
    var hashes = window.location.href.slice(window.location.href.indexOf('?') + 1).split('&');
    for (var i = 0; i < hashes.length; i++) {
        hash = hashes[i].split('=');
        vars[hash[0]] = hash[1];
    }
    return vars;
}


var doGetRequest = function($scope, $http, url, param, successCallback) {
    var user = getLoggedInUserName();
    var config = {
        headers:  {
            "userName" : user
        }
    };

    var errorCallback = function(response) {
        console.log(response);
    };

    if(param === null || param === undefined) {
        $http.get(url, config).then(successCallback, errorCallback);
    } else {
        $http({
            method: 'GET',
            url: url,
            params: param,
            headers:  {
                "userName" : user
            }
        }).then(successCallback,errorCallback);
    }
};

var doPostRequest = function($scope, $http, url, param, successCallback) {
    var user = getLoggedInUserName();
    var config = {
        headers:  {
            "userName" : user
        }
    };

    var errorCallback = function(response) {
        console.log(response);
    };
    if(param === null || param === undefined) {
        $http.post(url, config).then(successCallback, errorCallback);
    } else {
        $http.post(url, param, config).then(successCallback, errorCallback);
    }
};

var doPostRequestWithMultiPartData = function($scope, $http, url, param, successCallback, errorCallback) {
    var user = getLoggedInUserName();
    var config = {
        transformRequest: angular.identity,
        headers:  {
            "userName" : user,
            "Content-Type": undefined
        }
    };
    if(errorCallback === null || errorCallback === undefined) {
        errorCallback = function(response) {
            console.log(response);
        };
    }
    if(param === null || param === undefined) {
        $http.post(url, config).then(successCallback, errorCallback);
    } else {
        $http.post(url, param, config).then(successCallback, errorCallback);
    }
};

function getLoggedInUserName(){
    var userName = parent.$("div#login-info").text();
    var user = userName.replace("    Logged in User:","").trim();
    var strIndex = userName.indexOf('Impersonating User');
    if(strIndex !== -1) {
        var index = userName.indexOf("Impersonating User:");
        var impersonatingUser = userName.substring(index);
        user = impersonatingUser.replace("Impersonating User:","").trim();
    }
    return user;
}

var convertDateFormat = function (jsonObject, key) {
    var value = jsonObject[key];
    var dateToString2 = $.datepicker.formatDate('mm-dd-yy', value);
    jsonObject[key] = dateToString2;
    return jsonObject;
};

var setDateFormForFilterCriteria = function(filterCriteriaRow) {
    if (filterCriteriaRow["filterFieldName"] === 'Date Updated(of Bib, Holdings or Item)' || filterCriteriaRow["filterFieldName"] === 'Date Entered(of Bib, Holdings or Item)' || filterCriteriaRow["filterFieldName"] === 'Bib Status Updated Date'){
        filterCriteriaRow = convertDateFormat(filterCriteriaRow, "filterFieldValue");
        filterCriteriaRow = convertDateFormat(filterCriteriaRow, "filterFieldRangeFrom");
        filterCriteriaRow = convertDateFormat(filterCriteriaRow, "filterFieldRangeTo");
    }
}