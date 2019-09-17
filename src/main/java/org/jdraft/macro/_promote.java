package org.jdraft.macro;

import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.body.TypeDeclaration;

import java.lang.annotation.*;

/**
 * Annotation / Macro for moving a nested or local class to it's own top level class within a package)
 * @see macro
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE, ElementType.TYPE_USE})
public @interface _promote {

    /** the value is the package name, by default its the base package "" */
    String value() default "";

    class Act extends macro<_promote, TypeDeclaration> {

        String packageName;

        public Act( String packageName ){
            super(_promote.class);
            this.packageName = packageName;
        }

        public Act( _promote _pr){
            super(_pr );
            this.packageName = _pr.value();
        }

        @Override
        public void expand(TypeDeclaration typeDeclaration) {
            to(typeDeclaration, packageName);
        }

        public static <T extends TypeDeclaration> T to(T typeDeclaration, String packageName){
            typeDeclaration.setPublic(true);
            typeDeclaration.setStatic(false);
            //remove from old
            if( typeDeclaration.getParentNode().isPresent() ){
                typeDeclaration.getParentNode().get().remove( typeDeclaration );
            }
            CompilationUnit cu = new CompilationUnit();
            cu.addType(typeDeclaration);
            if( packageName != null && !packageName.isEmpty()){
                cu.setPackageDeclaration(packageName);
            }
            return typeDeclaration;
        }

        @Override
        public String toString(){
            return "macro[promote(\""+packageName+"\")]";
        }
    }
}
