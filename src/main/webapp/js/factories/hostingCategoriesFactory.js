angular.module('waterbnb')
.factory("hostingCategoriesFactory", ['$http',function($http){
	
	var url = 'https://localhost:8443/AirbnbDAO/rest/hostCategories/';
	

    var hostCatInterface = {
    	getHostCategories: function(id){
			return $http.get(url+id)
				.then(function(response){
					return response.data;
				});    		
    	
    	},
    	
    	postHostCat:  function(id,category){
			var urlid= url+id;
			return $http.post(urlid,category)
				.then(function(response){
					return response.status;
				});    	
    	}, 
        deleteHostCat : function(id){
			var urlid= url+id;
			return $http.delete(urlid)
				.then(function(response){
					return response.status;
				});    	
        }				  
    }
    return hostCatInterface;
}])