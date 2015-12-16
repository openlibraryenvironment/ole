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
    this.uploadFileToUrl = function($scope,file, profileName,uploadUrl){
        var fd = new FormData();
        fd.append('file', file);
        fd.append('profileName', profileName);
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

batchProcessAPP.controller('batchProfileController', ['$scope', 'fileUpload', function($scope, fileUpload){

    $scope.profileNames = [
        {id: 'BibForInvoiceCasalini', name: 'BibForInvoiceCasalini'},
        {id: 'BibForInvoiceYBP', name: 'BibForInvoiceYBP'}
    ];
    var url = "ole-kr-krad/batch/upload";
    $scope.uploadFile = function(){
        var file = $scope.selectedFile;
        var profileName = $scope.profileName;
        console.log('file is ' );
        console.log('Profile Name is '  + profileName);
        console.dir(file);
        fileUpload.uploadFileToUrl($scope,file,profileName, url);
    };

}]);
