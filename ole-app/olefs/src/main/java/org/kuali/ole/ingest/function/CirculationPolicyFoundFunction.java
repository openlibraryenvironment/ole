package org.kuali.ole.ingest.function;

import org.kuali.ole.DataCarrierService;
import org.kuali.ole.OLEConstants;
import org.kuali.rice.core.api.resourceloader.GlobalResourceLoader;
import org.kuali.rice.krms.framework.engine.Function;

/**
 * Created with IntelliJ IDEA.
 * User: vivekb
 * Date: 1/24/13
 * Time: 3:33 PM
 * To change this template use File | Settings | File Templates.
 */
public class CirculationPolicyFoundFunction implements Function {
    @Override
    public Object invoke(Object... arguments) {
        DataCarrierService dataCarrierService = GlobalResourceLoader.getService(OLEConstants.DATA_CARRIER_SERVICE);
        String patronId="";
        String itemId="";
        if(arguments!=null && arguments.length==2){
            itemId =arguments[0]!=null?(String) arguments[0]:"";
            patronId = arguments[1]!=null?(String) arguments[1]:"";
        }
        Boolean circulationPolicyFound =(Boolean)dataCarrierService.getData(patronId+itemId);
        circulationPolicyFound = circulationPolicyFound!=null?circulationPolicyFound:false;
        return !circulationPolicyFound;
    }
}
