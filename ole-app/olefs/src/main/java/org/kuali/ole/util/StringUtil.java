package org.kuali.ole.util;

import org.kuali.ole.OLEConstants;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: premkb
 * Date: 1/11/13
 * Time: 2:00 PM
 * To change this template use File | Settings | File Templates.
 */
public class StringUtil {

    public static String trimEmptyNullValues(String value){
        if(value!=null){
            value = value.trim();
            if(value.equals("null")){
                value = "";
            }
        }
        return value;
    }
    public static String trimHashNullValues(String value){
        if(value!=null){
            value = value.trim();
            if(value.equals("null")|| value.isEmpty()){
                value = OLEConstants.DELIMITER_HASH;
            }
        }
        return value;
    }

    public static String makeUrlClickable(List<String> protocolList, String content) {
        content = content.replace(" ", " ");
        StringBuffer result = new StringBuffer();
        String clickableUrl = "";
        int count = 0;
        int lenth = content.length();
        while (count < lenth) {
            String textToScan = content.substring(count);
            boolean urlFound = false;
            int spaceIndex = -1;
            String url = "";
            for (String protocol : protocolList) {
                if (textToScan.startsWith(protocol)) {
                    urlFound = true;
                    spaceIndex = textToScan.indexOf(" ");
                    if (spaceIndex > 0)
                        url = textToScan.substring(0, (spaceIndex));
                    else
                        url = textToScan.substring(0);
                    break;
                }
            }
            if (urlFound) {
                clickableUrl = "<a href='" + url + "' target='_blank'>" + url + "</a>";
                count = count + url.length();
                result.append(clickableUrl);
            } else {
                result.append(content.charAt(count));
                count = count + 1;
            }
        }
        return result.toString();

    }
}
