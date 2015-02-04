package org.kuali.ole.ingest;

import org.kuali.ole.OLEConstants;
import org.kuali.ole.ingest.ProfileXMLSchemaValidator;
import org.kuali.ole.ingest.pojo.*;
import org.kuali.rice.core.api.resourceloader.GlobalResourceLoader;
import org.kuali.rice.krad.bo.PersistableBusinessObject;
import org.kuali.rice.krad.service.BusinessObjectService;
import org.kuali.rice.krad.service.KRADServiceLocator;
import org.kuali.rice.krms.api.repository.term.TermSpecificationDefinition;
import org.kuali.rice.krms.impl.repository.*;
import org.xml.sax.SAXException;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.*;

/**
 *  ProfileBuilder is used to build the KRMS Agenda for the Profile xml which is used for Staff upload screen.
 */
public class ProfileBuilder {
    private TermBoService termBoService;
    private BusinessObjectService businessObjectService;
    private ProfileObjectGeneratorFromXML profileObjectGeneratorFromXML;

    /**
     *   This method creates the context.
     * @param namespace
     * @param contextName
     * @return  contextBo
     */
    public ContextBo createContext(String namespace, String contextName) {
        ContextBo contextBo;
        Object existing = objectExists(ContextBo.class, "nm", contextName);
        if (null == existing) {
            contextBo = new ContextBo();
            contextBo.setNamespace(namespace);
            contextBo.setName(contextName);
            getBusinessObjectService().save(contextBo);
        } else {
            contextBo = (ContextBo) existing;
        }
        return contextBo;
    }

    /**
     *  This method registers the default service types in the database for the context.
     * @param contextBo
     * @return  contextValidActionBo
     */
    public ContextValidActionBo registerDefaultServiceTypes(ContextBo contextBo) {
        KrmsTypeBo actionTypeService = createActionTypeService();
        ContextValidActionBo contextValidActionBo;
        Object existing = objectExists(ContextValidActionBo.class, "actn_typ_id", actionTypeService.getId());
        if (existing == null) {
            contextValidActionBo = new ContextValidActionBo();
            contextValidActionBo.setActionType(actionTypeService);
            contextValidActionBo.setActionTypeId(actionTypeService.getId());
            contextValidActionBo.setContextId(contextBo.getId());
            getBusinessObjectService().save(contextValidActionBo);
        } else {
            contextValidActionBo = (ContextValidActionBo) existing;
        }
        return contextValidActionBo;
    }

    /**
     * This method  create Bibliographic record action as type in the database for creating custom action.
     * @return actionTypeService
     */
    public KrmsTypeBo createActionTypeService() {
        KrmsTypeBo actionTypeService;
        String typeName = "Create Bibliographic Record";
        Object existing = objectExists(KrmsTypeBo.class, "nm", typeName);
        if (existing == null) {
            actionTypeService = new KrmsTypeBo();
            actionTypeService.setActive(true);
            actionTypeService.setName("Create Bibliographic Record");
            actionTypeService.setNamespace(OLEConstants.OLE_NAMESPACE);
            actionTypeService.setServiceName("createBibActionTypeService");
            getBusinessObjectService().save(actionTypeService);
        } else {
            actionTypeService = (KrmsTypeBo) existing;
        }
        return actionTypeService;
    }

    /**
     *   This method creates profileTermResolverType record as type in database for creating custom action.
     * @return profileTermResolverTypeService
     */
    public KrmsTypeBo createTermResolverTypeService() {
        KrmsTypeBo profileTermResolverTypeService;
        String serviceName = "profileTermResolverTypeService";
        Object existing = objectExists(KrmsTypeBo.class, "srvc_nm", serviceName);
        if (existing == null) {
            profileTermResolverTypeService = new KrmsTypeBo();
            profileTermResolverTypeService.setActive(true);
            profileTermResolverTypeService.setName("ProfileTermResolver");
            profileTermResolverTypeService.setNamespace(OLEConstants.OLE_NAMESPACE);
            profileTermResolverTypeService.setServiceName("profileTermResolverTypeService");
            getBusinessObjectService().save(profileTermResolverTypeService);
        } else {
            profileTermResolverTypeService = (KrmsTypeBo) existing;
        }
        return profileTermResolverTypeService;
    }

    /**
     *  This method creates the category.
     *  If category name already exists it returns the category else it returns new category.
     * @param namespace
     * @param categoryName
     * @return  categoryBo
     */
    public CategoryBo createCategory(String namespace, String categoryName) {
        CategoryBo categoryBo;
        Object existing = objectExists(CategoryBo.class, "nm", categoryName);
        if (existing == null) {
            categoryBo = new CategoryBo();
            categoryBo.setName(categoryName);
            categoryBo.setNamespace(namespace);
            getBusinessObjectService().save(categoryBo);
        } else {
            categoryBo = (CategoryBo) existing;
        }
        return categoryBo;
    }

    /**
     *  This method creates the term specifications .
     *  If the term already exists in termSpecificationBo then it will return that object else it will create  new termSpecificationBo.
     * @param namespace
     * @param termSpecificationName
     * @param type
     * @param categoryBoList
     * @param contextBoList
     * @return  termSpecificationBo
     */
    //TODO: Wasn't able to use business object service to save the term spec as the pk generated was empty string.
    //TOOD: When using the termboservice (which in turn uses the bo service) its generating the pk. Bug need to be
    //TODO: fixed with Rice Team.
    public TermSpecificationBo createTermSpecification(String namespace, String termSpecificationName, String type, List<CategoryBo> categoryBoList, List<ContextBo> contextBoList) {
        TermSpecificationBo termSpecificationBo;
        Object existing = objectExists(TermSpecificationBo.class, "nm", termSpecificationName);
        if (existing == null) {
            termSpecificationBo = new TermSpecificationBo();
            termSpecificationBo.setActive(true);
            termSpecificationBo.setName(termSpecificationName);
            termSpecificationBo.setNamespace(namespace);
            termSpecificationBo.setCategories(categoryBoList);
            termSpecificationBo.setDescription(termSpecificationName);
            termSpecificationBo.setContexts(contextBoList);
            termSpecificationBo.setType(type);
            TermSpecificationDefinition termSpecification = getTermBoService().createTermSpecification(TermSpecificationDefinition.Builder.create(termSpecificationBo).build());
            termSpecificationBo = TermSpecificationBo.from(termSpecification);
        } else {
            termSpecificationBo = (TermSpecificationBo) existing;
        }
        return termSpecificationBo;
    }

    /**
     *  This method creates the term for each termSpecification.
     * @param name
     * @param termSpecificationBo
     * @return  termBo
     */
    public TermBo createTerm(String name, TermSpecificationBo termSpecificationBo) {
        TermBo termBo;
        Object existing = objectExists(TermBo.class, "term_spec_id", termSpecificationBo.getId());
        if (existing == null) {
            termBo = new TermBo();
            termBo.setSpecification(termSpecificationBo);
            termBo.setSpecificationId(termSpecificationBo.getId());
            termBo.setDescription(name);
            getBusinessObjectService().save(termBo);
        } else {
            termBo = (TermBo) existing;
        }
        return termBo;
    }

    /**
     *  This method creates the custom function by using functionLoader.
     * @param namespace
     * @param termName
     * @param returnType
     * @param categories
     * @return  functionBo
     */
    public FunctionBo createFunction(String namespace, String termName, String returnType, List categories) {
        Object existing = objectExists(FunctionBo.class, "nm", termName + "Function");
        FunctionBo functionBo;
        if (existing == null) {
            HashMap<String, String> map = new HashMap<String, String>();
            map.put("nm", "FunctionLoader");
            List<KrmsTypeBo> matching =
                    (List<KrmsTypeBo>) getBusinessObjectService().findMatching(KrmsTypeBo.class, map);
            KrmsTypeBo krmsTypeBo = matching.get(0);
            functionBo = new FunctionBo();
            functionBo.setActive(true);
            functionBo.setName(termName + "Function");
            functionBo.setNamespace(namespace);
            functionBo.setReturnType(returnType);
            functionBo.setCategories(categories);
            functionBo.setTypeId(krmsTypeBo.getId());
            getBusinessObjectService().save(functionBo);

            FunctionParameterBo functionParameterBo = new FunctionParameterBo();
            functionParameterBo.setFunctionId(functionBo.getId());
            functionParameterBo.setParameterType("org.kuali.ole.Ingest.ProfileTerm");
            functionParameterBo.setName(termName);
            functionParameterBo.setSequenceNumber(0);

            getBusinessObjectService().save(functionParameterBo);

            List<FunctionParameterBo> parameters = functionBo.getParameters();
            if (null == parameters) {
                parameters = new ArrayList<FunctionParameterBo>();
            }
            parameters.add(functionParameterBo);
            functionBo.setParameters(parameters);

            getBusinessObjectService().save(functionBo);
        } else {
            functionBo = (FunctionBo) existing;
        }

        return functionBo;
    }

    /**
     *  This method creates the simple or compound proposition.
     * @param categoryId
     * @param propositionDescription
     * @param propositionType
     * @param rule
     * @return  propositionBo
     */
    public PropositionBo createProposition(String categoryId, String propositionDescription, String propositionType, RuleBo rule) {
        PropositionBo propositionBo = new PropositionBo();
        propositionBo.setCategoryId(categoryId);
        propositionBo.setDescription(propositionDescription);
        propositionBo.setRuleId(rule.getId());
        propositionBo.setPropositionTypeCode(propositionType);
        getBusinessObjectService().save(propositionBo);
        rule.setProposition(propositionBo);
        getBusinessObjectService().save(rule);
        return propositionBo;
    }

    /**
     * This method creates the rule for the ruleName.
     * if the rule already exists then it will return that object else it return new rule.
     * @param namespace
     * @param ruleName
     * @return  ruleBo
     */
    public RuleBo createRule(String namespace, String ruleName) {
        RuleBo ruleBo = new RuleBo();
        Object existing = objectExists(RuleBo.class, "nm", ruleName);
        if (existing == null) {
            ruleBo.setName(ruleName);
            ruleBo.setNamespace(namespace);
            getBusinessObjectService().save(ruleBo);
        } else {
            ruleBo = (RuleBo) existing;
        }
        return ruleBo;
    }

    /**
     *  This method adds the action into rule to create the agenda.
     * @param namespace
     * @param actionName
     * @param description
     * @param actionTypeId
     * @param ruleBo
     * @param sequenceNumber
     * @return  actionBo
     */
    public ActionBo addActionToRule(String namespace, String actionName, String description, String actionTypeId, RuleBo ruleBo, Integer sequenceNumber) {
        ActionBo actionBo = new ActionBo();
        Object existing = objectExists(ActionBo.class, "nm", actionName);
        if (existing == null) {
            actionBo.setName(actionName + "Action");
            actionBo.setRuleId(ruleBo.getId());
            actionBo.setNamespace(namespace);
            actionBo.setSequenceNumber(sequenceNumber);
            actionBo.setDescription(description);
            actionBo.setTypeId(actionTypeId);
            getBusinessObjectService().save(actionBo);
            List<ActionBo> actionBos = ruleBo.getActions();
            if (null == actionBos) {
                actionBos = new ArrayList<ActionBo>();
            }
            actionBos.add(actionBo);
            ruleBo.setActions(actionBos);
            getBusinessObjectService().save(ruleBo);
        } else {
            actionBo = (ActionBo) existing;
        }
        return actionBo;
    }

    /**
     *   This method adds the customFunction into propositionParameter to create the rule and agenda.
     * @param functionId
     * @param proposition
     * @return  proposition
     */
    public PropositionBo addCustomFunctionToPropositionParameter(String functionId, PropositionBo proposition) {
        PropositionParameterBo propositionParameterBo = new PropositionParameterBo();
        propositionParameterBo.setPropId(proposition.getId());
        propositionParameterBo.setValue(functionId);
        propositionParameterBo.setSequenceNumber(1);
        propositionParameterBo.setParameterType("F");
        getBusinessObjectService().save(propositionParameterBo);
        List<PropositionParameterBo> parameters = proposition.getParameters();
        if (null == parameters) {
            parameters = new ArrayList<PropositionParameterBo>();
        }
        parameters.add(propositionParameterBo);
        proposition.setParameters(parameters);
        getBusinessObjectService().save(proposition);
        return proposition;
    }

    /**
     *  This method adds the term to propositionParameter to create the rule and agenda.
     * @param termId
     * @param proposition
     * @return proposition
     */
    public PropositionBo addTermToPropositionParameter(String termId, PropositionBo proposition) {
        PropositionParameterBo propositionParameterBo = new PropositionParameterBo();
        propositionParameterBo.setPropId(proposition.getId());
        propositionParameterBo.setValue(termId);
        propositionParameterBo.setSequenceNumber(0);
        propositionParameterBo.setParameterType("T");
        getBusinessObjectService().save(propositionParameterBo);
        List<PropositionParameterBo> parameters = proposition.getParameters();
        if (null == parameters) {
            parameters = new ArrayList<PropositionParameterBo>();
        }
        parameters.add(propositionParameterBo);
        proposition.setParameters(parameters);
        getBusinessObjectService().save(proposition);
        return proposition;
    }

    /**
     *  This method creates the agenda and persist in the database for the context.
     * @param agendaName
     * @param contextBo
     * @return  agendaBo
     */
    public AgendaBo createAgenda(String agendaName, ContextBo contextBo) {
        AgendaBo agendaBo;
        Object existing = objectExists(AgendaBo.class, "nm", agendaName);
        if (existing == null) {
            agendaBo = new AgendaBo();
            agendaBo.setActive(true);
            agendaBo.setName(agendaName);
            agendaBo.setContext(contextBo);
            agendaBo.setContextId(contextBo.getId());
            getBusinessObjectService().save(agendaBo);
            List<AgendaBo> agendas = contextBo.getAgendas();
            if (null == agendas) {
                agendas = new ArrayList<AgendaBo>();
            }
            agendas.add(agendaBo);
            contextBo.setAgendas(agendas);
            getBusinessObjectService().save(contextBo);
        } else {
            agendaBo = (AgendaBo) existing;
        }
        return agendaBo;
    }

    /**
     *  This method adds the rule to agenda.
     * @param agendaBo
     * @param ruleBo
     * @return agendaBo
     */
    public AgendaBo addRuleToAgenda(AgendaBo agendaBo, RuleBo ruleBo) {
        AgendaItemBo agendaItemBo = new AgendaItemBo();
        agendaItemBo.setAgendaId(agendaBo.getId());
        agendaItemBo.setRule(ruleBo);
        getBusinessObjectService().save(agendaItemBo);
        List<AgendaItemBo> agendaBoItems = agendaBo.getItems();
        if (null == agendaBoItems) {
            agendaBoItems = new ArrayList<AgendaItemBo>();
        }
        agendaBoItems.add(agendaItemBo);
        agendaBo.setItems(agendaBoItems);
        agendaBo.setFirstItemId(agendaBoItems.get(0).getId());
        getBusinessObjectService().save(agendaBo);
        return agendaBo;
    }

    /**
     *  This method adds the dummy rule into database for falseAction.
     * @param ruleBo
     * @param falseActionRuleBo
     * @return existingAgendaItemBo
     */
    public AgendaItemBo addDummyRuleForFalseAction(RuleBo ruleBo, RuleBo falseActionRuleBo) {
        HashMap<String, String> map = new HashMap<String, String>();
        map.put("rule_id", ruleBo.getId());
        List<AgendaItemBo> matching =
                (List<AgendaItemBo>) getBusinessObjectService().findMatching(AgendaItemBo.class, map);
        AgendaItemBo existingAgendaItemBo = matching.get(0);

        AgendaItemBo falseActionAgendaItemBo = new AgendaItemBo();
        falseActionAgendaItemBo.setAgendaId(existingAgendaItemBo.getAgendaId());
        falseActionAgendaItemBo.setRule(falseActionRuleBo);
        falseActionAgendaItemBo.setRuleId(falseActionRuleBo.getId());
        getBusinessObjectService().save(falseActionAgendaItemBo);
        existingAgendaItemBo.setWhenFalse(falseActionAgendaItemBo);
        getBusinessObjectService().save(existingAgendaItemBo);
        return existingAgendaItemBo;

    }

    /**
     *  This method adds the agendaName,docType,incomingField,existingField into MatchBo.
     * @param agendaName
     * @param termName
     * @param docType
     * @param incomingField
     * @param existingField
     * @return  matchBo
     */
    public MatchBo addMatchBo(String agendaName, String termName, String docType, String incomingField, String existingField) {
        MatchBo matchBo = new MatchBo();
        matchBo.setAgendaName(agendaName);
        matchBo.setDocType(docType);
        matchBo.setTermName(termName);
        matchBo.setIncomingField(incomingField);
        matchBo.setExistingField(existingField);
        getBusinessObjectService().save(matchBo);
        return matchBo;

    }

    /**
     *  This method returns Profile build from fileContent.
     * @param fileContent
     * @return  Profile
     * @throws java.net.URISyntaxException
     * @throws java.io.IOException
     */
    public Profile buildProfileFromFileContent(String fileContent) throws URISyntaxException, IOException {
        return getProfileObjectGeneratorFromXML().buildProfileFromFileContent(fileContent);
    }

    /**
     *  This method builds the profile based on fileContent.
     * @param fileName
     * @return  Profile
     * @throws java.net.URISyntaxException
     * @throws java.io.IOException
     * @throws org.xml.sax.SAXException
     */
    public Profile buildProfile(String fileName) throws URISyntaxException, IOException, SAXException {
        URL resource = getClass().getResource(fileName);
        File file = new File(resource.toURI());
        String fileContent = new FileUtil().readFile(file);
        if (validProfileXML(fileContent)) {
            return getProfileObjectGeneratorFromXML().buildProfileFromFileContent(fileContent);
        }
        return null;
    }

    /**
     *  This method validates the XmlSchema.
     * @param fileContent
     * @return  boolean
     * @throws java.io.IOException
     * @throws org.xml.sax.SAXException
     */
    private boolean validProfileXML(String fileContent) throws IOException, SAXException {
        return new ProfileXMLSchemaValidator().validateContentsAgainstSchema(null);
    }

    /**
     * This method persists the KRMSProfile got from fileContent into the database.
     * @param fileContent
     * @return  name
     * @throws java.io.IOException
     * @throws java.net.URISyntaxException
     */
    public String persistKRMSProfileFromFileContent(String fileContent) throws IOException, URISyntaxException {
        Profile profile = getProfileObjectGeneratorFromXML().buildProfileFromFileContent(fileContent);
        return persist(profile);

    }

    /**
     * This method persists the KRMSProfile into database.
     * @param profileFileName
     * @return  name
     * @throws java.io.IOException
     * @throws java.net.URISyntaxException
     * @throws org.xml.sax.SAXException
     */
    public String persistKRMSProfile(String profileFileName) throws IOException, URISyntaxException, SAXException {
        Profile profile = buildProfile(profileFileName);
        return persist(profile);
    }

    /**
     *  This method  creates context,category,rules,preposition,term and action for creating agenda and finally persisted into database.
     * @param profile
     * @return name
     */
    public String persist(Profile profile) {
        Boolean profileExists = doesProfileExist(profile.getName());
        if (profileExists) {
            deleteAgenda(profile);
        }
        ContextBo context = createContext(OLEConstants.OLE_NAMESPACE, profile.getContextName());
        CategoryBo category = createCategory(OLEConstants.OLE_NAMESPACE, profile.getCategoryName());

        registerDefaultTermResolvers(Arrays.asList(context), Arrays.asList(category));
        registerDefaultFunctionLoader();

        ContextValidActionBo contextValidActionBo = registerDefaultServiceTypes(context);


        AgendaBo agendaBo = createAgenda(profile.getName(), context);

        List<OleRule> rules = profile.getRules();
        for (Iterator<OleRule> iterator = rules.iterator(); iterator.hasNext(); ) {
            OleRule rule = iterator.next();

            TermSpecificationBo termSpecification =
                    createTermSpecification(OLEConstants.OLE_NAMESPACE, rule.getTerm(), "org.kuali.ole.ProfileTerm", Arrays.asList(category), Arrays.asList(context));

            TermBo termBo = createTerm(rule.getTerm(), termSpecification);

            RuleBo ruleBo = createRule(OLEConstants.OLE_NAMESPACE, rule.getName());

            addRuleToAgenda(agendaBo, ruleBo);

            PropositionBo propositionBo = createProposition(category.getId(), rule.getTerm() + " check", "S", ruleBo);

            FunctionBo function = createFunction(OLEConstants.OLE_NAMESPACE, rule.getTerm(), "java.lang.Boolean", Arrays.asList(category));

            addTermToPropositionParameter(termBo.getId(), propositionBo);

            addCustomFunctionToPropositionParameter(function.getId(), propositionBo);

            List<OleAction> trueActions = rule.getTrueActions();
            for (Iterator<OleAction> oleActionIterator = trueActions.iterator(); oleActionIterator.hasNext(); ) {
                OleAction trueAction = oleActionIterator.next();
                addActionToRule(OLEConstants.OLE_NAMESPACE, trueAction.getName(), trueAction.getDescription(), contextValidActionBo.getActionTypeId(), ruleBo, trueAction.getSequenceNumber());
            }

            List<OleAction> falseActions = rule.getFalseActions();
            for (Iterator<OleAction> oleActionIterator = falseActions.iterator(); oleActionIterator.hasNext(); ) {
                OleAction falseAction = oleActionIterator.next();
                RuleBo falseActionRuleBo = createRule(OLEConstants.OLE_NAMESPACE, rule.getName() + "_falseActionRule");
                addActionToRule(OLEConstants.OLE_NAMESPACE, falseAction.getName(), falseAction.getDescription(), contextValidActionBo.getActionTypeId(), falseActionRuleBo, falseAction.getSequenceNumber());
                addDummyRuleForFalseAction(ruleBo, falseActionRuleBo);
            }
            persistMatchBo(agendaBo, rule);
        }
        persistProfileAttributes(profile);
        return agendaBo.getName();
    }

    /**
     *   This method creates function loader type in the database for creating custom function.
     */
    public void registerDefaultFunctionLoader() {
        if (null == objectExists(KrmsTypeBo.class, "nm", "FunctionLoader")) {
            KrmsTypeBo krmsTypeBo = new KrmsTypeBo();
            krmsTypeBo.setNamespace(OLEConstants.OLE_NAMESPACE);
            krmsTypeBo.setName("FunctionLoader");
            krmsTypeBo.setServiceName("functionLoader");
            getBusinessObjectService().save(krmsTypeBo);
        }
    }

    /**
     *   This method creates the termResolver for ExistingField and IncomingField based on category and context.
     * @param contextBos
     * @param categoryBos
     */
    public void registerDefaultTermResolvers(List<ContextBo> contextBos, List<CategoryBo> categoryBos) {
        List list = new ArrayList();
        KrmsTypeBo termResolverTypeService = createTermResolverTypeService();

        TermSpecificationBo termSpecificationBo = createTermSpecification(OLEConstants.OLE_NAMESPACE, OLEConstants.EXISTING_FIELD, "java.lang.String", categoryBos, contextBos);
        createTerm(OLEConstants.EXISTING_FIELD, termSpecificationBo);

        TermResolverBo termResolverBoForExistinField = null;
        if (null == objectExists(TermResolverBo.class, "nm", OLEConstants.EXISTING_FIELD)) {
            termResolverBoForExistinField = new TermResolverBo();
            termResolverBoForExistinField.setNamespace(OLEConstants.OLE_NAMESPACE);
            termResolverBoForExistinField.setName(OLEConstants.EXISTING_FIELD);
            termResolverBoForExistinField.setOutputId(termSpecificationBo.getId());
            termResolverBoForExistinField.setTypeId(termResolverTypeService.getId());
            list.add(termResolverBoForExistinField);
        }

        TermSpecificationBo termSpecificationBo1 = createTermSpecification(OLEConstants.OLE_NAMESPACE, OLEConstants.INCOMING_FIELD, "java.lang.String", categoryBos, contextBos);
        createTerm(OLEConstants.INCOMING_FIELD, termSpecificationBo1);

        TermResolverBo termResolverBoForIncomingField = null;
        if (null == objectExists(TermResolverBo.class, "nm", OLEConstants.INCOMING_FIELD)) {
            termResolverBoForIncomingField = new TermResolverBo();
            termResolverBoForIncomingField.setNamespace(OLEConstants.OLE_NAMESPACE);
            termResolverBoForIncomingField.setName(OLEConstants.INCOMING_FIELD);
            termResolverBoForIncomingField.setTypeId(termResolverTypeService.getId());
            termResolverBoForIncomingField.setOutputId(termSpecificationBo1.getId());
            getBusinessObjectService().save(termResolverBoForIncomingField);
            list.add(termResolverBoForIncomingField);
        }

        if (!list.isEmpty()) {
            getBusinessObjectService().save(list);
        }
    }

    /**
     *  This method persists the profile attributes in to database.
     * @param profile
     */
    private void persistProfileAttributes(Profile profile) {
        List<ProfileAttributeBo> profileAttributes = profile.getProfileAttributes();
        for (Iterator<ProfileAttributeBo> iterator = profileAttributes.iterator(); iterator.hasNext(); ) {
            ProfileAttributeBo profileAttributeBo = iterator.next();
            profileAttributeBo.setAgendaName(profile.getName());
        }
        getBusinessObjectService().save(profileAttributes);
    }

    /**
     * This method persists the agenda and rule into database.
     * @param agendaBo
     * @param rule
     */
    private void persistMatchBo(AgendaBo agendaBo, OleRule rule) {
        MatchBo matchBo = new MatchBo();
        matchBo.setAgendaName(agendaBo.getName());
        matchBo.setDocType(rule.getDocType());
        String incomingField = rule.getIncomingField().getField() + rule.getIncomingField().getSubfield();
        String existingField = rule.getExistingField().getField() + rule.getExistingField().getSubfield();
        matchBo.setIncomingField(incomingField);
        matchBo.setExistingField(existingField);
        matchBo.setTermName(rule.getTerm());
        getBusinessObjectService().save(matchBo);
    }

    /**
     *  This method returns the  object
     * @param objectClass
     * @param key
     * @param value
     * @return  This method returns object if exists in db, if not returns null.
     */
    public Object objectExists(Class objectClass, String key, String value) {
        HashMap<String, String> map = new HashMap<String, String>();
        map.put(key, value);
        List<Object> matching = (List<Object>) getBusinessObjectService().findMatching(objectClass, map);
        return matching.isEmpty() ? null : matching.get(0);
    }

    /**
     * To clear Profile Data if Profile already Exists
     *
     * @param profile
     */
    public void deleteAgenda(Profile profile) {

        Map<String, String> contextPks = new HashMap<String, String>();
        contextPks.put("nm", profile.getContextName());
        contextPks.put("nmspc_cd", OLEConstants.OLE_NAMESPACE);
        ContextBo context = getBusinessObjectService().findByPrimaryKey(ContextBo.class, contextPks);
        String contextId = context.getId();

        Map<String, String> agendaPks = new HashMap<String, String>();
        agendaPks.put("nm", profile.getName());
        agendaPks.put("cntxt_id", contextId);
        AgendaBo oldAgenda = getBusinessObjectService().findByPrimaryKey(AgendaBo.class, agendaPks);

        Map<String, String> itemPks = new HashMap<String, String>();
        itemPks.put("agenda_id", oldAgenda.getId());
        List<AgendaItemBo> items = (List<AgendaItemBo>) getBusinessObjectService().findMatching(AgendaItemBo.class, itemPks);

        getBusinessObjectService().delete(items);

        getBusinessObjectService().delete(oldAgenda);

        List<AgendaBo> agendas = context.getAgendas();
        AgendaBo removeAgenda = null;
        for (AgendaBo agenda : agendas) {
            if (agenda.getName().equalsIgnoreCase(oldAgenda.getName())) {
                removeAgenda = agenda;
            }
        }
        agendas.remove(removeAgenda);
        context.setAgendas(agendas);
        getBusinessObjectService().save(context);

        deleteRegisteredRules(profile);
        unregisterDefaultTermResolvers();
        unregisterFunctionLoader();

        HashMap matchingProfileAttr = new HashMap();
        matchingProfileAttr.put("agenda_name", oldAgenda.getName());
        List<ProfileAttributeBo> profileAttributeBos =
                (List<ProfileAttributeBo>) getBusinessObjectService().findMatching(ProfileAttributeBo.class, matchingProfileAttr);
        getBusinessObjectService().delete(profileAttributeBos);

        HashMap matchingProfileFacts = new HashMap();
        matchingProfileFacts.put("agenda_name", oldAgenda.getName());
        List<MatchBo> matchBos =
                (List<MatchBo>) getBusinessObjectService().findMatching(MatchBo.class, matchingProfileFacts);
        getBusinessObjectService().delete(matchBos);
    }

    /**
     * This method deletes the term related rules from database.
     * @param profile
     */
    private void deleteRegisteredRules(Profile profile) {
        Map map = new HashMap();
        List<OleRule> rules = profile.getRules();
        for (Iterator<OleRule> iterator = rules.iterator(); iterator.hasNext(); ) {
            OleRule oleRule = iterator.next();
            String term = oleRule.getTerm();
            deleteTerm(term);

            map.put("nm", oleRule.getName());
            List<RuleBo> matching = (List<RuleBo>) getBusinessObjectService().findMatching(RuleBo.class, map);
            getBusinessObjectService().delete(matching);
        }
    }

    /**
     *  This method deletes the term record from database.
     * @param term
     */
    private void deleteTerm(String term) {
        Map map = new HashMap();
        map.put("desc_txt", term);
        List<TermBo> termsToBeDeleted = (List<TermBo>) getBusinessObjectService().findMatching(TermBo.class, map);
        if (!termsToBeDeleted.isEmpty()) {
            getBusinessObjectService().delete(termsToBeDeleted);
        }
//        List<TermSpecificationBo> termSpecificationBosToBeDeleted =
//                (List<TermSpecificationBo>) getBusinessObjectService().findMatching(TermSpecificationBo.class, map);
//        for (Iterator<TermSpecificationBo> iterator = termSpecificationBosToBeDeleted.iterator(); iterator.hasNext(); ) {
//            TermSpecificationBo termSpecificationBo = iterator.next();
//            Map categoryBoMap = new HashMap();
//            categoryBoMap.put("term_spec_id", termSpecificationBo.getId());
//            List<CategoryBo> categoryBosToBeDeleted =
//                    (List<CategoryBo>) getBusinessObjectService().findMatching(CategoryBo.class, categoryBoMap);
//            getBusinessObjectService().delete(categoryBosToBeDeleted);
//        }
//        getBusinessObjectService().delete(termSpecificationBosToBeDeleted);
    }

    /**
     *   This method deletes the functionLoader record from database.
     */
    private void unregisterFunctionLoader() {
        HashMap map = new HashMap();
        map.put("nm", "functionLoader");
        List<KrmsTypeBo> matching = (List<KrmsTypeBo>) getBusinessObjectService().findMatching(KrmsTypeBo.class, map);
        if (!matching.isEmpty()) {
            getBusinessObjectService().delete(matching);
        }
    }

    /**
     *   This method deletes the incomingField and existingField record from database .
     */
    private void unregisterDefaultTermResolvers() {
        Map incomingTermMap = new HashMap();
        incomingTermMap.put("desc_txt", OLEConstants.INCOMING_FIELD);
        getBusinessObjectService().
                delete((List<? extends PersistableBusinessObject>) getBusinessObjectService().
                        findMatching(TermBo.class, incomingTermMap));

        Map existingTermMap = new HashMap();
        existingTermMap.put("desc_txt", OLEConstants.EXISTING_FIELD);
        getBusinessObjectService().
                delete((List<? extends PersistableBusinessObject>) getBusinessObjectService().
                        findMatching(TermBo.class, existingTermMap));


        HashMap map = new HashMap();
        map.put("nm", "Create Bibliographic Record");
        List<KrmsTypeBo> matching = (List<KrmsTypeBo>) getBusinessObjectService().findMatching(KrmsTypeBo.class, map);
        if (!matching.isEmpty()) {
            getBusinessObjectService().delete(matching);
        }
//
//        HashMap map1 = new HashMap();
//        map1.put("nm", "ProfileTermResolver");
//        List<KrmsTypeBo> matching1 = (List<KrmsTypeBo>) getBusinessObjectService().findMatching(KrmsTypeBo.class, map1);
//        if (!matching1.isEmpty()) {
//            getBusinessObjectService().delete(matching1);
//        }
    }

    /**
     *  This method returns True/False.
     *  This method checks the existence of profileName with the help of BusinessObjectService.
     * @param profileName
     * @return  Boolean
     */
    public Boolean doesProfileExist(String profileName) {
        HashMap<String, String> map = new HashMap<String, String>();
        map.put("nm", profileName);
        List<AgendaBo> matching = (List<AgendaBo>) getBusinessObjectService().findMatching(AgendaBo.class, map);
        return !matching.isEmpty();
    }

    /**
     * Get the businessObjectService attribute.
     *  if businessObjectService is null it will create a new instance else it will return existing instance.
     * @return businessObjectService
     */
    private BusinessObjectService getBusinessObjectService() {
        if (null == businessObjectService) {
            businessObjectService = KRADServiceLocator.getBusinessObjectService();
        }
        return businessObjectService;
    }

    /**
     *   Gets the termBoService attribute.
     *   if termBoService is null it will create a new instance else it will return existing instance.
     * @return termBoService
     */
    private TermBoService getTermBoService() {
        if (null == termBoService) {
            termBoService = GlobalResourceLoader.getService("termBoService");
        }
        return termBoService;
    }

    /**
     *  Gets the profileObjectGeneratorFromXML attribute.
     *  if profileObjectGeneratorFromXML is null it will create a new instance else it will return existing instance.
     * @return  profileObjectGeneratorFromXML
     */
    public ProfileObjectGeneratorFromXML getProfileObjectGeneratorFromXML() {
        if (null == profileObjectGeneratorFromXML) {
            profileObjectGeneratorFromXML = new ProfileObjectGeneratorFromXML();
        }
        return profileObjectGeneratorFromXML;
    }

    /**
     * Sets the profileObjectGeneratorFromXML attribute value.
     * @param profileObjectGeneratorFromXML
     */
    public void setProfileObjectGeneratorFromXML(ProfileObjectGeneratorFromXML profileObjectGeneratorFromXML) {
        this.profileObjectGeneratorFromXML = profileObjectGeneratorFromXML;
    }
}
