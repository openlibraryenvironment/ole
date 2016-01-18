package org.kuali.ole.scheduler;


public class BatchJobLauncher {

    private String url;
    private String profileName;
    private String batchType;

    public BatchJobLauncher(String profileName, String batchType) {
        this.profileName = profileName;
        this.batchType = batchType;
    }

    public void start() {
        System.out.println("start batch job");
        
        // invoke BatchRestController method UploadFile of BatchRestController
        
    }
}
