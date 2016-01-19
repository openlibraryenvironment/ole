/**
 * Created by rajeshbabuk on 1/14/16.
 */

validateMatchPointRow = function(matchPointRow, $scope) {
    var error = false;
    if (matchPointRow.matchPointDocType == 'Bibliographic') {
        if (matchPointRow.dataField == undefined || matchPointRow.dataField == null) {
            $scope.batchProfileForm['matchPointDataField_0'].$dirty = true;
            $scope.batchProfileForm['matchPointDataField_0'].$invalid = true;
            error = true;
        } else if (matchPointRow.dataField == '001') {
            $scope.batchProfileForm['matchPointSubField_0'].$dirty = false;
            $scope.batchProfileForm['matchPointSubField_0'].$invalid = false;
            return error;
        }
        if (matchPointRow.subField == undefined || matchPointRow.subField == null) {
            $scope.batchProfileForm['matchPointSubField_0'].$dirty = true;
            $scope.batchProfileForm['matchPointSubField_0'].$invalid = true;
            error = true;
        }
    }
    return error;
};

validateDataMappingRow = function(dataMappingRow, $scope) {
    var error = false;
    if (dataMappingRow.dataMappingDocType == 'Bib Marc') {
        if (dataMappingRow.dataField == undefined || dataMappingRow.dataField == null) {
            $scope.batchProfileForm['dataMappingDataField_0'].$dirty = true;
            $scope.batchProfileForm['dataMappingDataField_0'].$invalid = true;
            error = true;
        } else if (dataMappingRow.dataField == '001') {
            $scope.batchProfileForm['dataMappingSubField_0'].$dirty = false;
            $scope.batchProfileForm['dataMappingSubField_0'].$invalid = false;
            return error;
        }
        if (dataMappingRow.subField == undefined || dataMappingRow.subField == null) {
            $scope.batchProfileForm['dataMappingSubField_0'].$dirty = true;
            $scope.batchProfileForm['dataMappingSubField_0'].$invalid = true;
            error = true;
        }
        if ($scope.mainSectionPanel.batchProcessType == 'Bib Import') {
            if (dataMappingRow.destination == undefined || dataMappingRow.destination == null) {
                $scope.batchProfileForm['dataMappingDestination_0'].$dirty = true;
                $scope.batchProfileForm['dataMappingDestination_0'].$invalid = true;
                error = true;
            }
        }
        if (dataMappingRow.field == undefined || dataMappingRow.field == null) {
            $scope.batchProfileForm['dataMappingField_0'].$dirty = true;
            $scope.batchProfileForm['dataMappingField_0'].$invalid = true;
            error = true;
        }
    } else if (dataMappingRow.dataMappingDocType == 'Constant') {
        if ($scope.mainSectionPanel.batchProcessType == 'Bib Import') {
            if (dataMappingRow.destination == undefined || dataMappingRow.destination == null) {
                $scope.batchProfileForm['dataMappingDestination_0'].$dirty = true;
                $scope.batchProfileForm['dataMappingDestination_0'].$invalid = true;
                error = true;
            }
        }
        if (dataMappingRow.field == undefined || dataMappingRow.field == null) {
            $scope.batchProfileForm['dataMappingField_0'].$dirty = true;
            $scope.batchProfileForm['dataMappingField_0'].$invalid = true;
            error = true;
        }
        if (dataMappingRow.constant == undefined || dataMappingRow.constant == null) {
            $scope.batchProfileForm['dataMappingConstant_0'].$dirty = true;
            $scope.batchProfileForm['dataMappingConstant_0'].$invalid = true;
            error = true;
        }
    }
    return error;
};

validateFieldOperationRow = function (fieldOperationRow, $scope) {
    var error = false;
    if (fieldOperationRow.dataField == undefined || fieldOperationRow.dataField == null) {
        $scope.batchProfileForm['fieldOperationDataField_0'].$dirty = true;
        $scope.batchProfileForm['fieldOperationDataField_0'].$invalid = true;
        error = true;
    }
    if (fieldOperationRow.subField == undefined || fieldOperationRow.subField == null) {
        $scope.batchProfileForm['fieldOperationSubField_0'].$dirty = true;
        $scope.batchProfileForm['fieldOperationSubField_0'].$invalid = true;
        error = true;
    }
    return error;
};

makeMatchPointValid = function($scope) {
    $scope.batchProfileForm['matchPointDataField_0'].$dirty = false;
    $scope.batchProfileForm['matchPointDataField_0'].$invalid = false;
    $scope.batchProfileForm['matchPointSubField_0'].$dirty = false;
    $scope.batchProfileForm['matchPointSubField_0'].$invalid = false;
};

makeDataMappingValid = function($scope) {
    $scope.batchProfileForm['dataMappingDataField_0'].$dirty = false;
    $scope.batchProfileForm['dataMappingDataField_0'].$invalid = false;
    $scope.batchProfileForm['dataMappingSubField_0'].$dirty = false;
    $scope.batchProfileForm['dataMappingSubField_0'].$invalid = false;
    $scope.batchProfileForm['dataMappingDestination_0'].$dirty = false;
    $scope.batchProfileForm['dataMappingDestination_0'].$invalid = false;
    $scope.batchProfileForm['dataMappingField_0'].$dirty = false;
    $scope.batchProfileForm['dataMappingField_0'].$invalid = false;
    $scope.batchProfileForm['dataMappingConstant_0'].$dirty = false;
    $scope.batchProfileForm['dataMappingConstant_0'].$invalid = false;
};