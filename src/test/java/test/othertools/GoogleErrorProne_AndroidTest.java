package test.othertools;

import junit.framework.TestCase;
import org.jdraft._class;
import org.jdraft._code;
import org.jdraft.pattern.*;

/** This emulates the "heart" of a Google Error Prone matcher using jdraft $patterns */
public class GoogleErrorProne_AndroidTest extends TestCase {

    /**
     * <A HREF=https://github.com/google/error-prone/blob/master/core/src/main/java/com/google/errorprone/bugpatterns/inject/dagger/AndroidInjectionBeforeSuper.java>the ErrorProne Java code</A>
     * <A HREF="https://errorprone.info/bugpattern/AndroidInjectionBeforeSuper">description of the Error </A>
     */
    public void testInjectAfterSuper() {
        /** $pattern match for the Suppress Warnings annotation */
        $anno $SUPPRESS_WARNING_ANNO = $anno.of("@SuppressWarnings(\"AndroidInjectionBeforeSuper\")");

        // here is the method pattern to search for
        // a method named "onCreate" or "onAttach"
        // containing the expression "AndroidInjection.inject(this)"
        // that occurs AFTER super.onCreate() or super.onAttach()
        // WITHOUT the SUPPRESS_WARNINGS_ANNO
        $method $AndroidInjectAfterSuper = $method.of($name.or("onCreate", "onAttach"),
                $ex.of("AndroidInjection.inject(this)")
                        .$isAfter($.ex("super.onCreate($any$)"),
                                $.ex("super.onAttach($any$)")))
                .$not($SUPPRESS_WARNING_ANNO);

        //verify that there are (4) failures found
        assertEquals(4, $AndroidInjectAfterSuper.count(FAILURES));

        //verify that, if I add the SUPPRESS WARNINGS ANNOTATION to Failure matches they are no longer matches
        _code._cache _suppressed = FAILURES.copy();
        //add the annotation that suppresses the warning
        $method.of().forEachIn( _suppressed, m -> m.anno( $SUPPRESS_WARNING_ANNO.draft()));

        //verify we have 0 matches
        assertEquals(0, $AndroidInjectAfterSuper.count(_suppressed));
    }

    /**************BELOW ARE EXAMPLES TO TEST ***************************/

    /* Example class that represents a failure */
    static _class _FAIL_InjectAfterSuperActivity = _class.of(
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

    static _class _FAIL_InjectAfterSuperOnAttach = _class.of(
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

    static _class _FAIL_InjAfterSuperInBetween = _class.of(
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

    /** Heres all the examples we EXPECT to fail pre-parsed and cached*/
    static _code._cache FAILURES = _code._cache.of(
            _FAIL_InjectAfterSuperActivity, _FAIL_InFragment, _FAIL_InjAfterSuperInBetween, _FAIL_InjectAfterSuperOnAttach);
}
