package org.jdraft;

import java.util.*;
import java.util.function.Function;

import com.github.javaparser.ast.CompilationUnit;
import org.jdraft.text.Stencil;
import org.jdraft.text.Text;

/**
 * Representation of the Java source code of a package-info.java file
 *
 */
public final class _packageInfo
        implements _codeUnit<_packageInfo>, _annos._withAnnos<_packageInfo>, _tree._node<CompilationUnit, _packageInfo> {

    public static final Function<String, _packageInfo> PARSER = s-> _packageInfo.of(s);

    public static _packageInfo of(String... pkgInfo) {
        return new _packageInfo(Ast.of(Text.combine(pkgInfo)));
    }

    public static _packageInfo of(CompilationUnit astCu) {
        return new _packageInfo(astCu);
    }

    public static _feature._one<_packageInfo, _annos> ANNOS = new _feature._one<>(_packageInfo.class, _annos.class,
            _feature._id.ANNOS,
            a -> a.getAnnos(),
            (_packageInfo a, _annos o) -> a.setAnnos(o), PARSER);

    public static _feature._one<_packageInfo, _package> PACKAGE = new _feature._one<>(_packageInfo.class, _package.class,
            _feature._id.PACKAGE,
            a -> a.getPackage(),
            (_packageInfo a, _package _a) -> a.setPackage(_a), PARSER);

    public static _feature._one<_packageInfo, _imports> IMPORTS = new _feature._one<>(_packageInfo.class, _imports.class,
            _feature._id.IMPORTS,
            a -> a.getImports(),
            (_packageInfo a, _imports b) -> a.setImports(b), PARSER);

    public static _feature._features<_packageInfo> FEATURES = _feature._features.of(_packageInfo.class, PARSER, PACKAGE, ANNOS, IMPORTS );

    public CompilationUnit node;

    public _packageInfo(CompilationUnit astCu) {
        this.node = astCu;
    }

    public _feature._features<_packageInfo> features(){
        return FEATURES;
    }

    @Override
    public CompilationUnit astCompilationUnit() {
        return node;
    }

    @Override
    public _packageInfo copy(){
        return of( this.node.toString() );
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
    public CompilationUnit node() {
        return node;
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
        if (!Objects.equals(this.node, other.node)) {
            return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        return this.node.hashCode();
    }

    /**
     * Replace the underlying node within the AST (if this node has a parent)
     * and return this (now pointing to the new node)
     * @param replaceNode the node instance to swap in for the old node that this facade was pointing to
     * @return the modified this (now pointing to the replaceNode which was swapped into the AST)
     */
    public _packageInfo replace(CompilationUnit replaceNode){
        this.node.replace(replaceNode);
        this.node = replaceNode;
        return this;
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
        String str = Text.combine(stringRep);
        if( str.startsWith("$") && str.endsWith("$")){
            Stencil st = Stencil.of(str);
            if( st.isMatchAny() ){
                return true;
            }
        }
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
        return this.node.toString();
    }

}
