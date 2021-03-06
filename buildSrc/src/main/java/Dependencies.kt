
object Versions {
    val kotlin = "1.3.41"
    val android_support = "28.0.0"
    val constraint = "2.0.0-beta2"
    val firebase_core = "17.0.1"
    val firebase_firestore = "20.1.0"
    val firebase_storage = "18.1.0"
    val firebase_ui_auth = "5.0.0"

    val junit = "4.12"
    val runner = "1.0.1"
    val espresso = "3.0.1"
    val timber = "4.6.1"
    val threetenabp = "1.0.5"
    val dagger = "2.23.2"
    val rxjava = "2.2.10"
    val rxandroid = "2.1.1"
    val room = "1.1.0"
    val gson = "2.8.2"
    val lifecycle = "1.1.1"
    val multidex = "1.0.3"
    val rxkotlin = "2.3.0"
    val crashlytics = "2.9.1"
    val checklistview = "1.0.2"
    val navigation = "1.0.0-alpha01"
    val material = "1.0.0"
}

object Deps {
    val material = "com.google.android.material:material:${Versions.material}"
    val navigation_fragment = "android.arch.navigation:navigation-fragment:${Versions.navigation}"
    val navigation_ui = "android.arch.navigation:navigation-ui:${Versions.navigation}"
    val support_appcomat_v7 = "com.android.support:appcompat-v7:${Versions.android_support}"
    val support_constraint = "com.android.support.constraint:constraint-layout:${Versions.constraint}"
    val support_recycler_view = "com.android.support:recyclerview-v7:${Versions.android_support}"
    val support_card_view = "com.android.support:cardview-v7:${Versions.android_support}"

    val firebase_ui_auth = "com.firebaseui:firebase-ui-auth:${Versions.firebase_ui_auth}"
    val firebase_core = "com.google.firebase:firebase-core:${Versions.firebase_core}"
    val firebase_firestore = "com.google.firebase:firebase-firestore:${Versions.firebase_firestore}"
    val firebase_storage = "com.google.firebase:firebase-storage:${Versions.firebase_storage}"
    val crashlytics = "com.crashlytics.sdk.android:crashlytics:${Versions.crashlytics}"

    val timber = "com.jakewharton.timber:timber:${Versions.timber}"

    val test_junit = "junit:junit:${Versions.junit}"
    val test_runner = "com.android.support.test:runner:${Versions.runner}"
    val kotlin_stdlib = "org.jetbrains.kotlin:kotlin-stdlib-jdk8:${Versions.kotlin}"

    val threetenabp = "com.jakewharton.threetenabp:threetenabp:${Versions.threetenabp}"

    val rxjava = "io.reactivex.rxjava2:rxjava:${Versions.rxjava}"
    val rxandroid = "io.reactivex.rxjava2:rxandroid:${Versions.rxandroid}"
    val rxkotlin = "io.reactivex.rxjava2:rxkotlin:${Versions.rxkotlin}"

    val dagger = "com.google.dagger:dagger:${Versions.dagger}"
    val dagger_compiler = "com.google.dagger:dagger-compiler:${Versions.dagger}"
    val dagger_android = "com.google.dagger:dagger-android:${Versions.dagger}"
    val dagger_android_support = "com.google.dagger:dagger-android-support:${Versions.dagger}"
    val dagger_android_compiler = "com.google.dagger:dagger-android-processor:${Versions.dagger}"

    val room = "android.arch.persistence.room:runtime:${Versions.room}"
    val room_compiler = "android.arch.persistence.room:compiler:${Versions.room}"
    val room_test = "android.arch.persistence.room:testing:${Versions.room}"
    val room_rxjava2 = "android.arch.persistence.room:rxjava2:${Versions.room}"
    val arch_core_testing = "android.arch.core:core-testing:${Versions.room}"

    val lifecycle = "android.arch.lifecycle:extensions:${Versions.lifecycle}"
    val lifecycle_compiler = "android.arch.lifecycle:compiler:${Versions.lifecycle}"

    val gson = "com.google.code.gson:gson:${Versions.gson}"

    val multidex = "com.android.support:multidex:${Versions.multidex}"
    val checklistview = "com.gavincode:checklistview:${Versions.checklistview}"

    val espresso_core = "com.android.support.test.espresso:espresso-core:${Versions.espresso}"
    val espresso_contrib = "com.android.support.test.espresso:espresso-contrib:${Versions.espresso}"
    val espresso_intents = "com.android.support.test.espresso:espresso-intents:${Versions.espresso}"
}