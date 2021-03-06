import org.gradle.kotlin.dsl.dependencies
import org.jetbrains.kotlin.config.KotlinCompilerVersion

plugins {
    id("com.android.application")
    kotlin("android")
    kotlin("kapt")
    id("kotlin-android")
    id("kotlin-android-extensions")
    id("io.fabric")
    id("androidx.navigation.safeargs")
}

repositories {
    maven (
            url = "https://dl.bintray.com/gavinlinau/android"
    )
}

//def keystorePropertiesFile = new File("/Users/gavinlin/workspace/android_release_key/keystore.properties")
//def keystoreProperties = new Properties()
//keystoreProperties.load(new FileInputStream(keystorePropertiesFile)build.gradle)

android {
    compileSdkVersion(28)
    defaultConfig {
        applicationId = "com.gavincode.bujo"
        minSdkVersion(21)
        targetSdkVersion(28)
        versionCode = 3
        versionName = "1.2"
        multiDexEnabled = true
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

//    signingConfigs {
//        release {
////            keyAlias keystoreProperties["keyAlias"]
////            keyPassword keystoreProperties["keyPassword"]
////            storeFile file(keystoreProperties["storeFile"])
////            storePassword keystoreProperties["storePassword"]
//        }
//    }

//    buildTypes {
//        release {
//            minifyEnabled false
//            proguardFiles getDefaultProguardFile("proguard-android.txt"), "proguard-rules.pro"
//            signingConfig signingConfigs.release
//        }
//    }
}

dependencies {
    implementation(fileTree(mapOf("dir" to "libs", "include" to listOf("*.jar"))))
    implementation(Deps.material)
    implementation(Deps.navigation_fragment)
    implementation(Deps.navigation_ui)
    implementation(Deps.support_appcomat_v7)
    implementation(Deps.support_constraint)
    implementation(Deps.support_recycler_view)
    implementation(Deps.support_card_view)
    implementation(Deps.firebase_ui_auth)
    implementation(Deps.firebase_core)
    implementation(Deps.firebase_firestore)
    implementation(Deps.firebase_storage)
    implementation(Deps.crashlytics)
    implementation(Deps.threetenabp)
    implementation(Deps.timber)
    implementation(Deps.dagger)
    kapt(Deps.dagger_compiler)
    implementation(Deps.dagger_android)
    implementation(Deps.dagger_android_support)
    kapt(Deps.dagger_android_compiler)
    implementation(Deps.rxjava)
    implementation(Deps.rxandroid)
    implementation(Deps.rxkotlin)
    implementation(Deps.kotlin_stdlib)
    implementation(Deps.room)
    kapt(Deps.room_compiler)
    implementation(Deps.room_rxjava2)
    implementation(Deps.lifecycle)
    kapt(Deps.lifecycle_compiler)
    implementation(Deps.gson)
    implementation(Deps.multidex)
    implementation(Deps.checklistview)
    testImplementation(Deps.test_junit)
    androidTestImplementation(Deps.test_runner)
    androidTestImplementation(Deps.room_test)
    androidTestImplementation(Deps.espresso_core)
    androidTestImplementation(Deps.espresso_contrib)
    androidTestImplementation(Deps.espresso_intents)
    androidTestImplementation(Deps.arch_core_testing)
}

apply(mapOf("plugin" to "com.google.gms.google-services"))
