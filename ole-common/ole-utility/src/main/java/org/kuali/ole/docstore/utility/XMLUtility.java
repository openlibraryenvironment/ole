package org.kuali.ole.docstore.utility;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by IntelliJ IDEA.
 * User: Sreekanth
 * Date: 6/4/12
 * Time: 4:20 PM
 * To change this template use File | Settings | File Templates.
 */
public class XMLUtility {
    public static final String FIELD_SEPERATOR = ",";

    public StringBuffer getAllContentText(String content) {
        String reg = "<.*>(.*)<\\/.*?>";
        Pattern p = Pattern.compile(reg);
        StringBuffer buffer = new StringBuffer();
        if (content.length() > 0) {
            Matcher m = p.matcher(content);
            while (m.find()) {
                String allText = m.group(1);
                buffer.append(allText + FIELD_SEPERATOR);
            }
        }
        return buffer;

    }


}
