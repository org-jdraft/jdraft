package test.othertools;

import junit.framework.TestCase;
import org.jdraft._class;
import org.jdraft._codeUnits;
import org.jdraft._method;
import org.jdraft._type;
import org.jdraft.pattern.*;

/**
 * This is a detailed sketch going into how we might develop a
 * the error prone matcher...
 *
 * NOTE: I was modifying the $pattern implementations WHILE I was writing this
 * test just to cover the bases... (so it's intentianally scatter brained)
 *
 * <A HREF=https://github.com/google/error-prone/blob/master/core/src/main/java/com/google/errorprone/bugpatterns/inject/dagger/AndroidInjectionBeforeSuper.java>the ErrorProne Java code</A>
 * <A HREF="https://errorprone.info/bugpattern/AndroidInjectionBeforeSuper">description of the Error </A>
 */
public class GoogleErrorProneAndroidInjectBeforeSuper_PartsTest extends TestCase {

    /**
     * We can use/reuse this $pattern elsewhere, it represents top level Android Classes
     * all of which must extend some specific base class
     */
    public static final $class $ANDROID_CLASS = $class.or(
            $class.of().$extends("android.app.Activity"),
            $class.of().$extends("android.app.Fragment"),
            $class.of().$extends("android.support.v4.app.Fragment"),
            $class.of().$extends("android.app.Service") );

    /** test that $ANDROID_CLASS does its job & matches classes that extend the appropriate base classes */
    public void test$ANDROID_CLASS(){
        assertTrue( $ANDROID_CLASS.matches(_class.of("C").addExtend("android.app.Activity") ) );
        assertTrue( $ANDROID_CLASS.matches(_class.of("C").addExtend("android.app.Fragment") ) );
        assertTrue( $ANDROID_CLASS.matches(_class.of("C").addExtend("android.support.v4.app.Fragment") ) );
        assertTrue( $ANDROID_CLASS.matches(_class.of("C").addExtend("android.app.Service") ) );

        /** this test will verify classes can extend the class SimpleName */
        _codeUnits _cc = _codeUnits.of(
                _class.of("A").addExtend("Activity").addImports("android.app.Activity"),
                _class.of("F").addExtend("Fragment").addImports("android.app.Fragment"),
                _class.of("F2").addExtend("Fragment").addImports("android.support.v4.app.Fragment"),
                _class.of("S").addExtend("Service").addImports("android.app.Service")
        );
        assertEquals(4, $ANDROID_CLASS.countIn(_cc));
    }

    /** $pattern match for the Suppress Warnings annotation */
    static $annoRef $SUPPRESS_WARNING_ANNO = $annoRef.of("@SuppressWarnings(\"AndroidInjectionBeforeSuper\")");

    //@SuppressWarnings({"AndroidInjectionBeforeSuper", "SomeOtherWarning"})
    public void testSuppressAnno(){
        assertTrue($SUPPRESS_WARNING_ANNO.matches("@SuppressWarnings(\"AndroidInjectionBeforeSuper\")"));
        assertTrue($SUPPRESS_WARNING_ANNO.matches("@SuppressWarnings({\"SomeUnrelatedWarningToSuppress\", \"AndroidInjectionBeforeSuper\"})"));
        assertTrue($SUPPRESS_WARNING_ANNO.matches("@SuppressWarnings({\"AndroidInjectionBeforeSuper\", \"SomeUnrelatedWarningToSuppress\"})"));

        assertTrue( $method.of( $SUPPRESS_WARNING_ANNO ).matches("@SuppressWarnings(\"AndroidInjectionBeforeSuper\") void m(){}") );
        assertFalse( $method.of( ).$not($SUPPRESS_WARNING_ANNO ).matches("@SuppressWarnings(\"AndroidInjectionBeforeSuper\") void m(){}") );
    }

    /* Example class that represents a failure */
    static _class _FAIL_InjectBeforeSuperActivity = _class.of(
            "package who.cares;",
            "import android.app.Activity;",
            "import android.os.Bundle;",
            "import dagger.android.AndroidInjection;",
            "public class WrongOrder extends Activity {",
            "    @Override",
            "    public void onCreate(Bundle savedInstanceState) {",
            "        super.onCreate(savedInstanceState);",
            "        // BUG: Diagnostic contains: AndroidInjectionBeforeSuper",
            "        AndroidInjection.inject(this);",
            "    }",
            "}");

    static _class _FAIL_InjectBeforeSuperOnAttach = _class.of(
            "package who.cares;",
            "import android.app.Activity;",
            "import android.os.Bundle;",
            "import dagger.android.AndroidInjection;",
            "public class WrongOrder extends Activity {",
            "    @Override",
            "    public void onAttach(Activity activity) {",
            "        super.onAttach(activity);",
            "        // BUG: Diagnostic contains: AndroidInjectionBeforeSuper",
            "        AndroidInjection.inject(this);",
            "    }",
            "}");

    static _class _FAIL_InjBeforeSuperInBetween = _class.of(
            "package who.cares;",
            "import android.app.Activity;",
            "import android.os.Bundle;",
            "import dagger.android.AndroidInjection;",
            "public class StatementsInBetween extends Activity {\n",
                    "    @Override\n",
                    "    public void onCreate(Bundle savedInstanceState) {\n",
                    "      super.onCreate(savedInstanceState);\n",
                    "      System.out.println(\"hello, world\");\n",
                    "      AndroidInjection.inject(this);\n",
                    "    }\n",
                    "  }");

    static _class _FAIL_InFragment = _class.of(
            "package who.cares;",
            "import android.app.Fragment;",
            "import android.os.Bundle;",
            "import dagger.android.AndroidInjection;",
            "public class CorrectOrderFragment extends Fragment {\n",
                    "    @Override\n",
                    "    public void onAttach(Activity activity) {\n",
                    "      super.onAttach(activity);\n",
                    "      AndroidInjection.inject(this);\n",
                    "    }\n",
                    "}"
    );

    /** Heres all the examples we EXPECT to fail */
    static _codeUnits FAILURES = _codeUnits.of(
            _FAIL_InjectBeforeSuperActivity, _FAIL_InFragment, _FAIL_InjBeforeSuperInBetween, _FAIL_InjectBeforeSuperOnAttach);

    /** $pattern match for the onCreate Method for Android */
    static $method $ON_CREATE_METHOD = $method.of(
            "onCreate", $.PUBLIC, $.VOID, $parameter.of( $typeRef.of("android.os.Bundle")) )
            .$not($SUPPRESS_WARNING_ANNO);

    /** $pattern match for the onAttach() Method for Android */
    static $method $ON_ATTACH_METHOD = $method.of(
            "onAttach", $.PUBLIC, $.VOID, $parameter.of( $typeRef.of("android.app.Activity") ) )
            .$not($SUPPRESS_WARNING_ANNO);

    /** pattern match for EITHER $ON_CREATE_METHOD or $ON_ATTACH_METHOD  & a parent is of $ANDROID_CLASS */
    static $method $METHODS = $method.or($ON_CREATE_METHOD, $ON_ATTACH_METHOD )
                    .$isParentMember($ANDROID_CLASS);

    static $ex $injectEx = $ex.of("AndroidInjection.inject(this)");

    static $ex $onCreateEx = $.ex("super.onCreate($any$)");

    static $ex $onAttachEx = $.ex("super.onAttach($any$)");


    public void testFindExs(){
        assertEquals(4, $injectEx.countIn(FAILURES) );
        assertEquals(2, $onAttachEx.countIn(FAILURES) );
        assertEquals(2, $onCreateEx.countIn(FAILURES) );

        //now check for instances where $injectEx comes AFTER (in the body)
        $ex $injectAfterSuper = $ex.of("AndroidInjection.inject(this)").$isAfter($onCreateEx, $onAttachEx);

        assertEquals(4, $injectAfterSuper.countIn(FAILURES));

        //$ex onAttachBeforeInject = $onAttachEx.$before( $injectEx );
        //$ex onCreateBeforeInject = $onCreateEx.$before( $injectEx );
    }

    public void testFullThingInOneSwoop(){
        //$ex $injectAfterSuper =
        $ex $injectAfterSuper = $ex.of("AndroidInjection.inject(this)")
                .$isAfter( $.ex("super.onCreate($any$)"), $.ex("super.onAttach($any$)"));

        $method $onCreateAttach = $method.or(
                $method.of("onCreate",$.PUBLIC,$.VOID,$parameter.of($typeRef.of("android.os.Bundle"))),
                $method.of("onAttach",$.PUBLIC,$.VOID,$parameter.of($typeRef.of("android.app.Activity")))
            .$and($ex.of("AndroidInjection.inject(this)")
                 .$isAfter($.ex("super.onCreate($any$)"),
                           $.ex("super.onAttach($any$)"))) )
             .$not($SUPPRESS_WARNING_ANNO)
             .$isParentMember($ANDROID_CLASS);
        assertEquals(4, $onCreateAttach.countIn(FAILURES));

        //simplified(less strict/detailed version)
        $method $AndroidInjectAfterSuper = $method.of($name.or("onCreate","onAttach"),
                $ex.of("AndroidInjection.inject(this)")
                        .$isAfter($.ex("super.onCreate($any$)"),
                                $.ex("super.onAttach($any$)")))
                .$not($SUPPRESS_WARNING_ANNO);

        assertEquals(4, $AndroidInjectAfterSuper.countIn(FAILURES) );

        // lets copy of the .java source, adding the @Suppress... annotations to each method
        // then retesting and verifying there are no matches
        _codeUnits SUPPRESS_ANNO = FAILURES.copy();
        SUPPRESS_ANNO.forEach( _type.class, _t -> _t.forMembers(_method.class, _m -> ((_method)_m).addAnnoRefs($SUPPRESS_WARNING_ANNO.draft())) );
        //assertEquals(4, $SUPPRESS_WARNING_ANNO.count(SUPPRESS_ANNO));
        assertEquals(4, $method.of($SUPPRESS_WARNING_ANNO).countIn(SUPPRESS_ANNO)); //all (4) methods have the suppress anno
        System.out.println( SUPPRESS_ANNO.list() );
        assertEquals(0, $method.of().$not($SUPPRESS_WARNING_ANNO).countIn(SUPPRESS_ANNO) );

        $method.of().$not($SUPPRESS_WARNING_ANNO).printIn(SUPPRESS_ANNO);

        //verify there are 0 matches if the methods have the @SuppressWarnings(...) annotation
        assertEquals( 0, $onCreateAttach.countIn(SUPPRESS_ANNO));

    }


    public void testOnCreateOnAttachMethod(){
        //create a combined matcher that will match EITHER the onCreate method -or- the onAttach method
        //$method $targetMethods = $method.or($ON_CREATE_METHOD, $ON_ATTACH_METHOD);
        //verify I can use this matcher to match either method type
        assertTrue( $METHODS.isIn(_FAIL_InFragment) );
        assertTrue( $METHODS.isIn(_FAIL_InjBeforeSuperInBetween) );
        assertTrue( $METHODS.isIn(_FAIL_InjectBeforeSuperActivity) );
        assertTrue( $METHODS.isIn(_FAIL_InjectBeforeSuperOnAttach) );

        //UPDATE the $pattern add a constraint (the $methods parent must ALSO be an $ANDROID_CLASSES)
        //$METHODS.$isParentMember( $ANDROID_CLASS );
        //verify I can use this matcher will still match these methods
        //assertNotNull( $METHODS.firstIn(_FAIL_InFragment) );
        //assertNotNull( $METHODS.firstIn(_FAIL_InjBeforeSuperInBetween) );
        //assertNotNull( $METHODS.firstIn(_FAIL_InjectBeforeSuperActivity) );

        //any activity that BOTH extends Activity and has an onCreate Method
        //$act.$methods($onCreateMethod);

        //UPDATE the $pattern to ensure NO MATCH for methods that have the $SUPPRESS_WARNINGS annotation
        //$targetMethods.$not( $SUPPRESS_WARNING_ANNO );

        //assertNotNull( $targetMethods.firstIn(_FAIL_InFragment) );
        //assertNotNull( $targetMethods.firstIn(_FAIL_InjBeforeSuperInBetween) );
        //assertNotNull( $targetMethods.firstIn(_FAIL_InjectBeforeSuperActivity) );
        //assertNotNull( $targetMethods.firstIn(_FAIL_InjectBeforeSuperOnAttach) );

        //OK, lets MODIFY COPIES of each _class to ADD the @Suppress annotation to verify
        // that it will NOT match if it did
        _class _cp = $method.of().forEachIn( _FAIL_InFragment.copy(), m -> m.addAnnoRefs( $SUPPRESS_WARNING_ANNO.draft()));
        assertFalse( $METHODS.isIn(_cp) ); //wont match because @SuppressWarnings anno
        _cp = $method.of().forEachIn( _FAIL_InjBeforeSuperInBetween.copy(), m -> m.addAnnoRefs( $SUPPRESS_WARNING_ANNO.draft()));
        assertFalse( $METHODS.isIn(_cp) ); //wont match because @SuppressWarnings anno
        _cp = $method.of().forEachIn( _FAIL_InjectBeforeSuperOnAttach.copy(), m -> m.addAnnoRefs( $SUPPRESS_WARNING_ANNO.draft()));
        assertFalse( $METHODS.isIn(_cp) ); //wont match because @SuppressWarnings anno
        _cp = $method.of().forEachIn( _FAIL_InjectBeforeSuperActivity.copy(), m -> m.addAnnoRefs( $SUPPRESS_WARNING_ANNO.draft()));
        assertFalse( $METHODS.isIn(_cp) ); //wont match because @SuppressWarnings anno
    }

    //https://github.com/google/error-prone/blob/master/core/src/main/java/com/google/errorprone/bugpatterns/inject/dagger/AndroidInjectionBeforeSuper.java
    //https://errorprone.info/bugpattern/AndroidInjectionBeforeSuper
    //The first Query I would do would be to query only the classes that are Activity and Fragment
    //extensions...
    /*
    public void testMultiTierQuery(){
        //Since I (the developer) am writing the code, I can create a multi-tiered model (i.e. in batch)
        //this is because I might want to run SPECIFIC ANDROID tests ONLY against Android code
        //...so while I might have this very large codebase, I might have 30 or so checks that are going to
        //run SPECIFICALLY
        //for classes that extend Activity
        $class $act = $class.of().$extends("android.app.Activity");
        $class $fra = $class.of().$extends("android.app.Fragment");
        $class $v4fra = $class.of().$extends("android.support.v4.app.Fragment");
        _code._cache _cc =
                _code._cache.of(_FAIL_InFragment, _FAIL_InjBeforeSuperInBetween, _FAIL_InjectBeforeSuperActivity);
        //verify that $extends checks fully qualified and imports (for not) and package

        //OR
        //List<_class> _activitiesAndFragments = $node.of($act,$fra,$v4fra).listIn(_cc);

        //assertEquals( 1, $act.count(_c));
        //now I have a subset of classes to do queries on
        //
    }
     */

    /*
    //https://github.com/google/error-prone/blob/master/core/src/main/java/com/google/errorprone/bugpatterns/inject/dagger/AndroidInjectionBeforeSuper.java
    //https://errorprone.info/bugpattern/AndroidInjectionBeforeSuper
    public void testAndroidInjectBeforeSuper(){

        //for void onCreateMethods methods
        $method $onCreateMethod = $method.of(
                $.PUBLIC, $typeRef.VOID, $name.of("onCreate"), $parameter.of( $typeRef.of("android.os.Bundle")) );

        //any activity that BOTH extends Activity and has an onCreate Method
        //$act.$methods($onCreateMethod);
        //$method $onAttachMethod = $method.of($typeRef.VOID, $name.of("onAttach"));
        //$class $serv = $class.of().$extends("android.app.Service");

        //here we have the individual components that make up the test


        //$anno $suppress = $anno.of("@SuppressWarnings(\"AndroidInjectionBeforeSuper\")");


        _class _c = _class.of(
                "import android.app.Activity;",
                "public class WrongOrder extends Activity {",
                "    @Override",
                "    @SuppressWarnings(\"AndroidInjectionBeforeSuper\")",
                "    public void onCreate(Bundle savedInstanceState) {",
                "        super.onCreate(savedInstanceState);",
                "        super.onAttach(savedInstanceState);",
                "        // BUG: Diagnostic contains: AndroidInjectionBeforeSuper",
                "        AndroidInjection.inject(this);",
                "    }",
                "    @SuppressWarnings(\"AndroidInjectionBeforeSuper\")",
                "    public void onAttach(Bundle savedInstanceState) {",
                "        super.onCreate(savedInstanceState);",
                "        super.onAttach(savedInstanceState);",
                "        // BUG: Diagnostic contains: AndroidInjectionBeforeSuper",
                "        AndroidInjection.inject(this);",
                "    }",
                "}");
        //we can spot check each of the individual $patterns in isolation

        //assertEquals(1, $onCreateMethod.count(_c));
       // assertEquals(1, $onAttachMethod.count(_c));
        //assertEquals(2, $suppress.count(_c));
        //assertEquals(2, $onCreateEx.count(_c));
        //assertEquals(2, $onAttachEx.count(_c));
        //assertEquals(2, $injectEx.count(_c));

        //ok, heres where we compose them together (do the first composition)
        //
        //assertEquals(1, $.ex("super.onCreate($any$)").$isParentMember($method.of($name.of("onCreate"), $suppress)).count(_c));
            /*
            $onCreate = ($ex)$onCreate.$isNotParentMember($method.of($suppress));
            $onCreate = ($ex)$onCreate.$isNotParentMember($method.of($suppress));

            assertEquals(0, $onCreate.count(_c));
            $inject.$before(3, $pattern...)
            $inject.$after(3, $pattern...)

            $inject.$notAfter(3, $onCreate, $onAttach);
            $inject.$notBefore(3, $onCreate, $onAttach);

    }
             */
}
