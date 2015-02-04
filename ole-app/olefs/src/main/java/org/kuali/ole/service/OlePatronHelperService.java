package org.kuali.ole.service;

import org.kuali.ole.deliver.bo.OleAddressBo;
import org.kuali.ole.deliver.bo.OleEntityAddressBo;
import org.kuali.ole.deliver.bo.OlePatronDocument;
import org.kuali.rice.kim.impl.identity.email.EntityEmailBo;
import org.kuali.rice.kim.impl.identity.entity.EntityBo;
import org.kuali.rice.kim.impl.identity.phone.EntityPhoneBo;

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

    public boolean checkPhoneMultipleDefault(List<EntityPhoneBo> phoneBoList);

    public boolean checkAddressMultipleDefault(List<OleEntityAddressBo> addrBoList);

    public boolean checkEmailMultipleDefault(List<EntityEmailBo> emailBoList);

    public boolean isBorrowerTypeActive(OlePatronDocument olePatronDocument);

    public List<OleAddressBo> retrieveOleAddressBo(EntityBo entityBo,OlePatronDocument olePatronDocument);

    public EntityBo copyAndSaveEntityBo(OlePatronDocument patronDocument);

    public EntityBo editAndSaveEntityBo(OlePatronDocument patronDocument);

    public boolean validatePatron(OlePatronDocument patronDocument);
}
