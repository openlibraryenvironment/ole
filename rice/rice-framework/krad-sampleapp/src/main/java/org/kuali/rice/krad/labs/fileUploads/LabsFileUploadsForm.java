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
package org.kuali.rice.krad.labs.fileUploads;

import org.kuali.rice.krad.labs.KradLabsForm;
import org.kuali.rice.krad.uif.util.SessionTransient;
import org.springframework.web.multipart.MultipartFile;

/**
 * Form class for the file uploads lab view
 *
 * @author Kuali Rice Team (rice.collab@kuali.org)
 */
public class LabsFileUploadsForm extends KradLabsForm {
    private static final long serialVersionUID = -6189618372290245896L;

    @SessionTransient
    private MultipartFile uploadOne;
    @SessionTransient
    private MultipartFile uploadTwo;

    public MultipartFile getUploadOne() {
        return uploadOne;
    }

    public void setUploadOne(MultipartFile uploadOne) {
        this.uploadOne = uploadOne;
    }

    public MultipartFile getUploadTwo() {
        return uploadTwo;
    }

    public void setUploadTwo(MultipartFile uploadTwo) {
        this.uploadTwo = uploadTwo;
    }
}
