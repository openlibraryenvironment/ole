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
package org.kuali.rice.krms.impl.repository

import org.kuali.rice.krms.api.repository.term.TermSpecificationDefinition
import org.junit.Test
import org.kuali.rice.krms.api.repository.term.TermParameterDefinition
import org.kuali.rice.krms.api.repository.term.TermDefinition
import org.junit.Assert
import org.kuali.rice.krms.api.repository.context.ContextDefinition
import org.kuali.rice.krms.api.repository.TermSpecificationDefinitionTest
import org.kuali.rice.krms.api.repository.ContextDefinitionTest
import org.kuali.rice.krms.api.repository.TermDefinitionTest
import org.kuali.rice.krms.api.repository.type.KrmsAttributeDefinition
import org.kuali.rice.krms.api.repository.KrmsAttributeDefinitionTest

/**
 * Test translation from BO to model object and back again
 */
class BoTranslationTest {

    @Test
    public void testTermBoTranslation() {
        TermDefinition termDef = TermDefinitionTest.buildFullTermDefinition();

        Assert.assertEquals(termDef, TermBo.to(TermBo.from(termDef)));
    }

    @Test
    public void testContextBoTranslation() {
       ContextDefinition myContext = ContextDefinitionTest.buildFullContextDefinition();

        Assert.assertEquals(myContext, ContextBo.to(ContextBo.from(myContext)));
    }

    @Test
    void testTermSpecificationBoTranslation() {
        TermSpecificationDefinition termSpecDef = TermSpecificationDefinitionTest.buildFullTermSpecificationDefinition();

        Assert.assertEquals(termSpecDef, TermSpecificationBo.to(TermSpecificationBo.from(termSpecDef)));
    }

    @Test
    void testKrmsAttributeBoTranslation() {
        KrmsAttributeDefinition attr = KrmsAttributeDefinitionTest.buildFullKrmsAttributeDefinition();

        Assert.assertEquals(attr, KrmsAttributeDefinitionBo.to(KrmsAttributeDefinitionBo.from(attr)));
    }
}
