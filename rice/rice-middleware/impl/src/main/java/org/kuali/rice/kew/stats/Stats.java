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
package org.kuali.rice.kew.stats;

import java.util.List;

/**
 * A snapshot of statistics for the KEW application at a particular
 * point in time.
 *
 * @author Kuali Rice Team (rice.collab@kuali.org)
 */
public class Stats {

	//set defaults
	String numUsers = "0";
	String numActionItems = "0";
	String numDocTypes = "0";
	String canceledNumber = "0";
	String disapprovedNumber = "0";
	String enrouteNumber = "0";
	String exceptionNumber = "0";
	String finalNumber = "0";
	String initiatedNumber = "0";
	String processedNumber = "0";
	String savedNumber = "0";    
    List numInitiatedDocsByDocType;
     
    public String getCanceledNumber() {
        return canceledNumber;
    }

    public void setCanceledNumber(String canceledNumber) {
        this.canceledNumber = canceledNumber;
    }

    public String getDisapprovedNumber() {
        return disapprovedNumber;
    }

    public void setDisapprovedNumber(String disapprovedNumber) {
        this.disapprovedNumber = disapprovedNumber;
    }
 
    public String getEnrouteNumber() {
        return enrouteNumber;
    }
 
    public void setEnrouteNumber(String enrouteNumber) {
        this.enrouteNumber = enrouteNumber;
    }

    public String getExceptionNumber() {
        return exceptionNumber;
    }

    public void setExceptionNumber(String exceptionNumber) {
        this.exceptionNumber = exceptionNumber;
    }

    public String getFinalNumber() {
        return finalNumber;
    }
 
    public void setFinalNumber(String finalNumber) {
        this.finalNumber = finalNumber;
    }

    public String getInitiatedNumber() {
        return initiatedNumber;
    }
 
    public void setInitiatedNumber(String initiatedNumber) {
        this.initiatedNumber = initiatedNumber;
    }

    public String getNumActionItems() {
        return numActionItems;
    }
 
    public void setNumActionItems(String numActionItems) {
        this.numActionItems = numActionItems;
    }

    public String getNumDocTypes() {
        return numDocTypes;
    }
 
    public void setNumDocTypes(String numDocTypes) {
        this.numDocTypes = numDocTypes;
    }

    public String getNumUsers() {
        return numUsers;
    }

    public void setNumUsers(String numUsers) {
        this.numUsers = numUsers;
    }

    public String getProcessedNumber() {
        return processedNumber;
    }
 
    public void setProcessedNumber(String processedNumber) {
        this.processedNumber = processedNumber;
    }
 
    public String getSavedNumber() {
        return savedNumber;
    }

    public void setSavedNumber(String savedNumber) {
        this.savedNumber = savedNumber;
    }

    public List getNumInitiatedDocsByDocType() {
        return numInitiatedDocsByDocType;
    }

    public void setNumInitiatedDocsByDocType(List numInitiatedDocsByDocType) {
        this.numInitiatedDocsByDocType = numInitiatedDocsByDocType;
    }


}
