package org.jdraft.runtime;

import org.jdraft._draftException;

/**
 * Exception in compiling, class loading, or running dynamically built Java code
 * using the AdHoc / {@link javax.tools.JavaCompiler} runtime compilation API
 * 
 * @author Eric
 */
public class _runtimeException extends _draftException {
    
    public _runtimeException(String msg) {
	super(msg);
    }
    
    public _runtimeException(String msg, Throwable ex) {
        super(msg, ex);
    }
}
