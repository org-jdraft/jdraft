package org.jdraft;

import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.expr.CharLiteralExpr;
import com.github.javaparser.ast.expr.Name;
import com.github.javaparser.ast.modules.ModuleDeclaration;
import com.github.javaparser.ast.modules.ModuleUsesDirective;
import org.jdraft.text.Text;

import java.util.Objects;
import java.util.function.Function;

public final class _moduleUses implements _tree._node<ModuleUsesDirective, _moduleUses>,
        _moduleDirective<ModuleUsesDirective, _moduleUses> {

    public static final Function<String, _moduleUses> PARSER = s-> _moduleUses.of(s);

    public _feature._features<_moduleUses> features(){
        return FEATURES;
    }

    public static _moduleUses of(ModuleUsesDirective mod){
        return new _moduleUses(mod);
    }

    public static _moduleUses of(String...source ){
        String src = Text.combine( source );
        if( !src.startsWith("uses ") ){
            src = "uses "+src;
        }
        if( ! src.endsWith(";")){
            src = src+";";
        }
        CompilationUnit cu =
                Ast.parse("module $name${ "+System.lineSeparator()+src+System.lineSeparator()+"}");
        ModuleDeclaration md = cu.getModule().get();
        ModuleUsesDirective modd = (ModuleUsesDirective)md.getDirectives().get(0);
        modd.remove();
        return of( modd );
    }

    public static _feature._one<_moduleUses, String> MODULE_NAME = new _feature._one<>(_moduleUses.class, String.class,
            _feature._id.MODULE_NAME,
            a -> a.getName(),
            (_moduleUses a, String s) -> a.setName(s), PARSER);

    public static _feature._features<_moduleUses> FEATURES = _feature._features.of(_moduleUses.class,  PARSER, MODULE_NAME);

    public ModuleUsesDirective node;

    public _moduleUses(ModuleUsesDirective node){
        this.node = node;
    }

    public _moduleUses setName(String moduleName){
        this.node.setName(moduleName);
        return this;
    }

    public String getName(){
        return this.node.getNameAsString();
    }

    public _moduleUses setName(_name name){
        this.node.setName(name.toString());
        return this;
    }
    public Name getNameNode(){
        return this.node.getName();
    }

    @Override
    public _moduleUses copy() {
        return of( this.node.clone());
    }

    @Override
    public ModuleUsesDirective node() {
        return this.node;
    }

    /**
     * Replace the underlying node within the AST (if this node has a parent)
     * and return this (now pointing to the new node)
     * @param replaceNode the node instance to swap in for the old node that this facade was pointing to
     * @return the modified this (now pointing to the replaceNode which was swapped into the AST)
     */
    public _moduleUses replace(ModuleUsesDirective replaceNode){
        this.node.replace(replaceNode);
        this.node = replaceNode;
        return this;
    }

    public int hashCode(){
        return 31 * this.node.hashCode();
    }

    public boolean equals(Object o ){
        return o instanceof _moduleUses && Objects.equals( ((_moduleUses)o).node, this.node);
    }

    public String toString(){
        return this.node.toString();
    }
}
