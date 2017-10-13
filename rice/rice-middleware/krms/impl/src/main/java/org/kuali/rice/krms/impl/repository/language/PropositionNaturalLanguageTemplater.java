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
package org.kuali.rice.krms.impl.repository.language;

import org.apache.commons.lang.StringUtils;
import org.apache.velocity.exception.VelocityException;
import org.kuali.rice.krms.api.repository.language.NaturalLanguageTemplate;
import org.kuali.rice.krms.api.repository.language.NaturalLanguageTemplaterContract;
import org.kuali.rice.krms.api.repository.proposition.PropositionParameterType;
import org.kuali.rice.krms.api.repository.type.KrmsTypeDefinitionContract;
import org.kuali.rice.krms.api.repository.type.KrmsTypeRepositoryService;
import org.kuali.rice.krms.impl.repository.KrmsTypeRepositoryServiceImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * This class translates requirement components into a specific
 * natural language. This class is not thread safe.
 */
public class PropositionNaturalLanguageTemplater implements NaturalLanguageTemplaterContract {
    /**
     * SLF4J logging framework
     */
    private final static Logger logger = LoggerFactory.getLogger(PropositionNaturalLanguageTemplater.class);

    private TranslationContextRegistry<TranslationContext> translationContextRegistry;

    private KrmsTypeRepositoryService krmsTypeRepositoryService = new KrmsTypeRepositoryServiceImpl();

    /**
     * Relational operator token.
     */
    public final static String OPERATOR_TOKEN = "relationalOperator";
    /**
     * An integer value token.
     */
    public final static String CONSTANT_VALUE_TOKEN = "intValue";

    /**
     * Velocity template engine.
     */
    private VelocityTemplateEngine templateEngine = new VelocityTemplateEngine();


    /**
     * Constructs a new proposition natural language templater.
     */
    public PropositionNaturalLanguageTemplater() {
    }


    /**
     * Sets the template context registry.
     *
     * @param translationContextRegistry Template context registry
     */
    public void setTranslationContextRegistry(
            final TranslationContextRegistry<TranslationContext> translationContextRegistry) {
        this.translationContextRegistry = translationContextRegistry;
    }

    public String translate(NaturalLanguageTemplate naturalLanguageTemplate, Map<String, Object> parametersMap) {

        if (naturalLanguageTemplate == null) {
            return StringUtils.EMPTY;
        }

        Map<String, Object> contextMap = null;
        try {
            contextMap = buildContextMap(naturalLanguageTemplate.getTypeId(), parametersMap);
        } catch (Exception e) {
            e.printStackTrace();  //TODO hand back to service.
        }

        try {
            String nl = this.templateEngine.evaluate(contextMap, naturalLanguageTemplate.getTemplate());
            if (logger.isInfoEnabled()) {
                logger.info("nl=" + nl);
            }
            return nl;
        } catch (VelocityException e) {
            String msg = "Generating template for proposition failed: template='" + naturalLanguageTemplate.getTemplate() + "', contextMap=" + contextMap;
            logger.error(msg, e);
            //TODO hand back to service throw new Exception(msg);
        }
        return "Error";
    }

    /**
     * Builds a proposition type context map.
     *
     * @param typeId        the natural language template id
     * @param parametersMap map containing the proposition parameter types and their values
     * @throws java.lang.Exception Creating context map failed
     */
    private Map<String, Object> buildContextMap(String typeId, Map<String, Object> parametersMap) throws Exception {

        Map<String, Object> contextMap = new HashMap<String, Object>();
        //Add proposition constant to contextMap.
        if (parametersMap.containsKey(PropositionParameterType.CONSTANT.getCode())) {
            contextMap.put(CONSTANT_VALUE_TOKEN, (String) parametersMap.get(PropositionParameterType.CONSTANT.getCode()));
        }
        //Add proposition operator to contextMap.
        if (parametersMap.containsKey(PropositionParameterType.OPERATOR.getCode())) {
            contextMap.put(OPERATOR_TOKEN, (String) parametersMap.get(PropositionParameterType.OPERATOR.getCode()));
        }
        //Access type service to retrieve type name.
        KrmsTypeDefinitionContract type = getKrmsTypeRepositoryService().getTypeById(typeId);
        List<TranslationContext> translationContextList = this.translationContextRegistry.get(type.getName());
        if (translationContextList == null || translationContextList.isEmpty()) {
            return contextMap;
        }

        for (TranslationContext translationContext : translationContextList) {
            Map<String, Object> cm = translationContext.createContextMap(parametersMap);
            contextMap.putAll(cm);
        }

        if (logger.isInfoEnabled()) {
            logger.info("contextMap=" + contextMap);
        }
        return contextMap;
    }


    private KrmsTypeRepositoryService getKrmsTypeRepositoryService() {
        return krmsTypeRepositoryService;
    }

    public void setKrmsTypeRepositoryService(KrmsTypeRepositoryService krmsTypeRepositoryService) {
        this.krmsTypeRepositoryService = krmsTypeRepositoryService;
    }

}
