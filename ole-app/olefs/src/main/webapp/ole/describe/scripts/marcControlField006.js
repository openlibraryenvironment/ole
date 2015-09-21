function showControlFields006(controlField006Format) {
    if (controlField006Format == "e" || controlField006Format == "f") {
        jq('#Control_Field_001_h0').val(controlField006Format);
    }
    else if (controlField006Format == "a" || controlField006Format == "t") {
        jq('#Control_Field_001_h0').val(controlField006Format);
    }
    else if (controlField006Format == "c" || controlField006Format == "d" || controlField006Format == "i"
        || controlField006Format == "j") {
        jq('#Control_Field_001_h0').val(controlField006Format);
    }
    else if (controlField006Format == "m") {
        jq('#Control_Field_001_h0').val(controlField006Format);
    }
    else if (controlField006Format == "s") {
        jq('#Control_Field_001_h0').val(controlField006Format);
    }
    else if (controlField006Format == "p") {
        jq('#Control_Field_001_h0').val(controlField006Format);
    }
    else if (controlField006Format == "g" || controlField006Format == "k" || controlField006Format == "o"
        || controlField006Format == "r") {
        jq('#Control_Field_001_h0').val(controlField006Format);
    }
    jq('#006load').focus().click();
}
function setControlIndex006(controlField006Format) {
    if (controlField006Format == "a" || controlField006Format == "t") {
        controlIndex006 = 0;
    }
    else if (controlField006Format == "m") {
        controlIndex006 = 1;
    }
    else if (controlField006Format == "e" || controlField006Format == "f") {
        controlIndex006 = 2;
    }
    else if (controlField006Format == "c" || controlField006Format == "d" || controlField006Format == "i"
        || controlField006Format == "j") {
        controlIndex006 = 3;
    }
    else if (controlField006Format == "s") {
        controlIndex006 = 4;
    }
    else if (controlField006Format == "p") {
        controlIndex006 = 5;
    }
    else if (controlField006Format == "g" || controlField006Format == "k" || controlField006Format == "o"
        || controlField006Format == "r") {
        controlIndex006 = 6;
    }
}
function controlField006Set() {
    valueChange006 = false;
    var controlField006Format = jq("#006Field_0_control").val();
    controlField006Format=jq("#006Field_Common_control").val();
    var controlField006Text = "";
    var controlText = null;

    setControlIndex006(controlField006Format);
    var controlIndex = controlIndex006;
    if (controlIndex != null) {
        for (var i = 0; i < 20; i++) {
            controlText = jq('#006Field_' + controlIndex + '_' + i + '_control').val();
            if (i == 0 && controlIndex == 6) {
                if (controlText == "") {
                    controlText = "###";

                }
                else if (controlText.length == 1) {
                    controlText = controlText + "##";

                }
                else if (controlText.length == 2) {
                    controlText = controlText + "#";

                }
                jq('#006Field_' + controlIndex + '_' + i + '_control').val(controlText);
            }

            if (controlText != null) {
                controlField006Text = controlField006Text + controlText;
            } else {
                break;
            }

        }
    }
    jq('#Control_Field_006_line' + controlField006Id + '_control').val(controlField006Format + controlField006Text);
    jq('#Control_Field_006_line' + controlField006Id + '_control').css({"background-color": "white", "border-color": "#C0C0C0"});
}

function controlField006EditValue() {
    var id006 = jq("#Control_Field_001_h1").val();
    jq('#' + id006).css({"background-color": "#EFFFEF", "border-color": "#000000"});
    var startChar = 1;
    var endChar = 2;
    var text006 = jq("#" + id006).val();
    if (text006 != "" && text006 != null) {
        var controlField006Format = text006.substring(0, 1);
        var originalValue = jq("#006Field_0_control").val();
        var hiddenValue = jq("#Control_Field_001_h0").val();
        if (controlField006Format == originalValue) {
            jq("#006Field_0_control").val(controlField006Format);
        } else if (hiddenValue == controlField006Format) {
            jq("#006Field_0_control").val(controlField006Format);
        } else {
            jq("#" + id006).val("");
            text006 = "";
        }


        if (text006 != "" && text006 != null) {
            if (controlField006Format == "a" || controlField006Format == "t") {
                for (var i = 0; i < 17; i++) {
                    jq('#006Field_0_' + i + '_control').val(text006.substring(startChar, endChar));
                    startChar++;
                    endChar++;
                }
            }
            else if (controlField006Format == "m") {
                for (var i = 0; i < 9; i++) {
                    if (startChar == 1) {
                        endChar = endChar + 3;  // 5
                        jq('#006Field_1_' + i + '_control').val(text006.substring(startChar, endChar));
                        startChar = startChar + 4; //5
                        endChar = endChar + 1; //6
                    }
                    else if (startChar == 7) {
                        endChar = endChar + 1; //9
                        jq('#006Field_1_' + i + '_control').val(text006.substring(startChar, endChar));
                        startChar = startChar + 2;
                        endChar = endChar + 1;
                    }
                    else if (startChar == 12) {
                        endChar = endChar + 6; // 19
                        jq('#006Field_1_' + i + '_control').val(text006.substring(startChar, endChar));
                        startChar = startChar + 7; //19
                    } else {
                        jq('#006Field_1_' + i + '_control').val(text006.substring(startChar, endChar));
                        startChar++;
                        endChar++;
                    }


                }
            }
            else if (controlField006Format == "e" || controlField006Format == "f") {
                for (var i = 0; i < 16; i++) {
                    if (startChar == 5) {
                        endChar = endChar + 1;   //7
                        jq('#006Field_2_' + i + '_control').val(text006.substring(startChar, endChar));
                        startChar = startChar + 2;       //7
                        endChar = endChar + 1;              // 8

                    }
                    else if (startChar == 8) {           // endChar 9
                        jq('#006Field_2_' + i + '_control').val(text006.substring(startChar, endChar));
                        endChar = endChar + 2;          // 11
                        startChar = startChar + 1;       // 9
                    }
                    else if (startChar == 9) {           // endChar 11
                        jq('#006Field_2_' + i + '_control').val(text006.substring(startChar, endChar));
                        endChar = endChar + 1;          // 12
                        startChar = startChar + 2;       // 11
                    }

                    else {
                        jq('#006Field_2_' + i + '_control').val(text006.substring(startChar, endChar));

                        startChar++;
                        endChar++;
                    }


                }
            }
            else if (controlField006Format == "c" || controlField006Format == "d" || controlField006Format == "i"
                || controlField006Format == "j") {
                for (var i = 0; i < 17; i++) {
                    if (startChar == 1) {
                        endChar = endChar + 1;
                        jq('#006Field_3_' + i + '_control').val(text006.substring(startChar, endChar));
                        startChar = startChar + 2;
                        endChar = endChar + 1;
                    } else if (startChar == 16) {
                        jq('#006Field_3_' + i + '_control').val(text006.substring(startChar, endChar));
                        endChar = endChar + 1;
                        startChar = startChar + 1;
                    } else {
                        jq('#006Field_3_' + i + '_control').val(text006.substring(startChar, endChar));
                        startChar++;
                        endChar++;
                    }

                }
            }
            else if (controlField006Format == "s") {
                for (var i = 0; i < 16; i++) {
                    if (startChar == 13) {
                        endChar = endChar + 2;
                        jq('#006Field_4_' + i + '_control').val(text006.substring(startChar, endChar));
                        startChar = startChar + 3;
                        endChar++
                    } else {
                        jq('#006Field_4_' + i + '_control').val(text006.substring(startChar, endChar));
                        startChar++;
                        endChar++;
                    }
                }
            }
            else if (controlField006Format == "p") {
                for (var i = 0; i < 10; i++) {
                    if (startChar == 1) {
                        endChar = endChar + 4;    //6
                        jq('#006Field_5_' + i + '_control').val(text006.substring(startChar, endChar));
                        endChar = endChar + 1;        //7
                        startChar = startChar + 5;       //6
                    } else if (startChar == 6) {
                        jq('#006Field_5_' + i + '_control').val(text006.substring(startChar, endChar));
                        endChar = endChar + 11;
                        startChar = startChar + 1;
                    }
                }
            }
            else if (controlField006Format == "g" || controlField006Format == "k" || controlField006Format == "o"
                || controlField006Format == "r") {
                for (var i = 0; i < 10; i++) {
                    if (startChar == 1) {
                        endChar = endChar + 2;
                        jq('#006Field_6_' + i + '_control').val(text006.substring(startChar, endChar));
                        startChar = startChar + 3;
                        endChar = endChar + 1;
                    } else if (startChar == 6) {
                        endChar = endChar + 4;
                        jq('#006Field_6_' + i + '_control').val(text006.substring(startChar, endChar));
                        startChar = startChar + 5;
                        endChar = endChar + 1;
                    } else if (startChar == 13) {
                        endChar = endChar + 2;
                        jq('#006Field_6_' + i + '_control').val(text006.substring(startChar, endChar));
                        startChar = startChar + 3;
                        endChar = endChar + 1;
                    } else {
                        jq('#006Field_6_' + i + '_control').val(text006.substring(startChar, endChar));
                        startChar++;
                        endChar++;
                    }


                }
            }
        }
    }
}
//clear 006 text value depding on id
function clearIndex006(buttonId) {
    var txtId;
    buttonId = jq(buttonId).attr("id");
    var n = buttonId.split("_");
    for (var i = 0; i < n.length; i++)
        if (n[i].contains('line')) {
            txtId = n[i].split('line');
        }
    jq('#Control_Field_006_line' + txtId[1] + '_control').val('');
}

//getting  006 text   id
function getIndex006(buttonId) {
    var txtId;
    buttonId = jq(buttonId).attr("id");
    var n = buttonId.split("_");
    for (var i = 0; i < n.length; i++)
        if (n[i].contains('line')) {
            txtId = n[i].split('line');
        }
    controlField006Id = txtId[1];
    jq('#006Field_0_control').focus().click();

    for (var i = 0; i < 9; i++) {
        jq('#Control_Field_006_line' + i + '_control').css({"background-color": "white", "border-color": "#C0C0C0"});
    }
    controlField006Edit('Control_Field_006_line' + controlField006Id + '_control');

}
function getIndex006Add(id) {
    var JSONObject = JSON.stringify(id.extraData);//.split("actionParameters[selectedLineIndex]");
    var obj = jQuery.parseJSON(JSONObject);

    for (key in obj) {
        if (key == "actionParameters[selectedLineIndex]")
            controlField006Id = parseInt(obj[key]) + 1;
    }
    jq('#Control_Field_006_line' + controlField006Id + '_control').css({"background-color": "#EFFFEF", "border-color": "#000000"});
    jq("#Control_Field_001_h1").val('Control_Field_006_line' + controlField006Id + '_control');
    jq('#006Field_0_control').focus().click();
}
//    for 006 edit set selected values
function controlField006Edit(id006) {
    for (var i = 0; i < 20; i++) {
        jq('#Control_Field_006_line' + i + '_control').css({"background-color": "white", "border-color": "#C0C0C0"});
    }
    jq('#' + id006).css({"background-color": "#EFFFEF", "border-color": "#000000"});
    var startChar = 1;
    var endChar = 2;
    var text006 = jq("#" + id006).val();
    if (text006 != "") {
        var controlField006Format = text006.substring(0, 1);
        jq("#006Field_0_control").val(text006.substring(0, 1));
        jq("#Control_Field_001_h1").val(id006);
        jq("#Control_Field_001_h0").val(text006.substring(0, 1));
        changed();
    }
}

function clearSection006() {
    var id006 = jq("#Control_Field_001_h1").val();
    jq("#" + id006).val(" ");
}

