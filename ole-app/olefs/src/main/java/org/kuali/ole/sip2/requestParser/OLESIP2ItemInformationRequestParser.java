package org.kuali.ole.sip2.requestParser;

import org.apache.log4j.Logger;
import org.kuali.ole.request.OLESIP2RequestParser;
import org.kuali.ole.sip2.constants.OLESIP2Constants;

/**
 * Created by gayathria on 2/12/14.
 */
public class OLESIP2ItemInformationRequestParser extends OLESIP2RequestParser {


    private static final Logger LOG = Logger.getLogger(OLESIP2ItemInformationRequestParser.class);


    public OLESIP2ItemInformationRequestParser(String requestData) {
        this.parseItemInformationRequest(requestData);
    }


    public void parseItemInformationRequest(String requestData) {

        LOG.info("Entry OLESIP2ItemInformationRequestParser.parseItemInformation(String requestData)");
        String[] requestDataArray = requestData.split("\\|");

        for (String data : requestDataArray) {
            LOG.info(data);

            if (data.startsWith(OLESIP2Constants.ITEM_INFORMATION_REQUEST)) {
                code = data.substring(0, 2);
                transactionDate = data.substring(2, 20);
                institutionId = data.substring(22);
            }
            if (data.startsWith(OLESIP2Constants.ITEM_IDENTIFIER_CODE)) {
                itemIdentifier = (data.replaceFirst(OLESIP2Constants.ITEM_IDENTIFIER_CODE, "")).trim();
            }
            if (data.startsWith(OLESIP2Constants.TERMINAL_PWD_CODE)) {
                terminalPassword = (data.replaceFirst(OLESIP2Constants.TERMINAL_PWD_CODE, "")).trim();
            }
            if (data.startsWith(OLESIP2Constants.SEQUENCE_NUM_CODE)) {
                sequenceNum = data.substring(2, 5);
                checkSum = data.substring(5);
            }
        }
        LOG.info("Exit OLESIP2ItemInformationRequestParser.parseItemInformation(String requestData)");
    }

}
