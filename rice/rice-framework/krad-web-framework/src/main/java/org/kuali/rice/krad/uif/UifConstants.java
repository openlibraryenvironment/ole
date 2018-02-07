/**
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
package org.kuali.rice.krad.uif;

/**
 * General constants used within the User Interface Framework
 *
 * @author Kuali Rice Team (rice.collab@kuali.org)
 */
public class UifConstants {
    public static final String CONTROLLER_METHOD_DISPATCH_PARAMETER_NAME = "methodToCall";
    public static final String DEFAULT_MODEL_NAME = "KualiForm";
    public static final String COMPONENT_MODEL_NAME = "Component";
    public static final String DEFAULT_VIEW_NAME = "default";
    public static final String COMPONENT_ID_PREFIX = "u";

    public static final String DEFAULT_THEMES_DIRECTORY = "/themes";
    public static final String DEFAULT_IMAGES_DIRECTORY = "images";
    public static final String DEFAULT_SCRIPTS_DIRECTORY = "scripts";
    public static final String DEFAULT_STYLESHEETS_DIRECTORY = "stylesheets";
    public static final String THEME_DERIVED_PROPERTY_FILE = "theme-derived.properties";
    public static final String THEME_CSS_FILES = "themeCssFiles";
    public static final String THEME_JS_FILES = "themeJsFiles";

    // uncomment for freemarker testing
    public static final String SPRING_VIEW_ID = "/krad/WEB-INF/ftl/uifRender";
    public static final String SPRING_REDIRECT_ID = "/krad/WEB-INF/ftl/redirect";
    public static final String REDIRECT_PREFIX = "redirect:";

    public static final String EL_PLACEHOLDER_PREFIX = "@{";
    public static final String EL_PLACEHOLDER_SUFFIX = "}";
    public static final String NO_BIND_ADJUST_PREFIX = "#form.";
    public static final String DEFAULT_PATH_BIND_ADJUST_PREFIX = "#dp.";
    public static final String FIELD_PATH_BIND_ADJUST_PREFIX = "#fp.";
    public static final String LINE_PATH_BIND_ADJUST_PREFIX = "#lp.";
    public static final String NODE_PATH_BIND_ADJUST_PREFIX = "#np.";
    public static final String STRING_TEMPLATE_PARAMETER_PLACEHOLDER = "@";

    public static final String SPACE = " ";

    public static final String REQUEST_FORM = "requestForm";

    public static final String BLOCKUI_NAVOPTS = "navigation";
    public static final String BLOCKUI_REFRESHOPTS = "refresh";

    public static final String VALIDATE_VIEWS_ONBUILD = "validate.views.onbuild";

    public static final String MESSAGE_VIEW_ID = "Uif-MessageView";
    public static final String SESSION_TIMEOUT_VIEW_ID = "Uif-SessionTimeoutView";
    public static final String LOGGED_OUT_VIEW_ID = "Uif-LoggedOutView";
    public static final String GROUP_VALIDATION_DEFAULTS_MAP_ID = "Uif-GroupValidationMessages-DataDefaults";
    public static final String FIELD_VALIDATION_DEFAULTS_MAP_ID = "Uif-FieldValidationMessages-DataDefaults";
    public static final String REFERER = "Referer";
    public static final String NO_RETURN = "NO_RETURN";

    public static enum ReadOnlyListTypes {
        DELIMITED, BREAK, OL, UL
    }

    public static enum Position {
        BOTTOM, LEFT, RIGHT, TOP
    }

    public static enum Order {
        FIRST, LINE_FIRST, NEXT_INPUT, SELF
    }

    public static enum NavigationType {
        VERTICAL_MENU, HORIZONTAL_TABS
    }

    public static enum Orientation {
        HORIZONTAL, VERTICAL
    }

    public static enum ViewType {
        DEFAULT, DOCUMENT, INQUIRY, LOOKUP, MAINTENANCE, INCIDENT, TRANSACTIONAL;
    }

    public static enum ControlType {
        CHECKBOX, CHECKBOXGROUP, FILE, GROUP, HIDDEN, RADIOGROUP, SELECT,
        TEXTAREA, TEXT, USER
    }

    public static enum WorkflowAction {
        SAVE, ROUTE, BLANKETAPPROVE, APPROVE, DISAPPROVE, CANCEL, FYI, ACKNOWLEDGE, COMPLETE, SENDADHOCREQUESTS
    }

    /**
     * Enum of return types. Used to return the type of response being sent by the server to the client.
     */
    public enum AjaxReturnTypes {
        UPDATEPAGE("update-page"), UPDATECOMPONENT("update-component"), REDIRECT("redirect"),
        UPDATEVIEW("update-view"), UPDATECOLLECTION("update-collection"),
        UPDATENONE("update-none"), DISPLAYLIGHTBOX("display-lightbox"),
        UPDATEDIALOG("update-dialog");

        private String key;

        AjaxReturnTypes(String key) {
            this.key = key;
        }

        public String getKey() {
            return key;
        }

        public void setKey(String key) {
            this.key = key;
        }
    }

    public static class MethodToCallNames {
        public static final String NAVIGATE = "navigate";
        public static final String START = "start";
        public static final String SAVE = "save";
        public static final String SEARCH = "search";
        public static final String CLOSE = "close";
        public static final String ADD_LINE = "addLine";
        public static final String DELETE_LINE = "deleteLine";
        public static final String REFRESH = "refresh";
        public static final String CANCEL = "cancel";
        public static final String SESSION_TIMEOUT = "sessionTimeout";
        public static final String TABLE_JSON = "tableJsonRetrieval";
        public static final String TABLE_DATA = "tableDataRetrieval";
        public static final String TABLE_XML = "tableXmlRetrieval";
        public static final String TABLE_CSV = "tableCsvRetrieval";
        public static final String TABLE_XLS = "tableXlsRetrieval";
    }

    public static class ActionEvents {
        public static final String ADD_LINE = "addLine";
        public static final String ADD_BLANK_LINE = "addBlankLine";
    }

    public static class LayoutComponentOptions {
        public static final String COLUMN_SPAN = "colSpan";
        public static final String ROW_SPAN = "rowSpan";
    }

    public static class RowSelection {
        public static final String ALL = "all";
        public static final String ODD = "odd";
        public static final String EVEN = "even";
    }

    public static class IdSuffixes {
        public static final String ADD_LINE = "_add";
        public static final String CONTROL = "_control";
        public static final String ATTRIBUTE = "_attribute";
        public static final String COLUMN = "_c";
        public static final String COLUMN_SORTS = "_columnSorts";
        public static final String COMPARE = "_comp";
        public static final String CONSTRAINT = "_constraint";
        public static final String DETAIL_LINK = "_detLink";
        public static final String DETAIL_GROUP = "_detGroup";
        public static final String DISCLOSURE_CONTENT = "_disclosureContent";
        public static final String DIRECT_INQUIRY = "_directinquiry";
        public static final String ERRORS = "_errors";
        public static final String INSTRUCTIONAL = "_instructional";
        public static final String LINE = "_line";
        public static final String LABEL = "_label";
        public static final String FIELDSET = "_fieldset";
        public static final String SUB = "_sub";
        public static final String SUGGEST = "_suggest";
        public static final String QUICK_FINDER = "_quickfinder";
        public static final String SPAN = "_span";
        public static final String TAB = "_tab";
    }

    public static class ViewPhases {
        public static final String INITIALIZE = "INITIALIZE";
        public static final String APPLY_MODEL = "APPLY_MODEL";
        public static final String FINALIZE = "FINALIZE";
    }

    public static class ViewStatus {
        public static final String CREATED = "C";
        public static final String INITIALIZED = "I";
        public static final String FINAL = "F";
    }

    public static class ContextVariableNames {
        public static final String COLLECTION_GROUP = "collectionGroup";
        public static final String CONFIG_PROPERTIES = "ConfigProperties";
        public static final String COMPONENT = "component";
        public static final String CONSTANTS = "Constants";
        public static final String DOCUMENT_ENTRY = "DocumentEntry";
        public static final String INDEX = "index";
        public static final String IS_ADD_LINE = "isAddLine";
        public static final String LINE = "line";
        public static final String LINE_SUFFIX = "lineSuffix";
        public static final String READONLY_LINE = "readOnlyLine";
        public static final String MANAGER = "manager";
        public static final String NODE = "node";
        public static final String NODE_PATH = "nodePath";
        public static final String PARENT = "parent";
        public static final String THEME_IMAGES = "ThemeImages";
        public static final String UIF_CONSTANTS = "UifConstants";
        public static final String VIEW = "view";
        public static final String VIEW_HELPER = "ViewHelper";
        public static final String PARENT_LINE = "parentLine";
    }

    public static class TableToolsKeys {
        public static final String AASORTING = "aaSorting";
        public static final String BAUTO_TYPE = "bAutoType";
        public static final String BPROCESSING = "bProcessing";
        public static final String BSERVER_SIDE = "bServerSide";
        public static final String SDOM = "sDom";
        public static final String CELL_CLASS = "sClass";
        public static final String MDATA = "mDataProp";
        public static final String LANGUAGE = "oLanguage";
        public static final String EMPTY_TABLE = "sEmptyTable";
        public static final String AO_COLUMNS = "aoColumns";
        public static final String AO_COLUMN_DEFS = "aoColumnDefs";
        public static final String SORT_SKIP_ROWS = "aiSortingSkipRows";
        public static final String SORT_DATA_TYPE = "sSortDataType";
        public static final String SORTABLE = "bSortable";
        public static final String TARGETS = "aTargets";
        public static final String VISIBLE = "bVisible";
        public static final String SORT_TYPE = "sType";
        public static final String TABLE_SORT = "bSort";
        public static final String SAJAX_SOURCE = "sAjaxSource";
        public static final String FOOTER_CALLBACK = "fnFooterCallback";
        public static final String AA_DATA = "aaData";
        public static final String DEFER_RENDER = "bDeferRender";
        public static final String SDOWNLOAD_SOURCE = "sDownloadSource";
    }

    public static class TableToolsValues {
        public static final String DOM_TEXT = "dom-text";
        public static final String DOM_SELECT = "dom-select";
        public static final String DOM_CHECK = "dom-checkbox";
        public static final String DOM_RADIO = "dom-radio";

        // sort types:

        public static final String NUMERIC = "numeric";
        public static final String STRING = "string";
        public static final String DATE = "kuali_date";
        public static final String PERCENT = "kuali_percent";
        public static final String CURRENCY = "kuali_currency";
        public static final String TIMESTAMP = "kuali_timestamp";

        public static final String FALSE = "false";
        public static final String TRUE = "true";

        public static final int ADD_ROW_DEFAULT_INDEX = 0;
        public static final String JSON_TEMPLATE = "dataTablesJson.ftl";
    }

    public static class TableLayoutValues {
        public static final int ACTIONS_COLUMN_LEFT_INDEX = 1;
        public static final int ACTIONS_COLUMN_RIGHT_INDEX = -1;
    }

    public static class PageRequest {
        public static final String PREV = "prev";
        public static final String NEXT = "next";
        public static final String FIRST = "first";
        public static final String LAST = "last";
        public static final String PAGE_NUMBER = "pageNumber";
        public static final String DISPLAY_START_PROP = "#displayStart";
    }

    public static class TabOptionKeys {
        public static final String ACTIVE = "active";
    }

    public static class TitleAppendTypes {
        public static final String DASH = "dash";
        public static final String PARENTHESIS = "parenthesis";
        public static final String REPLACE = "replace";
        public static final String NONE = "none";
    }

    public static class ComponentProperties {
        public static final String HEADER_TEXT = "headerText";
        public static final String DEFAULT_VALUE = "defaultValue";
        public static final String DEFAULT_VALUES = "defaultValues";
    }

    public static class UrlParams {
        public static final String ACTION_EVENT = "actionEvent";
        public static final String FORM_KEY = "formKey";
        public static final String VIEW_ID = "viewId";
        public static final String PAGE_ID = "pageId";
        public static final String HISTORY = "history";
        public static final String LAST_FORM_KEY = "lastFormKey";
        public static final String LOGIN_USER = "__login_user";
    }

    public static class Messages {
        public static final String VALIDATION_MSG_KEY_PREFIX = "validation.";
        public static final String STATE_PREFIX = "validation.statePrefix";
    }

    public static class MessageKeys {
        public static final String QUERY_DATA_NOT_FOUND = "query.dataNotFound";
        public static final String OPTION_ALL = "option.all";
    }

    public static class ClientSideVariables {
        public static final String KRAD_IMAGE_LOCATION = "kradImageLocation";
        public static final String KRAD_URL = "kradUrl";
        public static final String APPLICATION_URL = "applicationUrl";
    }

    public static class RefreshCallerTypes {
        public static final String LOOKUP = "LOOKUP";
        public static final String MULTI_VALUE_LOOKUP = "MULTI_VALUE_LOOKUP";
        public static final String QUESTION = "QUESTION";
    }

    public static final class HistoryFlow {
        public static final String HISTORY_MANAGER = "historyManager";
        public static final String FLOW = "flow";
        public static final String START = "start";
        public static final String RETURN_TO_START = "returnToStart";
        public static final String SEPARATOR = "@@";
    }

    public static final class RoleTypes {
        public static final String CONTROL = "Control";
        public static final String INPUT_FIELD = "InputField";
        public static final String GROUP = "Group";
        public static final String ROW_GROUPING = "RowGrouping";
    }

    public static final class DataAttributes {
        public static final String TYPE = "type";
        public static final String ROLE = "role";
        public static final String ONCLICK = "onclick";
        public static final String SUBMIT_DATA = "submit_data";
        public static final String HAS_MESSAGES = "has_messages";
        public static final String SERVER_MESSAGES = "server_messages";
        public static final String VALIDATION_MESSAGES = "validation_messages";
        public static final String GROUP_VALIDATION_DEFAULTS = "group_validation_defaults";
        public static final String FIELD_VALIDATION_DEFAULTS = "field_validation_defaults";
        public static final String MESSAGES_FOR = "messages_for";
        public static final String PARENT = "parent";
        public static final String SUMMARIZE = "summarize";
        public static final String DISPLAY_MESSAGES = "displayMessages";
        public static final String COLLAPSE_FIELD_MESSAGES = "collapseFieldMessages";
        public static final String DISPLAY_LABEL = "displayLabel";
        public static final String SHOW_PAGE_SUMMARY_HEADER = "showPageSummaryHeader";
        public static final String DISPLAY_HEADER_SUMMARY = "displayHeaderSummary";
        public static final String IS_TABLE_COLLECTION = "isTableCollection";
        public static final String HAS_OWN_MESSAGES = "hasOwnMessages";
        public static final String PAGE_LEVEL = "pageLevel";
        public static final String FORCE_SHOW = "forceShow";
        public static final String SECTIONS = "sections";
        public static final String ORDER = "order";
        public static final String SERVER_ERRORS = "serverErrors";
        public static final String SERVER_WARNINGS = "serverWarnings";
        public static final String SERVER_INFO = "serverInfo";
        public static final String VIGNORE = "vignore";
        public static final String TOTAL = "total";
        public static final String SKIP_TOTAL = "skip_total";
        public static final String LABEL = "label";
        public static final String GROUP = "group";
        public static final String LABEL_FOR = "label_for";
        public static final String CONTROL_FOR = "control_for";
        public static final String ADD_CONTROLS = "add_controls";
        public static final String HEADER_FOR = "header_for";
        public static final String STICKY_FOOTER = "sticky_footer";
        public static final String DETAILS_DEFAULT_OPEN = "details_default_open";
        public static final String TAB_FOR = "tabfor";
    }

    public static final class CaseConstraintOperators {
        public static final String HAS_VALUE = "has_value";
        public static final String EQUALS = "equals";
        public static final String GREATER_THAN_EQUAL = "greater_than_equal";
        public static final String LESS_THAN_EQUAL = "less_than_equal";
        public static final String NOT_EQUAL = "not_equal";
        public static final String NOT_EQUALS = "not_equals";
        public static final String GREATER_THAN = "greater_than";
        public static final String LESS_THAN = "less_than";
    }

    public static final class JsFunctions {
        public static final String INITIALIZE_VIEW_STATE = "initializeViewState";
        public static final String INITIALIZE_SESSION_TIMERS = "initializeSessionTimers";
        public static final String SET_CONFIG_PARM = "setConfigParam";
        public static final String SET_VALUE = "setValue";
    }

    public static final String EVENT_NAMESPACE = "uif";

    public static final class JsEvents {
        public static final String DIALOG_RESPONSE = "dialogresponse." + EVENT_NAMESPACE;
        public static final String SHOW_DIALOG = "showdialog." + EVENT_NAMESPACE;
    }

    public static final class ConfigProperties {
        public static final String KRAD_IMAGES_URL = "krad.externalizable.images.url";
        public static final String KRAD_URL = "krad.url";
    }

    public static final class FileExtensions {
        public static final String CSS = ".css";
        public static final String JS = ".js";
        public static final String MIN = ".min";
    }

}
