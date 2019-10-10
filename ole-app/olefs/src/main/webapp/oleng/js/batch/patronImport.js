/**
 * Created by SheikS on 12/16/2015.
 */

var patronImportApp = angular.module('patronImportApp', ['ngLoadingSpinner', 'datatables', 'customDirectives', 'ngAnimate', 'ngSanitize', 'mgcrea.ngStrap', 'ui.bootstrap']);

patronImportApp.directive('fileModel', ['$parse', function ($parse) {
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

patronImportApp.controller('patronImportController', ['$scope','loadPatron','$http', function($scope,loadPatron,$http) {

    $scope.patronList = [];
    $scope.patronFields = PATRON_CONSTANTS.PATRON_IMPORT_CRITERIA;
    $scope.patronField = 'olePatronId';
    $scope.displayEditButton = false;

    var url = "rest/batch/patronImport";
    $scope.load = function() {
            var file = $scope.selectedFile;
            if (file == undefined) {
                $scope.fileMessage = "Please upload the file. File Extension should be .txt";
            } else {
                var fileName = file.name;
                $scope.fileMessage = ''
                var patronField = $scope.patronField;
                var extension = fileName.substring(fileName.lastIndexOf('.'), fileName.length);
                if (extension == '.txt') {
                loadPatron.loadPatron($scope, file, patronField, url);
                } else {
                    $scope.selectedFile = '';
                    $scope.fileMessage = "Please upload correct file. File Extension should be .txt"
                }
            }
    };

    $scope.clear = function (){
        $scope.patronList = [];
        $scope.displayEditButton = false;
        angular.element("input[type='file']").val(null);
        $scope.selectedFile = undefined;
    }

    $scope.showModal = false;

    $scope.closeModal = function(){
        $scope.showModal = false;
    };

    $scope.selectedPatron = function(selected, olePatronId){
        console.log(selected, olePatronId);
        angular.forEach($scope.patronList, function (value,index){
            if(value.olePatronId == olePatronId){
                value.selected = selected;
            }
        });

    }

    $scope.selectedAllPatron = function(selected){
        angular.forEach($scope.patronList, function (value,index){
            value.selected = selected;
        });

    }

    $scope.editPatron = function(){
        var selectedPatron = [];
        var redirectUrl = "rest/batch/appendPatron";
        angular.forEach($scope.patronList, function (value,index){
            if(value.selected){
                selectedPatron.push(value.olePatronId);
            }
        });
        doPostRequest($scope, $http, redirectUrl,{"selectedPatronId": selectedPatron}, function (response) {
            window.open(window.location.origin+"/olefs/portal.do?channelTitle=Patron&channelUrl="+window.location.origin+"/olefs/ole-kr-krad/patronGlobalEditController?viewId=OlePatronGlobalEditView&methodToCall=start");
        })
    }
}]);

patronImportApp.service('loadPatron', ['$http', function ($http) {
    this.loadPatron = function($scope,file,patronField, uploadUrl){
        var fd = new FormData();
        fd.append('file', file);
        fd.append('patronField', patronField);
        doPostRequestWithMultiPartData($scope, $http, uploadUrl, fd, function (response) {
            var data = response.data;
            var patronList = data.patronList;
            $scope.patronList = [];
            angular.forEach(patronList, function(value, key) {
                $scope.patronList.push(value);
            });
            if(patronList.length>0){
                $scope.displayEditButton = true;
            }
            angular.forEach($scope.patronList, function (value,index){
                value.selected = true;
            });
        });
    }
}]);
