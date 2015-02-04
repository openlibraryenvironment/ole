package org.kuali.ole.ingest;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

/**
 * FileUtil will reads the given file and returns stringBuilder
 */
public class FileUtil {
    /**
     *  This method reads the mentioned file and returns stringBuilder
     * @param file
     * @return  stringBuilder
     * @throws java.io.IOException
     */
    public String readFile(File file) throws IOException {
        BufferedReader reader = new BufferedReader(new FileReader(file));
        String line = null;
        StringBuilder stringBuilder = new StringBuilder();
        String ls = System.getProperty("line.separator");
        while ((line = reader.readLine()) != null) {
            stringBuilder.append(line);
            stringBuilder.append(ls);
        }
        return stringBuilder.toString();
    }
}
