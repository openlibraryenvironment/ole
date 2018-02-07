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
package edu.sampleu.travel.document;

import edu.sampleu.travel.bo.TravelAccount;
import org.kuali.rice.krad.document.SessionDocument;
import org.kuali.rice.krad.document.TransactionalDocumentBase;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;


@Entity
@Table(name="TRV_DOC_2")
public class TravelDocument2 extends TransactionalDocumentBase implements SessionDocument {

    @Column(name="traveler")
    private String traveler;
    @Column(name="org")
    private String origin;
    @Column(name="dest")
    private String destination;
    @Column(name="request_trav")
    private String requestType;
    @Transient
    private String accountType;

    @ManyToMany(fetch = FetchType.EAGER)    // Do not use cascade here or it will try to update the TravelAccounts
    @JoinTable(name="TRAV_DOC_2_ACCOUNTS", 
	           joinColumns={@JoinColumn(name="fdoc_nbr", referencedColumnName="fdoc_nbr", unique=false)},         // Goes with this class: TravelDocument2
	           inverseJoinColumns={@JoinColumn(name="acct_num", referencedColumnName="acct_num", unique=false)}   // Goes with that class: TravelAccount
    )
    private List<TravelAccount> travelAccounts;

    public TravelDocument2() {
        travelAccounts = new ArrayList<TravelAccount>();
    }

    public String getDestination() {
        return destination;
    }

    public void setDestination(String destination) {
        this.destination = destination;
    }

    public String getOrigin() {
        return origin;
    }

    public void setOrigin(String origin) {
        this.origin = origin;
    }

    public String getTraveler() {
        return traveler;
    }

    public void setTraveler(String traveler) {
        this.traveler = traveler;
    }

    public List<TravelAccount> getTravelAccounts() {
        return travelAccounts;
    }

    public void setTravelAccounts(List<TravelAccount> travelAccounts) {
        this.travelAccounts = travelAccounts;
    }

    public TravelAccount getTravelAccount(int index) {
        while(travelAccounts.size() - 1 < index) {
            travelAccounts.add(new TravelAccount());
        }
        return travelAccounts.get(index);
    }

    public String getRequestType() {
        return requestType;
    }

    public void setRequestType(String requestType) {
        this.requestType = requestType;
    }

    /**
     * @param accountType the accountType to set
     */
    public void setAccountType(String accountType) {
        this.accountType = accountType;
    }

    /**
     * @return the accountType
     */
    public String getAccountType() {
        return accountType;
    }

}
