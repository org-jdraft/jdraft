package org.jdraft;

import org.jdraft.TextBlanks.Builder;
import junit.framework.TestCase;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class TextBlanksTest extends TestCase{

    public void testOnlyBlank(){
        TextBlanks tb = TextBlanks.of(null);
        List<String> all = tb.decompose("The whole thing");
        assertTrue( all.size() == 1);
        assertEquals( all.get(0), "The whole thing" );

        TextBlanks.Builder tbb = new TextBlanks.Builder();
        tbb.blank();
        tb = tbb.compile();
        all = tb.decompose("The whole thing");
        assertTrue( all.size() == 1);
        assertEquals( all.get(0), "The whole thing" );
    }
    public void testStartsWithBlank() {
        //only text
        TextBlanks fitb = TextBlanks.of( "t");
        assertFalse( fitb.startsWithBlank() );
        assertFalse( fitb.endsWithBlank() );
        assertEquals(0, fitb.getBlanksCount());

        //only blank
        fitb = TextBlanks.of( null );
        assertTrue( fitb.startsWithBlank());
        assertTrue( fitb.endsWithBlank());
        assertEquals( 1, fitb.getBlanksCount());
        assertEquals( "toDir Extract", fitb.decompose("toDir Extract").get(0));
    }

    public void testExtractOnlyBlank() {
        TextBlanks fitb = TextBlanks.of("");
        assertFalse(fitb.startsWithBlank());
        assertFalse(fitb.endsWithBlank());
        assertEquals(0, fitb.getBlanksCount());
        assertEquals(0, fitb.decompose("").size());

        fitb = TextBlanks.of(null);
        List<String> strs = fitb.decompose("Pretty much anything");
        assertEquals(1, strs.size());
        assertEquals("Pretty much anything", strs.get(0));
    }

    public void testExtractStartsWithBlank(){
        TextBlanks fitb = TextBlanks.of(null, " a");
        List<String> strs = fitb.decompose("Ends with a");
        assertEquals("Ends with", strs.get(0));
        assertEquals(1, strs.size());
    }

    public void testExtractEndsWithBlank() {
        TextBlanks fitb = TextBlanks.of("a ", null);
        List<String> strs = fitb.decompose("a Starts With");
        System.out.println(strs);
        assertEquals("Starts With", strs.get(0));
        assertEquals(1, strs.size());
    }

    public void testExtractBlankInMiddle() {
        TextBlanks fitb = TextBlanks.of("in the ", null, " of text");
        List<String> strs = fitb.decompose("in the MIDDLE of text");
        assertEquals("MIDDLE", strs.get(0));
        assertEquals(1, strs.size());
    }

    public void testEquals(){
        assertEquals( TextBlanks.of(null," A ", null, " B- ", null), TextBlanks.of(null," A ", null, " B- ", null) );

        TextBlanks fitb = TextBlanks.of(null," A ", null, " B- ", null);
        List<String> strs = fitb.decompose("much at the front A then B- for the exam");
        assertEquals( fitb, TextBlanks.of(null," A ", null, " B- ", null) );
    }

    public void testExtractStartEndMiddleBlanks(){
        TextBlanks fitb = TextBlanks.of(null," A ", null, " B- ", null);
        List<String> strs = fitb.decompose("much at the front A then B- for the exam");
        assertEquals("much at the front",strs.get(0));
        assertEquals("then",strs.get(1));
        assertEquals("for the exam",strs.get(2));
        assertEquals(3, strs.size() );

        TextBlanks another = TextBlanks.of(null," A ", null, " B- ", null);

        assertEquals( fitb, another );
        assertEquals( fitb.hashCode(), another.hashCode() );
    }

    public void testEndsWithBlank(){
        TextBlanks fitb = TextBlanks.of( "This is some data" );
        assertFalse(fitb.endsWithBlank());

        fitb = TextBlanks.of( "This is some data", null );
        assertTrue(fitb.endsWithBlank());

        fitb = TextBlanks.of( "This is some data", null, "text" );
        assertFalse(fitb.endsWithBlank());
    }

    public void testNoBlanksGetTextAfterBlank(){
        TextBlanks fitb =
                TextBlanks.of( "This is some data" );
        assertEquals("This is some data", fitb.getTextBeforeBlank( 0 ));
        //assertEquals("this is some data", fitb.getTextAfterBlank( -1 ));
    }

    public void testCombine (){
        TextBlanks f = TextBlanks.of( "a");
        TextBlanks f0 = TextBlanks.of( "b");
        TextBlanks fcomb = TextBlanks.combine(f,f0);
        assertEquals( "ab", fcomb.fill(  ) );

        TextBlanks f1 = TextBlanks.of( "1", "3", "5" );
        TextBlanks f2 = TextBlanks.of( "6", "8", "10" );

        assertEquals( "12345", f1.fill( 2, 4 ) );
        assertEquals( "678910", f2.fill( 7,9 ) );

        TextBlanks f3 = TextBlanks.combine( f1, f2 );

        assertEquals( f3.getBlanksCount(), f1.getBlanksCount() + f2.getBlanksCount() );
        assertEquals( f3.getFixedText(), f1.getFixedText() + f2.getFixedText() );

        assertEquals( "12345678910", f3.fill( 2, 4, 7, 9 ) );
    }

    public void testMergeBlanksAtEdges(){
        TextBlanks f1 = TextBlanks.of( null, "2", "4", null );
        TextBlanks f2 = TextBlanks.of( null, "7", "9", null );

        assertEquals( "12345", f1.fill( 1, 3, 5 ) );
        assertEquals( "678910", f2.fill( 6, 8, 10 ) );

        TextBlanks f3 = TextBlanks.combine(f1,f2);

        assertEquals( f3.getBlanksCount(), f1.getBlanksCount() + f2.getBlanksCount() );
        assertEquals( f3.getFixedText(), f1.getFixedText() + f2.getFixedText() );
        assertEquals( "12345678910", f3.fill( 1, 3, 5, 6, 8, 10 ) );
    }

    public void testOfBetweenStrings(){
        TextBlanks ft = TextBlanks.of( "A", "C", "E", "G" );
        assertEquals( ft.getFixedText(), "ACEG" );
        assertEquals( "ABCDEFG", ft.fill( "B", "D", "F" ) );
        assertEquals( 3, ft.getBlanks().cardinality() );

        assertTrue( ft.getBlanks().get( 1 ) );
        assertTrue( ft.getBlanks().get( 3 ) );
        assertTrue( ft.getBlanks().get( 5 ) );

        assertEquals( 3, ft.getBlanksCount() );

        assertEquals( ft.getTextAfterBlank( 0 ), "C" );
        assertEquals( ft.getTextAfterBlank( 1 ), "E" );
        assertEquals( ft.getTextAfterBlank( 2 ), "G" );

        assertEquals( ft.getTextBeforeBlank( 0 ), "A" );
        assertEquals( ft.getTextBeforeBlank( 1 ), "C" );
        assertEquals( ft.getTextBeforeBlank( 2 ), "E" );
    }

    public void testOfDoubleBlanks(){
        //toDir create multiple (N) blanks in a row, accept (N) nulls in a row
        TextBlanks ft = TextBlanks.of( "A", null, null, "D", "F" );
        assertEquals( ft.getFixedText(), "ADF" );
        assertEquals( "ABCDEF", ft.fill( "B", "C", "E" ) );
        assertEquals( 3, ft.getBlanks().cardinality() );

        assertTrue( ft.getBlanks().get( 1 ) );
        assertTrue( ft.getBlanks().get( 2 ) );
        assertTrue( ft.getBlanks().get( 4 ) );

        assertEquals( 3, ft.getBlanksCount() );

        assertEquals( ft.getTextAfterBlank( 0 ), "" );
        assertEquals( ft.getTextAfterBlank( 1 ), "D" );
        assertEquals( ft.getTextAfterBlank( 2 ), "F" );

        assertEquals( ft.getTextBeforeBlank( 0 ), "A" );
        assertEquals( ft.getTextBeforeBlank( 1 ), "" );
        assertEquals( ft.getTextBeforeBlank( 2 ), "D" );
    }
    /*
    public void testOfAllBlanks()
    {
    	BlankBinding ft = FillInTheBlanks.of( null,null,null );
    	assertEquals( 3, ft.getBlanksCount() );
    	assertEquals( "", ft.getFixedText() );
    	assertEquals("ABC", ft.bind("A", "B", "C" ) );

    	assertTrue( ft.getBlanks().getAt( 0 ) );
    	assertTrue( ft.getBlanks().getAt( 1 ) );
    	assertTrue( ft.getBlanks().getAt( 2 ) );
    }
    */
    public void testOfPrefixBlanks(){
        TextBlanks ft = TextBlanks.of( null, null, "345", null );
        assertEquals( "123456", ft.fill( 1,2,6 ) );

        ft = TextBlanks.of( null, null, "3", "5", null );
        assertEquals( "123456", ft.fill( 1,2,4,6 ) );
    }

    public void testOfTrailingBlanks(){
        TextBlanks ft = TextBlanks.of( "12", null, null, null );
        assertEquals( "12345", ft.fill( 3,4,5 ) );

    }

    /**
     * Verify that:
     * <UL>
     * <LI>comment "open tags" "/+*" are converted toDir "/*"
     * <LI>comment close tags "*+/" are converted toDir "* /"
     * </UL>
     */
    public void testReplaceComment(){
        String comment = "/+**+/";
        TextBlanks replaceComment =
                new TextBlanks.Builder().text( comment ).compile();
        assertTrue( replaceComment.getBlanksCount() == 0 );
        //assertTrue( replaceComment.getFixedText().typesEqual( "/**/" ) );

        //replaceComment.fill(new Body(), new Object[ 0 ]);

        comment = "/+*/+**+/*+/";
        replaceComment =
                new TextBlanks.Builder().text( comment ).compile();
        assertTrue( replaceComment.getBlanksCount() == 0 );
        //assertTrue( replaceComment.getFixedText().typesEqual( "/*/**/*/" ) );

        comment = "/+** JAVADOC comment *+/";
        TextBlanks replaceJdocComment =
                new TextBlanks.Builder().text( comment ).compile();
        assertTrue( replaceJdocComment.getBlanksCount() == 0 );
        //assertTrue( replaceJdocComment.getFixedText().typesEqual( "/** JAVADOC comment */" ) );
    }

    public void testNoBlanks(){
        String alpha = "abcdefghijklmnopqrstuvwxyz";


        TextBlanks noBlanks =
                new TextBlanks.Builder().text( alpha ).compile();

        assertTrue( noBlanks.getBlanksCount() == 0 );

        assertTrue( noBlanks.getFixedText().equals( alpha ) );
    }

    public void testNoBlanksBuilder(){
        String alpha = "abcdefghijklmnopqrstuvwxyz";
        Builder b = new Builder();
        b.text( alpha );
        TextBlanks fo = b.compile();
        assertTrue( fo.getFixedText().equals( alpha ) );
    }

    public void testGetTextAfterBlanks(){
        TextBlanks empty =
                new Builder()
                        .blank().blank().blank().blank().compile();

        assertTrue( empty.getTextAfterBlank( -1 ).equals( "" ) );
        assertTrue( empty.getTextAfterBlank( 0 ).equals( "" ) );
        assertTrue( empty.getTextAfterBlank( 1 ).equals( "" ) );
        assertTrue( empty.getTextAfterBlank( 2 ).equals( "" ) );
        assertTrue( empty.getTextAfterBlank( 3 ).equals( "" ) );
        assertTrue( empty.getTextAfterBlank( 4 ).equals( "" ) );
        assertTrue( empty.getTextAfterBlank( 5 ).equals( "" ) );

        TextBlanks fo = new Builder()
                .text( "0123" ).blank().text( "567" ).blank()
                .text( "901" ).blank().text( "3456" ).blank()
                .text( "890" ).blank().text( "23" ).blank().compile();
        //( "0123_567_901_3456_890_23_", "to" );

        assertTrue( fo.getTextAfterBlank( -1 ).equals( "" ) );

        assertTrue( fo.getTextAfterBlank( 0 ).equals( "567" ) );
        assertTrue( fo.getTextAfterBlank( 1 ).equals( "901" ) );
        assertTrue( fo.getTextAfterBlank( 2 ).equals( "3456" ) );
        assertTrue( fo.getTextAfterBlank( 3 ).equals( "890" ) );
        assertTrue( fo.getTextAfterBlank( 4 ).equals( "23" ) );
        assertTrue( fo.getTextAfterBlank( 5 ).equals( "" ) );
    }

    public void testGetTextBeforeBlanks(){
        TextBlanks empty =
                new Builder().blank().blank().blank().blank().compile();
        assertTrue( empty.getTextBeforeBlank( -1 ).equals( "" ) );
        assertTrue( empty.getTextBeforeBlank( 0 ).equals( "" ) );
        assertTrue( empty.getTextBeforeBlank( 1 ).equals( "" ) );
        assertTrue( empty.getTextBeforeBlank( 2 ).equals( "" ) );
        assertTrue( empty.getTextBeforeBlank( 3 ).equals( "" ) );
        assertTrue( empty.getTextBeforeBlank( 4 ).equals( "" ) );
        assertTrue( empty.getTextBeforeBlank( 5 ).equals( "" ) );

        TextBlanks fo =
                new Builder().text("0123").blank().text( "567").blank().text( "901" )
                        .blank().text( "3456" ).blank().text( "890" ).blank() .text( "23")
                        .blank().compile();

        assertTrue( fo.getTextBeforeBlank( -1 ).equals( "" ) );

        assertTrue( fo.getTextBeforeBlank( 0 ).equals( "0123" ) );
        assertTrue( fo.getTextBeforeBlank( 1 ).equals( "567" ) );
        assertTrue( fo.getTextBeforeBlank( 2 ).equals( "901" ) );
        assertTrue( fo.getTextBeforeBlank( 3 ).equals( "3456" ) );
        assertTrue( fo.getTextBeforeBlank( 4 ).equals( "890" ) );
        assertTrue( fo.getTextBeforeBlank( 5 ).equals( "23" ) );
        assertTrue( fo.getTextBeforeBlank( 6 ).equals( "" ) );
    }

    public void testGetCharIndexOfBlank(){

        //FillOrder fo = ;
        assertTrue( new Builder().blank().text( " B" ).compile()
                .getCharIndexOfBlank( 0 ) == 0 );
        assertTrue( new Builder().text("A").blank().text( "B" ).compile()
                .getCharIndexOfBlank( 0 ) == 1 );
        assertTrue( new Builder().text( "AB" ).blank().compile()
                .getCharIndexOfBlank( 0 ) == 2 );

        assertTrue( new Builder().blank().text( "B" ).blank().compile()
                .getCharIndexOfBlank( 0 ) == 0 );

        //System.out.println( FillInTheBlanks.parse( "__B__" ).getCharIndexOfBlank( 1 ) );
        TextBlanks fo =
                new Builder().blank().text( "B" ).blank().compile();
        //System.out.println( fo );
        //System.out.println( fo.getCharIndexOfBlank( 1 ) );
        assertTrue( fo.getCharIndexOfBlank( 1 ) == 1 );

        fo = new Builder().blank().blank().compile();
        assertTrue( fo.getCharIndexOfBlank( 0 ) == 0 );
        assertTrue( fo.getCharIndexOfBlank( 1 ) == 0 );

        fo = new Builder()
                .text( "0123" ).blank()
                .text( "567" ).blank()
                .text( "901" ).blank()
                .text( "3456" ).blank()
                .text( "890" ).blank()
                .text( "23" ).blank().compile();
        //"0123_567_901_3456_890_23_", "to" );


        assertTrue( fo.getCharIndexOfBlank( 0 ) == 4 ); //0123_567901345689023
        assertTrue( fo.getCharIndexOfBlank( 1 ) == 7 ); //0123567_901345689023
        assertTrue( fo.getCharIndexOfBlank( 2 ) == 10 ); //0123567901_345689023
        assertTrue( fo.getCharIndexOfBlank( 3 ) == 14 ); //01235679013456_89023
        assertTrue( fo.getCharIndexOfBlank( 4 ) == 17 ); //01235679013456890_23
        assertTrue( fo.getCharIndexOfBlank( 5 ) == 19 ); //0123567901345689023_
    }

    /**
     * Test that things look right when I toString a FillInTheBlanks.FillOrder
     */
    public void testGetTextAfterBlank(){
        /*
        FillOrder a =
            FillInTheBlanks.parse(
                "public class __" + N
                + "    extends __" + N
                + "    implements __" + N
                + "{" + N
                + "    __" + N
                + "}"
            );
        */

        TextBlanks fillOrder = new Builder().text( "a" ).compile();
        assertTrue( fillOrder.getTextAfterBlank( 0 ).equals( "" ) );
        assertTrue( fillOrder.getTextAfterBlank( 1 ).equals( "" ) );

        //fillOrder = new Builder().blank().compile();
        //assertTrue( fillOrder.getTextAfterBlank( 0 ).typesEqual( ", b" ) );
        //assertTrue( fillOrder.getTextAfterBlank( 1 ).typesEqual( "" ) );

        //fillOrder = FillInTheBlanks.parse( "__, b, __" );

        //System.out.println( fillOrder.getTextAfterBlank( 0 ) );
        //assertTrue( fillOrder.getTextAfterBlank( 0 ).typesEqual( ", b, " ) );

        //System.out.println( "\"" + fillOrder.getTextAfterBlank( 1 ) + "\"" );
        //assertTrue( fillOrder.getTextAfterBlank( 1 ).typesEqual( "" ) );
    }

    public void testBlanks2(){
        TextBlanks f2 =
                new Builder()
                        .text( "A" ).blank()
                        .text( "C" ).blank()
                        .text( "E" ).blank()
                        .text( "G" ).blank()
                        .text( "I" ).compile();
        assertTrue( f2.getTextAfterBlank( 0 ).equals( "C" ) );
        assertTrue( f2.getTextAfterBlank( 1 ).equals( "E" ) );

        assertTrue( f2.getTextAfterBlank( 2 ).equals( "G" ) );
        assertTrue( f2.getTextAfterBlank( 3 ).equals( "I" ) );
        assertTrue( f2.getTextAfterBlank( 4 ).equals( "" ) );
    }

    /**
     * Test that things look right when I toString a FillInTheBlanks.FillOrder

     public void testToString()
     {
     FillOrder a = FillInTheBlanks.parse( "public class __" + N + "    extends __" + N
     + "    implements __" + N + "{" + N + "    __" + N + "}" );

     System.out.println( a );

     a = FillInTheBlanks.parse( "____ __ __ __ & __ __" + N + "    is __" );

     System.out.println( a );

     //String[] orderedNames = new String[]
     //  { "${1}","${2}", "${3}", "${4}", "${5}", "${6}", "${7}", "${8}", "${9}" };

     //System.out.println(  a.fill( (Object[])orderedNames ) );

     //System.out.println( a );
     }
     */
    //here is the conventional use, first parsing the template, then filling it in
    public void testSimpleUse(){
        assertEquals(
                new Builder().text( "I have a ").blank().text( " dog and a ").blank()
                        .text( " house" ).compile()
                        .fill( "black", "beige" ),
                "I have a black dog and a beige house" );
    }

    public static String staticText( String text ){
        return Pattern.quote(text);
        /*
        StringBuilder sb = new StringBuilder();
        for(int i=0;i<text.length(); i++) {
            char c = text.charAt(i);
            switch( c ){
                case '<': case '(': case '[': case '{': case '\\': case '^': case '-': case '=': case '$': case '!':
                    case '|' : case '\'': case ']' : case '}': case ')': case '?' : case '*' : case '+' : case '.': case '>':
                    sb.append('\\').append(c); break;
                default : sb.append( c );
            }
        }
        return sb.toString();
        */
    }



    public void testBuildRegexPattern1(){
        TextBlanks tb = TextBlanks.of( "$NAME ", null, " $VALUE" );
        //String regexPattern = tb.buildRegexPattern( tb );
        //System.out.println( regexPattern );
        //Pattern p = Pattern.compile( regexPattern );

        Matcher m = tb.getRegexPattern().matcher("$NAME and stuff in between $valu $VALUE");
        assertTrue( m.matches() );
        System.out.println( m.group( 0 ) );
        System.out.println( m.group( 1) );
        System.out.println( m.group( 2 ) );

        tb = TextBlanks.of("first ", null, " second");
        //regexPattern = buildRegexPattern( tb );
        //p = Pattern.compile( regexPattern );

        m = tb.getRegexPattern().matcher("second something first");
        assertFalse( m.matches() );


    }

    public void testBuildRegexPatternString(){
        //builds a pattern String that can be used toDir listAnyMatch the text/blanks
        TextBlanks tb = TextBlanks.of( "return ", null, ";" );
        //String regexPattern = buildRegexPattern( tb );
        //Pattern p = Pattern.compile( regexPattern );

        Matcher matcher = tb.getRegexPattern().matcher("return 3 + 4 + r;");
        System.out.println( matcher.matches() );


        System.out.println( tb.getRegexPattern() );
        System.out.println( "GROUPS " + matcher.groupCount() );
        System.out.println( "0 "+ matcher.group(0 ));// the whole thing matched
        System.out.println( "1 "+ matcher.group(1)); //
        System.out.println( "2 "+ matcher.group(2 ));
        System.out.println( "3 "+ matcher.group(3 ));
        //System.out.println( matcher.hasAnchoringBounds() );
        //System.out.println( matcher.hasTransparentBounds() );

        tb = TextBlanks.of( null, " is a ", null );
        matcher = tb.getRegexPattern().matcher("Eric is a good dude");
        System.out.println( tb.getRegexPattern() );
        assertTrue( matcher.matches() );
        System.out.println( "GROUPS " + matcher.groupCount() );
        System.out.println( "0 "+ matcher.group(0 ));// the whole thing matched
        System.out.println( "1 "+ matcher.group(1)); //
        System.out.println( "2 "+ matcher.group(2 ));
        System.out.println( "3 "+ matcher.group(3 ));

        List<String>vars = new ArrayList<String>();
        //if( tb.startsWithBlank() ){
        //
        //}
        /*
        while(matcher.find()){

            System.out.println("group 1: " + matcher.group(1));
        }
        */
        //matcher.groupCount();

    }

    public void testRegexp(){

        //I need a _1_build Pattern

        String line = "return true;";
        Pattern pattern = Pattern.compile("(return )(.*)(;)");
        Matcher matcher = pattern.matcher(line);
        while (matcher.find()) {
            System.out.println("group 1: " + matcher.group(1));
            System.out.println("group 2: " + matcher.group(2));
            System.out.println("group 3: " + matcher.group(3));
        }

        line = "return \";\";";
        pattern = Pattern.compile("(return )(.*)(;)");
        matcher = pattern.matcher(line);
        while (matcher.find()) {
            System.out.println("group 1: " + matcher.group(1));
            System.out.println("group 2: " + matcher.group(2));
            System.out.println("group 3: " + matcher.group(3));
        }

        line = "if return \";\";";
        pattern = Pattern.compile("(if return )(.*)(;)");
        matcher = pattern.matcher(line);

        while (matcher.find()) {
            System.out.println("group 1: " + matcher.group(1));
            System.out.println("group 2: " + matcher.group(2));
            System.out.println("group 3: " + matcher.group(3));
        }

        line = "if return \";\";";
        pattern = Pattern.compile("(.*?)( return )(.*)(;)");
        matcher = pattern.matcher(line);

        while (matcher.find()) {
            System.out.println("group 1: " + matcher.group(1));
            System.out.println("group 2: " + matcher.group(2));
            System.out.println("group 3: " + matcher.group(3));
            System.out.println("group 4: " + matcher.group(4));
        }
        //p = Pattern.compile("\\.(return).*");
        //m = p.matcher("return true");
        //System.out.println( m.group(0) );
    }


    /**
     * FillInTheBlanks is dumb, but adding an abstraction layer on top can make it
     *

     public void testFormatted()
     {
     FillOrder classDef =
     new Builder().text( "public class " ).blank().blank()
     __" + "__" + "__" + "extends __"
     + "__" + "__" + "implements __" + "__" + "{" + "__" + "}" );
     String N = System.lineSeparator();
     String T = "    ";
     String theClassDef =
     classDef.fill( "TheClass", N, T, "TheBaseClass", N, T, "TheInterface", N, N );

     assertTrue( theClassDef.typesEqual( "public class TheClass" + N + "    extends TheBaseClass" + N
     + "    implements TheInterface" + N + "{" + N + "}" ) );

     }
     */

    /*
    // verify that (for a FillInTheBlanks) I want toDir compact so I dont getAt multiple
    // newlines
    public void testCompressNewLines()
    {
        String to = null;
        FillOrder a = FillInTheBlanks.of( "A", N, N, N, N, to, N, N, N, N, "C " );
        System.out.println( a.fill( "B" ) );
    }
    */

    /*
    public void testNewLines()
    {
        String to = null;
        FillOrder a = new Builder().text( "A"), N, to, "C " );
        System.out.println( a.fill( "B" ) );

        FillInTheBlanks.Builder f = new FillInTheBlanks.Builder();
        f.text( "A" );
        f.text( N );
        f.blank();
        f.text( "C" );

        FillOrder d = f.compile();
        System.out.println( d.fill( "B" ) );
    }
    */

    /** Simple Usage examples of FillInTheBlank
     public static void main( String[] args )
     {
     String to = null;

     FillOrder a = FillInTheBlanks.of( "A", to, "C " );
     System.out.println( a.fill( "B" ) );

     //4 blanks
     FillOrder c = FillInTheBlanks.of( to, to, to, to );

     //you can use the "compiled" document
     //toDir Fill in the blank many times (and share a document across threads)
     System.out.println( c.fill( "A", "B", "C", "D" ) ); //"ABCD"
     System.out.println( c.fill( "Don't", " disturb", " the", " beast" ) ); //"Don't disturb the beast"

     //blanks intermingled with text (starts with blank)
     c = FillInTheBlanks.of( to, "B", to, "D", to );
     System.out.println( "\"" + c.fill( "A", "C", "E" ) + "\"" ); //"ABCDE"

     //blanks intermingled with text (starts with text)
     c = FillInTheBlanks.of( "A", to, "C", to, "E" );
     System.out.println( "\"" + c.fill( "B", "D" ) + "\"" ); //"ABCDE"

     c = FillInTheBlanks.of( "A", to, "C", to, "E", to, "G" );
     System.out.println( "\"" + c.fill( "B", "D", "F" ) + "\"" ); //"ABCDEF"

     //multiple blanks in a row
     c = FillInTheBlanks.of( "A", to, "C", to, "E", to, to, to );
     System.out.println( "\"" + c.fill( "B", "D", "F", "G", "H" ) + "\"" ); //"ABCDEFGH"
     }
     */
}
