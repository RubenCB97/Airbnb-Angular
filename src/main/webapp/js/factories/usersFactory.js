angular.module('waterbnb')
.factory('usersFactory',['$http', function($http){

   
    var url = 'https://localhost:8443/AirbnbDAO/rest/users/';
    var usersInterface = {
	
        getUser : function(){
            return $http.get(url)
            .then(function(response){
				console.log("Obteniendo Usuario");
                return response.data;
            });
        },
        putUser : function(user){
            return $http.put(url, user)
            .then(function(response){
                return response.status;
            });
        },
        deleteUser : function(id){
            var urlId = url + id;
            return $http.delete(urlId)
            .then(function(response){
                return response.status;
            });
        }
    }
    return usersInterface;
}])