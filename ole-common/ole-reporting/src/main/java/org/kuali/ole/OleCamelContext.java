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
    private String filePath = "/kuali/main/local/reports?fileName=report.txt";

    private OleCamelContext() {
        context = new DefaultCamelContext();
        try {
            context.start();
            context.addRoutes(new RouteBuilder() {
                @Override
                public void configure() throws Exception {
                    String userHome = System.getProperty("user.home");
                    from("seda:messages")
                            .to("file:" + userHome + filePath + "&fileExist=Append");
                }
            });
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

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }
}
