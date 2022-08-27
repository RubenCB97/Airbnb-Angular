angular.module('waterbnb')
.controller('headerCtrl', ['usersFactory',function(usersFactory){
    var headerViewModel = this;
    headerViewModel.user={};
    headerViewModel.functions = {
        readUser : function() {
            usersFactory.getUser()
            .then(function(response){
				if(typeof response === 'string' || response instanceof String){ //Comprueba si es string
					headerViewModel.user =null;
                 }else{
                	headerViewModel.user = response
	                console.log("User ID: ",headerViewModel.user.id);
				}
            }, function(response){
                console.log("ERROR");
            })
        }
    }
    headerViewModel.functions.readUser();
}])