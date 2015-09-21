package org.kuali.ole.olekrad.dao.impl;

import org.kuali.rice.krad.bo.PersistableBusinessObject;
import org.kuali.rice.krad.dao.impl.BusinessObjectDaoOjb;
import org.kuali.rice.krad.service.PersistenceStructureService;
import org.kuali.rice.krad.util.ObjectUtils;
import org.springframework.dao.DataAccessException;

import java.util.Iterator;
import java.util.List;
import java.util.Set;

/**
 * Created by sheiksalahudeenm on 9/5/2015.
 */
public class OleBusinessObjectDaoOjb extends BusinessObjectDaoOjb {

    private static final org.apache.log4j.Logger LOG = org.apache.log4j.Logger.getLogger(BusinessObjectDaoOjb.class);

    public OleBusinessObjectDaoOjb(PersistenceStructureService persistenceStructureService) {
        super(persistenceStructureService);
    }

    @Override
    public List<? extends PersistableBusinessObject> save(List businessObjects) throws DataAccessException {
        if ( LOG.isDebugEnabled() ) {
            LOG.debug( "About to persist the following BOs:" );
            for ( Object bo : businessObjects ) {
                LOG.debug( "   --->" + bo );
            }
        }
        for (Iterator i = businessObjects.iterator(); i.hasNext();) {
            PersistableBusinessObject bo = (PersistableBusinessObject) i.next();
            // refresh bo to get db copy of collections
            Set<String> boCollections = getPersistenceStructureService().listCollectionObjectTypes(bo.getClass()).keySet();
            PersistableBusinessObject savedBo = null;
            if (!boCollections.isEmpty()) {
                // refresh bo to get db copy of collections
                savedBo = (PersistableBusinessObject) ObjectUtils.deepCopy(bo);
                for (String boCollection : boCollections) {
                    if (getPersistenceStructureService().isCollectionUpdatable(savedBo.getClass(), boCollection)) {
                        savedBo.refreshReferenceObject(boCollection);
                    }
                }
                getOjbCollectionHelper().processCollections(this, bo, savedBo);
            }
            getPersistenceBrokerTemplate().store(bo);
        }
        return businessObjects;
    }
}
