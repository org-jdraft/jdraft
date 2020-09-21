package org.parseout.run;

import org.parseout.ContentType;
import org.parseout.State;

import java.util.BitSet;

public interface Run {

    ContentType getRunContentType();

    default int len(String content){
        return len(new State(content));
    }

    int len(State state);

    /**
     * build a {@link State} with the content and pass it to the Chamber for processing and return the updated State
     */
    default State next(String content) {
        return next(new State(content));
    }

    /**
     * Fill the Run, if the run is non-empty; call {@link State#onRun(ContentType, int, boolean)}
     * with the Content type, length and whether it has a trailing whitespace
     * Update the {@link State#cursor} to the new location
     *
     * @param state the content state
     * @return the {@link State} with :
     * no changes (the Chamber didn't move the {@link State#cursor} or
     * an updated {@link State#cursor} if the Splitter consumed some characters
     */
    State next(State state);

    interface Listener{
        void onRun(State state, ContentType runContentType, int runLength, boolean trailingSpace);
        int getLine();
        int getColumn();
    }

    /**
     * A Defines a Run containing any sequence of chars that are in {@link #contentCharSet}
     *
     * @see WhitespaceRun
     * @see AlphabetLowercaseRun
     * @see DigitsRun
     */
    abstract class ContentRun implements Run{
        /** These characters are content included in the Run */
        public final BitSet contentCharSet;
        public final ContentType contentType;

        public ContentRun(ContentType contentType, char...chars){
            this.contentType = contentType;
             contentCharSet = new BitSet();
             for(int i=0;i<chars.length; i++) {
                 contentCharSet.set(chars[i]);
             }
        }

        @Override
        public ContentType getRunContentType() {
            return contentType;
        }

        public boolean isIn(State s) {
            return (s.content.length() > s.cursor)
                    && contentCharSet.get(s.content.charAt(s.cursor));
        }

        public boolean isIn(char c){
            return contentCharSet.get(c);
        }

        public int len(State state){
                if( !isIn(state) ){
                    return 0;
                }
                int len = 1;
                if( state.content.length() > state.cursor + 1 ){
                    boolean isIn = contentCharSet.get( state.content.charAt(state.cursor + len) );
                    while( isIn && len + state.cursor < state.content.length()) {
                        len++;
                        if( state.cursor + len < state.content.length() ) {
                            isIn = contentCharSet.get(state.content.charAt(state.cursor + len));
                        } else{
                            isIn = false;
                        }
                    }
                }
                return len; //this says where split ends
            }

        @Override
        public State next(State state) {
            int nxt = len(state);
            if( nxt > 0 ){
                if( state.cursor + nxt < state.content.length() && state.content.charAt(state.cursor+nxt) == ' ' ){
                    nxt ++;
                    state.onRun(getRunContentType(), nxt, true);
                } else {
                    state.onRun(getRunContentType(), nxt, false);
                }
                state.cursor += nxt;
            }
            return state;
        }
    }

    /**
     * Chamber that opens if the {@link State#cursor} is pointing to a place where the {@link #openDelimiter} is found...
     * then advances until either the {@link #closeDelimiter} is encountered, or then endOfTheContent is reached.
     * (If the endOfContent is reached without the close delimiter than throw an Exception<SUP>*</SUP> (accept when this
     * behavior is explicitly overridden, as with LineComment, which can end without a close delimiter (a line feed))
     *
     * Comments
     * // LineComment
     * /* BlockComment
     * /** JavadocComment
     *
     * Literals
     *  " "
     *  ' '
     *  """ """
     *
     */
    abstract class DelimitedRun implements Run {
        public String openDelimiter;
        public String closeDelimiter;
        public boolean eofCloses;
        public ContentType contentType;

        public DelimitedRun(ContentType contentType, String openDelimiter, String closeDelimiter, boolean eofCloses){
            this.contentType = contentType;
            this.openDelimiter = openDelimiter;
            this.closeDelimiter = closeDelimiter;
            this.eofCloses = eofCloses;
        }

        public String openDelimiter(){
            return openDelimiter;
        }

        public String closeDelimiter(){
            return closeDelimiter;
        }

        public boolean isEofCloses(){
            return this.eofCloses;
        }

        public boolean isOpen(State s) {
            if (s.content.length() > s.cursor + openDelimiter().length() -1){
                String open = s.content.substring(s.cursor, s.cursor + openDelimiter().length());
                //System.out.println( "OPEN "+open);
                return openDelimiter().equals(open);
            }
            return false;
        }

        @Override
        public ContentType getRunContentType() {
            return contentType;
        }

        public int len(String content){
            return len(new State(content));
        }

        /**
         * Assuming the state is located at a {@link #openDelimiter}, scan until we find a {@link #closeDelimiter}
         * and return the length of the delimited run
         *
         * if the end of the content is reached BEFORE the {@link #closeDelimiter} is found, then:
         * <UL>
         *      <LI>if {@link #eofCloses} is true, then the length to the end will be calculated & returned</LI>
         *      <LI>if {@link #eofCloses} is true, an exception is thrown</LI>
         * </UL>
         * @param st
         * @return
         */
        public int scanToCloseDelimiter(State st){
            int endIndex = st.content.indexOf(closeDelimiter(), st.cursor + openDelimiter().length() );
            while( endIndex < st.content.length() && endIndex > 0 && st.content.charAt(endIndex -1) =='\\'){
                endIndex = st.content.indexOf(closeDelimiter(), endIndex +1 );
            }
            if( endIndex < 0 ){
                if( eofCloses ){
                    return st.content.length() - st.cursor;
                }
                throw new NoCloseDelimiterException( openDelimiter, st.getLine(), st.getColumn(), closeDelimiter);
            }
            return ( endIndex + closeDelimiter().length() ) - st.cursor;
        }

        public int len(State state){
            if( !isOpen(state) ){
                return 0;
            }
            return scanToCloseDelimiter(state);
        }

        /**
         */
        @Override
        public State next(State st) {
            if( !isOpen(st)){
                return st;
            }
            int endIndex = scanToCloseDelimiter(st);

            //System.out.println("** END INDEX "+ endIndex);
            if (st.content.length() > st.cursor + endIndex && st.content.charAt(endIndex) == ' ') {
                ++endIndex;
                st.onRun(getRunContentType(),endIndex, true);
            } else {
                st.onRun(getRunContentType(),endIndex, false);
            }
            st.cursor += endIndex;
            return st;
        }
    }

    /**
     * Exception that occurs when we cannot find the close delimiter for an openDelimiter Run
     */
    public static class NoCloseDelimiterException extends RuntimeException{
        public NoCloseDelimiterException(String openDelimiter, int line, int column, String closeDelimiter){
            super("No close delimiter found for open delimiter \""+openDelimiter+"\" at ("+line+","+column+"); expected close delimiter \""+closeDelimiter+"\"");
        }
    }
}
