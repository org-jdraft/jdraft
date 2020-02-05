package test.othertools;

import com.github.javaparser.Position;
import com.github.javaparser.Range;
import com.github.javaparser.ast.Node;
import com.github.javaparser.ast.body.BodyDeclaration;
import com.github.javaparser.ast.body.CallableDeclaration;
import com.github.javaparser.ast.stmt.ReturnStmt;
import junit.framework.TestCase;
import org.jdraft.Ast;
import org.jdraft._class;
import org.jdraft._returnStmt;
import org.jdraft.io._source;
import org.jdraft.pattern.*;

/**
 * this example came from Google's Error-Prone project:
 * https://github.com/google/error-prone/wiki/Writing-a-check
 *
 */
public class GoogleErrorProneTest extends TestCase {

    /**
     * Look through code to find return null;
     */
    public void testFindReturnNull(){

        //this models all return statements that return the null literal
        $stmt<ReturnStmt, _returnStmt> $returnNull =
                $.returnStmt("return null;");

        class retNull{
            String v(){
                return null;
            }
        }
        //verify we can find such statements in the above
        assertEquals(1, $returnNull.count(retNull.class));
    }


    //we just need this for test purposes
    @interface Nullable{}

    /**
     * <A HREF="https://github.com/google/error-prone/wiki/Writing-a-check#create-a-bugchecker">Error Prone Bug Checker</A>
     * "The use of return null; is banned, except in methods annotated with @Nullable."
     */
    public void testFindReturnsNullsIfNotAnnotatedWithNullable(){

        //match any callable declaration that is annotated with Nullable
        $node $memberAnnotatedWithNullable = $node.of(CallableDeclaration.class)
                .$and(cd-> ((CallableDeclaration)cd).getAnnotationByName("Nullable").isPresent());

        //match any Return null where that is NOT within a Member of
        $stmt<ReturnStmt, _returnStmt> $returnNull = $.returnStmt("return null;")
                .$and(r -> !$.hasAncestor(r, $memberAnnotatedWithNullable));


        class FFF{
            @Nullable Object m(){ //this shouldnt match
                return null;
            }

            String name(){ //this should match
                /** comment */
                return null;
            }
        }
        assertEquals(1, $returnNull.count(FFF.class));
        //System.out.println( "FOUND " + $returnNull.firstIn(FFF.class) );

        assertTrue( $returnNull.firstIn(FFF.class)
                .stream(Node.TreeTraversal.PARENTS).anyMatch( p-> $method.of().$name("name").match(p) ) );

    }

    /**
     * We can use/reuse this $pattern elsewhere,
     */
    public static final $class $ANDROID_CLASS = $class.or(
            $class.of().$extends("android.app.Activity"),
            $class.of().$extends("android.app.Fragment"),
            $class.of().$extends("android.support.v4.app.Fragment"),
            $class.of().$extends("android.app.Service") );

    public void test$ANDROID_CLASS(){
        _class _c = _class.of("C");
        _c.extend("android.app.Activity");
        assertEquals(_c, $ANDROID_CLASS.select(_c).selected);
        _c.extend("android.app.Fragment");
        assertEquals(_c, $ANDROID_CLASS.select(_c).selected);
        _c.extend("android.support.v4.app.Fragment");
        assertEquals(_c, $ANDROID_CLASS.select(_c).selected);
        _c.extend("android.app.Service");
        assertEquals(_c, $ANDROID_CLASS.select(_c).selected);

        _source _cc = _source.of(
                _class.of("A").extend("Activity").imports("android.app.Activity"),
                _class.of("F").extend("Fragment").imports("android.app.Fragment"),
                _class.of("F2").extend("Fragment").imports("android.support.v4.app.Fragment"),
                _class.of("S").extend("Service").imports("android.app.Service")
        );
        //$androidClasses.printIn(_cc);
        assertEquals(4, $ANDROID_CLASS.count(_cc));
    }


    static _class _FAIL_InjectBeforeSuperActivity = _class.of(
            "package who.cares;",
            "import android.app.Activity;",
            "import android.os.Bundle;",
            "import dagger.android.AndroidInjection;",
            "public class WrongOrder extends Activity {",
            "    @Override",
            "    @SuppressWarnings(\"AndroidInjectionBeforeSuper\")",
            "    public void onCreate(Bundle savedInstanceState) {",
            "        super.onCreate(savedInstanceState);",
            "        super.onAttach(savedInstanceState);",
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
    //https://github.com/google/error-prone/blob/master/core/src/main/java/com/google/errorprone/bugpatterns/inject/dagger/AndroidInjectionBeforeSuper.java
    //https://errorprone.info/bugpattern/AndroidInjectionBeforeSuper
    //The first Query I would do would be to query only the classes that are Activity and Fragment
    //extensions...
    public void testMultiTierQuery(){
        //Since I (the developer) am writing the code, I can create a multi-tiered model (i.e. in batch)
        //this is because I might want to run SPECIFIC ANDROID tests ONLY against Android code
        //...so while I might have this very large codebase, I might have 30 or so checks that are going to
        //run SPECIFICALLY
        //for classes that extend Activity
        $class $act = $class.of().$extends("android.app.Activity");
        $class $fra = $class.of().$extends("android.app.Fragment");
        $class $v4fra = $class.of().$extends("android.support.v4.app.Fragment");
        _source _cc =
                _source.of(_FAIL_InFragment, _FAIL_InjBeforeSuperInBetween, _FAIL_InjectBeforeSuperActivity);
        //verify that $extends checks fully qualified and imports (for not) and package

        //OR
        //List<_class> _activitiesAndFragments = $node.of($act,$fra,$v4fra).listIn(_cc);

        //assertEquals( 1, $act.count(_c));
        //now I have a subset of classes to do queries on
        //
    }

    //https://github.com/google/error-prone/blob/master/core/src/main/java/com/google/errorprone/bugpatterns/inject/dagger/AndroidInjectionBeforeSuper.java
    //https://errorprone.info/bugpattern/AndroidInjectionBeforeSuper
    public void testAndroidInjectBeforeSuper(){

        //for void onCreateMethods methods
        $method $onCreateMethod = $method.of(
                $.PUBLIC, $typeRef.VOID, $name.of("onCreate"), $parameter.of($typeRef.of("android.os.Bundle")) );

        //any activity that BOTH extends Activity and has an onCreate Method
        //$act.$methods($onCreateMethod);


        $method $onAttachMethod = $method.of($typeRef.VOID, $name.of("onAttach"));


        $class $serv = $class.of().$extends("android.app.Service");



        //here we have the individual components that make up the test
        $ex $onCreateEx = $.ex("super.onCreate($any$)");
        $ex $onAttachEx = $.ex("super.onAttach($any$)");
        $ex $injectEx = $ex.of("AndroidInjection.inject(this)");

        $anno $suppress = $anno.of("@SuppressWarnings(\"AndroidInjectionBeforeSuper\")");


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

            assertEquals(1, $onCreateMethod.count(_c));
            assertEquals(1, $onAttachMethod.count(_c));
            assertEquals(2, $suppress.count(_c));
            assertEquals(2, $onCreateEx.count(_c));
            assertEquals(2, $onAttachEx.count(_c));
            assertEquals(2, $injectEx.count(_c));

            //ok, heres where we compose them together (do the first composition)
            //
            assertEquals(1, $.ex("super.onCreate($any$)").$isParentMember($method.of($name.of("onCreate"), $suppress)).count(_c));
            /*
            $onCreate = ($ex)$onCreate.$isNotParentMember($method.of($suppress));
            $onCreate = ($ex)$onCreate.$isNotParentMember($method.of($suppress));

            assertEquals(0, $onCreate.count(_c));
            */
            /*
                $inject.$before(3, $pattern...)
                $inject.$after(3, $pattern...)

                $inject.$notAfter(3, $onCreate, $onAttach);
                $inject.$notBefore(3, $onCreate, $onAttach);
                */
    }

    public void testMember(){
        //a $member is just a BodyDeclaration
        //
        Range searchRange = new Range(new Position(0, 0), new Position(100, 100));

        //$isInRange( Range )
        //$isInRange(startLine, startColumn, endLine, endColumn)
        //$isInRange(startLine, endLine)


        BodyDeclaration bd = Ast.method("int i(){return 3;}");
        assertTrue( searchRange.strictlyContains( bd.getRange().get() ) );
        assertTrue(
                $node.of(BodyDeclaration.class).match(Ast.method("int i(){return 1;}")) );
    }

}
