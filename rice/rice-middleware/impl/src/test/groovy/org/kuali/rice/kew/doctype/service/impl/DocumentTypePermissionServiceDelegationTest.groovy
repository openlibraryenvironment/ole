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
package org.kuali.rice.kew.doctype.service.impl

import junit.framework.Assert
import org.junit.Before
import org.junit.Test
import org.kuali.rice.core.api.CoreConstants
import org.kuali.rice.core.api.config.property.ConfigContext
import org.kuali.rice.core.api.resourceloader.GlobalResourceLoader
import org.kuali.rice.core.api.resourceloader.ResourceLoader
import org.kuali.rice.core.api.util.ClassLoaderUtils
import org.kuali.rice.core.impl.config.property.JAXBConfigImpl
import org.kuali.rice.kew.doctype.bo.DocumentType
import org.kuali.rice.kew.doctype.service.DocumentTypePermissionService
import org.kuali.rice.kew.framework.document.security.DocumentTypeAuthorizer

import javax.xml.namespace.QName

/**
 * Test DocumentTypePermissionService delegation
 */
class DocumentTypePermissionServiceDelegationTest {
    static def DEFAULT_IMPL_CLASS = KimDocumentTypeAuthorizer.class

    static def service = new DocumentTypePermissionServiceAuthorizerImpl()


    def testDocumentTypeAuthorizer = [:] as DocumentTypeAuthorizer

    @Before
    void setupFakeEnv() {
        def config = new JAXBConfigImpl();
        config.putProperty(CoreConstants.Config.APPLICATION_ID, "APPID");
        ConfigContext.init(config);

        GlobalResourceLoader.stop()
        GlobalResourceLoader.addResourceLoader([
            getName: { -> new QName("KEW Unit Test", "DocumentTypePermissionServiceDelegationTest") },
            getService: { QName name ->
                [ testDocumentTypeAuthorizer: testDocumentTypeAuthorizer ][name.getLocalPart()]
            },
            getObject: { od -> try { return ClassLoaderUtils.getClass(od.className, DocumentTypeAuthorizer.class).newInstance() } catch (Exception e) { return null } },
            stop: {}
        ] as ResourceLoader)
    }

    @Test
    void testGetDelegateNullDocTypeReturnsDefaultImpl() {
        Assert.assertEquals(DEFAULT_IMPL_CLASS, service.getDocumentTypeAuthorizer(null).class)
    }

    @Test
    void testGetDelegateBlankDelegateReturnsDefaultImpl() {
        Assert.assertEquals(DEFAULT_IMPL_CLASS, service.getDocumentTypeAuthorizer(new DocumentType() {
            def DocumentType parentDocType = null
        }).class)
    }

    @Test
    void testGetDelegateReturnsConfiguredDelegateFromService() {
        def bo = new DocumentType(authorizer: "testDocumentTypeAuthorizer")
        Assert.assertEquals(testDocumentTypeAuthorizer, service.getDocumentTypeAuthorizer(bo))
    }

    @Test
    void testGetDelegateReturnsConfiguredDelegateFromClass() {
        def bo = new DocumentType(authorizer: TestDocumentTypeAuthorizer.class.getName())
        Assert.assertTrue(TestDocumentTypeAuthorizer.class.isAssignableFrom(service.getDocumentTypeAuthorizer(bo).getClass()))
    }

    @Test
    void testGetDelegateReturnsParentConfiguredDelegate() {
        def bo = new DocumentType() {
            def DocumentType parentDocType = new DocumentType(authorizer: "testDocumentTypeAuthorizer")
        }
        Assert.assertEquals(testDocumentTypeAuthorizer, service.getDocumentTypeAuthorizer(bo))
    }

    @Test
    void testGetDelegateReturnsParentConfiguredDelegateFromClass() {
        def bo = new DocumentType() {
            def DocumentType parentDocType = new DocumentType(authorizer: TestDocumentTypeAuthorizer.class.getName())
        }
        Assert.assertTrue(TestDocumentTypeAuthorizer.class.isAssignableFrom(service.getDocumentTypeAuthorizer(bo).getClass()))
    }
}