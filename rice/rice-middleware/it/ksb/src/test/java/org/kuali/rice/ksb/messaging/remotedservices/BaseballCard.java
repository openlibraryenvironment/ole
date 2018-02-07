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
package org.kuali.rice.ksb.messaging.remotedservices;

import javax.xml.bind.annotation.XmlRootElement;

/**
 * data bean used by the {@link BaseballCardCollectionService}
 * 
 * @author Kuali Rice Team (rice.collab@kuali.org)
 *
 */
@XmlRootElement
public class BaseballCard {
    
    private String cardType;
    private String playerName;
    private Integer year;
    
    public BaseballCard() {
    }

    public BaseballCard(String playerName, String cardType, Integer year) {
        this.playerName = playerName;
        this.cardType = cardType;
        this.year = year;
    }

    
    
    /**
     * @param cardType the cardType to set
     */
    public void setCardType(String cardType) {
        this.cardType = cardType;
    }

    /**
     * @param playerName the playerName to set
     */
    public void setPlayerName(String playerName) {
        this.playerName = playerName;
    }

    /**
     * @param year the year to set
     */
    public void setYear(Integer year) {
        this.year = year;
    }

    public String getCardType() {
        return cardType;
    }
    
    public String getPlayerName() {
        return playerName;
    }
    
    public Integer getYear() {
        return year;
    }

    /**
     * @see java.lang.Object#hashCode()
     */
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((this.cardType == null) ? 0 : this.cardType.hashCode());
        result = prime * result + ((this.playerName == null) ? 0 : this.playerName.hashCode());
        result = prime * result + ((this.year == null) ? 0 : this.year.hashCode());
        return result;
    }

    /**
     * @see java.lang.Object#equals(java.lang.Object)
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        BaseballCard other = (BaseballCard) obj;
        if (this.cardType == null) {
            if (other.cardType != null)
                return false;
        } else if (!this.cardType.equals(other.cardType))
            return false;
        if (this.playerName == null) {
            if (other.playerName != null)
                return false;
        } else if (!this.playerName.equals(other.playerName))
            return false;
        if (this.year == null) {
            if (other.year != null)
                return false;
        } else if (!this.year.equals(other.year))
            return false;
        return true;
    };
    
    

}
