package org.jdraft;

import com.github.javaparser.ast.CompilationUnit;
import junit.framework.TestCase;
import org.jdraft.pattern.$;
import org.jdraft.runtime.*;
import org.jdraft.diff._diff;
import org.jdraft.macro.*;
import org.jdraft.pattern.$ex;
import org.jdraft.pattern.$typeRef;

/**
 * Touch all of the major APIs to make sure they work
 * 
 * Verify that the jdraft and Javaparser APIs are working 
 * (integrating together seemlessly)
 * 
 * @author Eric
 */
public class APIIntegrationTest extends TestCase {

    public void testBuildtheModel(){
        //githubresolver
    }

    //can I do something on (the tool side of things) to make it easier to use?
    public void testApi(){
        //I don't need to know the exact name/type of expression, just build it and return it to me
        _expr _e = _expr.of("3 + 4"); //its really a _binaryExpression in case youre wondering

        //you can create an exact type if you wish
        _binaryExpr _be = _binaryExpr.of("3 + 4");

        assertEquals( _e, _be);

        //ditto with statements, just create me one that "does" this
        _stmt _st = _stmt.of("System.out.println(1);");

        //...or you can build one for its explicit type
        _exprStmt _es = _exprStmt.of( "System.out.println(1);");
        assertEquals(_st, _es);

        //Statements can also be built from the Java source within a lambda body:
        _st = _stmt.of( ()-> System.out.println(1));
        _es = _exprStmt.of( ()-> System.out.println(1));
        assertEquals( _st, _es);

        //methods work like this:
        _method.of("public int m(){ return 23; }");

        //here the _method is created based on the source
        // of the "m" method in the anonymous class... so we don't have to sacrifice
        // readability by putting everything in a String (we get all the fun of autocomplete)
        _method.of( new Object(){
            public int m(){
                return 23;
            }
        });

        //heres how we do a stateful method,
        // NOTE: we created the variable name within the anonymous class, but it's just temporary
        _method.of( new Object(){
            public String getName(){
                return this.name;
            }
            String name;//NOTE: this is just to avoid compiler errors
        });
    }

    public void testResolveLocalClassSource(){        
        class G{}
        //make sure I can resolve local classes in the IDE path
        _class _c = _class.of(G.class);
        assertTrue( _c.isPublic());
    }
    
    public void testJavaParserIntegration(){
        class A{}
        CompilationUnit cu = Ast.of(A.class);
        assertTrue( cu.getType(0).getNameAsString().equals("A"));
    }
    
    public void testAdhocAndMacroIntegration(){
        _runtime _ah = _runtime.of(_class.of( "aaaa.bbbb.C", new @_dto Object(){ public int x, y;}) );
        _proxy p = _ah.proxy("aaaa.bbbb.C" );
        System.out.println(p.get_class());
        p.set("x", 100).set("y", 200);
        assertEquals(100, p.get("x"));
        assertEquals(200, p.get("y"));
    }
    
    @interface N{
        int value();
    }
    
    public void testProto(){
        class LL{
            public int i = 100;
            
            @N(200)
            public void method(int j){
                int h = 300;
                h+=j;
                System.out.println( "OUTPUT "+ 400 + h );
            }
        }
        _class _c = _class.of(LL.class);
        //make sure there are (4) int literals (100, 200, 300, 400) in class
        assertEquals( 4, $ex.intLiteralEx().countIn(LL.class));
        assertEquals( 4, $.intLiteral().countIn(LL.class));
        //there is (1) 200 int literal
        assertEquals( 1, $ex.intLiteralEx(200).countIn(LL.class));
        
        
        //(3) reference to the int type in the class
        assertEquals(3, $typeRef.of(int.class).countIn(LL.class));
        //$.intLiteral()
        
        assertNotNull($.stringLiteral("OUTPUT ").firstIn(LL.class));
        // $expr.stringLiteral(pattern);
        //assertEquals(1, $.stringLiteral("OUTPUT").count(LL.class));
    }
    
    public void testDiff(){
        @_dto
        class FF{
            int x,y;
        }
        @_rename("FF") @_dto
        class EE{
            int x,y;
        }
        System.out.println( _class.of(FF.class) );
        System.out.println( _class.of(EE.class) );
        //diff the two classes above (after the macro annotations of course)
        assertTrue(_diff.of(FF.class, EE.class).isEmpty());
        
        _class _a = _class.of(FF.class);
        _class _b = _class.of(EE.class);
        
        assertTrue(_diff.of(_a, _b).isEmpty());
        
        //LeftOnly delta
        //add deprecated to _a only
        _a.addAnnos(Deprecated.class);
        _diff _d = _diff.of(_a, _b);        
        assertEquals(1, _d.size());//there is (1) diff
        
        _d.patchLeftToRight(); //apply all left(_a) changes to right (_b)
        assertEquals( _a, _b);
        assertTrue( _diff.of(_a, _b).isEmpty());
        
        //RightOnly Delta
        _b.addAnnos("N(2)");
        _d = _diff.of(_a, _b);
        assertEquals( 1, _d.size());
        assertTrue( _d.hasRightOnly());
        assertTrue( _d.hasRightOnlyAt(_anno.class));
        
        _d.patchRightToLeft(); //apply right changes to left
        
    }
    
}
