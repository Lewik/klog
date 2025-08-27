import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
    kotlin("multiplatform") version "2.2.0"
    `maven-publish`
}

repositories {
    mavenCentral()
}

group = "com.github.lewik"
version = "2.1.1"

kotlin {
    jvm {
        compilerOptions {
            jvmTarget.set(JvmTarget.JVM_1_8)
        }
    }
    js(IR) {
        browser()
    }

    sourceSets {
        val commonMain by getting {
            dependencies {
            }
        }
        val jvmMain by getting {
            dependencies {
                implementation("org.slf4j:slf4j-api:2.0.17")
            }
        }
        val jsMain by getting {
            dependencies {
            }
        }
    }
}
