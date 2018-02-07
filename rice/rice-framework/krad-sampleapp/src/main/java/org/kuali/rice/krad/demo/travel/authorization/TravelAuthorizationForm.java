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
package org.kuali.rice.krad.demo.travel.authorization;

import org.kuali.rice.krad.web.form.TransactionalDocumentFormBase;

/**
 * Transactional doc form implementation for the travel authorization document.
 *
 * <p>
 *   Holds properties necessary to determine the {@code View} instance that
 *   will be used to render the UI for the travel authorization document.
 * </p>
 *
 * @author Kuali Rice Team (rice.collab@kuali.org)
 */
public class TravelAuthorizationForm extends TransactionalDocumentFormBase {

    private String travelerFirstName;
    private String travelerLastName;

    public TravelAuthorizationForm() {
        super();
    }

    /**
     * Determines the default type name.
     *
     * <p>
     * The default document type name is specific for each type of KRAD transactional
     * document and manually set.
     * </p>
     *
     * @link TravelAuthorizationForm#getDefaultDocumentTypeName()
     * @return String - default document type name
     */
    @Override
    protected String getDefaultDocumentTypeName() {
        return "TravelAuthorization";
    }

    public void setTravelerFirstName(String travelerFirstName) {
        this.travelerFirstName = travelerFirstName;
    }

    public String getTravelerFirstName() {
        return travelerFirstName;
    }

    public void setTravelerLastName(String travelerLastName) {
        this.travelerLastName = travelerLastName;
    }

    public String getTravelerLastName() {
        return travelerLastName;
    }
}
