/*
 * Copyright 2005-2014 The Kuali Foundation
 *
 * Licensed under the Educational Community License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.opensource.org/licenses/ecl2.php
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
/**
 * Common constants and variables for KRAD
 */
function JavascriptKradVariables() {
}

JavascriptKradVariables.prototype = {
    ACTIVE_CLASS: ".active",
    APPLICATION_FOOTER_WRAPPER: "Uif-ApplicationFooter-Wrapper",
    APP_ID: "Uif-Application",
    APPLICATION_URL: "applicationUrl",

    ATTRIBUTES: {
        DATA_OPEN: "data-open"
    },

    CACHE_KEY: "cacheKey",
    CHANGED_HEADER_ICON_CLASS: "uif-changedHeaderIcon",
    CHANGE_COMPONENT_PROPERTIES: "changeProperties",
    CLEAR_FORM_METHOD_TO_CALL: "clearForm",
    CLIENT_MESSAGE_ITEMS_CLASS: "uif-clientMessageItems",
    CLIENT_WARNING_DIV_CLASS: "uif-clientWarningDiv",
    COLLAPSED_ERRORS_CLASS: "uif-collapsedErrors",
    COLLAPSED_INFO_CLASS: "uif-collapsedInfo",
    COLLAPSED_WARNINGS_CLASS: "uif-collapsedWarnings",
    COLLECTION_ITEM_CLASS: "uif-collectionItem",
    CONTROL_CLASS: "Uif-Application",
    COUNTDOWN_CLASS: "hasCountdown",

    // constants for data role attribute values
    DATA_ROLES: {
        DISCLOSURE_LINK: "disclosureLink"
    },

    DIRTY_CLASS: "dirty",
    DISABLE_BROWSER_CACHE: "disableBrowserCache",
    DISABLED_CLASS: ".disabled",
    DIALOG_PLACEHOLDER: "_dialogPlaceholder",
    ERROR_HIGHLIGHT_SECTION_CLASS: "uif-errorHighlight-section",
    ERROR_MESSAGE_ITEM_CLASS: "uif-errorMessageItem-field",
    FIELD_CLASS: "uif-field",
    FORM_KEY: "formKey",
    GRID_LAYOUT_CELL_CLASS: "uif-gridLayoutCell",
    HAS_ERROR_CLASS: "uif-hasError",
    HAS_INFO_CLASS: "uif-hasInfo",
    HAS_MODIFIED_ERROR_CLASS: "uif-hasError-modified",
    HAS_WARNING_CLASS: "uif-hasWarning",
    HEADER_TEXT_CLASS: "uif-headerText",
    IMAGE_LOCATION: "kradImageLocation",

    // constants for id suffixes
    ID_SUFFIX: {
        DISCLOSURE_CONTENT: "_disclosureContent",
        DISCLOSURE_TOGGLE: "_toggle"
    },

    PAGE_ID: "pageId",
    PORTAL_IFRAME_ID: "iframeportlet",
    INCIDENT_REPORT_VIEW_CLASS: "Uif-IncidentReportView",
    INFO_HIGHLIGHT_SECTION_CLASS: "uif-infoHighlight-section",
    INFO_MESSAGE_ITEM_CLASS: "uif-infoMessageItem-field",
    INPUT_FIELD_SELECTOR: "[data-role:'InputField']",
    KEEP_SESSION_ALIVE_METHOD_TO_CALL: "keepSessionAlive",
    KRAD_URL: "kradUrl",
    KUALI_FORM: "kualiForm",
    LIGHTBOX_PARAM: "lightbox",
    MESSAGE_COUNT_CLASS: "uif-messageCount",
    MESSAGE_KEY_DIRTY_FIELDS: "message.dirtyFields",
    MESSAGE_ERROR: "message.error",
    MESSAGE_ERROR_FIELD_MODIFIED: "message.errorFieldModified",
    MESSAGE_WARNING: "message.warning",
    MESSAGE_INFORMATION: "message.information",
    MESSAGE_DETAILS: "message.details",
    MESSAGE_CLOSE_DETAILS: "message.closeDetails",
    MESSAGE_LOADING: "message.loading",
    MESSAGE_CHANGE: "message.change",
    MESSAGE_FORM_CONTAINS_ERRORS: "message.formContainsErrors",
    MESSAGE_BEFORE: "message.before",
    MESSAGE_AFTER: "message.after",
    MESSAGE_PLEASE_ENTER_VALUE: "message.pleaseEnterValue",
    MESSAGE_EXPAND: "message.expand",
    MESSAGE_COLLAPSE: "message.collapse",
    MESSAGE_SERVER_RESPONSE_ERROR: "message.serverResponseError",
    MESSAGE_STATUS_ERROR: "message.statusError",
    MESSAGE_TOTAL_ERROR: "message.totalError",
    MESSAGE_TOTAL_ERRORS: "message.totalErrors",
    MESSAGE_TOTAL_OTHER_MESSAGES: "message.totalOtherMessages",
    MESSAGE_TOTAL_WARNING: "message.totalWarning",
    MESSAGE_TOTAL_WARNINGS: "message.totalWarnings",
    MESSAGE_TOTAL_MESSAGE: "message.totalMessage",
    MESSAGE_TOTAL_MESSAGES: "message.totalMessages",
    MESSAGE_THE: "message.the",
    MESSAGE_THE_SECTION_HAS_COUNT: "message.theSectionHasCount",
    NAVIGATION_ID: "Uif-Navigation",
    NAVIGATION_MENU_CLASS: "uif-navigationMenu",
    NAVIGATE_METHOD_TO_CALL: "navigate",
    PAGE_NUMBER_DATA: "num",
    PAGE_CONTENT_WRAPPER: "Uif-PageContentWrapper",
    PAGE_VALIDATION_HEADER_CLASS: "uif-pageValidationHeader",
    PAGE_VALIDATION_MESSAGE_ERROR_CLASS: "uif-pageValidationMessages-error",
    PAGE_VALIDATION_MESSAGE_INFO_CLASS: "uif-pageValidationMessages-info",
    PAGE_VALIDATION_MESSAGE_WARNING_CLASS: "uif-pageValidationMessages-warning",
    PROGRESSIVE_DISCLOSURE_HIGHLIGHT_CLASS: "uif-progressiveDisclosure-highlight",
    REFRESH_METHOD_TO_CALL: "refresh",
    RETRIEVE_MESSAGE_METHOD_TO_CALL: "retrieveMessage",
    RETRIEVE_COLLECTION_PAGE_METHOD_TO_CALL: "retrieveCollectionPage",
    RETRIEVE_ORIGINAL_COMPONENT_METHOD_TO_CALL: "retrieveOriginalComponent",
    RETURN_TYPE_UPDATE_COMPONENT: "update-component",
    RETURN_FROM_LIGHTBOX_METHOD_TO_CALL: "returnFromLightbox",
    RETURN_SELECTED_ACTION_CLASS: "uif-returnSelectedAction",
    REQUIRED_MESSAGE_CLASS: "uif-requiredMessage",
    SAVE_LINE_ACTION_CLASS: "uif-saveLineAction",
    SERVER_MESSAGE_ITEMS_CLASS: "uif-serverMessageItems",
    SESSION_TIMEOUT_WARNING_DIALOG: "Uif-SessionTimeoutWarning-DialogGroup",
    SESSION_TIMEOUT_DIALOG: "Uif-SessionTimeout-DialogGroup",
    SESSION_TIMEOUT_WARNING_TIMER: "sessionTimeoutWarningTimer",
    SESSION_TIMEOUT_TIMER: "sessionTimeoutTimer",
    SHOW_DIALOG_EVENT: "showdialog.uif",
    SINGLE_PAGE_VIEW: "singlePageView",
    STACKED_COLLECTION_LAYOUT_CLASS: "uif-stackedCollectionLayout",
    STICKY_CLASS: "uif-sticky",
    SUCCESS_RESPONSE: "success",
    VIEW_HEADER_UPDATE: "Uif-ViewHeaderUpdate",
    TOP_GROUP_UPDATE: "Uif-TopGroupUpdate",
    TABLE_COLLECTION_LAYOUT_CLASS: "uif-tableCollectionLayout",
    TAB_GROUP_CLASS: "Uif-TabGroup",
    TAB_MENU_CLASS: "uif-tabMenu",
    TOOLTIP_CLASS: "uif-tooltip",
    VALIDATION_IMAGE_CLASS: "uif-validationImage",
    SERVER_MESSAGES: "server_messages",
    VIEW_CONTENT_WRAPPER: "Uif-ViewContentWrapper",
    VALIDATION_MESSAGES: "validation_messages",
    VALIDATION_MESSAGES_CLASS: "uif-validationMessagesList",
    VALIDATION_PAGE_HEADER_CLASS: "uif-pageValidationHeader",
    VALIDATION_SETUP_EVENT: "validationSetup",
    GROUP_VALIDATION_DEFAULTS: "group_validation_defaults",
    FIELD_VALIDATION_DEFAULTS: "field_validation_defaults",
    PAGE_LOAD_EVENT: "pageLoad",
    VIEW_CONTENT_HEADER_CLASS: "Uif-ViewContentWrapper",
    VIEW_STATE: "ViewState",
    WARNING_HIGHLIGHT_SECTION_CLASS: "uif-warningHighlight-section",
    WARNING_MESSAGE_ITEM_CLASS: "uif-warningMessageItem-field",
    GROUP_CLASS: "uif-group",
    ROW_DETAILS_CLASS: "uif-rowDetails",
    NEXT_INPUT: "NEXT_INPUT:",
    SKIP_TOTAL: "skip_total",
    ADD_CONTROLS: "add_controls",
    SUBMIT_DATA: "submit_data",
    DETAILS_DEFAULT_OPEN: "details_default_open",
    BUBBLEPOPUP_THEME_PATH: "/plugins/tooltip/jquerybubblepopup-theme/",
    FORM_BUBBLEPOPUP_DEFAULT_OPTIONS: {
        position: 'bottom',
        align: 'left',
        tail: { align: 'left', hidden: false },
        manageMouseEvents: false,
        themeName: 'popup-form'
    }
}

var kradVariables = new JavascriptKradVariables();