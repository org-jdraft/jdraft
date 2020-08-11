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
public class SeparatorRun extends Run.ContentRun {
    public static ContentType CONTENT_TYPE = new ContentType("Separator", ContentType.SEPARATOR);
    public static SeparatorRun INSTANCE = new SeparatorRun();

    public static final String CHARS_STRING = ".,:";


    //separator '.', ',', ':'
    //signifier '@', '\\'
    //contextual '$', '_'
    //
    private SeparatorRun() {
        super(CONTENT_TYPE,
                '.',
                ',',
                ':');
    }
}
