package klog

import kotlin.reflect.KClass

actual object KLoggers {
    actual fun logger(owner: Any): KLogger = when (owner) {
        is String -> KLogger(NativeLogger(owner))
        is KClass<*> -> KLogger(NativeLogger(owner.simpleName ?: "Unknown"))
        else -> KLogger(NativeLogger(owner::class.simpleName ?: "Unknown"))
    }
}