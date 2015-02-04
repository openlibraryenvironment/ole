package org.kuali.ole.describe.bo.marc.structuralfields.controlfield006;

/**
 * Created with IntelliJ IDEA.
 * User: jayabharathreddy
 * Date: 6/24/13
 * Time: 1:32 PM
 * To change this template use File | Settings | File Templates.
 */
public class MixedMaterial {

    private String undefinedPos5 = "#####";
    private String undefinedPos10 = "###########";
    private String formOfItem="#";

    public String getUndefinedPos10() {
        return undefinedPos10;
    }

    public void setUndefinedPos10(String undefinedPos10) {
        this.undefinedPos10 = undefinedPos10;
    }

    public String getUndefinedPos5() {
        return undefinedPos5;
    }

    public void setUndefinedPos5(String undefinedPos5) {
        this.undefinedPos5 = undefinedPos5;
    }

    public String getFormOfItem() {
        return formOfItem;
    }

    public void setFormOfItem(String formOfItem) {
        this.formOfItem = formOfItem;
    }
}
