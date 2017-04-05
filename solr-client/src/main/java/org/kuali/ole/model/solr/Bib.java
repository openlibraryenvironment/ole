package org.kuali.ole.model.solr;

import org.apache.solr.client.solrj.beans.Field;
import org.springframework.data.annotation.Id;

/**
 * Created by sheiks on 21/11/16.
 */
public class Bib {
    @Id
    @Field("id")
    private String id;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
