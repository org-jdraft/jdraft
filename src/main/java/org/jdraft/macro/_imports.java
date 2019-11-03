package org.jdraft.macro;

import com.github.javaparser.ast.body.TypeDeclaration;
import org.jdraft._type;

import java.lang.annotation.*;
import java.util.Arrays;

/**
 * Annotation/Macro to add imports (and static imports) to a {@link _type}
 * This can be used in (2) ways
 * <OL>
 *     <LI> annotate at the type and provide Classes to import:<PRE>
 *     @_imports({A.class, B.class, C.class})
 *     class D{
 *         //...some code
 *     }
 *     //...when the @_imports macro is encountered it will add imports to D:
 *     _class _c = _class.of(D.class);
 *     </PRE>
 *     <LI>as a field within a given class:<PRE>
 *     class C{
 *       @_imports A a; //import a single class A
 *       @_imports Class[] clazzes = {B.class, C.class}
 *       //.. some code
 *     }</PRE>
 *     </LI>
 * </OL>
 * @see macro
 */
@Retention( RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE, ElementType.TYPE_USE, ElementType.FIELD})
public @interface _imports {
    /** @return the classes to be imported */
    Class[] value() default {};

    class Act extends macro<_imports, TypeDeclaration> {

        Class[] classes;

        public Act( _imports _ic ){
            super(_ic);
            this.classes = _ic.value();
        }

        public Act( Class...classes ){
            super(_imports.class);
            this.classes = classes;
        }

        @Override
        public void expand(TypeDeclaration typeDeclaration) {
            Arrays.stream(this.classes).forEach(c ->
                    typeDeclaration.tryAddImportToParentCompilationUnit(c));
        }
    }
}