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
package org.kuali.rice.ksb.messaging.bam.dao.impl;

import org.apache.ojb.broker.query.Criteria;
import org.apache.ojb.broker.query.QueryByCriteria;
import org.kuali.rice.core.api.reflect.ObjectDefinition;
import org.kuali.rice.ksb.messaging.bam.BAMParam;
import org.kuali.rice.ksb.messaging.bam.BAMTargetEntry;
import org.kuali.rice.ksb.messaging.bam.dao.BAMDAO;
import org.springmodules.orm.ojb.support.PersistenceBrokerDaoSupport;

import javax.xml.namespace.QName;
import java.util.List;


public class BAMDAOOjbImpl extends PersistenceBrokerDaoSupport implements BAMDAO {

	public void clearBAMTables() {
		getPersistenceBrokerTemplate().deleteByQuery(new QueryByCriteria(BAMTargetEntry.class));
		getPersistenceBrokerTemplate().deleteByQuery(new QueryByCriteria(BAMParam.class));
	}

	@SuppressWarnings("unchecked")
	public List<BAMTargetEntry> getCallsForService(QName serviceName, String methodName) {
		Criteria crit = new Criteria();
		crit.addEqualTo("serviceName", serviceName.toString());
		crit.addEqualTo("methodName", methodName);
		return (List<BAMTargetEntry>)getPersistenceBrokerTemplate().getCollectionByQuery(new QueryByCriteria(BAMTargetEntry.class, crit));
	}

	public void save(BAMTargetEntry bamEntry) {
		this.getPersistenceBrokerTemplate().store(bamEntry);
	}

	@SuppressWarnings("unchecked")
	public List<BAMTargetEntry> getCallsForService(QName serviceName) {
		Criteria crit = new Criteria();
		crit.addEqualTo("serviceName", serviceName.toString());
		return (List<BAMTargetEntry>)getPersistenceBrokerTemplate().getCollectionByQuery(new QueryByCriteria(BAMTargetEntry.class, crit));
	}

	@SuppressWarnings("unchecked")
	public List<BAMTargetEntry> getCallsForRemotedClasses(ObjectDefinition objDef) {
		Criteria crit = new Criteria();
		QName qname = new QName(objDef.getApplicationId(), objDef.getClassName());
		crit.addLike("serviceName", qname.toString() + "%");
		return (List<BAMTargetEntry>)getPersistenceBrokerTemplate().getCollectionByQuery(new QueryByCriteria(BAMTargetEntry.class, crit));
	}

	@SuppressWarnings("unchecked")
	public List<BAMTargetEntry> getCallsForRemotedClasses(ObjectDefinition objDef, String methodName) {
		Criteria crit = new Criteria();
		QName qname = new QName(objDef.getApplicationId(), objDef.getClassName());
		crit.addLike("serviceName", qname.toString() + "%");
		crit.addEqualTo("methodName", methodName);
		return (List<BAMTargetEntry>)getPersistenceBrokerTemplate().getCollectionByQuery(new QueryByCriteria(BAMTargetEntry.class, crit));
	}
}
