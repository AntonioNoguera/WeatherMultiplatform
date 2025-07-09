import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.androidLibrary)

    id("org.jetbrains.kotlin.plugin.serialization") version "1.9.21"
}

kotlin {
    androidTarget {
        compilations.all {
            compileTaskProvider.configure {
                compilerOptions {
                    jvmTarget.set(JvmTarget.JVM_1_8)
                }
            }
        }
    }
    
    listOf(
        iosX64(),
        iosArm64(),
        iosSimulatorArm64()
    ).forEach {
        it.binaries.framework {
            baseName = "shared"
            isStatic = true
        }
    }

    sourceSets {
        commonMain.dependencies {
            // Networking
            implementation("io.ktor:ktor-client-core:2.3.7")
            implementation("io.ktor:ktor-client-content-negotiation:2.3.7")
            implementation("io.ktor:ktor-serialization-kotlinx-json:2.3.7")

            // Coroutines
            implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.7.3")

            // Serialization
            implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.6.2")

            //DI
            implementation("io.insert-koin:koin-core:3.5.3")
        }

        androidMain.dependencies {
            implementation("io.ktor:ktor-client-android:2.3.7")
            implementation("io.insert-koin:koin-android:3.5.3")
        }

        iosMain.dependencies {
            implementation("io.ktor:ktor-client-darwin:2.3.7")
        }

        commonTest.dependencies {
            implementation(libs.kotlin.test)
            implementation("org.jetbrains.kotlinx:kotlinx-coroutines-test:1.7.3")
            implementation("io.insert-koin:koin-test:3.5.3")
        }
    }
}

android {
    namespace = "com.weather.app"
    compileSdk = 35
    defaultConfig {
        minSdk = 24
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
}
