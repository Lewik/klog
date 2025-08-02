package klog

import kotlin.test.*

class NativeKLoggersTest {

    @Test
    fun `should create logger by string name`() {
        val logger = KLoggers.logger("TestLogger")
        assertNotNull(logger)
    }

    @Test
    fun `should create logger by object`() {
        val testObject = TestClass()
        val logger = KLoggers.logger(testObject)
        assertNotNull(logger)
    }

    @Test
    fun `should log messages without errors`() {
        val logger = KLoggers.logger("TestLogger")
        
        logger.trace("Trace message")
        logger.debug("Debug message")
        logger.info("Info message")
        logger.warn("Warn message")
        logger.error("Error message")
        
        val exception = RuntimeException("Test exception")
        logger.error(exception, "Error with exception")
    }

    @Test
    fun `should use WithLogging pattern`() {
        val testObj = TestWithLogging()
        testObj.testLogging()
    }
}

private class TestClass

private class TestWithLogging : WithLogging {
    override val log = KLoggers.logger(this)
    
    fun testLogging() {
        log.info("WithLogging pattern works!")
        log("Invoke operator works!")
    }
}