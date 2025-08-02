package klog

import kotlin.test.*

class LogVerifierTest {

    @Test
    fun `should capture logger output`() {
        val verifier = LogVerifier()
        val logger = KLoggers.logger("TestLogger")
        
        verifier.startCapture()
        logger.info("Test message")
        logger.error("Error message")
        verifier.stopCapture()
        
        val messages = verifier.getCapturedMessages()
        assertEquals(2, messages.size)
        assertTrue(messages[0].contains("INFO TestLogger: Test message"))
        assertTrue(messages[1].contains("ERROR TestLogger: Error message"))
    }
    
    @Test
    fun `should not capture when not started`() {
        val verifier = LogVerifier()
        val logger = KLoggers.logger("TestLogger")
        
        logger.info("This should not be captured")
        
        val messages = verifier.getCapturedMessages()
        assertEquals(0, messages.size)
    }
    
    @Test
    fun `should restore original writer after stop`() {
        val verifier = LogVerifier()
        val logger = KLoggers.logger("TestLogger")
        
        verifier.startCapture()
        logger.info("Captured message")
        verifier.stopCapture()
        
        // This should go to println (not captured)
        logger.info("Not captured message")
        
        val messages = verifier.getCapturedMessages()
        assertEquals(1, messages.size)
        assertTrue(messages[0].contains("Captured message"))
    }
}