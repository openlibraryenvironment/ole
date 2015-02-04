package org.kuali.ole.docstore.common.document.config;

import org.kuali.rice.krad.bo.PersistableBusinessObjectBase;

import java.io.Serializable;

/**
 * Created with IntelliJ IDEA.
 * User: chandrasekharag
 * Date: 4/3/14
 * Time: 6:09 PM
 * To change this template use File | Settings | File Templates.
 */
public class SearchFacetPage extends PersistableBusinessObjectBase
        implements Serializable {
    private Integer id;
    private Integer shotSize;
    private Integer longSize;


    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getShotSize() {
        return shotSize;
    }

    public void setShotSize(Integer shotSize) {
        this.shotSize = shotSize;
    }

    public Integer getLongSize() {
        return longSize;
    }

    public void setLongSize(Integer longSize) {
        this.longSize = longSize;
    }
}
