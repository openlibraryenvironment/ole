/*
 * Copyright 2011 The Kuali Foundation.
 * 
 * Licensed under the Educational Community License, Version 1.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 * http://www.opensource.org/licenses/ecl1.php
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.kuali.batchingest;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;

public class ProcessVendorFile {

    public String transformStreamToRawData(InputStream inputStream) throws Exception{
        return processRawDataToXml(readInputStream(inputStream));
    }
    
    private String readInputStream(InputStream inputStream) throws Exception{
        BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
        StringBuilder stringBuilder = new StringBuilder();
        String line = null;
        while ((line = reader.readLine()) != null) {
            stringBuilder.append(line + "\n");
        }
        inputStream.close();
        return stringBuilder.toString();
    }
    
    public String getRawXml(InputStream inputStream,List bibinfoFailure) throws Exception{
       return getFailureRawData(readInputStream(inputStream),bibinfoFailure);
    }
    
    private String processRawDataToXml(String rawData) throws Exception{
        return new MarcDataFormatTransformer().transformRawDataToXml(rawData);
    }
    
    private String getFailureRawData(String rawData,List bibinfoFailure) throws Exception{
        return new MarcDataFormatTransformer().getFailureRawData(rawData,bibinfoFailure);
    }
    
    
    
    
}
