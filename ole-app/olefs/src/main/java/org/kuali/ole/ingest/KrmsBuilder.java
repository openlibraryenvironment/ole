package org.kuali.ole.ingest;

import org.kuali.ole.OLEConstants;
import org.kuali.ole.ingest.pojo.*;
import org.kuali.rice.core.api.resourceloader.GlobalResourceLoader;
import org.kuali.rice.kew.impl.peopleflow.PeopleFlowBo;
import org.kuali.rice.krad.service.BusinessObjectService;
import org.kuali.rice.krad.service.KRADServiceLocator;
import org.kuali.rice.krms.impl.repository.*;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.*;

/**
 * KrmsBuilder will create context,category and rules for creating agenda and finally persists into database.
 */
public class KrmsBuilder {

    private BusinessObjectService businessObjectService;
    private TermBoService termBoService;
    private KrmsObjectGeneratorFromXML krmsObjectGeneratorFromXML;
    private ProfileBuilder profileBuilder = new ProfileBuilder();
    private CategoryBo category;
    private ContextBo context;
    private AgendaBo agendaBo;
    private RuleBo ruleBo;

    /**
     * This method returns the contextBo object based on the namespace and contextName parameters.
     * Creates a new object, stores it in database and returns if none exists, otherwise returns the existing object.
     * @param namespace
     * @param contextName
     * @return  contextBo
     */
    public ContextBo createContext(String namespace, String contextName) {

        return profileBuilder.createContext(namespace,contextName);
    }

    /**
     * This method returns the Object value if it exists or else null value based on objectClass, key and value parameters.
     * @param objectClass
     * @param key
     * @param value
     * @return object
     */
    public Object objectExists(Class objectClass, String key, String value) {

        return profileBuilder.objectExists(objectClass,key,value);
    }

    /**
     * Returns the businessObjectService value
     * @return businessObjectService
     */
    private BusinessObjectService getBusinessObjectService() {
        if (null == businessObjectService) {
            businessObjectService = KRADServiceLocator.getBusinessObjectService();
        }
        return businessObjectService;
    }

    /**
     * This method returns the categoryBo object based on the namespace and the categoryName parameters.
     * Creates a new object, stores it in database and returns if none exists, otherwise returns the existing object.
     * @param namespace
     * @param categoryName
     * @return categoryBo
     */
    public CategoryBo createCategory(String namespace, String categoryName) {

        return profileBuilder.createCategory(namespace,categoryName);
    }

    /**
     * This method save the DefaultFunctionLoader into the database.
     */
    public void registerDefaultFunctionLoader() {
        profileBuilder.registerDefaultFunctionLoader();
    }

    /**
     * This method returns the agendaBo object based on the agendaName and contextBo parameters.
     * Creates a new object, stores it in database and returns if none exists, otherwise returns the existing object.
     * @param agendaName
     * @param contextBo
     * @return agendaBo
     */
    public AgendaBo createAgenda(String agendaName, ContextBo contextBo) {

        return profileBuilder.createAgenda(agendaName, contextBo);
    }

    /**
     * This method returns the termSpecificationBo object based on the namespace, termSpecificationName, type, categoryBoList and contextBoList parameters.
     * Creates a new object, stores it in database and returns if none exists, otherwise returns the existing object.
     * @param namespace
     * @param termSpecificationName
     * @param type
     * @param categoryBoList
     * @param contextBoList
     * @return termSpecificationBo
     */
    public TermSpecificationBo createTermSpecification(String namespace, String termSpecificationName, String type, List<CategoryBo> categoryBoList, List<ContextBo> contextBoList) {

        return profileBuilder.createTermSpecification(namespace,termSpecificationName,type,categoryBoList,contextBoList);
    }

    /**
     * This method  returns the termBoService object through the GlobalResourceLoader class.
     * @return termBoService
     */
    private TermBoService getTermBoService() {
        if (null == termBoService) {
            termBoService = GlobalResourceLoader.getService("termBoService");
        }
        return termBoService;
    }

    /**
     * This method returns the termBo object based on the name and termSpecificationBo parameters
     * Creates a new object, stores it in database and returns if none exists, otherwise returns the existing object.
     * @param name
     * @param termSpecificationBo
     * @return termBo
     */
    public TermBo createTerm(String name, TermSpecificationBo termSpecificationBo) {

        return profileBuilder.createTerm(name,termSpecificationBo);
    }

    /**
     * This method returns the ruleBo object based on the name and termSpecificationBo parameters
     * Creates a new object, stores it in database and returns if none exists, otherwise returns the existing object.
     * @param namespace
     * @param ruleName
     * @return ruleBo
     */
    public RuleBo createRule(String namespace, String ruleName) {

        return profileBuilder.createRule(namespace, ruleName);
    }

    /**
     * This method returns the agendaBo object based on the agendaBo, ruleBo and count parameters
     * Creates a new object, stores it in database and returns if none exists, otherwise returns the existing object.
     * @param agendaBo
     * @param ruleBo
     * @param count
     * @return agendaBo
     */
    public AgendaBo addRuleToAgenda(AgendaBo agendaBo, RuleBo ruleBo,int count) {
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
        if(count>-1){
            AgendaItemBo updateAgendaItem = agendaBoItems.get(count);
            updateAgendaItem.setAlways(agendaItemBo);
            getBusinessObjectService().save(updateAgendaItem);
        }
        getBusinessObjectService().save(agendaBo);
        return agendaBo;
    }

    /**
     * This method returns the propositionBo object based on the categoryId, propositionDescription, propositionType, rule and seqNum parameters
     * Creates a new object, stores it in database and returns the object.
     * @param categoryId
     * @param propositionDescription
     * @param propositionType
     * @param rule
     * @param seqNum
     * @return propositionBo
     */
    public PropositionBo createProposition(String categoryId, String propositionDescription, String propositionType, RuleBo rule,Integer seqNum) {
        PropositionBo propositionBo = new PropositionBo();
        propositionBo.setCategoryId(categoryId);
        propositionBo.setDescription(propositionDescription);
        propositionBo.setRuleId(rule.getId());
        propositionBo.setPropositionTypeCode(propositionType);
        propositionBo.setCompoundSequenceNumber(seqNum);
        getBusinessObjectService().save(propositionBo);
        return propositionBo;
    }

    /**
     * This method returns the propositionBo object based on the categoryId, propositionDescription, propositionType, rule and operatorCode parameters
     * Creates a new object, stores it in database and returns the object.
     * @param categoryId
     * @param propositionDescription
     * @param propositionType
     * @param rule
     * @param operatorCode
     * @return propositionBo
     */
    public PropositionBo createCompoundProposition(String categoryId, String propositionDescription, String propositionType, RuleBo rule,String operatorCode) {
        PropositionBo propositionBo = new PropositionBo();
        propositionBo.setCategoryId(categoryId);
        propositionBo.setDescription(propositionDescription);
        propositionBo.setRuleId(rule.getId());
        propositionBo.setPropositionTypeCode(propositionType);
        propositionBo.setCompoundOpCode(operatorCode);
        getBusinessObjectService().save(propositionBo);
        return propositionBo;
    }

    /**
     * This method returns the functionBo object based on the namespace, termName, returnType, categories and parameterType parameters
     * Creates a new object, stores it in database and returns if none exists, otherwise returns the existing object.
     * @param namespace
     * @param termName
     * @param returnType
     * @param categories
     * @param parameterType
     * @return functionBo
     */
    public FunctionBo createFunction(String namespace, String termName, String returnType, List categories,String parameterType) {
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
            functionParameterBo.setParameterType(parameterType);
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
     * This method returns the PropositionBo object based on the termId and proposition parameters
     * Creates a new object, stores it in database and returns the object.
     * @param termId
     * @param proposition
     * @return  PropositionBo
     */
    public PropositionBo addTermToPropositionParameter(String termId, PropositionBo proposition) {

        return profileBuilder.addTermToPropositionParameter(termId,proposition);
    }

    /**
     * This method returns the PropositionBo object based on the operator and proposition parameters
     * Creates a new object, stores it in database and returns the object.
     * @param operator
     * @param proposition
     * @return  proposition
     */
    public PropositionBo addOperatorToPropositionParameter(String operator,PropositionBo proposition){
        PropositionParameterBo propositionParameterBo = new PropositionParameterBo();
        propositionParameterBo.setPropId(proposition.getId());
        propositionParameterBo.setValue(operator);
        propositionParameterBo.setSequenceNumber(2);
        propositionParameterBo.setParameterType("O");
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
     * This method returns the PropositionBo object based on the constant and proposition parameters
     * Creates a new object, stores it in database and returns the object.
     * @param constant
     * @param proposition
     * @return  proposition
     */
    public PropositionBo addConstantToPropositionParameter(String constant,PropositionBo proposition){
        PropositionParameterBo propositionParameterBo = new PropositionParameterBo();
        propositionParameterBo.setPropId(proposition.getId());
        propositionParameterBo.setValue(constant);
        propositionParameterBo.setSequenceNumber(1);
        propositionParameterBo.setParameterType("C");
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
     * This method returns the PropositionBo object based on the functionId and proposition parameters
     * Creates a new object, stores it in database and returns the object.
     * @param functionId
     * @param proposition
     * @return PropositionBo
     */
    public PropositionBo addCustomFunctionToPropositionParameter(String functionId, PropositionBo proposition) {

        return profileBuilder.addCustomFunctionToPropositionParameter(functionId,proposition);
    }

    /**
     * This method will create context,category and rules for creating agenda and finally persists into database.
     * @param oleAgenda
     * @return  agendaName
     */
    public String persist(OleAgenda oleAgenda) {
        boolean agendaExists = doesKrmsExist(oleAgenda.getName());
        if(agendaExists){
            deleteAgenda(oleAgenda);
        }
        context = createContext(OLEConstants.OLE_NAMESPACE, oleAgenda.getContextName());
        category = createCategory(OLEConstants.OLE_NAMESPACE, oleAgenda.getCategoryName());

        registerDefaultFunctionLoader();

        ContextValidActionBo contextValidActionBo = registerDefaultServiceTypes(context);

        agendaBo = createAgenda(oleAgenda.getName(), context);

        List<KrmsRule> rules = oleAgenda.getRules();
        int count = -1;
        for (Iterator<KrmsRule> iterator = rules.iterator(); iterator.hasNext(); ) {
            KrmsRule rule = iterator.next();
            ruleBo = createRule(OLEConstants.OLE_NAMESPACE, rule.getName());

            addRuleToAgenda(agendaBo, ruleBo,count);

            count++;
            PropositionBo propositionBo =  null;

            if(rule.getProposition()!=null){
                propositionBo = createSimpleProposition(rule.getProposition(),1);
            }
            if(rule.getCompoundProposition()!=null){
                OleProposition oleProposition = rule.getCompoundProposition();
                List<PropositionBo> propositionBos = new ArrayList<PropositionBo>();
                propositionBos.addAll(createCompoundProposition(oleProposition, propositionBos));
                propositionBo = propositionBos.get(0);
            }
            ruleBo.setProposition(propositionBo);
            getBusinessObjectService().save(ruleBo);
            List<KrmsAction> trueActions =rule.getTrueActions()!=null && rule.getTrueActions().size()>0 ? rule.getTrueActions():new ArrayList<KrmsAction>();
            for (Iterator<KrmsAction> oleActionIterator = trueActions.iterator(); oleActionIterator.hasNext(); ) {
                KrmsAction trueAction = oleActionIterator.next();
                contextValidActionBo = getContextValidActionBo(context,trueAction);
                ActionBo actionBo = addActionToRule(OLEConstants.OLE_NAMESPACE, trueAction.getName(), trueAction.getDescription(), contextValidActionBo.getActionTypeId(), ruleBo, trueAction.getSequenceNumber());
                addActionAttributes(actionBo,trueAction.getName());
            }

            List<KrmsAction> falseActions = rule.getFalseActions()!=null && rule.getFalseActions().size()>0 ? rule.getFalseActions():new ArrayList<KrmsAction>();
            for (Iterator<KrmsAction> oleActionIterator = falseActions.iterator(); oleActionIterator.hasNext(); ) {
                KrmsAction falseAction = oleActionIterator.next();
                RuleBo falseActionRuleBo = createRule(OLEConstants.OLE_NAMESPACE, rule.getName() + "_falseActionRule");
                addActionToRule(OLEConstants.OLE_NAMESPACE, falseAction.getName(), falseAction.getDescription(), contextValidActionBo.getActionTypeId(), falseActionRuleBo, falseAction.getSequenceNumber());
                addDummyRuleForFalseAction(ruleBo, falseActionRuleBo);
            }

        }
        persistProfileAttributes(oleAgenda);
        return agendaBo.getName();
    }

    /**
     * This method creates compound proposition from the oleProposition and propositionBos parameters
     * @param oleProposition
     * @param propositionBos
     */
    private List<PropositionBo> createCompoundProposition(OleProposition oleProposition, List<PropositionBo> propositionBos){

        if(oleProposition.getPropositions()!=null){
            List<PropositionBo> propositionBoList = new ArrayList<PropositionBo>();
            propositionBoList.add(createCompoundProposition(oleProposition.getPropositions(), oleProposition.getPropositionType()));
            return propositionBoList;
        } else if(oleProposition.getOlePropositions()!=null){
            PropositionBo propositionBo =  null;
            List<PropositionBo> propositionBoList = new ArrayList<PropositionBo>();
            for(OleProposition oleProposition1 : oleProposition.getOlePropositions()){
                propositionBoList.addAll(createCompoundProposition(oleProposition1, propositionBoList));
            }
            propositionBo =  createCompoundProposition(category.getId(),"compound","C",ruleBo,oleProposition.getPropositionType());
            propositionBo.setCompoundComponents(propositionBoList);
            getBusinessObjectService().save(propositionBo);
            propositionBoList = new ArrayList<PropositionBo>();
            propositionBoList.add(propositionBo);
            return propositionBoList;
        }
        return null;
    }

    /**
     * This method adds the action attributes to create the agenda.
     * @param action
     * @param peopleFlowName
     */
    private void addActionAttributes(ActionBo action,String peopleFlowName){
        Map<String, String> peopleFlowPk = new HashMap<String, String>();
        peopleFlowPk.put("nm", peopleFlowName);
        List<PeopleFlowBo> peopleFlowBos =(List<PeopleFlowBo>) getBusinessObjectService().findMatching(PeopleFlowBo.class, peopleFlowPk);
        if(peopleFlowBos!=null && peopleFlowBos.size()>0){
            Map<String, String> attrDefPk = new HashMap<String, String>();
            attrDefPk.put("attr_defn_id", "1000");
            KrmsAttributeDefinitionBo peopleFlowIdDef = getBusinessObjectService().findByPrimaryKey(KrmsAttributeDefinitionBo.class,attrDefPk);

            attrDefPk = new HashMap<String, String>();
            attrDefPk.put("attr_defn_id", "1006");
            KrmsAttributeDefinitionBo peopleFlowNameDef = getBusinessObjectService().findByPrimaryKey(KrmsAttributeDefinitionBo.class,attrDefPk);

            Set<ActionAttributeBo> actionAttributeBos = new HashSet<ActionAttributeBo>();
            String peopleFlowId =  peopleFlowBos.get(0).getId();
            ActionAttributeBo actionAttributeBo = new ActionAttributeBo();
            actionAttributeBo.setActionId(action.getId());
            actionAttributeBo.setAttributeDefinitionId(peopleFlowIdDef.getId());
            actionAttributeBo.setAttributeDefinition(peopleFlowIdDef);
            actionAttributeBo.setValue(peopleFlowId);
            getBusinessObjectService().save(actionAttributeBo);
            actionAttributeBos.add(actionAttributeBo);

            actionAttributeBo = new ActionAttributeBo();
            actionAttributeBo.setActionId(action.getId());
            actionAttributeBo.setAttributeDefinitionId(peopleFlowNameDef.getId());
            actionAttributeBo.setAttributeDefinition(peopleFlowNameDef);
            actionAttributeBo.setValue(peopleFlowName);
            getBusinessObjectService().save(actionAttributeBo);
            actionAttributeBos.add(actionAttributeBo);

            action.setAttributeBos(actionAttributeBos);
            getBusinessObjectService().save(action);

        }

    }

    /**
     * This method returns the contextValidActionBo object based on the contextBo and action parameters
     * Creates a new object, stores it in database and returns if none exists, otherwise returns the existing object.
     * @param contextBo
     * @param action
     * @return  contextValidActionBo
     */
    private ContextValidActionBo getContextValidActionBo(ContextBo contextBo,KrmsAction action){
        KrmsTypeBo actionTypeService = getActiontypeService(action);
        ContextValidActionBo contextValidActionBo =null;
        if(actionTypeService!=null){
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
        }
        if(contextValidActionBo==null){
            contextValidActionBo = registerDefaultServiceTypes(contextBo);
        }
        return contextValidActionBo;
    }

    /**
     * Lookups the database for the particular Servicename and the returns the krmsTypeBo object.
     * @param action
     * @return  krmsTypeBo
     */
    private KrmsTypeBo  getActiontypeService(KrmsAction action){
        Map<String, String> krmsTypePk = new HashMap<String, String>();
        krmsTypePk.put("srvc_nm", action.getServiceName());
        KrmsTypeBo krmsTypeBo = getBusinessObjectService().findByPrimaryKey(KrmsTypeBo.class, krmsTypePk);
        return  krmsTypeBo;
    }

    /**
     * This method persists the matchBo object based on the agendaBo and proposition parameters..
     * @param agendaBo
     * @param proposition
     */
    private void persistMatchBo(AgendaBo agendaBo, KrmsProposition proposition) {
        MatchBo matchBo = new MatchBo();
        matchBo.setAgendaName(agendaBo.getName());
        matchBo.setTermName(proposition.getTerm());
        getBusinessObjectService().save(matchBo);
    }

    /**
     * This method builds the Krms object from the fileContent parameter and the returns the list of agendaNames from the Krms object.
     * @param fileContent
     * @return  agendaNames
     * @throws java.io.IOException
     * @throws java.net.URISyntaxException
     */
    public List<String> persistKrmsFromFileContent(String fileContent) throws IOException, URISyntaxException {
        Krms krms = getKrmsObjectGeneratorFromXML().buildKrmsFromFileContent(fileContent);
        List<String> agendaNames = new ArrayList<String>();
        for(OleAgenda oleAgenda :krms.getOleAgendaList()){
            agendaNames.add(persist(oleAgenda));
        }
        return agendaNames;

    }

    /**
     * Gets the krmsObjectGeneratorFromXML object if exists, otherwise it creates a new object.
     * @return  krmsObjectGeneratorFromXML
     */
    public KrmsObjectGeneratorFromXML getKrmsObjectGeneratorFromXML() {
        if(null==krmsObjectGeneratorFromXML){
            krmsObjectGeneratorFromXML = new KrmsObjectGeneratorFromXML();            krmsObjectGeneratorFromXML = new KrmsObjectGeneratorFromXML();
        }
        return krmsObjectGeneratorFromXML;
    }

    /**
     * Sets the krmsObjectGeneratorFromXML value.
     * @param krmsObjectGeneratorFromXML
     */
    public void setKrmsObjectGeneratorFromXML(KrmsObjectGeneratorFromXML krmsObjectGeneratorFromXML) {
        this.krmsObjectGeneratorFromXML = krmsObjectGeneratorFromXML;
    }

    /**
     * Lookups the database based on the agendaName and returns a boolean value.
     * @param agendaName
     * @return  Boolean
     */
    public Boolean doesKrmsExist(String agendaName) {
        HashMap<String, String> map = new HashMap<String, String>();
        map.put("nm", agendaName);
        List<AgendaBo> matching = (List<AgendaBo>) getBusinessObjectService().findMatching(AgendaBo.class, map);
        return !matching.isEmpty();
    }

    /**
     * This method deletes the term record from database.
     * @param oleAgenda
     */
    public void deleteAgenda(OleAgenda oleAgenda) {

        Map<String, String> contextPks = new HashMap<String, String>();
        contextPks.put("nm", oleAgenda.getContextName());
        contextPks.put("nmspc_cd", OLEConstants.OLE_NAMESPACE);
        ContextBo context = getBusinessObjectService().findByPrimaryKey(ContextBo.class, contextPks);
        String contextId = context.getId();

        Map<String, String> agendaPks = new HashMap<String, String>();
        agendaPks.put("nm", oleAgenda.getName());
        agendaPks.put("cntxt_id", contextId);
        AgendaBo oldAgenda = getBusinessObjectService().findByPrimaryKey(AgendaBo.class, agendaPks);

        Map<String, String> itemPks = new HashMap<String, String>();
        itemPks.put("agenda_id", oldAgenda.getId());
        List<AgendaItemBo> items = (List<AgendaItemBo>) getBusinessObjectService().findMatching(AgendaItemBo.class, itemPks);
        for(AgendaItemBo agendaItemBo : items){
            getBusinessObjectService().delete(agendaItemBo);
        }
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

        deleteRegisteredRules(oleAgenda);

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
     *  This method deletes the term related rules from database.
     * @param oleAgenda
     */
    private void deleteRegisteredRules(OleAgenda oleAgenda) {
        Map map = new HashMap();
        List<KrmsRule> rules = oleAgenda.getRules();
        for (Iterator<KrmsRule> iterator = rules.iterator(); iterator.hasNext(); ) {
            KrmsRule krmsRule = iterator.next();
            if(krmsRule.getProposition()!=null){
                String term = krmsRule.getProposition().getTerm();
                deleteTerm(term) ;
            }
            if(krmsRule.getCompoundProposition()!=null){
                deleteCompoundProposition(krmsRule.getCompoundProposition());
            }
            map.put("nm", krmsRule.getName());
            List<RuleBo> matching = (List<RuleBo>) getBusinessObjectService().findMatching(RuleBo.class, map);
            getBusinessObjectService().delete(matching);
        }
    }

    /**
     *  This method deletes the compound proposition based on the oleProposition from database.
     * @param oleProposition
     */
    private void deleteCompoundProposition(OleProposition oleProposition){
        for(OleProposition oleProposition1 : oleProposition.getOlePropositions()!=null?oleProposition.getOlePropositions():new ArrayList<OleProposition>()){
            deleteCompoundProposition(oleProposition1);
        }
        for(KrmsProposition krmsProposition : oleProposition.getPropositions()!=null?oleProposition.getPropositions():new ArrayList<KrmsProposition>()){
            String term = krmsProposition.getTerm();
            deleteTerm(term) ;
        }
    }

    /**
     *  This method deletes the term from database.
     * @param term
     */
    private void deleteTerm(String term) {
        Map map = new HashMap();
        map.put("desc_txt", term);
        List<TermBo> termsToBeDeleted = (List<TermBo>) getBusinessObjectService().findMatching(TermBo.class, map);
        if (!termsToBeDeleted.isEmpty()) {
            getBusinessObjectService().delete(termsToBeDeleted);
        }
    }

    /**
     * This method registers the default service types in the database for the context.
     * @param contextBo
     * @return  ContextValidActionBo
     */
    public ContextValidActionBo registerDefaultServiceTypes(ContextBo contextBo) {
        return profileBuilder.registerDefaultServiceTypes(contextBo);
    }

    /**
     * This method adds the action into rule to create the agenda.
     * @param namespace
     * @param actionName
     * @param description
     * @param actionTypeId
     * @param ruleBo
     * @param sequenceNumber
     * @return  ActionBo
     */
    public ActionBo addActionToRule(String namespace, String actionName, String description, String actionTypeId, RuleBo ruleBo, Integer sequenceNumber) {
        return profileBuilder.addActionToRule(namespace,actionName,description,actionTypeId,ruleBo,sequenceNumber);
    }

    /**
     * This method adds the dummy rule into database for falseAction.
     * @param ruleBo
     * @param falseActionRuleBo
     * @return  AgendaItemBo
     */
    public AgendaItemBo addDummyRuleForFalseAction(RuleBo ruleBo, RuleBo falseActionRuleBo) {
        return profileBuilder.addDummyRuleForFalseAction(ruleBo,falseActionRuleBo);
    }

    /**
     * This method persists the profile attributes in to database.
     * @param oleAgenda
     */
    private void persistProfileAttributes(OleAgenda oleAgenda) {
        List<ProfileAttributeBo> profileAttributes = oleAgenda.getProfileAttributes();
        for (Iterator<ProfileAttributeBo> iterator = profileAttributes.iterator(); iterator.hasNext(); ) {
            ProfileAttributeBo profileAttributeBo = iterator.next();
            profileAttributeBo.setAgendaName(oleAgenda.getName());
        }
        getBusinessObjectService().save(profileAttributes);
    }

    /**
     *  This method creates a compound proposition based on the propositions list and opCode parameters and returns the propositionBo.
     * @param propositions
     * @param opCode
     * @return  PropositionBo
     */
    private PropositionBo createCompoundProposition(List<KrmsProposition> propositions,String opCode){
        List<PropositionBo> propositionBos = new ArrayList<PropositionBo>();
        int seqNum = 0;
        for(KrmsProposition proposition : propositions){
            PropositionBo propositionBo = createSimpleProposition(proposition,seqNum);
            if(opCode==null){
                return propositionBo;
            }
            seqNum++;
            propositionBos.add(propositionBo);
        }
        PropositionBo propositionBo =  createCompoundProposition(category.getId(),"compound","C",ruleBo,opCode);
        propositionBo.setCompoundComponents(propositionBos);
        return propositionBo;
    }

    /**
     * This method creates a simple proposition based on the proposition and seqNum parameters and returns the propositionBo.
     * @param proposition
     * @param seqNum
     * @return PropositionBo
     */
    private PropositionBo createSimpleProposition(KrmsProposition proposition,int seqNum){
        TermSpecificationBo termSpecification =
                createTermSpecification(OLEConstants.OLE_NAMESPACE, proposition.getTerm(), proposition.getType(), Arrays.asList(category), Arrays.asList(context));

        TermBo termBo = createTerm(proposition.getTerm(), termSpecification);


        PropositionBo propositionBo = createProposition(category.getId(), proposition.getTerm() + " check", "S", ruleBo,seqNum);

        if(proposition.getFunction()!=null && proposition.getFunction().equalsIgnoreCase("yes")){

            FunctionBo function = createFunction(OLEConstants.OLE_NAMESPACE, proposition.getTerm(), "java.lang.Boolean", Arrays.asList(category),proposition.getType());

            addTermToPropositionParameter(termBo.getId(), propositionBo);

            addCustomFunctionToPropositionParameter(function.getId(), propositionBo);
        }   else{

            addTermToPropositionParameter(termBo.getId(), propositionBo);

            addConstantToPropositionParameter(proposition.getConstant(),propositionBo);

            addOperatorToPropositionParameter(proposition.getOperator(),propositionBo);
        }
        persistMatchBo(agendaBo, proposition);
        return propositionBo;
    }

    /**
     *  Gets the category attribute.
     * @return Returns the category
     */
    public CategoryBo getCategory() {
        return category;
    }

    /**
     *  Sets the category attribute value.
     * @param category .The category to set.
     */
    public void setCategory(CategoryBo category) {
        this.category = category;
    }

    /**
     * Gets the context attribute.
     * @return  Returns the context
     */
    public ContextBo getContext() {
        return context;
    }

    /**
     * Sets the Context attribute value.
     * @param context
     */
    public void setContext(ContextBo context) {
        this.context = context;
    }

    /**
     *  Gets the agendaBo attribute.
     * @return Returns agendaBo.
     */
    public AgendaBo getAgendaBo() {
        return agendaBo;
    }

    /**
     * Sets the agendaBo attribute value.
     * @param agendaBo
     */
    public void setAgendaBo(AgendaBo agendaBo) {
        this.agendaBo = agendaBo;
    }

    /**
     *  Gets the ruleBo attribute.
     * @return Returns ruleBo.
     */
    public RuleBo getRuleBo() {
        return ruleBo;
    }

    /**
     * Sets the ruleBo attribute value.
     * @param ruleBo
     */
    public void setRuleBo(RuleBo ruleBo) {
        this.ruleBo = ruleBo;
    }
}