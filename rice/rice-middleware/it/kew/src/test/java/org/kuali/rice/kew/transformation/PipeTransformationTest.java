/**
 * Copyright 2005-2013 The Kuali Foundation
 *
 * Licensed under the Educational Community License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.opensource.org/licenses/ecl2.php
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.kuali.rice.kew.transformation;

import java.io.FileOutputStream;

import javax.xml.transform.TransformerFactory;
import javax.xml.transform.URIResolver;
import javax.xml.transform.sax.SAXResult;
import javax.xml.transform.sax.SAXSource;
import javax.xml.transform.sax.SAXTransformerFactory;
import javax.xml.transform.sax.TransformerHandler;
import javax.xml.transform.stream.StreamSource;

import org.apache.xml.serializer.OutputPropertiesFactory;
import org.apache.xml.serializer.Serializer;
import org.apache.xml.serializer.SerializerFactory;
import org.junit.Test;
import org.kuali.rice.kew.test.KEWTestCase;
import org.xml.sax.InputSource;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.XMLReaderFactory;

public class PipeTransformationTest extends KEWTestCase {
	
	@Test public void testPipeUsingTemplate(){
		try{
			// Instantiate a TransformerFactory.
		  	TransformerFactory tFactory = TransformerFactory.newInstance();
		    // Determine whether the TransformerFactory supports The use uf SAXSource 
		    // and SAXResult
		    if (tFactory.getFeature(SAXSource.FEATURE) && tFactory.getFeature(SAXResult.FEATURE)){
		    	// Cast the TransformerFactory to SAXTransformerFactory.
		        SAXTransformerFactory saxTFactory = ((SAXTransformerFactory) tFactory);	  
		        // Get Relevant StyleSheets
		        URIResolver resolver = new WidgetURITestResolver();
		        saxTFactory.setURIResolver(resolver);
		        
		        /*
		        Document edlStyle,templateStyle;
		        edlStyle=XMLUtil.parseInputStream(PipeTransformationTest.class.getResourceAsStream("StudentInitiatedDrop_Style_pipeTest.xml"));
		        // Get Template Name
		        XPathFactory xPathFactory=XPathFactory.newInstance();
	            XPath xPath=xPathFactory.newXPath();
	            Node node=(Node)xPath.evaluate("//use[0]",edlStyle,XPathConstants.NODE);
	            //Node node=nodes.item(0);
	            if(node!=null){
	            	if(node.hasAttributes()){
	            		Node testAttri=node.getAttributes().getNamedItem("name");
	            		if(testAttri!=null){
	            			templateName=testAttri.getNodeValue();
	            		}else{
	            			//set default template as widgets
	            			templateName="widgets";
	            		}
	            	}
	            }
	           
	            System.out.println(templateName);
	             */
	            //templateStyle=XMLUtil.parseInputStream(PipeTransformationTest.class.getResourceAsStream("widgets_pipeTest.xml"));
		        // Create a TransformerHandler for each stylesheet.
		        TransformerHandler tHandler1 = saxTFactory.newTransformerHandler(new StreamSource(PipeTransformationTest.class.getResourceAsStream("StudentInitiatedDrop_Style_pipeTest.xml")));
		       // TransformerHandler tHandler2 = saxTFactory.newTransformerHandler(new StreamSource(PipeTransformationTest.class.getResourceAsStream("trans.xml")));
		        	//TransformerHandler tHandler3 = saxTFactory.newTransformerHandler(new StreamSource("foo3.xsl"));
		      
		        // Create an XMLReader.
		  	    XMLReader reader = XMLReaderFactory.createXMLReader();
		        reader.setContentHandler(tHandler1);
		        reader.setProperty("http://xml.org/sax/properties/lexical-handler", tHandler1);

		        //tHandler1.setResult(new SAXResult(tHandler2));
		        	//tHandler2.setResult(new SAXResult(tHandler3));

		        // transformer3 outputs SAX events to the serializer.
		        java.util.Properties xmlProps = OutputPropertiesFactory.getDefaultMethodProperties("xml");
		        xmlProps.setProperty("indent", "yes");
		        xmlProps.setProperty("standalone", "no");
		        Serializer serializer = SerializerFactory.getSerializer(xmlProps);
		        FileOutputStream transformedXML=new FileOutputStream("c://file.xml");
		        serializer.setOutputStream(transformedXML);
		        tHandler1.setResult(new SAXResult(serializer.asContentHandler()));

		  	    // Parse the XML input document. The input ContentHandler and output ContentHandler
		        // work in separate threads to optimize performance.   
		        reader.parse(new InputSource(PipeTransformationTest.class.getResourceAsStream("StudentInitiatedDrop.xml")));
		    }
			
		}catch(Exception ex){
			ex.printStackTrace();
		}
	}
	

}
