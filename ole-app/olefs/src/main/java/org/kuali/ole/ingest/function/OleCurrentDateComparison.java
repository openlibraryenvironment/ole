package org.kuali.ole.ingest.function;

import org.apache.log4j.Logger;
import org.kuali.rice.krms.framework.engine.Function;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

/**
 * PatronMembershipExpiration is used for validating checkMembershipExpirationDate
 */
public class OleCurrentDateComparison implements Function {
    private static final Logger LOG = Logger.getLogger(OleCurrentDateComparison.class);
    /**
     * This method takes the request and invoke the methods to checkMembershipExpirationDate.
     * @param arguments
     * @return  Object
     */
    @Override
    public Object invoke(Object... arguments) {
        LOG.info(" --------- Inside OleCurrentDateComparison ------------");
        boolean comparison = false;
        if(arguments!=null && arguments.length==1){
            Object object = arguments[0];
            if(object==null)
                return false;
            Date expirationDate = null;
            if(object instanceof String){
                DateFormat formatter = new SimpleDateFormat("dd-MM-yyyy hh:mm:ss");
                try {
                    expirationDate= (Date)formatter.parse((String)object);
                } catch (ParseException e) {
                    e.printStackTrace();
                }

            }else{
                expirationDate =(Date)object;
            }
            comparison =checkMembershipExpirationDate(expirationDate);
        }

        return comparison;
    }

    /**
     *  This method check the membership ExpiryDate with current date.
     * @param expDate
     * @return  boolean
     */
    private boolean checkMembershipExpirationDate(Date expDate){
        Date curDat = new Date();
        if(expDate!=null && curDat.compareTo(expDate)>=0){
            return  true;
        }
        return false;
    }
}
