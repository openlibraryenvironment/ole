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
package org.kuali.rice.krad.app.persistence.jpa;

import java.util.HashSet;
import java.util.Set;

/**
 * Exposes the names of all KNS business object entities - which are managed
 * by all JPA persistence units
 * 
 * @author Kuali Rice Team (rice.collab@kuali.org)
 */
public class KRADPersistableBusinessObjectClassExposer implements
        PersistableBusinessObjectClassExposer {

	/**
	 * Exposes all KNS objects - a hard coded list for now - to persistence units
	 * 
	 * @see PersistableBusinessObjectClassExposer#exposePersistableBusinessObjectClassNames()
	 */
	@Override
	public Set<String> exposePersistableBusinessObjectClassNames() {
		Set<String> knsBOs = new HashSet<String>();
		/*knsBOs.add(org.kuali.rice.kns.document.DocumentBase.class.getName());
		knsBOs.add(org.kuali.rice.kns.document.MaintenanceDocumentBase.class.getName());
		knsBOs.add(org.kuali.rice.kns.document.MaintenanceLock.class.getName());
		knsBOs.add(org.kuali.rice.kns.document.TransactionalDocumentBase.class.getName());
		knsBOs.add(org.kuali.rice.krad.bo.AdHocRoutePerson.class.getName());
		knsBOs.add(org.kuali.rice.krad.bo.AdHocRouteRecipient.class.getName());
		knsBOs.add(org.kuali.rice.kns.bo.AdHocRouteWorkgroup.class.getName());
		knsBOs.add(org.kuali.rice.krad.bo.Attachment.class.getName());
		//knsBOs.add(org.kuali.rice.kns.bo.CampusImpl.class.getName());
		//knsBOs.add(org.kuali.rice.kns.bo.CampusTypeImpl.class.getName());
		knsBOs.add(org.kuali.rice.kns.bo.DocumentAttachment.class.getName());
		knsBOs.add(org.kuali.rice.kns.bo.DocumentHeader.class.getName());
		knsBOs.add(org.kuali.rice.krad.bo.GlobalBusinessObjectDetailBase.class.getName());
		knsBOs.add(org.kuali.rice.kns.bo.LookupResults.class.getName());
		knsBOs.add(org.kuali.rice.krad.bo.KualiCodeBase.class.getName());
		knsBOs.add(org.kuali.rice.kns.lookup.MultipleValueLookupMetadata.class.getName());
		knsBOs.add(NamespaceBo.class.getName());
		knsBOs.add(org.kuali.rice.krad.bo.Note.class.getName());
		knsBOs.add(org.kuali.rice.krad.bo.NoteType.class.getName());
		knsBOs.add(ParameterBo.class.getName());
		knsBOs.add(ComponentBo.class.getName());
		knsBOs.add(ParameterTypeBo.class.getName());
		knsBOs.add(CampusBo.class.getName());
		knsBOs.add(CampusTypeBo.class.getName());
		knsBOs.add(org.kuali.rice.krad.bo.PersistableAttachmentBase.class.getName());
		knsBOs.add(org.kuali.rice.krad.bo.PersistableBusinessObjectBase.class.getName());
		knsBOs.add(org.kuali.rice.krad.bo.PersistableBusinessObjectExtensionBase.class.getName());
		knsBOs.add(org.kuali.rice.krad.document.authorization.PessimisticLock.class.getName());
		knsBOs.add(org.kuali.rice.kns.lookup.SelectedObjectIds.class.getName());
		knsBOs.add(org.kuali.rice.location.impl.country.CountryBo.class.getName());
		knsBOs.add(org.kuali.rice.kns.bo.CountyImpl.class.getName());
		knsBOs.add(org.kuali.rice.kns.bo.StateImpl.class.getName());
		knsBOs.add(org.kuali.rice.kns.bo.PostalCodeImpl.class.getName());
		knsBOs.add(org.kuali.rice.kns.bo.SessionDocument.class.getName()); */
		return knsBOs;
	}

}
