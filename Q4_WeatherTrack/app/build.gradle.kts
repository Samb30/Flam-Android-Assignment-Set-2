plugins {
    alias(libs.plugins.android.application)
}

android {
    namespace = "com.example.weathertrack"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.example.weathertrack"
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
    implementation(libs.swiperefreshlayout)
    implementation(libs.lifecycle.viewmodel.android)
    implementation(libs.filament.android)
    testImplementation(libs.junit)
    androidTestImplementation(libs.ext.junit)
    androidTestImplementation(libs.espresso.core)
    // Room
    implementation (libs.room.runtime)
    annotationProcessor(libs.room.compiler)
    implementation (libs.room.ktx)
    // ViewModel and LiveData
    implementation (libs.lifecycle.viewmodel)
    implementation (libs.lifecycle.livedata)
    // WorkManager
    implementation (libs.work.runtime)
    // RecyclerView and CardView
    implementation (libs.recyclerview)
    implementation (libs.cardview)
}