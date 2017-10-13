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
package org.kuali.rice.core.framework.persistence.ojb;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.apache.ojb.broker.metadata.ConnectionRepository;
import org.apache.ojb.broker.metadata.DescriptorRepository;
import org.apache.ojb.broker.metadata.JdbcConnectionDescriptor;
import org.apache.ojb.broker.metadata.MetadataManager;
import org.kuali.rice.core.api.config.ConfigurationException;
import org.kuali.rice.core.api.config.property.Config;
import org.kuali.rice.core.api.config.property.ConfigContext;
import org.kuali.rice.core.api.lifecycle.BaseLifecycle;
import org.kuali.rice.core.api.util.ClassLoaderUtils;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.core.io.DefaultResourceLoader;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathFactory;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.util.Iterator;
import java.util.List;

/**
 * Base Ojb Configurer implementation which configures OJB for a particular rice module.
 *
 * @author Kuali Rice Team (rice.collab@kuali.org)
 */
public class BaseOjbConfigurer extends BaseLifecycle implements InitializingBean {

    private static final Logger LOG = Logger.getLogger(BaseOjbConfigurer.class);

    public static final String RICE_OJB_PROPERTIES_PARAM = "rice.custom.ojb.properties";
    public static final String OJB_PROPERTIES_PROP = "OJB.properties";

    /**
     * The OJB JCD aliases 
     */
    protected String[] jcdAliases;
    /**
     * The location of the OJB repository/metadata descriptor
     */
    protected String metadataLocation;

    protected List<String> additionalMetadataLocations;

    /**
     * No-arg constructor
     */
    public BaseOjbConfigurer() {
        // nothing
    }

    /**
     * Constructor that derives jcd aliases and repository metadata location from the module name
     * jcdAliases = [ moduleName.toLowerCase() + "DataSource" ]
     * metadataLocation = "classpath:OJB-repository-" + moduleName.toLowerCase() + ".xml";
     * 
     * @param moduleName the module name
     */
    public BaseOjbConfigurer(String moduleName) {
        this.metadataLocation = "classpath:org/kuali/rice/" + moduleName.toLowerCase() + "/config/OJB-repository-" + moduleName.toLowerCase() + ".xml";
        this.jcdAliases = new String[] { moduleName.toLowerCase() + "DataSource" };
    }

    /**
     * Constructor which takes the jcdAliases and metadata location
     * 
     * @param jcdAliases the jcd aliases
     * @param metadataLocation the metadata location
     */
    public BaseOjbConfigurer(String[] jcdAliases, String metadataLocation) {
        this.jcdAliases = jcdAliases;
        this.metadataLocation = metadataLocation;
    }

    @Override
    public void start() throws Exception {
        // if OJB has not already been loaded, let's trigger a load using our built-in OJB properties file
        String currentValue = System.getProperty(OJB_PROPERTIES_PROP);
        try {
            System.setProperty(OJB_PROPERTIES_PROP, getOjbPropertiesLocation());
            MetadataManager mm = MetadataManager.getInstance();
            establishConnectionMetaData(mm);
            establishRepositoryMetaData(mm);
        } finally {
            if (currentValue == null) {
                System.getProperties().remove(OJB_PROPERTIES_PROP);
            } else {
                System.setProperty(OJB_PROPERTIES_PROP, currentValue);
            }
        }
        super.start();
    }

    @Override
    public void stop() throws Exception {
        super.stop();
    }



    protected String getOjbPropertiesLocation() {
        String ojbPropertiesLocation = ConfigContext.getCurrentContextConfig().getProperty(RICE_OJB_PROPERTIES_PARAM);
        return ojbPropertiesLocation;
    }

    protected void establishConnectionMetaData(MetadataManager mm) throws Exception {
        String connMetadata = getMetadataLocation();
        if (StringUtils.isBlank(connMetadata)) {
            LOG.info("No OJB connection metadata loaded.");
            return;
        }
        if (!isConnectionAlreadyConfigured(mm)) {
            LOG.info("Loading OJB Connection Metadata from " + connMetadata);
            DefaultResourceLoader resourceLoader = new DefaultResourceLoader(ClassLoaderUtils.getDefaultClassLoader());
            InputStream is = resourceLoader.getResource(connMetadata).getInputStream();
            is = preprocessConnectionMetadata(is);
            ConnectionRepository cr = mm.readConnectionRepository(is);
            mm.mergeConnectionRepository(cr);
            try {
                is.close();
            } catch (Exception e) {
                LOG.warn("Failed to close stream to file " + connMetadata, e);
            }
        } else {
            LOG.info("OJB Connections already configured for jcd aliases '" + StringUtils.join(getJcdAliases(), ", ") + "', skipping Metadata merge.");
        }
    }

    protected InputStream preprocessConnectionMetadata(InputStream inputStream) throws Exception {
        Document document = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(new InputSource(inputStream));
        XPath xpath = XPathFactory.newInstance().newXPath();
        NodeList connectionDescriptors = (NodeList)xpath.evaluate("/descriptor-repository/jdbc-connection-descriptor", document, XPathConstants.NODESET);
        for (int index = 0; index < connectionDescriptors.getLength(); index++) {
            Element descriptor = (Element)connectionDescriptors.item(index);
            String currentPlatform = descriptor.getAttribute("platform");
            if (StringUtils.isBlank(currentPlatform)) {
                String ojbPlatform = ConfigContext.getCurrentContextConfig().getProperty(Config.OJB_PLATFORM);
                if (StringUtils.isEmpty(ojbPlatform)) {
                    throw new ConfigurationException("Could not configure OJB, the '" + Config.OJB_PLATFORM + "' configuration property was not set.");
                }
                LOG.info("Setting OJB connection descriptor database platform to '" + ojbPlatform + "'");
                descriptor.setAttribute("platform", ojbPlatform);
            }
        }
        Transformer transformer = TransformerFactory.newInstance().newTransformer();
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        transformer.transform(new DOMSource(document), new StreamResult(new BufferedOutputStream(baos)));
        return new BufferedInputStream(new ByteArrayInputStream(baos.toByteArray()));
    }

    @SuppressWarnings("unchecked")
	protected boolean isConnectionAlreadyConfigured(MetadataManager mm) {
        List descriptors = mm.connectionRepository().getAllDescriptor();
        for (Iterator iterator = descriptors.iterator(); iterator.hasNext();) {
            JdbcConnectionDescriptor descriptor = (JdbcConnectionDescriptor) iterator.next();
            for (String jcdAlias : getJcdAliases()) {
                if (descriptor.getJcdAlias().equals(jcdAlias)) {
                    return true;
                }
            }
        }
        return false;
    }

    protected InputStream preprocessRepositoryMetadata(InputStream inputStream) throws Exception {
        return inputStream;
    }

    protected void establishRepositoryMetaData(MetadataManager mm) throws Exception {
        String repoMetadata = getMetadataLocation();
        if (StringUtils.isBlank(repoMetadata)) {
            LOG.info("No OJB repository metadata loaded.");
            return;
        }
        LOG.info("Loading OJB Metadata from " + repoMetadata);

        DefaultResourceLoader resourceLoader = new DefaultResourceLoader(ClassLoaderUtils.getDefaultClassLoader());
        InputStream is = resourceLoader.getResource(repoMetadata).getInputStream();
        is = preprocessRepositoryMetadata(is);
        DescriptorRepository dr = mm.readDescriptorRepository(is);
        mm.mergeDescriptorRepository(dr);

        try {
            is.close();
        } catch (Exception e) {
            LOG.warn("Failed to close stream to file " + repoMetadata, e);
        }

        if (additionalMetadataLocations != null) {
            for (String metadataLocation : additionalMetadataLocations) {
                LOG.info("Loading OJB Metadata from " + metadataLocation);

                InputStream is2 = resourceLoader.getResource(metadataLocation).getInputStream();
                is2 = preprocessRepositoryMetadata(is2);
                DescriptorRepository dr2 = mm.readDescriptorRepository(is2);
                mm.mergeDescriptorRepository(dr2);

                try {
                    is2.close();
                } catch (Exception e) {
                    LOG.warn("Failed to close stream to file " + metadataLocation, e);
                }
            }
        }
    }

    /**
     * Return the jcd alias of the connection loaded by the connection metadata.
     * The default implementation returns the jcd alias with which the instance was created (if any) 
     * @return the jcd alias of the connection loaded by the connection metadata.
     */
    protected String[] getJcdAliases() {
        return jcdAliases;
    }

    /**
     * Should return a String representing the location of a file to load OJB connection and
     * repository metadata from.  If null or empty than no metadata will be loaded.
     * The default implementation returns the metadata location with which the instance was created (if any)
     */
    protected String getMetadataLocation() {
        return metadataLocation;
    }

	/***
	 * @see org.springframework.beans.factory.InitializingBean#afterPropertiesSet()
	 */
	public void afterPropertiesSet() throws Exception {
		this.start();
	}

	/**
	 * @param jcdAliases the jcdAliases to set
	 */
	public void setJcdAliases(String[] jcdAliases) {
		this.jcdAliases = jcdAliases;
	}

	/**
	 * @param metadataLocation the metadataLocation to set
	 */
	public void setMetadataLocation(String metadataLocation) {
		this.metadataLocation = metadataLocation;
	}

    /**
     * List of additional OJB descriptor files to include with the connect
     *
     * @return List<String> list of ojb files
     */
    public List<String> getAdditionalMetadataLocations() {
        return additionalMetadataLocations;
    }

    /**
     * Setter for additional ojb metadata files
     *
     * @param additionalMetadataLocations
     */
    public void setAdditionalMetadataLocations(List<String> additionalMetadataLocations) {
        this.additionalMetadataLocations = additionalMetadataLocations;
    }
}
