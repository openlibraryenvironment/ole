package org.kuali.ole;

import org.kuali.ole.ingest.FileUtil;
import org.kuali.ole.ingest.krms.builder.OleKrmsBuilder;
import org.kuali.rice.core.api.impex.xml.XmlDocCollection;
import org.kuali.rice.core.api.impex.xml.XmlIngesterService;
//import sun.management.resources.agent;

import java.io.File;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by pvsubrah on 12/6/13.
 */
public class CircPoliciesIngesterService implements XmlIngesterService {
    protected OleKrmsBuilder circPolicyBuilder;

    @Override
    public Collection<XmlDocCollection> ingest(List<XmlDocCollection> xmlDocCollections) throws Exception {
        boolean failedRecords = false;
        for (Iterator<XmlDocCollection> iterator = xmlDocCollections.iterator(); iterator.hasNext(); ) {
            XmlDocCollection xmlDocCollection = iterator.next();
            File file = xmlDocCollection.getFile();
            String fileContent = new FileUtil().readFile(file);

            List<String> agendas = circPolicyBuilder.persistKrmsFromFileContent(fileContent);
            failedRecords = !(agendas.size() > 0);

        }
        if (!failedRecords) {
            return new LinkedList<>();
        }
        return xmlDocCollections;
    }

    @Override
    public Collection<XmlDocCollection> ingest(List<XmlDocCollection> xmlDocCollections, String principalId) throws Exception {
        return null;
    }

    public OleKrmsBuilder getCircPolicyBuilder() {
        return circPolicyBuilder;
    }

    public void setCircPolicyBuilder(OleKrmsBuilder circPolicyBuilder) {
        this.circPolicyBuilder = circPolicyBuilder;
    }
}
