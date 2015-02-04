/*
 * Copyright 2011 The Kuali Foundation.
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
package org.kuali.ole;

import org.junit.Ignore;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.jcr.nodetype.NodeType;

import static junit.framework.Assert.assertNotNull;

/**
 * Created by IntelliJ IDEA.
 * User: kuali-ole
 * Date: 5/5/11
 * Time: 10:00 AM
 * To change this template use File | Settings | File Templates.
 */
@Ignore
@Deprecated
public class CustomNodeRegistrar_UT
        extends BaseTestCase {
    private static Logger LOG = LoggerFactory.getLogger(CustomNodeRegistrar_UT.class);

    @Test
    public void testRegisterCustomNodeTypes() throws Exception {
        CustomNodeRegistrar customNodeRegistrar = new CustomNodeRegistrar();
        NodeType[] nodeTypes = customNodeRegistrar.registerCustomNodeTypes(null);
        assertNotNull(nodeTypes);
        for (int i = 0; i < nodeTypes.length; i++) {
            NodeType nodeType = nodeTypes[i];
            LOG.info(nodeType.getName());
        }
    }
}
