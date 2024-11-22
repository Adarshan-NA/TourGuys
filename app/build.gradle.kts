plugins {
    id("com.android.application")
}

android {
    namespace = "com.wlu.tourguys.project"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.wlu.tourguys.project"
        minSdk = 26
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
    // AndroidX dependencies
    implementation(libs.androidx.appcompat.v161)
    implementation(libs.androidx.core.ktx.v1120)
    implementation(libs.androidx.constraintlayout.v214)
    implementation(libs.androidx.activity.ktx)
    implementation(libs.androidx.cardview.v100)

    // CameraX dependencies
    implementation(libs.androidx.camera.core)
    implementation(libs.androidx.camera.camera2)
    implementation(libs.androidx.camera.lifecycle)
    implementation(libs.androidx.camera.view)

    // Material Components
    implementation(libs.material.v1100)

    // Google Maps and Places API
    implementation(libs.play.services.maps)
    implementation(libs.places)

    // Networking and image loading
    implementation(libs.retrofit)
    implementation(libs.converter.gson)
    implementation(libs.picasso)
    implementation(libs.logging.interceptor)

    // ML Kit Face Detection API
    implementation(libs.face.detection.v1606)

    // Testing
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit.v115)
    androidTestImplementation(libs.androidx.espresso.core.v351)
}
