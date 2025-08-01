package klog

import kotlin.test.*

class KLoggerTest {
    
    @Test
    fun `invoke operator should work as info`() {
        val logger = KLoggers.logger("InvokeTest")
        
        // Should not throw exception
        logger("test message")
        logger { "lazy test message" }
    }
    
    @Test
    fun `should support all log levels`() {
        val logger = KLoggers.logger("LevelsTest")
        
        // Should not throw exceptions
        logger.trace("trace message")
        logger.debug("debug message") 
        logger.info("info message")
        logger.warn("warn message")
        logger.error("error message")
    }
    
    @Test
    fun `should support lazy evaluation`() {
        val logger = KLoggers.logger("LazyTest")
        
        // Should not throw exceptions - lazy lambdas
        logger.trace { "lazy trace" }
        logger.debug { "lazy debug" }
        logger.info { "lazy info" }
        logger.warn { "lazy warn" }
        logger.error { "lazy error" }
    }
    
    @Test
    fun `should support exception logging`() {
        val logger = KLoggers.logger("ExceptionTest")
        val exception = RuntimeException("test exception")
        
        // Should not throw exceptions
        logger.trace(exception, "trace with exception")
        logger.debug(exception, "debug with exception")
        logger.info(exception, "info with exception")
        logger.warn(exception, "warn with exception")
        logger.error(exception, "error with exception")
    }
    
    @Test
    fun `should support lazy exception logging`() {
        val logger = KLoggers.logger("LazyExceptionTest")
        val exception = RuntimeException("test exception")
        
        // Should not throw exceptions
        logger.trace(exception) { "lazy trace with exception" }
        logger.debug(exception) { "lazy debug with exception" }
        logger.info(exception) { "lazy info with exception" }
        logger.warn(exception) { "lazy warn with exception" }
        logger.error(exception) { "lazy error with exception" }
    }
    
    @Test
    fun `should handle null messages`() {
        val logger = KLoggers.logger("NullTest")
        
        // Should not throw exceptions
        logger.info(null)
        logger.warn(null)
        logger.error(null)
    }
    
    @Test
    fun `should handle different message types`() {
        val logger = KLoggers.logger("TypesTest")
        
        // Should not throw exceptions
        logger.info("string message")
        logger.info(42)
        logger.info(true)
        logger.info(listOf(1, 2, 3))
    }
}