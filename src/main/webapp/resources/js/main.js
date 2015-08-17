 var idbsDemo = angular.module('idbsDemo', [])
	.config(function($httpProvider) {
		$httpProvider.defaults.transformRequest = [function(obj) {
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
		
		$scope.boundUsers = [];
		$scope.currentUser = null;
		$scope.viewBoundUsers = false;
		$scope.resources = [];
		$scope.panel = 'identities';
		$scope.addphoto = false;
		
		var fetchUserInfo = function() {
			UserInfoService.getUserInfo().then( function(user) {
				$scope.currentUser = ((typeof user === 'object' && user !== null) ? user : null);
				if( $scope.currentUser ) {
					$scope.getUserResources();
					$scope.getBoundUsers();
				}
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
					if($scope.boundUsers[i].showContent) continue;
					temp = temp.concat($scope.boundUsers[i].resources);
					$scope.boundUsers[i].showContent = true;
				}
				$scope.resources = temp;
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
		
		$scope.updateResources = function(user) {
			if(user.showContent) {
				var temp = $scope.resources;
				temp = temp.concat(user.resources);
				$scope.resources = temp;
			}
			else {
				var newResources = [];
				for(var i=0; i<$scope.resources.length; i++) {
					if( $scope.resources[i].author !== user.userInfo.name ) {
						newResources.push( $scope.resources[i] );
					}
				}
				$scope.resources = newResources;
			}
		}
		
		$scope.changePanel = function(newpanel) {
			$scope.panel = newpanel;
		}
		
		$scope.addPhoto = function(bool) {
			$scope.addphoto = bool;
		}
		
		var getToken = function() {
			console.log('Getting token');
			var promise = $http({method: 'GET',
								 url: 'http://localhost:8080/idbs-demo-webapp/getToken'})
							.then( function(res) {
								return res.data;
							});
			return promise;
		}
		
		$scope.getBoundUsers = function() {
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
		}
	}]);