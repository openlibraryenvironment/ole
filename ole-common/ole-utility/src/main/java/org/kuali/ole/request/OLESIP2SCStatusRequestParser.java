package org.kuali.ole.request;

import org.apache.log4j.Logger;
import org.kuali.ole.common.StatusCode;
import org.kuali.ole.constants.OLESIP2Constants;

/**
 * Created by gayathria on 2/12/14.
 */
public class OLESIP2SCStatusRequestParser extends OLESIP2RequestParser {

    private static final Logger LOG = Logger.getLogger(OLESIP2SCStatusRequestParser.class);

    private StatusCode statusCode;
    private String maxPrintWidth;
    private String protocolVersion;

    public OLESIP2SCStatusRequestParser(String requestData) {
        this.parseSCStstusRequest(requestData);
    }


    public void parseSCStstusRequest(String requestData) {

        LOG.info("Entry OLESIP2SCStatusRequestParser.parseSCStstusRequest(String requestData)");
        requestData = requestData.trim();
        code = requestData.substring(0, 2);
        if (String.valueOf(requestData.charAt(2)).equalsIgnoreCase("0"))
            statusCode = StatusCode.OK;
        else if (String.valueOf(requestData.charAt(2)).equalsIgnoreCase("1"))
            statusCode = StatusCode.PRINTER_OUT_OF_PAPER;
        else if (String.valueOf(requestData.charAt(2)).equalsIgnoreCase("2"))
            statusCode = StatusCode.SHUT_DOWN;
        maxPrintWidth = requestData.substring(3, 6);
        protocolVersion = requestData.substring(6, 10);
        if (requestData.length() == 19) {
            if (requestData.substring(10, 12).equalsIgnoreCase(OLESIP2Constants.SEQUENCE_NUM_CODE)) {
                sequenceNum = requestData.substring(12, 15);
                checkSum = requestData.substring(15);
            }
        }
        LOG.info("Exit OLESIP2SCStatusRequestParser.parseSCStstusRequest(String requestData)");
    }

    public StatusCode getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(StatusCode statusCode) {
        this.statusCode = statusCode;
    }

    public String getMaxPrintWidth() {
        return maxPrintWidth;
    }

    public void setMaxPrintWidth(String maxPrintWidth) {
        this.maxPrintWidth = maxPrintWidth;
    }

    public String getProtocolVersion() {
        return protocolVersion;
    }

    public void setProtocolVersion(String protocolVersion) {
        this.protocolVersion = protocolVersion;
    }
}
