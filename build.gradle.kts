import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
    alias(libs.plugins.kotlin.multiplatform)
    `maven-publish`
}

repositories {
    mavenCentral()
}

group = "com.github.lewik"
version = "2.1.0"

kotlin {
    jvm {
        compilerOptions {
            jvmTarget.set(JvmTarget.JVM_1_8)
        }
    }
    js(IR) {
        nodejs()
    }

    sourceSets {
        val commonMain by getting {
            dependencies {
            }
        }
        val commonTest by getting {
            dependencies {
                implementation(libs.kotlin.test)
            }
        }
        val jvmMain by getting {
            dependencies {
                implementation(libs.slf4j.api)
            }
        }
        val jvmTest by getting {
            dependencies {
                implementation(libs.logback.classic)
            }
        }
        val jsMain by getting {
            dependencies {
            }
        }
    }
}
