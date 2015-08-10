/**
 * Created by David on 7/17/2015.
 */

angular.module('projectFitpetApp.add_pet', [
    'ngRoute',
    'ngFileUpload'
]).config(['$routeProvider', function($routeProvider){
    $routeProvider.when('/add-pet', {
        templateUrl     : '/js/app/add_pet/add_pet.html',
        controller      : 'AddPetCtrl',
        controllerAs    : 'apc'
    });

}]).controller('AddPetCtrl', ['$scope', '$mdToast', '$mdDialog', '$http', '$location', 'Upload', function ($scope, $mdToast, $mdDialog, $http, $location, Upload) {
    var self = this;

    self.showUpdateButton = false;

    self.addPet = function(){

        var expDate = new Date(self.expiry_date);
        var expDateFormat = expDate.getFullYear() + '-' + (expDate.getMonth() + 1) + '-' + expDate.getDay();

        var content = 'pet name is ' + self.pet_name  + '\n' +
                    'pet description ' + self.description + '\n' +
                    'pet expiry date ' + expDateFormat + '\n' +
                    'pet price ' + self.price;

        var toast = $mdToast.simple()
            .content(content)
            .position('bottom right');

        //$mdToast.show(toast);
        console.log(content);

        if(self.checkParams()){
            if(self.petImage != null && self.petImage.length > 0){
                var petAvatar = null;

                for(var i = 0; i < self.petImage.length; i++){
                    var petFile = self.petImage[i];

                    if(petFile.name.indexOf('_still10001')){
                        petAvatar = petFile.webkitRelativePath;
                        break;
                    }
                }

                $http.post(
                    '/api/fitpet/addPet',
                    {
                        pet_name: self.pet_name,
                        description: self.description,
                        expiry_date: self.expiry_date,
                        price: self.price,
                        enabled: true,
                        avatar: replaceAll(petAvatar, '/', '+')
                    }
                ).success(function (data, status, headers, config) {
                        console.log(status);
                        console.log(data);
                        //if(status == 200){
                        //    console.log(data);
                        //}

                        if(data.status == 200){
                            self.pet_name = '';
                            self.description = '';
                            self.expiry_date = '';
                            self.price = '';
                            self.petImage = '';
                        }

                        $mdDialog.show(
                            $mdDialog.alert()
                                .parent(angular.element(document.body))
                                .title('Add Pet')
                                .content(data.message)
                                .ariaLabel('Add Pet')
                                .ok('OK')
                        );
                    })
                    .error(function (data, status) {
                        console.log(data);
                    });
            }else{
                $mdDialog.show(
                    $mdDialog.alert()
                        .parent(angular.element(document.body))
                        .title('Add Pet')
                        .content('Add pet images first!')
                        .ariaLabel('Add Pet Dialog')
                        .ok('OK')
                );
            }
        }
    };

    /*self.addPetAction = function () {
        var div = angular.element(document.querySelector('#pet-action-container'));
        var appendedElement =
            '<md-input-container class="md-default-theme"><label for="pet-action-name">Action name</label><input name="pet-action-name" required class="ng-pristine ng-untouched md-input ng-invalid ng-invalid-required" tabindex="0" aria-required="true" aria-invalid="true" style></md-input-container><br/>' +
            '<md-button class="md-raised md-primary"><label for="pet-action-file">Upload pet Animations</label></md-button><input type="file" name="pet-action-file" multiple>';


        ////<md-input-container class="md-default-theme">
        ////    <label for="expiry-date">Pet Expiry Date</label>
        ////<input type="date" id="expiry-date" ng-model="apc.expiry_date" required="" class="ng-pristine ng-untouched md-input ng-invalid ng-invalid-required" tabindex="0" aria-required="true" aria-invalid="true">
        //    </md-input-container>
        div.append(appendedElement);
    };*/

    self.uploadPetImages = function(){
        console.log('uploadPetImages', self.petImage);

        angular.forEach(self.petImage, function(file){
            //console.log('each file', file.webkitRelativePath);

            Upload.upload({
                url: '/api/fitpet/uploadPetResource/' + encodeURIComponent(file.webkitRelativePath),
                method: 'POST',
                headers: {
                    'Content-Type': 'multipart/form-data'
                },
                file: file,
                fileFromDataName: file.name
            }).success(function(data){
                console.log('success', data);
            }).error(function(data){
                console.log('error', data);
            });
        });

        //Upload.upload({
        //    url                         : '/api/fitpet/uploadPetResource',
        //    method                      : 'POST',
        //    headers                     : {
        //        'Content-Type'          : 'multipart/form-data'
        //    },
        //    file        : self.petImage,
        //    fileFromDataName            : 'sample 1'
        //}).success(function(data){
        //    console.log('success', data);
        //}).error(function(data){
        //    console.log('error', data);
        //});
    };

    self.updatePet = function(){
        $http.post('/api/fitpet/updatePet', {
            pet_name            : self.pet_name,
            description         : self.description,
            expiry_date         : self.expiry_date,
            price               : self.price,
            enabled             : self.is_enabled
        }).success(function(data, status){
            console.log(data);
        }).error(function(data, status){
            console.log(data);
        });
    };

    self.checkParams = function(){

        var flag = true;
        var message = '';

        if(self.pet_name == undefined){
            flag = false;
            message = 'Please enter pet name';
        }else if(self.description == undefined){
            flag = false;
            message = 'Please enter pet description';
        }else if(self.expiry_date == undefined){
            flag = false;
            message = 'Please enter expiry date';
        }else if(self.price == undefined){
            flag = false;
            message = 'Please enter price';
        }

        if(!flag){
            $mdDialog.show(
                $mdDialog.alert()
                    .parent(angular.element(document.body))
                    .title('Add Pet')
                    .content(message)
                    .ariaLabel('Add Pet Dialog')
                    .ok('OK')
            );
        }

        return flag;
    };

    $scope.init = function(){
        var params = $location.search();

        if(params.action == 'update'){
            self.showUpdateButton = true;

            var data = JSON.parse(params.data);

            self.pet_name = data.pet_name;
            self.description = data.description;
            self.expiry_date = new Date(data.expiry_date);
            self.price = data.price;
            self.is_enabled = data.enabled;
        }
    };
}]);