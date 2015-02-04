package org.kuali.ole.docstore.model.xmlpojo.work.bib.marc;

import java.io.Serializable;

/**
 * Created with IntelliJ IDEA.
 * User: meenrajd
 * Date: 9/18/13
 * Time: 6:39 PM
 * To change this template use File | Settings | File Templates.
 */
public class LeaderTag implements Serializable{

    public LeaderTag(){

    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    private String value;

    @Override
    public boolean equals(Object object) {
        if (this == object) return true;
        if (object == null || getClass() != object.getClass()) return false;

        LeaderTag that = (LeaderTag) object;

        if (value != null ? !value.equals(that.value) : that.value != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = (value != null ? value.hashCode() : 0);
        return result;
    }
}
