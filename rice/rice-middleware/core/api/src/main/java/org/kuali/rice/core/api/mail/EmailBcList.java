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
package org.kuali.rice.core.api.mail;

import javax.mail.Address;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import java.util.List;

/**
 * The bc addresses of an email message.
 *
 * @author Kuali Rice Team (kuali-rice@googlegroups.com)
 */
public class EmailBcList {

    private List<String> bcAddresses;

    public EmailBcList(List<String> bcAddresses) {
        this.bcAddresses = bcAddresses;
    }

    public List<String> getBcAddresses() {
        return bcAddresses;
    }

    public Address[] getToAddressesAsAddressArray() throws AddressException {
        Address[] recipientAddresses = new Address[this.bcAddresses.size()];
        for (int i = 0; i < recipientAddresses.length; i++) {
            recipientAddresses[i] = new InternetAddress((String) this.bcAddresses.get(i));
        }
        return recipientAddresses;
    }

    public void setBcAddress(List<String> bcAddresses) {
        this.bcAddresses = bcAddresses;
    }

}
