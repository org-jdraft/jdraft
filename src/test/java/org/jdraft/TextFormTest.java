package org.jdraft;

import org.jdraft.text.TextForm;
import org.jdraft.text.TextForm.Builder;
import junit.framework.TestCase;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class TextFormTest extends TestCase{

    public void testOnlyBlank(){
        TextForm tb = TextForm.of((String)null);
        List<String> all = tb.parse("The whole thing");
        assertTrue( all.size() == 1);
        assertEquals( all.get(0), "The whole thing" );

        TextForm.Builder tbb = new TextForm.Builder();
        tbb.blank();
        tb = tbb.compile();
        all = tb.parse("The whole thing");
        assertTrue( all.size() == 1);
        assertEquals( all.get(0), "The whole thing" );
    }
    public void testStartsWithBlank() {
        //only text
        TextForm fitb = TextForm.of( "t");
        assertFalse( fitb.startsWithBlank() );
        assertFalse( fitb.endsWithBlank() );
        assertEquals(0, fitb.getBlanksCount());

        //only blank
        fitb = TextForm.of( null );
        assertTrue( fitb.startsWithBlank());
        assertTrue( fitb.endsWithBlank());
        assertEquals( 1, fitb.getBlanksCount());
        assertEquals( "toDir Extract", fitb.parse("toDir Extract").get(0));
    }

    public void testExtractOnlyBlank() {
        TextForm fitb = TextForm.of("");
        assertFalse(fitb.startsWithBlank());
        assertFalse(fitb.endsWithBlank());
        assertEquals(0, fitb.getBlanksCount());
        assertEquals(0, fitb.parse("").size());

        fitb = TextForm.of(null);
        List<String> strs = fitb.parse("Pretty much anything");
        assertEquals(1, strs.size());
        assertEquals("Pretty much anything", strs.get(0));
    }

    public void testExtractStartsWithBlank(){
        TextForm fitb = TextForm.of(null, " a");
        List<String> strs = fitb.parse("Ends with a");
        assertEquals("Ends with", strs.get(0));
        assertEquals(1, strs.size());
    }

    public void testExtractEndsWithBlank() {
        TextForm fitb = TextForm.of("a ", null);
        List<String> strs = fitb.parse("a Starts With");
        System.out.println(strs);
        assertEquals("Starts With", strs.get(0));
        assertEquals(1, strs.size());
    }

    public void testExtractBlankInMiddle() {
        TextForm fitb = TextForm.of("in the ", null, " of text");
        List<String> strs = fitb.parse("in the MIDDLE of text");
        assertEquals("MIDDLE", strs.get(0));
        assertEquals(1, strs.size());
    }

    public void testEquals(){
        assertEquals( TextForm.of(null," A ", null, " B- ", null), TextForm.of(null," A ", null, " B- ", null) );

        TextForm fitb = TextForm.of(null," A ", null, " B- ", null);
        List<String> strs = fitb.parse("much at the front A then B- for the exam");
        assertEquals( fitb, TextForm.of(null," A ", null, " B- ", null) );
    }

    public void testExtractStartEndMiddleBlanks(){
        TextForm fitb = TextForm.of(null," A ", null, " B- ", null);
        List<String> strs = fitb.parse("much at the front A then B- for the exam");
        assertEquals("much at the front",strs.get(0));
        assertEquals("then",strs.get(1));
        assertEquals("for the exam",strs.get(2));
        assertEquals(3, strs.size() );

        TextForm another = TextForm.of(null," A ", null, " B- ", null);

        assertEquals( fitb, another );
        assertEquals( fitb.hashCode(), another.hashCode() );
    }

    public void testEndsWithBlank(){
        TextForm fitb = TextForm.of( "This is some data" );
        assertFalse(fitb.endsWithBlank());

        fitb = TextForm.of( "This is some data", null );
        assertTrue(fitb.endsWithBlank());

        fitb = TextForm.of( "This is some data", null, "text" );
        assertFalse(fitb.endsWithBlank());
    }

    public void testNoBlanksGetTextAfterBlank(){
        TextForm fitb =
                TextForm.of( "This is some data" );
        assertEquals("This is some data", fitb.getTextSegmentBeforeBlank( 0 ));
        //assertEquals("this is some data", fitb.getTextAfterBlank( -1 ));
    }

    public void testCombine (){
        TextForm f = TextForm.of( "a");
        TextForm f0 = TextForm.of( "b");
        TextForm fcomb = TextForm.combine(f,f0);
        assertEquals( "ab", fcomb.fill(  ) );

        TextForm f1 = TextForm.of( "1", "3", "5" );
        TextForm f2 = TextForm.of( "6", "8", "10" );

        assertEquals( "12345", f1.fill( 2, 4 ) );
        assertEquals( "678910", f2.fill( 7,9 ) );

        TextForm f3 = TextForm.combine( f1, f2 );

        assertEquals( f3.getBlanksCount(), f1.getBlanksCount() + f2.getBlanksCount() );
        assertEquals( f3.getFixedText(), f1.getFixedText() + f2.getFixedText() );

        assertEquals( "12345678910", f3.fill( 2, 4, 7, 9 ) );
    }

    public void testMergeBlanksAtEdges(){
        TextForm f1 = TextForm.of( null, "2", "4", null );
        TextForm f2 = TextForm.of( null, "7", "9", null );

        assertEquals( "12345", f1.fill( 1, 3, 5 ) );
        assertEquals( "678910", f2.fill( 6, 8, 10 ) );

        TextForm f3 = TextForm.combine(f1,f2);

        assertEquals( f3.getBlanksCount(), f1.getBlanksCount() + f2.getBlanksCount() );
        assertEquals( f3.getFixedText(), f1.getFixedText() + f2.getFixedText() );
        assertEquals( "12345678910", f3.fill( 1, 3, 5, 6, 8, 10 ) );
    }

    public void testOfBetweenStrings(){
        TextForm ft = TextForm.of( "A", "C", "E", "G" );
        assertEquals( ft.getFixedText(), "ACEG" );
        assertEquals( "ABCDEFG", ft.fill( "B", "D", "F" ) );
        assertEquals( 3, ft.getBlanks().cardinality() );

        assertTrue( ft.getBlanks().get( 1 ) );
        assertTrue( ft.getBlanks().get( 3 ) );
        assertTrue( ft.getBlanks().get( 5 ) );

        assertEquals( 3, ft.getBlanksCount() );

        assertEquals( ft.getTextSegmentAfterBlank( 0 ), "C" );
        assertEquals( ft.getTextSegmentAfterBlank( 1 ), "E" );
        assertEquals( ft.getTextSegmentAfterBlank( 2 ), "G" );

        assertEquals( ft.getTextSegmentBeforeBlank( 0 ), "A" );
        assertEquals( ft.getTextSegmentBeforeBlank( 1 ), "C" );
        assertEquals( ft.getTextSegmentBeforeBlank( 2 ), "E" );
    }

    public void testOfDoubleBlanks(){
        //toDir create multiple (N) blanks in a row, accept (N) nulls in a row
        TextForm ft = TextForm.of( "A", null, null, "D", "F" );
        assertEquals( ft.getFixedText(), "ADF" );
        assertEquals( "ABCDEF", ft.fill( "B", "C", "E" ) );
        assertEquals( 3, ft.getBlanks().cardinality() );

        assertTrue( ft.getBlanks().get( 1 ) );
        assertTrue( ft.getBlanks().get( 2 ) );
        assertTrue( ft.getBlanks().get( 4 ) );

        assertEquals( 3, ft.getBlanksCount() );

        assertEquals( ft.getTextSegmentAfterBlank( 0 ), "" );
        assertEquals( ft.getTextSegmentAfterBlank( 1 ), "D" );
        assertEquals( ft.getTextSegmentAfterBlank( 2 ), "F" );

        assertEquals( ft.getTextSegmentBeforeBlank( 0 ), "A" );
        assertEquals( ft.getTextSegmentBeforeBlank( 1 ), "" );
        assertEquals( ft.getTextSegmentBeforeBlank( 2 ), "D" );
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
        TextForm ft = TextForm.of( null, null, "345", null );
        assertEquals( "123456", ft.fill( 1,2,6 ) );

        ft = TextForm.of( null, null, "3", "5", null );
        assertEquals( "123456", ft.fill( 1,2,4,6 ) );
    }

    public void testOfTrailingBlanks(){
        TextForm ft = TextForm.of( "12", null, null, null );
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
        TextForm replaceComment =
                new TextForm.Builder().text( comment ).compile();
        assertTrue( replaceComment.getBlanksCount() == 0 );
        //assertTrue( replaceComment.getFixedText().typesEqual( "/**/" ) );

        //replaceComment.fill(new Body(), new Object[ 0 ]);

        comment = "/+*/+**+/*+/";
        replaceComment =
                new TextForm.Builder().text( comment ).compile();
        assertTrue( replaceComment.getBlanksCount() == 0 );
        //assertTrue( replaceComment.getFixedText().typesEqual( "/*/**/*/" ) );

        comment = "/+** JAVADOC comment *+/";
        TextForm replaceJdocComment =
                new TextForm.Builder().text( comment ).compile();
        assertTrue( replaceJdocComment.getBlanksCount() == 0 );
        //assertTrue( replaceJdocComment.getFixedText().typesEqual( "/** JAVADOC comment */" ) );
    }

    public void testNoBlanks(){
        String alpha = "abcdefghijklmnopqrstuvwxyz";


        TextForm noBlanks =
                new TextForm.Builder().text( alpha ).compile();

        assertTrue( noBlanks.getBlanksCount() == 0 );

        assertTrue( noBlanks.getFixedText().equals( alpha ) );
    }

    public void testNoBlanksBuilder(){
        String alpha = "abcdefghijklmnopqrstuvwxyz";
        Builder b = new Builder();
        b.text( alpha );
        TextForm fo = b.compile();
        assertTrue( fo.getFixedText().equals( alpha ) );
    }

    public void testGetTextAfterBlanks(){
        TextForm empty =
                new Builder()
                        .blank().blank().blank().blank().compile();

        assertTrue( empty.getTextSegmentAfterBlank( -1 ).equals( "" ) );
        assertTrue( empty.getTextSegmentAfterBlank( 0 ).equals( "" ) );
        assertTrue( empty.getTextSegmentAfterBlank( 1 ).equals( "" ) );
        assertTrue( empty.getTextSegmentAfterBlank( 2 ).equals( "" ) );
        assertTrue( empty.getTextSegmentAfterBlank( 3 ).equals( "" ) );
        assertTrue( empty.getTextSegmentAfterBlank( 4 ).equals( "" ) );
        assertTrue( empty.getTextSegmentAfterBlank( 5 ).equals( "" ) );

        TextForm fo = new Builder()
                .text( "0123" ).blank().text( "567" ).blank()
                .text( "901" ).blank().text( "3456" ).blank()
                .text( "890" ).blank().text( "23" ).blank().compile();
        //( "0123_567_901_3456_890_23_", "to" );

        assertTrue( fo.getTextSegmentAfterBlank( -1 ).equals( "" ) );

        assertTrue( fo.getTextSegmentAfterBlank( 0 ).equals( "567" ) );
        assertTrue( fo.getTextSegmentAfterBlank( 1 ).equals( "901" ) );
        assertTrue( fo.getTextSegmentAfterBlank( 2 ).equals( "3456" ) );
        assertTrue( fo.getTextSegmentAfterBlank( 3 ).equals( "890" ) );
        assertTrue( fo.getTextSegmentAfterBlank( 4 ).equals( "23" ) );
        assertTrue( fo.getTextSegmentAfterBlank( 5 ).equals( "" ) );
    }

    public void testGetTextBeforeBlanks(){
        TextForm empty =
                new Builder().blank().blank().blank().blank().compile();
        assertTrue( empty.getTextSegmentBeforeBlank( -1 ).equals( "" ) );
        assertTrue( empty.getTextSegmentBeforeBlank( 0 ).equals( "" ) );
        assertTrue( empty.getTextSegmentBeforeBlank( 1 ).equals( "" ) );
        assertTrue( empty.getTextSegmentBeforeBlank( 2 ).equals( "" ) );
        assertTrue( empty.getTextSegmentBeforeBlank( 3 ).equals( "" ) );
        assertTrue( empty.getTextSegmentBeforeBlank( 4 ).equals( "" ) );
        assertTrue( empty.getTextSegmentBeforeBlank( 5 ).equals( "" ) );

        TextForm fo =
                new Builder().text("0123").blank().text( "567").blank().text( "901" )
                        .blank().text( "3456" ).blank().text( "890" ).blank() .text( "23")
                        .blank().compile();

        assertTrue( fo.getTextSegmentBeforeBlank( -1 ).equals( "" ) );

        assertTrue( fo.getTextSegmentBeforeBlank( 0 ).equals( "0123" ) );
        assertTrue( fo.getTextSegmentBeforeBlank( 1 ).equals( "567" ) );
        assertTrue( fo.getTextSegmentBeforeBlank( 2 ).equals( "901" ) );
        assertTrue( fo.getTextSegmentBeforeBlank( 3 ).equals( "3456" ) );
        assertTrue( fo.getTextSegmentBeforeBlank( 4 ).equals( "890" ) );
        assertTrue( fo.getTextSegmentBeforeBlank( 5 ).equals( "23" ) );
        assertTrue( fo.getTextSegmentBeforeBlank( 6 ).equals( "" ) );
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
        TextForm fo =
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

        TextForm fillOrder = new Builder().text( "a" ).compile();
        assertTrue( fillOrder.getTextSegmentAfterBlank( 0 ).equals( "" ) );
        assertTrue( fillOrder.getTextSegmentAfterBlank( 1 ).equals( "" ) );

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
        TextForm f2 =
                new Builder()
                        .text( "A" ).blank()
                        .text( "C" ).blank()
                        .text( "E" ).blank()
                        .text( "G" ).blank()
                        .text( "I" ).compile();
        assertTrue( f2.getTextSegmentAfterBlank( 0 ).equals( "C" ) );
        assertTrue( f2.getTextSegmentAfterBlank( 1 ).equals( "E" ) );

        assertTrue( f2.getTextSegmentAfterBlank( 2 ).equals( "G" ) );
        assertTrue( f2.getTextSegmentAfterBlank( 3 ).equals( "I" ) );
        assertTrue( f2.getTextSegmentAfterBlank( 4 ).equals( "" ) );
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
        TextForm tb = TextForm.of( "$NAME ", null, " $VALUE" );
        //String regexPattern = tb.buildRegexPattern( tb );
        //System.out.println( regexPattern );
        //Pattern p = Pattern.compile( regexPattern );

        Matcher m = tb.getRegexPattern().matcher("$NAME and stuff in between $valu $VALUE");
        assertTrue( m.matches() );
        System.out.println( m.group( 0 ) );
        System.out.println( m.group( 1) );
        System.out.println( m.group( 2 ) );

        tb = TextForm.of("first ", null, " second");
        //regexPattern = buildRegexPattern( tb );
        //p = Pattern.compile( regexPattern );

        m = tb.getRegexPattern().matcher("second something first");
        assertFalse( m.matches() );


    }

    public void testBuildRegexPatternString(){
        //builds a pattern String that can be used toDir listAnyMatch the text/blanks
        TextForm tb = TextForm.of( "return ", null, ";" );
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

        tb = TextForm.of( null, " is a ", null );
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
