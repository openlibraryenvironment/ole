package org.kuali.ole.ingest;

import com.thoughtworks.xstream.XStream;
import org.kuali.ole.ingest.pojo.*;

import java.io.IOException;
import java.net.URISyntaxException;

/**
 * ProfileObjectGeneratorFromXML used to build the Profile based on fileContent
 */
public class ProfileObjectGeneratorFromXML {
    /**
     * This method return Profile Object.
     * This method build the Profile based on fileContent.
     * @param fileContent
     * @return  Profile
     * @throws java.net.URISyntaxException
     * @throws java.io.IOException
     */
    public Profile buildProfileFromFileContent(String fileContent) throws URISyntaxException, IOException {
        XStream xStream = new XStream();
        xStream.alias("profile", Profile.class);
        xStream.alias("attribute", ProfileAttributeBo.class);
        xStream.alias("rule", OleRule.class);
        xStream.alias("action", OleAction.class);
        xStream.alias("routeTo", OleRoute.class);
        xStream.alias("incomingField", MatchPoint.class);
        xStream.alias("existingField", MatchPoint.class);
        xStream.registerConverter(new ProfileAttributeConverter());
        Object object = xStream.fromXML(fileContent);
        return (Profile) object;
    }
}