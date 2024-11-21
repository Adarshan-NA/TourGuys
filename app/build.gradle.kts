plugins {
    alias(libs.plugins.android.application)
}

android {
    namespace = "com.wlu.tourguys.project"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.wlu.tourguys.project"
        minSdk = 24
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
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
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
}

dependencies {
    implementation(libs.appcompat)
    implementation(libs.material)
    implementation(libs.activity)
    implementation(libs.constraintlayout)
    implementation(libs.camera.view)
    // CameraX dependencies with explicit versions
    implementation(libs.androidx.camera.core)
    implementation(libs.camerax.camera2)
    implementation(libs.camerax.lifecycle)
    implementation(libs.camera.view) // Ensure this dependency is included

    testImplementation(libs.junit)
    androidTestImplementation(libs.ext.junit)
    androidTestImplementation(libs.espresso.core)

    // ML Kit Face Detection API
    implementation(libs.mlkit.facedetection)
    androidTestImplementation(libs.rules)
}