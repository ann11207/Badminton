plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.google.gms.google.services)
}

android {
    namespace = "com.example.badminton"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.example.badminton"
        minSdk = 24
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    buildFeatures {
        viewBinding = true
    }
}

dependencies {

    implementation(libs.appcompat)
    implementation(libs.material)
    implementation(libs.activity)
    implementation(libs.constraintlayout)
    implementation(libs.firebase.auth)
    implementation(libs.firebase.firestore)
    implementation(libs.firebase.storage)
    implementation(libs.firebase.database)
    implementation(libs.navigation.fragment)
    implementation(libs.navigation.ui)
    testImplementation(libs.junit)
    androidTestImplementation(libs.ext.junit)
    androidTestImplementation(libs.espresso.core)


    // thêm thư viện lottie
    implementation ("com.airbnb.android:lottie:6.4.0")
    // thêm thư viện mã hoá mật khẩu
    implementation ("org.mindrot:jbcrypt:0.4")

    implementation ("androidx.recyclerview:recyclerview:1.2.1")

    implementation("androidx.gridlayout:gridlayout:1.0.0")

    implementation("androidx.cardview:cardview:1.0.0")
    implementation( "com.google.firebase:firebase-auth:21.1.0")
    implementation ("com.google.firebase:firebase-firestore:24.3.0")

    implementation ("com.github.bumptech.glide:glide:4.12.0")
    annotationProcessor ("com.github.bumptech.glide:compiler:4.12.0")

    implementation ("de.hdodenhof:circleimageview:3.1.0")

    implementation("com.github.AnyChart:AnyChart-Android:0.0.3")
    implementation ("com.github.AnyChart:AnyChart-Android:1.1.5")
    implementation ("com.github.PhilJay:MPAndroidChart:v3.1.0")







}