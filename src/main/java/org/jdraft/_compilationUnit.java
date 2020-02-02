package org.jdraft;

import com.github.javaparser.ast.*;
import com.github.javaparser.ast.comments.*;

import java.util.function.*;
import java.util.*;
import java.util.stream.*;

/**
 * Common model for all top-level source code units of a Java codebase i.e. {@link CompilationUnit}
 * (a "code unit" is a model of the contents contained within a "top level source file") i.e.<UL>
 * <LI> a model (AST, etc.) of a regular XXX.java files ({@link _type})
 * <LI> a model of the contents  <B>package-info.java</B> files
 * <LI> <B>module-info.java</B> files
 * </UL>
 *
 * (i.e.{@link _type}
 * {@link _class} {@link _enum} {@link _interface} {@link _annotation},
 * {@link _packageInfo}, {@link _moduleInfo}
 *
 * @author Eric
 * @param <_C> the code implementation type
 */
public interface _compilationUnit<_C> extends _java._domain, _java._componentized {

    /**
     * Return a copy of the _code
     * @return
     */
    _compilationUnit<_C> copy();

    /**
     * Resolve the Compilation Unit that contains this _type,
     * either this TYPE is:
     * <UL>
     * <LI>a top-level class
     * <LI>a nested/member class
     * <LI>an orphan class (a class built separately without linkage to a CompilationUnit
     * (in which case this method returns a null)
     * </UL>
     * @return the top level CompilationUnit, or null if this _type is "orphaned" or a nested type
     * (created without linking to a CompilationUnit)
     */
    CompilationUnit astCompilationUnit();

    /**
     * A top level source code unit is the top level type, a "module-info.java"
     * file or a "package-info.java" file 
     *
     * @return
     */
    boolean isTopLevel();

    /**
     * Gets the simple name of the _code
     * (i.e. "Map" for "java.util.Map.java")
     * (i.e. "package-info" for "aaaa.bbbb.package-info.java")
     * (i.e. "module-info" for "aaaa.bbbb.module-info.java")
     * @return 
     */
    String getSimpleName();
    
    /**
     * returns the full name of the entity
     * for a _type, the fully qualified name (i.e. "java.util.Map" for Map.class)
     * for a package-info the fully qualified name (i.e. "my.project.package-info")
     * for a module-info (always just "module-info")
     * @return the full name
     */
    String getFullName();
    
    /**
     * Default for getting the package name for all types
     * (should also work for module-info)
     * 
     * @return 
     */
    default String getPackageName(){
        String simpleName = getSimpleName();
        String fullName = getFullName();
        if( simpleName.equals(fullName)){
            return "";
        }
        else{
            return fullName.substring(0, fullName.indexOf(simpleName) -1 );
        }
    }

    /**
     * Sets the package name
     * @param packageName
     * @return
     */
    default _C setPackage(String packageName){
        this.astCompilationUnit().setPackageDeclaration(packageName);
        return (_C)this;
    }

    /**
     * Gets the header comment (i.e. the license copyright, etc.)
     * at the top of the CompilationUnit
     * (NOTE: returns null if there are no header comments or
     * this type is not a top level type)
     *
     * @return the Comment a JavaDoc comment or BlockComment or null
     */
    default Comment getHeaderComment() {
        if (this.isTopLevel()) {
            CompilationUnit cu = astCompilationUnit();
            if (cu.getComment().isPresent()) {
                return astCompilationUnit().getComment().get();
            }
        }
        return null;
    }

    /**
     * Sets the header comment as the block comment provided
     * (Assuming the _type is a top level type... (i.e. not a nested type)
     * (this is for setting /resetting the copywrite, etc.)
     * @param astBlockComment the comment (i.e. the copyright)
     * @return the modified T
     */
    default _C setHeaderComment(BlockComment astBlockComment) {
        astCompilationUnit().setComment(astBlockComment);
        return (_C) this;
    }

    /**
     * Sets the header comment (i.e. the copywrite)
     *
     * @param commentLines the lines in the header comment
     * @return the modified T
     */
    default _C setHeaderComment(String... commentLines) {
        return setHeaderComment(Ast.blockComment(commentLines));
    }

    /**
     * gets the imports from the CompilationUnit
     *
     * @return the _imports
     */
    default _imports getImports() {
        CompilationUnit cu = astCompilationUnit();
        if (cu != null) {
            return _imports.of(cu);
        }
        return new _imports(new CompilationUnit()); //better of all the evils
    }

    /**
     * Gets the index(th) import for the compilationUnit
     * @param index
     * @return
     */
    default ImportDeclaration getImport(int index ){
        return this.astCompilationUnit().getImport(index);
    }

    /**
     *
     * @param _importMatchFn
     * @return
     */
    default ImportDeclaration getImport(Predicate<_import> _importMatchFn){
        List<_import> _is = this.listImports(_importMatchFn);
        if( _is.isEmpty()){
            return null;
        }
        return _is.get(0).ast();
    }

    /**
     * remove imports based on predicate
     *
     * @param _importMatchFn filter for deciding which imports to removeIn
     * @return the modified TYPE
     */
    default _C removeImports(Predicate<_import> _importMatchFn) {
        getImports().remove(_importMatchFn);
        return (_C) this;
    }

    /**
     * removeIn imports by classes
     *
     * @param clazzes
     * @return
     */
    default _C removeImports(Class... clazzes) {
        _imports.of(astCompilationUnit()).remove(clazzes);
        return (_C) this;
    }

    /**
     *
     * @param toRemove
     * @return
     */
    default _C removeImports(_import... toRemove) {
        _imports.of(astCompilationUnit()).remove(toRemove);
        return (_C) this;
    }

    /**
     *
     * @param _typesToRemove
     * @return
     */
    default _C removeImports(_type... _typesToRemove) {
        getImports().remove(_typesToRemove);
        return (_C) this;
    }

    /**
     *
     * @param toRemove the ImportDeclarations to removeIn
     * @return the modified _type
     */
    default _C removeImports(ImportDeclaration... toRemove) {
        CompilationUnit cu = astCompilationUnit();
        if (cu != null) {
            for (int i = 0; i < toRemove.length; i++) {
                cu.getImports().remove(toRemove[i]);
            }
        }
        return (_C) this;
    }

    /**
     *
     * @param toRemove
     * @return
     */
    default _C removeImports(List<ImportDeclaration> toRemove) {
        CompilationUnit cu = astCompilationUnit();
        if (cu != null) {
            for (int i = 0; i < toRemove.size(); i++) {
                cu.getImports().remove(toRemove.get(i));
            }
        }
        return (_C) this;
    }

    /**
     * Select some imports based on the astImportPredicate and apply the
     * astImportActionFn on the selected Imports
     *
     * @param _importActionFn function to apply to the imports
     * @return the T
     */
    default _C forImports(Consumer<_import> _importActionFn) {
        getImports().forEach(_importActionFn);
        return (_C) this;
    }

    /**
     * Select some imports based on the astImportPredicate and apply the
     * astImportActionFn on the selected Imports
     *
     * @param _importMatchFn selects the Imports to act on
     * @param _importActionFn function to apply to the imports
     * @return the _C
     */
    default _C forImports(Predicate<_import> _importMatchFn, Consumer<_import> _importActionFn) {
        getImports().forEach(_importMatchFn, _importActionFn);
        return (_C) this;
    }

    /**
     *
     * @return
     */
    default List<_import> listImports() {
        return getImports().list();
    }

    /**
     * Does this compilationUnit import (explicitly or *) import this _type
     *
     * @param _t a top level _type
     * @return true if the CompilationUnit imports this type, false otherwise
     */
    default boolean hasImport(_type _t) {
        return hasImport(_t.getFullName());
    }

    /**
     * Does this class statically import this class i.e.
     * <PRE>
     * _class _c = _class.of("A").imports("import static draft.java.Ast.*;");
     * assertTrue( _c.hasImportStatic(Ast.class));
     * </PRE>
     *
     * @param clazz
     * @return
     */
    default boolean hasImportStatic(Class clazz) {
        return !listImports(i -> i.isStatic() && i.isWildcard() && i.hasImport(clazz)).isEmpty();
    }

    /**
     * class or method name
     * <PRE>
     *
     * </PRE>
     *
     * @param className
     * @return
     */
    default boolean hasImport(String className) {
        return _imports.of(astCompilationUnit()).hasImport(className);
    }

    /**
     * Does this _code entity import all of these classes
     * @param clazzes list of classes to check if imported
     * @return true if the _code entity imports all the classes, false otherwise
     */
    default boolean hasImports(Class... clazzes) {
        return _imports.of(astCompilationUnit()).hasImports(clazzes);
    }

    /**
     *
     * @param clazz
     * @return
     */
    default boolean hasImport(Class clazz) {
        return _imports.of(astCompilationUnit()).hasImport(clazz);
    }

    /**
     * check if the _code has an import that matches this match function
     * @param _importMatchFn mathcing function for an import
     * @return true if found, false otherwise
     */
    default boolean hasImport(Predicate<_import> _importMatchFn) {
        return !listImports(_importMatchFn).isEmpty();
    }

    /**
     *
     * @param _i
     * @return
     */
    default boolean hasImport(_import _i) {
        return listImports(i -> i.equals(_i)).size() > 0;
    }

    /**
     *
     * @param _importMatchFn
     * @return
     */
    default List<_import> listImports(Predicate<_import> _importMatchFn) {
        return this.getImports().list().stream().filter(_importMatchFn).collect(Collectors.toList());
    }

    /**
     * Adds static wildcard imports for all Classes
     *
     * @param wildcardStaticImports a list of classes that will WildcardImports
     * @return the T
     */
    default _C importStatic(Class... wildcardStaticImports) {
        CompilationUnit cu = astCompilationUnit();
        if (cu != null) {
            Arrays.stream(wildcardStaticImports).forEach(i -> {
                ImportDeclaration id = Ast.importDeclaration(i);
                id.setAsterisk(true);
                id.setStatic(true);
                cu.addImport(id);
            });
        }
        return (_C) this;
    }

    /**
     *
     * @param staticWildcardImports
     * @return
     */
    default _C importStatic(String... staticWildcardImports) {
        CompilationUnit cu = astCompilationUnit();
        if (cu != null) {
            Arrays.stream(staticWildcardImports).forEach(i -> {
                ImportDeclaration id = Ast.importDeclaration(i);
                id.setStatic(true);
                cu.addImport(id);
            });
        }
        return (_C) this;
    }

    /**
     *
     * @param _ts
     * @return
     */
    default _C imports(_type... _ts) {
        Arrays.stream(_ts).forEach(_t -> imports(_t.getFullName()));
        return (_C) this;
    }

    /**
     * Add imports 
     * @param _is
     * @return 
     */
    default _C imports(_import..._is){
        CompilationUnit cu = astCompilationUnit();
        if (cu != null) {
            Arrays.stream(_is).forEach(i -> cu.addImport(i.astId));
            return (_C) this;
        }
        return (_C) this;
    }
    
    /**
     * Add a single class import to the compilationUnit
     *
     * @param singleClass
     * @return the modified compilationUnit
     */
    default _C imports(Class singleClass) {
        return imports(new Class[]{singleClass});
    }

    /**
     * Regularly import a class
     *
     * @param classesToImport
     * @return
     */
    default _C imports(Class... classesToImport) {
        CompilationUnit cu = astCompilationUnit();
        if (cu != null) {
            for (int i = 0; i < classesToImport.length; i++) {
                if (classesToImport[i].isArray()) {
                    //System.out.println("CT " + classesToImport[i].getComponentType() );
                    imports(classesToImport[i].getComponentType());
                } else {
                    //dont import primitives or primitive arrays
                    if (classesToImport[i] == null
                            || classesToImport[i].isPrimitive()
                            || classesToImport[i].isArray() && classesToImport[i].getComponentType().isPrimitive()
                            || classesToImport[i].getPackage().getName().equals("java.lang")) {                        
                    } else{
                        String cn = classesToImport[i].getCanonicalName();
                        //fix a minor bug in JavaParser API where anything in "java.lang.**.*" is not imported
                        // so java.lang.annotation.* classes are not imported when they should be
                        if (classesToImport[i].getPackage() != Integer.class.getPackage()
                                && classesToImport[i].getCanonicalName().startsWith("java.lang")) {

                            //cu.addImport(classesToImport[i].getCanonicalName());
                            //here there is some changes to JavaParser that (works in some versions not in others)
                            // so just "manually" create the importDeclaration with the name
                            cu.addImport( new ImportDeclaration(classesToImport[i].getCanonicalName(), false, false));
                        } else {
                            //System.out.println( "IMPORTING "+ cn );
                            //here there is some changes to JavaParser that (works in some versions not in others)
                            // so just "manually" create the importDeclaration with the name
                            cu.addImport( new ImportDeclaration(cn, false, false));
                            //cu.addImport(cn);
                        }
                    }
                }                
            }            
            return (_C) this;
        }
        throw new _jdraftException("No AST CompilationUnit to add imports");
    }

    /**
     * Adds these importDeclarations
     *
     * @param astImportDecls
     * @return
     */
    default _C imports(ImportDeclaration... astImportDecls) {
        CompilationUnit cu = astCompilationUnit();
        if (cu != null) {
            Arrays.stream(astImportDecls).forEach(c -> cu.addImport(c));
            return (_C) this;
        }
        throw new _jdraftException("No AST CompilationUnit of class to add imports");
    }

    /**
     * Add a single import statement based on the String
     * @param anImport
     * @return the modified T
     */
    default _C imports(String anImport) {
        return imports(new String[]{anImport});
    }

    /**
     * Add imports based on the individual Strings
     * @param importStatements a list of String representing discrete import statements
     * @return the modified T
     */
    default _C imports(String... importStatements) {
        CompilationUnit cu = astCompilationUnit();
        if (cu != null) {
            Arrays.stream(importStatements).forEach(c -> cu.addImport(Ast.importDeclaration(c)));
            return (_C) this;
        }
        throw new _jdraftException("No AST CompilationUnit of to add imports");
    }

    /**
     * An entity that can provide source code
     * (this could be reading source code from a .zip or jar file, reading the source code
     * from a Online repository, or any other mechanism for finding and returning _code models
     *
     */
    interface _provider extends _java._domain {

        /*
        <_CC extends _code> _CC first(Class<_CC> codeClass, Predicate<_CC> _codeMatchFn);
        <_CC extends _code> _CC first(Class<_CC> codeClass, Predicate<_CC> _codeMatchFn, Consumer<_CC> _codeActionFn);
        */

        /**
         *
         * @param codeClass
         * @param _codeMatchFn
         * @param _codeActionFn
         * @param <_C>
         * @return
         */
        <_C extends _compilationUnit> List<_C> for_code(Class<_C> codeClass, Predicate<_C> _codeMatchFn, Consumer<_C> _codeActionFn);

        /**
         *
         * @param codeClass
         * @param _codeActionFn
         * @param <_C>
         * @return
         */
        default <_C extends _compilationUnit> List<_C> for_code(Class<_C> codeClass, Consumer<_C> _codeActionFn){
            return for_code( codeClass, c->true, _codeActionFn);
        }

        /**
         * Perform some action on the _code entities
         * (INCLUDES _packageInfo and _moduleInfo files)
         * use {@link #for_code(Consumer)} to operate on
         * the {@link _type} files alone
         * @param _codeActionFn
         * @return
         */
        default List<_compilationUnit> for_code(Predicate<_compilationUnit> _codeMatchFn, Consumer<_compilationUnit> _codeActionFn){
            return for_code(_compilationUnit.class, _codeMatchFn, _codeActionFn);
        }

        /**
         * Perform some action on the _code entities
         * (INCLUDES _packageInfo and _moduleInfo files)
         * use {@link #for_code(Consumer)} to operate on
         * the {@link _type} files alone
         * @param _codeActionFn
         * @return
         */
        default List<_compilationUnit> for_code(Consumer<_compilationUnit> _codeActionFn ){
            return for_code(_compilationUnit.class, c->true, _codeActionFn);
        }

        /**
         *
         * @return
         */
        default List<_compilationUnit> list_code(){
            List<_compilationUnit> found = new ArrayList<>();
            for_code(t-> found.add(t));
            return found;
        }

        /**
         *
         * @return
         */
        default  <_C extends _compilationUnit> List<_C> list_code(Class<_C> _codeClass){
            List<_C> found = new ArrayList<>();
            for_code(_codeClass, t-> found.add(t));
            return found;
        }

        /**
         *
         * @return
         */
        default <_C extends _compilationUnit> List<_C> list_code(Class<_C> _codeClass, Predicate<_C>_codeMatchFn){
            List<_C> found = new ArrayList<>();
            for_code(_codeClass, _codeMatchFn, t-> found.add(t));
            return found;
        }

        default _class get_class(String name){
            List<_class> _cs = list_types(_class.class, c-> c.getFullName().equals(name));
            if(!_cs.isEmpty()){
                return _cs.get(0);
            }
            _cs = list_types(_class.class, c-> c.getName().equals(name));
            if(!_cs.isEmpty()){
                return _cs.get(0);
            }
            return null;
        }

        default _enum get_enum(String name){
            List<_enum> _es = list_types(_enum.class, e-> e.getFullName().equals(name));
            if(!_es.isEmpty()){
                return _es.get(0);
            }
            _es = list_types(_enum.class, c-> c.getName().equals(name));
            if(!_es.isEmpty()){
                return _es.get(0);
            }
            return null;
        }

        default _interface get_interface(String name){
            List<_interface> _es = list_types(_interface.class, e-> e.getFullName().equals(name));
            if(!_es.isEmpty()){
                return _es.get(0);
            }
            _es = list_types(_interface.class, c-> c.getName().equals(name));
            if(!_es.isEmpty()){
                return _es.get(0);
            }
            return null;
        }

        default _annotation get_annotation(String name){
            List<_annotation> _es = list_types(_annotation.class, e-> e.getFullName().equals(name));
            if(!_es.isEmpty()){
                return _es.get(0);
            }
            _es = list_types(_annotation.class, c-> c.getName().equals(name));
            if(!_es.isEmpty()){
                return _es.get(0);
            }
            return null;
        }

        /**
         *
         * @param _typeClass
         * @param _typeMatchFn
         * @param _typeActionFn
         * @param <_T>
         * @return
         */
        default <_T extends _type> List<_T> for_types(Class<_T> _typeClass, Predicate<_T> _typeMatchFn, Consumer<_T> _typeActionFn){
            return for_code(_typeClass, _typeMatchFn, _typeActionFn);
        }

        /**
         *
         * @param _typeClass
         * @param _typeActionFn
         * @param <_T>
         * @return
         */
        default <_T extends _type> List<_T> for_types(Class<_T> _typeClass,  Consumer<_T> _typeActionFn){
            return for_code(_typeClass, _t->true, _typeActionFn);
        }

        /**
         *
         * @param _typeActionFn
         * @return
         */
        default List<_type> for_types( Consumer<_type> _typeActionFn){
            return for_code(_type.class, _t->true, _typeActionFn);
        }

        /**
         *
         * @return
         */
        default List<_type> list_types( ){
            return list_code(_type.class, t->true);
        }

        /**
         *
         * @return
         */
        default <_T extends _type> List<_T> list_types(Class<_T> _typeClass){
            return list_code(_typeClass, t->true);
        }

        /**
         *
         * @return
         */
        default <_T extends _type> List<_T> list_types(Class<_T> _typeClass, Predicate<_T>_typeMatchFn){
            return list_code(_typeClass, _typeMatchFn);
        }
    }
}
