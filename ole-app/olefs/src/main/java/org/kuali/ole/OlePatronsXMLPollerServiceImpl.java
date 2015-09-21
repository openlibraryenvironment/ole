package org.kuali.ole;

import org.kuali.rice.core.api.impex.xml.XmlDocCollection;
import org.kuali.rice.core.api.impex.xml.XmlIngesterService;

import java.io.File;
import java.io.FileWriter;
import java.util.List;

/**
 * Created by pvsubrah on 12/9/13.
 */
public class OlePatronsXMLPollerServiceImpl extends OleXmlPollerServiceImpl{
    private static final org.apache.log4j.Logger LOG = org.apache.log4j.Logger.getLogger(OlePatronsXMLPollerServiceImpl.class);
    private PatronsIngesterService patronsIngesterService;

    @Override
    protected XmlIngesterService getIngesterService() {
        return patronsIngesterService;
    }

    public void setPatronsIngesterService(PatronsIngesterService patronsIngesterService) {
        this.patronsIngesterService = patronsIngesterService;
    }

    @Override
    protected void processReportContent(List<XmlDocCollection> collections, String loadTime) {
        StringBuffer reportContent = new StringBuffer();
        for (XmlDocCollection xmlDocCollection : collections) {
            if (xmlDocCollection.getXmlDocs() != null && xmlDocCollection.getXmlDocs().size() > 0) {
                reportContent.append(xmlDocCollection.getXmlDocs().get(0).getProcessingMessage());
            }
        }
        if (reportContent.length() > 0 && getXmlReportLocation() != null) {
            File reportDir = new File(getXmlReportLocation());
            if (reportDir.isDirectory() || (!reportDir.isDirectory() && reportDir.mkdirs())) {
                try {
                    FileWriter out = new FileWriter(reportDir + "/" + loadTime + ".txt");
                    out.write(reportContent.toString());
                    out.close();
                } catch (Exception e) {
                    LOG.error("Unable to create patron reports file");
                }
            } else {
                LOG.error("Unable to create patron reports directory " + reportDir.getPath());
            }
        }
    }

    public PatronsIngesterService getPatronsIngesterService() {
        return patronsIngesterService;
    }
}
