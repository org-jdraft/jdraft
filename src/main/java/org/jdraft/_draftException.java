package org.jdraft;

/**
 * Base RuntimeException for the jdraft tool(s)
 *
 * Often used to wrap lower level exceptions (IOException, JavaParserException)
 * from being thrown, or extended
 *
 * @see org.jdraft.adhoc._adhocException
 * @see org.jdraft.io._ioException
 *
 * @author M. Eric DeFazio
 */
public class _draftException extends RuntimeException{

    public _draftException(String message, Throwable throwable){
        super(message, throwable);
    }

    public _draftException(String message){
        super(message);
    }

    public _draftException(Throwable throwable){
        super(throwable);
    }
}
