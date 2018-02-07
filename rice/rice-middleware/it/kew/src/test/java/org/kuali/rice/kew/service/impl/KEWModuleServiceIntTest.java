/**
 * Copyright 2005-2013 The Kuali Foundation
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
package org.kuali.rice.kew.service.impl;

import org.junit.Before;
import org.junit.Test;
import org.kuali.rice.core.api.resourceloader.GlobalResourceLoader;
import org.kuali.rice.kew.doctype.bo.DocumentTypeEBO;
import org.kuali.rice.kew.test.KEWTestCase;
import org.kuali.rice.krad.service.KualiModuleService;
import org.kuali.rice.krad.service.ModuleService;
import org.springframework.util.CollectionUtils;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

import static org.junit.Assert.*;

/**
 * Integration test for the {@link KEWModuleService}
 */
public class KEWModuleServiceIntTest extends KEWTestCase {

    private static final String KUALI_NAMESPACE_CODE = "KUALI";

    private ModuleService kewModuleService;

    @Before
    public void setupServiceUnderTest() {
        KualiModuleService kualiModuleService = GlobalResourceLoader.getService("kualiModuleService");

        kewModuleService = kualiModuleService.getResponsibleModuleService(DocumentTypeEBO.class);
    }

    /**
     * Test that we can load the DocumentType EBO which this ModuleService is responsible for
     */
    @Test public void testGetDocumentTypeEbo() {
        assertNotNull("kewModuleService wasn't successfully configured", kewModuleService);

        DocumentTypeEBO riceDocument = kewModuleService.getExternalizableBusinessObject(DocumentTypeEBO.class,
                Collections.<String,Object>singletonMap("name", "RiceDocument"));

        assertNotNull("riceDocument wasn't successfully loaded", riceDocument);
        assertEquals("riceDocument doesn't have the requested name", riceDocument.getName(),
                "RiceDocument");

        assertNull("non-null result for bogus query", kewModuleService.getExternalizableBusinessObject(
                DocumentTypeEBO.class, Collections.<String, Object>singletonMap("name", "Kwisatz Haderach")));

        List<DocumentTypeEBO> documentTypes = kewModuleService.getExternalizableBusinessObjectsList(DocumentTypeEBO.class,
                Collections.<String,Object>emptyMap());

        assertFalse("documentTypes weren't successfully retrieved", CollectionUtils.isEmpty(documentTypes));
        assertTrue(documentTypes.size() > 1);
        assertTrue(contains(documentTypes, riceDocument));

        documentTypes = kewModuleService.getExternalizableBusinessObjectsList(DocumentTypeEBO.class,
                Collections.<String,Object>singletonMap("name", "RiceDocument"));

        assertFalse("documentTypes weren't successfully retrieved", CollectionUtils.isEmpty(documentTypes));
        assertTrue(documentTypes.size() == 1);
        assertTrue(contains(documentTypes, riceDocument));

        documentTypes = kewModuleService.getExternalizableBusinessObjectsList(DocumentTypeEBO.class,
                Collections.<String,Object>singletonMap("name", "Kwisatz Haderach"));

        assertTrue("no documentTypes should have been returned", CollectionUtils.isEmpty(documentTypes));
    }

    private static boolean contains(Collection<DocumentTypeEBO> collection, DocumentTypeEBO documentType) {
        if (collection != null) for (DocumentTypeEBO element : collection) {
            if (documentType.getName().equals(element.getName()) &&
                    documentType.getDocumentTypeId().equals(element.getDocumentTypeId())) {
                return true;
            }
        }
        return false;
    }

}
