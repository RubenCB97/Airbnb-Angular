angular.module('waterbnb')
.factory("hostsFactory", ['$http',function($http){
	
	var url = 'https://localhost:8443/AirbnbDAO/rest/hosts/';
	

    var hostInterface = {
    	getMyHost: function(){
			return $http.get(url)
				.then(function(response){
					return response.data;
				});    		
    	
    	},
    	
    	getMyAllHostCat: function(){
		var urlAllM = url + "myHosts/categories/";
			return $http.get(urlAllM)
				.then(function(response){
					return response.data;
				});    		
    	
		},
		getMyAllHostServ: function () {
			var urlAllM = url + "myHosts/services/";
			return $http.get(urlAllM)
				.then(function(response){
					return response.data;
				});    		
		},
    	getAllHost: function(){
		var urlAll = url + "all/";
			return $http.get(urlAll)
				.then(function(response){
					return response.data;
				});    		
    	
    	},
    	getHost : function(id){
			var urlid = url+id
			return $http.get(urlid)
				.then(function(response){
					return response.data;
				});    	 

    	},
    	
    	getEditHost : function(id){
			var urlid = url+"edit/"+id
			return $http.get(urlid)
				.then(function(response){
					return response.data;
				});    	 

    	},
    	putHost : function(host,id){
			var urlid= url+id;
			return $http.put(urlid,host)
				.then(function(response){
					return response.status;
				});    	
    	},
    	postHost:  function(host){
			return $http.post(url,host)
				.then(function(response){
					return response.data;
				});    	
    	}, 
        deleteHost : function(id){
			var urlid= url+id;
			return $http.delete(urlid)
				.then(function(response){
					return response.status;
				});    	
        }				  
    }
    return hostInterface;
}])