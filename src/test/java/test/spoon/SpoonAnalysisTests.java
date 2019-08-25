package test.spoon;

import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.Modifier;
import com.github.javaparser.ast.Node;
import com.github.javaparser.ast.PackageDeclaration;
import com.github.javaparser.ast.body.*;
import com.github.javaparser.ast.nodeTypes.NodeWithJavadoc;
import com.github.javaparser.ast.nodeTypes.NodeWithModifiers;
import junit.framework.TestCase;
import org.jdraft._class;
import org.jdraft._type;
import org.jdraft.macro._abstract;
import org.jdraft.macro._public;
import org.jdraft.macro._static;
import org.jdraft.proto.$;
import org.jdraft.proto.$catch;
import org.jdraft.proto.$method;
import org.jdraft.proto.$node;

import java.util.ArrayList;
import java.util.List;

public class SpoonAnalysisTests extends TestCase {

    public void testFindEmptyCatchBlocks(){
        //this represents empty catch blocks
        $catch $empty = $catch.of().and(c-> c.getBody().isEmpty());

        class C{
            void m(){
                try{
                    System.out.println("Hey");
                } catch(Exception e){
                    //empty
                }
            }
        }
        assertEquals(1, $empty.count(C.class));
    }

    public void testFindUndocumentedElements(){
        //find members that dont have javadocs
        $node $memberWithNoJavadoc =
                $.of( TypeDeclaration.class, MethodDeclaration.class, FieldDeclaration.class, ConstructorDeclaration.class)
                .and(n -> ((NodeWithModifiers)n).hasModifier(Modifier.Keyword.PUBLIC)
                        && !((NodeWithJavadoc)n).hasJavaDocComment() );
        @_public class EX{
            public int a;
            public void m(){}
            @_public @_static class I{

            }
        }
        assertEquals(4, $memberWithNoJavadoc.count(EX.class));
    }

    public void testFindEmptyMethodBodies(){
        $method $implementedMethodsWithNoBody =
                $method.of().and( m -> m.isImplemented() && m.getBody().isEmpty());
        @_abstract class MEB{

            void a(){} //this counts
            void b(){ /*only comment*/ } //this counts
            class Inner{
                void d(){} //this counts
                @_abstract void e() {} //doesnt count
            }
            @_abstract void c(){} //doesnt count
        }
        _class _c = _class.of(MEB.class);
        System.out.println( _c);
        assertEquals(3, $implementedMethodsWithNoBody.count(_c) );
    }

    /**
     * from a Spoon presentation
     * https://youtu.be/ZZzdVTIu-OY?t=477
     */
    public void testQueryPublicApi(){
        //I want all public methods on all types that exist in the "qpwc" and "publicapi" packages

        //this sorta sucks
        $node $expectedPkg = $.of(CompilationUnit.class)
                .and( c -> {
                    CompilationUnit cu = (CompilationUnit) c;
                    return cu.getPackageDeclaration().isPresent()
                            && (cu.getPackageDeclaration().get().getName().asString().equals("qpwc")
                            || cu.getPackageDeclaration().get().getName().asString().equals("publicapi") );
                });

        $method $publicMethodsInExpectedPackages = $.method($.PUBLIC)
                .$hasAncestor( $expectedPkg );

        //What I'd normally do...
        //List<_type> _ts = _batch.of(...path to source directory...).listTypes();
        //but for testing sake
        List<_type> _ts = new ArrayList<_type>();
        _ts.add( _class.of("qpwc.AClass").method("public void a(){}") );
        _ts.add( _class.of("qpwc.BClass").method("public void b(){}") );
        _ts.add( _class.of("publicapi.CClass").method("public void c(){}") );
        _ts.add( _class.of("qpwc.aaaa.BClass").method("public void XXXXX(){}") );

        assertEquals( 3, $publicMethodsInExpectedPackages.count(_ts));

    }
}
