package org.parseout.run;

import org.parseout.ContentType;

public class AlphabetLowercaseRun extends Run.ContentRun {

    public static ContentType CONTENT_TYPE = new ContentType("Alphabet Lowercase", ContentType.ALPHABET_LOWERCASE);
    public static AlphabetLowercaseRun INSTANCE = new AlphabetLowercaseRun();

    private AlphabetLowercaseRun() {
        super(CONTENT_TYPE,
                'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z');
    }
}
