# Changelog

All notable changes to this project will be documented in this file.

The format is based on [Keep a Changelog](https://keepachangelog.com/en/1.0.0/),
and this project adheres to [Semantic Versioning](https://semver.org/spec/v2.0.0.html).

## [2.1.0] - 2025-01-01

### Added
- Comprehensive test suite for Kotlin Multiplatform project
- Real JavaScript console output verification in tests (console monkey patching)
- JavaScript language injection for better IDE experience with `js()` blocks
- TESTING.md documentation for test implementation details
- Cross-platform LogVerifier contract with expect/actual pattern

### Changed
- Updated README.md to use modern Gradle DSL syntax (`implementation`)
- Enhanced IDE experience with proper JavaScript syntax highlighting in tests
- Improved test reliability - all tests now pass on both JVM and JS platforms

### Technical
- JVM tests use SLF4J + Logback ListAppender for real logging verification
- JS tests use console monkey patching instead of no-op stubs
- Added Contributors section crediting Claude Code assistance

## [2.0.5] - 2024-XX-XX

### Changed
- Updated dependencies and modernized build system
- Version catalog implementation with libs.versions.toml
- Updated Kotlin to 2.2.0, SLF4J to 2.0.17, Gradle to 8.14

### Added
- Modern version management with centralized dependency catalog

## [2.0.4] and earlier

### Note
For detailed history of versions 2.0.4 and earlier, see [commit messages](https://github.com/Lewik/klog/commits/master).

---

*This changelog is maintained starting from version 2.1.0. Previous versions can be tracked through git commit history.*