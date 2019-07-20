// Top-level build file where you can add configuration options common to all sub-projects/modules.

buildscript {
//    ext.kotlin_version = "1.2.30"

    repositories {
        google()
        jcenter()
        maven (
            url = "https://maven.fabric.io/public"
        )
    }
    dependencies {
        classpath("com.android.tools.build:gradle:3.4.2")
        classpath("android.arch.navigation:navigation-safe-args-gradle-plugin:1.0.0")

        classpath("com.google.gms:google-services:4.3.0")
        classpath("org.jetbrains.kotlin:kotlin-gradle-plugin:${Versions.kotlin}")
        // NOTE: Do not place your application dependencies here; they belong
        // in the individual module build.gradle files
        classpath ("io.fabric.tools:gradle:1.30.0")
    }
}

allprojects {
    repositories {
        google()
        jcenter()
    }
}

tasks {
    val clean by registering(Delete::class) {
        delete(buildDir)
    }
}
