# klog - Kotlin Multiplatform Logging Library

## Project Overview
klog is a Kotlin Multiplatform logging library providing unified API across JVM, JavaScript, and Native platforms (planned).

**Core Philosophy:** The MAIN IDEA of this project is to be minimalistic and simple. No complex configuration, no bloated features, no enterprise nonsense - just clean logging API that works. This is the fundamental principle that drives all design decisions.

**Key Features:**
- Unified logging API for all platforms
- Lazy message evaluation via lambdas
- Zero-cost abstractions with inline functions
- Convenient operators (invoke as info)
- Delegation pattern for easy usage

## Technical Stack
- **Language:** Kotlin 1.7.20
- **Build:** Gradle with Kotlin DSL
- **Platforms:** JVM, JS (IR compilation)
- **Publishing:** Maven publish + JitPack

## Architecture

### Module Structure
```
klog/
├── src/
│   ├── commonMain/    # Shared API and interfaces
│   ├── jvmMain/       # JVM-specific implementation (SLF4J adapter)
│   └── jsMain/        # JS-specific implementation (console API)
```

### Core Components

**Common Module:**
- `BaseLogger` - Core logging interface with trace/debug/info/warn/error methods
- `KLogger` - Wrapper with inline functions for lazy evaluation
- `KLoggers` - Factory object (expect declaration)
- `WithLogging`/`KLoggerHolder` - Delegation patterns for easy logger access

**Platform Implementations:**
- **JVM:** Delegates to SLF4J (de-facto Java standard)
- **JS:** Uses browser console API with configurable log levels

## API Design

### Basic Usage
```kotlin
// Get logger
val log = KLoggers.logger(this)
val log = KLoggers.logger("MyLogger")

// Log messages
log.info("Simple message")
log.info { "Lazy message - only evaluated if info is enabled" }

// With exceptions
log.error("Error occurred", exception)
log.error(exception) { "Error: ${exception.message}" }

// Convenient invoke operator
log("This is info level by default")
log { "Lazy info message" }
```

### Delegation Pattern
```kotlin
class MyClass : WithLogging {
    fun doWork() {
        log.info("Working...")  // log is available automatically
    }
}
```

## Performance Considerations
- All logging methods are inline functions
- Lambda messages are only evaluated if logging level is enabled
- No boxing overhead for primitives
- JS implementation caches loggers using dynamic properties

## Building & Publishing

### Local Development
```bash
./gradlew build
./gradlew publishToMavenLocal
```

### JitPack Integration
- Group: `com.github.lewik`
- Artifact: `klog-metadata`
- Latest version: `2.0.5`

### Dependency Declaration
```kotlin
// build.gradle.kts
dependencies {
    implementation("com.github.lewik.klog:klog-metadata:2.0.5")
}
```

## Platform-Specific Notes

### JVM
- Requires SLF4J API (1.7.36)
- Works with any SLF4J implementation (Logback, Log4j2, etc.)
- Full compatibility with existing Java logging ecosystem

### JavaScript
- Uses browser console methods (log, info, warn, error)
- Configurable log levels via `KLoggingLevels` enum
- Regex-based logger name matching for level configuration
- IR compilation mode only

## Code Style & Conventions
- Self-documenting code without unnecessary comments
- Inline functions for performance-critical paths
- Expect/actual pattern for platform-specific code
- Minimal external dependencies (only SLF4J for JVM)
- Keep it simple - resist feature bloat and complex configurations

## Future Plans
- Native platform support
- Enhanced configuration options

## Development Tips
- When adding new features, implement in common module first
- Use inline functions for any hot-path code
- Maintain backward compatibility
- Keep the API surface minimal and intuitive
- Test on both JVM and JS platforms before releasing
- **CRITICAL: Always ask "Does this feature make the library simpler or more complex?"**
- If it adds complexity - reject it. Simplicity is the core value.
- Prefer convention over configuration
- When in doubt, choose the simpler solution