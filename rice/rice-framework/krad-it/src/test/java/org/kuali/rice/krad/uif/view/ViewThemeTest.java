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
package org.kuali.rice.krad.uif.view;

import org.junit.Test;
import org.kuali.rice.krad.service.KRADServiceLocatorWeb;
import org.kuali.rice.krad.test.KRADTestCase;
import org.kuali.rice.krad.web.form.UifFormBase;

import java.util.HashMap;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.spy;

/**
 * Test cases for {@link ViewTheme}
 *
 * @author Kuali Rice Team (rice.collab@kuali.org)
 */
public class ViewThemeTest extends KRADTestCase {

    /**
     * Tests the final CSS and Script file listings are built correctly when using
     * manual configuration
     */
    @Test
    public void testManualThemeConfiguration() {
        View view = KRADServiceLocatorWeb.getViewService().getViewById("TestViewTheme1");

        ViewTheme theme = view.getTheme();
        assertNotNull(theme);

        assertEquals(2, theme.getMinCssSourceFiles().size());
        assertEquals(2, theme.getMinScriptSourceFiles().size());

        // test dev mode
        theme = spy(theme);
        doReturn(true).when(theme).inDevMode();
        view.setTheme(theme);

        KRADServiceLocatorWeb.getViewService().buildView(view, new UifFormBase(), new HashMap<String, String>());

        assertEquals(2, theme.getCssFiles().size());
        assertEquals(2, theme.getScriptFiles().size());

        // test non dev mode
        view = KRADServiceLocatorWeb.getViewService().getViewById("TestViewTheme1");

        theme = view.getTheme();
        assertNotNull(theme);

        theme = spy(theme);
        doReturn(false).when(theme).inDevMode();
        view.setTheme(theme);

        KRADServiceLocatorWeb.getViewService().buildView(view, new UifFormBase(), new HashMap<String, String>());

        assertEquals(1, theme.getCssFiles().size());
        assertEquals(1, theme.getScriptFiles().size());

        assertTrue(theme.getCssFiles().contains(theme.getMinCssFile()));
        assertTrue(theme.getScriptFiles().contains(theme.getMinScriptFile()));
    }
}
