package org.kuali.ole;

import org.apache.camel.CamelContext;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.impl.DefaultCamelContext;

/**
 * Created by pvsubrah on 1/10/15.
 */
public class OleCamelContext {
    public static OleCamelContext oleCamelContext;
    private CamelContext context;
    private String defaultFilePath;
    private String defaultFileName;

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
        try {
            context.addRoutes(new RouteBuilder() {
                @Override
                public void configure() throws Exception {
                    String projectHome = System.getProperty("project.home");
                    String fileEndPointPath = null == defaultFilePath ? projectHome+"/reports" : defaultFilePath + "?fileName=" + defaultFileName + "&fileExist=Append";
                    from("seda:messages")
                            .to("file:" + fileEndPointPath);
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
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
