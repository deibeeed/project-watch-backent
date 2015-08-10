'use strict';

angular.module('projectFitpetApp.pets', ['ngRoute'])

.config(['$routeProvider', function($routeProvider) {
  $routeProvider.when('/pets', {
      templateUrl: 'js/app/pets/pets.html',
      controller: 'PetsCtrl',
      controllerAs: 'pc'
  });
}])

.controller('PetsCtrl', ['$mdToast', '$animate', '$location', '$http', '$scope', function($mdToast, $animate, $location, $http, $scope) {
        var self = this;

        self.selectItem = function(petName){

            $location.url('/pet-detail?pet_name=' + petName);

            //var toast = $mdToast.simple()
            //                .content('you have selected item id: ' + id)
            //                .action('OK')
            //                .highlightAction(false)
            //                .position('bottom right');
            //
            //$mdToast.show(toast).then(function(){
            //    alert('you have clicked ' + id);
            //});
        };

        self.getAvatarUrl = function(avatar){
            return replaceAll(avatar, '/', '+');
        };

        $scope.init = function(){
            $http.get('/api/fitpet/getPets')
                .success(function(data, status){
                    console.log(data);

                    self.pets = data.data;
                })
                .error(function(data, status){
                    console.log(data);
                });
        };
}]);