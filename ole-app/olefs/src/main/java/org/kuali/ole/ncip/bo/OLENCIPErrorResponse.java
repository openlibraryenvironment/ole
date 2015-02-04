package org.kuali.ole.ncip.bo;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * Created with IntelliJ IDEA.
 * User: sheiksalahudeenm
 * Date: 8/19/13
 * Time: 8:38 PM
 * To change this template use File | Settings | File Templates.
 */
public class OLENCIPErrorResponse {
    private Map<String,String> errorMap=new HashMap<String,String>();
    public static final String parentStartTag ="<response>";
    public static final String startTag ="<";
    public static final String endTag =">";
    public static final String slash ="/";
    public static final String parentEndTag ="</response>";


    public Map<String, String> getErrorMap() {
        return errorMap;
    }

    public void setErrorMap(Map<String, String> errorMap) {
        this.errorMap = errorMap;
    }
    public String getErrorXml(String serviceName){
        StringBuffer responseMessge=new StringBuffer();
        if(this.getErrorMap()!=null){
            //responseMessge.append(parentStartTag);
            if(serviceName!=null && !serviceName.equalsIgnoreCase("")){
                responseMessge.append(startTag+serviceName+endTag);
            }
            else{
                responseMessge.append(parentStartTag);
            }
            for (Map.Entry<String, String> entry : this.getErrorMap().entrySet()) {
                responseMessge.append(startTag+entry.getKey()+endTag+entry.getValue()+startTag+slash+entry.getKey()+endTag);

            }
            if(serviceName!=null && !serviceName.equalsIgnoreCase("")){
                responseMessge.append(startTag+slash+serviceName+endTag);
            }
            else{
                responseMessge.append(parentEndTag);
            }
           // responseMessge.append(parentEndTag);
            return responseMessge.toString();
        }
        return null;
    }
}
