package org.kuali.ole.model.jpa;

import java.io.Serializable;
import java.sql.Timestamp;

/**
 * Created by sheiks on 28/10/16.
 */
public class ConfigDocument implements Serializable {

    private Integer id;
    private String name;
    private String label;
    private String description;
    private Timestamp updatedDate;

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Timestamp getUpdatedDate() {
        return updatedDate;
    }

    public void setUpdatedDate(Timestamp updatedDate) {
        this.updatedDate = updatedDate;
    }
}
