/**
 * Created by jayabharathreddy on 11/13/15.
 */
(function () {
    'use strict';

    app.controller('treesController', ['$scope', '$http' , '$rootScope', function ($scope, $http, $rootScope) {

        $scope.itemPageSize = 5;
        $scope.itemStart = 0;
        $scope.toggle = function (scope) {
            scope.collapsed = !scope.collapsed;
        };
        $scope.copyToTree1 = function () {
            var bibTree = [];
            buildBibData(bibTree, $http, $scope);
            $scope.tree1 = bibTree;
        };

        $scope.copyToTree2 = function () {
            var bibTree = [];
            buildBibData(bibTree, $http, $scope);
            $scope.tree2 = bibTree;
        };

        $rootScope.updateMessage = function () {
            $scope.message = $rootScope.message;
        };

        $scope.cancel = function () {

        }

        $scope.previous = function (holdingsIdentifier) {
            //Item tree searchResult previous
            var itemData = [];
            if($scope.itemStart - $scope.itemPageSize >= 0){
                $scope.itemStart = $scope.itemStart - $scope.itemPageSize;
            }
            var url = '(DocType:item)AND(holdingsIdentifier:' + holdingsIdentifier + ')';
            $http.get($rootScope.baseUri + url + '&wt=json&fl=Location_display,id,DocType&start=' + $scope.itemStart + "&rows=" + $scope.itemPageSize).
                success(function (data) {
                    console.log(data.response.docs);
                    angular.forEach(data.response.docs, function (itemResponse) {
                        var locationDispaly = "Item - " + itemResponse.id;
                        if (itemResponse.hasOwnProperty('Location_display')) {
                            locationDispaly = itemResponse.Location_display[0]
                        }
                        var item =
                        {
                            'id': itemResponse.id,
                            'title': locationDispaly,
                            "docType": itemResponse.DocType
                        }
                        itemData.push(item);
                    }, "");
                    angular.forEach($scope.tree1, function (bib) {
                        angular.forEach(bib.nodes, function (holdingsNode) {
                            if (holdingsNode.id == holdingsIdentifier) {
                                holdingsNode.nodes = itemData;
                            }
                        }, "");
                    });
                });
        };

        $scope.next = function (holdingsIdentifier) {
            //Item tree searchResult next
            var itemData = [];
            var url = '(DocType:item)AND(holdingsIdentifier:' + holdingsIdentifier + ')';
            $http.get($rootScope.baseUri + url + '&wt=json&fl=Location_display,id,DocType&start=' + $scope.itemStart + "&rows=" + $scope.itemPageSize).
                success(function (data) {
                    var itemTotalRecords = data.response.numFound;
                    if($scope.itemStart + $scope.itemPageSize <= itemTotalRecords){
                        $scope.itemStart = $scope.itemStart + $scope.itemPageSize;
                        $http.get($rootScope.baseUri + url + '&wt=json&fl=Location_display,id,DocType&start=' + $scope.itemStart + "&rows=" + $scope.itemPageSize).
                            success(function (data) {
                                console.log(data.response.docs);
                                angular.forEach(data.response.docs, function (itemResponse) {
                                    var locationDisplay = "Item - " + itemResponse.id;
                                    if (itemResponse.hasOwnProperty('Location_display')) {
                                        locationDisplay = itemResponse.Location_display[0]
                                    }
                                    var item =
                                    {
                                        'id': itemResponse.id,
                                        'title': locationDisplay,
                                        "docType": itemResponse.DocType
                                    }
                                    itemData.push(item);
                                }, "");
                                angular.forEach($scope.tree1, function (bib) {
                                    angular.forEach(bib.nodes, function (holdingsNode) {
                                        if (holdingsNode.id == holdingsIdentifier) {
                                            holdingsNode.nodes = itemData;
                                        }
                                    }, "");
                                });
                            }
                        );
                    }
                }
            );
        };

    }]);

}());

function buildItemData(holdingsResponse, itemData, $http, $scope) {
    /*angular.forEach(holdingsResponse.itemIdentifier, function (itemId) {
     var url = "id" + ":" + itemId;
     $http.get($scope.baseUri + url + '&wt=json&fl=Location_display,id,DocType').
     success(function (data) {
     $scope.itemResults = data.response.docs;
     var itemResponse = $scope.itemResults[0];
     var locationDispaly = "Item";
     if (itemResponse.hasOwnProperty('Location_display')) {
     locationDispaly = itemResponse.Location_display[0]
     }
     var item =
     {
     'id': itemResponse.id,
     'title': locationDispaly,
     "docType": itemResponse.DocType
     }
     itemData.push(item);
     });
     }, "");*/

    var url = '(DocType:item)AND(holdingsIdentifier:' + holdingsResponse.holdingsIdentifier + ')';
    $http.get($scope.baseUri + url + '&wt=json&fl=Location_display,id,DocType&start=0&rows=' + $scope.itemPageSize).
        success(function (data) {
            //console.log(data.response.docs);
            angular.forEach(data.response.docs, function (itemResponse) {
                var locationDispaly = "Item - " + itemResponse.id;
                if (itemResponse.hasOwnProperty('Location_display')) {
                    locationDispaly = itemResponse.Location_display[0]
                }
                var item =
                {
                    'id': itemResponse.id,
                    'title': locationDispaly,
                    "docType": itemResponse.DocType
                }
                itemData.push(item);
            }, "");
        });
}

function buildHoldingsData(searchResult, holdingsData, $http, $scope) {
    angular.forEach(searchResult.holdingsIdentifier, function (holdingsId) {
        var itemData = [];
        var url = "id" + ":" + holdingsId;
        $http.get($scope.baseUri + url + '&wt=json&fl=Location_display,id,itemIdentifier,holdingsIdentifier,DocType').
            success(function (data) {
                $scope.holdingsResults = data.response.docs;
                var holdingsResponse = $scope.holdingsResults[0];
                buildItemData(holdingsResponse, itemData, $http, $scope);
                var locationDispaly = "Holdings";

                if (holdingsResponse.hasOwnProperty('Location_display')) {
                    locationDispaly = holdingsResponse.Location_display[0]
                }

                var holdings = {
                    'id': holdingsResponse.id,
                    'title': locationDispaly,
                    "docType": holdingsResponse.DocType,
                    'nodes': itemData
                }
                holdingsData.push(holdings);

            });
    }, "");
}

function buildBibData(bibTree, $http, $scope) {
    angular.forEach($scope.searchResults, function (searchResult) {
        if (searchResult.selected) {
            searchResult.selected = false;
            var holdingsData = [];
            buildHoldingsData(searchResult, holdingsData, $http, $scope);

            var bibdata = {
                'id': searchResult.id,
                "docType": searchResult.DocType,
                'title': searchResult.Title_display[0],
                'nodes': holdingsData
            }
            bibTree.push(bibdata);
        }
    }, "");
}



