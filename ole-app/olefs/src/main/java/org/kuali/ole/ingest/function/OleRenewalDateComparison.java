package org.kuali.ole.ingest.function;

import org.apache.log4j.Logger;
import org.kuali.ole.DataCarrierService;
import org.kuali.ole.OLEConstants;
import org.kuali.rice.core.api.resourceloader.GlobalResourceLoader;
import org.kuali.rice.krms.framework.engine.Function;

import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * PatronMembershipExpiration is used for validating checkMembershipExpirationDate
 */
public class OleRenewalDateComparison implements Function {
    private static final Logger LOG = Logger.getLogger(OleRenewalDateComparison.class);
    /**
     * This method takes the request and invoke the methods to checkMembershipExpirationDate.
     * @param arguments
     * @return  Object
     */
    @Override
    public Object invoke(Object... arguments) {
        LOG.info(" --------- Inside OleRenewalDateComparison ------------");
        DataCarrierService dataCarrierService = GlobalResourceLoader.getService(OLEConstants.DATA_CARRIER_SERVICE);
        boolean comparison = false;
        if(arguments!=null && arguments.length==1){
            Object object = arguments[0];
            if(object==null)
                return false;
            Date orginalDueDate = null;
            if(object instanceof String){
                DateFormat formatter = new SimpleDateFormat("dd-MM-yyyy hh:mm:ss");
                try {
                    orginalDueDate= (Date)formatter.parse((String)object);
                } catch (ParseException e) {
                    e.printStackTrace();
                }

            }else{
                orginalDueDate =(Date)object;
            }
            if(dataCarrierService.getData(OLEConstants.DUE_DATE)!=null){
                Timestamp newDueDate =  (Timestamp) dataCarrierService.getData(OLEConstants.DUE_DATE);
                comparison =checkRenewalDate(orginalDueDate,newDueDate);
            }
        }

        return comparison;
    }

    /**
     *  This method check the membership ExpiryDate with current date.
     * @param orginalDueDate
     * @param newDueDate
     * @return  boolean
     */
    private boolean checkRenewalDate(Date orginalDueDate,Date newDueDate){
        if(orginalDueDate!=null && newDueDate.compareTo(orginalDueDate)>=0){
            return  true;
        }
        return false;
    }
}
