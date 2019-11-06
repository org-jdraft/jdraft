package org.jdraft.text;

import junit.framework.TestCase;

import org.jdraft._class;
import org.jdraft.text.Translator;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class CollectionToArrayTranslateTest
        extends TestCase {

    public void testArr(){
        assertTrue(
                Arrays.deepEquals(
                        new Object[0],
                        (Object[]) Translator.CollectionToArray.translate( Collections.EMPTY_LIST ) )
        );
    }

    public void testColl(){
        List<_class> _cs = new ArrayList<_class>();
        _cs.add( _class.of("aaaa.bbbb.CCCC"));
        Object[] arr = (Object[]) Translator.CollectionToArray.translate( _cs );
        assertEquals(1, arr.length);

    }
}
