angular.module('waterbnb', ['ngRoute'])
.config(function($routeProvider){
	$routeProvider
    	.when("/", {
    		controller: "listCtrl",
    		controllerAs: "listVM",
    		templateUrl: "mainweb.html",
    		resolve: {
    			// produce 500 miliseconds (0,5 seconds) of delay that should be enough to allow the server
    			//does any requested update before reading the hostings.
    			// Extracted from script.js used as example on https://docs.angularjs.org/api/ngRoute/service/$route
    			delay: function($q, $timeout) {
    			var delay = $q.defer();
    			$timeout(delay.resolve, 500);
    			return delay.promise;
    			}
    		}
    	})
    	//Crear host
    	.when("/user/insertHosting", { 
    		controller: "hostingHandlerCtrl",
    		controllerAs: "hostingHandlerVM",
    		templateUrl: "createHost.html"
    		
    	})
    	//Editar host
        .when("/user/editHosting/:ID", { 
        	controller: "hostingHandlerCtrl",
    		controllerAs: "hostingHandlerVM",
    		templateUrl: "editHosts.html"
    		
        })
        //Delete host
        .when("/deleteHosting/:ID", { 
			controller: "hostingHandlerCtrl",
    		controllerAs: "hostingHandlerVM",
    		templateUrl: "hostingHandlerTemplate.html"
    	})
    	
    	.when("/hosting/:ID", { 
    		controller: "hostingHandlerCtrl",
    		controllerAs: "hostingHandlerVM",
    		templateUrl: "detail.html"
    		
    	})
  		//Usuario
  		
  		
		//Editar usuario
		.when("/user/editProfile", {
			controller: "userHandlerCtrl",
			controllerAs: "userHandlerVM",
			templateUrl: "editProfile.html"
		})
		.when("/user/hosting", {
			controller: "listCtrl",
			controllerAs: "listVM",
			templateUrl: "myHosts.html"
		});

})

