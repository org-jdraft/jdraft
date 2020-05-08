package org.jdraft;

import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.NodeList;
import com.github.javaparser.ast.expr.Name;
import com.github.javaparser.ast.modules.ModuleDeclaration;
import com.github.javaparser.ast.modules.ModuleOpensDirective;
import org.jdraft.text.Text;

import java.util.Arrays;
import java.util.Objects;

public class _moduleOpens implements _java._node<ModuleOpensDirective, _moduleOpens>,
        _moduleDirective<ModuleOpensDirective, _moduleOpens> {

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
                Ast.parse("module J{ "+System.lineSeparator()+src+System.lineSeparator()+"}");
        ModuleDeclaration md = cu.getModule().get();
        ModuleOpensDirective modd = (ModuleOpensDirective)md.getDirectives().get(0);
        modd.remove();
        return of( modd );
    }

    public ModuleOpensDirective mod;

    public _moduleOpens(ModuleOpensDirective mod){
        this.mod = mod;
    }

    public _moduleOpens setModuleNames(String...moduleNames){
        NodeList<Name> modNames = new NodeList<>();
        Arrays.stream(moduleNames).forEach(mn -> modNames.add(new Name(mn)));
        this.mod.setModuleNames(modNames);
        return this;
    }

    public _moduleOpens setModuleNames(Name...moduleNames){
        NodeList<Name> modNames = new NodeList<>();
        Arrays.stream(moduleNames).forEach(mn -> modNames.add(mn));
        this.mod.setModuleNames(modNames);
        return this;
    }

    public _moduleOpens addModuleNames(String...names){
        Arrays.stream(names).forEach(mn -> this.mod.getModuleNames().add(new Name(mn) ));
        return this;
    }

    public _moduleOpens addModuleNames(Name...names){
        Arrays.stream(names).forEach(mn -> this.mod.getModuleNames().add( mn ));
        return this;
    }

    public _moduleOpens removeModuleNames(String...names){
        Arrays.stream(names).forEach(mn -> this.mod.getModuleNames().remove(new Name(mn) ));
        return this;
    }

    public _moduleOpens removeModuleNames(Name...names){
        Arrays.stream(names).forEach(mn -> this.mod.getModuleNames().remove( mn ));
        return this;
    }

    @Override
    public _moduleOpens copy() {
        return of( this.mod.clone());
    }

    @Override
    public ModuleOpensDirective ast() {
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
        return o instanceof _moduleOpens && Objects.equals( ((_moduleOpens)o).mod, this.mod);
    }

    public String toString(){
        return this.mod.toString();
    }
}
