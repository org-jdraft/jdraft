package test.othertools;

import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.Modifier;
import com.github.javaparser.ast.body.*;
import com.github.javaparser.ast.nodeTypes.NodeWithJavadoc;
import com.github.javaparser.ast.nodeTypes.NodeWithModifiers;
import junit.framework.TestCase;
import org.jdraft._class;
import org.jdraft._type;
import org.jdraft.macro._abstract;
import org.jdraft.macro._public;
import org.jdraft.macro._static;
import org.jdraft.pattern.$;
import org.jdraft.pattern.$catch;
import org.jdraft.pattern.$method;
import org.jdraft.pattern.$node;

import java.util.ArrayList;
import java.util.List;

public class SpoonAnalysisTests extends TestCase {

    public void testFindEmptyCatchBlocks(){
        //this represents empty catch blocks
        $catch $empty = $catch.of(c-> c.getBody().isEmpty());

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
                .$and(n -> ((NodeWithModifiers)n).hasModifier(Modifier.Keyword.PUBLIC)
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
                $method.of().$and(m -> m.isImplemented() && m.getBody().isEmpty());
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
                .$and(c -> {
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
        _ts.add( _class.of("qpwc.AClass").addMethod("public void a(){}") );
        _ts.add( _class.of("qpwc.BClass").addMethod("public void b(){}") );
        _ts.add( _class.of("publicapi.CClass").addMethod("public void c(){}") );
        _ts.add( _class.of("qpwc.aaaa.BClass").addMethod("public void XXXXX(){}") );

        assertEquals( 3, $publicMethodsInExpectedPackages.count(_ts));

    }
}
