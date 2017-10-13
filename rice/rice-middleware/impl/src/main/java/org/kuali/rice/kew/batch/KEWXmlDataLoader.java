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
package org.kuali.rice.kew.batch;

import org.apache.commons.io.IOUtils;
import org.kuali.rice.core.api.CoreApiServiceLocator;
import org.kuali.rice.core.api.config.ConfigurationException;
import org.kuali.rice.core.api.impex.xml.FileXmlDocCollection;
import org.kuali.rice.core.api.impex.xml.XmlDoc;
import org.kuali.rice.core.api.impex.xml.XmlDocCollection;
import org.kuali.rice.kew.api.WorkflowRuntimeException;
import org.springframework.core.io.DefaultResourceLoader;
import org.springframework.core.io.Resource;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;


/**
 * This is a description of what this class does - arh14 don't forget to fill this in. 
 * 
 * @author Kuali Rice Team (rice.collab@kuali.org)
 *
 */
public class KEWXmlDataLoader {
    /*protected void loadXmlFile(String fileName) {
        if (fileName.indexOf('/') < 0) {
            this.loadXmlFile(getClass(), fileName);
        } else {
            loadXmlStream(getClass().getClassLoader().getResourceAsStream(fileName));
        }
    }*/

    /**
     * Loads the XML specified by the resource string, which should be in Spring resource notation
     * @param resource resource string in Spring resource notation
     * @throws Exception 
     */
    public static void loadXmlResource(String resource) throws Exception {
        Resource res = new DefaultResourceLoader().getResource(resource);
        InputStream xmlFile = res.getInputStream();
        if (xmlFile == null) {
            throw new ConfigurationException("Didn't find resource " + resource);
        }
        try {
            loadXmlStream(xmlFile);
        } finally {
            xmlFile.close();
        }

    }

    /**
     * Loads the XML resource from the classloader, from the package of the specified class
     * if the class appears relative, or from the root of the classloader if it contains a slash
     * @param clazz the class whose package should be used to qualify the path
     * @param path the package-relative path of the XML resource
     * @throws Exception
     */
    public static void loadXmlClassLoaderResource(Class clazz, String path) throws Exception {
        if (path.indexOf('/') < 0) {
            loadXmlPackageResource(clazz, path);
        } else {
            loadXmlClassLoaderResource(clazz.getClassLoader(), path);
        }
    }

    /**
     * Loads the XML resource from the classloader, from the package of the specified class.
     * @param clazz the class whose package should be used to qualify the path
     * @param path the package-relative path of the XML resource
     * @throws Exception
     */
    public static void loadXmlPackageResource(Class clazz, String path) throws Exception {
        InputStream xmlFile = clazz.getResourceAsStream(path);
        if (xmlFile == null) {
            throw new WorkflowRuntimeException("Didn't find resource " + path);
        }
        try {
            loadXmlStream(xmlFile);
        } finally {
            xmlFile.close();
        }
    }

    /**
     * Loads the XML resource from the specified classloader
     * @param classloader the classloader from which to load the resource
     * @param path the classloader path of the XML resource
     * @throws Exception
     */
    public static void loadXmlClassLoaderResource(ClassLoader classloader, String path) throws Exception {
        InputStream xmlFile = classloader.getResourceAsStream(path);
        if (xmlFile == null) {
            throw new WorkflowRuntimeException("Didn't find resource " + path);
        }
        try {
            loadXmlStream(xmlFile);
        } finally {
            xmlFile.close();
        }
    }

    /**
     * Load the XML file from the file system.
     * 
     * @param fileName the path to the XML file
     * @throws Exception
     */
    public static void loadXmlFile(String fileName) throws Exception {
        FileInputStream fis = new FileInputStream(fileName);
        try {
            loadXmlStream(fis);
        } finally {
            fis.close();
        }
    }

    /**
     * Loads XML from a stream
     * @param xmlStream the XML byte stream
     * @throws Exception
     */
    public static void loadXmlStream(InputStream xmlStream) throws Exception {
       List<XmlDocCollection> xmlFiles = new ArrayList<XmlDocCollection>();
        XmlDocCollection docCollection = getFileXmlDocCollection(xmlStream, "UnitTestTemp");
        //XmlDocCollection docCollection = new StreamXmlDocCollection(xmlStream);
        xmlFiles.add(docCollection);
        CoreApiServiceLocator.getXmlIngesterService().ingest(xmlFiles);
        for (XmlDoc doc: docCollection.getXmlDocs()) {
            if (!doc.isProcessed()) {
                throw new RuntimeException("Failed to ingest xml doc: " + doc.getName());
            }
        }
    }

    /**
     * Helper method that turns a stream into a FileXmlDocCollection by first making a copy on the file system.
     * @param xmlFile
     * @param tempFileName
     * @return
     * @throws IOException
     */
    public static FileXmlDocCollection getFileXmlDocCollection(InputStream stream, String tempFileName) throws IOException {
        if (stream == null) {
            throw new RuntimeException("Stream is null!");
        }

        File temp = File.createTempFile(tempFileName, ".xml");
        temp.deleteOnExit();

        FileOutputStream fos = new FileOutputStream(temp);
        try {
            IOUtils.copy(stream, fos);
        } finally {
            fos.close();
        }

        return new FileXmlDocCollection(temp);
    }
}
