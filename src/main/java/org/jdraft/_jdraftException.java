package org.jdraft;

/**
 * Base RuntimeException for the jdraft tool(s)
 *
 * Often used to wrap lower level exceptions (IOException, JavaParserException)
 * from being thrown, or extended
 *
 * @see org.jdraft.runtime._runtimeException
 * @see org.jdraft.io._ioException
 *
 * @author M. Eric DeFazio
 */
public class _jdraftException extends RuntimeException{

    public _jdraftException(String message, Throwable throwable){
        super(message, throwable);
    }

    public _jdraftException(String message){
        super(message);
    }

    public _jdraftException(Throwable throwable){
        super(throwable);
    }
}
