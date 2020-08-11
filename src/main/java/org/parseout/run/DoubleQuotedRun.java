package org.parseout.run;

import org.parseout.ContentType;

/**
 * Content contained within Double Quotes, i.e. {"Double Quoted Information","a",
 * "multi
 *  line
 *  quote"}
 */
public class DoubleQuotedRun extends Run.DelimitedRun {
    public static ContentType CONTENT_TYPE = new ContentType("DoubleQuoted", ContentType.DOUBLE_QUOTED);
    public static DoubleQuotedRun INSTANCE = new DoubleQuotedRun();

    public DoubleQuotedRun() {
        super(CONTENT_TYPE,"\"", "\"", false);
    }

}
