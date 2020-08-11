package org.parseout.java;

import org.parseout.ContentType;
import org.parseout.run.Run;

/**
 * Runs of (non punctuation symbols)
 *
 * OPERATOR
 * SEPARATOR
 *
 */
public class OperatorRun extends Run.ContentRun {
    public static ContentType CONTENT_TYPE = new ContentType("Operator", ContentType.OPERATOR);
    public static OperatorRun INSTANCE = new OperatorRun();

    public static final String CHARS_STRING = "`~@#%^&*-_+=\\|/?";


    //separator '.', ',', ':'
    //signifier '@', '\\'
    //contextual '$', '_'
    //
    private OperatorRun() {
        super(CONTENT_TYPE,
                '`',
                '~',
                '@',
                '#',
                '%',
                '^',
                '&',
                '*',
                '-',
                '_',
                '+',
                '=',
                '/',
                '\\',
                '|',
                '?');
    }
}
