function setControlIndex008() {
    var type = jq("#Type_control").val();
    var level = jq("#Level_control").val();
    if (type == "a" || type == "t") {
        if (level == "a" || level == "c" || level == "d" || level == "m") {
            controlIndex008 = 7;
        }
    }
    if (type == "m") {
        controlIndex008 = 1;
    }
    if (type == "e" || type == "f") {
        controlIndex008 = 2;
    }
    if (type == "c" || type == "d" || type == "i" || type == "j") {
        controlIndex008 = 3;
    }
    if (type == "a") {
        if (level == "b" || level == "i" || level == "s") {
            controlIndex008 = 4;
        }
    }
    if (type == "g" || type == "k" || type == "o" || type == "r") {
        controlIndex008 = 6;
    }
    if (type == "p") {
        controlIndex008 = 5;
    }
}
// Control 008 field set
function controlField008Set() {
    valueChange008 = false;
    var controlField008Text1 = "";
    setControlIndex008();
    var controlIndex = controlIndex008;

    if (controlIndex != null) {
        for (var i = 0; i < 20; i++) {
            controlText = jq('#008Field_' + controlIndex + '_' + i + '_control').val();

            if (i == 0) {
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
                    jq('#008Field_' + controlIndex + '_' + i + '_control').val(controlText);

                }
            }
            if (controlText != null) {
                controlField008Text1 = controlField008Text1 + controlText;
            }
            else {
                break;
            }


        }
    }
    var controlField008Text = "";
    var controlText = null;
    for (var i = 0; i < 4; i++) {
        if (i == 0) {
            if (dateEnterOnfile == "######" || dateEnterOnfile == "") {
                controlText = jq('#008Field_0_' + i + '_control').val();
            } else {
                controlField008Text = dateEnterOnfile;
            }

        } else {
            controlText = jq('#008Field_0_' + i + '_control').val();
        }

        if (i == 2 || i == 3) {
            controlText = controlText.trim();
            if (controlText.length == 3) {
                controlText = controlText + '#';
            }
            else if (controlText.length == 2) {
                controlText = controlText + '##';
            }
            else if (controlText.length == 1) {
                controlText = controlText + '###';
            }
            else if (controlText == "" || controlText == "0000") {
                controlText = "####";
            }
            jq('#008Field_0_' + i + '_control').val(controlText);
        }

        if (controlText != null) {
            controlField008Text = controlField008Text + controlText;
        }


    }
    controlText = jq('#008Field_0_' + 4 + '_control').val();
    if (controlText != "") {
        var places = controlText.split("-");
        controlText = places[0];
    }

    controlText = controlText.trim();
    if (controlText.length == 2) {
        controlText = controlText + "#";
    }
    else if (controlText.length == 1) {
        controlText = controlText + "##";
    }
    else if (controlText == "") {
        controlText = "xxu";
    }

    jq('#008Field_0_' + 4 + '_control').val(controlText);
    controlField008Text = controlField008Text + controlText;
    controlField008Text = controlField008Text + controlField008Text1;

    controlText = jq('#008Field_0_' + 5 + '_control').val();
    if (controlText != "") {
        var places = controlText.split("-");
        controlText = places[0];
    }
    if (controlText.length < 2) {
        controlText = "eng";
        jq('#008Field_0_' + 5 + '_control').val(controlText);
    }
    controlText = controlText.trim();
    controlField008Text = controlField008Text + controlText;

    for (var i = 6; i < 10; i++) {
        controlText = jq('#008Field_0_' + i + '_control').val();
        if (controlText != null) {
            controlField008Text = controlField008Text + controlText;
        }
        else {
            break;
        }
    }
    jq("#Control_Field_008_control").val(controlField008Text);
}
function setControl008Position() {
    var type = jq("#Type_control").val();
    var level = jq("#Level_control").val();
    if (type == "a" || type == "t") {
        if (level == "a" || level == "c" || level == "d" || level == "m") {
            jq('#Control_Field_001_h2').val("books");
        }
    }
    if (type == "m") {
        jq('#Control_Field_001_h2').val("computer");
    }
    if (type == "e" || type == "f") {
        jq('#Control_Field_001_h2').val("map");
    }
    if (type == "c" || type == "d" || type == "i" || type == "j") {
        jq('#Control_Field_001_h2').val("music");
    }
    if (type == "a") {
        if (level == "b" || level == "i" || level == "s") {
            jq('#Control_Field_001_h2').val("countRes");
        }
    }
    if (type == "g" || type == "k" || type == "o" || type == "r") {
        jq('#Control_Field_001_h2').val("visMat");
    }
    if (type == "p") {
        jq('#Control_Field_001_h2').val("mixMat");
    }
}


function defaultDropDownSetting008() {
    jq('#008Field_0_0_control').val("######");
    jq('#008Field_0_1_control').val("s");
    jq('#008Field_0_2_control').val("####");
    jq('#008Field_0_3_control').val("####");
    jq('#008Field_0_4_control').val("xxu");
    jq('#008Field_0_5_control').val("eng");
    jq('#008Field_0_6_control').val("#");
    jq('#008Field_0_7_control').val("d");
    jq('#Control_Field_008_control').val("");

}
function controlField008Edit() {
    var type = jq("#Type_control").val();
    var level = jq("#Level_control").val();
    var text008 = jq("#Control_Field_008_control").val();
    var startChar = 18;
    var endChar = 19;
    if (text008 != "" && positionStr == 'false') {
        if (type == "a" || type == "t") {
            if (level == "a" || level == "c" || level == "d" || level == "m") {
                for (var i = 0; i < 17; i++) {

                    jq('#008Field_7_' + i + '_control').val(text008.substring(startChar, endChar));
                    startChar++;
                    endChar++;
                }
            }
        }
        if (type == "m") {
            for (var i = 0; i < 9; i++) {
                if (startChar == 18) {
                    endChar = endChar + 3;
                    jq('#008Field_1_' + i + '_control').val(text008.substring(startChar, endChar));
                    startChar = startChar + 4;
                    endChar = endChar + 1;
                }
                else if (startChar == 24) {
                    endChar = endChar + 1;
                    jq('#008Field_1_' + i + '_control').val(text008.substring(startChar, endChar));
                    startChar = startChar + 2;
                    endChar = endChar + 1;
                }
                else if (startChar == 29) {
                    endChar = endChar + 5;
                    jq('#008Field_1_' + i + '_control').val(text008.substring(startChar, endChar));
                    startChar = startChar + 6;
                } else {
                    jq('#008Field_1_' + i + '_control').val(text008.substring(startChar, endChar));
                    startChar++;
                    endChar++;
                }


            }
        }
        if (type == "e" || type == "f") {

            for (var i = 0; i < 16; i++) {
                if (startChar == 22) {
                    endChar = endChar + 1;   //7
                    jq('#008Field_2_' + i + '_control').val(text008.substring(startChar, endChar));
                    startChar = startChar + 2;       //7
                    endChar = endChar + 1;              // 8

                }
                else if (startChar == 25) {           // endChar 9
                    jq('#008Field_2_' + i + '_control').val(text008.substring(startChar, endChar));
                    endChar = endChar + 2;          // 11
                    startChar = startChar + 1;       // 9
                }
                else if (startChar == 26) {           // endChar 11
                    jq('#008Field_2_' + i + '_control').val(text008.substring(startChar, endChar));
                    endChar = endChar + 1;          // 12
                    startChar = startChar + 2;       // 11
                }

                else {
                    jq('#008Field_2_' + i + '_control').val(text008.substring(startChar, endChar));

                    startChar++;
                    endChar++;
                }


            }

        }

        if (type == "c" || type == "d" || type == "i" || type == "j") {
            for (var i = 0; i < 17; i++) {
                if (startChar == 18) {
                    endChar = endChar + 1;
                    jq('#008Field_3_' + i + '_control').val(text008.substring(startChar, endChar));
                    startChar = startChar + 2;
                    endChar = endChar + 1;
                } else if (startChar == 33) {
                    jq('#008Field_3_' + i + '_control').val(text008.substring(startChar, endChar));
                    endChar = endChar + 1;
                    startChar = startChar + 1;
                } else {
                    jq('#008Field_3_' + i + '_control').val(text008.substring(startChar, endChar));
                    startChar++;
                    endChar++;
                }

            }
        }
        if (type == "a") {
            if (level == "b" || level == "i" || level == "s") {
                for (var i = 0; i < 16; i++) {
                    if (startChar == 30) {
                        endChar = endChar + 2;
                        jq('#008Field_4_' + i + '_control').val(text008.substring(startChar, endChar));
                        startChar = startChar + 3;
                        endChar++
                    } else {
                        jq('#008Field_4_' + i + '_control').val(text008.substring(startChar, endChar));
                        startChar++;
                        endChar++;
                    }
                }
            }
        }
        if (type == "p") {
            for (var i = 0; i < 10; i++) {
                if (startChar == 18) {
                    endChar = endChar + 4;    //6
                    jq('#008Field_5_' + i + '_control').val(text008.substring(startChar, endChar));
                    endChar = endChar + 1;        //7
                    startChar = startChar + 5;       //6
                } else if (startChar == 23) {
                    jq('#008Field_5_' + i + '_control').val(text008.substring(startChar, endChar));
                    endChar = endChar + 11;
                    startChar = startChar + 1;
                }
            }
        }

        if (type == "g" || type == "k" || type == "o" || type == "r") {
            for (var i = 0; i < 10; i++) {
                if (startChar == 18) {
                    endChar = endChar + 2;
                    jq('#008Field_6_' + i + '_control').val(text008.substring(startChar, endChar));
                    startChar = startChar + 3;
                    endChar = endChar + 1;
                } else if (startChar == 23) {
                    endChar = endChar + 4;
                    jq('#008Field_6_' + i + '_control').val(text008.substring(startChar, endChar));
                    startChar = startChar + 5;
                    endChar = endChar + 1;
                } else if (startChar == 30) {
                    endChar = endChar + 2;
                    jq('#008Field_6_' + i + '_control').val(text008.substring(startChar, endChar));
                    startChar = startChar + 3;
                    endChar = endChar + 1;
                } else {
                    jq('#008Field_6_' + i + '_control').val(text008.substring(startChar, endChar));
                    startChar++;
                    endChar++;
                }
            }
        }

    }
    if (change008 && text008 != "") {
        setControlIndex008();
        var controlIndex = controlIndex008;
        for (var i = 0; i < 20; i++) {
            jq('#008Field_' + controlIndex + '_' + i + '_control').prop("selectedIndex", 0);
        }
        controlField008Set();
        var textChanged008 = jq("#Control_Field_008_control").val();
        jq("#Control_Field_008_control").val(text008.substring(0, 18) + textChanged008.substring(18, 35) + text008.substring(35, 39) + text008.substring(39));
    }
    if (text008 != "" && text008 != null) {
        jq('#008Field_0_0_control').val(text008.substring(0, 6));
        jq('#008Field_0_1_control').val(text008.substring(6, 7));
        jq('#008Field_0_2_control').val(text008.substring(7, 11));
        jq('#008Field_0_3_control').val(text008.substring(11, 15));
        var place = text008.substring(15, 18);
        if (place.indexOf("#") > 0 && place != "###") {
            place.replace("#", "")
        }
        jq('#008Field_0_4_control').val(place);
        jq('#008Field_0_5_control').val(text008.substring(35, 38));
        jq('#008Field_0_6_control').val(text008.substring(38, 39));
        jq('#008Field_0_7_control').val(text008.substring(39));
    }
    data008 = true;
}

function controlField008setDepending06and07() {
    setControl008Position();
    jq('#008load').focus().click();
}

jq(window).load(function () {
    controlField008setDepending06and07();
});
