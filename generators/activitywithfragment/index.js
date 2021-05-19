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
        "Welcome to the Activity with Fragments " +
          chalk.red("generator-android-scaffolding") +
          " generator!"
      )
    );
    const prompts = [
      {
        name: "applicationId",
        message: "What is the applicationId of your app?",
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
          console.log("CONTENTS" + contents);
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
          console.log("CONTENTS" + contents);

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
              "@ContributesAndroidInjector(modules = [FragmentBuildersModule::class])\n" +
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
          console.log("CONTENTS" + contents);

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
  }
};
