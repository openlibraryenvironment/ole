package org.kuali.ole.docstore.discovery.rest;

import org.apache.commons.lang.StringUtils;
import org.kuali.ole.docstore.discovery.service.ServiceLocator;
import org.kuali.ole.docstore.service.BeanLocator;
import org.kuali.ole.docstore.service.IngestNIndexHandlerService;
import org.kuali.ole.repository.DocumentStoreManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: Sreekanth
 * Date: 9/17/12
 * Time: 5:37 PM
 * To change this template use File | Settings | File Templates.
 */

@Path("/documentTitles")
public class DocumentTitleResources {

    private static final Logger LOG = LoggerFactory.getLogger(DocumentTitleResources.class);

    /**
     * @param request
     * @return
     * @throws Exception
     */
    @GET
    @Produces({MediaType.TEXT_XML, MediaType.TEXT_PLAIN})
    public String getTitles(@Context HttpServletRequest request) throws Exception {
        String fieldValue = request.getParameter("title");
        LOG.info("title-->" + fieldValue);
        String xmlResponse = null;
        if (!StringUtils.isBlank(fieldValue)) {
            List<String> titleList = ServiceLocator.getQueryService().getTitleValues(fieldValue);
            xmlResponse = buildResponse(titleList);
            LOG.debug("xml response-->" + xmlResponse);
        }

        return xmlResponse;
    }

    /**
     * @param titleList
     * @return
     */
    private String buildResponse(List<String> titleList) {
        StringBuffer buffer = new StringBuffer();
        buffer.append("<titles>\n");
        for (String title : titleList) {
            buffer.append("\t<title>" + title + "</title>\n");

        }
        buffer.append("</titles>\n");
        return buffer.toString();
    }

}
