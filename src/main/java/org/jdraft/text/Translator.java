package org.jdraft.text;

import java.lang.reflect.*;
import java.util.Collection;

/**
 * Translates Objects/primitives to String
 *
 * NOTE: the interface returns Object because it may "call itself" in a
 * meta-circular or recursive fashion...
 *
 * for instance, if we have an List of custom objects:
 * <PRE>{@code
 * List<MyCustomBean> beanList = new ArrayList<>();
 * }
 * </PRE>
 * ...we can: 
 * 1) convert the list into an array (MyCustomBean[]) 
 * 2) iterate over each element in the array to a convert each instance to a String
 * </PRE>
 * @author Eric
 */
public interface Translator {

    /**
     * Given the source, translate and return the translation
     *
     * @param source
     * @return the translated version of the object
     */
    Object translate( Object source );

    /**  Translates a Java Class to it's canonical NAME when writing to text*/
    Translator ClassToString = (Object source) -> {
        if( source == null ) {
            return "";
        }
        if( source instanceof Class ) {
            Class<?> clazz = (Class<?>)source;

            if( clazz.isPrimitive() ) {
                return clazz.getSimpleName();
            }
            return clazz.getCanonicalName();
        }
        return source;
    };

    /**
     * Translates any Collection to an Object Array
     * (so each element can be handled where a element translator)
     */
    Translator CollectionToArray = (Object source) -> {
        if( source instanceof Collection ) {
            Collection<?> coll = (Collection<?>)source;
            return coll.toArray( new Object[ 0 ] );
        }
        return source;
    };

    /** Converts Type or AnnotatedType instances to their names */
    Translator TypeTranslate = (Object source) -> {
        if( source instanceof AnnotatedType ) {
            AnnotatedType t = (AnnotatedType)source;
            return t.getType().getTypeName();
        }
        if( source instanceof Type ) {
            return ((Type)source).getTypeName();
        }
        return source;
    };

    /** Converts primitives/Wrappers to Text*/
    Translator Primitive = (Object source) -> {
        if( Character.class.isAssignableFrom( source.getClass() ) ) {
            return "'" + source.toString() + "'";
        }
        if( Long.class.isAssignableFrom( source.getClass() ) ) {
            return source.toString() + "L";
        }
        if( Integer.class.isAssignableFrom( source.getClass() ) ) {
            return source.toString();
        }
        if( Float.class.isAssignableFrom( source.getClass() ) ) {
            return source.toString() + "f";
        }
        if( Double.class.isAssignableFrom( source.getClass() ) ) {
            return source.toString() + "d";
        }
        if( Byte.class.isAssignableFrom( source.getClass() ) ) {
            return "(byte)" + source.toString();
        }
        if( Short.class.isAssignableFrom( source.getClass() ) ) {
            return "(short)" + source.toString();
        }
        return source;
    };

    /**
     * Translate objects passed into Stencil (via fill() or compose()}) to
     * Strings
     */
    Translator DEFAULT_TRANSLATOR = new Translator.Multi(
            "null", //use the word "null" to represent a null VALUE
            ",",// use a comma to separate ELEMENTS of an array (or collection)
            Translator.ClassToString, //return the NAME of the class "java.util.HashMap.class"
            Translator.TypeTranslate, //returns a String representation of a Type (i.e. "Map<K,V>")
            Translator.CollectionToArray, //collections are converted to arrays
            Translator.Primitive //translate primitives to string literals 1.0f = "1.0f"
    );

    /**
     * Translates Java Elements (Classes, primitives, literals, arrays) to
     * appropriate String representations for code.
     *
     * for instance, if we pass the RootTranslator jet = new RootTranslator();
     * <PRE>{@code
     * String translated = jet.translate( 'a' );
     *                     "'a'";
     *
     * String translated = jet.translate( Math.PI );
     *                     "3.14159d"; //-- Note the d POSTFIX
     *
     * String translated = jet.translate( 1.0f );
     *                     "1.0f"; //-- Note the F POSTFIX
     * }</PRE>
     *
     * @author M. Eric DeFazio
     */
    class Multi implements Translator {

        public static Multi of( String nullTranslation,
                                String arraySeparator, Translator... translators ) {
            return new Multi( nullTranslation, arraySeparator, translators );
        }

        private final String nullTranslation;
        private final String arraySeparator;
        private final Translator[] translators;

        public Multi(
                String nullTranslation, String arraySeparator, Translator... translators ) {
            this.nullTranslation = nullTranslation;
            this.arraySeparator = arraySeparator;
            this.translators = translators;
        }

        @Override
        public String translate( Object input ) {
            if( input == null ) {
                return this.nullTranslation;
            }
            if( input instanceof CharSequence ) {
                return ((CharSequence)input).toString();
            }
            if( input.getClass().isArray() ) {
                StringBuilder sb = new StringBuilder();
                int len = Array.getLength( input );

                for( int i = 0; i < len; i++ ) {
                    if( i > 0 ) {
                        sb.append( this.arraySeparator );
                    }
                    Object o = Array.get( input, i );
                    Object translated = o;
                    for( Translator translator : this.translators ) {
                        //TODO: I could have a Map between Classes and translators
                        translated = translator.translate( translated );
                    }
                    sb.append( translated );
                }
                return sb.toString();
            }
            Object translated = input;
            for( Translator translator : this.translators ) {
                if( translator == null ){
                }
                translated = translator.translate( translated );
            }
            if( !(translated.equals( input )) && !(translated instanceof String) ) {
                return translate( translated );
            }
            //if there was no translation, just append
            return translated.toString();
        }
    }
}

