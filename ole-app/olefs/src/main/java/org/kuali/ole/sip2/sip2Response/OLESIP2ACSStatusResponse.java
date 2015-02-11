package org.kuali.ole.sip2.sip2Response;

import org.apache.commons.lang3.StringUtils;
import org.kuali.ole.sip2.common.MessageUtil;
import org.kuali.ole.sip2.constants.OLESIP2Constants;
import org.kuali.ole.sip2.requestParser.OLESIP2SCStatusRequestParser;

/**
 * Created by gayathria on 1/9/14.
 */
public class OLESIP2ACSStatusResponse extends OLESIP2Response {

    public OLESIP2ACSStatusResponse() {
        this.code = OLESIP2Constants.ACS_STATUS_RESPONSE;
    }


    public String getSIP2ACSStatusResponse(OLESIP2SCStatusRequestParser sip2SCStatusRequestParser) {

        StringBuilder builder = new StringBuilder();

        builder.append(code);
        builder.append(OLESIP2Constants.Y);
        builder.append(OLESIP2Constants.Y);
        builder.append(OLESIP2Constants.Y);
        builder.append(OLESIP2Constants.Y);
        builder.append(OLESIP2Constants.N);
        builder.append(OLESIP2Constants.N);
        builder.append(OLESIP2Constants.TIME_OUT_PERIOD);//TimeOut Period
        builder.append(OLESIP2Constants.RETRIES_ALLOWED);//Retries Allowed
        builder.append(MessageUtil.getSipDateTime());//Date/Time Sync
        builder.append(OLESIP2Constants.SIP_VERSION);//SIP Version
        builder.append(OLESIP2Constants.SPLIT+
                        OLESIP2Constants.INSTITUTION_ID_CODE);
        builder.append(StringUtils.isNotBlank(sip2SCStatusRequestParser.getInstitutionId())?
                        sip2SCStatusRequestParser.getInstitutionId():OLESIP2Constants.INSTITUTION);
        builder.append(OLESIP2Constants.SPLIT+
                        OLESIP2Constants.TERMINAL_LOCATION_CODE);
        builder.append(OLESIP2Constants.INSTITUTION);
        builder.append(OLESIP2Constants.SPLIT+
                        OLESIP2Constants.SUPPORTED_MSG_CODE);
        builder.append(OLESIP2Constants.Y);
        builder.append(OLESIP2Constants.Y);
        builder.append(OLESIP2Constants.Y);
        builder.append(OLESIP2Constants.Y);
        builder.append(OLESIP2Constants.Y);
        builder.append(OLESIP2Constants.Y);
        builder.append(OLESIP2Constants.Y);
        builder.append(OLESIP2Constants.Y);
        builder.append(OLESIP2Constants.Y);
        builder.append(OLESIP2Constants.Y);
        builder.append(OLESIP2Constants.Y);
        builder.append(OLESIP2Constants.N);
        builder.append(OLESIP2Constants.Y);
        builder.append(OLESIP2Constants.Y);
        builder.append(OLESIP2Constants.Y);
        builder.append(OLESIP2Constants.Y);
        builder.append(OLESIP2Constants.SPLIT+
                        OLESIP2Constants.SCREEN_MSG_CODE);
        builder.append(OLESIP2Constants.ACS_SCREEN_MSG);
        if (StringUtils.isNotBlank(sip2SCStatusRequestParser.getSequenceNum())) {
            builder.append(OLESIP2Constants.SPLIT+
                        OLESIP2Constants.SEQUENCE_NUM_CODE);
            builder.append(sip2SCStatusRequestParser.getSequenceNum());
            builder.append(MessageUtil.computeChecksum(builder.toString()));
        }

        return builder.toString() + '\r';
    }

}
