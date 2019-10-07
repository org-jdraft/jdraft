package org.jdraft;

import java.lang.reflect.Method;
import java.util.*;
import java.util.function.Consumer;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.ImportDeclaration;
import org.jdraft.macro._remove;

/**
 * Representation of a Java import declaration
 *
 * @author Eric
 */
public class _import implements _node<ImportDeclaration, _import> {

    /** return a copy of this import */
    @Override
    public _import copy(){
        return of( this.astId.toString());
    }
    
    /**
     * create a new _import over the ImportDeclaration
     * @param astId the ast ImportDeclaration
     * @return the import
     */
    public static _import of(ImportDeclaration astId) {
        return new _import(astId);
    }

    /**
     *
     * @param importDecl the string declaration of the import
     * @return 
     */
    public static _import of(String importDecl) {
        return new _import(Ast.importDeclaration(importDecl));
    }

    public static _import of(String importDecl, boolean isStatic, boolean isWildcard){
        return new _import(Ast.importDeclaration(importDecl)).setStatic(isStatic).setWildcard(isWildcard);
    }
    
    /**
     * statically import a static method
     *
     * @param method
     * @return
     */
    public static _import of(Method method) {
        return of(Ast.importDeclaration(method));
    }

    /**
     * add an import of this EXACT class
     *
     * @param clazz the class to import
     * @return
     */
    public static _import of(Class clazz) {
        return new _import(Ast.importDeclaration(clazz));
    }

    /** the underlying ast import declaration */
    public ImportDeclaration astId;

    /**
     * 
     * @return 
     */
    @Override
    public ImportDeclaration ast(){
        return astId;
    }
    
    public _import(ImportDeclaration astId) {
        this.astId = astId;
    }

    /**
     * Id this import a wildcard import (i.e. "java.util.*")
     *
     * @return true if this is a wildcard import
     */
    public boolean isWildcard() {
        return this.astId.isAsterisk();
    }

    /**
     * Make this a wildcard import
     * @return 
     */
    public _import setWildcard(){
        return setWildcard(true);
    }
    
    /**
     * Make this a wildcard import
     *
     * @param isWildcard
     * @return
     */
    public _import setWildcard(boolean isWildcard) {
        this.astId.setAsterisk(isWildcard);
        return this;
    }

    /**
     * Make this a static import
     * @return 
     */
    public _import setStatic(){
        return setStatic(true);
    }
    
    /**
     *
     * @param isStatic whether to set this import as static
     * @return
     */
    public _import setStatic(boolean isStatic) {
        this.astId.setStatic(isStatic);
        return this;
    }

    /** @return Is this a static import */
    public boolean isStatic() {
        return this.astId.isStatic();
    }

    /** @return the name (everything after "import" and before the optional ".*") */
    public String getName() {
        return this.astId.getNameAsString();
    }

    /**
     * @param method the reflective method
     * @return IS this a static import for this reflective method?
     */
    public boolean is(Method method) {
        //return !astId.isAsterisk() && astId.getNameAsString().equals(method.getDeclaringClass().getCanonicalName() + "." + method.getName());
        return is( astId, method);
    }

    public boolean is( String importDeclaration){
        try{
            return is(Ast.importDeclaration(importDeclaration));
        } catch(Exception e){
            return false;
        }
    }
    
    /**
     * 
     * @param clazz
     * @return 
     */
    public boolean is(Class clazz) {
        return is( this.astId, clazz );
    }

    /**
     * is this
     * @param id
     * @return 
     */
    @Override
    public boolean is(ImportDeclaration id) {
        return this.astId.equals(id);
    }

    public static boolean is( ImportDeclaration astId, Method method){
        return !astId.isAsterisk() && astId.getNameAsString().equals(method.getDeclaringClass().getCanonicalName() + "." + method.getName());        
    }
    
    public static boolean is( ImportDeclaration astId, Class clazz){
        return !astId.isAsterisk() && astId.getNameAsString().equals(clazz.getCanonicalName());
    }
    
    public boolean hasImport(Method method) {
        return hasImport(method.getDeclaringClass())
            || astId.isStatic() && astId.getNameAsString().equals(method.getDeclaringClass().getCanonicalName() + "." + method.getName());
    }

    public boolean hasImport( _type _t ){
        return _import.this.hasImport( _t.getFullName() );
    }
    
    public boolean hasImport( String anImport){
        if (astId.isAsterisk()) {
            if (anImport.startsWith(astId.getNameAsString())) {
                String ln = anImport.substring(astId.getNameAsString().length() + 1);
                return !ln.contains(".");
            }
            return false;
        }
        return astId.getNameAsString().equals(anImport);
    }
    
    /**
     * Is this class imported (either specifically or using a wildcard)
     *
     * @param clazz
     * @return
     */
    public boolean hasImport(Class clazz) {
        return _import.hasImport( astId, clazz );        
    }

    private static boolean hasImport(ImportDeclaration astId, String importClass) {
        ImportDeclaration testAstId = null;
        try{
            testAstId = Ast.importDeclaration(importClass);
        }
        catch(Exception e ){
            return false;
        }
        if( astId.equals(testAstId) ){
            return true;
        }
        if( astId.getNameAsString().equals(testAstId.getNameAsString()) ){
            return true;
        }
        if (astId.isAsterisk()) {
            if (importClass.startsWith(astId.getNameAsString())) {
                String ln = importClass.substring(astId.getNameAsString().length() + 1);
                return !ln.contains(".");
            }            
        }
        return false;
    }
    
    private static boolean hasImport(ImportDeclaration astId, Class clazz) {
        if( astId.getNameAsString().equals(clazz.getCanonicalName())){
            return true;
        }
        if (astId.isAsterisk()) {
            if( clazz.getCanonicalName().startsWith(astId.getNameAsString())) {
                String ln = clazz.getCanonicalName().substring(astId.getNameAsString().length() + 1);
                return !ln.contains(".");
            }            
        }
        return false;
    }
    
    private static boolean hasImport(ImportDeclaration astId, Method method) {
        return _import.hasImport( astId, method.getDeclaringClass()) ||
            astId.isStatic() && astId.getNameAsString().equals(
               method.getDeclaringClass().getCanonicalName() + "." + method.getName());        
    }
    
    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final _import other = (_import) obj;
        if (!Objects.equals(this.astId, other.astId)) {
            return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        return this.astId.hashCode();
    }
    
    @Override
    public String toString(){
        return this.astId.toString();
    }

    @Override
    public boolean is(String... stringRep) {
        try{
            return of( Text.combine(stringRep) ).equals(this);
        } catch(Exception e){
            return false;
        }
    }

    @Override
    public Map<_java.Component, Object> components() {
        Map<_java.Component,Object>components = new HashMap<>();
        components.put(_java.Component.NAME, this.astId.getNameAsString());
        components.put(_java.Component.STATIC, this.astId.isStatic());
        components.put(_java.Component.WILDCARD, this.astId.isAsterisk());
        return components;
    }

    /**
     * mutable grouping of ImportDeclarations applied to an Ast CompilationUnit
     * 
     */
    public static class _imports implements _model {

        public static _imports of( CompilationUnit cu ){
            return new _imports(cu);
        }
        
        public CompilationUnit astCompilationUnit;

        public _imports( CompilationUnit astCu ){
            this.astCompilationUnit = astCu;
        }
        
        /** returns the number of imports */
        public int size(){
            return this.astCompilationUnit.getImports().size();
        }

        public boolean isEmpty(){
            return size() == 0;
        }
        
        /**
         * Top level Classes in the same package are "implied imports"
         * @param clazz the class to check
         * @return true if this import is implied
         */
        public boolean isImpliedImport(Class clazz){
            return isImpliedImport(clazz.getCanonicalName());
        }
        
        /**
         * 
         * @param _t
         * @return 
         */
        public boolean isImpliedImport( _type _t ){
            return _t.isTopLevel() && isImpliedImport( _t.getFullName() );
        }
        
        /**
         * IF the 
         * @param className the name of the class
         * @return 
         */
        public boolean isImpliedImport(String className){
            if( this.astCompilationUnit == null ){
                return false;
            }
            if(astCompilationUnit.getPackageDeclaration().isPresent()){
               
               String pkgName = astCompilationUnit.getPackageDeclaration().get().getNameAsString();               
               if( className.startsWith(pkgName) ){                   
                   String left = className.substring(pkgName.length() + 1 );                   
                   return !left.contains(".");
               }
               return false;
            }
            //its in the base package, so anything in the base package is fair game
            return !className.contains(".");
        }
        
        public boolean hasImport(Class clazz) {
            return isImpliedImport(clazz) ||
                (astCompilationUnit != null &&
                        astCompilationUnit.getImports().stream().anyMatch(i -> {
                        return _import.hasImport(i, clazz);
                    }));
        }

        public boolean hasImport(String className) {
            return isImpliedImport(className) ||
                (astCompilationUnit != null &&
                        astCompilationUnit.getImports().stream().anyMatch(i -> {
                            return _import.hasImport(i, className);
                        }));
        }        
        
        public boolean hasImports( Class...classes ){
            return Arrays.stream(classes).allMatch(i-> _imports.this.hasImport(i) );
        }
        
        public boolean hasImports( String...imports  ){
            return Arrays.stream(imports).allMatch(i-> _imports.this.hasImport(i) );
        }
        
        public boolean hasImports( _type..._ts ){
            return Arrays.stream(_ts).allMatch( i-> hasImport(i) );
        }

        public ImportDeclaration get( int index ){
            return this.astCompilationUnit.getImport(index);
        }

        public boolean hasImport(Method m){
            if( this.astCompilationUnit == null ){
                return false;
            }
            return astCompilationUnit.getImports().stream().anyMatch(i -> {
                return _import.hasImport(i, m);
            });
        }
        
        public boolean hasImport( _type _t){
            return _imports.this.hasImport( _t.getFullName() );
        }
        
        public List<_import> list(){
            List<_import> l = new ArrayList<>();
            if( this.astCompilationUnit == null ){
                return l;
            }
            astCompilationUnit.getImports().forEach(i -> l.add( _import.of(i)));
            return l;
        }
        
        public List<_import> list( Predicate<_import> _importMatchFn ){
            return list().stream().filter(_importMatchFn).collect(Collectors.toList());
        }
        
        public _imports forEach( Predicate<_import> _importMatchFn, Consumer<_import> _importConsumer ){
            list(_importMatchFn).forEach(_importConsumer);
            return this;
        }
        
        public _imports forEach( Consumer<_import> _importConsumer ){
            list().forEach(_importConsumer);
            return this;
        }
        
        public _imports clear(){
            this.astCompilationUnit.getImports().clear();
            return this;
        }
        
        public _imports remove( _type _t){
            return remove( _t.getFullName() );
        }
        
        public _imports remove( _type... _ts){
            Arrays.stream(_ts).forEach(t-> remove(t));
            return this;            
        }
        
        public _imports remove( _import...imports ){
            Arrays.stream(imports).forEach(i -> remove(i));
            return this;
        }
        
        public List<_import> remove( Predicate<_import> importMatchFn ){
            List<_import> toRemove = list( importMatchFn );
            toRemove.forEach( r -> remove(r));
            return toRemove;
        }

        public _imports remove(_import toRemove){
            return remove( toRemove.astId );
        }
        
        public _imports remove(ImportDeclaration toRemove){
            if( this.astCompilationUnit == null ){
                return this;
            }
            this.astCompilationUnit.getImports().remove(toRemove);
            return this;
        }
        
        public _imports remove( Class clazz ){
            if( this.astCompilationUnit == null ){
                return this;
            }
            List<ImportDeclaration> toRemove = new ArrayList<>();
            this.astCompilationUnit.getImports().forEach(i -> {
                if( _import.is(i, clazz) ) {
                    toRemove.add( i );
                }
            });
            toRemove.forEach(i -> this.astCompilationUnit.getImports().remove(i));
            return this;
        }
        
        public _imports remove( String importDeclaration ){
            try{
                return remove(Ast.importDeclaration(importDeclaration));
            }catch(Exception e){
                return this;
            }            
        }
        
        public _imports remove( String...importDeclarations ){
            Arrays.stream(importDeclarations).forEach( i -> remove(i) );
            return this;
        }
        
        /**
         * removeIn imports by classes
         * @param clazzes
         * @return
         */
        public _imports remove( Class...clazzes ){
            Arrays.stream( clazzes ).forEach( r -> remove( r) );            
            return this;
        }
        
        /**
         *
         * @param toRemove the ImportDeclarations to removeIn
         * @return the modified _type
         */
        public _imports remove( ImportDeclaration...toRemove ){
            if( this.astCompilationUnit == null ){
                return this;
            }            
            for(int i=0;i<toRemove.length;i++){
                this.astCompilationUnit.getImports().remove( toRemove[i] );
            }            
            return this;
        }
        
        @Override
        public int hashCode(){
            Set<Integer> is = new HashSet<>();
            if( this.astCompilationUnit == null ){
                return 0;
            }
            this.astCompilationUnit.getImports().forEach( i -> is.add( i.hashCode()));
            return is.hashCode();
        }

        @Override
        public boolean equals(Object obj) {
            if( this.astCompilationUnit == null ){
                return false;
            }
            if (this == obj) {
                return true;
            }
            if (obj == null) {
                return false;
            }
            if (getClass() != obj.getClass()) {
                return false;
            }
            final _imports other = (_imports) obj;
            Set<ImportDeclaration> t = new HashSet<>();            
            Set<ImportDeclaration> o = new HashSet<>();
            
            this.astCompilationUnit.getImports().forEach( i -> t.add( i ));
            other.astCompilationUnit.getImports().forEach( i -> o.add( i ));
            
            return Objects.equals(t, o);                
        }        
        
        @Override
        public String toString(){
            StringBuilder sb = new StringBuilder();
            this.astCompilationUnit.getImports().forEach(i -> sb.append( i.toString() ) );
            return sb.toString();
        }
    }

    /**
     * 
     * When we initialize or "import" the body of code for a _type... i.e.
     * <PRE>
     * //create the _class from the name, and anonymous object body
     * _class _c = _class.of("aaaa.bbbb.C", new Serializable(){
     *      Map m;
     *
     *      public UUID id(List l) throws IOException {
     *          return UUID.randomUUID();
     *      }
     * });
     *
     * there are "inferred imports" that we should use for the _class... in the above case
     * we need to import:
     * <UL>
     *     <LI>java.io.Serializable : the interface the anonymous object implements</LI>
     *     <LI>java.util.Map : the type of field m</LI>
     *     <LI>java.util.UUID : the return type of method id</LI>
     *     <LI>java.util.List : the parameter passed into method id</LI>
     *     <LI>java.io.IOException : the exception type throws from method id</LI>
     * </UL>
     * To do this... we reflectively look at the {@link java.lang.reflect.Field}s,
     * {@link java.lang.reflect.Method}s, and {@link java.lang.reflect.Constructor}s,
     * that exist in the anonymous object body, (as well as the declared interfaces and extended classes)
     * and create a Set of classes that represent these runtime Classes (to add via _import)
     *    
     * TODO: ???? What about RuntimeAnnotations? (on each type, method, parameter, etc.
     * should I include the imports for these things?? and when... 
     * for right now NO, but possible in the future
     * 
     * </PRE>
     * @param anonymousObjectBody an anonymous Object
     * @return a Set<Class> containing the surface level classes that should be automatically
     * inferred to be imported
     */
    public static Set<Class>inferImportsFrom(Object anonymousObjectBody){
        return inferImportsFrom(anonymousObjectBody.getClass());
    }

    /**
     * 
     * @param anonymousClass
     * @return a Set of Class imports that should be imported
     */
    public static Set<Class>inferImportsFrom(Class anonymousClass){
        Set<Class>classes = new HashSet<>();
        //implemented interfaces
        Arrays.stream(anonymousClass.getInterfaces()).forEach(c-> classes.add(c));

        //super class if not Object
        if( anonymousClass.getSuperclass() != null &&
            !anonymousClass.getSuperclass().equals(Object.class )){
            classes.add(anonymousClass.getSuperclass() );
        }
        // field types
        Arrays.stream(anonymousClass.getDeclaredFields()).forEach( f-> {
            if( !f.isSynthetic() && f.getAnnotation(_remove.class) == null ){
                classes.add(f.getType());
            }
        } );
        //constructors
        Arrays.stream(anonymousClass.getDeclaredConstructors()).forEach( c -> {
            if( c.getAnnotation(_remove.class) == null ) {
                Arrays.stream(c.getParameters()).forEach( p -> {
                    if( p.isSynthetic() ){
                        classes.add( p.getType() );
                    }
                } );
                Arrays.stream(c.getExceptionTypes()).forEach(p -> classes.add(p));
            }
        });
        //methods
        Arrays.stream(anonymousClass.getDeclaredMethods()).forEach( m -> {
            if( m.getAnnotation(_remove.class) == null ) {
                classes.add(m.getReturnType());

                Arrays.stream(m.getParameters()).forEach( p -> {
                    if( !p.isSynthetic() ){
                        classes.add( p.getType() );
                    }
                } );
                Arrays.stream(m.getExceptionTypes()).forEach(p -> classes.add(p));
            }
        });
        //nested declared classes
        Arrays.stream(anonymousClass.getDeclaredClasses()).forEach( c -> {
            if( c.getAnnotationsByType(_remove.class).length == 0 ) {
                classes.addAll(inferImportsFrom(c));
            }
        });
        //lets not try importing java.lang.String or int.class
        classes.removeIf( c-> c.isPrimitive() || ( c.isArray() && c.getComponentType().isPrimitive() ) 
                || (c.getCanonicalName() == null)
                || ( c.getPackage() != null && c.getPackage().getName().equals("java.lang") ));
        return classes;
    }
}
