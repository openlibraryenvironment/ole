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
    this.uploadFileToUrl = function(file, profileName,uploadUrl){
        var fd = new FormData();
        fd.append('file', file);
        fd.append('profileName', profileName);
        $http.post(uploadUrl, fd, {
                transformRequest: angular.identity,
                headers: {'Content-Type': undefined}
            })
            .success(function(){
            })
            .error(function(){
            });
    }
}]);

batchProcessAPP.controller('batchProfileController', ['$scope', 'fileUpload', function($scope, fileUpload){

    $scope.profileNames = [
        {id: 'BibForInvoiceCasalini', name: 'BibForInvoiceCasalini'},
        {id: 'BibForInvoiceYBP', name: 'BibForInvoiceYBP'}
    ];

    var url = "http://localhost:8080/olefs/rest/batch/upload";
    $scope.uploadFile = function(){
        var file = $scope.selectedFile;
        var profileName = $scope.profileName;
        console.log('file is ' );
        console.log('Profile Name is '  + profileName);
        console.dir(file);
        fileUpload.uploadFileToUrl(file,profileName, url);
    };

}]);
