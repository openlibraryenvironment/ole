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

import org.kuali.rice.core.api.reflect.ObjectDefinition;
import org.kuali.rice.ksb.messaging.bam.BAMTargetEntry;
import org.kuali.rice.ksb.messaging.bam.dao.BAMDAO;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.xml.namespace.QName;
import java.util.List;


public class BAMDaoJpaImpl implements BAMDAO {

    private EntityManager entityManager;

    @PersistenceContext
    public void setEntityManager(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    public void clearBAMTables() {
        entityManager.createQuery("delete from BAMTargetEntry").executeUpdate();
        entityManager.createQuery("delete from BAMParam").executeUpdate();        
    }

    public List<BAMTargetEntry> getCallsForService(QName serviceName, String methodName) {       
        return (List<BAMTargetEntry>) entityManager.createQuery("select bte from BAMTargetEntry bte where bte.serviceName = :serviceName and bte.methodName = :methodName").setParameter("serviceName", serviceName.toString()).setParameter("methodName", methodName).getResultList();
    }

    public void save(BAMTargetEntry bamEntry) {
        if(bamEntry.getBamId() == null) {
            entityManager.persist(bamEntry);
        }
        else {
            entityManager.merge(bamEntry);
        }
    }

    public List<BAMTargetEntry> getCallsForService(QName serviceName) {
        return (List<BAMTargetEntry>) entityManager.createQuery("select bte from BAMTargetEntry bte where bte.serviceName = :serviceName").setParameter("serviceName", serviceName.toString()).getResultList();
    }

    public List<BAMTargetEntry> getCallsForRemotedClasses(ObjectDefinition objDef) {
        QName qname = new QName(objDef.getApplicationId(), objDef.getClassName());
        return (List<BAMTargetEntry>) entityManager.createQuery("select bte from BAMTargetEntry bte where bte.serviceName like :serviceName%").setParameter("serviceName", qname.toString()).getResultList();
    }

    public List<BAMTargetEntry> getCallsForRemotedClasses(ObjectDefinition objDef, String methodName) {
        QName qname = new QName(objDef.getApplicationId(), objDef.getClassName());
        return (List<BAMTargetEntry>) entityManager.createQuery("select bte from BAMTargetEntry bte where bte.serviceName like :serviceName% and bte.methodName = :methodName").setParameter("serviceName", qname.toString()).setParameter("methodName", methodName).getResultList();
    }

}
