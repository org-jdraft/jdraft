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
public class _jDraftException extends RuntimeException{

    public _jDraftException(String message, Throwable throwable){
        super(message, throwable);
    }

    public _jDraftException(String message){
        super(message);
    }

    public _jDraftException(Throwable throwable){
        super(throwable);
    }
}
