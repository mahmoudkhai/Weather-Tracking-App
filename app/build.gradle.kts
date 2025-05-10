plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
}

android {
    namespace = "com.example.weathertrackingapp"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.example.weathertrackingapp"
        minSdk = 26
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

}

dependencies {

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.activity)
    implementation(libs.androidx.constraintlayout)
    implementation(libs.play.services.location)
//    testImplementation(libs.junit)
//    androidTestImplementation(libs.androidx.junit)
//    androidTestImplementation(libs.androidx.espresso.core)
    testImplementation("org.junit.jupiter:junit-jupiter-api:5.10.0")
//
//    // JUnit 5 engine (run tests)
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:5.10.0")
//
//    // (Optional) Parameterized tests
//    testImplementation("org.junit.jupiter:junit-jupiter-params:5.10.0")
//
//    // (Optional) Kotlin test helpers (works with JUnit 5)
//    testImplementation("org.jetbrains.kotlin:kotlin-test:1.9.23")

    testImplementation("org.junit.jupiter:junit-jupiter:5.8.1")
    testImplementation("org.junit.jupiter:junit-jupiter:5.8.1")
    testImplementation(kotlin("test"))

}
tasks.withType<Test> {
    useJUnitPlatform()
}
