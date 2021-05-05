'use strict';
const Generator = require('yeoman-generator');
const chalk = require('chalk');
const yosay = require('yosay');
const mkdirp = require('mkdirp');

module.exports = class extends Generator {
  initializing() {
    this.props = {};
  }

  prompting() {
    // Have Yeoman greet the user.
    this.log(yosay(
      'Welcome to the Mobile Chapter App Generator ' + chalk.red('generator-mc-android.starter') + ' generator!'
    ));
    const prompts = [{
      name: 'packageName',
      message: 'What is the Package Name of your app?',
      store: true,
      default: this.packageName
    }];
    return this.prompt(prompts).then(props => {
      this.props.packageName = props.packageName;
    });
  }


  writing() {
    var packageDir = this.props.packageName.replace(/\./g, '/');
    mkdirp('app');
    mkdirp('app/src/androidTest/java/' + packageDir);
    mkdirp('app/src/androidTestMock/java/' + packageDir);
    mkdirp('app/src/main/java/' + packageDir);
    mkdirp('app/src/mock/java/' + packageDir);
    mkdirp('app/src/prod/java/' + packageDir);
    mkdirp('app/src/test/java/' + packageDir);


    this.fs.copy(this.sourceRoot() + '/android/gradle', 'gradle');
    this.fs.copy(this.sourceRoot() + '/android/app/src/main/res', 'app/src/main/res');

    this.fs.copy(this.sourceRoot() + '/android/build.gradle', 'build.gradle');
    this.fs.copy(this.sourceRoot() + '/android/gradle.properties', 'gradle.properties');
    this.fs.copy(this.sourceRoot() + '/android/gradlew', 'gradlew');
    this.fs.copy(this.sourceRoot() + '/android/gradlew.bat', 'gradlew.bat');
    this.fs.copy(this.sourceRoot() + '/android/settings.gradle', 'settings.gradle');
    this.fs.copy(this.sourceRoot() + '/android/local.properties', 'local.properties');
    this.fs.copy(this.sourceRoot() + '/android/versions.gradle', 'versions.gradle');

    this.fs.copyTpl(this.sourceRoot() + '/android/app/proguard-rules.pro', 'app/proguard-rules.pro', { appPackage: this.props.packageName });
    this.fs.copyTpl(this.sourceRoot() + '/android/app/build.gradle', 'app/build.gradle', { appPackage: this.props.packageName });
    //this.fs.copyTpl(this.sourceRoot() + '/android/app/src/androidTest/java/com/generator/superapp', 'app/src/androidTest/java/' + packageDir, { appPackage: this.props.packageName });

    this.fs.copyTpl(this.sourceRoot() + '/android/app/src/main/java/com/generator/superapp', 'app/src/main/java/' + packageDir, { appPackage: this.props.packageName });
    //this.fs.copyTpl(this.sourceRoot() + '/android/app/src/test/java/com/generator/superapp', 'app/src/test/java/' + packageDir, { appPackage: this.props.packageName });
    this.fs.copyTpl(this.sourceRoot() + '/android/app/src/main/AndroidManifest.xml', 'app/src/main/AndroidManifest.xml', { appPackage: this.props.packageName });
    this.fs.copyTpl(this.sourceRoot() + '/android/app/src/main/res/layout', 'app/src/main/res/layout', { appPackage: this.props.packageName });

  }
};
