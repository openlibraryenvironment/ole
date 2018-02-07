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

import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.persistence.Version;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;
import org.kuali.rice.core.api.config.CoreConfigHelper;
import org.kuali.rice.core.api.util.RiceUtilities;
import org.kuali.rice.core.framework.persistence.jpa.OrmUtils;
import org.kuali.rice.ksb.api.bus.ServiceConfiguration;
import org.kuali.rice.ksb.api.messaging.AsynchronousCall;
import org.kuali.rice.ksb.service.KSBServiceLocator;
import org.kuali.rice.ksb.util.KSBConstants;

/**
 * A message which has been persisted to the data store.
 *
 * @author Kuali Rice Team (rice.collab@kuali.org)
 */
@Entity
@Table(name="KRSB_MSG_QUE_T")
//@Sequence(name="KRSB_MSG_QUE_S", property="routeQueueId")
@NamedQueries({
  @NamedQuery(name="PersistedMessage.FindAll", query="select pm from PersistedMessageBO pm"),
  @NamedQuery(name="PersistedMessage.FindByServiceName", query="select pm from PersistedMessage pm where pm.serviceName = :serviceName and pm.methodName = :methodName"),
  @NamedQuery(name="PersistedMessage.GetNextDocuments", query="select pm from PersistedMessage pm where pm.applicationId = :applicationId and pm.queueStatus <> :queueStatus and pm.ipNumber = :ipNumber order by queuePriority asc, routeQueueId asc, queueDate asc")
})
public class PersistedMessageBO implements PersistedMessage {

	private static final long serialVersionUID = -7047766894738304195L;

	@Id
	@GeneratedValue(generator="KRSB_MSG_QUE_S")
	@GenericGenerator(name="KRSB_MSG_QUE_S",strategy="org.hibernate.id.enhanced.SequenceStyleGenerator",parameters={
			@Parameter(name="sequence_name",value="KRSB_MSG_QUE_S"),
			@Parameter(name="value_column",value="id")
	})
	@Column(name="MSG_QUE_ID")
	private Long routeQueueId;
	@Column(name="PRIO")
	private Integer queuePriority;
	@Column(name="STAT_CD")
	private String queueStatus;
	@Column(name="DT")
	private Timestamp queueDate;
	@Column(name="EXP_DT")
	private Timestamp expirationDate;
	@Column(name="RTRY_CNT")
	private Integer retryCount;
	@Version
	@Column(name="VER_NBR")
	private Integer lockVerNbr;
    @Column(name="IP_NBR")
	private String ipNumber;
    @Column(name="SVC_NM")
	private String serviceName;
    @Column(name="APPL_ID")
	private String applicationId;
    @Column(name="SVC_MTHD_NM")
	private String methodName;
    @Transient
    private AsynchronousCall methodCall;
    @Transient
    private PersistedMessagePayload payload;
    @Column(name="APP_VAL_ONE")
	private String value1;
    @Column(name="APP_VAL_TWO")
	private String value2;
    
    public PersistedMessageBO() {
        // default constructor
    }

    public static PersistedMessageBO buildMessage(ServiceConfiguration serviceConfiguration, AsynchronousCall methodCall) {
        PersistedMessageBO message = new PersistedMessageBO();
        message.setPayload(new PersistedMessagePayload(methodCall, message));
        message.setIpNumber(RiceUtilities.getIpNumber());
        message.setServiceName(serviceConfiguration.getServiceName().toString());
        message.setQueueDate(new Timestamp(System.currentTimeMillis()));
        if (serviceConfiguration.getPriority() == null) {
            message.setQueuePriority(KSBConstants.ROUTE_QUEUE_DEFAULT_PRIORITY);
        } else {
            message.setQueuePriority(serviceConfiguration.getPriority());
        }
        message.setQueueStatus(KSBConstants.ROUTE_QUEUE_QUEUED);
        message.setRetryCount(0);
        if (serviceConfiguration.getMillisToLive() > 0) {
            message.setExpirationDate(new Timestamp(System.currentTimeMillis() + serviceConfiguration.getMillisToLive()));
        }
        message.setApplicationId(CoreConfigHelper.getApplicationId());
        message.setMethodName(methodCall.getMethodName());
        return message;
    }
    
    //@PrePersist
    public void beforeInsert(){
        OrmUtils.populateAutoIncValue(this, KSBServiceLocator.getMessageEntityManagerFactory().createEntityManager());
    }
    
	@Override
    public String getApplicationId() {
		return this.applicationId;
	}

	public void setApplicationId(String applicationId) {
		this.applicationId = applicationId;
	}

    @Override
    public String getIpNumber() {
        return this.ipNumber;
    }
    
    public void setIpNumber(String ipNumber) {
        this.ipNumber = ipNumber;
    }
    
	@Override
    public Timestamp getQueueDate() {
		return this.queueDate;
	}

	@Override
    public Integer getQueuePriority() {
		return this.queuePriority;
	}

	@Override
    public String getQueueStatus() {
		return this.queueStatus;
	}

	@Override
    public Integer getRetryCount() {
		return this.retryCount;
	}


	public void setQueueDate(Timestamp timestamp) {
	    this.queueDate = timestamp;
	}

	public void setQueuePriority(Integer integer) {
	    this.queuePriority = integer;
	}

	public void setQueueStatus(String string) {
	    this.queueStatus = string;
	}

	public void setRetryCount(Integer integer) {
	    this.retryCount = integer;
	}


    public Integer getLockVerNbr() {
        return this.lockVerNbr;
    }
    
    public void setLockVerNbr(Integer lockVerNbr) {
        this.lockVerNbr = lockVerNbr;
    }
    
    @Override
    public Long getRouteQueueId() {
        return this.routeQueueId;
    }
    
    public void setRouteQueueId(Long queueSequence) {
        this.routeQueueId = queueSequence;
    }
    
	@Override
    public String getServiceName() {
		return this.serviceName;
	}

	public void setServiceName(String serviceName) {
		this.serviceName = serviceName;
	}

    public String toString() {
	return "[RouteQueue: " + ", routeQueueId=" + this.routeQueueId + ", ipNumber=" + this.ipNumber 
		+ "applicationId=" + this.applicationId + ", serviceName=" + this.serviceName + ", methodName=" + methodName 
		+ ", queueStatus=" + this.queueStatus + ", queuePriority=" + this.queuePriority
		+ ", queueDate=" + this.queueDate + "]";
    }

	@Override
    public AsynchronousCall getMethodCall() {
		return this.methodCall;
	}

	public void setMethodCall(AsynchronousCall methodCall) {
		this.methodCall = methodCall;
	}

	@Override
    public String getMethodName() {
		return this.methodName;
	}

	public void setMethodName(String methodName) {
		this.methodName = methodName;
	}

	@Override
    public Timestamp getExpirationDate() {
		return this.expirationDate;
	}

	public void setExpirationDate(Timestamp expirationDate) {
		this.expirationDate = expirationDate;
	}

    @Override
    public PersistedMessagePayload getPayload() {
	if (this.payload == null) {
	    if (this.getRouteQueueId() == null) {
		return null;
}	    this.payload = KSBServiceLocator.getMessageQueueService().findByPersistedMessageByRouteQueueId(this.getRouteQueueId()); 
	}
        return this.payload;
    }

    public void setPayload(PersistedMessagePayload payload) {
        this.payload = payload;
    }

    @Override
    public String getValue1() {
        return this.value1;
    }

    public void setValue1(String value1) {
        this.value1 = value1;
    }

    @Override
    public String getValue2() {
        return this.value2;
    }

    public void setValue2(String value2) {
        this.value2 = value2;
    }

}
