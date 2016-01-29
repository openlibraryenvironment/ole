package org.kuali.ole;

import org.apache.camel.CamelContext;
import org.apache.camel.Processor;
import org.apache.camel.Route;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.impl.DefaultCamelContext;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Created by pvsubrah on 1/10/15.
 */
public class OleCamelContext {
    public static OleCamelContext oleCamelContext;
    private CamelContext context;
    private String defaultFilePath;
    private String defaultFileName;
    private String destinationURI;
    private String startingURI;
    List<Processor> processors;

    private OleCamelContext() {
        context = new DefaultCamelContext();
        try {
            context.start();
        } catch (Exception e) {
            System.out.println("Camel Context not initialized");
            e.printStackTrace();
        }
    }

    public static OleCamelContext getInstance() {
        if (null == oleCamelContext) {
            oleCamelContext = new OleCamelContext();
        }
        return oleCamelContext;
    }

    public CamelContext getContext() {
        return context;
    }

    public void setFileNameAndPath(String filePath, String fileName) {
        this.defaultFilePath = filePath;
        this.defaultFileName = fileName;
        buildInitialRoute();
    }

    private void buildInitialRoute() {
        try {
            context.addRoutes(new RouteBuilder() {
                @Override
                public void configure() throws Exception {
                    from(getStartingURI())
                            .process(getProcessors().get(0))
                            .to(getDestinationURI());
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public List<Processor> getProcessors() {
        if (null == processors) {
            processors = new ArrayList<>();
        }
        return processors;
    }

    public void addProcessor(Processor processor) {
        getProcessors().add(processor);
    }

    public String getDestinationURI() {
        if (null == destinationURI) {
            String projectHome = System.getProperty("project.home");
            String fileEndPointPath = null == defaultFilePath ? projectHome + "/reports" : defaultFilePath + "?fileName=" + defaultFileName + "&fileExist=Append";
            destinationURI = "file:" + fileEndPointPath;
        }
        return destinationURI;
    }

    public void setDestinationURI(String destinationURI) {
        this.destinationURI = destinationURI;
    }

    public String getStartingURI() {
        if (null == startingURI) {
            startingURI = "seda:messages";
        }
        return startingURI;
    }

    public void setStartingURI(String startingURI) {
        this.startingURI = startingURI;
    }

    public String getDefaultFilePath() {
        return defaultFilePath;
    }

    public void setDefaultFilePath(String defaultFilePath) {
        this.defaultFilePath = defaultFilePath;
    }

    public String getDefaultFileName() {
        return defaultFileName;
    }

    public void setDefaultFileName(String defaultFileName) {
        this.defaultFileName = defaultFileName;
    }

}
