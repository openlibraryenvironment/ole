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
package org.kuali.rice.ksb.messaging.web;

import org.apache.struts.action.ActionForm;
import org.kuali.rice.core.api.config.CoreConfigHelper;
import org.kuali.rice.ksb.messaging.threadpool.KSBThreadPool;


/**
 * Struts ActionForm for the {@link ThreadPoolAction}.
 *
 * @author Kuali Rice Team (rice.collab@kuali.org)
 *
 */
public class ThreadPoolForm extends ActionForm {

    private static final long serialVersionUID = 6670668383823543732L;

    private String methodToCall;
    private KSBThreadPool threadPool;
    private Integer corePoolSize; //editable
    private Integer maximumPoolSize; //editable

    private boolean allServers;
    private Long timeIncrement;
    private Long maxRetryAttempts;

    public String getMethodToCall() {
        return this.methodToCall;
    }
    public void setMethodToCall(String methodToCall) {
        this.methodToCall = methodToCall;
    }
    public Integer getCorePoolSize() {
        return this.corePoolSize;
    }
    public void setCorePoolSize(Integer corePoolSize) {
        this.corePoolSize = corePoolSize;
    }
    public Integer getMaximumPoolSize() {
        return this.maximumPoolSize;
    }
    public void setMaximumPoolSize(Integer maximumPoolSize) {
        this.maximumPoolSize = maximumPoolSize;
    }
    public KSBThreadPool getThreadPool() {
        return this.threadPool;
    }
    public void setThreadPool(KSBThreadPool threadPool) {
        this.threadPool = threadPool;
    }
    public boolean isAllServers() {
        return this.allServers;
    }
    public void setAllServers(boolean allServers) {
        this.allServers = allServers;
    }
    public String getApplicationId() {
    	return CoreConfigHelper.getApplicationId();
    }
    public Long getMaxRetryAttempts() {
        return this.maxRetryAttempts;
    }
    public void setMaxRetryAttempts(Long maxRetryAttempts) {
        this.maxRetryAttempts = maxRetryAttempts;
    }
    public Long getTimeIncrement() {
        return this.timeIncrement;
    }
    public void setTimeIncrement(Long timeIncrement) {
        this.timeIncrement = timeIncrement;
    }

}
