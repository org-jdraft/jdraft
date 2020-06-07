package org.jdraft;

import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.Node;
import com.github.javaparser.ast.modules.ModuleDeclaration;
import com.github.javaparser.ast.modules.ModuleDirective;
import org.jdraft.text.Text;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * "module-info.java" file describing the module dependencies
 * 
 */
public final class _moduleInfo
        implements _codeUnit<_moduleInfo>, _java._node<CompilationUnit, _moduleInfo>,
        _java._withName<_moduleInfo>,
        _java._withComments<CompilationUnit, _moduleInfo> {

    public static final Function<String, _moduleInfo> PARSER = s-> _moduleInfo.of(s);

    public CompilationUnit astCompUnit;

    @Override
    public CompilationUnit astCompilationUnit() {
        return astCompUnit;
    }

    @Override
    public _moduleInfo copy(){
        return of( this.astCompUnit.toString() );
    }

    public _feature._features<_moduleInfo> features(){
        return FEATURES;
    }

    /**
     * is the String representation equal to the _node entity (i.e. if we parse
     * the string, does it create an AST entity that is equal to the node?)
     *
     * @param stringRep the string representation of the node (parsed as an AST
     * and compared to this entity to see if equal)
     * @return true if the Parsed String represents the entity

    @Override
    public boolean is(String... stringRep) {
        try {
            return is(Ast.of(stringRep));
        } catch (_jdraftException e) {
            return false;
        }
    }
     */

    @Override
    public String getSimpleName(){
        return "module-info";
    }
    
    @Override
    public String getFullName(){
        return "module-info";
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
        final _moduleInfo other = (_moduleInfo) obj;
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
     * Is the AST node representation equal to the underlying entity
     *
     * @param astCu the astNode to compare against
     * @return true if they represent the same _node, false otherwise
     */
    @Override
    public boolean is(CompilationUnit astCu) {
        return false;
    }

    public static _feature._one<_moduleInfo, _imports> IMPORTS = new _feature._one<>(_moduleInfo.class, _imports.class,
            _feature._id.IMPORTS,
            a -> a.getImports(),
            (_moduleInfo a, _imports b) -> a.setImports(b), PARSER);

    public static _feature._one<_moduleInfo, Boolean> IS_OPEN = new _feature._one<>(_moduleInfo.class, Boolean.class,
            _feature._id.IS_OPEN,
            a -> a.isOpen(),
            (_moduleInfo a, Boolean b) -> a.setOpen(b), PARSER);

    public static _feature._one<_moduleInfo, String> MODULE_NAME = new _feature._one<>(_moduleInfo.class, String.class,
            _feature._id.MODULE_NAME,
            a -> a.getName(),
            (_moduleInfo a, String s) -> a.setName(s), PARSER);

    public static _feature._many<_moduleInfo, _moduleDirective> MODULE_DIRECTIVES =
            new _feature._many<>(_moduleInfo.class, _moduleDirective.class,
            _feature._id.MODULE_DIRECTIVES,
            _feature._id.MODULE_DIRECTIVES,
            a -> a.listModuleDirectives(),
            (_moduleInfo a, List<_moduleDirective> _ns) -> a.setModuleDirectives(_ns), PARSER, s-> _moduleDirective.of(s))
            .setOrdered(false);

    public static _feature._features<_moduleInfo> FEATURES = _feature._features.of(_moduleInfo.class, IMPORTS, IS_OPEN, MODULE_NAME, MODULE_DIRECTIVES);

    /**
     * Decompose the entity into key-VALUE pairs
     *
     * @return a map of key values

    public Map<_java.Feature, Object> features() {

        Map m = new HashMap();
        m.put(_java.Feature.HEADER_COMMENT, getHeaderComment());
        //OPEN
        m.put(_java.Feature.NAME, getModuleAst().getNameAsString());
        m.put(_java.Feature.MODULE_DECLARATION, getModuleAst());
        //m.put(_java.Feature.ANNO_EXPRS, _annoExprs.of(getModuleAst()));
        m.put(_java.Feature.IMPORTS, _imports.of(astCompUnit));
        //m.put(_java.Component.JAVADOC, this.javadocHolder.getJavadoc());

        return m;
    }
     */

    @Override
    public String toString() {
        return this.astCompUnit.toString();
    }

    @Override
    public CompilationUnit ast() {
        return astCompUnit;
    }

    public static _moduleInfo of(String... input) {
        return new _moduleInfo(Ast.of(Text.combine(input)));
    }

    public static _moduleInfo of(CompilationUnit astCu) {
        return new _moduleInfo(astCu);
    }

    public static _moduleInfo of( ModuleDeclaration md ){
        return new _moduleInfo( md.findCompilationUnit().get());
    }

    public _moduleInfo(CompilationUnit cu) {
        this.astCompUnit = cu;
    }

    @Override
    public boolean isTopLevel() {
        return true;
    }

    public boolean isOpen(){
        return getModuleAst().isOpen();
    }

    public _moduleInfo setOpen(Boolean open){
        this.getModuleAst().setOpen(open);
        return this;
    }

    /**
     * creates and returns a list of moduleDirectives
     * @return
     */
    public List<_moduleDirective> listModuleDirectives(){
        return this.getModuleAst().getDirectives().stream().map(m -> _moduleDirective.of(m)).collect(Collectors.toList());
    }

    public List<ModuleDirective> listAstModuleDirectives(){
        return this.getModuleAst().getDirectives();
    }

    public _moduleInfo setModuleDirectives(List<_moduleDirective> mds){
        this.getModuleAst().getDirectives().clear();
        mds.forEach( md -> this.getModuleAst().getDirectives().add( (ModuleDirective)md.ast() ) );
        return this;
    }

    public ModuleDeclaration getModuleAst() {
        if (this.astCompUnit.getModule().isPresent()) {
            return this.astCompUnit.getModule().get();
        }
        return null;
    }

    @Override
    public _moduleInfo setName(String name) {
        this.getModuleAst().setName(name);
        return this;
    }

    @Override
    public String getName() {
        return this.getModuleAst().getNameAsString();
    }

    @Override
    public Node getNameNode() {
        return this.getModuleAst().getName();
    }
}
