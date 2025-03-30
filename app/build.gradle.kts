plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.compose.compiler)
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
        vectorDrawables {
            useSupportLibrary = true // Recommended for vector drawables
        }
    }

    buildTypes {
        release {
            isMinifyEnabled = false // Keep as false for now, enable for release builds later
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
        viewBinding = false
        compose = true
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}" // Common excludes for Compose
        }
    }
}

dependencies {
    // Core AndroidX
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat) // Keep for base Activity theme if needed, or potentially remove later
    implementation(libs.androidx.lifecycle.runtime.ktx) // Base lifecycle

    // Jetpack Compose BOM (Bill of Materials) - Manages Compose library versions
    implementation(platform(libs.compose.bom)) // Correctly using the BOM alias
    implementation(libs.androidx.compose.ui)
    implementation(libs.androidx.compose.ui.graphics)
    implementation(libs.androidx.compose.ui.tooling.preview)
    implementation(libs.androidx.compose.material3) // Material Design 3 components

    // Compose Integration
    implementation(libs.material)
    implementation(libs.androidx.activity.compose) // For setContent in Activity
    implementation(libs.androidx.lifecycle.viewmodel.compose) // For viewModel() composable
    implementation(libs.androidx.navigation.compose) // For Compose Navigation
    implementation(libs.androidx.lifecycle.runtime.compose) // For collectAsStateWithLifecycle

    // ViewModels (Keep if you use them, which is recommended)
    implementation(libs.androidx.lifecycle.livedata.ktx) // If using LiveData
    implementation(libs.androidx.lifecycle.viewmodel.ktx) // Base ViewModel KTX

    // Material Components (Legacy - Can likely be removed if fully Compose)
    // implementation(libs.material) // Remove this if not using any Material View components

    // Testing
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.compose.bom)) // Add BOM for Compose testing
    androidTestImplementation(libs.androidx.compose.ui.test.junit4) // Compose UI Tests
    debugImplementation(libs.androidx.compose.ui.tooling) // UI Tooling (Preview, Inspector)
    debugImplementation(libs.androidx.compose.ui.test.manifest) // Test Manifest

// Remove legacy navigation and constraint layout if no longer used
// implementation(libs.androidx.constraintlayout)
// implementation(libs.androidx.navigation.fragment.ktx)
// implementation(libs.androidx.navigation.ui.ktx)
}