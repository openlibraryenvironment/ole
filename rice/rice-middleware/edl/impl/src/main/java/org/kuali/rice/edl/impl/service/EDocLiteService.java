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
package org.kuali.rice.edl.impl.service;

import java.io.InputStream;
import java.util.List;

import javax.xml.transform.Templates;
import javax.xml.transform.TransformerConfigurationException;

import org.kuali.rice.core.framework.impex.xml.XmlExporter;
import org.kuali.rice.core.framework.impex.xml.XmlLoader;
import org.kuali.rice.edl.impl.EDLController;
import org.kuali.rice.edl.impl.bo.EDocLiteAssociation;
import org.kuali.rice.edl.impl.bo.EDocLiteDefinition;
import org.w3c.dom.Document;


public interface EDocLiteService extends XmlLoader, XmlExporter {

	void saveEDocLiteDefinition(InputStream xml);
    void saveEDocLiteAssociation(InputStream xml);

    EDocLiteDefinition getEDocLiteDefinition(String defName);
    EDocLiteAssociation getEDocLiteAssociation(String docType);
    EDocLiteAssociation getEDocLiteAssociation(Long associationId);

    List<EDocLiteDefinition> getEDocLiteDefinitions();
    List<EDocLiteAssociation> getEDocLiteAssociations();

    Templates getStyleAsTranslet(String styleName) throws TransformerConfigurationException;
    List<EDocLiteAssociation> search(EDocLiteAssociation edocLite);

    EDLController getEDLControllerUsingEdlName(String edlName);
	EDLController getEDLControllerUsingDocumentId(String documentId);
	void initEDLGlobalConfig();
    void saveEDocLiteDefinition(EDocLiteDefinition data) ;
    void saveEDocLiteAssociation(EDocLiteAssociation assoc);
    Document getDefinitionXml(EDocLiteAssociation edlAssociation);
}
