package org.kuali.ole.request;

import org.apache.log4j.Logger;
import org.kuali.ole.constants.OLESIP2Constants;

/**
 * Created by gayathria on 2/12/14.
 */
public class OLESIP2PatronEnableRequestParser extends OLESIP2RequestParser {


    private static final Logger LOG = Logger.getLogger(OLESIP2PatronEnableRequestParser.class);


    public OLESIP2PatronEnableRequestParser(String requestData) {
        this.parsePatronEnableRequest(requestData);
    }


    public void parsePatronEnableRequest(String requestData) {
        LOG.info("Entry OLESIP2PatronEnableRequestParser.parsePatronEnable >>>> " + requestData);

        String[] requestDataArray = requestData.split("\\|");
        for (String data : requestDataArray) {
            if (data.startsWith(OLESIP2Constants.PATRON_ENABLE_REQUEST)) {
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
            if (data.startsWith(OLESIP2Constants.SEQUENCE_NUM_CODE)) {
                sequenceNum = data.substring(2, 5);
                checkSum = data.substring(5);
            }
        }
        LOG.info("Exit OLESIP2PatronEnableRequestParser.parsePatronEnable(String requestData)");
    }
}
