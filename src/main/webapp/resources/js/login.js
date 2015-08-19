angular.module('loginModule', [])
	.controller('LoginCtrl', ['$scope', '$http', function($scope, $http) {
		
		$scope.idp = '';
		
		$scope.login = function() {
			$http({method: 'GET',
				   url: 'openid_connect_login',
				   headers: {
					   'Content-Type': 'application/x-www-form-urlencoded'
				   },
				   data: 'identifier=http://localhost:8080/openid-connect-server-webapp/'})
			.success( function(res) {
				console.log('it worked ' + res);
			})
			.error( function(err) {
				console.log('it failed ' + err)
			});
		}
	}]);