/**
 * Created by rajeshbabuk on 3/29/16.
 */

function buildBibTreeData(bibTree, $http, $scope) {
    angular.forEach($scope.searchResults, function (searchResult) {
        if (searchResult.selected) {
            searchResult.selected = false;
            var holdingsData = [];
            buildHoldingsTreeData(searchResult, holdingsData, $http, $scope);
            var bibData = {
                'id': searchResult.id,
                "docType": searchResult.DocType,
                'title': searchResult.Title_display[0],
                'nodes': holdingsData
            };
            bibTree.push(bibData);
        }
    }, "");
}

function buildHoldingsTreeData(searchResult, holdingsData, $http, $scope) {
    angular.forEach(searchResult.holdingsIdentifier, function (holdingsId) {
        buildHoldingWithItems(holdingsId, holdingsData, $http, $scope);
    }, "");
}

function buildHoldingWithItems(holdingsId, holdingsData, $http, $scope) {
    var showPrevious = false;
    var showNext = false;
    var itemUrl = '(DocType:item)AND(holdingsIdentifier:' + holdingsId + ')';
    $http({
        method: 'JSONP',
        url: $scope.baseUri,
        params: {
            'json.wrf': 'JSON_CALLBACK',
            'q': itemUrl,
            'wt': 'json',
            'start': $scope.start,
            'rows': $scope.itemPageSize,
            'fl': 'id,DocType,Location_display,CallNumberPrefix_display,CallNumber_display,CopyNumber_display,ItemBarcode_display'
        }
    }).success(function (data) {
        if (data.response.numFound > $scope.itemPageSize) {
            showNext = true;
        }
        var itemData = buildItemDataFromResponse(data, holdingsId);
        buildHolding(holdingsId, holdingsData, itemData, showPrevious, showNext, $http, $scope);
    }).error(function () {
        console.log('Search failed while retrieving items!');
    });
}

function buildHolding(holdingsId, holdingsData, itemData, showPrevious, showNext, $http, $scope) {
    var holdingsUrl = "id" + ":" + holdingsId;
    $http({
        method: 'JSONP',
        url: $scope.baseUri,
        params: {
            'json.wrf': 'JSON_CALLBACK',
            'q': holdingsUrl,
            'wt': 'json',
            'fl': 'id,DocType,Location_display,CallNumberPrefix_display,CallNumber_display,CopyNumber_display,itemIdentifier,holdingsIdentifier,bibIdentifier'
        }
    }).success(function (data) {
        $scope.holdingsResults = data.response.docs;
        var holdingsResponse = $scope.holdingsResults[0];
        var displayLabel = buildLabel(holdingsResponse);
        if (displayLabel == null || displayLabel == '' || displayLabel == undefined || displayLabel == '-' || displayLabel == 'null') {
            displayLabel = "Holdings - " + holdingsResponse.id;
        }
        var holdings = {
            'id': holdingsResponse.id,
            'title': displayLabel,
            "docType": holdingsResponse.DocType,
            'bibId': holdingsResponse.bibIdentifier[0],
            'nodes': itemData,
            'showPrevious': showPrevious,
            'showNext': showNext
        };
        holdingsData.push(holdings);
    }).error(function () {
        console.log('Search failed while retrieving holdings!');
    });
}

function buildItemDataFromResponse(data, holdingsId) {
    var itemData = [];
    angular.forEach(data.response.docs, function (itemResponse) {
        var displayLabel = buildLabel(itemResponse);
        if (displayLabel == null || displayLabel == '' || displayLabel == undefined || displayLabel == '-' || displayLabel == 'null') {
            displayLabel = "Item - " + itemResponse.id;
        }
        var item = {
            'id': itemResponse.id,
            'title': displayLabel,
            "docType": itemResponse.DocType,
            'holdingsId': holdingsId
        };
        itemData.push(item);
    }, "");
    return itemData;
}

function previous(holdingsId, tree, leftOrRight, $http, $scope, $rootScope) {
    var startId = document.getElementById(leftOrRight + "Start_" + holdingsId).value;
    startId = parseInt(startId) - parseInt($scope.itemPageSize);
    document.getElementById(leftOrRight + "Start_" + holdingsId).value = startId;
    document.getElementById(leftOrRight + "Next_" + holdingsId).style.visibility = "visible";
    if (startId == 0) {
        document.getElementById(leftOrRight + "Previous_" + holdingsId).style.visibility = "hidden";
        document.getElementById(leftOrRight + "Next_" + holdingsId).style.visibility = "visible";
    }
    setPageItemsOnHoldings(startId, holdingsId, tree, $http, $scope, $rootScope, leftOrRight, "previous");
}

function next(holdingsId, tree, leftOrRight, $http, $scope, $rootScope) {
    var startId = document.getElementById(leftOrRight + "Start_" + holdingsId).value;
    startId = parseInt(startId) + parseInt($scope.itemPageSize);
    document.getElementById(leftOrRight + "Start_" + holdingsId).value = startId;
    document.getElementById(leftOrRight + "Previous_" + holdingsId).style.visibility = "visible";
    setPageItemsOnHoldings(startId, holdingsId, tree, $http, $scope, $rootScope, leftOrRight, "next");
}

function setPageItemsOnHoldings(startId, holdingsId, tree, $http, $scope, $rootScope, leftOrRight, type) {
    var itemUrl = '(DocType:item)AND(holdingsIdentifier:' + holdingsId + ')';
    $http({
        method: 'JSONP',
        url: $rootScope.baseUri,
        params: {
            'json.wrf': 'JSON_CALLBACK',
            'q': itemUrl,
            'wt': 'json',
            'start': startId,
            'rows': $scope.itemPageSize,
            'fl': 'id,DocType,Location_display,CallNumberPrefix_display,CallNumber_display,CopyNumber_display,ItemBarcode_display'
        }
    }).success(function (data) {
        var itemData = buildItemDataFromResponse(data, holdingsId);
        if (data.response.numFound <=  (parseInt(startId) + parseInt($scope.itemPageSize))) {
            document.getElementById(leftOrRight + "Next_" + holdingsId).style.visibility = "hidden";
            document.getElementById(leftOrRight + "Previous_" + holdingsId).style.visibility = "visible";
        }
        angular.forEach(tree, function (bib) {
            angular.forEach(bib.nodes, function (holdingsNode) {
                if (holdingsNode.id == holdingsId) {
                    if (type != null && type == "previous") {
                        holdingsNode.showNext = true;
                    } else if (type != null && type == "next") {
                        holdingsNode.showPrevious = true;
                    }
                    holdingsNode.nodes = itemData;
                }
            }, "");
        });
    }).error(function () {
        console.log('Search failed while retrieving page items!');
    });
}

function buildLabel(response) {
    var displayLabel = null;
    if (response.hasOwnProperty('Location_display') && !isFieldEmpty(response.Location_display[0])) {
        displayLabel = response.Location_display[0];
    }
    if (response.hasOwnProperty('CallNumberPrefix_display') && !isFieldEmpty(response.CallNumberPrefix_display[0])) {
        if (displayLabel == null || displayLabel == '' || displayLabel == undefined || displayLabel == '-') {
            displayLabel = response.CallNumberPrefix_display[0];
        } else {
            displayLabel = displayLabel + "-" + response.CallNumberPrefix_display[0];
        }
    }
    if (response.hasOwnProperty('CallNumber_display') && !isFieldEmpty(response.CallNumber_display[0])) {
        if (displayLabel == null || displayLabel == '' || displayLabel == undefined || displayLabel == '-') {
            displayLabel = response.CallNumber_display[0];
        } else {
            displayLabel = displayLabel + "-" + response.CallNumber_display[0];
        }
    }
    if (response.hasOwnProperty('CopyNumber_display') && !isFieldEmpty(response.CopyNumber_display)) {
        if (displayLabel == null || displayLabel == '' || displayLabel == undefined || displayLabel == '-') {
            displayLabel = response.CopyNumber_display;
        } else {
            displayLabel = displayLabel + "-" + response.CopyNumber_display;
        }
    }
    if (response.hasOwnProperty('ItemBarcode_display') && !isFieldEmpty(response.ItemBarcode_display)) {
        if (displayLabel == null || displayLabel == '' || displayLabel == undefined || displayLabel == '-') {
            displayLabel = response.ItemBarcode_display;
        } else {
            displayLabel = displayLabel + "-" + response.ItemBarcode_display;
        }
    }
    return displayLabel + "";
}

function isFieldEmpty(field) {
    if (field == undefined || field == null || field == '') {
        return true;
    }
    return false;
}

function rebuildTrees($scope, $http) {
    var tree1 = $scope.tree1;
    var tree2 = $scope.tree2;
    $scope.tree1 = null;
    $scope.tree2 = null;
    $scope.tree1 = buildBibTree(tree1, $http, $scope);
    $scope.tree2 = buildBibTree(tree2, $http, $scope);
}

function buildBibTree(tree, $http, $scope) {
    var bibTree = [];
    angular.forEach(tree, function (bib) {
        var bibUrl = '(id:' + bib.id + ')';
        var holdingsData = [];
        $http({
            method: 'JSONP',
            url: $scope.baseUri,
            params: {
                'json.wrf': 'JSON_CALLBACK',
                'q': bibUrl,
                'wt': 'json'
            }
        }).success(function (data) {
            var bibResults = data.response.docs[0];
            var holdingsIdentifiers = bibResults.holdingsIdentifier;
            rebuildHoldingsTreeData(holdingsIdentifiers, holdingsData, $http, $scope);
            var bibData = {
                'id': bibResults.id,
                "docType": bibResults.DocType,
                'title': bibResults.Title_display[0],
                'nodes': holdingsData
            };
            bibTree.push(bibData);
        }).error(function () {
            console.log('Search failed while retrieving bib!');
        });
    }, "");
    return bibTree;
}

function rebuildHoldingsTreeData(holdingsIdentifiers, holdingsData, $http, $scope) {
    angular.forEach(holdingsIdentifiers, function (holdingsId) {
        buildHoldingWithItems(holdingsId, holdingsData, $http, $scope);
    }, "");
}



