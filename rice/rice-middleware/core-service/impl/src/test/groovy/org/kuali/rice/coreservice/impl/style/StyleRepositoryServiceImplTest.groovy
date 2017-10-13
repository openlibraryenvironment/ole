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
package org.kuali.rice.coreservice.impl.style

import groovy.mock.interceptor.MockFor
import org.junit.Before
import org.junit.Ignore
import org.junit.Test
import org.kuali.rice.coreservice.api.style.Style
import org.kuali.rice.coreservice.api.style.StyleContract
import org.kuali.rice.coreservice.api.style.StyleRepositoryService
import static org.junit.Assert.assertEquals
import static org.junit.Assert.assertTrue

class StyleRepositoryServiceImplTest {

    private MockFor daoMock
    StyleRepositoryService styleRepositoryService;
    StyleRepositoryServiceImpl styleRepositoryServiceImpl;

    //importing the should fail method since I don't want to extend
    //GroovyTestCase which is junit 3 style
    private final shouldFail = new GroovyTestCase().&shouldFail

    private static final String STYLE_ID = "1"
    private static final String NAME = "MyFirstStyle"
    private static final boolean ACTIVE = true
    private static final String XML_CONTENT = "<my><awesome><xml-stylesheet/></awesome></my>"
    private static final Long VERSION_NUMBER = 1
    private static final String OBJECT_ID = UUID.randomUUID()

    private static createStyle() {
        return Style.Builder.create(new StyleContract() {
            def String id = StyleRepositoryServiceImplTest.STYLE_ID
            def String name = StyleRepositoryServiceImplTest.NAME
            def boolean active = StyleRepositoryServiceImplTest.ACTIVE
            def String xmlContent = StyleRepositoryServiceImplTest.XML_CONTENT
            def Long versionNumber = StyleRepositoryServiceImplTest.VERSION_NUMBER
            def String objectId = StyleRepositoryServiceImplTest.OBJECT_ID
        }).build()
    }

    private static final Style style = createStyle()

    @Before
    void setupServiceUnderTest() {
        daoMock = new MockFor(StyleDao)
        styleRepositoryServiceImpl = new StyleRepositoryServiceImpl()
        styleRepositoryServiceImpl.setStyleDao(daoMock.proxyDelegateInstance())
        styleRepositoryService = styleRepositoryServiceImpl
    }

    void injectStyleDaoIntoStyleRepositoryService() {
        def styleDao = daoMock.proxyDelegateInstance()
        styleRepositoryServiceImpl.setStyleDao(styleDao)
    }

    private void verifyMocks() {
        daoMock.verify(styleRepositoryServiceImpl.styleDao)
    }

    @Test
    void testGetStyle_nullStyleName() {
        shouldFail(IllegalArgumentException.class) {
            styleRepositoryService.getStyle(null)
        }
        verifyMocks()
    }

    @Test
    void testGetStyle_emptyStyleName() {
        shouldFail(IllegalArgumentException.class) {
            styleRepositoryService.getStyle("")
        }
        verifyMocks()
    }

    @Test
    void testGetStyle_blankStyleName() {
        shouldFail(IllegalArgumentException.class) {
            styleRepositoryService.getStyle(" ")
        }
        verifyMocks()
    }

    @Test
    void testGetStyle_valid() {
        def styleBo = StyleBo.from(style)
        setupServiceUnderTest()
        daoMock.demand.getStyle(1) { styleName -> styleBo }
        injectStyleDaoIntoStyleRepositoryService()

        def style = styleRepositoryService.getStyle(NAME)
        assertTrue style != null
        assertEquals style, createStyle()

        verifyMocks()
    }

    // currently fails because of ConfigContext not being initialized, can we mock this somehow?

    @Ignore
    @Test
    void testGetStyle_doesntExist() {
        daoMock.demand.getStyle(1) { styleName -> null }
        injectStyleDaoIntoStyleRepositoryService()

        def style = styleRepositoryService.getStyle(NAME)
        assertTrue style == null

        verifyMocks()
    }

    @Test
    void testSaveStyle_nullStyle() {
        shouldFail(IllegalArgumentException.class) {
            styleRepositoryService.saveStyle(null)
        }

        verifyMocks()
    }

    @Test
    void testSaveStyle_modify() {
        def styleBo = StyleBo.from(style)
        setupServiceUnderTest()
        def builder = Style.Builder.create(styleBo)
        builder.setActive(false)
        def styleModified = builder.build()
        assert !styleModified.active

        def savedStyle = styleBo
        daoMock.demand.getStyle(1) { styleName -> savedStyle }
        daoMock.demand.saveStyle(2) { styleToSave -> savedStyle = styleToSave }
        daoMock.demand.getStyle(1) { styleName -> savedStyle }

        injectStyleDaoIntoStyleRepositoryService()

        styleRepositoryService.saveStyle(styleModified)
        assertTrue styleModified == styleRepositoryService.getStyle(styleModified.name);

        verifyMocks()

    }
    @Test
    void testGetStyle_valid_afterModify() {
        def styleBo = StyleBo.from(style)
        setupServiceUnderTest()
        def builder = Style.Builder.create(styleBo)
        builder.setActive(false)
        def styleModified = builder.build()
        assert !styleModified.active

        def savedStyle = styleBo
        daoMock.demand.getStyle(1) { styleName -> savedStyle }
        daoMock.demand.saveStyle(2) { styleToSave -> savedStyle = styleToSave }
        daoMock.demand.getStyle(1) { styleName -> savedStyle }

        injectStyleDaoIntoStyleRepositoryService()

        styleRepositoryService.saveStyle(styleModified)
        assertTrue styleModified == styleRepositoryService.getStyle(styleModified.name);

        verifyMocks()
        // getStyle_valid test after SaveStyle_modify
        styleBo = StyleBo.from(style)
        setupServiceUnderTest()
        daoMock.demand.getStyle(1) { styleName -> styleBo }
        injectStyleDaoIntoStyleRepositoryService()

        def style = styleRepositoryService.getStyle(NAME)
        assertTrue style != null
        assertEquals style, createStyle()

        verifyMocks()
    }
}
