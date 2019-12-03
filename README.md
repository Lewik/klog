# klog

This project **is not abandonned**. Just create an issue if kotlin version update is missed.

[![](https://jitpack.io/v/lewik/klog.svg)](https://jitpack.io/#lewik/klog)

KLogging provides unified logging API, which you can use from Kotlin code targeted for JVM, Javascript common kotlin (and native soon).
                                      
                                      
## Download
Use https://jitpack.io repository
```
	allprojects {
		repositories {
			...
			maven { url 'https://jitpack.io' }
		}
	}
```
Use these dependencies per kotlin module respectively:
```
compile 'com.github.lewik.klog:klog-metadata:1.3.61' //for common modules
compile 'com.github.lewik.klog:klog-js:1.3.61'  //for js modules
compile 'com.github.lewik.klog:klog-jvm:1.3.61'  //for jvm modules
```                                   
Versions will be updated with same kotlin version (PR are welcome)

## Usage                                              
```kotlin
class Foo {
    val log = KLoggers.logger(this)
    
    fun test() {
        log("This string will be evaluated regardless if trace enabled = ${log.isTraceEnabled}")
        log {"This string will not be evaluated unless trace enabled = ${log.isTraceEnabled}"}
    
        log("debug level")
        log { "debug level" }
        
        log.trace("trace")
        log.debug("debug")
        log.info("info")
        log.warn("warn")
        log.debug("error")
        
        log.trace { "trace" }
        log.debug { "debug" }
        log.info { "info" }
        log.warn { "warn" }
        log.debug { "error" }
    }
}
```

Another ways to obtain logger:
```kotlin
class Bar {
    private val log = KLoggers.logger(this)
    
    fun test() { log("Have some logging!") }
}

class Baz : WithLogging by KLoggerHolder() {
    fun test() { log("Have some logging!") }
}
 
class Qux {//*
    companion object: WithLogging by KLoggerHolder() 
    
    fun test() { log("Have some logging!") }
} 

```

\* Qux: https://en.wikipedia.org/wiki/Metasyntactic_variable

## Inspired by
- code at [https://github.com/shafirov/klogging] 
- code at [https://github.com/MicroUtils/kotlin-logging] 
- and discussion at [http://stackoverflow.com/questions/34416869/idiomatic-way-of-logging-in-kotlin]
