/**
 * Copyright 2005-2014 The Kuali Foundation
 *
 * Licensed under the Educational Community License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.opensource.org/licenses/ecl2.php
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package edu.sampleu.kew.krad.form;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.kuali.rice.kew.stats.Stats;
import org.kuali.rice.kew.stats.web.StatsAction;
import org.kuali.rice.kew.api.KewApiConstants;
import org.kuali.rice.krad.util.GlobalVariables;
import org.kuali.rice.krad.web.form.UifFormBase;

/**
 * A Struts ActionForm for the {@link StatsAction}.
 * 
 * @see StatsAction
 * 
 * @author Kuali Rice Team (rice.collab@kuali.org)
 */
public class StatsForm extends UifFormBase {

    private static final long serialVersionUID = 4587377779133823858L;
    private static final org.apache.log4j.Logger LOG = org.apache.log4j.Logger.getLogger(StatsForm.class);
    private static final String BEGIN_DATE = "begDate";
    private static final String END_DATE = "endDate";

    public static final String DAY_TIME_UNIT = "DDD";
    public static final String WEEK_TIME_UNIT = "WW";
    public static final String MONTH_TIME_UNIT = "MM";
    public static final String YEAR_TIME_UNIT = "YYYY";

    public static final String DEFAULT_BEGIN_DATE = "01/01/1900";
    public static final String DEFAULT_END_DATE = "01/01/2400";
    public static final String BEG_DAY_TIME = " 00:00";
    public static final String END_DAY_TIME = " 23:59";
    public static final String DATE_FORMAT = "MM/dd/yyyy";
    public static final String TIME_FORMAT = " HH:mm";

    private Stats stats;
    private String methodToCall = "";
    private String avgActionsPerTimeUnit = DAY_TIME_UNIT;

    private String begDate;
    private String endDate;

    private Date beginningDate;
    private Date endingDate;

    // KULRICE-3137: Added a backLocation parameter similar to the one from lookups.
    private String backLocation;

    public StatsForm() {
        stats = new Stats();
    }

    /**
     * Retrieves the "returnLocation" parameter after calling "populate" on the superclass.
     * 
     * @see org.kuali.rice.krad.web.struts.form.KualiForm#populate(javax.servlet.http.HttpServletRequest)
     */
    //	@Override
    //	public void populate(HttpServletRequest request) {
    //		super.populate(request);
    //		
    //        if (getParameter(request, KRADConstants.RETURN_LOCATION_PARAMETER) != null) {
    //            setBackLocation(getParameter(request, KRADConstants.RETURN_LOCATION_PARAMETER));
    //        }
    //	}

    public void determineBeginDate() {
        SimpleDateFormat dateFormat = new SimpleDateFormat(DATE_FORMAT + TIME_FORMAT);

        beginningDate = null;
        try {
            if (getBegDate() == null || getBegDate().trim().equals("")) {
                beginningDate = dateFormat.parse(DEFAULT_BEGIN_DATE + BEG_DAY_TIME);
            } else {
                beginningDate = dateFormat.parse(getBegDate() + BEG_DAY_TIME);
            }

            dateFormat = new SimpleDateFormat(DATE_FORMAT);
            begDate = dateFormat.format(beginningDate);
        } catch (ParseException e) {
            //parse error caught in validate methods
        } finally {
            if (beginningDate == null) {
                try {
                    beginningDate = dateFormat.parse(DEFAULT_BEGIN_DATE + BEG_DAY_TIME);
                } catch (ParseException e) {
                    throw new RuntimeException("Default Begin Date format incorrect");
                }
            }
        }
    }

    public void determineEndDate() {
        SimpleDateFormat dateFormat = new SimpleDateFormat(DATE_FORMAT + TIME_FORMAT);

        endingDate = null;
        try {
            if (getEndDate() == null || getEndDate().trim().equals("")) {
                endingDate = dateFormat.parse(DEFAULT_END_DATE + END_DAY_TIME);
            } else {
                endingDate = dateFormat.parse(getEndDate() + END_DAY_TIME);
            }

            dateFormat = new SimpleDateFormat(DATE_FORMAT);
            endDate = dateFormat.format(endingDate);
        } catch (ParseException e) {
            //parse error caught in validate methods
        } finally {
            if (endingDate == null) {
                try {
                    endingDate = dateFormat.parse(DEFAULT_END_DATE + END_DAY_TIME);
                } catch (ParseException e) {
                    throw new RuntimeException("Default End Date format incorrect");
                }
            }
        }
    }

    public Map makePerUnitOfTimeDropDownMap() {

        Map dropDownMap = new HashMap();
        dropDownMap.put(DAY_TIME_UNIT, KewApiConstants.DAILY_UNIT);
        dropDownMap.put(WEEK_TIME_UNIT, KewApiConstants.WEEKLY_UNIT);
        dropDownMap.put(MONTH_TIME_UNIT, KewApiConstants.MONTHLY_UNIT);
        dropDownMap.put(YEAR_TIME_UNIT, KewApiConstants.YEARLY_UNIT);
        return dropDownMap;

    }

    public void validateDates() {
        LOG.debug("validate()");

        //this.validateDate(BEGIN_DATE, this.getBegDate(), "general.error.fieldinvalid");
        //this.validateDate(END_DATE, this.getEndDate(), "general.error.fieldinvalid");
        if (getBegDate() != null && getBegDate().length() != 0) {
            try {
                new SimpleDateFormat(DATE_FORMAT + TIME_FORMAT).parse(getBegDate().trim() + END_DAY_TIME);
            } catch (ParseException e) {
                GlobalVariables.getMessageMap().putError(BEGIN_DATE, "general.error.fieldinvalid", "Begin Date");
            }
        }
        if (getEndDate() != null && getEndDate().length() != 0) {
            try {
                new SimpleDateFormat(DATE_FORMAT + TIME_FORMAT).parse(getEndDate().trim() + END_DAY_TIME);
            } catch (ParseException e) {
                GlobalVariables.getMessageMap().putError(END_DATE, "general.error.fieldinvalid", "End Date");
            }
        }
    }

    public Stats getStats() {
        return stats;
    }

    public void setStats(Stats stats) {
        this.stats = stats;
    }

//    public String getApprovedLabel() {
//        return KewApiConstants.ROUTE_HEADER_APPROVED_LABEL;
//    }

    public String getCanceledLabel() {
        return KewApiConstants.ROUTE_HEADER_CANCEL_LABEL;
    }

    public String getDisapprovedLabel() {
        return KewApiConstants.ROUTE_HEADER_DISAPPROVED_LABEL;
    }

    public String getEnrouteLabel() {
        return KewApiConstants.ROUTE_HEADER_ENROUTE_LABEL;
    }

    public String getExceptionLabel() {
        return KewApiConstants.ROUTE_HEADER_EXCEPTION_LABEL;
    }

    public String getFinalLabel() {
        return KewApiConstants.ROUTE_HEADER_FINAL_LABEL;
    }

    public String getInitiatedLabel() {
        return KewApiConstants.ROUTE_HEADER_INITIATED_LABEL;
    }

    public String getProcessedLabel() {
        return KewApiConstants.ROUTE_HEADER_PROCESSED_LABEL;
    }

    public String getSavedLabel() {
        return KewApiConstants.ROUTE_HEADER_SAVED_LABEL;
    }

    public String getAvgActionsPerTimeUnit() {
        return avgActionsPerTimeUnit;
    }

    public void setAvgActionsPerTimeUnit(String string) {
        avgActionsPerTimeUnit = string;
    }

    public String getBegDate() {
        return begDate;
    }

    public void setBegDate(String begDate) {
        this.begDate = begDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    public String getMethodToCall() {
        return methodToCall;
    }

    public void setMethodToCall(String methodToCall) {
        this.methodToCall = methodToCall;
    }

    public Date getBeginningDate() {
        return beginningDate;
    }

    public void setBeginningDate(Date beginningDate) {
        this.beginningDate = beginningDate;
    }

    public Date getEndingDate() {
        return endingDate;
    }

    public void setEndingDate(Date endingDate) {
        this.endingDate = endingDate;
    }

    public String getDayTimeUnit() {
        return DAY_TIME_UNIT;
    }

    public String getMonthTimeUnit() {
        return MONTH_TIME_UNIT;
    }

    public String getWeekTimeUnit() {
        return WEEK_TIME_UNIT;
    }

    public String getYearTimeUnit() {
        return YEAR_TIME_UNIT;
    }

    public String getBackLocation() {
        return this.backLocation;
    }

    public void setBackLocation(String backLocation) {
        this.backLocation = backLocation;
    }

}
