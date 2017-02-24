/* eslint indent: 0 */
// jscs:disable requireCamelCaseOrUpperCaseIdentifiers

const path = require('path');
const ExtractTextPlugin = require('extract-text-webpack-plugin');
const BrowserSyncPlugin = require('browser-sync-webpack-plugin');
const merge = require('webpack-merge');
const webpack = require('webpack');

const TARGET = process.env.npm_lifecycle_event;
const SRC = path.resolve(__dirname, 'lib/src');
const DEST = path.resolve(__dirname, 'public');

const common = {
  entry: [
    'webpack-dev-server/client?http://0.0.0.0:3000',
    'webpack/hot/only-dev-server',
    SRC + '/index.js'
  ],
  resolve: {
    extensions: ['', '.js']
  },
  output: {
    path: path.join(__dirname, 'dist'),
    filename: 'js/main.js',
    publicPath: '/static/'
  }
};

if (TARGET.includes('fe:') || !TARGET) {
  module.exports = merge(common, {
    devtool: 'eval-source-map',
    module: {
      preLoaders: [
        { test: /\.js?$/, loaders: ['eslint', 'jscs'], exclude: /node_modules/ }
      ],
      loaders: [
        { test: /\.js?/, loaders: ['react-hot', 'babel'], exclude: /node_modules/ },
        {
          test: /\.scss?$/,
          loader: 'style!css?sourceMap' +
            '!autoprefixer?browsers=last 2 version' +
            '!sass?outputStyle=expanded&sourceMap&sourceMapContents'
        }
      ]
    },
    plugins: [
      new webpack.HotModuleReplacementPlugin()
    ]
  });
}
