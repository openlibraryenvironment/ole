getMaintenanceDataForFieldTypeForDropDown = function (dataObject, index, fieldType, $scope, $http) {
    if ((fieldType == 'Call Number Type' || fieldType == 'Holdings Call Number Type')) {
        if ($scope.callNumberTypeValues == undefined) {
            doGetRequest($scope, $http, OLENG_CONSTANTS.PROFILE_GET_CALLNUMBER_TYPES, null, function (response) {
                var data = response.data;
                $scope.callNumberTypeValues = data;
                $scope.constantValues = data;
                populateOptions(dataObject, index, $scope.constantValues);
            });
        } else {
            $scope.constantValues = $scope.callNumberTypeValues;
            populateOptions(dataObject, index, $scope.constantValues);
        }
    } else if (fieldType == 'Item Type') {
        if ($scope.itemTypeValues == undefined) {
            doGetRequest($scope, $http, OLENG_CONSTANTS.PROFILE_GET_ITEM_TYPES, null, function (response) {
                var data = response.data;
                $scope.itemTypeValues = data;
                $scope.constantValues = data;
                populateOptions(dataObject, index, $scope.constantValues);
            });
        } else {
            $scope.constantValues = $scope.itemTypeValues;
            populateOptions(dataObject, index, $scope.constantValues);
        }
    } else if (fieldType == 'Item Status') {
        if ($scope.itemStatusValues == undefined) {
            doGetRequest($scope, $http, OLENG_CONSTANTS.PROFILE_GET_ITEM_STATUS, null, function (response) {
                var data = response.data;
                $scope.itemStatusValues = data;
                $scope.constantValues = data;
                populateOptions(dataObject, index, $scope.constantValues);
            });
        } else {
            $scope.constantValues = $scope.itemStatusValues;
            populateOptions(dataObject, index, $scope.constantValues);
        }
    } else if (fieldType == 'Donor Code') {
        if ($scope.donorCodes == undefined) {
            doGetRequest($scope, $http, OLENG_CONSTANTS.PROFILE_GET_DONOR_CODES, null, function (response) {
                var data = response.data;
                $scope.donorCodes = data;
                $scope.constantValues = data;
                populateOptions(dataObject, index, $scope.constantValues);
            });
        } else {
            $scope.constantValues = $scope.donorCodes;
            populateOptions(dataObject, index, $scope.constantValues);
        }
    } else if ((fieldType == 'Location Level1' || fieldType == 'Holdings Location Level1')) {
        if ($scope.locationLevel1Values == undefined) {
            doGetRequest($scope, $http, OLENG_CONSTANTS.PROFILE_GET_LOCATIONS, {"levelId": 1}, function (response) {
                var data = response.data;
                $scope.locationLevel1Values = data;
                $scope.constantValues = data;
                populateOptions(dataObject, index, $scope.constantValues);
            });
        } else {
            $scope.constantValues = $scope.locationLevel1Values;
            populateOptions(dataObject, index, $scope.constantValues);
        }
    } else if ((fieldType == 'Location Level2' || fieldType == 'Holdings Location Level2')) {
        if ($scope.locationLevel2Values == undefined) {
            doGetRequest($scope, $http, OLENG_CONSTANTS.PROFILE_GET_LOCATIONS, {"levelId": 2}, function (response) {
                var data = response.data;
                $scope.locationLevel2Values = data;
                $scope.constantValues = data;
                populateOptions(dataObject, index, $scope.constantValues);
            });
        } else {
            $scope.constantValues = $scope.locationLevel2Values;
            populateOptions(dataObject, index, $scope.constantValues);
        }
    } else if ((fieldType == 'Location Level3' || fieldType == 'Holdings Location Level3')) {
        if ($scope.locationLevel3Values == undefined) {
            doGetRequest($scope, $http, OLENG_CONSTANTS.PROFILE_GET_LOCATIONS, {"levelId": 3}, function (response) {
                var data = response.data;
                $scope.locationLevel3Values = data;
                $scope.constantValues = data;
                populateOptions(dataObject, index, $scope.constantValues);
            });
        } else {
            $scope.constantValues = $scope.locationLevel3Values;
            populateOptions(dataObject, index, $scope.constantValues);
        }
    } else if ((fieldType == 'Location Level4' || fieldType == 'Holdings Location Level4')) {
        if ($scope.locationLevel4Values == undefined) {
            doGetRequest($scope, $http, OLENG_CONSTANTS.PROFILE_GET_LOCATIONS, {"levelId": 4}, function (response) {
                var data = response.data;
                $scope.locationLevel4Values = data;
                $scope.constantValues = data;
                populateOptions(dataObject, index, $scope.constantValues);
            });
        } else {
            $scope.constantValues = $scope.locationLevel4Values;
            populateOptions(dataObject, index, $scope.constantValues);
        }
    } else if ((fieldType == 'Location Level5' || fieldType == 'Holdings Location Level5')) {
        if ($scope.locationLevel5Values == undefined) {
            doGetRequest($scope, $http, OLENG_CONSTANTS.PROFILE_GET_LOCATIONS, {"levelId": 5}, function (response) {
                var data = response.data;
                $scope.locationLevel5Values = data;
                $scope.constantValues = data;
                populateOptions(dataObject, index, $scope.constantValues);
            });
        } else {
            $scope.constantValues = $scope.locationLevel5Values;
            populateOptions(dataObject, index, $scope.constantValues);
        }
    } else if (fieldType == 'Bib Status') {
        if ($scope.bibStatusValues == undefined) {
            doGetRequest($scope, $http, OLENG_CONSTANTS.PROFILE_GET_DROP_DOWN_VALUES, {"dropDownType": fieldType}, function (response) {
                var data = response.data;
                $scope.bibStatusValues = data;
                $scope.constantValues = data;
                populateOptions(dataObject, index, $scope.constantValues);
            });
        } else {
            $scope.constantValues = $scope.bibStatusValues;
            populateOptions(dataObject, index, $scope.constantValues);
        }
    } else if (fieldType == 'Authentication Type') {
        if ($scope.authenticationTypeValues == undefined) {
            doGetRequest($scope, $http, OLENG_CONSTANTS.PROFILE_GET_DROP_DOWN_VALUES, {"dropDownType": fieldType}, function (response) {
                var data = response.data;
                $scope.authenticationTypeValues = data;
                $scope.constantValues = data;
                populateOptions(dataObject, index, $scope.constantValues);
            });
        } else {
            $scope.constantValues = $scope.authenticationTypeValues;
            populateOptions(dataObject, index, $scope.constantValues);
        }
    } else if (fieldType == 'Receipt Status') {
        if ($scope.receiptStatusValues == undefined) {
            doGetRequest($scope, $http, OLENG_CONSTANTS.PROFILE_GET_DROP_DOWN_VALUES, {"dropDownType": fieldType}, function (response) {
                var data = response.data;
                $scope.receiptStatusValues = data;
                $scope.constantValues = data;
                populateOptions(dataObject, index, $scope.constantValues);
            });
        } else {
            $scope.constantValues = $scope.receiptStatusValues;
            populateOptions(dataObject, index, $scope.constantValues);
        }
    } else if (fieldType == 'Access Location') {
        if ($scope.accessLocationValues == undefined) {
            doGetRequest($scope, $http, OLENG_CONSTANTS.PROFILE_GET_DROP_DOWN_VALUES, {"dropDownType": fieldType}, function (response) {
                var data = response.data;
                $scope.accessLocationValues = data;
                $scope.constantValues = data;
                populateOptions(dataObject, index, $scope.constantValues);
            });
        } else {
            $scope.constantValues = $scope.accessLocationValues;
            populateOptions(dataObject, index, $scope.constantValues);
        }
    } else if (fieldType == 'Statistical Code') {
        if ($scope.statisticalCodeValues == undefined) {
            doGetRequest($scope, $http, OLENG_CONSTANTS.PROFILE_GET_DROP_DOWN_VALUES, {"dropDownType": fieldType}, function (response) {
                var data = response.data;
                $scope.statisticalCodeValues = data;
                $scope.constantValues = data;
                populateOptions(dataObject, index, $scope.constantValues);
            });
        } else {
            $scope.constantValues = $scope.statisticalCodeValues;
            populateOptions(dataObject, index, $scope.constantValues);
        }
    } else if (fieldType == 'Access Status') {
        if ($scope.accessStatusValues == undefined) {
            doGetRequest($scope, $http, OLENG_CONSTANTS.PROFILE_GET_DROP_DOWN_VALUES, {"dropDownType": fieldType}, function (response) {
                var data = response.data;
                $scope.accessStatusValues = data;
                $scope.constantValues = data;
                populateOptions(dataObject, index, $scope.constantValues);
            });
        } else {
            $scope.constantValues = $scope.accessStatusValues;
            populateOptions(dataObject, index, $scope.constantValues);
        }
    } else if (fieldType == 'EResource Name') {
        if ($scope.eresourceNameValues == undefined) {
            doGetRequest($scope, $http, OLENG_CONSTANTS.PROFILE_GET_DROP_DOWN_VALUES, {"dropDownType": fieldType}, function (response) {
                var data = response.data;
                $scope.eresourceNameValues = data;
                $scope.constantValues = data;
                populateOptions(dataObject, index, $scope.constantValues);
            });
        } else {
            $scope.constantValues = $scope.eresourceNameValues;
            populateOptions(dataObject, index, $scope.constantValues);
        }
    } else if (fieldType == 'EResource Id') {
        if ($scope.eresourceIdValues == undefined) {
            doGetRequest($scope, $http, OLENG_CONSTANTS.PROFILE_GET_DROP_DOWN_VALUES, {"dropDownType": fieldType}, function (response) {
                var data = response.data;
                $scope.eresourceIdValues = data;
                $scope.constantValues = data;
                populateOptions(dataObject, index, $scope.constantValues);
            });
        } else {
            $scope.constantValues = $scope.eresourceIdValues;
            populateOptions(dataObject, index, $scope.constantValues);
        }
    } else if (fieldType == 'Platform') {
        if ($scope.platformValues == undefined) {
            doGetRequest($scope, $http, OLENG_CONSTANTS.PROFILE_GET_DROP_DOWN_VALUES, {"dropDownType": fieldType}, function (response) {
                var data = response.data;
                $scope.platformValues = data;
                $scope.constantValues = data;
                populateOptions(dataObject, index, $scope.constantValues);
            });
        } else {
            $scope.constantValues = $scope.platformValues;
            populateOptions(dataObject, index, $scope.constantValues);
        }
    } else if (fieldType == 'Chart Code') {
        if ($scope.chartCodeValues == undefined) {
            doGetRequest($scope, $http, OLENG_CONSTANTS.PROFILE_GET_ORDER_FIELD_VALUES, {"fieldName": fieldType}, function (response) {
                var data = response.data;
                $scope.chartCodeValues = data;
                $scope.constantValues = data;
                populateOptions(dataObject, index, $scope.constantValues);
            });
        } else {
            $scope.constantValues = $scope.chartCodeValues;
            populateOptions(dataObject, index, $scope.constantValues);
        }
    } else if (fieldType == 'Org Code') {
        if ($scope.orgCodeValues == undefined) {
            doGetRequest($scope, $http, OLENG_CONSTANTS.PROFILE_GET_ORDER_FIELD_VALUES, {"fieldName": fieldType}, function (response) {
                var data = response.data;
                $scope.orgCodeValues = data;
                $scope.constantValues = data;
                populateOptions(dataObject, index, $scope.constantValues);
            });
        } else {
            $scope.constantValues = $scope.orgCodeValues;
            populateOptions(dataObject, index, $scope.constantValues);
        }
    } else if (fieldType == 'Item Chart Code') {
        if ($scope.itemChartCodeValues == undefined) {
            doGetRequest($scope, $http, OLENG_CONSTANTS.PROFILE_GET_ORDER_FIELD_VALUES, {"fieldName": fieldType}, function (response) {
                var data = response.data;
                $scope.itemChartCodeValues = data;
                $scope.constantValues = data;
                populateOptions(dataObject, index, $scope.constantValues);
            });
        } else {
            $scope.constantValues = $scope.itemChartCodeValues;
            populateOptions(dataObject, index, $scope.constantValues);
        }
    } else if (fieldType == 'Contract Manager') {
        if ($scope.contractManagerValues == undefined) {
            doGetRequest($scope, $http, OLENG_CONSTANTS.PROFILE_GET_ORDER_FIELD_VALUES, {"fieldName": fieldType}, function (response) {
                var data = response.data;
                $scope.contractManagerValues = data;
                $scope.constantValues = data;
                populateOptions(dataObject, index, $scope.constantValues);
            });
        } else {
            $scope.constantValues = $scope.contractManagerValues;
            populateOptions(dataObject, index, $scope.constantValues);
        }
    } else if (fieldType == 'Order Type') {
        if ($scope.orderTypeValues == undefined) {
            doGetRequest($scope, $http, OLENG_CONSTANTS.PROFILE_GET_ORDER_FIELD_VALUES, {"fieldName": fieldType}, function (response) {
                var data = response.data;
                $scope.orderTypeValues = data;
                $scope.constantValues = data;
                populateOptions(dataObject, index, $scope.constantValues);
            });
        } else {
            $scope.constantValues = $scope.orderTypeValues;
            populateOptions(dataObject, index, $scope.constantValues);
        }
    } else if (fieldType == 'Funding Source') {
        if ($scope.fundingSourceValues == undefined) {
            doGetRequest($scope, $http, OLENG_CONSTANTS.PROFILE_GET_ORDER_FIELD_VALUES, {"fieldName": fieldType}, function (response) {
                var data = response.data;
                $scope.fundingSourceValues = data;
                $scope.constantValues = data;
                populateOptions(dataObject, index, $scope.constantValues);
            });
        } else {
            $scope.constantValues = $scope.fundingSourceValues;
            populateOptions(dataObject, index, $scope.constantValues);
        }
    } else if (fieldType == 'Delivery Campus Code') {
        if ($scope.deliveryCampusCodeValues == undefined) {
            doGetRequest($scope, $http, OLENG_CONSTANTS.PROFILE_GET_ORDER_FIELD_VALUES, {"fieldName": fieldType}, function (response) {
                var data = response.data;
                $scope.deliveryCampusCodeValues = data;
                $scope.constantValues = data;
                populateOptions(dataObject, index, $scope.constantValues);
            });
        } else {
            $scope.constantValues = $scope.deliveryCampusCodeValues;
            populateOptions(dataObject, index, $scope.constantValues);
        }
    } else if (fieldType == 'Building Code') {
        if ($scope.buildingCodeValues == undefined) {
            doGetRequest($scope, $http, OLENG_CONSTANTS.PROFILE_GET_ORDER_FIELD_VALUES, {"fieldName": fieldType}, function (response) {
                var data = response.data;
                $scope.buildingCodeValues = data;
                $scope.constantValues = data;
                populateOptions(dataObject, index, $scope.constantValues);
            });
        } else {
            $scope.constantValues = $scope.buildingCodeValues;
            populateOptions(dataObject, index, $scope.constantValues);
        }
    } else if (fieldType == 'Building Room Number') {
        if ($scope.buildingRoomNumberValues == undefined) {
            doGetRequest($scope, $http, OLENG_CONSTANTS.PROFILE_GET_ORDER_FIELD_VALUES, {"fieldName": fieldType}, function (response) {
                var data = response.data;
                $scope.buildingRoomNumberValues = data;
                $scope.constantValues = data;
                populateOptions(dataObject, index, $scope.constantValues);
            });
        } else {
            $scope.constantValues = $scope.buildingRoomNumberValues;
            populateOptions(dataObject, index, $scope.constantValues);
        }
    } else if (fieldType == 'Vendor Choice') {
        if ($scope.vendorChoiceValues == undefined) {
            doGetRequest($scope, $http, OLENG_CONSTANTS.PROFILE_GET_ORDER_FIELD_VALUES, {"fieldName": fieldType}, function (response) {
                var data = response.data;
                $scope.vendorChoiceValues = data;
                $scope.constantValues = data;
                populateOptions(dataObject, index, $scope.constantValues);
            });
        } else {
            $scope.constantValues = $scope.vendorChoiceValues;
            populateOptions(dataObject, index, $scope.constantValues);
        }
    } else if (fieldType == 'Cost Source') {
        if ($scope.costSourceValues == undefined) {
            doGetRequest($scope, $http, OLENG_CONSTANTS.PROFILE_GET_ORDER_FIELD_VALUES, {"fieldName": fieldType}, function (response) {
                var data = response.data;
                $scope.costSourceValues = data;
                $scope.constantValues = data;
                populateOptions(dataObject, index, $scope.constantValues);
            });
        } else {
            $scope.constantValues = $scope.costSourceValues;
            populateOptions(dataObject, index, $scope.constantValues);
        }
    } else if (fieldType == 'Default Location') {
        if ($scope.defaultLocationValues == undefined) {
            doGetRequest($scope, $http, OLENG_CONSTANTS.PROFILE_GET_ORDER_FIELD_VALUES, {"fieldName": fieldType}, function (response) {
                var data = response.data;
                $scope.defaultLocationValues = data;
                $scope.constantValues = data;
                populateOptions(dataObject, index, $scope.constantValues);
            });
        } else {
            $scope.constantValues = $scope.defaultLocationValues;
            populateOptions(dataObject, index, $scope.constantValues);
        }
    } else if (fieldType == 'Vendor Number') {
        if ($scope.vendorNumberValues == undefined) {
            doGetRequest($scope, $http, OLENG_CONSTANTS.PROFILE_GET_ORDER_FIELD_VALUES, {"fieldName": fieldType}, function (response) {
                var data = response.data;
                $scope.vendorNumberValues = data;
                $scope.constantValues = data;
                populateOptions(dataObject, index, $scope.constantValues);
            });
        } else {
            $scope.constantValues = $scope.vendorNumberValues;
            populateOptions(dataObject, index, $scope.constantValues);
        }
    } else if (fieldType == 'Vendor Alias Name') {
        if ($scope.vendorAliasNameValues == undefined) {
            doGetRequest($scope, $http, OLENG_CONSTANTS.PROFILE_GET_ORDER_FIELD_VALUES, {"fieldName": fieldType}, function (response) {
                var data = response.data;
                $scope.vendorAliasNameValues = data;
                $scope.constantValues = data;
                populateOptions(dataObject, index, $scope.constantValues);
            });
        } else {
            $scope.constantValues = $scope.vendorAliasNameValues;
            populateOptions(dataObject, index, $scope.constantValues);
        }
    } else if (fieldType == 'Acquisition Unit\'s Vendor account / Vendor Info Customer #') {
        if ($scope.acquisitionUnitsValues == undefined) {
            doGetRequest($scope, $http, OLENG_CONSTANTS.PROFILE_GET_ORDER_FIELD_VALUES, {"fieldName": fieldType}, function (response) {
                var data = response.data;
                $scope.acquisitionUnitsValues = data;
                $scope.constantValues = data;
                populateOptions(dataObject, index, $scope.constantValues);
            });
        } else {
            $scope.constantValues = $scope.acquisitionUnitsValues;
            populateOptions(dataObject, index, $scope.constantValues);
        }
    } else if (fieldType == 'Requestor Name') {
        if ($scope.requestorNameValues == undefined) {
            doGetRequest($scope, $http, OLENG_CONSTANTS.PROFILE_GET_ORDER_FIELD_VALUES, {"fieldName": fieldType}, function (response) {
                var data = response.data;
                $scope.requestorNameValues = data;
                $scope.constantValues = data;
                populateOptions(dataObject, index, $scope.constantValues);
            });
        } else {
            $scope.constantValues = $scope.requestorNameValues;
            populateOptions(dataObject, index, $scope.constantValues);
        }
    } else if (fieldType == 'Account Number') {
        if ($scope.accountNumberValues == undefined) {
            doGetRequest($scope, $http, OLENG_CONSTANTS.PROFILE_GET_ORDER_FIELD_VALUES, {"fieldName": fieldType}, function (response) {
                var data = response.data;
                $scope.accountNumberValues = data;
                $scope.constantValues = data;
                populateOptions(dataObject, index, $scope.constantValues);
            });
        } else {
            $scope.constantValues = $scope.accountNumberValues;
            populateOptions(dataObject, index, $scope.constantValues);
        }
    } else if (fieldType == 'Object Code') {
        if ($scope.objectCodeValues == undefined) {
            doGetRequest($scope, $http, OLENG_CONSTANTS.PROFILE_GET_ORDER_FIELD_VALUES, {"fieldName": fieldType}, function (response) {
                var data = response.data;
                $scope.objectCodeValues = data;
                $scope.constantValues = data;
                populateOptions(dataObject, index, $scope.constantValues);
            });
        } else {
            $scope.constantValues = $scope.objectCodeValues;
            populateOptions(dataObject, index, $scope.constantValues);
        }
    } else if (fieldType == 'Request Source') {
        if ($scope.requestSourceValues == undefined) {
            doGetRequest($scope, $http, OLENG_CONSTANTS.PROFILE_GET_ORDER_FIELD_VALUES, {"fieldName": fieldType}, function (response) {
                var data = response.data;
                $scope.requestSourceValues = data;
                $scope.constantValues = data;
                populateOptions(dataObject, index, $scope.constantValues);
            });
        } else {
            $scope.constantValues = $scope.requestSourceValues;
            populateOptions(dataObject, index, $scope.constantValues);
        }
    } else if (fieldType == 'Assign To User') {
        if ($scope.assignToUserValues == undefined) {
            doGetRequest($scope, $http, OLENG_CONSTANTS.PROFILE_GET_ORDER_FIELD_VALUES, {"fieldName": fieldType}, function (response) {
                var data = response.data;
                $scope.assignToUserValues = data;
                $scope.constantValues = data;
                populateOptions(dataObject, index, $scope.constantValues);
            });
        } else {
            $scope.constantValues = $scope.assignToUserValues;
            populateOptions(dataObject, index, $scope.constantValues);
        }
    } else if (fieldType == 'Method Of PO Transmission') {
        if ($scope.methodOfPOTransmissionValues == undefined) {
            doGetRequest($scope, $http, OLENG_CONSTANTS.PROFILE_GET_ORDER_FIELD_VALUES, {"fieldName": fieldType}, function (response) {
                var data = response.data;
                $scope.methodOfPOTransmissionValues = data;
                $scope.constantValues = data;
                populateOptions(dataObject, index, $scope.constantValues);
            });
        } else {
            $scope.constantValues = $scope.methodOfPOTransmissionValues;
            populateOptions(dataObject, index, $scope.constantValues);
        }
    } else if (fieldType == 'Recurring Payment Type') {
        if ($scope.recurringPaymentTypeValues == undefined) {
            doGetRequest($scope, $http, OLENG_CONSTANTS.PROFILE_GET_ORDER_FIELD_VALUES, {"fieldName": fieldType}, function (response) {
                var data = response.data;
                $scope.recurringPaymentTypeValues = data;
                $scope.constantValues = data;
                populateOptions(dataObject, index, $scope.constantValues);
            });
        } else {
            $scope.constantValues = $scope.recurringPaymentTypeValues;
            populateOptions(dataObject, index, $scope.constantValues);
        }
    } else if (fieldType == 'Fund Code') {
        if ($scope.fundCodeValues == undefined) {
            doGetRequest($scope, $http, OLENG_CONSTANTS.PROFILE_GET_ORDER_FIELD_VALUES, {"fieldName": fieldType}, function (response) {
                var data = response.data;
                $scope.fundCodeValues = data;
                $scope.constantValues = data;
                populateOptions(dataObject, index, $scope.constantValues);
            });
        } else {
            $scope.constantValues = $scope.fundCodeValues;
            populateOptions(dataObject, index, $scope.constantValues);
        }
    } else if (fieldType == 'Currency Type') {
        if ($scope.currencyTypeValues == undefined) {
            doGetRequest($scope, $http, OLENG_CONSTANTS.PROFILE_GET_ORDER_FIELD_VALUES, {"fieldName": fieldType}, function (response) {
                var data = response.data;
                $scope.currencyTypeValues = data;
                $scope.constantValues = data;
                populateOptions(dataObject, index, $scope.constantValues);
            });
        } else {
            $scope.constantValues = $scope.currencyTypeValues;
            populateOptions(dataObject, index, $scope.constantValues);
        }
    } else if (fieldType == 'Format'){
        if ($scope.formatTypeValues == undefined) {
            doGetRequest($scope, $http, OLENG_CONSTANTS.PROFILE_GET_ORDER_FIELD_VALUES, {"fieldName": fieldType}, function (response) {
                var data = response.data;
                $scope.formatTypeValues = data;
                $scope.constantValues = data;
                populateOptions(dataObject, index, $scope.constantValues);
            });
        } else {
            $scope.constantValues = $scope.formatTypeValues;
            populateOptions(dataObject, index, $scope.constantValues);
        }
    }
};


getBibImportProfileNames = function ($scope, $http) {
    if ($scope.bibImportProfileNames == undefined) {
        doGetRequest($scope, $http, OLENG_CONSTANTS.PROFILE_GET_NAMES, {"batchType": "Bib Import"},  function (response) {
            var data = response.data;
            $scope.bibImportProfileNames = data;
        });
    }
};

populateOptions = function (dataObject, index, constantValues) {
    var fieldName = removeOptions(dataObject.title, index);
    if(fieldName !== null && fieldName !== undefined) {
        constantValues.sort(function(a, b){
            if (a.value < b.value) return -1;
            if (b.value < a.value) return 1;
            return 0;
        });
        for (d in constantValues) {
            document.getElementById(fieldName).add(new Option(constantValues[d]["value"],constantValues[d]["value"]));
        }
    }


};

removeOptions = function (title, index) {
    var fieldName = null;
    if(title == 'Match Points') {
        fieldName = 'matchPointValues_' + index;
    } else if(title == 'Data Mappings') {
        fieldName = 'dataMappingDestinationValue_' + index;
    } else if(title == 'Matching, Add and Overlay') {
        fieldName = 'addOrOverlayFieldValue_' + index;
    }
    if(fieldName !== null && fieldName !== undefined) {
        document.getElementById(fieldName).options.length = 0;
        document.getElementById(fieldName).add(new Option('',''));
    }

    return fieldName;


};

