/*
 * Copyright 2007 The Kuali Foundation
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
package org.kuali.ole.module.purap;


/**
 * Holds Workflow constants for PURAP documents
 */
public class PurapWorkflowConstants {

    // Global
    public static final String FOR_INFORMATION = "ForInformation";
    public static final String HAS_ACCOUNTING_LINES = "HasAccountingLines";
    public static final String AMOUNT_REQUIRES_SEPARATION_OF_DUTIES_REVIEW_SPLIT = "AmountRequiresSeparationOfDutiesReview";
    public static final String CONTRACT_MANAGEMENT_REVIEW_REQUIRED = "RequiresContractManagementReview";
    public static final String VENDOR_IS_EMPLOYEE_OR_NON_RESIDENT_ALIEN = "VendorIsEmployeeOrNonResidentAlien";
    public static final String AWARD_REVIEW_REQUIRED = "RequiresAwardReview";
    public static final String BUDGET_REVIEW_REQUIRED = "RequiresBudgetReview";
    public static final String TRANSMISSION_METHOD_IS_PRINT = "TransmissionMethodIsPrint";
    public static final String HAS_NEW_UNORDERED_ITEMS = "HasNewUnorderedItems";
    public static final String RELATES_TO_OUTSTANDING_TRANSACTIONS = "RelatesToOutstandingTransactions";
    public static final String REQUIRES_IMAGE_ATTACHMENT = "RequiresImageAttachment";
    public static final String PURCHASE_WAS_RECEIVED = "PurchaseWasReceived";
    public static final String DUPLICATE_RECORD_CHECK = "DuplicateRecord";
    public static final String HAS_ORDER_CHANGE = "HasOrderChange";
    public static final String NEW_VENDOR_CHECK = "HasNewVendor";
    public static final String YBP_ORDERS = "HasFirmFixedYBPOrders";
    public static final String SUBSCRIPTION_ORDERS = "HasSubscriptionOrders";
    public static final String STANDING_ORDERS = "HasStandingOrders";
    public static final String APPROVAL_ORDERS = "HasApprovalOrders";
    public static final String HAS_VENDOR = "HasVendor";
    public static final String LINE_ITEM_RECEIVING_APPROVAL = "ReceiveLineItemApproval";
    public static final String BUDGET_NODE = "Budget";
    public static final String DOC_SEARCH_RESULT_PROPERTY_NAME_ROUTE_HEADER_ID = "routeHeaderId";
    public static final String HAS_LICENSE_REQUEST = "HasLicenseRequest";
    public static final String HAS_FIRMFIXED_WITH_LR = "HasFirmFixedWithLicenseRequest";
    public static final String NOTIFY_BUDGET_REVIEW = "NotifyBudgetReview";
    public static final String FYI_BUDGET = "FYIBudget";


    public static final String DOC_ADHOC_NODE_NAME = "AdHoc";

    public static class ContractManagerAssignmentDocument {
        public static final String WORKFLOW_DOCUMENT_TITLE = "Contract Manager Assignment";
        public static final String ASSIGN_CONTRACT_DOC_ERROR_COMPLETING_POST_PROCESSING = "Unable to save the contract manager for the following Requisitions: ";
    }


}
