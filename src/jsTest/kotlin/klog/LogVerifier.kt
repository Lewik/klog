package klog

actual class LogVerifier {
    private var originalConsole: dynamic = null
    
    actual fun startCapture() {
        // language=JavaScript
        js("""
            var self = this;
            // Create global messages array if not exists
            if (typeof window !== 'undefined') {
                window.globalMessages = [];
            } else {
                global.globalMessages = [];
            }
            var globalMessages = (typeof window !== 'undefined') ? window.globalMessages : global.globalMessages;
            
            // Save originals
            this.originalConsole = {
                log: console.log,
                info: console.info,
                warn: console.warn,
                error: console.error
            };
            
            // Replace with spies
            console.log = function() {
                // Convert all arguments to strings
                var parts = [];
                for (var i = 0; i < arguments.length; i++) {
                    var arg = arguments[i];
                    parts.push(arg == null ? 'null' : String(arg));
                }
                var message = parts.join(' ');
                globalMessages.push(message);
                // Still call original for debugging
                self.originalConsole.log.apply(console, arguments);
            };
            
            console.info = function() {
                var parts = [];
                for (var i = 0; i < arguments.length; i++) {
                    var arg = arguments[i];
                    parts.push(arg == null ? 'null' : String(arg));
                }
                var message = parts.join(' ');
                globalMessages.push(message);
                self.originalConsole.info.apply(console, arguments);
            };
            
            console.warn = function() {
                var parts = [];
                for (var i = 0; i < arguments.length; i++) {
                    var arg = arguments[i];
                    parts.push(arg == null ? 'null' : String(arg));
                }
                var message = parts.join(' ');
                globalMessages.push(message);
                self.originalConsole.warn.apply(console, arguments);
            };
            
            console.error = function() {
                var parts = [];
                for (var i = 0; i < arguments.length; i++) {
                    var arg = arguments[i];
                    parts.push(arg == null ? 'null' : String(arg));
                }
                var message = parts.join(' ');
                globalMessages.push(message);
                self.originalConsole.error.apply(console, arguments);
            };
        """)
    }
    
    actual fun stopCapture() {
        if (originalConsole != null) {
            // language=JavaScript
            js("""
                console.log = this.originalConsole.log;
                console.info = this.originalConsole.info;
                console.warn = this.originalConsole.warn;
                console.error = this.originalConsole.error;
                this.originalConsole = null;
            """)
        }
    }
    
    actual fun getCapturedMessages(): List<String> {
        val messages = mutableListOf<String>()
        
        // Get the global messages array
        // language=JavaScript
        val globalArray: dynamic = js("""
            (typeof window !== 'undefined') ? window.globalMessages : global.globalMessages
        """)
        
        if (globalArray != null) {
            val length = globalArray.length as? Int ?: 0
            for (i in 0 until length) {
                val msg = globalArray[i] as? String
                if (msg != null) {
                    messages.add(msg)
                }
            }
        }
        
        return messages
    }
    
    actual fun clear() {
        // language=JavaScript
        js("""
            var globalMessages = (typeof window !== 'undefined') ? window.globalMessages : global.globalMessages;
            if (globalMessages) {
                globalMessages.length = 0;
            }
        """)
    }
    
    actual fun expectMessage(message: String): Boolean {
        val found = false
        // language=JavaScript
        js("""
            var globalMessages = (typeof window !== 'undefined') ? window.globalMessages : global.globalMessages;
            if (globalMessages) {
                for (var i = 0; i < globalMessages.length; i++) {
                    if (globalMessages[i].indexOf(message) >= 0) {
                        found = true;
                        break;
                    }
                }
            }
        """)
        return found
    }
}