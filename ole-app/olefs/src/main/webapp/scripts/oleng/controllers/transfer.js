var app = angular.module("transferApp", ['ngAnimate', 'ngSanitize', 'mgcrea.ngStrap', 'ui.bootstrap', 'ui.tree', 'angularUtils.directives.dirPagination']);

app.controller('searchController', ['$scope', '$http', '$rootScope', searchConditions]);
function searchConditions($scope, $http, $rootScope) {
    //$scope.pageno = 1;
    $scope.total_count;
    $scope.itemsPerPage = 10;
    $rootScope.solrUri = '/bib/select?q='
    $rootScope.baseUri;
    $rootScope.searched = false;
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

    function getUri(){
        $http.get("rest/ngTransferController/url")
            .success(function (data) {
                console.log(data.docstoreUrl)
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
        $scope.conditions[0] = {value: '', searchScope: 'AND', inDocumentType: 'bibliographic', inField: 'all_text', operator: 'AND'};
        $scope.conditions.push({value: condition.value, searchScope: condition.searchScope, inDocumentType: condition.inDocumentType, inField: condition.inField, operator:condition.operator});
    };

    $scope.clear = function () {
        angular.forEach($scope.conditions, function (condition) {
            condition.value = '';
            condition.searchScope = 'AND';
            condition.inDocumentType = 'bibliographic';
            condition.inField = 'all_text'
            condition.operator = 'AND';
        }, "");
    };

    $scope.cancel = function () {
        angular.forEach($scope.conditions, function (condition) {
            var index = $scope.conditions.indexOf(condition);
            if(index!=0){
                $scope.conditions.splice(index);
            }
        }, "");
    };

    $scope.searchOnChange = function (showEntry) {
        $scope.start = 0;
        $scope.itemsPerPage = showEntry;
        $scope.total_count;
        $rootScope.searchResults;
        $scope.searchResults;
        searchSolrForResults();
    };

    $scope.search = function () {
        console.log("search");
        $scope.start = 0;
        $scope.itemsPerPage = 10;
        $scope.total_count;
        $rootScope.searchResults;
        $scope.searchResults;
        buildSearchConditionsWithJoin();
        searchSolrForResults();
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
                        queryCondition = queryCondition + query.concat(condition.searchScope);
                    }
                }
            }, "");
            if($scope.conditions.length >= 2){
                $scope.searchQuery = $scope.searchQuery + queryCondition + ')';
            }
        }
        console.log($scope.searchQuery);
    }


    function buildSearchConditionsWithJoin(){
        $scope.nonCrossDocTypeCondition = [];
        $scope.crossDocTypeCondition = [];

        angular.forEach($scope.conditions, function (condition) {
            if(condition.inDocumentType == $scope.documentType && condition.value!= ''){
                $scope.nonCrossDocTypeCondition.push(condition);
            }else if(condition.inDocumentType != $scope.documentType && condition.value!= '') {
                $scope.crossDocTypeCondition.push(condition);
            }
        }, "");

        if($scope.crossDocTypeCondition.length > 0){

            var joinQuery = '{!join from=id to=';

            angular.forEach($scope.crossDocTypeCondition, function (condition) {
                var searchText = condition.value;
                if(condition.searchScope == 'phrase'){
                    searchText = '\"' + condition.value + '\"'
                }

                if($scope.documentType != condition.inDocumentType && condition.inDocumentType == 'holdings'){

                    if(joinQuery.indexOf('holdingsIdentifier}(DocType:holdings)') ==-1){
                        joinQuery = joinQuery.concat('holdingsIdentifier}(DocType:holdings)AND(');
                        joinQuery = joinQuery.concat("(" + condition.inField +':(' + condition.value + '))');
                    }else{
                        joinQuery = joinQuery.concat(condition.operator + "(" + condition.inField +':(' + condition.value + '))');
                    }

                    var index = $scope.crossDocTypeCondition.indexOf(condition);
                    if(index == $scope.crossDocTypeCondition.length-1){
                        joinQuery = joinQuery.concat(')&fq=(DocType:' + $scope.documentType + ')' );
                        if($scope.nonCrossDocTypeCondition.length>0){
                            joinQuery = joinQuery.concat('AND(');
                        }
                    }

                }else if($scope.documentType != condition.inDocumentType && condition.inDocumentType == 'item'){

                    if(joinQuery.indexOf('itemIdentifier}(DocType:item)') ==-1){
                        joinQuery = joinQuery.concat('itemIdentifier}(DocType:item)AND(');
                        joinQuery = joinQuery.concat("(" + condition.inField +':(' + condition.value + '))');
                    }else{
                        joinQuery = joinQuery.concat(condition.operator + "(" + condition.inField +':(' + condition.value + '))');
                    }

                    var index = $scope.crossDocTypeCondition.indexOf(condition);
                    if(index == $scope.crossDocTypeCondition.length-1){
                        joinQuery = joinQuery.concat(')&fq=(DocType:' + $scope.documentType + ')' );
                        if($scope.nonCrossDocTypeCondition.length>0){
                            joinQuery = joinQuery.concat('AND(');
                        }
                    }

                }else if($scope.documentType != condition.inDocumentType && condition.inDocumentType == 'bibliographic'){

                    if(joinQuery.indexOf('bibIdentifier}(DocType:bibliographic)') ==-1){
                        joinQuery = joinQuery.concat('bibIdentifier}(DocType:bibliographic)AND(');
                        joinQuery = joinQuery.concat("(" + condition.inField +':(' + condition.value + '))');
                    }else{
                        joinQuery = joinQuery.concat(condition.operator + "(" + condition.inField +':(' + condition.value + '))');
                    }

                    var index = $scope.crossDocTypeCondition.indexOf(condition);
                    if(index == $scope.crossDocTypeCondition.length-1){
                        joinQuery = joinQuery.concat(')&fq=(DocType:' + $scope.documentType + ')' );
                        if($scope.nonCrossDocTypeCondition.length>0){
                            joinQuery = joinQuery.concat('AND(');
                        }
                    }

                }

            }, "");

            angular.forEach($scope.nonCrossDocTypeCondition, function (condition) {
                var index = $scope.nonCrossDocTypeCondition.indexOf(condition);
                if(index ==0){
                    joinQuery = joinQuery.concat("(" + condition.inField +':(' + condition.value + '))');
                }else{
                    joinQuery = joinQuery.concat( condition.operator + "(" + condition.inField +':(' + condition.value + '))');
                }
                if(index == $scope.nonCrossDocTypeCondition.length-1){
                    joinQuery = joinQuery.concat( ')');
                }
            }, "");

            $scope.searchQuery = joinQuery;
            console.log($scope.searchQuery);

        } else if($scope.nonCrossDocTypeCondition.length >= 0 && $scope.crossDocTypeCondition.length == 0){
            buildSearchConditions();
        }


    }

    /*function searchSolr(){
        //var url = " DocType:" + condition.inDocumentType;
        var url = $scope.searchQuery;
        $http.get($rootScope.baseUri + url + '&wt=json&fl=Title_display,Author_display,Publisher_display,id,holdingsIdentifier,DocType&rows=10&sort=Title_sort asc').
            success(function (data) {
                $scope.totalRecords = data.response.numFound;
                vm.total_count = data.response.numFound;
                $http.get($rootScope.baseUri + url + '&wt=json&fl=Title_display,Author_display,Publisher_display,id,holdingsIdentifier,DocType&sort=Title_sort asc&rows=' + $scope.totalRecords).
                    success(function (data) {
                        //console.log(data.response.docs);
                        $rootScope.searchResults = data.response.docs;
                        vm.searchResults = data.response.docs;
                        $rootScope.searched = false;
                        paginate();
                    });

            });

    }*/

    function searchSolrForResults(){
        //var url = " DocType:" + condition.inDocumentType;
        var url = $scope.searchQuery;
        console.log(url);
        //$timeout(function(){
                /*$http.get($rootScope.baseUri + url + '&wt=json&fl=Title_display,Author_display,Publisher_display,id,holdingsIdentifier,DocType&rows=10&sort=Title_sort asc').
                    success(function (data) {
                        $scope.totalRecords = data.response.numFound;
                        $scope.total_count = data.response.numFound;
                        console.log($scope.total_count);*/
                        console.log($rootScope.baseUri + url + '&wt=json&sort=Title_sort asc&rows=' + $scope.itemsPerPage + '&start=' + $scope.start);
                        $http.get($rootScope.baseUri + url + '&wt=json&sort=Title_sort asc&rows=' + $scope.itemsPerPage + '&start=' + $scope.start).
                            success(function (data) {
                                //console.log(data.response.docs);
                                //$scope.totalRecords = data.response.numFound;
                                console.log(data.response);
                                $scope.total_count = data.response.numFound;
                                $rootScope.searchResults = data.response.docs;
                                $scope.searchResults = data.response.docs;
                                $rootScope.searched = true;
                                $scope.showEntryInfo = 'Showing ' + parseInt($scope.start + 1) + ' to ' + $scope.itemsPerPage + ' of ' + $scope.total_count +' entries'
                                //paginate();

                            });
                    //});
        //},5000,false);
    }

    $scope.nextSearchSolr = function(pageNo){
        $rootScope.searchResults;
        $scope.searchResults;
        var url = $scope.searchQuery;
        $scope.start = (pageNo * $scope.itemsPerPage) - $scope.itemsPerPage;
        $http.get($rootScope.baseUri + url + '&wt=json&sort=Title_sort asc&rows=' + $scope.itemsPerPage + '&start=' + $scope.start).
            success(function (data) {
                $rootScope.searchResults = data.response.docs;
                $scope.searchResults = data.response.docs;
                $rootScope.searched = true;
                var displayedItems;
                if(parseInt($scope.start + $scope.itemsPerPage) < $scope.total_count){
                    displayedItems = parseInt($scope.start + $scope.itemsPerPage);
                }else{
                    displayedItems = $scope.total_count;
                }
                $scope.showEntryInfo = 'Showing ' + parseInt($scope.start + 1) + ' to ' + displayedItems + ' of ' + $scope.total_count +' entries'
            });
    }

    /*$scope.previous = function () {
        //searchResult previous
        angular.forEach($scope.conditions, function (condition) {
            $scope.start = $scope.start - $scope.pageSize;
            var url = " DocType:" + condition.inDocumentType;
            $http.get($rootScope.baseUri + url + '&wt=json&fl=Title_display,Author_display,Publisher_display,id,holdingsIdentifier,DocType&sort=Title_sort asc&start=' + $scope.start).
                success(function (data) {
                    $rootScope.searchResults = data.response.docs;
                    $rootScope.searched = false;
                    paginate();
                });
        }, "");
    };

    $scope.next = function () {
        //searchResult next
        $scope.start = $scope.start + $scope.pageSize;
        angular.forEach($scope.conditions, function (condition) {
            var url = " DocType:" + condition.inDocumentType;
            $http.get($rootScope.baseUri + url + '&wt=json&fl=Title_display,Author_display,Publisher_display,id,holdingsIdentifier,DocType&sort=Title_sort asc&start=' + $scope.start).
                success(function (data) {
                    $rootScope.searchResults = data.response.docs;
                    $rootScope.searched = false;
                    paginate();
                });
        }, "");
    };

    function paginate(){
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
    }*/

};
