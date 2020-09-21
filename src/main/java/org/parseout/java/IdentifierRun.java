package org.parseout.java;

import org.parseout.ContentType;
import org.parseout.run.Run;

public class IdentifierRun extends Run.ContentRun {

    //Any ContentType that is a mixture of Content Types is a "Hybrid" Content Type
    public static final ContentType CONTENT_TYPE = new ContentType("Identifier",
            ContentType.ALPHABET_LOWERCASE, ContentType.ALPHABET_UPPERCASE, ContentType.DIGITS,  ContentType.CONTEXTUAL);

    public static final char[] CHARS = {
        'a','b','c','d','e','f','g','h','i','j','k','l','m','n','o','p','q','r','s','t','u','v','w','x','y','z',
        'A','B','C','D','E','F','G','H','I','J','K','L','M','N','O','P','Q','R','S','T','U','V','W','X','Y','Z','_','$'};

    public static IdentifierRun INSTANCE = new IdentifierRun();

    private IdentifierRun(){
        super(CONTENT_TYPE, CHARS);
    }

    @Override
    public ContentType getRunContentType() {
        return CONTENT_TYPE;
    }
}

