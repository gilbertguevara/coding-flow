var babel = require('babel');
var wallabyWebpack = require('wallaby-webpack');

var webpackPostprocessor = wallabyWebpack({
  resolve: {
    root: ['../node_modules']
  },
  module: {
    loaders: [
      {
        test: /sinon.*\.js$/,
        loader: 'imports?define=>false'
      }
    ]
  }
});

module.exports = function (wallaby) {
  var babelCompiler = wallaby.compilers.babel({ babel: babel, stage: 0 });

  return {
    files: [
      { pattern: 'node_modules/phantomjs-polyfill/bind-polyfill.js', instrument: false },
      { pattern: 'node_modules/babel-core/browser-polyfill.js', instrument: false },
      { pattern: 'lib/src/**/*.js', load: false }
    ],

    tests: [
      { pattern: 'lib/tests/**/*.spec.js', load: false }
    ],

    compilers: {
      'lib/**/*.js': babelCompiler
    },

    postprocessor: webpackPostprocessor,

    bootstrap: function () {
      window.__moduleBundler.loadTests();
    }
  };
};

