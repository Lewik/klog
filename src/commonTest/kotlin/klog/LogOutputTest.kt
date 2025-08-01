package klog

import kotlin.test.*

class LogOutputTest {
    private val verifier = LogVerifier()
    
    @BeforeTest
    fun setUp() {
        verifier.startCapture()
        verifier.clear()
    }
    
    @AfterTest
    fun tearDown() {
        verifier.stopCapture()
    }
    
    @Test
    fun `should actually log info messages`() {
        val logger = KLoggers.logger("TestLogger")
        
        logger.info("test info message")
        
        assertTrue(verifier.expectMessage("test info message"), 
                  "Expected 'test info message' to be logged")
    }
    
    @Test
    fun `should log all levels`() {
        val logger = KLoggers.logger("LevelsLogger")
        
        logger.trace("trace message")
        logger.debug("debug message")
        logger.info("info message")
        logger.warn("warn message")
        logger.error("error message")
        
        val messages = verifier.getCapturedMessages()
        assertTrue(messages.isNotEmpty(), "Should capture at least some log messages")
        
        // Verify specific messages were logged
        assertTrue(verifier.expectMessage("info message"), "Should capture info message")
        assertTrue(verifier.expectMessage("warn message"), "Should capture warn message")
        assertTrue(verifier.expectMessage("error message"), "Should capture error message")
    }
    
    @Test
    fun `should evaluate lazy lambdas when logging is enabled`() {
        val logger = KLoggers.logger("LazyLogger")
        
        var evaluated = false
        logger.info { 
            evaluated = true
            "lazy info message"
        }
        
        assertTrue(evaluated, "Lambda should be evaluated")
        assertTrue(verifier.expectMessage("lazy info message"),
                  "Expected lazy message to be logged")
    }
    
    @Test
    fun `should handle invoke operator`() {
        val logger = KLoggers.logger("InvokeLogger")
        
        logger("invoke message")
        
        assertTrue(verifier.expectMessage("invoke message"),
                  "Expected invoke message to be logged as info")
    }
    
    @Test
    fun `should log different message types`() {
        val logger = KLoggers.logger("TypesLogger")
        
        logger.info("string message")
        logger.info(42)
        logger.info(true)
        
        val messages = verifier.getCapturedMessages()
        assertTrue(messages.isNotEmpty(), "Should capture messages")
        assertTrue(messages.any { it.contains("string message") }, "Should capture string message")
        assertTrue(messages.any { it.contains("42") }, "Should capture number message")
        assertTrue(messages.any { it.contains("true") }, "Should capture boolean message")
    }
    
    @Test
    fun `should handle null messages gracefully`() {
        val logger = KLoggers.logger("NullLogger")
        
        logger.info(null)
        
        // Should not crash - if we reach this point, test passes
        val messages = verifier.getCapturedMessages()
        assertTrue(messages.isNotEmpty(), "Should capture null message (as 'null' string)")
        assertTrue(messages.any { it.contains("null") }, "Should log null as string")
    }
}