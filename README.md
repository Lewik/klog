# klog

[![](https://jitpack.io/v/lewik/klog.svg)](https://jitpack.io/#lewik/klog)
[![CI](https://github.com/lewik/klog/actions/workflows/build.yml/badge.svg)](https://github.com/lewik/klog/actions/workflows/build.yml)
[![Coverage](https://img.shields.io/endpoint?url=https://gist.githubusercontent.com/lewik/b519fc518e28671da0089ae4f911d699/raw/klog-coverage.json)](https://github.com/lewik/klog/actions/workflows/coverage.yml)
![Platform](https://img.shields.io/badge/platform-jvm%20%7C%20js%20%7C%20macosX64%20%7C%20macosArm64-orange?logo=kotlin)
![Kotlin](https://img.shields.io/badge/kotlin-2.2.0-blue?logo=kotlin)
[![NASA-not affiliated](https://img.shields.io/badge/NASA-not%20affiliated-blue?logo=nasa)](STARTUP.md)
![Built with Claude](https://img.shields.io/badge/Built%20with-Claude-5436DA?style=flat&logo=anthropic&logoColor=white)

A minimalistic Kotlin Multiplatform logging library providing unified API across JVM, JavaScript, and Native platforms.

**Philosophy:** klog is a "dumb box" that just works. Zero dependencies, zero configuration, zero complexity. Out of the box it simply uses `println()` on all platforms with lambda support for performance. You can easily swap the output mechanism if needed, but the defaults are intentionally simple and fast.

## Supported Platforms

| Platform | Implementation | Output |
|----------|---------------|---------|
| **JVM** | println() | Standard output (simple and fast) |
| **JavaScript** | Console API | Browser console / Node.js console |
| **Native** | println() | Standard output (macOS, Linux, Windows) |

**The "dumb box" approach:**
- No external dependencies (not even SLF4J!)
- No configuration files or complex setup
- Just `println()` by default - works everywhere
- Lazy lambda evaluation for performance: `log.debug { "expensive $calculation" }`
- Easy customization through simple writer interface when needed

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
import klog.klog

class MyClass {
    private val log = klog()  // Simple extension function
    
    fun doWork() {
        log.info("Starting work")
        log.debug { "Lazy evaluation: expensive calculation only if debug enabled" }
        
        try {
            // ... some work
        } catch (e: Exception) {
            log.error(e, "Work failed")
        }
    }
}
```

### All logging methods:
```kotlin
import klog.klog

val log = klog()

// Basic logging
log.trace("trace message")
log.debug("debug message") 
log.info("info message")
log.warn("warning message")
log.error("error message")

// Lazy evaluation (only computed if level enabled)
log.debug { "Expensive calculation: ${expensiveFunction()}" }

// With exceptions
log.error(exception, "Something went wrong")
log.error(exception) { "Error in ${getCurrentContext()}" }

// Shorthand (defaults to info level)
log("Quick info message")
```

### Alternative ways to create loggers:
```kotlin
import klog.klog
import klog.KLoggers
import klog.WithLogging
import klog.KLoggerHolder

// Extension function (recommended)
class MyClass {
    private val log = klog()
}

// Explicit factory call
class MyClass {
    private val log = KLoggers.logger(this)
}

// Delegation pattern
class MyClass : WithLogging by KLoggerHolder() {
    fun work() { 
        log.info("Using delegated logger") 
    }
}

// String-based logger name
val log = KLoggers.logger("CustomName") 

```

## Customization

The "dumb box" philosophy means klog just works out of the box with `println()`, but you can easily customize the output when needed:

```kotlin
import klog.KLoggerRegistry
import klog.LogWriter

// Lambda syntax (simple cases)
KLoggerRegistry.currentWriter = LogWriter { level, tag, message, throwable ->
    writeToFile("$level [$tag]: $message")
    if (throwable != null) {
        writeToFile(throwable.stackTraceToString())
    }
}

// Class syntax (complex cases)
class FileLogWriter(private val file: File) : LogWriter {
    override fun write(level: LogLevel, tag: String, message: String, throwable: Throwable?) {
        file.appendText("$level [$tag]: $message\n")
        throwable?.printStackTrace()
    }
}

KLoggerRegistry.currentWriter = FileLogWriter(File("app.log"))
```

**Why this works:**
- Default behavior: simple `println()` - no setup needed
- Custom behavior: swap the writer when you need something different  
- No complex configuration - just a single function to override


## Contributors

This project is actively developed with assistance from [Claude Code](https://claude.ai/code) AI assistant.

## Inspired by
- code at [https://github.com/shafirov/klogging] 
- code at [https://github.com/MicroUtils/kotlin-logging] 
- and discussion at [http://stackoverflow.com/questions/34416869/idiomatic-way-of-logging-in-kotlin]
