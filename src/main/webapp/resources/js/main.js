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
	.controller('IdbsDemoController', ['$scope', '$http', 'UserInfoService', function($scope, $http, UserInfoService) {
		
		//var tokenResponse = {};
		$scope.boundUsers = null;
		$scope.currentUser = null;
		$scope.viewBoundUsers = false;
		$scope.showBoundUserContent = false;
		//$scope.userInfo = null;
		$scope.resources = null;
		$scope.panel = 'identities';
		$scope.addphoto = false;
		
		var fetchUserInfo = function() {
			UserInfoService.getUserInfo().then( function(user) {
				$scope.currentUser = ((typeof user === 'object' && user !== null) ? user : null);
				$scope.getUserResources();
				$scope.getBoundUsers();
			});
		}
		fetchUserInfo();
		
		var handleBoundUsers = function() {
			if(!$scope.boundUsers.length) return;
			$scope.viewBoundUsers = true;
		}
		
		$scope.boundUsersResponse = function(response) {
			$scope.viewBoundUsers = false;
			if(response) {
				var temp = $scope.resources;
				for(var i=0; i<$scope.boundUsers.length; i++) {
					temp = temp.concat($scope.boundUsers[i].resources);
				}
				$scope.resources = temp;
				$scope.showBoundUserContent = true;
			}
		}
		
		$scope.getUserResources = function() {
			$http({method: 'GET',
				   url: 'http://localhost:8080/idbs-demo-webapp/getResources'})
			.success( function(res) {
				console.log(res);
				$scope.resources = res;
			});
		}
		
		$scope.changePanel = function(newpanel) {
			$scope.panel = newpanel;
		}
		
		$scope.addPhoto = function(bool) {
			$scope.addphoto = bool;
		}
		
		/*$scope.getToken = function() {
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
		}*/
		
		var getToken = function() {
			console.log('Getting token');
			var promise = $http({method: 'GET',
								 url: 'http://localhost:8080/idbs-demo-webapp/getToken'})
							.then( function(res) {
								return res.data;
							});
			return promise;
		}
		
		/*var getResponse = function() {
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
		}*/
		
		$scope.getBoundUsers = function() {
			//var promise;
			getToken().then( function(token) {
				console.log('Got token - ' + token);
				console.log('Fetching identities');
				$http({method: 'GET',
								 url: 'http://localhost:8080/idbs-demo-webapp/getBoundUsers'})
							.then( function(res) {
								$scope.boundUsers = res.data;
								handleBoundUsers();
							});
			});
			//return promise;
		}
		
		/*getBoundUsers().then( function() {
			console.log('Bound user(s) found: ' + $scope.boundUsers);
		});*/
	}]);