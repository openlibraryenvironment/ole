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
package org.kuali.rice.ksb.messaging;

import org.kuali.rice.core.api.util.io.SerializationUtils;
import org.kuali.rice.ksb.api.messaging.AsynchronousCall;

import javax.persistence.*;
import java.io.Serializable;

/**
 * Holds message payload content.  Needed to proxy the content so we don't have to 
 * take the hit when grabbing large amounts of persisted messages at time.
 * 
 * @author Kuali Rice Team (rice.collab@kuali.org)
 *
 */
@Entity
@Table(name="KRSB_MSG_PYLD_T")
public class PersistedMessagePayload implements Serializable {
    
    private static final long serialVersionUID = 508778527504899029L;
    
    @Id
	@Column(name="MSG_QUE_ID")
	private Long routeQueueId;
    @Lob
	@Basic(fetch=FetchType.LAZY)
	@Column(name="MSG_PYLD", length=4000)
	private String payload;
    @Transient
    private AsynchronousCall methodCall;
    @Transient
    private PersistedMessageBO message;
    
    public PersistedMessagePayload() {}
    
    public PersistedMessagePayload (AsynchronousCall methodCall, PersistedMessageBO message) {
	this.setPayload(SerializationUtils.serializeToBase64(methodCall));
	this.methodCall = methodCall;
	this.message = message;
    }
    
    public String getPayload() {
        return this.payload;
    }
    public void setPayload(String payload) {
        this.payload = payload;
    }
    public Long getRouteQueueId() {
        return this.routeQueueId;
    }
    public void setRouteQueueId(Long routeQueueId) {
        this.routeQueueId = routeQueueId;
    }
    public AsynchronousCall getMethodCall() {
	if (this.methodCall != null) {
	    return this.methodCall;
	} 
	this.methodCall = (AsynchronousCall) SerializationUtils.deserializeFromBase64(getPayload());
	return this.methodCall;
    }

    public PersistedMessageBO getMessage() {
        return this.message;
    }

    public void setMessage(PersistedMessageBO message) {
        this.message = message;
    }

    public void setMethodCall(AsynchronousCall methodCall) {
        this.methodCall = methodCall;
    }
    

}

