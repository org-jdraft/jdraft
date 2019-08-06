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
 * @param <T>
 */
public interface _code<T> extends _java {

    /**
     * @return the compilationUnit (NOTE: could be null for nested _types)
     */
    public CompilationUnit astCompilationUnit();

    /**
     * A top level source code unit is the top level type, a "module-info.java"
     * file or a "package-info.java" file 
     *
     * @return
     */
    public boolean isTopLevel();

    /**
     * Gets the simple name of the _code
     * (i.e. "Map" for "java.util.Map.java")
     * (i.e. "package-info" for "aaaa.bbbb.package-info.java")
     * (i.e. "module-info" for "aaaa.bbbb.module-info.java")
     * @return 
     */
    public String getSimpleName();
    
    /**
     * returns the full name of the entity
     * for a _type, the fully qualified name (i.e. "java.util.Map" for Map.class)
     * for a package-info the fully qualified name (i.e. "my.project.package-info")
     * for a module-info (always just "module-info")
     * @return the full name
     */
    public String getFullName();
    
    /**
     * Default for getting the package name for all types
     * (should also work for module-info)
     * 
     * @return 
     */
    public default String getPackageName(){
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
    default T setHeaderComment(BlockComment astBlockComment) {
        astCompilationUnit().setComment(astBlockComment);
        return (T) this;
    }

    /**
     * Sets the header comment (i.e. the copywrite)
     *
     * @param commentLines the lines in the header comment
     * @return the modified T
     */
    default T setHeaderComment(String... commentLines) {
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
    default T removeImports(Predicate<_import> _importMatchFn) {
        getImports().remove(_importMatchFn);
        return (T) this;
    }

    /**
     * removeIn imports by classes
     *
     * @param clazzes
     * @return
     */
    default T removeImports(Class... clazzes) {
        _imports.of(astCompilationUnit()).remove(clazzes);
        return (T) this;
    }

    /**
     *
     * @param toRemove
     * @return
     */
    default T removeImports(_import... toRemove) {
        _imports.of(astCompilationUnit()).remove(toRemove);
        return (T) this;
    }

    /**
     *
     * @param _typesToRemove
     * @return
     */
    default T removeImports(_type... _typesToRemove) {
        getImports().remove(_typesToRemove);
        return (T) this;
    }

    /**
     *
     * @param toRemove the ImportDeclarations to removeIn
     * @return the modified _type
     */
    default T removeImports(ImportDeclaration... toRemove) {
        CompilationUnit cu = astCompilationUnit();
        if (cu != null) {
            for (int i = 0; i < toRemove.length; i++) {
                cu.getImports().remove(toRemove[i]);
            }
        }
        return (T) this;
    }

    /**
     *
     * @param toRemove
     * @return
     */
    default T removeImports(List<ImportDeclaration> toRemove) {
        CompilationUnit cu = astCompilationUnit();
        if (cu != null) {
            for (int i = 0; i < toRemove.size(); i++) {
                cu.getImports().remove(toRemove.get(i));
            }
        }
        return (T) this;
    }

    /**
     * Select some imports based on the astImportPredicate and apply the
     * astImportActionFn on the selected Imports
     *
     * @param _importActionFn function to apply to the imports
     * @return the T
     */
    default T forImports(Consumer<_import> _importActionFn) {
        getImports().forEach(_importActionFn);
        return (T) this;
    }

    /**
     * Select some imports based on the astImportPredicate and apply the
     * astImportActionFn on the selected Imports
     *
     * @param _importMatchFn selects the Imports to act on
     * @param _importActionFn function to apply to the imports
     * @return the T
     */
    default T forImports(Predicate<_import> _importMatchFn, Consumer<_import> _importActionFn) {
        getImports().forEach(_importMatchFn, _importActionFn);
        return (T) this;
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

    default boolean hasImports(Class... clazzes) {
        return _imports.of(astCompilationUnit()).hasImports(clazzes);
    }

    default boolean hasImport(Class clazz) {
        return _imports.of(astCompilationUnit()).hasImport(clazz);
    }

    default boolean hasImport(Predicate<_import> _importMatchFn) {
        return !listImports(_importMatchFn).isEmpty();
    }

    default boolean hasImport(_import _i) {
        return listImports(i -> i.equals(_i)).size() > 0;
    }

    default List<_import> listImports(Predicate<_import> _importMatchFn) {
        return this.getImports().list().stream().filter(_importMatchFn).collect(Collectors.toList());
    }

    /**
     * Adds static wildcard imports for all Classes
     *
     * @param wildcardStaticImports a list of classes that will WildcardImports
     * @return the T
     */
    default T importStatic(Class... wildcardStaticImports) {
        CompilationUnit cu = astCompilationUnit();
        if (cu != null) {
            Arrays.stream(wildcardStaticImports).forEach(i -> {
                ImportDeclaration id = Ast.importDeclaration(i);
                id.setAsterisk(false);
                id.setStatic(true);
                cu.addImport(id);
            });
        }
        return (T) this;
    }

    /**
     *
     * @param staticWildcardImports
     * @return
     */
    default T importStatic(String... staticWildcardImports) {
        CompilationUnit cu = astCompilationUnit();
        if (cu != null) {
            Arrays.stream(staticWildcardImports).forEach(i -> {
                ImportDeclaration id = Ast.importDeclaration(i);
                id.setStatic(true);
                id.setAsterisk(false);
                cu.addImport(id);
            });
        }
        return (T) this;
    }

    /**
     * Statically import all of the
     *
     * @param wildcardTypeStaticImport
     * @return
     */
    default T importStatic(_type... wildcardTypeStaticImport) {
        CompilationUnit cu = astCompilationUnit();
        if (cu != null) {
            Arrays.stream(wildcardTypeStaticImport).forEach(i -> {
                cu.addImport(new ImportDeclaration(i.getFullName(), true, false));
            });
        }
        return (T) this;
    }

    /**
     *
     * @param _ts
     * @return
     */
    default T imports(_type... _ts) {
        Arrays.stream(_ts).forEach(_t -> imports(_t.getFullName()));
        return (T) this;
    }

    /**
     * Add imports 
     * @param _is
     * @return 
     */
    default T imports( _import..._is){
        CompilationUnit cu = astCompilationUnit();
        if (cu != null) {
            Arrays.stream(_is).forEach(i -> cu.addImport(i.astId));
            return (T) this;
        }
        return (T) this;
    }
    
    /**
     * Add a single class import to the compilationUnit
     *
     * @param singleClass
     * @return the modified compilationUnit
     */
    default T imports(Class singleClass) {
        return imports(new Class[]{singleClass});
    }

    /**
     * Regularly import a class
     *
     * @param classesToImport
     * @return
     */
    default T imports(Class... classesToImport) {
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
            return (T) this;
        }
        throw new _draftException("No AST CompilationUnit to add imports");
    }

    /**
     * Adds these importDeclarations
     *
     * @param astImportDecls
     * @return
     */
    default T imports(ImportDeclaration... astImportDecls) {
        CompilationUnit cu = astCompilationUnit();
        if (cu != null) {
            Arrays.stream(astImportDecls).forEach(c -> cu.addImport(c));
            return (T) this;
        }
        throw new _draftException("No AST CompilationUnit of class to add imports");
    }

    default T imports(String anImport) {
        return imports(new String[]{anImport});
    }

    default T imports(String... importStatements) {
        CompilationUnit cu = astCompilationUnit();
        if (cu != null) {
            Arrays.stream(importStatements).forEach(c -> cu.addImport(Ast.importDeclaration(c)));
            return (T) this;
        }
        throw new _draftException("No AST CompilationUnit of to add imports");
    }
}
