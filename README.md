# Wave Client

This is an experimental port of some parts of the Apache Wave client to Android. The aim is to provide a 
simple client able to communicate with the Wave server. The Wave's real-time concurrent editor is excluded as long as it's a
Web application developed with GWT.



## Building and Debug the project

- Configure and run an AVD with Android 4.0
- Build the debug apk `ant clean debug`
- Deploy the app running `adb install bin/WaveClient-debug.apk`
- Use Eclipse LogCat & DDMS

More info `http://developer.android.com/tools/building/building-cmdline.html`



## Setting up the Android project from scratch

Get original source code from Wave repository (git clone git://git.apache.org/incubator-wave.git wave).
This projects contains modifications from the original Wave project, named Wave-mod.
Get third party dependencies and compile the Wave project:

```
ant get-third-party compile
```


Copy selected set of source code files. First get all files:


```
cp -R wave/src/* wave-client/src/
cp -R wave/proto_src/ wave-client/

# The gen/ folder is reserved in Android project
mkdir wave-client/gen-wave
cp -R wave/gen/* wave-client/gen-wave/
```

Following packages must be removed from the Android project's build path, in order to ignore unused classes.


```
org/waveprotocol/box/server
org/waveprotocol/box/expimp
org/waveprotocol/wave/model/extended/WaveConversationUtils.java

com/google/wave
org/waveprotocol/examples
org/waveprotocol/pst
org/waveprotocol/wave/federation/
org/waveprotocol/wave/util/settings
org/waveprotocol/wave/crypto
org/waveprotocol/box/server/gxp

org/waveprotocol/wave/model/testing
org/waveprotocol/wave/model/conversation/testing
org/waveprotocol/wave/client/testing
```

Libraries. From the wave's third-party library folder, get the following jar's files and put them into the `libs/` folder:

```
protobuf-java-2.5.0.jar
gson-2.2.4.jar
jsr305-2.0.1.jar
```

These other libs must be repackaged after removing unused classes:

```
gwt-user-2.6.1.jar
gwt-dev-2.6.1.jar
guava-15.0.jar
```

Put them into the `tools/jarjar/jar-source folder`. Launch the script `slice-jars.sh`.
Then copy sliced .jars into the `libs/` 

## Proguard

Proguard is needed for development. It avois the 65K methods limit. Be sure to use the provided config.
See custom_rules.xml and proguard config files.

## Memory problems

Building process can use a lot of memory. Set more heap space to the JVM.
See http://stackoverflow.com/questions/9471194/unable-to-execute-dex-gc-overhead-limit-exceeded



## General notes

Project must be managed via Ant in order to use Proguard in the development build process. 
This avoids the 65K methods limit problem of the Dalvik VM. See following references:

- http://stackoverflow.com/questions/15209831/unable-to-execute-dex-method-id-not-in-0-0xffff-65536
- http://stackoverflow.com/questions/15471772/how-to-shrink-code-65k-method-limit-in-dex
- http://android-developers.blogspot.se/2011/07/custom-class-loading-in-dalvik.html
- http://code.google.com/p/guava-libraries/wiki/UsingProGuardWithGuava
- http://stackoverflow.com/questions/14023397/is-there-a-way-to-get-count-of-number-methods-used-in-a-jar-file



The Android project has been create with the following command:

```
./android create project --target android-15 \
--name WaveClient \
--path ~/wave-android \ 
--activity WaveAndroid \
--package org.waveprotocol.android
```


### Dependencies

WAsync-1.4.3 - Atmosphere Client
Async-Http-Client-18.14 - Asynchronous Http Client




