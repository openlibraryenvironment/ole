package org.kuali.ole.service;

import org.kuali.ole.deliver.bo.*;
import org.kuali.rice.kim.impl.identity.email.EntityEmailBo;
import org.kuali.rice.kim.impl.identity.entity.EntityBo;
import org.kuali.rice.kim.impl.identity.phone.EntityPhoneBo;
import org.kuali.rice.kim.impl.identity.type.EntityTypeContactInfoBo;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: ?
 * Date: 6/1/12
 * Time: 8:28 PM
 * To change this template use File | Settings | File Templates.
 */
public interface OlePatronHelperService {


    public boolean deletePatron(OlePatronDocument olePatronDocument);

    public boolean checkAddressSource(List<OleAddressBo> oleAddresses);

    public boolean checkPhoneSource(List<OlePhoneBo> olePhones);

    public boolean checkEmailSource(List<OleEmailBo> oleEmails);

    public boolean checkPhoneMultipleDefault(List<OleEntityPhoneBo> phoneBoList);

    public boolean checkAddressMultipleDefault(List<OleEntityAddressBo> addrBoList);

    public boolean checkEmailMultipleDefault(List<OleEntityEmailBo> emailBoList);

    public boolean isBorrowerTypeActive(OlePatronDocument olePatronDocument);

    public List<OleAddressBo> retrieveOleAddressBo(EntityBo entityBo,OlePatronDocument olePatronDocument);

    public List<OlePhoneBo> retrieveOlePhoneBo(EntityBo entityBo, OlePatronDocument olePatronDocument);

    public List<OleEmailBo> retrieveOleEmailBo(EntityBo entityBo, OlePatronDocument olePatronDocument);

    public EntityBo copyAndSaveEntityBo(OlePatronDocument patronDocument);

    public EntityBo editAndSaveEntityBo(OlePatronDocument patronDocument);

    public boolean validatePatron(OlePatronDocument patronDocument);

    public String getPatronPreferredAddress(EntityTypeContactInfoBo entityTypeContactInfoBo) throws Exception;

    public String getPatronHomePhoneNumber(EntityTypeContactInfoBo entityTypeContactInfoBo) throws Exception;

    public String getPatronHomeEmailId(EntityTypeContactInfoBo entityTypeContactInfoBo) throws Exception;

    public void sendMailToPatron(List<OleLoanDocument> oleLoanDocumentList) throws Exception;

}
