package org.jdraft.macro;

import com.github.javaparser.ast.body.TypeDeclaration;
import org.jdraft._type;

import java.lang.annotation.*;
import java.util.Arrays;

/**
 * Annotation/Macro to add imports (and static imports) to a {@link _type}
 *
 * @see macro
 */
@Retention( RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE, ElementType.TYPE_USE})
public @interface _importClass {
    /** @return the classes to be imported */
    Class[] value() default {};

    class Act extends macro<_importClass, TypeDeclaration> {

        Class[] classes;

        public Act( _importClass _ic ){
            super(_ic);
            this.classes = _ic.value();
        }

        public Act( Class...classes ){
            super(_importClass.class);
            this.classes = classes;
        }

        @Override
        public void expand(TypeDeclaration typeDeclaration) {
            Arrays.stream(this.classes).forEach( c ->
                typeDeclaration.tryAddImportToParentCompilationUnit(c) );
        }
    }
}