package org.jdraft.macro;

import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.body.TypeDeclaration;
import org.jdraft._type;

import java.lang.annotation.*;
import java.util.function.Consumer;

/**
 * Annotation/Macro to set the package of a {@link _type} (and optionally create a top-level type
 * from a nested/local type)
 *
 * @see macro
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE, ElementType.TYPE_USE})
public @interface _package {

    String value() default "";

    class Act extends macro<_package, TypeDeclaration> {

        String packageName = "";

        public Act(String packageName ){
            super(_package.class);
            this.packageName = packageName;
        }

        public Act( _package p){
            super(p);
            this.packageName = p.value();
        }

        @Override
        public void expand(TypeDeclaration typeDeclaration) {
            to(typeDeclaration, this.packageName);
        }

        public static <T extends TypeDeclaration> T to(T typeDeclaration, String packageName){
            if( !typeDeclaration.isTopLevelType() ){
                typeDeclaration.setStatic(false);
                CompilationUnit cu = new CompilationUnit();
                cu.addType( typeDeclaration );
                cu.setPackageDeclaration( packageName);
            } else{
                typeDeclaration.findCompilationUnit().get().setPackageDeclaration(packageName);
            }
            return typeDeclaration;
        }

        @Override
        public String toString(){
            return "macro[package(\""+packageName+"\")]";
        }
    }
}
