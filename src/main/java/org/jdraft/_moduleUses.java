package org.jdraft;

import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.modules.ModuleDeclaration;
import com.github.javaparser.ast.modules.ModuleUsesDirective;
import org.jdraft.text.Text;

import java.util.Objects;

public class _moduleUses implements _java._node<ModuleUsesDirective, _moduleUses>,
        _moduleDirective<ModuleUsesDirective, _moduleUses> {

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

    public ModuleUsesDirective mod;

    public _moduleUses(ModuleUsesDirective mod){
        this.mod = mod;
    }


    public _moduleUses setName(String moduleName){
        this.mod.setName(moduleName);
        return this;
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
