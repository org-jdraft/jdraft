package org.jdraft.pattern;

import junit.framework.TestCase;
import org.jdraft._class;
import org.jdraft._code;
import org.jdraft.io._cache;

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
        _c.extend("android.app.Activity");
        assertEquals(_c, $ANDROID_CLASS.select(_c).selected);
        _c.extend("android.app.Fragment");
        assertEquals(_c, $ANDROID_CLASS.select(_c).selected);
        _c.extend("android.support.v4.app.Fragment");
        assertEquals(_c, $ANDROID_CLASS.select(_c).selected);
        _c.extend("android.app.Service");
        assertEquals(_c, $ANDROID_CLASS.select(_c).selected);

        _cache _cc = _cache.of(
                _class.of("A").extend("Activity").imports("android.app.Activity"),
                _class.of("F").extend("Fragment").imports("android.app.Fragment"),
                _class.of("F2").extend("Fragment").imports("android.support.v4.app.Fragment"),
                _class.of("S").extend("Service").imports("android.app.Service")
        );
        //$androidClasses.printIn(_cc);
        assertEquals(4, $ANDROID_CLASS.count(_cc));
        assertEquals(4, $C.count(_cc));

        _cc = _cache.of(
                _class.of("A").extend("android.app.Activity"),
                _class.of("F").extend("android.app.Fragment"),
                _class.of("F2").extend("android.support.v4.app.Fragment"),
                _class.of("S").extend("android.app.Service")
        );

        assertEquals(4, $ANDROID_CLASS.count(_cc));
        assertEquals(4, $C.count(_cc));
    }

    //todo fix isExtends
    public void testIsImplements(){
        _class _c = _class.of("C").imports("a.b.c.Map;").implement("Map");
        assertFalse( _c.hasImport(Map.class));
        assertFalse(_c.isImplements(Map.class));

    }
}
