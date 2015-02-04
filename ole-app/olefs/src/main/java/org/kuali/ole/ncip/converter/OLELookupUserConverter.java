package org.kuali.ole.ncip.converter;

import com.thoughtworks.xstream.XStream;
import org.apache.log4j.Logger;
import org.kuali.ole.ncip.bo.*;
import org.kuali.rice.kim.impl.identity.address.EntityAddressBo;
import org.kuali.rice.kim.impl.identity.email.EntityEmailBo;
import org.kuali.rice.kim.impl.identity.name.EntityNameBo;
import org.kuali.rice.kim.impl.identity.phone.EntityPhoneBo;
import org.kuali.rice.krad.bo.PersistableBusinessObjectBase;

/**
 * Created with IntelliJ IDEA.
 * User: maheswarang
 * Date: 7/21/13
 * Time: 5:02 PM
 * To change this template use File | Settings | File Templates.
 */
public class OLELookupUserConverter {
    final Logger LOG = Logger.getLogger(OLELookupUserConverter.class);

    public String generateLookupUserResponseXml(OLELookupUser lookupUser){
        XStream xstream = new XStream();
        xstream.alias("lookupUser",OLELookupUser.class);
        xstream.alias("userPrivilege",OLEUserPrivilege.class);
        xstream.alias("hold",OLEHold.class);
        xstream.alias("checkedOutItem",OLECheckedOutItem.class);
        xstream.alias("fine",OLEItemFine.class);
        xstream.alias("patronName",OlePatronNameBo.class);
        xstream.alias("patronEmail",OlePatronEmailBo.class);
        xstream.alias("patronAddress",OlePatronAddressBo.class);
        xstream.alias("patronPhone",OlePatronPhoneBo.class);
        xstream.omitField(OLECheckedOutItems.class,"message");
        xstream.omitField(OLEHolds.class,"message");
        xstream.omitField(OLEItemFines.class,"message");
        xstream.omitField(OLECheckedOutItems.class,"code");
        xstream.omitField(OLEHolds.class,"code");
        xstream.omitField(OLEItemFines.class,"code");
        xstream.aliasField("userPrivileges", OLELookupUser.class, "oleUserPrivileges");
        xstream.alias("holds",OLEHolds.class);
        xstream.aliasField("holdsList",OLEHolds.class,"oleHoldsList");
        xstream.alias("fines",OLEItemFines.class);
        xstream.aliasField("finesList",OLEItemFines.class,"oleItemFineList");
        String response  =xstream.toXML(lookupUser);
        if (LOG.isDebugEnabled())
            LOG.debug(response);
    return  response;
    }



    public OLELookupUser getLookupUser(String xml){
        XStream xstream = new XStream();
        xstream.alias("lookupUser",OLELookupUser.class);
        xstream.alias("userPrivilege",OLEUserPrivilege.class);
        xstream.alias("hold",OLEHold.class);
        xstream.alias("checkedOutItem",OLECheckedOutItem.class);
        xstream.alias("fine",OLEItemFine.class);
        xstream.omitField(OLECheckedOutItems.class,"message");
        xstream.omitField(OLECheckedOutItems.class,"code");
        xstream.omitField(OLEHolds.class,"message");
        xstream.omitField(OLEHolds.class,"code");
        xstream.omitField(OLEItemFines.class,"message");
        xstream.omitField(OLEItemFines.class,"code");
        xstream.alias("patronName",OlePatronNameBo.class);
        xstream.alias("patronEmail",OlePatronEmailBo.class);
        xstream.alias("patronAddress",OlePatronAddressBo.class);
        xstream.alias("patronPhone",OlePatronPhoneBo.class);
        xstream.aliasField("userPrivileges", OLELookupUser.class, "oleUserPrivileges");
        xstream.alias("holds",OLEHolds.class);
        xstream.aliasField("holdsList",OLEHolds.class,"oleHoldsList");
        xstream.alias("fines",OLEItemFines.class);
        xstream.aliasField("finesList",OLEItemFines.class,"oleItemFineList");
        OLELookupUser oleLookupUser   =(OLELookupUser)xstream.fromXML(xml);
        return oleLookupUser;
    }

    public String generateLookupUserJson(String xml) {
        OLELookupUser oleLookupUser = (OLELookupUser)getLookupUser(xml);
        OleCirculationHandler xmlContentHandler = new OleCirculationHandler();
        try{
            return xmlContentHandler.marshalToJSON(oleLookupUser);
        }catch(Exception e){
            LOG.error(e,e);
        }
        return null;
    }
}
