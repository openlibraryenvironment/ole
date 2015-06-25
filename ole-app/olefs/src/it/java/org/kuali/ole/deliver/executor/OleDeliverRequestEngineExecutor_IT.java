package org.kuali.ole.deliver.executor;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.kuali.ole.KFSTestCaseBase;
import org.kuali.ole.SpringBaseTestCase;
import org.kuali.rice.kew.engine.RouteContext;
import org.kuali.rice.kew.routeheader.DocumentRouteHeaderValue;
import org.kuali.rice.krms.api.KrmsApiServiceLocator;
import org.kuali.rice.krms.api.engine.Engine;
import org.kuali.rice.krms.api.engine.EngineResults;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.transaction.TransactionConfiguration;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.Assert.assertEquals;

/**
 * Created with IntelliJ IDEA.
 * User: vivekb
 * Date: 1/22/13
 * Time: 7:23 PM
 * To change this template use File | Settings | File Templates.
 */

public class OleDeliverRequestEngineExecutor_IT extends KFSTestCaseBase {
    @Test
    public void testExecute(){
        OleDeliverRequestEngineExecutor oleDeliverRequestEngineExecutor = new OleDeliverRequestEngineExecutor();
        String docContent = "<documentContent>\n" +
                "    <applicationContent>\n" +
                "        <org.kuali.rice.krad.workflow.KualiDocumentXmlMaterializer>          \n" +
                "            <document class=\"org.kuali.rice.krad.maintenance.MaintenanceDocumentBase\">                           \n" +
                "                <newMaintainableObject class=\"org.kuali.ole.deliver.maintenance.OleDeliverRequestMaintenanceImpl\">                    \n" +
                "                    <dataObject class=\"org.kuali.ole.deliver.bo.OleDeliverRequestBo\">                      \n" +
                "                        <borrowerId>00377755J</borrowerId>                      \n" +
                "                        <requestTypeId>7</requestTypeId>                      \n" +
                "                        <itemId>221127885</itemId>\n" +
                "                        <itemType>BOOK</itemType>\n" +
                "                        <itemLibrary>B-EDUC</itemLibrary>                                                                \n" +
                "                        <requestTypeCode>Page/Delivery Request</requestTypeCode>                                                                \n" +
                "                        <olePatron>\n" +
                "                            <oleBorrowerType>                    \n" +
                "                                <borrowerTypeCode>UGRAD</borrowerTypeCode>                            \n" +
                "                            </oleBorrowerType>\n" +
                "                        </olePatron>                                              \n" +
                "                    </dataObject>                   \n" +
                "                </newMaintainableObject>                \n" +
                "            </document>\n" +
                "        </org.kuali.rice.krad.workflow.KualiDocumentXmlMaterializer>\n" +
                "    </applicationContent>\n" +
                "</documentContent>\n";
        RouteContext routeContext =  new RouteContext();
        DocumentRouteHeaderValue documentRouteHeaderValue = new DocumentRouteHeaderValue();
        documentRouteHeaderValue.setDocContent(docContent);
        routeContext.setDocument(documentRouteHeaderValue);
        Engine engine = KrmsApiServiceLocator.getEngine();
        //EngineResults engineResults =  oleDeliverRequestEngineExecutor.execute(routeContext, engine);
        //assertEquals("F:OLE10006", engineResults.getAttribute("peopleFlowsSelected"));
    }
}
