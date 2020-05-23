package org.jdraft;

import com.github.javaparser.JavaParser;
import com.github.javaparser.ast.*;
import com.github.javaparser.ast.body.TypeDeclaration;
import com.github.javaparser.ast.comments.*;
import org.jdraft.io._io;
import org.jdraft.io._ioException;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.net.URL;
import java.nio.file.Path;
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
 * @param <_CU> the code implementation type
 */
public interface _codeUnit<_CU> extends _java._domain {

    /**
     * Read and return a _code from the .java source code file at
     * javaSourceFilePath
     *
     * @param javaSourceFilePath the path to the local Java source code
     * @return the _code instance
     */
    static _codeUnit of(Path javaSourceFilePath) throws _jdraftException {
        return of( Ast.JAVAPARSER, javaSourceFilePath);
    }

    static _codeUnit of(JavaParser javaParser, Path javaSourceFilePath) throws _jdraftException {
        return of(Ast.of(javaParser, javaSourceFilePath));
    }

    static _codeUnit of(URL url) {
        return of( Ast.JAVAPARSER, url);
    }

    static _codeUnit of(JavaParser javaParser, URL url) {
        try {
            InputStream inStream = url.openStream();
            return of(javaParser, inStream);
        }catch(IOException ioe){
            throw new _ioException("invalid input url \""+url.toString()+"\"", ioe);
        }
    }

    /**
     * Read and return the appropriate _code model based on the .java source
     * within the javaSourceInputStream
     *
     * @param javaSourceInputStream
     * @return
     */
    static _codeUnit of(InputStream javaSourceInputStream) throws _jdraftException {
        return of(Ast.JAVAPARSER, javaSourceInputStream);
    }

    static _codeUnit of(JavaParser javaParser, InputStream javaSourceInputStream) throws _jdraftException {
        return of(Ast.of(javaParser, javaSourceInputStream));
    }

    /**
     * Build the _codeUnit from the string and return it
     * could be:
     * {@link _packageInfo}
     * {@link _moduleInfo}
     * {@link _type}
     *     {@link _annotation}
     *     {@link _enum}
     *     {@link _class}
     *     {@link _interface}
     *
     * @param contents the String contents of the _codeUnit
     * @return the codeUnit
     * @throws _jdraftException if we cannot parse the String representing the contents
     */
    static _codeUnit of(String...contents) throws _jdraftException {
        return of(Ast.JAVAPARSER, contents);
    }

    /**
     *
     * @param javaParser
     * @param contents
     * @return
     * @throws _jdraftException
     */
    static _codeUnit of(JavaParser javaParser, String...contents) throws _jdraftException {
        return of( Ast.of(javaParser, contents));
    }

    /**
     * Read and return the appropriate {@link _codeUnit} based on the .java source
     * within the javaSourceFile
     *
     * @param javaSourceFile
     * @return
     * @throws _jdraftException
     */
    static _codeUnit of(File javaSourceFile) throws _jdraftException {
        return of(Ast.of(javaSourceFile));
    }

    /**
     * build and return the _code wrapper to encapsulate the AST representation
     * of the .java source code stored in the javaSourceReader
     *
     * @param javaSourceReader reader containing .java source code
     * @return the _code model instance representing the source
     */
    static _codeUnit of(Reader javaSourceReader) throws _jdraftException {
        return of(Ast.of(javaSourceReader));
    }

    /**
     * build the appropriate draft wrapper object to encapsulate the AST
     * compilationUnit
     *
     * @param astRoot the AST
     * @return a _code wrapper implementation that wraps the AST
     */
    static _codeUnit of(CompilationUnit astRoot) {
        if (astRoot.getModule().isPresent()) {
            return _moduleInfo.of(astRoot);
        }
        if (astRoot.getTypes().isEmpty()) {
            return _packageInfo.of(astRoot);
        }
        if (astRoot.getTypes().size() == 1) { //only one type
            return _type.of(astRoot, astRoot.getTypes().get(0));
        }
        //the first public type
        Optional<TypeDeclaration<?>> otd
                = astRoot.getTypes().stream().filter(t -> t.isPublic()).findFirst();
        if (otd.isPresent()) {
            return _type.of(astRoot, otd.get());
        }
        //the primary type
        if (astRoot.getPrimaryType().isPresent()) {
            return _type.of(astRoot, astRoot.getPrimaryType().get());
        }
        return _type.of(astRoot, astRoot.getType(0));
    }

    /**
     * Return a copy of the _code
     * @return
     */
    _codeUnit<_CU> copy();

    /**
     * Returns the origin of the compilationUnit (i.e. where the source java files originated from)
     * (could be null) this is an extension of the {@link CompilationUnit.Storage} which only allows
     * the storage to be a path on the local file system
     *
     * @return the _origin of the _codeUnit or null if not set / undefined
     * @see com.github.javaparser.ast.CompilationUnit#setData
     * @see com.github.javaparser.ast.CompilationUnit#getData
     */
    default _io._origin getOrigin(){
        return astCompilationUnit().getData(_io.ORIGIN_KEY);
    }

    /**
     * gets the parent project for this codeUnit if one is defined...
     * think about all these ASTs that exist in a project as separate, but they can
     * be (indirectly attached to each other via a _project (forest) of ASTs
     *
     * @return the parent _project for this _codeUnit or null if not set
     * @see _project
     * @see com.github.javaparser.ast.CompilationUnit#getData
     */
    default _project getParentProject(){
        return astCompilationUnit().getData(_project.PARENT_PROJECT_KEY);
    }

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
     * Determines the package this class is in
     * @return
     */
    default _package getPackage(){
        CompilationUnit cu = astCompilationUnit();
        if( cu == null ){
            return null;
        }
        if( cu.getPackageDeclaration().isPresent() ){
            return _package.of(cu.getPackageDeclaration( ).get());
        }
        return null;
    }

    /**
     * Default for getting the package name for all types
     * (should also work for module-info)
     * 
     * @return 
     */
    default String getPackageName(){
        CompilationUnit cu = astCompilationUnit();
        if( cu == null ){
            return null;
        }
        if( cu.getPackageDeclaration().isPresent() ){
            return cu.getPackageDeclaration( ).get().getNameAsString();
        }
        return null;
    }

    /**
     * Sets the package name
     * @param _pkg
     * @return
     */
    default _CU setPackage(_package _pkg){
        this.astCompilationUnit().setPackageDeclaration(_pkg.astPackage);
        return (_CU)this;
    }

    /**
     * Sets the package name
     * @param packageName
     * @return
     */
    default _CU setPackage(String packageName){
        this.astCompilationUnit().setPackageDeclaration(packageName);
        return (_CU)this;
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
    default _CU setHeaderComment(BlockComment astBlockComment) {
        astCompilationUnit().setComment(astBlockComment);
        return (_CU) this;
    }

    /**
     * Sets the header comment (i.e. the copywrite)
     *
     * @param commentLines the lines in the header comment
     * @return the modified T
     */
    default _CU setHeaderComment(String... commentLines) {
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
    default _import getImport(int index ){
        return _import.of(this.astCompilationUnit().getImport(index));
    }

    /**
     *
     * @param _importMatchFn
     * @return
     */
    default _import getImport(Predicate<_import> _importMatchFn){
        List<_import> _is = this.listImports(_importMatchFn);
        if( _is.isEmpty()){
            return null;
        }
        return _is.get(0);
    }

    default _CU setImports(NodeList<ImportDeclaration> id){
        this.astCompilationUnit().getImports().clear();
        id.forEach(i -> this.astCompilationUnit().getImports().add(i));
        return (_CU)this;
    }

    default _CU setImports(_imports _is){
        this.astCompilationUnit().getImports().clear();
        _is.forEach(_i -> this.astCompilationUnit().getImports().add(_i.ast()));
        return (_CU)this;
    }

    /**
     * remove imports based on predicate
     *
     * @param _importMatchFn filter for deciding which imports to removeIn
     * @return the modified TYPE
     */
    default _CU removeImports(Predicate<_import> _importMatchFn) {
        getImports().remove(_importMatchFn);
        return (_CU) this;
    }

    /**
     * removeIn imports by classes
     *
     * @param clazzes
     * @return
     */
    default _CU removeImports(Class... clazzes) {
        _imports.of(astCompilationUnit()).remove(clazzes);
        return (_CU) this;
    }

    /**
     *
     * @param toRemove
     * @return
     */
    default _CU removeImports(_import... toRemove) {
        _imports.of(astCompilationUnit()).remove(toRemove);
        return (_CU) this;
    }

    /**
     *
     * @param _importTypesToRemove
     * @return
     */
    default _CU removeImports(_type... _importTypesToRemove) {
        getImports().remove(_importTypesToRemove);
        return (_CU) this;
    }

    /**
     *
     * @param toRemove the ImportDeclarations to removeIn
     * @return the modified _type
     */
    default _CU removeImports(ImportDeclaration... toRemove) {
        CompilationUnit cu = astCompilationUnit();
        if (cu != null) {
            for (int i = 0; i < toRemove.length; i++) {
                cu.getImports().remove(toRemove[i]);
            }
        }
        return (_CU) this;
    }

    /**
     *
     * @param toRemove
     * @return
     */
    default _CU removeImports(List<ImportDeclaration> toRemove) {
        CompilationUnit cu = astCompilationUnit();
        if (cu != null) {
            for (int i = 0; i < toRemove.size(); i++) {
                cu.getImports().remove(toRemove.get(i));
            }
        }
        return (_CU) this;
    }

    /**
     * Select some imports based on the astImportPredicate and apply the
     * astImportActionFn on the selected Imports
     *
     * @param _importActionFn function to apply to the imports
     * @return the T
     */
    default _CU forImports(Consumer<_import> _importActionFn) {
        getImports().forEach(_importActionFn);
        return (_CU) this;
    }

    /**
     * Select some imports based on the astImportPredicate and apply the
     * astImportActionFn on the selected Imports
     *
     * @param _importMatchFn selects the Imports to act on
     * @param _importActionFn function to apply to the imports
     * @return the _C
     */
    default _CU forImports(Predicate<_import> _importMatchFn, Consumer<_import> _importActionFn) {
        getImports().forEach(_importMatchFn, _importActionFn);
        return (_CU) this;
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
    default _CU addImportStatic(Class... wildcardStaticImports) {
        CompilationUnit cu = astCompilationUnit();
        if (cu != null) {
            Arrays.stream(wildcardStaticImports).forEach(i -> {
                ImportDeclaration id = Ast.importDeclaration(i);
                id.setAsterisk(true);
                id.setStatic(true);
                cu.addImport(id);
            });
        }
        return (_CU) this;
    }

    /**
     *
     * @param staticWildcardImports
     * @return
     */
    default _CU addImportStatic(String... staticWildcardImports) {
        CompilationUnit cu = astCompilationUnit();
        if (cu != null) {
            Arrays.stream(staticWildcardImports).forEach(i -> {
                ImportDeclaration id = Ast.importDeclaration(i);
                id.setStatic(true);
                cu.addImport(id);
            });
        }
        return (_CU) this;
    }

    /**
     *
     * @param _ts
     * @return
     */
    default _CU addImports(_type... _ts) {
        Arrays.stream(_ts).forEach(_t -> addImports(_t.getFullName()));
        return (_CU) this;
    }

    /**
     * Add imports 
     * @param _is
     * @return 
     */
    default _CU addImports(_import..._is){
        CompilationUnit cu = astCompilationUnit();
        if (cu != null) {
            Arrays.stream(_is).forEach(i -> cu.addImport(i.astId));
            return (_CU) this;
        }
        return (_CU) this;
    }
    
    /**
     * Add a single class import to the compilationUnit
     *
     * @param singleClass
     * @return the modified compilationUnit
     */
    default _CU addImports(Class singleClass) {
        return addImports(new Class[]{singleClass});
    }

    /**
     * Regularly import a class
     *
     * @param classesToImport
     * @return
     */
    default _CU addImports(Class... classesToImport) {
        CompilationUnit cu = astCompilationUnit();
        if (cu != null) {
            for (int i = 0; i < classesToImport.length; i++) {
                if (classesToImport[i].isArray()) {
                    //System.out.println("CT " + classesToImport[i].getComponentType() );
                    addImports(classesToImport[i].getComponentType());
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
            return (_CU) this;
        }
        throw new _jdraftException("No AST CompilationUnit to add imports");
    }

    /**
     * Adds these importDeclarations
     *
     * @param astImportDecls
     * @return
     */
    default _CU addImports(ImportDeclaration... astImportDecls) {
        CompilationUnit cu = astCompilationUnit();
        if (cu != null) {
            Arrays.stream(astImportDecls).forEach(c -> cu.addImport(c));
            return (_CU) this;
        }
        throw new _jdraftException("No AST CompilationUnit of class to add imports");
    }

    /**
     * Add a single import statement based on the String
     * @param anImport
     * @return the modified T
     */
    default _CU addImports(String anImport) {
        return addImports(new String[]{anImport});
    }

    /**
     * Add imports based on the individual Strings
     * @param importStatements a list of String representing discrete import statements
     * @return the modified T
     */
    default _CU addImports(String... importStatements) {
        CompilationUnit cu = astCompilationUnit();
        if (cu != null) {
            Arrays.stream(importStatements).forEach(c -> cu.addImport(Ast.importDeclaration(c)));
            return (_CU) this;
        }
        throw new _jdraftException("No AST CompilationUnit of to add imports");
    }
}
