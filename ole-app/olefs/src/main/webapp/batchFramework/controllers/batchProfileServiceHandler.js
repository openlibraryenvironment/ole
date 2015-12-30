getMaintenanceData = function (dataObject, $scope, $http) {
    if (dataObject.title == 'Match Points') {
        if ((dataObject.matchPointType == 'Call Number Type' || dataObject.matchPointType == 'Holdings Call Number Type') && $scope.callNumberTypeValues == undefined) {
            $http.get(OLENG_CONSTANTS.PROFILE_GET_CALLNUMBER_TYPES).success(function (data) {
                $scope.callNumberTypeValues = data;
            });
        } else if (dataObject.matchPointType == 'Item Type' && $scope.itemTypeValues == undefined) {
            $http.get(OLENG_CONSTANTS.PROFILE_GET_ITEM_TYPES).success(function (data) {
                $scope.itemTypeValues = data;
            });
        } else if (dataObject.matchPointType == 'Item Status' && $scope.itemStatusValues == undefined) {
            $http.get(OLENG_CONSTANTS.PROFILE_GET_ITEM_STATUS).success(function (data) {
                $scope.itemStatusValues = data;
            });
        } else if (dataObject.matchPointType == 'Donor Code' && $scope.donorCodeValues == undefined) {
            $http.get(OLENG_CONSTANTS.PROFILE_GET_DONOR_CODES).success(function (data) {
                $scope.donorCodes = data;
            });
        } else if ((dataObject.matchPointType == 'Location Level1' || dataObject.matchPointType == 'Holdings Location Level1') && $scope.locationLevel1Values == undefined) {
            $http.get(OLENG_CONSTANTS.PROFILE_GET_LOCATIONS, {params: {"levelId": 1}}).success(function (data) {
                $scope.locationLevel1Values = data;
            });
        } else if ((dataObject.matchPointType == 'Location Level2' || dataObject.matchPointType == 'Holdings Location Level2') && $scope.locationLevel2Values == undefined) {
            $http.get(OLENG_CONSTANTS.PROFILE_GET_LOCATIONS, {params: {"levelId": 2}}).success(function (data) {
                $scope.locationLevel2Values = data;
            });
        } else if ((dataObject.matchPointType == 'Location Level3' || dataObject.matchPointType == 'Holdings Location Level3') && $scope.locationLevel3Values == undefined) {
            $http.get(OLENG_CONSTANTS.PROFILE_GET_LOCATIONS, {params: {"levelId": 3}}).success(function (data) {
                $scope.locationLevel3Values = data;
            });
        } else if ((dataObject.matchPointType == 'Location Level4' || dataObject.matchPointType == 'Holdings Location Level4') && $scope.locationLevel4Values == undefined) {
            $http.get(OLENG_CONSTANTS.PROFILE_GET_LOCATIONS, {params: {"levelId": 4}}).success(function (data) {
                $scope.locationLevel4Values = data;
            });
        } else if ((dataObject.matchPointType == 'Location Level5' || dataObject.matchPointType == 'Holdings Location Level5') && $scope.locationLevel5Values == undefined) {
            $http.get(OLENG_CONSTANTS.PROFILE_GET_LOCATIONS, {params: {"levelId": 5}}).success(function (data) {
                $scope.locationLevel5Values = data;
            });
        }
    } else if (dataObject.title == 'Data Mappings') {
        if ((dataObject.field == 'Call Number Type' || dataObject.field == 'Holdings Call Number Type') && $scope.callNumberTypeValues == undefined) {
            $http.get(OLENG_CONSTANTS.PROFILE_GET_CALLNUMBER_TYPES).success(function (data) {
                $scope.callNumberTypeValues = data;
            });
        } else if (dataObject.field == 'Item Type' && $scope.itemTypeValues == undefined) {
            $http.get(OLENG_CONSTANTS.PROFILE_GET_ITEM_TYPES).success(function (data) {
                $scope.itemTypeValues = data;
            });
        } else if (dataObject.field == 'Item Status' && $scope.itemStatusValues == undefined) {
            $http.get(OLENG_CONSTANTS.PROFILE_GET_ITEM_STATUS).success(function (data) {
                $scope.itemStatusValues = data;
            });
        } else if (dataObject.field == 'Donor Code' && $scope.donorCodeValues == undefined) {
            $http.get(OLENG_CONSTANTS.PROFILE_GET_DONOR_CODES).success(function (data) {
                $scope.donorCodes = data;
            });
        } else if ((dataObject.field == 'Location Level1' || dataObject.field == 'Holdings Location Level1') && $scope.locationLevel1Values == undefined) {
            $http.get(OLENG_CONSTANTS.PROFILE_GET_LOCATIONS, {params: {"levelId": 1}}).success(function (data) {
                $scope.locationLevel1Values = data;
            });
        } else if ((dataObject.field == 'Location Level2' || dataObject.field == 'Holdings Location Level2') && $scope.locationLevel2Values == undefined) {
            $http.get(OLENG_CONSTANTS.PROFILE_GET_LOCATIONS, {params: {"levelId": 2}}).success(function (data) {
                $scope.locationLevel2Values = data;
            });
        } else if ((dataObject.field == 'Location Level3' || dataObject.field == 'Holdings Location Level3') && $scope.locationLevel3Values == undefined) {
            $http.get(OLENG_CONSTANTS.PROFILE_GET_LOCATIONS, {params: {"levelId": 3}}).success(function (data) {
                $scope.locationLevel3Values = data;
            });
        } else if ((dataObject.field == 'Location Level4' || dataObject.field == 'Holdings Location Level4') && $scope.locationLevel4Values == undefined) {
            $http.get(OLENG_CONSTANTS.PROFILE_GET_LOCATIONS, {params: {"levelId": 4}}).success(function (data) {
                $scope.locationLevel4Values = data;
            });
        } else if ((dataObject.field == 'Location Level5' || dataObject.field == 'Holdings Location Level5') && $scope.locationLevel5Values == undefined) {
            $http.get(OLENG_CONSTANTS.PROFILE_GET_LOCATIONS, {params: {"levelId": 5}}).success(function (data) {
                $scope.locationLevel5Values = data;
            });
        }
    } else if (dataObject.title == 'Data Transformations') {
        if (dataObject.dataTransformationDocType == 'Bibliographic' && dataObject.dataTransformationField == 'Bib Status' && (dataObject.dataTransformationAction == 'Add' || dataObject.dataTransformationAction == 'Update') && $scope.bibStatuses == undefined) {
            $http.get(OLENG_CONSTANTS.PROFILE_GET_BIB_STATUS).success(function (data) {
                $scope.bibStatuses = data;
            });
        } else if (dataObject.dataTransformationDocType == 'Item' && dataObject.dataTransformationField == 'Item Type' && (dataObject.dataTransformationAction == 'Add' || dataObject.dataTransformationAction == 'Update') && $scope.itemTypeValues == undefined) {
            $http.get(OLENG_CONSTANTS.PROFILE_GET_ITEM_TYPES).success(function (data) {
                $scope.itemTypeValues = data;
            });
        } else if (dataObject.dataTransformationDocType == 'Item' && dataObject.dataTransformationField == 'Item Status' && (dataObject.dataTransformationAction == 'Add' || dataObject.dataTransformationAction == 'Update') && $scope.itemStatusValues == undefined) {
            $http.get(OLENG_CONSTANTS.PROFILE_GET_ITEM_STATUS).success(function (data) {
                $scope.itemStatusValues = data;
            });
        }
    } else if (dataObject.title == 'Constants And Defaults') {
        dataObject.fieldValue = null;
        dataObject.constantsAndDefault = 'Constant';
        if (dataObject.fieldName == 'Chart Code'
            || dataObject.fieldName == 'Org Code'
            || dataObject.fieldName == 'Item Chart Code'
            || dataObject.fieldName == 'Contract Manager'
            || dataObject.fieldName == 'Order Type'
            || dataObject.fieldName == 'Funding Source'
            || dataObject.fieldName == 'Delivery Campus Code'
            || dataObject.fieldName == 'Building Code'
            || dataObject.fieldName == 'Building Room Number'
            || dataObject.fieldName == 'Vendor Choice'
            || dataObject.fieldName == 'Cost Source'
            || dataObject.fieldName == 'Default Location'
            || dataObject.fieldName == 'Vendor Number'
            || dataObject.fieldName == 'Vendor Alias Name'
            || dataObject.fieldName == 'Acquisition Unit\'s Vendor account / Vendor Info Customer #'
            || dataObject.fieldName == 'Requestor Name'
            || dataObject.fieldName == 'Item Status'
            || dataObject.fieldName == 'Account Number'
            || dataObject.fieldName == 'Object Code'
            || dataObject.fieldName == 'Request Source'
            || dataObject.fieldName == 'Assign To User'
            || dataObject.fieldName == 'Method Of PO Transmission'
            || dataObject.fieldName == 'Recurring Payment Type'
            || dataObject.fieldName == 'Fund Code') {
            $http.get(OLENG_CONSTANTS.PROFILE_GET_ORDER_FIELD_VALUES, {params: {"fieldName": dataObject.fieldName}}).success(function (data) {
                $scope.fieldValues = data;
            });
        }
    }
};