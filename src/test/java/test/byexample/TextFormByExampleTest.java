package test.byexample;

import junit.framework.TestCase;
import org.jdraft.text.TextForm;
import org.jdraft.text.Template;

import java.util.BitSet;
import java.util.List;
import java.util.regex.Pattern;

/**
 * TextBlanks is a {@link Template <String>} model of text and blanks like a "Form Letter"
 * <PRE>
 *     "I ____________ on ____________ hereby dedicate this _______________
 *     the property of ______________"
 * </PRE>
 * where there is static text and "blanks" which can be filled in at a later time
 */
public class TextFormByExampleTest extends TestCase {

    /**
     *
     * One of the major concept in draft is the duality of composition and decomposition
     * we want the ability to "compose"
     *
     * TextBlanks is a simple data structure for intermingling
     * fixed text with blanks (i.e. holes where text can later be filled)
     *
     * TextBlanks is not often a "user level" data structure normally
     * a Stencil (built on top of the TextBlanks which provides parsing
     * and parameteric data binding) is used.  However for holistic
     * understanding of Stencils, it is important to understand what TextBlanks
     * is/does.
     */
    public void testTextForm_Fixed() {
        //create a TextForm that is fixed text
        TextForm fixed = TextForm.of("ABCDE");
        assertEquals("ABCDE", fixed.getFixedText());
        assertEquals(1, fixed.getTextSegments().size());
        //the fixed text has a regex pattern
        Pattern regexPattern = fixed.getRegexPattern();
        System.out.println(regexPattern);//(\QABCDE\E)

        //there are no blanks in this TextForm
        assertEquals(0, fixed.getBlanksCount());

        //we can create a new String by calling fill...
        //fixed has no "blanks" so there are no values needed to be passed to fill()
        String filled = fixed.fill();

        //since the fixed text has no blanks, we'll always return the fixed text
        assertEquals("ABCDE", filled);

        //we can use the parse method to mean (does this
        assertNotNull( fixed.parse("ABCDE") );
    }

    public void testTextForm_Blank() {
        //a simple blank textForm
        TextForm blank = TextForm.of();

        assertEquals("", blank.getFixedText());
        assertEquals(1, blank.getBlanksCount());

        //a single blank will match any text
        assertNotNull(blank.parse("Any text here will do"));

        List<String> toks = blank.parse("Any text here will do");
        assertEquals(1, toks.size()); //the blank has (1) token
        assertEquals("Any text here will do", toks.get(0));

        //getBlanks returns the character indexes of all the blanks within the text
        BitSet blankIndexes = blank.getBlanks();
        assertTrue(blankIndexes.get(0)); //verify there is a blank at the 0th char
    }

    public void testTextBlanks(){
        //by default creating a TextForm will place blanks BETWEEN text segments in a String array
        // so this will mean A___C___E (2 blanks between A-C and C-E)
        TextForm tbs = TextForm.of("A", "C", "E");
        assertEquals(2, tbs.getBlanksCount());

        //there are (3) text segments {"A" "C" "E"}
        assertEquals( 3, tbs.getTextSegments().size());

        //we can fill(B,D) and return a new String "ABCDE"
        assertEquals( "ABCDE", tbs.fill("B","D"));

        //we can also parse (from a String) and retrieve the tokens
        List<String> parsed = tbs.parse("ABCDE");
        assertEquals( "B", parsed.get(0));
        assertEquals( "D", parsed.get(1));

        //we can return the fixed text within the TextForm
        assertEquals("ACE", tbs.getFixedText());

        //we can also print a simple "template" representation of the TextForm
        String template = tbs.toString();
        //"A$1$C$2$E" this "means" A___C___E
        //                          $1$ $2$
        //  ...where there are (2) blanks signified by {$1$,$2$}
        System.out.println( template );

        // the textForm can generate and return a Regex "Pattern"
        Pattern regexPattern = tbs.getRegexPattern();
        // (\QA\E)(.*?)(\QC\E)(.*?)(\QE\E) //the (2) blanks are "(.*?)" within the pattern
        System.out.println( regexPattern );

        //to get further detail of the internals of the TextForm
        //you can query for individual segments (portions of text between blanks)
        assertEquals( "A", tbs.getTextSegmentBeforeBlank(0));
        assertEquals( "C", tbs.getTextSegmentBeforeBlank(1));

        assertEquals( "C", tbs.getTextSegmentAfterBlank(0));
        assertEquals( "E", tbs.getTextSegmentAfterBlank(1));
    }

    public void testStartingWithOrEndingWithBlank(){
        //DSL for analyzing composing & refactoring Java source code

        TextForm tbs = TextForm.of(null, "2", "4", "6", null);

        tbs.isStartsWithBlank();
        tbs.isStartsWithFixedText();
        tbs.isEndsWithBlank();
    }

}
