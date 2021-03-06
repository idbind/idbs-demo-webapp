idbsDemo
	.factory('UserInfoService', ['$http', function($http) {	
		var promise;
		var service = {
			getUserInfo: function() {
				if(!promise) {
					promise = $http({method: 'GET',
						   			 url: 'getUser'})
						   	  .then( function(res) {
						   		  console.log(res);
						   		  return res.data;
						   	  });
				}
				return promise;
			}
		}
		return service;
	}]);