package org.kuali.ole.gobi.request;

import org.kuali.ole.exception.XmlErrorHandler;

import javax.xml.XMLConstants;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import javax.xml.validation.Validator;
import java.io.StringReader;
import java.net.URL;

public class GobiRequestValidator {
    private static final org.apache.log4j.Logger LOG = org.apache.log4j.Logger.getLogger(GobiRequestValidator.class);
    private static final String GOBI_SCHEMA_FILE = "GOBIAPI.OutboundOrder.xsd";
    private static final String MARC21_SCHEMA_FILE = "GOBIAPI.OutboundOrder.xsd";

    public boolean validate(String body) {
        try {
            SchemaFactory factory = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
            URL xsdUrlForGOBISchema = this.getClass().getResource(GOBI_SCHEMA_FILE);
            URL xsdUrlForMARC21Schema = this.getClass().getResource(MARC21_SCHEMA_FILE);

            SchemaFactory schemaFactory = factory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
            String W3C_XSD_TOP_ELEMENT =
                    "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?>\n"
                            + "<xs:schema xmlns:xs=\"http://www.w3.org/2001/XMLSchema\" elementFormDefault=\"qualified\">\n"
                            + "<xs:include schemaLocation=\"" + xsdUrlForGOBISchema.getPath() + "\"/>\n"
                            + "<xs:include schemaLocation=\"" + xsdUrlForMARC21Schema.getPath() + "\"/>\n"
                            + "</xs:schema>";
            Schema schema = schemaFactory.newSchema(new StreamSource(new StringReader(W3C_XSD_TOP_ELEMENT), "xsdTop"));
            Validator validator = schema.newValidator();
            validator.setErrorHandler(new XmlErrorHandler());
//            validator.validate(new StreamSource(new StringReader(body)));
            return true;
        } catch (Exception ex) {
            LOG.error(ex.getMessage());
        }
        return false;
    }
}

