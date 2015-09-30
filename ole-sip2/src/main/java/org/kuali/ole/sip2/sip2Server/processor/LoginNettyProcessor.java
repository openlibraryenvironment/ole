package org.kuali.ole.sip2.sip2Server.processor;

import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;
import org.kuali.ole.request.OLESIP2LoginRequestParser;
import org.kuali.ole.response.OLESIP2LoginResponse;
import org.kuali.ole.sip2.response.OLESIP2LoginTurnedOffResponse;

import java.util.Properties;

/**
 * Created by chenchulakshmig on 9/29/15.
 */
public class LoginNettyProcessor extends NettyProcessor {
    private final Properties properties;
    private String serverURL;

    public LoginNettyProcessor(Properties properties, String serverURL) {
        this.properties = properties;
        this.serverURL = serverURL;
    }

    @Override
    public boolean isInterested(String code) {
        return code.equals("93");
    }

    @Override
    public String process(String requestData) {
        String response = "";
        OLESIP2LoginRequestParser loginRequestParser = new OLESIP2LoginRequestParser(requestData);

        requestData = createJSONForLogin(loginRequestParser.getLoginUserId(), loginRequestParser.getLoginPassword());

        response = postRequest(requestData, "/loginSIP2", serverURL);
        JSONObject jsonObject = null;
        boolean isValidUser = false;
        try {
            jsonObject = new JSONObject(response);
            isValidUser = jsonObject.getBoolean("loginStatus");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        OLESIP2LoginResponse sip2LoginResponseParser = new OLESIP2LoginResponse();
        response = sip2LoginResponseParser.getSIP2LoginResponse(isValidUser, loginRequestParser);
        return response;
    }

    private String createJSONForLogin(String loginUserId, String loginPassword) {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("loginUserId", loginUserId);
            jsonObject.put("loginPassword", loginPassword);
            jsonObject.put("requestFormatType", "JSON");
            jsonObject.put("responseFormatType", "JSON");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jsonObject.toString();
    }

    @Override
    public boolean isServiceTurnedOn() {
        return properties.getProperty("sip2.service.login").equalsIgnoreCase("yes");
    }

    @Override
    public String getResponseForServiceTurnedOff(String requestData) {
        OLESIP2LoginTurnedOffResponse olesip2LoginTurnedOffResponse = new OLESIP2LoginTurnedOffResponse();
        return olesip2LoginTurnedOffResponse.getOLESIP2LoginTurnedOffResponse(requestData);
    }
}
