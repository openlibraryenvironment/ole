package org.kuali.ole.loaders.common;

import org.apache.log4j.Logger;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;

import java.io.*;
import java.net.URI;

/**
 * Created by sheiksalahudeenm on 3/5/15.
 */
public class FileUtils {

    private static final Logger LOG = Logger.getLogger(FileUtils.class);

    public static String readFileContent(String path) throws IOException {
        BufferedReader br=new BufferedReader(new FileReader(getFilePath(path)));
        String line=null;
        String fullContent = "";
        while ((line=br.readLine())!=null)
        {
            fullContent += line;
        }
        return fullContent;
    }

    public static String getFilePath(String classpathRelativePath)  {
        try {
            Resource rsrc = new ClassPathResource(classpathRelativePath);
            return rsrc.getFile().getAbsolutePath();
        } catch(Exception e){
            LOG.error("Error : while accessing file "+e);
        }
        return null;
    }
}
