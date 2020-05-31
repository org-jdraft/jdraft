package org.jdraft;

import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.expr.Name;
import com.github.javaparser.ast.modules.ModuleDeclaration;
import com.github.javaparser.ast.modules.ModuleUsesDirective;
import org.jdraft.text.Text;

import java.util.Objects;
import java.util.function.Function;

public final class _moduleUses implements _java._node<ModuleUsesDirective, _moduleUses>,
        _moduleDirective<ModuleUsesDirective, _moduleUses> {

    public static final Function<String, _moduleUses> PARSER = s-> _moduleUses.of(s);

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
                Ast.parse("module J{ "+System.lineSeparator()+src+System.lineSeparator()+"}");
        ModuleDeclaration md = cu.getModule().get();
        ModuleUsesDirective modd = (ModuleUsesDirective)md.getDirectives().get(0);
        modd.remove();
        return of( modd );
    }

    public static _feature._one<_moduleUses, String> MODULE_NAME = new _feature._one<>(_moduleUses.class, String.class,
            _feature._id.MODULE_NAME,
            a -> a.getName(),
            (_moduleUses a, String s) -> a.setName(s), PARSER);

    public static _feature._meta<_moduleUses> META = _feature._meta.of(_moduleUses.class, MODULE_NAME);

    public ModuleUsesDirective mod;

    public _moduleUses(ModuleUsesDirective mod){
        this.mod = mod;
    }

    public _moduleUses setName(String moduleName){
        this.mod.setName(moduleName);
        return this;
    }

    public String getName(){
        return this.mod.getNameAsString();
    }

    public _moduleUses setName(_name name){
        this.mod.setName(name.toString());
        return this;
    }
    public Name getNameNode(){
        return this.mod.getName();
    }


    @Override
    public _moduleUses copy() {
        return of( this.mod.clone());
    }

    @Override
    public ModuleUsesDirective ast() {
        return this.mod;
    }

    @Override
    public boolean is(String... stringRep) {
        return Objects.equals( of(stringRep ).mod, this.mod);
    }

    public int hashCode(){
        return 31 * this.mod.hashCode();
    }

    public boolean equals(Object o ){
        return o instanceof _moduleUses && Objects.equals( ((_moduleUses)o).mod, this.mod);
    }

    public String toString(){
        return this.mod.toString();
    }
}
