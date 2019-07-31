package org.jdraft;

import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.Node;
import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.github.javaparser.ast.body.EnumConstantDeclaration;
import com.github.javaparser.ast.body.EnumDeclaration;
import com.github.javaparser.ast.expr.StringLiteralExpr;
import junit.framework.TestCase;

import java.util.ArrayList;
import java.util.List;

public class AstWalkTest extends TestCase {

    public @interface ann{
        String value();
    }

    @ann("class") public static class WalkThis{
        @ann("field") public int f = 100;

        @ann("enum") public enum E {
            @ann("constant") A{
                public int H = 1023;
                @ann("method") public String toString(){
                    int g = 20;
                    return "A TOSTRING";
                }
            }
        }
    }

    public void testWalkParents() {
         _class _c = _class.of(WalkThis.class);
        //...

        // getField("A")
        // getField("F.A");

        // getMethod("E.A.toString")
        _method _m = _c.getNestedEnum("E").getConstant("A").getMethod("toString");

         //List<Node>
         List<Node>parents = new ArrayList<Node>();
         _walk.parents( _m, n -> parents.add( n ));
         assertTrue( parents.get(0) instanceof EnumConstantDeclaration);
         assertTrue( parents.get(1) instanceof EnumDeclaration);
         assertTrue( parents.get(2) instanceof ClassOrInterfaceDeclaration);
         assertTrue( parents.get(3) instanceof ClassOrInterfaceDeclaration);
         assertTrue( parents.get(4) instanceof CompilationUnit);

        List<_java>parentNodes = new ArrayList<>();
        //_m.walkParents(n -> parentNodes.add( _java.of( n ) ));
        _walk.parents(_m, n -> parentNodes.add( _java.of( n ) ));
        assertTrue( parentNodes.get(0) instanceof _enum._constant);
        assertTrue( parentNodes.get(1) instanceof _enum);
        assertTrue( parentNodes.get(2) instanceof _class);
        assertTrue( parentNodes.get(3) instanceof _class); //the typeDeclaration AstWalkTest
        assertTrue( parentNodes.get(4) instanceof _class); //the CompilationUnit
    }

    public void testWalkAnno(){
        //_anno _a = _anno.of("a({\"eric\", \"defazio\"})");
        //_anno _b = _anno.of("a(\"t\")");
        //_class _c = _class.of("C");
        //_c.field(_field.of("int i=1;").annotate(_a, _b));
        
        Node ast = Ast.type(WalkThis.class);
        //_walk.list(_a, StringLiteralExpr.class);
        List<StringLiteralExpr> l = new ArrayList<>();
        //Ast.walk(Node.TreeTraversal.POSTORDER, ast, Ast.STRING_LITERAL_EXPR, n->true, n-> l.add(n));
        _walk.in(_walk.POST_ORDER, ast, Ast.STRING_LITERAL_EXPR, n-> true, n-> l.add(n));
        assertEquals(6, l.size());
        assertEquals( "class", l.get(0).asString());
        assertEquals( "field", l.get(1).asString());
        assertEquals( "enum", l.get(2).asString());
        assertEquals( "constant", l.get(3).asString());
        assertEquals( "method", l.get(4).asString());
        assertEquals( "A TOSTRING", l.get(5).asString());
    }
    
    public void testWalkPreorderOrPostOrder(){
        Node ast = Ast.type(WalkThis.class);

        List<Integer> i = new ArrayList<>();

        //Preorder postorder not really different when dealing with leaf nodes
        //_java.walk(Node.TreeTraversal.PREORDER, ast, Ast.INT_LITERAL_EXPR, n->true, n-> i.add(n.asInt()));
        _walk.preOrder(ast, Ast.INT_LITERAL_EXPR, n->true, n-> i.add(n.asInt()));
        assertEquals(i.size(), 3);
        assertEquals( (Integer)100, i.get(0) );
        assertEquals( (Integer)1023, i.get(1) );
        assertEquals( (Integer)20, i.get(2) );

        i.clear();
        //_java.walk(Node.TreeTraversal.POSTORDER, ast, Ast.INT_LITERAL_EXPR, n->true, n-> i.add(n.asInt()));
        _walk.postOrder(ast, Ast.INT_LITERAL_EXPR, n->true, n-> i.add(n.asInt()));
        assertEquals(i.size(), 3);
        assertEquals( (Integer)100, i.get(0) );
        assertEquals( (Integer) 1023, i.get(1) );
        assertEquals( (Integer) 20, i.get(2) );


        //PRE ORDER processes ROOT NODES FIRST (then leaves)
        List<_anno._hasAnnos> a = new ArrayList<>();
        //_java.walk(Node.TreeTraversal.PREORDER, ast, _anno._hasAnnos.class, n-> n.hasAnno(ann.class), n-> a.add(n));
        _walk.preOrder(ast, _anno._hasAnnos.class, n-> n.hasAnno(ann.class), n-> a.add(n));
        assertEquals( 5, a.size());
        assertTrue( a.get(0).getAnnos().is("@ann(\"class\")") );
        assertTrue( a.get(1).getAnnos().is("@ann(\"field\")") );
        assertTrue( a.get(2).getAnnos().is("@ann(\"enum\")") );
        assertTrue( a.get(3).getAnnos().is("@ann(\"constant\")") );
        assertTrue( a.get(4).getAnnos().is("@ann(\"method\")") );
        a.clear();

        //for POST ORDER THE LEAF NODES ARE PROCESSED FIRST
        _walk.postOrder(ast, _anno._hasAnnos.class, n-> n.hasAnno(ann.class), n-> a.add(n));
        //_java.walk(Node.TreeTraversal.POSTORDER, ast, _anno._hasAnnos.class, n-> n.hasAnno(ann.class), n-> a.add(n));
        
        assertEquals( 5, a.size());
        assertTrue( a.get(0).getAnnos().is("@ann(\"field\")") );
        assertTrue( a.get(1).getAnnos().is("@ann(\"method\")") );
        assertTrue( a.get(2).getAnnos().is("@ann(\"constant\")") );
        assertTrue( a.get(3).getAnnos().is("@ann(\"enum\")")  );
        assertTrue( a.get(4).getAnnos().is("@ann(\"class\")") );

        //List<StringLiteralExpr> l = new ArrayList<>();
        //Ast.walk(Node.TreeTraversal.POSTORDER, ast, Ast.STRING_LITERAL_EXPR, n->true, n-> l.add(n));
        

        //System.out.println( ast );
        //System.out.println( l );
        
        //assertEquals( 6, l.size());
       

        //td.walk(Node.TreeTraversal.POSTORDER).forEach(n-> System.out.println( ai.addAndGet(1)+"> ("+n.getClass().getSimpleName()+") "+ n));
        //td.walk(Node.TreeTraversal.PREORDER).forEach(n-> System.out.println( ai.addAndGet(1)+"> ("+n.getClass().getSimpleName()+") "+ n));
        //td.walk(Node.TreeTraversal.BREADTHFIRST).forEach(n-> System.out.println( ai.addAndGet(1)+"> ("+n.getClass().getSimpleName()+") "+ n));
        //td.walk(Node.TreeTraversal.PARENTS).forEach(n-> System.out.println( ai.addAndGet(1)+"> ("+n.getClass().getSimpleName()+") "+ n));
        //td.walk(Node.TreeTraversal.PARENTS)
    }
}
