/**
 * Created by sheiks on 11/08/16.
 */
angular.module('oleSolrClient.app-filter', [])
    .filter('paginate', function () {
        return function (arr, currentPage, pageSize) {
            try {
                return arr.slice((currentPage - 1)*pageSize, currentPage * pageSize);
            } catch(err) {
                return arr;
            }
        }
    });