package org.kuali.ole.select.bo;

import org.kuali.rice.kim.impl.identity.principal.PrincipalBo;
import org.kuali.rice.krad.bo.PersistableBusinessObjectBase;

/**
 * Created with IntelliJ IDEA.
 * User: srinivasane
 * Date: 6/18/13
 * Time: 2:56 PM
 * To change this template use File | Settings | File Templates.
 */
public class OLEEResourceSelector extends PersistableBusinessObjectBase {

    private String oleERSSelectorId;
    private String oleERSIdentifier;
    private String selectorId;
    private PrincipalBo person;

    public String getOleERSSelectorId() {
        return oleERSSelectorId;
    }

    public void setOleERSSelectorId(String oleERSSelectorId) {
        this.oleERSSelectorId = oleERSSelectorId;
    }

    public String getOleERSIdentifier() {
        return oleERSIdentifier;
    }

    public void setOleERSIdentifier(String oleERSIdentifier) {
        this.oleERSIdentifier = oleERSIdentifier;
    }

    public String getSelectorId() {
        return selectorId;
    }

    public void setSelectorId(String selectorId) {
        this.selectorId = selectorId;
    }

    public PrincipalBo getPerson() {
        return person;
    }

    public void setPerson(PrincipalBo person) {
        this.person = person;
    }
}
