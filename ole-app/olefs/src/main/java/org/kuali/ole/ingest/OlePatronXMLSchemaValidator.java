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
import java.net.MalformedURLException;

/**
 * OlePatronXMLSchemaValidator is for schema validation against W3C Xml Schema standards
 */
public class OlePatronXMLSchemaValidator {
    //TODO: refactor the validateContentsAgainstSchema method to take only 1 parameter which is the file content.
    private static final org.apache.log4j.Logger LOG = org.apache.log4j.Logger.getLogger(OlePatronXMLSchemaValidator.class);
    private static final String PATRON_SCHEMA_FILE = "olePatronRecord.xsd";

    /**
     *   This method returns True/False.
     *   This method validate the patron xml schema against W3C Xml Schema standards,If it matches it return True else return False.
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
            LOG.error(ex.getMessage());
        }
        return false;
    }

    /**
     *  This method returns fileContent as InputStream.
     *  This method get the fileContent based on Patron schema file.
     * @return  patronXmlFile
     */
    private InputStream getFileContents(){
        byte[] patronByteArray;
        ByteArrayInputStream patronXmlFile=null;
        try{
            patronByteArray = IOUtils.toByteArray(getClass().getResourceAsStream(PATRON_SCHEMA_FILE));
            patronXmlFile = new ByteArrayInputStream(patronByteArray);
        }
        catch(Exception e){
            LOG.error(e.getMessage());
        }
        return patronXmlFile;
    }

}
