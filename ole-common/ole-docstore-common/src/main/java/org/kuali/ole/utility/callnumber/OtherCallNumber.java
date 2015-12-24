package org.kuali.ole.utility.callnumber;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.solrmarc.callnum.AbstractCallNumber;
import org.solrmarc.callnum.CallNumber;

/**
 * Created with IntelliJ IDEA.
 * User: Pranitha J
 * Date: 7/3/13
 * Time: 12:34 PM
 * To change this template use File | Settings | File Templates.
 */
public class OtherCallNumber extends AbstractCallNumber implements CallNumber {
    private static final Logger Log = LoggerFactory.getLogger(OtherCallNumber.class);
    /**
     * regular expression string for complete SuDoc classification
     * Splits the based on continuous numbers and alphabets
     * Ignore any special char and spaces.
     */
    public static final String SUDOC_REGEX = "[^A-Z0-9]+|(?<=[A-Z])(?=[0-9])|(?<=[0-9])(?=[A-Z])";
    protected String shelfKey;

    private static OtherCallNumber ourInstance = null;

    public static OtherCallNumber getInstance() {
        if (null == ourInstance) {
            ourInstance = new OtherCallNumber();
        }
        return ourInstance;
    }


    @Override
    public void parse(String call) {
        try {
            this.rawCallNum = call;
            this.parse();
        } catch (Exception e) {
            Log.error("Other Call Number Exception" + e);
        }
    }

    @Override
    public String getShelfKey() {
        return shelfKey;
    }

    protected void parse() {
        if (this.rawCallNum != null) {
            this.buildShelfKey();
        }

    }

    protected void buildShelfKey() {
        String upcaseSuDoccallnum = rawCallNum.toUpperCase();
        StringBuffer callNum = new StringBuffer();
        //split the call number based on numbers and alphabets
        String[] cNumSub = upcaseSuDoccallnum.split(SUDOC_REGEX);
        for (String str : cNumSub) {
            if (StringUtils.isNumeric(str)) {   // numbers
                // append zeros to sort Ordinal
                str = StringUtils.leftPad(str, 5, "0"); // constant length 5
                callNum.append(str);
                callNum.append(" ");
            } else {                     // alphabets
                // append spaces to sort Lexicographic
                str = StringUtils.rightPad(str, 5);  // constant length 5
                callNum.append(str);
                callNum.append(" ");
            }
        }
        shelfKey = callNum.toString().trim();
    }

}
