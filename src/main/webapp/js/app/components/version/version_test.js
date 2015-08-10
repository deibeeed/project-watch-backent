'use strict';

describe('projectFitpetApp.version module', function() {
  beforeEach(module('projectFitpetApp.version'));

  describe('version service', function() {
    it('should return current version', inject(function(version) {
      expect(version).toEqual('0.1');
    }));
  });
});
