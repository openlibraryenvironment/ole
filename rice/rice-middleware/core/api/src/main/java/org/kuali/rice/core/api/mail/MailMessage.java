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

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

/**
 * 
 * 
 */
public class MailMessage implements Serializable {
    private String fromAddress;
    private Set toAddresses = new HashSet();
    private Set ccAddresses = new HashSet();
    private Set bccAddresses = new HashSet();
    private String subject = "";
    private String message = "";

    public MailMessage() {
        super();
    }

    public String getFromAddress() {
        return fromAddress;
    }

    public void setFromAddress(String fromAddress) {
        this.fromAddress = fromAddress;
    }

    /**
     * @return Returns the bccAddresses.
     */
    public Set getBccAddresses() {
        return bccAddresses;
    }

    public void addBccAddress(String addr) {
        bccAddresses.add(addr);
    }

    public void removeBccAddress(String addr) {
        bccAddresses.remove(addr);
    }

    /**
     * @param bccAddresses The bccAddresses to set.
     */
    public void setBccAddresses(Set bccAddresses) {
        this.bccAddresses = bccAddresses;
    }

    /**
     * @return Returns the ccAddresses.
     */
    public Set getCcAddresses() {
        return ccAddresses;
    }

    public void addCcAddress(String addr) {
        ccAddresses.add(addr);
    }

    public void removeCcAddress(String addr) {
        ccAddresses.remove(addr);
    }

    /**
     * @param ccAddresses The ccAddresses to set.
     */
    public void setCcAddresses(Set ccAddresses) {
        this.ccAddresses = ccAddresses;
    }

    /**
     * @return Returns the message.
     */
    public String getMessage() {
        return message;
    }

    /**
     * @param message The message to set.
     */
    public void setMessage(String message) {
        this.message = message;
    }

    /**
     * @return Returns the subject.
     */
    public String getSubject() {
        return subject;
    }

    /**
     * @param subject The subject to set.
     */
    public void setSubject(String subject) {
        this.subject = subject;
    }

    /**
     * @return Returns the toAddresses.
     */
    public Set getToAddresses() {
        return toAddresses;
    }

    public void addToAddress(String addr) {
        toAddresses.add(addr);
    }

    public void removeToAddress(String addr) {
        toAddresses.remove(addr);
    }

    /**
     * @param toAddresses The toAddresses to set.
     */
    public void setToAddresses(Set toAddresses) {
        this.toAddresses = toAddresses;
    }
}
