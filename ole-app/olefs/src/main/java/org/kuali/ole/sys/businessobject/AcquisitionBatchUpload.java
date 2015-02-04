/*
 * Copyright 2007 The Kuali Foundation
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
package org.kuali.ole.sys.businessobject;

import org.kuali.rice.krad.bo.TransientBusinessObjectBase;

/**
 * Object that contains properties used on the batch upload screen.
 */
public class AcquisitionBatchUpload extends TransientBusinessObjectBase {
    private String batchFilePath;
    private String batchLoadProfile;
    private String batchDescription;
    private String batchInputTypeName;
    //private String batchDestinationFilePath;



    /**
     * Default no-arg constructor.
     */
    public AcquisitionBatchUpload() {

    }

    public String getBatchFilePath() {
        return batchFilePath;
    }


    public void setBatchFilePath(String batchFilePath) {
        this.batchFilePath = batchFilePath;
    }


    public String getBatchLoadProfile() {
        return batchLoadProfile;
    }


    public void setBatchLoadProfile(String batchLoadProfile) {
        this.batchLoadProfile = batchLoadProfile;
    }


    public String getBatchDescription() {
        return batchDescription;
    }


    public void setBatchDescription(String batchDescription) {
        this.batchDescription = batchDescription;
    }


    public String getBatchInputTypeName() {
        return batchInputTypeName;
    }


    public void setBatchInputTypeName(String batchInputTypeName) {
        this.batchInputTypeName = batchInputTypeName;
    }

   /* public String getBatchDestinationFilePath() {
        return batchDestinationFilePath;
    }


    public void setBatchDestinationFilePath(String batchDestinationFilePath) {
        this.batchDestinationFilePath = batchDestinationFilePath;
    }*/


}
