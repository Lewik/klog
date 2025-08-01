package klog

import ch.qos.logback.classic.Logger
import ch.qos.logback.classic.spi.ILoggingEvent
import ch.qos.logback.core.read.ListAppender
import org.slf4j.LoggerFactory
import kotlin.test.*

class JvmKLoggersTest {
    private val listAppender = ListAppender<ILoggingEvent>()
    private val rootLogger = LoggerFactory.getLogger(Logger.ROOT_LOGGER_NAME) as Logger
    
    @BeforeTest
    fun setUp() {
        listAppender.start()
        rootLogger.addAppender(listAppender)
        listAppender.list.clear()
    }
    
    @AfterTest
    fun tearDown() {
        rootLogger.detachAppender(listAppender)
        listAppender.stop()
    }
    
    @Test
    fun `should create logger by class`() {
        val logger = KLoggers.logger(JvmKLoggersTest::class)
        assertNotNull(logger)
    }
    
    @Test
    fun `should create logger by java class`() {
        val logger = KLoggers.logger(JvmKLoggersTest::class.java)
        assertNotNull(logger)
    }
    
    @Test
    fun `should create logger by string name`() {
        val logger = KLoggers.logger("com.example.TestLogger")
        assertNotNull(logger)
    }
    
    @Test
    fun `should create logger by object instance`() {
        val testObject = TestObject()
        val logger = KLoggers.logger(testObject)
        assertNotNull(logger)
    }
    
    @Test
    fun `should log to SLF4J backend`() {
        val logger = KLoggers.logger("SLF4JTest")
        
        logger.info("JVM test message")
        logger.warn("JVM warning message")
        
        val messages = listAppender.list.map { it.formattedMessage }
        assertTrue(messages.contains("JVM test message"))
        assertTrue(messages.contains("JVM warning message"))
    }
    
    @Test
    fun `should handle exceptions in SLF4J`() {
        val logger = KLoggers.logger("ExceptionTest")
        val exception = RuntimeException("test exception")
        
        logger.error(exception, "Error occurred")
        
        val events = listAppender.list
        assertEquals(1, events.size)
        
        val event = events[0]
        assertEquals("Error occurred", event.formattedMessage)
        assertEquals(exception.javaClass.name, event.throwableProxy?.className)
    }
}

private class TestObject