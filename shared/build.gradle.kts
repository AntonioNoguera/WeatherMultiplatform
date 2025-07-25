import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import org.jetbrains.kotlin.gradle.ExperimentalKotlinGradlePluginApi

plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.androidLibrary)

    id("org.jetbrains.kotlin.plugin.serialization") version "1.9.21"
    id("co.touchlab.skie") version "0.9.0"
}



kotlin {

    // Para suprimir warnings de expect/actual
    @OptIn(ExperimentalKotlinGradlePluginApi::class)
    compilerOptions {
        freeCompilerArgs.add("-Xexpected-actual-classes")
    }

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
            isStatic = false
            export("co.touchlab:kermit:2.0.3")
        }
    }

    sourceSets {

        all {
            languageSettings.apply {
                optIn("kotlin.RequiresOptIn")
                optIn("kotlinx.coroutines.ExperimentalCoroutinesApi")
            }
        }

        commonMain.dependencies {
            // Networking
            implementation("io.ktor:ktor-client-core:2.3.7")
            implementation("io.ktor:ktor-client-content-negotiation:2.3.7")
            implementation("io.ktor:ktor-serialization-kotlinx-json:2.3.7")

            // Coroutines
            implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.7.3")

            // Serialization
            implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.6.2")

            // DI
            implementation("io.insert-koin:koin-core:3.5.6")

            // SKIE annotations
            implementation("co.touchlab.skie:configuration-annotations:0.10.1")
 }

        androidMain.dependencies {
            // Networking Android
            implementation("io.ktor:ktor-client-android:2.3.7")
            implementation("io.ktor:ktor-client-okhttp:2.3.7")
            implementation("io.insert-koin:koin-android:3.5.6")

            // AndroidX ViewModel para heredar
            implementation("androidx.lifecycle:lifecycle-viewmodel-ktx:2.7.0")
        }

        iosMain.dependencies {
            implementation("io.ktor:ktor-client-darwin:2.3.7")

            // NUEVAS DEPENDENCIAS iOS
            api("co.touchlab:kermit-simple:2.0.3")

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
