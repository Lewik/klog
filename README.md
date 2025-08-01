# klog

[![](https://jitpack.io/v/lewik/klog.svg)](https://jitpack.io/#lewik/klog)
[![CI](https://github.com/lewik/klog/actions/workflows/ci.yml/badge.svg)](https://github.com/lewik/klog/actions/workflows/ci.yml)
[![Coverage](https://github.com/lewik/klog/actions/workflows/coverage.yml/badge.svg)](https://github.com/lewik/klog/actions/workflows/coverage.yml)

A minimalistic Kotlin Multiplatform logging library providing unified API across JVM, JavaScript, and Native platforms (planned).

**Philosophy:** Simple, clean logging API that just works. No complex configuration, no enterprise bloat.

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

This project is actively maintained and developed with assistance from:
- [Claude Code](https://claude.ai/code) - AI coding assistant by Anthropic
- Automated testing, code improvements, and release management

## Inspired by
- code at [https://github.com/shafirov/klogging] 
- code at [https://github.com/MicroUtils/kotlin-logging] 
- and discussion at [http://stackoverflow.com/questions/34416869/idiomatic-way-of-logging-in-kotlin]
