package org.kuali.ole.deliver.rule;

import org.apache.commons.collections.CollectionUtils;
import org.apache.cxf.common.util.StringUtils;
import org.kuali.ole.OLEConstants;
import org.kuali.ole.deliver.bo.*;
import org.kuali.rice.core.api.util.RiceKeyConstants;
import org.kuali.rice.coreservice.api.CoreServiceApiServiceLocator;
import org.kuali.rice.coreservice.api.parameter.Parameter;
import org.kuali.rice.coreservice.api.parameter.ParameterKey;
import org.kuali.rice.kim.api.identity.IdentityService;
import org.kuali.rice.kim.api.identity.entity.EntityDefault;
import org.kuali.rice.kim.api.services.KimApiServiceLocator;
import org.kuali.rice.kim.impl.identity.address.EntityAddressBo;
import org.kuali.rice.kim.impl.identity.email.EntityEmailBo;
import org.kuali.rice.kim.impl.identity.name.EntityNameBo;
import org.kuali.rice.kim.impl.identity.phone.EntityPhoneBo;
import org.kuali.rice.kim.service.KIMServiceLocatorInternal;
import org.kuali.rice.kim.service.UiDocumentService;
import org.kuali.rice.krad.maintenance.MaintenanceDocument;
import org.kuali.rice.krad.rules.MaintenanceDocumentRuleBase;
import org.kuali.rice.krad.service.KRADServiceLocator;
import org.kuali.rice.krad.util.GlobalVariables;
import org.kuali.rice.krad.util.KRADConstants;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * OlePatronDocumentRule performs validation for OlePatronDocument..
 */
public class OlePatronDocumentRule extends MaintenanceDocumentRuleBase {

    protected UiDocumentService uiDocumentService;
    protected IdentityService identityService;

    /**
     * This method validates the patron object and returns boolean value
     *
     * @param document
     * @return isValid
     */

    protected boolean processCustomSaveDocumentBusinessRules(MaintenanceDocument document) {
        LOG.debug(" Inside processCustomSaveDocumentBusinessRule");
        boolean isValid = true;
        OlePatronDocument patronDocument = (OlePatronDocument) document.getNewMaintainableObject().getDataObject();
        isValid &= validateBorrowerType(patronDocument);
        isValid &= checkName(patronDocument);
        EntityDefault origEntity = null;
        if (patronDocument.getOlePatronId() != null) {
            origEntity = getIdentityService().getEntityDefault(patronDocument.getOlePatronId());
        }
        boolean isCreatingNew = origEntity == null ? true : false;

        isValid &= validateEntityInformation(isCreatingNew, patronDocument);
        isValid &= validateBarcode(patronDocument);
        isValid &= checkDuplicateBarcode(patronDocument);
        isValid &= validateAddress(patronDocument, "oleEntityAddressBo");
        isValid &= validateRequiredField(patronDocument);
        isValid &= validatePatronName(patronDocument);
        if (LOG.isDebugEnabled()) {
            LOG.debug(" DocumentBusinessRule for patron record is " + isValid);
        }
        return isValid;
    }

    /**
     * this method check the borrowerType property and returns boolean value
     *
     * @param patronDoc
     * @return valid
     */
    protected boolean validateBorrowerType(OlePatronDocument patronDoc) {
        boolean valid = true;
        if (StringUtils.isEmpty(patronDoc.getBorrowerType())) {
            this.putFieldError("dataObject.borrowerType", "error.field.required");
            valid = false;
        }
        return valid;
    }

    /**
     * this method validates the name object and returns boolean value
     *
     * @param patronDoc
     * @return valid
     */
    protected boolean checkName(OlePatronDocument patronDoc) {
        boolean valid = true;
        if (StringUtils.isEmpty(patronDoc.getName().getFirstName())) {
            this.putFieldError("dataObject.name.firstName", "error.field.required");
            valid = false;
        }
        if (StringUtils.isEmpty(patronDoc.getName().getLastName())) {
            this.putFieldError("dataObject.name.lastName", "error.field.required");
            valid = false;
        }

        return valid;
    }

    /**
     * This method validates the phone,address,and the email object in the patron document
     *
     * @param isCreatingNew
     * @param patronDoc
     * @return valid(boolean)
     */
    protected boolean validateEntityInformation(boolean isCreatingNew, OlePatronDocument patronDoc) {
        boolean valid = true;
        boolean canOverridePrivacyPreferences = getUIDocumentService().canOverrideEntityPrivacyPreferences(GlobalVariables.getUserSession().getPrincipalId(), null);
        /*if(isCreatingNew || canOverridePrivacyPreferences) {*/
        List<OleEntityAddressBo> addressBoList = patronDoc.getOleEntityAddressBo();
        List<OleEntityEmailBo> emailBoList = patronDoc.getOleEntityEmailBo();
        List<OleEntityPhoneBo> phoneBoList = patronDoc.getOleEntityPhoneBo();
        if (addressBoList.size() == 1) {
            OleEntityAddressBo oleEntityAddressBo = addressBoList.get(0);
            oleEntityAddressBo.getEntityAddressBo().setDefaultValue(true);
        }
        if (CollectionUtils.isNotEmpty(emailBoList) && emailBoList.size() == 1) {
            OleEntityEmailBo oleEntityEmailBo = emailBoList.get(0);
            oleEntityEmailBo.getEntityEmailBo().setDefaultValue(true);
        }
        if (CollectionUtils.isNotEmpty(phoneBoList) && phoneBoList.size() == 1) {
            OleEntityPhoneBo oleEntityPhoneBo = phoneBoList.get(0);
            oleEntityPhoneBo.getEntityPhoneBo().setDefaultValue(true);
        }
        if (!checkPhoneMultipleDefault(patronDoc.getOleEntityPhoneBo(), "oleEntityPhoneBo")) {
            GlobalVariables.getMessageMap().putErrorForSectionId(OLEConstants.OlePatron.PHONE_SECTION_ID, OLEConstants.OlePatron.ERROR_SELECTION_PREFERRED_PHONE);
            valid &= false;
        }
        if (!checkAddressMultipleDefault(patronDoc.getOleEntityAddressBo(), "oleEntityAddressBo")) {
            GlobalVariables.getMessageMap().putErrorForSectionId(OLEConstants.OlePatron.ADDRESS_SECTION_ID, OLEConstants.OlePatron.ERROR_SELECTION_PREFERRED_ADDRESS);
            valid &= false;
        }

        if (!checkAddressMultipleDeliverAddress(patronDoc.getOleEntityAddressBo(), "oleEntityAddressBo")) {
            // GlobalVariables.getMessageMap().putErrorForSectionId(OLEConstants.OlePatron.ADDRESS_SECTION_ID, OLEConstants.OlePatron.ERROR_SELECTION_PREFERRED_DELIVER_ADDRESS);
            valid &= false;
        }

        if (!checkEmailMultipleDefault(patronDoc.getOleEntityEmailBo(), "oleEntityEmailBo")) {
            GlobalVariables.getMessageMap().putErrorForSectionId(OLEConstants.OlePatron.EMAIL_SECTION_ID, OLEConstants.OlePatron.ERROR_SELECTION_PREFERRED_EMAIL);
            valid &= false;
        }
        return valid;
    }

    /**
     * This method validates the barcode number in the patron document
     *
     * @param patronDoc
     * @return valid(boolean)
     */
    protected boolean validateBarcode(OlePatronDocument patronDoc) {
        boolean valid = true;
        if (StringUtils.isEmpty(patronDoc.getBarcode())) {
            this.putFieldError("dataObject.barcode", "error.field.required");
            valid = false;
        }
        return valid;
    }

    /**
     * This method validates the duplicate barcode number in the patron document
     *
     * @param patronDoc
     * @return valid(boolean)
     */
    protected boolean checkDuplicateBarcode(OlePatronDocument patronDoc) {
        boolean valid = true;
        Map barcodeMap = new HashMap();
        barcodeMap.put(OLEConstants.OlePatron.BARCODE, patronDoc.getBarcode());
        List<OlePatronDocument> olePatronDocuments = (List<OlePatronDocument>) KRADServiceLocator.getBusinessObjectService().findMatching(OlePatronDocument.class, barcodeMap);
        if (olePatronDocuments.size() > 0) {
            for (OlePatronDocument olePatronDocument : olePatronDocuments) {
                if (patronDoc.getOlePatronId() == null || !(patronDoc.getOlePatronId().equals(olePatronDocument.getOlePatronId()))) {
                    this.putFieldError("dataObject.barcode", "error.barcode.duplicate");
                    valid = false;
                }
            }
        }
        return valid;
    }


    /**
     * Gets the value of uiDocumentService property
     *
     * @return uiDocumentService(UiDocumentService)
     */
    public UiDocumentService getUIDocumentService() {
        if (uiDocumentService == null) {
            uiDocumentService = KIMServiceLocatorInternal.getUiDocumentService();
        }
        return uiDocumentService;
    }

    /**
     * this method validates the phone object for default value
     *
     * @param phoneBoList
     * @param listName
     * @return valid
     */
    protected boolean checkPhoneMultipleDefault(List<OleEntityPhoneBo> phoneBoList, String listName) {
        boolean valid = true;
        boolean isDefaultSet = false;
        int i = 0;
        for (OleEntityPhoneBo phone : phoneBoList) {
            EntityPhoneBo entityPhoneBo = phone.getEntityPhoneBo();
            if (entityPhoneBo != null && entityPhoneBo.isDefaultValue()) {
                if (isDefaultSet) {
                    this.putFieldError("dataObject." + listName + "[" + i + "].defaultValue", RiceKeyConstants.ERROR_MULTIPLE_DEFAULT_SELETION);
                    valid = false;
                } else {
                    isDefaultSet = true;
                }
            }
            i++;
        }
        if (!phoneBoList.isEmpty() && !isDefaultSet) {
            //this.putFieldError("dataObject."+listName+"[0].defaultValue",RiceKeyConstants.ERROR_NO_DEFAULT_SELETION);
            valid = false;
        }
        return valid;
    }

    /**
     * this method validates the address object for default value
     *
     * @param addrBoList
     * @param listName
     * @return valid
     */
    protected boolean checkAddressMultipleDefault(List<OleEntityAddressBo> addrBoList, String listName) {
        boolean valid = true;
        boolean isDefaultSet = false;
        int i = 0;
        for (OleEntityAddressBo addr : addrBoList) {
            EntityAddressBo entityAddressBo = addr.getEntityAddressBo();
            if (entityAddressBo.isDefaultValue()) {
                if (isDefaultSet) {
                    this.putFieldError("dataObject." + listName + "[" + i + "].defaultValue", OLEConstants.OlePatron.ERROR_PATRON_MULIT_PREFERRED_ADDRESS);
                    valid = false;
                } else {
                    isDefaultSet = true;
                }
            }
            i++;
        }
        if (!addrBoList.isEmpty() && !isDefaultSet) {
            //this.putFieldError("dataObject."+listName+"[0].defaultValue",RiceKeyConstants.ERROR_NO_DEFAULT_SELETION);
            valid = false;
        }
        return valid;
    }


    protected boolean checkAddressMultipleDeliverAddress(List<OleEntityAddressBo> addrBoList, String listName) {
        boolean valid = true;
        boolean isDefaultSet = false;
        int i = 0;
        boolean isAtleastOneChecked=false;
        for (OleEntityAddressBo addr : addrBoList) {
            OleAddressBo oleAddressBo = addr.getOleAddressBo();
            if (oleAddressBo.isDeliverAddress()) {
                isAtleastOneChecked=true;
                if (isDefaultSet) {
                    this.putFieldError("dataObject." + listName + "[" + i + "].defaultValue", OLEConstants.OlePatron.ERROR_PATRON_MULIT_DELIVER_ADDRESS);
                    valid = false;
                } else {
                    isDefaultSet = true;
                }
            }
            i++;
        }
        if(!isAtleastOneChecked){
            valid=true;
        } else {
            if (!addrBoList.isEmpty() && !isDefaultSet) {
                //this.putFieldError("dataObject."+listName+"[0].defaultValue",RiceKeyConstants.ERROR_NO_DEFAULT_SELETION);
                valid = false;
            }
        }
        return valid;
    }
    /**
     * this method validates the email object for default value
     *
     * @param emailBoList
     * @param listName
     * @return valid
     */
    protected boolean checkEmailMultipleDefault(List<OleEntityEmailBo> emailBoList, String listName) {
        boolean valid = true;
        boolean isDefaultSet = false;
        int i = 0;
        for (OleEntityEmailBo email : emailBoList) {
            EntityEmailBo entityEmailBo = email.getEntityEmailBo();
            if (entityEmailBo != null && entityEmailBo.isDefaultValue()) {
                if (isDefaultSet) {
                    this.putFieldError("dataObject." + listName + "[" + i + "].defaultValue", OLEConstants.OlePatron.ERROR_PATRON_MULIT_PREFERRED_EMAIL);
                    valid = false;
                } else {
                    isDefaultSet = true;
                }
            }
            i++;
        }
        if (!emailBoList.isEmpty() && !isDefaultSet) {
            //this.putFieldError("dataObject."+listName+"[0].defaultValue",RiceKeyConstants.ERROR_NO_DEFAULT_SELETION);
            valid = false;
        }
        return valid;
    }

    /**
     * Gets the value of identityService property
     *
     * @return identityService(IdentityService)
     */
    public IdentityService getIdentityService() {
        if (identityService == null) {
            identityService = KimApiServiceLocator.getIdentityService();
        }
        return identityService;
    }


    public boolean validateAddress(OlePatronDocument olePatronDocument, String addressBos) {
        List<OleEntityAddressBo> addressBoList = olePatronDocument.getOleEntityAddressBo();
        OleEntityAddressBo oleEntityAddressBo;
        boolean valid = true;
        boolean flag=true;
        boolean dateExist = true;
        Map<Date,Date> map=new HashMap<>();
        for (int i = 0; i < addressBoList.size(); i++) {
            oleEntityAddressBo = addressBoList.get(i);
/*            if (oleEntityAddressBo.getEntityAddressBo().isDefaultValue()) {
                if (oleEntityAddressBo.getOleAddressBo().getAddressValidFrom() != null || oleEntityAddressBo.getOleAddressBo().getAddressValidTo() != null) {
                    GlobalVariables.getMessageMap().putError("dataObject." + addressBos + "[0].defaultValue", OLEConstants.OlePatron.ERROR_PATRON_ADDRESS_DEFAULT_DATE);
                    flag= false;
                }
            }*/
            if(oleEntityAddressBo.getOleAddressBo().getAddressValidFrom()!=null && oleEntityAddressBo.getOleAddressBo().getAddressValidTo()!=null && oleEntityAddressBo.getOleAddressBo().getAddressValidFrom().compareTo(oleEntityAddressBo.getOleAddressBo().getAddressValidTo())>0){
                GlobalVariables.getMessageMap().putError("dataObject." + addressBos + "[0].defaultValue", OLEConstants.OlePatron.ERROR_PATRON_VALID_ADDRESS_TO_DATE);
                flag= false;
            }

            if(!oleEntityAddressBo.getEntityAddressBo().isDefaultValue()){
                if(oleEntityAddressBo.getOleAddressBo().getAddressValidFrom()==null && oleEntityAddressBo.getOleAddressBo().getAddressValidTo()!=null){
                    GlobalVariables.getMessageMap().putError("dataObject." + addressBos + "[0].defaultValue", OLEConstants.OlePatron.ERROR_PATRON_REQUIRED_ADDRESS_FROM_DATE, "ValidFrom");
                    flag= false;
                }
                if(oleEntityAddressBo.getOleAddressBo().getAddressValidTo()==null && oleEntityAddressBo.getOleAddressBo().getAddressValidFrom()!=null){
                    GlobalVariables.getMessageMap().putError("dataObject." + addressBos + "[0].defaultValue", OLEConstants.OlePatron.ERROR_PATRON_REQUIRED_ADDRESS_FROM_DATE, "ValidTo");
                    flag= false;
                }
                for(Map.Entry<Date,Date> entry:map.entrySet()){
                    if(entry.getKey()!=null&&entry.getValue()!=null&&oleEntityAddressBo.getOleAddressBo().getAddressValidFrom()!=null&&oleEntityAddressBo.getOleAddressBo().getAddressValidTo()!=null){
                        if(oleEntityAddressBo.getOleAddressBo().getAddressValidFrom().compareTo(entry.getKey())>=0 && oleEntityAddressBo.getOleAddressBo().getAddressValidFrom().compareTo(entry.getValue())<=0
                                || oleEntityAddressBo.getOleAddressBo().getAddressValidTo().compareTo(entry.getKey())>=0 && oleEntityAddressBo.getOleAddressBo().getAddressValidTo().compareTo(entry.getValue())<=0){
                            GlobalVariables.getMessageMap().putError("dataObject." + addressBos + "[0].defaultValue",OLEConstants.OlePatron.ERROR_PATRON_ADDRESS_FROM_TO_DATE_OVERLAP);
                            flag= false;
                        }
                        if(entry.getKey().compareTo(oleEntityAddressBo.getOleAddressBo().getAddressValidFrom())>=0 && entry.getKey().compareTo(oleEntityAddressBo.getOleAddressBo().getAddressValidTo())<=0
                                || entry.getValue().compareTo(oleEntityAddressBo.getOleAddressBo().getAddressValidFrom())>=0 && entry.getValue().compareTo(oleEntityAddressBo.getOleAddressBo().getAddressValidTo())<=0){
                            GlobalVariables.getMessageMap().putError("dataObject." + addressBos + "[0].defaultValue", OLEConstants.OlePatron.ERROR_PATRON_ADDRESS_FROM_TO_DATE_OVERLAP);
                            flag= false;
                        }
                    }
                }
            }else if (oleEntityAddressBo.getOleAddressBo().getAddressValidFrom() != null || oleEntityAddressBo.getOleAddressBo().getAddressValidTo() != null) {

                if (dateExist) {
                    dateExist = false;
                } else {
                    GlobalVariables.getMessageMap().putError("dataObject." + addressBos + "[0].defaultValue", OLEConstants.OlePatron.ERROR_PATRON_ADDRESS_SINGLE_DATE);
                    flag= false;
                }
            }
            map.put(oleEntityAddressBo.getOleAddressBo().getAddressValidFrom(),oleEntityAddressBo.getOleAddressBo().getAddressValidTo());
        }
        if(!flag){
            return false;
        }
        return valid;
    }

    public boolean validateRequiredField(OlePatronDocument olePatronDocument) {
        boolean valid = true;
        List<OleEntityAddressBo> addressBoList = olePatronDocument.getOleEntityAddressBo();
        List<OleEntityEmailBo> entityEmailBos = olePatronDocument.getOleEntityEmailBo();
        if ((!(addressBoList.size() > 0)) && (!(entityEmailBos.size() > 0))) {
             GlobalVariables.getMessageMap().putError(KRADConstants.GLOBAL_ERRORS, OLEConstants.OlePatron.ERROR_PATRON_REQUIRED_ADDRESS);
            valid = false;
        }
       /* if ( affiliationBoList.size() == 0 ) {
            GlobalVariables.getMessageMap().putError(KRADConstants.GLOBAL_ERRORS,OLEConstants.OlePatron.ERROR_PATRON_REQUIRED_AFFILIATION);
            //this.putFieldError("dataObject.patronAffiliations",OLEConstants.OlePatron.ERROR_PATRON_REQUIRED_AFFILIATION);
            valid = false;
        }
        if ( employeeList.size() == 0 ) {
            GlobalVariables.getMessageMap().putError(KRADConstants.GLOBAL_ERRORS,OLEConstants.OlePatron.ERROR_PATRON_REQUIRED_EMPLOYEE);
            //this.putFieldError("dataObject.employments",OLEConstants.OlePatron.ERROR_PATRON_REQUIRED_AFFILIATION);
            valid = false;
        }*/

        return valid;
    }

    public boolean validatePatronName(OlePatronDocument olePatronDocument) {
        boolean valid = true;
        EntityNameBo names = olePatronDocument.getName();
        String firstName = names.getFirstName();
        String middleName = names.getMiddleName();
        String lastName = names.getLastName();
        String parameterResult = "";
        Pattern pattern;
        Matcher matcher;
        boolean matchName;
        if (firstName != null && !firstName.equals("")) {
            parameterResult = getParameter(OLEConstants.OlePatron.PATRON_NAME_VALIDATION);
            if (parameterResult != null) {
                pattern = Pattern.compile(parameterResult);
                matcher = pattern.matcher(firstName);
                if (matcher.find()) {
                    this.putFieldError(OLEConstants.OlePatron.FIRST_NAME, OLEConstants.OlePatron.ERROR_PATRON_FIRST_NAME);
                    valid = false;
                }
            }
        }
        if (middleName != null && !middleName.equals("")) {
            parameterResult = getParameter(OLEConstants.OlePatron.PATRON_NAME_VALIDATION);
            if (parameterResult != null) {
                pattern = Pattern.compile(parameterResult);
                matcher = pattern.matcher(middleName);
                if (matcher.find()) {
                    this.putFieldError(OLEConstants.OlePatron.MIDDLE_NAME, OLEConstants.OlePatron.ERROR_PATRON_MIDDLE_NAME);
                    valid = false;
                }
            }
        }
        if (lastName != null && !lastName.equals("")) {
            parameterResult = getParameter(OLEConstants.OlePatron.PATRON_NAME_VALIDATION);
            if (parameterResult != null) {
                pattern = Pattern.compile(parameterResult);
                matcher = pattern.matcher(lastName);
                if (matcher.find()) {
                    this.putFieldError(OLEConstants.OlePatron.LAST_NAME, OLEConstants.OlePatron.ERROR_PATRON_LAST_NAME);
                    valid = false;
                }
            }
        }
        return valid;
    }

    public String getParameter(String name) {
        ParameterKey parameterKey = ParameterKey.create(OLEConstants.APPL_ID, OLEConstants.DLVR_NMSPC, OLEConstants.DLVR_CMPNT, name);
        Parameter parameter = CoreServiceApiServiceLocator.getParameterRepositoryService().getParameter(parameterKey);
        return parameter != null ? parameter.getValue() : null;
    }
}