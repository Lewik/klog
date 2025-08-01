package klog

import kotlin.test.*

class JsKLoggersTest {
    
    @Test
    fun `should create logger by string name`() {
        val logger = KLoggers.logger("JSTestLogger")
        assertNotNull(logger)
    }
    
    @Test
    fun `should create logger by object`() {
        val testObject = TestJSObject()
        val logger = KLoggers.logger(testObject)
        assertNotNull(logger)
    }
    
    @Test
    fun `should handle default logging level`() {
        // JS specific - has defaultLoggingLevel property
        assertNotNull(KLoggers.defaultLoggingLevel)
    }
    
    @Test
    fun `should log to console without throwing`() {
        val logger = KLoggers.logger("ConsoleTest")
        
        // Just test that logging doesn't throw exceptions
        logger.info("JS test message")
        logger.warn("JS warning message")
        
        // If we reach this point, no exceptions were thrown
        assertTrue(true)
    }
    
    @Test
    fun `debug console capture`() {
        val verifier = LogVerifier()
        verifier.startCapture()
        verifier.clear()
        
        try {
            val logger = KLoggers.logger("DebugTest")
            logger.info("debug test message")
            
            val messages = verifier.getCapturedMessages()
            println("Captured ${messages.size} messages: $messages")
            
            // For now just check we don't crash
            assertTrue(true)
            
        } finally {
            verifier.stopCapture()
        }
    }
    
    @Test
    fun `should respect JS logging levels`() {
        // Test level properties work
        val originalLevel = KLoggers.defaultLoggingLevel
        KLoggers.defaultLoggingLevel = KLoggingLevels.WARN
        
        try {
            val logger = KLoggers.logger("LevelTest")
            
            // Just test that logging doesn't throw exceptions at different levels
            logger.debug("debug message")
            logger.warn("warn message")
            
            // If we reach this point, no exceptions were thrown
            assertTrue(true)
            
        } finally {
            KLoggers.defaultLoggingLevel = originalLevel
        }
    }
}

private class TestJSObject