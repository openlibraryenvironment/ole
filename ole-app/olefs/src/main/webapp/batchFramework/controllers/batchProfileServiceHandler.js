getMaintenanceDataForFieldType = function (title, fieldType, $scope, $http) {
    if ((fieldType == 'Call Number Type' || fieldType == 'Holdings Call Number Type') && $scope.callNumberTypeValues == undefined) {
        $http.get(OLENG_CONSTANTS.PROFILE_GET_CALLNUMBER_TYPES).success(function (data) {
            $scope.callNumberTypeValues = data;
        });
    } else if (fieldType == 'Item Type' && $scope.itemTypeValues == undefined) {
        $http.get(OLENG_CONSTANTS.PROFILE_GET_ITEM_TYPES).success(function (data) {
            $scope.itemTypeValues = data;
        });
    } else if (fieldType == 'Item Status' && $scope.itemStatusValues == undefined) {
        $http.get(OLENG_CONSTANTS.PROFILE_GET_ITEM_STATUS).success(function (data) {
            $scope.itemStatusValues = data;
        });
    } else if (fieldType == 'Donor Code' && $scope.donorCodeValues == undefined) {
        $http.get(OLENG_CONSTANTS.PROFILE_GET_DONOR_CODES).success(function (data) {
            $scope.donorCodes = data;
        });
    } else if ((fieldType == 'Location Level1' || fieldType == 'Holdings Location Level1') && $scope.locationLevel1Values == undefined) {
        $http.get(OLENG_CONSTANTS.PROFILE_GET_LOCATIONS, {params: {"levelId": 1}}).success(function (data) {
            $scope.locationLevel1Values = data;
        });
    } else if ((fieldType == 'Location Level2' || fieldType == 'Holdings Location Level2') && $scope.locationLevel2Values == undefined) {
        $http.get(OLENG_CONSTANTS.PROFILE_GET_LOCATIONS, {params: {"levelId": 2}}).success(function (data) {
            $scope.locationLevel2Values = data;
        });
    } else if ((fieldType == 'Location Level3' || fieldType == 'Holdings Location Level3') && $scope.locationLevel3Values == undefined) {
        $http.get(OLENG_CONSTANTS.PROFILE_GET_LOCATIONS, {params: {"levelId": 3}}).success(function (data) {
            $scope.locationLevel3Values = data;
        });
    } else if ((fieldType == 'Location Level4' || fieldType == 'Holdings Location Level4') && $scope.locationLevel4Values == undefined) {
        $http.get(OLENG_CONSTANTS.PROFILE_GET_LOCATIONS, {params: {"levelId": 4}}).success(function (data) {
            $scope.locationLevel4Values = data;
        });
    } else if ((fieldType == 'Location Level5' || fieldType == 'Holdings Location Level5') && $scope.locationLevel5Values == undefined) {
        $http.get(OLENG_CONSTANTS.PROFILE_GET_LOCATIONS, {params: {"levelId": 5}}).success(function (data) {
            $scope.locationLevel5Values = data;
        });
    } else if (fieldType == 'Bib Status' && $scope.bibStatuses == undefined) {
        $http.get(OLENG_CONSTANTS.PROFILE_GET_BIB_STATUS).success(function (data) {
            $scope.bibStatuses = data;
        });
    } else if (fieldType == 'Chart Code'
        || fieldType == 'Org Code'
        || fieldType == 'Item Chart Code'
        || fieldType == 'Contract Manager'
        || fieldType == 'Order Type'
        || fieldType == 'Funding Source'
        || fieldType == 'Delivery Campus Code'
        || fieldType == 'Building Code'
        || fieldType == 'Building Room Number'
        || fieldType == 'Vendor Choice'
        || fieldType == 'Cost Source'
        || fieldType == 'Default Location'
        || fieldType == 'Vendor Number'
        || fieldType == 'Vendor Alias Name'
        || fieldType == 'Acquisition Unit\'s Vendor account / Vendor Info Customer #'
        || fieldType == 'Requestor Name'
        || fieldType == 'Account Number'
        || fieldType == 'Object Code'
        || fieldType == 'Request Source'
        || fieldType == 'Assign To User'
        || fieldType == 'Method Of PO Transmission'
        || fieldType == 'Recurring Payment Type'
        || fieldType == 'Fund Code') {
        $http.get(OLENG_CONSTANTS.PROFILE_GET_ORDER_FIELD_VALUES, {params: {"fieldName": fieldType}}).success(function (data) {
            if (title == 'Constants And Defaults') {
                $scope.constantAndDefaultFieldValues = data;
            } else if (title == 'Data Mappings') {
                $scope.dataMappingFieldValues = data;
            }
        });
    }
};


getMaintenanceDataForFieldTypeForDropDown = function (title, fieldType, $scope, $http) {
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
    } else if (fieldType == 'Chart Code'
        || fieldType == 'Org Code'
        || fieldType == 'Item Chart Code'
        || fieldType == 'Contract Manager'
        || fieldType == 'Order Type'
        || fieldType == 'Funding Source'
        || fieldType == 'Delivery Campus Code'
        || fieldType == 'Building Code'
        || fieldType == 'Building Room Number'
        || fieldType == 'Vendor Choice'
        || fieldType == 'Cost Source'
        || fieldType == 'Default Location'
        || fieldType == 'Vendor Number'
        || fieldType == 'Vendor Alias Name'
        || fieldType == 'Acquisition Unit\'s Vendor account / Vendor Info Customer #'
        || fieldType == 'Requestor Name'
        || fieldType == 'Account Number'
        || fieldType == 'Object Code'
        || fieldType == 'Request Source'
        || fieldType == 'Assign To User'
        || fieldType == 'Method Of PO Transmission'
        || fieldType == 'Recurring Payment Type'
        || fieldType == 'Fund Code'
        || fieldType == 'Currency Type') {
        $http.get(OLENG_CONSTANTS.PROFILE_GET_ORDER_FIELD_VALUES, {params: {"fieldName": fieldType}}).success(function (data) {
            if (title == 'Constants And Defaults') {
                $scope.constantAndDefaultFieldValues = data;
            } else if (title == 'Data Mappings') {
                $scope.dataMappingFieldValues = data;
            }
        });
    }
};