package org.jdraft.macro;

import com.github.javaparser.ast.body.TypeDeclaration;
import org.jdraft._type;

import java.lang.annotation.*;
import java.util.Arrays;

/**
 * Annotation/Macro to add imports (and static imports) to a {@link _type}
 *
 * @see _macro
 */
@Retention( RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE, ElementType.TYPE_USE})
public @interface _importClass {
    /** @return the classes to be imported */
    Class[] value() default {};


    /*
    class Macro implements _macro<_type> {
        Set<ImportDeclaration> importDeclarations = new HashSet<>();

        public static Macro of(){
            return new Macro();
        }

        private Macro (){
        }

        @Override
        public String toString(){
           return "macro[importClass("+importDeclarations+")]"; 
        }
        
        public Macro( _importClass _i ){
            Arrays.stream( _i.value() ).forEach(c->  importDeclarations.add(new ImportDeclaration( c.getCanonicalName(), false, false)));
            /*
            Arrays.stream( _i.statically() ).forEach(s-> {
                boolean isAsterisk = s.endsWith(".*");
                if( isAsterisk ){
                    s = s.substring(0, s.length() -2);
                }
                importDeclarations.add(new ImportDeclaration( s, true, isAsterisk));
            });

        }
    */
        /*
        public Macro add( ImportDeclaration ...ids ){
            Arrays.stream(ids).forEach( id-> this.importDeclarations.add(id));
            return this;
        }

        public Macro addStatic( String...staticImports ){
            Arrays.stream( staticImports ).forEach(s-> {
                boolean isAsterisk = s.endsWith(".*");
                if( isAsterisk ){
                    s = s.substring(0, s.length() -2);
                }
                importDeclarations.add(new ImportDeclaration( s, true, isAsterisk));
            });
            return this;
        }


        @Override
        public _type apply( _type _t){
            return to( _t, this.importDeclarations);
        }

        public static <T extends _type> T to( T _t, Set<ImportDeclaration> ids){
            ids.forEach( id-> _t.astCompilationUnit().addImport(id) );
            return _t;
        }
    }
    */

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