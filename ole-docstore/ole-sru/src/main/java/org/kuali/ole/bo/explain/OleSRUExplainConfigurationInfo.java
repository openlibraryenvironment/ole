package org.kuali.ole.bo.explain;

/**
 * Created with IntelliJ IDEA.
 * User: ?
 * Date: 7/19/12
 * Time: 5:11 PM
 * To change this template use File | Settings | File Templates.
 */
public class OleSRUExplainConfigurationInfo {

    private OleSRUExplainConfigDefaultTagField defaultValue;
    private OleSRUExplainConfigSettingTagField setting;
    private OleSRUExplainConfigSupportTagField supports;

    public OleSRUExplainConfigDefaultTagField getDefaultValue() {
        return defaultValue;
    }

    public void setDefaultValue(OleSRUExplainConfigDefaultTagField defaultValue) {
        this.defaultValue = defaultValue;
    }

    public OleSRUExplainConfigSettingTagField getSetting() {
        return setting;
    }

    public void setSetting(OleSRUExplainConfigSettingTagField setting) {
        this.setting = setting;
    }

    public OleSRUExplainConfigSupportTagField getSupports() {
        return supports;
    }

    public void setSupports(OleSRUExplainConfigSupportTagField supports) {
        this.supports = supports;
    }

    @Override
    public String toString() {
        return "Explain ConfigurationInfo{" +
                "defaultValue=" + defaultValue +
                ", setting=" + setting +
                ", supports=" + supports +
                '}';
    }
}
