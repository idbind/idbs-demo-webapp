/*angular.module('services', [])
	.service('UserInfoService', ['$http', function($http) {	
		this.getUserInfo = function() {
			var info = {};
			
			$http({method: 'GET',
				   url: 'http://localhost:8080/idbs-demo-webapp/getUserInfo'})
				.success( function(res) {
					info = res;
				})
				.error( function(err) {
					console.log(err);
				});
			
			return info;
		}
	}]);*/