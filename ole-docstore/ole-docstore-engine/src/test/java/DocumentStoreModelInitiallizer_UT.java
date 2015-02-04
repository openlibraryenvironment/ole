import org.junit.Ignore;
import org.junit.Test;
import org.kuali.ole.DocumentStoreModelInitiallizer;
import org.kuali.ole.RepositoryManager;
import org.kuali.ole.BaseTestCase;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.jcr.*;

/**
 * Created by IntelliJ IDEA.
 * User: pvsubrah
 * Date: 1/10/12
 * Time: 11:03 AM
 * To change this template use File | Settings | File Templates.
 */
@Ignore
public class DocumentStoreModelInitiallizer_UT
        extends BaseTestCase {
    private static final Logger LOG = LoggerFactory.getLogger(DocumentStoreModelInitiallizer_UT.class);

    @Test
    public void initDocStoreModel() throws Exception {
        DocumentStoreModelInitiallizer documentStoreModelInitiallizer = new DocumentStoreModelInitiallizer();
        documentStoreModelInitiallizer.init();
        RepositoryManager repositoryManager = RepositoryManager.getRepositoryManager();
        Session session = repositoryManager.getSession();
        dump(session.getRootNode());
        repositoryManager.logout(session);
    }

    private static void dump(Node node) throws RepositoryException {
        LOG.info(node.getPath());
        if (node.getName().equals("jcr:system")) {
            return;
        }

        PropertyIterator properties = node.getProperties();
        while (properties.hasNext()) {
            Property property = properties.nextProperty();
            if (property.getDefinition().isMultiple()) {
                Value[] values = property.getValues();
                for (int i = 0; i < values.length; i++) {
                    LOG.info(property.getPath() + " = " + values[i].getString());
                }
            }
            else {
                LOG.info(property.getPath() + " = " + property.getString());
            }
        }

        NodeIterator nodes = node.getNodes();
        while (nodes.hasNext()) {
            dump(nodes.nextNode());
        }
    }
}
