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
package org.kuali.rice.kim.document;

import org.apache.log4j.Logger;
import org.kuali.rice.kim.api.KimConstants;
import org.kuali.rice.kim.api.services.KimApiServiceLocator;
import org.kuali.rice.kim.impl.permission.GenericPermissionBo;
import org.kuali.rice.kim.impl.permission.PermissionBo;
import org.kuali.rice.kim.impl.permission.PermissionTemplateBo;
import org.kuali.rice.kns.document.MaintenanceDocument;
import org.kuali.rice.kns.maintenance.KualiMaintainableImpl;
import org.kuali.rice.krad.bo.BusinessObject;
import org.kuali.rice.krad.bo.PersistableBusinessObject;
import org.kuali.rice.krad.service.KRADServiceLocator;
import org.kuali.rice.krad.service.SequenceAccessorService;

import java.util.Map;

/**
 * This is a description of what this class does - jonathan don't forget to fill this in. 
 * 
 * @author Kuali Rice Team (rice.collab@kuali.org)
 *
 */
public class GenericPermissionMaintainable extends KualiMaintainableImpl {

	private static final Logger LOG = Logger.getLogger( GenericPermissionMaintainable.class );	
	private static final long serialVersionUID = -8102504656976243468L;
    protected transient SequenceAccessorService sequenceAccessorService;

    /**
     * Saves the responsibility via the responsibility update service
     *
     * @see org.kuali.rice.kns.maintenance.KualiMaintainableImpl#saveBusinessObject()
     */
    @Override
    public void saveDataObject() {
        if (getDataObject() instanceof PersistableBusinessObject) {
            GenericPermissionBo genericPermissionBo = (GenericPermissionBo)getDataObject();
            boolean permissionExists = false;
            if (genericPermissionBo.getId() != null) {
                permissionExists = KimApiServiceLocator.getPermissionService().getPermission(genericPermissionBo.getId()) != null;
            }

            if (genericPermissionBo.getTemplateId() != null) {
                genericPermissionBo.setTemplate(
                        PermissionTemplateBo.from(
                                KimApiServiceLocator.getPermissionService().getPermissionTemplate(genericPermissionBo.getTemplateId())));
            }
            PermissionBo perm = GenericPermissionBo.toPermissionBo(genericPermissionBo);
            if (permissionExists) {
                KimApiServiceLocator.getPermissionService().updatePermission(PermissionBo.to(perm));
            } else {
                KimApiServiceLocator.getPermissionService().createPermission(PermissionBo.to(perm));
            }
        } else {
            throw new RuntimeException(
                    "Cannot save object of type: " + getDataObjectClass() + " with permission service");
        }
    }
	
    /**
     * Pre-populates the ID field of the new PermissionBo to be created.
     *
     * @see org.kuali.rice.kns.maintenance.KualiMaintainableImpl#saveBusinessObject()
     */
    @Override
    public void processAfterCopy(MaintenanceDocument document, Map<String, String[]> parameters) {
        super.processAfterCopy(document,parameters);
        // get id for new permission
        String newId = getSequenceAccessorService()
                .getNextAvailableSequenceNumber(KimConstants.SequenceNames.KRIM_PERM_ID_S, PermissionBo.class).toString();
        ((GenericPermissionBo)document.getNewMaintainableObject().getDataObject()).setId(newId);
    }

	/**
	 * This overridden method ...
	 * 
	 * @see org.kuali.rice.kns.maintenance.KualiMaintainableImpl#getBoClass()
	 */
	@Override
	public Class<? extends PersistableBusinessObject> getBoClass() {
		return GenericPermissionBo.class;
	}
	
	/**
	 * This overridden method ...
	 * 
	 * @see org.kuali.rice.kns.maintenance.KualiMaintainableImpl#isExternalBusinessObject()
	 */
	@Override
	public boolean isExternalBusinessObject() {
		return true;
	}
	
	/**
	 * This overridden method ...
	 * 
	 * @see org.kuali.rice.kns.maintenance.KualiMaintainableImpl#prepareBusinessObject(org.kuali.rice.krad.bo.BusinessObject)
	 */
	@Override
	public void prepareBusinessObject(BusinessObject businessObject) {
		try {
			if ( businessObject == null ) {
				throw new RuntimeException( "Configuration ERROR: GenericPermissionMaintainable.prepareBusinessObject passed a null object." );
			}
			if ( businessObject instanceof PermissionBo ) {
				PermissionBo perm = getBusinessObjectService().findBySinglePrimaryKey(PermissionBo.class, ((PermissionBo)businessObject).getId() );
				businessObject = new GenericPermissionBo(perm);
			} else if ( businessObject instanceof GenericPermissionBo ) {
				// lookup the PermissionBo and convert to a GenericPermissionBo
				PermissionBo perm = getBusinessObjectService().findBySinglePrimaryKey(PermissionBo.class, ((GenericPermissionBo)businessObject).getId() );
				((GenericPermissionBo)businessObject).loadFromPermission(perm);
			} else {
				throw new RuntimeException( "Configuration ERROR: GenericPermissionMaintainable passed an unsupported object type: " + businessObject.getClass() );
			}
			if ( businessObject instanceof PersistableBusinessObject ) {
				setBusinessObject( (PersistableBusinessObject)businessObject );
			}
			super.prepareBusinessObject(businessObject);
		} catch ( RuntimeException ex ) {
			LOG.error( "Exception in prepareBusinessObject()", ex );
			throw ex;
		}
	}

    protected SequenceAccessorService getSequenceAccessorService(){
        if(this.sequenceAccessorService==null){
            this.sequenceAccessorService = KRADServiceLocator.getSequenceAccessorService();
        }
        return this.sequenceAccessorService;
    }

}
