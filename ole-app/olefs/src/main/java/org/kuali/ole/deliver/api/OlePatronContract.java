package org.kuali.ole.deliver.api;

import org.kuali.rice.core.api.mo.common.Identifiable;
import org.kuali.rice.core.api.mo.common.Versioned;
import org.kuali.rice.kim.api.identity.address.EntityAddressContract;
import org.kuali.rice.kim.api.identity.email.EntityEmailContract;
import org.kuali.rice.kim.api.identity.entity.EntityContract;
import org.kuali.rice.kim.api.identity.name.EntityNameContract;
import org.kuali.rice.kim.api.identity.phone.EntityPhoneContract;

import java.util.Date;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: ?
 * Date: 5/25/12
 * Time: 12:31 PM
 * To change this template use File | Settings | File Templates.
 */
public interface OlePatronContract extends Versioned, Identifiable {

    public String getOlePatronId();

    public String getBarcode();

    public String getBorrowerType();

    public boolean isActiveIndicator();

    public boolean isGeneralBlock();

    public boolean isPagingPrivilege();

    public boolean isCourtesyNotice();

    public boolean isDeliveryPrivilege();

    public Date getExpirationDate();

    public Date getActivationDate();

    public OleBorrowerTypeContract getOleBorrowerType();

    public List<? extends EntityAddressContract> getAddresses();

    public List<? extends EntityEmailContract> getEmails();

    public EntityNameContract getName();

    public List<? extends EntityPhoneContract> getPhones();

    public EntityContract getEntity();

    public List<? extends OlePatronNotesContract> getNotes();

    public List<? extends OleEntityAddressContract> getOleEntityAddressBo();

    public List<? extends OleEntityPhoneContract> getOleEntityPhoneBo();

    public List<? extends OleEntityEmailContract> getOleEntityEmailBo();

    public List<? extends OlePatronAffiliationContract> getPatronAffiliations();

    public List<? extends OleProxyPatronContract> getOleProxyPatronDocuments();

    //public List<? extends OlePatronContract> getOlePatronDocuments();

    public String getGeneralBlockNotes();

    public List<? extends OlePatronLostBarcodeContract> getLostBarcodes();

    public String getSource();

    public String getStatisticalCategory();

    public List<? extends OleAddressContract> getOleAddresses();

    public List<? extends OlePhoneContract> getOlePhones();

    public List<? extends OleEmailContract> getOleEmails();

    public List<? extends OlePatronLocalIdentificationContract> getOlePatronLocalIds();
}