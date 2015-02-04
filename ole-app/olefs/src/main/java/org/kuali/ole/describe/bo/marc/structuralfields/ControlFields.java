package org.kuali.ole.describe.bo.marc.structuralfields;

import org.kuali.ole.describe.bo.marc.structuralfields.controlfield006.ControlField006;
import org.kuali.ole.describe.bo.marc.structuralfields.controlfield006.ControlField006Text;
import org.kuali.ole.describe.bo.marc.structuralfields.controlfield007.ControlField007;
import org.kuali.ole.describe.bo.marc.structuralfields.controlfield007.ControlField007Text;
import org.kuali.ole.describe.bo.marc.structuralfields.controlfield008.ControlField008;

import java.util.ArrayList;
import java.util.List;

/**
 * Class for handling control field details
 */
public class ControlFields {

    /**
     * Default Constructor.
     * The default behaviour of this object.
     */
    public ControlFields() {
        controlFields006List.add(new ControlField006Text());
        controlFields007List.add(new ControlField007Text());
    }

    private List<ControlField006Text> controlFields006List = new ArrayList<ControlField006Text>();
    private List<ControlField007Text> controlFields007List = new ArrayList<ControlField007Text>();

    private ControlField006 controlField006;
    private ControlField007 controlField007;
    private ControlField008 controlField008;

    private String controlField001;
    private String controlField003;
    private String controlField005;
    private String localId;

    private String value;
    private String value007;
    private String mapVisible007 = "false";
    private String electronicResourcesVisible007 = "false";
    private String globeVisible007 = "false";
    private String tactileMaterialVisible007 = "false";
    private String projectGraphicVisible007 = "false";
    private String microFormVisible007 = "false";
    private String nonProjectedGraphicVisible007 = "false";
    private String motionPictureVisible007 = "false";
    private String kitVisible007 = "false";
    private String notatedMusicVisible007 = "false";
    private String remoteSensingImageVisible007 = "false";
    private String soundRecordingVisible007 = "false";
    private String textVisible007 = "false";
    private String videoRecordingVisible007 = "false";
    private String unspecifiedVisible007 = "false";
    private String value008;
    private String id006;
    private String id007;


    private String booksVisible= "false";
    private String musicVisible= "false";
    private String mapVisible= "false";
    private String mixedMaterialVisible= "false";
    private String visualMaterialsVisible= "false";
    private String continuingResourcesVisible= "false";
    private String computerFilesVisible= "false";

    private String booksVisible008= "false";
    private String musicVisible008= "false";
    private String mapVisible008= "false";
    private String mixedMaterialVisible008= "false";
    private String visualMaterialsVisible008= "false";
    private String continuingResourcesVisible008= "false";
    private String computerFilesVisible008= "false";

    public static final String CONTROL_FIELD_001 = "001" ;
    public static final String CONTROL_FIELD_003 = "003";
    public static final String CONTROL_FIELD_005 = "005";
    public static final String CONTROL_FIELD_006 = "006";
    public static final String CONTROL_FIELD_007 = "007";
    public static final String CONTROL_FIELD_008 = "008";



    public String getId007() {
        return id007;
    }

    public void setId007(String id007) {
        this.id007 = id007;
    }

    public List<ControlField006Text> getControlFields006List() {
        return controlFields006List;
    }

    public void setControlFields006List(List<ControlField006Text> controlFields006List) {
        this.controlFields006List = controlFields006List;
    }

    public List<ControlField007Text> getControlFields007List() {
        return controlFields007List;
    }

    public void setControlFields007List(List<ControlField007Text> controlFields007List) {
        this.controlFields007List = controlFields007List;
    }

    public void setControlField001(String controlField001) {
        this.controlField001 = controlField001;
    }

    public String getControlField001() {
        return controlField001;
    }

    public String getControlField003() {
        return controlField003;
    }

    public void setControlField003(String controlField003) {
        this.controlField003 = controlField003;
    }


    public String getControlField005() {
        return controlField005;
    }

    public void setControlField005(String controlField005) {
        this.controlField005 = controlField005;
    }

    public ControlField006 getControlField006() {
        return controlField006;
    }

    public void setControlField006(ControlField006 controlField006) {
        this.controlField006 = controlField006;
    }

    public ControlField007 getControlField007() {
        return controlField007;
    }

    public void setControlField007(ControlField007 controlField007) {
        this.controlField007 = controlField007;
    }

    public ControlField008 getControlField008() {
        return controlField008;
    }

    public void setControlField008(ControlField008 controlField008) {
        this.controlField008 = controlField008;
    }

    public String getLocalId() {
        return localId;
    }

    public void setLocalId(String localId) {
        this.localId = localId;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getValue007() {
        return value007;
    }

    public void setValue007(String value007) {
        this.value007 = value007;
    }

    public String getMapVisible007() {
        return mapVisible007;
    }

    public void setMapVisible007(String mapVisible007) {
        this.mapVisible007 = mapVisible007;
    }

    public String getElectronicResourcesVisible007() {
        return electronicResourcesVisible007;
    }

    public String getGlobeVisible007() {
        return globeVisible007;
    }

    public void setGlobeVisible007(String globeVisible007) {
        this.globeVisible007 = globeVisible007;
    }

    public String getTactileMaterialVisible007() {
        return tactileMaterialVisible007;
    }

    public void setTactileMaterialVisible007(String tactileMaterialVisible007) {
        this.tactileMaterialVisible007 = tactileMaterialVisible007;
    }

    public String getProjectGraphicVisible007() {
        return projectGraphicVisible007;
    }

    public void setProjectGraphicVisible007(String projectGraphicVisible007) {
        this.projectGraphicVisible007 = projectGraphicVisible007;
    }

    public String getMicroFormVisible007() {
        return microFormVisible007;
    }

    public void setMicroFormVisible007(String microFormVisible007) {
        this.microFormVisible007 = microFormVisible007;
    }

    public String getNonProjectedGraphicVisible007() {
        return nonProjectedGraphicVisible007;
    }

    public void setNonProjectedGraphicVisible007(String nonProjectedGraphicVisible007) {
        this.nonProjectedGraphicVisible007 = nonProjectedGraphicVisible007;
    }

    public String getMotionPictureVisible007() {
        return motionPictureVisible007;
    }

    public void setMotionPictureVisible007(String motionPictureVisible007) {
        this.motionPictureVisible007 = motionPictureVisible007;
    }

    public String getKitVisible007() {
        return kitVisible007;
    }

    public void setKitVisible007(String kitVisible007) {
        this.kitVisible007 = kitVisible007;
    }

    public String getNotatedMusicVisible007() {
        return notatedMusicVisible007;
    }

    public void setNotatedMusicVisible007(String notatedMusicVisible007) {
        this.notatedMusicVisible007 = notatedMusicVisible007;
    }

    public String getRemoteSensingImageVisible007() {
        return remoteSensingImageVisible007;
    }

    public void setRemoteSensingImageVisible007(String remoteSensingImageVisible007) {
        this.remoteSensingImageVisible007 = remoteSensingImageVisible007;
    }

    public String getSoundRecordingVisible007() {
        return soundRecordingVisible007;
    }

    public void setSoundRecordingVisible007(String soundRecordingVisible007) {
        this.soundRecordingVisible007 = soundRecordingVisible007;
    }

    public String getTextVisible007() {
        return textVisible007;
    }

    public void setTextVisible007(String textVisible007) {
        this.textVisible007 = textVisible007;
    }

    public String getVideoRecordingVisible007() {
        return videoRecordingVisible007;
    }

    public void setVideoRecordingVisible007(String videoRecordingVisible007) {
        this.videoRecordingVisible007 = videoRecordingVisible007;
    }

    public String getUnspecifiedVisible007() {
        return unspecifiedVisible007;
    }

    public void setUnspecifiedVisible007(String unspecifiedVisible007) {
        this.unspecifiedVisible007 = unspecifiedVisible007;
    }

    public void setElectronicResourcesVisible007(String electronicResourcesVisible007) {
        this.electronicResourcesVisible007 = electronicResourcesVisible007;
    }

    public String getValue008() {
        return value008;
    }

    public void setValue008(String value008) {
        this.value008 = value008;
    }
    public String getId006() {
        return id006;
    }

    public void setId(String id006) {
        this.id006 = id006;
    }

    public String getBooksVisible() {
        return booksVisible;
    }

    public void setBooksVisible(String booksVisible) {
        this.booksVisible = booksVisible;
    }

    public String getMusicVisible() {
        return musicVisible;
    }

    public void setMusicVisible(String musicVisible) {
        this.musicVisible = musicVisible;
    }

    public String getMapVisible() {
        return mapVisible;
    }

    public void setMapVisible(String mapVisible) {
        this.mapVisible = mapVisible;
    }

    public String getMixedMaterialVisible() {
        return mixedMaterialVisible;
    }

    public void setMixedMaterialVisible(String mixedMaterialVisible) {
        this.mixedMaterialVisible = mixedMaterialVisible;
    }

    public String getVisualMaterialsVisible() {
        return visualMaterialsVisible;
    }

    public void setVisualMaterialsVisible(String visualMaterialsVisible) {
        this.visualMaterialsVisible = visualMaterialsVisible;
    }

    public String getContinuingResourcesVisible() {
        return continuingResourcesVisible;
    }

    public void setContinuingResourcesVisible(String continuingResourcesVisible) {
        this.continuingResourcesVisible = continuingResourcesVisible;
    }

    public String getComputerFilesVisible() {
        return computerFilesVisible;
    }

    public void setComputerFilesVisible(String computerFilesVisible) {
        this.computerFilesVisible = computerFilesVisible;
    }

    public String getBooksVisible008() {
        return booksVisible008;
    }

    public void setBooksVisible008(String booksVisible008) {
        this.booksVisible008 = booksVisible008;
    }

    public String getMusicVisible008() {
        return musicVisible008;
    }

    public void setMusicVisible008(String musicVisible008) {
        this.musicVisible008 = musicVisible008;
    }

    public String getMapVisible008() {
        return mapVisible008;
    }

    public void setMapVisible008(String mapVisible008) {
        this.mapVisible008 = mapVisible008;
    }

    public String getMixedMaterialVisible008() {
        return mixedMaterialVisible008;
    }

    public void setMixedMaterialVisible008(String mixedMaterialVisible008) {
        this.mixedMaterialVisible008 = mixedMaterialVisible008;
    }

    public String getVisualMaterialsVisible008() {
        return visualMaterialsVisible008;
    }

    public void setVisualMaterialsVisible008(String visualMaterialsVisible008) {
        this.visualMaterialsVisible008 = visualMaterialsVisible008;
    }

    public String getContinuingResourcesVisible008() {
        return continuingResourcesVisible008;
    }

    public void setContinuingResourcesVisible008(String continuingResourcesVisible008) {
        this.continuingResourcesVisible008 = continuingResourcesVisible008;
    }

    public String getComputerFilesVisible008() {
        return computerFilesVisible008;
    }

    public void setComputerFilesVisible008(String computerFilesVisible008) {
        this.computerFilesVisible008 = computerFilesVisible008;
    }
}
