angular.module('idbsDemo', [])
	.config(function($httpProvider) {
		$httpProvider.defaults.transformRequest = [function(obj) {
			//return obj === undefined ? obj : $.param(obj);
			var query = '';
			var name, value;
			for(name in obj) {
				value = obj[name];
				
				if(value !== undefined && value !== null)
					query += encodeURIComponent(name) + '=' + encodeURIComponent(value) + '&';
			}
			return query.length ? query.substr(0, query.length-1) : query;
		}];
	})
	.controller('IdbsDemoController', ['$scope', '$http', function($scope, $http) {
		
		var tokenResponse = {};
		$scope.queryResponse = [];
		
		$scope.getToken = function() {
			$http({method: 'GET',
				   url: 'http://localhost:8080/idbs-demo-webapp/getToken'})
			.success( function(res) {
				tokenResponse = res;
				console.log(res);
				getResponse();
			})
			.error( function(err) {
				console.log(err);
			})
		}
		
		var getResponse = function() {
			$http({method: 'GET',
				   url: 'http://localhost:8080/idbs-demo-webapp/getIdentities'})
			.success( function(res) {
				console.log(res);
				$scope.queryResponse = res;
			})
			.error( function(err) {
				console.log(err);
			})
		}
	}]);