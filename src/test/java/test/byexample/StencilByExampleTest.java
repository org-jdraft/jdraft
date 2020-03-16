package test.byexample;

import junit.framework.TestCase;
import org.jdraft.text.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * A Stencil is both:
 * <UL>
 * <LI>A Template/Pattern for binding parameter values and building structured text
 * <LI>A way for parsing/extracting parameters form structured text that follows the pattern
 * </UL>
 * The Stencil API builds on top of the {@link TextForm} API to provide
 * NAMED parameter bindings.
 *
 * (it is a little bit like a "fill in the blanks" form and like a very Regex)
 *
 * We demarcate things that are intended to be "parameters" within the text
 * by using prefix and postfix $ $s
 *
 * @see TextForm
 * @see TranslatorByExampleTest
 * @see TextFormByExampleTest
 */
public class StencilByExampleTest extends TestCase {

    /**
     * A Fixed stencil is just static text to match against
     */
    public void testFixedStencil() {
        //a Stencil can be a simple fixed sequence of characters
        Stencil fixed = Stencil.of("This is simple text");

        //It will match only the same exact fixed text
        assertTrue(fixed.matches("This is simple text"));
        assertFalse(fixed.matches("this is different text"));

        //parse is like match, it will "parse variables" from within the stencil
        //it returns a Tokens() if a match is found,
        assertNotNull(fixed.parse("This is simple text"));

        //...null if the text doesnt match
        assertNull(fixed.parse("This text does not match the fixed stencil"));

        //you can always ask if a stencil instance is a fixedText stencil:
        assertTrue(fixed.isFixedText());

        //stencils can also be constructed multi lined,
        // (here we use a varargs constructor for a stencil on (3) lines (each String on a separate line)
        Stencil multiLine  = Stencil.of("multi",
                "line",
                "stencil");

        String manualMultiLine = "multi"+System.lineSeparator()
                + "line"+System.lineSeparator()
                +"stencil";

        assertEquals( manualMultiLine, multiLine.draft() );

        //it also works i you pass in a single (multi line) string
        assertEquals( multiLine, Stencil.of(manualMultiLine) );
    }

     /**
      * A Stencil can have parameterized values... we signify a "parameterized value"
      * as a name (sequence of characters) surrounded with the $ prefix and suffix:
      * for example: $param$
      * a Stencil that consists of:
      * 1) NO fixed text (including spaces, carriage returns, etc.)
      * 2) a single named $param$
      * ...is called a "MatchAny" Stencil (because it will match ANY sequence of characters or even null)
      */
     public void testMatchAnyStencil(){
        //A Stencil can have parameterized values (with a prefix and postfix $)
        //Here is a parameterized Stencil that is made up only of the parameter "any"
        Stencil any = Stencil.of("$matchAnything$");

         //stencils that are made up of ONLY a single parameter (with no constant text) are
         //special... they are called "match any" stencils, you can ask each stencil if
         // it's a matchAny stencil
         assertTrue(any.isMatchAny());
         assertFalse(any.isFixedText()); //matchAny is effectively the opposite of a "FixedText" Stencil
         assertEquals( "matchAnything", any.$list().get(0));

        //when the entire stencil is a parameter, it will match ANY string of text
        assertTrue(any.matches("Any text will match YOLO!@@##"));

        //when we want to extract the value of the parameter from within any matching Stencil
        // we call parse(...) which returns Tokens (a map of key value pairs)
        Tokens tokens = any.parse("Anything");

        //Tokens is a map (we can get values based on the $param$ key)
        assertEquals(tokens.get("matchAnything"), "Anything");

        // we also have a convenience method is() where we can pass in many key value
        // pairs to check the value of all of the tokens
        assertTrue(tokens.is("matchAnything", "Anything"));
    }

    public void testParameterizedStencil(){
        // "fixedText" stencils and "matchAny" stencils are of limited usefulness
        // stencils containing both fixed text and parameters are more valuable
        Stencil ifThen = Stencil.of("if($cond$){$then$}");

        //the ifThen Stencil is neither a matchAny or FixedText stencil
        assertFalse(ifThen.isMatchAny());
        assertFalse(ifThen.isFixedText());

        //we can get all of the parameterized names from the stencil with list$()
        List<String> $params = ifThen.$list();
        assertTrue( $params.contains("cond") );
        assertTrue( $params.contains("then") );

        //we can use the stencil to match
        assertTrue(ifThen.matches("if(a==1){out.print(1);}"));

        //here we can parse out the parameters as Tokens  {cond, then}
        Tokens ts = ifThen.parse("if(a==1){out.print(1);}");

        //verify the tokens contains what we expect when parsing the String
        assertTrue(ts.is("cond", "a==1", "then", "out.print(1);"));

        //NOTE: if we try to parse(...) a String that DOESNT match the stencil
        // we get a null back (instead of a Tokens)
        assertNull(ifThen.parse("for(int i=0;i<100;i++){out.print(1);}"));

        // we can ALSO do the opposite of parse(...) a String to retrieve Tokens
        // with draft(...) we can BUILD a String by populating key value
        // pairs that belong in the Stencil
        String drafted = ifThen.draft("cond", "a==2", "then", "out.print(2);");
        assertEquals("if(a==2){out.print(2);}", drafted);

        // fill(...) is like draft, accept you pass in the VALUES ONLY and internally
        // fill(...) will assign the values to parameters in the order they appear
        // in the stencil { {cond,"a==3"}, {then, "out.print(3);"}
        String filled = ifThen.fill("a==3", "out.print(3);");
        assertEquals("if(a==3){out.print(3);}", filled);
    }


    /**
     * when populating a Stencil with the fill() or draft() methods you can pass Objects
     * that are translated using Translator.DEFAULT_TRANSLATOR into appropriate textual representation
     *
     * @see Translator#DEFAULT_TRANSLATOR
     * @see TranslatorByExampleTest
     */
    public void testFillDraftTranslate(){
        Stencil lValue = Stencil.of( "$label$ is $values$");

        String aString = lValue.fill("String", "String");
        assertEquals( "String is String", lValue.fill("String", "String"));
        assertEquals( "Quoted is \"Quoted\"", lValue.fill("Quoted", "\"Quoted\""));
        assertEquals( "int is 101", lValue.fill("int", 101));
        assertEquals( "boolean is true", lValue.fill("boolean", true));
        assertEquals( "char is 'c'", lValue.fill("char", 'c'));
        assertEquals( "float is 1.0f", lValue.fill("float", 1.0f));
        assertEquals( "double is 1.0d", lValue.fill("double", 1.0d));

        assertEquals( "class is java.util.Map", lValue.fill("class", Map.class));

        //translating arrays & array elements for fill() and draft()
        assertEquals( "String[] is A,B,C", lValue.fill("String[]", new String[]{"A","B","C"}));
        assertEquals( "int[] is 1,2,3", lValue.fill("int[]", new int[]{1,2,3}));
        assertEquals( "Object[] is A,1,true,'c'", lValue.fill("Object[]", new Object[]{"A",1,true,'c',}));

        List<String> ls = new ArrayList<String>();
        ls.add("A");
        ls.add("E");
        ls.add("I");

        assertEquals( "List<String> is A,E,I", lValue.fill("List<String>", ls));

        //Collections (Lists, Sets)
        List lo = new ArrayList();
        lo.add('c');
        lo.add(1.0d);
        lo.add(Map.class);

        assertEquals( "List is 'c',1.0d,java.util.Map", lValue.fill("List", lo));

        //when objects are passed to fill() or draft(), the toString method is called
        class RandoClass{
            public String toString(){
                return "(ToString)";
            }
        }
        assertEquals( "SomeOtherClass is (ToString)", lValue.fill("SomeOtherClass", new RandoClass()));
    }

    /**
     * by convention params are lowercase (i.e. "$name$),
     * if the first letter of a $parameter is uppercase (i.e. "$Name$"),
     * the value passed in will "first capped" (i.e. "eric" ->"Eric")
     * if the entire parameter is uppercase, (i.e. "$NAME$)
     * the value passed in will be "full capped" (i.e. "eric"-> "ERIC")
     */
    public void testFillDraftCapitalization(){
        Stencil cap = Stencil.of( "$name$ $Name$ $NAME$");
        List<String> $params = cap.$list();
        assertEquals(3, $params.size());
        assertEquals("name", $params.get(0));
        assertEquals("Name", $params.get(1));
        assertEquals("NAME", $params.get(2));

        //we can find the number of required/distinct params by calling list$Normalized();
        List<String>$normalized = cap.$listNormalized();
        assertEquals(1, $normalized.size()); //only (1) distinct $param "name"
        assertEquals("name", $normalized.get(0));

        //when we draft the stencil, even though we have (3) distinct parameters
        //{name, Name, NAME} we only need to pass in a single key/value {"name"}
        //and the Stencil will adapt the value
        String names = cap.draft("name", "dennis");

        //here we verify that the only unique
        assertEquals("name", $params.get(0));

        //the string value of the parameters passed to
        assertEquals("dennis Dennis DENNIS", names);
        assertEquals("eric Eric ERIC", cap.fill("eric"));
        assertEquals("caps Caps CAPS", cap.fill("caps"));
    }

    /**
     * It is important to know that Stencils are IMMUTABLE after construction, but
     * we can create and return NEW INSTANCES that are variants of existing Stencils
     * AFTER the Stencil has been created:
     *
     * The below methods will return new Variants of Stencils with modifications
     * @see Stencil#$(String, String)
     * @see Stencil#$hardcode(Object...)
     * @see Stencil#rename$(String, String)
     *
     * Here is an example of using the $() (parameterize) method
     * to replace ALL instances of some fixed text within a Stencil with a
     * $parameter
     *
     */
    public void testPost$Parameterize(){
        //heres a simple stencil with a single $parameter
        Stencil ifCond = Stencil.of("if($cond$){ then(); }");
        assertEquals(1,  ifCond.$list().size()); //{"cond"}
        assertEquals( "if(x==10){ then(); }", ifCond.draft("cond", "x==10") );

        //if we have a Stencil and we want to ADD A PARAMETER WHERE FIXED TEXT IS, we use $(...)
        //                        target text, $param name
        Stencil ifThen = ifCond.$(" then(); ", "then");
        assertEquals( 2, ifThen.$list().size()); // {cond, then}
        assertEquals( "if(x==10){out.print(10);}", ifThen.draft("cond", "x==10", "then", "out.print(10);") );

        //here is an example of a fixed method (no $parameters)
        Stencil fixedMethod = Stencil.of(
                "public void setUrl(String url){",
                "    this.url = url;",
                "}");
        assertTrue( fixedMethod.isFixedText() );

        //create and return a Stencil that $parameterizes the name and type:
        Stencil setMethod = fixedMethod
                //we replace the ANY and ALL matches to the exact text with a $parameter
                .$("url", "name") //(3x)
                .$("Url", "Name") //(1x)
                .$("String", "type"); //(1x)

        //we can always print out the stencil to understand the pattern
        System.out.println( setMethod );
        //prints :
        //public void set$Name$($type$ $name$){
        //    this.$name$ = $name$;
        //}

        //there should be (5) blanks to be filled with the (3) parameters
        assertEquals( 5, setMethod.$list().size()); //{"Name", "type", "name", "name", "name"}
        assertEquals( 2, setMethod.$listNormalized().size()); //{"name", "type"}

        assertEquals(
                Text.combine("public void setLength(int length){",
                "    this.length = length;",
                "}"),
                setMethod.fill("length", int.class));

        //the Stencil itself extends the functionality provided by the TextForm
        // we can get the TextForm and use its API
        /** @see TextFormByExampleTest */
        TextForm tf = setMethod.getTextForm();
    }

    public void test(){
        Stencil s = Stencil.of("public $type$ get$Name$(){ return this.$name$; }");
        TextForm tf = s.getTextForm();

    }
    /**
     *
     * @see Stencil#$hardcode(Translator, Tokens)
     * @see Stencil#$hardcode(Tokens)
     * @see Stencil#$hardcode(Object...)
     * @see Stencil#$hardcode(Translator, Object...)
     */
    public void testHardcodePostProcess(){

        Stencil ifThenElse = Stencil.of("if($cond$){$then$}$else$");
        assertEquals( "if(a==4){out.print(4);}else{out.print(\"not 4\");}",
                ifThenElse.draft("cond", "a==4", "then", "out.print(4);", "else", "else{out.print(\"not 4\");}"));

        //hardcode$ will Return a NEW stencil with the $else$ parameter hardcoded at "" (empty)
        Stencil ifThen = ifThenElse.$hardcode("else", "");

        //now we can create the if then (w/o ANY else statement)
        assertEquals( "if(a==4){out.print(4);}", ifThen.draft("cond", "a==4", "then", "out.print(4);"));

        //NOTE: it's important to note that we did NOT CHANGE the original ifThenElse Stencil
        //but rather created and returned a NEW STENCIL called ifThen
        assertEquals( "if(a==4){out.print(4);}else{out.print(\"not 4\");}",
                ifThenElse.draft("cond", "a==4", "then", "out.print(4);", "else", "else{out.print(\"not 4\");}"));
    }


    /**
     * we can construct and merge stencils (one after another)
     * to create a single "super stencil" (just pass all the stencils in to Stencil.of(...))
     */
    public void testMergeMultipleStencils(){
        Stencil ifThenPart = Stencil.of(
                "if($cond$){",
                "    $then$",
                "}");

        Stencil elsePart = Stencil.of(
                "else{",
                "    $else$",
                "}");

        Stencil ifThenElse = Stencil.of(ifThenPart, elsePart);
        String ite = ifThenElse.draft("cond", "a==0",
                "then", "out.print(0);",
                "else", "out.print(\"not 0\");");
        System.out.println( ite );
    }


}
