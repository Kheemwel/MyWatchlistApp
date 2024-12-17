// Top-level build file where you can add configuration options common to all sub-projects/modules.
plugins {
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.jetbrains.kotlin.android) apply false
    // Serialization
    id("org.jetbrains.kotlin.plugin.serialization") version "1.9.0" apply false
    // Hilt
    id("com.google.devtools.ksp") version "1.9.0-1.0.12" apply false
    id("com.google.dagger.hilt.android") version "2.47" apply false
    // DataStore Proto
    id("com.google.protobuf") version "0.8.12" apply false
}

// DataStore Proto
buildscript{
    dependencies {
        classpath("com.google.protobuf:protobuf-gradle-plugin:0.9.4")
    }
}