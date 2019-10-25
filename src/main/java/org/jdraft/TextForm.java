package org.jdraft;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Builds a "FillInTheBlanks"-TYPE text document containing static text
 * and "blanks" at certain character indexes which can be filled.
 *
 * In general it's a simple "Form" made of static text and "blanks" where
 * text can later be filled in.
 *
 * <UL>
 * <LI>the {@code Builder} captures text and "blanks" ("indexes" where "blanks"
 * will be that may be filled in later) the <B>builder is not thread-safe</B>.
 * (It is the "larval" stage ofLines the object, when the document is completed, it
 * can be compiled() to a thread safe entity). <LI>The {@code TextBind} allows
 * many threads to share and populate unique documents where "filling in the
 * blanks" with the {@code fill(...)} METHODS.
 * </UL>
 *
 * <H3>Usage Example:</H3><BR>
 * <PRE><CODE>
 * TextBlanks s = TextBlanks.of( null, null, null, null, null ); //5 blanks
 * System.out.println ( s.fill("A", "B", "C", "D", "E") ); //produces "ABCDE"
 * System.out.println ( s.fill(" ", " ", " ", " ", " ") ); //produces "     "
 * System.out.println ( s.fill("Don't ", "disturb ", "the ", "man ", "outside") );
 * //produces "Don't disturb the man outside"
 * TextBlanks c2 = TextBlanks.of( "four score and ", null, " years ago");
 *
 * System.out.println(c2.fill("seven"));//"four score and seven years ago"
 * System.out.println(c2.fill("7")); //"four score and 7 years ago"
 </CODE></PRE>
 *
 * @author M. Eric DeFazio
 */
public final class TextForm {

    /**
     * All the "Static" Text for the Sequence For instance if we had:
     * <PRE>
     * TextBind s = TextBind.of(
     * "Mary had a ", null, " lamb,",null," lamb,",null," lamb");
     * </PRE> the text would be: text = "Mary had a lamb, lamb, lamb";
     */
    private final String fixedText;

    /**
     * Indexes within the Static text where text is to be inserted For
     * instance if we had:
     * <PRE>
     * TextBind s = TextBind.of(
     * "Mary had a ", null, " lamb,",null," lamb,",null," lamb");
     *
     * the text would be:
     * text = "Mary had a  lamb,  lamb,  lamb";
     *
     * there are "Blanks" where test can be inserted at indexes:
     *
     * text = "Mary had a  lamb,  lamb,  lamb";
     *                    ^      ^      ^
     *                   [10]   [17]   [24]
     *
     * so we represent all ofLines the characters in the fill in the blanks as a
     * BitSet, where a 0 means a static character in {@code text}, and a 1
     * means a "blank" where text can be inserted, so we end up with a
     * BitSet with the following contents:
     *
     * text = "Mary had a  lamb,  lamb,  lamb";
     *         000000000010000001000000100000   // NOTE: string indexes goes Left to right
     *                                          // i.e. "M" is at text[0], "a" is at text[1]
     *
     *         000000000010000001000000100000   // so the binary stored in the BitSet is actually
     *                                          // transposed (if we wanted to look at the binary VALUE):
     *         000001000000100000010000000000
     *         ^                            ^
     *         |                            |
     *        [29]                         [0]
     *
     *  how each character matches the binary is:
     *
     *  "Mary had a  lamb,  lamb,  lamb" (transpose the indexes,
     *  "bmal  ,bmal  ,bmal  a dah yraM"
     *   000001000000100000010000000000 //1s for "blanks"
     *   ^                            ^
     *   |                            |
     *  [29]                         [0]
     */
    private final BitSet blankIndexes;

    /** Lazy regex pattern for extracting blank values from pre-rendered Text */
    private Pattern pattern;

    /**
     * Builds a textform with a single blank (no text)
     * @return
     */
    public static TextForm of(){
        BitSet bs = new BitSet(1);
        bs.set(0);
        return new TextForm("", bs);
    }

    /**
     * Uses the Builder to _1_build a TextBlanks...
     *
     * Here is the BLANK Rules:
     * <OL>
     * <LI> Rule: (NULL ADDS BLANK)<BR>
     * if we encounter a null element, <B>ADD A BLANK</B>
     * <LI> Rule: (BLANKS BETWEEN NON-NULL ELEMENTS)<BR>
     * if we encounter a non-null String element, <BR>
     * AND there is another element AFTER it <BR>
     * AND the element AFTER it is NON-NULL <BR>
     * <B>ADD A BLANK</B>
     * </OL>
     *
     * Use Cases: Rule 1) (Add a blank when null element encountered)
     * <PRE>TextBind FIVE_BLANKS = TextBind.of( null, null, null, null, null);</PRE>
     *
     * To accept a double-blank (or multiple blanks in a row) use a null
     * <PRE>
     *
     * </PRE>
     *
     * @param textOrNullBlanks PARAMETERS, text is static, null typesEqual a blank
     * @return a TextBlanks
     */
    public static TextForm of(String... textOrNullBlanks ) {
        Builder builder = new Builder();

        if( textOrNullBlanks == null ) {
            builder.blank();
            return builder.compile();
        }
        for( int i = 0; i < textOrNullBlanks.length; i++ ) {
            if( textOrNullBlanks[ i ] == null ) {
                builder.blank();
            }
            else {
                builder.text(textOrNullBlanks[ i ] );
                if( i < textOrNullBlanks.length - 1 && textOrNullBlanks[ i + 1 ] != null ) {
                    //accept a blank AFTER text provided there is ANOTHER textOrNullBlanks element AFTER this one
                    builder.blank();
                }
            }
        }
        return builder.compile();
    }

    /**
     * Builds a TestBlanks with the fixedText and a bitSet representing character
     * positions of the blanks within the fixedText
     * @param fixedText the static text of the text blanks
     * @param blankIndexes the character positions where "blanks" are to be filled
     * within the document
     */
    public TextForm(String fixedText, BitSet blankIndexes ) {
        this.fixedText = fixedText;
        this.blankIndexes = blankIndexes;
    }

    @Override
    public boolean equals( Object o ){
        if( o == null){
            return false;
        }
        if( !( o instanceof TextForm)){
            return false;
        }
        TextForm tb = (TextForm)o;
        return this.fixedText.equals(tb.fixedText) && this.blankIndexes.equals(tb.blankIndexes);
    }

    @Override
    public int hashCode() {
        return Objects.hash(fixedText, blankIndexes);
    }

    /**
     * Builds the regex pattern to represent the textblanks (where the static 
     * text is and where the blanks are)
     * @return the regex pattern
     */
    public Pattern getRegexPattern(){
        if( this.pattern == null ){
            this.pattern = Pattern.compile( buildRegexPattern( this ), Pattern.DOTALL );
        }
        return this.pattern;
    }

    /**
     * Given a TextBlanks, return a String representing a Regular Expression pattern
     * @param tb a textBlanks
     * @return the String that can be used as a Regex pattern
     */
    public static String buildRegexPattern( TextForm tb ){
        int currentTextCharAt = 0; //current char index in the document
        int previousBlankAt = -1; // previous blank index in the document

        int nextBlankAt = tb.getBlanks().nextSetBit( 0 );
        int charsBetweenCount
                = (nextBlankAt - previousBlankAt) - 1; //chars BETWEEN previous blank and next blank

        StringBuilder regexPattern = new StringBuilder();

        while( nextBlankAt >= 0 ) {
            if( charsBetweenCount > 0 ) {
                //there is text between the previous blank and the next blank(need to fill in the text)
                String prepend = tb.getFixedText().substring(
                        currentTextCharAt,
                        currentTextCharAt + charsBetweenCount
                );
                regexPattern.append("(").append(Pattern.quote(prepend)).append(")"); //add a group of static text
                //out.add( prepend );
                currentTextCharAt += charsBetweenCount;
            }
            regexPattern.append("(.*?)"); //(.*?) any "blank"

            previousBlankAt = nextBlankAt;
            nextBlankAt = tb.getBlanks().nextSetBit( nextBlankAt + 1 );
            charsBetweenCount = (nextBlankAt - previousBlankAt) - 1; //
        }
        String tail = tb.getFixedText().substring( currentTextCharAt );
        if( tail.length() > 0 ){
            regexPattern.append("(").append(Pattern.quote(tail)).append(")");
        } else{
            //since it MAY ends with a blank... we need to change the (.*?) to a (.*) capturing
            if( regexPattern.toString().endsWith("(.*?)")){
                regexPattern = new StringBuilder(regexPattern.substring(0, regexPattern.length() - "(.*?)".length()));
                regexPattern.append("(.*)");
            }
        }
        return regexPattern.toString();
    }

    /**
     * Combines many {@code TextBlank}s into a single {@code TextBlank} in order
     *
     * @param textForms multiple textBlanks to sequence into one
     * @return a single {@link TextForm}
     */
    public static TextForm combine(TextForm... textForms ) {
        Builder builder = new Builder();
        for( TextForm thisTemplate : textForms ) {
            int blankCountInThisTemplate = thisTemplate.getBlanksCount();
            if( blankCountInThisTemplate == 0 ) {
                builder.text( thisTemplate.getFixedText() );
            }
            else {
                for( int b = 0; b < blankCountInThisTemplate; b++ ) {
                    String staticText = thisTemplate.getTextSegmentBeforeBlank( b );
                    builder.text( staticText );
                    builder.blank();
                }
            }
            builder.text( thisTemplate.getTextSegmentAfterBlank( blankCountInThisTemplate - 1 ) );
        }
        return builder.compile();
    }

    /**
     * Is the first thing in the TextForm a character?
     * @return true if the first thing in the text is a character (NOT a blank)
     */
    public boolean startsWithText(){
        return !this.blankIndexes.get(0);
    }

    /**
     * Is the first thing in the TextForm a blank?
     * (if the TextForm starts with a blank it is said to be "open" on the left)
     * @return true if the first thing in the text is a blank (not a textual char)
     */
    public boolean startsWithBlank(){
        return this.blankIndexes.get(0);
    }

    /**
     * Returns a list of textual segments that make up the text
     * @return a list of text segments
     */
    public List<String> getTextSegments(){
        List<String> segs = new ArrayList<>();
        for(int i=0;i<this.getBlanksCount() +1;i++){
            String nt = getTextSegmentBeforeBlank(i);
            if( nt.length() > 0 ){
                segs.add( nt );
            }
        }
        String nt = getTextSegmentAfterBlank( this.getBlanksCount() -1);
        if( nt.length() > 0 ){
            segs.add( nt );
        }
        return segs;
    }

    /**
     * Does this TextForm have ANY blanks?
     * @return true if there are ANY blanks in the TextForm
     */
    public boolean hasBlanks() {
        return blankIndexes.cardinality() > 0;
    }

    /**
     * Fill the blanks of the TextForm matching the order of the blanks with the fills
     * @param fillsInOrder the fills objects in order of
     * @return the filled in TextBlanks
     */
    public String fill( Object... fillsInOrder ) {
        return fill( Translator.DEFAULT_TRANSLATOR, fillsInOrder );
    }

    /**
     * fills the TextForm and returns the a String
     *
     * @param translator translates input PARAMETERS objects to textual representations
     * @param fillsInOrder the files to be put into the Template in order
     * @return the Filled in template
     */
    public String fill(Translator translator, Object... fillsInOrder)
            throws _jdraftException {

        StringBuilder out = new StringBuilder();
        if( fillsInOrder == null ) {
            if( blankIndexes.cardinality() != 1 ) {
                throw new _jdraftException(
                        "fill parameter count (1) must accept blanks count ("
                                + blankIndexes.cardinality() + ")" );
            }
            fillsInOrder = new Object[]{ null };
        }
        if( fillsInOrder.length < blankIndexes.cardinality() ) {
            throw new _jdraftException( "fill parameter count ("
                    + fillsInOrder.length + ") must accept blanks count ("
                    + blankIndexes.cardinality() + ")" );
        }

        int currentTextCharAt = 0; //current char index in the document
        int previousBlankAt = -1; // previous blank index in the document

        int nextBlankAt = blankIndexes.nextSetBit( 0 );
        int fillIndex = 0;
        int charsBetweenCount = (nextBlankAt - previousBlankAt) - 1; //chars BETWEEN previous blank and next blank

        while( nextBlankAt >= 0 ) {
            if( charsBetweenCount > 0 ) {
                //there is text between the previous blank and the next blank
                //(need to fill in the text)
                String prepend = fixedText.substring(
                        currentTextCharAt,
                        currentTextCharAt + charsBetweenCount);
                out.append(prepend );
                currentTextCharAt += charsBetweenCount;
            }
            //fill in the next blank
            if( fillsInOrder[ fillIndex ] != null ) {   //we only fill if non-null (i,.e. dont accept the string "null"
                //out.add( fillsInOrder[ fillIndex ] );
                out.append( translator.translate( fillsInOrder[ fillIndex ] ) );
            }
            fillIndex++;

            previousBlankAt = nextBlankAt;
            nextBlankAt = blankIndexes.nextSetBit( nextBlankAt + 1 );
            charsBetweenCount = (nextBlankAt - previousBlankAt) - 1; //
        }
        out.append( fixedText.substring( currentTextCharAt ) );
        return out.toString();
    }

    /**
     * Returns the fixed (unchanging) text
     *
     * @return
     */
    public String getFixedText() {
        return this.fixedText;
    }

    /**
     * Returns the Fixed Text and the blanks annotated as {<1>, <2>, <3>,...}
     *
     * @return the String representation of Lines the TextForm
     */
    @Override
    public String toString() {
        Object[] fillMarkers = new String[ blankIndexes.cardinality() ];
        for( int i = 0; i < fillMarkers.length; i++ ) {
            fillMarkers[ i ] = "$" + (i + 1) + "$";
        }
        return fill( Translator.DEFAULT_TRANSLATOR, fillMarkers );
    }

    /**
     * the number of blanks in the TextForm
     *
     * @return count of blanks in the TextForm
     */
    public int getBlanksCount() {
        return blankIndexes.cardinality();
    }

    /**
     * BitSet where set bits represent a "blank" to be filled where text.
     * each 0 bit corresponds to a fixed character
     * @return
     */
    public BitSet getBlanks() {
        return this.blankIndexes;
    }

    /**
     * Count to the nth blank, and return the character index within the
     * TextForm
     * {@code text} where the blank would be placed
     *
     * @param index the index ofLines the blank (0-based) {0, 1,,2, ...}
     * @return the character index within the {@code text} where the blank
     * would be
     */
    public int getCharIndexOfBlank( int index ) {
        if( index >= this.blankIndexes.cardinality() || index < 0 ) {
            return -1;
        }
        //find the index ofLines the first blank
        int blankIndex = this.blankIndexes.nextSetBit( 0 );

        //iterate until we find the location ofLines the indexth blank
        for( int i = 1; i <= index; i++ ) {   //go to the index ofLines the NEXT BLANK
            blankIndex = this.blankIndexes.nextSetBit( blankIndex + 1 );
        }
        if( index == 0 ) {
            return blankIndex;
        }
        // we need to ADJUST the fillIndex to be a character index,
        // since the String staticText does not contain ANY character
        // place holder for a blank, so we need to backup where index
        // characters
        return blankIndex - (index);
    }

    /**
     * Retrieves the Text Before the {@code index}<SUP>th</SUP> blank
     * (0-based) for example, Assume we had the following (where __
     * represents a Blank)
     * <PRE>
     * FillOrder fo = FillInTheBlanks.parse(
     * "__ sells __ shells __ the __ shore");
     * //      [0]      [1]       [2]    [3]
     *
     * String first fo.getTextBeforeBlank( 0 ); // ""
     * String second fo.getTextBeforeBlank( 1 ); // " sells "
     * String third fo.getTextBeforeBlank( 2 ); // " shells "
     * String fourth fo.getTextBeforeBlank( 3 ); // " the "
     *
     * String fifth fo.getTextAfterBlank( 4 ); // ""
     * String hundreth fo.getTextAfterBlank( 100 ); // ""
     * </PRE>
     *
     * @param blankIndex the 0-based blank index that demarcates the text to retrieve
     * @return the text between the {@index}<SUP>th</SUP> blank and the blank after (or the remaining text
     * if the {@index}<SUP>th</SUP> blank is the last blank
     */
    public String getTextSegmentBeforeBlank(int blankIndex ) {
        if( getBlanksCount() == 0 ) {
            return fixedText;
        }
        if( blankIndex >= getBlanksCount() ) {
            return "";
        }
        if( blankIndex < 0 ) {
            return "";
        }
        if( blankIndex == 0 ) {
            int end = getCharIndexOfBlank( blankIndex );
            return getFixedText().substring( 0, end );
        }
        int start = getCharIndexOfBlank( blankIndex - 1 );
        int end = getCharIndexOfBlank( blankIndex );
        return getFixedText().substring( start, end );
    }

    /**
     * Retrieves the Text After the {@code index}<SUP>th</SUP> blank
     * (0-based)
     *
     * for example, Assume we had the following (where __ represents a
     * Blank)
     * <PRE>
     * FillOrder fo = FillInTheBlanks.parse(
     * "__ sells __ shells __ the __ shore");
     * //  [0]      [1]       [2]    [3]
     *
     * String first fo.getTextAfterBlank( 0 ); // " sells "
     * String second fo.getTextAfterBlank( 1 ); // " shells "
     * String third fo.getTextAfterBlank( 2 ); // " the "
     * String fourth fo.getTextAfterBlank( 3 ); // " shore"
     *
     * String fifth fo.getTextAfterBlank( 4 ); // ""
     * String hundreth fo.getTextAfterBlank( 100 ); // ""
     * </PRE>
     *
     * @param blankIndex the 0-based blank index that demarcates the text to
     * retrieve
     * @return the text between the {@index}<SUP>th</SUP> blank and the blank after (or the remaining text
     * if the {@index}<SUP>th</SUP> blank is the last blank
     */
    public String getTextSegmentAfterBlank(int blankIndex ) {
        if( blankIndex >= getBlanksCount() ) {
            return "";
        }
        if( blankIndex < 0 ) {
            return "";
        }
        if( blankIndex == getBlanksCount() - 1 ) { //last blank
            int start = getCharIndexOfBlank( blankIndex );
            return getFixedText().substring( start );
        }
        int start = getCharIndexOfBlank( blankIndex );
        int end = getCharIndexOfBlank( blankIndex + 1 );
        return getFixedText().substring( start, end );
    }

    /**
     * Determines if the TextForm has (2) or more blanks immediately next to
     * each other i.e. TextForm.of("a", null, null, "b");
     *
     * This presents problems if we try to use a regex pattern to match
     * since there is no way of knowing where one blank ends and the next blank 
     * starts since there are no delimiter(s) between two consecutive blanks
     * @return true if this TextBlanks has one or more -- (blanks with no spaces/
     * text/characters between)
     */
    public boolean hasConsecutiveBlanks(){
        int setBit = -2;
        int nxtSetBit =  this.blankIndexes.nextSetBit(0);
        while(  nxtSetBit > 0 ) {
            if( nxtSetBit == (setBit+1) ){
                return true;
            }
            setBit = nxtSetBit;
            nxtSetBit = this.blankIndexes.nextSetBit(setBit + 1);
        }
        return false;
    }

    /**
     * Given a filled in "form" as a String, check that this form matches with the regexPattern
     * derived from this TextBlanks, then return a List of Strings that represent the values at
     * the filled the blanks. (Or return null if the decomposition is unsucessful)
     * Given a String representing a filled-in textblanks, the data that was used to
     * fill in the blanks.
     *
     * for example:
     * <PRE>
     * TextBlanks form =  TextBlanks.of(null," is ",null," back from the ",null,"?");
     * String constructed =             "Eric is Sally back from the store?"
     * List<String> deconstructed = tb.deconstruct(constructed);
     * // tb represents the TextBlanks:
     * //   ____ is _____ back from the _____?   //The TextBlanks form
     * //  "Eric is Sally back from the store?"  //constructed
     * // {"Eric", "Sally",             "store"} //the Deconstructed List
     *
     * </PRE>
     * NOTE: for this to work the TextBlanks CANNOT have consecutive blanks (two blanks with no characters between)
     * otherwise we can never match
     * @param constructed the constructed Text to deconstruct
     * @return a List of Strings representing the values that exist where the blanks would be, in the order they are
     * extracted from the constructed text
     */
    public List<String> parse(String constructed ){

        if( this.hasConsecutiveBlanks() ){
            return null; //cant listAnyMatch which
        }
        Pattern pat = this.getRegexPattern();
        Matcher matcher = pat.matcher( constructed);
        if( !matcher.matches() ){
            return null;
        }

        List<String> ext = new ArrayList<>();
        //if... the pattern is "anything" return the whole thing;
        if( pat.pattern().equals("(.*)") ){
            ext.add(constructed);
            return ext;
        }
        int startIndex = 1;
        if( !this.startsWithBlank() ){
            startIndex = 2;
        }

        for(int i=startIndex; i<= matcher.groupCount(); i+=2 ){
            ext.add(matcher.group(i));
        }
        return ext;
    }

    /**
     * Is there any fixed text after the last blank?
     * (If not the TextForm is "open" on the right)
     */
    public boolean endsWithBlank() {
        if( this.getBlanksCount() > 0 ) {
            return getTextSegmentAfterBlank(this.getBlanksCount() - 1).length() == 0;
        }
        return false;
    }

    /**
     * NOTE: INTENTIONALLY NOT THREAD SAFE!!
     * (use a separate builder when creating a Builder())
     * Incremental "builder" ofLines the larval stage ofLines the
     * TextBind document it is STATEFUL
     */
    public static class Builder {

        private final StringBuilder sb;

        /** translate between some object and some text within the Builder*/
        private final Translator translator;

        private int cursorIndex;

        /**where blanks should be*/
        private final BitSet blankIndexes;

        public Builder() {
            this( Translator.DEFAULT_TRANSLATOR);
        }

        public Builder( String... strings ) {
            this( Translator.DEFAULT_TRANSLATOR, strings );
        }

        public Builder(Translator translator, String... strings ) {
            this.translator = translator;
            this.blankIndexes = new BitSet();
            sb = new StringBuilder();
            cursorIndex = 0;
            of( strings );
        }

        public final Builder of( String... params ) {
            for( String param : params ) {
                if( param == null ) {
                    blank();
                }
                else {
                    text( param );
                }
            }
            return this;
        }

        public Builder object( Object object ) {
            if( object == null ) {
                return this;
            }
            String stringified = this.translator.translate( object ).toString();
            return text( stringified );
        }

        /**
         * ADDS text to the TextBlanks and returns the updated builder
         *
         * @param staticText text to accept
         * @return return the builder
         */
        public Builder text( String staticText ) {
            if( staticText == null ) {
                return this;
            }
            cursorIndex += staticText.length();
            sb.append( staticText ); //Text.replaceComment( staticText ) );
            return this;
        }

        public Builder blank() {
            blankIndexes.set( cursorIndex );
            cursorIndex++;
            return this;
        }

        public TextForm compile() {
            return new TextForm( sb.toString(), blankIndexes );
        }
    }
}

