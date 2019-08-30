package test.byexample;

import junit.framework.TestCase;
import org.jdraft.Translator;

import java.lang.reflect.AnnotatedType;
import java.util.ArrayList;
import java.util.Map;

/**
 * The Translator is an interface that exists to serialize some Object representation
 * to a String (for filling Stencils, TextForms or other Templates) It is used in scenarios
 * where the normal toString() method does not adequately represent the data. for example:
 * <PRE>
 * int[] i = new int[] {1,2,3,4};
 *
 * System.out.println( i );
 * //prints out "[I@68bbe345"
 *
 * //however... if we pass the same array to
 * System.out.println( Translator.DEFAULT_TRANSLATOR.translate(i));
 * //prints out "1,2,3,4"
 *
 * </PRE>
 * @see org.jdraft.TextForm
 * @see org.jdraft.Stencil
 * @see org.jdraft.Template
 */
public class TranslatorByExampleTest extends TestCase {

    /**
     * The Default translator will handle translating
     * @throws NoSuchMethodException
     */
    public void testDefaultTranslator() throws NoSuchMethodException {

        Translator DEF = Translator.DEFAULT_TRANSLATOR;

        assertEquals( "null", DEF.translate(null));
        assertEquals("1", DEF.translate(1));
        assertEquals( Long.MAX_VALUE+"L", DEF.translate(Long.MAX_VALUE));
        assertEquals( "true", DEF.translate(Boolean.TRUE) );
        assertEquals("'c'", DEF.translate('c'));
        assertEquals("java.lang.String", DEF.translate(String.class));

        //Translates
        assertEquals("1,2", DEF.translate(new int[]{1,2}) );
        assertEquals("eric,bo", DEF.translate(new String[]{"eric","bo"}) );
        assertEquals("'a','b'", DEF.translate(new char[]{'a','b'}) );

        assertEquals("java.lang.String,java.util.Map", DEF.translate(new Class[]{String.class, Map.class}) );

        //generic
        class CustomClassWithToString{
            public int a;
            public String toString(){
                return "toString("+a+")";
            }
            public CustomClassWithToString(int a){
                this.a = a;
            }
        }

        assertEquals( "toString(1)", DEF.translate(new CustomClassWithToString(1)));

        assertEquals( "toString(1),toString(2)", DEF.translate(new Object[]{new CustomClassWithToString(1), new CustomClassWithToString(2)}));


        //translates Reflective Types (including the Generics)
        class Internal{
            public Map<String,Integer> ms(){ return null; }
        }
        //an AnnotatedType maintains the generic information unlike Class
        AnnotatedType at = Internal.class.getMethod("ms", null).getAnnotatedReturnType();

        assertEquals("java.util.Map<java.lang.String, java.lang.Integer>", DEF.translate(at));
    }

    /**
     * You can use the Translator.Multi class to build a custom Translator
     * by configuring
     *
     * the Multi will help "unroll" arrays and Collections and ensure the individual
     * elements within these arrays or Collections are converted individually
     */
    public void testCustomTranslator(){

        Translator MyTranslator = new Translator.Multi(
                "", //use an empty String to represent a null VALUE
                ", ",// use a comma (AND SPACE) to separate ELEMENTS of an array (or collection)
                Translator.ClassToString, //NAME of the class "java.util.HashMap" instead of "class java.util.HashMap"
                Translator.TypeTranslate, //returns a String representation of a Type (i.e. "java.util.Map<K,V>")
                Translator.CollectionToArray, //collections are converted to arrays
                Translator.Primitive //translate primitives to string literals 1.0f = "1.0f", 'c' to "'c'"
        );

        assertEquals( "", MyTranslator.translate(null));
        assertEquals( "1, 2, 3", MyTranslator.translate(new int[]{1,2,3}));
        ArrayList al = new ArrayList(){{
            add(1);
            add(true);
            add('c');
            add(12345678901L);
            add(null);
        }};
        assertEquals( "1, true, 'c', 12345678901L, ", MyTranslator.translate(al));
    }
}
