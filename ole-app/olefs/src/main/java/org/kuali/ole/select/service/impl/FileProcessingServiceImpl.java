/*
 * Copyright 2011 The Kuali Foundation.
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
package org.kuali.ole.select.service.impl;

import org.kuali.ole.select.OleSelectConstant;
import org.kuali.ole.select.service.FileProcessingService;
import org.kuali.ole.sys.context.SpringContext;
import org.kuali.rice.core.api.config.property.ConfigurationService;

import java.io.*;
import java.util.HashMap;

public class FileProcessingServiceImpl implements FileProcessingService {
    protected static org.apache.log4j.Logger LOG = org.apache.log4j.Logger.getLogger(FileProcessingServiceImpl.class);

    @Override
    public HashMap<String, String> getFileContentAndDeleteFile(HashMap<String, String> dataMap) throws Exception {
        StringBuilder strigObj = new StringBuilder();
        String xmlString = null;
        String filePath = dataMap.get(OleSelectConstant.FILEPATH) + dataMap.get(OleSelectConstant.FILENAME) + OleSelectConstant.XML_FILE_TYPE_EXTENSION;
        boolean isFileExist = true;
        boolean isBibEdit = false;
        if (isCreateFileExist(dataMap)) {
            isFileExist = true;
        } else if (isEditFileExist(dataMap)) {
            isFileExist = true;
            isBibEdit = true;
            filePath = dataMap.get(OleSelectConstant.FILEPATH) + dataMap.get(OleSelectConstant.FILENAME) + "edit" + OleSelectConstant.XML_FILE_TYPE_EXTENSION;
        }

        if (LOG.isInfoEnabled())
            LOG.info("FileProcessingServiceImpl.getFileContentAndDeleteFile filePath--------->" + filePath);
        FileInputStream fstream = null;
        DataInputStream in = null;
        BufferedReader br = null;
        try {
            if (isFileExist) {
                fstream = new FileInputStream(filePath);
                in = new DataInputStream(fstream);
                br = new BufferedReader(new InputStreamReader(in));
                String strLine;
                while ((strLine = br.readLine()) != null) {
                    strigObj.append(strLine);
                }
                xmlString = strigObj.toString();
            }
        } catch (FileNotFoundException fnfe) {
            throw new FileNotFoundException("The file mentioned in particular path is not available");
        } catch (Exception e) {
            LOG.error("Exception while getting the file content of the file...",e);
            throw new RuntimeException(e);
        } finally {
            if (in != null && br != null) {
                fstream.close();
                in.close();
                br.close();
            }
        }
        File file = new File(filePath);
        file.delete();
        dataMap.put(OleSelectConstant.XML_FILE_CONTENT, xmlString);
        dataMap.put(OleSelectConstant.IS_BIB_EDIT, String.valueOf(isBibEdit));
        return dataMap;
    }

    public String getMarcXMLFileDirLocation() throws Exception {
        ConfigurationService kualiConfigurationService = SpringContext.getBean(ConfigurationService.class);
        String externaleDirectory = kualiConfigurationService.getPropertyValueAsString(OleSelectConstant.BIBMARCXML_DIR);
        return externaleDirectory;
    }

    public boolean isCreateFileExist(HashMap<String, String> dataMap) throws Exception {
        String filePath = dataMap.get(OleSelectConstant.FILEPATH) + dataMap.get(OleSelectConstant.FILENAME) + OleSelectConstant.XML_FILE_TYPE_EXTENSION;
        File file = new File(filePath);
        boolean isFileExist = true;
        if (!file.exists()) {
            isFileExist = false;
        }
        return isFileExist;
    }

    public boolean isEditFileExist(HashMap<String, String> dataMap) throws Exception {
        String filePath = dataMap.get(OleSelectConstant.FILEPATH) + dataMap.get(OleSelectConstant.FILENAME) + OleSelectConstant.XML_FILE_TYPE_EXTENSION;
        File file = new File(filePath);
        boolean isFileExist = true;
        if (!file.exists()) {
            filePath = dataMap.get(OleSelectConstant.FILEPATH) + dataMap.get(OleSelectConstant.FILENAME) + "edit" + OleSelectConstant.XML_FILE_TYPE_EXTENSION;
            file = new File(filePath);
            if (file.exists()) {
                isFileExist = true;
            } else {
                isFileExist = false;
            }
        }
        return isFileExist;
    }

}
