This is generator project for Android so that we can pick and choose the modules

## Installation

First, install [Yeoman](http://yeoman.io) and generator-android-scaffolding using [npm](https://www.npmjs.com/) (we assume you have pre-installed [node.js](https://nodejs.org/)).

```bash
npm install -g yo
npm install -g generator-android-scaffolding
```

Then generate your new project:

```bash
yo android-scaffolding
```

Then generate your new Activity:

```bash
yo android-scaffolding:activity
```

Generating Activity with fragment

```bash
yo android-scaffolding:activitywithfragment
```
You might face issue with compile if you already have no fragment in the project built from scaffolding

generating fragment

```bash
yo android-scaffolding:fragment
```

Generating React Native Activity to run the bundle

```bash
yo android-scaffolding:ReactNativeFragment
```

Add bundle file in assets folder and access by name in ReactNativeActivity

## License

AGPL-3.0 © [Prayag]()
