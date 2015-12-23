var app = angular.module('batchProcessProfile', ['ngAnimate', 'ngSanitize', 'mgcrea.ngStrap']);

var documentTypes = [
    {id: 'bibliographic', name: 'Bibliographic'},
    {id: 'holdings', name: 'Holdings'},
    {id: 'item', name: 'Item'},
    {id: 'eHoldings', name: 'EHoldings'}
];

var documentTypesForDataTransformations = [
    {id: 'bibliographic', name: 'Bibliographic'}
];

var destinationDocumentTypes = [
    {id: 'holdings', name: 'Holdings'},
    {id: 'item', name: 'Item'},
    {id: 'eHoldings', name: 'EHoldings'}
];

var addOrOverlayDocumentTypes = [
    {id: 'bibliographic', name: 'Bibliographic'},
    {id: 'holdings', name: 'Holdings'},
    {id: 'item', name: 'Item'},
    {id: 'eHoldings', name: 'EHoldings'}
];

var holdingsMatchPoints = [
    {id: 'callNumber', name: 'Call Number'},
    {id: 'callNumberPrefix', name: 'Call Number Prefix'},
    {id: 'callNumberType', name: 'Call Number Type'},
    {id: 'copyNumber', name: 'Copy Number'},
    {id: 'locationLevel1', name: 'Location Level1'},
    {id: 'locationLevel2', name: 'Location Level2'},
    {id: 'locationLevel3', name: 'Location Level3'},
    {id: 'locationLevel4', name: 'Location Level4'},
    {id: 'locationLevel5', name: 'Location Level5'}
];

var itemMatchPoints = [
    {id: 'holdingsLocationLevel1', name: 'Holdings Location Level1'},
    {id: 'holdingsLocationLevel2', name: 'Holdings Location Level2'},
    {id: 'holdingsLocationLevel3', name: 'Holdings Location Level3'},
    {id: 'holdingsLocationLevel4', name: 'Holdings Location Level4'},
    {id: 'holdingsLocationLevel5', name: 'Holdings Location Level5'},
    {id: 'holdingsCallNumber', name: 'Holdings Call Number'},
    {id: 'holdingsCallNumberPrefix', name: 'Holdings Call Number Prefix'},
    {id: 'holdingsCallNumberType', name: 'Holdings Call Number Type'},
    {id: 'holdingsCopyNumber', name: 'Holdings Copy Number'},
    {id: 'callNumber', name: 'Call Number'},
    {id: 'callNumberPrefix', name: 'Call Number Prefix'},
    {id: 'callNumberType', name: 'Call Number Type'},
    {id: 'copyNumber', name: 'Copy Number'},
    {id: 'locationLevel1', name: 'Location Level1'},
    {id: 'locationLevel2', name: 'Location Level2'},
    {id: 'locationLevel3', name: 'Location Level3'},
    {id: 'locationLevel4', name: 'Location Level4'},
    {id: 'locationLevel5', name: 'Location Level5'},
    {id: 'barcode', name: 'Item Barcode'},
    {id: 'type', name: 'Item Type'},
    {id: 'status', name: 'Item Status'},
    {id: 'donorCode', name: 'Donor Code'},
    {id: 'donorPublicDisplay', name: 'Donor Public Display'},
    {id: 'donorNote', name: 'Donor Note'},
    {id: 'enumeration', name: 'Enumeration'},
    {id: 'chronology', name: 'Chronology'},
    {id: 'vendorLineItemId', name: 'Vendor Line Item Identifier'}
];

var eHoldingsMatchPoints = [
    {id: 'callNumber', name: 'Call Number'},
    {id: 'accessStatus', name: 'Access Status'},
    {id: 'callNumberType', name: 'Call Number Type'},
    {id: 'locationLevel1', name: 'Location Level1'},
    {id: 'locationLevel2', name: 'Location Level2'},
    {id: 'locationLevel3', name: 'Location Level3'},
    {id: 'locationLevel4', name: 'Location Level4'},
    {id: 'locationLevel5', name: 'Location Level5'},
    {id: 'url', name: 'URL'},
    {id: 'persistentLink', name: 'Persistent Link'},
    {id: 'linkText', name: 'Link Text'},
    {id: 'donorCode', name: 'Donor Code'},
    {id: 'donorPublicDisplay', name: 'Donor Public Display'},
    {id: 'donorNote', name: 'Donor Note'},
    {id: 'statisticalCode', name: 'Statistical Code'},
    {id: 'platform', name: 'Platform'},
    {id: 'publisher', name: 'Publisher'},
    {id: 'coverageStartDate', name: 'Coverage Start Date'},
    {id: 'coverageStartIssue', name: 'Coverage Start Issue'},
    {id: 'coverageStartVolume', name: 'Coverage Start Volume'},
    {id: 'coverageEndDate', name: 'Coverage End Date'},
    {id: 'coverageEndIssue', name: 'Coverage End Issue'},
    {id: 'coverageEndVolume', name: 'Coverage End Volume'},
    {id: 'eResourceName', name: 'EResource Name'},
    {id: 'eResourceId', name: 'EResource Id'}
];

var matchOptions = [
    {id: 'matchFound', name: 'If Match Found'},
    {id: 'matchNotFound', name: 'If Match Not Found'}
];

var operations = [
    {id: 'add', name: 'Add'},
    {id: 'overlay', name: 'Overlay'},
    {id: 'discard', name: 'Discard'}
];

var bibDoNotMatchOperations = [
    {id: 'add', name: 'Add'}
];

var doNotMatchOperations = [
    {id: 'add', name: 'Add'},
    {id: 'discard', name: 'Discard'}
];

var addOperations = [
    {id: 'deleteAll', name: 'Delete all existing and add'},
    {id: 'keepAll', name: 'Keep all existing and add'}
];

var fieldOperations = [
    {id: 'profile', name: 'Profile Protected Field'}
];

var fields = [
    {id: 'callNumber', name: 'Call Number'},
    {id: 'callNumberPrefix', name: 'Call Number Prefix'},
    {id: 'callNumberType', name: 'Call Number Type'},
    {id: 'copyNumber', name: 'Copy Number'},
    {id: 'locationLevel1', name: 'Location Level1'},
    {id: 'locationLevel2', name: 'Location Level2'},
    {id: 'locationLevel3', name: 'Location Level3'},
    {id: 'locationLevel4', name: 'Location Level4'},
    {id: 'locationLevel5', name: 'Location Level5'}
];

var transformers = [
    {id: 'regex', name: 'Regex Pattern Transformer'},
    {id: '', name: ''}
];

var actionTypes = [
    {id: 'all', name: 'All'},
    {id: 'new', name: 'New'},
    {id: 'overlay', name: 'Overlay'}
];

var actions = [
    {id: 'add', name: 'Add'},
    {id: 'update', name: 'Update'},
    {id: 'transform', name: 'Transform'}
];

var bibFields = [
    {id: 'bibStatus', name: 'Bib Status'},
    {id: 'staffOnly', name: 'Staff Only'}
];

var booleanOptions = [
    {id: 'true', value: 'True'},
    {id: 'false', value: 'False'}
];

var holdingsFields = [
    {id: 'receiptStatus', name: 'Receipt Status'},
    {id: 'subscriptionStatus', name: 'Subscription Status'},
    {id: 'accessStatus', name: 'Access Status'},
    {id: 'staffOnly', name: 'Staff Only'}
];

var itemFields = [
    {id: 'itemType', name: 'Item Type'},
    {id: 'itemStatus', name: 'Item Status'}
];

var eHoldingsFields = [
    {id: 'accessStatus', name: 'Access Status'},
    {id: 'staffOnly', name: 'Staff Only'}
];

var transformationOperations = [
    {id: 'add', name: 'Add'},
    {id: 'delete', name: 'Delete'},
    {id: 'replace', name: 'Replace'},
    {id: 'join', name: 'Join'},
    {id: 'prepend', name: 'prepend with prefix'},
    {id: 'remove', name: 'Remove'}
];



app.controller('batchProfileController', ['$scope', '$http', function ($scope, $http) {

    $scope.booleanOptions = booleanOptions;

    $scope.submitted = false;

    $scope.toggle = function (panel) {
        var panelTitle = panel[0].title;

        if (panelTitle == 'Main Section') {
            $scope.mainSectionPanel.collapsed = !$scope.mainSectionPanel.collapsed;
        } else if (panelTitle == 'Match Points') {
            $scope.matchPointsPanel.collapsed = !$scope.matchPointsPanel.collapsed;
        } else if (panelTitle == 'Matching, Add and Overlay') {
            $scope.addOrOverlayPanel.collapsed = !$scope.addOrOverlayPanel.collapsed;
        } else if (panelTitle == 'Field Operations') {
            $scope.fieldOperationsPanel.collapsed = !$scope.fieldOperationsPanel.collapsed;
        } else if (panelTitle == 'Data Mappings') {
            $scope.dataMappingsPanel.collapsed = !$scope.dataMappingsPanel.collapsed;
        } else if (panelTitle == 'Data Transformations') {
            $scope.dataTransformationsPanel.collapsed = !$scope.dataTransformationsPanel.collapsed;
        }
    };

    $scope.mainSectionPanel = [
        {
            title: 'Main Section',
            batchProcessType: 'Bib Import',
            collapsed: false
        }
    ];

    $scope.matchPointsPanel = [
        {
            title: 'Match Points',
            matchPointDocTypes: documentTypes,
            matchPointDocType: 'Bibliographic',
            holdingsMatchPoints: holdingsMatchPoints,
            itemMatchPoints: itemMatchPoints,
            eHoldingsMatchPoints: eHoldingsMatchPoints,
            isAddLine: false
        }
    ];

    $scope.addOrOverlayPanel = [
        {
            title: 'Matching, Add and Overlay',
            matchOptions: matchOptions,
            matchOption: 'If Match Found',
            addOrOverlayDocTypes: addOrOverlayDocumentTypes,
            addOrOverlayDocType: 'Bibliographic',
            operations: operations,
            operation: 'Add',
            bibDoNotMatchOperations: bibDoNotMatchOperations,
            doNotMatchOperations: doNotMatchOperations,
            addOperations: addOperations,
            addItems: false,
            isAddLine: false,
            collapsed: true
        }
    ];

    $http.get(OLENG_CONSTANTS.PROFILE_GET_GLOBALLY_PROTECTED_FIELDS).success(function(data) {
        $scope.fieldOperationsPanel = data;

        $scope.fieldOperationsPanel.unshift(
            {
                title: 'Field Operations',
                fieldOperationTypes: fieldOperations,
                fieldOperationType: 'Profile Protected Field',
                ignoreGPF: false,
                isAddLine: false,
                collapsed: true
            }
        );
    });

    $scope.dataMappingsPanel = [
        {
            title: 'Data Mappings',
            dataMappingDocTypes: documentTypes,
            dataMappingDocType: 'Bibliographic',
            destinations: destinationDocumentTypes,
            holdingsFields: holdingsMatchPoints,
            itemFields: itemMatchPoints,
            eHoldingsFields: eHoldingsMatchPoints,
            priority: 1,
            isAddLine: false,
            collapsed: true
        }
    ];

    $scope.dataTransformationsPanel = [
        {
            title: 'Data Transformations',
            dataTransformationDocTypes: documentTypesForDataTransformations,
            dataTransformationDocType: 'Bibliographic',
            transformers: transformers,
            transformer: 'Regex Pattern Transformer',
            dataTransformationActionTypes: actionTypes,
            dataTransformationActionType: 'All',
            dataTransformationActions: actions,
            dataTransformationAction: 'Add',
            dataTransformationBibFields: bibFields,
            dataTransformationStaffOnlyFields: booleanOptions,
            dataTransformationHoldingsFields: holdingsFields,
            dataTransformationItemFields: itemFields,
            dataTransformationEHoldingsFields: eHoldingsFields,
            dataTransformationOperations: transformationOperations,
            dataTransformationTransformHoldingsFields: holdingsMatchPoints,
            dataTransformationTransformItemFields: itemMatchPoints,
            dataTransformationTransformEHoldingsFields: eHoldingsMatchPoints,
            dataTransformationStep: 1,
            isAddLine: false,
            collapsed: true
        }
    ];

    $scope.mainSectionActivePanel = [0];
    $scope.matchPointsActivePanel = [];
    $scope.addOrOverlayActivePanel = [];
    $scope.fieldOperationsActivePanel = [];
    $scope.dataMappingsActivePanel = [];
    $scope.dataTransformationsActivePanel = [];

    $scope.matchPointAdd = function () {
        $scope.matchPointsPanel.push({
            matchPointDocType: $scope.matchPointsPanel[0].matchPointDocType,
            matchPointType: $scope.matchPointsPanel[0].matchPointType,
            matchPointValue: $scope.matchPointsPanel[0].matchPointValue,
            dataField: $scope.matchPointsPanel[0].dataField,
            ind1: $scope.matchPointsPanel[0].ind1,
            ind2: $scope.matchPointsPanel[0].ind2,
            subField: $scope.matchPointsPanel[0].subField,
            constant: $scope.matchPointsPanel[0].constant,
            isAddLine: true
        });
        $scope.matchPointsPanel[0].matchPointDocType = 'Bibliographic';
        $scope.matchPointsPanel[0].matchPointType = null;
        $scope.matchPointsPanel[0].matchPointValue = null;
        $scope.matchPointsPanel[0].dataField = null;
        $scope.matchPointsPanel[0].ind1 = null;
        $scope.matchPointsPanel[0].ind2 = null;
        $scope.matchPointsPanel[0].subField = null;
        $scope.matchPointsPanel[0].constant = null;
    };

    $scope.matchPointRemove = function (matchPoint) {
        var index = $scope.matchPointsPanel.indexOf(matchPoint);
        $scope.matchPointsPanel.splice(index, 1);
    };

    $scope.addOrOverlayAdd = function () {
        $scope.addOrOverlayPanel.push({
            matchOption: $scope.addOrOverlayPanel[0].matchOption,
            addOrOverlayDocType: $scope.addOrOverlayPanel[0].addOrOverlayDocType,
            operation: $scope.addOrOverlayPanel[0].operation,
            bibStatus: $scope.addOrOverlayPanel[0].bibStatus,
            addOperation: $scope.addOrOverlayPanel[0].addOperation,
            addItems: $scope.addOrOverlayPanel[0].addItems,
            isAddLine: true
        });
        $scope.addOrOverlayPanel[0].matchOption = 'If Match Found';
        $scope.addOrOverlayPanel[0].addOrOverlayDocType = 'Bibliographic';
        $scope.addOrOverlayPanel[0].operation = 'Add';

    };

    $scope.addOrOverlayRemove = function (addOrOverlay) {
        var index = $scope.addOrOverlayPanel.indexOf(addOrOverlay);
        $scope.addOrOverlayPanel.splice(index, 1);
    };

    $scope.fieldOperationAdd = function () {
        $scope.fieldOperationsPanel.push({
            fieldOperationType: $scope.fieldOperationsPanel[0].fieldOperationType,
            dataField: $scope.fieldOperationsPanel[0].dataField,
            ind1: $scope.fieldOperationsPanel[0].ind1,
            ind2: $scope.fieldOperationsPanel[0].ind2,
            subField: $scope.fieldOperationsPanel[0].subField,
            ignoreGPF: false,
            isAddLine: true
        });
        $scope.fieldOperationsPanel[0].fieldOperationType = 'Profile Protected Field';
        $scope.fieldOperationsPanel[0].dataField = null;
        $scope.fieldOperationsPanel[0].ind1 = null;
        $scope.fieldOperationsPanel[0].ind2 = null;
        $scope.fieldOperationsPanel[0].subField = null;
        $scope.fieldOperationsPanel[0].ignoreGPF = false;
    };

    $scope.fieldOperationRemove = function (fieldOperation) {
        var index = $scope.fieldOperationsPanel.indexOf(fieldOperation);
        $scope.fieldOperationsPanel.splice(index, 1);
    };

    $scope.dataMappingAdd = function () {
        $scope.dataMappingsPanel.push({
            dataMappingDocType: $scope.dataMappingsPanel[0].dataMappingDocType,
            dataField: $scope.dataMappingsPanel[0].dataField,
            ind1: $scope.dataMappingsPanel[0].ind1,
            ind2: $scope.dataMappingsPanel[0].ind2,
            subField: $scope.dataMappingsPanel[0].subField,
            constant: $scope.dataMappingsPanel[0].constant,
            destination: $scope.dataMappingsPanel[0].destination,
            field: $scope.dataMappingsPanel[0].field,
            priority: $scope.dataMappingsPanel[0].priority,
            isAddLine: true
        });
        $scope.dataMappingsPanel[0].dataMappingDocType = 'Bibliographic';
        $scope.dataMappingsPanel[0].dataField = null;
        $scope.dataMappingsPanel[0].ind1 = null;
        $scope.dataMappingsPanel[0].ind2 = null;
        $scope.dataMappingsPanel[0].subField = null;
        $scope.dataMappingsPanel[0].constant = null;
        $scope.dataMappingsPanel[0].destination = null;
        $scope.dataMappingsPanel[0].field = null;
        $scope.dataMappingsPanel[0].priority = 1;
    };

    $scope.dataMappingRemove = function (dataMapping) {
        var index = $scope.dataMappingsPanel.indexOf(dataMapping);
        $scope.dataMappingsPanel.splice(index, 1);
    };

    $scope.dataTransformationAdd = function () {
        $scope.dataTransformationsPanel.push({
            dataTransformationDocType: $scope.dataTransformationsPanel[0].dataTransformationDocType,
            dataTransformationActionType: $scope.dataTransformationsPanel[0].dataTransformationActionType,
            dataTransformationAction: $scope.dataTransformationsPanel[0].dataTransformationAction,
            dataTransformationField: $scope.dataTransformationsPanel[0].dataTransformationField,
            dataTransformationFieldValue: $scope.dataTransformationsPanel[0].dataTransformationFieldValue,
            dataTransformationSourceField: $scope.dataTransformationsPanel[0].dataTransformationSourceField,
            dataTransformationOperation: $scope.dataTransformationsPanel[0].dataTransformationOperation,
            dataTransformationDestinationField: $scope.dataTransformationsPanel[0].dataTransformationDestinationField,
            dataTransformationConstant: $scope.dataTransformationsPanel[0].dataTransformationConstant,
            dataTransformationTransformField: $scope.dataTransformationsPanel[0].dataTransformationTransformField,
            dataTransformationStep: $scope.dataTransformationsPanel[0].dataTransformationStep,
            isAddLine: true
        });
        $scope.dataTransformationsPanel[0].dataTransformationDocType = 'Bibliographic';
        $scope.dataTransformationsPanel[0].dataTransformationActionType = 'All';
        $scope.dataTransformationsPanel[0].dataTransformationAction = 'Add';
        $scope.dataTransformationsPanel[0].dataTransformationField = null;
        $scope.dataTransformationsPanel[0].dataTransformationFieldValue = null;
        $scope.dataTransformationsPanel[0].dataTransformationSourceField = null;
        $scope.dataTransformationsPanel[0].dataTransformationOperation = null;
        $scope.dataTransformationsPanel[0].dataTransformationDestinationField = null;
        $scope.dataTransformationsPanel[0].dataTransformationConstant = null;
        $scope.dataTransformationsPanel[0].dataTransformationTransformField = null;
        $scope.dataTransformationsPanel[0].dataTransformationStep = 1;
    };

    $scope.dataTransformationRemove = function (dataTransformation) {
        var index = $scope.dataTransformationsPanel.indexOf(dataTransformation);
        $scope.dataTransformationsPanel.splice(index, 1);
    };

    $scope.setDefaultsMatchPoint = function (matchPoint) {
        matchPoint.matchPointType = null;
        matchPoint.dataField = null;
        matchPoint.ind1 = null;
        matchPoint.ind2 = null;
        matchPoint.subField = null;
    };

    $scope.setDefaultsDataTransformation = function (dataTransformation) {
        dataTransformation.dataTransformationActionType = 'All';
        dataTransformation.dataTransformationAction = 'Add';
        dataTransformation.dataTransformationField = null;
        dataTransformation.dataTransformationFieldValue = null;
        dataTransformation.dataTransformationSourceField = null;
        dataTransformation.dataTransformationOperation = null;
        dataTransformation.dataTransformationDestinationField = null;
    };

    $scope.setDefaultsAction = function (dataTransformation) {
        dataTransformation.dataTransformationField = null;
    };

    $scope.setDefaultsDestination = function (dataMapping) {
        dataMapping.field = null;
    };

    $scope.getMaintenanceValuesForType = function (dataObject) {
        if (dataObject.title == 'Match Points') {
            if ((dataObject.matchPointType == 'Call Number Type' || dataObject.matchPointType == 'Holdings Call Number Type') && $scope.callNumberTypeValues == undefined) {
                $http.get(OLENG_CONSTANTS.PROFILE_GET_CALLNUMBER_TYPES).success(function(data) {
                    $scope.callNumberTypeValues = data;
                });
            } else if (dataObject.matchPointType == 'Item Type' && $scope.itemTypeValues == undefined) {
                $http.get(OLENG_CONSTANTS.PROFILE_GET_ITEM_TYPES).success(function(data) {
                    $scope.itemTypeValues = data;
                });
            } else if (dataObject.matchPointType == 'Item Status' && $scope.itemStatusValues == undefined) {
                $http.get(OLENG_CONSTANTS.PROFILE_GET_ITEM_STATUS).success(function(data) {
                    $scope.itemStatusValues = data;
                });
            } else if (dataObject.matchPointType == 'Donor Code' && $scope.donorCodeValues == undefined) {
                $http.get(OLENG_CONSTANTS.PROFILE_GET_DONOR_CODES).success(function(data) {
                    $scope.donorCodes = data;
                });
            } else if ((dataObject.matchPointType == 'Location Level1' || dataObject.matchPointType == 'Holdings Location Level1') && $scope.locationLevel1Values == undefined) {
                $http.get(OLENG_CONSTANTS.PROFILE_GET_LOCATIONS, {params:{"levelId": 1}}).success(function(data) {
                    $scope.locationLevel1Values = data;
                });
            } else if ((dataObject.matchPointType == 'Location Level2' || dataObject.matchPointType == 'Holdings Location Level2') && $scope.locationLevel2Values == undefined) {
                $http.get(OLENG_CONSTANTS.PROFILE_GET_LOCATIONS, {params:{"levelId": 2}}).success(function(data) {
                    $scope.locationLevel2Values = data;
                });
            } else if ((dataObject.matchPointType == 'Location Level3' || dataObject.matchPointType == 'Holdings Location Level3') && $scope.locationLevel3Values == undefined) {
                $http.get(OLENG_CONSTANTS.PROFILE_GET_LOCATIONS, {params:{"levelId": 3}}).success(function(data) {
                    $scope.locationLevel3Values = data;
                });
            } else if ((dataObject.matchPointType == 'Location Level4' || dataObject.matchPointType == 'Holdings Location Level4') && $scope.locationLevel4Values == undefined) {
                $http.get(OLENG_CONSTANTS.PROFILE_GET_LOCATIONS, {params:{"levelId": 4}}).success(function(data) {
                    $scope.locationLevel4Values = data;
                });
            } else if ((dataObject.matchPointType == 'Location Level5' || dataObject.matchPointType == 'Holdings Location Level5') && $scope.locationLevel5Values == undefined) {
                $http.get(OLENG_CONSTANTS.PROFILE_GET_LOCATIONS, {params:{"levelId": 5}}).success(function(data) {
                    $scope.locationLevel5Values = data;
                });
            }
        } /*else if (dataObject.title == 'Matching, Add and Overlay') {
            if (dataObject.matchOption == 'Do Match' && dataObject.addOrOverlayDocType == 'Bibliographic' && dataObject.operation == 'Overlay' && $scope.bibStatuses == undefined) {
                $http.get(OLENG_CONSTANTS.PROFILE_GET_BIB_STATUS).success(function(data) {
                    $scope.bibStatuses = data;
                });
            }
        }*/ else if (dataObject.title == 'Data Transformations') {
            if (dataObject.dataTransformationDocType == 'Bibliographic' && dataObject.dataTransformationField == 'Bib Status' && (dataObject.dataTransformationAction == 'Add' || dataObject.dataTransformationAction == 'Update') && $scope.bibStatuses == undefined) {
                $http.get(OLENG_CONSTANTS.PROFILE_GET_BIB_STATUS).success(function(data) {
                    $scope.bibStatuses = data;
                });
            } else if (dataObject.dataTransformationDocType == 'Item' && dataObject.dataTransformationField == 'Item Type' && (dataObject.dataTransformationAction == 'Add' || dataObject.dataTransformationAction == 'Update') && $scope.itemTypeValues == undefined) {
                $http.get(OLENG_CONSTANTS.PROFILE_GET_ITEM_TYPES).success(function(data) {
                    $scope.itemTypeValues = data;
                });
            } else if (dataObject.dataTransformationDocType == 'Item' && dataObject.dataTransformationField == 'Item Status' && (dataObject.dataTransformationAction == 'Add' || dataObject.dataTransformationAction == 'Update') && $scope.itemStatusValues == undefined) {
                $http.get(OLENG_CONSTANTS.PROFILE_GET_ITEM_STATUS).success(function(data) {
                    $scope.itemStatusValues = data;
                });
            }
        }
    };

    $scope.submit = function () {
        $scope.submitted = true;
        removeEmptyValues();
        var profile = {
            "profileId": $scope.mainSectionPanel[0].profileId,
            "profileName": $scope.mainSectionPanel[0].profileName,
            "description": $scope.mainSectionPanel[0].profileDescription,
            "batchProcessType": $scope.mainSectionPanel[0].batchProcessType,
            "forceLoad": $scope.matchPointsActivePanel.forceLoad,
            "batchProfileMatchPointList": $scope.matchPointsPanel,
            "batchProfileAddOrOverlayList": $scope.addOrOverlayPanel,
            "batchProfileFieldOperationList": $scope.fieldOperationsPanel,
            "batchProfileDataMappingList": $scope.dataMappingsPanel,
            "batchProfileDataTransformerList": $scope.dataTransformationsPanel
        };
        $http.post(OLENG_CONSTANTS.PROFILE_SUBMIT, profile)
            .success(function (data) {
                $scope.profile = data;
                $scope.message = 'Document was successfully submitted.';
            });
    };

    $scope.init = function () {
        //JSON.stringify(vars)
        var urlVars = getUrlVars();
        var profileId = urlVars['profileId'];
        var action = urlVars['action'];
        if (profileId !== null && profileId !== undefined && profileId !== '') {
            var data = {};
            data["profileId"] = profileId;
            data["action"] = action;
            $http.post(OLENG_CONSTANTS.PROFILE_EDIT, JSON.stringify(data))
                .success(function (data) {
                    $scope.profile = data;
                    $scope.mainSectionPanel[0].profileId = data.profileId;
                    $scope.mainSectionPanel[0].profileName = data.profileName;
                    $scope.mainSectionPanel[0].profileDescription = data.description;
                    $scope.mainSectionPanel[0].batchProcessType = data.batchProcessType;
                    $scope.matchPointsActivePanel.forceLoad = data.forceLoad;
                    $scope.matchPointsPanel = data.batchProfileMatchPointList;
                    $scope.addOrOverlayPanel = data.batchProfileAddOrOverlayList;
                    $scope.fieldOperationsPanel = data.batchProfileFieldOperationList;
                    $scope.dataMappingsPanel = data.batchProfileDataMappingList;
                    $scope.dataTransformationsPanel = data.batchProfileDataTransformerList;

                    addEmptyValueToAddNew();
                });
        }
    };

    $scope.cancel = function () {
        window.location = OLENG_CONSTANTS.PROFILE_CANCEL;
    };

    var removeEmptyValues = function () {
        $scope.matchPointsPanel.splice(0, 1);
        $scope.addOrOverlayPanel.splice(0, 1);
        $scope.fieldOperationsPanel.splice(0, 1);
        $scope.dataMappingsPanel.splice(0, 1);
        $scope.dataTransformationsPanel.splice(0, 1);
    };


    var addEmptyValueToAddNew = function () {

        $scope.matchPointsPanel.unshift(
            {
                title: 'Match Points',
                matchPointDocTypes: documentTypes,
                matchPointDocType: 'Bibliographic',
                holdingsMatchPoints: holdingsMatchPoints,
                itemMatchPoints: itemMatchPoints,
                eHoldingsMatchPoints: eHoldingsMatchPoints,
                isAddLine: false
            }
        );

        $scope.addOrOverlayPanel.unshift(
            {
                title: 'Matching, Add and Overlay',
                matchOptions: matchOptions,
                matchOption: 'If Match Found',
                addOrOverlayDocTypes: addOrOverlayDocumentTypes,
                addOrOverlayDocType: 'Bibliographic',
                operations: operations,
                operation: 'Add',
                bibDoNotMatchOperations: bibDoNotMatchOperations,
                doNotMatchOperations: doNotMatchOperations,
                addOperations: addOperations,
                addItems: false,
                isAddLine: false,
                collapsed: true
            }
        );

        $scope.fieldOperationsPanel.unshift(
            {
                title: 'Field Operations',
                fieldOperationTypes: fieldOperations,
                fieldOperationType: 'Profile Protected Field',
                ignoreGPF: false,
                isAddLine: false,
                collapsed: true
            }
        );

        $scope.dataMappingsPanel.unshift(
            {
                title: 'Data Mappings',
                dataMappingDocTypes: documentTypes,
                dataMappingDocType: 'Bibliographic',
                destinations: destinationDocumentTypes,
                holdingsFields: holdingsMatchPoints,
                itemFields: itemMatchPoints,
                eHoldingsFields: eHoldingsMatchPoints,
                priority: 1,
                isAddLine: false,
                collapsed: true
            }
        );

        $scope.dataTransformationsPanel.unshift(
            {
                title: 'Data Transformations',
                dataTransformationDocTypes: documentTypes,
                dataTransformationDocType: 'Bibliographic',
                transformers: transformers,
                transformer: 'Regex Pattern Transformer',
                dataTransformationActionTypes: actionTypes,
                dataTransformationActionType: 'All',
                dataTransformationActions: actions,
                dataTransformationAction: 'Add',
                dataTransformationBibFields: bibFields,
                dataTransformationStaffOnlyFields: booleanOptions,
                dataTransformationHoldingsFields: holdingsFields,
                dataTransformationItemFields: itemFields,
                dataTransformationEHoldingsFields: eHoldingsFields,
                dataTransformationOperations: transformationOperations,
                dataTransformationTransformHoldingsFields: holdingsMatchPoints,
                dataTransformationTransformItemFields: itemMatchPoints,
                dataTransformationTransformEHoldingsFields: eHoldingsMatchPoints,
                dataTransformationStep: 1,
                isAddLine: false,
                collapsed: true
            }
        );
    };

    var getUrlVars = function () {
        var vars = {}, hash;
        var hashes = window.location.href.slice(window.location.href.indexOf('?') + 1).split('&');
        for (var i = 0; i < hashes.length; i++) {
            hash = hashes[i].split('=');
            vars[hash[0]] = hash[1];
        }
        return vars;
    }


}]);