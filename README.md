Android SDK
===========

The Android SDK for Hasura.

NOTE
====

### This sdk works with Hasura running on version below 0.15.


Installation
------------

### Step 1 : Download the Hasura Android SDK

Using Gradle:

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
   compile 'com.github.hasura.android-sdk:sdk:v0.0.9'
}
```

Using Maven:

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
  <version>v0.0.9/version>
</dependency>
```

### Step 2 : Setup Hasura

#### Project Config

You set the project name and other hasura-project related things in Project Config object.

```java
//Minimum Config
ProjectConfig config = new ProjectConfig.Builder()
                 .setProjectName("projectName") // or it can be .setCustomBaseDomain("myCustomDomain.com")
                 .build()
```
Other methods available are :

- .enableOverHttp() // if included, then every network call is made over http (https is default)
- .setDefaultRole("customDefaultRole") // if not included then "user" role is used by default
- .setApiVersion(2) //if not included v1 is used by default

Use the above project config to initialise Hasura.

```java
Hasura.setProjectConfig(config)
  .enableLogs() // not included by default
  .initialise(this);
```

***Note***: Initialisation **MUST** be done before you use the SDK.The best place to initialise Hasura would be in your `application` class or in your Launcher Activity.

Hasura Client
-------------

The `HasuraClient` object is the most functional feature of the SDK. It is built using the project config specified on initialisation.
You can get an instance of the client only from Hasura, like so :

```java
HasuraClient client = Hasura.getClient();
```

### Hasura User

`HasuraClient` provides a `HasuraUser` object for all of your authentication needs(login, signup etc). This ensures that certain data can only be accessed by authorized users.

You can get an instance of the `HasuraUser` from the `HasuraClient` like so :

```java
HasuraUser user = client.getUser();
```

#### SignUp

```java
user.setUsername("username");
user.setPassword("password");
user.signUp(new SignUpResponseListener() {
            @Override
            public void onSuccessAwaitingVerification(HasuraUser user) {
              //The user is registered on Hasura, but either his mobile or email needs to be verified.
            }

            @Override
            public void onSuccess(HasuraUser user) {
              //Now Hasura.getClient().getCurrentUser() will have this user
            }

            @Override
            public void onFailure(HasuraException e) {
                //Handle Error
            }
        });
```

***Note***: All network calls are called on a non ui thread and all the callbacks are pushed into the ui thread.

#### Login

```java
user.setUsername("username");
user.setPassword("password");
user.login(new AuthResponseListener() {

            @Override
            public void onSuccess(HasuraUser user) {
              //Now Hasura.getClient().getCurrentUser() will have this user
            }

            @Override
            public void onFailure(HasuraException e) {
                //Handle Error
            }
        });
```    

#### LoggedIn User

Each time a `HasuraUser` is signed up or logged in, the session is cached by the `HasuraClient`. Hence, you do not need to log the user in each time your app starts.

```java
HasuraUser user = client.getUser();
if (user.isLoggedIn()) {
  //This user is logged in
} else {
  //This user is not logged in
}
```

#### Log Out

To log the user out, simple call `.logout` method on the user object.

```java
user.logout(new LogoutResponseListener() {
            @Override
            public void onSuccess(String message) {

            }

            @Override
            public void onFailure(HasuraException e) {

            }
        });
```

### Data Service

Hasura provides out of the box data apis on the Tables and views you make in your project. To learn more about how they work, check out the docs [here](https://hasura.io/_docs/platform/0.6/getting-started/4-data-query.html)

```java
client.useDataService()
  .setRequestBody(JsonObject)
  .expectResponseType(MyResponse.class)
  .enqueue(new Callback<MyResponse>, HasuraException>() {
                    @Override
                    public void onSuccess(MyResponse response) {
                      //Handle response
                    }

                    @Override
                    public void onFailure(HasuraException e) {
                        //Handle error
                    }
                });
```

In the above method, there are a few things to be noted :
- `.setRequestBody()`: This is an overloaded method which accepts either an object of type `JsonObject` or a POJO (ensure that the JSON representation of this object is correct)
- `.expectResponseType()`: Specify the POJO representation of the expected response.

***Note***: In case you are expecting an array response, use `.expectResponseTypeArrayOf()`. *All SELECT queries to the data service will return an array response.*

```
If the HasuraUser in the HasuraClient is loggedin/signedup then every call made by the HasuraClient will be
authenticated by default with "user" as the default role (This default role can be changed when building the project config)
```

In case you want to make the above call for an anonymous user

```java
client.asAnonymousRole()
  .useDataService()
  .setRequestBody(JsonObject)
  .expectResponseType(MyResponse.class)
  .enqueue(new Callback<MyResponse>, HasuraException>() {
                    @Override
                    public void onSuccess(MyResponse response) {
                      //Handle response
                    }

                    @Override
                    public void onFailure(HasuraException e) {
                        //Handle error
                    }
                });
```

In case you want to make the above call for a custom user

```java
client.asRole("customRole") //throws an error if the current user does not have this role
  .useDataService()
  .setRequestBody(JsonObject)
  .expectResponseType(MyResponse.class)
  .enqueue(new Callback<MyResponse>, HasuraException>() {
                    @Override
                    public void onSuccess(MyResponse response) {
                      //Handle response
                    }

                    @Override
                    public void onFailure(HasuraException e) {
                        //Handle error
                    }
                });
```

***Note***: This role will be sent JUST for this query and ***will not*** become the default role.

### Query Template Service

The syntax for the query template service remains the same as `Data Service` except for setting the name of the query template being used.

```java
client.useQueryTemplateService("templateName")
  .setRequestBody(JsonObject)
  .expectResponseType(MyResponse.class)
  .enqueue(new Callback<MyResponse>, HasuraException>() {
                    @Override
                    public void onSuccess(MyResponse response) {
                      //Handle response
                    }

                    @Override
                    public void onFailure(HasuraException e) {
                        //Handle error
                    }
                });
```


### Filestore Service

Hasura provides a filestore service, which can be used to upload and download files. To use the Filestore service properly, kindly take a look at the docs [here](https://docs.hasura.io/0.13/ref/hasura-microservices/filestore/index.html).

#### Upload File

The upload file method accepts the following:

- either a `File` object or a `byte` array (byte[]) which is to be uploaded.
- a `mimetype` of the file.
- `FileUploadResponseListener` which is an interface that handles the response.
- FileId (optional): Every uploaded file has an unique Id associated with it. You can optionally specify this fileId on     the `uploadFile` method. In case it is not, the SDK automatically assigns a unique Id for the file.

```java
client.useFileStoreService()
                .uploadFile(/*File or byte[]*/, /*mimeType*/, new FileUploadResponseListener() {
                    @Override
                    public void onUploadComplete(FileUploadResponse response) {
                      //Success
                    }

                    @Override
                    public void onUploadFailed(HasuraException e) {
                      //handle error
                    }
                });
```

`FileUploadResponse` object in the above response contains the following:
- file_id: The uniqiue Id of the file that was uploaded.
- user_id: The id of the user who uploaded the file.
- created_at : The time string for when this file was uploaded/created.

#### Download File

```java
client.useFileStoreService()
         .downloadFile("fileId", new FileDownloadResponseListener() {
                    @Override
                    public void onDownloadComplete(byte[] data) {
                      //successfule
                    }

                    @Override
                    public void onDownloadFailed(HasuraException e) {
                      //handle error
                    }

                    @Override
                    public void onDownloading(float completedPercentage) {
                      //download percentage
                    }
                });
```

### Custom Service

In addition to the Data, Auth and FileStore services, you can also deploy your own custom service on Hasura. For such cases, you can still utilize the session management of the SDK to make your APIs. Currently, we have support for [Retrofit](http://square.github.io/retrofit/).

#### Using a custom service - Retrofit Support

- Let's say you have a custom service set up on Hasura called "api"
- Your external endpoint for this custom service would be -> "api.<project-name>.hasura-app.io"
- This is a wrapper over Retrofit for custom services, assuming that your interface with the api definitions is called "MyCustomInterface.java"

#### Step1: Including the retrofit support

Using Gradle:

Add the following to your app level build.gradle

```groovy
dependencies {
   compile 'com.github.hasura:android-sdk:custom-service-retrofit:v0.0.999999999'
}
```

Using Maven:

Add the dependency

```xml
<dependency>
  <groupId>com.github.hasura.android-sdk</groupId>
  <artifactId>custom-service-retrofit</artifactId>
  <version>v0.0.9/version>
</dependency>
```


##### Step2: Build your custom service (before Hasura Init)

```java
RetrofitCustomService<MyCustomInterface> cs = new RetrofitCustomService.Builder()
                .serviceName("api")
                .build(MyCustomInterface.class);
```

##### Step3: Add this custom service during init

```java
Hasura.setProjectConfig(new HasuraConfig.Builder()
                 .setProjectName("projectName") // or it can be .setCustomBaseDomain("somthing.com")
                 .enableOverHttp() // if not included, then https by default
                 .setDefaultRole("customDefaultRole") // if not included then "user" role is used by default
                 .setApiVersion(2) //if not included v1 is used by default
                 .build())
  .enableLogs() // not included by default
  .addCustomService(cs)
  .initialise(this);
```

##### Step4: Accessing Custom Service

```java
MyCustomService cs = client.useCustomService(MyCustomInterface.class);
```

##### Bonus: Handle the Response

`RetrofitCallbackHandler` is a helper class which you can use to handle the responses from your custom APIs and parse errors.

EXAMPLES
--------

Check our [this](https://github.com/hasura/Modules-Android) for sample apps built using the SDK.

ISSUES
------

In case of bugs, please raise an issue [here](https://github.com/hasura/support)
