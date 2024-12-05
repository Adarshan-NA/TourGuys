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
    //        sourceCompatibility = JavaVersion.VERSION_1_8
    //        targetCompatibility = JavaVersion.VERSION_1_8
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }

    testOptions {
        unitTests {
            isIncludeAndroidResources = true

        }
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
    implementation(libs.google.firebase.storage)

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

    // Glide for image loading
    implementation(libs.glide)

    // ML Kit Face Detection API
    implementation(libs.face.detection.v1606)
    implementation(libs.firebase.firestore)
    implementation(libs.google.firebase.auth)

    // Testing dependencies for unit tests
    testImplementation(libs.junit.v413)
    testImplementation(libs.core.testing)
    testImplementation(libs.mockito.core.v400)
    testImplementation(libs.mockito.inline.v400)
    testImplementation(libs.robolectric.v461)
    testImplementation(libs.core.testing)
    testImplementation(libs.google.firebase.firestore)
    testImplementation(libs.firebase.auth.v2103)

    // Hamcrest Matchers
    testImplementation(libs.hamcrest.library)

    // AndroidX testing dependencies for instrumentation tests
    androidTestImplementation(libs.androidx.espresso.core.v361)
    androidTestImplementation(libs.androidx.junit.v121)
    androidTestImplementation(libs.rules)
    androidTestImplementation(libs.runner)
    androidTestImplementation(libs.mockito.android.v400)
    androidTestImplementation(libs.androidx.rules.v150)
    androidTestImplementation(libs.androidx.core)
    androidTestImplementation(libs.androidx.espresso.core.v361)
    androidTestImplementation(libs.androidx.espresso.intents)
    androidTestImplementation(libs.androidx.espresso.contrib)
    androidTestImplementation(libs.androidx.rules)
    androidTestImplementation(libs.androidx.runner.v152)
    androidTestImplementation(libs.androidx.junit.v121)
    androidTestImplementation(libs.androidx.espresso.core.v361)
    androidTestImplementation(libs.androidx.espresso.intents.v361)
    androidTestImplementation(libs.androidx.espresso.contrib.v361)

    // Optional: Firebase test dependencies (if using Firebase in tests)
    androidTestImplementation(libs.firebase.firestore.v2430)
    androidTestImplementation(libs.google.firebase.auth)
}