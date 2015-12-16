var app = angular.module('batchProcessProfile', [ 'ngAnimate', 'ngSanitize', 'mgcrea.ngStrap' ]);

var documentTypes = [
    {id: 'bibliographic', name: 'Bibliographic'},
    {id: 'holdings', name: 'Holdings'},
    {id: 'item', name: 'Item'},
    {id: 'eHoldings', name: 'EHoldings'}
];

var addOrOverlayDocumentTypes = [
    {id: 'bibliographic', name: 'Bibliographic'},
    {id: 'holdings', name: 'Holdings'},
    {id: 'item', name: 'Item'},
    {id: 'eHoldings', name: 'EHoldings'},
    {id: 'po', name: 'Purchase Order'}
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
    {id: 'doMatch', name: 'Do Match'},
    {id: 'doNotMatch', name: 'Do Not Match'}
];

var operations = [
    {id: 'add', name: 'Add'},
    {id: 'overlay', name: 'Overlay'},
    {id: 'discard', name: 'Discard'}
];

var bibDonotMatchOperations = [
    {id: 'add', name: 'Add'}
];

var doNotMatchOperations = [
    {id: 'add', name: 'Add'},
    {id: 'discard', name: 'Discard'}
];

var poOperations = [
    {id: 'create', name: 'Create PO if matched'},
    {id: 'update', name: 'Update PO if matched'}
];

var addOperations = [
    {id: 'deleteAll', name: 'Delete all existing and add'},
    {id: 'keepAll', name: 'Keep all existing and add'}
];

var bibStatuses = [
    {id: 'none', name: 'None'},
    {id: 'cataloguing', name: 'Cataloguing'},
    {id: 'catalogued', name: 'Catalogued'}
];

var fieldOperations = [
    {id: 'global', name: 'Globally Protected Field'},
    {id: 'profile', name: 'Profile Protected Field'},
    {id: 'delete', name: 'Delete Field'},
    {id: 'rename', name: 'Rename Field'}
];

var fields = [
    {id: 'url', name: 'URL'},
    {id: 'callnumber', name: 'Call Number'},
    {id: 'callnumbertype', name: 'Call Number Type'}
];

var transformers = [
    {id: 'regex', name: 'Regex Pattern Transformer'},
    {id: '', name: ''}
];

app.controller('batchProfileController', ['$scope', '$http', function($scope, $http) {

    $scope.submited = false;

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

    $scope.mainSectionPanel = {
        title:'Main Section', collapsed: true
    };

    $scope.matchPointsPanel = [
        {title:'Match Points',  matchPointDocTypes: documentTypes, matchPointDocType: 'bibliographic', holdingsMatchPoints: holdingsMatchPoints, itemMatchPoints: itemMatchPoints, eHoldingsMatchPoints: eHoldingsMatchPoints, collapsed: true}
    ];

    $scope.addOrOverlayPanel = [
        {title:'Matching, Add and Overlay',  matchOptions: matchOptions, matchOption: 'doMatch', addOrOverlayDocTypes: addOrOverlayDocumentTypes, addOrOverlayDocType: 'bibliographic', operations: operations, operation: 'add', bibDonotMatchOperations: bibDonotMatchOperations, doNotMatchOperations: doNotMatchOperations, poOperations: poOperations, addOperations: addOperations, addOperation: 'deleteAll', bibStatuses: bibStatuses, bibStatus: 'none', addItems: false, collapsed: true}
    ];

    $scope.fieldOperationsPanel = [
        {title:'Field Operations',  fieldOperationTypes: fieldOperations, fieldOperationType: 'global', collapsed: true}
    ];

    $scope.dataMappingsPanel = [
        {title:'Data Mappings',  dataMappingDocTypes: documentTypes, dataMappingDocType: 'bibliographic', destinations: documentTypes, destination: 'bibliographic', fields: fields, destination: 'url', collapsed: true}
    ];

    $scope.dataTransformationsPanel = [
        {title:'Data Transformations',  dataTransformationDocTypes: documentTypes, dataTransformationDocType: 'bibliographic', transformers: transformers, transformer: 'regex', collapsed: true}
    ];

    $scope.mainSectionActivePanel = [0];
    $scope.matchPointsActivePanel = [];
    $scope.addOrOverlayActivePanel = [];
    $scope.fieldOperationsActivePanel = [];
    $scope.dataMappingsActivePanel = [];
    $scope.dataTransformationsActivePanel = [];

    $scope.matchPointAdd = function () {
        $scope.matchPointsPanel.push({ matchPointDocTypes: documentTypes, matchPointDocType: $scope.matchPointsPanel[0].matchPointDocType, matchPointValue: $scope.matchPointsPanel[0].matchPointValue, holdingsMatchPoints: holdingsMatchPoints, itemMatchPoints: itemMatchPoints, eHoldingsMatchPoints: eHoldingsMatchPoints, showRemove: true });
        $scope.matchPointsPanel[0].matchPointDocType = 'bibliographic';
        $scope.matchPointsPanel[0].matchPointValue = null;
    };

    $scope.matchPointRemove = function (matchPoint) {
        var index = $scope.matchPointsPanel.indexOf(matchPoint);
        $scope.matchPointsPanel.splice(index, 1);
    };

    $scope.addOrOverlayAdd = function () {
        $scope.addOrOverlayPanel.push({ matchOptions: matchOptions, matchOption: $scope.addOrOverlayPanel[0].matchOption, addOrOverlayDocTypes: addOrOverlayDocumentTypes, addOrOverlayDocType: $scope.addOrOverlayPanel[0].addOrOverlayDocType, operations: operations, operation: $scope.addOrOverlayPanel[0].operation, poOperations: poOperations, bibStatuses: bibStatuses, bibStatus: $scope.addOrOverlayPanel[0].bibStatus, addOperations: addOperations, addOperation: $scope.addOrOverlayPanel[0].addOperation, addItems: $scope.addOrOverlayPanel[0].addItems, showRemove: true });
        $scope.addOrOverlayPanel[0].matchOption = 'doMatch';
        $scope.addOrOverlayPanel[0].addOrOverlayDocType = 'bibliographic';
        $scope.addOrOverlayPanel[0].operation = 'add';

    };

    $scope.addOrOverlayRemove = function (addOrOverlay) {
        var index = $scope.addOrOverlayPanel.indexOf(addOrOverlay);
        $scope.addOrOverlayPanel.splice(index, 1);
    };

    $scope.fieldOperationAdd = function () {
        $scope.fieldOperationsPanel.push({ fieldOperationTypes: fieldOperations, fieldOperationType: $scope.fieldOperationsPanel[0].fieldOperationType, dataField: $scope.fieldOperationsPanel[0].dataField, ind1: $scope.fieldOperationsPanel[0].ind1, ind2: $scope.fieldOperationsPanel[0].ind2, subField: $scope.fieldOperationsPanel[0].subField, showRemove: true });
        $scope.fieldOperationsPanel[0].fieldOperationType = 'global';
        $scope.fieldOperationsPanel[0].dataField = null;
        $scope.fieldOperationsPanel[0].ind1 = null;
        $scope.fieldOperationsPanel[0].ind2 = null;
        $scope.fieldOperationsPanel[0].subField = null;
    };

    $scope.fieldOperationRemove = function (fieldOperation) {
        var index = $scope.fieldOperationsPanel.indexOf(fieldOperation);
        $scope.fieldOperationsPanel.splice(index, 1);
    };

    $scope.dataMappingAdd = function () {
        $scope.dataMappingsPanel.push({ dataMappingDocTypes: documentTypes, dataMappingDocType: $scope.dataMappingsPanel[0].dataMappingDocType, dataField: $scope.dataMappingsPanel[0].dataField, ind1: $scope.dataMappingsPanel[0].ind1, ind2: $scope.dataMappingsPanel[0].ind2, subField: $scope.dataMappingsPanel[0].subField, destinations: documentTypes, destination: $scope.dataMappingsPanel[0].destination, fields: fields, field: $scope.dataMappingsPanel[0].field, showRemove: true });
        $scope.dataMappingsPanel[0].dataMappingDocType = 'bibliographic';
        $scope.dataMappingsPanel[0].dataField = null;
        $scope.dataMappingsPanel[0].ind1 = null;
        $scope.dataMappingsPanel[0].ind2 = null;
        $scope.dataMappingsPanel[0].subField = null;
        $scope.dataMappingsPanel[0].destination = null;
        $scope.dataMappingsPanel[0].field = null;
    };

    $scope.dataMappingRemove = function (dataMapping) {
        var index = $scope.dataMappingsPanel.indexOf(dataMapping);
        $scope.dataMappingsPanel.splice(index, 1);
    };

    $scope.dataTransformationAdd = function () {
        $scope.dataTransformationsPanel.push({ dataTransformationDocTypes: documentTypes, dataTransformationDocType: $scope.dataTransformationsPanel[0].dataTransformationDocType, dataField: $scope.dataTransformationsPanel[0].dataField, ind1: $scope.dataTransformationsPanel[0].ind1, ind2: $scope.dataTransformationsPanel[0].ind2, subField: $scope.dataTransformationsPanel[0].subField, transformers: transformers, transformer: $scope.dataTransformationsPanel[0].transformer, expression: $scope.dataTransformationsPanel[0].expression, showRemove: true });
        $scope.dataTransformationsPanel[0].dataTransformationDocType = 'bibliographic';
        $scope.dataTransformationsPanel[0].dataField = null;
        $scope.dataTransformationsPanel[0].ind1 = null;
        $scope.dataTransformationsPanel[0].ind2 = null;
        $scope.dataTransformationsPanel[0].subField = null;
        $scope.dataTransformationsPanel[0].transformer = 'regex';
        $scope.dataTransformationsPanel[0].expression = null;
    };

    $scope.dataTransformationRemove = function (dataTransformation) {
        var index = $scope.dataTransformationsPanel.indexOf(dataTransformation);
        $scope.dataTransformationsPanel.splice(index, 1);
    };

    $scope.submit = function () {
        angular.element(document.querySelectorAll('profileSubmit_button'))[0].disabled = true;
        $scope.submited = true;
        removeEmptyValues();
        var profile = {
            "profileName": $scope.mainSectionPanel.profileName,
            "description": $scope.mainSectionPanel.profileDescription,
            "batchProfileMatchPointList": $scope.matchPointsPanel,
            "batchProfileAddOrOverlayList": $scope.addOrOverlayPanel,
            "batchProfileFieldOperationList": $scope.fieldOperationsPanel,
            "batchProfileDataMappingList": $scope.dataMappingsPanel,
            "batchProfileDataTransformerList": $scope.dataTransformationsPanel
        };
        $http.post("/olefs/batchProfile/batchProfileRestController/submit", profile)
            .success(function (data) {
                $scope.profile = data;
                $scope.message = 'Document was successfully submitted.';
            });
    };

    $scope.cancel = function () {
        window.location = '/olefs/portal.jsp';
    };

    var removeEmptyValues = function(){
        $scope.matchPointsPanel.splice(0, 1);
        $scope.addOrOverlayPanel.splice(0, 1);
        $scope.fieldOperationsPanel.splice(0, 1);
        $scope.dataMappingsPanel.splice(0, 1);
        $scope.dataTransformationsPanel.splice(0, 1);
    }


}]);