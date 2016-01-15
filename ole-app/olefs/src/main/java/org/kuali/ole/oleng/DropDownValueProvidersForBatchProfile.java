package org.kuali.ole.oleng;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.map.HashedMap;
import org.codehaus.jettison.json.JSONArray;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;
import org.kuali.ole.describe.bo.OleBibliographicRecordStatus;
import org.kuali.ole.docstore.engine.service.storage.rdbms.pojo.AccessLocation;
import org.kuali.ole.docstore.engine.service.storage.rdbms.pojo.AuthenticationTypeRecord;
import org.kuali.ole.docstore.engine.service.storage.rdbms.pojo.ReceiptStatusRecord;
import org.kuali.ole.docstore.engine.service.storage.rdbms.pojo.StatisticalSearchRecord;
import org.kuali.ole.oleng.dao.DescribeDAO;
import org.kuali.ole.oleng.dao.SelectDAO;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * Created by SheikS on 1/4/2016.
 */
public class DropDownValueProvidersForBatchProfile {

    private DescribeDAO describeDAO;
    private SelectDAO selectDAO;
    private Map<String, Method> methodMap;

    public JSONArray populateDropDownValues(String type) {
        JSONArray jsonArray = new JSONArray();

        Method method = getMethodMap().get(type);
        if(null != method) {
            try {
                jsonArray= (JSONArray) method.invoke(this);
            } catch (IllegalArgumentException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (InvocationTargetException e) {
                e.printStackTrace();

            }
        }
        return jsonArray;
    }

    public DescribeDAO getDescribeDAO() {
        return describeDAO;
    }

    public void setDescribeDAO(DescribeDAO describeDAO) {
        this.describeDAO = describeDAO;
    }

    public SelectDAO getSelectDAO() {
        return selectDAO;
    }

    public void setSelectDAO(SelectDAO selectDAO) {
        this.selectDAO = selectDAO;
    }

    public JSONArray prepareAuthenticationType() throws JSONException {
        JSONArray jsonArray = new JSONArray();
        List<AuthenticationTypeRecord> authenticationTypeRecords = getDescribeDAO().fetchAuthenticationTypeRecords();
        if(CollectionUtils.isNotEmpty(authenticationTypeRecords)) {
            for (Iterator<AuthenticationTypeRecord> iterator = authenticationTypeRecords.iterator(); iterator.hasNext(); ) {
                AuthenticationTypeRecord authenticationTypeRecord = iterator.next();
                JSONObject jsonObject = new JSONObject();
                jsonObject.put(OleNGConstants.ID, authenticationTypeRecord.getCode());
                jsonObject.put(OleNGConstants.VALUE, authenticationTypeRecord.getName());
                jsonArray.put(jsonObject);
            }
        }
        return jsonArray;
    }

    public JSONArray prepareBibStatus(){
        List<OleBibliographicRecordStatus> bibStatusList = getDescribeDAO().fetchAllBibStatus();
        JSONArray jsonArray = new JSONArray();
        try {
            if(CollectionUtils.isNotEmpty(bibStatusList)) {
                for (Iterator<OleBibliographicRecordStatus> iterator = bibStatusList.iterator(); iterator.hasNext(); ) {
                    OleBibliographicRecordStatus bibliographicRecordStatus = iterator.next();
                    JSONObject jsonObject = new JSONObject();
                    jsonObject.put(OleNGConstants.ID,bibliographicRecordStatus.getBibliographicRecordStatusId());
                    jsonObject.put(OleNGConstants.VALUE,bibliographicRecordStatus.getBibliographicRecordStatusName());
                    jsonArray.put(jsonObject);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return jsonArray;
    }

    public JSONArray prepareReceiptStatus(){
        List<ReceiptStatusRecord> bibStatusList = getDescribeDAO().fetchReceiptStatusRecords();
        JSONArray jsonArray = new JSONArray();
        try {
            if(CollectionUtils.isNotEmpty(bibStatusList)) {
                for (Iterator<ReceiptStatusRecord> iterator = bibStatusList.iterator(); iterator.hasNext(); ) {
                    ReceiptStatusRecord receiptStatusRecord = iterator.next();
                    JSONObject jsonObject = new JSONObject();
                    jsonObject.put(OleNGConstants.ID,receiptStatusRecord.getCode());
                    jsonObject.put(OleNGConstants.VALUE,receiptStatusRecord.getName());
                    jsonArray.put(jsonObject);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return jsonArray;
    }

    public JSONArray prepareAccessLocation(){
        List<AccessLocation> accessLocations = getDescribeDAO().fetchAccessLocations();
        JSONArray jsonArray = new JSONArray();
        try {
            if(CollectionUtils.isNotEmpty(accessLocations)) {
                for (Iterator<AccessLocation> iterator = accessLocations.iterator(); iterator.hasNext(); ) {
                    AccessLocation accessLocation = iterator.next();
                    JSONObject jsonObject = new JSONObject();
                    jsonObject.put(OleNGConstants.ID,accessLocation.getCode());
                    jsonObject.put(OleNGConstants.VALUE,accessLocation.getValue());
                    jsonArray.put(jsonObject);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return jsonArray;
    }

    public JSONArray prepareStatisticalSearchCode(){
        List<StatisticalSearchRecord> statisticalSearchRecords = getDescribeDAO().fetchStatisticalSearchCodes();
        JSONArray jsonArray = new JSONArray();
        try {
            if(CollectionUtils.isNotEmpty(statisticalSearchRecords)) {
                for (Iterator<StatisticalSearchRecord> iterator = statisticalSearchRecords.iterator(); iterator.hasNext(); ) {
                    StatisticalSearchRecord statisticalSearchRecord = iterator.next();
                    JSONObject jsonObject = new JSONObject();
                    jsonObject.put(OleNGConstants.ID,statisticalSearchRecord.getCode());
                    jsonObject.put(OleNGConstants.VALUE,statisticalSearchRecord.getName());
                    jsonArray.put(jsonObject);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return jsonArray;
    }

    public Map<String, Method> getMethodMap() {
        if(null == methodMap) {
            try {
                methodMap = new HashedMap();
                methodMap.put("Authentication Type", this.getClass().getMethod("prepareAuthenticationType"));
                methodMap.put("Bib Status", this.getClass().getMethod("prepareBibStatus"));
                methodMap.put("Receipt Status", this.getClass().getMethod("prepareReceiptStatus"));
                methodMap.put("Access Location", this.getClass().getMethod("prepareAccessLocation"));
                methodMap.put("Statistical Code", this.getClass().getMethod("prepareStatisticalSearchCode"));
            } catch (SecurityException e) {
                e.printStackTrace();
            } catch (NoSuchMethodException e) {
                e.printStackTrace();
            }
        }
        return methodMap;
    }
}
