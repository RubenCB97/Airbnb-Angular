angular.module('waterbnb')
.controller('hostingHandlerCtrl', ['hostsFactory','usersFactory','categoriesFactory','servicesFactory','userLikesFactory','hostingCategoriesFactory','hostingServicesFactory','$routeParams','$location',
	function (hostsFactory, usersFactory,categoriesFactory,servicesFactory,userLikesFactory, hostingCategoriesFactory, hostingServicesFactory, $routeParams, $location) {
		var hostsHandlerViewModel = this;
		
		hostsHandlerViewModel.host = {};
		hostsHandlerViewModel.myHost = {};

		
		
		hostsHandlerViewModel.categories = [];
		hostsHandlerViewModel.allCategories = [];
		hostsHandlerViewModel.resultCategories = [];
		

		hostsHandlerViewModel.services = [];
		hostsHandlerViewModel.allServices = [];
		hostsHandlerViewModel.resultServices = [];
		hostsHandlerViewModel.user = {};
		hostsHandlerViewModel.like = 0;
		
		hostsHandlerViewModel.functions = {
			
			where: function (route) {

				return $location.path() == route;
   			
   		
			},
		    readUser : function() {
	            usersFactory.getUser()
	            .then(function(response){
	                hostsHandlerViewModel.user = response
	                console.log("User ID: ",hostsHandlerViewModel.user.id);
	            }, function(response){
					$location.path("/");
	                console.log("ERROR");
	            })
	        },
			readEditHosts: function (id) {
				hostsFactory.getEditHost(id)
					.then(function (response) {
						console.log("Reading hosts with id: ", id, "Response: ", response);
						hostsHandlerViewModel.host = response;
						
					}, function (response) {
						console.log("Error reading edithosts");
						$location.path("/");

					})
			
			},
			readHosts: function (id) {
				hostsFactory.getHost(id)
					.then(function (response) {
						console.log("Reading hosts with id: ", id, "Response: ", response);
						hostsHandlerViewModel.myHost = response;
					}, function (response) {
						console.log("Error reading hosts");
					})
			
			},
			readCategories: function (id) {
				hostingCategoriesFactory.getHostCategories(id)
					.then(function (response) {
						
						hostsHandlerViewModel.categories = response;		
						hostsHandlerViewModel.resultCategories = [...hostsHandlerViewModel.categories];

						
						
						console.log("Reading categories with id: ", id, "Response: ", response);
					}, function (response) {
						console.log("Error reading categories");
					})
			},
			readAllCategories: function () {
				categoriesFactory.getCategories()
					.then(function (response) {
						hostsHandlerViewModel.allCategories = response;
						console.log("Reading all categories: ", response);
					}, function (response) {
						console.log("Error reading all categories");
					})
			},
			
			readServices: function (id) {
				hostingServicesFactory.getHostServices(id)
					.then(function (response) {
						
						hostsHandlerViewModel.services = response;
						hostsHandlerViewModel.resultServices = [...hostsHandlerViewModel.services];
						console.log("Reading services with id: ", id, "Response: ", response);
					}, function (response) {
						console.log("Error reading services");
					})
			},
			readAllServices: function () {
				servicesFactory.getServices()
					.then(function (response) {
						hostsHandlerViewModel.allServices = response;
						console.log("Reading all services: ", response);
					}, function (response) {
						console.log("Error reading all services");
					})
			},
			//UPDATE HOST
			updateHosts: function () {
				hostsFactory.putHost(hostsHandlerViewModel.host,hostsHandlerViewModel.host.id)
					.then(function (response) {
						console.log("Updating hosts with id: ", hostsHandlerViewModel.name, "Response: ", response);
					}, function (response) {
						console.log("Error updating hosts");
					}),
					hostingCategoriesFactory.postHostCat(hostsHandlerViewModel.host.id,hostsHandlerViewModel.resultCategories)
					.then(function (response) {
						console.log("Updating categories:  ", hostsHandlerViewModel.resultCategories, "Response: ", response);
					}
						, function (response) {
							console.log("Error updating categories");
						}
					)
					hostingServicesFactory.postHostServices(hostsHandlerViewModel.host.id,hostsHandlerViewModel.resultServices)
					.then(function (response) {
						console.log("Updating services: ", hostsHandlerViewModel.resultServices, "Response: ", response);
					}
						, function (response) {
							console.log("Error updating services");
						}
					)
			$location.path("/user/hosting");
			},
			//CREATE HOST
			createHosts: function () {
					

			hostsHandlerViewModel.host.idu = hostsHandlerViewModel.user.id; 
			hostsFactory.postHost(hostsHandlerViewModel.host)
					.then(function (response) {
						hostsHandlerViewModel.host.id  = response;
						hostsHandlerViewModel.functions.addCatServHosts();
						console.log("Creating hosts. Response: ", response);
					}, function (response) {
						console.log("Error creating the hosts");
					}
				)
			},
			addCatServHosts : function() {
					
				hostingCategoriesFactory.postHostCat(hostsHandlerViewModel.host.id, hostsHandlerViewModel.resultCategories)
				.then(function (response) {
					console.log("Creating categories:  ", hostsHandlerViewModel.resultCategories, "Response: ", response);
				}
					, function (response) {
						console.log("Error creating categories");
					}
				)
				hostingServicesFactory.postHostServices(hostsHandlerViewModel.host.id, hostsHandlerViewModel.resultServices)
				.then(function (response) {
					console.log("Creating services: ", hostsHandlerViewModel.resultServices, "Response: ", response);
				}
					, function (response) {
						console.log("Error creating services");
					}
				)
				hostsHandlerViewModel.resultCategories = [];
				hostsHandlerViewModel.resultServices = [];
				$location.path("/user/hosting");
			
			},
			deleteHosts: function (id) {
				hostsFactory.deleteHost(id)
					.then(function (response) {
						console.log("Deleting hosts with id: ", id, " Response: ", response);
						$location.path('/');
					}, function (response) {
						console.log("Error deleting hosts");
					})
			},

			//-------UPDATEHOST//CREATEHOST------//
			//Comprobar que en la lista categories exista el parametro de entrada
			checkCategories: function (categories) {
				return hostsHandlerViewModel.categories.some(cat => cat.name === categories);
			},
		
			//Comprobar que en la lista services exista el parametro de entrada
			checkServices: function (services) {
				return hostsHandlerViewModel.services.some(serv => serv.name === services);
			},
			

			//Comprueba con el parametro de entrada que exista el nombre en la lista allCategories, si es asi, añadir a la lista categories
			addCategories: function (categories) {
				//si la lista resultCategories añadir categories
					hostsHandlerViewModel.allCategories.forEach(function (cat2) {
						if (cat2.name === categories) {
								//comprobar que cat2 exista en categories
							if (hostsHandlerViewModel.resultCategories.some(cat => cat.name === cat2.name)) {
								//borar cat2 de resultCategories sin alterar el resto
								hostsHandlerViewModel.resultCategories = hostsHandlerViewModel.resultCategories.filter(cat => cat.name !== cat2.name);
								
							} else {		
							
								hostsHandlerViewModel.resultCategories.push(cat2);
							}
						}
					})
			},
			//Comprueba con el parametro de entrada que exista el nombre en la lista allCategories, si es asi, añadir a la lista categories
			addServices: function (services) {
				//si la lista resultCategories añadir categories
					hostsHandlerViewModel.allServices.forEach(function (serv2) {
						if (serv2.name === services) {
								//comprobar que cat2 exista en categories
							if (hostsHandlerViewModel.resultServices.some(serv => serv.name === serv2.name)) {
								hostsHandlerViewModel.resultServices = hostsHandlerViewModel.resultServices.filter(serv => serv.name !== serv2.name);
							}else{		
							
								hostsHandlerViewModel.resultServices.push(serv2);
							}
						}
					})
			},
			//Añade un like al host
			addLike: function (id) {
				userLikesFactory.addLike(id)
					.then(function (response) {
						console.log("Adding like to host with id: ", id, " Response: ", response);
						$location.path("/");

					}
						, function (response) {
							console.log("Error adding like to host");
						}
					)
			},
			//Quita un like al host
			removeLike: function (id) {
				userLikesFactory.removeLike(id)
					.then(function (response) {
						console.log("Removing like to host with id: ", id, " Response: ", response);
						$location.path("/");

					}
						, function (response) {
							console.log("Error removing like to host");
						}
					)
			},
			//Obtiene si ha dado like o no al host
			getLike: function (id) {
				userLikesFactory.getUserLikes(id)
					.then(function (response) {
						console.log("Getting like to host with id: ", id, " Response: ", response);
						hostsHandlerViewModel.like = response;

					}
						, function (response) {
							console.log("Error getting like to host");
						}
					)
			},
		}

			
		if ($routeParams.ID != undefined){
			hostsHandlerViewModel.functions.readHosts($routeParams.ID);
			hostsHandlerViewModel.functions.readCategories($routeParams.ID);
			hostsHandlerViewModel.functions.readServices($routeParams.ID);
			hostsHandlerViewModel.functions.getLike($routeParams.ID);
			if (hostsHandlerViewModel.functions.where('/user/editHosting/'+$routeParams.ID))
				hostsHandlerViewModel.functions.readEditHosts($routeParams.ID);
			if (hostsHandlerViewModel.functions.where('/hosting/'+$routeParams.ID))
				hostsHandlerViewModel.functions.readHosts($routeParams.ID);

				
			

		}
		hostsHandlerViewModel.functions.readAllCategories();
		hostsHandlerViewModel.functions.readAllServices();
		hostsHandlerViewModel.functions.readUser();
   	
}]);