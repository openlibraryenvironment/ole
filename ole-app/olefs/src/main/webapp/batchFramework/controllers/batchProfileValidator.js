/**
 * Created by rajeshbabuk on 1/14/16.
 */
validateMatchPointRow = function(matchPointRow, $scope) {
    if (matchPointRow.matchPointDocType == 'Bibliographic') {
        var error = false;
        if (matchPointRow.dataField == undefined || matchPointRow.dataField == null) {
            $scope.batchProfileForm['matchPointDataField_0'].$dirty = true;
            $scope.batchProfileForm['matchPointDataField_0'].$invalid = true;
            error = true;
        }
        if (matchPointRow.subField == undefined || matchPointRow.subField == null) {
            $scope.batchProfileForm['matchPointSubField_0'].$dirty = true;
            $scope.batchProfileForm['matchPointSubField_0'].$invalid = true;
            error = true;
        }
        return error;
    }
};