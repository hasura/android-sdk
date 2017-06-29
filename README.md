Hasura Android SDK(This repo is being worked on and is not ready yet)
==================

The Android SDK for Hasura. 


Download
--------

For Gradle:

Add this to your project level build.gradle file

```groovy
allprojects {
  repositories {
    ...
    maven { url 'https://jitpack.io' }
  }
}
```

Next, Add the following to your app level build.gradle 

```groovy
dependencies {
   compile 'com.github.hasura:android-sdk.sdk:$release_tag' 
   //for eg: compile 'com.github.hasura:android-sdk.sdk:v0.0.4' 
}
```

For Maven:

Add the JitPack repository to your build file

```xml 
<repositories>
  <repository>
    <id>jitpack.io</id>
    <url>https://jitpack.io</url>
  </repository>
</repositories>
```

Add the dependency

```xml
<dependency>
  <groupId>com.github.hasura.android-sdk</groupId>
  <artifactId>sdk</artifactId>
  <version>v0.0.3/version>
</dependency>
```

For a rough documentation on how to use the sdk. Take a look at [this](https://gist.github.com/jaisontj/40f1a7cf55fc5b889c43c4a71996a34a).


