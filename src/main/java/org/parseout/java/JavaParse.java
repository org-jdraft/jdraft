package org.parseout.java;

import org.parseout.*;
import org.parseout.run.*;
import org.parseout.ContentType;

import java.util.ArrayList;
import java.util.List;

public class JavaParse implements Run {


    public static final ContentType CONTENT_TYPE = new ContentType("Java", 0b0);

    public static final JavaParse INSTANCE = new JavaParse();

    public static final int WHITESPACE_CONTENT = WhitespaceRun.CONTENT_TYPE.runBits[0];
    public static final int DIGIT_CONTENT_CONTENT = DigitsRun.CONTENT_TYPE.runBits[0];
    public static final int LINE_COMMENT_CONTENT = CommentRun.LineCommentRun.CONTENT_TYPE.runBits[0];
    public static final int BLOCK_COMMENT_CONTENT = CommentRun.BlockCommentRun.CONTENT_TYPE.runBits[0];
    public static final int JAVADOC_COMMENT_CONTENT = CommentRun.JavadocCommentRun.CONTENT_TYPE.runBits[0];
    public static final int ALPHABET_CONTENT = CommentRun.JavadocCommentRun.CONTENT_TYPE.runBits[0];

    public void verifyUnique(int val){
        switch(val){
            case ContentType.WHITESPACE: { }
        }
    }
    @Override
    public ContentType getRunContentType() {
        return CONTENT_TYPE;
    }

    @Override
    public int len(State state) {
        if( state.cursor < state.content.length()){
            char c = state.content.charAt(state.cursor);
            if( Character.isWhitespace(c)){
                return WhitespaceRun.INSTANCE.len(state);
            }
            if( IdentifierRun.INSTANCE.isIn(c) ){ //this handles Alphabetic + _ & $
                return IdentifierRun.INSTANCE.len(state);
            }
            if( Character.isDigit(c)){
                return DigitsRun.INSTANCE.len(state);
            }
            if( c == '/' ){
                //could be divide, line comment, block comment
                int len = CommentRun.INSTANCE.len(state);
                if( len > state.cursor){
                    return len;
                }
            }
            if( c=='"' ){
                return DoubleQuotedRun.INSTANCE.len(state);
            }
            if( c=='\'' ){
                return QuotedRun.INSTANCE.len(state);
            }
            int len = PunctuationRun.INSTANCE.len(state);
            if( len > 0 ){
                return len;
            }
            len = OperatorRun.INSTANCE.len(state);
            if( len > 0 ){
                return len;
            }
            return SeparatorRun.INSTANCE.len(state);
        }
        return state.cursor;
    }

    public State next(String str){
        return next(new State(str));
    }

    @Override
    public State next(State s) {
        if( s.cursor < s.content.length()){
            char c = s.content.charAt(s.cursor);
            if( Character.isWhitespace(c)){
                return WhitespaceRun.INSTANCE.next(s);
            }
            if( IdentifierRun.INSTANCE.isIn(c)){
                return IdentifierRun.INSTANCE.next(s);
            }
            if( Character.isDigit(c)){
                return DigitsRun.INSTANCE.next(s);
            }
            if( c == '/' ){
                //could be divide, line comment, block comment
                int len = s.cursor;
                CommentRun.INSTANCE.next(s);
                if( s.cursor > len ){
                    return s;
                }
            }
            if( c=='"' ){
                return DoubleQuotedRun.INSTANCE.next(s);
            }
            if( c=='\'' ){
                return QuotedRun.INSTANCE.next(s);
            }
            int pos = s.cursor;
            PunctuationRun.INSTANCE.next(s);
            if( s.cursor > pos ){
                return s;
            }
            OperatorRun.INSTANCE.next(s);
            if( s.cursor  > pos ){
                return s;
            }
            return SeparatorRun.INSTANCE.next(s);
        }
        return s;
    }

    public static class RunToken{
        int length;
        boolean trailingSpace;
        ContentType contentType;

        public RunToken(ContentType contentType, int length, boolean trailingSpace){
            this.contentType = contentType;
            this.length = length;
            this.trailingSpace = trailingSpace;
        }

        public String describe(String content, int startPosition){
            return contentType + " "+ content.substring(startPosition, startPosition+length);
        }

        public String toString(){
            return contentType + " "+ length;
        }
    }

    public static State parse(String content){
        State st = new State(content);

        List<RunToken> freeTokens = new ArrayList<>();
        st.listener = new MergeOnEventResolveOnPunctuationListener();


        while( !st.isParsed() ){
            INSTANCE.next(st);
            //System.out.println( st.getLine()+" "+st.getColumn()+" "+ st.context.get());
        }
        System.out.println( freeTokens );
        return st;
    }

    /** I COULD build a bitset*/
    static class MergeOnEventResolveOnPunctuationListener extends State.LineColumnTrackListener {

        public List<RunToken>allTokens = new ArrayList<>();
        //public List<RunToken> rawTokens = new ArrayList<>(); //temporary, we'll use raw & resolved tokens later
        //public List<Term> terms = new ArrayList<>();

        @Override
        public void onRun(State st, ContentType runContentType, int runLength, boolean trailingSpace) {
            int startCursor = st.cursor;
            int startColumn = this.column;
            int startLine = this.line;
            System.out.println("     Token ("+st.cursor+","+(st.cursor+runLength)+")"+st.content.substring( st.cursor, st.cursor + runLength ));
            if( runContentType instanceof PunctuationRun.PunctuationType){
                //resolve (the context)
                if( runContentType instanceof PunctuationRun.Open){
                    //resolve the rawTokens
                    //System.out.println("based on current Context "+ st.contextQueue +" resolve the rawTokens "+ rawTokens+" into terms");
                    // the raw tokens to resolvedTokens & push to context
                    //terms.add(rawTokens);
                    //terms.add( new RunToken(contentType, runLength, trailingSpace) );
                    Context.NamedContext nc = new Context.NamedContext("Class body");
                    st.contextQueue.push(nc); //this might be class body
                    System.out.println( "Pushed "+ nc +" from "+ runContentType);
                } else { //close ) } > or semi ;
                    // resolve the interior tokens
                    // & pop the context
                    System.out.println( "Popped "+ st.contextQueue.pop() +" from "+ runContentType);
                }
            } else{
                //if( rawTokens.isEmpty() ){

                //}
                //let me see if I can Merge this new token/event with the existing head
                //algorithms:
                // for identifiers with combinations of alphabetic + number (no whitespaces between them)
                // for fully qualified names identifiers with '.' between them
            }
            allTokens.add( new RunToken(runContentType,runLength, trailingSpace) );
            super.onRun(st, runContentType, runLength, trailingSpace); //handle the line and column
        }
    }

    //Merge Numbers 0x
    // 0B + Digits
    //Merge Identifiers
    //Merge

    /*
    public static class Numeric extends ContentType {
        // + - . x X _ L l ABCDEF abcdef

        private Numeric() {
            super("Numeric", DigitsRun.CONTENT_TYPE.runBits[0], );
        }
    }
     */

    public static class RunCollector{
        public long[] terms = new long[100];
        public int index = 0;

        public long getCurrent(){
            return terms[index];
        }

        public void mergeOrAdd( State st, ContentType runContentType, int runLength, boolean trailingSpace) {
            long current = getCurrent();

        }


        /**
         * |
         * contentType      runLength                        trailingChar
         *
         * @param st
         * @param runContentType
         * @param runLength
         * @param trailingSpace
         */
        public void add( State st, ContentType runContentType, int runLength, int trailingSpace){
            char c = st.content.charAt(st.cursor + runLength);
            long term = c;//the last 16 bits are the character

            /*
            //do I need to encode the
            if( runContentType == CommentRun.COMMENT_CONTENT ||
                runContentType == PunctuationRun.CONTENT_TYPE ||
                runContentType == ){

            }
             */
        }

        /**
         * Build a new Term
         * @param contentType
         * @param length
         * @param trailingSpace
         * @return

        public int encodeTerm(ContentType contentType, int length, boolean trailingSpace ){

        }
        */
    }

}
