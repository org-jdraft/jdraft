package org.jdraft;

import com.github.javaparser.ast.CompilationUnit;
import junit.framework.TestCase;
import org.jdraft.adhoc.*;
import org.jdraft.diff._diff;
import org.jdraft.diff._path;
import org.jdraft.macro.*;
import org.jdraft.proto.$;
import org.jdraft.proto.$expr;
import org.jdraft.proto.$typeRef;


/**
 * Touch all of the major APIs to make sure they work
 * 
 * Verify that the jdraft and Javaparser APIs are working 
 * (integrating together seemlessly)
 * 
 * @author Eric
 */
public class APIIntegrationTest extends TestCase {
    
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
        _adhoc _ah = _adhoc.of(_class.of( "aaaa.bbbb.C", new Object(){ int x, y;}, _dto.$) );
        _proxy p = _ah.proxy("aaaa.bbbb.C" ).set("x", 100).set("y", 200);
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
        assertEquals( 4, $expr.intLiteral().count(LL.class));
        assertEquals( 4, $.intLiteral().count(LL.class));
        //there is (1) 200 int literal
        assertEquals( 1, $expr.intLiteral(200).count(LL.class));
        
        
        //(3) reference to the int type in the class
        assertEquals(3, $typeRef.of(int.class).count(LL.class));
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
        @_name("FF") @_dto
        class EE{
            int x,y;
        }
        //diff the two classes above (after the macro annotations of course)
        assertTrue(_diff.of(FF.class, EE.class).isEmpty());
        
        _class _a = _class.of(FF.class);
        _class _b = _class.of(EE.class);
        
        assertTrue(_diff.of(_a, _b).isEmpty());
        
        //LeftOnly delta
        //add deprecated to _a only
        _a.annotate(Deprecated.class);        
        _diff _d = _diff.of(_a, _b);        
        assertEquals(1, _d.size());//there is (1) diff
        
        _d.patchLeftToRight(); //apply all left(_a) changes to right (_b)
        assertEquals( _a, _b);
        assertTrue( _diff.of(_a, _b).isEmpty());
        
        //RightOnly Delta
        _b.annotate("N(2)");
        _d = _diff.of(_a, _b);
        assertEquals( 1, _d.size());
        assertTrue( _d.hasRightOnly());
        assertTrue( _d.hasRightOnlyAt(_anno.class));
        
        _d.patchRightToLeft(); //apply right changes to left
        
    }
    
}
