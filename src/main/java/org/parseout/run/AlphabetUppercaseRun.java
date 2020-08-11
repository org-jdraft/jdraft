package org.parseout.run;

import org.parseout.ContentType;

public class AlphabetUppercaseRun extends Run.ContentRun {
    public static ContentType CONTENT_TYPE = new ContentType("Alphabet Lowercase", ContentType.ALPHABET_UPPERCASE);

    public static AlphabetUppercaseRun INSTANCE = new AlphabetUppercaseRun();

    private AlphabetUppercaseRun() {
        super(CONTENT_TYPE,
                'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z');
    }
}
