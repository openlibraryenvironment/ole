function showControlFields007(controlField007Format) {

    if (controlField007Format == "a") {
        jq('#Control_Field_001_h3').val(controlField007Format);
    }
    else if (controlField007Format == "c") {
        jq('#Control_Field_001_h3').val(controlField007Format);
    }
    else if (controlField007Format == "d") {
        jq('#Control_Field_001_h3').val(controlField007Format);
        //jq('#Control_Field_007_Globe').show();
    }
    else if (controlField007Format == "f") {
        jq('#Control_Field_001_h3').val(controlField007Format);
        //jq('#Control_Field_007_TactileMaterial').show();
    }
    else if (controlField007Format == "g") {
        jq('#Control_Field_001_h3').val(controlField007Format);
        //jq('#Control_Field_007_ProjectGraphic').show();
    }
    else if (controlField007Format == "h") {
        jq('#Control_Field_001_h3').val(controlField007Format);
        //jq('#Control_Field_007_MicroForm').show();
    }
    else if (controlField007Format == "k") {
        jq('#Control_Field_001_h3').val(controlField007Format);
        //jq('#Control_Field_007_NonProjectedGraphic').show();
    }
    else if (controlField007Format == "m") {
        jq('#Control_Field_001_h3').val(controlField007Format);
        //jq('#Control_Field_007_MotionPicture').show();
    }
    else if (controlField007Format == "o") {
        jq('#Control_Field_001_h3').val(controlField007Format);
        //jq('#Control_Field_007_Kit').show();
    }
    else if (controlField007Format == "q") {
        jq('#Control_Field_001_h3').val(controlField007Format);
        //jq('#Control_Field_007_NotatedMusic').show();
    }
    else if (controlField007Format == "r") {
        jq('#Control_Field_001_h3').val(controlField007Format);
        //jq('#Control_Field_007_RemoteSensingImage').show();
    }
    else if (controlField007Format == "s") {
        jq('#Control_Field_001_h3').val(controlField007Format);
        //jq('#Control_Field_007_SoundRecording').show();
    }
    else if (controlField007Format == "t") {
        jq('#Control_Field_001_h3').val(controlField007Format);
        //jq('#Control_Field_007_Text').show();
    }
    else if (controlField007Format == "v") {
        jq('#Control_Field_001_h3').val(controlField007Format);
        //jq('#Control_Field_007_VideoRecording').show();
    }
    else if (controlField007Format == "z") {
        jq('#Control_Field_001_h3').val(controlField007Format);
        //jq('#Control_Field_007_Unspecified').show();
    }
    jq('#007load').focus().click();
}

function setControlIndex007(controlField007Format) {
    if (controlField007Format != null) {
        if (controlField007Format == "a") {
            controlIndex007 = 0;
        }
        else if (controlField007Format == "c") {
            controlIndex007 = 1;
        }
        else if (controlField007Format == "d") {
            controlIndex007 = 2;
        }
        else if (controlField007Format == "f") {
            controlIndex007 = 3;
        }
        else if (controlField007Format == "g") {
            controlIndex007 = 4;
        }
        else if (controlField007Format == "h") {
            controlIndex007 = 5;
        }
        else if (controlField007Format == "k") {
            controlIndex007 = 6;
        }
        else if (controlField007Format == "m") {
            controlIndex007 = 7;
        }
        else if (controlField007Format == "o") {
            controlIndex007 = 8;
        }
        else if (controlField007Format == "q") {
            controlIndex007 = 9;
        }
        else if (controlField007Format == "r") {
            controlIndex007 = 10;
        }
        else if (controlField007Format == "s") {
            controlIndex007 = 11;
        }
        else if (controlField007Format == "t") {
            controlIndex007 = 12;
        }
        else if (controlField007Format == "v") {
            controlIndex007 = 13;
        }
        else if (controlField007Format == "z") {
            controlIndex007 = 14;
        }
    }
}

function controlField007Set() {
    valueChange007 = false;
    var controlField007Text = "";
    var controlText = null;
    var controlField007Format = jq("#007Field_0_control").val();
    controlField007Format= jq("#007Field_0_common_control").val();
    setControlIndex007(controlField007Format);
    var controlIndex = controlIndex007;
    //Append all structural fields for 007 control field text box
    if (controlIndex != null) {
        for (var i = 0; i < 20; i++) {
            controlText = jq('#007Field_' + controlIndex + '_' + i + '_control').val();
            if (i == 16 && controlIndex == 7) {
                if (controlText == "") {
                    controlText = "||||||";
                }
                else if (controlText.length == 1) {
                    controlText = controlText + "|||||";
                }
                else if (controlText.length == 2) {
                    controlText = controlText + "||||";
                }
                else if (controlText.length == 3) {
                    controlText = controlText + "|||";
                }
                else if (controlText.length == 4) {
                    controlText = controlText + "||";
                }
                else if (controlText.length == 5) {
                    controlText = controlText + "|";
                }
                jq('#007Field_' + controlIndex + '_' + i + '_control').val(controlText);
            }
            if (i == 5 && controlIndex == 5) {
                if (controlText == "") {
                    controlText = "|||";
                }
                else if (controlText.length == "1") {
                    controlText = controlText + "||";
                }
                else if (controlText.length == "2") {
                    controlText = controlText + "|";
                }
                jq('#007Field_' + controlIndex + '_' + i + '_control').val(controlText);

            }
            if (controlIndex == 1 && i == 5) {
                if (controlText == "") {
                    controlText = "|||";
                }
                else if (controlText == "000") {
                    controlText = "|||";
                }
                jq('#007Field_' + controlIndex + '_' + i + '_control').val(controlText);
            }
            if (controlText != null) {
                controlField007Text = controlField007Text + controlText;
            }

            else {
                break;
            }
        }
    }
    jq("#Control_Field_007_line" + controlField007Id + "_control").val(controlField007Format + controlField007Text);
    jq('#Control_Field_007_line' + controlField007Id + '_control').css({"background-color": "white", "border-color": "#C0C0C0"});
}
function controlField007EditValue() {
    var id007 = jq("#Control_Field_001_h4").val();
    jq('#' + id007).css({"background-color": "#EFFFEF", "border-color": "#000000"});
    var text007 = jq("#" + id007).val();
    if (text007 != "" && text007 != null) {
        var controlField007Format = text007.substring(0, 1);
        var originalValue = jq("#007Field_0_control").val();
        var hiddenValue = jq("#Control_Field_001_h3").val();
        if (controlField007Format == originalValue) {
            jq("#007Field_0_control").val(controlField007Format);
        } else if (hiddenValue == controlField007Format) {
            jq("#007Field_0_control").val(controlField007Format);
        } else {
            jq("#" + id007).val("");
            text007 = "";
        }
        if (text007 != "" && text007 != null) {
            jq("#007Field_0_control").val(text007.substring(0, 1));
            if (controlField007Format == "a") {
                var startChar = 1;
                var endChar = 2;
                for (var i = 0; i < 17; i++) {
                    jq('#007Field_0_' + i + '_control').val(text007.substring(startChar, endChar));
                    startChar++;
                    endChar++;
                }
            }
            else if (controlField007Format == "c") {
                var startChar = 1;
                var endChar = 2;
                for (var i = 0; i < 17; i++) {
                    if (startChar == 6) {
                        endChar = endChar + 2;
                        jq('#007Field_1_' + i + '_control').val(text007.substring(startChar, endChar));
                        startChar = startChar + 2;
                    } else {
                        jq('#007Field_1_' + i + '_control').val(text007.substring(startChar, endChar));
                    }
                    startChar++;
                    endChar++;
                }
            }
            else if (controlField007Format == "d") {
                var startChar = 1;
                var endChar = 2;
                for (var i = 0; i < 17; i++) {
                    jq('#007Field_2_' + i + '_control').val(text007.substring(startChar, endChar));
                    startChar++;
                    endChar++;
                }
            }
            else if (controlField007Format == "f") {
                var startChar = 1;
                var endChar = 2;
                for (var i = 0; i < 17; i++) {
                    jq('#007Field_3_' + i + '_control').val(text007.substring(startChar, endChar));
                    startChar++;
                    endChar++;
                }
            }
            else if (controlField007Format == "g") {
                var startChar = 1;
                var endChar = 2;
                for (var i = 0; i < 17; i++) {
                    jq('#007Field_4_' + i + '_control').val(text007.substring(startChar, endChar));
                    startChar++;
                    endChar++;
                }
            }
            else if (controlField007Format == "h") {
                var startChar = 1;
                var endChar = 2;
                for (var i = 0; i < 17; i++) {
                    if (i == 5) {
                        endChar = endChar + 2;
                        jq('#007Field_5_' + i + '_control').val(text007.substring(startChar, endChar));
                        startChar = startChar + 2;
                    } else {
                        jq('#007Field_5_' + i + '_control').val(text007.substring(startChar, endChar));
                    }
                    startChar++;
                    endChar++;
                }
            }
            else if (controlField007Format == "k") {
                var startChar = 1;
                var endChar = 2;
                for (var i = 0; i < 17; i++) {
                    jq('#007Field_6_' + i + '_control').val(text007.substring(startChar, endChar));
                    startChar++;
                    endChar++;
                }
            }
            else if (controlField007Format == "m") {
                var startChar = 1;
                var endChar = 2;
                for (var i = 0; i < 25; i++) {
                    if (startChar == 17) {
                        endChar = endChar + 5;
                        jq('#007Field_7_' + i + '_control').val(text007.substring(startChar, endChar));
                        startChar = startChar + 5;
                    } else {
                        jq('#007Field_7_' + i + '_control').val(text007.substring(startChar, endChar));
                    }
                    startChar++;
                    endChar++;
                }
            }
            else if (controlField007Format == "o") {
                jq('#007Field_8_0_control').val(text007.substring(1));
            }
            else if (controlField007Format == "q") {
                jq('#007Field_9_0_control').val(text007.substring(1));
            }
            else if (controlField007Format == "r") {
                var startChar = 1;
                var endChar = 2;
                for (var i = 0; i < 17; i++) {
                    if (startChar == 9) {
                        endChar++;
                        jq('#007Field_10_' + i + '_control').val(text007.substring(startChar, endChar));
                        startChar++;
                    } else {
                        jq('#007Field_10_' + i + '_control').val(text007.substring(startChar, endChar));
                    }
                    startChar++;
                    endChar++;
                }
            }
            else if (controlField007Format == "s") {
                var startChar = 1;
                var endChar = 2;
                for (var i = 0; i < 17; i++) {
                    jq('#007Field_11_' + i + '_control').val(text007.substring(startChar, endChar));
                    startChar++;
                    endChar++;
                }
            }
            else if (controlField007Format == "t") {
                jq('#007Field_12_0_control').val(text007.substring(1));
            }
            else if (controlField007Format == "v") {
                var startChar = 1;
                var endChar = 2;
                for (var i = 0; i < 17; i++) {
                    jq('#007Field_13_' + i + '_control').val(text007.substring(startChar, endChar));
                    startChar++;
                    endChar++;
                }
            }
            else if (controlField007Format == "z") {
                jq('#007Field_14_0_control').val(text007.substring(1));
            }
        }
    }
}

function controlField007Edit(id007) {
    for (var i = 0; i < 20; i++) {
        jq('#Control_Field_007_line' + i + '_control').css({"background-color": "white", "border-color": "#C0C0C0"});
    }
    jq('#' + id007).css({"background-color": "#EFFFEF", "border-color": "#000000"});
    var text007 = jq("#" + id007).val();
    if (text007 != "") {
        var controlField007Format = text007.substring(0, 1);
        jq("#007Field_0_control").val(text007.substring(0, 1));
        jq("#Control_Field_001_h4").val(id007);
        jq("#Control_Field_001_h3").val(text007.substring(0, 1));
        controlField007FormatChange();
    }
}
function clearSection007() {
    var id007 = jq("#Control_Field_001_h4").val();
    jq("#" + id007).val(" ");
}
function clearIndex007(buttonId) {
    var txtId;
    buttonId = jq(buttonId).attr("id");
    var n = buttonId.split("_");
    for (var i = 0; i < n.length; i++)
        if (n[i].contains('line')) {
            txtId = n[i].split('line');
        }
    jq('#Control_Field_007_line' + txtId[1] + '_control').val(" ");
}
function getIndex007(buttonId) {
    var txtId;
    buttonId = jq(buttonId).attr("id");
    var n = buttonId.split("_");
    for (var i = 0; i < n.length; i++)
        if (n[i].contains('line')) {
            txtId = n[i].split('line');
        }
    controlField007Id = txtId[1];
    jq('#007Field_0_control').focus().click();
    for (var i = 0; i < 9; i++) {
        jq('#Control_Field_007_line' + i + '_control').css({"background-color": "white", "border-color": "#C0C0C0"});
    }
    controlField007Edit('Control_Field_007_line' + controlField007Id + '_control');
}

function getIndex007Add(id) {
    var JSONObject = JSON.stringify(id.extraData);//.split("actionParameters[selectedLineIndex]");
    var obj = jQuery.parseJSON(JSONObject);
    for (key in obj) {
        if (key == "actionParameters[selectedLineIndex]")
            controlField007Id = parseInt(obj[key]) + 1;
    }
    jq('#Control_Field_007_line' + controlField007Id + '_control').css({"background-color": "#EFFFEF", "border-color": "#000000"});
    jq("#Control_Field_001_h4").val('Control_Field_007_line' + controlField007Id + '_control');
    jq('#007Field_0_control').focus().click();
}
function controlField007FormatChange() {
    var controlField007Format = jq("#007Field_0_control").val();
    showControlFields007(controlField007Format);
}


