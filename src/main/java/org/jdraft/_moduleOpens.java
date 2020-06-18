package org.jdraft;

import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.NodeList;
import com.github.javaparser.ast.expr.Name;
import com.github.javaparser.ast.modules.ModuleDeclaration;
import com.github.javaparser.ast.modules.ModuleOpensDirective;
import com.github.javaparser.printer.PrettyPrinterConfiguration;
import org.jdraft.text.Text;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.function.Function;
import java.util.stream.Collectors;

public final class _moduleOpens implements _tree._node<ModuleOpensDirective, _moduleOpens>,
        _tree._group<Name, _name, _moduleOpens>,
        _moduleDirective<ModuleOpensDirective, _moduleOpens> {

    public static final Function<String, _moduleOpens> PARSER = s-> _moduleOpens.of(s);

    public _feature._features<_moduleOpens> features(){
        return FEATURES;
    }

    public static _moduleOpens of(ModuleOpensDirective mod){
        return new _moduleOpens(mod);
    }

    public static _moduleOpens of( String...source ){
        String src = Text.combine( source );
        if( !src.startsWith("opens ") ){
            src = "opens "+src;
        }
        if( ! src.endsWith(";")){
            src = src+";";
        }
        CompilationUnit cu =
                Ast.parse("module $name${ "+System.lineSeparator()+src+System.lineSeparator()+"}");
        ModuleDeclaration md = cu.getModule().get();
        ModuleOpensDirective modd = (ModuleOpensDirective)md.getDirectives().get(0);
        modd.remove();
        return of( modd );
    }

    public static _feature._one<_moduleOpens, String> MODULE_NAME = new _feature._one<>(_moduleOpens.class, String.class,
            _feature._id.MODULE_NAME,
            a -> a.getName(),
            (_moduleOpens a, String s) -> a.setName(s), PARSER);

    public static _feature._many<_moduleOpens, _name> MODULE_NAMES = new _feature._many<>(_moduleOpens.class, _name.class,
            _feature._id.MODULE_NAMES,
            _feature._id.MODULE_NAME,
            a -> a.list(),
            (_moduleOpens a, List<_name> _ns) -> a.set(_ns), PARSER, s->_name.of(s))
            .setOrdered(false);

    public static _feature._features<_moduleOpens> FEATURES = _feature._features.of(_moduleOpens.class,  PARSER, MODULE_NAME, MODULE_NAMES);

    public ModuleOpensDirective node;

    public _moduleOpens(ModuleOpensDirective node){
        this.node = node;
    }

    public _moduleOpens setName(String name){
        this.node.setName(name);
        return this;
    }

    public _moduleOpens setName(_name name){
        this.node.setName(name.toString());
        return this;
    }

    public String getName(){
        return this.node.getNameAsString();
    }

    public Name getNameNode(){
        return this.node.getName();
    }

    public _moduleOpens setModuleNames(String...moduleNames){
        NodeList<Name> modNames = new NodeList<>();
        Arrays.stream(moduleNames).forEach(mn -> modNames.add(new Name(mn)));
        this.node.setModuleNames(modNames);
        return this;
    }

    public _moduleOpens setModuleNames(Name...moduleNames){
        NodeList<Name> modNames = new NodeList<>();
        Arrays.stream(moduleNames).forEach(mn -> modNames.add(mn));
        this.node.setModuleNames(modNames);
        return this;
    }

    public _moduleOpens addModuleNames(String...names){
        Arrays.stream(names).forEach(mn -> this.node.getModuleNames().add(new Name(mn) ));
        return this;
    }

    public _moduleOpens addModuleNames(Name...names){
        Arrays.stream(names).forEach(mn -> this.node.getModuleNames().add( mn ));
        return this;
    }

    public _moduleOpens removeModuleNames(String...names){
        Arrays.stream(names).forEach(mn -> this.node.getModuleNames().remove(new Name(mn) ));
        return this;
    }

    public _moduleOpens removeModuleNames(Name...names){
        Arrays.stream(names).forEach(mn -> this.node.getModuleNames().remove( mn ));
        return this;
    }

    @Override
    public _moduleOpens copy() {
        return of( this.node.clone());
    }

    @Override
    public List<_name> list() {
        return this.node.getModuleNames().stream().map(m-> _name.of(m)).collect(Collectors.toList());
    }

    public boolean is(String code){
        return is(new String[]{code});
    }

    @Override
    public NodeList<Name> astList() {
        return this.node.getModuleNames();
    }

    @Override
    public ModuleOpensDirective node() {
        return this.node;
    }

    /**
     * Replace the underlying node within the AST (if this node has a parent)
     * and return this (now pointing to the new node)
     * @param replaceNode the node instance to swap in for the old node that this facade was pointing to
     * @return the modified this (now pointing to the replaceNode which was swapped into the AST)
     */
    public _moduleOpens replace(ModuleOpensDirective replaceNode){
        this.node.replace(replaceNode);
        this.node = replaceNode;
        return this;
    }

    public int hashCode(){
        return 31 * this.node.hashCode();
    }

    public boolean equals(Object o ){
        return o instanceof _moduleOpens && Objects.equals( ((_moduleOpens)o).node, this.node);
    }

    public String toString() {
        return toString( new PrettyPrinterConfiguration());
    }

    public String toString(PrettyPrinterConfiguration ppc){
        return this.node.toString(ppc);
    }
}
