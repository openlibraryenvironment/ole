package org.kuali.ole.sip2.requestParser;

import org.apache.log4j.Logger;
import org.kuali.ole.request.OLESIP2RequestParser;
import org.kuali.ole.sip2.constants.OLESIP2Constants;

/**
 * Created by gayathria on 2/12/14.
 */
public class OLESIP2PatronInformationRequestParser extends OLESIP2RequestParser {

    private String language;
    private String summary;
    private String startItem;
    private String endItem;

    private static final Logger LOG = Logger.getLogger(OLESIP2PatronInformationRequestParser.class);


    public OLESIP2PatronInformationRequestParser(String requestData) {
        this.parsePatronInformationRequest(requestData);
    }


    public void parsePatronInformationRequest(String requestData) {

        LOG.info("Entry OLESIP2PatronInformationRequestParser.parsePatronInformation(String requestData)");

        String[] requestDataArray = requestData.split("\\|");
        for (String data : requestDataArray) {
            LOG.info(data);
            if (data.startsWith(OLESIP2Constants.PATRON_INFORMATION_REQUEST)) {
                code = data.substring(0, 2);
                language = data.substring(2, 5);
                transactionDate = data.substring(5, 23);
                summary = data.substring(23, 33);
                institutionId = data.substring(35);
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
        LOG.info("Exit OLESIP2PatronInformationRequestParser.parsePatronInformation(String requestData)");
    }


    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public String getStartItem() {
        return startItem;
    }

    public void setStartItem(String startItem) {
        this.startItem = startItem;
    }

    public String getEndItem() {
        return endItem;
    }

    public void setEndItem(String endItem) {
        this.endItem = endItem;
    }
}
