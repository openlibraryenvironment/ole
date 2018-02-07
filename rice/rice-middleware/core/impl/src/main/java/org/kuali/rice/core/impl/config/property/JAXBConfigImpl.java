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
package org.kuali.rice.core.impl.config.property;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Random;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import javax.xml.bind.UnmarshallerHandler;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParserFactory;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.kuali.rice.core.api.config.ConfigurationException;
import org.kuali.rice.core.api.config.property.Config;
import org.kuali.rice.core.api.util.RiceUtilities;
import org.kuali.rice.core.framework.config.property.AbstractBaseConfig;
import org.kuali.rice.core.util.ImmutableProperties;
import org.xml.sax.Attributes;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLFilter;
import org.xml.sax.helpers.XMLFilterImpl;

/**
 * This implementation of the Config interface uses JAXB to parse the config file and maintains an
 * internal copy of all properties in their "raw" form (without any nested properties resolved).
 * This allows properties to be added in stages and still alter values of properties previously read
 * in. It also has settings for whether system properties should override all properties or only
 * serve as default when the property has not been defined.
 * 
 * @author Kuali Rice Team (rice.collab@kuali.org)
 * 
 */
public class JAXBConfigImpl extends AbstractBaseConfig {

    private static final Logger LOG = Logger.getLogger(JAXBConfigImpl.class);

    private static final String IMPORT_NAME = "config.location";
    private static final String INDENT = "  ";
    private static final String PLACEHOLDER_REGEX = "\\$\\{([^{}]+)\\}";

    // keep the same random
    private static final Random RANDOM = new Random();

    private final List<String> fileLocs = new ArrayList<String>();

    private final Map<String, Object> objects = new LinkedHashMap<String, Object>();
    private final Properties rawProperties = new Properties();
    private final Properties resolvedProperties = new Properties();

    // compile pattern for regex once
    private final Pattern pattern = Pattern.compile(PLACEHOLDER_REGEX);

    private boolean systemOverride;

    public JAXBConfigImpl() {}

    public JAXBConfigImpl(Config config) {
        this.copyConfig(config);
    }

    public JAXBConfigImpl(String fileLoc, Config config) {
        this.copyConfig(config);
        this.fileLocs.add(fileLoc);
    }

    public JAXBConfigImpl(List<String> fileLocs, Config config) {
        this.copyConfig(config);
        this.fileLocs.addAll(fileLocs);

    }

    public JAXBConfigImpl(String fileLoc) {
        this.fileLocs.add(fileLoc);
    }

    public JAXBConfigImpl(List<String> fileLocs) {
        this.fileLocs.addAll(fileLocs);
    }

    public JAXBConfigImpl(Properties properties) {
        this.putProperties(properties);
    }

    public JAXBConfigImpl(String fileLoc, Properties properties) {
        this.fileLocs.add(fileLoc);
        this.putProperties(properties);
    }

    public JAXBConfigImpl(List<String> fileLocs, Properties properties) {
        this.fileLocs.addAll(fileLocs);
        this.putProperties(properties);
    }

    /*****************************************************/

    /*
     * We need the ability to take a config object and copy the raw + cached data into this config object.
     */
    private void copyConfig(Config config) {
        if (config == null) {
            return;
        }

        this.putProperties(config.getProperties());

        if (config.getObjects() != null) {
            this.objects.putAll(config.getObjects());
        }
    }

    @Override
    public Object getObject(String key) {
        return objects.get(key);
    }

    @Override
    public Map<String, Object> getObjects() {
        return Collections.unmodifiableMap(objects);
    }

    @Override
    public Properties getProperties() {
        return new ImmutableProperties(resolvedProperties);
    }

    @Override
    public String getProperty(String key) {
        return resolvedProperties.getProperty(key);
    }

    /**
     * 
     * This overrides the property. Takes the place of the now deprecated overrideProperty
     * 
     * @see Config#putProperty(java.lang.String, java.lang.String)
     */
    @Override
    public void putProperty(String key, String value) {
        this.setProperty(key, replaceVariable(key, value));
        resolveRawToCache();
    }

    @Override
    public void putProperties(Properties properties) {
        // Nothing to do
        if (properties == null) {
            return;
        }

        // Cycle through the keys, using Rice's convention for expanding variables as we go
        replaceVariables(properties);

        // Still need to resolve placeholders in addition to expanding variables
        resolveRawToCache();
    }

    /**
     * Expand variables and invoke this.setProperty() for each property in the properties object
     * passed in
     */
    protected void replaceVariables(Properties properties) {
        SortedSet<String> keys = new TreeSet<String>(properties.stringPropertyNames());
        for (String key : keys) {
            String originalValue = properties.getProperty(key);
            String replacedValue = replaceVariable(key, originalValue);
            logPropertyChange("", key, null, originalValue, replacedValue);
            this.setProperty(key, replacedValue);
        }
    }

    protected Unmarshaller getUnmarshaller() {
        try {
            Class<org.kuali.rice.core.impl.config.property.Config> c = org.kuali.rice.core.impl.config.property.Config.class;
            JAXBContext jaxbContext = JAXBContext.newInstance(c);
            return jaxbContext.createUnmarshaller();
        } catch (JAXBException e) {
            throw new ConfigurationException("Error initializing JAXB for config", e);
        }
    }

    @Override
    public void parseConfig() throws IOException {

        LOG.info("----------------Loading Rice Configuration----------------");

        if (fileLocs.isEmpty()) {
            // Nothing to do
            LOG.info("No config files specified");
            return;
        }

        // Get a reference to an unmarshaller
        Unmarshaller unmarshaller = getUnmarshaller();

        // Add host.ip and host.name
        configureBuiltIns();

        // Parse all of the indicated config files, but do not resolve any right hand side variables
        for (String s : fileLocs) {
            parseConfig(s, unmarshaller, 0);
        }

        // now that all properties have been loaded, resolve the right hand side from
        // the raw properties into the resolved properties. This will also replace properties
        // defined in the files with system properties if systemOverride==true.
        resolveRawToCache();

        LOG.info("----------------Rice Configuration Loaded-----------------");
        logPropertyValues(resolvedProperties);
    }

    protected void logPropertyValues(Properties p) {
        LOG.info("Loaded " + p.size() + " properties");
        if (LOG.isDebugEnabled()) {
            String s = getPropertyValuesAsString(p);
            LOG.debug("Displaying " + p.size() + " properties\n\n" + s + "\n");
        }
    }

    protected String getPropertyValuesAsString(Properties p) {
        StringBuilder sb = new StringBuilder();
        SortedSet<String> keys = new TreeSet<String>(p.stringPropertyNames());
        for (String key : keys) {
            String rawValue = p.getProperty(key);
            String logValue = flatten(ConfigLogger.getDisplaySafeValue(key, rawValue));
            sb.append(key);
            sb.append("=");
            sb.append("[");
            sb.append(logValue);
            sb.append("]\n");
        }
        return sb.toString();
    }

    protected String flatten(String s) {
        if (s == null) {
            return null;
        } else {
            return s.replace("\n", " ").replace("\r", " ");
        }
    }

    protected InputStream getInputStream(String filename) throws IOException {
        // have to check for empty filename because getResource will return non-null if passed ""
        if (StringUtils.isNotEmpty(filename)) {
            return RiceUtilities.getResourceAsStream(filename);
        } else {
            return null;
        }
    }

    protected void parseConfig(String filename, Unmarshaller unmarshaller, int depth) throws IOException {

        // Open an InputStream to the resource
        InputStream in = getInputStream(filename);

        // Setup an indentation prefix based on the recursive depth
        final String prefix = StringUtils.repeat(INDENT, depth);

        // If we couldn't open an input stream we are done
        if (in == null) {
            LOG.warn(prefix + "+ Skipping non-existent location [" + filename + "]");
            return;
        }

        // Load properties from the InputStream
        if (isPropertiesFile(filename)) {
            // Handle normal Java .properties file
            loadProperties(in, prefix, filename);
        } else {
            // Handle Rice style XML files (These are not in the same format as Java XML properties files)
            loadRiceXML(in, prefix, filename, depth, unmarshaller);
        }
    }

    protected void loadRiceXML(InputStream in, String prefix, String filename, int depth, Unmarshaller unmarshaller)
            throws IOException {
        LOG.info(prefix + "+ Parsing config: [" + filename + "]");
        org.kuali.rice.core.impl.config.property.Config config = unmarshalQuietly(unmarshaller, in);
        for (Param p : config.getParamList()) {
            if (p.getName().equals(IMPORT_NAME)) {
                doImport(p, unmarshaller, depth);
            } else if (p.isSystem()) {
                doSystem(p);
            } else if (p.isOverride() || !rawProperties.containsKey(p.getName())) {
                doSetProperty(p);
            }
        }
        LOG.debug(prefix + "- Parsed config: " + filename);
    }

    protected void loadProperties(InputStream in, String prefix, String filename) throws IOException {
        LOG.info(prefix + "+ Loading properties: [" + filename + "]");
        Properties properties = new Properties();
        properties.load(in);
        replaceVariables(properties);
    }

    protected boolean isPropertiesFile(String filename) {
        String lower = StringUtils.lowerCase(filename);
        return StringUtils.endsWith(lower, ".properties");
    }

    protected void doSetProperty(Param p) {
        String name = p.getName();
        if (p.isRandom()) {
            String randStr = String.valueOf(generateRandomInteger(p.getValue()));
            this.setProperty(p.getName(), randStr);
            LOG.info("generating random string " + randStr + " for property " + p.getName());
        } else {
            /*
             * myProp = dog We have a case where you might want myProp = ${myProp}:someOtherStuff:${foo} This would normally overwrite the existing myProp with
             * ${myProp}:someOtherStuff:${foo} but what we want is: myProp = dog:someOtherStuff:${foo} so that we put the existing value of myProp into the new value. Basically how
             * path works.
             */
            String value = replaceVariable(name, p.getValue());

            this.setProperty(name, value);
        }
    }

    protected void doSystem(Param p) {
        // If override is false and the system property is already set, we can't override it
        boolean skip = !p.isOverride() && System.getProperty(p.getName()) != null;
        if (skip) {
            return;
        }

        // Set both a system property and a local config property
        String name = p.getName();
        if (p.isRandom()) {
            String randStr = String.valueOf(generateRandomInteger(p.getValue()));
            System.setProperty(name, randStr);
            this.setProperty(p.getName(), randStr);
            LOG.info("generating random string " + randStr + " for system property " + p.getName());
        } else {
            // resolve and set system params immediately so they can override
            // existing system params. Add to rawProperties resolved as well to
            // prevent possible mismatch
            HashSet<String> set = new HashSet<String>();
            set.add(p.getName());
            String value = parseValue(p.getValue(), set);
            System.setProperty(name, value);
            this.setProperty(name, value);
        }
    }

    protected void doImport(Param p, Unmarshaller unmarshaller, int depth) throws IOException {
        String configLocation = StringUtils.trim(parseValue(p.getValue(), new HashSet<String>()));
        parseConfig(configLocation, unmarshaller, depth + 1);
    }

    /*
     * This will set the property. No logic checking so what you pass in gets set. We use this as a focal point for debugging the raw config changes.
     */
    protected void setProperty(String name, String value) {
        String oldValue = rawProperties.getProperty(name);
        logPropertyChange("Raw Config Override: ", name, null, oldValue, value);
        rawProperties.setProperty(name, value);
    }

    protected String resolve(String key) {
        return resolve(key, null);
    }

    /**
     * This method will determine the value for a property by looking it up in the raw properties.
     * If the property value contains a nested property (foo=${nested}) it will start the recursion
     * by calling parseValue(). It will also check for a system property of the same name and, based
     * on the value of systemOverride, 'override with' the system property or 'default to' the
     * system property if not found in the raw properties. This method only determines the resolved
     * value, it does not modify the properties in the resolved or raw properties objects.
     * 
     * @param key they key of the property for which to determine the value
     * @param keySet contains all keys used so far in this recursion. used to check for circular
     *        references.
     * @return
     */
    protected String resolve(String key, Set<String> keySet) {

        // check if we have already resolved this key and have circular reference
        if (keySet != null && keySet.contains(key)) {
            throw new ConfigurationException("Circular reference in config: " + key);
        }

        String value = this.rawProperties.getProperty(key);

        if ((value == null || systemOverride) && System.getProperties().containsKey(key)) {
            value = System.getProperty(key);
        }

        if (value != null && value.contains("${")) {
            if (keySet == null) {
                keySet = new HashSet<String>();
            }
            keySet.add(key);

            value = parseValue(value, keySet);

            keySet.remove(key);
        }

        if (value == null) {
            value = "";
            LOG.debug("Property key: '" + key + "' is not available and hence set to empty");
        }

        return value;
    }

    /**
     * This method parses the value string to find all nested properties (foo=${nested}) and
     * replaces them with the value returned from calling resolve(). It does this in a new string
     * and does not modify the raw or resolved properties objects.
     * 
     * @param value the string to search for nest properties
     * @param keySet contains all keys used so far in this recursion. used to check for circular
     *        references.
     * @return
     */
    protected String parseValue(String value, Set<String> keySet) {
        String result = value;

        Matcher matcher = pattern.matcher(value);

        while (matcher.find()) {

            // get the first, outermost ${} in the string. removes the ${} as well.
            String key = matcher.group(1);

            String resolved = resolve(key, keySet);

            result = matcher.replaceFirst(Matcher.quoteReplacement(resolved));
            matcher = matcher.reset(result);
        }

        return result;
    }

    /**
     * This method is used when reading in new properties to check if there is a direct reference to
     * the key in the value. This emulates operating system environment variable setting behavior
     * and replaces the reference in the value with the current value of the property from the
     * rawProperties.
     * 
     * <pre>
     * ex:
     * path=/usr/bin;${someVar}
     * path=${path};/some/other/path
     * 
     * resolves to:
     * path=/usr/bin;${someVar};/some/other/path
     * </pre>
     * 
     * It does not resolve the the value from rawProperties as it could contain nested properties
     * that might change later. If the property does not exist in the rawProperties it will check
     * for a default system property now to prevent a circular reference error.
     * 
     * @param name the property name
     * @param value the value to check for nested property of the same name
     * @return
     */
    protected String replaceVariable(String name, String value) {
        String regex = "(?:\\$\\{" + name + "\\})";
        String temporary = null;

        // Look for a property in the map first and use that. If system override is true
        // then it will get overridden during the resolve phase. If the value is null
        // we need to check the system now so we don't throw an error.
        if (value.contains("${" + name + "}")) {
            if ((temporary = rawProperties.getProperty(name)) == null) {
                temporary = System.getProperty(name);
            }

            if (temporary != null) {
                return value.replaceAll(regex, Matcher.quoteReplacement(temporary));
            }
        }

        return value;
    }

    /**
     * This method iterates through the raw properties and stores their resolved values in the
     * resolved properties map, which acts as a cache so we don't have to run the recursion every
     * time getProperty() is called.
     */
    protected void resolveRawToCache() {
        // Make sure we have something to do
        if (rawProperties.size() == 0) {
            return;
        }

        // Store the existing resolved properties in another object
        Properties oldProps = new Properties(new ImmutableProperties(resolvedProperties));

        // Clear the resolved properties object
        resolvedProperties.clear();

        // Setup sorted property keys
        SortedSet<String> keys = new TreeSet<String>(rawProperties.stringPropertyNames());

        // Cycle through the properties resolving values as we go
        for (String key : keys) {

            // Fully resolve the value for this key
            String newValue = resolve(key);

            // Extract the old value for this key
            String oldValue = oldProps.getProperty(key);

            // Extract the raw value for this key
            String rawValue = rawProperties.getProperty(key);

            // Log what happened (if anything) in terms of an existing property being overridden
            logPropertyChange("Resolved Config Override: ", key, rawValue, oldValue, newValue);

            // Store the fully resolved property value
            resolvedProperties.setProperty(key, newValue);
        }
    }

    protected void logPropertyChange(String msg, String key, String rawValue, String oldValue, String newValue) {

        // We are not in debug mode, we are done
        if (!LOG.isDebugEnabled()) {
            return;
        }

        // There was no previous value, we are done
        if (oldValue == null) {
            return;
        }

        // There was a previous value, but it's the same as the new value, we are done
        if (StringUtils.equals(oldValue, newValue)) {
            return;
        }

        // Create some log friendly strings
        String displayOld = flatten(ConfigLogger.getDisplaySafeValue(key, oldValue));
        String displayNew = flatten(ConfigLogger.getDisplaySafeValue(key, newValue));

        // Log what happened to this property value
        if (StringUtils.contains(rawValue, "$")) {
            LOG.debug(msg + key + "(" + rawValue + ")=[" + displayOld + "]->[" + displayNew + "]");
        } else {
            LOG.debug(msg + key + "=[" + displayOld + "]->[" + displayNew + "]");
        }
    }

    /**
     * Configures built-in properties.
     */
    protected void configureBuiltIns() {
        this.setProperty("host.ip", RiceUtilities.getIpNumber());
        this.setProperty("host.name", RiceUtilities.getHostName());
    }

    /**
     * Generates a random integer in the range specified by the specifier, in the format: min-max
     * 
     * @param rangeSpec a range specification, 'min-max'
     * @return a random integer in the range specified by the specifier, in the format: min-max
     */
    protected int generateRandomInteger(String rangeSpec) {
        String[] range = rangeSpec.split("-");
        if (range.length != 2) {
            throw new IllegalArgumentException("Invalid range specifier: " + rangeSpec);
        }
        int from = Integer.parseInt(range[0].trim());
        int to = Integer.parseInt(range[1].trim());
        if (from > to) {
            int tmp = from;
            from = to;
            to = tmp;
        }
        int num;
        // not very random huh...
        if (from == to) {
            num = from;
            LOG.info("from==to, so not generating random value for property.");
        } else {
            num = from + RANDOM.nextInt((to - from) + 1);
        }
        return num;
    }

    public boolean isSystemOverride() {
        return systemOverride;
    }

    /**
     * If set to true then system properties will always be checked first, disregarding any values
     * in the config.
     * 
     * The default is false.
     * 
     * @param systemOverride
     */
    public void setSystemOverride(boolean systemOverride) {
        this.systemOverride = systemOverride;
    }

    protected org.kuali.rice.core.impl.config.property.Config unmarshal(Unmarshaller unmarshaller, InputStream in)
            throws SAXException, ParserConfigurationException, IOException,
            IllegalStateException, JAXBException {
        SAXParserFactory spf = SAXParserFactory.newInstance();
        spf.setNamespaceAware(true);

        XMLFilter filter = new ConfigNamespaceURIFilter();
        filter.setParent(spf.newSAXParser().getXMLReader());

        UnmarshallerHandler handler = unmarshaller.getUnmarshallerHandler();
        filter.setContentHandler(handler);

        filter.parse(new InputSource(in));

        return (org.kuali.rice.core.impl.config.property.Config) handler.getResult();
    }

    protected org.kuali.rice.core.impl.config.property.Config unmarshalQuietly(Unmarshaller unmarshaller, InputStream in) {
        try {
            return unmarshal(unmarshaller, in);
        } catch (Exception e) {
            throw new IllegalStateException(e);
        }
    }

    /**
     * This is a SAX filter that adds the config xml namespace to the document if the document does
     * not have a namespace (for backwards compatibility). This filter assumes unqualified
     * attributes and does not modify their namespace (if any).
     * 
     * This could be broken out into a more generic class if Rice makes more use of JAXB.
     * 
     * @author Kuali Rice Team (kuali-rice@googlegroups.com)
     * 
     */
    public class ConfigNamespaceURIFilter extends XMLFilterImpl {

        public static final String CONFIG_URI = "http://rice.kuali.org/core/impl/config";

        @Override
        public void startElement(String uri, String localName, String qName, Attributes atts) throws SAXException {
            if (StringUtils.isBlank(uri)) {
                uri = CONFIG_URI;
            }

            super.startElement(uri, localName, qName, atts);
        }

        @Override
        public void endElement(String uri, String localName, String qName) throws SAXException {
            if (StringUtils.isBlank(uri)) {
                uri = CONFIG_URI;
            }

            super.endElement(uri, localName, qName);
        }
    }

    @Override
    public void putObject(String key, Object value) {
        this.objects.put(key, value);
    }

    @Override
    public void putObjects(Map<String, Object> objects) {
        this.objects.putAll(objects);
    }

    @Override
    public void removeObject(String key) {
        this.objects.remove(key);
    }

    @Override
    public void removeProperty(String key) {
        this.rawProperties.remove(key);
        resolveRawToCache();
    }

    @Override
    public void putConfig(Config config) {
        this.copyConfig(config);
    }

    @Override
    public String toString() {
        return String.valueOf(resolvedProperties);
    }

}
