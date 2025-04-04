plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
}

android {
    namespace = "com.trailblazers.digitalecohome"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.trailblazers.digitalecohome"
        minSdk = 33
        targetSdk = 35
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
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    kotlinOptions {
        jvmTarget = "11"
    }
    buildFeatures {
        viewBinding = true
    }
}

dependencies {

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.constraintlayout)
    implementation(libs.androidx.lifecycle.livedata.ktx)
    implementation(libs.androidx.lifecycle.viewmodel.ktx)
    implementation(libs.androidx.navigation.fragment.ktx)
    implementation(libs.androidx.navigation.ui.ktx)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
}


// build.gradle (Groovy DSL)
dependencies {
    // Coroutines
    implementation "org.jetbrains.kotlinx:kotlinx-coroutines-android:1.7.3" // Use the latest stable version

    // OkHttp (for HTTP requests)
    implementation "com.squareup.okhttp3:okhttp:4.11.0" // Use the latest stable version

    // Paho MQTT Android Service (for MQTT)
    implementation 'org.eclipse.paho:org.eclipse.paho.client.mqttv3:1.2.5'
    implementation 'org.eclipse.paho:org.eclipse.paho.android.service:1.1.1'

    // Required if using Paho with Android API 26+
    implementation 'androidx.localbroadcastmanager:localbroadcastmanager:1.1.0'

    // Other standard dependencies (likely already there)
    implementation 'androidx.appcompat:appcompat:...' // Use your existing version
    // ... other dependencies
}

// build.gradle.kts (Kotlin DSL)
dependencies {
    // Coroutines
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.7.3") // Use the latest stable version

    // OkHttp (for HTTP requests)
    implementation("com.squareup.okhttp3:okhttp:4.11.0") // Use the latest stable version

    // Paho MQTT Android Service (for MQTT)
    implementation("org.eclipse.paho:org.eclipse.paho.client.mqttv3:1.2.5")
    implementation("org.eclipse.paho:org.eclipse.paho.android.service:1.1.1")

    // Required if using Paho with Android API 26+
    implementation("androidx.localbroadcastmanager:localbroadcastmanager:1.1.0")

    // Other standard dependencies (likely already there)
    implementation("androidx.appcompat:appcompat:...") // Use your existing version
    // ... other dependencies
}