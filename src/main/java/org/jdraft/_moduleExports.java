package org.jdraft;

import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.NodeList;
import com.github.javaparser.ast.expr.Name;
import com.github.javaparser.ast.modules.ModuleDeclaration;
import com.github.javaparser.ast.modules.ModuleExportsDirective;
import org.jdraft.text.Text;

import java.util.Arrays;
import java.util.Objects;

public class _moduleExports implements _java._node<ModuleExportsDirective, _moduleExports>,
        _moduleDirective<ModuleExportsDirective, _moduleExports> {

    public static _moduleExports of( ModuleExportsDirective med){
        return new _moduleExports(med);
    }

    public static _moduleExports of( String...source ){
        String src = Text.combine( source );
        if( !src.startsWith("exports ") ){
            src = "exports "+src;
        }
        if( ! src.endsWith(";")){
            src = src+";";
        }
        CompilationUnit cu =
                Ast.parse("module J{ "+System.lineSeparator()+src+System.lineSeparator()+"}");
        ModuleDeclaration md = cu.getModule().get();
        ModuleExportsDirective modd = (ModuleExportsDirective)md.getDirectives().get(0);
        modd.remove();
        return of( modd );
    }

    public ModuleExportsDirective me;

    public _moduleExports setName(String name){
        this.me.setName(name);
        return this;
    }

    public String getName(){
        return this.me.getNameAsString();
    }

    public _moduleExports setModuleNames(String...moduleNames){
        NodeList<Name> modNames = new NodeList<>();
        Arrays.stream(moduleNames).forEach(mn -> modNames.add(new Name(mn)));
        this.me.setModuleNames(modNames);
        return this;
    }

    public _moduleExports setModuleNames(Name...moduleNames){
        NodeList<Name> modNames = new NodeList<>();
        Arrays.stream(moduleNames).forEach(mn -> modNames.add(mn));
        this.me.setModuleNames(modNames);
        return this;
    }

    public _moduleExports addModuleNames(String...names){
        Arrays.stream(names).forEach(mn -> this.me.getModuleNames().add(new Name(mn) ));
        return this;
    }

    public _moduleExports addModuleNames(Name...names){
        Arrays.stream(names).forEach(mn -> this.me.getModuleNames().add( mn ));
        return this;
    }

    public _moduleExports removeModuleNames(String...names){
        Arrays.stream(names).forEach(mn -> this.me.getModuleNames().remove(new Name(mn) ));
        return this;
    }

    public _moduleExports removeModuleNames(Name...names){
        Arrays.stream(names).forEach(mn -> this.me.getModuleNames().remove( mn ));
        return this;
    }

    public _moduleExports(ModuleExportsDirective med){
        this.me = med;
    }

    @Override
    public _moduleExports copy() {
        return of( this.me.clone());
    }

    @Override
    public ModuleExportsDirective ast() {
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
        return o instanceof _moduleExports && Objects.equals( ((_moduleExports)o).me, this.me);
    }

    public String toString(){
        return this.me.toString();
    }
}
