package org.kuali.ole.fixture;

import org.kuali.ole.deliver.bo.OlePatronDocument;
import org.kuali.rice.kim.impl.identity.address.EntityAddressBo;
import org.kuali.rice.kim.impl.identity.email.EntityEmailBo;
import org.kuali.rice.kim.impl.identity.name.EntityNameBo;
import org.kuali.rice.kim.impl.identity.phone.EntityPhoneBo;
import org.kuali.rice.krad.service.BusinessObjectService;
import org.kuali.rice.krad.service.KRADServiceLocator;

import java.util.Arrays;

/**
 * Created with IntelliJ IDEA.
 * User: ?
 * Date: 5/23/12
 * Time: 3:16 PM
 * To change this template use File | Settings | File Templates.
 */

public enum OlePatronDocumentFixture {
    PATRON_DOC(),;
    private BusinessObjectService businessObjectService;

    private OlePatronDocumentFixture() { }
        public OlePatronDocument createPatron() {
            businessObjectService = KRADServiceLocator.getBusinessObjectService();
            OlePatronDocument patronDocument = new OlePatronDocument();
            EntityNameBo entityNameBo = new EntityNameBo();
            entityNameBo.setNameCode("PRFR");
            entityNameBo.setFirstName("mockFirstName");
            entityNameBo.setLastName("mockLastName");

            EntityAddressBo entityAddressBo = new EntityAddressBo();
            entityAddressBo.setLine1("mockLine1");
            entityAddressBo.setLine2("mockLine2");
            entityAddressBo.setAddressTypeCode("HM");
            entityAddressBo.setStateProvinceCode("IL");

            EntityEmailBo entityEmailBo = new EntityEmailBo();
            entityEmailBo.setEmailTypeCode("HM");
            entityEmailBo.setEmailAddress("test@mock");

            EntityPhoneBo entityPhoneBo = new EntityPhoneBo();
            entityPhoneBo.setPhoneNumber("987654321");
            entityPhoneBo.setCountryCode("US");
            entityPhoneBo.setPhoneTypeCode("MBL");

            OlePatronDocument olePatron= new OlePatronDocument();
            olePatron.setBarcode("mockBarcode");

            olePatron.setName(entityNameBo);
            olePatron.setAddresses(Arrays.asList(entityAddressBo));
            olePatron.setEmails(Arrays.asList(entityEmailBo));
            olePatron.setPhones(Arrays.asList(entityPhoneBo));
            return patronDocument;
        }

}
