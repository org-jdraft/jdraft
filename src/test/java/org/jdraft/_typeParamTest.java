package org.jdraft;

import com.github.javaparser.ast.NodeList;
import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.expr.SimpleName;
import com.github.javaparser.ast.type.ClassOrInterfaceType;
import com.github.javaparser.ast.type.TypeParameter;
import com.github.javaparser.ast.type.UnionType;
import junit.framework.TestCase;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Eric
 */
public class _typeParamTest extends TestCase {

    public void testTypeParam(){
        //without <>
        assertTrue(_typeParam.of("A").is("A"));
        assertTrue(_typeParam.of("A extends Serializable").is("A extends Serializable"));
        assertTrue(_typeParam.of("A extends Serializable & Cloneable").is("A extends Serializable & Cloneable"));
        assertTrue(_typeParam.of("A extends Serializable & Cloneable").is("A extends Cloneable & Serializable"));

        //with <>
        assertTrue(_typeParam.of("<A>").is("A"));
        assertTrue(_typeParam.of("<A extends Serializable>").is("A extends Serializable"));
        assertTrue(_typeParam.of("<A extends Serializable & Cloneable>").is("A extends Serializable & Cloneable"));
        assertTrue(_typeParam.of("<A extends Serializable & Cloneable>").is("A extends Cloneable & Serializable"));

        _typeParam _tp = _typeParam.of("A extends Serializable & Cloneable");
        //Print.tree(_tp.ast());

        _tp = _typeParam.of("A extends Base & Pair<A,B>");
        assertTrue( _tp.is("A extends Pair<A,B> & Base"));
        //Print.tree(_tp.ast());

        //TypeParameter tp = Types.typeParam("A extends Base & Pair<A,B>");
        /*
        _tp = _typeParam.of("A super Serializable & Cloneable");
        Print.tree(_tp);
         */

    }

    public void testTPS(){
        _typeParam _tp = _typeParam.of("<U extends Comparable<? super java.io.Serializable>>");
        assertTrue( _tp.is("<U extends Comparable<? super java.io.Serializable>>"));
        assertTrue( _tp.is("<U extends Comparable<? super Serializable>>")); //fully v non fully qualified
    }
    public void testBuildFromScratch(){
        _typeParam _tp = _typeParam.of();
        NodeList<ClassOrInterfaceType> tb = new NodeList<>();
        tb.add(Types.classOrInterfaceType("Map<Integer,String>"));
        _tp.setExtendsTypeBound(tb);

        _tp.addExtendsTypeBound("String");


        System.out.println( _tp );
        //_tp.name("A").ast().setTypeBound();

    }
    public void testTypeParameterT(){
        _typeParams _tps = _typeParams.of("B extends R & J");
        System.out.println( _tps.getAt(0).getAstExtendsTypeBound() );
        
        System.out.println( _tps );
        
        
        TypeParameter t = new TypeParameter();
        
        t.setName("A");
        
        
        System.out.println( t );
        
        System.out.println( _typeParams.of("A") );
        System.out.println( _typeParams.of("A,B") );
        System.out.println( _typeParams.of("A,B extends R & J") );
        //System.out.println( _typeParameters.of("A,B super R") );        
        _typeParams _tp = _typeParams.of("A, B extends R");
        TypeParameter A = _tp.getAt(0).ast();
        System.out.println( A.getName() );
        System.out.println( "TYPE BOUND" + A.getTypeBound() );
        TypeParameter B = _tp.getAt(1).ast();
        System.out.println( B.getName() );
        System.out.println( "TYPE BOUND" + B.getTypeBound() );
        
        _tp = _typeParams.of( "<String, Integer>" );
        _tp = _typeParams.of( "<A, B extends Map<Integer, ? extends R>>" );
        
        
        //System.out.println("TYPE PARAMETERS "+  _tp );        
        //Ast.classDeclaration("class Dummy"+ Text.combine(tps) +"{}");
    }
    
    //class CC <A, B extends Map<Integer, ? extends R>> {
        
    //}
   
    public void testTypeParameterAnno(){
        String s = "@R A extends @Test B";
        _typeParam _tp = _typeParam.of(s);
        TypeParameter astTp = _tp.ast();
        System.out.println( _tp );
        System.out.println( astTp );
        
        assertEquals( 1, _tp.listAnnoExprs().size());
        assertTrue( _tp.is(s) );
        
        
        SimpleName sn = astTp.getName();
        astTp.getTypeBound();
        
        _tp.getAstExtendsTypeBound().forEach(t->System.out.println( "TB "+ t));
        
        assertEquals( Ast.annotationExpr("@Test"), _tp.getAstExtendsTypeBound().get(0).getAnnotation(0) );
        System.out.println( _tp );
    }
    
    public void testTypeParametersWithAnno(){
        _typeParams _a = _typeParams.of("<A extends @Test B>");
        System.out.println( _a );
        
        _a = _typeParams.of("<A extends @Test aaaa.B>");
        System.out.println( _a );
        
    }
    
    public void testTypeParameters(){
        _typeParams _a = _typeParams.of("<A,B,C,D,E>");
        _typeParams _b = _typeParams.of("<E,D,C,B,A>");
        assertEquals( _a, _b);
        assertEquals( _a.hashCode(), _b.hashCode());

        _a = _typeParams.of("<A extends aaaa.B>");
        _b = _typeParams.of("<A extends B>");
        assertEquals( _a, _b);

        //out of order, &
        _a = _typeParams.of("<A extends aaaa.B, T extends ccc.R>");
        _b = _typeParams.of("<T extends R, A extends B>");
        assertEquals( _a, _b);
    }

    public void testFullyQualified(){
        _typeParam _a = _typeParam.of("T extends java.io.Serializable");
        _typeParam _b = _typeParam.of("T extends Serializable");

        assertEquals( _a, _b);
        assertEquals( _a.hashCode(), _b.hashCode());

        _typeParam _c = _typeParam.of("I extends java.io.DataInput & java.io.Closeable");
        _typeParam _d = _typeParam.of("I extends DataInput & Closeable");

        assertEquals( _c, _d);
        assertEquals( _c.hashCode(), _d.hashCode());
    }

    public void testTypeParameter(){
        _typeParam _tp = _typeParam.of( "T" );
    }


    /*
    public void testIntersectionTypes(){
        class D{
            <I extends java.io.DataInput & java.io.Closeable> int read(I source) {
                return 1;
            }
        }
        _class _c = _class.of( D.class);

        List<IntersectionType> it = new ArrayList<>();
        //_c.walkAllCode( IntersectionType.class, t-> it.add(t) );
        //_c.walkAllCode( IntersectionType.class, t-> it.add(t) );
        System.out.println( _c.getMethod( "read").getTypeParameters() );

        //System.out.println( it.get(0));
        _typeParameter._typeParameters _tps = _typeParameter._typeParameters.of("<I extends java.io.DataInput & java.io.Closeable>" );
        assertTrue( _tps.is( "<I extends DataInput & Closeable>"));

        //Type t = AST.typeRef( "B extends C & D");
        //System.out.println( t );
    }
    */

    public void testUnionTypeParams(){
        class WException{
            void f(){
                try{
                    URI u = URI.create( "A URI" );
                    File f = File.createTempFile( "A", "B" );
                }
                catch( IllegalArgumentException | IOException e){

                }
            }
        }
        _class _c = _class.of( WException.class);
        //System.out.println( _c );
        List<UnionType> uut = new ArrayList<UnionType>();
        //_c.getMethod( "f").walkBody( UnionType.class, ut-> uut.add(ut) );
        Walk.in( _c.getMethod("f").getBody().ast(),
                UnionType.class,
                ut-> uut.add( ut) );
        //_walk.in( _c.getMethod("f").getBody(),
        //        UnionType.class,
        //        ut-> uut.add( (UnionType)ut) );
        // System.out.println( uut.get(0)+" "+uut.get(0).getClass() );
        //AST.typeRef( "IllegalArgumentException|IOException" );
        _typeRef _ttr = _typeRef.of( "IllegalArgumentException|IOException");
        //_typeRef _utr = _typeRef.of( uut.get(0) );
        //System.out.println( "UTR " + _utr );
        //List<Parameter> pp = new ArrayList<Parameter>();
        //_c.getMethod( "f").walk( CatchClause.class, cc-> pp.add(cc.getParameter())  );

        //System.out.println( Ast.normalize( _ttr.getType() ) );

        //System.out.println( pp.get(0) );
        //System.out.println( pp.get(0).getClass() );
        //Parameter pa = pp.get(0);
        //pa.walk( c-> System.out.println( c.getClass() ) );
        //pp.forEach( p -> System.out.println( "HEY "+ p));

        //pp.
        //_typeRef _tr = _typeRef.of( "AException | BException");
    }

    public void testParseTypeParams(){
        MethodDeclaration md = Ast.methodDeclaration(
                "default <N extends Node> _hasBlock walkAST( Class<N> astNodeClass, Predicate<N> astNodeMatchFn, Consumer<N> astNodeActionFn ){}");
        NodeList<TypeParameter> tps =  md.getTypeParameters();
        System.out.println( tps );

        ClassOrInterfaceDeclaration coid =
                (ClassOrInterfaceDeclaration)Ast.typeDeclaration( "class D <T extends A & B & C, X> { }" );
        tps = coid.getTypeParameters();

        System.out.println( tps );
    }

    public void testTypeArgs(){
        _typeParams _tp = _typeParams.of("<T>");
        System.out.println (_tp);

    }
}
