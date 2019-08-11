package org.jdraft.proto;

import com.github.javaparser.ast.Node;
import com.github.javaparser.ast.type.ClassOrInterfaceType;
import com.github.javaparser.ast.type.Type;
import com.github.javaparser.ast.type.TypeParameter;
import junit.framework.TestCase;
import org.jdraft.Ast;
import org.jdraft._class;
import org.jdraft._node;
import org.jdraft._typeParameter;

import java.util.List;
import java.util.Map;

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
        class F<I>{
            int a;
            String b;

            public <A extends Map & _node> A m(){ return null; }
        }

        _class _c = _class.of(F.class);

        System.out.println( $typeParameter.of().$name("A").listIn( _c ) );

        $node typeUse =
                $node.of(Type.class)
                        .addConstraint(t-> ! (t instanceof TypeParameter))
                        .addConstraint(t-> $typeParameter.of().$name(t.toString()).count( Ast.root( t ) ) == 0);

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
