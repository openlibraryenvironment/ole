package org.kuali.ole.ingest.util;

import junit.framework.Assert;
import org.apache.log4j.Logger;
import org.junit.Test;

/**
 * Created with IntelliJ IDEA.
 * User: ?
 * Date: 11/6/12
 * Time: 3:43 PM
 * To change this template use File | Settings | File Templates.
 */
public class ISSNUtil_UT {

    private static Logger logger = Logger.getLogger( ISSNUtil_UT.class );

    @Test
    public void checkISSN() throws Exception {
        ISSNUtil issn = new ISSNUtil("0378-5955");
        int chekSum = issn.getCheckSum();
        if(logger.isInfoEnabled()){
            logger.info( "chekSum----------->" + chekSum );
        }
        Assert.assertEquals(5,chekSum);
    }
}
