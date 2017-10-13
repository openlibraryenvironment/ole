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
package org.kuali.rice.core.api.util;

/**
 * Holds error key constants.
 */
public final class RiceKeyConstants {
    public static final String ERROR_EXISTENCE = "error.existence";
    public static final String ERROR_EXPIRED = "error.expired";
    public static final String ERROR_INACTIVE = "error.inactive";
    public static final String ERROR_CLOSED = "error.closed";
    public static final String ERROR_DUPLICATE_ELEMENT = "error.duplicate.element";
    public static final String ERROR_INVALIDNEGATIVEAMOUNT = "error.invalidNegativeAmount";
    public static final String ERROR_REQUIRED = "error.required";
    public static final String ERROR_REQUIRED_FOR_US = "error.requiredForUs";
    public static final String ERROR_DATE = "error.invalidDate";
    public static final String ERROR_TIME = "error.invalidTime";
    public static final String ERROR_DATE_TIME = "error.invalidDateTime";
    public static final String ERROR_NUMBER = "error.invalidNumber";
    public static final String ERROR_BOOLEAN = "error.invalidBoolean";
    public final static String ERROR_CURRENCY = "error.currency";
    public final static String ERROR_CURRENCY_DECIMAL = "error.currency.decimal";
    public final static String ERROR_BIG_DECIMAL = "error.bigDecimal";
    public final static String ERROR_INTEGER = "error.integer";
    public final static String ERROR_LONG = "error.long";
    public final static String ERROR_PHONE_NUMBER = "error.phonenumber";
    public final static String ERROR_PERCENTAGE = "error.percentage";
    public final static String ERROR_NUMERIC = "error.numeric";
    public static final String ERROR_MIN_LENGTH = "error.minLength";
    public static final String ERROR_MAX_LENGTH = "error.maxLength";
    public static final String ERROR_LENGTH_OUT_OF_RANGE = "error.lengthOutOfRange";
    public static final String ERROR_MIN_OCCURS = "error.minOccurs";
    public static final String ERROR_MAX_OCCURS = "error.maxOccurs";
    public static final String ERROR_QUANTITY_RANGE = "error.quantityRange";
    public static final String ERROR_OCCURS = "error.occurs";
    public static final String ERROR_REQUIRES_FIELD = "error.requiresField";
    public static final String ERROR_INVALID_FORMAT = "error.invalidFormat";
    public static final String ERROR_EXCLUSIVE_MIN = "error.exclusiveMin";
    public static final String ERROR_INCLUSIVE_MIN = "error.inclusiveMin";
    public static final String ERROR_INCLUSIVE_MAX = "error.inclusiveMax";
    public static final String ERROR_OUT_OF_RANGE = "error.outOfRange";

    public static final String ERROR_INACTIVATION_BLOCKED = "error.inactivation.blocked";

    // KULRICE-7419: Adhoc route completion validation rule
    public static final String ERROR_ADHOC_COMPLETE_PERSON_IS_INITIATOR = "error.adhoc.complete.person.is.initiator";

    // KULRICE-8760: Multiple complete adhoc requests should not be allowed on the same document
    public static final String ERROR_ADHOC_COMPLETE_MORE_THAN_ONE = "error.adhoc.complete.more.than.one.request";

    // KULRICE-7864: blanket approve should not be allowed when adhoc route for completion request is newly added 
    public static final String ERROR_ADHOC_COMPLETE_BLANKET_APPROVE_NOT_ALLOWED =
            "error.adhoc.complete.blanket.approve.not.allowed";

    public static final String ERROR_INVALID_ADHOC_PERSON_ID = "error.adhoc.invalid.person";
    public static final String ERROR_MISSING_ADHOC_PERSON_ID = "error.adhoc.missing.person";
    public static final String ERROR_UNAUTHORIZED_ADHOC_PERSON_ID = "error.adhoc.unauthorized.person";
    public static final String ERROR_INACTIVE_ADHOC_PERSON_ID = "error.adhoc.inactive.person";
    public static final String ERROR_INVALID_ADHOC_WORKGROUP_ID = "error.adhoc.invalid.workgroup";
    public static final String ERROR_MISSING_ADHOC_WORKGROUP_ID = "error.adhoc.missing.workgroup";
    public static final String ERROR_INVALID_ADHOC_WORKGROUP_NAMESPACECODE =
            "error.adhoc.invalid.workgroupNamespaceCode";
    public static final String ERROR_UNAUTHORIZED_ADHOC_WORKGROUP_ID = "error.adhoc.unauthorized.workgroup";
    public static final String MESSAGE_ADHOC_ANNOTATION = "message.adhoc.annotation";

    //  KULRICE-8091: Adhoc routing tab utilizing Groups on all documents missing asterisks 
    public static final String ERROR_ADHOC_INVALID_WORKGROUP_NAMESPACE_MISSING =
            "error.adhoc.invalid.workgroup.namespace.missing";
    public static final String ERROR_ADHOC_INVALID_WORKGROUP_NAMESPACE = "error.adhoc.invalid.workgroup.namespace";

    public static final String ERROR_SECURE_FIELD = "error.secureField";
    public static final String ERROR_SEND_NOTE_NOTIFICATION_RECIPIENT = "error.send.note.notification.recipient";
    public static final String ERROR_SEND_NOTE_NOTIFICATION_DOCSTATUS = "error.send.note.notification.docStatus";
    public static final String MESSAGE_SEND_NOTE_NOTIFICATION_SUCCESSFUL = "message.send.note.notification.successful";
    public static final String MESSAGE_NOTE_NOTIFICATION_ANNOTATION = "message.note.notification.annotation";

    public static final String UNAUTHORIZED_INQUIRY = "unauthorized.inquiry";
    public static final String UNAUTHORIZED_LOOKUP = "unauthorized.lookup";
    public static final String UNAUTHORIZED_DOCUMENT = "unauthorized.document";
    public static final String UNAUTHORIZED_CUSTOM = "unauthorized.custom";

    public static final String MULTIPLE_VALUE_LOOKUP_ICON_LABEL = "multiple.value.lookup.icon.label";

    public static final String AUTHORIZATION_ERROR_GENERAL = "error.authorization.general";
    public static final String AUTHORIZATION_ERROR_INACTIVE_DOCTYPE = "error.authorization.inactiveDocumentType";
    public static final String AUTHORIZATION_ERROR_DOCTYPE = "error.authorization.documentType";
    public static final String AUTHORIZATION_ERROR_DOCUMENT = "error.authorization.document";
    public static final String AUTHORIZATION_ERROR_MAINTENANCE_NEWCOPY = "error.authorization.maintenance.newCopy";
    public static final String AUTHORIZATION_ERROR_MODULE = "error.authorization.module";
    public static final String AUTHORIZATION_ERROR_DOCUMENT_WORKGROUP = "error.authorization.workgroupInitiation";

    // Document-specific errors
    public static final String ERROR_DOCUMENT_ANNOTATION_MAX_LENGTH_EXCEEDED =
            "error.document.annotation.maxLength.exceeded";
    public static final String ERROR_DOCUMENT_DISAPPROVE_REASON_REQUIRED = "error.document.disapprove.reasonRequired";
    public static final String ERROR_DOCUMENT_RECALL_REASON_REQUIRED = "error.document.recall.reasonRequired";
    public static final String ERROR_DOCUMENT_NO_DESCRIPTION = "error.document.noDescription";
    public static final String ERROR_UPLOADFILE_NULL = "error.uploadFile.null";
    public static final String ERROR_UPLOADFILE_EMPTY = "error.uploadFile.empty";
    public static final String ERROR_UPLOADFILE_SIZE = "error.uploadFile.size";
    public static final String ERROR_UNIMPLEMENTED = "error.unimplemented";
    public static final String ERROR_OPTIMISTIC_LOCK = "error.document.optimisticLockException";

    public static final String QUESTION_CONTINUATION_ACCOUNT_SELECTION =
            "document.question.selectContinuationAccount.text";
    public static final String QUESTION_SAVE_BEFORE_CLOSE = "document.question.saveBeforeClose.text";

    // General Maintenance Document Error Messages
    public static final String ERROR_DOCUMENT_MAINTENANCE_PRIMARY_KEYS_CHANGED_ON_EDIT =
            "error.document.maintenance.general.primaryKeysChangedOnEdit";
    public static final String ERROR_DOCUMENT_MAINTENANCE_KEYS_ALREADY_EXIST_ON_CREATE_NEW =
            "error.document.maintenance.general.objectAlreadyExistsByPrimaryKeysOnCreateNew";
    public static final String ERROR_DOCUMENT_AUTHORIZATION_RESTRICTED_FIELD_CHANGED =
            "error.document.maintenance.authorization.restrictedFieldChanged";
    public static final String ERROR_DOCUMENT_MAINTENANCE_PARTIALLY_FILLED_OUT_REF_FKEYS =
            "error.document.maintenance.partiallyFilledOutReferenceForeignKeys";
    public static final String ERROR_DOCUMENT_MAINTENANCE_FORMATTING_ERROR =
            "error.document.maintenance.formattingError";
    public static final String ERROR_DOCUMENT_INVALID_VALUE_ALLOWED_VALUES_PARAMETER =
            "error.document.invalid.value.allowedValuesParameter";
    public static final String ERROR_DOCUMENT_INVALID_VALUE_DENIED_VALUES_PARAMETER =
            "error.document.invalid.value.deniedValuesParameter";

    // Person errors
    public static final String ERROR_DOCUMENT_KUALIUSERMAINT_UNIQUE_EMPLID =
            "error.document.PersonMaintenance.UniqueEmplId";

    public static final String ERROR_CUSTOM = "error.custom";
    public static final String ERROR_INQUIRY = "error.inquiry";
    public static final String ERROR_MAINTENANCE_LOCKED = "error.maintenance.locked";
    public static final String ERROR_MAINTENANCE_LOCKED1 = "error.maintenance.locked.1";
    public static final String ERROR_MAINTENANCE_LOCKED2 = "error.maintenance.locked.2";
    public static final String ERROR_MAINTENANCE_LOCKED3 = "error.maintenance.locked.3";
    public static final String ERROR_ZERO_AMOUNT = "error.zeroAmount";
    public static final String ERROR_ZERO_OR_NEGATIVE_AMOUNT = "error.zeroOrNegativeAmount";
    public static final String ERROR_NEGATIVE_AMOUNT = "error.negativeAmount";
    public static final String ERROR_NOT_AMONG = "error.invalidNotAmong";

    public static final String WARNING_MAINTENANCE_LOCKED = "warning.maintenance.locked";
    public static final String ERROR_VERSION_MISMATCH = "error.version.mismatch";
    public static final String UNABLE_TO_GET_DATA_FROM_XML = "unable.to.get.data.from.xml";

    public static final String MESSAGE_CANCELLED = "message.cancelled";
    public static final String MESSAGE_RELOADED = "message.document.reloaded";
    public static final String MESSAGE_ROUTE_SUCCESSFUL = "message.route.successful";
    public static final String MESSAGE_SAVED = "message.saved";
    public static final String MESSAGE_ROUTE_APPROVED = "message.route.approved";
    public static final String MESSAGE_ROUTE_DISAPPROVED = "message.route.disapproved";
    public static final String MESSAGE_ROUTE_CANCELED = "message.route.canceled";
    public static final String MESSAGE_ROUTE_RECALLED = "message.route.recalled";
    public static final String MESSAGE_ROUTE_ACKNOWLEDGED = "message.route.acknowledged";
    public static final String MESSAGE_ROUTE_FYIED = "message.route.fyied";
    public static final String MESSAGE_NO_HELP_TEXT = "message.nohelp";
    public static final String MESSAGE_REVERT_SUCCESSFUL = "message.revert.successful";
    public static final String MESSAGE_REVERT_UNNECESSARY = "message.revert.unnecessary";
    public static final String MESSAGE_DISAPPROVAL_NOTE_TEXT_INTRO = "message.disapprove.noteTextIntro";
    public static final String MESSAGE_RECALL_NOTE_TEXT_INTRO = "message.recall.noteTextIntro";

    public static final String INFO_LOOKUP_RESULTS_DISPLAY_ALL = "lookup.results.found.display.all";
    public static final String INFO_LOOKUP_RESULTS_DISPLAY_ONE = "lookup.results.found.display.one";
    public static final String INFO_LOOKUP_RESULTS_NONE_FOUND = "lookup.results.none.found";
    public static final String INFO_LOOKUP_RESULTS_USING_PRIMARY_KEY = "lookup.using.primary.keys";
    public static final String INFO_LOOKUP_RESULTS_EXCEEDS_LIMIT = "lookup.results.exceeds.limit";
    public static final String INFO_LOOKUP_RESULTS_MV_RETURN_EXCEEDS_LIMIT = "lookup.results.exceeds.mv.return.limit";

    public static final String MESSAGE_DELETE = "message.delete";
    public static final String MESSAGE_DELETED = "mesage.deleted";

    public static final String QUESTION_DISAPPROVE_DOCUMENT = "document.question.disapprove.text";
    public static final String QUESTION_RECALL_DOCUMENT = "document.question.recall.text";
    public static final String QUESTION_SENSITIVE_DATA_DOCUMENT = "document.question.sensitiveData.text";

    public static final String ERROR_MISSING = "error.missing";

    // Application Parameter Component errors
    public static final String ERROR_APPLICATION_PARAMETERS_ALLOWED_RESTRICTION =
            "error.applicationParametersAllowedRestriction";
    public static final String ERROR_APPLICATION_PARAMETERS_DENIED_RESTRICTION =
            "error.applicationParametersDeniedRestriction";
    public static final String ERROR_PAYMENT_REASON_ALLOWED_RESTRICTION = "error.paymentReasonAllowedRestriction";
    public static final String ERROR_PAYMENT_REASON_DENIED_RESTRICTION = "error.paymentReasonDeniedRestriction";

    public static final Object WARNING_LINE_IMPORT_LENGTH_MISMATCH = "warning.core.bo.AccountImportLengthMismatch";

    // kim role document
    public static final String QUESTION_ACTIVE_DELEGATES_FOR_INACTIVE_MEMBERS =
            "question.document.maintenance.role.activeDelegatesForInactiveRoleMembers";

    // kim person document
    public static final String ERROR_MULTIPLE_DEFAULT_SELETION = "error.multiple.default.selection";
    public static final String ERROR_NO_DEFAULT_SELETION = "error.no.default.selection";
    public static final String ERROR_MULTIPLE_PRIMARY_EMPLOYMENT = "error.multiple.primary.employment";
    public static final String ERROR_NO_PRIMARY_EMPLOYMENT = "error.no.primary.employment";
    public static final String ERROR_DUPLICATE_ENTRY = "error.duplicate.entry";
    public static final String ERROR_EMPTY_ENTRY = "error.empty.entry";
    public static final String ERROR_EXIST_PRINCIPAL_NAME = "error.exist.principalName";
    public static final String ERROR_ASSIGN_ROLE = "error.assign.role";
    public static final String ERROR_ASSIGN_GROUP = "error.assign.group";
    public static final String ERROR_ASSIGN_GROUP_INVALID = "error.assign.group.invalid";
    public static final String ERROR_ASSIGN_PERMISSION = "error.assign.perimssion";
    public static final String ERROR_ASSIGN_RESPONSIBILITY = "error.assign.responsibility";
    public static final String ERROR_POPULATE_GROUP = "error.populate.group";
    public static final String ERROR_ACTIVE_TO_DATE_BEFORE_FROM_DATE = "error.active.todate.before.fromdate";
    public static final String ERROR_NOT_EMPLOYMENT_AFFILIATION_TYPE = "error.not.employment.affilationType";
    public static final String ERROR_NOT_UNIQUE_AFFILIATION_TYPE_PER_CAMPUE =
            "error.not.unique.affilationType.per.campus";
    public static final String ERROR_ROLE_QUALIFIER_REQUIRED = "error.role.qualifier.required";
    public static final String MESSAGE_SEND_AD_HOC_REQUESTS_SUCCESSFUL = "message.sendAdHocRequests.successful";
    public static final String ERROR_ONE_ITEM_REQUIRED = "error.one.item.required";
    public static final String ERROR_ONE_ACTIVE_ITEM_REQUIRED = "error.one.active.item.required";
    public static final String ERROR_MEMBERID_MEMBERTYPE_MISMATCH = "error.memberid.membertype.mismatch";
    public static final String ERROR_PRIORITY_NUMBER_RANGE = "error.prioritynumber.range";
    public static final String ERROR_CANT_ADD_DERIVED_ROLE = "error.cant.add.derived.role";
    public static final String ERROR_INVALID_ROLE = "error.invalid.role";
    public static final String ERROR_REQUIRED_CONDITIONALLY = "error.required.conditionally";
    public static final String ERROR_CANT_BE_MODIFIED = "error.unmodifiable.attribute";
    public static final String ERROR_DELEGATE_ROLE_MEMBER_ASSOCIATION = "error.delegation.notassociatedwith.rolemember";
    public static final String ERROR_DELEGATION_TO_AMOUNT_GREATER = "error.delegation.toamount.greater";
    public static final String ERROR_DELEGATION_FROM_AMOUNT_LESSER = "error.delegation.fromamount.lesser";
    public static final String ERROR_ASSIGN_ROLE_MEMBER_CIRCULAR = "error.assign.role.member.circular";
    public static final String ERROR_ASSIGN_GROUP_MEMBER_CIRCULAR = "error.assign.group.member.circular";

    //parameter document
    public static final String AUTHORIZATION_ERROR_PARAMETER = "error.authorization.parameter";

    public static final String ERROR_DOCUMENT_FIELD_CONTAINS_POSSIBLE_SENSITIVE_DATA =
            "error.document.fieldContainsPossibleSensitiveData";

    public static final String ERROR_WILDCARDS_AND_OPERATORS_NOT_ALLOWED_ON_FIELD =
            "error.wildcards.and.operators.not.allowed.on.field";
    public static final String INFO_WILDCARDS_AND_OPERATORS_TREATED_LITERALLY =
            "info.wildcards.and.operators.treated.literally";
    public static final String ERROR_DOCUMENT_IDENTITY_MANAGEMENT_PERSON_QUALIFIER_VALUE_NOT_UNIQUE =
            "error.document.identityManagementPerson.qualifier.valueNotUnique";

    // PeopleFlow
    public static final String PEOPLEFLOW_DUPLICATE = "peopleFlow.duplicate";

    private RiceKeyConstants() {
        throw new UnsupportedOperationException("do not call");
    }
}

