package org.kuali.ole.deliver.drools;

import org.apache.camel.util.StopWatch;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.kie.api.event.rule.DebugAgendaEventListener;
import org.kie.api.io.ResourceType;
import org.kie.internal.KnowledgeBase;
import org.kie.internal.KnowledgeBaseFactory;
import org.kie.internal.builder.KnowledgeBuilder;
import org.kie.internal.builder.KnowledgeBuilderFactory;
import org.kie.internal.io.ResourceFactory;
import org.kie.internal.runtime.StatefulKnowledgeSession;
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
import java.io.FileInputStream;
import java.io.FileNotFoundException;

/**
 * Created by pvsubrah on 3/23/15.
 */
public class DroolsEngine {
    private Logger LOG = LoggerFactory.getLogger(DroolsEngine.class);
    private KnowledgeBase knowledgeBase;
    private ParameterService parameterService;

    private static Boolean forceLoadPolicies = true;
    private static final String LOAD_POLICIES_IND = "LOAD_CIRC_POLICIES_IND";

    private static DroolsEngine droolsEngine = new DroolsEngine();

    protected DroolsEngine() {

    }

    public static DroolsEngine getInstance() {
        return droolsEngine;
    }

    /*
    This method should be called first to load the rules repository.
     */
    public void initKnowledgeBase() {
        long startTime = System.currentTimeMillis();
        knowledgeBase = populateKnowledgeBase();
        long endTime = System.currentTimeMillis();
        System.out.println("Time taken to populate drools knowledgebase: " + (endTime - startTime) + " ms");
    }

    private KnowledgeBase populateKnowledgeBase() {
        Boolean parameterValueAsBoolean = getParameterAsBoolean();
        if (forceLoadPolicies || parameterValueAsBoolean) {
            readRules();
            updateParameter();
        }
        return knowledgeBase;
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
                    "upon next policy evalvuation.");
            newParameter.setValue("Y");
            getParameterService().createParameter(newParameter.build());
            forceLoadPolicies = false;
        }
    }

    private void readRules() {
        // seed a builder with our rules file from classpath
        KnowledgeBuilder builder = KnowledgeBuilderFactory.newKnowledgeBuilder();

        File rulesDirectory = FileUtils.getFile(getRulesDirectory());
        if (null != rulesDirectory && rulesDirectory.isDirectory() && FileUtils.sizeOfDirectory(rulesDirectory) > 0) {
            File[] files = rulesDirectory.listFiles();
            loadRules(builder, files);

            if (builder.hasErrors()) {
                LOG.error("Could not load Circulation Rules as directory doest not exist or is unreadable.");
                LOG.error(builder.getErrors().toString());
            }

            // create a knowledgeBase from our builder packages
            knowledgeBase = KnowledgeBaseFactory.newKnowledgeBase();
            knowledgeBase.addKnowledgePackages(builder.getKnowledgePackages());
        } else {
            LOG.error("Could not load Circulation Rules as directory doest not exist or is unreadable.");
        }
    }

    private void loadRules(KnowledgeBuilder builder, File[] files) {
        for (int i = 0; i < files.length; i++) {
            File file = files[i];
            if (!file.isDirectory()) {
                if (FilenameUtils.getExtension(file.getName()).equals("drl")) {
                    FileInputStream fis = null;
                    try {
                        fis = new FileInputStream(file);
                        builder.add(ResourceFactory.newInputStreamResource(fis), ResourceType.DRL);
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }
                }
            } else {
                File[] subDirFiles = file.listFiles();
                loadRules(builder, subDirFiles);
            }
        }
    }

    public synchronized void disposePreviousSession(StatefulKnowledgeSession statefulKnowledgeSession) {
            statefulKnowledgeSession.dispose();
    }

    public String getRulesDirectory() {
        String rulesDirectory = ConfigContext.getCurrentContextConfig().getProperty("rules.directory");
        return rulesDirectory;
    }

    public synchronized StatefulKnowledgeSession getSession() {
        StopWatch stopWatch = new StopWatch();
        stopWatch.restart();
        StatefulKnowledgeSession statefulKnowledgeSession = null;

        try {
            Boolean parameterValueAsBoolean = getParameterAsBoolean();
            if (null != parameterValueAsBoolean && parameterValueAsBoolean) {
                populateKnowledgeBase();
            }
            // load a new knowledgeBase with our rules
            // initialize a session for usage
            statefulKnowledgeSession = knowledgeBase.newStatefulKnowledgeSession();
            statefulKnowledgeSession.addEventListener(new CustomAgendaEventListener());
            statefulKnowledgeSession.addEventListener(new DebugAgendaEventListener());

        } catch (Exception e) {
            e.printStackTrace();
        }

        stopWatch.stop();
        System.out.println("Time taken to initialize Drools session: " + stopWatch.taken() + " milli seconds");
        return statefulKnowledgeSession;
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
}
