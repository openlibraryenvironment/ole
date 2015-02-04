/*
 * Copyright 2007 The Kuali Foundation
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
package org.kuali.ole.module.purap.batch;

import org.kuali.ole.module.purap.service.PdpExtractService;
import org.kuali.ole.sys.batch.AbstractStep;
import org.kuali.rice.core.api.datetime.DateTimeService;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

@Transactional
public class ExtractPdpImmediatesStep extends AbstractStep {
    private static org.apache.log4j.Logger LOG = org.apache.log4j.Logger.getLogger(ExtractPdpImmediatesStep.class);

    private PdpExtractService pdpExtractService;
    private DateTimeService dateTimeService;

    public ExtractPdpImmediatesStep() {
        super();
    }

    /**
     * @see org.kuali.ole.sys.batch.Step#execute(java.lang.String, java.util.Date)
     */
    @Override
    public boolean execute(String jobName, Date jobRunDate) throws InterruptedException {
        LOG.debug("execute() started");
        pdpExtractService.extractImmediatePaymentsOnly();
        return true;
    }

    public boolean execute() throws InterruptedException {
        try {
            return execute(null, dateTimeService.getCurrentDate());
        } catch (InterruptedException e) {
            LOG.error("Exception occured executing step", e);
            throw e;
        } catch (RuntimeException e) {
            LOG.error("Exception occured executing step", e);
            throw e;
        }
    }

    public void setPdpExtractService(PdpExtractService pdpExtractService) {
        this.pdpExtractService = pdpExtractService;
    }

    @Override
    public void setDateTimeService(DateTimeService dateTimeService) {
        this.dateTimeService = dateTimeService;
    }
}
