/**
 * Created by jayabharathreddy on 11/13/15.
 */
(function () {
    'use strict';

    app.controller('treesController', ['$scope', '$http', '$rootScope', '$interval', function ($scope, $http, $rootScope) {

        $scope.itemPageSize = 10;
        $scope.itemStart = 0;
        $scope.toggle = function (scope) {
            scope.collapsed = !scope.collapsed;
        };

        $scope.copyToTree1 = function () {
            $scope.message = '';
            var bibTree = [];
            buildBibTreeData(bibTree, $http, $scope);
            if (bibTree.length > 0) {
                $scope.tree1 = bibTree;
            }
        };

        $scope.copyToTree2 = function () {
            $scope.message = '';
            var bibTree = [];
            buildBibTreeData(bibTree, $http, $scope);
            if (bibTree.length > 0) {
                $scope.tree2 = bibTree;
            }
        };

        $rootScope.updateMessage = function () {
            $scope.message = $rootScope.message;
        };

        $rootScope.transfer = function (transfer) {
            $http.post("/olefs/rest/ngTransferController/transfer", transfer).success(function (data) {
                $scope.message = data.message;
            });
        };

        $rootScope.rebuildTree = function () {
            $scope.$watch('message', function () {
                if ($scope.message != '') {
                    rebuildTrees($scope, $http);
                }
            }, true);
        };

        $scope.leftTreePrevious = function (holdingsIdentifier) {
            previous(holdingsIdentifier, $scope.tree1, "left", $http, $scope, $rootScope);
        };

        $scope.leftTreeNext = function (holdingsIdentifier) {
            next(holdingsIdentifier, $scope.tree1, "left", $http, $scope, $rootScope);
        };

        $scope.rightTreePrevious = function (holdingsIdentifier) {
            previous(holdingsIdentifier, $scope.tree2, "right", $http, $scope, $rootScope);
        };

        $scope.rightTreeNext = function (holdingsIdentifier) {
            next(holdingsIdentifier, $scope.tree2, "right", $http, $scope, $rootScope);
        };

    }]);

}());






