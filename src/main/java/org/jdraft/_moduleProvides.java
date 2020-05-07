package org.jdraft;

import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.NodeList;
import com.github.javaparser.ast.expr.Name;
import com.github.javaparser.ast.modules.ModuleDeclaration;
import com.github.javaparser.ast.modules.ModuleProvidesDirective;
import org.jdraft.text.Text;

import java.util.Arrays;
import java.util.Objects;

public class _moduleProvides implements _java._node<ModuleProvidesDirective, _moduleProvides>,
        _moduleDirective<ModuleProvidesDirective, _moduleProvides> {

    public static _moduleProvides of(ModuleProvidesDirective med){
        return new _moduleProvides(med);
    }

    public static _moduleProvides of(String...source ){
        String src = Text.combine( source );
        if( !src.startsWith("provides ") ){
            src = "provides "+src;
        }
        if( ! src.endsWith(";")){
            src = src+";";
        }
        CompilationUnit cu =
                Ast.parse("module J{ "+System.lineSeparator()+src+System.lineSeparator()+"}");
        ModuleDeclaration md = cu.getModule().get();
        ModuleProvidesDirective modd = (ModuleProvidesDirective)md.getDirectives().get(0);
        modd.remove();
        return of( modd );
    }

    public ModuleProvidesDirective  me;

    public _moduleProvides setName(String name){
        this.me.setName(name);
        return this;
    }

    public String getName(){
        return this.me.getNameAsString();
    }

    public _moduleProvides setModuleNames(String...moduleNames){
        NodeList<Name> modNames = new NodeList<>();
        Arrays.stream(moduleNames).forEach(mn -> modNames.add(new Name(mn)));
        this.me.setWith(modNames);
        return this;
    }

    public _moduleProvides setModuleNames(Name...moduleNames){
        NodeList<Name> modNames = new NodeList<>();
        Arrays.stream(moduleNames).forEach(mn -> modNames.add(mn));
        this.me.setWith(modNames);
        return this;
    }

    public _moduleProvides addModuleNames(String...names){
        Arrays.stream(names).forEach(mn -> this.me.getWith().add(new Name(mn) ));
        return this;
    }

    public _moduleProvides addModuleNames(Name...names){
        Arrays.stream(names).forEach(mn -> this.me.getWith().add( mn ));
        return this;
    }

    public _moduleProvides removeModuleNames(String...names){
        Arrays.stream(names).forEach(mn -> this.me.getWith().remove(new Name(mn) ));
        return this;
    }

    public _moduleProvides removeModuleNames(Name...names){
        Arrays.stream(names).forEach(mn -> this.me.getWith().remove( mn ));
        return this;
    }

    public _moduleProvides(ModuleProvidesDirective med){
        this.me = med;
    }

    @Override
    public _moduleProvides copy() {
        return of( this.me.clone());
    }

    @Override
    public ModuleProvidesDirective ast() {
        return this.me;
    }

    @Override
    public boolean is(String... stringRep) {
        return Objects.equals( of(stringRep ).me, this.me);
    }

    public String toString(){
        return this.me.toString();
    }
}
