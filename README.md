# klog

[![](https://jitpack.io/v/lewik/klog.svg)](https://jitpack.io/#lewik/klog)
[![CI](https://github.com/lewik/klog/actions/workflows/build.yml/badge.svg)](https://github.com/lewik/klog/actions/workflows/build.yml)
[![Coverage](https://img.shields.io/endpoint?url=https://gist.githubusercontent.com/lewik/b519fc518e28671da0089ae4f911d699/raw/klog-coverage.json)](https://github.com/lewik/klog/actions/workflows/coverage.yml)
![Platform](https://img.shields.io/badge/platform-jvm%20%7C%20js%20%7C%20macosX64%20%7C%20macosArm64-orange?logo=kotlin)
![Kotlin](https://img.shields.io/badge/kotlin-2.2.0-blue?logo=kotlin)
[![NASA-not affiliated](https://img.shields.io/badge/NASA-not%20affiliated-blue?logo=nasa)](STARTUP.md)
![Built with Claude](https://img.shields.io/badge/Built%20with-Claude-5436DA?style=flat&logo=anthropic&logoColor=white)

A minimalistic Kotlin Multiplatform logging library providing unified API across JVM, JavaScript, and Native platforms.

**Philosophy:** Simple, clean logging API that just works. No complex configuration, no enterprise bloat.

## Supported Platforms

| Platform | Implementation | Output |
|----------|---------------|---------|
| **JVM** | SLF4J adapter | Any SLF4J implementation (Logback, Log4j2, etc.) |
| **JavaScript** | Console API | Browser console / Node.js console |
| **Native** | println() | Standard output (macOS, Linux, Windows) |

**Native targets:**
- `macosX64` - Intel Mac
- `macosArm64` - Apple Silicon Mac  
- More platforms coming soon

## Java Version Support

klog supports **Java 8+** to ensure compatibility with the widest range of production environments. **29% of production applications still use Java 8** (New Relic 2024), representing a significant portion of the Java ecosystem that deserves first-class support.

**Tested on:** Java 8, 11, 17, 21 with comprehensive CI/CD pipeline.
                                      
                                      
## Download
Use https://jitpack.io repository
```
	allprojects {
		repositories {
			...
			maven { url 'https://jitpack.io' }
		}
	}
```

```gradle
implementation 'com.github.lewik.klog:klog-metadata:2.1.0'
```

## Usage                                              
```kotlin
class Foo {
    val log = KLoggers.logger(this)
    
    fun test() {
        log("This string will be evaluated regardless if trace enabled = ${log.isTraceEnabled}")
        log {"This string will not be evaluated unless trace enabled = ${log.isTraceEnabled}"}
    
        log("debug level")
        log { "debug level" }
        
        log.trace("trace")
        log.debug("debug")
        log.info("info")
        log.warn("warn")
        log.debug("error")
        
        log.trace { "trace" }
        log.debug { "debug" }
        log.info { "info" }
        log.warn { "warn" }
        log.debug { "error" }
    }
}
```

Another ways to obtain logger:
```kotlin
class Bar {
    private val log = KLoggers.logger(this)
    
    fun test() { log("Have some logging!") }
}

class Baz : WithLogging by KLoggerHolder() {
    fun test() { log("Have some logging!") }
}
 
class Qux {//*
    companion object: WithLogging by KLoggerHolder() 
    
    fun test() { log("Have some logging!") }
} 

```

\* Qux: https://en.wikipedia.org/wiki/Metasyntactic_variable


## Contributors

This project is actively developed with assistance from [Claude Code](https://claude.ai/code) AI assistant.

## Inspired by
- code at [https://github.com/shafirov/klogging] 
- code at [https://github.com/MicroUtils/kotlin-logging] 
- and discussion at [http://stackoverflow.com/questions/34416869/idiomatic-way-of-logging-in-kotlin]
