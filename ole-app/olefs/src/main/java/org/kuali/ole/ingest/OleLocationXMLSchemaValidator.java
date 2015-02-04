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
 * This class provides schema validation for uploaded location xml
 */
public class OleLocationXMLSchemaValidator {
    //TODO: refactor the validateContentsAgainstSchema method to take only 1 parameter which is the file content.
    private static final String LOCATION_SCHEMA_FILE = "locations.xsd";

    /**
     *  This method returns True/False.
     *  This method  validate the inputStream content with W3C Xml Schema standard,if it matches it return True else return False.
     * @param inputStream
     * @return   boolean
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
     *  This method get the fileContent based on Location file schema.
     * @return locationXmlFile.
     */
    private InputStream getFileContents(){
        byte[] locationByteArray;
        ByteArrayInputStream locationXmlFile=null;
        try{
            locationByteArray = IOUtils.toByteArray(getClass().getResourceAsStream(LOCATION_SCHEMA_FILE));
            locationXmlFile = new ByteArrayInputStream(locationByteArray);
        }
        catch(Exception e){
            //TODO: logging required
        }
        return locationXmlFile;
    }

}
