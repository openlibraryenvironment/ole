package org.kuali.ole.ingest;

import com.thoughtworks.xstream.XStream;
import org.kuali.ole.ingest.pojo.*;

import java.io.IOException;
import java.net.URISyntaxException;

/**
 * KrmsObjectGeneratorFromXML is for converting the string fileContent into Krms object
 */
public class KrmsObjectGeneratorFromXML {
    /**
     *  This method returns Krms object from fileContent.
     *  The xStream will convert the fileContent into Krms Object.
     * @param fileContent
     * @return Krms object
     * @throws java.net.URISyntaxException
     * @throws java.io.IOException
     */
    public Krms buildKrmsFromFileContent(String fileContent) throws URISyntaxException, IOException {
        XStream xStream = new XStream();
        xStream.alias("krms", Krms.class);
        xStream.alias("agenda", OleAgenda.class);
        xStream.alias("attribute", ProfileAttributeBo.class);
        xStream.alias("rule", KrmsRule.class);
        xStream.alias("proposition",KrmsProposition.class);
        xStream.alias("compoundProposition",OleProposition.class);
        xStream.alias("action", KrmsAction.class);
        xStream.addImplicitCollection(Krms.class,"oleAgendaList");
        xStream.addImplicitCollection(OleProposition.class,"propositions",KrmsProposition.class);
        xStream.addImplicitCollection(OleProposition.class,"olePropositions",OleProposition.class);
        xStream.registerConverter(new ProfileAttributeConverter());
        Object object = xStream.fromXML(fileContent);
        return (Krms) object;
    }
}
