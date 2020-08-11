package org.parseout.run;

import org.parseout.ContentType;
import org.parseout.State;

/** Run of Whitespace characters */
public class WhitespaceRun extends Run.ContentRun {

    public static ContentType CONTENT_TYPE = new ContentType("Whitespace", ContentType.WHITESPACE);
    public static WhitespaceRun INSTANCE = new WhitespaceRun();

    public WhitespaceRun() {
        super(CONTENT_TYPE,' ', '\t', '\r', '\n');
    }

    public State next(State state) {
        int nxt = len(state);
        if (nxt > 0) {
            state.onRun(getRunContentType(), nxt, false);
            state.cursor += nxt;
        }
        return state;
    }
}
