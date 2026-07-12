# MyCureMate xDrip (Android)

This is a fork of [xDrip+](https://github.com/NightscoutFoundation/xDrip) for the MyCureMate / Chronosync ecosystem. It uploads Dexcom / Libre / Stelo readings to the MyCureMate curemate backend via Nightscout-compatible REST API.

## Quick start for MyCureMate

1. Build and install the APK on your Android phone (see **Build** below).
2. Pair your sensor in xDrip as usual.
3. In xDrip: **Settings → Cloud Upload → Nightscout (REST-API)**
   - Enable **API v1.x**
   - **Base URL**: `http://<your-backend-ip>:8000/api/v1/`
   - **API Secret**: paste the **CGM token** shown in MyCureMate app → Settings → CGM Integration
4. Tap **Test credentials**. If the backend is reachable, uploads start automatically.

### Why paste the token as API Secret?

The MyCureMate token is already a 40-character SHA1 hex string. This fork detects that format and sends it as-is in the `api-secret` header, so it matches the token stored in the curemate backend.

## Build (local debug APK for Realme / Android)

Requires Java 17 and Android SDK. Set the environment before building:

```bash
export JAVA_HOME=/opt/homebrew/opt/openjdk@17/libexec/openjdk.jdk/Contents/Home
export ANDROID_HOME=/Users/nithin/Library/Android/sdk
export PATH="$PATH:$ANDROID_HOME/platform-tools:$ANDROID_HOME/cmdline-tools/latest/bin"

cd /Users/nithin/Downloads/experiments/chronosync/xdripandroid
./gradlew assembleDebug -PskipGoogleServices

# APK is built for the fastDebug variant:
adb install app/build/outputs/apk/fast/debug/app-fast-debug.apk
```

`-PskipGoogleServices` is needed because the bundled `google-services.json` is for the original `com.eveningoutpost.dexdrip` package, and we changed the application ID to `com.chronosync.mycuremate.xdrip` so this fork can install alongside the official xDrip+.

## What changed from upstream xDrip+

- `app/build.gradle`: applicationId → `com.chronosync.mycuremate.xdrip`
- `app/src/main/res/values/internal.xml`: app name → `MyCureMate xDrip`
- `app/src/main/java/com/eveningoutpost/dexdrip/utilitymodels/NightscoutUploader.java`: if the API secret is a 40-character hex token, send it without re-hashing.

---

# Nightscout xDrip
> Enhanced personal research version of xDrip

 <img align="right" src="Documentation/images/download-xdrip-plus-qr-code.png">
 Info page and APK download: https://jamorham.github.io/#xdrip-plus

<img align="right" src="https://travis-ci.org/jamorham/xDrip-plus.svg?branch=master"><a align="right" title="Crowdin" target="_blank" href="https://crowdin.com/project/xdrip"><img align="right" src="https://badges.crowdin.net/xdrip/localized.svg"></a>

## Features
* Voice, Keypad or Watch input of Treatments (Insulin/Carbs/Notes)
* Visualization of Insulin and Carb action curves + Undo/Redo
* Improved alerts and predictive low forecasting feature
* Instant data synchronization between phones and tablets
* Support for many different data sources
* Published by the Nightscout Foundation

 <img align="middle" src="https://jamorham.github.io/images/jamorham-natural-language-treatments-two-web.png">

## What does it do?

xDrip is an unofficial and independent Android app which works as data hub and processor between many different devices.

It supports wireless connections to G6, G7, Medtrum A6, Libre via NFC and Bluetooth, 630G, 640G, 670G pumps, CareSens Air and Eversense CGM via companion apps. Bluetooth Glucose Meters such as the Contour Next One, AccuChek Guide, Verio Flex & Diamond Mini as well as devices like the Pendiq 2.0 Insulin Pen.

Heart-rate and step counter data is processed from Android Wear, Garmin, Fitbit and Pebble smart-watches and watch-faces for those that show glucose values and graphs.

On some Android Wear watches, it is possible for the G6 to talk directly to the watch so it can display values even when out of range of the phone.

The app contains sophisticated charting, customization and data entry features as well as a predictive simulation model.

Instant two-way synchronization is possible by linking follower handsets, data can also be uploaded and downloaded to a Nightscout web service or uploaded directly to Tidepool, MongoDB or InfluxDB.

Customization allows for different options to configure alarms, vocalize readings, change the display preferences etc. International users can update translations from within the app too.

Your data is yours and can be exported in many different ways. xDrip also intercommunicates with other apps, for example sending and receiving live data with AndroidAPS.


## Ethos
* Developed using Rapid Prototyping methodology
* Immediate results favoured to prove concepts
* Designed to support my personal research goals
* User Choice always a high priority
* No registration or Internet access required
* Community testing and collaboration appreciated!

## Roadmap
* Calibration improvements
* Supporting the large family of devices
* Increasing automation and data backup and sync options
* More Nightscout and APS integration

## Collaboration
We are very happy if people want to collaborate with this project. Please contact us at [Discussions](https://github.com/NightscoutFoundation/xDrip/discussions) if you want to get involved and study the [collaboration guidelines](CONTRIBUTING.md) before submitting any patches or pull requests.

## Thanks
None of this would be possible without all the hard work of the xDrip and Nightscout communities who have developed such excellent software and allowed us to build upon it.

