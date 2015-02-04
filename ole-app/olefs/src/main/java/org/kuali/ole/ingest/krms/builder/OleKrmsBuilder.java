package org.kuali.ole.ingest.krms.builder;

import org.apache.log4j.Logger;
import org.kuali.ole.OLEConstants;
import org.kuali.ole.docstore.model.xmlpojo.work.bib.marc.DataField;
import org.kuali.ole.docstore.model.xmlpojo.work.bib.marc.SubField;
import org.kuali.ole.ingest.krms.pojo.*;
import org.kuali.ole.ingest.pojo.*;
import org.kuali.rice.core.api.resourceloader.GlobalResourceLoader;
import org.kuali.rice.kew.impl.peopleflow.PeopleFlowBo;
import org.kuali.rice.krad.service.BusinessObjectService;
import org.kuali.rice.krad.service.KRADServiceLocator;
import org.kuali.rice.krms.api.repository.term.TermSpecificationDefinition;
import org.kuali.rice.krms.impl.repository.*;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.*;

/**
 * Created with IntelliJ IDEA.
 * User: vivekb
 * Date: 8/22/12
 * Time: 3:04 PM
 * To change this template use File | Settings | File Templates.
 */
public class OleKrmsBuilder {

    private BusinessObjectService businessObjectService;
    private TermBoService termBoService;
    private OleKrmsObjectGeneratorFromXML krmsObjectGeneratorFromXML;
    private CategoryBo category;
    private ContextBo context;
    private AgendaBo agendaBo;
    private RuleBo ruleBo;
    private HashMap<String,String> operators;
    private static final Logger LOG = Logger.getLogger(OleKrmsBuilder.class);

    public OleKrmsBuilder() {
        operators = new HashMap<String, String>();
        operators.put("AND","&");
        operators.put("OR","|");
        operators.put("and","&");
        operators.put("or","|");
        operators.put("IN","=");
        operators.put("in","=");
        operators.put("=","=");
        operators.put("greaterThan",">");
        operators.put("lessThan","<");
        operators.put(">",">");
        operators.put("<","<");
        operators.put("!=","!=");
    }

    /**
     * This method returns the contextBo object based on the namespace and contextName parameters.
     * Creates a new object, stores it in database and returns if none exists, otherwise returns the existing object.
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
     * This method returns the Object value if it exists or else null value based on objectClass, key and value parameters.
     * @param objectClass
     * @param key
     * @param value
     * @return object
     */
    public Object objectExists(Class objectClass, String key, String value) {

        HashMap<String, String> map = new HashMap<String, String>();
        map.put(key, value);
        List<Object> matching = (List<Object>) getBusinessObjectService().findMatching(objectClass, map);
        return matching.isEmpty() ? null : matching.get(0);
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
     * This method save the DefaultFunctionLoader into the database.
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
     * This method returns the agendaBo object based on the agendaName and contextBo parameters.
     * Creates a new object, stores it in database and returns if none exists, otherwise returns the existing object.
     * @param agendaName
     * @param contextBo
     * @return agendaBo
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
     * This method returns the ruleBo object based on the name and termSpecificationBo parameters
     * Creates a new object, stores it in database and returns if none exists, otherwise returns the existing object.
     * @param namespace
     * @param ruleName
     * @return ruleBo
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
     * This method returns the agendaBo object based on the agendaBo, ruleBo and count parameters
     * Creates a new object, stores it in database and returns if none exists, otherwise returns the existing object.
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
        if(agendaBo.getFirstItemId()==null){
            agendaBo.setFirstItemId(agendaBoItems.get(0).getId());
            getBusinessObjectService().save(agendaBo);
        }
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
     * @param functionName
     * @param returnType
     * @param categories
     * @param parameterTypes
     * @return functionBo
     */
    public FunctionBo createFunction(String namespace, String functionName, String returnType, List categories,List<String> parameterTypes) {
        Object existing = objectExists(FunctionBo.class, "nm", functionName);
        FunctionBo functionBo;
        if (existing == null) {
            HashMap<String, String> map = new HashMap<String, String>();
            map.put("nm", "FunctionLoader");
            List<KrmsTypeBo> matching =
                    (List<KrmsTypeBo>) getBusinessObjectService().findMatching(KrmsTypeBo.class, map);
            KrmsTypeBo krmsTypeBo = matching.get(0);
            functionBo = new FunctionBo();
            functionBo.setActive(true);
            functionBo.setName(functionName);
            functionBo.setNamespace(namespace);
            functionBo.setReturnType(returnType);
            functionBo.setCategories(categories);
            functionBo.setTypeId(krmsTypeBo.getId());
            getBusinessObjectService().save(functionBo);
            List<FunctionParameterBo> parameters = functionBo.getParameters();
            if (null == parameters) {
                parameters = new ArrayList<FunctionParameterBo>();
            }
            FunctionParameterBo functionParameterBo = null;
            int i =0;
            for(String parameterType : parameterTypes){
                functionParameterBo = new FunctionParameterBo();
                functionParameterBo.setFunctionId(functionBo.getId());
                functionParameterBo.setParameterType(parameterType);
                functionParameterBo.setName(functionName);
                functionParameterBo.setSequenceNumber(i++);

                getBusinessObjectService().save(functionParameterBo);
                parameters.add(functionParameterBo);
            }
            functionBo.setParameters(parameters);

            getBusinessObjectService().save(functionBo);
        } else {
            functionBo = (FunctionBo) existing;
            List<FunctionParameterBo> parameters = functionBo.getParameters();
            if (null == parameters) {
                parameters = new ArrayList<FunctionParameterBo>();
            }
            FunctionParameterBo functionParameterBo = null;
            int j = 0;
            for(int i=0 ; i< parameterTypes.size(); i++){
                if(parameters.size() > i){
                    functionParameterBo = parameters.get(i);
                    functionParameterBo.setFunctionId(functionBo.getId());
                    functionParameterBo.setParameterType(parameterTypes.get(i));
                    functionParameterBo.setName(functionName);
                    functionParameterBo.setSequenceNumber(j++);

                    getBusinessObjectService().save(functionParameterBo);
                }else{
                    functionParameterBo =  new FunctionParameterBo();
                    functionParameterBo.setFunctionId(functionBo.getId());
                    functionParameterBo.setParameterType(parameterTypes.get(i));
                    functionParameterBo.setName(functionName);
                    functionParameterBo.setSequenceNumber(i++);

                    getBusinessObjectService().save(functionParameterBo);
                    parameters.add(functionParameterBo);
                }
            }
            functionBo.setParameters(parameters);

            getBusinessObjectService().save(functionBo);
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
    public PropositionBo addTermToPropositionParameter(String termId, PropositionBo proposition,Integer seqNum) {
        PropositionParameterBo propositionParameterBo = new PropositionParameterBo();
        propositionParameterBo.setPropId(proposition.getId());
        propositionParameterBo.setValue(termId);
        propositionParameterBo.setSequenceNumber(seqNum);
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
        propositionParameterBo.setValue(constant!=null && !constant.isEmpty() ? constant : null);
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
    public PropositionBo addCustomFunctionToPropositionParameter(String functionId, PropositionBo proposition,Integer seqNum) {
        PropositionParameterBo propositionParameterBo = new PropositionParameterBo();
        propositionParameterBo.setPropId(proposition.getId());
        propositionParameterBo.setValue(functionId);
        propositionParameterBo.setSequenceNumber(seqNum);
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
     * This method will create context,category and rules for creating agenda and finally persists into database.
     * @param oleAgenda
     * @return  agendaName
     */
    public String persist(OleKrmsAgenda oleAgenda) {
        boolean agendaExists = doesKrmsExist(oleAgenda.getName());
        if(agendaExists){
            deleteAgenda(oleAgenda);
        }
        context = createContext(OLEConstants.OLE_NAMESPACE, "OLE-CONTEXT");
        category = createCategory(OLEConstants.OLE_NAMESPACE, "OLE_CATEGORY");

        registerDefaultFunctionLoader();
        agendaBo = createAgenda(oleAgenda.getName(), context);
        createRules(oleAgenda.getRules());
        updateAgendaItems(oleAgenda.getRules(),null,null,agendaBo.getId());
        persistProfileAttributes(oleAgenda);
        persistOverlayOptions(oleAgenda);
       // persistOverlayLookupActions(oleAgenda);
        return agendaBo.getName();
    }

    /**
     *  This method persists the profile attributes in to database.
     * @param oleAgenda
     */
    private void persistProfileAttributes(OleKrmsAgenda oleAgenda) {
        List<ProfileAttributeBo> profileAttributes = oleAgenda.getProfileAttributes()!=null?oleAgenda.getProfileAttributes():new ArrayList<ProfileAttributeBo>();
        for (Iterator<ProfileAttributeBo> iterator = profileAttributes.iterator(); iterator.hasNext(); ) {
            ProfileAttributeBo profileAttributeBo = iterator.next();
            profileAttributeBo.setAgendaName(oleAgenda.getName());
        }
        getBusinessObjectService().save(profileAttributes);
    }

    /**
     *  This method persists the overlay options in to database.
     * @param oleAgenda
     */
    private void persistOverlayOptions(OleKrmsAgenda oleAgenda) {
        List<OverlayOption> overlayOptionList = oleAgenda.getOverlayOptions()!=null?oleAgenda.getOverlayOptions():new ArrayList<OverlayOption>();
        for(OverlayOption overlayOption : overlayOptionList){
            overlayOption.setAgendaName(oleAgenda.getName());
            getBusinessObjectService().save(overlayOption);
            for(DataField dataField : overlayOption.getDataFields()){
                    List<OleDataField> oleDataFieldList = new ArrayList<OleDataField>();
                    setOleDataField(dataField,oleDataFieldList,overlayOption);
                    getBusinessObjectService().save(oleDataFieldList);
                    if(LOG.isInfoEnabled()){
                        LOG.info("dataField.getTag---------->"+dataField.getTag());
                        LOG.info("dataField.getInd1--------->"+dataField.getInd1());
                        LOG.info("dataField.getInd2--------->"+dataField.getInd2());
                        for(SubField subField : dataField.getSubFields()){
                            LOG.info("subField.getCode--------->"+subField.getCode());
                        }
                    }
            }
        }
    }

    private void setOleDataField(DataField dataField,List<OleDataField> oleDataFieldList,OverlayOption overlayOption){
        for(SubField subField : dataField.getSubFields()){
            OleDataField oleDataField = new OleDataField();
            oleDataField.setOverlayOptionId(overlayOption.getId());
            oleDataField.setAgendaName(overlayOption.getAgendaName());
            oleDataField.setDataFieldTag(dataField.getTag());
            oleDataField.setDataFieldInd1(dataField.getInd1());
            oleDataField.setDataFieldInd2(dataField.getInd2());
            oleDataField.setSubFieldCode(subField.getCode());
            oleDataFieldList.add(oleDataField);
        }
    }


    private void updateAgendaItems(List<OleKrmsRule> rules,AgendaItemBo oldAgendaItemBo,AgendaItemBo agendaItemBo,String agendaId) {
        for (Iterator<OleKrmsRule> iterator = rules.iterator(); iterator.hasNext(); ) {
            OleKrmsRule rule = iterator.next();
            Object existing = objectExists(RuleBo.class, "nm", rule.getName());
            OleKrmsRuleAction oleKrmsRuleFalseAction= rule.getFalseActions();
            OleKrmsRuleAction oleKrmsRuleTrueAction = rule.getTrueActions();
            RuleBo oleRuleBo = existing!=null? (RuleBo)existing:null;
            Map<String, String> rulePk = new HashMap<String, String>() ;
            rulePk.put("ruleId",oleRuleBo.getId());
            rulePk.put("agendaId",agendaId);
            List<AgendaItemBo> agendaItemBos =(List<AgendaItemBo>) getBusinessObjectService().findMatching(AgendaItemBo.class,rulePk);
            agendaItemBo = agendaItemBos!=null && agendaItemBos.size()>0?agendaItemBos.get(0):null;
            if(oleKrmsRuleTrueAction!=null && oleKrmsRuleTrueAction.getKrmsRules()!=null && oleKrmsRuleTrueAction.getKrmsRules().size()>0){
                agendaItemBo.setWhenTrue(getTrueOrFalseItem( oleKrmsRuleTrueAction.getKrmsRules().get(0),agendaId));
                getBusinessObjectService().save(agendaItemBo);
            }
            if(oleKrmsRuleFalseAction!=null && oleKrmsRuleFalseAction.getKrmsRules()!=null && oleKrmsRuleFalseAction.getKrmsRules().size()>0){
                agendaItemBo.setWhenFalse(getTrueOrFalseItem( oleKrmsRuleFalseAction.getKrmsRules().get(0),agendaId));
                getBusinessObjectService().save(agendaItemBo);
            }
            if(rules.size()>1 && !rules.get(0).equals(rule) && oldAgendaItemBo!=null) {
                rulePk = new HashMap<String, String>() ;
                rulePk.put("ruleId",oldAgendaItemBo.getRuleId());
                rulePk.put("agendaId",agendaId);
                agendaItemBos =(List<AgendaItemBo>) getBusinessObjectService().findMatching(AgendaItemBo.class,rulePk);
                oldAgendaItemBo = agendaItemBos!=null && agendaItemBos.size()>0?agendaItemBos.get(0):null;
                if(oldAgendaItemBo!=null){
                    oldAgendaItemBo.setAlways(agendaItemBo);
                    getBusinessObjectService().save(oldAgendaItemBo);
                }
            }
            oldAgendaItemBo = agendaItemBo;
            if(oleKrmsRuleTrueAction!=null && oleKrmsRuleTrueAction.getKrmsRules()!=null){
                agendaItemBo = new AgendaItemBo();
                updateAgendaItems(oleKrmsRuleTrueAction.getKrmsRules(),oldAgendaItemBo,agendaItemBo,agendaId);
            }
            if(oleKrmsRuleFalseAction!=null && oleKrmsRuleFalseAction.getKrmsRules()!=null){
                agendaItemBo = new AgendaItemBo();
                updateAgendaItems(oleKrmsRuleFalseAction.getKrmsRules(),oldAgendaItemBo,agendaItemBo,agendaId);
            }
        }
    }
    private AgendaItemBo getTrueOrFalseItem(OleKrmsRule rule,String agendaId) {
        Object existing = objectExists(RuleBo.class, "nm", rule.getName());
        RuleBo oleRuleBo = existing!=null? (RuleBo)existing:null;
        return getAgendaItemUsingRuleId(oleRuleBo.getId(),agendaId);
    }
    private AgendaItemBo getAgendaItemUsingRuleId(String ruleId,String agendaId){
        Map<String, String> rulePk = new HashMap<String, String>() ;
        rulePk.put("ruleId",ruleId);
        rulePk.put("agendaId",agendaId);
        List<AgendaItemBo> agendaItemBos =(List<AgendaItemBo>) getBusinessObjectService().findMatching(AgendaItemBo.class,rulePk);
        for(AgendaItemBo agendaItemBo : agendaItemBos){
            rulePk = new HashMap<String, String>() ;
            rulePk.put("whenFalseId",agendaItemBo.getId());
            List<AgendaItemBo> whenFalse =(List<AgendaItemBo>) getBusinessObjectService().findMatching(AgendaItemBo.class,rulePk);

            rulePk = new HashMap<String, String>() ;
            rulePk.put("whenTrueId",agendaItemBo.getId());
            List<AgendaItemBo> whenTrue =(List<AgendaItemBo>) getBusinessObjectService().findMatching(AgendaItemBo.class,rulePk);

            rulePk = new HashMap<String, String>() ;
            rulePk.put("alwaysId",agendaItemBo.getId());
            List<AgendaItemBo> always =(List<AgendaItemBo>) getBusinessObjectService().findMatching(AgendaItemBo.class,rulePk);

            if(whenFalse.size()==0 && whenTrue.size()==0 && always.size()==0){
                return agendaItemBo;
            }
        }
        return null;
    }

    private void createRules(List<OleKrmsRule> oleKrmsRules){
        ContextValidActionBo contextValidActionBo = registerDefaultServiceTypes(context);
        List<OleKrmsRule> rules =oleKrmsRules;

        for (Iterator<OleKrmsRule> iterator = rules.iterator(); iterator.hasNext(); ) {
            OleKrmsRule rule = iterator.next();
            ruleBo = createRule(OLEConstants.OLE_NAMESPACE, rule.getName());

            addRuleToAgenda(agendaBo, ruleBo);
            PropositionBo propositionBo =  null;
            OleKrmsProposition oleProposition = rule.getOleProposition();
            if(oleProposition!=null){
                OleSimpleProposition simpleProposition = oleProposition.getSimpleProposition();
                OleCompoundProposition compoundProposition = oleProposition.getCompoundProposition();
                if(simpleProposition!=null){
                    propositionBo = createSimpleProposition(simpleProposition);
                }
                if(compoundProposition!=null){
                    List<PropositionBo> propositionBos = createCompoundProposition(compoundProposition.getCompoundPropositions(),compoundProposition.getSimplePropositions(),operators.get(compoundProposition.getOperator()));
                    propositionBo = propositionBos.get(0);

                }
            }
            ruleBo.setProposition(propositionBo);
            getBusinessObjectService().save(ruleBo);
            OleKrmsRuleAction oleKrmsRuleTrueAction = rule.getTrueActions();
            List<OleKrmsAction> trueActions =oleKrmsRuleTrueAction!=null && oleKrmsRuleTrueAction.getKrmsActions()!=null && oleKrmsRuleTrueAction.getKrmsActions().size()>0 ? oleKrmsRuleTrueAction.getKrmsActions():new ArrayList<OleKrmsAction>();
            int i = 0;
            for (Iterator<OleKrmsAction> oleActionIterator = trueActions.iterator(); oleActionIterator.hasNext(); ) {
                OleKrmsAction trueAction = oleActionIterator.next();
                contextValidActionBo = getContextValidActionBo(context,trueAction);
                String description = trueAction.getParameters()!=null && trueAction.getParameters().size()>0?trueAction.getParameters().get(0).getValue():"null";
                ActionBo actionBo = addActionToRule(OLEConstants.OLE_NAMESPACE, trueAction.getName(),description , contextValidActionBo.getActionTypeId(), ruleBo,i++);
                addActionAttributes(actionBo,trueAction);
            }
            OleKrmsRuleAction oleKrmsRuleFalseAction= rule.getFalseActions();

            List<OleKrmsAction> falseActions =oleKrmsRuleFalseAction!=null && oleKrmsRuleFalseAction.getKrmsActions()!=null && oleKrmsRuleFalseAction.getKrmsActions().size()>0 ? oleKrmsRuleFalseAction.getKrmsActions():new ArrayList<OleKrmsAction>();
            int j=0;
            if(falseActions.size()>0){
                 RuleBo falseActionRuleBo = createRule(OLEConstants.OLE_NAMESPACE, rule.getName() + "_falseActionRule");
                for (Iterator<OleKrmsAction> oleActionIterator = falseActions.iterator(); oleActionIterator.hasNext(); ) {
                    OleKrmsAction falseAction = oleActionIterator.next();
                    contextValidActionBo = getContextValidActionBo(context,falseAction);
                    String description = falseAction.getParameters()!=null && falseAction.getParameters().size()>0?falseAction.getParameters().get(0).getValue():"null";
                    ActionBo actionBo = addActionToRule(OLEConstants.OLE_NAMESPACE, falseAction.getName(), description, contextValidActionBo.getActionTypeId(), falseActionRuleBo, j++);
                    addActionAttributes(actionBo,falseAction);
                }
                addDummyRuleForFalseAction(ruleBo, falseActionRuleBo);
            }
            if(oleKrmsRuleTrueAction!=null && oleKrmsRuleTrueAction.getKrmsRules()!=null){
                createRules(oleKrmsRuleTrueAction.getKrmsRules());
            }

            if(oleKrmsRuleFalseAction!=null && oleKrmsRuleFalseAction.getKrmsRules()!=null){
                createRules(oleKrmsRuleFalseAction.getKrmsRules());
            }
        }
    }
    /**
     * This method creates compound proposition from the oleProposition and propositionBos parameters
     * @param compoundPropositions
     */
    private List<PropositionBo> createCompoundProposition(List<OleCompoundProposition> compoundPropositions,List<OleSimpleProposition> simplePropositions,String operator){
        if(compoundPropositions!=null){
            List<PropositionBo> propositionBos = new ArrayList<PropositionBo>();
            PropositionBo propositionBo = null;
            for(OleCompoundProposition compoundProposition :compoundPropositions){
                propositionBos.addAll(createCompoundProposition(compoundProposition.getCompoundPropositions(),compoundProposition.getSimplePropositions(),operators.get(compoundProposition.getOperator())));
            }
            if(simplePropositions!=null){
                propositionBos.addAll(createCompoundProposition(null,simplePropositions,operator));
            }
            propositionBo = createCompoundProposition(category.getId(), "compound","C",ruleBo,operator);
            propositionBo.setCompoundComponents(propositionBos);
            getBusinessObjectService().save(propositionBo);
            propositionBos = new ArrayList<PropositionBo>();
            propositionBos.add(propositionBo);
            return propositionBos;
        }else if(simplePropositions!=null){
            List<PropositionBo> propositionBos = new ArrayList<PropositionBo>() ;
            PropositionBo propositionBo = null;
            for(OleSimpleProposition simpleProposition : simplePropositions){
                propositionBos.add(createSimpleProposition(simpleProposition));
            }
            if(simplePropositions.size()>1){
                propositionBo = createCompoundProposition(category.getId(), "compound","C",ruleBo,operator);
                propositionBo.setCompoundComponents(propositionBos);
                getBusinessObjectService().save(propositionBo);
                propositionBos = new ArrayList<PropositionBo>();
                propositionBos.add(propositionBo);
            }
            return propositionBos;
        }
        return null;
    }

    /**
     * This method adds the action attributes to create the agenda.
     * @param action
     * @param oleKrmsAction
     */
    private void addActionAttributes(ActionBo action,OleKrmsAction oleKrmsAction){
        Set<ActionAttributeBo> actionAttributeBos = new HashSet<ActionAttributeBo>();
        List<OleParameter> oleParameters =  oleKrmsAction.getParameters()!=null ? oleKrmsAction.getParameters() :new ArrayList<OleParameter>();
        for(OleParameter oleParameter : oleParameters)  {
            Map<String, String> peopleFlowPk = new HashMap<String, String>();
            peopleFlowPk.put("nm", oleParameter.getValue());
            List<PeopleFlowBo> peopleFlowBos =(List<PeopleFlowBo>) getBusinessObjectService().findMatching(PeopleFlowBo.class, peopleFlowPk);
            if(peopleFlowBos!=null && peopleFlowBos.size()>0){
                Map<String, String> attrDefPk = new HashMap<String, String>();
                attrDefPk.put("nm", "peopleFlowId");
                attrDefPk.put("nmspc_cd", "KR-RULE");
                KrmsAttributeDefinitionBo peopleFlowIdDef =   getKrmsAttributeDefinitionBo(attrDefPk);
                attrDefPk = new HashMap<String, String>();
                attrDefPk.put("nm", "peopleFlowName");
                attrDefPk.put("nmspc_cd", "KR-RULE");
                KrmsAttributeDefinitionBo peopleFlowNameDef = getKrmsAttributeDefinitionBo(attrDefPk);
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
                actionAttributeBo.setValue(oleParameter.getValue());
                getBusinessObjectService().save(actionAttributeBo);
                actionAttributeBos.add(actionAttributeBo);
            } else{
                KrmsAttributeDefinitionBo attributeDefinitionBo = createAttributeDefinition(oleParameter);
                //createTypeAttribute(attributeDefinitionBo,action);
                actionAttributeBos.add(createActionAttribute(attributeDefinitionBo,action,oleParameter));
            }
        }
        if(actionAttributeBos.size()>0){
            action.setAttributeBos(actionAttributeBos);
            getBusinessObjectService().save(action);
        }

    }

    private KrmsAttributeDefinitionBo getKrmsAttributeDefinitionBo( Map<String, String> attrDefPk){
        List<KrmsAttributeDefinitionBo> krmsAttributeDefinitionBos = ( List<KrmsAttributeDefinitionBo>)getBusinessObjectService().findMatching(KrmsAttributeDefinitionBo.class,attrDefPk);
        return  krmsAttributeDefinitionBos!=null && krmsAttributeDefinitionBos.size()>0 ? krmsAttributeDefinitionBos.get(0) : null;
    }
    private KrmsAttributeDefinitionBo createAttributeDefinition(OleParameter oleParameter){
        Map<String, String> attrDefPk = new HashMap<String, String>();
        attrDefPk.put("nm", oleParameter.getName());
        attrDefPk.put("nmspc_cd",OLEConstants.OLE_NAMESPACE);
        KrmsAttributeDefinitionBo attributeDefinitionBo = getBusinessObjectService().findByPrimaryKey(KrmsAttributeDefinitionBo.class,attrDefPk);
        if(attributeDefinitionBo==null){
            attributeDefinitionBo = new KrmsAttributeDefinitionBo();
        }
        attributeDefinitionBo.setNamespace(OLEConstants.OLE_NAMESPACE);
        attributeDefinitionBo.setName(oleParameter.getName());
        getBusinessObjectService().save(attributeDefinitionBo);
        return attributeDefinitionBo;
    }
    private KrmsAttributeDefinitionBo createTypeAttribute(KrmsAttributeDefinitionBo attributeDefinitionBo,ActionBo action){
        Map<String, String> typeAttrPk = new HashMap<String, String>();
        typeAttrPk.put("typ_id", action.getTypeId());
        typeAttrPk.put("attr_defn_id",attributeDefinitionBo.getId());
        KrmsTypeAttributeBo typeAttributeBo = getBusinessObjectService().findByPrimaryKey(KrmsTypeAttributeBo.class,typeAttrPk);
        if(typeAttributeBo==null){
            typeAttributeBo = new KrmsTypeAttributeBo();
        }
        typeAttributeBo.setAttributeDefinitionId(attributeDefinitionBo.getId());
        typeAttributeBo.setTypeId(action.getTypeId());
        typeAttributeBo.setSequenceNumber(0);
        getBusinessObjectService().save(typeAttributeBo);
        return attributeDefinitionBo;
    }
    private ActionAttributeBo createActionAttribute(KrmsAttributeDefinitionBo attributeDefinitionBo,ActionBo action,OleParameter oleParameter){
        /*Map<String, String> actnAttrPk = new HashMap<String, String>();
        actnAttrPk.put("attr_defn_id",attributeDefinitionBo.getId());
        ActionAttributeBo actionAttributeBo = getBusinessObjectService().findByPrimaryKey(ActionAttributeBo.class,actnAttrPk);
        if(actionAttributeBo==null){*/
       // }
        ActionAttributeBo actionAttributeBo = new ActionAttributeBo();
        actionAttributeBo.setActionId(action.getId());
        actionAttributeBo.setAttributeDefinitionId(attributeDefinitionBo.getId());
        actionAttributeBo.setAttributeDefinition(attributeDefinitionBo);
        actionAttributeBo.setValue(oleParameter.getValue());
        getBusinessObjectService().save(actionAttributeBo);
        return actionAttributeBo;
    }
    /**
     * This method returns the contextValidActionBo object based on the contextBo and action parameters
     * Creates a new object, stores it in database and returns if none exists, otherwise returns the existing object.
     * @param contextBo
     * @param action
     * @return  contextValidActionBo
     */
    private ContextValidActionBo getContextValidActionBo(ContextBo contextBo,OleKrmsAction action){
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
    private KrmsTypeBo  getActiontypeService(OleKrmsAction action){
        Map<String, String> krmsTypePk = new HashMap<String, String>();
        krmsTypePk.put("nm", action.getName());
        krmsTypePk.put("nmspc_cd",OLEConstants.OLE_NAMESPACE);
        KrmsTypeBo krmsTypeBo = getBusinessObjectService().findByPrimaryKey(KrmsTypeBo.class, krmsTypePk);
        if(krmsTypeBo==null){
            krmsTypePk.put("nmspc_cd","KR-RULE");
            krmsTypeBo = getBusinessObjectService().findByPrimaryKey(KrmsTypeBo.class, krmsTypePk);
        }
        return  krmsTypeBo;
    }

    /**
     * This method persists the matchBo object based on the agendaBo and proposition parameters..
     * @param agendaBo
     * @param term
     */
    private void persistMatchBo(AgendaBo agendaBo, OleTerm term) {
       MatchBo matchBo = new MatchBo();
        matchBo.setAgendaName(agendaBo.getName());
        matchBo.setTermName(term.getValue());
        String[] termNames = term.getValue().split("-");
        if(termNames[0].equalsIgnoreCase(OLEConstants.INCOMING_FIELD)){
            matchBo.setIncomingField(termNames[1]);
        }
        else if(termNames[0].equalsIgnoreCase(OLEConstants.EXISTING_FIELD)){
            matchBo.setExistingField(termNames[1]);
        }
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
        OleKrms krms = getKrmsObjectGeneratorFromXML().buildKrmsFromFileContent(fileContent);
        List<String> agendaNames = new ArrayList<String>();
        for(OleKrmsAgenda oleAgenda :krms.getAgendas()){
            agendaNames.add(persist(oleAgenda));
        }
        updateDefinitions();
        return agendaNames;

    }

    private void updateDefinitions(){
        Map<String, Object> contextMap = new HashMap<String, Object>();
        contextMap.put("name", "OLE-CONTEXT");
        contextMap.put("namespace", "OLE");
        ContextBo contextBo = getBusinessObjectService().findByPrimaryKey(ContextBo.class, contextMap);
        KrmsRepositoryServiceLocator.getContextBoService().updateContext(ContextBo.to(contextBo));
    }

    /**
     * Gets the krmsObjectGeneratorFromXML object if exists, otherwise it creates a new object.
     * @return  krmsObjectGeneratorFromXML
     */
    public OleKrmsObjectGeneratorFromXML getKrmsObjectGeneratorFromXML() {
        if(null==krmsObjectGeneratorFromXML){
            krmsObjectGeneratorFromXML = new OleKrmsObjectGeneratorFromXML();
        }
        return krmsObjectGeneratorFromXML;
    }

    /**
     * Sets the krmsObjectGeneratorFromXML value.
     * @param krmsObjectGeneratorFromXML
     */
    public void setKrmsObjectGeneratorFromXML(OleKrmsObjectGeneratorFromXML krmsObjectGeneratorFromXML) {
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
    public void deleteAgenda(OleKrmsAgenda oleAgenda) {

        LOG.info(" inside deleteAgenda method ");
        Map<String, String> contextPks = new HashMap<String, String>();
        contextPks.put("nm", "OLE-CONTEXT");
        contextPks.put("nmspc_cd", OLEConstants.OLE_NAMESPACE);
        ContextBo context = getBusinessObjectService().findByPrimaryKey(ContextBo.class, contextPks);
        String contextId = context.getId();

        Map<String, String> agendaPks = new HashMap<String, String>();
        agendaPks.put("nm", oleAgenda.getName());
        agendaPks.put("cntxt_id", contextId);
        AgendaBo oldAgenda = getBusinessObjectService().findByPrimaryKey(AgendaBo.class, agendaPks);

        Map<String, String> itemPks = new HashMap<String, String>();
        LOG.info(" oldAgenda.getId() --------------> " + oldAgenda.getId());
        itemPks.put("agenda_id", oldAgenda.getId());
        List<AgendaItemBo> items = (List<AgendaItemBo>) getBusinessObjectService().findMatching(AgendaItemBo.class, itemPks);
        for(AgendaItemBo agendaItemBo : items){
            itemPks = new HashMap<String, String>();
            itemPks.put("rule_id", agendaItemBo.getRuleId());
            List<AgendaItemBo> agendaItemBos =(List<AgendaItemBo>) getBusinessObjectService().findMatching(AgendaItemBo.class, itemPks);
            if(agendaItemBos.size()>1){
                agendaItemBo.setRule(null);
                agendaItemBo.setRuleId(null);
            }
            if(agendaItemBo.getWhenTrue()!=null){
                agendaItemBo.setWhenTrue(null);
                agendaItemBo.setWhenTrueId(null);
            }
            if(agendaItemBo.getWhenFalse()!=null){
                agendaItemBo.setWhenFalse(null);
                agendaItemBo.setWhenFalseId(null);
            }
            if(agendaItemBo.getAlways()!=null){
                agendaItemBo.setAlways(null);
                agendaItemBo.setAlwaysId(null);
            }
            getBusinessObjectService().save(agendaItemBo);
        }

        itemPks = new HashMap<String, String>();
        itemPks.put("agenda_id", oldAgenda.getId());
        items = (List<AgendaItemBo>) getBusinessObjectService().findMatching(AgendaItemBo.class, itemPks);
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

      //  deleteRegisteredRules(oleAgenda);
     //   unregisterFunctionLoader();

        HashMap matchingProfileAttr = new HashMap();
        matchingProfileAttr.put("agenda_name", oldAgenda.getName());
        List<ProfileAttributeBo> profileAttributeBos =
                (List<ProfileAttributeBo>) getBusinessObjectService().findMatching(ProfileAttributeBo.class, matchingProfileAttr);
        getBusinessObjectService().delete(profileAttributeBos);

        HashMap matchingOleDataField = new HashMap();
        matchingProfileAttr.put("agenda_name", oldAgenda.getName());
        List<OleDataField> matchingOleDataFields =
                (List<OleDataField>) getBusinessObjectService().findMatching(OleDataField.class, matchingOleDataField);
        getBusinessObjectService().delete(matchingOleDataFields);

        HashMap matchingOverlayOption = new HashMap();
        matchingProfileAttr.put("agenda_name", oldAgenda.getName());
        List<OverlayOption> matchingOverlayOptions =
                (List<OverlayOption>) getBusinessObjectService().findMatching(OverlayOption.class, matchingOverlayOption);
        getBusinessObjectService().delete(matchingOverlayOptions);

/*        HashMap matchingOverlayLookupAction = new HashMap();
        matchingProfileAttr.put("agenda_name", oldAgenda.getName());
        List<OverlayLookupAction> matchingOverlayLookupActions =
                (List<OverlayLookupAction>) getBusinessObjectService().findMatching(OverlayLookupAction.class, matchingOverlayLookupAction);
        getBusinessObjectService().delete(matchingOverlayLookupActions);

        HashMap matchingOverlayLookupTable = new HashMap();
        matchingProfileAttr.put("agenda_name", oldAgenda.getName());
        List<OverlayLookupTable> matchingOverlayLookupTables =
                (List<OverlayLookupTable>) getBusinessObjectService().findMatching(OverlayLookupTable.class, matchingOverlayLookupTable);
        getBusinessObjectService().delete(matchingOverlayLookupTables);*/

        HashMap matchingProfileFacts = new HashMap();
        matchingProfileFacts.put("agenda_name", oldAgenda.getName());
        List<MatchBo> matchBos =
                (List<MatchBo>) getBusinessObjectService().findMatching(MatchBo.class, matchingProfileFacts);
        getBusinessObjectService().delete(matchBos);
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
     *  This method deletes the term related rules from database.
     * @param oleAgenda
     */
    private void deleteRegisteredRules(OleKrmsAgenda oleAgenda) {
        Map map = new HashMap();
        List<OleKrmsRule> rules = oleAgenda.getRules();
        for (Iterator<OleKrmsRule> iterator = rules.iterator(); iterator.hasNext(); ) {
            OleKrmsRule krmsRule = iterator.next();
            OleSimpleProposition simpleProposition = krmsRule.getOleProposition().getSimpleProposition();
            OleCompoundProposition compoundProposition = krmsRule.getOleProposition().getCompoundProposition();
         if(simpleProposition!=null){
                List<OleTerm> terms =simpleProposition.getTerms();
                for(OleTerm term : terms){
                    deleteTerm(term.getValue()) ;
                }
         }
         if(compoundProposition!=null){
                deleteCompoundProposition(compoundProposition);
         }
            map.put("nm", krmsRule.getName()+oleAgenda.getName());
            List<RuleBo> matching = (List<RuleBo>) getBusinessObjectService().findMatching(RuleBo.class, map);
            getBusinessObjectService().delete(matching);
        }
    }

    /**
     *  This method deletes the compound proposition based on the oleProposition from database.
     * @param compoundProposition
     */
    private void deleteCompoundProposition(OleCompoundProposition compoundProposition){
        for(OleCompoundProposition compoundProposition1 : compoundProposition.getCompoundPropositions()!=null?compoundProposition.getCompoundPropositions():new ArrayList<OleCompoundProposition>()){
            deleteCompoundProposition(compoundProposition1);
        }
        for(OleSimpleProposition simpleProposition : compoundProposition.getSimplePropositions()!=null?compoundProposition.getSimplePropositions():new ArrayList<OleSimpleProposition>()){
            List<OleTerm> terms =simpleProposition.getTerms();
            for(OleTerm term : terms){
                deleteTerm(term.getValue()) ;
            }
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
        String typeName = "KRMS Action";
        Object existing = objectExists(KrmsTypeBo.class, "nm", typeName);
        if (existing == null) {
            actionTypeService = new KrmsTypeBo();
            actionTypeService.setActive(true);
            actionTypeService.setName("KRMS Action");
            actionTypeService.setNamespace(OLEConstants.OLE_NAMESPACE);
            actionTypeService.setServiceName("krmsActionTypeService");
            getBusinessObjectService().save(actionTypeService);
        } else {
            actionTypeService = (KrmsTypeBo) existing;
        }
        return actionTypeService;
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
        ActionBo actionBo = new ActionBo();
        HashMap<String, String> map = new HashMap<String, String>();
        map.put("nm", actionName);
        map.put("rule_id",ruleBo.getId());
        List<ActionBo> matching = (List<ActionBo>) getBusinessObjectService().findMatching(ActionBo.class, map);
        Object existing =  matching.isEmpty() ? null : matching.get(0);
        if (existing == null) {
            actionBo.setName(actionName);
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
     * This method adds the dummy rule into database for falseAction.
     * @param ruleBo
     * @param falseActionRuleBo
     * @return  AgendaItemBo
     */
    public AgendaItemBo addDummyRuleForFalseAction(RuleBo ruleBo, RuleBo falseActionRuleBo) {
        HashMap<String, String> map = new HashMap<String, String>();
        map.put("rule_id", ruleBo.getId());
        List<AgendaItemBo> matching =
                (List<AgendaItemBo>) getBusinessObjectService().findMatching(AgendaItemBo.class, map);
        AgendaItemBo existingAgendaItemBo = matching.get(0);

        AgendaItemBo falseActionAgendaItemBo = new AgendaItemBo();
        falseActionAgendaItemBo.setAgendaId(existingAgendaItemBo.getAgendaId());
        RuleBo falseRuleBo = getBusinessObjectService().findBySinglePrimaryKey(RuleBo.class,falseActionRuleBo.getId());
        if(falseRuleBo!=null){
            falseActionAgendaItemBo.setRule(falseRuleBo);
            falseActionAgendaItemBo.setRuleId(falseRuleBo.getId());
        }
        getBusinessObjectService().save(falseActionAgendaItemBo);
        existingAgendaItemBo.setWhenFalse(falseActionAgendaItemBo);
        getBusinessObjectService().save(existingAgendaItemBo);
        return existingAgendaItemBo;
    }


    /**
     * This method creates a simple proposition based on the proposition and seqNum parameters and returns the propositionBo.
     * @param proposition
     * @return PropositionBo
     */
    private PropositionBo createSimpleProposition(OleSimpleProposition proposition){
        List<String> parameterTypes = new ArrayList<String>();
        List<OleTerm> terms = proposition.getTerms();
        OleTerm term =terms.get(0);
        if(terms.size()>1){
             return createSimplePropositionWithTerms(proposition, terms,parameterTypes)  ;
        }
        List<OleValue> values = proposition.getValues()!=null?proposition.getValues():new ArrayList<OleValue>();

        TermSpecificationBo termSpecification =
                createTermSpecification(OLEConstants.OLE_NAMESPACE,term.getValue(), term.getType(), Arrays.asList(category), Arrays.asList(context));

        TermBo termBo = createTerm(term.getValue(), termSpecification);
        List<PropositionBo> propositionBos = new ArrayList<PropositionBo>();
        int i =0;
        PropositionBo propositionBo = null;
        for(OleValue value :values){

            propositionBo = createProposition(category.getId(), term.getValue() + value.getName(), "S", ruleBo,i++);

            addTermToPropositionParameter(termBo.getId(), propositionBo,0);

            addConstantToPropositionParameter(value.getName(),propositionBo);

            addOperatorToPropositionParameter(operators.get(proposition.getOperator()),propositionBo);

            propositionBos.add(propositionBo);

        }
        if(propositionBo==null && proposition.getFunction()!=null){

            propositionBo = createProposition(category.getId(), term.getValue() + proposition.getFunction(), "S", ruleBo,i++);


            parameterTypes.add(term.getType());

            FunctionBo function = createFunction(OLEConstants.OLE_NAMESPACE, proposition.getFunction(), "java.lang.Boolean", Arrays.asList(category),parameterTypes);

            addTermToPropositionParameter(termBo.getId(), propositionBo,0);

            addCustomFunctionToPropositionParameter(function.getId(),propositionBo,1);
        }
        if(propositionBos.size()>1){
            propositionBo = createCompoundProposition(category.getId(),term.getValue() + "compound","C",ruleBo,"|");
            propositionBo.setCompoundComponents(propositionBos);
            getBusinessObjectService().save(propositionBo);
        }
        persistMatchBo(agendaBo, term);
        return propositionBo;
    }

    private PropositionBo createSimplePropositionWithTerms(OleSimpleProposition proposition, List<OleTerm> terms,List<String> parameterTypes) {
        PropositionBo propositionBo = createProposition(category.getId(), "Two Terms", "S", ruleBo,0);
        int  seqNum=0;
        for(OleTerm term :terms){
            TermSpecificationBo termSpecification =
                    createTermSpecification(OLEConstants.OLE_NAMESPACE,term.getValue(), term.getType(), Arrays.asList(category), Arrays.asList(context));
            TermBo termBo = createTerm(term.getValue(), termSpecification);
            addTermToPropositionParameter(termBo.getId(), propositionBo,seqNum);
            parameterTypes.add(term.getType()) ;
            persistMatchBo(agendaBo, term);
            seqNum ++;
        }
        if(proposition.getFunction()!=null){
            FunctionBo function = createFunction(OLEConstants.OLE_NAMESPACE, proposition.getFunction(), "java.lang.Boolean", Arrays.asList(category),parameterTypes);
            addCustomFunctionToPropositionParameter(function.getId(),propositionBo,seqNum);
        }
        if(proposition.getOperator()!=null){
            addOperatorToPropositionParameter(operators.get(proposition.getOperator()),propositionBo);
        }
        getBusinessObjectService().save(propositionBo);
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
