package org.jdraft;

import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.NodeList;
import com.github.javaparser.ast.expr.Name;
import com.github.javaparser.ast.modules.ModuleDeclaration;
import com.github.javaparser.ast.modules.ModuleOpensDirective;
import org.jdraft.text.Text;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.function.Function;
import java.util.stream.Collectors;

public final class _moduleOpens implements _java._node<ModuleOpensDirective, _moduleOpens>,
        _java._set<Name, _name, _moduleOpens>,
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
                Ast.parse("module J{ "+System.lineSeparator()+src+System.lineSeparator()+"}");
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

    public ModuleOpensDirective mod;

    public _moduleOpens(ModuleOpensDirective mod){
        this.mod = mod;
    }

    public _moduleOpens setName(String name){
        this.mod.setName(name);
        return this;
    }

    public _moduleOpens setName(_name name){
        this.mod.setName(name.toString());
        return this;
    }

    public String getName(){
        return this.mod.getNameAsString();
    }

    public Name getNameNode(){
        return this.mod.getName();
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
    public List<_name> list() {
        return this.mod.getModuleNames().stream().map(m-> _name.of(m)).collect(Collectors.toList());
    }

    @Override
    public NodeList<Name> listAstElements() {
        return this.mod.getModuleNames();
    }

    @Override
    public ModuleOpensDirective ast() {
        return this.mod;
    }

    /*
    @Override
    public boolean is(String... stringRep) {
        return Objects.equals( of(stringRep ).mod, this.mod);
    }
     */

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
