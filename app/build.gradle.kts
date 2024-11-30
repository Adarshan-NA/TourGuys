plugins {
    id("com.android.application")
    id("com.google.gms.google-services")
}

android {
    namespace = "com.wlu.tourguys.project"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.wlu.tourguys.project"
        minSdk = 28
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

    // Firebase dependencies
    implementation(platform(libs.firebase.bom))
    implementation(platform(libs.firebase.bom.v3200))
    implementation(libs.firebase.analytics)
    implementation(libs.firebase.database.ktx)
    implementation(libs.firebase.auth)
    implementation(libs.firebase.database)
    implementation(libs.firebase.firestore)
    implementation(libs.google.firebase.auth)


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
    implementation(libs.firebase.firestore)
    implementation(libs.google.firebase.auth)

    // Testing dependencies for unit tests
    testImplementation(libs.junit)
    testImplementation(libs.mockito.core)
    testImplementation(libs.mockito.inline)
    testImplementation(libs.androidx.core.testing)
    testImplementation(libs.byte.buddy)
    testImplementation(libs.robolectric)
    testImplementation(libs.androidx.core.v150)


    // AndroidX testing dependencies for instrumentation tests
    androidTestImplementation(libs.androidx.junit.v115)
    androidTestImplementation(libs.androidx.espresso.core.v351)
    androidTestImplementation(libs.androidx.core)
    androidTestImplementation(libs.androidx.junit.v121)
    androidTestImplementation(libs.androidx.rules)
    androidTestImplementation(libs.androidx.runner)
    androidTestImplementation(libs.mockito.android)
    androidTestImplementation(libs.androidx.core.testing.v210)
}
}
