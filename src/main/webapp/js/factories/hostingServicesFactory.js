angular.module('waterbnb')
.factory("hostingServicesFactory", ['$http',function($http){
	
	var url = 'https://localhost:8443/AirbnbDAO/rest/hostServices/';
	

    var hostServInterface = {
    	getHostServices: function(id){
			return $http.get(url+id)
				.then(function(response){
					return response.data;
				});    		
    	
    	},
    	
    	postHostServices:  function(id,service){
			return $http.post(url+id,service)
				.then(function(response){
					return response.status;
				});    	
    	}, 
        deleteHostServices : function(id){
			var urlid= url+id;
			return $http.delete(urlid)
				.then(function(response){
					return response.status;
				});    	
        }				  
    }
    return hostServInterface;
}])