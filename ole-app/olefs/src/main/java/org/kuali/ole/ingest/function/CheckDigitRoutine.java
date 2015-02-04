package org.kuali.ole.ingest.function;

import org.apache.log4j.Logger;
import org.kuali.ole.LoanUtil;
import org.kuali.ole.service.OleCirculationPolicyService;
import org.kuali.ole.service.OleCirculationPolicyServiceImpl;
import org.kuali.rice.krms.framework.engine.Function;

import java.util.ArrayList;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: ?
 * Date: 10/4/12
 * Time: 6:00 PM
 * To change this template use File | Settings | File Templates.
 */
public class CheckDigitRoutine implements Function {

    private static final Logger LOG = Logger.getLogger(CheckDigitRoutine.class);

    @Override
    public Object invoke(Object... arguments) {
        LOG.info("-------- Inside CheckDigitRoutine -------------");
        String pattern = "";
        String itemBarcode = "";
        if(arguments!=null && arguments.length==2){
            pattern = (String)(arguments[0]);
            itemBarcode = (String)(arguments[1]);
        }
        OleCirculationPolicyService oleCirculationPolicyService = new OleCirculationPolicyServiceImpl();
        return oleCirculationPolicyService.isValidBarcode(itemBarcode,pattern);
    }
}
