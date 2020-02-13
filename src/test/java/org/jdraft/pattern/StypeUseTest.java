package org.jdraft.pattern;

import com.github.javaparser.ast.Node;
import com.github.javaparser.ast.body.TypeDeclaration;
import com.github.javaparser.ast.expr.Name;
import com.github.javaparser.ast.expr.SimpleName;
import com.github.javaparser.ast.type.ClassOrInterfaceType;
import com.github.javaparser.ast.type.Type;
import com.github.javaparser.ast.type.TypeParameter;
import junit.framework.TestCase;
import org.jdraft.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class StypeUseTest extends TestCase {

    public static List<_typeParameter> listAllTypeParameters(Node n ){
        return $.typeParameter().listIn(Ast.root(n));
    }

    public static boolean isTypeUsedTypeParameter( Node n ){
        if( n instanceof ClassOrInterfaceType && n.getParentNode().isPresent()){

        }
        return false;
    }

    public void testTypeUse(){
        class F<I> extends TestCase {
            int a;
            String b;

            public <A extends Map & _java._compoundNode> A m() throws IOException { return null; }

            public java.util.List getAList(){
                System.out.println( 1);
                int localVar = 100;
                return new ArrayList();
            }
        }

        //System.out.println( "Hello");
        //$.of().forEachIn(F.class, System.out::println);
        $.of( $method.of(m-> m.isPublic()), $var.of().$local() ).forEachIn(F.class, System.out::println);
        $.of(Ast.METHOD_CALL_EXPR).forEachIn(F.class, System.out::println);


        $node typeUse =
                $node.of(Type.class)
                        .$and(t-> !Ast.isParent(t, p-> p instanceof ClassOrInterfaceType) )
                        .$and(t-> ! (t instanceof TypeParameter))
                        .$and(t-> $typeParameter.of().$name(t.toString()).countIn( Ast.root( t ) ) == 0);

        $node.of().$isParent( $import.of() ).forEachIn( F.class, n-> System.out.println(n +" "+ n.getClass()) );
        _class _c = _class.of(F.class).addImports(Map.class, UUID.class);



        $node.of().$isParent($import.of()).forEachIn(_c, e-> System.out.println(e+ " *** "+e.getClass()));

        $node $importNames = $node.of(Name.class).$isParent($import.of());

        // System.out.println( _c );
        System.out.println( "NODE IMPORTS "+ $node.of( $importNames, $node.of(SimpleName.class).$isParent(TypeDeclaration.class), typeUse).listIn(_c) );
        //System.out.println( "IMPORTS "+ $import.of().listIn(_c));


        //assertEquals(1,$import.of(Map.class).count(_c));

        //System.out.println( $node.of().forEachIn(F.class, n-> System.out.println( n))); //+" "+n.getClass())));

        //System.out.println( "TYPE USES "+ $typeUse.of().listIn(F.class) );
        //$node.of( $proto );

        //System.out.println( $typeParameter.of().$name("A").listIn( _c ) );


        //$node typeName = $node.of(SimpleName.class).$hasParent(TypeDeclaration.class);
        //assertEquals( "F", typeName.listIn(F.class).get(0).toString());



        typeUse.forEachIn(F.class, e-> System.out.println( e+ " "+e.getClass().getCanonicalName()));

        //a type name
        //$node.of(SimpleName.class).$hasParent(TypeDeclaration.class).forEachIn(F.class, e-> System.out.println( e+ " "+e.getClass().getCanonicalName()));
        //$node.of(Type.class).addConstraint(t-> ! (t instanceof TypeParameter))
        //        .$hasParent( $node.of().$hasChild( $typeParameter.of() ) )
        //        .forEachIn(F.class, e-> System.out.println( e+ " "+e.getClass().getCanonicalName()));
        //$node.of().forEachIn(F.class, e-> System.out.println( e+ " "+e.getClass().getCanonicalName()));
        //
        // $node.of("$typeUse$", SimpleName.class)
    }
}
