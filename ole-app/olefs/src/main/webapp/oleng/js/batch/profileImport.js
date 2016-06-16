/**
 * Created by SheikS on 12/16/2015.
 */
var batchProfileImportApp = angular.module('batchProfileImportApp', []);

batchProfileImportApp.directive('fileModel', ['$parse', function ($parse) {
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

batchProfileImportApp.service('fileUpload', ['$http', function ($http) {
    this.uploadFileToUrl = function($scope,file, profileName,uploadUrl){
        var fd = new FormData();
        fd.append('file', file);
        fd.append('profileName', profileName);
        $scope.profileImportStatus = "Batch Profile Import initiated....";
        angular.element(document.getElementById('run'))[0].disabled = true;
        angular.element(document.getElementById('file'))[0].disabled = true;
        doPostRequestWithMultiPartData($scope, $http, uploadUrl, fd, function(response){
                var data = response.data;
                var totalTime = data.processTime;
                var report = "Profile imported successfully .\nTotal time taken : " +totalTime;
                $scope.profileImportStatus = report;

            }, function(){
                $scope.profileImportStatus = "Profile import failed failed.";
            });
    }
}]);

batchProfileImportApp.controller('batchProfileImportController', ['$scope', 'fileUpload','$http', function($scope, fileUpload,$http){

    $scope.profileNames = [];
    var url = OLENG_CONSTANTS.PROFILE_IMPORT;
    $scope.importFile = function(){
        var file = $scope.selectedFile;
        var profileName = $scope.profileName;
        console.log('file is ' );
        console.dir(file);
        fileUpload.uploadFileToUrl($scope,file,profileName, url);
    };

}]);
