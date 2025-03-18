plugins {
    alias(libs.plugins.android.application)
}

android {
    namespace = "com.example.movilproyectofinal"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.example.movilproyectofinal"
        minSdk = 33
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
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    buildFeatures {
        viewBinding = true;
        buildConfig = true;
    }


    dependencies {

        implementation(libs.appcompat)
        implementation(libs.material)
        implementation(libs.activity)
        implementation(libs.constraintlayout)
        testImplementation(libs.junit)
        androidTestImplementation(libs.ext.junit)
        androidTestImplementation(libs.espresso.core)


        implementation ("com.google.android.material:material:1.11.0")//BottomNavigationView
        implementation ("com.google.android.material:material:1.9.0") //para usar FloatingActionButton
        implementation ("androidx.lifecycle:lifecycle-viewmodel:2.8.7")  // Verifica la última versión
        implementation ("androidx.lifecycle:lifecycle-livedata:2.8.7")
        implementation ("com.google.android.material:material:1.10.0")
        // Parse SDK

        // picasso para manejar imágenes
        implementation ("com.squareup.picasso:picasso:2.71828")

        implementation("com.squareup.picasso:picasso:2.71828")
        implementation("de.hdodenhof:circleimageview:3.1.0")

        implementation("androidx.cardview:cardview:1.0.0") // Usar CardView de AndroidX
        implementation("androidx.recyclerview:recyclerview:1.2.1") // Usar RecyclerView de AndroidX

        implementation("com.github.parse-community.Parse-SDK-Android:parse:4.3.0")
        implementation("com.github.parse-community.Parse-SDK-Android:bolts-tasks:4.3.0")


        //circle image view
        implementation ("de.hdodenhof:circleimageview:3.1.0")
        implementation ("org.imaginativeworld.whynotimagecarousel:whynotimagecarousel:2.1.0")

        implementation ("androidx.appcompat:appcompat:1.6.1")//appcompat


        //Glide
        implementation ("com.github.bumptech.glide:glide:4.12.0")
        annotationProcessor("com.github.bumptech.glide:compiler:4.12.0")
        testImplementation("junit:junit:4.13.2")
        androidTestImplementation("androidx.test.ext:junit:1.1.5")
        androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")

    }
}
dependencies {
    implementation(libs.appcompat)
    implementation(libs.material)
    implementation(libs.activity)
    implementation(libs.constraintlayout)
    implementation(libs.lifecycle.livedata.ktx)
    implementation(libs.lifecycle.viewmodel.ktx)
    implementation(libs.navigation.fragment)
    implementation(libs.navigation.ui)
}
