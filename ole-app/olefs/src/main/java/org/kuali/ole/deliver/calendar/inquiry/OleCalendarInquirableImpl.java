package org.kuali.ole.deliver.calendar.inquiry;

import org.kuali.ole.OLEConstants;
import org.kuali.ole.deliver.calendar.bo.*;
import org.kuali.ole.deliver.calendar.service.DateUtil;
import org.kuali.rice.krad.bo.BusinessObject;
import org.kuali.rice.krad.bo.ExternalizableBusinessObject;
import org.kuali.rice.krad.datadictionary.exception.UnknownBusinessClassAttributeException;
import org.kuali.rice.krad.inquiry.InquirableImpl;
import org.kuali.rice.krad.service.KRADServiceLocatorWeb;
import org.kuali.rice.krad.service.ModuleService;

import java.security.GeneralSecurityException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: dileepp
 * Date: 8/26/13
 * Time: 4:17 PM
 * To change this template use File | Settings | File Templates.
 */
public class OleCalendarInquirableImpl extends InquirableImpl {

    private static final org.apache.log4j.Logger LOG = org.apache.log4j.Logger.getLogger(OleCalendarInquirableImpl.class);

    public Object retrieveDataObject(Map<String, String> parameters) {
        if (dataObjectClass == null) {
            LOG.error("Data object class must be set in inquirable before retrieving the object");
            throw new RuntimeException("Data object class must be set in inquirable before retrieving the object");
        }

        // build list of key values from the map parameters
        List<String> pkPropertyNames = getDataObjectMetaDataService().listPrimaryKeyFieldNames(dataObjectClass);

        // some classes might have alternate keys defined for retrieving
        List<List<String>> alternateKeyNames = this.getAlternateKeysForClass(dataObjectClass);

        // add pk set as beginning so it will be checked first for match
        alternateKeyNames.add(0, pkPropertyNames);

        List<String> dataObjectKeySet = retrieveKeySetFromMap(alternateKeyNames, parameters);
        if ((dataObjectKeySet == null) || dataObjectKeySet.isEmpty()) {
            LOG.warn("Matching key set not found in request for class: " + getDataObjectClass());

            return null;
        }

        // found key set, now build map of key values pairs we can use to retrieve the object
        Map<String, Object> keyPropertyValues = new HashMap<String, Object>();
        for (String keyPropertyName : dataObjectKeySet) {
            String keyPropertyValue = parameters.get(keyPropertyName);

            // uppercase value if needed
            Boolean forceUppercase = Boolean.FALSE;
            try {
                forceUppercase = getDataDictionaryService().getAttributeForceUppercase(dataObjectClass,
                        keyPropertyName);
            } catch (UnknownBusinessClassAttributeException ex) {
                // swallowing exception because this check for ForceUppercase would
                // require a DD entry for the attribute, and we will just set force uppercase to false
                LOG.warn("Data object class "
                        + dataObjectClass
                        + " property "
                        + keyPropertyName
                        + " should probably have a DD definition.", ex);
            }

            if (forceUppercase.booleanValue() && (keyPropertyValue != null)) {
                keyPropertyValue = keyPropertyValue.toUpperCase();
            }

            // check security on key field
            if (getDataObjectAuthorizationService().attributeValueNeedsToBeEncryptedOnFormsAndLinks(dataObjectClass,
                    keyPropertyName)) {
                try {
                    keyPropertyValue = getEncryptionService().decrypt(keyPropertyValue);
                } catch (GeneralSecurityException e) {
                    LOG.error("Data object class "
                            + dataObjectClass
                            + " property "
                            + keyPropertyName
                            + " should have been encrypted, but there was a problem decrypting it.", e);
                    throw new RuntimeException("Data object class "
                            + dataObjectClass
                            + " property "
                            + keyPropertyName
                            + " should have been encrypted, but there was a problem decrypting it.", e);
                }
            }

            keyPropertyValues.put(keyPropertyName, keyPropertyValue);
        }

        // now retrieve the object based on the key set
        Object dataObject = null;

        ModuleService moduleService = KRADServiceLocatorWeb.getKualiModuleService().getResponsibleModuleService(
                getDataObjectClass());
        if (moduleService != null && moduleService.isExternalizable(getDataObjectClass())) {
            dataObject = moduleService.getExternalizableBusinessObject(getDataObjectClass().asSubclass(
                    ExternalizableBusinessObject.class), keyPropertyValues);
        } else if (BusinessObject.class.isAssignableFrom(getDataObjectClass())) {
            dataObject = getBusinessObjectService().findByPrimaryKey(getDataObjectClass().asSubclass(
                    BusinessObject.class), keyPropertyValues);
        }
        OleCalendar oleCalendar = new OleCalendar();
        try {
            oleCalendar = (OleCalendar) dataObject;
            DateUtil dateUtil = new DateUtil();
            // oleCalendar = (OleCalendar) document.getDocumentDataObject();//changed for edit purpose
            for (OleCalendarWeek oldCalendarWeek : oleCalendar.getOleCalendarWeekList()) {


                if (oldCalendarWeek.getOpenTime().equals("") || oldCalendarWeek.getCloseTime().equals("")) {
                    break;
                }
                String convertedOpenTime = "";
                String convertCloseTime = "";
                String oleOpenTime = oldCalendarWeek.getOpenTime();
                String oleCloseTime = oldCalendarWeek.getCloseTime();
                try {
                    convertedOpenTime = dateUtil.convertTo12HoursFormat(oleOpenTime);
                    convertCloseTime = dateUtil.convertTo12HoursFormat(oleCloseTime);
                } catch (java.text.ParseException e) {
                    LOG.error("Exception while converting to 12Hours format", e);  //To change body of catch statement use File | Settings | File Templates.
                }

                if (convertedOpenTime != null) {
                    oldCalendarWeek.setOpenTime(convertedOpenTime.substring(0, convertedOpenTime.length() - 2));
                }
                if (convertCloseTime != null) {
                    oldCalendarWeek.setCloseTime(convertCloseTime.substring(0, convertCloseTime.length() - 2));
                }
            }
            oleCalendar.sortCalendarWeek(oleCalendar); //added for OLE-5381
            for (OleCalendarExceptionPeriod oleCalendarExceptionPeriod : oleCalendar.getOleCalendarExceptionPeriodList()) {
                for (OleCalendarExceptionPeriodWeek oleCalendarExceptionPeriodWeek : oleCalendarExceptionPeriod.getOleCalendarExceptionPeriodWeekList()) {
                    String convOpenTimeExcptPrdWk = "";
                    String convCloseTimeExcptPrdWk = "";
                    String oleOpenTimeExcptPrdWk = oleCalendarExceptionPeriodWeek.getOpenTime();
                    String oleCloseTimeExcptPrdWk = oleCalendarExceptionPeriodWeek.getCloseTime();
                    try {
                        convOpenTimeExcptPrdWk = dateUtil.convertTo12HoursFormat(oleOpenTimeExcptPrdWk);
                        convCloseTimeExcptPrdWk = dateUtil.convertTo12HoursFormat(oleCloseTimeExcptPrdWk);
                    } catch (java.text.ParseException e) {
                        LOG.error("Exception while converting to 12Hours format", e);  //To change body of catch statement use File | Settings | File Templates.
                    }

                    if (convOpenTimeExcptPrdWk != null) {
                        oleCalendarExceptionPeriodWeek.setOpenTime(convOpenTimeExcptPrdWk.substring(0, convOpenTimeExcptPrdWk.length() - 2));
                    }
                    if (convCloseTimeExcptPrdWk != null) {
                        oleCalendarExceptionPeriodWeek.setCloseTime(convCloseTimeExcptPrdWk.substring(0, convCloseTimeExcptPrdWk.length() - 2));
                    }
                }
            }


            for (OleCalendarExceptionDate oleCalendarExceptionDate : oleCalendar.getOleCalendarExceptionDateList()) {
                String convertedOpenTimeExcptDate = "";
                String convertedCloseTimeExcptDate = "";
                String oleOpenTimeExcptDate = oleCalendarExceptionDate.getOpenTime();
                String oleCloseTimeExcptDate = oleCalendarExceptionDate.getCloseTime();
                if ((!oleCalendarExceptionDate.getOpenTime().equals("")) && (!oleCalendarExceptionDate.getCloseTime().equals(""))) { //changed
                    try {
                        convertedOpenTimeExcptDate = dateUtil.convertTo12HoursFormat(oleOpenTimeExcptDate);
                        convertedCloseTimeExcptDate = dateUtil.convertTo12HoursFormat(oleCloseTimeExcptDate);
                    } catch (java.text.ParseException e) {
                        LOG.error("Exception while converting to 12Hours format", e);  //To change body of catch statement use File | Settings | File Templates.
                    }

                    if (convertedOpenTimeExcptDate != null) {
                        oleCalendarExceptionDate.setOpenTime(convertedOpenTimeExcptDate.substring(0, convertedOpenTimeExcptDate.length() - 2));
                    }
                    if (convertedCloseTimeExcptDate != null) {
                        oleCalendarExceptionDate.setCloseTime(convertedCloseTimeExcptDate.substring(0, convertedCloseTimeExcptDate.length() - 2));
                    }
                }
            }
        } catch (Exception e) {
            LOG.error("Exception", e);
        }

        for (OleCalendarWeek oleCalendarWeek : oleCalendar.getOleCalendarWeekList()) {
            oleCalendarWeek.setStartDay(weekDayConversion(oleCalendarWeek.getStartDay()));
            oleCalendarWeek.setEndDay(weekDayConversion(oleCalendarWeek.getEndDay()));
        }


        for (OleCalendarExceptionPeriod oleCalendarExceptionPeriod : oleCalendar.getOleCalendarExceptionPeriodList()) {
            for (OleCalendarExceptionPeriodWeek oleCalendarExceptionPeriodWeek : oleCalendarExceptionPeriod.getOleCalendarExceptionPeriodWeekList()) {

                oleCalendarExceptionPeriodWeek.setStartDay(weekDayConversion(oleCalendarExceptionPeriodWeek.getStartDay()));
                oleCalendarExceptionPeriodWeek.setEndDay(weekDayConversion(oleCalendarExceptionPeriodWeek.getEndDay()));
            }

        }

        return oleCalendar;
    }

    private String weekDayConversion(String key) {
        HashMap<String, String> weekDays = new HashMap<String, String>();
        weekDays.put("0", OLEConstants.CALENDER_SUN);
        weekDays.put("1", OLEConstants.CALENDER_MON);
        weekDays.put("2", OLEConstants.CALENDER_TUE);
        weekDays.put("3", OLEConstants.CALENDER_WED);
        weekDays.put("4", OLEConstants.CALENDER_THU);
        weekDays.put("5", OLEConstants.CALENDER_FRI);
        weekDays.put("6", OLEConstants.CALENDER_SAT);

        return weekDays.get(key);

    }
}
