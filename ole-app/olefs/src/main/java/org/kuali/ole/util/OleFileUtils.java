package org.kuali.ole.util;

import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;

import java.io.File;

/**
 * Created by SheikS on 11/23/2015.
 */
public class OleFileUtils {
    private static final org.apache.log4j.Logger LOG = org.apache.log4j.Logger.getLogger(OleFileUtils.class);
    private static OleFileUtils oleFileUtils;
    public static OleFileUtils getInstance() {
        if(null == oleFileUtils) {
            oleFileUtils =  new OleFileUtils();
        }
        return oleFileUtils;
    }

    private OleFileUtils() {
    }

    public File getFile(String relativePath)  {
        try {
            Resource pathResource = new ClassPathResource(relativePath);
            return pathResource.getFile();
        } catch(Exception e){
            LOG.error("Error : while accessing file "+e);
        }
        return null;
    }
}
