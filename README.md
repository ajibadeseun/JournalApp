# JournalApp
# The AndroidManifest.xml file should be as shown below:
       <?xml version="1.0" encoding="utf-8"?>
       <manifest xmlns:android="http://schemas.android.com/apk/res/android"
       package="com.example.user.journalapp">

       <uses-permission android:name="android.permission.INTERNET" />
       <uses-permission android:name="android.permission.READ_EXTERNAL_STORAGE" />
       <uses-permission android:name="android.permission.ACCESS_NETWORK_STATE" />
       <uses-permission android:name="android.permission.CALL_PHONE" />

    <application
        android:allowBackup="true"
        android:icon="@mipmap/ic_launcher"
        android:label="@string/app_name"
        android:roundIcon="@mipmap/ic_launcher_round"
        android:supportsRtl="true"
        android:theme="@style/AppTheme">
        <activity android:name=".MainActivity"
            android:theme="@style/FullScreen">
            <intent-filter>
                <action android:name="android.intent.action.MAIN" />

                <category android:name="android.intent.category.LAUNCHER" />
            </intent-filter>
        </activity>
        <activity
            android:name=".LoginActivity"
            android:label="Sign in"
            android:parentActivityName=".MainActivity">
            <meta-data
                android:name=".android.support.PARENT_ACTIVITY"
                android:value=".MainActivity" />
        </activity>
        <activity
            android:name=".HomeActivity"
            android:label="Journal App"
            android:theme="@style/AppTheme.NoActionBar" />
        <activity android:name=".NoteDetailsActivity"
            android:label="Make a note"
            android:windowSoftInputMode="adjustResize|stateHidden"
            android:parentActivityName=".HomeActivity">
            <meta-data
                android:name="android.support.PARENT_ACTIVITY"
                android:value=".HomeActivity"/>
        </activity>
    </application>

    </manifest>
# The build.gradle(Project:JournalApp) folder shown be as shown below:
     // Top-level build file where you can add configuration options common to all sub-projects/modules.

    buildscript {
    
      repositories {
        google()
        jcenter()
        maven { url "http://jcenter.bintray.com"}
        maven {
            url 'https://maven.fabric.io/public'
        }
      }
      dependencies {
        classpath 'com.android.tools.build:gradle:3.1.3'
        classpath 'com.google.gms:google-services:4.0.0'
        classpath 'io.fabric.tools:gradle:1.25.4'
        

        // NOTE: Do not place your application dependencies here; they belong
        // in the individual module build.gradle files
        classpath 'com.google.gms:google-services:3.1.1'
    }
}

    allprojects {
      repositories {
        google()
        jcenter()
        maven {
            url 'https://jitpack.io'
        }
      }
   }

    task clean(type: Delete) {
    delete rootProject.buildDir
    }

# The build.gradle(Module:app) folder should be as shown below:
    apply plugin: 'com.android.application'

      android {
        compileSdkVersion 27
        defaultConfig {
        applicationId "com.example.user.journalapp"
        minSdkVersion 16
        targetSdkVersion 27
        versionCode 1
        versionName "1.0"
        vectorDrawables.useSupportLibrary = true
        testInstrumentationRunner "android.support.test.runner.AndroidJUnitRunner"
    }
    buildTypes {
        release {
            minifyEnabled false
            proguardFiles getDefaultProguardFile('proguard-android.txt'), 'proguard-rules.pro'
        }
    }
}

       dependencies {
       implementation fileTree(dir: 'libs', include: ['*.jar'])
       implementation 'com.android.support:appcompat-v7:27.1.1'
       implementation 'com.android.support.constraint:constraint-layout:1.1.2'
       implementation 'com.android.support:support-v4:27.1.1'

       // FirebaseUI for Firebase Realtime Database
       implementation 'com.firebaseui:firebase-ui-database:4.1.0'

       // FirebaseUI for Cloud Firestore
       implementation 'com.firebaseui:firebase-ui-firestore:4.1.0'

       // FirebaseUI for Firebase Auth
       implementation 'com.firebaseui:firebase-ui-auth:4.1.0'

       // FirebaseUI for Cloud Storage
       implementation 'com.firebaseui:firebase-ui-storage:4.1.0'
       // Firebase
       implementation 'com.google.firebase:firebase-analytics:16.0.0'
       implementation 'com.google.firebase:firebase-database:16.0.1'
       implementation 'com.google.firebase:firebase-storage:16.0.1'
       implementation 'com.google.firebase:firebase-auth:16.0.1'
       implementation 'com.google.firebase:firebase-config:16.0.0'
       implementation 'com.google.android.gms:play-services-appinvite:16.0.0'
       implementation 'com.google.firebase:firebase-messaging:17.0.0'
       implementation 'com.google.android.gms:play-services-ads:15.0.1'
       implementation 'com.google.firebase:firebase-appindexing:15.0.1'
       implementation 'com.google.firebase:firebase-crash:16.0.0'
       implementation 'com.firebaseui:firebase-ui-auth:4.1.0'
       implementation 'com.crashlytics.sdk.android:crashlytics:2.9.3'
       implementation('com.facebook.android:facebook-android-sdk:4.27.0')
       implementation 'com.android.support:support-vector-drawable:27.1.1'
       // Google
       implementation 'com.google.android.gms:play-services-auth:15.0.1'

       // Firebase UI
       implementation 'com.firebaseui:firebase-ui-database:3.0.0'

       implementation 'com.google.firebase:firebase-core:16.0.0'
       testImplementation 'junit:junit:4.12'
       androidTestImplementation 'com.android.support.test:runner:1.0.2'
       androidTestImplementation 'com.android.support.test.espresso:espresso-core:3.0.2'
      }

    apply plugin: 'com.google.gms.google-services'

