angular.module('loginModule', [])
	.controller('LoginCtrl', ['$scope', '$http', function($scope, $http) {
		
		$scope.idp = 'http://localhost:8080/openid-connect-server-webapp/';
	}]);