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

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

import javax.xml.namespace.QName;

import org.apache.ojb.broker.PersistenceBroker;
import org.apache.ojb.broker.query.Criteria;
import org.apache.ojb.broker.query.QueryByCriteria;
import org.kuali.rice.ksb.api.registry.ServiceEndpointStatus;
import org.springmodules.orm.ojb.PersistenceBrokerCallback;
import org.springmodules.orm.ojb.support.PersistenceBrokerDaoSupport;

/**
 * An OJB dao implementation of the {@link ServiceRegistryDao}. 
 * 
 * @author Kuali Rice Team (rice.collab@kuali.org)
 *
 */
public class ServiceRegistryDaoOjb extends PersistenceBrokerDaoSupport implements ServiceRegistryDao {

	@Override
	public ServiceInfoBo getServiceInfo(String serviceId) {
		Criteria crit = new Criteria();
		crit.addEqualTo("serviceId", serviceId);
		return (ServiceInfoBo)getPersistenceBrokerTemplate().getObjectByQuery(new QueryByCriteria(ServiceInfoBo.class, crit));
	}

	@Override
	public List<ServiceInfoBo> getOnlineServiceInfosByName(QName serviceName) {
		Criteria crit = new Criteria();
		crit.addEqualTo("serviceName", serviceName.toString());
		crit.addEqualTo("statusCode", ServiceEndpointStatus.ONLINE.getCode());
		return (List<ServiceInfoBo>)getPersistenceBrokerTemplate().getCollectionByQuery(new QueryByCriteria(ServiceInfoBo.class, crit));
	}

	@Override
	public List<ServiceInfoBo> getAllOnlineServiceInfos() {
		Criteria crit = new Criteria();
		crit.addEqualTo("statusCode", ServiceEndpointStatus.ONLINE.getCode());
		return (List<ServiceInfoBo>)getPersistenceBrokerTemplate().getCollectionByQuery(new QueryByCriteria(ServiceInfoBo.class, crit));
	}
	
	@Override
	public List<ServiceInfoBo> getAllServiceInfos() {
		Criteria crit = new Criteria();
		return (List<ServiceInfoBo>)getPersistenceBrokerTemplate().getCollectionByQuery(new QueryByCriteria(ServiceInfoBo.class, crit));
	}

	@Override
	public List<ServiceInfoBo> getAllServiceInfosForInstance(String instanceId) {
		Criteria crit = new Criteria();
		crit.addEqualTo("instanceId", instanceId);
		return (List<ServiceInfoBo>)getPersistenceBrokerTemplate().getCollectionByQuery(new QueryByCriteria(ServiceInfoBo.class, crit));
	}

    @Override
    public List<ServiceInfoBo> getAllServiceInfosForApplication(String applicationId) {
        Criteria crit = new Criteria();
        crit.addEqualTo("applicationId", applicationId);
        return (List<ServiceInfoBo>)getPersistenceBrokerTemplate().getCollectionByQuery(new QueryByCriteria(ServiceInfoBo.class, crit));
    }
	
	@Override
	public ServiceDescriptorBo getServiceDescriptor(String serviceDescriptorId) {
		Criteria crit = new Criteria();
		crit.addEqualTo("id", serviceDescriptorId);
		return (ServiceDescriptorBo)getPersistenceBrokerTemplate().getObjectByQuery(new QueryByCriteria(ServiceDescriptorBo.class, crit));
	}

	@Override
	public ServiceDescriptorBo saveServiceDescriptor(ServiceDescriptorBo serviceDescriptor) {
		getPersistenceBrokerTemplate().store(serviceDescriptor);
		return serviceDescriptor;
	}

	@Override
	public ServiceInfoBo saveServiceInfo(ServiceInfoBo serviceInfo) {
		getPersistenceBrokerTemplate().store(serviceInfo);
		return serviceInfo;
	}

	@Override
	public void removeServiceInfo(String serviceId) {
		Criteria crit = new Criteria();
		crit.addEqualTo("serviceId", serviceId);
		getPersistenceBrokerTemplate().deleteByQuery(new QueryByCriteria(ServiceInfoBo.class, crit));
	}

	@Override
	public void removeServiceDescriptor(String serviceDescriptorId) {
		Criteria crit = new Criteria();
		crit.addEqualTo("id", serviceDescriptorId);
		getPersistenceBrokerTemplate().deleteByQuery(new QueryByCriteria(ServiceDescriptorBo.class, crit));
	}

	@Override
	public boolean updateStatus(String serviceId, String statusCode) {
		ServiceInfoBo serviceInfoBo = getServiceInfo(serviceId);
		if (serviceInfoBo == null) {
			return false;
		}
		serviceInfoBo.setStatusCode(statusCode);
		saveServiceInfo(serviceInfoBo);
		return true;
	}

	private static final String UPDATE_STATUS_FOR_INSTANCE_ID = "update KRSB_SVC_DEF_T set STAT_CD=? where INSTN_ID=?";
	@Override
	public void updateStatusForInstanceId(final String instanceId, final String statusCode) {
		getPersistenceBrokerTemplate().execute(new PersistenceBrokerCallback() {
			@Override
			public Object doInPersistenceBroker(PersistenceBroker broker) {
				PreparedStatement preparedStatement = null;
				try {
					Connection connection = broker.serviceConnectionManager().getConnection();
					preparedStatement = connection.prepareStatement(UPDATE_STATUS_FOR_INSTANCE_ID);
					preparedStatement.setString(1, statusCode);
					preparedStatement.setString(2, instanceId);
					preparedStatement.executeUpdate();
				} catch (Exception e) {
					throw new RuntimeException("Failed to update status for instance id", e);
				} finally {
					if (preparedStatement != null) {
						try {
							preparedStatement.close();
						} catch (SQLException e) {}
					}
				}
				return null;
			}
		});
	}

}
