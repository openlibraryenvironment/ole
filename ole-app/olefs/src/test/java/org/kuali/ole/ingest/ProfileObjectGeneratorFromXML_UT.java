package org.kuali.ole.ingest;

import org.junit.Test;
import org.kuali.ole.ingest.pojo.Profile;

import java.io.File;
import java.net.URL;

import static junit.framework.Assert.assertNotNull;

/**
 * Created by IntelliJ IDEA.
 * User: pvsubrah
 * Date: 4/8/12
 * Time: 9:31 AM
 * To change this template use File | Settings | File Templates.
 */
public class ProfileObjectGeneratorFromXML_UT {
    @Test
    public void testBuildProfileFromFileContent() throws Exception {
        URL resource = getClass().getResource("ybp-sample-profile.xml");
        File file = new File(resource.toURI());
        String profileXML = new FileUtil().readFile(file);

        ProfileObjectGeneratorFromXML profileObjectGeneratorFromXML =
                new ProfileObjectGeneratorFromXML();

        Profile profile = profileObjectGeneratorFromXML.buildProfileFromFileContent(profileXML);
        assertNotNull(profile);
    }
}
