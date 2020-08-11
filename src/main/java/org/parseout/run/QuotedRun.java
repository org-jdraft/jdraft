package org.parseout.run;

import org.parseout.ContentType;

/**
 * Content contained within Quoted, i.e. {'Quoted Information','c',
 * 'multi
 *  line
 *  quote'
 *
 *  NOTE: not limited to single characters (i.e. certain languages like SQL support Strings in single quotes)
 */
public class QuotedRun extends Run.DelimitedRun {
    public static ContentType CONTENT_TYPE = new ContentType("Quoted", ContentType.QUOTED);
    public static QuotedRun INSTANCE = new QuotedRun();

    public QuotedRun() {
        super(CONTENT_TYPE,"'", "'", false);
    }
}
