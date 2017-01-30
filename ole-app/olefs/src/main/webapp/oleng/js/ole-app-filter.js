/**
 * Created by sheiks on 15/01/17.
 */
angular.module('ole.app-filter', [])
    .filter('paginate', function () {
        return function (arr, currentPage, pageSize) {
            try {
                return arr.slice((currentPage - 1)*pageSize, currentPage * pageSize);
            } catch(err) {
                return arr;
            }
        }
    });