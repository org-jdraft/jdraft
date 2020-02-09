package org.jdraft;

import java.util.*;

import com.github.javaparser.StaticJavaParser;
import com.github.javaparser.ast.CompilationUnit;
import org.jdraft.text.Text;

/**
 * Representation of the Java source code of a package-info.java file
 *
 */
public class _packageInfo
        implements _compilationUnit<_packageInfo>, _anno._hasAnnos<_packageInfo>, _java._compound<CompilationUnit, _packageInfo> {

    public static _packageInfo of(String... pkgInfo) {
        return new _packageInfo(StaticJavaParser.parse(Text.combine(pkgInfo)));
    }

    public static _packageInfo of(CompilationUnit astCu) {
        return new _packageInfo(astCu);
    }

    public CompilationUnit astCompUnit;
    private final _javadoc.JavadocHolderAdapter javadocHolder;

    
    public _packageInfo(CompilationUnit astCu) {
        this.astCompUnit = astCu;
        this.javadocHolder = new _javadoc.JavadocHolderAdapter(astCu);
    }

    @Override
    public CompilationUnit astCompilationUnit() {
        return astCompUnit;
    }

    @Override
    public _packageInfo copy(){
        return of( this.astCompUnit.toString() );
    }
    
    @Override
    public boolean isTopLevel() {
        return true;
    }

    @Override
    public String getSimpleName(){
        return "package-info";
    }
    
    @Override
    public String getFullName(){
        String pkg = this.getPackage();
        if( pkg != null ){
            return pkg+"."+"package-info";
        }
        return "package-info";
    }
    
    @Override
    public CompilationUnit ast() {
        return astCompUnit;
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
        final _packageInfo other = (_packageInfo) obj;
        if (!Objects.equals(this.astCompUnit, other.astCompUnit)) {
            return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        return this.astCompUnit.hashCode();
    }

    /**
     *
     * @return
     */
    public String getPackage() {
        if (astCompilationUnit().getPackageDeclaration().isPresent()) {
            return astCompilationUnit().getPackageDeclaration().get().getNameAsString();
        }
        return "";
    }

    /**
     * Sets the package this TYPE is in
     *
     * @param packageName
     * @return the modified TYPE
     */
    public _packageInfo setPackage(String packageName) {
        CompilationUnit cu = astCompilationUnit();
        //TODO I need to make sure it's a valid name
        cu.setPackageDeclaration(packageName);
        return this;
    }

    /**
     * Returns true if this compilationUnit is located directly within this
     * package
     *
     * @param packageName
     * @return
     */
    public boolean isInPackage(String packageName) {
        String pn = getPackage();
        if (pn == null) {
            return packageName == null || packageName.length() == 0;
        }
        if (Objects.equals(pn, packageName)) {
            return true;
        }
        if (packageName != null) {
            return packageName.indexOf(pn) == 0;
        }
        return false;
    }

    @Override
    public _annos getAnnos() {
        if (astCompilationUnit().getPackageDeclaration().isPresent()) {
            //annos are on the packageDeclaration
            return _annos.of(astCompilationUnit().getPackageDeclaration().get());
        }
        return _annos.of(); //dont like this... but
    }

    /**
     * is the String representation equal to the _node entity (i.e. if we parse
     * the string, does it create an AST entity that is equal to the node?)
     *
     * @param stringRep the string representation of the node (parsed as an AST
     * and compared to this entity to see if equal)
     * @return true if the Parsed String represents the entity
     */
    @Override
    public boolean is(String... stringRep) {
        try {
            return is(Ast.of(stringRep));
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * Is the AST node representation equal to the underlying entity
     *
     * @param astCu the astNode to compare against
     * @return true if they represent the same _node, false otherwise
     */
    @Override
    public boolean is(CompilationUnit astCu) {
        return false;
    }

    @Override
    public String toString() {
        return this.astCompUnit.toString();
    }

    /**
     * Decompose the entity into key-VALUE pairs
     *
     * @return a map of key values
     */
    @Override
    public Map<_java.Component, Object> components() {
        Map m = new HashMap();
        m.put(_java.Component.HEADER_COMMENT, this.getHeaderComment());
        m.put(_java.Component.JAVADOC, this.javadocHolder.getJavadoc());
        m.put(_java.Component.PACKAGE, getPackage());
        m.put(_java.Component.ANNOS, getAnnos());
        m.put(_java.Component.IMPORTS, _imports.of(astCompUnit));
        return m;
    }
}
