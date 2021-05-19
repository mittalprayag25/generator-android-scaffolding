"use strict";
var Generator = require("yeoman-generator");
var chalk = require("chalk");
var yosay = require("yosay");
var DOMParser = require("xmldom").DOMParser;
var XMLSerializer = require("xmldom").XMLSerializer;

module.exports = class extends Generator {
  initializing() {
    this.props = {};
  }

  prompting() {
    // Have Yeoman greet the user.
    this.log(
      yosay(
        "Welcome to the " + chalk.red("React Native Activity") + " generator!"
      )
    );
    const prompts = [
      {
        name: "applicationId",
        message:
          "What is the applicationId of your app example (com.company.project)?",
        store: true,
        default: this.applicationId
      },
      {
        name: "packageName",
        message: "In which package do you want to put this activity?",
        store: true,
        default: this.packageName
      },
      {
        name: "layoutXml",
        message:
          "What are you calling your activity xml (example : activity_home)? DONT WRITE .xml",
        store: true,
        default: this.layoutXml,
        validate: function(input) {
          if (/^([a-zA-Z0-9_]*)$/.test(input)) {
            return true;
          }

          return (
            "Your application name cannot contain special characters or a blank space, using the default name instead : " +
            this.layoutXml
          );
        }
      },
      {
        name: "projectName",
        store: true,
        default: this.projectName
      }
    ];
    return this.prompt(prompts).then(props => {
      this.props.layoutXml = props.layoutXml;
      this.props.packageName = props.packageName;
      this.props.applicationId = props.applicationId;
      this.props.projectName = props.projectName;
    });
  }

  writing() {
    var fullPackage = this.props.applicationId;
    var fullPackageFolder = this.props.applicationId.split(".").join("/");

    var xmlSplit = this.props.layoutXml.toLowerCase().split("_");
    var packageName = this.props.packageName;
    var packageNameFolder = this.props.packageName.split(".").join("/");

    var layoutXml = this.props.layoutXml.toLowerCase();
    for (var i = 0; i < xmlSplit.length; i++) {
      xmlSplit[i] =
        xmlSplit[i].charAt(0).toUpperCase() + xmlSplit[i].substring(1);
    }

    var projectName = this.props.projectName;
    var projectPath = projectName + "/";

    var name = xmlSplit.join("");
    var BR = name.charAt(0).toLowerCase() + name.substring(1);
    this.fs.copyTpl(
      this.templatePath("TemplateActivity.kt"),
      this.destinationPath(
        projectPath +
          "app/src/main/java/" +
          fullPackageFolder +
          "/" +
          packageNameFolder +
          "/" +
          name +
          ".kt"
      ),
      {
        appPackage: fullPackage,
        packageName: packageName,
        name: name,
        layoutName: layoutXml,
        BR: BR
      }
    );

    this.fs.copyTpl(
      this.templatePath("TemplateActivityViewModel.kt"),
      this.destinationPath(
        projectPath +
          "app/src/main/java/" +
          fullPackageFolder +
          "/" +
          packageNameFolder +
          "/" +
          name +
          "ViewModel.kt"
      ),
      {
        appPackage: fullPackage,
        packageName: packageName,
        name: name
      }
    );
    this.fs.copyTpl(
      this.templatePath("template_activity.xml"),
      this.destinationPath(
        projectPath + "app/src/main/res/layout/" + layoutXml + ".xml"
      ),
      {
        appPackage: fullPackage,
        packageName: packageName,
        name: name,
        BR: BR
      }
    );
    this.fs.copy(
      this.destinationPath(projectPath + "app/src/main/AndroidManifest.xml"),
      this.destinationPath(projectPath + "app/src/main/AndroidManifest.xml"),
      {
        process: function(contents) {
          // TODO parse xml and add activity into it

          var parser = new DOMParser();
          var xmlDoc = parser.parseFromString(contents.toString(), "text/xml");

          var application = xmlDoc.getElementsByTagName("application")[0];
          var newActivity = xmlDoc.createElement(
            'activity android:name=".' + packageName + "." + name + '"'
          );
          application.appendChild(newActivity);
          var oSerializer = new XMLSerializer();
          var sPrettyXML = oSerializer.serializeToString(xmlDoc);
          return sPrettyXML;
        }
      }
    );

    this.fs.copy(
      this.destinationPath(
        projectPath +
          "app/src/main/java/" +
          fullPackageFolder +
          "/di/ActivityModule.kt"
      ),
      this.destinationPath(
        projectPath +
          "app/src/main/java/" +
          fullPackageFolder +
          "/di/ActivityModule.kt"
      ),
      {
        process: function(contents) {
          var actualClass = contents.toString();

          var updatedClass = actualClass.replace(
            "@Module\nabstract class ActivityModule {",
            "import " +
              fullPackage +
              "." +
              packageName +
              "." +
              name +
              "\n\n\n" +
              "@Module\n" +
              "abstract class ActivityModule {\n\n" +
              "@ContributesAndroidInjector\n" +
              "abstract fun contribute" +
              name +
              "(): " +
              name
          );

          return updatedClass;
        }
      }
    );
    // Updating FragmentBuildersModule
    this.fs.copy(
      this.destinationPath(
        projectPath +
          "app/src/main/java/" +
          fullPackageFolder +
          "/di/ViewModelModule.kt"
      ),
      this.destinationPath(
        projectPath +
          "app/src/main/java/" +
          fullPackageFolder +
          "/di/ViewModelModule.kt"
      ),
      {
        process: function(contents) {
          var actualClass = contents.toString();

          var updatedClass = actualClass.replace(
            "@Module\ninternal abstract class ViewModelModule {",
            "import " +
              fullPackage +
              "." +
              packageName +
              "." +
              name +
              "ViewModel\n\n\n" +
              "@Module\n" +
              "internal abstract class ViewModelModule {\n\n" +
              "@Binds\n" +
              "@IntoMap\n" +
              "@ViewModelKey(" +
              name +
              "ViewModel:: class)\n" +
              "abstract fun bind" +
              name +
              "(viewModel: " +
              name +
              "ViewModel): ViewModel"
          );

          return updatedClass;
        }
      }
    );

    // Updating dependancies in build.gradle related to RN

    this.fs.copy(
      this.destinationPath(projectPath + "app/build.gradle"),
      this.destinationPath(projectPath + "app/build.gradle"),
      {
        process: function(contents) {
          var actualClass = contents.toString();
          var isReactConfigurationAlreadyComplete = actualClass.includes(
            "react-native\\/react.gradle"
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
                'apply from: file("../../node_modules/@react-native-community/cli-platform-android/native_modules.gradle"); applyNativeModulesAppBuildGradle(project)'
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

    // Copying the package json file

    this.fs.copy(
      this.templatePath("package.json"),
      this.destinationPath("../package.json")
    );
  }
};
