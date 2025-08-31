plugins {
    id("com.android.application")
}

android {
    namespace = "io.github.dot166.soundstream"
    compileSdk = 36

    defaultConfig {
        applicationId = "io.github.dot166.soundstream"
        minSdk = 26
        targetSdk = 36
        versionCode = 1
        versionName = "1.0"
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
}

dependencies {
    implementation("io.github.dot166:j-Lib:91")
}