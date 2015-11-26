var app = angular.module('batchProcessProfile', ['ngAnimate', 'ngSanitize', 'mgcrea.ngStrap']);

var documentTypes = [
    {id: 'bibliographic', name: 'Bibliographic'},
    {id: 'holdings', name: 'Holdings'},
    {id: 'item', name: 'Item'},
    {id: 'eHoldings', name: 'EHoldings'}
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

app.controller('batchProfileController', function($scope) {

    $scope.matchPointsPanel = [
        {title:'Match Points',  matchPointDocTypes: documentTypes, matchPointDocType: 'bibliographic'}
    ];

    $scope.fieldOperationsPanel = [
        {title:'Field Operations',  fieldOperationTypes: fieldOperations, fieldOperationType: 'global'}
    ];

    $scope.dataMappingsPanel = [
        {title:'Data Mappings',  dataMappingDocTypes: documentTypes, dataMappingDocType: 'bibliographic', destinations: documentTypes, destination: 'bibliographic', fields: fields, destination: 'url'}
    ];

    $scope.dataTransformationsPanel = [
        {title:'Data Transformations',  dataTransformationDocTypes: documentTypes, dataTransformationDocType: 'bibliographic', transformers: transformers, transformer: 'regex'}
    ];

    $scope.matchPointsActivePanel = [0];
    $scope.fieldOperationsActivePanel = [];
    $scope.dataMappingsActivePanel = [];
    $scope.dataTransformationsActivePanel = []

    $scope.matchPointAdd = function () {
        $scope.matchPointsPanel.push({ matchPointDocTypes: documentTypes, matchPointDocType: 'bibliographic'});
    };

    $scope.matchPointRemove = function (matchPoint) {
        var index = $scope.matchPointsPanel.indexOf(matchPoint);
        $scope.matchPointsPanel.splice(index, 1);
    };

    $scope.fieldOperationAdd = function () {
        $scope.fieldOperationsPanel.push({ fieldOperationTypes: fieldOperations, fieldOperationType: 'global'});
    };

    $scope.fieldOperationRemove = function (fieldOperation) {
        var index = $scope.fieldOperationsPanel.indexOf(fieldOperation);
        $scope.fieldOperationsPanel.splice(index, 1);
    };

    $scope.dataMappingAdd = function () {
        $scope.dataMappingsPanel.push({ dataMappingDocTypes: documentTypes, dataMappingDocType: 'bibliographic', destinations: documentTypes, destination: 'bibliographic', fields: fields, destination: 'url'});
    };

    $scope.dataMappingRemove = function (dataMapping) {
        var index = $scope.dataMappingsPanel.indexOf(dataMapping);
        $scope.dataMappingsPanel.splice(index, 1);
    };

    $scope.dataTransformationAdd = function () {
        $scope.dataTransformationsPanel.push({ dataTransformationDocTypes: documentTypes, dataTransformationDocType: 'bibliographic', transformers: transformers, transformer: 'regex'});
    };

    $scope.dataTransformationRemove = function (dataTransformation) {
        var index = $scope.dataTransformationsPanel.indexOf(dataTransformation);
        $scope.dataTransformationsPanel.splice(index, 1);
    };

});
