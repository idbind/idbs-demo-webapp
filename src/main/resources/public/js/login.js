angular.module('loginModule', [])
	.controller('LoginCtrl', function($scope) {
		$scope.register = false;
		$scope.showRegForm = function() {
			$scope.register = true;
		}
		$scope.hideRegForm = function() {
			$scope.register = false;
		}
	});