var batchProfileApp = angular.module('batchProcessProfile', ['ngAnimate', 'ngSanitize', 'mgcrea.ngStrap', 'ui.bootstrap', 'ui.date']);

batchProfileApp.controller('batchProfileController', ['$scope', '$http', function ($scope, $http) {
    $scope.booleanOptions = booleanOptions;
    $scope.submitted = false;
    $scope.rowToEdit = null;
    $scope.mainSectionActivePanel = [0];
    $scope.matchPointsActivePanel = [];
    $scope.addOrOverlayActivePanel = [];
    $scope.fieldOperationsActivePanel = [];
    $scope.dataMappingsActivePanel = [];
    $scope.localDataMappingsActivePanel = [];
    $scope.dataTransformationsActivePanel = [];
    $scope.filterCriteriaActivePanel = [];
    $scope.localDataMappingsPanel = [];
    $scope.mainSectionPanel = mainSection;

    $scope.setValuesForBatchProcessType = function (mainSectionPanel) {
        if (mainSectionPanel.batchProcessType == 'Bib Import') {
            $scope.matchPointsPanel = [matchPoint];
            $scope.addOrOverlayPanel = [addOrOverlay];
            $scope.dataMappingsPanel = [dataMapping];
            $scope.dataTransformationsPanel = [dataTransformation];
            doGetRequest($scope, $http, OLENG_CONSTANTS.PROFILE_GET_GLOBALLY_PROTECTED_FIELDS, null, function (response) {
                var data = response.data;
                $scope.fieldOperationsPanel = data;
                $scope.fieldOperationsPanel.unshift(fieldOperation);
            });
            $scope.mainSectionPanel.bibImportProfileForOrderImport = null;
            $scope.matchPointsActivePanel = [];
            $scope.addOrOverlayActivePanel = [];
            $scope.fieldOperationsActivePanel = [];
            $scope.dataMappingsActivePanel = [];
            $scope.dataTransformationsActivePanel = [];
            $scope.mainSectionPanel.requisitionForTitlesOption = null;
            $scope.mainSectionPanel.matchPointToUse = null;
            $scope.mainSectionPanel.exportScope = null;
            $scope.mainSectionPanel.marcOnly = false;
            $scope.matchPointsPanel[0].matchPointType = null;
            $scope.matchPointsPanel[0].matchPointDocType = 'Bibliographic';
            $scope.dataMappingsPanel[0].dataField = null;
            $scope.dataMappingsPanel[0].ind1 = null;
            $scope.dataMappingsPanel[0].ind2 = null;
            $scope.dataMappingsPanel[0].subField = null;
            $scope.dataMappingsPanel[0].constant = null;
            $scope.dataMappingsPanel[0].destination = null;
            $scope.dataMappingsPanel[0].field = null;
            $scope.dataMappingsPanel[0].isMultiValue = false;
            $scope.dataMappingsPanel[0].priority = 1;
            $scope.dataMappingsPanel[0].dataMappingFields = null;
            $scope.constantValues = null;
        } else if (mainSectionPanel.batchProcessType == 'Order Record Import') {
            $scope.mainSectionPanel.bibImportProfileForOrderImport = null;
            getBibImportProfileNames($scope, $http);
            $scope.matchPointsPanel = [matchPoint];
            $scope.addOrOverlayPanel = [addOrOverlay];
            $scope.dataMappingsPanel = [dataMapping];
            $scope.matchPointsPanel[0].matchPointTypes = orderFields;
            $scope.dataMappingsPanel[0].dataMappingFields = orderFields;
            $scope.localDataMappingsPanel = [localDataMapping];
            $scope.localDataMappingsPanel[0].destinations = orderFields;
            $scope.mainSectionPanel.requisitionForTitlesOption = 'One Requisition Per Title';
            $scope.mainSectionPanel.orderType = "Holdings and Item";
            $scope.mainSectionPanel.matchPointToUse = "Order Import";
            clearProfileValues();
        } else if (mainSectionPanel.batchProcessType == 'Invoice Import') {
            $scope.mainSectionPanel.bibImportProfileForOrderImport = null;
            getBibImportProfileNames($scope, $http);
            $scope.addOrOverlayPanel = [];
            $scope.matchPointsPanel = [matchPoint];
            $scope.dataMappingsPanel = [dataMapping];
            $scope.matchPointsPanel[0].matchPointTypes = invoiceFieldObject.matchPoint;
            $scope.dataMappingsPanel[0].dataMappingFields = invoiceFieldObject.dataMapping;
            clearProfileValues();
        } else if (mainSectionPanel.batchProcessType == 'Batch Export') {
            $scope.dataMappingsPanel = [dataMappingBatchExport];
            $scope.dataTransformationsPanel = [batchExpDataTransformation];
            $scope.filterCriteriaPanel = [filterCriteria];
            $scope.dataMappingsActivePanel = [];
            $scope.dataTransformationsActivePanel = [];
            $scope.dataMappingsPanel[0].dataField = null;
            $scope.dataMappingsPanel[0].ind1 = null;
            $scope.dataMappingsPanel[0].ind2 = null;
            $scope.dataMappingsPanel[0].subField = null;
            $scope.dataMappingsPanel[0].constant = null;
            $scope.dataMappingsPanel[0].destination = null;
            $scope.dataMappingsPanel[0].field = null;
            $scope.dataMappingsPanel[0].isMultiValue = false;
            $scope.dataMappingsPanel[0].priority = 1;
            $scope.dataMappingsPanel[0].dataMappingFields = null;
            $scope.mainSectionPanel.exportScope = "Full";
        } else if (mainSectionPanel.batchProcessType == 'Batch Delete') {
            $scope.matchPointsPanel = [matchPoint];
            $scope.matchPointsPanel[0].matchPointDocType = 'Bibliographic';
        }
    };

    function clearProfileValues() {
        makeMatchPointValid($scope);
        makeDataMappingValid($scope);
        $scope.fieldOperationsPanel = [];
        $scope.dataTransformationsPanel = [];
        $scope.matchPointsActivePanel = [];
        $scope.addOrOverlayActivePanel = [];
        $scope.dataMappingsActivePanel = [];
        $scope.localDataMappingsActivePanel = [];
        $scope.matchPointsPanel[0].matchPointType = null;
        $scope.matchPointsPanel[0].matchPointDocType = 'Bib Marc';
        $scope.dataMappingsPanel[0].dataMappingDocType = 'Bib Marc';
        $scope.dataMappingsPanel[0].dataField = null;
        $scope.dataMappingsPanel[0].ind1 = null;
        $scope.dataMappingsPanel[0].ind2 = null;
        $scope.dataMappingsPanel[0].subField = null;
        $scope.dataMappingsPanel[0].constant = null;
        $scope.dataMappingsPanel[0].destination = null;
        $scope.dataMappingsPanel[0].field = null;
        $scope.dataMappingsPanel[0].priority = 1;
        $scope.dataMappingsPanel[0].isMultiValue = false;
        $scope.dataMappingsPanel.collapsed = false;
        $scope.matchPointsPanel.collapsed = false;
        $scope.addOrOverlayPanel.collapsed = false;
        $scope.constantValues = null;
    }

    $scope.matchPointAdd = function () {
        $scope.matchPointIndex = 0;
        var matchPointRow = getMatchPointRowByIndex(0);
        if (!isValidMatchPointRow(matchPointRow, 0, $scope)) {
            return;
        }
        $scope.matchPointsPanel.push(matchPointRow);
        if ($scope.mainSectionPanel.batchProcessType == 'Batch Delete') {
            $scope.matchPointsPanel[0].matchPointDocType = 'Bibliographic';
        } else {
            $scope.matchPointsPanel[0].matchPointDocType = null;
        }
        $scope.matchPointsPanel[0].matchPointType = null;
        $scope.matchPointsPanel[0].matchPointValue = null;
        $scope.matchPointsPanel[0].dataField = null;
        $scope.matchPointsPanel[0].ind1 = null;
        $scope.matchPointsPanel[0].ind2 = null;
        $scope.matchPointsPanel[0].subField = null;
        $scope.matchPointsPanel[0].destDataField = null;
        $scope.matchPointsPanel[0].destInd1 = null;
        $scope.matchPointsPanel[0].destInd2 = null;
        $scope.matchPointsPanel[0].destSubField = null;
        $scope.matchPointsPanel[0].isMultiValue = false;
        $scope.matchPointsPanel[0].constant = null;
        $scope.matchPointsPanel[0].matchPointTypes = null;
        removeOptions('Match Points', 0);

    };

    $scope.matchPointCopyRow = function (index) {
        var copiedRow = getMatchPointRowByIndex(index);
        $scope.matchPointsPanel.splice(index + 1, 0, copiedRow);
    };

    $scope.matchPointEditRow = function(index) {
        if ($scope.rowToEdit === null || $scope.rowToEdit === undefined) {
            $scope.rowToEdit = getMatchPointRowByIndex(index);
            $scope.matchPointsPanel[index].isEdit = true;
            $scope.matchPointsPanel[index].matchPointDocTypes = documentTypes;
            $scope.matchPointsPanel[index].matchPointOrderDocTypes = dataMappingProcessTypes;
            $scope.matchPointsPanel[index].matchPointTypes = getMatchPointType($scope.matchPointsPanel[index].matchPointDocType);
            $scope.matchPointsPanel[index].isAddLine = false;
            $scope.matchPointsPanel[index].title = 'Match Points';
            $scope.populateDestinationFieldValues(index, $scope.matchPointsPanel[index], $scope.matchPointsPanel[index].matchPointDocType, $scope.matchPointsPanel[index].matchPointType);
        }
    };

    function getMatchPointType(type) {
        if (type == 'Holdings') {
            return matchPointObject.matchPointTypeForHoldings;
        } else if (type == 'Item') {
            return matchPointObject.matchPointTypeForItem;
        } else if (type == 'EHoldings') {
            return matchPointObject.matchPointTypeForEHoldings;
        } else if ((type == 'Bib Marc' || type == 'Constant') && $scope.mainSectionPanel.batchProcessType == 'Order Record Import') {
            return orderFields;
        } else if ((type == 'Bib Marc' || type == 'Constant') && $scope.mainSectionPanel.batchProcessType == 'Invoice Import') {
            return invoiceFieldObject.matchPoint;
        }
        return "[]";
    }

    $scope.matchPointUpdateRow = function(index) {
        $scope.matchPointIndex = index;
        var updatedRow = getMatchPointRowByIndex(index);
        if (!isValidMatchPointRow(updatedRow, index, $scope)) {
            return;
        }
        $scope.matchPointsPanel[index] = updatedRow;
        $scope.matchPointsPanel[index].isEdit = false;
        $scope.rowToEdit = null;
        removeOptions('Match Points', index);
    };

    $scope.matchPointCancelUpdate = function(index) {
        $scope.matchPointsPanel[index].isEdit = false;
        $scope.matchPointsPanel[index] = $scope.rowToEdit;
        $scope.matchPointsPanel[index].isAddLine = true;
        $scope.rowToEdit = null;
    };

    $scope.matchPointRemove = function (matchPoint) {
        var index = $scope.matchPointsPanel.indexOf(matchPoint);
        $scope.matchPointsPanel.splice(index, 1);
    };

    $scope.addOrOverlayAdd = function () {
        var addOrOverlayRow = getAddOrOverlayRowByIndex(0);
        $scope.addOrOverlayPanel.push(addOrOverlayRow);
        $scope.addOrOverlayPanel[0].matchOption = 'If Match Found';
        $scope.addOrOverlayPanel[0].addOrOverlayDocType = 'Bibliographic';
        $scope.addOrOverlayPanel[0].operation = '';
        $scope.addOrOverlayPanel[0].addOrOverlayField = null;
        $scope.addOrOverlayPanel[0].addOrOverlayFieldOperation = null;
        $scope.addOrOverlayPanel[0].addOrOverlayFieldValue = null;
        $scope.addOrOverlayPanel[0].dataField = null;
        $scope.addOrOverlayPanel[0].ind1 = null;
        $scope.addOrOverlayPanel[0].ind2 = null;
        $scope.addOrOverlayPanel[0].subField = null;
        $scope.addOrOverlayPanel[0].linkField = null;
        $scope.addOrOverlayPanel[0].operations = bibMatchOperations;
        $scope.addOrOverlayPanel[0].operation = "Add";
        $scope.resetAddOrOverlayFieldDropdownSize(0);
        removeOptions('Matching, Add and Overlay', 0);
    };

    $scope.addOrOverlayCopyRow = function (index) {
        var copiedRow = getAddOrOverlayRowByIndex(index);
        $scope.addOrOverlayPanel.splice(index + 1, 0, copiedRow);
    };

    $scope.addOrOverlayEditRow = function(index) {
        if ($scope.rowToEdit === null || $scope.rowToEdit === undefined) {
            $scope.rowToEdit = getAddOrOverlayRowByIndex(index);
            $scope.addOrOverlayPanel[index].isEdit = true;
            $scope.addOrOverlayPanel[index].matchOptions = matchOptions;
            $scope.addOrOverlayPanel[index].addOrOverlayDocTypes = addOrOverlayDocumentTypes;
            $scope.addOrOverlayPanel[index].linkFields = dataMappingObject.destinationFieldsForBibMarcHoldings;
            $scope.addOrOverlayPanel[index].operations = operations;
            $scope.addOrOverlayPanel[index].bibDoNotMatchOperations = bibDoNotMatchOperations;
            $scope.addOrOverlayPanel[index].doNotMatchOperations = doNotMatchOperations;
            $scope.addOrOverlayPanel[index].addOperations = addOperations;
            $scope.addOrOverlayPanel[index].matchedOrderOperations = matchedOrderOperations;
            $scope.addOrOverlayPanel[index].unmatchedOrderOperations = unmatchedOrderOperations;
            $scope.addOrOverlayPanel[index].addOrOverlayFields = addOrOverlayFields;
            $scope.addOrOverlayPanel[index].addOrOverlayFieldOperations = addOrOverlayFieldOperations;
            $scope.addOrOverlayPanel[index].title = 'Matching, Add and Overlay';
            $scope.populateDestinationFieldValues(index, $scope.addOrOverlayPanel[index], $scope.addOrOverlayPanel[index].addOrOverlayDocType, $scope.addOrOverlayPanel[index].addOrOverlayField);
            var addOperationWithMultipleOptions = getAddOperationWithMultipleOptions($scope.mainSectionPanel.batchProcessType,$scope.addOrOverlayPanel[index]);
            $scope.addOrOverlayPanel[index].addOperationsWithMultiple = addOperationWithMultipleOptions;
            $scope.addOrOverlayPanel[index].isAddLine = false;
        }
    };

    $scope.addOrOverlayUpdateRow = function(index) {
        var updatedRow = getAddOrOverlayRowByIndex(index);
        $scope.addOrOverlayPanel[index] = updatedRow;
        $scope.addOrOverlayPanel[index].isEdit = false;
        $scope.rowToEdit = null;
        $scope.resetAddOrOverlayFieldDropdownSize(index);
        removeOptions('Matching, Add and Overlay', index);
    };

    $scope.addOrOverlayCancelUpdate = function(index) {
        $scope.addOrOverlayPanel[index].isEdit = false;
        $scope.addOrOverlayPanel[index] = $scope.rowToEdit;
        $scope.addOrOverlayPanel[index].isAddLine = true;
        $scope.rowToEdit = null;
        $scope.resetAddOrOverlayFieldDropdownSize(index);
    };

    $scope.addOrOverlayRemove = function (addOrOverlay) {
        var index = $scope.addOrOverlayPanel.indexOf(addOrOverlay);
        $scope.addOrOverlayPanel.splice(index, 1);
    };

    $scope.fieldOperationAdd = function () {
        $scope.fieldOperationIndex = 0;
        var fieldOperationRow = getFieldOperationRowByIndex(0);
        if (!isValidFieldOperationRow(fieldOperationRow, 0, $scope)) {
            return;
        }
        $scope.fieldOperationsPanel.push(fieldOperationRow);
        $scope.fieldOperationsPanel[0].fieldOperationType = 'Profile Protected Field';
        $scope.fieldOperationsPanel[0].dataField = null;
        $scope.fieldOperationsPanel[0].ind1 = null;
        $scope.fieldOperationsPanel[0].ind2 = null;
        $scope.fieldOperationsPanel[0].subField = null;
        $scope.fieldOperationsPanel[0].value = null;
        $scope.fieldOperationsPanel[0].ignoreGPF = false;
    };

    $scope.fieldOperationCopyRow = function (index) {
        var copiedRow = getFieldOperationRowByIndex(index);
        $scope.fieldOperationsPanel.splice(index + 1, 0, copiedRow);
    };

    $scope.fieldOperationEditRow = function(index) {
        if ($scope.rowToEdit === null || $scope.rowToEdit === undefined) {
            $scope.rowToEdit = getFieldOperationRowByIndex(index);
            $scope.fieldOperationsPanel[index].isEdit = true;
            $scope.fieldOperationsPanel[index].fieldOperationTypes = fieldOperations;
            $scope.fieldOperationsPanel[index].fieldOperationType = 'Profile Protected Field';
            $scope.fieldOperationsPanel[index].ignoreGPF = false;
            $scope.fieldOperationsPanel[index].isAddLine = false;
        }
    };

    $scope.fieldOperationUpdateRow = function(index) {
        $scope.fieldOperationIndex = index;
        var updatedRow = getFieldOperationRowByIndex(index);
        if (!isValidFieldOperationRow(updatedRow, index, $scope)) {
            return;
        }
        $scope.fieldOperationsPanel[index] = updatedRow;
        $scope.fieldOperationsPanel[index].isEdit = false;
        $scope.rowToEdit = null;
    };

    $scope.fieldOperationCancelUpdate = function(index) {
        $scope.fieldOperationsPanel[index].isEdit = false;
        $scope.fieldOperationsPanel[index] = $scope.rowToEdit;
        $scope.fieldOperationsPanel[index].isAddLine = true;
        $scope.rowToEdit = null;
    };

    $scope.fieldOperationRemove = function (fieldOperation) {
        var index = $scope.fieldOperationsPanel.indexOf(fieldOperation);
        $scope.fieldOperationsPanel.splice(index, 1);
    };

    $scope.dataMappingAdd = function () {
        $scope.dataMappingIndex = 0;
        var dataMappingRow = getDataMappingRowByIndex(0);
        if (!isValidDataMappingRow(dataMappingRow, 0, $scope)) {
            return;
        }
        $scope.dataMappingsPanel.push(dataMappingRow);
        $scope.dataMappingsPanel[0].dataMappingDocType = 'Bib Marc';
        $scope.dataMappingsPanel[0].dataField = null;
        $scope.dataMappingsPanel[0].ind1 = null;
        $scope.dataMappingsPanel[0].ind2 = null;
        $scope.dataMappingsPanel[0].subField = null;
        $scope.dataMappingsPanel[0].constant = null;
        $scope.dataMappingsPanel[0].destination = null;
        $scope.dataMappingsPanel[0].field = null;
        $scope.dataMappingsPanel[0].transferOption = 'Pre Marc Transformation';
        $scope.dataMappingsPanel[0].isMultiValue = false;
        $scope.dataMappingsPanel[0].priority = 1;
        removeOptions('Data Mappings', 0);
    };

    $scope.dataMappingCopyRow = function (index) {
        var copiedRow = getDataMappingRowByIndex(index);
        $scope.dataMappingsPanel.splice(index + 1, 0, copiedRow);
    };

    $scope.dataMappingEditRow = function(index) {
        if ($scope.rowToEdit === null || $scope.rowToEdit === undefined) {
            $scope.rowToEdit = getDataMappingRowByIndex(index);
            $scope.dataMappingsPanel[index].isEdit = true;
            $scope.dataMappingsPanel[index].dataMappingDocTypes = dataMappingProcessTypes;
            $scope.dataMappingsPanel[index].destinations = populateDestinationForDataMappingToEdit($scope.dataMappingsPanel[index].dataMappingDocType);
            $scope.dataMappingsPanel[index].transferOptions = transferOptions;
            $scope.dataMappingsPanel[index].title = 'Data Mappings';
            if ($scope.mainSectionPanel.batchProcessType == 'Order Record Import') {
                $scope.dataMappingsPanel[index].dataMappingFields = orderFields;
            } else if ($scope.mainSectionPanel.batchProcessType == 'Invoice Import') {
                $scope.dataMappingsPanel[index].dataMappingFields = invoiceFieldObject.dataMapping;
            } else if ($scope.mainSectionPanel.batchProcessType == 'Bib Import') {
                $scope.populateDestinationFields($scope.dataMappingsPanel[index]);
            }
            $scope.populateDestinationFieldValues(index, $scope.dataMappingsPanel[index], $scope.dataMappingsPanel[index].dataMappingDocType, $scope.dataMappingsPanel[index].field);
            $scope.dataMappingsPanel[index].isAddLine = false;
        }
    };

    $scope.dataMappingUpdateRow = function(index) {
        $scope.dataMappingIndex = index;
        var updatedRow = getDataMappingRowByIndex(index);
        if (!isValidDataMappingRow(updatedRow, index, $scope)) {
            return;
        }
        $scope.dataMappingsPanel[index] = updatedRow;
        $scope.dataMappingsPanel[index].isEdit = false;
        $scope.rowToEdit = null;
        removeOptions('Data Mappings', index);
    };

    $scope.dataMappingCancelUpdate = function(index) {
        $scope.dataMappingsPanel[index].isEdit = false;
        $scope.dataMappingsPanel[index] = $scope.rowToEdit;
        $scope.dataMappingsPanel[index].isAddLine = true;
        $scope.rowToEdit = null;
    };

    $scope.dataMappingRemove = function (dataMapping) {
        var index = $scope.dataMappingsPanel.indexOf(dataMapping);
        $scope.dataMappingsPanel.splice(index, 1);
    };

    $scope.dataTransformationAdd = function () {
        $scope.dataTransformIndex = 0;
        var dataTransformationRow = getDataTransformationRowByIndex(0);
        if (!isValidDataTransformationRow(dataTransformationRow, 0, $scope)) {
            return;
        }
        $scope.dataTransformationsPanel.push(dataTransformationRow);
        $scope.dataTransformationsPanel[0].dataTransformationDocType = 'Bib Marc';
        $scope.dataTransformationsPanel[0].dataTransformationActionType = 'All';
        $scope.dataTransformationsPanel[0].dataField = null;
        $scope.dataTransformationsPanel[0].ind1 = null;
        $scope.dataTransformationsPanel[0].ind2 = null;
        $scope.dataTransformationsPanel[0].sufField = null;
        $scope.dataTransformationsPanel[0].dataTransformationOperation = null;
        $scope.dataTransformationsPanel[0].destDataField = null;
        $scope.dataTransformationsPanel[0].destInd1 = null;
        $scope.dataTransformationsPanel[0].destInd2 = null;
        $scope.dataTransformationsPanel[0].destSubField = null;
        $scope.dataTransformationsPanel[0].dataTransformationConstant = null;
        $scope.dataTransformationsPanel[0].dataTransformationStep = 1;
    };

    $scope.dataTransformationCopyRow = function (index) {
        var copiedRow = getDataTransformationRowByIndex(index);
        $scope.dataTransformationsPanel.splice(index + 1, 0, copiedRow);
    };

    $scope.dataTransformationEditRow = function(index) {
        if ($scope.rowToEdit === null || $scope.rowToEdit === undefined) {
            $scope.rowToEdit = getDataTransformationRowByIndex(index);
            $scope.dataTransformationsPanel[index].isEdit = true;
            $scope.dataTransformationsPanel[index].dataTransformationDocTypes = transformationDocumentTypes;
            $scope.dataTransformationsPanel[index].dataTransformationActionTypes = actionTypes;
            $scope.dataTransformationsPanel[index].dataTransformationOperations = transformationOperations;
            $scope.dataTransformationsPanel[index].isAddLine = false;
        }
    };

    $scope.dataTransformationUpdateRow = function(index) {
        $scope.dataTransformIndex = index;
        var updatedRow = getDataTransformationRowByIndex(index);
        if (!isValidDataTransformationRow(updatedRow, index, $scope)) {
            return;
        }
        $scope.dataTransformationsPanel[index] = updatedRow;
        $scope.dataTransformationsPanel[index].isEdit = false;
        $scope.rowToEdit = null;
    };
    $scope.dataTransformationCancelUpdate = function(index) {
        $scope.dataTransformationsPanel[index].isEdit = false;
        $scope.dataTransformationsPanel[index] = $scope.rowToEdit;
        $scope.dataTransformationsPanel[index].isAddLine = true;
        $scope.rowToEdit = null;
    };

    $scope.dataTransformationRemove = function (dataTransformation) {
        var index = $scope.dataTransformationsPanel.indexOf(dataTransformation);
        $scope.dataTransformationsPanel.splice(index, 1);
    };

    $scope.populateMatchPointTypes = function (matchPoint) {
        makeMatchPointValid($scope);
        matchPoint.matchPointType = null;
        matchPoint.dataField = null;
        matchPoint.ind1 = null;
        matchPoint.ind2 = null;
        matchPoint.subField = null;
        matchPoint.matchPointValue = null;
        matchPoint.destDataField = null;
        matchPoint.destInd1 = null;
        matchPoint.destInd2 = null;
        matchPoint.destSubField = null;
        matchPoint.isMultiValue = false;

        if(matchPoint.matchPointDocType == 'Holdings') {
            matchPoint.matchPointTypes = matchPointObject.matchPointTypeForHoldings;
        } else if(matchPoint.matchPointDocType == 'Item') {
            matchPoint.matchPointTypes = matchPointObject.matchPointTypeForItem;
        } else if(matchPoint.matchPointDocType == 'EHoldings') {
            matchPoint.matchPointTypes =  matchPointObject.matchPointTypeForEHoldings;
        } else if($scope.mainSectionPanel.batchProcessType == 'Order Record Import' && (matchPoint.matchPointDocType == 'Bib Marc' || matchPoint.matchPointDocType == 'Constant')) {
            matchPoint.matchPointTypes = orderFields;
        } else if($scope.mainSectionPanel.batchProcessType == 'Invoice Import' && (matchPoint.matchPointDocType == 'Bib Marc' || matchPoint.matchPointDocType == 'Constant')) {
            matchPoint.matchPointTypes = invoiceFieldObject.matchPoint;
        }

    };

    function getMatchPointRowByIndex(index) {
        var matchPointNewRow = {
            matchPointDocType: $scope.matchPointsPanel[index].matchPointDocType,
            matchPointType: $scope.matchPointsPanel[index].matchPointType,
            matchPointValue: $scope.matchPointsPanel[index].matchPointValue,
            dataField: $scope.matchPointsPanel[index].dataField,
            ind1: $scope.matchPointsPanel[index].ind1,
            ind2: $scope.matchPointsPanel[index].ind2,
            subField: $scope.matchPointsPanel[index].subField,
            destDataField: $scope.matchPointsPanel[index].destDataField,
            destInd1: $scope.matchPointsPanel[index].destInd1,
            destInd2: $scope.matchPointsPanel[index].destInd2,
            destSubField: $scope.matchPointsPanel[index].destSubField,
            isMultiValue: $scope.matchPointsPanel[index].isMultiValue,
            constant: $scope.matchPointsPanel[index].constant,
            isAddLine: true,
            isEdit: false
        };
        return matchPointNewRow;
    }

    function getAddOrOverlayRowByIndex(index) {
        var addOrOverlayNewRow = {
            matchOption: $scope.addOrOverlayPanel[index].matchOption,
            addOrOverlayDocType: $scope.addOrOverlayPanel[index].addOrOverlayDocType,
            operation: $scope.addOrOverlayPanel[index].operation,
            addOperation: $scope.addOrOverlayPanel[index].addOperation,
            addOrOverlayField: $scope.addOrOverlayPanel[index].addOrOverlayField,
            addOrOverlayFieldOperation: $scope.addOrOverlayPanel[index].addOrOverlayFieldOperation,
            addOrOverlayFieldValue: $scope.addOrOverlayPanel[index].addOrOverlayFieldValue,
            addItems: $scope.addOrOverlayPanel[index].addItems,
            dataField: $scope.addOrOverlayPanel[index].dataField,
            ind1: $scope.addOrOverlayPanel[index].ind1,
            ind2: $scope.addOrOverlayPanel[index].ind2,
            subField: $scope.addOrOverlayPanel[index].subField,
            linkField: $scope.addOrOverlayPanel[index].linkField,
            isAddLine: true,
            isEdit: false
        };
        return addOrOverlayNewRow;
    }

    function getFieldOperationRowByIndex(index) {
        var fieldOperationNewRow = {
            fieldOperationType: $scope.fieldOperationsPanel[index].fieldOperationType,
            dataField: $scope.fieldOperationsPanel[index].dataField,
            ind1: $scope.fieldOperationsPanel[index].ind1,
            ind2: $scope.fieldOperationsPanel[index].ind2,
            subField: $scope.fieldOperationsPanel[index].subField,
            value: $scope.fieldOperationsPanel[index].value,
            ignoreGPF: false,
            isAddLine: true,
            isEdit: false
        };
        return fieldOperationNewRow;
    }

    function getDataTransformationRowByIndex(index) {
        var dataTransformationNewRow = {
            dataTransformationDocType: $scope.dataTransformationsPanel[index].dataTransformationDocType,
            dataTransformationActionType: $scope.dataTransformationsPanel[index].dataTransformationActionType,
            dataField: $scope.dataTransformationsPanel[index].dataField,
            ind1: $scope.dataTransformationsPanel[index].ind1,
            ind2: $scope.dataTransformationsPanel[index].ind2,
            subField: $scope.dataTransformationsPanel[index].subField,
            dataTransformationOperation: $scope.dataTransformationsPanel[index].dataTransformationOperation,
            destDataField: $scope.dataTransformationsPanel[index].destDataField,
            destInd1: $scope.dataTransformationsPanel[index].destInd1,
            destInd2: $scope.dataTransformationsPanel[index].destInd2,
            destSubField: $scope.dataTransformationsPanel[index].destSubField,
            dataTransformationConstant: $scope.dataTransformationsPanel[index].dataTransformationConstant,
            dataTransformationStep: $scope.dataTransformationsPanel[index].dataTransformationStep,
            isAddLine: true,
            isEdit: false
        };
        return dataTransformationNewRow;
    }

    function getDataMappingRowByIndex(index) {
        var dataMappingNewRow = {
            dataMappingDocType: $scope.dataMappingsPanel[index].dataMappingDocType,
            dataField: $scope.dataMappingsPanel[index].dataField,
            ind1: $scope.dataMappingsPanel[index].ind1,
            ind2: $scope.dataMappingsPanel[index].ind2,
            subField: $scope.dataMappingsPanel[index].subField,
            constant: $scope.dataMappingsPanel[index].constant,
            destination: $scope.dataMappingsPanel[index].destination,
            field: $scope.dataMappingsPanel[index].field,
            transferOption: $scope.dataMappingsPanel[index].transferOption,
            priority: $scope.dataMappingsPanel[index].priority,
            isMultiValue: $scope.dataMappingsPanel[index].isMultiValue,
            isAddLine: true,
            isEdit: false
        };
        return dataMappingNewRow;
    }

    function setDataMappingDropDowns(dataMapping) {
        dataMapping.dataMappingDocTypes = dataMappingProcessTypes;
        dataMapping.destinations = populateDestinationForDataMappingToEdit(dataMapping.dataMappingDocType);
        dataMapping.transferOptions = transferOptions;
        if ($scope.mainSectionPanel.batchProcessType == 'Order Record Import') {
            dataMapping.dataMappingFields = orderFields;
        } else if ($scope.mainSectionPanel.batchProcessType == 'Invoice Import') {
            dataMapping.dataMappingFields = invoiceFieldObject.dataMapping;
        } else if ($scope.mainSectionPanel.batchProcessType == 'Bib Import') {
            $scope.populateDestinationFields(dataMapping);
        }
        $scope.populateDestinationFieldValues(index, null, dataMapping.dataMappingDocType, dataMapping.field);
    }

    function getFilterCriteriaRowByIndex(index) {
        var filterCriteriaNewRow = {
            filterFieldName: $scope.filterCriteriaPanel[index].filterFieldName,
            filterFieldNameText: $scope.filterCriteriaPanel[index].filterFieldNameText,
            filterFieldValue: $scope.filterCriteriaPanel[index].filterFieldValue,
            filterFieldRangeFrom: $scope.filterCriteriaPanel[index].filterFieldRangeFrom,
            filterFieldRangeTo: $scope.filterCriteriaPanel[index].filterFieldRangeTo,
            isAddLine: true,
            isEdit: false
        };
        return filterCriteriaNewRow;
    }

    $scope.setDefaultsDataTransformation = function (dataTransformation) {
        dataTransformation.dataTransformationActionType = 'All';
        dataTransformation.dataTransformationAction = 'Add';
        dataTransformation.dataTransformationField = null;
        dataTransformation.dataTransformationFieldValue = null;
        dataTransformation.dataField = null;
        dataTransformation.ind1 = null;
        dataTransformation.ind2 = null;
        dataTransformation.subField = null;
        dataTransformation.dataTransformationOperation = null;
        dataTransformation.destDataField = null;
        dataTransformation.destInd1 = null;
        dataTransformation.destInd2 = null;
        dataTransformation.destSubField = null;
    };

    $scope.setDefaultsAction = function (dataTransformation) {
        dataTransformation.dataTransformationField = null;
        dataTransformation.dataTransformationFieldValue = null;
        dataTransformation.dataField = null;
        dataTransformation.ind1 = null;
        dataTransformation.ind2 = null;
        dataTransformation.subField = null;
        dataTransformation.dataTransformationOperation = null;
        dataTransformation.destDataField = null;
        dataTransformation.destInd1 = null;
        dataTransformation.destInd2 = null;
        dataTransformation.destSubField = null;
    };

    $scope.setDefaultsAddOrOverlay = function (batchProcessType, addOrOverlay) {
        addOrOverlay.addOrOverlayField = null;
        addOrOverlay.addOrOverlayFieldOperation = null;
        addOrOverlay.addOrOverlayFieldValue = null;
        addOrOverlay.operation = '';
        addOrOverlay.addOperation = '';
        addOrOverlay.addItems = false;
        addOrOverlay.dataField = null;
        addOrOverlay.ind1 = null;
        addOrOverlay.ind2 = null;
        addOrOverlay.subfield = null;
        addOrOverlay.value = null;
        addOrOverlay.linkField = null;
        populateAddOrOverlayOperations(batchProcessType, addOrOverlay);
    };

    function populateAddOrOverlayOperations(batchProcessType, addOrOverlay) {
        if (batchProcessType == 'Bib Import') {
            if (addOrOverlay.matchOption == 'If Match Found') {
                if (addOrOverlay.addOrOverlayDocType == 'Bibliographic') {
                    addOrOverlay.operations = bibMatchOperations;
                } else if (addOrOverlay.addOrOverlayDocType == 'Holdings' || addOrOverlay.addOrOverlayDocType == 'Item' || addOrOverlay.addOrOverlayDocType == 'EHoldings') {
                    addOrOverlay.operations = operations;
                }
            } else if (addOrOverlay.matchOption == 'If Match Not Found') {
                if (addOrOverlay.addOrOverlayDocType == 'Bibliographic') {
                    addOrOverlay.operations = bibDoNotMatchOperations;
                } else if (addOrOverlay.addOrOverlayDocType == 'Holdings' || addOrOverlay.addOrOverlayDocType == 'Item' || addOrOverlay.addOrOverlayDocType == 'EHoldings') {
                    addOrOverlay.operations = doNotMatchOperations;
                }
            }
        }
    }

    function getAddOperationWithMultipleOptions(batchProcessType, addOrOverlay) {
        if (batchProcessType == 'Bib Import' && (addOrOverlay.matchOption == 'If Match Found' || addOrOverlay.matchOption == 'If Match Not Found')
            && (addOrOverlay.addOrOverlayDocType == 'Holdings' || addOrOverlay.addOrOverlayDocType == 'EHoldings' || addOrOverlay.addOrOverlayDocType == 'Item')
            && addOrOverlay.operation == 'Add') {
            return createMultiple;
        } else if (batchProcessType == 'Bib Import' && addOrOverlay.matchOption == 'If Match Found'
            && (addOrOverlay.addOrOverlayDocType == 'Holdings' || addOrOverlay.addOrOverlayDocType == 'EHoldings' || addOrOverlay.addOrOverlayDocType == 'Item')
            && addOrOverlay.operation == 'Overlay') {
            return overlayMultiple;
        } else {
            return null;
        }
    }

    function populateFilterCriteriaFieldNames($scope) {
        doGetRequest($scope, $http, OLENG_CONSTANTS.PROFILE_GET_FILTER_NAMES, null, function (response) {
            var data = response.data;
            console.log(data);
            $scope.filterCriteriaPanel[0].filterFieldNames = data;
        });
    }

    $scope.populateActionDropDownValues = function (batchProcessType, addOrOverlay) {
        addOrOverlay.addOperationsWithMultiple = getAddOperationWithMultipleOptions(batchProcessType, addOrOverlay);
    };

    $scope.populateDestinationFields = function (dataMapping) {
        if(dataMapping.dataMappingDocType == 'Bib Marc') {
            if(dataMapping.destination == 'Holdings') {
                dataMapping.dataMappingFields = dataMappingObject.destinationFieldsForBibMarcHoldings;
            } else if(dataMapping.destination == 'Item') {
                dataMapping.dataMappingFields = dataMappingObject.destinationFieldsForBibMarcItems;
            }else if(dataMapping.destination == 'EHoldings') {
                dataMapping.dataMappingFields =  dataMappingObject.destinationFieldsForBibMarcEHoldings
            }
        } else if(dataMapping.dataMappingDocType == 'Constant') {
            if(dataMapping.destination == 'Bibliographic') {
                dataMapping.dataMappingFields = dataMappingObject.destinationFieldsForConstantsBib;
            }else if(dataMapping.destination == 'Holdings') {
                dataMapping.dataMappingFields = dataMappingObject.destinationFieldsForConstantsHoldings;
            } else if(dataMapping.destination == 'Item') {
                dataMapping.dataMappingFields = dataMappingObject.destinationFieldsForConstantsItems;
            }else if(dataMapping.destination == 'EHoldings') {
                dataMapping.dataMappingFields =  dataMappingObject.destinationFieldsForConstantsEHoldings;
            }
        }

    };

    $scope.populateDestinationFieldValues = function (index, dataObject, dataType, fieldType) {
        if (dataType !== 'Bib Marc' || (dataObject != undefined && dataObject != null && dataObject.title == 'Match Points')) {
            $scope.constantValues = [];
            if (fieldType === 'Staff Only') {
                $scope.constantValues = booleanOptionsYorN;
                populateOptions(dataObject, index, $scope.constantValues);
            } else if (fieldType === 'Discount Type') {
                $scope.constantValues = discountTypes;
                populateOptions(dataObject, index, $scope.constantValues);
            } else if (fieldType === 'Receiving Required' || fieldType === 'Use Tax Indicator' || fieldType === 'Pay Req Positive Approval Req' || fieldType === 'Purchase Order Confirmation Indicator' || fieldType === 'Route To Requestor') {
                $scope.constantValues = booleanOptionsYorN;
                populateOptions(dataObject, index, $scope.constantValues);
            }

            if (null !== dataObject && dataObject !== undefined) {
                var addOrOverlayField = dataObject.addOrOverlayField;
                if (addOrOverlayField !== null && addOrOverlayField !== undefined && addOrOverlayField !== '') {
                    if (addOrOverlayField === 'Bib Status') {
                        $scope.increateFieldSizeForAddOrOverlayFieldDropdown(index);
                    } else {
                        $scope.resetAddOrOverlayFieldDropdownSize(index);
                    }
                }
            }
            getMaintenanceDataForFieldTypeForDropDown(dataObject, index, fieldType, $scope, $http);
        }
    };

    $scope.increateFieldSizeForAddOrOverlayFieldDropdown = function(index) {
        $('#addOrOverlayFieldValue_' + index).css('height', '200px');
        $('#addOrOverlayFieldValue_' + index).attr('multiple', true);
    }

    $scope.resetAddOrOverlayFieldDropdownSize = function(index) {
        $('#addOrOverlayFieldValue_' + index).css('height', '30px');
        $('#addOrOverlayFieldValue_' + index).attr('multiple', false);
    }

    $scope.populationDestinations = function (dataMapping) {
        makeDataMappingValid($scope);
        dataMapping.dataField = null;
        dataMapping.ind1 = null;
        dataMapping.ind2 = null;
        dataMapping.subField = null;
        dataMapping.destination = null;
        dataMapping.field = null;
        dataMapping.constant = null;
        if (dataMapping.dataMappingDocType == 'Bib Marc') {
            dataMapping.destinations = dataMappingObject.destinationForBibMarc;
        } else if (dataMapping.dataMappingDocType == 'Constant') {
            dataMapping.destinations = dataMappingObject.destinationForConstant;
        }
    };

    function populateDestinationForDataMappingToEdit(type) {
        if(type == 'Bib Marc') {
            return dataMappingObject.destinationForBibMarc;
        }else if(type == 'Constant') {
            return dataMappingObject.destinationForConstant;
        }
        return [];
    }

    $scope.clearDataTransformationValidations = function() {
        makeDataTransformationValid($scope);
    };

    $scope.submit = function () {
        $scope.submitted = true;
        removeEmptyValues();
        var profile = {
            "profileId": $scope.mainSectionPanel.profileId,
            "profileName": $scope.mainSectionPanel.profileName,
            "description": $scope.mainSectionPanel.profileDescription,
            "batchProcessType": $scope.mainSectionPanel.batchProcessType,
            "orderType": $scope.mainSectionPanel.orderType,
            "exportScope": $scope.mainSectionPanel.exportScope,
            "bibImportProfileForOrderImport": $scope.mainSectionPanel.bibImportProfileForOrderImport,
            "requisitionForTitlesOption": $scope.mainSectionPanel.requisitionForTitlesOption,
            "matchPointToUse": $scope.mainSectionPanel.matchPointToUse,
            "marcOnly": $scope.mainSectionPanel.marcOnly,
            "forceLoad": $scope.matchPointsActivePanel.forceLoad,
            "batchProfileFilterCriteriaList": $scope.filterCriteriaPanel,
            "batchProfileMatchPointList": $scope.matchPointsPanel,
            "batchProfileAddOrOverlayList": $scope.addOrOverlayPanel,
            "batchProfileFieldOperationList": $scope.fieldOperationsPanel,
            "batchProfileDataMappingList": $scope.dataMappingsPanel,
            "batchProfileDataTransformerList": $scope.dataTransformationsPanel,
            "batchProfileLocalDataMappings": $scope.localDataMappingsPanel
        };
        doPostRequest($scope, $http, OLENG_CONSTANTS.PROFILE_SUBMIT, profile, function (response) {
                var data = response.data;
                $scope.profile = data;
                $scope.message = 'Document was successfully submitted.';
            });
    };

    $scope.init = function () {
        //JSON.stringify(vars)
        $scope.decideToShowLocalDataMapping();
        var urlVars = getUrlVars();
        var profileId = urlVars['profileId'];
        var profileType = decodeURIComponent(urlVars['profileType']);
        var action = urlVars['action'];
        if (profileId !== null && profileId !== undefined && profileId !== '') {
            var data = {};
            data["profileId"] = profileId;
            data["action"] = action;
            $scope.mainSectionPanel.batchProcessType = profileType;
            doPostRequest($scope, $http, OLENG_CONSTANTS.PROFILE_EDIT, JSON.stringify(data), function (response) {
                    var data = response.data;
                    $scope.enableFilterCriteria(data.exportScope);
                    $scope.profile = data;
                    $scope.mainSectionPanel.profileId = data.profileId;
                    $scope.mainSectionPanel.profileName = data.profileName;
                    $scope.mainSectionPanel.profileDescription = data.description;
                    $scope.mainSectionPanel.orderType = data.orderType;
                    //$scope.mainSectionPanel.batchProcessType = data.batchProcessType;
                    $scope.mainSectionPanel.bibImportProfileForOrderImport = data.bibImportProfileForOrderImport;
                    $scope.mainSectionPanel.requisitionForTitlesOption = data.requisitionForTitlesOption;
                    $scope.mainSectionPanel.matchPointToUse = data.matchPointToUse;
                    $scope.mainSectionPanel.exportScope = data.exportScope;
                    $scope.mainSectionPanel.marcOnly = data.marcOnly;
                    $scope.matchPointsActivePanel.forceLoad = data.forceLoad;
                    $scope.filterCriteriaPanel = data.batchProfileFilterCriteriaList;
                    $scope.matchPointsPanel = data.batchProfileMatchPointList;
                    $scope.addOrOverlayPanel = data.batchProfileAddOrOverlayList;
                    $scope.fieldOperationsPanel = data.batchProfileFieldOperationList;
                    $scope.dataMappingsPanel = data.batchProfileDataMappingList;
                    $scope.dataTransformationsPanel = data.batchProfileDataTransformerList;
                    console.log(data.batchProfileLocalDataMappings);
                    if(null !== data.batchProfileLocalDataMappings && undefined !== data.batchProfileLocalDataMappings) {
                        $scope.localDataMappingsPanel = data.batchProfileLocalDataMappings;
                    }

                    addEmptyValueToAddNew(data.batchProcessType);

                    console.log($scope.localDataMappingsPanel);
                    $scope.enableFilterCriteria($scope.mainSectionPanel);

                    if ((data.batchProcessType == 'Order Record Import' || data.batchProcessType == 'Invoice Import')) {
                        $scope.dataMappingsActivePanel = [];
                        $scope.dataMappingsPanel.collapsed = false;
                        getBibImportProfileNames($scope, $http);
                    }
                });
        }
    };

    $scope.cancel = function () {
        window.location = OLENG_CONSTANTS.PROFILE_CANCEL;
    };

    var removeEmptyValues = function () {
        if ($scope.mainSectionPanel.batchProcessType == 'Bib Import') {
            $scope.matchPointsPanel.splice(0, 1);
            $scope.addOrOverlayPanel.splice(0, 1);
            $scope.fieldOperationsPanel.splice(0, 1);
            $scope.dataTransformationsPanel.splice(0, 1);
            $scope.dataMappingsPanel.splice(0, 1);
        }
        if ($scope.mainSectionPanel.batchProcessType == 'Order Record Import') {
            $scope.matchPointsPanel.splice(0, 1);
            $scope.addOrOverlayPanel.splice(0, 1);
            $scope.dataMappingsPanel.splice(0, 1);
            $scope.localDataMappingsPanel.splice(0, 1);
        }
        if($scope.mainSectionPanel.batchProcessType == 'Invoice Import') {
            $scope.matchPointsPanel.splice(0, 1);
            $scope.dataMappingsPanel.splice(0, 1);
        }
        if($scope.mainSectionPanel.batchProcessType == 'Batch Delete') {
            $scope.matchPointsPanel.splice(0, 1);
        }
        if($scope.mainSectionPanel.batchProcessType == 'Batch Export') {
            $scope.filterCriteriaPanel.splice(0, 1);
            $scope.dataMappingsPanel.splice(0, 1);
            $scope.dataTransformationsPanel.splice(0, 1);
        }
    };

    var addEmptyValueToAddNew = function (batchProcessType) {
        if ($scope.mainSectionPanel.batchProcessType == 'Bib Import') {
            $scope.matchPointsPanel.unshift(matchPoint);
            $scope.addOrOverlayPanel.unshift(addOrOverlay);
            $scope.fieldOperationsPanel.unshift(fieldOperation);
            $scope.dataMappingsPanel.unshift(dataMapping);
            $scope.dataTransformationsPanel.unshift(dataTransformation);
        }
        if ($scope.mainSectionPanel.batchProcessType == 'Order Record Import') {
            $scope.matchPointsPanel.unshift(matchPoint);
            $scope.addOrOverlayPanel.unshift(addOrOverlay);
            $scope.dataMappingsPanel.unshift(dataMappingOrder);
            if(null !== $scope.localDataMappingsPanel &&  undefined !== $scope.localDataMappingsPanel){
                $scope.localDataMappingsPanel.unshift(localDataMapping);
            }
        }
        if($scope.mainSectionPanel.batchProcessType == 'Invoice Import') {
            $scope.matchPointsPanel.unshift(matchPoint);
            $scope.dataMappingsPanel.unshift(dataMappingInvoice);
        }
        if($scope.mainSectionPanel.batchProcessType == 'Batch Delete') {
            matchPoint.matchPointDocType = 'Bibliographic';
            $scope.matchPointsPanel.unshift(matchPoint);
        }
        if($scope.mainSectionPanel.batchProcessType == 'Batch Export') {
            $scope.filterCriteriaPanel.unshift(filterCriteria);
            $scope.dataMappingsPanel.unshift(dataMappingBatchExport);
            $scope.dataTransformationsPanel.unshift(batchExpDataTransformation);
        }
    };

    $scope.dateOptions = {
        changeYear: true,
        changeMonth: true,
        yearRange: '1900:-0'
    };

    $scope.enableFilterCriteria = function(exportScope) {
        if(exportScope == 'Filter') {
           // makeFilterCriteriaValid($scope);
            populateFilterCriteriaFieldNames($scope);
        }
    };

    $scope.filterCriteriaAdd = function () {
        $scope.filterCriteriaIndex = 0;
        var filterCriteriaRow = getFilterCriteriaRowByIndex(0);
        if (!isValidFilterCriteriaRow(filterCriteriaRow, 0, $scope)) {
            return;
        }
        setDateFormForFilterCriteria(filterCriteriaRow);
        $scope.filterCriteriaPanel.push(filterCriteriaRow);
        $scope.filterCriteriaPanel[0].filterFieldNameText = null;
        $scope.filterCriteriaPanel[0].filterFieldValue = null;
        $scope.filterCriteriaPanel[0].filterFieldRangeFrom = null;
        $scope.filterCriteriaPanel[0].filterFieldRangeTo = null;
    };

    $scope.filterCriteriaCopyRow = function (index) {
        var copiedRow = getFilterCriteriaRowByIndex(index);
        $scope.filterCriteriaPanel.splice(index + 1, 0, copiedRow);
    };

    $scope.filterCriteriaEditRow = function(index) {
        if ($scope.rowToEdit === null || $scope.rowToEdit === undefined) {
            $scope.rowToEdit = getFilterCriteriaRowByIndex(index);
            $scope.filterCriteriaPanel[index].isEdit = true;
            $scope.filterCriteriaPanel[index].filterFieldNames = $scope.filterCriteriaPanel[0].filterFieldNames;
            $scope.filterCriteriaPanel[index].isAddLine = false;
        }
    };

    $scope.filterCriteriaUpdateRow = function(index) {
        $scope.filterCriteriaIndex = index;
        var updatedRow = getFilterCriteriaRowByIndex(index);
        if (!isValidFilterCriteriaRow(updatedRow, index, $scope)) {
            return;
        }
        setDateFormForFilterCriteria(updatedRow);
        $scope.filterCriteriaPanel[index] = updatedRow;
        $scope.filterCriteriaPanel[index].isEdit = false;
        $scope.rowToEdit = null;
    };

    $scope.filterCriteriaCancelUpdate = function(index) {
        $scope.filterCriteriaPanel[index].isEdit = false;
        $scope.filterCriteriaPanel[index] = $scope.rowToEdit;
        $scope.filterCriteriaPanel[index].isAddLine = true;
        $scope.rowToEdit = null;
    };

    $scope.filterCriteriaRemove = function (filterCriteria) {
        var index = $scope.filterCriteriaPanel.indexOf(filterCriteria);
        $scope.filterCriteriaPanel.splice(index, 1);
    };

    $scope.localDataMappingAdd = function () {
        $scope.localDataMappingIndex = 0;
        var localDataMappingRow = getLocalDataMappingRowByIndex(0);
        if (!isValidLocalDataMappingRow(localDataMappingRow, 0, $scope)) {
            return;
        }
        $scope.localDataMappingsPanel.push(localDataMappingRow);
        $scope.localDataMappingsPanel[0].source = null;
        $scope.localDataMappingsPanel[0].destination = null;
        removeOptions('Local Data Mappings', 0);
    };

    $scope.localDataMappingCopyRow = function (index) {
        var copiedRow = getLocalDataMappingRowByIndex(index);
        $scope.localDataMappingsPanel.splice(index + 1, 0, copiedRow);
    };

    $scope.localDataMappingEditRow = function(index) {
        if ($scope.rowToEdit === null || $scope.rowToEdit === undefined) {
            $scope.rowToEdit = getLocalDataMappingRowByIndex(index);
            $scope.localDataMappingsPanel[index].isEdit = true;
            $scope.localDataMappingsPanel[index].destinations = orderFields;
            $scope.localDataMappingsPanel[index].isAddLine = false;
        }
    };

    $scope.localDataMappingUpdateRow = function(index) {
        $scope.localDataMappingIndex = index;
        var updatedRow = getLocalDataMappingRowByIndex(index);
        if (!isValidLocalDataMappingRow(updatedRow, index, $scope)) {
            return;
        }
        $scope.localDataMappingsPanel[index] = updatedRow;
        $scope.localDataMappingsPanel[index].isEdit = false;
        $scope.rowToEdit = null;
        removeOptions('Local Data Mappings', index);
    };

    $scope.localDataMappingCancelUpdate = function(index) {
        $scope.localDataMappingsPanel[index].isEdit = false;
        $scope.localDataMappingsPanel[index] = $scope.rowToEdit;
        $scope.localDataMappingsPanel[index].isAddLine = true;
        $scope.rowToEdit = null;
    };

    $scope.localDataMappingRemove = function (localDataMapping) {
        var index = $scope.localDataMappingsPanel.indexOf(localDataMapping);
        $scope.localDataMappingsPanel.splice(index, 1);
    };

    $scope.decideToShowLocalDataMapping = function () {
        doGetRequest($scope, $http, OLENG_CONSTANTS.CAN_SHOW_LOCAL_DATA_MAPPING, null, function (response) {
            var data = response.data;
            $scope.showLocalDataMapping = data.canShow;
            console.log("Show local datamapping : " + $scope.showLocalDataMapping);
        });
    };

    function getLocalDataMappingRowByIndex(index) {
        var dataMappingNewRow = {
            source: $scope.localDataMappingsPanel[index].source,
            destination: $scope.localDataMappingsPanel[index].destination,
            isAddLine: true,
            isEdit: false
        };
        return dataMappingNewRow;
    }


}]);