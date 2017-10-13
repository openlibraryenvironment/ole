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
package org.kuali.rice.core.api.cache;

import org.apache.commons.lang.StringUtils;
import org.kuali.rice.core.api.CoreConstants;
import org.kuali.rice.core.api.mo.AbstractDataTransferObject;
import org.w3c.dom.Element;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAnyElement;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;
import java.util.Collection;

@XmlRootElement(name = CacheTarget.Constants.ROOT_ELEMENT_NAME)
@XmlAccessorType(XmlAccessType.NONE)
@XmlType(name = CacheTarget.Constants.TYPE_NAME, propOrder = {
        CacheTarget.Elements.CACHE,
        CacheTarget.Elements.KEY,
        CoreConstants.CommonElements.FUTURE_ELEMENTS
})
/**
 * A class that represents a target of a cache operation.  If the cache key is not specified then the entire cache is
 * the target.
 *
 * @author Kuali Rice Team (rice.collab@kuali.org)
 * @since 2.0
 */
public final class CacheTarget extends AbstractDataTransferObject {

    private static final long serialVersionUID = -7143135771254777648L;
    
    @XmlElement(name = Elements.CACHE, required = true)
    private final String cache;

    @XmlElement(name = Elements.KEY, required = false)
    private final String key;
    
    @SuppressWarnings("unused")
    @XmlAnyElement
    private final Collection<Element> _futureElements = null;

    /**
     * This constructor should never be called except during JAXB unmarshalling.
     */
    @SuppressWarnings("unused")
    private CacheTarget() {
        this.cache = null;
        this.key = null;
    }

    private CacheTarget(String cache, String key) {
        if (StringUtils.isBlank(cache)) {
            throw new IllegalArgumentException("cache is blank or null");
        }
        this.cache = cache;
        this.key = key;
    }

    /**
     * Creates an instance targeting an entire cache.
     *
     * @param cache the name of the cache.  cannot be a null or blank string
     * 
     * @return an instance of the cache target specified
     *
     * @throws IllegalArgumentException if cache is null or blank
     */
    public static CacheTarget entireCache(String cache) {
        return new CacheTarget(cache, null);
    }

    /**
     * Creates an instance targeting a single item in a cache based on the given key.
     *
     * @param cache The name of the cache.  cannot be a null or blank string.
     * @param key The key of the item in the cache.  cannot be a null of blank string.
     *
     * @return an instance of the cache target specified
     * 
     * @throws IllegalArgumentException if the cache or key is null or blank
     */
    public static CacheTarget singleEntry(String cache, String key) {
        if (StringUtils.isBlank(key)) {
            throw new IllegalArgumentException("a blank or null key does not target a single entry");
        }
        return new CacheTarget(cache, key);
    }

    /**
     * Checks if this instance contains a key.
     *
     * @return true if a key exists, false otherwise
     */
    public boolean containsKey() {
        return key != null;
    }

    /**
     * The name of the cache to target.  This value should never be null or blank.
     *
     * @return the name of the cache
     */
    public String getCache() {
        return cache;
    }

    /**
     * The key of the item in the cache to target.  The key is optional on the cache target, if this target has no key
     * then this method will return null.
     *
     * @return the key of this cache target, or null if a specific key is not being targeted
     */
    public String getKey() {
        return key;
    }

    /**
     * Defines some internal constants used on this class.
     */
    static class Constants {
        final static String ROOT_ELEMENT_NAME = "cacheTarget";
        final static String TYPE_NAME = "CacheTargetType";
    }

    /**
     * A private class which exposes constants which define the XML element names to use
     * when this object is marshalled to XML.
     */
    static class Elements {
        final static String CACHE = "cache";
        final static String KEY = "key";
    }
    
}
