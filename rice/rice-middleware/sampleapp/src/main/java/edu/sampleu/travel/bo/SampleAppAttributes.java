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
package edu.sampleu.travel.bo;

import java.io.Serializable;

/**
 * Wrapper class for exposing remote attribute definitions
 *
 * @author Kuali Rice Team (rice.collab@kuali.org)
 */
public class SampleAppAttributes implements Serializable {
    private static final long serialVersionUID = -5536424163658038143L;

    private String number;
    private String foId;

    public SampleAppAttributes() {

    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getFoId() {
        return foId;
    }

    public void setFoId(String foId) {
        this.foId = foId;
    }
}
