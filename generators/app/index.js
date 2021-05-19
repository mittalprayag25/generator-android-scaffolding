"use strict";
const Generator = require("yeoman-generator");
const chalk = require("chalk");
const yosay = require("yosay");
const mkdirp = require("mkdirp");
const { exec } = require("child_process");

module.exports = class extends Generator {
  initializing() {
    this.props = {};
  }

  prompting() {
    // Have Yeoman greet the user.
    this.log(
      yosay(
        "Welcome to the Mobile Chapter App Generator " +
          chalk.red("generator-mc-android.scaffolding") +
          " generator!"
      )
    );
    const prompts = [
      {
        name: "projectName",
        message: "What is the Name of your app?",
        store: true,
        default: this.projectName
      },
      {
        name: "packageName",
        message: "What is the Package Name of your app?",
        store: true,
        default: this.packageName
      }
    ];
    return this.prompt(prompts).then(props => {
      this.props.packageName = props.packageName;
      this.props.projectName = props.projectName;
    });
  }

  writing() {
    var packageDir = this.props.packageName.replace(/\./g, "/");
    var projectName = this.props.projectName;

    mkdirp(projectName);
    mkdirp(projectName + "/app");
    mkdirp(projectName + "/app/src/androidTest/java/" + packageDir);
    mkdirp(projectName + "/app/src/androidTestMock/java/" + packageDir);
    mkdirp(projectName + "/app/src/main/java/" + packageDir);
    mkdirp(projectName + "/app/src/mock/java/" + packageDir);
    mkdirp(projectName + "/app/src/prod/java/" + packageDir);
    mkdirp(projectName + "/app/src/test/java/" + packageDir);

    var projectPath = projectName + "/";
    var appPath = projectName + "/app/";

    this.fs.copy(this.sourceRoot() + "/android/gradle", projectPath + "gradle");
    this.fs.copyTpl(
      this.sourceRoot() + "/android/app/src/main/res",
      projectPath + "/app/src/main/res",
      { projectName: projectName }
    );

    // This.fs.copyTpl(
    //   this.sourceRoot() + "/android/app/src/main/res/values/strings.xml",
    //   projectPath + "/app/src/main/res/values/strings.xml",
    //   { projectName: projectName }
    // );

    this.fs.copy(
      this.sourceRoot() + "/android/build.gradle",
      projectPath + "build.gradle"
    );
    this.fs.copy(
      this.sourceRoot() + "/android/gradle.properties",
      projectPath + "gradle.properties"
    );
    this.fs.copy(
      this.sourceRoot() + "/android/gradlew",
      projectPath + "gradlew"
    );
    this.fs.copy(
      this.sourceRoot() + "/android/gradlew.bat",
      projectPath + "gradlew.bat"
    );
    this.fs.copy(
      this.sourceRoot() + "/android/settings.gradle",
      projectPath + "settings.gradle"
    );
    this.fs.copy(
      this.sourceRoot() + "/android/local.properties",
      projectPath + "local.properties"
    );
    this.fs.copy(
      this.sourceRoot() + "/android/versions.gradle",
      projectPath + "versions.gradle"
    );

    this.fs.copyTpl(
      this.sourceRoot() + "/android/app/proguard-rules.pro",
      appPath + "proguard-rules.pro",
      { appPackage: this.props.packageName }
    );
    this.fs.copyTpl(
      this.sourceRoot() + "/android/app/build.gradle",
      appPath + "build.gradle",
      { appPackage: this.props.packageName }
    );
    // This.fs.copyTpl(this.sourceRoot() + '/android/app/src/androidTest/java/com/generator/superapp', 'app/src/androidTest/java/' + packageDir, { appPackage: this.props.packageName });

    this.fs.copyTpl(
      this.sourceRoot() + "/android/app/src/main/java/com/generator/superapp",
      appPath + "src/main/java/" + packageDir,
      { appPackage: this.props.packageName }
    );
    // This.fs.copyTpl(this.sourceRoot() + '/android/app/src/test/java/com/generator/superapp', 'app/src/test/java/' + packageDir, { appPackage: this.props.packageName });
    this.fs.copyTpl(
      this.sourceRoot() + "/android/app/src/main/AndroidManifest.xml",
      appPath + "src/main/AndroidManifest.xml",
      { appPackage: this.props.packageName }
    );
    this.fs.copyTpl(
      this.sourceRoot() + "/android/app/src/main/res/layout",
      appPath + "src/main/res/layout",
      { appPackage: this.props.packageName }
    );

    this.fs.copyTpl(this.sourceRoot() + "/package.json", "package.json", {
      projectName: projectName
    });

    // Updating dependancies in build.gradle related to RN

    this.fs.copy(
      this.destinationPath(appPath + "build.gradle"),
      this.destinationPath(appPath + "build.gradle"),
      {
        process: function(contents) {
          var actualClass = contents.toString();
          var isReactConfigurationAlreadyComplete = actualClass.includes(
            'apply from: "../../node_modules/react-native/react.gradle"'
          );
          var addPlugins;
          if (!isReactConfigurationAlreadyComplete) {
            addPlugins = actualClass.replace(
              "apply plugin: 'androidx.navigation.safeargs'",
              "apply plugin: 'androidx.navigation.safeargs'\n\n\n" +
                "project.ext.react = [\n" +
                'entryFile   : "index.js",\n' +
                "enableHermes: false,  // clean and rebuild if changing\n" +
                "]\n" +
                'apply from: "../../node_modules/react-native/react.gradle"\n' +
                "def jscFlavor = 'org.webkit:android-jsc:+'\n" +
                'def enableHermes = project.ext.react.get("enableHermes", false);\n\n\n'
            );

            if (
              addPlugins &&
              !addPlugins.includes(
                'implementation "com.facebook.react:react-native:+"'
              )
            ) {
              var addDependancies = addPlugins.replace(
                "androidTestImplementation deps.mockito.android",
                "androidTestImplementation deps.mockito.android\n\n" +
                  'implementation "com.facebook.react:react-native:+"\n' +
                  "implementation 'androidx.legacy:legacy-support-v4:1.0.0'\n" +
                  "if (enableHermes) {\n" +
                  'def hermesPath = "../../node_modules/hermes-engine/android/";\n' +
                  'debugImplementation files(hermesPath + "hermes-debug.aar")\n' +
                  'releaseImplementation files(hermesPath + "hermes-release.aar")\n' +
                  "} else {\n" +
                  "implementation jscFlavor\n" +
                  "}\n"
              );

              var applyFile = addDependancies.concat(
                '\napply from: file("../../node_modules/@react-native-community/cli-platform-android/native_modules.gradle"); applyNativeModulesAppBuildGradle(project)'
              );

              return applyFile;
            }

            return addPlugins;
          }

          return actualClass;
        }
      }
    );

    // Updating dependancies settings.gradle

    this.fs.copy(
      this.destinationPath(projectPath + "settings.gradle"),
      this.destinationPath(projectPath + "settings.gradle"),
      {
        process: function(contents) {
          var actualClass = contents.toString();
          var isReactConfigurationAlreadyComplete = actualClass.includes(
            "applyNativeModulesSettingsGradle"
          );
          if (!isReactConfigurationAlreadyComplete) {
            var changees = actualClass.concat(
              '\n\napply from: file("../node_modules/@react-native-community/cli-platform-android/native_modules.gradle"); applyNativeModulesSettingsGradle(settings)'
            );
            return changees;
          }

          return actualClass;
        }
      }
    );
  }

  end() {
    this.log(
      yosay(
        chalk.yellow(
          "Take a deep breath while I download node modules for React Native use"
        )
      )
    );
    exec("yarn", (err, stdout, stderr) => {
      if (err) {
        // Node couldn't execute the command
        return;
      }

      // The *entire* stdout and stderr (buffered)
      console.log(`stdout: ${stdout}`);
      console.log(`stderr: ${stderr}`);

      this.log(
        yosay(chalk.green("Its done enjoy.. call Prayag for any more help"))
      );
    });
  }
};
