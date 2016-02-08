getMaintenanceDataForFieldTypeForDropDown = function (fieldType, $scope, $http) {
    if ((fieldType == 'Call Number Type' || fieldType == 'Holdings Call Number Type')) {
        if ($scope.callNumberTypeValues == undefined) {
            $http.get(OLENG_CONSTANTS.PROFILE_GET_CALLNUMBER_TYPES).success(function (data) {
                $scope.callNumberTypeValues = data;
                $scope.constantValues = data;
            });
        } else {
            $scope.constantValues = $scope.callNumberTypeValues;
        }
    } else if (fieldType == 'Item Type') {
        if ($scope.itemTypeValues == undefined) {
            $http.get(OLENG_CONSTANTS.PROFILE_GET_ITEM_TYPES).success(function (data) {
                $scope.itemTypeValues = data;
                $scope.constantValues = data;
            });
        } else {
            $scope.constantValues = $scope.itemTypeValues;
        }
    } else if (fieldType == 'Item Status') {
        if ($scope.itemStatusValues == undefined) {
            $http.get(OLENG_CONSTANTS.PROFILE_GET_ITEM_STATUS).success(function (data) {
                $scope.itemStatusValues = data;
                $scope.constantValues = data;
            });
        } else {
            $scope.constantValues = $scope.itemStatusValues;
        }
    } else if (fieldType == 'Donor Code') {
        if ($scope.donorCodes == undefined) {
            $http.get(OLENG_CONSTANTS.PROFILE_GET_DONOR_CODES).success(function (data) {
                $scope.donorCodes = data;
                $scope.constantValues = data;
            });
        } else {
            $scope.constantValues = $scope.donorCodes;
        }
    } else if ((fieldType == 'Location Level1' || fieldType == 'Holdings Location Level1')) {
        if ($scope.locationLevel1Values == undefined) {
            $http.get(OLENG_CONSTANTS.PROFILE_GET_LOCATIONS, {params: {"levelId": 1}}).success(function (data) {
                $scope.locationLevel1Values = data;
                $scope.constantValues = data;
            });
        } else {
            $scope.constantValues = $scope.locationLevel1Values;
        }
    } else if ((fieldType == 'Location Level2' || fieldType == 'Holdings Location Level2')) {
        if ($scope.locationLevel2Values == undefined) {
            $http.get(OLENG_CONSTANTS.PROFILE_GET_LOCATIONS, {params: {"levelId": 2}}).success(function (data) {
                $scope.locationLevel2Values = data;
                $scope.constantValues = data;
            });
        } else {
            $scope.constantValues = $scope.locationLevel2Values;
        }
    } else if ((fieldType == 'Location Level3' || fieldType == 'Holdings Location Level3')) {
        if ($scope.locationLevel3Values == undefined) {
            $http.get(OLENG_CONSTANTS.PROFILE_GET_LOCATIONS, {params: {"levelId": 3}}).success(function (data) {
                $scope.locationLevel3Values = data;
                $scope.constantValues = data;
            });
        } else {
            $scope.constantValues = $scope.locationLevel3Values;
        }
    } else if ((fieldType == 'Location Level4' || fieldType == 'Holdings Location Level4')) {
        if ($scope.locationLevel4Values == undefined) {
            $http.get(OLENG_CONSTANTS.PROFILE_GET_LOCATIONS, {params: {"levelId": 4}}).success(function (data) {
                $scope.locationLevel4Values = data;
                $scope.constantValues = data;
            });
        } else {
            $scope.constantValues = $scope.locationLevel4Values;
        }
    } else if ((fieldType == 'Location Level5' || fieldType == 'Holdings Location Level5')) {
        if ($scope.locationLevel5Values == undefined) {
            $http.get(OLENG_CONSTANTS.PROFILE_GET_LOCATIONS, {params: {"levelId": 5}}).success(function (data) {
                $scope.locationLevel5Values = data;
                $scope.constantValues = data;
            });
        } else {
            $scope.constantValues = $scope.locationLevel5Values;
        }
    } else if (fieldType == 'Bib Status') {
        if ($scope.bibStatusValues == undefined) {
            $http.get(OLENG_CONSTANTS.PROFILE_GET_DROP_DOWN_VALUES, {params: {"dropDownType": fieldType}}).success(function (data) {
                $scope.bibStatusValues = data;
                $scope.constantValues = data;
            });
        } else {
            $scope.constantValues = $scope.bibStatusValues;
        }
    } else if (fieldType == 'Authentication Type') {
        if ($scope.authenticationTypeValues == undefined) {
            $http.get(OLENG_CONSTANTS.PROFILE_GET_DROP_DOWN_VALUES, {params: {"dropDownType": fieldType}}).success(function (data) {
                $scope.authenticationTypeValues = data;
                $scope.constantValues = data;
            });
        } else {
            $scope.constantValues = $scope.authenticationTypeValues;
        }
    } else if (fieldType == 'Receipt Status') {
        if ($scope.receiptStatusValues == undefined) {
            $http.get(OLENG_CONSTANTS.PROFILE_GET_DROP_DOWN_VALUES, {params: {"dropDownType": fieldType}}).success(function (data) {
                $scope.receiptStatusValues = data;
                $scope.constantValues = data;
            });
        } else {
            $scope.constantValues = $scope.receiptStatusValues;
        }
    } else if (fieldType == 'Access Location') {
        if ($scope.accessLocationValues == undefined) {
            $http.get(OLENG_CONSTANTS.PROFILE_GET_DROP_DOWN_VALUES, {params: {"dropDownType": fieldType}}).success(function (data) {
                $scope.accessLocationValues = data;
                $scope.constantValues = data;
            });
        } else {
            $scope.constantValues = $scope.accessLocationValues;
        }
    } else if (fieldType == 'Statistical Code') {
        if ($scope.statisticalCodeValues == undefined) {
            $http.get(OLENG_CONSTANTS.PROFILE_GET_DROP_DOWN_VALUES, {params: {"dropDownType": fieldType}}).success(function (data) {
                $scope.statisticalCodeValues = data;
                $scope.constantValues = data;
            });
        } else {
            $scope.constantValues = $scope.statisticalCodeValues;
        }
    } else if (fieldType == 'Chart Code') {
        if ($scope.chartCodeValues == undefined) {
            $http.get(OLENG_CONSTANTS.PROFILE_GET_ORDER_FIELD_VALUES, {params: {"fieldName": fieldType}}).success(function (data) {
                $scope.chartCodeValues = data;
                $scope.constantValues = data;
            });
        } else {
            $scope.constantValues = $scope.chartCodeValues;
        }
    } else if (fieldType == 'Org Code') {
        if ($scope.orgCodeValues == undefined) {
            $http.get(OLENG_CONSTANTS.PROFILE_GET_ORDER_FIELD_VALUES, {params: {"fieldName": fieldType}}).success(function (data) {
                $scope.orgCodeValues = data;
                $scope.constantValues = data;
            });
        } else {
            $scope.constantValues = $scope.orgCodeValues;
        }
    } else if (fieldType == 'Item Chart Code') {
        if ($scope.itemChartCodeValues == undefined) {
            $http.get(OLENG_CONSTANTS.PROFILE_GET_ORDER_FIELD_VALUES, {params: {"fieldName": fieldType}}).success(function (data) {
                $scope.itemChartCodeValues = data;
                $scope.constantValues = data;
            });
        } else {
            $scope.constantValues = $scope.itemChartCodeValues;
        }
    } else if (fieldType == 'Contract Manager') {
        if ($scope.contractManagerValues == undefined) {
            $http.get(OLENG_CONSTANTS.PROFILE_GET_ORDER_FIELD_VALUES, {params: {"fieldName": fieldType}}).success(function (data) {
                $scope.contractManagerValues = data;
                $scope.constantValues = data;
            });
        } else {
            $scope.constantValues = $scope.contractManagerValues;
        }
    } else if (fieldType == 'Order Type') {
        if ($scope.orderTypeValues == undefined) {
            $http.get(OLENG_CONSTANTS.PROFILE_GET_ORDER_FIELD_VALUES, {params: {"fieldName": fieldType}}).success(function (data) {
                $scope.orderTypeValues = data;
                $scope.constantValues = data;
            });
        } else {
            $scope.constantValues = $scope.orderTypeValues;
        }
    } else if (fieldType == 'Funding Source') {
        if ($scope.fundingSourceValues == undefined) {
            $http.get(OLENG_CONSTANTS.PROFILE_GET_ORDER_FIELD_VALUES, {params: {"fieldName": fieldType}}).success(function (data) {
                $scope.fundingSourceValues = data;
                $scope.constantValues = data;
            });
        } else {
            $scope.constantValues = $scope.fundingSourceValues;
        }
    } else if (fieldType == 'Delivery Campus Code') {
        if ($scope.deliveryCampusCodeValues == undefined) {
            $http.get(OLENG_CONSTANTS.PROFILE_GET_ORDER_FIELD_VALUES, {params: {"fieldName": fieldType}}).success(function (data) {
                $scope.deliveryCampusCodeValues = data;
                $scope.constantValues = data;
            });
        } else {
            $scope.constantValues = $scope.deliveryCampusCodeValues;
        }
    } else if (fieldType == 'Building Code') {
        if ($scope.buildingCodeValues == undefined) {
            $http.get(OLENG_CONSTANTS.PROFILE_GET_ORDER_FIELD_VALUES, {params: {"fieldName": fieldType}}).success(function (data) {
                $scope.buildingCodeValues = data;
                $scope.constantValues = data;
            });
        } else {
            $scope.constantValues = $scope.buildingCodeValues;
        }
    } else if (fieldType == 'Building Room Number') {
        if ($scope.buildingRoomNumberValues == undefined) {
            $http.get(OLENG_CONSTANTS.PROFILE_GET_ORDER_FIELD_VALUES, {params: {"fieldName": fieldType}}).success(function (data) {
                $scope.buildingRoomNumberValues = data;
                $scope.constantValues = data;
            });
        } else {
            $scope.constantValues = $scope.buildingRoomNumberValues;
        }
    } else if (fieldType == 'Vendor Choice') {
        if ($scope.vendorChoiceValues == undefined) {
            $http.get(OLENG_CONSTANTS.PROFILE_GET_ORDER_FIELD_VALUES, {params: {"fieldName": fieldType}}).success(function (data) {
                $scope.vendorChoiceValues = data;
                $scope.constantValues = data;
            });
        } else {
            $scope.constantValues = $scope.vendorChoiceValues;
        }
    } else if (fieldType == 'Cost Source') {
        if ($scope.costSourceValues == undefined) {
            $http.get(OLENG_CONSTANTS.PROFILE_GET_ORDER_FIELD_VALUES, {params: {"fieldName": fieldType}}).success(function (data) {
                $scope.costSourceValues = data;
                $scope.constantValues = data;
            });
        } else {
            $scope.constantValues = $scope.costSourceValues;
        }
    } else if (fieldType == 'Default Location') {
        if ($scope.defaultLocationValues == undefined) {
            $http.get(OLENG_CONSTANTS.PROFILE_GET_ORDER_FIELD_VALUES, {params: {"fieldName": fieldType}}).success(function (data) {
                $scope.defaultLocationValues = data;
                $scope.constantValues = data;
            });
        } else {
            $scope.constantValues = $scope.defaultLocationValues;
        }
    } else if (fieldType == 'Vendor Number') {
        if ($scope.vendorNumberValues == undefined) {
            $http.get(OLENG_CONSTANTS.PROFILE_GET_ORDER_FIELD_VALUES, {params: {"fieldName": fieldType}}).success(function (data) {
                $scope.vendorNumberValues = data;
                $scope.constantValues = data;
            });
        } else {
            $scope.constantValues = $scope.vendorNumberValues;
        }
    } else if (fieldType == 'Vendor Alias Name') {
        if ($scope.vendorAliasNameValues == undefined) {
            $http.get(OLENG_CONSTANTS.PROFILE_GET_ORDER_FIELD_VALUES, {params: {"fieldName": fieldType}}).success(function (data) {
                $scope.vendorAliasNameValues = data;
                $scope.constantValues = data;
            });
        } else {
            $scope.constantValues = $scope.vendorAliasNameValues;
        }
    } else if (fieldType == 'Acquisition Unit\'s Vendor account / Vendor Info Customer #') {
        if ($scope.acquisitionUnitsValues == undefined) {
            $http.get(OLENG_CONSTANTS.PROFILE_GET_ORDER_FIELD_VALUES, {params: {"fieldName": fieldType}}).success(function (data) {
                $scope.acquisitionUnitsValues = data;
                $scope.constantValues = data;
            });
        } else {
            $scope.constantValues = $scope.acquisitionUnitsValues;
        }
    } else if (fieldType == 'Requestor Name') {
        if ($scope.requestorNameValues == undefined) {
            $http.get(OLENG_CONSTANTS.PROFILE_GET_ORDER_FIELD_VALUES, {params: {"fieldName": fieldType}}).success(function (data) {
                $scope.requestorNameValues = data;
                $scope.constantValues = data;
            });
        } else {
            $scope.constantValues = $scope.requestorNameValues;
        }
    } else if (fieldType == 'Account Number') {
        if ($scope.accountNumberValues == undefined) {
            console.time("timer1");
            $http.get(OLENG_CONSTANTS.PROFILE_GET_ORDER_FIELD_VALUES, {params: {"fieldName": fieldType}}).success(function (data) {
                $scope.accountNumberValues = data;
                $scope.constantValues = data;
                console.timeEnd("timer1");
            });
        } else {
            $scope.constantValues = $scope.accountNumberValues;
        }
    } else if (fieldType == 'Object Code') {
        if ($scope.objectCodeValues == undefined) {
            $http.get(OLENG_CONSTANTS.PROFILE_GET_ORDER_FIELD_VALUES, {params: {"fieldName": fieldType}}).success(function (data) {
                $scope.objectCodeValues = data;
                $scope.constantValues = data;
            });
        } else {
            $scope.constantValues = $scope.objectCodeValues;
        }
    } else if (fieldType == 'Request Source') {
        if ($scope.requestSourceValues == undefined) {
            $http.get(OLENG_CONSTANTS.PROFILE_GET_ORDER_FIELD_VALUES, {params: {"fieldName": fieldType}}).success(function (data) {
                $scope.requestSourceValues = data;
                $scope.constantValues = data;
            });
        } else {
            $scope.constantValues = $scope.requestSourceValues;
        }
    } else if (fieldType == 'Assign To User') {
        if ($scope.assignToUserValues == undefined) {
            $http.get(OLENG_CONSTANTS.PROFILE_GET_ORDER_FIELD_VALUES, {params: {"fieldName": fieldType}}).success(function (data) {
                $scope.assignToUserValues = data;
                $scope.constantValues = data;
            });
        } else {
            $scope.constantValues = $scope.assignToUserValues;
        }
    } else if (fieldType == 'Method Of PO Transmission') {
        if ($scope.methodOfPOTransmissionValues == undefined) {
            $http.get(OLENG_CONSTANTS.PROFILE_GET_ORDER_FIELD_VALUES, {params: {"fieldName": fieldType}}).success(function (data) {
                $scope.methodOfPOTransmissionValues = data;
                $scope.constantValues = data;
            });
        } else {
            $scope.constantValues = $scope.methodOfPOTransmissionValues;
        }
    } else if (fieldType == 'Recurring Payment Type') {
        if ($scope.recurringPaymentTypeValues == undefined) {
            $http.get(OLENG_CONSTANTS.PROFILE_GET_ORDER_FIELD_VALUES, {params: {"fieldName": fieldType}}).success(function (data) {
                $scope.recurringPaymentTypeValues = data;
                $scope.constantValues = data;
            });
        } else {
            $scope.constantValues = $scope.recurringPaymentTypeValues;
        }
    } else if (fieldType == 'Fund Code') {
        if ($scope.fundCodeValues == undefined) {
            $http.get(OLENG_CONSTANTS.PROFILE_GET_ORDER_FIELD_VALUES, {params: {"fieldName": fieldType}}).success(function (data) {
                $scope.fundCodeValues = data;
                $scope.constantValues = data;
            });
        } else {
            $scope.constantValues = $scope.fundCodeValues;
        }
    } else if (fieldType == 'Currency Type') {
        if ($scope.currencyTypeValues == undefined) {
            $http.get(OLENG_CONSTANTS.PROFILE_GET_ORDER_FIELD_VALUES, {params: {"fieldName": fieldType}}).success(function (data) {
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
        $http.get(OLENG_CONSTANTS.PROFILE_GET_NAMES, {params: {"batchType": "Bib Import"}}).success(function (data) {
            $scope.bibImportProfileNames = data;
        });
    }
};

