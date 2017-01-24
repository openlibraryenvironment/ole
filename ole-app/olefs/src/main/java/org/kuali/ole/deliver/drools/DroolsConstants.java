package org.kuali.ole.deliver.drools;

/**
 * Created by pvsubrah on 6/8/15.
 */
public class DroolsConstants {


    public static final String PRINT_SLIP_FLAG = "PRINT_SLIP_FLAG";
    public static final String ROUTE_TO_LOCATION_SELECTOR = "ROUTE_TO_LOCATION_SELECTOR";
    public static final String AUTO_CHECKOUT = "AUTO_CHECKOUT";
    public static final String SHOW_LOCATION_POPUP = "SHOW_LOCATION_POPUP";
    public static enum ERROR_CODES
    {
        CUSTOM_LOAN_DUE_DATE_REQUIRED(DroolsConstants.CUSTOM_DUE_DATE_REQUIRED_FLAG),
        GENERAL_MESSAGE(DroolsConstants.GENERAL_MESSAGE_FLAG);

        private String name;

        ERROR_CODES(String name){
            this.name = name;
        }

        public String getName() {
            return name;
        }

    };

    public enum EDITOR_SECTIONS {
        GENERAL_CHECK("DroolsEditorBo-GeneralChecks-MaintenanceView-ruleSection"),
        CHECKOUT("DroolsEditorBo-Checkout-MaintenanceView-ruleSection"),
        CHECKIN("DroolsEditorBo-Checkin-MaintenanceView-ruleSection"),
        RENEW("DroolsEditorBo-Renew-MaintenanceView-ruleSection"),
        NOTICES("DroolsEditorBo-Notices-MaintenanceView-ruleSection");

        private String sectionId;

        EDITOR_SECTIONS(String sectionId) {
            this.sectionId = sectionId;
        }

        public String getSectionId() {
            return sectionId;
        }
    };

    public static final String GENERAL_MESSAGE_FLAG = "GENERAL_MESSAGE";

    public static final String CUSTOM_DUE_DATE_REQUIRED_FLAG = "CUSTOM_DUE_DATE_REQUIRED";

    public static final String EMAIL = "email";

    public static final String INTERVAL_TO_GENERATE_NOTICE_FOR_COURTESY = "intervalToGenerateNoticeForCourtesy";

    public static final String NUMBER_OF_OVERDUE_NOTICES_TO_BE_SENT = "numberOfOverdueToBeSent";
    public static final String NUMBER_OF_COURTESY_NOTICES_TO_BE_SENT = "numberOfCourtesyToBeSent";

    public static final String INTERVAL_TO_GENERATE_NOTICE_FOR_OVERDUE = "intervalToGenerateNotice";
    public static final String REPLACEMENT_BILL_AMT = "replacementBill";
    public static final String LOST_ITEM_PROCESSING_FEE_AMT = "lostItemProcessingFeeAmount";
    public static final String GENERAL_BLOCK_PERMISSION = "Patron has a general block";

    public static final String MAX_CHARGES_PERMISSION = "Patron has max amount of all charges";
    public static final String SHORT_TERM_LOANS_NOTICE_CONFIG = "SHORT_TERM_LOANS_TERM_NOTICE";
    public static final String ILL_NOTICE_CONFIG = "ILL_NOTICE_CONFIG";
    public static final String REGULAR_LOANS_NOTICE_CONFIG = "REGULAR_LOANS_NOTICE";
    public static final String DROOL_UPLOAD_SELECT_FILE = "error.drool.file.upload";

    public static final String DROOL_UPLOAD_INVALID_FILE = "error.drool.invalid.file";
    public static final String DROOL_UPLOAD_SUCCESS = "drool.upload.success";

    public static final class PATRON_ADDRESS_VERIFIED {
        public static final String RULE_NAME = "isAddressVerifiedRuleName";
        public static final String ERROR_MSG = "isAddressVerifiedErrorMessage";

        public static final String OVERRIDE_PERMISSION = "isAddressVerifiedOverridePermission";
        public static final String ACTIVATION_GROUP = "isAddressVerifiedActivation-group";
    }
    public static final class PATRON_ACTIVE {
        public static final String RULE_NAME = "isPatronActiveRuleName";
        public static final String ERROR_MSG = "isPatronActiveErrorMessage";
        public static final String OVERRIDE_PERMISSION = "isPatronActiveOverridePermission";
        public static final String ACTIVATION_GROUP = "isPatronActiveActivation-group";
    }
    public static final class PATRON_BLOCKED {
        public static final String RULE_NAME = "isPatronBlockRuleName";
        public static final String ERROR_MSG = "isPatronBlockErrorMessage";
        public static final String OVERRIDE_PERMISSION = "isPatronBlockOverridePermission";
        public static final String ACTIVATION_GROUP = "isPatronBlockActivation-group";
    }
    public static final class PATRON_EXPIRED {
        public static final String RULE_NAME = "isPatronExpiredRuleName";
        public static final String ERROR_MSG = "isPatronExpiredErrorMessage";

        public static final String OVERRIDE_PERMISSION = "isPatronExpiredOverridePermission";
        public static final String ACTIVATION_GROUP = "isPatronExpiredActivation-group";
    }
    public static final class PATRON_ALL_CHARGES {
        public static final String RULE_NAME = "patronAllChargesRuleName";
        public static final String ERROR_MSG = "patronAllChargesErrorMessage";

        public static final String OVERRIDE_PERMISSION = "patronAllChargesOverridePermission";
        public static final String ALL_CHARGES = "allCharges";
        public static final String ACTIVATION_GROUP = "patronAllChargesActivation-group";
    }
    public static final class PATRON_REPLACEMENT_AMOUNT {
        public static final String RULE_NAME = "patronReplacementAmountRuleName";
        public static final String ERROR_MSG = "patronReplacementAmountErrorMessage";

        public static final String OVERRIDE_PERMISSION = "patronReplacementAmountOverridePermission";
        public static final String REPLACEMENT_AMOUNT = "replacementAmount";
        public static final String ACTIVATION_GROUP = "patronReplacementAmountActivation-group";
    }
    public static final class PATRON_OVERDUE_DAYS {
        public static final String RULE_NAME = "patronOverdueRuleName";
        public static final String ERROR_MSG = "patronOverdueErrorMessage";

        public static final String OVERRIDE_PERMISSION = "patronOverdueOverridePermission";
        public static final String OVERDUE_DAYS = "overdueDays";
        public static final String ACTIVATION_GROUP = "patronOverdueActivation-group";
    }
    public static final class PATRON_RECALLED_AND_OVERDUE_DAYS {
        public static final String RULE_NAME = "patronOverdueRuleName";
        public static final String ERROR_MSG = "patronOverdueErrorMessage";

        public static final String OVERRIDE_PERMISSION = "patronOverdueOverridePermission";
        public static final String RECALL_AND_OVERDUE_DAYS = "overdueDays";
        public static final String ACTIVATION_GROUP = "patronOverdueActivation-group";
    }
    public static final  class GENERAL_CHECK_RULE_TYPE {
        public static final String IS_ACTIVE = "isActive";
        public static final String IS_BLOCKED = "isBlocked";

        public static final String ADDRESS_VERIFIED = "addressVerified";
        public static final String IS_PATRON_EXPIRED = "isPatronExpired";
        public static final String ALL_CHARGES = "allCharges";
        public static final String REPLACEMENT_FEE_AMOUNT = "replacementFeeAmount";
        public static final String OVERDUE_CHECK = "overdueCheck";
        public static final String RECALL_AND_OVERDUE_DAYS = "recallAndOverDueDays";
        public static final String OVERDUE_FINE_AMOUNT = "overdueFineAmount";
        public static final String ACTIVATION_DATE = "activationDate";
    }
    public static final  class CHECKOUT_RULE_TYPE {
        public static final String CHECKOUT = "checkout";
    }

    public static final  class EDITOR_TYPE {
        public static final String GENERAL_CHECK = "general-checks";
        public static final String CHECKOUT= "checkout";

        public static final String RENEW = "renew";
        public static final String CHECKIN = "checkin";
        public static final String NOTICE = "notices";
    }
    public static final String REQUEST_EXITS = "deliver-request";
    public static final String GENERAL_INFO = "GENERAL_INFO";
    public static final String ITEM_DAMAGED = "damaged-item";
    public static final String ITEM_LOST = "lost-item";
    public static final String ITEM_LOST_REPLACEMENT_BILL = "lost-item-replacement-bill";

    public static final String ITEM_CLAIMS_RETURNED = "claims-returned-item";
    public static final String ITEM_MISSING_PIECE = "missing-piece-item";
    public static final String CHECKED_OUT_BY_SAME_PATRON = "CHECKED_OUT_BY_SAME_PATRON";
    public static final String CHECKIN_REQUEST_EXITS_FOR_THIS_ITEM = "REQUEST_EXITS_FOR_THIS_ITEM";
    public static final String REQUEST_EXITS_FOR_AVAIL_ITEM = "REQUEST_EXITS_FOR_AVAIL_ITEM";
    public static final String REQUEST_EXITS_FOR_LOANED_ITEM = "REQUEST_EXITS_FOR_LOANED_ITEM";
    public static final String LOANED_BY_DIFFERENT_PATRON = "loaned_to_different_patron";
    public static final String DUE_DATE_TRUNCATED= "DUE_DATE_TRUNCATED";


    public static final String ITS_ADP_LOANS_NOTICE_CONFIG = "its_adp_loans_notice_config";
    public static final String ITS_LAP_NOTICE_CONFIG = "its_lap_notice_config";
    public static final String ITS_CNF_NOTICE_CONFIG = "its_cnf_notice_config";
    public static final String ITS_IPD_NOTICE_CONFIG = "its_ipd_notice_config";
}
