package org.kuali.ole.docstore.model.repopojo;

import java.util.*;

/**
 * Base class for nodes in docstore
 * User: Pranitha
 * Date: 2/28/12
 * Time: 3:02 PM
 * To change this template use File | Settings | File Templates.
 */
public class DocStoreNode {

    public final String UUID = "jcr:uuid";
    public final String PRIMARY_TYPE = "jcr:primaryType";
    public final String NODE_TYPE = "nodeType";
    public final String MIXIN_TYPES = "jcr:mixinTypes";
    public final String CREATED_BY = "jcr:createdBy";
    public final String CREATED = "jcr:created";
    public final String MIME_TYPE = "jcr:mimeType";
    public final String CONTENT = "jcr:data";
    public final String FILE_NODE_COUNT = "fileNodeCount";
    private String name;
    private String path;
    private Map<String, Object> propertyMap = new LinkedHashMap<String, Object>();

    public Map<String, Object> getPropertyMap() {
        return propertyMap;
    }

    public void setPropertyMap(Map<String, Object> propertyMap) {
        this.propertyMap = propertyMap;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public Object getProperty(String propertyName) {
        return propertyMap.get(propertyName);
    }

    public void setProperty(String key, Object value) {
        if (propertyMap.containsKey(key) && !key.equalsIgnoreCase("jcr:data")) {
            List<Object> multiValue = new ArrayList<Object>();
            multiValue.add(propertyMap.get(key));
            multiValue.add(value);
            propertyMap.put(key, multiValue);
        } else {
            propertyMap.put(key, value);
        }
        setPropertyMap(propertyMap);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getPath() + "\n");
        Map<String, Object> resultMap = (Map<String, Object>) getPropertyMap();
        if (resultMap.size() > 0) {
            Set<String> result = resultMap.keySet();
            for (Iterator<String> iterator1 = result.iterator(); iterator1.hasNext(); ) {
                String key = iterator1.next();
                Object value = resultMap.get(key);
                if (value instanceof List) {
                    for (int i = 0; i < ((List) value).size(); i++) {
                        sb.append(getPath() + "/" + key + " = " + ((List) value).get(i) + "\n");
                    }
                } else if (value instanceof Long) {
                    long longValue = ((Long) value).longValue();
                    sb.append(getPath() + "/" + key + " = " + longValue + "\n");
                } else {
                    value = (String) resultMap.get(key);
                    if (getPath().equalsIgnoreCase("/")) {
                        sb.append("/" + key + " = " + value + "\n");
                    } else {
                        sb.append(getPath() + "/" + key + " = " + value + "\n");
                    }
                }
            }
        }
        return sb.toString();
    }
}
