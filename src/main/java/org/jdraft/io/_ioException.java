
package org.jdraft.io;

import org.jdraft._jdraftException;

/**
 * base exception for doing i/o typically reading in .java source or
 * writing .java source of .class files
 * 
 * @author Eric
 */
public final class _ioException
    extends _jdraftException {

    public _ioException(String message, Throwable throwable ){
        super( message, throwable );
    }

    public _ioException(String message ){
        super( message );
    }

    public _ioException(Throwable throwable ){
        super( throwable );
    }
}
