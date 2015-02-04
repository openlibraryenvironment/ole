package org.kuali.ole.ingest.krms.pojo;


import org.kuali.ole.ingest.pojo.OverlayOption;
import org.kuali.ole.ingest.pojo.ProfileAttributeBo;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: vivekb
 * Date: 8/22/12
 * Time: 3:04 PM
 * To change this template use File | Settings | File Templates.
 */
public class OleKrmsAgenda {
    private String name;
    private List<ProfileAttributeBo> profileAttributes;
    private List<OleKrmsRule> rules;
    private List<OverlayOption> overlayOptions;

    public List<ProfileAttributeBo> getProfileAttributes() {
        return profileAttributes;
    }

    public void setProfileAttributes(List<ProfileAttributeBo> profileAttributes) {
        this.profileAttributes = profileAttributes;
    }

    /**
     * Gets the name attribute.
     * @return  Returns the name.
     */
    public String getName() {
        return name;
    }
    /**
     * Sets the name attribute value.
     * @param name The name to set.
     */
    public void setName(String name) {
        this.name = name;
    }
    /**
     * Gets the rules attribute.
     * @return  Returns the rules.
     */
    public List<OleKrmsRule> getRules() {
        return rules;
    }
    /**
     * Sets the rules attribute value.
     * @param rules The rules to set.
     */
    public void setRules(List<OleKrmsRule> rules) {
        this.rules = rules;
    }
    /**
     * Gets the overlayOptions attribute.
     * @return  Returns the overlayOptions.
     */
    public List<OverlayOption> getOverlayOptions() {
        return overlayOptions;
    }
    /**
     * Sets the overlayOptions attribute value.
     * @param overlayOptions The overlayOptions to set.
     */
    public void setOverlayOption(List<OverlayOption> overlayOptions) {
        this.overlayOptions = overlayOptions;
    }
}
