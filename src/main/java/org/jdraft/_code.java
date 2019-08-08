package org.jdraft;

import com.github.javaparser.ast.*;
import com.github.javaparser.ast.comments.*;
import org.jdraft._import.*;
import java.util.function.*;
import java.util.*;
import java.util.stream.*;

/**
 * Common model for all source code units of a Java codebase (a "code unit" is a
 * model of the contents contained within a source file) i.e.<UL>
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
public interface _code<_C> extends _java {

    /**
     * @return the compilationUnit (NOTE: could be null for nested _types)
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
     * Gets the "Header comment" (usually the License) from the compilationUnit
     * (NOTE: returns null if there are no header comments or this is nested
     * _code)
     *
     * @return the Comment implementation of the Header comment
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
     * sets the header comment (i.e.the license copyright, etc.) at the top of
     * the _compilationUnit
     *
     * @param astBlockComment
     * @return the Comment a JavaDoc comment or BlockComment or null
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
                id.setAsterisk(false);
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
                id.setAsterisk(false);
                cu.addImport(id);
            });
        }
        return (_C) this;
    }

    /**
     * Statically import all of the
     *
     * @param wildcardTypeStaticImport
     * @return
     */
    default _C importStatic(_type... wildcardTypeStaticImport) {
        CompilationUnit cu = astCompilationUnit();
        if (cu != null) {
            Arrays.stream(wildcardTypeStaticImport).forEach(i -> {
                cu.addImport(new ImportDeclaration(i.getFullName(), true, false));
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
                            cu.addImport(classesToImport[i].getCanonicalName());
                        } else {
                            cu.addImport(cn);
                        }
                    }
                }                
            }            
            return (_C) this;
        }
        throw new _draftException("No AST CompilationUnit to add imports");
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
        throw new _draftException("No AST CompilationUnit of class to add imports");
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
        throw new _draftException("No AST CompilationUnit of to add imports");
    }
}
