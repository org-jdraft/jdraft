package org.jdraft.macro;

import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.body.TypeDeclaration;

import java.lang.annotation.*;

/**
 * Annotation / Macro for moving a nested or local class to it's own top level class within a package)
 * @see _macro
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE, ElementType.TYPE_USE})
public @interface _promote {

    /** the value is the package name, by default its the base package "" */
    String value() default "";

    /*
    Macro $ = new Macro((String)null);

    class Macro implements _macro<_type> {

        public String packageName;

        @Override
        public String toString(){
           return "macro[promote(\""+packageName+"\")]"; 
        }
        
        public Macro( _promote _pr){ this.packageName = _pr.value(); }

        public Macro( String packageName ){
            this.packageName = packageName;
        }

        @Override
        public _type apply(_type _t) {
            return to( _t, packageName );
        }

        public static <T extends _type> T to( T _model ){
            return to( _model, null );
        }

        public static <T extends _type> T to( T _model, String packageName ){
            _model.setPublic();
            _model.getModifiers().setStatic(false); //top level entities cannot be static
            CompilationUnit cu = new CompilationUnit();
            cu.addType( (TypeDeclaration)_model.ast());
            if( packageName != null && !packageName.isEmpty()){
                cu.setPackageDeclaration(packageName);
            }
            _model.removeAnnos(_promote.class);
            return _model;
        }
    }
    */

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
            //_macro.removeAnnotation( typeDeclaration, _promote.class);
            return typeDeclaration;
        }

        @Override
        public String toString(){
            return "macro[promote(\""+packageName+"\")]";
        }
    }
}
