 var idbsDemo = angular.module('idbsDemo', [])
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
	/*.factory('UserInfoService', ['$http', function($http) {	
		var promise;
		var service = {
			getUserInfo: function() {
				if(!promise) {
					promise = $http({method: 'GET',
						   			 url: 'http://localhost:8080/idbs-demo-webapp/getUserInfo'})
						   	  .then( function(res) {
						   		  console.log(res);
						   		  return res.data;
						   	  });
				}
				return promise;
			}
		}
		return service;
	}])*/
	.controller('IdbsDemoController', ['$scope', '$http', 'UserInfoService', function($scope, $http, UserInfoService) {
		
		var tokenResponse = {};
		$scope.identities = [];
		$scope.userInfo = {};
		
		var updateUserInfo = function() {
			UserInfoService.getUserInfo().then( function(info) {
				$scope.userInfo = info;
			});
		}
		updateUserInfo();
		
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
				$scope.identities = res;
				//$scope.userInfo = UserInfoService.getUserInfo();
			})
			.error( function(err) {
				console.log(err);
			})
		}
	}]);