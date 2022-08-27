angular.module('waterbnb')
.factory("servicesFactory", ['$http',function($http){
	
	var url = 'https://localhost:8443/AirbnbDAO/rest/services/';
	

    var servicesInterface = {
	
    	getServices: function(){
			return $http.get(url)
				.then(function(response){
					return response.data;
				});    		
    	
    	},
    	postServices:  function(service){
			return $http.post(url+id,service)
				.then(function(response){
					return response.status;
				});    	
    	}, 
        deleteServices : function(id){
			var urlid= url+id;
			return $http.delete(urlid)
				.then(function(response){
					return response.status;
				});    	
        }				  
    }
    return servicesInterface;
}])