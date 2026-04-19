plugins {
    id("com.android.library")
    id("org.jetbrains.kotlin.android")
    id("maven-publish")
}


apply(
    "flatbuffers.gradle"
)

configure<com.android.build.api.dsl.LibraryExtension> {
    namespace = "com.mgg.flatbuffers"
    compileSdk = 37

    defaultConfig {
        minSdk = 26

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        consumerProguardFiles("consumer-rules.pro")
        externalNativeBuild {
            cmake {
                arguments += "-DANDROID_STL=c++_shared"
                cppFlags("-std=c++17")
            }
        }
    }

    println("project.path:" + project.rootDir)

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    externalNativeBuild {
        cmake {
            path("src/main/cpp/CMakeLists.txt")
            version = "3.22.1"
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }

    // Enable generation of Prefab packages and include them in the library's AAR.
    buildFeatures {
        prefab = true
        prefabPublishing = true
        buildConfig = true
    }

    prefab {
        create("flatbuffers") {
            headers = project.rootDir.path + "/third_party/flatbuffers/include"
            name = "flatbuffers"
        }
    }

    publishing {
        multipleVariants {
            withSourcesJar()
            includeBuildTypeValues("debug", "release")
        }
    }
}

kotlin {
    jvmToolchain(17)
    explicitApi()
}

apply(
    "publish.gradle"
)

dependencies {

    implementation("androidx.core:core-ktx:1.15.0")
    implementation("androidx.appcompat:appcompat:1.7.0")
    implementation("com.google.android.material:material:1.12.0")
    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.2.1")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.6.1")
}
