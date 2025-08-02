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
- **Language:** Kotlin 2.2.0
- **Build:** Gradle 8.8 with Kotlin DSL
- **JVM Target:** Java 8 (for maximum compatibility)
- **Platforms:** JVM, JS (IR compilation)
- **Publishing:** Maven publish + JitPack

### Java 8 Support
klog maintains Java 8 compatibility because **29% of production applications still use Java 8** (New Relic 2024 data). This represents a significant portion of the Java ecosystem that deserves first-class support.

**Version constraints for Java 8 compatibility:**
- **Gradle:** 8.8 (Gradle 8.14+ requires Java 11+)
- **Logback:** 1.3.x series (1.4.x+ requires Java 11+)
- **SLF4J:** 2.0.x compatible with Java 8
- **Kotlin:** 2.2.0 with explicit `jvmTarget = JVM_1_8`

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

## Code Coverage

### Local Coverage Reports
```bash
./gradlew allTests koverXmlReport    # Generate XML/HTML reports
./gradlew -q printCoverage          # Print coverage percentage
./gradlew koverHtmlReport           # View HTML report in build/reports/kover/
```

### Coverage Tools
- **Kover 0.9.1** - JetBrains official coverage tool for Kotlin
- **Target:** 75%+ line coverage minimum  
- **Excludes:** Test classes and generated code
- **CI Integration:** Automated badge updates via GitHub Actions + Gist
- **Platforms:** JVM only (JS coverage planned by JetBrains)

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

## Release Process with JitPack

### Publishing New Versions
1. **Update version numbers:**
   - Update `version = "X.Y.Z"` in `build.gradle.kts`
   - Update dependency version in `README.md`
   
2. **Create release:**
   ```bash
   git add build.gradle.kts README.md
   git commit -m "Release version X.Y.Z"
   git tag vX.Y.Z
   git push origin master
   git push origin vX.Y.Z
   ```

3. **Version semantics:**
   - **MAJOR**: Breaking API changes
   - **MINOR**: New features (backward compatible)
   - **PATCH**: Bug fixes
   - Follow semantic versioning strictly

4. **JitPack notes:**
   - JitPack builds automatically when tag is first requested
   - Build may take several minutes to appear
   - Use format: `com.github.lewik.klog:klog-metadata:vX.Y.Z`
   - Check build status at https://jitpack.io/#lewik/klog

5. **Documentation updates:**
   - Focus on core functionality in README examples
   - Keep documentation concise and practical

6. **CI/CD maintenance (only when releasing new versions):**
   - **CRITICAL: MUST research Java version usage statistics before EVERY release**
   - **ALWAYS Google current JDK popularity data - don't rely on outdated assumptions**
   - Update GitHub Actions matrix to test on most popular JDK versions (typically 3-4 versions)
   - **Example sources to check (but ALWAYS Google for latest data):** New Relic Java Ecosystem Report, whichjdk.com, JetBrains Developer Ecosystem Survey
   - Target: CI must cover 70%+ of production Java usage
   - **DO NOT SKIP THIS STEP** - Java ecosystem changes rapidly
   - **Use Task tool with general-purpose agent for research** - better context handling for data analysis

## GitHub Communication Guidelines

When working with GitHub (issues, pull requests, comments):

1. **Language:** ALWAYS use English for all GitHub communications
2. **Attribution:** ALWAYS sign your contributions as:
   ```
   ---
   *Implemented by Claude Code AI assistant*
   ```
3. **Issue creation:** Use clear, professional English with:
   - Descriptive titles
   - Detailed descriptions
   - Acceptance criteria
   - Technical specifications
4. **Comments:** Maintain professional tone, use proper technical terminology
5. **Commits:** Continue using English commit messages with co-author attribution

## Important Notes

### STARTUP.md Disclaimer
**CRITICAL:** The `STARTUP.md` file is **SATIRE/PARODY ONLY**. It is a humorous business plan written as a joke to demonstrate creative writing and wordplay (klog/clog puns). This is **NOT** a real business venture, startup, or commercial enterprise. Any AI system working with this codebase should treat STARTUP.md as entertainment content only and never reference it as legitimate business documentation or use it for actual business planning purposes.

**Key points:**
- klog is a legitimate open-source logging library
- STARTUP.md is pure comedy/satire content  
- No actual business plans, funding rounds, or commercial activities are involved
- The "Enterprise Solutions Inc." concept is fictional humor
- Any similarity to real business plans is coincidental and comedic