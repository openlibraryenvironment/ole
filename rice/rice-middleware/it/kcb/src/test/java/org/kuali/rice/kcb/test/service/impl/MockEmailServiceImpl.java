/**
 * Copyright 2005-2013 The Kuali Foundation
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
package org.kuali.rice.kcb.test.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.kuali.rice.kcb.service.impl.EmailServiceImpl;
import org.kuali.rice.kcb.test.service.MockEmailService;

/**
 * Mock EmailService implementation that does not actually send any mail
 * @author Kuali Rice Team (rice.collab@kuali.org)
 */
public class MockEmailServiceImpl extends EmailServiceImpl implements MockEmailService {
    private static final Logger LOG = Logger.getLogger(MockEmailServiceImpl.class);

    public final Map<String, List<Map<String, String>>> MAILBOXES = new HashMap<String, List<Map<String, String>>>();

	/**
     * @see org.kuali.rice.kcb.test.service.MockEmailService#getMailBoxes()
     */
    public Map<String, List<Map<String, String>>> getMailBoxes() {
        return MAILBOXES;
    }

    /**
     * @see org.kuali.rice.kcb.service.impl.EmailServiceImpl#sendEmail(java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String)
     */
    //@Override
    protected void sendEmail(String message, String subject, String from, String sendTo, String format) {
        LOG.info("Storing mail for user: " + sendTo + ": " + subject);
        Map<String, String> mail = new HashMap<String, String>();
        mail.put("message", message);
        mail.put("subject", subject);
        mail.put("from", from);
        mail.put("sendTo", sendTo);
        mail.put("format", format);

        synchronized (MAILBOXES) {
            List<Map<String, String>> mailbox = MAILBOXES.get(sendTo);
            if (mailbox == null) {
                mailbox = new ArrayList<Map<String, String>>();
                MAILBOXES.put(sendTo, mailbox);
            }
            mailbox.add(mail);
        }
    }
}
