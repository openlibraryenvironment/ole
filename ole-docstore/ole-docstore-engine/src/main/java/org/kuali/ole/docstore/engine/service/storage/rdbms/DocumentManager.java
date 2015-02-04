package org.kuali.ole.docstore.engine.service.storage.rdbms;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: sambasivam
 * Date: 12/17/13
 * Time: 2:49 PM
 * To change this template use File | Settings | File Templates.
 */
public interface DocumentManager {

    public void create(Object object);

    public void update(Object object);

    public Object retrieve(String id);

    public List<Object> retrieve(List<String> id);

    public void delete(String id);

    public Object retrieveTree(String id);

    public void validate(Object object);

    public void deleteVerify(String id);

}
