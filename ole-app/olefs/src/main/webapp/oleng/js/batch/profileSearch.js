/**
 * Created by SheikS on 12/16/2015.
 */

var batchProfileSearchApp = angular.module('batchProfileSearchApp', ['ngLoadingSpinner', 'datatables', 'customDirectives', 'ngAnimate', 'ngSanitize', 'mgcrea.ngStrap', 'ui.bootstrap']);

batchProfileSearchApp.controller('batchProfileSearchController', ['$scope','searchProfile','$http', function($scope,searchProfile,$http) {

    initializeProfilePanels();
    $scope.profiles = [];
    $scope.profileTypes = BATCH_CONSTANTS.PROFILE_TYPES;

    var url = OLENG_CONSTANTS.PROFILE_SEARCH;
    $scope.search = function(){
        $scope.profiles = [];
        var profileName = $scope.profileName;
        var profileType = $scope.profileType;
        searchProfile.searchProfile($scope,profileName, profileType, url);
    };

    $scope.showModal = false;
    $scope.profileInquiry = function(profileId) {
        initializeProfilePanels();
        document.getElementById('modalContentId').style.width = '1250px';
        document.getElementById('modalContentId').style.height = '500px';
        document.getElementById('modalContentId').style.overflowX = 'auto';
        document.getElementById('modalContentId').style.overflowY = 'auto';
        document.getElementById('modalContentId').style.left = '-320px';
        document.getElementById('modalContentId').style.top = '80px';
        for (var i = 0; i < $scope.profiles.length; i++) {
            if (profileId == $scope.profiles[i].profileId) {
                var profile = JSON.parse($scope.profiles[i].content);
                $scope.mainSectionPanel.profileId = profile.profileId;
                $scope.mainSectionPanel.profileName = profile.profileName;
                $scope.mainSectionPanel.profileDescription = profile.description;
                $scope.mainSectionPanel.batchProcessType = profile.batchProcessType;
                $scope.mainSectionPanel.bibImportProfileForOrderImport = profile.bibImportProfileForOrderImport;
                $scope.mainSectionPanel.requisitionForTitlesOption = profile.requisitionForTitlesOption;
                $scope.mainSectionPanel.matchPointToUse = profile.matchPointToUse;
                $scope.mainSectionPanel.dataToExport = profile.dataToExport;
                $scope.mainSectionPanel.exportScope = profile.exportScope;
                $scope.mainSectionPanel.orderType = profile.orderType;
                $scope.mainSectionPanel.marcOnly = profile.marcOnly;
                $scope.filterCriteriaPanel = profile.batchProfileFilterCriteriaList;
                $scope.matchPointsPanel = profile.batchProfileMatchPointList;
                $scope.addOrOverlayPanel = profile.batchProfileAddOrOverlayList;
                $scope.fieldOperationsPanel = profile.batchProfileFieldOperationList;
                $scope.dataMappingsPanel = profile.batchProfileDataMappingList;
                $scope.dataTransformationsPanel = profile.batchProfileDataTransformerList;
                $scope.submitted = true;
                $scope.inquiry = true;
                break;
            }
        }
        $scope.showModal = !$scope.showModal;
    };
    $scope.closeModal = function(){
        $scope.showModal = false;
    };

    $scope.exportProfile = function (profileId) {
        var blob = new Blob([$scope.profiles[profileId].content], {
            type: "application/json;charset=utf-8"
        });
        saveAs(blob, $scope.profiles[profileId].profileName + ".txt");
    };

    $scope.deleteProfile = function (profileId,index) {
        var data = {};
        data["profileId"] = profileId;
        doPostRequest($scope, $http, OLENG_CONSTANTS.PROFILE_DELETE, JSON.stringify(data), function (response) {
                $scope.profiles.splice(index, 1);
            });
    };

    function initializeProfilePanels() {
        $scope.mainSectionActivePanel = [0];
        $scope.filterCriteriaActivePanel = [];
        $scope.matchPointsActivePanel = [];
        $scope.addOrOverlayActivePanel = [];
        $scope.fieldOperationsActivePanel = [];
        $scope.dataMappingsActivePanel = [];
        $scope.dataTransformationsActivePanel = [];
        $scope.mainSectionPanel = [];
        $scope.filterCriteriaPanel = [];
        $scope.matchPointsPanel = [];
        $scope.addOrOverlayPanel = [];
        $scope.fieldOperationsPanel = [];
        $scope.dataMappingsPanel = [];
        $scope.dataTransformationsPanel = [];
        $scope.mainSectionPanel.collapsed = false;
        $scope.filterCriteriaPanel.collapsed = false;
        $scope.matchPointsPanel.collapsed = false;
        $scope.addOrOverlayPanel.collapsed = false;
        $scope.fieldOperationsPanel.collapsed = false;
        $scope.dataMappingsPanel.collapsed = false;
        $scope.dataTransformationsPanel.collapsed = false;
    }

}]);

batchProfileSearchApp.service('searchProfile', ['$http', function ($http) {
    this.searchProfile = function($scope,profileName,profileType, uploadUrl){
        var data = {};
        data["profileName"] = profileName;
        data["profileType"] = profileType;
        doPostRequest($scope, $http, uploadUrl, JSON.stringify(data), function(response){
                var data = response.data;
                var profiles = JSON.stringify(data) ;
                var log = [];
                angular.forEach(data, function(value, key) {
                    $scope.profiles.push(value);
                }, log);
            });
    }
}]);
