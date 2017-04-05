'use strict';

angular.module("oleSolrClient", [
	'ngRoute',
	'oleSolrClient.login',
	'oleSolrClient.fullIndex',
	'oleSolrClient.partialIndex',
	'oleSolrClient.report',
	'oleSolrClient.app-service',
	'oleSolrClient.app-filter'
	])
.config(['$routeProvider','$httpProvider', function($routeProvider,$httpProvider) {
	$routeProvider
		.when("/partialIndex", {templateUrl: "view/partialIndex.html",controller:"partialIndexCtrl"})
		.when("/fullIndex", {templateUrl: "view/fullIndex.html",controller:"fullIndexCtrl"})
		.when("/report", {templateUrl: "view/report.html",controller:"reportCtrl"});

	$httpProvider.defaults.headers.common['X-Requested-With'] = 'XMLHttpRequest';

}])
.run(function($rootScope, $http, $window) {
		$rootScope.authenticated = false;
		$rootScope.userRol = null;
		$rootScope.OLESOLRCLIENT_CONSTANTS = OLESOLRCLIENT_CONSTANTS;

		$rootScope.logout = function() {
			$rootScope.authenticated = false;
			console.log("Logout called");
			$http.post('logoutUser', {}).finally(function(response) {
				$rootScope.authenticated = false;
				$rootScope.loggedInName = null;
			});
		};
	});


var OLESOLRCLIENT_CONSTANTS = {
	SIGNIN : "signin",
	FULL_INDEX: "fullIndex",
	PARTIAL_INDEX_BIB_ID: "partialIndexByBibIdRange",
	PARTIAL_INDEX_FILE: "partialIndexByFile",
	PARTIAL_INDEX_DATE: "partialIndexByDate",
	GENERATE_REPORT: "generateReport",
	GET_REPORT_FILES: "getReportsFiles",
    DOWNLOAD_REPORT_FILE: "downloadReportFile",
	FULL_INDEX_STATUS : "fullIndexStatus",
	PARTIAL_INDEX_STATUS : "partialIndexStatus",
	ROL_ADMINISTRATOR : "ROL_ADMINISTRATOR"
}