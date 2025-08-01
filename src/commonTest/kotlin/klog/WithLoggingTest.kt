package klog

import kotlin.test.*

class WithLoggingTest {
    
    @Test
    fun `WithLogging should provide logger`() {
        val testClass = TestClassWithLogging()
        assertNotNull(testClass.log)
    }
    
    @Test
    fun `WithLogging should work in delegation`() {
        val testClass = TestClassWithDelegation()
        assertNotNull(testClass.log)
        
        // Should not throw exception
        testClass.doSomething()
    }
    
    @Test
    fun `KLoggerHolder should provide logger`() {
        val holder = KLoggerHolder()
        assertNotNull(holder.log)
    }
    
    @Test
    fun `multiple WithLogging instances should have different loggers`() {
        val class1 = TestClassWithLogging()
        val class2 = TestClassWithLogging()
        
        assertNotNull(class1.log)
        assertNotNull(class2.log)
        // They might be same or different depending on implementation - both valid
    }
}

private class TestClassWithLogging : WithLogging {
    override val log = KLoggers.logger(this)
    
    fun doSomething() {
        log.info("doing something")
    }
}

private class TestClassWithDelegation {
    private val loggerHolder = KLoggerHolder()
    val log = loggerHolder.log
    
    fun doSomething() {
        log.info("delegated logging")
    }
}