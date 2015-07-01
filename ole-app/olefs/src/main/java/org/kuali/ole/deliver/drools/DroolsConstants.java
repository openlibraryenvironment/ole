package org.kuali.ole.deliver.drools;

/**
 * Created by pvsubrah on 6/8/15.
 */
public class DroolsConstants {

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

    public static final String GENERAL_MESSAGE_FLAG = "GENERAL_MESSAGE";

    public static final String CUSTOM_DUE_DATE_REQUIRED_FLAG = "CUSTOM_DUE_DATE_REQUIRED";

    public static final String EMAIL = "email";
    public static final String INTERVAL_TO_GENERATE_NOTICE_FOR_COURTSEY = "intervalToGenerateNoticeForCourtsey";
    public static final String NUMBER_OF_OVERDUE_NOTICES_TO_BE_SENT = "numberOfOverdueToBeSent";

    public static final String INTERVAL_TO_GENERATE_NOTICE_FOR_OVERDUE = "intervalToGenerateNotice";
    public static final String REPLACEMENT_BILL_AMT = "replacementBill";
    public static final String GENERAL_BLOCK_PERMISSION = "Patron has a general block";
    public static final String MAX_CHARGES_PERMISSION = "Patron has max amount of all charges";
    public static final String SHORT_TERM_LOANS_NOTICE_CONFIG = "SHORT_TERM_LOANS_TERM_NOTICE";

    public static final String ILL_NOTICE_CONFIG = "ILL_NOTICE_CONFIG";
    public static final String REGULAR_LOANS_NOTICE_CONFIG = "REGULAR_LOANS_NOTICE";

    public static final String DROOL_UPLOAD_SELECT_FILE = "error.drool.file.upload";
    public static final String DROOL_UPLOAD_INVALID_FILE = "error.drool.invalid.file";
    public static final String DROOL_UPLOAD_SUCCESS = "drool.upload.success";


}
