package test.quickstart.model;

import com.github.javaparser.StaticJavaParser;
import com.github.javaparser.ast.body.MethodDeclaration;
import junit.framework.TestCase;
import org.jdraft.*;

/**
 * jdraft builds Object models from java source code to make writing programs to analyze and modify source code easy.
 * to use it, you need to:
 * <OL>
 *     <LI>build a model (_class, _method, _field, _constructor, ...)</LI>
 *     <LI>use the model to do something useful</LI>
 * </OL>
 */
public class _0_Model_Test extends TestCase {

    public void testBuildJdraftModelFromStringAndUse(){
        // jdraft builds a model representing .java source code
        _method _m = _method.of("public int m(){ return 100; }");

        // jdraft makes reading code properties easy
        assertEquals( "m", _m.getName() );
        assertTrue( _m.isType(int.class) );
        assertFalse( _m.hasThrows() );

        // jdraft models can be nested inside other jdraft models
        _modifiers _ms = _m.getModifiers();
        assertTrue( _ms.isPublic() );

        _typeRef _t = _m.getType();
        assertTrue(_t.is(int.class));

        _parameters _ps = _m.getParameters();
        assertTrue( _ps.isEmpty() );

        // jdraft models make modifying code easy
        _m.setName("getValue")
                .setProtected()
                .anno(Deprecated.class)
                .setFinal();

        // internally jdraft models wrap a JavaParser Ast instance
        MethodDeclaration mAst = _m.ast();

        // if we modify the AST; the _jdraft model will reflect the change
        mAst.setStatic(true);
        assertTrue( mAst.isStatic());
        assertTrue( _m.isStatic());

        // writing the source of a jdraft model is simple
        System.out.println( _m );
    }

    // storing code as embedded Strings is a real nightmare to maintain
    // therefore jdraft provides alternatives to building models that are easier to maintain(*)
    // (*) in these scenarios, the .java source code MUST be on the classpath
    public void testOtherWaysOfBuildingModels(){
        // build a _method(_m) representing the SOURCE of the method m in the anonymous Object (*)
        _method _mAnon = _method.of(new Object(){
                public int m(){ return 100; }
            });

        // with a JavaParser MethodDeclaration...
        MethodDeclaration md =
                StaticJavaParser.parseMethodDeclaration("public int m(){ return 100; }");
        // ...we can build a jdraft _method (_mast)
        _method _mAst = _method.of( md );

        // load the source of a runtime Classes method (*)
        _method _mClass = _method.of(this.getClass(), "m");

        // verify these _methods are equal
        assertEquals( _mAnon, _mAst);
        assertEquals( _mAnon, _mClass);
        assertEquals( _mAst, _mClass);
    }

    public int m(){ return 100; }
}
