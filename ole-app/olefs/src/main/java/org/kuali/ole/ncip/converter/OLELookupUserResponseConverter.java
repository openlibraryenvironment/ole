package org.kuali.ole.ncip.converter;

import com.thoughtworks.xstream.XStream;
import org.apache.log4j.Logger;
import org.kuali.ole.ncip.bo.OLELookupUserResponse;
import org.kuali.ole.ncip.bo.OLEUserPrivilege;

/**
 * Created with IntelliJ IDEA.
 * User: sheiksalahudeenm
 * Date: 9/4/13
 * Time: 12:05 PM
 * To change this template use File | Settings | File Templates.
 */
public class OLELookupUserResponseConverter {
    final Logger LOG = Logger.getLogger(OLELookupUserResponseConverter.class);

    public String generateLookupUserResponseXml(OLELookupUserResponse lookupUser){
        XStream xstream = new XStream();
        xstream.alias("lookupUser",OLELookupUserResponse.class);
        xstream.alias("userPrivilege",OLEUserPrivilege.class);
        xstream.aliasField("userPrivileges", OLELookupUserResponse.class, "userPrivileges");
        String response  =xstream.toXML(lookupUser);

        System.out.println(response);
        return  response;
    }



    public OLELookupUserResponse getLookupUser(String xml){
        XStream xstream = new XStream();
        xstream.alias("lookupUser",OLELookupUserResponse.class);
        xstream.alias("userPrivilege",OLEUserPrivilege.class);
        xstream.aliasField("userPrivileges", OLELookupUserResponse.class, "userPrivileges");
        OLELookupUserResponse oleLookupUser   =(OLELookupUserResponse)xstream.fromXML(xml);
        return oleLookupUser;
    }

    public String generateLookupUserJson(String xml) {
        OLELookupUserResponse oleLookupUser = (OLELookupUserResponse)getLookupUser(xml);
        OleCirculationHandler xmlContentHandler = new OleCirculationHandler();
        try{
            return xmlContentHandler.marshalToJSON(oleLookupUser);
        }catch(Exception e){
            LOG.error(e,e);
        }
        return null;
    }




}
