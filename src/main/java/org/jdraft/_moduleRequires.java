package org.jdraft;

import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.expr.CharLiteralExpr;
import com.github.javaparser.ast.expr.Name;
import com.github.javaparser.ast.modules.ModuleDeclaration;
import com.github.javaparser.ast.modules.ModuleRequiresDirective;
import org.jdraft.text.Text;

import java.util.Objects;
import java.util.function.Function;

public final class _moduleRequires implements _tree._node<ModuleRequiresDirective, _moduleRequires>,
        _moduleDirective<ModuleRequiresDirective, _moduleRequires> {

    public static final Function<String, _moduleRequires> PARSER = s-> _moduleRequires.of(s);

    public _feature._features<_moduleRequires> features(){
        return FEATURES;
    }

    public static _moduleRequires of(ModuleRequiresDirective med){
        return new _moduleRequires(med);
    }

    public static _moduleRequires of(String...source ){
        String src = Text.combine( source );
        if( !src.startsWith("requires ") ){
            src = "requires "+src;
        }
        if( ! src.endsWith(";")){
            src = src+";";
        }
        CompilationUnit cu =
                Ast.parse("module $name${ "+System.lineSeparator()+src+System.lineSeparator()+"}");
        ModuleDeclaration md = cu.getModule().get();
        ModuleRequiresDirective modd = (ModuleRequiresDirective)md.getDirectives().get(0);
        modd.remove();
        return of( modd );
    }

    public static _feature._one<_moduleRequires, String> MODULE_NAME = new _feature._one<>(_moduleRequires.class, String.class,
            _feature._id.MODULE_NAME,
            a -> a.getName(),
            (_moduleRequires a, String s) -> a.setName(s), PARSER);

    public static _feature._one<_moduleRequires, Boolean> IS_TRANSITIVE = new _feature._one<>(_moduleRequires.class, Boolean.class,
            _feature._id.IS_TRANSITIVE,
            a -> a.isTransitive(),
            (_moduleRequires a, Boolean b) -> a.setTransitive(b), PARSER);

    public static _feature._features<_moduleRequires> FEATURES = _feature._features.of(_moduleRequires.class, PARSER, IS_TRANSITIVE, MODULE_NAME);

    public ModuleRequiresDirective node;

    public _moduleRequires setName(String name){
        this.node.setName(name);
        return this;
    }

    public String getName(){
        return this.node.getNameAsString();
    }

    public _moduleRequires setName(_name name){
        this.node.setName(name.toString());
        return this;
    }

    public Name getNameNode(){
        return this.node.getName();
    }

    public _moduleRequires setStatic(boolean st){
        this.node.setStatic(st);
        return this;
    }

    public _moduleRequires setTransitive (Boolean isTransitive){
        this.node.setTransitive(isTransitive);
        return this;
    }

    public Boolean isTransitive(){
        return this.node.isTransitive();
    }

    public _moduleRequires(ModuleRequiresDirective med){
        this.node = med;
    }

    @Override
    public _moduleRequires copy() {
        return of( this.node.clone());
    }

    @Override
    public ModuleRequiresDirective node() {
        return this.node;
    }

    /**
     * Replace the underlying node within the AST (if this node has a parent)
     * and return this (now pointing to the new node)
     * @param replaceNode the node instance to swap in for the old node that this facade was pointing to
     * @return the modified this (now pointing to the replaceNode which was swapped into the AST)
     */
    public _moduleRequires replace(ModuleRequiresDirective replaceNode){
        this.node.replace(replaceNode);
        this.node = replaceNode;
        return this;
    }

    public int hashCode(){
        return 31 * this.node.hashCode();
    }

    public boolean equals(Object o ){
        return o instanceof _moduleRequires && Objects.equals( ((_moduleRequires)o).node, this.node);
    }

    public String toString(){
        return this.node.toString();
    }
}
