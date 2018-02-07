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
package org.kuali.rice.ksb.impl.registry;

import org.kuali.rice.ksb.api.registry.ServiceEndpointStatus;
import org.kuali.rice.ksb.api.registry.ServiceInfo;
import org.kuali.rice.ksb.api.registry.ServiceInfoContract;

import javax.xml.namespace.QName;
import java.io.Serializable;

/**
 * Model bean that represents the definition of a service on the bus.
 * 
 * @see ServiceInfo
 *
 * @author Kuali Rice Team (rice.collab@kuali.org)
 */
//@Entity
//@Table(name="KRSB_SVC_DEF_T")
//@NamedQueries([
//	@NamedQuery(name="ServiceInfo.FetchAll", query="select s from ServiceInfo s"),
//	@NamedQuery(name="ServiceInfo.FetchAllActive",query="select s from ServiceInfo s where s.alive = true"),
//	@NamedQuery(name="ServiceInfo.FetchActiveByName",query="select s from ServiceInfo s where s.alive = true AND s.serviceName LIKE :serviceName"),
//	@NamedQuery(name="ServiceInfo.FindLocallyPublishedServices",query="select s from ServiceInfo s where s.serverIp = :serverIp AND s.applicationId = :applicationId"),
//	@NamedQuery(name="ServiceInfo.DeleteLocallyPublishedServices",query="delete from ServiceInfo s WHERE s.serverIp = :serverIp AND s.applicationId = :applicationId"),
//	@NamedQuery(name="ServiceInfo.DeleteByEntry",query="delete from ServiceInfo s where s.messageEntryId = :messageEntryId")			
//])
public class ServiceInfoBo implements ServiceInfoContract, Serializable {
 
	private static final long serialVersionUID = -4244884858494208070L;

	// TODO for some reason groovy won't compile this so commenting out for now...
//	@Id
//	@GeneratedValue(generator="KRSB_SVC_DEF_S")
//	@GenericGenerator(name="KRSB_SVC_DEF_S",strategy="org.hibernate.id.enhanced.SequenceStyleGenerator",parameters=[
//			@Parameter(name="sequence_name",value="KRSB_SVC_DEF_S"),
//			@Parameter(name="value_column",value="id")
//	])
	@javax.persistence.Column(name="SVC_DEF_ID")
	private String serviceId;
	
	@javax.persistence.Column(name="SVC_NM")
	private String serviceName;
    
	@javax.persistence.Column(name="SVC_URL", length=500)
	private String endpointUrl;
	
	@javax.persistence.Column(name="INSTN_ID")
	private String instanceId;

	@javax.persistence.Column(name="APPL_ID")
	private String applicationId;

	@javax.persistence.Column(name="SRVR_IP")
	private String serverIpAddress;
	
	@javax.persistence.Column(name="TYP_CD")
	private String type;
	
	@javax.persistence.Column(name="SVC_VER")
	private String serviceVersion;
	
	@javax.persistence.Column(name="STAT_CD")
	private String statusCode;
	
	@javax.persistence.Column(name="SVC_DSCRPTR_ID")
	private String serviceDescriptorId;
	
	@javax.persistence.Column(name="CHKSM", length=30)
	private String checksum;
	
	@javax.persistence.Version
	@javax.persistence.Column(name="VER_NBR")
	private Long versionNumber;

    public String getServiceId() {
        return serviceId;
    }

    public void setServiceId(String serviceId) {
        this.serviceId = serviceId;
    }

    @Override
    public QName getServiceName() {
        if (this.serviceName == null) {
            return null;
        }
        return QName.valueOf(this.serviceName);
    }

    public void setServiceName(String serviceName) {
        this.serviceName = serviceName;
    }

    public String getEndpointUrl() {
        return endpointUrl;
    }

    public void setEndpointUrl(String endpointUrl) {
        this.endpointUrl = endpointUrl;
    }

    public String getInstanceId() {
        return instanceId;
    }

    public void setInstanceId(String instanceId) {
        this.instanceId = instanceId;
    }

    public String getApplicationId() {
        return applicationId;
    }

    public void setApplicationId(String applicationId) {
        this.applicationId = applicationId;
    }

    public String getServerIpAddress() {
        return serverIpAddress;
    }

    public void setServerIpAddress(String serverIpAddress) {
        this.serverIpAddress = serverIpAddress;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getServiceVersion() {
        return serviceVersion;
    }

    public void setServiceVersion(String serviceVersion) {
        this.serviceVersion = serviceVersion;
    }

    public String getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(String statusCode) {
        this.statusCode = statusCode;
    }

    public String getServiceDescriptorId() {
        return serviceDescriptorId;
    }

    public void setServiceDescriptorId(String serviceDescriptorId) {
        this.serviceDescriptorId = serviceDescriptorId;
    }

    public String getChecksum() {
        return checksum;
    }

    public void setChecksum(String checksum) {
        this.checksum = checksum;
    }

    public Long getVersionNumber() {
        return versionNumber;
    }

    public void setVersionNumber(Long versionNumber) {
        this.versionNumber = versionNumber;
    }

    @Override
	public ServiceEndpointStatus getStatus() {
		if (getStatusCode() == null) {
			return null;
		}
		return ServiceEndpointStatus.fromCode(getStatusCode());
	}

	static ServiceInfo to(ServiceInfoBo bo) {
		if (bo == null) {
			return null;
		}
		return ServiceInfo.Builder.create(bo).build();
	}
	
	static ServiceInfoBo from(ServiceInfo im) {
		if (im == null) {
			return null;
		}
		ServiceInfoBo bo = new ServiceInfoBo();
		bo.serviceId = im.getServiceId();
		bo.serviceName = im.getServiceName().toString();
		bo.endpointUrl = im.getEndpointUrl();
		bo.instanceId = im.getInstanceId();
		bo.applicationId = im.getApplicationId();
		bo.serverIpAddress = im.getServerIpAddress();
		bo.type = im.getType();
		bo.serviceVersion = im.getServiceVersion();
		bo.statusCode = im.getStatus().getCode();
		bo.serviceDescriptorId = im.getServiceDescriptorId();
		bo.checksum = im.getChecksum();
		bo.versionNumber = im.getVersionNumber();
		return bo;
	}
	
	
	
	
}
