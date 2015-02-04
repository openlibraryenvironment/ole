package org.kuali.ole.docstore.discovery.service;

/**
 * Class for administrative or maintenance services.
 * User: tirumalesh.b
 * Date: 23/12/11 Time: 11:26 AM
 */
public interface AdminService {

    /**
     * Optimizes the given index. (In solr, it is called core.)
     *
     * @param indexName
     */
    public void optimize(String indexName) throws Exception;

    /**
     * Optimizes all available indexes.
     */
    public void optimize() throws Exception;

    /**
     * Optimize the indexes according to params
     *
     * @param waitFlush
     * @param waitSearcher
     * @throws Exception waitFlush 	Default is true. Blocks until index changes are flushed to disk.
     *                   waitSearcher Default is true. Blocks until a new searcher is opened and registered as
     *                   the main query searcher, making the changes visible.
     */
    public void optimize(Boolean waitFlush, Boolean waitSearcher) throws Exception;


}
