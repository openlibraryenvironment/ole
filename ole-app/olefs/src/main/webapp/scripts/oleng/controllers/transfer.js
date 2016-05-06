var app = angular.module("transferApp", ['ngAnimate', 'ngSanitize', 'mgcrea.ngStrap', 'ui.bootstrap', 'ui.tree', 'angularUtils.directives.dirPagination']);

app.controller('searchController', ['$scope', '$http', '$rootScope', searchConditions]);
function searchConditions($scope, $http, $rootScope) {
    //$scope.pageno = 1;
    $scope.total_count;
    $scope.itemsPerPage = 10;
    $rootScope.solrUri = '/bib/select/';
    $rootScope.baseUri;
    $rootScope.searched = false;
    $rootScope.showResults = true;
    $scope.showEntryInfo;
    //$scope.rows = 10;
    $scope.start = 0;
    //$scope.pageSize = 10;
    //$scope.showNext = false;
    //$scope.showPrevious = false;
    //$scope.totalRecords = 0;
    $scope.searchConditionActivePanel = [0];
    $scope.searchConditionPanel = searchCondition;
    var searchCondition = {
        collapsed: false
    };
    $scope.searchResultActivePanel = [0];
    $scope.searchResultPanel = searchResult;
    var searchResult = {
        collapsed: false
    };

    $scope.init = function () {
        getUri();
    };

    function getUri() {
        doGetRequest($scope, $http, "rest/ngTransferController/url", null, function (response) {
            var data = response.data;
            console.log(data.docstoreUrl);
            $rootScope.baseUri = data.docstoreUrl + $rootScope.solrUri;
        });
    }

    $scope.showEntry = '10';
    $scope.showEntires = [
        {id: '10', name: '10'},
        {id: '25', name: '25'},
        {id: '50', name: '50'},
        {id: '75', name: '75'},
        {id: '100', name: '100'}
    ];

    $scope.documentType = 'bibliographic';
    $scope.documentTypes = [
        {id: 'bibliographic', name: 'Bibliographic'},
        {id: 'eHoldings', name: 'EHoldings'},
        {id: 'holdings', name: 'Holdings'},
        {id: 'item', name: 'Item'},
        {id: 'license', name: 'License'}
    ];

    $scope.operators = [
        {id: 'AND', name: 'AND'},
        {id: 'OR', name: 'OR'},
        {id: 'NOT', name: 'NOT'}
    ];

    $scope.searchTypes = [
        {id: 'search', name: 'Search'},
        {id: 'browse', name: 'Browse'}
    ];

    $scope.searchScopes = [
        {id: 'AND', name: 'All of these'},
        {id: 'OR', name: 'Any of these'},
        {id: 'phrase', name: 'As a phrase'}
    ];

    $scope.inDoctypes = [
        {id: 'bibliographic', name: 'Bibliographic'},
        {id: 'eHoldings', name: 'EHoldings'},
        {id: 'holdings', name: 'Holdings'},
        {id: 'item', name: 'Item'}
    ];

    $scope.inFields = [
        {id: 'all_text', name: 'ANY'},
        {id: 'Author_search', name: 'Author'},
        {id: 'Carrier_search', name: 'Carrier'},
        {id: 'PublicationDate_search', name: 'Date of Publication'},
        {id: 'Format_search', name: 'Format'},
        {id: 'ISBN_search', name: 'ISBN'},
        {id: 'ISSN_search', name: 'ISSN'},
        {id: 'ItemBarcode_search', name: 'Item Barcode'},
        {id: 'JournalTitle_search', name: 'Journal Title'},
        {id: 'Language_search', name: 'Language'},
        {id: 'LocalId_search', name: 'Local Identifier'},
        {id: 'mdf_035a', name: 'OCLC No'},
        {id: 'Publisher_search', name: 'Publisher'},
        {id: 'ResourceType_search', name: 'Resource Type'},
        {id: 'Subject_search', name: 'Subject'},
        {id: 'Title_search', name: 'Title'},
        {id: 'Uri_search', name: 'Uri'}
    ];

    $scope.inFields_Holdings = [
        {id: 'all_text', name: 'ANY'},
        {id: 'CallNumber_search', name: 'Call Number'},
        {id: 'CallNumberPrefix_search', name: 'Call Number Prefix'},
        {id: 'ShelvingSchemeCode_search', name: 'Call Number Type Code'},
        {id: 'HoldingsNote_search', name: 'Holdings Note'},
        {id: 'Level1Location_search', name: 'Level1Location'},
        {id: 'Level2Location_search', name: 'Level2Location'},
        {id: 'Level3Location_search', name: 'Level3Location'},
        {id: 'Level4Location_search', name: 'Level4Location'},
        {id: 'Level5Location_search', name: 'Level5Location'},
        {id: 'LocalId_search', name: 'Local Identifier'},
        {id: 'Location_search', name: 'Location'},
        {id: 'ReceiptStatus_search', name: 'Receipt Status'},
        {id: 'ShelvingOrder_search', name: 'Shelving Order'},
        {id: 'Uri_search', name: 'Uri'}
    ];

    $scope.inFields_EHoldings = [
        {id: 'all_text', name: 'ANY'},
        {id: 'AccessLocation_search', name: 'Access Location'},
        {id: 'AccessStatus_search', name: 'Call Number Prefix'},
        {id: 'Authentication_search', name: 'Authentication'},
        {id: 'CallNumber_search', name: 'Call Number'},
        {id: 'CallNumberPrefix_search', name: 'Call Number Prefix'},
        {id: 'ShelvingSchemeCode_search', name: 'Call Number Type Code'},
        {id: 'ShelvingSchemeValue_search', name: 'Call Number Type Name'},
        {id: 'Imprint_search', name: 'Imprint_search'},
        {id: 'Level1Location_search', name: 'Level1Location'},
        {id: 'Level2Location_search', name: 'Level2Location'},
        {id: 'Level3Location_search', name: 'Level3Location'},
        {id: 'Level4Location_search', name: 'Level4Location'},
        {id: 'Level5Location_search', name: 'Level5Location'},
        {id: 'LocalId_search', name: 'Local Identifier'},
        {id: 'NumberOfSimultaneousUses_search', name: 'No. Of Simultaneous Uses'},
        {id: 'Platform_search', name: 'Platform'},
        {id: 'Proxied_search', name: 'Proxied'},
        {id: 'ReceiptStatus_search', name: 'Receipt Status'},
        {id: 'ShelvingOrder_search', name: 'Shelving Order'},
        {id: 'StatisticalSearchingCodeValue_search', name: 'Statistical Code'},
        {id: 'Subscription_search', name: 'Subscription'},
        {id: 'URL_search', name: 'URL'}
    ];

    $scope.inFields_Item = [
        {id: 'all_text', name: 'ANY'},
        {id: 'Author_search', name: 'Author'},
        {id: 'BarcodeARSL_search', name: 'Barcode ARSL'},
        {id: 'bibIdentifier', name: 'Bib Local Identifier'},
        {id: 'CallNumber_search', name: 'Call Number'},
        {id: 'CallNumberPrefix_search', name: 'Call Number Prefix'},
        {id: 'ShelvingSchemeCode_search', name: 'Call Number Type Code'},
        {id: 'ShelvingSchemeValue_search', name: 'Call Number Type Name'},
        {id: 'Chronology_search', name: 'Chronology'},
        {id: 'ClaimsReturnedFlag_search', name: 'Claims Return Flag'},
        {id: 'CopyNumber_search', name: 'Copy Number'},
        {id: 'PublicationDate_search', name: 'Date of Publication'},
        {id: 'DonorCode_search', name: 'Donor Code'},
        {id: 'dueDateTime', name: 'Due date Time'},
        {id: 'Enumeration_search', name: 'Enumeration'},
        {id: 'FastAdd_search', name: 'Fast Add'},
        {id: 'Format_search', name: 'Format'},
        {id: 'ISBN_search', name: 'ISBN'},
        {id: 'ISSN_search', name: 'ISSN'},
        {id: 'ItemBarcode_search', name: 'ItemBarcode'},
        {id: 'ItemDamagedStatus_search', name: 'Item Damaged Status'},
        {id: 'ItemStatus_search', name: 'Item Status'},
        {id: 'ItemTypeCodeValue_search', name: 'Item Type Code'},
        {id: 'ItemTypeFullValue_search', name: 'Item Type Name'},
        {id: 'ItemUri_search', name: 'Item Uri'},
        {id: 'Language_search', name: 'Language'},
        {id: 'Level1Location_search', name: 'Level1Location'},
        {id: 'Level2Location_search', name: 'Level2Location'},
        {id: 'Level3Location_search', name: 'Level3Location'},
        {id: 'Level4Location_search', name: 'Level4Location'},
        {id: 'Level5Location_search', name: 'Level5Location'},
        {id: 'LocalId_search', name: 'Local Identifier'},
        {id: 'Location_search', name: 'Location'},
        {id: 'MissingPieceFlag_search', name: 'MissingPieceFlag'},
        {id: 'Publisher_search', name: 'Publisher'},
        {id: 'PurchaseOrderLineItemIdentifier_search', name: 'Purchase Order Line Item Identifier'},
        {id: 'ShelvingOrder_search', name: 'Shelving Order'},
        {id: 'StatisticalSearchingCodeValue_search', name: 'Statistical Searching Code Value'},
        {id: 'StatisticalSearchingFullValue_search', name: 'Statistical Searching Name'},
        {id: 'TempItemType_search', name: 'Temp Item Type'},
        {id: 'Title_search', name: 'Title'},
        {id: 'VendorLineItemIdentifier_search', name: 'Vendor Line Item Identifier'}
    ];

    $scope.conditions = [
        {value: '', searchScope: 'AND', inDocumentType: 'bibliographic', inField: 'all_text', operator:'AND'}
    ];

    $scope.removeCondition = function (conditionToRemove) {
        var index = $scope.conditions.indexOf(conditionToRemove);
        if($scope.conditions.length > 1){
            $scope.conditions.splice(index, 1);
        }
    };

    $scope.addCondition = function (condition) {
        $scope.conditions.push({value: condition.value, searchScope: condition.searchScope, inDocumentType: condition.inDocumentType, inField: condition.inField, operator:condition.operator});
        var i= $scope.conditions.length ;
        $scope.conditions[i-1] = {value: '', searchScope: 'AND', inDocumentType: 'bibliographic', inField: 'all_text', operator: 'AND'};
    };

    $scope.clear = function () {
        $rootScope.showResults = false;
        $rootScope.errorMessage = null;
        document.getElementById("showEntry").value = 10;
        $rootScope.newPageNumber = 1;
        $rootScope.searchResults = [];
        $scope.searchResults = [];
        angular.forEach($scope.conditions, function (condition) {
            condition.value = '';
            condition.searchScope = 'AND';
            condition.inDocumentType = 'bibliographic';
            condition.inField = 'all_text';
            condition.operator = 'AND';
        }, "");
    };

    $scope.cancel = function () {
        window.location = "portal.jsp";
    };

    $scope.searchOnChange = function (showEntry) {
        $scope.start = 0;
        $scope.itemsPerPage = showEntry;
        $scope.total_count;
        $rootScope.searchResults;
        $scope.searchResults;
        //searchSolrForResults();
    };

    $scope.search = function () {
        $rootScope.showResults = false;
        $rootScope.errorMessage = null;
        $scope.start = 0;
        $scope.itemsPerPage = document.getElementById("showEntry").value;
        $scope.total_count;
        $rootScope.searchResults;
        $scope.searchResults;
        buildSearchConditionsWithJoin();
        searchSolrForResults(1);
        buildResults();
    };

    function buildResults(){
        console.log($scope.documentType);
        $http.get("rest/ngTransferController/buildResults", {params:{'sourceDocType': $scope.documentType}}).success(function (data) {
            console.log(JSON.stringify(data));
            $scope.displayFields = data;
            //var display = JSON.parse(data);
            //$scope.displayFields = display;
        });
    }

    function buildSearchConditions(){
        $scope.searchQuery = "(DocType:" + $scope.documentType + ') AND (';
        var queryCondition = new String();
        if($scope.conditions.length == 1 && $scope.conditions[0].value==''){
            $scope.searchQuery = $scope.searchQuery+ '*:*)'
        }else if($scope.conditions.length == 1 && $scope.conditions[0].value != ''){
            var query = new String();
            $scope.searchQuery = $scope.searchQuery + query.concat("("+ $scope.conditions[0].inField, ":", $scope.conditions[0].value, "))" );
        }else{
            angular.forEach($scope.conditions, function (condition) {
                var index = $scope.conditions.indexOf(condition);
                if((condition.value != undefined && condition.value != null && condition.value !='')){
                    var query = new String();
                    queryCondition = queryCondition + query.concat("("+ condition.inField, ":", condition.value, ")" );
                    if(index!= $scope.conditions.length-1){
                        queryCondition = queryCondition + query.concat(condition.operator);
                    }
                }
            }, "");
            if($scope.conditions.length >= 2){
                $scope.searchQuery = $scope.searchQuery + queryCondition + ')';
            }
        }
    }


    function buildSearchConditionsWithJoin() {
        $scope.nonCrossDocTypeCondition = [];
        $scope.crossDocTypeCondition = [];

        angular.forEach($scope.conditions, function (condition) {
            if (condition.inDocumentType == $scope.documentType && condition.value != '') {
                $scope.nonCrossDocTypeCondition.push(condition);
            } else if (condition.inDocumentType != $scope.documentType && condition.value != '') {
                $scope.crossDocTypeCondition.push(condition);
            }
        }, "");

        if ($scope.crossDocTypeCondition.length > 0) {
            var joinQuery = '{!join from=id to=';
            angular.forEach($scope.crossDocTypeCondition, function (condition) {
                var searchText = condition.value;
                if (condition.searchScope == 'phrase') {
                    searchText = '\"' + condition.value + '\"'
                }
                if ($scope.documentType != condition.inDocumentType && condition.inDocumentType == 'holdings' && joinQuery.indexOf('holdingsIdentifier}(DocType:holdings)') == -1) {
                    joinQuery = joinQuery.concat('holdingsIdentifier}(DocType:holdings)AND(');
                    joinQuery = joinQuery.concat(condition.inField + ':(' + condition.value + ')');
                } else if ($scope.documentType != condition.inDocumentType && condition.inDocumentType == 'item' && joinQuery.indexOf('itemIdentifier}(DocType:item)') == -1) {
                    joinQuery = joinQuery.concat('itemIdentifier}(DocType:item)AND(');
                    joinQuery = joinQuery.concat(condition.inField + ':(' + condition.value + ')');
                } else if ($scope.documentType != condition.inDocumentType && condition.inDocumentType == 'bibliographic' && joinQuery.indexOf('bibIdentifier}(DocType:bibliographic)') == -1) {
                    joinQuery = joinQuery.concat('bibIdentifier}(DocType:bibliographic)AND(');
                    joinQuery = joinQuery.concat(condition.inField + ':(' + condition.value + ')');
                } else {
                    joinQuery = joinQuery.concat(condition.operator + "(" + condition.inField + ':(' + condition.value + '))');
                }
                joinQuery = buildcrossDocTypeCondition($scope, condition, joinQuery);
            }, "");

            angular.forEach($scope.nonCrossDocTypeCondition, function (condition) {
                var index = $scope.nonCrossDocTypeCondition.indexOf(condition);
                if (index == 0) {
                    joinQuery = joinQuery.concat("(" + condition.inField + ':(' + condition.value + '))');
                } else {
                    joinQuery = joinQuery.concat(condition.operator + "(" + condition.inField + ':(' + condition.value + '))');
                }
                if (index == $scope.nonCrossDocTypeCondition.length - 1) {
                    joinQuery = joinQuery.concat(')');
                }
            }, "");
            $scope.searchQuery = joinQuery;

        } else if ($scope.nonCrossDocTypeCondition.length >= 0 && $scope.crossDocTypeCondition.length == 0) {
            buildSearchConditions();
        }
    }

    function searchSolrForResults(pageNo) {
        searchAndBuildPageEntries(pageNo);
    }

    $scope.nextSearchSolr = function (pageNo) {
        searchAndBuildPageEntries(pageNo);
    };

    function searchAndBuildPageEntries(pageNo) {
        if (pageNo == null) {
            pageNo = 1;
        }
        var searchUrl = $scope.searchQuery;
        $scope.start = (pageNo * $scope.itemsPerPage) - $scope.itemsPerPage;
         var  query = searchUrl.split("&fq=")
         var  q = query[0];
         var  fq = ""
        if(query.length > 1){
            fq = query[1];
        }
        console.log('q=',q);
        console.log('fq=',fq);
        $http({
            method: 'JSONP',
            url: $rootScope.baseUri,
            params: {
                'json.wrf': 'JSON_CALLBACK',
                'q': q,
                'wt': 'json',
                'start': $scope.start,
                'rows': $scope.itemsPerPage,
                'sort': 'Title_sort asc',
                'fq':fq
            }
        }).success(function (data) {
            $rootScope.searchResults = data.response.docs;
            $scope.searchResults = data.response.docs;
            if ($scope.searchResults.length > 0) {
                $rootScope.showResults = true;
                $rootScope.errorMessage = null;
            } else {
                $rootScope.showResults = false;
                $rootScope.errorMessage = "No search results found";
            }
            $scope.total_count = data.response.numFound;
            $rootScope.searched = true;
            var noOfItemsOnPage = (pageNo * $scope.itemsPerPage);
            if (noOfItemsOnPage > $scope.total_count) {
                noOfItemsOnPage = $scope.total_count;
            }
            buildPageEntriesInfo($scope.start + 1, noOfItemsOnPage, $scope.total_count);
        }).error(function () {
            $rootScope.errorMessage = "Search failed";
            console.log('Search failed while retrieving bibs!');
        });
    }

    function buildPageEntriesInfo(start, itemsPerPage, totalCount) {
        $scope.showEntryInfo = 'Showing ' + start + ' to ' + itemsPerPage + ' of ' + totalCount + ' entries';
    }
    function buildcrossDocTypeCondition($scope, condition, joinQuery) {
        var index = $scope.crossDocTypeCondition.indexOf(condition);
        if (index == $scope.crossDocTypeCondition.length - 1) {
            joinQuery = joinQuery.concat(')&fq=(DocType:' + $scope.documentType + ')');
            if ($scope.nonCrossDocTypeCondition.length > 0) {
                joinQuery = joinQuery.concat('AND(');
            }
        }
        return joinQuery;
    }

}
