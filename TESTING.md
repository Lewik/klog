# Testing

This project uses comprehensive testing across all Kotlin Multiplatform targets.

## Running Tests

```bash
# Run all tests (JVM + JS)
./gradlew allTests

# JVM tests only
./gradlew jvmTest

# JavaScript tests only
./gradlew jsTest
```

## Test Implementation

### JVM Tests
- Uses **SLF4J + Logback** for real logging output verification
- `ListAppender` captures actual log messages for assertions
- Tests verify logging levels, message content, and exception handling

### JavaScript Tests
- Uses **console monkey patching** to capture browser/Node.js console output
- Temporarily replaces `console.log/info/warn/error` with spy functions
- Real console verification (not just "doesn't throw" stubs)
- Automatically restores original console methods after tests

## Test Coverage
- Common tests for cross-platform functionality
- Platform-specific tests for JVM and JS implementations
- All major logging scenarios covered
- Lazy evaluation testing
- Exception handling verification