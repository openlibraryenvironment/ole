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
package org.kuali.rice.krad.service.impl;

import org.apache.log4j.Logger;
import org.kuali.rice.krad.exception.ExceptionIncident;
import org.kuali.rice.krad.exception.KualiExceptionIncident;
import org.kuali.rice.krad.service.KualiExceptionIncidentService;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Modified this service so that it now extends the KualiFeedbackServiceImpl.
 * This has been done to allow user feedback and exception incidents to be
 * reported in the same way, but to potentially different email lists.  Part
 * of this refactor included moving the mailer and messageTemplate properties
 * and the emailReport and createMailMessage methods to the new parent class.
 * 
 * @author Kuali Rice Team (rice.collab@kuali.org)
 *
 */
public class KualiExceptionIncidentServiceImpl extends KualiFeedbackServiceImpl implements KualiExceptionIncidentService {
    private Logger LOG=Logger.getLogger(KualiExceptionIncidentServiceImpl.class);
    
    /**
     * An list to send incident emails to.
     */
    private String incidentMailingList;
    
    /**
     * This property must be defined in the base configuration file for specifying
     * the mailing list for the report to be sent.
     * <p>Example:
     * <code>
     * <param name="KualiReporterServiceImpl.REPORT_MAIL_LIST">a@y,b@z</param>
     * </code>
     */
    public static final String REPORT_MAIL_LIST=String.format("%s.REPORT_MAIL_LIST", KualiExceptionIncidentServiceImpl.class.getSimpleName());

    @Override
    protected String getToAddressesPropertyName() {
        return REPORT_MAIL_LIST;
    }

    /**
     * This overridden method send email to the specified list of addresses.
     * 
     * @see org.kuali.rice.krad.service.KualiExceptionIncidentService#report(org.kuali.rice.krad.exception.KualiExceptionIncident)
     */
    @Override
	public void report(KualiExceptionIncident exceptionIncident) throws Exception {
        if (LOG.isTraceEnabled()) {
            String lm=String.format("ENTRY %s",
                    (exceptionIncident==null)?"null":exceptionIncident.toString());
            LOG.trace(lm);
        }
        
        emailReport(
                exceptionIncident.getProperty(
                        KualiExceptionIncident.EXCEPTION_REPORT_SUBJECT),
                exceptionIncident.getProperty(
                        KualiExceptionIncident.EXCEPTION_REPORT_MESSAGE));
        
        if (LOG.isTraceEnabled()) {
            String lm=String.format("EXIT");
            LOG.trace(lm);
        }
        
    }

    /**
     * This method first separate a composite string of the format
     * "string token string".
     * <p>Example: 1,2,a,b where ',' is the token
     * 
     * @param s
     * @param token
     * @return
     */
    public List<String> split(String s, String token) {
        if (LOG.isTraceEnabled()) {
            String lm=String.format("ENTRY %s;%s", s, token);
            LOG.trace(lm);
        }
                
        String[] sarray=s.split(token);
        List<String> list=new ArrayList<String>();
        for (int i=0; i<sarray.length && sarray[i].length() > 0; i++) {
            list.add(sarray[i]);
        }
        
        if (LOG.isTraceEnabled()) {
            String lm=String.format("EXIT %s", list.toString());
            LOG.trace(lm);
        }
        
        return list;
    }

    /**
     * This overridden method create an instance of the KualiExceptionIncident.
     * 
     * @see org.kuali.rice.krad.service.KualiExceptionIncidentService#getExceptionIncident(
     * java.lang.Exception,java.util.Map)
     */
    @Override
	public KualiExceptionIncident getExceptionIncident(Exception exception,
            Map<String, String> properties) {
    	if ( exception == null ) {
    		return getExceptionIncident(properties);
    	}
        if (LOG.isTraceEnabled()) {
            String lm=String.format("ENTRY %s;%s", exception.getMessage(),
                    properties.toString());
            LOG.trace(lm);
        }
        
        KualiExceptionIncident ei=new ExceptionIncident(exception, properties);
        
        if (LOG.isTraceEnabled()) {
            String lm=String.format("EXIT %s", ei.toProperties().toString());
            LOG.trace(lm);
        }
                
        return ei;
    }

    /**
     * This overridden method create an instance of ExceptionIncident from list of
     * name-value pairs as exception incident information.
     * 
     * @see org.kuali.rice.krad.service.KualiExceptionIncidentService#getExceptionIncident(java.util.Map)
     */
    @Override
	public KualiExceptionIncident getExceptionIncident(Map<String, String> properties) {
        if (LOG.isTraceEnabled()) {
            String lm=String.format("ENTRY %s", properties.toString());
            LOG.trace(lm);
        }
        
        ExceptionIncident ei=new ExceptionIncident(properties);
                
        if (LOG.isTraceEnabled()) {
            String lm=String.format("EXIT %s", ei.toProperties().toString());
            LOG.trace(lm);
        }
                
        return ei;
    }
    
	/**
     * Returns the incident report mailing list.
	 * @return the incidentMailingList
	 */
	public String getIncidentMailingList() {
		return this.incidentMailingList;
	}

	/**
     * Sets the incident report mailing list.
	 * @param incidentMailingList the incidentMailingList to set
	 */
	public void setIncidentMailingList(String incidentMailingList) {
		this.incidentMailingList = incidentMailingList;
	}

}
