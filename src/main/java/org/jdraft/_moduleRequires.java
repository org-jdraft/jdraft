package org.jdraft;

import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.expr.Name;
import com.github.javaparser.ast.modules.ModuleDeclaration;
import com.github.javaparser.ast.modules.ModuleRequiresDirective;
import org.jdraft.text.Text;

import java.util.Objects;
import java.util.function.Function;

public final class _moduleRequires implements _java._node<ModuleRequiresDirective, _moduleRequires>,
        _moduleDirective<ModuleRequiresDirective, _moduleRequires> {

    public static final Function<String, _moduleRequires> PARSER = s-> _moduleRequires.of(s);

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
                Ast.parse("module J{ "+System.lineSeparator()+src+System.lineSeparator()+"}");
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

    public static _feature._features<_moduleRequires> FEATURES = _feature._features.of(_moduleRequires.class, IS_TRANSITIVE, MODULE_NAME);

    public ModuleRequiresDirective  me;

    public _moduleRequires setName(String name){
        this.me.setName(name);
        return this;
    }

    public String getName(){
        return this.me.getNameAsString();
    }

    public _moduleRequires setName(_name name){
        this.me.setName(name.toString());
        return this;
    }

    public Name getNameNode(){
        return this.me.getName();
    }

    public _moduleRequires setStatic(boolean st){
        this.me.setStatic(st);
        return this;
    }

    public _moduleRequires setTransitive (Boolean isTransitive){
        this.me.setTransitive(isTransitive);
        return this;
    }

    public Boolean isTransitive(){
        return this.me.isTransitive();
    }

    public _moduleRequires(ModuleRequiresDirective med){
        this.me = med;
    }

    @Override
    public _moduleRequires copy() {
        return of( this.me.clone());
    }

    @Override
    public ModuleRequiresDirective ast() {
        return this.me;
    }

    @Override
    public boolean is(String... stringRep) {
        return Objects.equals( of(stringRep ).me, this.me);
    }

    public int hashCode(){
        return 31 * this.me.hashCode();
    }

    public boolean equals(Object o ){
        return o instanceof _moduleRequires && Objects.equals( ((_moduleRequires)o).me, this.me);
    }

    public String toString(){
        return this.me.toString();
    }
}
