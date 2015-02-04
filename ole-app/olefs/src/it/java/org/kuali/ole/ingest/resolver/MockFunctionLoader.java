package org.kuali.ole.ingest.resolver;

import org.kuali.ole.OLEConstants;
import org.kuali.ole.ingest.function.MockISBNFunction;
import org.kuali.ole.ingest.function.MockISSNFunction;
import org.kuali.ole.ingest.function.MockLocationFunction;
import org.kuali.ole.ingest.function.MockOCLCFunction;
import org.kuali.rice.krms.api.repository.function.FunctionDefinition;
import org.kuali.rice.krms.framework.engine.Function;
import org.kuali.rice.krms.framework.type.FunctionTypeService;

/**
 * Created with IntelliJ IDEA.
 * User: ?
 * Date: 12/18/12
 * Time: 7:09 PM
 * To change this template use File | Settings | File Templates.
 */
public class MockFunctionLoader implements FunctionTypeService {

    private MockISBNFunction isbnFunction;
    private MockISSNFunction issnFunction;
    private MockOCLCFunction oclcFunction;
    private MockLocationFunction locationFunction;

    @Override
    public Function loadFunction(FunctionDefinition functionDefinition) {

        if (functionDefinition.getName().equals(OLEConstants.ISBN_FUNCTION_DEF_NAME)) {
            return getISBNFunction();
        } else if(functionDefinition.getName().equals("issnFunction")) {
            return getISSNFunction();
        } else if(functionDefinition.getName().equals("oclcFunction")) {
            return getOCLCFunction();
        } else if(functionDefinition.getName().equals("locationFunction")){
            return getLocationFunction();
        }
        throw new IllegalArgumentException("Failed to load function for the given definition: " + functionDefinition);
    }

    /**
     * Returns the LocationFunction.
     * if LocationFunction is null return new instance,otherwise return existing instance of isbnFunction.
     * @return  isbnFunction
     */
    public Function getLocationFunction() {
        if (null == locationFunction) {
            locationFunction = new MockLocationFunction();
        }
        return locationFunction;
    }

    /**
     * Returns the ISBNFunction.
     * if ISBNFunction is null return new instance,otherwise return existing instance of isbnFunction.
     * @return  isbnFunction
     */
    private Function getISBNFunction() {
        if (null == isbnFunction) {
            isbnFunction = new MockISBNFunction();
        }
        return isbnFunction;
    }
    /**
     * Returns the ISSNFunction.
     * if ISSNFunction is null return new instance,otherwise return existing instance of isbnFunction.
     * @return  issnFunction
     */
    private Function getISSNFunction() {
        if (null == issnFunction) {
            issnFunction = new MockISSNFunction();
        }
        return issnFunction;
    }

    /**
     * Returns the ISSNFunction.
     * if ISSNFunction is null return new instance,otherwise return existing instance of isbnFunction.
     * @return  issnFunction
     */
    private Function getOCLCFunction() {
        if (null == oclcFunction) {
            oclcFunction = new MockOCLCFunction();
        }
        return oclcFunction;
    }
}
