var batchProcessTypeValues = [
    {id: 'bibImport', name: 'Bib Import'},
    {id: 'orderRecordImport', name: 'Order Record Import'},
    {id: 'invoiceImport', name: 'Invoice Import'}
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

var dataMappingProcessTypes = [
    {id: 'bibMarc', name: 'Bib Marc'},
    {id: 'constant', name: 'Constant'}
];

var transformationDocumentTypes = [
    {id: 'bibMarc', name: 'Bib Marc'}
];

var destinationDocumentTypes = [
    {id: 'holdings', name: 'Holdings'},
    {id: 'item', name: 'Item'},
    {id: 'eHoldings', name: 'EHoldings'}
];

var dataMappingObject = {
    destinationForBibMarc : [
        {id: 'holdings', name: 'Holdings'},
        {id: 'item', name: 'Item'},
        {id: 'eHoldings', name: 'EHoldings'}
    ],
    destinationForConstant : [
        {id: 'bibliographic', name: 'Bibliographic'},
        {id: 'holdings', name: 'Holdings'},
        {id: 'item', name: 'Item'},
        {id: 'eHoldings', name: 'EHoldings'}
    ],
    destinationFieldsForBibMarcHoldings : [
        {id: 'callNumber', name: 'Call Number'},
        {id: 'callNumberPrefix', name: 'Call Number Prefix'},
        {id: 'callNumberType', name: 'Call Number Type'},
        {id: 'copyNumber', name: 'Copy Number'},
        {id: 'locationLevel1', name: 'Location Level1'},
        {id: 'locationLevel2', name: 'Location Level2'},
        {id: 'locationLevel3', name: 'Location Level3'},
        {id: 'locationLevel4', name: 'Location Level4'},
        {id: 'locationLevel5', name: 'Location Level5'}
    ],
    destinationFieldsForBibMarcItems : [
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
    ],
    destinationFieldsForBibMarcEHoldings :  [
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
        {id: 'eResourceId', name: 'EResource Id'},
        {id: 'Imprint', name: 'Imprint'},
        {id: 'noOfSimultaneousUser', name: 'No. of Simultaneous User'},
        {id: 'accessLocation', name: 'Access Location'},
        {id: 'adminUsername', name: 'Admin Username'},
        {id: 'adminPassword', name: 'Admin Password'},
        {id: 'adminURL', name: 'Admin URL'},
        {id: 'accessUsername', name: 'Access Username'},
        {id: 'accessPassword', name: 'Access Password'},
        {id: 'authenticationType', name: 'Authentication Type'},
        {id: 'proxied', name: 'Proxied'},
        {id: 'prefix', name: 'Prefix'},
        {id: 'coverageStartDateRelativeFormat', name: 'Coverage Start Date Relative Format'},
        {id: 'coverageEndDateRelativeFormat', name: 'Coverage End Date Relative Format'},
        {id: 'perpetualAccessStartDate', name: 'Perpetual Access Start Date'},
        {id: 'perpetualAccessStartDateRelativeFormat', name: 'Perpetual access Start Date Relative Format'},
        {id: 'perpetualAccessStartVolume', name: 'Perpetual Access Start Volume'},
        {id: 'perpetualAccessStarIssue', name: 'Perpetual Access Start Issue'},
        {id: 'perpetualAccessEndVolume', name: 'Perpetual Access End Volume'},
        {id: 'perpetualAccessEndIssue', name: 'Perpetual Access End Issue'},
        {id: 'perpetualAccessEndDate', name: 'Perpetual Access End Date'},
        {id: 'perpetualAccessEndDateRelativeFormat', name: 'Perpetual Access End Date Relative Format'},
        {id: 'subscriptionStatus', name: 'Subscription Status'},
        {id: 'initialSubscriptionStartDate', name: 'Initial Subscription Start Date'},
        {id: 'currentSubscriptionStartDate', name: 'Current Subscription Start Date'},
        {id: 'currentSubscriptionEndDate', name: 'Current Subscription End Date'},
        {id: 'cancellationDecisionDate', name: 'Cancellation Decision Date'},
        {id: 'cancellationEffectiveDate', name: 'Cancellation Effective Date'},
        {id: 'cancellationReason', name: 'Cancellation Reason'},
        {id: 'publicNote', name: 'Public Note'},
        {id: 'non-publicNote', name: 'Non-public Note'}
    ],
    destinationFieldsForConstantsBib : [
        {id: 'bibStatus', name: 'Bib Status'},
        {id: 'staffOnly', name: 'Staff Only'}
    ],
    destinationFieldsForConstantsHoldings : [
        {id: 'receiptStatus', name: 'Receipt Status'},
        {id: 'subscriptionStatus', name: 'Subscription Status'},
        {id: 'accessStatus', name: 'Access Status'},
        {id: 'staffOnly', name: 'Staff Only'},
        {id: 'callNumber', name: 'Call Number'},
        {id: 'callNumberPrefix', name: 'Call Number Prefix'},
        {id: 'callNumberType', name: 'Call Number Type'},
        {id: 'copyNumber', name: 'Copy Number'},
        {id: 'locationLevel1', name: 'Location Level1'},
        {id: 'locationLevel2', name: 'Location Level2'},
        {id: 'locationLevel3', name: 'Location Level3'},
        {id: 'locationLevel4', name: 'Location Level4'},
        {id: 'locationLevel5', name: 'Location Level5'}
    ],
    destinationFieldsForConstantsItems :  [
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
    ],
    destinationFieldsForConstantsEHoldings : [
        {id: 'accessStatus', name: 'Access Status'},
        {id: 'staffOnly', name: 'Staff Only'},
        {id: 'callNumber', name: 'Call Number'},
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
        {id: 'eResourceId', name: 'EResource Id'},
        {id: 'Imprint', name: 'Imprint'},
        {id: 'noOfSimultaneousUser', name: 'No. of Simultaneous User'},
        {id: 'accessLocation', name: 'Access Location'},
        {id: 'adminUsername', name: 'Admin Username'},
        {id: 'adminPassword', name: 'Admin Password'},
        {id: 'adminURL', name: 'Admin URL'},
        {id: 'accessUsername', name: 'Access Username'},
        {id: 'accessPassword', name: 'Access Password'},
        {id: 'authenticationType', name: 'Authentication Type'},
        {id: 'proxied', name: 'Proxied'},
        {id: 'prefix', name: 'Prefix'},
        {id: 'coverageStartDateRelativeFormat', name: 'Coverage Start Date Relative Format'},
        {id: 'coverageEndDateRelativeFormat', name: 'Coverage End Date Relative Format'},
        {id: 'perpetualAccessStartDate', name: 'Perpetual Access Start Date'},
        {id: 'perpetualAccessStartDateRelativeFormat', name: 'Perpetual access Start Date Relative Format'},
        {id: 'perpetualAccessStartVolume', name: 'Perpetual Access Start Volume'},
        {id: 'perpetualAccessStarIssue', name: 'Perpetual Access Start Issue'},
        {id: 'perpetualAccessEndVolume', name: 'Perpetual Access End Volume'},
        {id: 'perpetualAccessEndIssue', name: 'Perpetual Access End Issue'},
        {id: 'perpetualAccessEndDate', name: 'Perpetual Access End Date'},
        {id: 'perpetualAccessEndDateRelativeFormat', name: 'Perpetual Access End Date Relative Format'},
        {id: 'subscriptionStatus', name: 'Subscription Status'},
        {id: 'initialSubscriptionStartDate', name: 'Initial Subscription Start Date'},
        {id: 'currentSubscriptionStartDate', name: 'Current Subscription Start Date'},
        {id: 'currentSubscriptionEndDate', name: 'Current Subscription End Date'},
        {id: 'cancellationDecisionDate', name: 'Cancellation Decision Date'},
        {id: 'cancellationEffectiveDate', name: 'Cancellation Effective Date'},
        {id: 'cancellationReason', name: 'Cancellation Reason'},
        {id: 'publicNote', name: 'Public Note'},
        {id: 'non-publicNote', name: 'Non-public Note'}
    ]
};

var matchPointObject = {
    matchPointTypeForHoldings : [
        {id: 'callNumber', name: 'Call Number'},
        {id: 'callNumberPrefix', name: 'Call Number Prefix'},
        {id: 'callNumberType', name: 'Call Number Type'},
        {id: 'copyNumber', name: 'Copy Number'},
        {id: 'locationLevel1', name: 'Location Level1'},
        {id: 'locationLevel2', name: 'Location Level2'},
        {id: 'locationLevel3', name: 'Location Level3'},
        {id: 'locationLevel4', name: 'Location Level4'},
        {id: 'locationLevel5', name: 'Location Level5'}
    ],
    matchPointTypeForItem : [
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
    ],
    matchPointTypeForEHoldings : [
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
    ]
};

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


var eHoldingsDataMappings = [
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
    {id: 'eResourceId', name: 'EResource Id'},
    {id: 'Imprint', name: 'Imprint'},
    {id: 'noOfSimultaneousUser', name: 'No. of Simultaneous User'},
    {id: 'accessLocation', name: 'Access Location'},
    {id: 'adminUsername', name: 'Admin Username'},
    {id: 'adminPassword', name: 'Admin Password'},
    {id: 'adminURL', name: 'Admin URL'},
    {id: 'accessUsername', name: 'Access Username'},
    {id: 'accessPassword', name: 'Access Password'},
    {id: 'authenticationType', name: 'Authentication Type'},
    {id: 'proxied', name: 'Proxied'},
    {id: 'prefix', name: 'Prefix'},
    {id: 'coverageStartDateRelativeFormat', name: 'Coverage Start Date Relative Format'},
    {id: 'coverageEndDateRelativeFormat', name: 'Coverage End Date Relative Format'},
    {id: 'perpetualAccessStartDate', name: 'Perpetual Access Start Date'},
    {id: 'perpetualAccessStartDateRelativeFormat', name: 'Perpetual access Start Date Relative Format'},
    {id: 'perpetualAccessStartVolume', name: 'Perpetual Access Start Volume'},
    {id: 'perpetualAccessStarIssue', name: 'Perpetual Access Start Issue'},
    {id: 'perpetualAccessEndVolume', name: 'Perpetual Access End Volume'},
    {id: 'perpetualAccessEndIssue', name: 'Perpetual Access End Issue'},
    {id: 'perpetualAccessEndDate', name: 'Perpetual Access End Date'},
    {id: 'perpetualAccessEndDateRelativeFormat', name: 'Perpetual Access End Date Relative Format'},
    {id: 'subscriptionStatus', name: 'Subscription Status'},
    {id: 'initialSubscriptionStartDate', name: 'Initial Subscription Start Date'},
    {id: 'currentSubscriptionStartDate', name: 'Current Subscription Start Date'},
    {id: 'currentSubscriptionEndDate', name: 'Current Subscription End Date'},
    {id: 'cancellationDecisionDate', name: 'Cancellation Decision Date'},
    {id: 'cancellationEffectiveDate', name: 'Cancellation Effective Date'},
    {id: 'cancellationReason', name: 'Cancellation Reason'},
    {id: 'publicNote', name: 'Public Note'},
    {id: 'non-publicNote', name: 'Non-public Note'}
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

var addOrOverlayFields = [
    {id: 'bibStatus', name: 'Bib Status'},
    {id: 'staffOnly', name: 'Staff Only'}
];

var addOrOverlayFieldOperations = [
    {id: 'equalsTo', name: 'Equals To'},
    {id: 'notEqualsTo', name: 'Not Equals To'}
];

var bibDoNotMatchOperations = [
    {id: 'add', name: 'Add'},
    {id: 'discard', name: 'Discard'}
];

var doNotMatchOperations = [
    {id: 'add', name: 'Add'},
    {id: 'discard', name: 'Discard'}
];

var addOperations = [
    {id: 'deleteAll', name: 'Delete all existing and add'},
    {id: 'keepAll', name: 'Keep all existing and add'}
];

var createMultiple = [
    {id: 'createMultiple', name: 'Create multiple'},
    {id: 'createMultipleAndDeleteAllExisting', name: 'Create multiple, Delete all existing and add'},
    {id: 'createMultipleAndKeepAllExisting', name: 'Create multiple, Keep all existing and add'},
    {id: 'deleteAll', name: 'Delete all existing and add'},
    {id: 'keepAll', name: 'Keep all existing and add'}
];

var overlayMultiple = [
    {id: 'overlayMultiple', name: 'Overlay multiple'}
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

var actionTypes = [
    {id: 'all', name: 'All'},
    {id: 'new', name: 'New'},
    {id: 'overlay', name: 'Overlay'}
];


var booleanOptions = [
    {id: 'true', value: 'True'},
    {id: 'false', value: 'False'}
];

var booleanOptionsYorN = [
    {id: 'y', value: 'Y'},
    {id: 'n', value: 'N'}
];

var transferOptions = [
    {id: 'preMarcTransformation', name: 'Pre Marc Transformation'},
    {id: 'postMarcTransformation', name: 'Post Marc Transformation'}
];

var transformationOperations = [
    {id: 'new', name: 'New'},
    {id: 'deleteTag', name: 'Delete Tag'},
    {id: 'deleteValue', name: 'Delete Value'},
    {id: 'replace', name: 'Replace'},
    {id: 'move', name: 'Move'},
    {id: 'prepend', name: 'Prepend with Prefix'}
];

var invoiceFields = [
    {id: 'accountNumber', name: 'Account Number'},
    {id: 'bookPlate', name: 'Book Plate'},
    {id: 'currencyType', name: 'Currency Type'},
    {id: 'ebook', name: 'E-Book'},
    {id: 'exchangeRate', name: 'Exchange Rate'},
    {id: 'fundCode', name: 'Fund Code'},
    {id: 'invoiceDate', name: 'Invoice Date'},
    {id: 'invoiceNumber', name: 'Invoice Number'},
    {id: 'foreignListPrice', name: 'Invoiced Foreign Price'},
    {id: 'listPrice', name: 'Invoiced Price'},
    {id: 'itemDescription', name: 'Item Description'},
    {id: 'financialObjectCode', name: 'Object Code'},
    {id: 'quantity', name: 'Quantity'},
    {id: 'requestor', name: 'Requestor'},
    {id: 'vendorItemIdentifier', name: 'Vendor Item Identifier'},
    {id: 'vendorNumber', name: 'Vendor Number'}
];

var orderFields = [
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
    {id: 'requestSourceType', name: 'Request Source'},
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
    {id: 'vendorItemIdentifier', name: 'Vendor Item Identifier'},
    {id: 'volumeNumber', name: 'Volume Number'}
];

var discountTypes = [
    {id: '%', value: '%'},
    {id: '#', value: '#'}
];

var matchedOrderOperations = [
    {id: 'createReqAndPo', name: 'Create Requisition and PO'},
    {id: 'createReqOnly', name: 'Create Requisition Only'},
    {id: 'createReqOrPo', name: 'Create neither a Requisition nor a PO'}
];

var unmatchedOrderOperations = [
    {id: 'createReqAndPo', name: 'Create Requisition and PO'},
    {id: 'createReqOnly', name: 'Create Requisition Only'}
];

var mainSection = {
    title: 'Main Section',
    batchProcessTypeValues: batchProcessTypeValues,
    requisitionForTitlesValues: requisitionForTitlesValues,
    requisitionForTitlesOption: 'One Requisition Per Title',
    marcOnly: false,
    collapsed: false
};

var matchPoint = {
    title: 'Match Points',
    matchPointDocTypes: documentTypes,
    matchPointOrderDocTypes: dataMappingProcessTypes,
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
    addOrOverlayFields: addOrOverlayFields,
    addOrOverlayFieldOperations: addOrOverlayFieldOperations,
    linkFields: dataMappingObject.destinationFieldsForBibMarcHoldings,
    operation: 'Add',
    bibDoNotMatchOperations: bibDoNotMatchOperations,
    doNotMatchOperations: doNotMatchOperations,
    addOperations: addOperations,
    matchedOrderOperations: matchedOrderOperations,
    unmatchedOrderOperations: unmatchedOrderOperations,
    addItems: false,
    isAddLine: false,
    collapsed: true
};

var dataMapping = {
    title: 'Data Mappings',
    dataMappingDocTypes: dataMappingProcessTypes,
    dataMappingDocType: 'Bib Marc',
    destinations: destinationDocumentTypes,
    holdingsFields: holdingsMatchPoints,
    itemFields: itemMatchPoints,
    eHoldingsFields: eHoldingsDataMappings,
    discountTypes: discountTypes,
    transferOptions: transferOptions,
    transferOption: 'Pre Marc Transformation',
    priority: 1,
    isMultiValue: false,
    isAddLine: false,
    collapsed: true
};

var dataMappingOrder = {
    title: 'Data Mappings',
    dataMappingDocTypes: dataMappingProcessTypes,
    dataMappingDocType: 'Bib Marc',
    destinations: destinationDocumentTypes,
    holdingsFields: holdingsMatchPoints,
    itemFields: itemMatchPoints,
    eHoldingsFields: eHoldingsDataMappings,
    dataMappingFields: orderFields,
    discountTypes: discountTypes,
    transferOptions: transferOptions,
    transferOption: 'Pre Transfer',
    priority: 1,
    isAddLine: false,
    collapsed: true
};

var dataMappingInvoice = {
    title: 'Data Mappings',
    dataMappingDocTypes: dataMappingProcessTypes,
    dataMappingDocType: 'Bib Marc',
    destinations: destinationDocumentTypes,
    holdingsFields: holdingsMatchPoints,
    itemFields: itemMatchPoints,
    eHoldingsFields: eHoldingsDataMappings,
    dataMappingFields: invoiceFields,
    discountTypes: discountTypes,
    transferOptions: transferOptions,
    transferOption: 'Pre Transfer',
    priority: 1,
    isAddLine: false,
    collapsed: true
};

var dataTransformation = {
    title: 'Data Transformations',
    dataTransformationDocTypes: transformationDocumentTypes,
    dataTransformationDocType: 'Bib Marc',
    dataTransformationActionTypes: actionTypes,
    dataTransformationActionType: 'All',
    dataTransformationOperations: transformationOperations,
    dataTransformationStep: 1,
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