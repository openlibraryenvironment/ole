'use strict';

angular.module("oleSolrClient.login", ['ui.bootstrap', 'customDirectives'])
    .controller('loginCtrl', ["$rootScope", "$scope", "$http", "$location", "$window", "oleSolrClientAPIService",
        function ($rootScope, $scope, $http, $location, $window, oleSolrClientAPIService) {
            console.log("login");

            var authenticate = function (credentials, callback) {

                var headers = credentials ? {
                    authorization: "Basic "
                    + btoa(credentials.username + ":"
                        + credentials.password)
                } : {};

                oleSolrClientAPIService.getRestCall('authenticate', {
                    headers: headers
                }).then(function (response) {
                    var data = response.data;
                    if (data.name) {
                        $rootScope.loggedInName = data.name;
                        $rootScope.authenticated = true;
                        $rootScope.userRol = data.authorities[0].authority;
                    } else {
                        $rootScope.authenticated = false;
                    }
                    callback && callback($rootScope.authenticated);
                    $location.path("/fullIndex");
                }, function () {
                    $rootScope.authenticated = false;
                    callback && callback(false);
                    $location.path("/");
                });


            };

            $scope.credentials = {};
            $scope.login = function () {
                console.log($scope.credentials);
                authenticate($scope.credentials, function (authenticated) {
                    if (authenticated) {
                        console.log("Login succeeded");
                        $location.path("/fullIndex");
                        $scope.error = false;
                        $rootScope.authenticated = true;
                    } else {
                        console.log("Login failed");
                        $location.path("/");
                        $scope.error = true;
                        $rootScope.authenticated = false;
                    }
                })
            };

            $scope.reset = function () {
                $scope.credentials = null;
                $scope.userRol = null;
                $rootScope.credentials = null;
                $rootScope.userRol = null;
            };
        }]);