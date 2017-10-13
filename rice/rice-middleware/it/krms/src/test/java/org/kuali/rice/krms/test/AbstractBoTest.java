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
package org.kuali.rice.krms.test;

import org.junit.Before;
import org.kuali.rice.krad.service.BusinessObjectService;
import org.kuali.rice.krad.service.KRADServiceLocator;
import org.kuali.rice.krms.api.repository.context.ContextDefinition;
import org.kuali.rice.krms.api.repository.type.KrmsAttributeDefinition;
import org.kuali.rice.krms.api.repository.type.KrmsTypeAttribute;
import org.kuali.rice.krms.api.repository.type.KrmsTypeDefinition;
import org.kuali.rice.krms.api.repository.type.KrmsTypeRepositoryService;
import org.kuali.rice.krms.impl.repository.ContextAttributeBo;
import org.kuali.rice.krms.impl.repository.ContextBo;
import org.kuali.rice.krms.impl.repository.ContextBoService;
import org.kuali.rice.krms.impl.repository.KrmsAttributeDefinitionService;
import org.kuali.rice.krms.impl.repository.KrmsRepositoryServiceLocator;
import org.kuali.rice.test.BaselineTestCase.BaselineMode;
import org.kuali.rice.test.BaselineTestCase.Mode;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;

@BaselineMode(Mode.ROLLBACK)
public abstract class AbstractBoTest extends KRMSTestCase {
	
    protected ContextBoService contextRepository;
    protected KrmsTypeRepositoryService krmsTypeRepository;
    protected KrmsAttributeDefinitionService krmsAttributeDefinitionService;

    @Override
    @Before
    public void setUp() throws Exception {
        super.setUp();
        contextRepository = KrmsRepositoryServiceLocator.getContextBoService();
        krmsTypeRepository = KrmsRepositoryServiceLocator.getKrmsTypeRepositoryService();
        krmsAttributeDefinitionService = KrmsRepositoryServiceLocator.getKrmsAttributeDefinitionService();
    }

    protected BusinessObjectService getBoService() {
		return KRADServiceLocator.getBusinessObjectService();
	}

    protected ContextDefinition createContextDefinition(String nameSpace, String name,
            Map<String, String> contextAttributes) {
        // Attributes for context;
        List<KrmsTypeAttribute.Builder> contextAttributeBuilders = new ArrayList<KrmsTypeAttribute.Builder>();
        int contextAttrSequenceIndex = 0;

        List<KrmsAttributeDefinition> attributeDefintions = new ArrayList<KrmsAttributeDefinition>();

        if (contextAttributes != null) for (Map.Entry<String,String> entry : contextAttributes.entrySet()) {
            KrmsAttributeDefinition.Builder contextTypeAttributeDefnBuilder = KrmsAttributeDefinition.Builder.create(null, entry.getKey(), nameSpace);
            contextTypeAttributeDefnBuilder.setLabel(entry.getKey() + " attribute label");
            KrmsAttributeDefinition contextTypeAttributeDefinition = krmsAttributeDefinitionService.createAttributeDefinition(contextTypeAttributeDefnBuilder.build());
            attributeDefintions.add(contextTypeAttributeDefinition);

            // Attr for context;
            contextAttributeBuilders.add(KrmsTypeAttribute.Builder.create(null, contextTypeAttributeDefinition.getId(),
                    contextAttrSequenceIndex));
            contextAttrSequenceIndex += 1;
        }

        // KrmsType for context
        KrmsTypeDefinition krmsContextTypeDefinition = krmsTypeRepository.getTypeByName(nameSpace, "KrmsTestContextType");

        if (krmsContextTypeDefinition == null) {
            KrmsTypeDefinition.Builder krmsContextTypeDefnBuilder = KrmsTypeDefinition.Builder.create("KrmsTestContextType", nameSpace);
            krmsContextTypeDefnBuilder.setAttributes(contextAttributeBuilders);
            krmsContextTypeDefinition = krmsContextTypeDefnBuilder.build();
            krmsContextTypeDefinition = krmsTypeRepository.createKrmsType(krmsContextTypeDefinition);
        }

        // Context
        ContextDefinition.Builder contextBuilder = ContextDefinition.Builder.create(nameSpace, name);
        contextBuilder.setTypeId(krmsContextTypeDefinition.getId());
        ContextDefinition contextDefinition = contextBuilder.build();
        contextDefinition = contextRepository.createContext(contextDefinition);

        // Context Attribute
        // TODO: do this fur eel
        for (KrmsAttributeDefinition contextTypeAttributeDefinition : attributeDefintions) {
            ContextAttributeBo contextAttribute = new ContextAttributeBo();
            contextAttribute.setAttributeDefinitionId(contextTypeAttributeDefinition.getId());
            contextAttribute.setContextId(contextDefinition.getId());
            contextAttribute.setValue(contextAttributes.get(contextTypeAttributeDefinition.getName()));
            getBoService().save(contextAttribute);
        }

        return contextDefinition;
    }

    protected KrmsTypeDefinition createKrmsGenericTypeDefinition(String nameSpace, String serviceName,
            String attributeDefinitionLabel, String attributeDefinitionName) {
        KrmsTypeDefinition krmsGenericTypeDefinition = krmsTypeRepository.getTypeByName(nameSpace, "KrmsTestGenericType");

        if (null == krmsGenericTypeDefinition) {

            // Attribute Defn for generic type;
            KrmsAttributeDefinition.Builder genericTypeAttributeDefnBuilder = KrmsAttributeDefinition.Builder.create(null,
                    attributeDefinitionName, nameSpace);
            genericTypeAttributeDefnBuilder.setLabel(attributeDefinitionLabel);
            KrmsAttributeDefinition genericTypeAttributeDefinition1 = krmsAttributeDefinitionService.createAttributeDefinition(genericTypeAttributeDefnBuilder.build());

            // Attr for generic type;
            KrmsTypeAttribute.Builder genericTypeAttrBuilder = KrmsTypeAttribute.Builder.create(null, genericTypeAttributeDefinition1.getId(), 1);

            // Can use this generic type for KRMS bits that don't actually rely on services on the bus at this point in time
            KrmsTypeDefinition.Builder krmsGenericTypeDefnBuilder = KrmsTypeDefinition.Builder.create("KrmsTestGenericType", nameSpace);
            krmsGenericTypeDefnBuilder.setServiceName(serviceName);
            krmsGenericTypeDefnBuilder.setAttributes(Collections.singletonList(genericTypeAttrBuilder));
            krmsGenericTypeDefinition = krmsTypeRepository.createKrmsType(krmsGenericTypeDefnBuilder.build());

        }

        return krmsGenericTypeDefinition;
    }


    public String getNamespaceByContextName(String name) {
        Collection<ContextBo> results = getBoService().findMatching(ContextBo.class, Collections.singletonMap("name", name));
        if (CollectionUtils.isEmpty(results)) {
            return null;
        }
        if (results.size() != 1) {
            throw new IllegalStateException(
                    "getNamespaceByContextName can't handle a universe where multiple contexts have the same name");
        }
        return results.iterator().next().getNamespace();
    }

    public ContextBoService getContextRepository() {
        return contextRepository;
    }

    public KrmsTypeRepositoryService getKrmsTypeRepository() {
        if (krmsTypeRepository == null ) {
            krmsTypeRepository = KrmsRepositoryServiceLocator.getKrmsTypeRepositoryService();
        }
        return krmsTypeRepository;
    }
}
