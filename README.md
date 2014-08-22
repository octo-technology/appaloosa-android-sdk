SDK Appaloosa Android
=====================

For android 2.1+

Main features of Appaloosa SDK
------------------------------

* Application auto-update: check if the application is up to date and install the latest version if not
* Application developer-panel: display a screen with information about the device and the application.
* Application kill-switch: disallow a device to launch the application (if the device is blacklisted on appaloosa-store)

Installation
------------

### Maven way

If you use maven or any other tool with dependency management, add the following dependency :

       	<dependency>
			<groupId>com.appaloosa-store</groupId>
			<artifactId>appaloosa-android-sdk</artifactId>
			<type>aar</type>
			<version>1.0.4</version>
		</dependency>

### With Eclipse

Tested with Eclipse Juno.

* Install the Maven Integration for Eclipse v1.4.1 from the following repo via Eclipse's software installation feature: [Maven Integration for Eclipse v1.4.1 Repo](http://download.eclipse.org/technology/m2e/releases/1.4/1.4.1.20140328-1905/)
* Install the Android for Maven Eclipse package and the Android Developer tools if necessary from the repo [m2e-android Repo](http://rgladwell.github.io/m2e-android/updates)

* Import the SDK as an Android project.
* Mavenize it (right-click on the project folder, Configure, Convert to Maven Project).
* Update the Maven project (right-click on the project folder, Maven, Update Project...

At this point, the SDK folder project should not display any errors.
* Right-click the project folder, go to Properties, Android and check that "is Library" is checked.
* Still in the properties, go to Java Build Path, Source and expand the AppaloosaDevPanel/src folder. Double-click the Output Folder and set it as its default setting.
* Still in the Java Build Path, go to Order and Export and check "Maven Dependencies".
* In your own Android project properties, go to Java Build Path, Projects and add the SDK from there.
* Also add it in Properties, Android as a library reference.

You may encounter a Dex loader error: as the android-support-v4.jar is already included in the SDK's dependancies, you might have to delete it from your libs folder. 


Usage
-----

### Auto-update

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

### Developer panel

The developer panel gives information about the device and the application.

![appaloosa dev panel screenshot](https://raw.github.com/octo-online/appaloosa-android-sdk/dev-panel/images/appaloosa-dev-panel-1.png)

You need to add the following activity in your AndroidManifest.xml :

    <activity android:name="com.octo.appaloosasdk.ui.activity.AppaloosaDevPanelActivity" android:exported="false" />

Then you only need the following line in your code:

    Appaloosa.getInstance().displayDevPanel(this); 

### Kill switch

This SDK provides a mecanism of kill switch. Since the web interface (http://www.appaloosa-store.com/), you are able to authorize or not a device to access to the application. The mecanism also works offline by reading the blacklisted status from a protected local file.

In all cases, you need to configure a service in your AndroidManifest.xml in order to request the Appaloosa server asynchronously:

    <service android:name="com.octo.appaloosasdk.async.AppaloosaSpiceService" android:exported="false" />

You also need the following permisions (network state, internet and wifi_state to request Appaloosa services on internet, external storage to store the apk downloaded, phone_state to identify the device):

    <uses-permission android:name="android.permission.ACCESS_NETWORK_STATE" />
    <uses-permission android:name="android.permission.INTERNET" />
    <uses-permission android:name="android.permission.ACCESS_WIFI_STATE" />
    <uses-permission android:name="android.permission.WRITE_EXTERNAL_STORAGE" />
    <uses-permission android:name="android.permission.READ_PHONE_STATE" />

Then you have two choice to implement the kill switch.
The easiest way is to add this line to do the verification. Tips: do it in the first activity of your application.

     Appaloosa.getInstance().checkAuthorizations(this, STORE_ID, STORE_TOKEN);
     
STORE_ID and STORE_TOKEN can be found in the Appaloosa console (in settings section).

Follow the settings link

![Settings link](https://raw.github.com/octo-online/appaloosa-android-sdk/master/images/appaloosa-store-settings-link.png)

STORE_ID and STORE_TOKEN are located at the bottom of this page

![store toek and store id](https://raw.github.com/octo-online/appaloosa-android-sdk/master/images/appaloosa-store-token-and-id.png)

During the process, ProgressDialog is shown. 
If the device is allowed to access to the application, the ProcessDialog disapear.
If the device is now allowed, user if forced to quit the application.

You can also implement your own behavior through a listener ApplicationAuthorizationListener:
	
	Appaloosa.getInstance().checkAuthorizations(this, STORE_ID, STORE_TOKEN, new ApplicationAuthorizationListener(){
		@Override
		public void allow(Status status, String message) {
			// User is allowed to launch the application
		}
		@Override
		public void dontAllow(Status status, String message) {
			// User is not allowed to launch the application
		}
	});

Status are the following: 
	
	public static enum Status {
		UNKNOWN_APPLICATION, 
		AUTHORIZED, 
		UNREGISTERED_DEVICE, 
		UNKNOWN_DEVICE, 
		NOT_AUTHORIZED, 
		DEVICE_ID_FORMAT_ERROR, 
		NO_NETWORK, 
		REQUEST_ERROR, 
		UNKNOWN
	}

You can use them to help your user to launch the application (for example by telling him to switch on the network)

Sample
------

A sample app using this sdk is available here : https://github.com/octo-online/appaloosa-android-sdk-sample

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
