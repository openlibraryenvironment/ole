/**
 * Created by rajeshbabuk on 1/14/16.
 */

isValidMatchPointRow = function(matchPointRow, index, $scope) {
    var isValid = true;
    if (matchPointRow.matchPointDocType == 'Bibliographic') {
        if (isFieldEmpty(matchPointRow.dataField)) {
            $scope.batchProfileForm['matchPointDataField_' + index].$dirty = true;
            $scope.batchProfileForm['matchPointDataField_' + index].$invalid = true;
            isValid = false;
        } else if (matchPointRow.dataField == '001') {
            $scope.batchProfileForm['matchPointSubField_' + index].$dirty = false;
            $scope.batchProfileForm['matchPointSubField_' + index].$invalid = false;
            return isValid;
        }
        if (isFieldEmpty(matchPointRow.subField)) {
            $scope.batchProfileForm['matchPointSubField_' + index].$dirty = true;
            $scope.batchProfileForm['matchPointSubField_' + index].$invalid = true;
            isValid = false;
        }
    }
    return isValid;
};

isValidDataMappingRow = function(dataMappingRow, index, $scope) {
    var isValid = true;
    if (dataMappingRow.dataMappingDocType == 'Bib Marc') {
        if (isFieldEmpty(dataMappingRow.dataField)) {
            $scope.batchProfileForm['dataMappingDataField_' + index].$dirty = true;
            $scope.batchProfileForm['dataMappingDataField_' + index].$invalid = true;
            isValid = false;
        } else if (dataMappingRow.dataField == '001') {
            $scope.batchProfileForm['dataMappingSubField_' + index].$dirty = false;
            $scope.batchProfileForm['dataMappingSubField_' + index].$invalid = false;
        }
        if (isFieldEmpty(dataMappingRow.subField) && dataMappingRow.dataField != '001') {
            $scope.batchProfileForm['dataMappingSubField_' + index].$dirty = true;
            $scope.batchProfileForm['dataMappingSubField_' + index].$invalid = true;
            isValid = false;
        }
        if ($scope.mainSectionPanel.batchProcessType == 'Bib Import') {
            if (isFieldEmpty(dataMappingRow.destination)) {
                $scope.batchProfileForm['dataMappingDestination_' + index].$dirty = true;
                $scope.batchProfileForm['dataMappingDestination_' + index].$invalid = true;
                isValid = false;
            }
        }
        if (isFieldEmpty(dataMappingRow.field)) {
            $scope.batchProfileForm['dataMappingField_' + index].$dirty = true;
            $scope.batchProfileForm['dataMappingField_' + index].$invalid = true;
            isValid = false;
        }
    } else if (dataMappingRow.dataMappingDocType == 'Constant') {
        if ($scope.mainSectionPanel.batchProcessType == 'Bib Import') {
            if (isFieldEmpty(dataMappingRow.destination)) {
                $scope.batchProfileForm['dataMappingDestination_' + index].$dirty = true;
                $scope.batchProfileForm['dataMappingDestination_' + index].$invalid = true;
                isValid = false;
            }
        }
        if (isFieldEmpty(dataMappingRow.field)) {
            $scope.batchProfileForm['dataMappingField_' + index].$dirty = true;
            $scope.batchProfileForm['dataMappingField_' + index].$invalid = true;
            isValid = false;
        }
        if (isFieldEmpty(dataMappingRow.constant)) {
            $scope.batchProfileForm['dataMappingConstant_' + index].$dirty = true;
            $scope.batchProfileForm['dataMappingConstant_' + index].$invalid = true;
            isValid = false;
        }
    }
    return isValid;
};

isValidFieldOperationRow = function (fieldOperationRow, index, $scope) {
    var isValid = true;
    if (isFieldEmpty(fieldOperationRow.dataField)) {
        $scope.batchProfileForm['fieldOperationDataField_' + index].$dirty = true;
        $scope.batchProfileForm['fieldOperationDataField_' + index].$invalid = true;
        isValid = false;
    }
    return isValid;
};

isValidDataTransformationRow = function (dataTransformationRow, index, $scope) {
    var isValid = true;
    if (isFieldEmpty(dataTransformationRow.dataTransformationSourceField)) {
        $scope.batchProfileForm['dataTransformationSourceField_' + index].$dirty = true;
        $scope.batchProfileForm['dataTransformationSourceField_' + index].$invalid = true;
        isValid = false;
    }
    if (isFieldEmpty(dataTransformationRow.dataTransformationOperation)) {
        $scope.batchProfileForm['dataTransformationOperation_' + index].$dirty = true;
        $scope.batchProfileForm['dataTransformationOperation_' + index].$invalid = true;
        isValid = false;
    }
    if (isFieldEmpty(dataTransformationRow.dataTransformationDestinationField)) {
        if (dataTransformationRow.dataTransformationOperation == 'Move' || dataTransformationRow.dataTransformationOperation == 'Prepend with Prefix' || dataTransformationRow.dataTransformationOperation == 'Replace') {
            $scope.batchProfileForm['dataTransformationDestinationField_' + index].$dirty = true;
            $scope.batchProfileForm['dataTransformationDestinationField_' + index].$invalid = true;
            isValid = false;
        }
    }
    if (isFieldEmpty(dataTransformationRow.dataTransformationConstant)) {
        if (dataTransformationRow.dataTransformationOperation == 'Delete Value' || dataTransformationRow.dataTransformationOperation == 'New' || dataTransformationRow.dataTransformationOperation == 'Replace') {
            $scope.batchProfileForm['dataTransformationConstant_' + index].$dirty = true;
            $scope.batchProfileForm['dataTransformationConstant_' + index].$invalid = true;
            isValid = false;
        }
    }
    return isValid;
};

makeMatchPointValid = function ($scope) {
    $scope.batchProfileForm['matchPointDataField_0'].$dirty = false;
    $scope.batchProfileForm['matchPointDataField_0'].$invalid = false;
    $scope.batchProfileForm['matchPointSubField_0'].$dirty = false;
    $scope.batchProfileForm['matchPointSubField_0'].$invalid = false;
};

makeFieldOperationValid = function ($scope) {
    $scope.batchProfileForm['fieldOperationDataField_0'].$dirty = false;
    $scope.batchProfileForm['fieldOperationSubField_0'].$dirty = false;
};

makeDataMappingValid = function ($scope) {
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

makeDataTransformationValid = function ($scope) {
    $scope.batchProfileForm['dataTransformationSourceField_0'].$dirty = false;
    $scope.batchProfileForm['dataTransformationSourceField_0'].$invalid = false;
    $scope.batchProfileForm['dataTransformationOperation_0'].$dirty = false;
    $scope.batchProfileForm['dataTransformationOperation_0'].$invalid = false;
    $scope.batchProfileForm['dataTransformationDestinationField_0'].$dirty = false;
    $scope.batchProfileForm['dataTransformationDestinationField_0'].$invalid = false;
    $scope.batchProfileForm['dataTransformationConstant_0'].$dirty = false;
    $scope.batchProfileForm['dataTransformationConstant_0'].$invalid = false;
};

clearAllValidations = function ($scope) {
    makeMatchPointValid($scope);
    makeFieldOperationValid($scope);
    makeDataMappingValid($scope);
    makeDataTransformationValid($scope);
};

isFieldEmpty = function (field) {
    if (field == undefined || field == null || field == '') {
        return true;
    }
    return false;
};