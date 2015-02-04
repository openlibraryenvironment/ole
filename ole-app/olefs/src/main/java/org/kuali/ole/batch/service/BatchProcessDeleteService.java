package org.kuali.ole.batch.service;

import java.util.List;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: mjagan
 * Date: 07/27/13
 * Time: 4:40 PM
 * To change this template use File | Settings | File Templates.
 */
public interface BatchProcessDeleteService {

    public int performBatchDelete(List docBibIds , String profileField) throws Exception;

    public Map getBibIdsForBatchDelete(String searchMrcFieldData, String profileFiled) throws Exception;



}
