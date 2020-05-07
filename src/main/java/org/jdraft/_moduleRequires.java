package org.jdraft;

import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.modules.ModuleDeclaration;
import com.github.javaparser.ast.modules.ModuleRequiresDirective;
import org.jdraft.text.Text;

import java.util.Objects;

public class _moduleRequires implements _java._node<ModuleRequiresDirective, _moduleRequires>,
        _moduleDirective<ModuleRequiresDirective, _moduleRequires> {

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

    public ModuleRequiresDirective  me;

    public _moduleRequires setName(String name){
        this.me.setName(name);
        return this;
    }

    public boolean isStatic(){
        return this.me.isStatic();
    }

    public _moduleRequires setStatic(boolean st){
        this.me.setStatic(st);
        return this;
    }

    public _modifiers getModifiers(){
        return _modifiers.of(this.me);
    }

    public _moduleRequires setModifiers( String...mods){
        this.me.setModifiers(_modifiers.of(mods).ast());
        return this;
    }

    public _moduleRequires setModifiers( _modifiers _ms){
        this.me.setModifiers(_ms.ast());
        return this;
    }

    public _moduleRequires setTransitive (boolean isTransitive){
        this.me.setTransitive(isTransitive);
        return this;
    }

    public boolean isTransitive(){
        return this.me.isTransitive();
    }

    public String getName(){
        return this.me.getNameAsString();
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

    public String toString(){
        return this.me.toString();
    }
}
