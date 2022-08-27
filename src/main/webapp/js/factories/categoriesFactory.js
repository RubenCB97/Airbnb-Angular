angular.module('waterbnb')
.factory("categoriesFactory", ['$http',function($http){
	
	var url = 'https://localhost:8443/AirbnbDAO/rest/categories/';
	

    var categoriesInterface = {
	
    	getCategories: function(){
			return $http.get(url)
				.then(function(response){
					return response.data;
				});    		
    	
    	},
    	postCategories:  function(category){
			return $http.post(url+id,category)
				.then(function(response){
					return response.status;
				});    	
    	}, 
        deleteCategories : function(id){
			var urlid= url+id;
			return $http.delete(urlid)
				.then(function(response){
					return response.status;
				});    	
        }				  
    }
    return categoriesInterface;
}])