package org.kuali.ole.ingest.pojo;

import java.util.List;

/**
 * Profile is a business object class for Profile
 */
public class Profile {
    private String name;
    private String description;
    private String contextName;
    private String categoryName;
    private List<OleRule> rules;
    private List<ProfileAttributeBo> profileAttributes;
    /**
     * Sets the name attribute value.
     * @param name The name to set.
     */
    public void setName(String name) {
        this.name = name;
    }
    /**
     * Sets the description attribute value.
     * @param description The description to set.
     */
    public void setDescription(String description) {
        this.description = description;
    }
    /**
     * Sets the contextName attribute value.
     * @param contextName The contextName to set.
     */
    public void setContextName(String contextName) {
        this.contextName = contextName;
    }
    /**
     * Sets the categoryName attribute value.
     * @param categoryName The categoryName to set.
     */
    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }
    /**
     * Sets the rules attribute value.
     * @param rules The rules to set.
     */
    public void setRules(List<OleRule> rules) {
        this.rules = rules;
    }
    /**
     * Gets the name attribute.
     * @return  Returns the name.
     */
    public String getName() {
        return name;
    }
    /**
     * Gets the description attribute.
     * @return  Returns the description.
     */
    public String getDescription() {
        return description;
    }
    /**
     * Gets the contextName attribute.
     * @return  Returns the contextName.
     */
    public String getContextName() {
        return contextName;
    }
    /**
     * Gets the categoryName attribute.
     * @return  Returns the categoryName.
     */
    public String getCategoryName() {
        return categoryName;
    }
    /**
     * Gets the rules attribute.
     * @return  Returns the rules.
     */
    public List<OleRule> getRules() {
        return rules;
    }
    /**
     * Gets the profileAttributes attribute.
     * @return  Returns the profileAttributes.
     */
    public List<ProfileAttributeBo> getProfileAttributes() {
        return profileAttributes;
    }
    /**
     * Gets the profileAttributes attribute.
     * @return  Returns the profileAttributes.
     */
    public void setProfileAttributes(List<ProfileAttributeBo> profileAttributes) {
        this.profileAttributes = profileAttributes;
    }
}
