angular.module('waterbnb')
.controller('listCtrl', ['hostsFactory',function(hostsFactory){
    var listViewModel = this;
    
    listViewModel.hosting=[];
	listViewModel.categories = [];
	listViewModel.categoriesEspecific = {};
	listViewModel.services = [];
    listViewModel.allHosting=[];
    listViewModel.allHostingaux=[];
    listViewModel.priceMin=0;
    listViewModel.priceMax=0;
	listViewModel.filterLikes = 0; //Filtro de likes
	listViewModel.location = "";

    listViewModel.functions = {
		readHost : function() {
			hostsFactory.getMyHost()
				.then(function(response){
					listViewModel.hosting = response;
				
					}, function(response){
						console.log("Error reading hosting");
					
				})
			},
			

		readMyHostCat : function() {
			hostsFactory.getMyAllHostCat()
				.then(function(response){
					console.log("Reading the hostingCat: ", response);
					listViewModel.categories = response;
					}, function(response){
						console.log("Error reading myhostingCat");
					
				})
		},
		readMyHostServ: function () {
			hostsFactory.getMyAllHostServ()
				.then(function (response) {
					console.log("Reading the hostingServ: ", response);
					listViewModel.services = response;
				}, function (response) {
					console.log("Error reading myhostingServ");
				})
		},

		
		readAllHost : function() {
			hostsFactory.getAllHost()
				.then(function(response){
					console.log("Reading all the hosting: ", response);
					listViewModel.allHosting = response;
					listViewModel.allHostingaux = listViewModel.allHosting;
					}, function(response){
						console.log("Error reading hosting");
					
				})
		},
		filterByLikes : function() {
				console.log("filteeeeer" , listViewModel.filterLikes);
			if(listViewModel.filterLikes && listViewModel.filterLikes>=0)
			{
				listViewModel.allHosting = listViewModel.allHostingaux.filter(hosting => //filtering by likes
				hosting.likes>=listViewModel.filterLikes)
			}else{
				listViewModel.allHosting = [...listViewModel.allHostingaux] //Clona
					
				}
		},
		
		filterByAvailable : function() {
			listViewModel.allHosting = listViewModel.allHostingaux.filter(hosting => //filtering by available
				!hosting.available)
		},

		filterByNotAviable : function() {
			listViewModel.allHosting = listViewModel.allHostingaux.filter(hosting => //filtering by not available
				hosting.available)
		},
		filterByPrice: function () {
			listViewModel.allHosting = listViewModel.allHostingaux.filter(hosting => //filtering by price
				hosting.price >= listViewModel.priceMin && hosting.price <= listViewModel.priceMax)
		},
		filterByLocation: function () {
			if (listViewModel.location === "selecciona") {
				listViewModel.functions.filterByAllHosting();
			}else{
			listViewModel.allHosting = listViewModel.allHostingaux.filter(hosting => //filtering by location
				hosting.location == listViewModel.location)
			}
		},
		
		filterByAllHosting : function() {
			listViewModel.allHosting = listViewModel.allHostingaux;
			listViewModel.priceMin=0;
			listViewModel.priceMax = 0;
			listViewModel.location = "";
			listViewModel.filterLikes=0;
		},

		shortByMaxLikes : function() {
			listViewModel.allHosting.sort(function(a,b){
				return b.likes-a.likes;
			}
		)},
		shortByMinLikes : function() {
			listViewModel.allHosting.sort(function(a,b){
				return a.likes-b.likes;
			}
		)},


    }
    listViewModel.functions.readAllHost();
    listViewModel.functions.readHost();
	listViewModel.functions.readMyHostCat();
	listViewModel.functions.readMyHostServ();
    listViewModel.functions.filterByLikes();


}]);