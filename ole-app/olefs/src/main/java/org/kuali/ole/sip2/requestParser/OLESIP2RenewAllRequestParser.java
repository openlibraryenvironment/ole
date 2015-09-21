package org.kuali.ole.sip2.requestParser;

import org.apache.log4j.Logger;
import org.kuali.ole.request.OLESIP2RequestParser;
import org.kuali.ole.sip2.constants.OLESIP2Constants;

/**
 * Created by gayathria on 2/12/14.
 */
public class OLESIP2RenewAllRequestParser extends OLESIP2RequestParser {

    private static final Logger LOG = Logger.getLogger(OLESIP2RenewAllRequestParser.class);


    public OLESIP2RenewAllRequestParser(String requestData) {
        this.parseRenewAllRequest(requestData);
    }


    public void parseRenewAllRequest(String requestData) {

        LOG.info("Entry OLESIP2RenewAllRequestParser.parseRenewAllRequest(String requestData)");
        String[] requestDataArray = requestData.split("\\|");
        try {
            for (String data : requestDataArray) {
                LOG.info(data);
                if (data.startsWith(OLESIP2Constants.RENEW_ALL_REQUEST)) {
                    code = data.substring(0, 2);
                    transactionDate = data.substring(2, 20);
                    institutionId = data.substring(22);
                }
                if (data.startsWith(OLESIP2Constants.PATRON_IDENTIFIER_CODE)) {
                    patronIdentifier = (data.replaceFirst(OLESIP2Constants.PATRON_IDENTIFIER_CODE, "")).trim();
                }
                if (data.startsWith(OLESIP2Constants.TERMINAL_PWD_CODE)) {
                    terminalPassword = (data.replaceFirst(OLESIP2Constants.TERMINAL_PWD_CODE, "")).trim();
                }
                if (data.startsWith(OLESIP2Constants.PATRON_PWD_CODE)) {
                    patronPassword = (data.replaceFirst(OLESIP2Constants.PATRON_PWD_CODE, "")).trim();
                }
                if (data.startsWith(OLESIP2Constants.FEE_ACKNOWLEDGED_CODE)) {
                    feeAcknowledged = charToBool(data.charAt(2));
                }
                if (data.startsWith(OLESIP2Constants.SEQUENCE_NUM_CODE)) {
                    sequenceNum = data.substring(2, 5);
                    checkSum = data.substring(5);
                }
            }
        } catch (Exception e) {
            LOG.error(e.getMessage(), e);
        }
        LOG.info("Exit OLESIP2RenewAllRequestParser.parseRenewAllRequest(String requestData)");
    }
}
