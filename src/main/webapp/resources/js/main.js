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
		$scope.showPhotoBox = false;
		$scope.photoFormData = {};
		var nonCurrentUserResources = [];
		
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
					nonCurrentUserResources = nonCurrentUserResources.concat($scope.boundUsers[i].resources);
					$scope.boundUsers[i].showContent = true;
				}
				$scope.resources = temp.concat(nonCurrentUserResources);
			}
		}
		
		$scope.getUserResources = function() {
			$http({method: 'GET',
				   url: 'getResources'})
			.success( function(res) {
				console.log(res);
				$scope.resources = nonCurrentUserResources.concat(res);
			});
		}
		
		$scope.updateResources = function(user) {
			if(user.showContent) {
				var temp = nonCurrentUserResources;
				temp = temp.concat(user.resources);
				nonCurrentUserResources = temp;
			}
			else {
				var newResources = [];
				for(var i=0; i<nonCurrentUserResources.length; i++) {
					if( nonCurrentUserResources[i].author !== user.userInfo.name ) {
						newResources.push( nonCurrentUserResources[i] );
					}
				}
				nonCurrentUserResources = newResources;
			}
			$scope.resources = nonCurrentUserResources.concat($scope.currentUser.resources);
		}
		
		$scope.changePanel = function(newpanel) {
			$scope.panel = newpanel;
		}
		
		$scope.showAddPhotoBox = function(bool) {
			$scope.showPhotoBox = bool;
		}
		
		$scope.addPhoto = function() {
			//if($scope.photoUrl === '' || $scope.photoCaption === '') return;
			/*var photo = {url: $scope.photoUrl,
						 caption: $scope.photoCaption,}*/
			$http({method: 'POST',
				   headers: {'Content-Type': 'application/x-www-form-urlencoded'},
				   data: $scope.photoFormData,
				   url: 'addPhoto'})
			.then( function(res) {
				$scope.getUserResources();
			});
		}
		
		var getToken = function() {
			console.log('Getting token');
			var promise = $http({method: 'GET',
								 url: 'getToken'})
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
								 url: 'getBoundUsers'})
							.then( function(res) {
								$scope.boundUsers = res.data;
								handleBoundUsers();
							});
			});
		}
	}]);