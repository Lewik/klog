import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
    alias(libs.plugins.kotlin.multiplatform)
    alias(libs.plugins.kover)
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
    
    macosX64()
    macosArm64()

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

kover {
    reports {
        filters {
            excludes {
                classes("**/*Test*", "**/*\$\$serializer*")
            }
        }
        
        total {
            xml {
                onCheck = true
            }
            html {
                onCheck = true
            }
        }
    }
    
}

tasks.register("printCoverage") {
    dependsOn("koverXmlReport")
    doLast {
        val report = file("${layout.buildDirectory.get()}/reports/kover/report.xml")
        if (report.exists()) {
            val doc = javax.xml.parsers.DocumentBuilderFactory.newInstance()
                .newDocumentBuilder()
                .parse(report)
            val rootElement = doc.documentElement
            val childNodes = rootElement.childNodes

            for (i in 0 until childNodes.length) {
                val node = childNodes.item(i)
                if (node.nodeName == "counter" && 
                    node.attributes.getNamedItem("type").textContent == "LINE") {
                    val missed = node.attributes.getNamedItem("missed").textContent.toInt()
                    val covered = node.attributes.getNamedItem("covered").textContent.toInt()
                    val percentage = if (missed + covered > 0) {
                        (covered.toDouble() / (missed + covered) * 100).toInt()
                    } else 0
                    println("$percentage")
                    break
                }
            }
        } else {
            println("0")
        }
    }
}
