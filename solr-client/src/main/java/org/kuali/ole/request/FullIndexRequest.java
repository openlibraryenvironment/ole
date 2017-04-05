package org.kuali.ole.request;

/**
 * Created by sheiks on 08/11/16.
 */
public class FullIndexRequest {
    private Integer noOfDbThreads;
    private Integer docsPerThread;

    public Integer getNoOfDbThreads() {
        return noOfDbThreads;
    }

    public void setNoOfDbThreads(Integer noOfDbThreads) {
        this.noOfDbThreads = noOfDbThreads;
    }

    public Integer getDocsPerThread() {
        return docsPerThread;
    }

    public void setDocsPerThread(Integer docsPerThread) {
        this.docsPerThread = docsPerThread;
    }

    @Override
    public String toString() {
        return "FullIndexRequest{" +
                "noOfDbThreads=" + noOfDbThreads +
                ", docsPerThread=" + docsPerThread +
                '}';
    }
}
