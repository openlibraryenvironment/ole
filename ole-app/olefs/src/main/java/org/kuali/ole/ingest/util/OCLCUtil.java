package org.kuali.ole.ingest.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created with IntelliJ IDEA.
 * User: ?
 * Date: 11/8/12
 * Time: 2:41 PM
 * To change this template use File | Settings | File Templates.
 */
public class OCLCUtil {

    String normalizedOclc = null;

    /**
     * Gets oclc exists in the docstore and normalizes it.
     * @param oclc
     * @return normalizedOclc
     */
    public String normalizedOclc(String oclc) {

        StringBuffer buffer = new StringBuffer();
        String regex = "(ocl77|ocm|ocn|on)";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(oclc);
        String modifiedString = matcher.replaceAll(""); // removes ocl77,ocm,ocn,on
        String[] splitValues = modifiedString.split(" ");
        if (splitValues.length > 1) {
            for (String splitValue : splitValues) {
                if(splitValue.contains("(") && splitValue.contains(")")) {
                    buffer.append(splitValue);
                } else {
                    buffer.append((Integer.parseInt(splitValue))); //removes leading zeros
                }
            }
        }else {
            String ocolc = modifiedString.substring(modifiedString.indexOf("("), modifiedString.indexOf(")") + 1);
            buffer.append(ocolc);
            String content = modifiedString.substring(modifiedString.indexOf(")")+1,modifiedString.length());
            buffer.append((Integer.parseInt(content))); // removes leading zeros
        }
        normalizedOclc = buffer.toString();
        return normalizedOclc;
    }

}
