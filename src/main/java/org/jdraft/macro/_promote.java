package org.jdraft.macro;

import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.body.TypeDeclaration;
import org.jdraft._type;

import java.lang.annotation.*;
import java.util.function.Consumer;

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE, ElementType.TYPE_USE})
public @interface _promote {

    Macro $ = new Macro((String)null);

    String value() default "";

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

    class Act  implements Consumer<TypeDeclaration> {
        String packageName;

        public Act( String packageName ){
            this.packageName = packageName;
        }

        public Act( _promote _pr){
            this(_pr.value());
        }

        @Override
        public void accept(TypeDeclaration typeDeclaration) {
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
            _macro.removeAnnotation( typeDeclaration, _promote.class);
        }
    }
}
