package org.kuali.ole.sip2.requestParser;

import org.apache.log4j.Logger;
import org.kuali.ole.sip2.constants.OLESIP2Constants;

/**
 * Created by gayathria on 2/12/14.
 */
public class OLESIP2PatronStatusRequestParser extends OLESIP2RequestParser {

    private String language;

    private static final Logger LOG = Logger.getLogger(OLESIP2PatronStatusRequestParser.class);


    public OLESIP2PatronStatusRequestParser(String requestData) {
        this.parsePatronStatusRequest(requestData);
    }


    public void parsePatronStatusRequest(String requestData) {

        LOG.info("Entry OLESIP2PatronStatusRequestParser.parsePatronStatusRequest(String requestData)");
        String[] requestDataArray = requestData.split("\\|");
        for (String data : requestDataArray) {

            if (data.startsWith(OLESIP2Constants.PATRON_STATUS_REQUEST)) {
                code = data.substring(0, 2);
                language = data.substring(2, 5);
                transactionDate = data.substring(5, 23);
                institutionId = data.substring(25);
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
        LOG.info("Exit OLESIP2PatronStatusRequestParser.parsePatronStatusRequest(String requestData)");
    }


    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }
}
