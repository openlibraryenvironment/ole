package org.kuali.ole.docstore.common.document.factory;

/**
 * Created by sheiksalahudeenm on 4/5/15.
 */
public class JAXBContextThread  implements Runnable {
    private String command;

    public JAXBContextThread(String s){
        this.command=s;
    }

    @Override
    public void run() {
        JAXBContextFactoryTest jaxbContextFactoryTest = new JAXBContextFactoryTest();
        try {
            jaxbContextFactoryTest.deserializeAndSerialize();
        } catch (Exception e) {
            e.printStackTrace();
        }
        //processCommand();

    }

    private void processCommand() {
        try {
            Thread.sleep(0);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Override
    public String toString(){
        return this.command;
    }
}
