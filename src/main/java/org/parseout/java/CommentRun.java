package org.parseout.java;

import org.parseout.ContentType;
import org.parseout.State;
import org.parseout.run.Run;

/**
 * Looks for Comments
 */
public class CommentRun implements Run {

    public static final ContentType COMMENT_CONTENT = new ContentType("Comment", ContentType.COMMENT);
            //LineCommentRun.CONTENT_TYPE.runBits[0],
            //BlockCommentRun.CONTENT_TYPE.runBits[0],
            //JavadocCommentRun.CONTENT_TYPE.runBits[0]);

    public static final CommentRun INSTANCE = new CommentRun();

    @Override
    public ContentType getRunContentType() {
        return COMMENT_CONTENT;
    }

    @Override
    public int len(State state) {
        if (state.cursor < state.content.length() + 1 && state.content.charAt(state.cursor) == '/') {
            switch (state.content.charAt(state.cursor + 1)) {
                case '/':
                    return LineCommentRun.INSTANCE.scanToCloseDelimiter(state);
                case '*':
                    return BlockCommentRun.INSTANCE.scanToCloseDelimiter(state);
                default:
                    return 0;
            }
        }
        return 0;
    }

    @Override
    public State next(State s) {
        if (s.cursor < s.content.length() + 1 && s.content.charAt(s.cursor) == '/') {
            switch (s.content.charAt(s.cursor + 1)) {
                case '/':
                    return LineCommentRun.INSTANCE.next(s);
                case '*':
                    int cur = s.cursor;
                    try {
                        JavadocCommentRun.INSTANCE.next(s);
                        if (s.cursor > cur) {
                            return s;
                        }
                    } catch(Exception e){
                        //we have (1) instance /**/ really means a "block comment" /* open */ close
                        // and not /** /(open without a close), so we catch that
                    }
                    return BlockCommentRun.INSTANCE.next(s);
                default:
                    return s;
            }
        }
        return s;
    }

    public static class LineCommentRun extends DelimitedRun {
        public static final ContentType CONTENT_TYPE = new ContentType("LineComment", 0b00000000000000000000000000100000);
        public static final LineCommentRun INSTANCE = new LineCommentRun();

        private LineCommentRun() {
            super(CONTENT_TYPE,"//", "\n", true);
        }
    }

    public static class BlockCommentRun extends DelimitedRun {

        public static final ContentType CONTENT_TYPE = new ContentType("BlockComment",0b00000000000000000000000001000000);
        public static final BlockCommentRun INSTANCE = new BlockCommentRun();

        private BlockCommentRun() {
            super(CONTENT_TYPE,"/*", "*/", false);
        }
    }

    public static class JavadocCommentRun extends DelimitedRun {

        public static final ContentType CONTENT_TYPE = new ContentType("JavaDocComment", 0b00000000000000000000000010000000);
        public static final JavadocCommentRun INSTANCE = new JavadocCommentRun();

        private JavadocCommentRun() {
            super(CONTENT_TYPE,"/**", "*/", false);
        }
    }
}
