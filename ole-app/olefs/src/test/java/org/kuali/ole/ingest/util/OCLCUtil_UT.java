package org.kuali.ole.ingest.util;

import org.apache.log4j.Logger;
import org.junit.Test;


/**
 * Created with IntelliJ IDEA.
 * User: ?
 * Date: 11/8/12
 * Time: 6:52 PM
 * To change this template use File | Settings | File Templates.
 */

public class OCLCUtil_UT {

    private static Logger logger = Logger.getLogger( ISSNUtil_UT.class );

    @Test
    public void testNormalization() throws Exception {
        OCLCUtil oclcUtil = new OCLCUtil();
        String oclc = "(OCoLC) ocm000000002";
        String oclc1 = "(OCoLC)ocm07002373" ;
        String oclc2 = "(OCoLC) ocm00123456 800630";
        String normalizedOCLC = oclcUtil.normalizedOclc(oclc);
        String normalizedOCLC1 = oclcUtil.normalizedOclc(oclc1);
        String normalizedOCLC2 = oclcUtil.normalizedOclc(oclc2);
        if(logger.isInfoEnabled()){
            logger.info( "normalizedOCLC----------->" + normalizedOCLC );
            logger.info( "normalizedOCLC1----------->" + normalizedOCLC1 );
            logger.info( "normalizedOCLC2----------->" + normalizedOCLC2 );
        }
    }
}
