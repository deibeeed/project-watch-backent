'use strict';

// Declare app level module which depends on views, and components
angular.module('projectFitpetApp', [
    'ngRoute',
    'ngMaterial',
    'projectFitpetApp.pets',
    'projectFitpetApp.pet_detail',
    'projectFitpetApp.add_pet',
    'projectFitpetApp.version'
]).
config(['$routeProvider', '$mdIconProvider', function($routeProvider) {
        $routeProvider.otherwise({redirectTo: '/pets'});
}]).
controller('IndexController', ['$scope', '$mdSidenav', '$location', '$window', function($scope, $mdSidenav, $location, $window){
        var self = this;

        $scope.toggleSidenav = function(menuId){
            $mdSidenav(menuId).toggle();
        };

        self.onMenuClick = function(destination){

            if(destination == '/') $location.path('/');
            else $location.path('/' + destination);
        };
}]);
