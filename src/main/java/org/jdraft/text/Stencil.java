package org.jdraft.text;

import com.github.javaparser.ast.stmt.Statement;
import org.jdraft._jdraftException;
import org.jdraft.io._ioException;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;
import java.util.function.Predicate;
import java.util.regex.MatchResult;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * Template composing or parsing a Structured String
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
 * @see TextForm
 * @see Template
 *
 *
 * @author Eric
 */
public final class Stencil implements Template<String>{

    /**
     * Keeps track of the blanks (parameters/Embeds) and fixed text within the Stencils
     * (Holes where data can be filled in)
     * NOTE: there is a 1-to-1 relationship between the blanks and
     * the $Names (each blank is associated with a $Name, some $Names can be associated with {@link Embed}s)
     */
    private final TextForm textForm;

    /**
     * the identifiers (i.e. "Hello, $Name$" where "Name" is a $Name)
     * in the order they appear in the markup/binding (may contain duplicates)
     */
    private final List<String> $Names;

    /**
     * A List of Embeds (Embedded Stencils) that appear in the order they appear in the markup /binding
     * (i.e. "$$a: an embedded stencil $within$ a $Stencil$ :$$")
     */
    private final List<Embed> embeds;

    /**
     * UNIQUE param names ordered where location in the Text
     */
    private final List<String> $NamesNormalized = new ArrayList<>();

    /**
     * returns a unique list of "normalized" $Names in the order
     * in which they occur within the Stencil (NO DUPLICATES)
     *
     * @return a list of "Normalized" $ names (i.e. "Name" is converted to "name")
     */
    @Override
    public List<String> $listNormalized() {
        return this.$NamesNormalized;
    }


    public Stencil(TextForm textForm, List<String> $names, List<Embed> embeds) {
        this.textForm = textForm;
        this.$Names = $names;

        for (String $name : $names) {
            String normal = normalize$($name);
            if (!$NamesNormalized.contains(normal)) {
                $NamesNormalized.add(normal);
            }
        }
        this.embeds = embeds;
    }

    public Stencil copy(){
        return Stencil.of( this );
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
     * @return a list of all (NON-NORMALIZED i.e. $name =/= $Name) $ parameter names
     */
    @Override
    public List<String> $list() {
        return this.$Names;
    }

    /**
     * Returns the list of embedded Stencils within this Stencil
     * @return
     */
    public List<Embed> listEmbeds(){
        return this.embeds;
    }

    /**
     * Gets the embed(ed Stencil) by the given name or null if not found
     * @param name the name of the {@link Embed}ed Stencil
     * @return the Embed based on the name, or null if not found
     */
    public Embed getEmbed(String name){
        Optional<Embed> oe = this.embeds.stream().filter(e-> e.name.equals(name) ).findFirst();
        if( oe.isPresent()){
            return oe.get();
        }
        return null;
    }

    /**
     *
     * @param name
     * @param unparsedStencil
     * @return
     */
    public Stencil setEmbed(String name, String unparsedStencil){
        Stencil em = Stencil.of(unparsedStencil);
        return setEmbed(name, em);
    }

    public Stencil setEmbed(String name, Stencil stencil){
        Embed e = getEmbed(name);
        if( e != null ){
            e.stencil = stencil;
            return this;
        }
        else{
            throw new _jdraftException("no embed found with name "+ name);
        }
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
     * Read in the contents of the file at the Path and return the Stencil for it
     * @param path the path to read the file contents from
     * @return the Stencil
     */
    public static Stencil of( Path path){
        try {
            String s = new String(Files.readAllBytes(path));
            return of( s );
        }catch(IOException ioe){
            throw new _ioException("unable to read from path "+path.toString(), ioe);
        }
    }
    /**
     *
     * @param code an array of individual lines of text that will be "stencil-ized"
     * @return the composite stencil
     */
    public static Stencil of( Object... code ) {
        StringBuilder sb = new StringBuilder();
        for( int i = 0; i < code.length; i++ ) {
            if( i > 0 ) {
                sb.append( System.lineSeparator() );
            }
            if( code[i] != null && String.class.isAssignableFrom( code[i].getClass() )){
                sb.append(code[i]);
            } else {
                sb.append(Translator.DEFAULT_TRANSLATOR.translate(code[i]));
            }
        }
        return Stencil.$Markup.of( sb.toString() );
    }

    /**
     * Combine many stencils together (end to end) and return a single Stencil
     *
     * @param stencils one or more stencils
     * @return the uber-Stencil
     */
    public static Stencil of( Stencil... stencils ) {

        List<TextForm> textBlanks = new ArrayList<>();
        List<String> parameters = new ArrayList<>();
        List<Embed> embedStencils = new ArrayList<>();
        for( Stencil stencil : stencils ) {
            textBlanks.add( stencil.textForm.copy());
            parameters.addAll( stencil.$Names );
            stencil.embeds.forEach(es-> embedStencils.add( es.copy()));
            //embeddedStencils.addAll( stencil.embeddedStencils);
        }
        return new Stencil( TextForm.combine( textBlanks.toArray( new TextForm[ 0 ] ) ), parameters, embedStencils);
    }

    /**
     * Builds and returns a NEW STENCIL that changes the NAME of a $parameter and the
     * Embedded Stencils
     *
     * i.e.
     * Stencil s = Stencil.of("public void set$Int$( int $int$){this.$int$ = $int$; }");
     * Stencil r = s.rename$( "int", "NAME");
     * will change the Stencil (note: the param NAME MUST be normalized)
     *
     * r = Stencil.of("public void set$Name$( int $name$){ this.$name$ = $name$;}");
     *
     * @param old$Name the old $ parameter name
     * @param new$Name the new $ parameter name
     * @return a ** NEW ** Stencil instance with the PARAMETERS renamed
     */
    public Stencil rename$( String old$Name, String new$Name ) {
        List<Embed> revisedEmbed = new ArrayList<>();
        //add copies of the embedded stencils
        this.embeds.forEach(es -> revisedEmbed.add(es.copy()));
        //update parameters
        List<String> revisedParams = new ArrayList<>();

        /*
        if( !$NamesNormalized.contains( old$Name ) ) {
            this.embeddedStencils.forEach(es-> es.rename$(old$Name, new$Name));
            //return this;
            return new Stencil( this.textForm, revisedParams, embeddedStencils );
        }
         */

        revisedEmbed.forEach(es-> es.rename$(old$Name, new$Name));
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
                //revisedEmbedded.forEach(es-> es.rename$(old$Name, new$Name));
            }
        }
        //update embeddeds
        //revisedParams.forEach();
        return new Stencil( this.textForm.copy(), revisedParams, revisedEmbed);
    }

    /**
     * Internal way to normalize the $ names
     * @param $name the $name (parameter name)
     * @return the normalized String representing a unique $ variable
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
     * @param targetAndName the target name AND the $Name (param name) of the variable
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
    public Stencil $hardcode(Tokens kvs ) {
        return $hardcode( Translator.DEFAULT_TRANSLATOR, kvs );
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
    public Stencil $hardcode(Object... keyValues ) {
        return $hardcode( Translator.DEFAULT_TRANSLATOR, Tokens.of( keyValues ) );
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
    public Stencil $hardcode(Translator translator, Object... keyValues ) {
        return $hardcode( translator, Tokens.of( keyValues ) );
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
     * @return the *NEW* Stencil instance
     */
    public Stencil $hardcode(Translator translator, Tokens kvs ) {
        if( this.$Names.isEmpty() ) {
           return of(this);
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
        //
        List<Embed> embeds = new ArrayList<>();
        this.embeds.forEach(es->embeds.add(es.copy().hardcode$(translator, kvs)));
        return new Stencil( tbb.compile(), new$Names, embeds ); //, NESTS );
    }

    public Stencil $( String t1, String $n1, String t2, String $n2) {
        Stencil t = $(t1,$n1);
        t = t.$(t2,$n2);
        return t;
    }

    public Stencil $( String t1, String $n1, String t2, String $n2, String t3, String $n3) {
        Stencil t = $(t1,$n1);
        t = t.$(t2,$n2);
        t = t.$(t3,$n3);
        return t;
    }

    public Stencil $( String t1, String $n1, String t2, String $n2, String t3, String $n3, String t4, String $n4 ) {
        Stencil t = $(t1,$n1);
        t = t.$(t2,$n2);
        t = t.$(t3,$n3);
        t = t.$(t4,$n4);
        return t;
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

        List<Embed> embeds = new ArrayList<>();
        this.embeds.forEach(e -> embeds.add( e.$(target, $name)));
        return new Stencil( builder.compile(), params, embeds );
    }

    @Override
    public Stencil $hardcode(Translator translator, Map<String, Object> keyValues) {
        return $hardcode(translator, Tokens.of(keyValues));
    }

    @Override
    public String draft(Translator translator, Map<String, Object> $nameValues ){

        //Map<String, Object> combinedParams = new HashMap<>($nameValues);

        /** MED: Im adding defaults for Embeds */
        Map<String,Object> combinedParams = new HashMap<>();

        //System.out.println( "EMBEDS "+ this.embeds);

        this.listEmbeds().forEach(e -> {
            //System.out.println("   EMBED " + e );
            if( combinedParams.get(e.name) == null){
                combinedParams.put(e.name, "");
            }
        });
        combinedParams.putAll($nameValues);
        //System.out.println( this );
        Object[] fills = inline( translator, $Names, embeds, combinedParams );
        return this.textForm.fill( fills );
    }

    /**
     *
     * @param translator translates parameter Objects to text
     * @param $names the names of PARAMETERS in the order to bind into the text
     * @param embeds the embedded stencils within this stencil
     * @param $nameValues NAME-> (object)VALUE binding data to be filled into
     * @return
     */
    private static Object[] inline( Translator translator, List<String> $names, List<Embed> embeds, Map<String, Object> $nameValues ) {

        Object[] fills = new Object[ $names.size() ];
        for( int i = 0; i < $names.size(); i++ ) {
            String p = $names.get( i );
            Object val = $nameValues.get( p );

            //is the p(parameter) referring to an embed (or just a flat value?)
            //System.out.println("Looking for Embed \""+p+"\" in "+ $nameValues);
            Optional<Embed> em = embeds.stream().filter(e-> e.name.equals(p)).findFirst();
            if( em.isPresent() ){
                //System.out.println("Found embed \""+p+"\"");
                String drafted = em.get().draft(val, $nameValues);
                fills[i] = drafted;
            }
            else { //its a parameter
                if (val == null) {
                    val = $nameValues.get("$" + p + "$");
                }
                fills[i] = val;
                if (val == null) {
                    Optional<String> matchFound = $nameValues.keySet().stream()
                            .filter(s -> s.equalsIgnoreCase(p))
                            .findFirst();

                    if (matchFound.isPresent()) {
                        String found = matchFound.get();
                        val = $nameValues.get(found);
                    }
                    if (val == null) {
                        Optional<String> mFound = $nameValues.keySet().stream()
                                .filter(s -> s.equalsIgnoreCase("$" + p + "$"))
                                .findFirst();

                        if (mFound.isPresent()) {
                            String found = mFound.get();
                            val = $nameValues.get(found);
                        }
                    }
                    /*
                     * I could handle this where having the translator throw an
                     * exception
                     * RATHER than manually doing this.
                     */
                    if (val == null) {
                        throw new _jdraftException("no \"" + p + "\" in " + $nameValues);
                    }
                    String original = p;
                    if (p.toUpperCase().equals(original)) {
                        //it's all caps
                        val = translator.translate(val).toString().toUpperCase();
                    } else { //do firstCaps
                        String v = translator.translate(val).toString();
                        val = Character.toUpperCase(v.charAt(0)) + v.substring(1);
                    }
                }
                fills[i] = translator.translate(val);
            }
        }
        return fills;
    }

    /**
     *
     * @param translator translates parameter Objects to text
     * @param $names the names of PARAMETERS in the order to bind into the text
     * @param $nameValues NAME-> (object)VALUE binding data to be filled into
     * @return
     */
    private static Object[] inline( Translator translator, List<String> $names, Map<String, Object> $nameValues ) {

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
                    throw new _jdraftException( "no \"" + p + "\" in " + $nameValues );
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
     * Sometimes, we have whitespace that has meaning, and the Stencil needs to be "careful" when matching
     * that the whitespace appears (i.e. in the strict REGEX expression)...
     *
     * MOST OFTEN: we should just allow matching with patterns that contain leading whitespace (tabs, spaces)
     * because they are "logically the same"...
     *
     * TO signify that leading whitespace matters, we MUST start the stencil with whitespace
     * a Stencil has leadingWhitespace if the first text segment starts with any whitespace character
     *
     * if the first character of the first segment of the Stencil IS NOT a
     *
     * Some examples:
     * assertTrue( Stencil.of("fixed text").matches("fixed text") ); //exact match
     * assertTrue( Stencil.of("fixed text").matches(" fixed text") ); //EVEN THOUGH the candidate has a prefix space, it matches
     *
     * assertTrue( Stencil.of("fixed text").matches("fixed text ") ); //EVEN THOUGH the candidate has a postfix space, it matches
     * assertTrue( Stencil.of("fixed text").matches("fixed text\n") ); //EVEN THOUGH the candidate has a postfix linefeed, it matches
     * @return
     */
    public boolean isLeadingWhitespaceRelevant(){
        if( this.getTextForm().getFixedText().length() == 0 ){
            return false;
        }
        if( this.isFixedText()){ //check first character on the fixed text
            return Character.isWhitespace( this.textForm.getFixedText().charAt(0) );
        }
        //get the first fixed text segment
        return( this.getTextForm().getTextSegmentBeforeBlank(0).length() > 0
                && Character.isWhitespace( this.getTextForm().getTextSegmentBeforeBlank(0).charAt(0) ));
    }

    public boolean isTrailingWhitespaceRelevant(){
        if( this.getTextForm().getFixedText().length() == 0 ){
            return false;
        }
        if( this.isFixedText()){
            //if the LAST character is whitespace
            if( this.getTextForm().getFixedText().length() == 0 ){
                return false;
            }
            return Character.isWhitespace( this.textForm.getFixedText().charAt(this.getTextForm().getFixedText().length()-1) );
            //return Character.isWhitespace( .charAt(0) );
        }
        //get the last fixed text segment
        String lastSeg = this.textForm.getTextSegments().get(this.getTextForm().getTextSegments().size() -1);
        return( this.getTextForm().getTextSegmentBeforeBlank(0).length() > 0
                && Character.isWhitespace( this.getTextForm().getTextSegmentBeforeBlank(0).charAt(0) ));
    }
    /**
     * Does the stencil pattern match the multi line string passed in
     * @param constructed
     * @return true if the stencil pattern matches, false otherwise
     */
    public boolean matches(String constructed ) {
        //do i need to trim()?
        //if the STENCIL has explicit padding... then I need to match
        if( !isLeadingWhitespaceRelevant() && !isTrailingWhitespaceRelevant()){
            List<String> valsInOrder = this.textForm.parse( constructed.trim() );
            return (valsInOrder != null);
        }


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
            final String nm = fills[i];
            Optional<Embed> em = this.embeds.stream().filter(emb-> emb.name.equals(nm)).findFirst();
            if( em.isPresent() ){
                fillMap.put(fills[i], em.get().toString() );
                //fillMap.put(fills[i], "$$" +em.get().name+":"+em.get().stencil.toString()+ " $$");
            } else{
                fillMap.put(fills[i], "$" + fills[i] + "$");
            }
        }

        Object[] fis = inline( Translator.DEFAULT_TRANSLATOR, $Names, fillMap );
        return this.textForm.fill( fis );
    }

    /**
     * find the first match within the text, parses it and returns the tokens
     * if not match is found, return null
     *
     * @param text the text to look through for a match
     * @return the tokens for the stencil or null if it is not found
     */
    public Tokens parseFirst(String text ){
        MatchResult mr = matchFirst(this, text);
        if( mr != null ) {
            return parse(mr.group(0));
        }
        return null;
    }

    /**
     * Returns the first MatchResult for the first to match the stencil within the text
     * @param st the stencil
     * @param text the text to look through
     * @return the first Match within the text or null if none found
     */
    public static MatchResult matchFirst(Stencil st, String text){
        return matchFirst(st.getRegexPattern(),text);
    }

    /**
     * Returns the first MatchResult for the first to match the stencil within the text
     * @param pattern the regex pattern
     * @param text the text to look through
     * @return the first Match within the text or null if none found
     */
    public static MatchResult matchFirst(Pattern pattern, String text ){
        Matcher matcher = pattern.matcher(text);
        if( matcher.find() ){
            return matcher.toMatchResult();
        }
        return null;
    }

    /**
     * with string content, and a matchStencil (to match some pattern) and replaceStencil
     * (to provide a replacement) return the modified content (with the replacements)
     * <PRE>
     * String result = matchReplace( "the price is 120 dollars, less the 12 dollars credit", "dollars", "euros");
     *
     * assertEquals( "the price is 120 euros, less the 12 euros credit", result);
     * </PRE>
     *
     * @param content the String content that should be updated with replacements for matches
     * @param matchStencil matches a pattern
     * @param replaceStencil (optionally) takes the tokens returned from the matchStencil and
     * @return the modified content with stencils matched and replaced
     */
    public static String matchReplace(String content, Stencil matchStencil, Stencil replaceStencil){
        String unparsed = content; //parts of the content that havent been parsed yet
        StringBuilder sb = new StringBuilder(); // where the replaced code is written

        Tokens ts = matchStencil.parseFirst(unparsed);
        while(ts != null){
            //build the exact match
            String match = matchStencil.draft(ts);

            //find the location of the match within the
            int matchIndex = unparsed.indexOf(match);

            //retrieve the static content BEFORE the match
            String beforeMatch = unparsed.substring(0, matchIndex);

            //draft/build the replacement for the match encountered
            String replacement = replaceStencil.draft(ts);

            //append the static content before the match, then the replacement
            sb.append(beforeMatch).append(replacement);

            //bump the cursor position
            unparsed = unparsed.substring(Math.min( matchIndex+match.length()+1, unparsed.length()));
            //sb.append(replaceSelection);
            ts = matchStencil.parseFirst(unparsed);
        }
        //append any trailing characters that are found after the last match
        sb.append(unparsed);
        return sb.toString();
    }

    /**
     * Stencil embedded within another Stencil
     * i.e
     * <PRE>
     * Stencil s = Stencil.of(
     *     "This Stencil is $name$",
     *     "it contains an $$Embedded:Another Stencil which can also have $parameters$  $$"
     *     "...and things at the end of the Stencil");
     *
     * </PRE>
     * the name of the embedded stencil above is: "Embedded"
     * the "body" of the embedded stencil is    : "Another Stencil which can also have $parameters$"
     *
     * the embedded stencil has the form :
     * $$<identifier>:<Stencil>$$
     *
     * There are some constraints:
     * the identifier/name MUST BE unique within the Stencils $Names
     * trailing whitespace BEFORE the final $$ will be REMOVED/TRIMMED
     */
    public static class Embed {

        /** Name of the embedded Stencil*/
        public String name;

        /** underlying Stencil of the optional entity (could be blank) */
        public Stencil stencil;

        /**
         * Optional name of the translator that will handle translating objects to drafted Strings
         * Importantly this can decide how to transform a LIST types of things into the drafted document...
         * SPECIFICALLY if we want to use SEPARATORS BETWEEN items(i.e. ",") in a list and terminal characters (i.e. ";")
         * i.e.
         * Stencil s = Stencil.of("$list$");
         * s.draft("list", new String[]{1,2,3,4,5}); //prints "1, 2, 3, 4, 5
         * ...
         */
        public String translatorName = "";

        public Embed(String name, Stencil stencil){
            this(name, stencil, "");
        }

        public Embed(String name, Stencil stencil, String translatorName){
            this.name = name;
            this.stencil = stencil;
            this.translatorName = translatorName;
            //System.out.println("trnalsatorName "+translatorName);
        }

        public Embed copy(){
            return new Embed( name, Stencil.of(stencil), translatorName );
        }

        public Embed $(String target, String $name){
            this.stencil = this.stencil.$(target, $name);
            return this;
        }

        public Embed rename$(String old$Name, String new$Name){
            if( this.name.equals(old$Name) ){
                this.name = new$Name;
            }
            this.stencil = stencil.rename$(old$Name, new$Name);
            return this;
        }

        //TODO replace Embedded
        public Embed hardcode$(Translator translator, Map<String,Object>keyValues){
            if( keyValues.containsKey(this.name)){ //they want to hardcode the Entire embedded stencil?
                Object stencilValue = keyValues.get(this.name);
                Stencil newStencil = Stencil.of( translator.translate(stencilValue));
                this.stencil = newStencil;
            }
            this.stencil = (Stencil)this.stencil.$hardcode(translator, keyValues);
            return this;
        }

        public String draft( Object val, Map<String,Object> keyValues ){
            if( val == null || val.equals(Boolean.FALSE) || val.equals("") ){
                return "";
            }
            //get translator from keyvalues
            Translator tl = Translator.DEFAULT_TRANSLATOR;
            if(this.translatorName != null && this.translatorName.length() >0){
                tl = (Translator) keyValues.get(this.translatorName);
                if( tl == null ){
                    throw new _jdraftException("[Stenci.Embed]No translator : \""+ this.translatorName+"\" in "+ keyValues);
                }
            }

            List iterations;
            if( val.getClass().isArray() ){ //any array
                iterations = Arrays.stream( (Object[])val).collect(Collectors.toList());
            } else if( val instanceof Collection ){ //any collection
                iterations = (List)((Collection)val).stream().collect(Collectors.toList());
            } else{ //a single value
                iterations = new ArrayList<>();
                iterations.add(val);
            }

            List<String> drafted = new ArrayList<>();
            for(int i=0;i<iterations.size();i++){
                Object it = iterations.get(i);
                if( it instanceof Map ){
                    drafted.add( this.stencil.draft(tl, (Map<String,Object>)iterations.get(i))); //draft and add the drafted stencils to list
                }else {
                    if( it instanceof String ){
                        //System.out.println("====================Adding String Embed");
                        drafted.add( (String)it );
                    } else if (it instanceof Statement) {
                        //System.out.println("{{{{{{{{{{{{{{{{{{{{{{ Adding Statement Embed");
                        drafted.add( ((Statement)it).toString() );
                    } else if( it instanceof Template ){
                        Template t = (Template)it;
                        drafted.add( t.draft(keyValues).toString() );
                    }
                    else{
                        //TODO this should be smarter... i.e. handle propertyizing Objects
                        drafted.add(this.stencil.draft(tl, "val", iterations.get(i))); //draft and add the drafted stencils to list
                    }
                }
            }
            //use the translator to compose the individual drafted stencils
            return (String)tl.translate(drafted);
        }

        public static Embed parse( String tok ) {
            if( tok.startsWith("$$") && tok.endsWith("$$") ){
                String embedName = null;
                String stripped = tok.substring(2, tok.length()-2);
                //start at the END of the token...

                //  first check if we have a translator, the translator name is within brackets [] immediately b4 $$
                //  for example in : "$$a: stencil :[trans]$$" "trans" is the translator name
                //  for example in : "$$a: stencil :a[trans]$$" "trans" is the translator name
                String translatorName = "";
                //check if there is a translator at the end  $$<name>:<stencil>:[<translator>]$$
                //                                      i.e. $$a: stencil $param$ :[,;]$$
                //                                      i.e. $$a: stencil $param$ :a[,;]$$
                if(stripped.endsWith("]") ){ //a translator is specified
                    int bracketStart = stripped.lastIndexOf("[");
                    if( bracketStart < 0 ){
                        throw new _jdraftException("missing '[' for start of translator name:" + System.lineSeparator() + tok);
                    }
                    translatorName = stripped.substring(bracketStart+1, stripped.length()-1);
                    stripped = stripped.substring(0,bracketStart); //remove the entire [translator] from stripped
                }
                //check if we have a "symmetrical" closing tag
                if( !stripped.endsWith(":")){ //if stripped ends with : then we DONT have a symmetrical end tag
                    int colon = stripped.lastIndexOf(':');
                    if( colon < 0 ){
                        throw new _jdraftException("missing ':' previous to $$ ending tag" + System.lineSeparator() + tok);
                    }
                    embedName =  stripped.substring(colon +1);
                    if( !isValidIdentifier(embedName)){
                        throw new _jdraftException("name : "+stripped+" in trailing tag NOT a valid identifier:"+System.lineSeparator()+tok);
                    }
                    stripped = stripped.substring(0, colon);
                } else{
                    stripped = stripped.substring(0, stripped.length()-1); //get rid of ending ':'
                }
                //by now we've completed stripping the END of the Embed tag including the
                //                                      ":<name>[<translator>]$$"
                // from the whole tag "$$<name>:<stencil>:<name>[<translator>]$$"

                //THERE MUST BE a name at the front of the tag
                //                    "$$<name>:"
                // from the whole tag "$$<name>:<stencil>:<name>[<translator>]$$"
                int nmidx = stripped.indexOf(":");
                if( nmidx < 0 ){
                    throw new _jdraftException("missing ':' to separate name from body of Embedded Stencil in:"+System.lineSeparator()+tok);
                }
                String name = stripped.substring(0, nmidx);
                //stripped = stripped.substring(0, nmidx);
                //System.out.println( "Embedded Name "+name);
                if( !isValidIdentifier(name)){
                    throw new _jdraftException("leading name : "+name+" not valid a valid identifier for Embedded :"+System.lineSeparator()+tok);
                }
                if( embedName != null && !embedName.equals(name)){
                    throw new _jdraftException("leading name : "+name+" not equal to end name : "+embedName+" for "+System.lineSeparator()+tok);
                }
                //String st = tok.substring(tok.indexOf(":") + 1, tok.length() -2);
                String st = stripped.substring(stripped.indexOf(":") + 1 );
                //System.out.println( "Embedded Stencil String \""+st+"\"");
                Stencil stencil = Stencil.of(st);
                return new Embed(name, stencil, translatorName);
            }
            throw new _jdraftException("missing '$$' prefix and/or '$$' postfix for Embedded Stencil in:"+System.lineSeparator()+tok);
        }

        /**
         * An Embedded has this form ($$ <name> : <stencil> $$):
         * <PRE>
         * $$name:Stencil with any number of $param$    :$$
         * -------                                      ---| signifier characters
         *   ----                                          | the name "name" of the Embed
         *        --------------------------------------   | the Stencil (could contain parameters)
         *                                                 | trailing whitespace ALWAYS REMOVED
         * </PRE>
         * @param tok
         * @return
         */
        private static boolean isValidEmbedded( String tok ) {
            //System.out.println("checking for embedded "+tok );
            if( tok.startsWith("$$") && tok.endsWith("$$") ){
                try{
                    Embed.parse(tok);
                    return true;
                }
                catch(Exception e){
                    return false;
                }
                /*
                String name = tok.substring(2, tok.length()-2);
                int nmidx = name.indexOf(":");
                if( nmidx < 0 ){
                    return false;
                }
                name = name.substring(0, nmidx);
                boolean isValid = isValidIdentifier(name);
                //System.out.println("Is Valid : "+ tok+" "+isValid);
                return isValid;
                 */
            }
            return false;
        }

        public String toString(){
            if( this.translatorName == null || this.translatorName.length() == 0 ){
                return "$$"+this.name+":"+this.stencil+":"+name+"$$";
            }
            return "$$"+this.name+":"+this.stencil+":"+name+"["+translatorName+"]$$";
        }
    }

    private static boolean isValidIdentifier(String id){
        if( !Character.isJavaIdentifierStart( id.charAt( 0 ) ) ) {
            return false;
        }
        for( int i = 1; i < id.length(); i++ ) {
            if( !Character.isJavaIdentifierPart( id.charAt( i ) ) ) {
                return false;
            }
        }
        return true;
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
            String code = Text.combine(markup);
            int cursor = 0;
            int next = nextToken( code, cursor );

            TextForm.Builder bb = new TextForm.Builder();
            List<String> parameters = new ArrayList<>();
            List<Embed> embedded = new ArrayList<>();

            while( next > 0 ) {
                String tok = code.substring( cursor, next );

                /** Added for Embedded Stencils */
                if( Embed.isValidEmbedded(tok) ) {
                    bb.blank(); //we add a blank for an embedded stencil
                    Embed embed = Embed.parse(tok); //parse the Stencil
                    //System.out.println("Embed" + embed );
                    parameters.add( embed.name ); //add a parameter
                    embedded.add(embed); //add to the embedded
                    //System.out.println("EMBED "+ embed);
                    cursor = next +1;
                }
                else if( isValidParameter( tok ) ) {
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
            return new Stencil( bb.compile(), parameters, embedded );
        }

        private static boolean isValidParameter( String tok ) {
            if( tok.length() > 2 //"$$" is NOT a valid token
                    && tok.startsWith( "$" )
                    && tok.endsWith( "$" ) ) {
                return isValidIdentifier(tok.substring(1, tok.length()-1));
            }
            return false;
        }

        private static int nextToken( String code, int cursor ) {
            if( cursor >= code.length() ) {
                return -1;
            }
            if( code.charAt( cursor ) == '$' ) { //maybe cursor is ON a $parameter
                int end = code.indexOf( '$', cursor + 1 );
                if( end < 0 ) { //no matching end $ found
                    return code.length();
                }

                /** START Added for embed*/
                // $$<name>: stencil :<name>$$                   i.e. $$a: a $param$ :a$$
                // $$<name>: stencil :<name>[<translator>]$$     i.e. $$a: a $param$ :a[,;]$$
                // $$<name>: stencil :$$                         i.e. $$a: a $param$ :$$
                // __<name>: stencil :<name>__                   i.e. __a: a $param$ :a__
                // __<name>: stencil :<name>[<translator>]__     i.e. __a: a $param$ :a[,;]__
                // __<name>: stencil :__                         i.e. __a: a $param$ :__
                if( end == cursor + 1){ //found a "$$" two consecutive $$'s usually means an "embedded"
                    //FIRST look for "$$<name>: <stencil> :<name>$$
                    //find the name
                    int colonIndex = code.indexOf(":", end+1);

                    int endOptional = code.indexOf("$$", end + 1);
                    if( endOptional < 0 ){ //we couldnt find a matching end optional
                        return cursor + 2; //we MAY still have $parameters$ left, skip over the $$ at current cursor
                    }
                    else{ //we did find an optional
                        return endOptional + 2; //we need to skip over the "$$" end optional params
                    }
                }
                /** END Added for optional */

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
