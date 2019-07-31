package org.jdraft.adhoc;

import org.jdraft._jDraftException;

/**
 * Exception in compiling, class loading, or running dynamically built Java code
 * using the AdHoc / {@link javax.tools.JavaCompiler} runtime compilation API
 * 
 * @author Eric
 */
public class _adhocException extends _jDraftException {
    
    public _adhocException(String msg) {
	super(msg);
    }
    
    public _adhocException(String msg, Throwable ex) {
        super(msg, ex);
    }
}
