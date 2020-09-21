package org.parseout;

import org.parseout.run.Run;

import java.util.BitSet;

/**
 * I CAN
 * rewind(n) looks at the current "free tokens" in the listener,
 * discards them
 * backs up the cursor to before them (looks at the length of the tokens &
 * rewindRuns(n)
 *
 * rewindCursor(int charCount)
 * rewindRuns(n)
 * rewind,
 * because I am only creating an outline and not building individual tokens
 *
 * this is useful if I have an error & then change
 *
 */
public class State {

    /**
     * Runs just delineate different basic character types (whitespace, digits, symbols, alphabetic)
     * "Tokens" these point to the derived/categorized textual entities within the structured text
     */
    public int cursor;

    /** The context being parsed into an outline */
    public String content;

    /** will respond to "runs" of chars as the document is being parsed*/
    public Run.Listener listener = new LineColumnTrackListener(){};

    /**
     * As we walk the document, we encounter new contexts of terms (i.e. method declarations, parameter lists, etc.)
     * each time we enter a new context, we "push" onto the Context queue
     *
     * an example might be, we encounter a '(' within the document, & determine it is the start of a "Method Param List",
     * we push "Method Parameter List" on the ContextQueue.
     * then we continue reading in runs of characters into the "free tokens",
     * when we reach the matching ')', we know the "free tokens" can be evaluated as method parameters
     * (i.e. we can separate them by ','s
     * while within the context, we have context
     * specific rules for interpreting tokens as terms, when we exit the context, we pop() the context off the context
     * queue.
     */
    public Context.ContextQueue contextQueue = new Context.ContextQueue();

    public State(){  }

    public State(String content){
        this.content = content;
        this.cursor = 0;
    }

    public State(String content, int cursor){
        this.content = content;
        this.cursor = cursor;
    }

    public State(String content, int cursor, Context.ContextQueue contextQueue, Run.Listener listener){
        this.content = content;
        this.cursor = cursor;
        this.contextQueue = contextQueue;
        this.listener = listener;
    }

    public boolean isParsed(){
        return cursor >= content.length();
    }

    /**
     * check if the cursor is pointing to a place in the document where the target string is
     * @param target exact string I am looking for in the content
     * @return true if the content is at a place where the target exists
     */
    public boolean isAt(String target){
        if( content.length() >= (cursor + target.length()) ){ //range check
            return target.equals( content.substring(cursor, cursor+target.length() ));
        }
        return false;
    }

    public boolean isAt(BitSet expectedCharacter){
        if( this.content.length() > cursor){
            return expectedCharacter.get( this.content.charAt(cursor) );
        }
        return false;
    }

    public boolean isNext(BitSet expectedCharacter){
        if( this.content.length() > cursor + 1){
            return expectedCharacter.get( this.content.charAt(cursor +1) );
        }
        return false;
    }

    /**
     * is the String content AFTER the character at the current cursor within the
     * @param nextContent
     * @return
     */
    public boolean isNext(String nextContent){
        if( content.length() > cursor + nextContent.length() + 1 ){ //range check
            return nextContent.equals( content.substring(cursor+1, cursor+1+nextContent.length() ));
        }
        return false;
    }

    /**
     * Gets the line location within the {@link #content}
     * @return the line number within the content 0 = before reading any content, 1= first line, ...
     */
    public int getLine(){
        return listener.getLine();
    }

    /**
     * The column number location within the {@link #content}
     * @return the column number within the content 0 = before reading any content, 1= first column, ...
     */
    public int getColumn(){
        return listener.getColumn();
    }

    /**
     * Encountered a Run, now process the run via the internally created listener
     * @param runContentType
     * @param runLength
     * @param trailingSpace
     */
    public void  onRun(ContentType runContentType, int runLength, boolean trailingSpace){
        listener.onRun(this, runContentType, runLength, trailingSpace);
    }

    /**
     * Maintains the line and column of within {@link State#content} based on the {@link State#cursor}
     */
    public abstract  static class LineColumnTrackListener implements Run.Listener {
        public int line = 0; //0 means we havent started parsing the content yet, the first line in content is 1
        public int column = 0;

        public void updateLineAndColumn( State state, int runLength) {
            for (int i = 0; i < runLength; i++) {
                if (state.content.charAt(i) == '\n') {
                    ++line;
                    column = 1;
                } else {
                    line = Math.max(1, line);//we set the line to (1) because when we start reading it's 0
                    ++column;
                }
            }
        }

        @Override
        public void onRun(State state, ContentType runContentType, int runLength, boolean trailingSpace) {
            updateLineAndColumn(state, runLength);
        }

        public int getLine(){
            return line;
        }

        public int getColumn(){
            return column;
        }
    }
}
