/**
 * Created by SheikS on 12/20/2015.
 */

var OLENG_CONSTANTS = {
    PROFILE_GET_BIB_STATUS : "rest/describe/getBibStatus",
    PROFILE_GET_CALLNUMBER_TYPES : "rest/describe/getCallNumberTypes",
    PROFILE_GET_ITEM_TYPES : "rest/describe/getItemTypes",
    PROFILE_GET_ITEM_STATUS : "rest/describe/getItemStatus",
    PROFILE_GET_DONOR_CODES : "rest/describe/getDonorCodes",
    PROFILE_GET_LOCATIONS : "rest/describe/getLocations",
    PROFILE_GET_NAMES : "rest/describe/getProfileNames",
    PROFILE_GET_GLOBALLY_PROTECTED_FIELDS : "rest/describe/getGloballyProtectedFields",
    PROFILE_GET_ORDER_FIELD_VALUES : "rest/describe/getOrderImportFieldValues",
    PROFILE_GET_DROP_DOWN_VALUES : "rest/describe/getValuesForDropDown",

    /*Profile Actions*/
    PROFILE_SEARCH : "../../rest/describe/profile/search",
    PROFILE_EDIT : "rest/describe/profile/edit",
    PROFILE_SUBMIT : "rest/describe/profile/submit",
    PROFILE_DELETE : "../../rest/describe/profile/delete",
    PROFILE_CANCEL : "portal.jsp",
    PROFILE_IMPORT : "../../rest/describe/profile/import",

    PROCESS_SUBMIT : "rest/batch/process/submit",
    PROCESS_LAUNCH : "rest/batch/process/launch",
    PROCESS_JOBS : "rest/describe/getBatchProcessJobs",
    BATCH_JOBS : "rest/describe/getBatchJobs",
    DESTROY_PROCESS : "rest/batch/process/destroy"

};

var BATCH_CONSTANTS = {
    PROFILE_TYPES : [
        {id: 'orderRecordImport', value: 'Order Record Import'},
        {id: 'invoiceImport', value: 'Invoice Import'},
        {id: 'bibImport', value: 'Bib Import'}
    ]

};
