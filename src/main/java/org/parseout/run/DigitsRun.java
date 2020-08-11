package org.parseout.run;

import org.parseout.ContentType;

/** Sequence of digits */
public class DigitsRun extends Run.ContentRun {
    public static ContentType CONTENT_TYPE = new ContentType("Digits", ContentType.DIGITS);
    public static DigitsRun INSTANCE = new DigitsRun();

    private DigitsRun() {
        super(CONTENT_TYPE, '0', '1', '2', '3', '4', '5', '6', '7', '8', '9');
    }


}
