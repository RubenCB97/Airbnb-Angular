angular.module('waterbnb')
.controller('userHandlerCtrl', ['usersFactory','$routeParams','$location',
			function(usersFactory,$routeParams,$location){
	var usersHandlerViewModel = this;
    usersHandlerViewModel.users={};
    usersHandlerViewModel.errPass=0;
    

   	usersHandlerViewModel.functions = {
   		where : function(route){

   			return $location.path() == route;
   			
   		},
		 readUser : function() {
            usersFactory.getUser()
            .then(function(response){
                usersHandlerViewModel.user = response
                usersHandlerViewModel.user.password="";
               
                console.log("User ID: ",usersHandlerViewModel.user.id);
            }, function(response){
                console.log("ERROR");
            })
        },
		updateUsers : function() {
			usersFactory.putUser(usersHandlerViewModel.user)
				.then(function(response){
					 usersHandlerViewModel.errPass=2;
					console.log("Updating user with id: ",usersHandlerViewModel.users.id, "Response: ", response);
				},function(response){
					usersHandlerViewModel.errPass=1;
					console.log("Error updating user" + usersHandlerViewModel.errPass);

				})
			
		},	
		
		deleteUsers : function() {
			usersFactory.deleteUser(usersHandlerViewModel.users.id)
				.then(function(response){
					console.log("Deleting user with id: ",usersHandlerViewModel.users.id, " Response: ",response);
				}, function(response){
					console.log("Error deleting user");
				})
		},
	
	}
   	usersHandlerViewModel.functions.readUser();
}]);