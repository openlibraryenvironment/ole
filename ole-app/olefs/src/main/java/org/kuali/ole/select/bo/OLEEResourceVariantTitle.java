package org.kuali.ole.select.bo;

import org.kuali.rice.krad.bo.PersistableBusinessObjectBase;

/**
 * Created with IntelliJ IDEA.
 * User: jgupta69
 * Date: 29/10/14
 * Time: 2:56 PM
 * To change this template use File | Settings | File Templates.
 */

public class OLEEResourceVariantTitle extends PersistableBusinessObjectBase {

//    @PortableSequenceGenerator(name = "OLE_VRNT_TTL_S")
//    @GeneratedValue(generator = "OLE_VRNT_TTL_S")
//    @Id
//    @Column(name = "E_RES_VRNT_TTL_ID")
    private String oleVariantTitleId;

//    @Column(name = "E_RES_REC_ID")
    private String oleERSIdentifier;

//    @Column(name = "E_RES_VRNT_TTL")
    private String oleVariantTitle;

    public String getOleVariantTitleId() {
        return oleVariantTitleId;
    }

    public void setOleVariantTitleId(String oleVariantTitleId) {
        this.oleVariantTitleId = oleVariantTitleId;
    }

    public String getOleERSIdentifier() {
        return oleERSIdentifier;
    }

    public void setOleERSIdentifier(String oleERSIdentifier) {
        this.oleERSIdentifier = oleERSIdentifier;
    }

    public String getOleVariantTitle() {
        return oleVariantTitle;
    }

    public void setOleVariantTitle(String oleVariantTitle) {
        this.oleVariantTitle = oleVariantTitle;
    }
}
