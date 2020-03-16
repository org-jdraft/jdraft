package org.jdraft.pattern;

import junit.framework.TestCase;
import org.jdraft._class;
import org.jdraft.io._sources;

import java.util.Map;

public class SorTest extends TestCase {

    /**
     * NOTE: this is probably the preferred way of
     * doing below (just add a single match constraint that check whether
     * the class extends one of the known interfaces
     */
    $class $C = $class.of(c->
            c.isExtends("android.app.Activity")||
            c.isExtends("android.app.Fragment")||
            c.isExtends("android.support.v4.app.Fragment")||
            c.isExtends("android.app.Service"));
    /**
     * We can use/reuse this $pattern elsewhere,
     * this "matches" any class that extends
     */
    public static final $or<$class> $ANDROID_CLASS = $or.of(
            $class.of().$extends("android.app.Activity"),
            $class.of().$extends("android.app.Fragment"),
            $class.of().$extends("android.support.v4.app.Fragment"),
            $class.of().$extends("android.app.Service") );

    public void test$Or(){
        _class _c = _class.of("C");
        _c.addExtend("android.app.Activity");
        assertEquals(_c, $ANDROID_CLASS.select(_c).selected);
        _c.addExtend("android.app.Fragment");
        assertEquals(_c, $ANDROID_CLASS.select(_c).selected);
        _c.addExtend("android.support.v4.app.Fragment");
        assertEquals(_c, $ANDROID_CLASS.select(_c).selected);
        _c.addExtend("android.app.Service");
        assertEquals(_c, $ANDROID_CLASS.select(_c).selected);

        _sources _cc = _sources.of(
                _class.of("A").addExtend("Activity").addImports("android.app.Activity"),
                _class.of("F").addExtend("Fragment").addImports("android.app.Fragment"),
                _class.of("F2").addExtend("Fragment").addImports("android.support.v4.app.Fragment"),
                _class.of("S").addExtend("Service").addImports("android.app.Service")
        );
        //$androidClasses.printIn(_cc);
        assertEquals(4, $ANDROID_CLASS.countIn(_cc));
        assertEquals(4, $C.countIn(_cc));

        _cc = _sources.of(
                _class.of("A").addExtend("android.app.Activity"),
                _class.of("F").addExtend("android.app.Fragment"),
                _class.of("F2").addExtend("android.support.v4.app.Fragment"),
                _class.of("S").addExtend("android.app.Service")
        );

        assertEquals(4, $ANDROID_CLASS.countIn(_cc));
        assertEquals(4, $C.countIn(_cc));
    }

    //todo fix isExtends
    public void testIsImplements(){
        _class _c = _class.of("C").addImports("a.b.c.Map;").addImplement("Map");
        assertFalse( _c.hasImport(Map.class));
        assertFalse(_c.isImplements(Map.class));

    }
}
