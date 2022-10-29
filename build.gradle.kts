plugins {
    kotlin("multiplatform") version "1.7.20"
    `maven-publish`
}

repositories {
    mavenCentral()
}

group = "com.github.lewik"
version = "2.0.5"

kotlin {

    jvm()
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
                implementation("org.slf4j:slf4j-api:1.7.36")
            }
        }
        val jsMain by getting {
            dependencies {
            }
        }
    }
}
