package org.kuali.ole.ingest.krms.builder;

import com.thoughtworks.xstream.XStream;
import org.kuali.ole.docstore.model.xmlpojo.work.bib.marc.DataField;
import org.kuali.ole.docstore.model.xmlpojo.work.bib.marc.SubField;
import org.kuali.ole.docstore.model.xstream.work.bib.marc.DataFieldConverter;
import org.kuali.ole.docstore.model.xstream.work.bib.marc.SubFieldConverter;
import org.kuali.ole.ingest.ProfileAttributeConverter;
import org.kuali.ole.ingest.krms.pojo.*;
import org.kuali.ole.ingest.pojo.*;
import org.kuali.rice.krad.bo.PersistableBusinessObjectBase;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: vivekb
 * Date: 8/22/12
 * Time: 3:04 PM
 * To change this template use File | Settings | File Templates.
 */
public class OleKrmsObjectGeneratorFromXML {
    /**
     *  This method returns Krms object from fileContent.
     *  The xStream will convert the fileContent into Krms Object.
     * @param fileContent
     * @return Krms object
     * @throws java.net.URISyntaxException
     * @throws java.io.IOException
     */
    public OleKrms buildKrmsFromFileContent(String fileContent) throws URISyntaxException, IOException {
        XStream xStream = new XStream();
        xStream.alias("agendas", OleKrms.class);
        xStream.alias("agenda", OleKrmsAgenda.class);
        xStream.alias("rule", OleKrmsRule.class);
        xStream.alias("attribute", ProfileAttributeBo.class);
        xStream.alias("overlayOption", OverlayOption.class);
        xStream.alias("datafield", DataField.class);
        xStream.alias("subfield", SubField.class);
        xStream.alias("oleProposition",OleKrmsProposition.class);
        xStream.alias("simpleProposition",OleSimpleProposition.class);
        xStream.alias("compoundProposition",OleCompoundProposition.class);
        xStream.alias("action", OleKrmsAction.class);
        xStream.alias("value", OleValue.class);
        xStream.alias("term", OleTerm.class);
        xStream.alias("parameter",OleParameter.class);
        xStream.addImplicitCollection(OleKrms.class,"agendas");
        xStream.addImplicitCollection(OleCompoundProposition.class,"simplePropositions",OleSimpleProposition.class);
        xStream.addImplicitCollection(OleCompoundProposition.class,"compoundPropositions",OleCompoundProposition.class);
        xStream.addImplicitCollection(OleKrmsRuleAction.class,"krmsActions",OleKrmsAction.class);
        xStream.addImplicitCollection(OleKrmsRuleAction.class,"krmsRules",OleKrmsRule.class);
        xStream.addImplicitCollection(OleSimpleProposition.class,"terms",OleTerm.class);
        xStream.addImplicitCollection(OleKrmsAction.class,"parameters",OleParameter.class);
        xStream.registerConverter(new ProfileAttributeConverter());
        xStream.registerConverter(new DataFieldConverter());
        xStream.registerConverter(new SubFieldConverter());
        Object object = xStream.fromXML(fileContent);
        return (OleKrms) object;
    }
}
