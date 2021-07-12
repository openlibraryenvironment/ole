package org.kuali.ole.request;

import org.apache.log4j.Logger;
import org.kuali.ole.constants.OLESIP2Constants;

/**
 * Created by gayathria on 1/12/14.
 */
public class OLESIP2LoginRequestParser extends OLESIP2RequestParser {

    private static final Logger LOG = Logger.getLogger(OLESIP2LoginRequestParser.class);

    public Integer uidAlgorithm;
    public Integer passwordAlgorithm;
    public String loginUserId;
    public String loginPassword;
    public String locationCode;

    public OLESIP2LoginRequestParser(String requestData) {
        this.parseLoginRequest(requestData);
    }


    public void parseLoginRequest(String requestData) {

        String[] requestDataArray = requestData.split("\\|");
        LOG.info("Entry OLESIP2LoginRequestParser.parseLoginRequest >>>>> " + requestData);

        try {
            for (String data : requestDataArray) {
                if (data.startsWith(OLESIP2Constants.LOGIN_REQUEST)) {
                    code = data.substring(0, 2);
                    uidAlgorithm = stringToInt(String.valueOf(data.charAt(2)));
                    passwordAlgorithm = stringToInt(String.valueOf(data.charAt(3)));
                    loginUserId = data.substring(6);
                }
                if (data.startsWith(OLESIP2Constants.LOGIN_PWD_ID_CODE)) {
                    loginPassword = (data.replaceFirst(OLESIP2Constants.LOGIN_PWD_ID_CODE, "")).trim();
                }
                if (data.startsWith(OLESIP2Constants.LOCATION_CODE_SIP)) {
                    locationCode = (data.replaceFirst(OLESIP2Constants.LOCATION_CODE_SIP, "")).trim();
                }
                if (data.startsWith(OLESIP2Constants.SEQUENCE_NUM_CODE)) {
                    sequenceNum = data.substring(2, 5);
                    checkSum = data.substring(5);
                }
            }

        } catch (Exception e) {
            LOG.error(e.getMessage(), e);
        }
        LOG.info("Entry OLESIP2LoginRequestParser.parseLoginRequest(String requestData)");
    }


    public Integer getUidAlgorithm() {
        return uidAlgorithm;
    }

    public void setUidAlgorithm(Integer uidAlgorithm) {
        this.uidAlgorithm = uidAlgorithm;
    }

    public Integer getPasswordAlgorithm() {
        return passwordAlgorithm;
    }

    public void setPasswordAlgorithm(Integer passwordAlgorithm) {
        this.passwordAlgorithm = passwordAlgorithm;
    }

    public String getLoginUserId() {
        return loginUserId;
    }

    public void setLoginUserId(String loginUserId) {
        this.loginUserId = loginUserId;
    }

    public String getLocationCode() {
        return locationCode;
    }

    public void setLocationCode(String locationCode) {
        this.locationCode = locationCode;
    }

    public String getLoginPassword() {
        return loginPassword;
    }

    public void setLoginPassword(String loginPassword) {
        this.loginPassword = loginPassword;
    }
}
