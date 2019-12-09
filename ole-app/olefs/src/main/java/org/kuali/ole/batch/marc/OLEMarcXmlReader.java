package org.kuali.ole.batch.marc;

import org.marc4j.ErrorHandler.Error;
import org.marc4j.RecordStack;
import org.marc4j.marc.Record;
import org.xml.sax.InputSource;
import org.marc4j.ErrorHandler;


import java.io.InputStream;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

/**
 * Created with IntelliJ IDEA.
 * User: meenrajd
 * Date: 9/2/13
 * Time: 11:57 AM
 * To change this template use File | Settings | File Templates.
 */
public class OLEMarcXmlReader implements OLEMarcReader {

    private RecordStack queue;
    private ErrorHandler errors;
    private int errCount;

    public OLEMarcXmlReader(InputSource input) throws ExecutionException, InterruptedException {
        this.queue = new RecordStack();
        this.errors = new OLEMarcErrorHandler();
        /*ExecutorService executor = Executors.newSingleThreadExecutor();
        //OLEMarcXmlParserThread producer = new OLEMarcXmlParserThread(queue, input, errors);
        Runnable worker = new OLEMarcXmlParserThread(queue, input, errors);
        Future future = executor.submit(worker);
        if(future.get()==null){
            executor.shutdown();
        }*/
        OLEMarcXmlParserThread producer = new OLEMarcXmlParserThread(queue, input, errors);
        producer.start();
    }

    public OLEMarcXmlReader(InputStream input) throws ExecutionException, InterruptedException {
        this(new InputSource(input));
    }

    /**
     * Returns true if the iteration has more records, false otherwise.
     *
     * @return boolean - true if the iteration has more records, false otherwise
     */
    public boolean hasNext() {
        return queue.hasNext();
    }

    /**
     * Returns the next record in the iteration.
     *
     * @return Record - the record object
     */
    public Record next() {
        return queue.pop();
    }

    @Override
    public boolean hasErrors() {
        return errors.hasErrors();
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<Error> getErrors() {
        return errors.getErrors();
    }

    @Override
    public Error getError() {
        return (Error) errors.getErrors().get(0);
    }

    public void clearErrors(){
       errors.getErrors().clear();
       OLEMarcErrorHandler errorHandler = (OLEMarcErrorHandler)errors;
        errorHandler.getErrorMap().clear();
    }

}