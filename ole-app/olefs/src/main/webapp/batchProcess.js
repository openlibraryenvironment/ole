var batchProcessAPP = angular.module('batchProcessAPP', []);

batchProcessAPP.directive('fileModel', ['$parse', function ($parse) {
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

batchProcessAPP.service('fileUpload', ['$http', function ($http) {
    this.uploadFileToUrl = function($scope,file, profileName,batchType, uploadUrl){
        var fd = new FormData();
        fd.append('file', file);
        fd.append('profileName', profileName);
        fd.append('batchType', batchType);
        $scope.batchProcessStatus = "Batch process job initiated....";
        angular.element(document.getElementById('run'))[0].disabled = true;
        angular.element(document.getElementById('file'))[0].disabled = true;
        angular.element(document.getElementById('profileName'))[0].disabled = true;
        $http.post(uploadUrl, fd, {
                transformRequest: angular.identity,
                headers: {'Content-Type': undefined}
            })
            .success(function(response){
                var totalTime = response.processTime;
                var report = "Job successfully completed.\nTotal time taken : " +totalTime;
                $scope.batchProcessStatus = report;

            })
            .error(function(){
                $scope.batchProcessStatus = "Job failed.";
            });
    }
}]);

batchProcessAPP.controller('batchProfileController', ['$scope', 'fileUpload','$http', function($scope, fileUpload,$http){

    $scope.batchProcessTypes = [
        {id: 'orderRecordImport', value: 'Order Record Import'},
        {id: 'invoiceImport', value: 'Invoice Import'},
        {id: 'bibImport', value: 'Bib Import'}
    ];

    $scope.profileNames = [];
    var url = "rest/batch/upload";
    $scope.uploadFile = function(){
        var file = $scope.selectedFile;
        var profileName = $scope.profileName;
        var batchType = $scope.batchType;
        console.log('file is ' );
        console.log('Profile Name is '  + profileName);
        console.dir(file);
        fileUpload.uploadFileToUrl($scope,file,profileName,batchType, url);
    };

    $scope.populationProfileNames = function() {
        $http.get(OLENG_CONSTANTS.PROFILE_GET_NAMES, {params: {"batchType": $scope.batchType}}).success(function(data) {
            $scope.profileNames = data;
        });
    }

}]);
