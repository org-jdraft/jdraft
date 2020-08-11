package org.parseout.java;

import org.parseout.ContentType;
import org.parseout.State;
import org.parseout.run.Run;

public class PunctuationRun implements Run {

    public static ContentType CONTENT_TYPE = new ContentType("Punctuation", 0b00000000000000000000111000000000);
    public static PunctuationRun INSTANCE = new PunctuationRun();

    public static final String CHARS_STRING = "([{<)]}>;";
    public static final char[] CHARS = {'(', '[', '{', '<', ')', ']', '}', '>', ';'};

    @Override
    public ContentType getRunContentType() {
        return CONTENT_TYPE;
    }

    @Override
    public int len(State state) {
        if (state.content.length() > state.cursor) {
            switch (state.content.charAt(state.cursor)) {
                case '(':
                case '[':
                case '{':
                case '<':
                case ')':
                case ']':
                case '}':
                case '>':
                case ';':
                    return 1;
                default:
                    return 0;
            }
        }
        return 0;
    }

    @Override
    public State next(State s) {
        if (s.content.length() > s.cursor) {
            ContentType rt;
            switch (s.content.charAt(s.cursor)) {
                case '(':
                    rt = Open.OPEN_PAREN;
                    break;
                case '[':
                    rt = Open.OPEN_BRAKT;
                    break;
                case '{':
                    rt = Open.OPEN_CURLY;
                    break;
                case '<':
                    rt = Open.OPEN_ANGLE;
                    break;
                case ')':
                    rt = Close.CLOSE_PAREN;
                    break;
                case ']':
                    rt = Close.CLOSE_BRAKT;
                    break;
                case '}':
                    rt = Close.CLOSE_CURLY;
                    break;
                case '>':
                    rt = Close.CLOSE_ANGLE;
                    break;
                case ';':
                    rt = Semi.INSTANCE;
                    break;
                default:
                    return s;
            }
            s.onRun(rt, 1, false);
            s.cursor++;
        }
        return s;
    }

    public static abstract class PunctuationType extends ContentType {
        public PunctuationType(String name, int runBit){
            super(name, runBit);
        }
    }

    /**
     * Punctuation kicks off an "evaluation" for the things before it and also the opening/closing of context
     */
    public static class Close extends PunctuationType implements Run {
        /** we cache instances of these */
        public static final Close CLOSE_PAREN = new Close('(',ContentType.PUNCTUATION_CLOSE);
        public static final Close CLOSE_BRAKT = new Close(']',ContentType.PUNCTUATION_CLOSE);
        public static final Close CLOSE_CURLY = new Close('}',ContentType.PUNCTUATION_CLOSE);
        public static final Close CLOSE_ANGLE = new Close('>',ContentType.PUNCTUATION_CLOSE);

        public static final String CHARS_STRING = ")]}>";
        public static final char[] CHARS = {')', ']', '}', '>'}; //because this is a CLOSE, we should be able to know (based on context what is open)

        public final char closeChar;

        //this causes
        private Close(char closeChar, int runBit) {
            super("Punctuation Close ("+closeChar+")", runBit);
            //paramsopen, ctor open, array dim open
            this.closeChar = closeChar;
        }

        @Override
        public ContentType getRunContentType() {
            return this;
        }

        @Override
        public int len(State state) {
            return (state.content.length() > state.cursor
                    &&
                    (state.content.charAt(state.cursor) == ')' ||
                            state.content.charAt(state.cursor) == ']' ||
                            state.content.charAt(state.cursor) == '}' ||
                            state.content.charAt(state.cursor) == '>'
                    )) ? 1 : 0;
        }

        @Override
        public State next(State s) {
            if (s.content.length() > s.cursor) {
                switch ((s.content.charAt(s.cursor))) {
                    case ')':
                        s.onRun(CLOSE_PAREN, 1, false);
                    case ']':
                        s.onRun(CLOSE_BRAKT, 1, false);
                    case '}':
                        s.onRun(CLOSE_CURLY, 1, false);
                    case '>':
                        s.onRun(CLOSE_ANGLE, 1, false);
                    default:
                        return s;
                }
            }
            return s;
        }

        public String toString() {
            return this.closeChar + "";
        }
    }

    /**
     * Punctuation kicks off an "evaluation" for the things before it and also the opening/closing of context
     */
    public static class Open extends PunctuationType implements Run {
        //instance for testing
        //public static final Open INSTANCE = new Open();

        public static final Open OPEN_PAREN = new Open('(', ContentType.PUNCTUATION_OPEN);
        public static final Open OPEN_BRAKT = new Open('[', ContentType.PUNCTUATION_OPEN);
        public static final Open OPEN_CURLY = new Open('{', ContentType.PUNCTUATION_OPEN);
        public static final Open OPEN_ANGLE = new Open('<', ContentType.PUNCTUATION_OPEN);
        public static final String CHARS_STRING = "([{<";
        public static final char[] CHARS = {'(', '[', '{', '<'};

        public final char openChar;

        protected Open(char openChar, int runBit) {
            super("Punctuation Open ("+openChar+")", runBit);
            //paramsopen, ctor open, array dim open
            this.openChar = openChar;
        }

        public char getOpenChar() {
            return openChar;
        }

        @Override
        public ContentType getRunContentType() {
            return this;
        }

        @Override
        public int len(State state) {
            return (state.content.length() > state.cursor
                    &&
                    (state.content.charAt(state.cursor) == '(' ||
                            state.content.charAt(state.cursor) == '[' ||
                            state.content.charAt(state.cursor) == '{' ||
                            state.content.charAt(state.cursor) == '<'
                    )) ? 1 : 0;
        }

        public String toString() {
            return this.openChar + "";
        }

        @Override
        public State next(State s) {
            if (s.content.length() > s.cursor) {
                switch ((s.content.charAt(s.cursor))) {
                    case '(':
                        s.onRun(OPEN_PAREN, 1, false);
                    case '[':
                        s.onRun(OPEN_BRAKT, 1, false);
                    case '{':
                        s.onRun(OPEN_CURLY, 1, false);
                    case '<':
                        s.onRun(OPEN_ANGLE, 1, false);
                    default:
                        return s;
                }
            }
            return s;
        }
    }

    public static class Semi extends PunctuationType implements Run {
        public static final Semi INSTANCE = new Semi();

        private Semi() {
            super("Punctuation ;", ContentType.PUNCTUATION_CLOSE);
        }

        @Override
        public ContentType getRunContentType() {
            return this;
        }

        @Override
        public int len(State state) {
            return (state.content.length() > state.cursor && state.content.charAt(state.cursor) == ';') ? 1 : 0;
        }

        @Override
        public State next(State s) {
            if (s.content.length() > s.cursor && s.content.charAt(s.cursor) == ';') {
                s.onRun(this, 1, false);
                s.cursor++;
            }
            return s;
        }

        public String toString() {
            return ";";
        }
    }
}
