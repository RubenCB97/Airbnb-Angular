angular.module('waterbnb')
.controller('categories', ['hostsFactory','usersFactory','$routeParams','$location',
			function(hostsFactory,usersFactory,$routeParams,$location){
    var categoriesViewModel = this;
    categoriesViewModel.categories=[];
    

    categoriesViewModel.functions = {
		readHost : function() {
			hostsFactory.getMyHost()
				.then(function(response){
					console.log("Reading the hosting: ", response);
					categoriesViewModel.categorie = response;
					}, function(response){
						console.log("Error reading hosting");
					
				})
		}
	
    }
    listViewModel.functions.readAllHost();
    listViewModel.functions.readHost();

}])