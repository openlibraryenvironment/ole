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
package org.kuali.rice.krad.datadictionary.validation.processor;

import org.kuali.rice.core.api.CoreApiServiceLocator;
import org.kuali.rice.core.api.datetime.DateTimeService;
import org.kuali.rice.krad.datadictionary.validation.constraint.Constraint;
import org.kuali.rice.krad.service.DataDictionaryService;
import org.kuali.rice.krad.service.KRADServiceLocatorWeb;

/**
 * This abstract class can be extended by constraint processor classes that
 * must be processed on every validation.
 *
 * @author Kuali Rice Team (rice.collab@kuali.org)
 */
public abstract class MandatoryElementConstraintProcessor<C extends Constraint> implements ConstraintProcessor<Object, C> {

    protected DataDictionaryService dataDictionaryService;
    protected DateTimeService dateTimeService;

    /**
     * @see org.kuali.rice.krad.datadictionary.validation.processor.ConstraintProcessor#isOptional()
     */
    @Override
    public boolean isOptional() {
        return false;
    }

    /**
     * @return the dataDictionaryService
     */
    public DataDictionaryService getDataDictionaryService() {
        if (dataDictionaryService == null) {
            dataDictionaryService = KRADServiceLocatorWeb.getDataDictionaryService();
        }
        return this.dataDictionaryService;
    }

    /**
     * @param dataDictionaryService the dataDictionaryService to set
     */
    public void setDataDictionaryService(DataDictionaryService dataDictionaryService) {
        this.dataDictionaryService = dataDictionaryService;
    }

    /**
     * @return the dateTimeService
     */
    public DateTimeService getDateTimeService() {
        if (dateTimeService == null) {
            dateTimeService = CoreApiServiceLocator.getDateTimeService();
        }

        return this.dateTimeService;
    }

    /**
     * @param dateTimeService the dateTimeService to set
     */
    public void setDateTimeService(DateTimeService dateTimeService) {
        this.dateTimeService = dateTimeService;
    }

}
