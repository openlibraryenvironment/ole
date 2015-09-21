package org.kuali.ole.deliver.drools;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.lucene.util.CollectionUtil;
import org.drools.core.definitions.rule.impl.RuleImpl;
import org.kie.api.KieServices;
import org.kie.api.builder.KieBuilder;
import org.kie.api.builder.KieFileSystem;
import org.kie.api.builder.Message;
import org.kie.api.builder.Results;
import org.kie.api.definition.KiePackage;
import org.kie.api.definition.rule.Rule;
import org.kie.api.event.rule.DebugAgendaEventListener;
import org.kie.api.runtime.KieContainer;
import org.kie.api.runtime.KieSession;
import org.kie.internal.io.ResourceFactory;
import org.kuali.ole.OLEConstants;
import org.kuali.ole.deliver.service.ParameterValueResolver;
import org.kuali.ole.sys.context.SpringContext;
import org.kuali.rice.core.api.config.property.ConfigContext;
import org.kuali.rice.coreservice.api.parameter.Parameter;
import org.kuali.rice.coreservice.api.parameter.ParameterType;
import org.kuali.rice.coreservice.framework.parameter.ParameterService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.util.*;

/**
 * Created by pvsubrah on 3/23/15.
 */
public class DroolsKieEngine {
    private Logger LOG = LoggerFactory.getLogger(DroolsKieEngine.class);
    private KieContainer kieContainer;
    private ParameterService parameterService;

    private static Boolean forceLoadPolicies = true;
    private static final String LOAD_POLICIES_IND = "LOAD_CIRC_POLICIES_IND";

    private static DroolsKieEngine droolsEngine = new DroolsKieEngine();

    protected DroolsKieEngine() {

    }

    public static DroolsKieEngine getInstance() {
        return droolsEngine;
    }

    /*
    This method should be called first to load the rules repository.
     */
    public void initKnowledgeBase() {
        long startTime = System.currentTimeMillis();
        populateKnowledgeBase();
        long endTime = System.currentTimeMillis();
        System.out.println("Time taken to populate drools knowledgebase: " + (endTime - startTime) + " ms");
    }

    private void populateKnowledgeBase() {
        Boolean parameterValueAsBoolean = getParameterAsBoolean();
        if (forceLoadPolicies || parameterValueAsBoolean) {
            readRules();
            updateParameter();
        }
    }

    private Boolean getParameterAsBoolean() {
        return ParameterValueResolver.getInstance().getParameterAsBoolean(OLEConstants
                        .APPL_ID, OLEConstants
                        .DLVR_NMSPC,
                OLEConstants
                        .DLVR_CMPNT, LOAD_POLICIES_IND);
    }

    private void updateParameter() {
        Parameter existingParameter = getParameterService().getParameter(OLEConstants.DLVR_NMSPC, OLEConstants
                .DLVR_CMPNT, LOAD_POLICIES_IND);
        if (existingParameter != null) {
            Parameter.Builder updatedParameter = Parameter.Builder.create(existingParameter);
            updatedParameter.setValue("N");
            forceLoadPolicies = false;
            getParameterService().updateParameter(updatedParameter.build());
        } else {
            Parameter.Builder newParameter = Parameter.Builder.create(OLEConstants.APPL_ID, OLEConstants.DLVR_NMSPC, OLEConstants.DLVR_CMPNT, LOAD_POLICIES_IND, ParameterType.Builder.create("CONFG"));
            newParameter.setDescription("Set to 'Y' to have the application ingest the default circulation policies " +
                    "upon next policy evaluation.");
            newParameter.setValue("Y");
            getParameterService().createParameter(newParameter.build());
            forceLoadPolicies = false;
        }
    }

    private void readRules() {

        File rulesDirectory = FileUtils.getFile(getRulesDirectory());
        if (null != rulesDirectory && rulesDirectory.isDirectory() && FileUtils.sizeOfDirectory(rulesDirectory) > 0) {
            File[] files = rulesDirectory.listFiles();

            KieServices kieServices = KieServices.Factory.get();
            KieFileSystem kfs = kieServices.newKieFileSystem();

            loadRules(kfs, files);

            KieBuilder kieBuilder = kieServices.newKieBuilder(kfs).buildAll();
            Results results = kieBuilder.getResults();

            if (results.hasMessages(Message.Level.ERROR)) {
               //TODO: Populate the DroolResponse object with the appropriate message.
            }

            long startTime = System.currentTimeMillis();
            kieContainer = kieServices.newKieContainer(kieServices.getRepository().getDefaultReleaseId());
            long endTime = System.currentTimeMillis();
            System.out.println("Time taken to initiallize KieContainer: " + (endTime - startTime) + "ms");


        } else {
            LOG.error("Could not load Circulation Rules as directory doest not exist or is unreadable.");
        }
    }

    private void loadRules(KieFileSystem kfs, File[] files) {
        for (int i = 0; i < files.length; i++) {
            File file = files[i];
            if (!file.isDirectory()) {
                if (FilenameUtils.getExtension(file.getName()).equals("drl")) {
                    kfs.write(ResourceFactory.newFileResource(file));
                }
            } else {
                File[] subDirFiles = file.listFiles();
                loadRules(kfs, subDirFiles);
            }
        }
    }

    public synchronized void disposeSession(KieSession kieSession) {
        kieSession.dispose();
    }

    public String getRulesDirectory() {
        String rulesDirectory = ConfigContext.getCurrentContextConfig().getProperty("rules.directory");
        return rulesDirectory;
    }

    public synchronized KieSession getSession() {
        populateKnowledgeBase();
        long startTime1 = System.currentTimeMillis();
        KieSession kieSession = kieContainer.newKieSession();
        kieSession.addEventListener(new CustomAgendaEventListener());
        kieSession.addEventListener(new DebugAgendaEventListener());
        long endTime1 = System.currentTimeMillis();

        System.out.println("Time taken to initialize KieSession: " + (endTime1 - startTime1) + "ms");

        return kieSession;

    }

    public ParameterService getParameterService() {
        if (null == parameterService) {
            parameterService = SpringContext.getBean(ParameterService.class);
        }
        return parameterService;
    }

    public void setParameterService(ParameterService parameterService) {
        this.parameterService = parameterService;
    }


    public List<String> getRulesByAgendaGroup(List<String>agendaGroups){
        List<String> ruleNames = new ArrayList<>();

        for (Iterator iterator = kieContainer.getKieBase().getKiePackages().iterator(); iterator.hasNext(); ) {
            KiePackage kiePackage = (KiePackage) iterator.next();
            Collection<Rule> rules = kiePackage.getRules();
            for (Iterator<Rule> ruleIterator = rules.iterator(); ruleIterator.hasNext(); ) {
                Rule rule = ruleIterator.next();
                if(agendaGroups.contains(((RuleImpl)rule).getAgendaGroup())){
                    ruleNames.add(rule.getName());
                }
            }
        }
        Collections.sort(ruleNames);
        return ruleNames;
    }


    public List<String> getAllLoadedRules(){
        List<String> ruleNames = new ArrayList<>();

        for (Iterator iterator = kieContainer.getKieBase().getKiePackages().iterator(); iterator.hasNext(); ) {
            KiePackage kiePackage = (KiePackage) iterator.next();
            Collection<Rule> rules = kiePackage.getRules();
            for (Iterator<Rule> ruleIterator = rules.iterator(); ruleIterator.hasNext(); ) {
                Rule rule = ruleIterator.next();
                    ruleNames.add(rule.getName());
            }
        }

        return ruleNames;
    }
}
