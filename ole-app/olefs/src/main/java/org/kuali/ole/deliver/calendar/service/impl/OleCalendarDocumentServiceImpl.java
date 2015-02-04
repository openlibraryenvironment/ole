package org.kuali.ole.deliver.calendar.service.impl;

import org.kuali.ole.deliver.calendar.bo.*;
import org.kuali.ole.deliver.calendar.service.OleCalendarDocumentService;
import org.kuali.rice.krad.bo.PersistableBusinessObject;
import org.kuali.rice.krad.maintenance.MaintenanceDocument;
import org.kuali.rice.krad.service.DocumentService;
import org.kuali.rice.krad.service.KRADServiceLocator;
import org.kuali.rice.krad.service.impl.MaintenanceDocumentServiceImpl;
import org.kuali.rice.krad.uif.util.ObjectPropertyUtils;
import org.kuali.rice.krad.util.KRADConstants;
import org.kuali.rice.krad.util.ObjectUtils;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * OleCalendarDocumentServiceImpl generates maintenance object and perform copy operation.
 */
public class OleCalendarDocumentServiceImpl extends MaintenanceDocumentServiceImpl implements OleCalendarDocumentService {
    private static final org.apache.log4j.Logger LOG = org.apache.log4j.Logger.getLogger(OleCalendarDocumentServiceImpl.class);


    private DocumentService documentService;

    /**
     * Gets the value of documentService which is of type DocumentService
     *
     * @return documentService(DocumentService)
     */
    protected DocumentService getDocumentService() {
        return this.documentService;
    }

    /**
     * Sets the value for documentService which is of type DocumentService
     *
     * @param documentService(DocumentService)
     *
     */
    public void setDocumentService(DocumentService documentService) {
        this.documentService = documentService;
    }

    /**
     * This method will set the patron object from the request parameters
     *
     * @param document
     * @param maintenanceAction
     * @param requestParameters
     */
    @Override
    public void setupMaintenanceObject(MaintenanceDocument document, String maintenanceAction,
                                       Map<String, String[]> requestParameters) {
        LOG.debug("Inside setupMaintenanceObject method");
        document.getNewMaintainableObject().setMaintenanceAction(maintenanceAction);
        document.getOldMaintainableObject().setMaintenanceAction(maintenanceAction);

        // if action is edit or copy first need to retrieve the old record
        if (!KRADConstants.MAINTENANCE_NEW_ACTION.equals(maintenanceAction) &&
                !KRADConstants.MAINTENANCE_NEWWITHEXISTING_ACTION.equals(maintenanceAction)) {
            Object oldDataObject = retrieveObjectForMaintenance(document, requestParameters);

            // enhancement to indicate fields to/not to copy
            Object newDataObject = ObjectUtils.deepCopy((Serializable) oldDataObject);

            // process further object preparations for copy action
            if (KRADConstants.MAINTENANCE_COPY_ACTION.equals(maintenanceAction)) {
                processMaintenanceObjectForCopy(document, newDataObject, requestParameters);
                processCalendarDocumentForCopy(newDataObject);
            } else {
                checkMaintenanceActionAuthorization(document, oldDataObject, maintenanceAction, requestParameters);
            }

            // set object instance for editing
            document.getOldMaintainableObject().setDataObject(oldDataObject);
            document.getNewMaintainableObject().setDataObject(newDataObject);
        }

        if (KRADConstants.MAINTENANCE_NEWWITHEXISTING_ACTION.equals(maintenanceAction)) {
            Object newBO = document.getNewMaintainableObject().getDataObject();
            Map<String, String> parameters =
                    buildKeyMapFromRequest(requestParameters, document.getNewMaintainableObject().getDataObjectClass());
            ObjectPropertyUtils.copyPropertiesToObject(parameters, newBO);
            if (newBO instanceof PersistableBusinessObject) {
                ((PersistableBusinessObject) newBO).refresh();
            }

            document.getNewMaintainableObject().setupNewFromExisting(document, requestParameters);
        } else if (KRADConstants.MAINTENANCE_NEW_ACTION.equals(maintenanceAction)) {
            document.getNewMaintainableObject().processAfterNew(document, requestParameters);
        }
    }

    /**
     * @see org.kuali.rice.krad.service.impl.MaintenanceDocumentServiceImpl#setupMaintenanceObject
     */
    /**
     * This method creates maintenance object for delete operation using maintenanceAction.
     *
     * @param document
     * @param maintenanceAction
     * @param requestParameters
     */
    public void setupMaintenanceObjectForDelete(MaintenanceDocument document, String maintenanceAction,
                                                Map<String, String[]> requestParameters) {
        document.getNewMaintainableObject().setMaintenanceAction(maintenanceAction);
        document.getOldMaintainableObject().setMaintenanceAction(maintenanceAction);

        Object oldDataObject = retrieveObjectForMaintenance(document, requestParameters);
        Object newDataObject = ObjectUtils.deepCopy((Serializable) oldDataObject);

        document.getOldMaintainableObject().setDataObject(oldDataObject);
        document.getNewMaintainableObject().setDataObject(newDataObject);
    }

    /**
     * This method used to delete all the list of objects and save the latest collection in OleCalendar maintenance document.
     *
     * @param oleCalendar
     * @return
     */
    public OleCalendar getNewCalendar(OleCalendar oleCalendar) {
        if (oleCalendar.getCalendarId() != null && !oleCalendar.getCalendarId().isEmpty()) {
            HashMap<String, String> calendarMap = new HashMap<>();
            calendarMap.put("calendarId", oleCalendar.getCalendarId());
            OleCalendar tempCalendar = KRADServiceLocator.getBusinessObjectService().findByPrimaryKey(OleCalendar.class, calendarMap);
            if (tempCalendar != null) {
                if (tempCalendar.getOleCalendarExceptionPeriodList().size() > 0) {
                    for (OleCalendarExceptionPeriod oleCalendarExceptionPeriod : tempCalendar.getOleCalendarExceptionPeriodList()) {
                        if (oleCalendarExceptionPeriod.getOleCalendarExceptionPeriodWeekList().size() > 0) {
                            KRADServiceLocator.getBusinessObjectService().delete(tempCalendar.getOleCalendarExceptionPeriodList());
                        }
                    }
                    KRADServiceLocator.getBusinessObjectService().delete(tempCalendar.getOleCalendarExceptionPeriodList());
                }
                if (tempCalendar.getOleCalendarWeekList().size() > 0) {
                    KRADServiceLocator.getBusinessObjectService().delete(tempCalendar.getOleCalendarWeekList());
                }
                if (tempCalendar.getOleCalendarExceptionDateList().size() > 0) {
                    KRADServiceLocator.getBusinessObjectService().delete(tempCalendar.getOleCalendarExceptionDateList());
                }

            }
        }
        return oleCalendar;
    }

    /**
     * This method will remove the primary key and the object Id's of the calendar object and its child object which is used in copy functionality
     * @param maintenanceObject
     */
    protected void processCalendarDocumentForCopy(Object maintenanceObject) {
        LOG.debug("Inside processCalendarDocumentForCopy method");
        OleCalendar newCalendar = (OleCalendar) maintenanceObject;
        List<OleCalendarWeek> oleCalendarWeekList = newCalendar.getOleCalendarWeekList();
        if(oleCalendarWeekList.size() > 0) {
            for(OleCalendarWeek oleCalendarWeek : oleCalendarWeekList) {
                oleCalendarWeek.setCalendarWeekId(null);
                oleCalendarWeek.setCalendarId(null);
                oleCalendarWeek.setVersionNumber(null);
                oleCalendarWeek.setObjectId(null);
            }
        }
        List<OleCalendarExceptionPeriod> oleCalendarExceptionPeriodList = newCalendar.getOleCalendarExceptionPeriodList();
        if(oleCalendarExceptionPeriodList.size() > 0) {
            for(OleCalendarExceptionPeriod oleCalendarExceptionPeriod : oleCalendarExceptionPeriodList) {
                for(OleCalendarExceptionPeriodWeek oleCalendarExceptionPeriodWeek : oleCalendarExceptionPeriod.getOleCalendarExceptionPeriodWeekList()) {
                    {
                        oleCalendarExceptionPeriodWeek.setCalendarWeekId(null);
                        oleCalendarExceptionPeriodWeek.setCalendarExceptionPeriodId(null);
                        oleCalendarExceptionPeriodWeek.setVersionNumber(null);
                        oleCalendarExceptionPeriodWeek.setObjectId(null);
                    }
                oleCalendarExceptionPeriod.setCalendarExceptionPeriodId(null);
                oleCalendarExceptionPeriod.setCalendarId(null);
                oleCalendarExceptionPeriod.setVersionNumber(null);
                oleCalendarExceptionPeriod.setObjectId(null);
                }
            }
        }
        List<OleCalendarExceptionDate> oleCalendarExceptionDateList = newCalendar.getOleCalendarExceptionDateList();
        if(oleCalendarExceptionDateList.size() > 0) {
            for(OleCalendarExceptionDate oleCalendarExceptionDate : oleCalendarExceptionDateList) {
                oleCalendarExceptionDate.setCalendarExceptionDateId(null);
                oleCalendarExceptionDate.setCalendarId(null);
                oleCalendarExceptionDate.setVersionNumber(null);
                oleCalendarExceptionDate.setObjectId(null);
            }
        }

    }
}
