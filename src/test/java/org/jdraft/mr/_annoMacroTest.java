package org.jdraft.mr;

import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.Node;
import com.github.javaparser.ast.body.ConstructorDeclaration;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.body.TypeDeclaration;
import com.github.javaparser.ast.type.Type;
import junit.framework.TestCase;
import org.jdraft.*;
import org.jdraft.macro.*;

import java.io.IOException;
import java.io.Serializable;
import java.lang.annotation.*;
import java.lang.reflect.Method;
import java.util.*;
import java.util.function.Consumer;

public class _annoMacroTest extends TestCase {

    public void testApplyToField(){
        class GG{
            public @_static final int a = 100;
        }
        _class _c = _class.of(GG.class);
        //apply macro to field
        _annoMacro.Apply.to(GG.class, _c.getField("a").getFieldDeclaration() );

        //make sure we "ran" the macro
        assertTrue( _c.getField("a").isStatic());
        //make sure the macro cleaned up after itself
        assertFalse( _c.getField("a").hasAnno(_static.class));
    }

    public void testApplyMultipleToFields(){
        //System.out.println( _c );
        class KK{
            @_public @_static @_final @Deprecated int a;
        }
        _class _c = _class.of(KK.class);
        _annoMacro.Apply.to(KK.class, _c.getField("a").getFieldDeclaration() );

        _field _f = _c.getField("a");
        assertTrue( _f.isStatic() && _f.isPublic() && _f.isFinal());
        assertTrue( _f.hasAnno(Deprecated.class)); //make sure I DONT remove a NON-macro annotation
        assertFalse( _f.hasAnno(_public.class)); //make sure I removed all the other (runtime/macro/annotations)
        assertFalse( _f.hasAnno(_static.class));
        assertFalse( _f.hasAnno(_final.class));
    }

    public void testApplyToMethod(){
        class MM{
            @_public @_static @Deprecated void main(String[] args){

            }
        }
        _class _c = _class.of( MM.class );
        _annoMacro.Apply.to( MM.class, _c.getMethod("main").ast() );
        _method _m = _c.getMethod("main");
        assertTrue( _m.isStatic());
        assertTrue( _m.isPublic());
        assertTrue( _m.hasAnno(Deprecated.class));
        assertFalse( _m.hasAnno(_public.class));
        assertFalse( _m.hasAnno(_static.class));
        //System.out.println( _c );
    }

    public void testApplyToMethodParameters(){
        class AMP{
            public void m( @_final @Deprecated int a ){

            }
        }
        _class _c = _class.of(AMP.class);
        _annoMacro.Apply.to(AMP.class, _c.getMethod("m").ast() );

        assertTrue( _c.getMethod("m").getParameter(0).isFinal() );
        assertTrue( _c.getMethod("m").getParameter(0).hasAnno(Deprecated.class) );
        assertFalse( _c.getMethod("m").getParameter(0).hasAnno(_final.class) );
    }

    @Target(ElementType.TYPE_USE)
    @Retention(RetentionPolicy.RUNTIME)
    @interface _str{
        class Act implements Consumer<com.github.javaparser.ast.type.Type>{

            @Override
            public void accept(Type node) {
                node.replace( Types.of(String.class));
                _annoMacro.removeAnnotation(node, _str.class);
            }
        }
    }

    public void testApplyToMethodReturnType(){
        class MRT{
            public @_str int a(){
                return 2;
            }
        }

        _class _c = _class.of(MRT.class);

        //!! problem with JavaParser!!
        // this will parse and move the @_str annotation out from the return type and put it on the method
        // like so:
        //    @_str
        //    public int a() {
        //        return 2;
        //    }
        // it should be:
        //    public @_str int a(){
        //        return 2;
        //    }
        //CompilationUnit cu = StaticJavaParser.parse("class MRT{ public @_str int a(){ return 2; } }");

        //OK, manually add an annotation and process it
        _c.getMethod("a").getTypeRef().ast().getAnnotations().add(Ast.anno("@_str"));
        //System.out.println( cu );

        //System.out.println( _c );
        assertTrue( _c.getMethod("a").getTypeRef().hasAnno(_str.class));
        _annoMacro.Apply.to(MRT.class, _c.getMethod("a").ast() );

        assertTrue( _c.getMethod("a").getTypeRef().is(String.class) );
        assertFalse( _c.getMethod("a").getTypeRef().hasAnno(_str.class));
    }

    public void testApplyToMethodExceptionType(){
        class MTE{
            public int a() throws @_remove IOException {
                return 2;
            }
        }
        _class _c = _class.of(MTE.class);
        assertTrue( _c.getMethod("a").getThrows().ast().get(0).getAnnotation(0).getNameAsString().equals("_remove") );
        //System.out.println( _c.getMethod("a").getThrows().ast().get(0).getAnnotation(0).getNameAsString().equals("_remove") );
        _annoMacro.Apply.to(MTE.class, _c.getMethod("a").ast());

        System.out.println(_c);
        assertFalse( _c.getMethod("a").hasThrows() );
        //assertTrue( _c.getMethod("a").getThrows().ast().get(0).getAnnotations().isEmpty() ); //no annotation
    }

    public void testApplyToConstructor(){
        class C{
            @_public private C(){

            }
        }
        CompilationUnit cu = Ast.of(C.class);
        ConstructorDeclaration ct = (ConstructorDeclaration) ((TypeDeclaration)cu.getType(0)).getConstructors().get(0);
        assertTrue( ct.isPrivate() );

        //apply macro
        _annoMacro.Apply.to(C.class, ct);
        assertTrue( ct.isPublic());
        assertFalse( ct.getAnnotationByClass(_public.class).isPresent());

        System.out.println( cu );
    }

    public void testApplyToConstructorParameter(){
        class D{
            D( @_final int f){ }
        }
        CompilationUnit cu = Ast.of(D.class);
        ConstructorDeclaration ct = (ConstructorDeclaration) ((TypeDeclaration)cu.getType(0)).getConstructors().get(0);
        assertFalse( ct.getParameter(0).isFinal() );
        assertTrue( ct.getParameter(0).getAnnotationByClass(_final.class).isPresent() );

        _annoMacro.Apply.to( D.class, ct);
        System.out.println( cu );
        /*
        assertTrue( ct.getParameter(0).isFinal() );
        assertFalse( ct.getParameter(0).getAnnotationByClass(_final.class).isPresent() );
        */

    }

    /**
     * This is a prototype AnnotationExpr Macro
     * the annotation CONTAINS a field "$" that is the meat of the Macro
     * i.e.
     * you annotate (a Type in this case) like so
     *
     */
    @Retention(RetentionPolicy.RUNTIME)
    @Target(ElementType.TYPE)
    @interface _printMethodNames {
        /**
         * When a Type is annotated with this @_printMethodNames
         * we will modify the code of all methods to include a println
         * statement as the first statement in the method
         */
        Consumer<TypeDeclaration> $ = (t) -> t.walk(MethodDeclaration.class, m -> {
            if (m.getBody().isPresent()) {
                m.getBody().get().addStatement(0,
                        Ast.statement("System.out.println( \"called " + m.getNameAsString() + "\")"));
            }
            _annoMacro.removeAnnotation(t, _printMethodNames.class);
        });
    }

    public void testUseAnnotationMacro() {
        @_printMethodNames
        class F {
            public void m() {
                System.out.println("the thing");
            }
            public void bb(){

            }
        }

        CompilationUnit cu = Ast.of(F.class);
        List<Consumer<Node>> lcns = _annoMacro.Resolve.from(F.class );
        assertEquals( 1, lcns.size());

        //individual macro
        Consumer<Node> c = _annoMacro.Resolve.resolveMacro(F.class.getAnnotations()[0]);
        assertNotNull(c);
        c.accept(cu.getType(0));
        System.out.println(cu);
    }

    public void testAbstractAppliedToTypeAndMethod(){
        @_abstract class F{
            @_abstract public void getValue(){}
        }

        CompilationUnit cu = Ast.of(F.class);
        //apply abstract annotation to a class
        Consumer<Node> am = _annoMacro.Resolve.resolveMacro(F.class.getAnnotations()[0]);
        am.accept(cu.getType(0)); //apply the abstract macro to the class

        //apply the abstract annotation to the method
        try {
            Method m = F.class.getDeclaredMethod("getValue");
            Consumer<Node> ma = _annoMacro.Resolve.resolveMacro(m.getDeclaredAnnotations()[0]);
            ma.accept(cu.getType(0).getMethodsByName("getValue").get(0));
        }catch(NoSuchMethodException nsme){
            fail("expected a method");
        }
        System.out.println( cu );
    }

    public void testCtorAnnoMacro() {
        @_extend(Serializable.class)
        class F {
        }
        _class _c = _class.of(F.class);
        Consumer<Node> cd =
                _annoMacro.Resolve.resolveMacro(F.class.getAnnotations()[0]);
        cd.accept(_c.ast());

        System.out.println(_c);
    }

    public static void runMacros(CompilationUnit cu, Class clazz) {
        Map<Node, List<Consumer<Node>>> nodeToMacro = new HashMap<>();
        Annotation[] as = clazz.getAnnotations();

    }

    public void testAll(){
        @_dto
        class C{
             @_public int x,y;
             @_static void m( @_final int a){}
             @_public C(){}

             @_static class Inner{
                 @_final int f = 100;
                 @_final void m(){}
                 @_public Inner(){ }
             }
        };
        CompilationUnit cu = Ast.of(C.class);
        System.out.println( cu );
        _annoMacro.Apply.to(C.class, cu);
        System.out.println( cu );
    }


}
