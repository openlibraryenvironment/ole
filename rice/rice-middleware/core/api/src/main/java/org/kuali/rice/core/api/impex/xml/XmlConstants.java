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
package org.kuali.rice.core.api.impex.xml;

import org.jdom.Namespace;

/**
 * Constants for various XML namespaces, elements and attributes for the various parsers.
 *
 * @author Kuali Rice Team (rice.collab@kuali.org)
 */
public final class XmlConstants {

    // namespaces
    public static final Namespace WORKFLOW_NAMESPACE = Namespace.getNamespace("", "ns:workflow");
    public static final Namespace SCHEMA_NAMESPACE = Namespace.getNamespace("xsi", "http://www.w3.org/2001/XMLSchema-instance");
    public static final Namespace RULE_NAMESPACE = Namespace.getNamespace("", "ns:workflow/Rule");
    public static final Namespace DOCUMENT_TYPE_NAMESPACE = Namespace.getNamespace("", "ns:workflow/DocumentType");
    public static final Namespace GROUP_NAMESPACE = Namespace.getNamespace("", "ns:workflow/Group");
    public static final Namespace RULE_TEMPLATE_NAMESPACE = Namespace.getNamespace("", "ns:workflow/RuleTemplate");
    public static final Namespace RULE_ATTRIBUTE_NAMESPACE = Namespace.getNamespace("", "ns:workflow/RuleAttribute");
    public static final Namespace EDL_NAMESPACE = Namespace.getNamespace("", "ns:workflow/EDocLite");
    public static final Namespace STYLE_NAMESPACE = Namespace.getNamespace("", "ns:workflow/Style");

    // schemas
    public static final String SCHEMA_LOCATION_ATTR = "schemaLocation";
    public static final String WORKFLOW_SCHEMA_LOCATION = "ns:workflow resource:WorkflowData";
    public static final String RULE_SCHEMA_LOCATION = "ns:workflow/Rule resource:Rule";
    public static final String GROUP_SCHEMA_LOCATION = "ns:workflow/Group resource:Group";
    public static final String DOCUMENT_TYPE_SCHEMA_LOCATION = "ns:workflow/DocumentType resource:DocumentType";
    public static final String RULE_TEMPLATE_SCHEMA_LOCATION = "ns:workflow/RuleTemplate resource:RuleTemplate";
    public static final String RULE_ATTRIBUTE_SCHEMA_LOCATION = "ns:workflow/RuleAttribute resource:RuleAttribute";
    public static final String EDL_SCHEMA_LOCATION = "ns:workflow/EDocLite resource:EDocLite";
    public static final String STYLE_SCHEMA_LOCATION = "ns:workflow/Style resource:Style";

    // data
    public static final String DATA_ELEMENT = "data";

    // general
    public static final String NAME = "name";
    public static final String DESCRIPTION = "description";
    public static final String SUMMARY = "summary";
    public static final String LABEL = "label";
    public static final String SHORT_LABEL = "shortLabel";
    public static final String KEY = "key";
    public static final String VALUE = "value";
    public static final String STRING_VALUE = "stringValue";
    public static final String ACTIVE= "active";
    public static final String TYPE = "type";

    // document types
    public static final String DOCUMENT_TYPES = "documentTypes";
    public static final String DOCUMENT_TYPE = "documentType";
    public static final String DOCUMENT_TYPE_OVERWRITE_MODE = "overwriteMode";
    public static final String PARENT = "parent";
    /**
     * @deprecated use {@link #APPLICATION_ID} instead
     */
    @Deprecated
    public static final String SERVICE_NAMESPACE = "serviceNamespace";
    public static final String APPLICATION_ID = "applicationId";
    public static final String POST_PROCESSOR_NAME = "postProcessorName";
    public static final String AUTHORIZER = "authorizer";
    public static final String SUPER_USER_WORKGROUP_NAME = "superUserWorkgroupName";
    public static final String SUPER_USER_GROUP_NAME = "superUserGroupName";
    public static final String BLANKET_APPROVE_WORKGROUP_NAME = "blanketApproveWorkgroupName";
    public static final String BLANKET_APPROVE_GROUP_NAME = "blanketApproveGroupName";
    public static final String BLANKET_APPROVE_POLICY = "blanketApprovePolicy";
    public static final String REPORTING_WORKGROUP_NAME = "reportingWorkgroupName";
    public static final String REPORTING_GROUP_NAME = "reportingGroupName";
    public static final String DEFAULT_EXCEPTION_WORKGROUP_NAME = "defaultExceptionWorkgroupName";
    public static final String DEFAULT_EXCEPTION_GROUP_NAME = "defaultExceptionGroupName";
    public static final String DOC_HANDLER = "docHandler";
    public static final String HELP_DEFINITION_URL = "helpDefinitionURL";
    public static final String DOC_SEARCH_HELP_URL = "docSearchHelpURL";
    public static final String NOTIFICATION_FROM_ADDRESS = "notificationFromAddress";
    public static final String CUSTOM_EMAIL_STYLESHEET = "emailStylesheet";
    public static final String POLICIES = "policies";
    public static final String POLICY = "policy";
    public static final String SECURITY = "security";
    public static final String ROUTING_VERSION = "routingVersion";
    public static final String ROUTE_PATHS = "routePaths";
    public static final String ROUTE_PATH = "routePath";
    public static final String INITIAL_NODE = "initialNode";
    public static final String PROCESS_NAME = "processName";
    public static final String ROUTE_NODES = "routeNodes";
    public static final String BRANCH = "branch";
    public static final String EXCEPTION_WORKGROUP_NAME = "exceptionWorkgroupName";
    public static final String EXCEPTION_WORKGROUP = "exceptionWorkgroup";
    public static final String EXCEPTION_GROUP_NAME = "exceptionGroupName";
    public static final String ACTIVATION_TYPE = "activationType";
    public static final String FINAL_APPROVAL = "finalApproval";
    public static final String MANDATORY_ROUTE = "mandatoryRoute";
    public static final String ROUTE_MODULE = "routeModule";
    public static final String NEXT_NODE = "nextNode";
    public static final String APP_DOC_STATUSES = "validApplicationStatuses";
    public static final String STATUS = "status";
    public static final String CATEGORY = "category";

    // rules
    public static final String RULES = "rules";
    public static final String RULE = "rule";
    public static final String EXPRESSION = "expression";
    public static final String FROM_DATE = "fromDate";
    public static final String TO_DATE = "toDate";
    public static final String FORCE_ACTION = "forceAction";
    public static final String RESPONSIBILITIES = "responsibilities";
    public static final String RESPONSIBILITY = "responsibility";
    public static final String RESPONSIBILITY_ID = "responsibilityId";
    public static final String ACTION_REQUESTED = "actionRequested";
    public static final String USER = "user";
    public static final String ROLE = "role";
    public static final String ROLE_NAME = "roleName";
    public static final String ATTRIBUTE_CLASS_NAME = "attributeClassName";
    public static final String APPROVE_POLICY = "approvePolicy";
    public static final String PRIORITY = "priority";
    public static final String DELEGATIONS = "delegations";
    public static final String DELEGATION_TYPE = "delegationType";
    public static final String RULE_EXTENSIONS = "ruleExtensions";
    public static final String RULE_EXTENSION = "ruleExtension";
    public static final String RULE_EXTENSION_VALUES = "ruleExtensionValues";
    public static final String RULE_EXTENSION_VALUE = "ruleExtensionValue";
    public static final String RULE_DELEGATIONS = "ruleDelegations";
    public static final String RULE_DELEGATION = "ruleDelegation";
    public static final String PARENT_RESPONSIBILITY = "parentResponsibility";
    public static final String PARENT_RULE_NAME = "parentRuleName";

    public static final String GROUPS = "groups";
    public static final String GROUP = "group";
    public static final String ID = "id";
    public static final String NAMESPACE = "namespace";
    public static final String EXTENSIONS = "extensions";
    public static final String EXTENSION = "extension";
    public static final String DATA = "data";
    public static final String WORKGROUPS = "workgroups";
    public static final String WORKGROUP = "workgroup";
    public static final String MEMBERS = "members";
    public static final String PRINCIPAL_NAME = "principalName";
    public static final String PRINCIPAL_ID = "principalId";
    public static final String GROUP_ID = "groupId";
    public static final String GROUP_NAME = "groupName";

    // rule templates
    public static final String RULE_TEMPLATES = "ruleTemplates";
    public static final String RULE_TEMPLATE = "ruleTemplate";
    public static final String RULE_EXPRESSION = "expression";
    public static final String DELEGATION_TEMPLATE = "delegationTemplate";
    public static final String REQUIRED = "required";
    public static final String ATTRIBUTES = "attributes";
    public static final String ATTRIBUTE = "attribute";
    public static final String RULE_DEFAULTS = "ruleDefaults";
    public static final String DEFAULT_ACTION_REQUESTED = "defaultActionRequested";
    public static final String SUPPORTS_COMPLETE = "supportsComplete";
    public static final String SUPPORTS_APPROVE = "supportsApprove";
    public static final String SUPPORTS_ACKNOWLEDGE = "supportsAcknowledge";
    public static final String SUPPORTS_FYI = "supportsFYI";

    // rule attributes
    public static final String RULE_ATTRIBUTES = "ruleAttributes";
    public static final String RULE_ATTRIBUTE = "ruleAttribute";
    public static final String CLASS_NAME = "className";
    public static final String ROUTING_CONFIG = "routingConfig";
    public static final String SEARCHING_CONFIG = "searchingConfig";
	public static final String SEARCH_RESULT_CONFIG = "searchResultConfig";
    public static final String RESOLVER_CONFIG = "resolverConfig";
    public static final String IS_MEMBER_OF_WORKGROUP = "isMemberOfWorkgroup";
    public static final String IS_MEMBER_OF_GROUP = "isMemberOfGroup";

    //edoclite
    public static final String EDL_EDOCLITE = "edoclite";
    public static final String EDL_STYLE = "style";
    public static final String EDL_ASSOCIATION = "association";
    public static final String EDL_DOC_TYPE = "docType";
    public static final String EDL_DEFINITION = "definition";
    public static final String EDL_ACTIVE = "active";

    //style
    public static final String STYLE_STYLES = "styles";
    public static final String STYLE_STYLE = "style";
    
    private XmlConstants() {
    	throw new UnsupportedOperationException("do not call");
    }
}
