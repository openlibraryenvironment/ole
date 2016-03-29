getMaintenanceDataForFieldTypeForDropDown = function (fieldType, $scope, $http) {
    if ((fieldType == 'Call Number Type' || fieldType == 'Holdings Call Number Type')) {
        if ($scope.callNumberTypeValues == undefined) {
            doGetRequest($scope, $http, OLENG_CONSTANTS.PROFILE_GET_CALLNUMBER_TYPES, null, function (response) {
                var data = response.data;
                $scope.callNumberTypeValues = data;
                $scope.constantValues = data;
            });
        } else {
            $scope.constantValues = $scope.callNumberTypeValues;
        }
    } else if (fieldType == 'Item Type') {
        if ($scope.itemTypeValues == undefined) {
            doGetRequest($scope, $http, OLENG_CONSTANTS.PROFILE_GET_ITEM_TYPES, null, function (response) {
                var data = response.data;
                $scope.itemTypeValues = data;
                $scope.constantValues = data;
            });
        } else {
            $scope.constantValues = $scope.itemTypeValues;
        }
    } else if (fieldType == 'Item Status') {
        if ($scope.itemStatusValues == undefined) {
            doGetRequest($scope, $http, OLENG_CONSTANTS.PROFILE_GET_ITEM_STATUS, null, function (response) {
                var data = response.data;
                $scope.itemStatusValues = data;
                $scope.constantValues = data;
            });
        } else {
            $scope.constantValues = $scope.itemStatusValues;
        }
    } else if (fieldType == 'Donor Code') {
        if ($scope.donorCodes == undefined) {
            doGetRequest($scope, $http, OLENG_CONSTANTS.PROFILE_GET_DONOR_CODES, null, function (response) {
                var data = response.data;
                $scope.donorCodes = data;
                $scope.constantValues = data;
            });
        } else {
            $scope.constantValues = $scope.donorCodes;
        }
    } else if ((fieldType == 'Location Level1' || fieldType == 'Holdings Location Level1')) {
        if ($scope.locationLevel1Values == undefined) {
            doGetRequest($scope, $http, OLENG_CONSTANTS.PROFILE_GET_LOCATIONS, {"levelId": 1}, function (response) {
                var data = response.data;
                $scope.locationLevel1Values = data;
                $scope.constantValues = data;
            });
        } else {
            $scope.constantValues = $scope.locationLevel1Values;
        }
    } else if ((fieldType == 'Location Level2' || fieldType == 'Holdings Location Level2')) {
        if ($scope.locationLevel2Values == undefined) {
            doGetRequest($scope, $http, OLENG_CONSTANTS.PROFILE_GET_LOCATIONS, {"levelId": 2}, function (response) {
                var data = response.data;
                $scope.locationLevel2Values = data;
                $scope.constantValues = data;
            });
        } else {
            $scope.constantValues = $scope.locationLevel2Values;
        }
    } else if ((fieldType == 'Location Level3' || fieldType == 'Holdings Location Level3')) {
        if ($scope.locationLevel3Values == undefined) {
            doGetRequest($scope, $http, OLENG_CONSTANTS.PROFILE_GET_LOCATIONS, {"levelId": 3}, function (response) {
                var data = response.data;
                $scope.locationLevel3Values = data;
                $scope.constantValues = data;
            });
        } else {
            $scope.constantValues = $scope.locationLevel3Values;
        }
    } else if ((fieldType == 'Location Level4' || fieldType == 'Holdings Location Level4')) {
        if ($scope.locationLevel4Values == undefined) {
            doGetRequest($scope, $http, OLENG_CONSTANTS.PROFILE_GET_LOCATIONS, {"levelId": 4}, function (response) {
                var data = response.data;
                $scope.locationLevel4Values = data;
                $scope.constantValues = data;
            });
        } else {
            $scope.constantValues = $scope.locationLevel4Values;
        }
    } else if ((fieldType == 'Location Level5' || fieldType == 'Holdings Location Level5')) {
        if ($scope.locationLevel5Values == undefined) {
            doGetRequest($scope, $http, OLENG_CONSTANTS.PROFILE_GET_LOCATIONS, {"levelId": 5}, function (response) {
                var data = response.data;
                $scope.locationLevel5Values = data;
                $scope.constantValues = data;
            });
        } else {
            $scope.constantValues = $scope.locationLevel5Values;
        }
    } else if (fieldType == 'Bib Status') {
        if ($scope.bibStatusValues == undefined) {
            doGetRequest($scope, $http, OLENG_CONSTANTS.PROFILE_GET_DROP_DOWN_VALUES, {"dropDownType": fieldType}, function (response) {
                var data = response.data;
                $scope.bibStatusValues = data;
                $scope.constantValues = data;
            });
        } else {
            $scope.constantValues = $scope.bibStatusValues;
        }
    } else if (fieldType == 'Authentication Type') {
        if ($scope.authenticationTypeValues == undefined) {
            doGetRequest($scope, $http, OLENG_CONSTANTS.PROFILE_GET_DROP_DOWN_VALUES, {"dropDownType": fieldType}, function (response) {
                var data = response.data;
                $scope.authenticationTypeValues = data;
                $scope.constantValues = data;
            });
        } else {
            $scope.constantValues = $scope.authenticationTypeValues;
        }
    } else if (fieldType == 'Receipt Status') {
        if ($scope.receiptStatusValues == undefined) {
            doGetRequest($scope, $http, OLENG_CONSTANTS.PROFILE_GET_DROP_DOWN_VALUES, {"dropDownType": fieldType}, function (response) {
                var data = response.data;
                $scope.receiptStatusValues = data;
                $scope.constantValues = data;
            });
        } else {
            $scope.constantValues = $scope.receiptStatusValues;
        }
    } else if (fieldType == 'Access Location') {
        if ($scope.accessLocationValues == undefined) {
            doGetRequest($scope, $http, OLENG_CONSTANTS.PROFILE_GET_DROP_DOWN_VALUES, {"dropDownType": fieldType}, function (response) {
                var data = response.data;
                $scope.accessLocationValues = data;
                $scope.constantValues = data;
            });
        } else {
            $scope.constantValues = $scope.accessLocationValues;
        }
    } else if (fieldType == 'Statistical Code') {
        if ($scope.statisticalCodeValues == undefined) {
            doGetRequest($scope, $http, OLENG_CONSTANTS.PROFILE_GET_DROP_DOWN_VALUES, {"dropDownType": fieldType}, function (response) {
                var data = response.data;
                $scope.statisticalCodeValues = data;
                $scope.constantValues = data;
            });
        } else {
            $scope.constantValues = $scope.statisticalCodeValues;
        }
    } else if (fieldType == 'Chart Code') {
        if ($scope.chartCodeValues == undefined) {
            doGetRequest($scope, $http, OLENG_CONSTANTS.PROFILE_GET_ORDER_FIELD_VALUES, {"fieldName": fieldType}, function (response) {
                var data = response.data;
                $scope.chartCodeValues = data;
                $scope.constantValues = data;
            });
        } else {
            $scope.constantValues = $scope.chartCodeValues;
        }
    } else if (fieldType == 'Org Code') {
        if ($scope.orgCodeValues == undefined) {
            doGetRequest($scope, $http, OLENG_CONSTANTS.PROFILE_GET_ORDER_FIELD_VALUES, {"fieldName": fieldType}, function (response) {
                var data = response.data;
                $scope.orgCodeValues = data;
                $scope.constantValues = data;
            });
        } else {
            $scope.constantValues = $scope.orgCodeValues;
        }
    } else if (fieldType == 'Item Chart Code') {
        if ($scope.itemChartCodeValues == undefined) {
            doGetRequest($scope, $http, OLENG_CONSTANTS.PROFILE_GET_ORDER_FIELD_VALUES, {"fieldName": fieldType}, function (response) {
                var data = response.data;
                $scope.itemChartCodeValues = data;
                $scope.constantValues = data;
            });
        } else {
            $scope.constantValues = $scope.itemChartCodeValues;
        }
    } else if (fieldType == 'Contract Manager') {
        if ($scope.contractManagerValues == undefined) {
            doGetRequest($scope, $http, OLENG_CONSTANTS.PROFILE_GET_ORDER_FIELD_VALUES, {"fieldName": fieldType}, function (response) {
                var data = response.data;
                $scope.contractManagerValues = data;
                $scope.constantValues = data;
            });
        } else {
            $scope.constantValues = $scope.contractManagerValues;
        }
    } else if (fieldType == 'Order Type') {
        if ($scope.orderTypeValues == undefined) {
            doGetRequest($scope, $http, OLENG_CONSTANTS.PROFILE_GET_ORDER_FIELD_VALUES, {"fieldName": fieldType}, function (response) {
                var data = response.data;
                $scope.orderTypeValues = data;
                $scope.constantValues = data;
            });
        } else {
            $scope.constantValues = $scope.orderTypeValues;
        }
    } else if (fieldType == 'Funding Source') {
        if ($scope.fundingSourceValues == undefined) {
            doGetRequest($scope, $http, OLENG_CONSTANTS.PROFILE_GET_ORDER_FIELD_VALUES, {"fieldName": fieldType}, function (response) {
                var data = response.data;
                $scope.fundingSourceValues = data;
                $scope.constantValues = data;
            });
        } else {
            $scope.constantValues = $scope.fundingSourceValues;
        }
    } else if (fieldType == 'Delivery Campus Code') {
        if ($scope.deliveryCampusCodeValues == undefined) {
            doGetRequest($scope, $http, OLENG_CONSTANTS.PROFILE_GET_ORDER_FIELD_VALUES, {"fieldName": fieldType}, function (response) {
                var data = response.data;
                $scope.deliveryCampusCodeValues = data;
                $scope.constantValues = data;
            });
        } else {
            $scope.constantValues = $scope.deliveryCampusCodeValues;
        }
    } else if (fieldType == 'Building Code') {
        if ($scope.buildingCodeValues == undefined) {
            doGetRequest($scope, $http, OLENG_CONSTANTS.PROFILE_GET_ORDER_FIELD_VALUES, {"fieldName": fieldType}, function (response) {
                var data = response.data;
                $scope.buildingCodeValues = data;
                $scope.constantValues = data;
            });
        } else {
            $scope.constantValues = $scope.buildingCodeValues;
        }
    } else if (fieldType == 'Building Room Number') {
        if ($scope.buildingRoomNumberValues == undefined) {
            doGetRequest($scope, $http, OLENG_CONSTANTS.PROFILE_GET_ORDER_FIELD_VALUES, {"fieldName": fieldType}, function (response) {
                var data = response.data;
                $scope.buildingRoomNumberValues = data;
                $scope.constantValues = data;
            });
        } else {
            $scope.constantValues = $scope.buildingRoomNumberValues;
        }
    } else if (fieldType == 'Vendor Choice') {
        if ($scope.vendorChoiceValues == undefined) {
            doGetRequest($scope, $http, OLENG_CONSTANTS.PROFILE_GET_ORDER_FIELD_VALUES, {"fieldName": fieldType}, function (response) {
                var data = response.data;
                $scope.vendorChoiceValues = data;
                $scope.constantValues = data;
            });
        } else {
            $scope.constantValues = $scope.vendorChoiceValues;
        }
    } else if (fieldType == 'Cost Source') {
        if ($scope.costSourceValues == undefined) {
            doGetRequest($scope, $http, OLENG_CONSTANTS.PROFILE_GET_ORDER_FIELD_VALUES, {"fieldName": fieldType}, function (response) {
                var data = response.data;
                $scope.costSourceValues = data;
                $scope.constantValues = data;
            });
        } else {
            $scope.constantValues = $scope.costSourceValues;
        }
    } else if (fieldType == 'Default Location') {
        if ($scope.defaultLocationValues == undefined) {
            doGetRequest($scope, $http, OLENG_CONSTANTS.PROFILE_GET_ORDER_FIELD_VALUES, {"fieldName": fieldType}, function (response) {
                var data = response.data;
                $scope.defaultLocationValues = data;
                $scope.constantValues = data;
            });
        } else {
            $scope.constantValues = $scope.defaultLocationValues;
        }
    } else if (fieldType == 'Vendor Number') {
        if ($scope.vendorNumberValues == undefined) {
            doGetRequest($scope, $http, OLENG_CONSTANTS.PROFILE_GET_ORDER_FIELD_VALUES, {"fieldName": fieldType}, function (response) {
                var data = response.data;
                $scope.vendorNumberValues = data;
                $scope.constantValues = data;
            });
        } else {
            $scope.constantValues = $scope.vendorNumberValues;
        }
    } else if (fieldType == 'Vendor Alias Name') {
        if ($scope.vendorAliasNameValues == undefined) {
            doGetRequest($scope, $http, OLENG_CONSTANTS.PROFILE_GET_ORDER_FIELD_VALUES, {"fieldName": fieldType}, function (response) {
                var data = response.data;
                $scope.vendorAliasNameValues = data;
                $scope.constantValues = data;
            });
        } else {
            $scope.constantValues = $scope.vendorAliasNameValues;
        }
    } else if (fieldType == 'Acquisition Unit\'s Vendor account / Vendor Info Customer #') {
        if ($scope.acquisitionUnitsValues == undefined) {
            doGetRequest($scope, $http, OLENG_CONSTANTS.PROFILE_GET_ORDER_FIELD_VALUES, {"fieldName": fieldType}, function (response) {
                var data = response.data;
                $scope.acquisitionUnitsValues = data;
                $scope.constantValues = data;
            });
        } else {
            $scope.constantValues = $scope.acquisitionUnitsValues;
        }
    } else if (fieldType == 'Requestor Name') {
        if ($scope.requestorNameValues == undefined) {
            doGetRequest($scope, $http, OLENG_CONSTANTS.PROFILE_GET_ORDER_FIELD_VALUES, {"fieldName": fieldType}, function (response) {
                var data = response.data;
                $scope.requestorNameValues = data;
                $scope.constantValues = data;
            });
        } else {
            $scope.constantValues = $scope.requestorNameValues;
        }
    } else if (fieldType == 'Account Number') {
        if ($scope.accountNumberValues == undefined) {
            console.time("timer1");
            doGetRequest($scope, $http, OLENG_CONSTANTS.PROFILE_GET_ORDER_FIELD_VALUES, {"fieldName": fieldType}, function (response) {
                var data = response.data;
                $scope.accountNumberValues = data;
                $scope.constantValues = data;
                console.timeEnd("timer1");
            });
        } else {
            $scope.constantValues = $scope.accountNumberValues;
        }
    } else if (fieldType == 'Object Code') {
        if ($scope.objectCodeValues == undefined) {
            doGetRequest($scope, $http, OLENG_CONSTANTS.PROFILE_GET_ORDER_FIELD_VALUES, {"fieldName": fieldType}, function (response) {
                var data = response.data;
                $scope.objectCodeValues = data;
                $scope.constantValues = data;
            });
        } else {
            $scope.constantValues = $scope.objectCodeValues;
        }
    } else if (fieldType == 'Request Source') {
        if ($scope.requestSourceValues == undefined) {
            doGetRequest($scope, $http, OLENG_CONSTANTS.PROFILE_GET_ORDER_FIELD_VALUES, {"fieldName": fieldType}, function (response) {
                var data = response.data;
                $scope.requestSourceValues = data;
                $scope.constantValues = data;
            });
        } else {
            $scope.constantValues = $scope.requestSourceValues;
        }
    } else if (fieldType == 'Assign To User') {
        if ($scope.assignToUserValues == undefined) {
            doGetRequest($scope, $http, OLENG_CONSTANTS.PROFILE_GET_ORDER_FIELD_VALUES, {"fieldName": fieldType}, function (response) {
                var data = response.data;
                $scope.assignToUserValues = data;
                $scope.constantValues = data;
            });
        } else {
            $scope.constantValues = $scope.assignToUserValues;
        }
    } else if (fieldType == 'Method Of PO Transmission') {
        if ($scope.methodOfPOTransmissionValues == undefined) {
            doGetRequest($scope, $http, OLENG_CONSTANTS.PROFILE_GET_ORDER_FIELD_VALUES, {"fieldName": fieldType}, function (response) {
                var data = response.data;
                $scope.methodOfPOTransmissionValues = data;
                $scope.constantValues = data;
            });
        } else {
            $scope.constantValues = $scope.methodOfPOTransmissionValues;
        }
    } else if (fieldType == 'Recurring Payment Type') {
        if ($scope.recurringPaymentTypeValues == undefined) {
            doGetRequest($scope, $http, OLENG_CONSTANTS.PROFILE_GET_ORDER_FIELD_VALUES, {"fieldName": fieldType}, function (response) {
                var data = response.data;
                $scope.recurringPaymentTypeValues = data;
                $scope.constantValues = data;
            });
        } else {
            $scope.constantValues = $scope.recurringPaymentTypeValues;
        }
    } else if (fieldType == 'Fund Code') {
        if ($scope.fundCodeValues == undefined) {
            doGetRequest($scope, $http, OLENG_CONSTANTS.PROFILE_GET_ORDER_FIELD_VALUES, {"fieldName": fieldType}, function (response) {
                var data = response.data;
                $scope.fundCodeValues = data;
                $scope.constantValues = data;
            });
        } else {
            $scope.constantValues = $scope.fundCodeValues;
        }
    } else if (fieldType == 'Currency Type') {
        if ($scope.currencyTypeValues == undefined) {
            doGetRequest($scope, $http, OLENG_CONSTANTS.PROFILE_GET_ORDER_FIELD_VALUES, {"fieldName": fieldType}, function (response) {
                var data = response.data;
                $scope.currencyTypeValues = data;
                $scope.constantValues = data;
            });
        } else {
            $scope.constantValues = $scope.currencyTypeValues;
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

