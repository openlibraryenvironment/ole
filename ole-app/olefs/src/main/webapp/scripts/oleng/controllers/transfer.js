var app = angular.module("transferApp", ['ui.tree']);


app.controller('searchController', ['$scope', '$http', '$rootScope', searchConditions]);
function searchConditions($scope, $http, $rootScope) {
    $rootScope.baseUri = '/oledocstore/bib/select?q='
    $rootScope.searched = true;
    $scope.rows = 10;
    $scope.start = 0;
    $scope.pageSize = 10;
    $scope.showNext = false;
    $scope.showPrevious = false;
    $scope.totalRecords = 0;
    $scope.documentTypes = [
        {id: 'bibliographic', name: 'Bibliographic'},
        {id: 'eHoldings', name: 'EHoldings'},
        {id: 'holdings', name: 'Holdings'},
        {id: 'item', name: 'Item'},
        {id: 'license', name: 'License'}
    ];

    $scope.searchTypes = [
        {id: 'search', name: 'Search'},
        {id: 'browse', name: 'Browse'}
    ];

    $scope.searchScopes = [
        {id: 'AND', name: 'All of these'},
        {id: 'OR', name: 'Any of these'},
        {id: 'phrase', name: 'Any of these'}
    ];

    $scope.inDoctypes = [
        {id: 'bibliographic', name: 'Bibliographic'},
        {id: 'eHoldings', name: 'EHoldings'},
        {id: 'holdings', name: 'Holdings'},
        {id: 'item', name: 'Item'}
    ];

    $scope.inFields = [
        {id: 'any', name: 'ANY'},
        {id: 'LocalId_search', name: 'Local Identifier'},
        {id: 'Title_search', name: 'Title'}
    ];

    $scope.conditions = [
        {value: '', searchScope: '', inDocumentType: 'bibliographic', inField: '', documentType: 'bibliographic'}
    ];

    $scope.removeCondition = function (ConditionToRemove) {
        var index = $scope.conditions.indexOf(ConditionToRemove);
        $scope.conditions.splice(index, 1);
    };

    $scope.addCondition = function () {
        $scope.conditions.push({type: '', value: ''});
    };

    $scope.search = function () {
        angular.forEach($scope.conditions, function (condition) {
            var url = " DocType:" + condition.inDocumentType;
            /*$.ajax({
                //url: "http://162.243.230.176:8080/easydoc/persons/person/",
                url: $rootScope.baseUri + url + '&wt=json&fl=Title_display,Author_display,Publisher_display,id,holdingsIdentifier,DocType&rows=10',
                type: "POST",
                crossDomain: true,
                contentType: "application/json"
            }).done(function(data) {
                console.log(data.response.docs);
                $rootScope.searchResults = data.response.docs;
            });*/


            /*$http({
                url: $rootScope.baseUri + url + '&wt=json&fl=Title_display,Author_display,Publisher_display,id,holdingsIdentifier,DocType&rows=10',
                method: "GET",
                withCredentials: true,
                headers: {
                    'Content-Type': 'application/json',
                    'Accept': 'application/json'
                }
            }).success(function (data, status, headers, config) {
                  alert("test")
                });*/

            $http.get($rootScope.baseUri + url + '&wt=json&fl=Title_display,Author_display,Publisher_display,id,holdingsIdentifier,DocType&rows=10')
                .success(function (data) {
                    console.log(data.response.docs);
                    $rootScope.searchResults = data.response.docs;
                    $rootScope.searched = false;
                    $scope.totalRecords = data.response.numFound;
                    if( $scope.start > 0){
                        $scope.showPrevious = true;
                    }else{
                        $scope.showPrevious = false;
                    }
                    if($scope.start + $scope.pageSize <= $scope.totalRecords){
                        $scope.showNext = true;
                    }else{
                        $scope.showNext = false;
                    }
                });
        }, "");
    };

    $scope.previous = function () {
        //searchResult previous
        angular.forEach($scope.conditions, function (condition) {
            $scope.start = $scope.start - $scope.pageSize;
            var url = " DocType:" + condition.inDocumentType;
            $http.get($rootScope.baseUri + url + '&wt=json&fl=Title_display,Author_display,Publisher_display,id,holdingsIdentifier,DocType&start=' + $scope.start).
                success(function (data) {
                    console.log(data.response.docs);
                    $rootScope.searchResults = data.response.docs;
                    $rootScope.searched = false;
                    if( $scope.start > 0){
                        $scope.showPrevious = true;
                    }else{
                        $scope.showPrevious = false;
                    }
                    if($scope.start + $scope.pageSize <= $scope.totalRecords){
                        $scope.showNext = true;
                    }else{
                        $scope.showNext = false;
                    }
                });
        }, "");
    };

    $scope.next = function () {
        //searchResult next
        $scope.start = $scope.start + $scope.pageSize;
        angular.forEach($scope.conditions, function (condition) {
            var url = " DocType:" + condition.inDocumentType;
            $http.get($rootScope.baseUri + url + '&wt=json&fl=Title_display,Author_display,Publisher_display,id,holdingsIdentifier,DocType&start=' + $scope.start).
                success(function (data) {
                    console.log(data.response.docs);
                    $rootScope.searchResults = data.response.docs;
                    $rootScope.searched = false;
                    if( $scope.start > 0){
                        $scope.showPrevious = true;
                    }else{
                        $scope.showPrevious = false;
                    }
                    if($scope.start + $scope.pageSize <= $scope.totalRecords){
                        $scope.showNext = true;
                    }else{
                        $scope.showNext = false;
                    }
                });
        }, "");
    };

};
