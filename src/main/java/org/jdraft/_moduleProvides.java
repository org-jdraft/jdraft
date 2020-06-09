package org.jdraft;

import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.NodeList;
import com.github.javaparser.ast.expr.Name;
import com.github.javaparser.ast.modules.ModuleDeclaration;
import com.github.javaparser.ast.modules.ModuleProvidesDirective;
import com.github.javaparser.printer.PrettyPrinterConfiguration;
import org.jdraft.text.Text;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.function.Function;
import java.util.stream.Collectors;

public final class _moduleProvides implements _tree._node<ModuleProvidesDirective, _moduleProvides>,
        _tree._group<Name, _name, _moduleProvides>,
        _moduleDirective<ModuleProvidesDirective, _moduleProvides> {

    public static final Function<String, _moduleProvides> PARSER = s-> _moduleProvides.of(s);

    public _feature._features<_moduleProvides> features(){
        return FEATURES;
    }

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

    public static _feature._one<_moduleProvides, String> MODULE_NAME = new _feature._one<>(_moduleProvides.class, String.class,
            _feature._id.MODULE_NAME,
            a -> a.getName(),
            (_moduleProvides a, String s) -> a.setName(s), PARSER);

    public static _feature._many<_moduleProvides, _name> MODULE_NAMES = new _feature._many<>(_moduleProvides.class, _name.class,
            _feature._id.MODULE_NAMES,
            _feature._id.MODULE_NAME,
            a -> a.list(),
            (_moduleProvides a, List<_name> _ns) -> a.set(_ns), PARSER, s->_name.of(s)).setOrdered(false);

    public static _feature._features<_moduleProvides> FEATURES = _feature._features.of(_moduleProvides.class, PARSER, MODULE_NAME, MODULE_NAMES);

    public ModuleProvidesDirective me;

    public _moduleProvides setName(_name name){
        this.me.setName(name.toString());
        return this;
    }

    public Name getNameNode(){
        return this.me.getName();
    }

    public _moduleProvides setName(String name){
        this.me.setName(name);
        return this;
    }


    public boolean is(String code){
        return is(new String[]{code});
    }

    public String getName(){
        return this.me.getNameAsString();
    }

    @Override
    public List<_name> list() {
        return this.me.getWith().stream().map(m-> _name.of(m)).collect(Collectors.toList());
    }

    @Override
    public NodeList<Name> listAstElements() {
        return this.me.getWith();
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

    /*
    @Override
    public boolean is(String... stringRep) {
        return Objects.equals( of(stringRep ).me, this.me);
    }
     */

    public int hashCode(){
        return 31 * this.me.hashCode();
    }

    public boolean equals(Object o ){
        return o instanceof _moduleProvides && Objects.equals( ((_moduleProvides)o).me, this.me);
    }

    public String toString() {
        return toString( new PrettyPrinterConfiguration());
    }

    public String toString(PrettyPrinterConfiguration ppc){
        return this.me.toString(ppc);
    }
}
