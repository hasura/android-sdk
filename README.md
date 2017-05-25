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
   compile 'com.github.hasura:android-sdk:007bdf6543'
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
  <groupId>com.github.hasura</groupId>
  <artifactId>android-sdk</artifactId>
  <version>d537b795c5</version>
</dependency>
```


