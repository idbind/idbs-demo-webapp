angular.module('idbsDemo', [])
	.config(function($httpProvider) {
		$httpProvider.defaults.transformRequest = [function(obj) {
			return obj === undefined ? obj : $.param(obj);
		}];
	})
	.controller('IdbsDemoController', ['$scope', '$http', function($scope, $http) {
		
		var authToken = '';
		$scope.queryResponse = {};
		
		$scope.getToken = function() {
			console.log('getting token...');
			$http({method: 'POST',
				   url: 'http://localhost:8080/openid-connect-server-webapp/token',
				   headers: {'Content-Type': 'application/x-www-form-urlencoded'},
				   data: { 'client_id': 'client', 'client_secret': 'secret', 'grant_type': 'client_credentials', 'scope': 'org.mitre.idbind.query'}})
			.success( function(res) {
				authToken = res.access_token;
				console.log(res.access_token);
				getResponse();
			})
			.error( function(err) {
				console.log(err);
			});
		}
		
		var getResponse = function() {
			var authString = 'Bearer '+authToken;
			console.log(authString);
			$http({method: 'POST',
				   url: 'http://localhost:8080/identity-binder/query',
				   headers: {'Content-Type': 'application/x-www-form-urlencoded',
					         'Accept': 'application/json',
					         'Authorization': authString },
				   data: {'issuer':'http://localhost:8080/openid-connect-server-webapp/','subject':'90342.ASDFJWFA'}})
			.success( function(res) {
				$scope.queryResponse = res;
			});
		}
	}]);