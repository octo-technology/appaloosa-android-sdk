SDK Appaloosa Android
=====================

Main features of Appaloosa SDK
------------------------------

* Application auto-update: check if the application is up to date and install the latest version if not

Installation
------------

If you use maven or any other tool with dependency management, add the following dependency :

      <dependency>
          <groupId>com.octo.android.appaloosa</groupId>
	        <artifactId>appaloosa-sdk</artifactId>
	        <version>1.0</version>
	    </dependency>

Since the component is not on maven central, you will need to install the dependency first:

    mvn install:install-file -Dfile=<path-to-file> -DgroupId=com.octo.android.appaloosa -DartifactId=appaloosa-sdk -Dversion=1.0 -Dpackaging=jar

If you don't use maven, simply add the appaloosa-sdk-1.0-jar-with-dependencies.jar to your libs folder.

Usage
-----

In all cases, you need to configure a service in your AndroidManifest.xml in order to request the Appaloosa server asynchronously:

    <service android:name="com.octo.appaloosasdk.async.AppaloosaSpiceService" android:exported="false" />

You also need the following permisions (network state and internet to request Appaloosa services on internet, external storage to store the apk downloaded):

    <uses-permission android:name="android.permission.ACCESS_NETWORK_STATE" />
    <uses-permission android:name="android.permission.INTERNET" />
    <uses-permission android:name="android.permission.WRITE_EXTERNAL_STORAGE" />

Then you only need the following line in your code:

     Appaloosa.getInstance().autoUpdate(this, STORE_ID, STORE_TOKEN);
     
STORE_ID and STORE_TOKEN can be found in the Appaloosa console (in settings section).

Follow the settings link

![Settings link](https://raw.github.com/octo-online/appaloosa-android-sdk/master/images/appaloosa-store-settings-link.png)

STORE_ID and STORE_TOKEN are located at the bottom of this page

![store toek and store id](https://raw.github.com/octo-online/appaloosa-android-sdk/master/images/appaloosa-store-token-and-id.png)

If you want to go further, you can watch the sdk-test subproject to have 3 samples.

License
-------

  Copyright (C) 2012 Octo Technology (http://www.octo.com)
  
	Licensed under the Apache License, Version 2.0 (the "License");
	you may not use this file except in compliance with the License.
	You may obtain a copy of the License at
	
	     http://www.apache.org/licenses/LICENSE-2.0
	
	Unless required by applicable law or agreed to in writing, software
	distributed under the License is distributed on an "AS IS" BASIS,
	WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
	See the License for the specific language governing permissions and
	limitations under the License.
