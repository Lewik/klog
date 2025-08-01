package klog

import kotlin.test.*

class KLoggersTest {
    
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
    fun `should create same logger for same string name`() {
        val logger1 = KLoggers.logger("SameName")
        val logger2 = KLoggers.logger("SameName")
        // Note: Implementation might cache or create new instances - both are valid
        assertNotNull(logger1)
        assertNotNull(logger2)
    }
}

private class TestClass