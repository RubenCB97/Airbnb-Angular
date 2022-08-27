angular.module('waterbnb')
.factory('userLikesFactory',['$http', function($http){

   
    var url = 'https://localhost:8443/AirbnbDAO/rest/userLikes/';
    var usersInterface = {
        
        getUserLikes: function (id) {
            return $http.get(url + id)
                .then(function (response) {
                    return response.data;
                });
        },
        addLike: function (id) {
            return $http.post(url + id)
                .then(function (response) {
                    return response.status;
                });
        },
    
        removeLike : function(id){
            var urlId = url + id;
            return $http.delete(urlId)
            .then(function(response){
                return response.status;
            });
        }
    }
    return usersInterface;
}])