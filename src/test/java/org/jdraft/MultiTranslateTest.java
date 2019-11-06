package org.jdraft;

import junit.framework.TestCase;

import java.util.ArrayList;
import java.util.List;

import org.jdraft.text.Translator;

public class MultiTranslateTest extends TestCase {

    public void testTransColl(){
        Translator.Multi rt = new
                Translator.Multi("null", ", ", Translator.CollectionToArray );
        List<String> str  = new ArrayList<String>();
        str.add("A");
        str.add("B");
        assertEquals( "A, B", rt.translate( str ) );

        List<_class> cs  = new ArrayList<_class>();
        cs.add( _class.of("xxxx.yyyy.ZZZZ"));

        rt = new Translator.Multi(
                "null", ", ", Translator.CollectionToArray, new Translator() {

            public Object translate(Object source) {
                if( source instanceof _class ){
                    return ((_class)source).getFullName();
                }
                return source;
            }
        });
        assertEquals( "xxxx.yyyy.ZZZZ", rt.translate( cs ));

    }
}
