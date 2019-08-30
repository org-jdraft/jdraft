package org.jdraft;

import java.util.*;
import java.util.function.Predicate;
import java.util.regex.Pattern;

/**
 * Template composing and representing a Structured String
 *
 * Compiles text (from markup) that has "named" blanks ($Names) that can be
 * filled (via key-values) or direct fill(in-lining).
 *
 * for example:
 * <PRE>
 * Stencil st = Stencil.of(
 *     "Hello $salutation$ $LastName$, ",
 *     "    Thanks for your purchase of $itemName$ for the price of $itemPrice$.",
 *     "We would like to keep our customers happy, so if you have any questions",
 *     "or comments, please contact $repName$ at $repContact$");
 *
 * //this will construct the key/VALUE pairs to the Stencil
 * String letter = st.construct( "salutation", "Mr.",
 *     "lastName", "DeFazio",
 *     "widget", "$250.00",
 *     "repName", "Ted Fergeson",
 *     "repContact", "ted@mywidget.net");
 *
 * //-OR- this will directly fill the blanks in order
 * String letter = st.fill("Mr.", "DeFazio", "$250.00", "Ted Fergeson","ted@mywidget.net");
 *
 * // Produces the string
 * Hello Mr. DeFazio,
 *     Thanks for your purchase of widget for the price of $250.00.
 * We would like to keep our customers happy, so if you have any questions
 * or comments, please contact Ted Fergeson at ted@mywidget.com
 * </PRE>
 *
 * @author Eric
 */
public final class Stencil implements Template<String>{

    /**
     * Keeps track of the blanks (parameters) and fixed text within the Stencils
     * (Holes where data can be filled in)
     * NOTE: there is a 1-to1 relationship between the blanks and
     * the $Names (each blank is associated with a parameter)
     */
    private final TextForm textForm;

    /**
     * the $Names names for PARAMETERS
     * (in the order they appear in the markup/binding (may contain duplicates)
     */
    private final List<String> $Names;

    /**
     * UNIQUE param names ordered where location in the Text
     */
    private final List<String> $NamesNormalized = new ArrayList<>();

    /**
     * returns a unique list of "normalized" $Names in the order
     * in which they occur within the Stencil (NO DUPLICATES)
     *
     * @return
     */
    @Override
    public List<String> list$Normalized() {
        return this.$NamesNormalized;
    }

    public Stencil(TextForm textForm, List<String> $names ) {
        this.textForm = textForm;
        this.$Names = $names;

        for (String $name : $names) {
            String normal = normalize$($name);
            if (!$NamesNormalized.contains(normal)) {
                $NamesNormalized.add(normal);
            }
        }
    }

    /**
     * If there are no blanks in the pattern (it's just fixed text)
     * @return true if the pattern is fixed (no blanks to be filled)
     */
    public boolean isFixedText(){
        return !this.textForm.hasBlanks();
    }
    
    /**
     * Returns the Predicate<String> for matching the Stencil as a String
     * for instance:
     * <PRE>
     * Stencil today = Stencil.of("Today is $dayOfWeek$ the $dayOfMonth$ day of $month$");
     * assertTrue( today.asPredicate().test( "today is Monday the 4th day of August"));
     * </PRE>
     * @return
     */
    public Predicate<String> asPredicate(){
        return this.getRegexPattern().asPredicate();
    }

    /**
     * Lists the $Names as they appear in the Stencil
     * (NOTE: contains possible duplicates and non_normalized params
     * (i.e. "Name" rather than "NAME")
     *
     * @return
     */
    @Override
    public List<String> list$() {
        return this.$Names;
    }

    /**
     * @return if the ENTIRE Stencil is a variable (with no fixed text)
     */ 
    public boolean isMatchAny(){
        return this.$Names.size() == 1 && 
            this.textForm.getFixedText() == null ||
            this.textForm.getFixedText().length() == 0;
    }
    
    /**
     *
     * @param code
     * @return the composite stencil
     */
    public static Stencil of( Object... code ) {
        StringBuilder sb = new StringBuilder();
        for( int i = 0; i < code.length; i++ ) {
            if( i > 0 ) {
                sb.append( System.lineSeparator() );
            }
            sb.append( Translator.DEFAULT_TRANSLATOR.translate( code[ i ] ) );
        }
        return Stencil.$Markup.of( sb.toString() );
    }

    /**
     * Combine many stencils together (end to end) and return a single Stencil
     *
     * @param stencils one or more stencils
     * @return the uber Stencil
     */
    public static Stencil of( Stencil... stencils ) {

        List<TextForm> textBlanks = new ArrayList<>();
        List<String> parameters = new ArrayList<>();
        for( Stencil stencil : stencils ) {
            textBlanks.add( stencil.textForm);
            parameters.addAll( stencil.$Names );
        }
        return new Stencil( TextForm.combine( textBlanks.toArray( new TextForm[ 0 ] ) ), parameters );
    }

    /**
     * Builds and returns a NEW STENCIL that changes the NAME of a parameter
     *
     * i.e.
     * Stencil s = Stencil.of("public void set$Int$( int $int$){this.$int$ = $int$; }");
     * Stencil r = s.rename$( "int", "NAME");
     * will change the Stencil (note: the param NAME MUST be normalized)
     *
     * r = Stencil.of("public void set$name$( int $name$){ this.$name$ = $name$;}");
     *
     * @param old$Name the old parameter NAME
     * @param new$Name the new parameter NAME
     * @return a ** NEW ** Stencil instance with the PARAMETERS renamed
     */
    public Stencil rename$( String old$Name, String new$Name ) {
        if( !$NamesNormalized.contains( old$Name ) ) {
            return this;
        }
        List<String> revisedParams = new ArrayList<>();
        for (String $Name : this.$Names) {
            String norm = normalize$($Name);
            if (norm.equals(old$Name)) {
                String match = $Name;
                String test = match;
                if (test.toUpperCase().equals(match)) {
                    revisedParams.add(new$Name.toUpperCase());
                } else if (Character.isUpperCase(match.charAt(0))) {
                    revisedParams.add(
                            Character.toUpperCase(new$Name.charAt(0))
                                    + new$Name.substring(1));
                } else {
                    revisedParams.add(new$Name);
                }
            } else {
                revisedParams.add($Name);
            }
        }
        return new Stencil( this.textForm, revisedParams );
    }

    /**
     * Internal A way to normalize the $names
     * @param $name
     * @return 
     */
    private static String normalize$( String $name ) {
        if( $name.toUpperCase().equals( $name ) ) {
            return $name.toLowerCase();
        }
        return Character.toLowerCase( $name.charAt( 0 ) ) + $name.substring( 1 );
    }

    @Override
    public boolean equals( Object o ) {
        if( o == null ) {
            return false;
        }
        if( !(o instanceof Stencil) ) {
            return false;
        }
        Stencil st = (Stencil)o;
        return this.textForm.equals( st.textForm)
                && this.$Names.equals( st.$Names );
    }

    @Override
    public int hashCode() {
        return Objects.hash(textForm, $Names );
    }

    /**
     * Internal parser for building up the Stencil
     * @param builder text blanks builder
     * @param $names the current names of things in the stencil
     * @param text the FULL text to be parsed
     * @param target the target tag
     * @param $name the tag being parsed
     */
    private static void parseStencil(
            TextForm.Builder builder, List<String> $names, String text,
            String target, String $name ) {

        int previous = 0;
        int cursor = text.indexOf( target );
        while( cursor > -1 ) {
            builder.text( text.substring( previous, cursor ) );
            builder.blank();
            $names.add( $name );
            previous = cursor + target.length(); //1; //text.length();
            cursor = text.indexOf( target, previous );
        }
        //adding the remaining text
        builder.text( text.substring( previous ) );
    }

    /**
     * Gets the text and blanks underlying the Stencils parameters
     * @return the text blanks for the stencil
     */
    public TextForm getTextForm() {
        return this.textForm;
    }

    /**
     * Creates & return a NEW STENCIL that parameterizes the "var" the given NAME
     * <PRE>
     * Stencil alpha = Stencil.of("abcdefghijklmnopqrstuvwxyz");
     * Stencil vowels = alpha.$("a").$("e").$("i").$("o").$("u");
     * will create a Stencil similar to:
     * Stencil.of("$a$bcd$e$fgh$i$jklmn$o$pqrst$u$vwmyz");
     *
     * ..where we parameterized the vowels...
     * Note: duplicate values will be parameterized
     * </PRE>
     * @param targetAndName
     * @return a ** NEW ** Stencil instance with the target parameterized
     */
    public Stencil $( String targetAndName ) {
        return $( targetAndName, targetAndName );
    }

    /**
     * Hardcode parameterized values
     * (i.e. what was once a parameter, now is static text)
     * Example:
     * <PRE>
     *     Stencil $letter = Stencil.of( "$letter$ is a $letterType$ and $case$");
     *     Stencil $vowels = $s.hardcode$("letterType", "vowel");
     *
     *     //the stencil returned has constant text "vowel" in place of $parameter "$letter$"
     *     assertEquals( $vowels, Stencil.of("$letter$ is a vowel and $case$"));
     *
     *     //the original stencil is unchanged
     *     assertEquals( $letter, Stencil.of("$letter$ is a $letterType$ and $case$"));
     * </PRE>
     *
     * @param kvs the key parameter NAME and String VALUE to assign to the
     * @return a ** NEW ** Stencil instance
     */
    public Stencil hardcode$( Tokens kvs ) {
        return hardcode$( Translator.DEFAULT_TRANSLATOR, kvs );
    }

    /**
     * Hardcode parameterized values
     * (i.e. what was once a parameter, now is static text)
     * Example:
     * <PRE>
     *     Stencil $letter = Stencil.of( "$letter$ is a $letterType$ and $case$");
     *     Stencil $vowels = $s.hardcode$("letterType", "vowel");
     *
     *     //the stencil returned has constant text "vowel" in place of $parameter "$letter$"
     *     assertEquals( $vowels, Stencil.of("$letter$ is a vowel and $case$"));
     *
     *     //the original stencil is unchanged
     *     assertEquals( $letter, Stencil.of("$letter$ is a $letterType$ and $case$"));
     * </PRE>
     *
     * @param keyValues the key parameter NAME and String VALUE to assign to the
     * @return a ** NEW ** Stencil instance
     */
    public Stencil hardcode$( Object... keyValues ) {
        return hardcode$( Translator.DEFAULT_TRANSLATOR, Tokens.of( keyValues ) );
    }

    /**
     * Hardcode parameterized values
     * (i.e. what was once a parameter, now is static text)
     * Example:
     * <PRE>
     *     Stencil $letter = Stencil.of( "$letter$ is a $letterType$ and $case$");
     *     Stencil $vowels = $s.hardcode$("letterType", "vowel");
     *
     *     //the stencil returned has constant text "vowel" in place of $parameter "$letter$"
     *     assertEquals( $vowels, Stencil.of("$letter$ is a vowel and $case$"));
     *
     *     //the original stencil is unchanged
     *     assertEquals( $letter, Stencil.of("$letter$ is a $letterType$ and $case$"));
     * </PRE>
     *
     * @param translator translates values to be hardcoded into the Stencil
     * @param keyValues the key parameter NAME and String VALUE to assign to the
     * @return a ** NEW ** Stencil instance
     */
    public Stencil hardcode$( Translator translator, Object... keyValues ) {
        return hardcode$( translator, Tokens.of( keyValues ) );
    }

    /**
     * <P>Assign/ fill in a constant VALUE for an existing $parameter
     * ************** and return the NEW Stencil instance*****************
     * (the opposite of $() method which parameterizes the instance of some text
     * within the
     * Stencil, hardcode$() will removeAll a parameter
     * for instance:
     * <PRE>
     * Stencil query = Stencil.of("SELECT $query$ FROM $from$ WHERE $where$");
     * query = query.hardcode$("FROM", "NyTable");
     * // query is now the following Stencil:
     * "SELECT $query$ FROM MyTable WHERE $where$"
     * </PRE>
     * </P>
     * 
     * @param translator translator for translating values to text for filling
     * the template
     * @param kvs tokens to assign
     * @return the NEW Stencil instance
     */
    public Stencil hardcode$( Translator translator, Tokens kvs ) {
        if( this.$Names.isEmpty() ) {
            return this;
        }
        TextForm.Builder tbb = new TextForm.Builder();
        List<String> new$Names = new ArrayList<>();
        for( int i = 0; i < this.$Names.size(); i++ ) {
            String text = this.getTextForm().getTextSegmentBeforeBlank( i );
            if( text.length() > 0 ) {
                tbb.text( text );
            }
            //now determine if we are assigning this variable
            Object val = kvs.get( $Names.get( i ) );
            if( val != null ) { //found assignment
                tbb.text( (String)translator.translate( val ) );
            } //firstCaps, allCaps, etc.?
            else {
                new$Names.add( $Names.get( i ) );
                tbb.blank();
            }
        }
        //check if there is trailing text
        String text = this.getTextForm().getTextSegmentAfterBlank( this.$Names.size() - 1 );
        if( text.length() > 0 ) {
            tbb.text( text );
        }
        return new Stencil( tbb.compile(), new$Names ); //, NESTS );
    }

    /**
     * Creates a NEW STENCIL that parameterizes the "target" the given $NAME
     * For example:
     * <PRE>
     * Stencil dna = Stencil.of("AACCGTTCAAAGGT");
     * Stencil varAni = dna.$("T", "typ");
     * //is like:
     * Stencil dna = Stencil.of("AACCG$typ$$typ$CAAAGG$typ$");
     *
     * You can also combine these in a single step:
     * Stencil s = Stencil.of("abcdefghijklmnopqrstuvwxyz")
     * .$("a","A").$("e", "E").$("i","I").$("o"."O").$("u","U");
     * Stencil s =
     *    Stencil.of("$a$bcd$e$fgh$i$jklmn$o$pqrst$u$vwxyz").$("y","sometimes");
     * </PRE>
     * @param target some static text to be parameterized
     * @param $name the NAME to assign to this parameter
     * @return a ** NEW ** Stencil instance with the target parameterized
     */
    @Override
    public Stencil $( String target, String $name ) {
        TextForm.Builder builder = new TextForm.Builder();
        List<String> params = new ArrayList<>();

        if( !textForm.hasBlanks() ) {
            String text = textForm.getFixedText();
            parseStencil( builder, params, text, target, $name );
        }
        for(int i = 0; i < textForm.getBlanksCount(); i++ ) {
            String text = textForm.getTextSegmentBeforeBlank( i );
            parseStencil( builder, params, text, target, $name );
            params.add( this.$Names.get( i ) );
            builder.blank();
        }
        String text = textForm.getTextSegmentAfterBlank( textForm.getBlanksCount() - 1 );
        parseStencil( builder, params, text, target, $name );

        return new Stencil( builder.compile(), params );
    }

    @Override
    public String draft(Translator translator, Map<String, Object> $nameValues ){

        Map<String, Object> combinedParams = new HashMap<>($nameValues);

        Object[] fills = inline( translator, $Names, combinedParams );
        return this.textForm.fill( fills );
    }

    /**
     *
     * @param translator translates parameter Objects to text
     * @param $names the names of PARAMETERS in the order to bind into the text
     * @param $nameValues NAME-> (object)VALUE binding data to be filled into
     * @return
     */
    private static Object[] inline(
            Translator translator, List<String> $names,
            Map<String, Object> $nameValues ) {

        Object[] fills = new Object[ $names.size() ];
        for( int i = 0; i < $names.size(); i++ ) {
            String p = $names.get( i );
            Object val = $nameValues.get( p );
            if( val == null ) {
                val = $nameValues.get( "$" + p + "$" );
            }
            fills[ i ] = val;
            if( val == null ) {
                Optional<String> matchFound = $nameValues.keySet().stream()
                        .filter( s -> s.equalsIgnoreCase( p ) )
                        .findFirst();

                if( matchFound.isPresent() ) {
                    String found = matchFound.get();
                    val = $nameValues.get( found );
                }
                if( val == null ) {
                    Optional<String> mFound = $nameValues.keySet().stream()
                            .filter( s -> s.equalsIgnoreCase( "$" + p + "$" ) )
                            .findFirst();

                    if( mFound.isPresent() ) {
                        String found = mFound.get();
                        val = $nameValues.get( found );
                    }
                }
                /*
                 * I could handle this where having the translator throw an
                 * exception
                 * RATHER than manually doing this.
                 */
                if( val == null ) {
                    throw new _draftException( "no \"" + p + "\" in " + $nameValues );
                }
                String original = p;
                if( p.toUpperCase().equals( original ) ) {
                    //it's all caps
                    val = translator.translate( val ).toString().toUpperCase();
                }
                else { //do firstCaps
                    String v = translator.translate( val ).toString();
                    val = Character.toUpperCase( v.charAt( 0 ) ) + v.substring( 1 );
                }
            }
            fills[ i ] = translator.translate( val );
        }
        return fills;
    }

    /**
     * Does the stencil pattern match the multi line string passed in
     * @param constructed
     * @return true if the stencil pattern matches, false otherwise
     */
    public boolean matches(String... constructed ) {
        return matches( Text.combine( constructed ) );
    }

    /**
     * Does the stencil pattern match the multi line string passed in
     * @param constructed
     * @return true if the stencil pattern matches, false otherwise
     */
    public boolean matches(String constructed ) {
        List<String> valsInOrder = this.textForm.parse( constructed );
        return (valsInOrder != null);
    }

    /**
     * Gets the regex pattern
     * @return
     */
    public Pattern getRegexPattern(){
        return this.textForm.getRegexPattern();
    }

    /**
     * If the stencil matches the (single) string input, deconstruct it into
     * Tokens (key-value pairs) from the fixed text or return null
     * 
     * @param constructed the String to be deconstructed
     * @return the Tokens or null if the constructed string doesn't match the stencil pattern
     */
    public Tokens parse(String constructed ){
        return parse( new String[]{constructed} );
    }
    
    /**
     * If the stencil matches the (single) string input, deconstruct it into
     * Tokens (key-value pairs) from the fixed text or return null
     * 
     * @param constructed the String to be deconstructed
     * @return the Tokens (or null if the constructed String doesnt match the stencil pattern)
     */
    public Tokens parse(String... constructed ) {
        List<String> valsInOrder = this.textForm.parse( Text.combine( constructed) );
        if( valsInOrder == null ) {
            return null;
        }
        Tokens paramValues = new Tokens();
        for( int i = 0; i < this.$Names.size(); i++ ) {
            Object previouslyExtractedVal = paramValues.get( this.$Names.get( i ) );
            //If I previously mapped a template VALUE, it MUST query or the template is invalid
            if( previouslyExtractedVal != null && !previouslyExtractedVal.equals( valsInOrder.get( i ) ) ) {
                return null;
            }
            paramValues.put( this.$Names.get( i ), valsInOrder.get( i ) );
        }
        return paramValues;
    }

    @Override
    public String toString() {
        Map<String, Object> fillMap = new HashMap<>();
        String[] fills = this.$Names.toArray( new String[ 0 ] );
        for( int i = 0; i < fills.length; i++ ) {
            fillMap.put( fills[ i ],  "$" + fills[ i ] +"$" );
        }

        Object[] fis = inline( Translator.DEFAULT_TRANSLATOR, $Names, fillMap );
        return this.textForm.fill( fis );
    }

    /**
     * Simple Markup with $ delimited $Names ($a$, $name$, $VALUE$)
     *
     * example:
     * <CODE>
     * Stencil s = $Markup.of("$name$, thanks for buying $product$");
     *
     * s.construct("product", "toaster", "NAME","Eric")//"Eric, thanks for buying toaster"
     * s.fill("Eric", "toaster") //"Eric, thanks for buying toaster"
     * </CODE>
     */
    public static class $Markup {

        public static Stencil of( String... markup ) {
            StringBuilder sb = new StringBuilder();
            for( int i = 0; i < markup.length; i++ ) {
                sb.append( markup[ i ] );
                if( i < markup.length - 1 ) {
                    sb.append( System.lineSeparator() );
                }
            }
            String code = sb.toString();
            int cursor = 0;
            int next = nextToken( code, cursor );

            TextForm.Builder bb = new TextForm.Builder();
            List<String> parameters = new ArrayList<>();

            while( next > 0 ) {
                String tok = code.substring( cursor, next );

                if( isValidParameter( tok ) ) {
                    bb.blank();
                    //with a String like "$a$" the token NAME is "a"
                    String param = tok.substring( 1, tok.length() - 1 );
                    parameters.add( param );
                    cursor = next;
                }
                else {
                    bb.text( tok );
                    cursor = next;
                }
                next = nextToken( code, cursor );
            }
            return new Stencil( bb.compile(), parameters );
        }

        private static boolean isValidParameter( String tok ) {
            if( tok.length() > 2 //"$$" is NOT a valid token
                    && tok.startsWith( "$" )
                    && tok.endsWith( "$" ) ) {

                if( !Character.isJavaIdentifierStart( tok.charAt( 1 ) ) ) {
                    return false;
                }
                for( int i = 2; i < tok.length(); i++ ) {
                    if( !Character.isJavaIdentifierPart( tok.charAt( i ) ) ) {
                        return false;
                    }
                }
                return true;
            }
            return false;
        }

        private static int nextToken( String code, int cursor ) {
            if( cursor >= code.length() ) {
                return -1;
            }
            if( code.charAt( cursor ) == '$' ) { //cursor is ON a parameter
                int end = code.indexOf( '$', cursor + 1 );
                if( end < 0 ) { //not found
                    return code.length();
                }
                if( isValidParameter( code.substring( cursor, end + 1 ) ) ) {
                    return end + 1;
                }
                return end; //return the data up until the next $

            }
            int end = code.indexOf( '$', cursor + 1 );
            if( end < 0 ) {
                end = code.length();
            }
            return end;
        }
    }
}
