package org.kuali.ole.service;

import org.kuali.ole.deliver.api.OleDeliverRequestDefinition;
import org.kuali.ole.deliver.bo.OlePatronLoanDocuments;
import org.kuali.ole.deliver.api.OleEntityAddressDefinition;
import org.kuali.ole.deliver.api.OlePatronDefinition;
import org.kuali.ole.deliver.api.OlePatronNotesDefinition;
import org.kuali.ole.deliver.api.OlePatronQueryResults;
import org.kuali.rice.core.api.criteria.QueryByCriteria;
import org.kuali.rice.kim.api.identity.address.EntityAddress;
import org.kuali.rice.kim.api.identity.email.EntityEmail;
import org.kuali.rice.kim.api.identity.entity.Entity;
import org.kuali.rice.kim.api.identity.name.EntityName;
import org.kuali.rice.kim.api.identity.phone.EntityPhone;
import org.kuali.rice.kim.api.identity.type.EntityTypeContactInfo;

import javax.jws.WebService;
import javax.jws.soap.SOAPBinding;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: pvsubrah
 * Date: 5/9/12
 * Time: 2:52 PM
 * To change this template use File | Settings | File Templates.
 */
@WebService(name = "olePatronService", targetNamespace = "http://service.ole.kuali.org/")
@SOAPBinding(style = SOAPBinding.Style.DOCUMENT, use = SOAPBinding.Use.LITERAL, parameterStyle = SOAPBinding.ParameterStyle.WRAPPED)
public interface OlePatronService {

    public OlePatronDefinition getPatron(String patronId);

    public OlePatronDefinition createPatron(OlePatronDefinition olePatron);

    public OlePatronDefinition updatePatron(OlePatronDefinition olePatron);

    public OlePatronDefinition inactivatePatron(String patronId);

    public void addNameToEntity(EntityName name, Entity entity);

    public EntityName updateName(EntityName name);

    public boolean inactivateName(String nameId);

    public void addEmailToEntity(List<EntityEmail> emails, EntityTypeContactInfo entityTypeContactInfo);

    public boolean updateEmail(EntityEmail entityEmail);

    public boolean inactivateEmail(String emailId);

    public void addAddressToEntity(List<OleEntityAddressDefinition> oleEntityAddress, EntityTypeContactInfo entityTypeContactInfo);

    public boolean updateAddress(EntityAddress entityAddress);

    public boolean inactivateAddress(String addressId);

    public void addPhoneToEntity(List<EntityPhone> entityPhone, EntityTypeContactInfo entityTypeContactInfo);

    public boolean updatePhone(EntityPhone entityPhone);

    public boolean inactivatePhone(String phoneId);

    public boolean addNoteToPatron(OlePatronNotesDefinition patronNote);

    public boolean updateNote(OlePatronNotesDefinition patronNote);

    public boolean inactivateNote(String patronNoteId);

    public OlePatronQueryResults getPatrons();

    public OlePatronQueryResults findPatron(QueryByCriteria queryCriteria);

    public void deletePatronBatchProgram();
    // renewal by patron methods

    public OlePatronLoanDocuments getPatronLoanedItems(String patronBarcode);

    public List<OleDeliverRequestDefinition> getPatronRequestItems(String patronId);

    public OlePatronLoanDocuments performRenewalItems(OlePatronLoanDocuments olePatronLoanDocuments);

}
