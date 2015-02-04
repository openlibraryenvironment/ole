package org.kuali.ole.ingest.function;

import org.apache.log4j.Logger;
import org.kuali.ole.OLEConstants;
import org.kuali.rice.krms.framework.engine.Function;

import java.util.ArrayList;
import java.util.List;

/**
 * OleContainsComparison is used for checking an argument contains in another argument.
 */
public class OleContainsComparison implements Function {
    private static final Logger LOG = Logger.getLogger(OleContainsComparison.class);
    /**
     * This method takes the request and invoke the methods  for checking an argument contains in another argument.
     * @param arguments
     * @return  Object
     */
    @Override
    public Object invoke(Object... arguments) {
        LOG.debug(" --------- Inside OleContainsComparison ------------");
        boolean result = true;
        if(arguments!=null && arguments.length==2){
            String operatorsCirculationLocations =arguments[0]!=null?(String) arguments[0]:"";
            String itemLocation = arguments[1]!=null?(String) arguments[1]:"";
            String[] splitOperatorLocationTerm = operatorsCirculationLocations.split(OLEConstants.DELIMITER_HASH);
            String[] splitLocationTerm = itemLocation.split(OLEConstants.DELIMITER_HASH);
            for(String itemLocationTerm: splitLocationTerm){
                for(String location: splitOperatorLocationTerm){
                    result = true;
                    String[] splitTerm = itemLocationTerm.split("/");
                    for(String term:splitTerm){
                        result &= location.contains(term);
                    }
                    if(result){
                        return result;
                    }
                }
            }
        }
       return result;
    }

}
