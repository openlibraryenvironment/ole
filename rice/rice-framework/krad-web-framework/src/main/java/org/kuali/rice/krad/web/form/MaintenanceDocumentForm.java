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
package org.kuali.rice.krad.web.form;

import org.kuali.rice.krad.maintenance.MaintenanceDocument;
import org.kuali.rice.krad.uif.UifConstants.ViewType;

/**
 * Form class for <code>MaintenanceDocumentView</code> screens
 *
 * @author Kuali Rice Team (rice.collab@kuali.org)
 */
public class MaintenanceDocumentForm extends DocumentFormBase {
	private static final long serialVersionUID = -5805825500852498048L;

	protected String dataObjectClassName;
	protected String maintenanceAction;

	public MaintenanceDocumentForm() {
		super();
		setViewTypeName(ViewType.MAINTENANCE);
	}

	@Override
	public MaintenanceDocument getDocument() {
		return (MaintenanceDocument) super.getDocument();
	}

	// This is to provide a setter with matching type to
	// public MaintenanceDocument getDocument() so that no
	// issues occur with spring 3.1-M2 bean wrappers
	public void setDocument(MaintenanceDocument document) {
	    super.setDocument(document);
	}

	public String getDataObjectClassName() {
		return this.dataObjectClassName;
	}

	public void setDataObjectClassName(String dataObjectClassName) {
		this.dataObjectClassName = dataObjectClassName;
	}

	public String getMaintenanceAction() {
		return this.maintenanceAction;
	}

	public void setMaintenanceAction(String maintenanceAction) {
		this.maintenanceAction = maintenanceAction;
	}

}
