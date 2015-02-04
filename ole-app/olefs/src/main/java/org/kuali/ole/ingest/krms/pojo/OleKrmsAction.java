package org.kuali.ole.ingest.krms.pojo;


import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: vivekb
 * Date: 7/20/12
 * Time: 11:48 AM
 * To change this template use File | Settings | File Templates.
 */
public class OleKrmsAction {
    private String name;
    private List<OleParameter> parameters;
  //  private List<OleKrmsAction> krmsActions;

    /**
     * Gets the oleAgendaList attribute.
     * @return  Returns the oleAgendaList.
     */
    public String getName() {
        return name;
    }
    /**
     * Sets the name attribute value.
     * @param name  The name to set.
     */
    public void setName(String name) {
        this.name = name;
    }

    public List<OleParameter> getParameters() {
        return parameters;
    }

    public void setParameters(List<OleParameter> parameters) {
        this.parameters = parameters;
    }

    /*  *//**
     * Gets the krmsActions attribute.
     * @return  Returns the krmsActions.
     *//*
    public List<OleKrmsAction> getKrmsActions() {
        return krmsActions;
    }
    *//**
     * Sets the krmsActions attribute value.
     * @param krmsActions  The krmsActions to set.
     *//*
    public void setKrmsActions(List<OleKrmsAction> krmsActions) {
        this.krmsActions = krmsActions;
    }*/
}
