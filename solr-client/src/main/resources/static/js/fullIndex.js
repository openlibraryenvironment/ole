'use strict';

angular.module("oleSolrClient.fullIndex", ['ui.bootstrap','customDirectives', 'ngStorage'])
	.controller('fullIndexCtrl', ["$scope", "oleSolrClientAPIService", '$localStorage', "$location",  function($scope,
																									  oleSolrClientAPIService,
																									  $localStorage,
																									  $location) {
		console.log("Full Index");

		$scope.fullIndexStatus = {};
		$scope.fullIndexRequest = {};

		$scope.getFullIndexStatus = function () {
			oleSolrClientAPIService.getRestCall(OLESOLRCLIENT_CONSTANTS.FULL_INDEX_STATUS, {}).then(function (response) {
				var data = response.data;
				$scope.fullIndexStatus = data;
			}, function (response) {
				console.log("loading full index status failed");
				console.log(response);
			});
		};
		$scope.getFullIndexStatus();

		$scope.fullIndex = function () {
			oleSolrClientAPIService.postRestCall(OLESOLRCLIENT_CONSTANTS.FULL_INDEX, {}, $scope.fullIndexRequest).then(function (response) {
				var data = response.data;
				$scope.fullIndexStatus = data;
				console.log($scope.fullIndexStatus);
				$scope.message = "Successfully submitted";
				$scope.title = "Success Message";
				$scope.showDialog = true;
			}, function (response) {
				console.log("Full index initiation failed");
				console.log(response);
				$scope.message = "Submit failed!";
				$scope.title = "Error";
				$scope.showDialog = true;
			});
		};
	}]);