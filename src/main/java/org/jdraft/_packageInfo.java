package org.jdraft;

import java.util.*;
import java.util.function.Function;

import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.expr.Name;
import org.jdraft.text.Text;

/**
 * Representation of the Java source code of a package-info.java file
 *
 */
public final class _packageInfo
        implements _codeUnit<_packageInfo>, _annoExprs._withAnnoExprs<_packageInfo>, _java._node<CompilationUnit, _packageInfo> {

    public static final Function<String, _packageInfo> PARSER = s-> _packageInfo.of(s);

    public static _packageInfo of(String... pkgInfo) {
        return new _packageInfo(Ast.of(Text.combine(pkgInfo)));
    }

    public static _packageInfo of(CompilationUnit astCu) {
        return new _packageInfo(astCu);
    }

    public static _feature._one<_packageInfo, _annoExprs> ANNOS = new _feature._one<>(_packageInfo.class, _annoExprs.class,
            _feature._id.ANNOS,
            a -> a.getAnnoExprs(),
            (_packageInfo a, _annoExprs o) -> a.setAnnoExprs(o), PARSER);

    public static _feature._one<_packageInfo, _package> PACKAGE = new _feature._one<>(_packageInfo.class, _package.class,
            _feature._id.PACKAGE,
            a -> a.getPackage(),
            (_packageInfo a, _package _a) -> a.setPackage(_a), PARSER);

    public static _feature._one<_packageInfo, _imports> IMPORTS = new _feature._one<>(_packageInfo.class, _imports.class,
            _feature._id.IMPORTS,
            a -> a.getImports(),
            (_packageInfo a, _imports b) -> a.setImports(b), PARSER);


    public static _feature._meta<_packageInfo> META = _feature._meta.of(_packageInfo.class, PACKAGE, ANNOS, IMPORTS );

    public CompilationUnit astCompUnit;


    public _packageInfo(CompilationUnit astCu) {
        this.astCompUnit = astCu;
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
        String pkg = this.getPackageName();
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

    public String getPackage() {
        if (astCompilationUnit().getPackageDeclaration().isPresent()) {
            return astCompilationUnit().getPackageDeclaration().get().getNameAsString();
        }
        return "";
    }
    */

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
        String pn = getPackageName();
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
    public _annoExprs getAnnoExprs() {
        if (astCompilationUnit().getPackageDeclaration().isPresent()) {
            //annos are on the packageDeclaration
            return _annoExprs.of(astCompilationUnit().getPackageDeclaration().get());
        }
        return _annoExprs.of(); //dont like this... but
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
    public Map<_java.Feature, Object> features() {
        Map m = new HashMap();
        m.put(_java.Feature.HEADER_COMMENT, this.getHeaderComment());
        //m.put(_java.Component.JAVADOC, this.javadocHolder.getJavadoc());
        m.put(_java.Feature.PACKAGE, getPackage());
        m.put(_java.Feature.ANNO_EXPRS, getAnnoExprs());
        m.put(_java.Feature.IMPORTS, _imports.of(astCompUnit));
        return m;
    }
}
