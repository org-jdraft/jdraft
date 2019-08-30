package org.jdraft;

import java.util.*;

/**
 * Dynamically create a T where accepting Key-Value Data Parameters
 * (i.e. a {@link Tokens}) and optionally a {@link Translator}
 * (for converting Objects to text and filling the Template)
 * T can be an unstructured String
 * or
 * T can be an Object entity (_field, _method, _anno)
 *
 * @param <T> the entity being constructed or modeled where the Template
 * (i.e. a String, or entity or an AST Node entity
 *
 * @author Eric
 */
public interface Template<T> {

    /**
     * Construct and return the T given the {@link Translator}
     * (for converting objects to text) and a map of key VALUE pairs
     *
     * @param translator converts objects to text within the composition
     * @param keyValues a map of key-VALUE data ELEMENTS used in composing the
     * text
     * @return a constructed T entity
     */
    T draft(Translator translator, Map<String, Object> keyValues );

    /**
     * Construct and return the T using the default the {@link Translator}
     * (for converting objects to text) and a map of key VALUE pairs
     *
     * @param keyValues a map of key-VALUE data ELEMENTS used in composing the
     * text
     * @return a constructed T entity
     */
    default T draft(Map<String, Object> keyValues ){
        return draft( Translator.DEFAULT_TRANSLATOR, keyValues );
    }

    /**
     * construct and return a new T given the tokens
     *
     * @param keyValues alternating key, and values
     * @return
     */
    default T draft(Object... keyValues ){
        return draft(Translator.DEFAULT_TRANSLATOR, Tokens.of(keyValues));
    }

    /**
     * construct and return a new T given the tokens and translator
     *
     * @param translator converts values from Objects to Strings when composing
     * @param keyValues alternating key, and values
     * @return
     */
    default T draft(Translator translator, Object... keyValues ){
        return draft(translator, Tokens.of(keyValues));
    }

    /**
     * fill in the values of the Template sequentially
     * (i.e. the first VALUE of values) is mapped to the first $parameter (i.e.
     * the first VALUE in {@link #list$Normalized()} )
     * <PRE>
     *     Template<String> st = Stencil.of("1 $1$ 2 $2$ 3 $3$");
     *     //fill the parameters inline (no keyvalue pairs)
     *     assertEquals( "1 A 2 B 3 C", st.fill("A","B","C"));
     *
     *     Template<String> st = Stencil.of("1 $1$ 2 $1$ 3 $1$ ");
     *     //with only (1) unique parameter, we only need (1) fill parameter
     *     assertEquals( "1 a 2 a 3 a", st.fill("a"));
     *
     *     //with only (1) unique parameter (besides the uppercase variant), we only need (1) fill parameter
     *     Template<String> st = Stencil.of("1 $a$ 2 $A$ 3 $a$ ");
     *     assertEquals( "1 f 2 F 3 f", st.fill("f"));
     * </PRE>
     * @param values data to populate the template
     * @return the constructed T (i.e. _method, _field, _ctor)
     */
    default T fill( Object... values ){
        return fill(Translator.DEFAULT_TRANSLATOR, values );    
    }

    /**
     * fill in the values of the Template sequentially
     * (i.e. the first VALUE of values) is mapped to the first $parameter (i.e.
     * in {@link #list$Normalized()} ) example:
     * <PRE>
     *     Template<String> st = Stencil.of("1 $1$ 2 $2$ 3 $3$");
     *     //fill the parameters inline (no keyvalue pairs)
     *     assertEquals( "1 A 2 B 3 C", st.fill("A","B","C"));
     *
     *     Template<String> st = Stencil.of("1 $1$ 2 $1$ 3 $1$ ");
     *     //with only (1) unique parameter, we only need (1) fill parameter
     *     assertEquals( "1 a 2 a 3 a", st.fill("a"));
     *
     *     //with only (1) unique parameter (besides the uppercase variant), we only need (1) fill parameter
     *     Template<String> st = Stencil.of("1 $a$ 2 $A$ 3 $a$ ");
     *     assertEquals( "1 f 2 F 3 f", st.fill("f"));
     * </PRE>
     *
     * @param translator translates Objects to text within the t
     * @param values data to populate the template
     * @return the constructed T (i.e. _method, _field, _ctor)
     */
    default T fill( Translator translator, Object... values ){
        List<String> keys = list$Normalized();
        if( values.length < keys.size() ){
            throw new _draftException("not enough values("+values.length+") to fill ("+keys.size()+") variables "+ keys);
        }
        Map<String,Object> kvs = new HashMap<>();
        for(int i=0;i<values.length;i++){
            kvs.put( keys.get(i), values[i]);
        }
        return draft( translator, kvs );
    }

    /**
     * Post - parameterize, create a parameter ($Name) from the target String.<BR/>
     *
     * Parameterize some text (i.e. convert a segment of literal text within
     * a "template" into a parameter that data can be mapped to)
     * for instance:
     * <PRE>
     * String text = "Today is Monday, the 1st day of the week";
     * Template<String> st = Stencil.of( text);
     * st = st.$("Monday", "day");   //make "Monday" a parameter "day"
     * st = st.$("1st", "dayOfWeek");//make "1st" a parameter "dayOfWeek"
     *
     * //here is the resulting template (st)
     * assertEquals(st, Stencil.of("Today is $day$, the $dayOfWeek$ day of the week"));
     * assertTrue(
     *
     * //we can construct or fill the post parameters
     * String tues = st.construct("Tuesday", "2nd");
     * //tues =  "Today is Tuesday, the 2nd day of the week"
     * String wed = st.construct("Wednesday", "3rd");
     * //wed =  "Today is Wednesday, the 3rd day of the week"
     * </PRE>
     *
     * @param target the target text to be converted into a parameter
     * @param $Name the parameter NAME
     * @return the modified Template
     */
    Template<T> $( String target, String $Name );

    /**
     *
     * @return Lists the $ PARAMETERS names in the order they appear in the
     * "template"
     * NOTE: (MAY contain duplicates and Capitalization variants)
     */
    List<String> list$();

    /**
     *
     * @return a List all of the unique / normalized $params that can be
     * populated
     * NOTE: (No duplicates and caps variants)
     */
    List<String> list$Normalized();
}