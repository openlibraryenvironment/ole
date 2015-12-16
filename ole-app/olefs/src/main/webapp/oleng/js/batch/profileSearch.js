/**
 * Created by SheikS on 12/16/2015.
 */

var batchProfileSearchApp = angular.module('batchProfileSearchApp', []);

batchProfileSearchApp.controller('batchProfileSearchController', ['$scope','searchProfile', function($scope,searchProfile){

    $scope.profiles = [];

    var url = "../../batchProfile/batchProfileRestController/search";
    $scope.search = function(){
        $scope.profiles = [];
        var profileName = $scope.profileName;
        searchProfile.searchProfile($scope,profileName, url);
    };

}]);



batchProfileSearchApp.service('searchProfile', ['$http', function ($http) {
    this.searchProfile = function($scope,profileName,uploadUrl){
        var data = {};
        data["profileName"] = profileName;
        $http.post(uploadUrl, JSON.stringify(data), {
                transformRequest: angular.identity,
                headers: {'Content-Type': undefined}
            })
            .success(function(response){
                var profiles = JSON.stringify(response) ;
                var log = [];
                angular.forEach(response, function(value, key) {
                    $scope.profiles.push(value);
                }, log);
            })
            .error(function(){
                alert("Failed");
            });
    }
}]);

