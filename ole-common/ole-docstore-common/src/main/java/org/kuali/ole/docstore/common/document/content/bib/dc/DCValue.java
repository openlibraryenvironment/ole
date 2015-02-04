package org.kuali.ole.docstore.common.document.content.bib.dc;


/**
 * Class to represent data entity DC Value of Work Bib Dublin Core Document.
 *
 */
public class DCValue {
    private String element = null;
    private String qualifier = null;
    private String value = null;

    public DCValue() {
    }

    public DCValue(String element) {
        this.element = element;
    }

    public String getElement() {
        return element;
    }

    public void setElement(String element) {
        this.element = element;
    }

    public String getQualifier() {
        return qualifier;
    }

    public void setQualifier(String qualifier) {
        this.qualifier = qualifier;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return "element=" + element + ", qualifier=" + qualifier + ", value=" + value;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof DCValue) {
            DCValue dcValue = (DCValue) obj;
            if (dcValue.getElement().equals(this.getElement())) {
                return true;
            } else {
                return false;
            }
        } else {
            return false;
        }
    }
}
