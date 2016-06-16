package org.kuali.ole.utility;

import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.type.TypeReference;
import org.codehaus.jettison.json.JSONArray;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by SheikS on 3/2/2016.
 */
public class JSONHelperUtil {

    public String getStringValueFromJsonObject(JSONObject jsonObject, String key) {
        String returnValue = null;
        try {
            if (jsonObject.has(key)) {
                returnValue = jsonObject.getString(key);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return returnValue;
    }

    public boolean getBooleanValueFromJsonObject(JSONObject jsonObject, String key) {
        boolean returnValue = false;
        try {
            if (jsonObject.has(key)) {
                returnValue = jsonObject.getBoolean(key);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return returnValue;
    }

    public JSONArray getJSONArrayeFromJsonObject(JSONObject jsonObject, String key) {
        JSONArray returnValue = null;
        try {
            if(jsonObject.has(key)){
                returnValue = jsonObject.getJSONArray(key);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return returnValue;
    }

    public JSONObject getJSONObjectFromJSONObject(JSONObject jsonObject, String key) {
        JSONObject returnObject = null;
        try {
            returnObject = jsonObject.getJSONObject(key);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return returnObject;
    }

    public JSONObject getJSONObjectFromJsonArray(JSONArray jsonArray, int index) {
        JSONObject returnObject = null;
        try {
            returnObject = jsonArray.getJSONObject(index);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return returnObject;
    }

    public List<String> getListFromJSONArray(String operation){
        List ops = new ArrayList();
        try {
            ops = new ObjectMapper().readValue(operation, new TypeReference<List<String>>() {
            });
        } catch (IOException e) {
            e.printStackTrace();
        }

        return ops;

    }
}
