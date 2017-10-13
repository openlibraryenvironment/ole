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
package org.kuali.rice.kew.doctype.bo;

import org.kuali.rice.krad.bo.ExternalizableBusinessObject;

/**
 * This is a description of what this class does - Garey don't forget to fill this in.
 *
 * @author Kuali Rice Team (rice.collab@kuali.org)
 *
 */
public interface DocumentTypeEBO extends ExternalizableBusinessObject{

	public String getDocTypeParentId();

	public String getDescription();

	/**
	 * This method gets the help definition url from this object and resolves any
	 * potential variables that may be in use
	 */
	public String getHelpDefinitionUrl();

	public String getLabel();

	public String getName();

	public String getDocumentTypeId();

	/**
	 * Returns the application id for this DocumentType which can be specified on the document type itself,
	 * inherited from the parent, or defaults to the configured application id of the application.
	 *
	 * chb:12Nov2008: seems like the accessor should return the field and the auxiliary method "getActualFoo" should
	 * be the one to do more elaborate checking
	 */
	public String getApplicationId();

	/**
	 * In order to make this object Inactivateable. Not sure if I
	 * should remove the getActive method.
	 *
	 * @see org.kuali.rice.core.api.mo.common.active.MutableInactivatable#isActive()
	 */
	public boolean isActive();

}
