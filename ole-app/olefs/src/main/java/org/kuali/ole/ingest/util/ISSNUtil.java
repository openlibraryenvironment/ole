package org.kuali.ole.ingest.util;

import org.apache.log4j.Logger;

/**
 * Created with IntelliJ IDEA.
 * User: ?
 * Date: 11/6/12
 * Time: 3:38 PM
 * To change this template use File | Settings | File Templates.
 */
public class ISSNUtil
{
    private String issn;
    private static Logger logger = Logger.getLogger( ISSNUtil.class );


    public ISSNUtil(String issn) throws Exception
    {
        this.issn = issn.replace( "-", "" ); // remove hyphens
        if( this.issn == null || this.issn.length() != 8 )
        {
            logger.debug( "ISSN length=" + this.issn.length() );
            throw new Exception( "Bad ISSN length:" + this.issn );
        }
    }


    public ISSNUtil(int issn)
    {}


    public int getCheckSum()
    {
        int sum = 0;
        int weight = 0;

        for( int i = 0; i < 7; i++ )
        {
            switch( i ){
                case 0:
                    weight = 8;
                    break;
                case 1:
                    weight = 7;
                    break;
                case 2:
                    weight = 6;
                    break;
                case 3:
                    weight = 5;
                    break;
                case 4:
                    weight = 4;
                    break;
                case 5:
                    weight = 3;
                    break;
                case 6:
                    weight = 2;
                    break;
            }

            char c = issn.charAt( i );
            int n = Character.digit( c, 10 ); // convert character to digit
            sum += ( n * weight );
        }

        int cSum = sum % 11;

        return cSum == 0 ? cSum : 11 - cSum;
    }


    /**
     * @return whether this ISBN has a correct checksum
     */
    public boolean hasCorrectChecksum()
    {
        char checksumChar;
        int checksum = getCheckSum(); // get the calculated check sum digit

        if( checksum == 10 )
        {
            checksumChar = 'X';
        }
        else
        {
            checksumChar = Character.forDigit( checksum, 10 ); // convert digit to character
        }

        char fileDigit = issn.charAt( 7 ); // check sum char

        return checksumChar == fileDigit;
    }

}
