/**
 * Copyright 2005-2014 The Kuali Foundation
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
package org.kuali.rice.ken.util;

import java.io.StringReader;
import java.io.StringWriter;
import java.util.HashMap;
import java.util.Iterator;

import javax.xml.transform.ErrorListener;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;

import org.apache.log4j.Logger;

/**
 * This class handles XSLT transformations.
 * @author Kuali Rice Team (rice.collab@kuali.org)
 */
public class ContentTransformer {
    private static final Logger LOG = Logger.getLogger(ContentTransformer.class);

    private static final class LoggingErrorListener implements ErrorListener {
        private final ErrorListener delegate;
        public LoggingErrorListener(ErrorListener delegate) {
            this.delegate = delegate;
        }

        public void error(TransformerException exception) throws TransformerException {
            LOG.error("Error transforming document", exception);
        }

        public void fatalError(TransformerException exception) throws TransformerException {
            if (delegate != null) {
                delegate.fatalError(exception);
            } else {
                throw exception;
            }
            
        }

        public void warning(TransformerException exception) throws TransformerException {
            LOG.warn("Error transforming document", exception);
        }
        
    };

    private Transformer transformer;

    /**
     * Constructs a ContentTransformer.java.
     * @param aStyleSheet
     * @throws Exception
     */
    public ContentTransformer(StreamSource aStyleSheet) throws Exception {
        // create transformer        
        TransformerFactory factory = TransformerFactory.newInstance();
        transformer = factory.newTransformer( aStyleSheet );
    }

    /**
     * Constructs a ContentTransformer.java.
     * @param aStyleSheet
     * @param parametermap
     * @throws Exception
     */
    public ContentTransformer(StreamSource aStyleSheet, HashMap parametermap) throws Exception {
       // create transformer
       TransformerFactory factory = TransformerFactory.newInstance();
       transformer = factory.newTransformer( aStyleSheet );
       Iterator iter = parametermap.keySet().iterator();
       while (iter.hasNext()) {
          Object o = iter.next();
          String param = o.toString();
          String value = (String) parametermap.get(param);
          transformer.setParameter(param, value);
       }
       transformer.setErrorListener(new LoggingErrorListener(transformer.getErrorListener()));
    }

    /**
     * This method performs the actual transformation.
     * @param xml
     * @return
     * @throws Exception
     */
    public String transform(String xml) throws Exception {

        // perform transformation
        Source xmlsource = new StreamSource(new StringReader(xml));
        StringWriter sout = new StringWriter();
         
        transformer.transform(xmlsource, new StreamResult(sout));

        // return resulting document
        return sout.toString();
    }
}
