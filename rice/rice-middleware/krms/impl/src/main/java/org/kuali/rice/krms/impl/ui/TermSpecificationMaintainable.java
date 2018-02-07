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
package org.kuali.rice.krms.impl.ui;

import org.apache.commons.collections.CollectionUtils;
import org.kuali.rice.krad.maintenance.MaintenanceDocument;
import org.kuali.rice.krad.maintenance.MaintainableImpl;
import org.kuali.rice.krad.service.BusinessObjectService;
import org.kuali.rice.krad.service.KRADServiceLocator;
import org.kuali.rice.krad.util.KRADConstants;
import org.kuali.rice.krms.api.repository.context.ContextDefinition;
import org.kuali.rice.krms.impl.repository.ContextBo;
import org.kuali.rice.krms.impl.repository.ContextValidTermBo;
import org.kuali.rice.krms.impl.repository.KrmsRepositoryServiceLocator;
import org.kuali.rice.krms.impl.repository.TermSpecificationBo;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Map;

/**
 * {@link org.kuali.rice.krad.maintenance.Maintainable} for the {@link AgendaEditor}
 *
 * @author Kuali Rice Team (rice.collab@kuali.org)
 *
 */
public class TermSpecificationMaintainable extends MaintainableImpl {
	
	private static final long serialVersionUID = 1L;

    private static final org.apache.log4j.Logger LOG = org.apache.log4j.Logger.getLogger(TermSpecificationMaintainable.class);

	/**
	 * @return the boService
	 */
	public BusinessObjectService getBoService() {
		return KRADServiceLocator.getBusinessObjectService();
	}

    @Override
    public Object retrieveObjectForEditOrCopy(MaintenanceDocument document, Map<String, String> dataObjectKeys) {

        TermSpecificationBo termSpecificationBo = (TermSpecificationBo) super.retrieveObjectForEditOrCopy(document,
                dataObjectKeys);

        if (!CollectionUtils.isEmpty(termSpecificationBo.getContextIds())) {
            for (String contextId : termSpecificationBo.getContextIds()) {
                ContextDefinition context =
                        KrmsRepositoryServiceLocator.getContextBoService().getContextByContextId(contextId);
                if (context != null) {
                    termSpecificationBo.getContexts().add(ContextBo.from(context));
                }
            }
        }

        if (KRADConstants.MAINTENANCE_COPY_ACTION.equals(getMaintenanceAction())) {
            document.getDocumentHeader().setDocumentDescription("New Term Specification Document");
        }

        return termSpecificationBo;
    }

    /**
     * Add the Term Specification's Context to the given termSpecificationBo.  Note that there is no check for the Context
     * already having been added to the Term Specification.
     * @param termSpecificationBo with
     */
    private void findContexts(TermSpecificationBo termSpecificationBo) {
        Collection<ContextValidTermBo> validContextMappings =
            getBoService().findMatching(ContextValidTermBo.class,
                    Collections.singletonMap("termSpecificationId", termSpecificationBo.getId()));

        if (!CollectionUtils.isEmpty(validContextMappings)) for (ContextValidTermBo validContextMapping : validContextMappings) {
            termSpecificationBo.getContextIds().add(validContextMapping.getContextId());
        }
    }

    /**
	 * {@inheritDoc}
	 */
	@Override
	public void processAfterNew(MaintenanceDocument document,
		Map<String, String[]> requestParameters) {

		super.processAfterNew(document, requestParameters);
        document.getDocumentHeader().setDocumentDescription("New Term Specification Document");

	}

    /**
     * {@inheritDoc}
     */
    @Override
    public void processAfterEdit(MaintenanceDocument document, Map<String, String[]> requestParameters) {
        super.processAfterEdit(document, requestParameters);
        copyContextsOldToNewBo(document);
        document.getDocumentHeader().setDocumentDescription("Edited Term Specification Document");
    }

    @Override
    public void processAfterCopy(MaintenanceDocument document, Map<String, String[]> requestParameters) {
        super.processAfterCopy(document, requestParameters);
        copyContextsOldToNewBo(document);
    }

    /**
     * Copy the contexts from the old TermSpecificationBo to the newTermSpecificationBo of the maintenance document.
     * <p>
     * Since the contexts is a transient field it doesn't get copied by the deepCopy in
     * MaintenanceDocumentServiceImpl.setupMaintenanceObject, we manually need to copy the values over.
     * For performance reasons a shallow copy is done since the ContextBo themselves are never changed.
     * </p>
     * @param document that contains the old and new TermSpecificationBos
     */
    private void copyContextsOldToNewBo(MaintenanceDocument document) {
        TermSpecificationBo oldTermSpec = (TermSpecificationBo) document.getOldMaintainableObject().getDataObject();
        TermSpecificationBo newTermSpec = (TermSpecificationBo) document.getNewMaintainableObject().getDataObject();
        newTermSpec.setContexts(new ArrayList<ContextBo>());
        for (ContextBo contextBo : oldTermSpec.getContexts()) {
            newTermSpec.getContexts().add(contextBo);
        }
    }

    /**
     * Store contexts in contextIds (needed for serialization)
     *
     * @see org.kuali.rice.krad.maintenance.Maintainable#prepareForSave
     */
    @Override
    public void prepareForSave() {
        super.prepareForSave();

        TermSpecificationBo termSpec = (TermSpecificationBo) getDataObject();
        termSpec.setContextIds(new ArrayList<String>());
        for (ContextBo context : termSpec.getContexts()) {
            termSpec.getContextIds().add(context.getId());
        }
    }

    @Override
    public void saveDataObject() {
        TermSpecificationBo termSpec = (TermSpecificationBo) getDataObject();

        super.saveDataObject();    // save it, it should get an id assigned

        if (termSpec.getId() != null) {
            // clear all context valid term mappings
            getBoService().deleteMatching(ContextValidTermBo.class,
                    Collections.singletonMap("termSpecificationId", termSpec.getId()));

            // add a new mapping for each context in the collection
            for (String contextId : termSpec.getContextIds()) {
                ContextValidTermBo contextValidTerm = new ContextValidTermBo();
                contextValidTerm.setContextId(contextId);
                contextValidTerm.setTermSpecificationId(termSpec.getId());
                getBoService().save(contextValidTerm);
            }
        }
    }

    @Override
    public Class getDataObjectClass() {
        return TermSpecificationBo.class;
    }

    /**
     * Recreate the contexts from the contextIDs (needed due to serialization)
     *
     * @see org.kuali.rice.krad.maintenance.Maintainable#processAfterRetrieve
     */
    @Override
    public void processAfterRetrieve() {
        super.processAfterRetrieve();

        TermSpecificationBo termSpec = (TermSpecificationBo) getDataObject();
        termSpec.setContexts(new ArrayList<ContextBo>());
        for (String contextId : termSpec.getContextIds()) {
            ContextDefinition context = KrmsRepositoryServiceLocator.getContextBoService().getContextByContextId(contextId);
            termSpec.getContexts().add(ContextBo.from(context));
        }
    }
}