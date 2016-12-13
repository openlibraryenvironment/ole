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

isValidLocalDataMappingRow = function(localDataMappingRow, index, $scope) {
    var isValid = true;
    if (isFieldEmpty(localDataMappingRow.source)) {
        $scope.batchProfileForm['localDataMappingSourceField_' + index].$dirty = true;
        $scope.batchProfileForm['localDataMappingSourceField_' + index].$invalid = true;
        isValid = false;
    }
    if (isFieldEmpty(localDataMappingRow.destination)) {
        $scope.batchProfileForm['localDataMappingDestinationField_' + index].$dirty = true;
        $scope.batchProfileForm['localDataMappingDestinationField_' + index].$invalid = true;
        isValid = false;
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

    if (isFieldEmpty(dataTransformationRow.dataTransformationOperation)) {
        $scope.batchProfileForm['dataTransformationOperation_' + index].$dirty = true;
        $scope.batchProfileForm['dataTransformationOperation_' + index].$invalid = true;
        return false;
    }
    if (isFieldEmpty(dataTransformationRow.dataField)) {
        $scope.batchProfileForm['dataTransformationSourceDataField_' + index].$dirty = true;
        $scope.batchProfileForm['dataTransformationSourceDataField_' + index].$invalid = true;
        isValid = false;
    }
    if (isFieldEmpty(dataTransformationRow.destDataField)) {
        if (dataTransformationRow.dataTransformationOperation == 'Add Field' ||
            dataTransformationRow.dataTransformationOperation == 'Add Subfield' ||
            dataTransformationRow.dataTransformationOperation == 'Copy and Paste' ||
            dataTransformationRow.dataTransformationOperation == 'Cut and Paste' ||
            dataTransformationRow.dataTransformationOperation == 'Prepend with Prefix') {
            $scope.batchProfileForm['dataTransformationDestDataField_' + index].$dirty = true;
            $scope.batchProfileForm['dataTransformationDestDataField_' + index].$invalid = true;
            isValid = false;
        }
    }
    if (isFieldEmpty(dataTransformationRow.dataTransformationConstant)) {
        if (dataTransformationRow.dataTransformationOperation == 'Remove Value') {
            $scope.batchProfileForm['dataTransformationConstant_' + index].$dirty = true;
            $scope.batchProfileForm['dataTransformationConstant_' + index].$invalid = true;
            isValid = false;
        }
    }

    if (isFieldEmpty(dataTransformationRow.destSubField) && dataTransformationRow.dataTransformationOperation == 'Add Subfield') {
        $scope.batchProfileForm['dataTransformationDestSubField_' + index].$dirty = true;
        $scope.batchProfileForm['dataTransformationDestSubField_' + index].$invalid = true;
        isValid = false;
    }

    if (isFieldEmpty(dataTransformationRow.subField) && dataTransformationRow.dataTransformationOperation == 'Delete SubField') {
        $scope.batchProfileForm['dataTransformationSourceSubField_' + index].$dirty = true;
        $scope.batchProfileForm['dataTransformationSourceSubField_' + index].$invalid = true;
        isValid = false;
    }
    console.log(isValid);
    return isValid;
};

isValidFilterCriteriaRow = function(filterCriteriaRow, index, $scope) {
    var isValid = true;

    if(isFieldEmpty(filterCriteriaRow.filterFieldName) && isFieldEmpty(filterCriteriaRow.filterFieldNameText)) {
        $scope.batchProfileForm['filterFieldName_' + index].$dirty = true;
        $scope.batchProfileForm['filterFieldName_' + index].$invalid = true;
        isValid = false;
    }

    if(!(isFieldEmpty(filterCriteriaRow.filterFieldNameText))){
        var fieldTextName = filterCriteriaRow.filterFieldNameText;
        var regrex = /(^\d\d\d\s+([$][a-z])+$)|(^\d\d\d+([$][a-z])+$)/;
        var match = fieldTextName.match(regrex);
        if(!match){
            $scope.batchProfileForm['filterFieldNameText_' + index].$dirty = true;
            $scope.batchProfileForm['filterFieldNameText_' + index].$invalid = true;
            isValid = false;
        }
    }
    return isValid;
};

makeFilterCriteriaValid = function($scope) {
    $scope.batchProfileForm['filterFieldName_0'].$dirty = true;
    $scope.batchProfileForm['filterFieldName_0'].$invalid = true;
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
    $scope.batchProfileForm['dataTransformationSourceDataField_0'].$dirty = false;
    $scope.batchProfileForm['dataTransformationSourceDataField_0'].$invalid = false;
    $scope.batchProfileForm['dataTransformationOperation_0'].$dirty = false;
    $scope.batchProfileForm['dataTransformationOperation_0'].$invalid = false;
    $scope.batchProfileForm['dataTransformationDestDataField_0'].$dirty = false;
    $scope.batchProfileForm['dataTransformationDestDataField_0'].$invalid = false;
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