'use strict';

angular.module('projectFitpetApp.version', [
  'projectFitpetApp.version.interpolate-filter',
  'projectFitpetApp.version.version-directive'
])

.value('version', '0.1');
