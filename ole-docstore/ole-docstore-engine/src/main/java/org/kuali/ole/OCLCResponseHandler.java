package org.kuali.ole;

/**
 * User: peris
 * Date: 2/7/13
 * Time: 2:59 PM
 */
public class OCLCResponseHandler {
    public String getMARCXMLFromOCLCResponse(String oclcResponse) {
        MRKToMARCXMLConverter mrkToMARCXMLConverter = new MRKToMARCXMLConverter();
        String transformedResponse = mrkToMARCXMLConverter.convert(oclcResponse);
        return transformedResponse;
    }
}
