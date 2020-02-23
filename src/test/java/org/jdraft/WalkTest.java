package org.jdraft;

import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.ImportDeclaration;
import com.github.javaparser.ast.Node;
import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.github.javaparser.ast.body.EnumConstantDeclaration;
import com.github.javaparser.ast.body.EnumDeclaration;
import com.github.javaparser.ast.body.TypeDeclaration;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import com.github.javaparser.ast.expr.StringLiteralExpr;
import com.github.javaparser.utils.Log;
import junit.framework.TestCase;
import org.jdraft.io._io;
import org.jdraft.macro._dto;
import org.jdraft.macro._final;
import org.jdraft.pattern.$;


public class WalkTest extends TestCase {

    public class NestedClass{
        int i;
    }
    public void testGetSource(){
        /*
        _class _c = _class.of(NestedClass.class);
        System.out.println(_c);

        class LocalClass{
            int j;
        }
        _c = _class.of(LocalClass.class);
        System.out.println(_c);

        class LocalClass2{
            @_final int x, y;
        }
        _c = _class.of(LocalClass2.class);
        System.out.println(_c);

        _c = _class.of("C", new Object(){
            int k;
        });

        System.out.println(_c);
*/
        _class _c = _class.of("C", new @_final Object(){
            String s, t;
        });



        System.out.println(_c);
    }
    /**
     * Verify I can walk into a collection of _code or _type
     * using the first()
     *           list()
     *           in()
     */
    public void testWalkCollections(){
        //we create a collection (in this case a list) to verify we can use the
        // list(), in(), and first()
        List<_type> lts = new ArrayList<>();
        Log.setAdapter(new Log.StandardOutStandardErrorAdapter());
        _class _c = _dto.Act.to(_class.of("C", new @_dto Object(){
            int x, y;
            String name;
        }));
        lts.add(_c);
        System.out.println(_io.describe());
        Log.setAdapter(new Log.SilentAdapter());
        assertEquals( Walk.list(lts, _method.class ).size(), $.method().countIn(lts));
        assertNull( Walk.first(lts, _field.class, f-> f.isFinal()));
        assertNull( Walk.first(lts, _method.class, _method.IS_MAIN)); //find the first main method

        //commented out for noise
        //_walk.in(lts.get(0), _method.class, m-> System.out.println( m));
        //_walk.in(lts, _method.class, m-> System.out.println( m) );
        //_walk.in(lts, _field.class, f-> System.out.println( f ));

        assertEquals( Walk.list(lts, _field.class).size(), $.field().countIn(lts) );
        assertEquals( Walk.list(lts, _method.class).size(), $.method().countIn(lts) );
        assertEquals( Walk.list(lts, _constructor.class).size(), $.constructor().countIn(lts) );

        System.out.println( "C IS "+ _c );
        //verify we can find the equals method
        assertNotNull( $.method().$name("equals").firstIn(lts) );

        assertNull( $.method(_method.IS_MAIN).firstIn(lts) ); //can't find a main method

        //assertEquals( _walk.list(lts, _method.class).size(), $.staticBlock().count(lts) );
    }

    public void testWalkPackageInfo(){
        _packageInfo _pi = _packageInfo.of("package aaaa.xxxx.gggg;", "import java.util.*;", "import java.net.URL;");
        //_walk.in(_pi, n -> System.out.println(n)); //walk nodes & do some action
        
        List<_import> imports = Walk.list(_pi, _import.class);
        assertEquals(2, imports.size());
        
        _import _i = Walk.first( Walk.PRE_ORDER, _pi, _import.class, t->true);
        assertEquals(_import.of("import java.util.*;"), _i);
        
        _i = Walk.first( _pi, _import.class );
        assertEquals(_import.of("import java.util.*;"), _i);
        
        
        ImportDeclaration astI = Walk.first(Walk.PRE_ORDER, _pi, ImportDeclaration.class, t->true);
        assertEquals(Ast.importDeclaration("import java.util.*;"), astI);
        
        astI = Walk.first(_pi, ImportDeclaration.class );
        assertEquals(Ast.importDeclaration("import java.util.*;"), astI);
        
    }
    
    /**
     * Note: _walk is much different than forMembers because it will walk into
     * nested classes.
     */
    public void testWalkList(){
        _class _c = _class.of("C")
            .addFields("int x=1;", "int y=2;", "String z;");
        
        //these arent TOO interesting, just some simple tests
        //do some stuff with ASTs...
        assertEquals(3, Walk.list(_c, Ast.FIELD_DECLARATION).size());
        assertEquals(2, Walk.list(_c, Ast.FIELD_DECLARATION, fd->fd.getVariable(0).getType().isPrimitiveType() ).size());


        //using draft classes can also walk, a little more concise IMHO
        assertEquals(3, Walk.list(_c, _field.class).size());

        assertEquals(2, Walk.list(_c, _field.class, fd->fd.isPrimitive()).size());
        assertEquals(1, Walk.list(_c, _field.class, fd->fd.isInit(2)).size());

    }
    
    public void testWalkIn(){
        _class _c = _class.of("C")
                .addFields("int x=1;", "int y=2;", "String z;");


        //_walk.in(_c, _class.class, c-> System.out.println(c));
        AtomicInteger at = new AtomicInteger(0);
        Walk.in(_c, TypeDeclaration.class, td->at.incrementAndGet() );
        assertTrue(at.intValue() ==1);
        
        assertTrue( Walk.list(_c, Ast.ENUM_DECLARATION).isEmpty());
        //_walk.in(_c, Ast.NODE_WITH_ABSTRACT_MOD, td->System.out.println(td) );
        //_walk.in(_c, Ast.IMPORT_DECLARATION, td->System.out.println(td) );

    }


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
        _method _m = _c.getDeclared(_enum.class, "E").getConstant("A").getMethod("toString");

        //List<Node>
        List<Node>parents = new ArrayList<Node>();
        Walk.parents( _m, n -> parents.add( n ));
        assertTrue( parents.get(0) instanceof EnumConstantDeclaration);
        assertTrue( parents.get(1) instanceof EnumDeclaration);
        assertTrue( parents.get(2) instanceof ClassOrInterfaceDeclaration);
        assertTrue( parents.get(3) instanceof ClassOrInterfaceDeclaration);
        assertTrue( parents.get(4) instanceof CompilationUnit);

        List<_java._domain>parentNodes = new ArrayList<>();
        //_m.walkParents(n -> parentNodes.add( _java.of( n ) ));
        Walk.parents(_m, n -> parentNodes.add( _java.of( n ) ));
        assertTrue( parentNodes.get(0) instanceof _constant);
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

        Node ast = Ast.typeDecl(WalkThis.class);
        //_walk.list(_a, StringLiteralExpr.class);
        List<StringLiteralExpr> l = new ArrayList<>();
        //Ast.walk(Node.TreeTraversal.POSTORDER, ast, Ast.STRING_LITERAL_EXPR, n->true, n-> l.add(n));
        Walk.in(Walk.POST_ORDER, ast, Ast.STRING_LITERAL_EXPR, n-> true, n-> l.add(n));
        assertEquals(6, l.size());
        assertEquals( "class", l.get(0).asString());
        assertEquals( "field", l.get(1).asString());
        assertEquals( "enum", l.get(2).asString());
        assertEquals( "constant", l.get(3).asString());
        assertEquals( "method", l.get(4).asString());
        assertEquals( "A TOSTRING", l.get(5).asString());
    }

    public void testWalkPreorderOrPostOrder(){
        Node ast = Ast.typeDecl(WalkThis.class);

        List<Integer> i = new ArrayList<>();

        //Preorder postorder not really different when dealing with leaf nodes
        //_java.walk(Node.TreeTraversal.PREORDER, ast, Ast.INT_LITERAL_EXPR, n->true, n-> i.add(n.asInt()));
        Walk.preOrder(ast, Ast.INT_LITERAL_EXPR, n->true, n-> i.add(n.asInt()));
        assertEquals(i.size(), 3);
        assertEquals( (Integer)100, i.get(0) );
        assertEquals( (Integer)1023, i.get(1) );
        assertEquals( (Integer)20, i.get(2) );

        i.clear();
        //_java.walk(Node.TreeTraversal.POSTORDER, ast, Ast.INT_LITERAL_EXPR, n->true, n-> i.add(n.asInt()));
        Walk.postOrder(ast, Ast.INT_LITERAL_EXPR, n->true, n-> i.add(n.asInt()));
        assertEquals(i.size(), 3);
        assertEquals( (Integer)100, i.get(0) );
        assertEquals( (Integer) 1023, i.get(1) );
        assertEquals( (Integer) 20, i.get(2) );


        //PRE ORDER processes ROOT NODES FIRST (then leaves)
        List<_anno._withAnnos> a = new ArrayList<>();
        //_java.walk(Node.TreeTraversal.PREORDER, ast, _anno._hasAnnos.class, n-> n.hasAnno(ann.class), n-> a.add(n));
        Walk.preOrder(ast, _anno._withAnnos.class, n-> n.hasAnno(ann.class), n-> a.add(n));
        assertEquals( 5, a.size());
        assertTrue( a.get(0).getAnnos().is("@ann(\"class\")") );
        assertTrue( a.get(1).getAnnos().is("@ann(\"field\")") );
        assertTrue( a.get(2).getAnnos().is("@ann(\"enum\")") );
        assertTrue( a.get(3).getAnnos().is("@ann(\"constant\")") );
        assertTrue( a.get(4).getAnnos().is("@ann(\"method\")") );
        a.clear();

        //for POST ORDER THE LEAF NODES ARE PROCESSED FIRST
        Walk.postOrder(ast, _anno._withAnnos.class, n-> n.hasAnno(ann.class), n-> a.add(n));
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
