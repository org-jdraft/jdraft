package org.jdraft;

import junit.framework.TestCase;

import java.util.List;
import java.util.Map;

/**
 *
 * @author Eric
 */
public class StencilTest
        extends TestCase {

    /*
    public void testNest(){
        Stencil n = Stencil.of("Hello dear sir, this is $person$ speaking");
        assertEquals("Hello dear sir, this is Eric speaking", n.compose("person", "Eric"));
        n.$("Hello dear sir,", Stencil.of("Hello, $greeting$,") );

        System.out.println( n );
        assertEquals( "$${1}$$ this is $$person$$ speaking", n.toString());

        String str = n.compose("person", "Eric", "greeting", "Greet");

        System.out.println( str );
        assertEquals( "Hello, Greet, this is Eric speaking", str);
    }
    */

    public void testOnlyBlank(){
        Stencil s = Stencil.of("$name$");
        Tokens all =
                s.parse("The whole thing");
        assertTrue( all.size() == 1);
        assertEquals( all.get("name"), "The whole thing" );

    }

    public void testParamOnMultiLine(){
        Stencil s = Stencil.of("this is the first line", "$any$", "this is the last line");

        //make sure I can tokenize with exactly one line in between
        Tokens kvs = s.parse("this is the first line", "some data", "this is the last line");
        assertNotNull(kvs );

        kvs = s.parse("this is the first line", "some data", "more data", "this is the last line");
        assertNotNull( kvs );
        assertTrue( ((String)kvs.get("any" )).contains("some data"));
        assertTrue( ((String)kvs.get("any" )).contains("more data"));
    }

    public void testAssign$() {
        Stencil st = Stencil.of( "SELECT $select$ FROM $from$ WHERE $where$" );
        Stencil st2 = st.hardcode$( Translator.DEFAULT_TRANSLATOR, Tokens.of("from", "PERSON"));
        assertEquals( st2, Stencil.of( "SELECT $select$ FROM PERSON WHERE $where$") );

    }
    public void testEquals(){
        assertEquals( Stencil.of( "$a$ and a $b$ of $c$;" ), Stencil.of( "$a$ and a $b$ of $c$;" ));
        assertEquals( Stencil.of( "$a$" ), Stencil.of( "$a$" ));
        assertEquals( Stencil.of( "static text" ), Stencil.of( "static text" ));
    }

    public void testStencilExtract(){
        Stencil s = Stencil.of( "$a$ and a $b$ of $c$;" );
        List<String> strs = s.getTextBlanks().decompose("XXX and a YYY of ZZZ;");
        assertEquals( Tokens.of("a", "XXX", "b", "YYY", "c", "ZZZ"), s.parse("XXX and a YYY of ZZZ;"));

    }

    //TODO Stencil.bind() instead of Stencil.compose();

    public void testStencilRoundTripExtract(){
        Stencil s = Stencil.of( "return $any$;" );
        assertEquals( "return 3+4;", s.draft( "any", "3+4"));
        assertEquals( Tokens.of("any", "3 + 4"), s.parse("return 3 + 4;"));

        assertEquals( "return \";\";", s.draft( "any", "\";\""));
        assertEquals( Tokens.of("any", "\";\"") , s.parse("return \";\";"));
    }


    public void testStencilToString() {
        Stencil s = Stencil.of( "hello $a$ this is $b$" );
        assertEquals( "hello $a$ this is $b$", s.toString() );
        assertEquals("hello 1 this is 2", s.draft("a", 1, "b", 2));
        assertEquals("hello 1 this is 2", s.fill(1,2));
    }


    public void testStencilCaseRoundTrip() {
        Stencil s = Stencil.of(
                "public $type$ get$Name$(){",
                "    return $name$;",
                "}" );

        //String str
        //    = s.compose( "TYPE", int.class, "NAME", "x" );
        //assertTrue( str.contains( "getX" ) );


        String str = s.fill( int.class, "x" );
        assertTrue( str.contains( "getX" ) );

        String expect =
                "public int getX(){"+System.lineSeparator()+
                        "    return x;"+System.lineSeparator()+
                        "}";
        assertEquals( expect, str );

        //make sure we can tokenize the variables back out
        assertEquals( Tokens.of("name", "x", "Name", "X", "type", "int" ), s.parse( expect ));

    }

    /** Stencil is a template for simple text processing & the foundation of draft template specialization*/
    public void testStencilSoupToNuts() {

        //create a $signature with a single $parameter$ "NAME"
        Stencil st = Stencil.of("Hi $name$!");

        //compose($nameValues...) populates the NAME-VALUE pairs into the template based on the $NAME
        assertEquals("Hi eric!", st.draft("name", "eric"));

        //fill(values...) populates the values based on the order in the template
        assertEquals("Hi eric!", st.fill("eric"));

        //a $variable$ with the first letter capitalized will ensure the parameter VALUE has its first letter in caps
        st = Stencil.of("Hi $Name$!");

        assertEquals("Hi Eric!", st.draft("name", "eric"));
        assertEquals("Hi Eric!", st.fill("eric"));

        //variables that are ALL CAPS like NAME will _2_template the values toDir be ALL CAPS
        st = Stencil.of("Hi $NAME$!");

        assertEquals("Hi ERIC!", st.draft("name", "eric"));
        assertEquals("Hi ERIC!", st.fill("eric"));

        //heres a more concrete example, creating a getter
        st = Stencil.of("public $type$ get$Name$(){",
                "return this.$name$;",
                "}");

        //note getX() uses an uppercase X, also
        assertEquals( "public int getX(){"+System.lineSeparator()+
                        "return this.x;"+System.lineSeparator()
                        +"}",
                st.fill(int.class, "x"));

        //about translators
        //Note that above, we passed in int.class and it was printed out as "int"
        //the $signature converts data values toDir Strings using "Translators"... and the
        //default Translator is used by the Stencil if none is provided
        assertEquals( "int", Translator.DEFAULT_TRANSLATOR.translate(int.class));

        //if we wanted toDir instead print all classes by their fully qualified NAME
        // with ".class" at the end... for instance: "java.util.Map.class"
        // we would need toDir provide a new translator toDir the Stencil
        Translator classCanonName = new Translator(){
            public Object translate(Object in){
                if( in instanceof Class ) {
                    return ((Class) in).getCanonicalName() + ".class";
                }
                return in;
            }
        };
        assertEquals( "int.class", classCanonName.translate(int.class));
        assertEquals( "java.util.Map.class", classCanonName.translate(Map.class));

        //now that we know our translator does what we want,
        // we can pass the translator in as the FIRST parameter toDir either compose or fill and it will populate things
        Stencil arrClasses = Stencil.of( "Class[] cs = { $classes$ };" );
        assertEquals( "Class[] cs = { int.class };", arrClasses.draft( classCanonName, "classes", int.class ));
        assertEquals( "Class[] cs = { java.util.Map.class };", arrClasses.draft( classCanonName, "classes", Map.class ));
        assertEquals( "Class[] cs = { int.class };", arrClasses.fill( classCanonName, int.class ));
        assertEquals( "Class[] cs = { java.util.Map.class };", arrClasses.fill( classCanonName, Map.class ));

        //a RootTranslator aggregates many translators toDir deal with all kinds of data and nested data types
        //(data within Arrays and collections)
        // instead of directly using a custom Translator in a Stencil, it is advisable toDir use a custom RootTranslator
        // toDir manage all different aggregations of data values (i.e. Arrays or Lists of Classes)
        Translator.Multi rt = Translator.Multi.of("null", ", ",
                Translator.CollectionToArray, //converts collections (lists sets) toDir arrays
                classCanonName, // "java.util.Map.class"
                Translator.Primitive //numbers with postfixes (i.e. "3.14f" for floating point, "2.77d" for double)
        );

        //make sure the translator works like we expect
        assertEquals("int.class, java.util.Map.class", rt.translate(new Class[]{int.class, Map.class}));

        //now pass in the RootTranslator instance "rt" toDir fill or compose
        assertEquals( "Class[] cs = { java.util.Map.class, int.class };",
                arrClasses.fill(rt, (Object)new Class[]{Map.class, int.class} ));

        assertEquals( "Class[] cs = { java.util.Map.class, int.class };",
                arrClasses.draft(rt, "classes", (Object)new Class[]{Map.class, int.class} ));

    }

    public void testExtract() {
        Stencil st = Stencil.of("$a$ this $b$ is $c$, OK");

        //a manufactured $signature toDir tokenize from
        String toExtract = "Eric this math is hard, OK";
        //segs             "     this      is     , OK"
        //segs             "    ------    ----    ----"
        //params           "----      ----    ----    "
        //params           "$a$       $b$     $c$
        TextBlanks tb = st.getTextBlanks();
        assertFalse(tb.startsWithText());
        assertTrue(tb.startsWithBlank());

        List<String> segs = tb.getTextSegments();
        System.out.println( segs );
        assertEquals(3, segs.size());

        //the textBind will tokenize
        List<String> vals = st.getTextBlanks().decompose(toExtract);
        assertEquals("Eric", vals.get(0));
        assertEquals("math", vals.get(1));
        assertEquals("hard", vals.get(2));

        Tokens extracted =  st.parse(toExtract);

        assertEquals( "Eric", extracted.get("a"));
        assertEquals( "math", extracted.get("b"));
        assertEquals( "hard", extracted.get("c"));
    }

    public void testExtract2(){
        Stencil st2 = Stencil.of("public static final $type$ $paramName$ = $init$");
        String MyVAL = "public static final String STATIC = \"Eric\"";

        List<String>vals = st2.getTextBlanks().decompose( MyVAL );

        assertEquals( vals.get(0), "String");
        assertEquals( vals.get(1), "STATIC");
        assertEquals( vals.get(2), "\"Eric\"");

        Tokens paramVals = st2.parse( MyVAL );

        assertEquals( paramVals.get("type"), "String");
        assertEquals( paramVals.get("paramName"), "STATIC");
        assertEquals( paramVals.get("init"), "\"Eric\"");
    }

    public void testExtractFail(){
        Stencil s = Stencil.of("this is $a$ problem");
        assertNull( s.parse("this is alright no") );

        s = Stencil.of("this is $a$ $b$");
        assertEquals( Tokens.of("a", "alright","b","no"), s.parse("this is alright no") );
    }
}
