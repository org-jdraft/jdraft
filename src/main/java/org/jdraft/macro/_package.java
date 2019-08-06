package org.jdraft.macro;

import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.body.TypeDeclaration;
import org.jdraft._type;

import java.lang.annotation.*;
import java.util.function.Consumer;

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE})
public @interface _package {

    String value() default "";

    class Macro implements _macro<_type> {
        String packageName;

        @Override
        public String toString(){
           return "macro[package(\""+packageName+"\")]"; 
        }
        
        public Macro( _package _p ){ this.packageName = _p.value(); }

        public Macro( String packageName ){
            this.packageName = packageName;
        }

        @Override
        public _type apply(_type _t) {
            return to( _t, packageName);
        }

        public static <T extends _type> T to( T _model, String packageName ){
            if( !_model.isTopLevel() ){
                _model.getModifiers().setStatic(false); //top level entities cannot be static
                CompilationUnit cu = new CompilationUnit();
                cu.addType( (TypeDeclaration)_model.ast());
                if( packageName != null && !packageName.isEmpty()){
                    cu.setPackageDeclaration(packageName);
                }
            }
            _model.setPackage(packageName);
            return _model;
        }
    }

    class Act implements Consumer<TypeDeclaration> {
        String packageName = "";

        public Act(String packageName ){
            this.packageName = packageName;
        }

        public Act( _package p){
            this (p.value());
        }

        @Override
        public void accept(TypeDeclaration typeDeclaration) {
            if( !typeDeclaration.isTopLevelType() ){
                typeDeclaration.setStatic(false);
                CompilationUnit cu = new CompilationUnit();
                cu.addType( typeDeclaration );
                cu.setPackageDeclaration( packageName);
            } else{
                typeDeclaration.findCompilationUnit().get().setPackageDeclaration(packageName);
            }
        }
    }
}
