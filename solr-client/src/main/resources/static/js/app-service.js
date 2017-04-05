'use strict';

angular.module('oleSolrClient.app-service', [])
    .factory('oleSolrClientAPIService', ["$rootScope", "$http", "$window", function ($rootScope, $http, $window) {
        var oleSolrClientAPI = {};

        oleSolrClientAPI.getReIndexReport = function () {
            return $http.get('fullIndexReport');
        };


        oleSolrClientAPI.getRestCall = function (serviceName, config) {
            return $http.get(serviceName, config);
        };


        oleSolrClientAPI.postRestCall = function (serviceName, config, param) {
            return $http.post(serviceName, param, config);
        };


        oleSolrClientAPI.putRestCall = function (serviceName, param) {
            return $http({
                method : "PUT",
                url : serviceName,
                data : param,
                headers : {
                    'Content-Type' : 'application/json'
                }
            });
        };


        oleSolrClientAPI.deleteRestCall = function (serviceName) {
            console.log("delete rest call service called");
            return $http({
                method : "DELETE",
                url : serviceName
            });
        };


        oleSolrClientAPI.logout = function (serviceName, config, param) {
            console.log("post rest call service called");
            return $http.post(serviceName, param, config);
        };

        oleSolrClientAPI.isNotEmpty = function(object) {
            if(null !== object && undefined !== object && NaN !== object) {
                return true;
            }
            return false;
        };


        oleSolrClientAPI.stringSort = function (items, field, reverse) {
            var filtered = [];
            var index = 0;
            var firstRow = items[index];
            items.splice(index, 1);
            angular.forEach(items, function(item) {
                filtered.push(item);
            });
            filtered.sort(function (a, b) {
                var aValue = oleSolrClientAPI.isNotEmpty(a[field]) ? a[field].toLowerCase() : a[field];
                var bValue = oleSolrClientAPI.isNotEmpty(b[field]) ? b[field].toLowerCase() : b[field];
                return (aValue > bValue ? 1 : -1);
            });
            if(reverse) filtered.reverse();

            filtered.unshift(firstRow);

            return filtered;
        };
        
        oleSolrClientAPI.validate = function(form, index, requiredFields) {
            var valid  = true;
            for(var pos = 0; pos < requiredFields.length; pos++) {
                var requiredField = requiredFields[pos];
                if(form[requiredField + index].$invalid) {
                    oleSolrClientAPI.setDirtyToRequiredInputFields(form, requiredField + index, true);
                    valid = false;
                }
            }
            console.log(valid);
            return valid;
        };

        oleSolrClientAPI.resetDirtyToAllRequiredInputFields = function (form, index, requiredFields) {
            for(var pos = 0; pos < requiredFields.length; pos++) {
                var requiredField = requiredFields[pos];
                oleSolrClientAPI.setDirtyToRequiredInputFields(form, requiredField + index, false);
            }
        };

        oleSolrClientAPI.setDirtyToRequiredInputFields = function (form, fieldName, value) {
            form[fieldName].$dirty = value;
            form[fieldName].$invalid = value;
        };

        return oleSolrClientAPI;
    }]);