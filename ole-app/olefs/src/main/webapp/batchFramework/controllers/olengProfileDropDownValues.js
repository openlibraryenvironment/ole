var batchProcessTypeValues = [
    {id: 'bibImport', name: 'Bib Import'},
    {id: 'orderRecordImport', name: 'Order Record Import'}
];

var requisitionForTitlesValues = [
    {id: 'oneReqPerTitle', name: 'One Requisition Per Title'},
    {id: 'oneReqWithAllTitles', name: 'One Requisition With All Titles'}
];

var documentTypes = [
    {id: 'bibliographic', name: 'Bibliographic'},
    {id: 'holdings', name: 'Holdings'},
    {id: 'item', name: 'Item'},
    {id: 'eHoldings', name: 'EHoldings'}
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

var constantsAndDefaultsOrderFields = [
    {id: 'accountNumber', name: 'Account Number'},
    {id: 'vendorCustomerNumber', name: 'Acquisition Unit\'s Vendor account / Vendor Info Customer #'},
    {id: 'assignToUser', name: 'Assign To User'},
    {id: 'buildingCode', name: 'Building Code'},
    {id: 'deliveryBuildingRoomNumber', name: 'Building Room Number'},
    {id: 'caption', name: 'Caption'},
    {id: 'chartCode', name: 'Chart Code'},
    {id: 'contractManager', name: 'Contract Manager'},
    {id: 'costSource', name: 'Cost Source'},
    {id: 'defaultLocation', name: 'Default Location'},
    {id: 'deliveryCampusCode', name: 'Delivery Campus Code'},
    {id: 'discount', name: 'Discount'},
    {id: 'discountType', name: 'Discount Type'},
    {id: 'fundCode', name: 'Fund Code'},
    {id: 'fundingSource', name: 'Funding Source'},
    {id: 'itemChartCode', name: 'Item Chart Code'},
    {id: 'itemStatus', name: 'Item Status'},
    {id: 'listPrice', name: 'List Price'},
    {id: 'methodOfPOTransmission', name: 'Method Of PO Transmission'},
    {id: 'itemNoOfParts', name: 'No Of Parts'},
    {id: 'financialObjectCode', name: 'Object Code'},
    {id: 'orderType', name: 'Order Type'},
    {id: 'orgCode', name: 'Org Code'},
    {id: 'payReqPositiveApprovalReq', name: 'Pay Req Positive Approval Req'},
    {id: 'percent', name: 'Percent'},
    {id: 'purchaseOrderConfirmationIndicator', name: 'Purchase Order Confirmation Indicator'},
    {id: 'quantity', name: 'Quantity'},
    {id: 'receivingRequired', name: 'Receiving Required'},
    {id: 'RecurringPaymentBeginDate', name: 'Recurring Payment Begin Date'},
    {id: 'RecurringPaymentEndDate', name: 'Recurring Payment End Date'},
    {id: 'RecurringPaymentType', name: 'Recurring Payment Type'},
    {id: 'requestSourceType', name: 'Request Source'},
    {id: 'requestorName', name: 'Requestor Name'},
    {id: 'routeToRequestor', name: 'Route To Requestor'},
    {id: 'useTaxIndicator', name: 'Use Tax Indicator'},
    {id: 'vendorAliasName', name: 'Vendor Alias Name'},
    {id: 'vendorChoice', name: 'Vendor Choice'},
    {id: 'vendorNumber', name: 'Vendor Number'},
    {id: 'vendorProfileCode', name: 'Vendor Profile Code'},
    {id: 'vendorReferenceNumber', name: 'Vendor Reference Number'},
    {id: 'volumeNumber', name: 'Volume Number'}
];

var dataMappingOrderFields = [
    {id: 'accountNumber', name: 'Account Number'},
    {id: 'vendorCustomerNumber', name: 'Acquisition Unit\'s Vendor account / Vendor Info Customer #'},
    {id: 'assignToUser', name: 'Assign To User'},
    {id: 'buildingCode', name: 'Building Code'},
    {id: 'deliveryBuildingRoomNumber', name: 'Building Room Number'},
    {id: 'caption', name: 'Caption'},
    {id: 'chartCode', name: 'Chart Code'},
    {id: 'contractManager', name: 'Contract Manager'},
    {id: 'costSource', name: 'Cost Source'},
    {id: 'defaultLocation', name: 'Default Location'},
    {id: 'deliveryCampusCode', name: 'Delivery Campus Code'},
    {id: 'discount', name: 'Discount'},
    {id: 'discountType', name: 'Discount Type'},
    {id: 'donorCode', name: 'Donor Code'},
    {id: 'formatTypeName', name: 'Format'},
    {id: 'fundCode', name: 'Fund Code'},
    {id: 'fundingSource', name: 'Funding Source'},
    {id: 'itemChartCode', name: 'Item Chart Code'},
    {id: 'itemStatus', name: 'Item Status'},
    {id: 'listPrice', name: 'List Price'},
    {id: 'methodOfPOTransmission', name: 'Method Of PO Transmission'},
    {id: 'Miscellaneous/Other Note', name: 'Miscellaneous/Other Note'},
    {id: 'itemNoOfParts', name: 'No Of Parts'},
    {id: 'financialObjectCode', name: 'Object Code'},
    {id: 'orderType', name: 'Order Type'},
    {id: 'orgCode', name: 'Org Code'},
    {id: 'payReqPositiveApprovalReq', name: 'Pay Req Positive Approval Req'},
    {id: 'percent', name: 'Percent'},
    {id: 'purchaseOrderConfirmationIndicator', name: 'Purchase Order Confirmation Indicator'},
    {id: 'quantity', name: 'Quantity'},
    {id: 'Receipt Note', name: 'Receipt Note'},
    {id: 'receivingRequired', name: 'Receiving Required'},
    {id: 'RecurringPaymentBeginDate', name: 'Recurring Payment Begin Date'},
    {id: 'RecurringPaymentEndDate', name: 'Recurring Payment End Date'},
    {id: 'RecurringPaymentType', name: 'Recurring Payment Type'},
    {id: 'requestorName', name: 'Requestor Name'},
    {id: 'Requestor Note', name: 'Requestor Note'},
    {id: 'routeToRequestor', name: 'Route To Requestor'},
    {id: 'Selector Note', name: 'Selector Note'},
    {id: 'Special Processing Instruction Note', name: 'Special Processing Instruction Note'},
    {id: 'useTaxIndicator', name: 'Use Tax Indicator'},
    {id: 'vendorAliasName', name: 'Vendor Alias Name'},
    {id: 'vendorChoice', name: 'Vendor Choice'},
    {id: 'Vendor Instructions Note', name: 'Vendor Instructions Note'},
    {id: 'vendorNumber', name: 'Vendor Number'},
    {id: 'vendorProfileCode', name: 'Vendor Profile Code'},
    {id: 'vendorReferenceNumber', name: 'Vendor Reference Number'},
    {id: 'volumeNumber', name: 'Volume Number'}
];

var constantOrDefaultTypes = [
    {id: 'constant', name: 'Constant'},
    {id: 'default', name: 'Default'}
];

var mainSection = {
    title: 'Main Section',
    batchProcessTypeValues: batchProcessTypeValues,
    requisitionForTitlesValues: requisitionForTitlesValues,
    marcOnly: false,
    collapsed: false
};

var matchPoint = {
    title: 'Match Points',
    matchPointDocTypes: documentTypes,
    matchPointDocType: 'Bibliographic',
    holdingsMatchPoints: holdingsMatchPoints,
    itemMatchPoints: itemMatchPoints,
    eHoldingsMatchPoints: eHoldingsMatchPoints,
    isAddLine: false,
    collapsed: true
};

var addOrOverlay = {
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
};

var dataMapping = {
    title: 'Data Mappings',
    dataMappingDocTypes: documentTypes,
    dataMappingDocType: 'Bibliographic',
    destinations: destinationDocumentTypes,
    holdingsFields: holdingsMatchPoints,
    itemFields: itemMatchPoints,
    eHoldingsFields: eHoldingsMatchPoints,
    dataMappingOrderFields: dataMappingOrderFields,
    priority: 1,
    isAddLine: false,
    collapsed: true
};

var dataTransformation = {
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
};

var constantAndDefault = {
    title: 'Constants And Defaults',
    constantsAndDefaultsOrderFields: constantsAndDefaultsOrderFields,
    constantOrDefaultTypes: constantOrDefaultTypes,
    constantOrDefault: 'Constant',
    isAddLine: false,
    collapsed: true
};

var fieldOperation = {
    title: 'Field Operations',
    fieldOperationTypes: fieldOperations,
    fieldOperationType: 'Profile Protected Field',
    ignoreGPF: false,
    isAddLine: false,
    collapsed: true
};