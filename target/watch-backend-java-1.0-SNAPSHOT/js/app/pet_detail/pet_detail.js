'use strict';

angular.module('projectFitpetApp.pet_detail', ['ngRoute'])

.config(['$routeProvider', function($routeProvider) {
        $routeProvider.when('/pet-detail', {
            templateUrl: '/js/app/pet_detail/pet_detail.html',
            controller: 'PetDetailCtrl',
            controllerAs: 'pdc'
        });
}])

.controller('PetDetailCtrl', ['$http', '$scope', '$location', '$mdDialog', function($http, $scope, $location, $mdDialog) {
        var self = this;

        self.retrievePetAnim = function(petGroup){
            $http.get('/api/fitpet/getImageResources/' + petGroup)
                .success(function(data, status, headers, config){
                    //console.log('success', data);

                    if(status == 200){
                        self.petImgArr = [];
                        self.petImgArrIndexes = [];

                        for(var i = 0; i < data.length; i++){
                            var tempPetAnim = data[i].split('/')[1];
                            var tempPetName = data[i].split('/')[2];
                            var tempPetAnimContainer = [];

                            if(self.petImgArr[tempPetAnim] != null){
                                self.petImgArr[tempPetAnim].push(tempPetName);
                            }else{
                                tempPetAnimContainer.push(tempPetName);
                                self.petImgArr[tempPetAnim] = tempPetAnimContainer;
                                self.petImgArrIndexes.push(tempPetAnim);

                                //angular.element('#action-container').append('<span>' + tempPetAnim + '</span>');
                            }
                        }

                        console.log(self.petImgArr);
                        console.log(self.petImgArrIndexes);

                        //Code below for animation image per image
                        /*var canvasElement = document.getElementById('canvas');
                        var ctx = canvasElement.getContext('2d');
                        ctx.clearRect(0, 0, ctx.canvas.width, ctx.canvas.height);

                        var world = createWorldWithGravity();

                        for(var j = 0; j < data.length; j++){
                            var body = createBox(world, 75, 75, 150, 150, true);

                            body.image = new Image();
                            body.image.src = '/api/fitpet/getSpecificFile/' + replaceAll(data[j], '/', '+');
                            body.image.onload = function(){
                                draw(world, ctx);
                            };
                        }

                        runWorld(world);
                        runAnimation(ctx);*/
                    }
                }).error(function(data, status, headers, config){
                    console.log('error', data);
                });
        };

        self.retrieveFile = function(petImage){
            $http.get('/api/fitpet/getImageResource/' + petImage)
                .success(function(data, status){
                    console.log('success', status, data);
                })
                .error(function(data, status){
                    console.log('error', status, data);
                });
        };

        self.updatePet = function(){
            var data = {
                pet_name: self.pet_name,
                description: self.description,
                price: self.price,
                enabled: self.enabled,
                update_date: self.update_date,
                create_date: self.create_date,
                expiry_date: self.expiry_date
            };

            $location.url('/add-pet?action=update&data=' + JSON.stringify(data));
        };

        self.getPet = function(petName){
            $http.get('/api/fitpet/getPet/' + petName)
                .success(function (data, status) {
                    console.log(data);

                    if(data.status == 200){
                        var pet = data.data;

                        self.pet_name = pet.name;
                        self.description = pet.description;
                        self.price = pet.price;
                        self.enabled = pet.enabled;
                        self.update_date = pet.updateDate;
                        self.create_date = pet.createDate;
                        self.expiry_date = pet.expiryDate;
                        self.avatar = pet.avatar;


                        self.retrievePetAnim(pet.avatar.split('+')[0]);
                    }
                })
                .error(function (data, status) {
                    console.log(data);
                });
        };

        self.deletePet = function(){
            $http.delete('/api/fitpet/deletePet/' + self.pet_name)
                .success(function(data, status){
                    console.log(data);

                    if(data.status == 200){
                        $http.delete('/api/fitpet/deleteImageResources/' + self.avatar.split('+')[0])
                            .success(function (data2, status) {

                                if(data.status == 200){
                                    $location.path('/');
                                    $mdDialog.show(
                                        $mdDialog.alert()
                                            .parent(angular.element(document.body))
                                            .title('Pet detail')
                                            .content(data.message)
                                            .ariaLabel('Pet detail')
                                            .ok('OK')
                                    );
                                }
                            })
                            .error(function (data2, status) {
                                console.log(data);
                            });
                        //$location.path('/');
                    }else{
                        $mdDialog.show(
                            $mdDialog.alert()
                                .parent(angular.element(document.body))
                                .title('Pet detail')
                                .content(data.message)
                                .ariaLabel('Pet detail')
                                .ok('OK')
                        );
                    }
                })
                .error(function(data, status){
                    console.log(data);
                });
        };


        $scope.init = function () {

            var pet = $location.search();
            console.log('pet_name', pet.pet_name);
            self.getPet(pet.pet_name);
        };

        function draw(world, ctx){
            if (ctx !== undefined && world !== undefined){
                for (var b = world.GetBodyList(); b; b = b.GetNext()) {
                    if (b.image !== undefined) {
                        ctx.save();
                        var pos = b.GetPosition(); // center position
                        ctx.translate(pos.x*scale, pos.y*scale);
                        ctx.rotate(b.GetAngle());
                        ctx.drawImage(b.image, -b.image.width/2, - b.image.height/2);
                        ctx.restore();
                    }
                }
            }
        }

        function runWorld(world){
            // Box2D simulation step at 60 fps, 10 iterations for solvers
            world.Step(1/60, 10, 10); // seconds
            world.ClearForces(); // Box2D specific
            setTimeout(runWorld, 1000/60); // loop at 60 fps (milliseconds)
        }

        function runAnimation(ctx){
            ctx.clearRect(0,0,ctx.canvas.width, ctx.canvas.height); // erase
            //world.DrawDebugData(); // draw
            requestAnimationFrame(runAnimation); // let the browser decide fps
        }
}]);