package org.kuali.ole.ingest;

import org.apache.commons.io.IOUtils;
import org.kuali.ole.exception.ParseException;
import org.kuali.ole.exception.XmlErrorHandler;
import org.xml.sax.SAXException;

import javax.xml.XMLConstants;
import javax.xml.transform.Source;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import javax.xml.validation.Validator;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * ProfileXMLSchemaValidator is for validating ProfileAttributeSchema against W3C Xml Schema standards
 */
public class ProfileXMLSchemaValidator {

    private static final String PROFILE_SCHEMA_FILE = "profileBuilder.xsd";

    /**
     * This method returns True/False
     * This method check the ProfileAttributeSchema against W3C Xml Schema,If it matches it return True else return False.
     * @param inputStream
     * @return  boolean
     * @throws org.kuali.ole.exception.ParseException
     * @throws java.io.IOException
     * @throws org.xml.sax.SAXException
     */
    public boolean validateContentsAgainstSchema(InputStream inputStream)
            throws ParseException, IOException, SAXException {
        try {
                SchemaFactory factory = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
                Source schemaSource = null;
                schemaSource = new StreamSource(getFileContents());
                Schema schema = null;
                schema = factory.newSchema(schemaSource);
                Validator validator = schema.newValidator();
                validator.setErrorHandler(new XmlErrorHandler());
                validator.validate(new StreamSource(inputStream));
                return true;
        }
        catch(Exception ex){
           //TODO: logging required
        }
        return false;
    }

    /**
     *  This method returns fileContent as InputStream.
     *  This method get the fileContent based on Profile schema file.
     * @return  profileXmlFile
     */
    private InputStream getFileContents(){
        byte[] profileByteArray;
        ByteArrayInputStream profileXmlFile=null;
        try{
                profileByteArray = IOUtils.toByteArray(getClass().getResourceAsStream(PROFILE_SCHEMA_FILE));
                profileXmlFile = new ByteArrayInputStream(profileByteArray);
        }
        catch(Exception e){
          //TODO: logging required
        }
        return profileXmlFile;
    }
}
