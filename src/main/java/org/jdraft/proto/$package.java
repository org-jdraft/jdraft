package org.jdraft.proto;

import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.Node;
import com.github.javaparser.ast.PackageDeclaration;
import org.jdraft.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Predicate;
import java.util.stream.Collectors;

/**
 * $proto on modelling the package declaration
 *
 * @see PackageDeclaration
 */
public class $package implements $proto<PackageDeclaration, $package>, Template<PackageDeclaration> {

    public Stencil name = Stencil.of("$packageName$");

    public $annos annos = $annos.of();

    public Predicate<PackageDeclaration> constraint = t->true;

    public static $package of(){
        return new $package("$packageName$", $annos.of(), t->true);
    }

    public static $package of( String namePattern ){
        return new $package( namePattern, $annos.of(), t->true );
    }

    public static $package of( Predicate<PackageDeclaration> matchFn ){
        return new $package( "$packageName$", $annos.of(), matchFn );
    }

    public static $package of( String namePattern, Predicate<PackageDeclaration> matchFn ){
        return new $package( namePattern, $annos.of(), matchFn );
    }

    public static $package of( String namePattern, $annos annos, Predicate<PackageDeclaration> matchFn ){
        return new $package( namePattern, annos, matchFn );
    }

    public $package( String namePattern, $annos annos, Predicate<PackageDeclaration> constraint){
        //the pattern must be a valid package name
        PackageDeclaration pd = Ast.packageDeclaration(namePattern);
        this.name = Stencil.of(pd.getName().asString());
        this.annos = annos;
        this.constraint = constraint;
    }

    @Override
    public $package and(Predicate<PackageDeclaration> constraint) {
        this.constraint = this.constraint.and(constraint);
        return null;
    }

    @Override
    public $package hardcode$(Translator translator, Tokens kvs) {
        this.name = this.name.hardcode$(translator, kvs);
        this.annos = this.annos.hardcode$(translator, kvs);
        return this;
    }

    @Override
    public boolean match(Node candidate) {
        if( candidate instanceof PackageDeclaration ){
            PackageDeclaration pd = (PackageDeclaration)candidate;
            return matches(pd);
        }
        return false;
    }

    public boolean matches(CompilationUnit cu ){
         return matches( cu.getPackageDeclaration() );
    }

    public boolean matches( Optional<PackageDeclaration> opd){
        if( isMatchAny() ){
            return true;
        }
        if( !opd.isPresent()){
            return false;
        }
        return matches(opd.get());
    }

    public boolean matches(String packageDecl){
        try{
            return matches(Ast.packageDeclaration(packageDecl));
        } catch(Exception e){
            return false;
        }
    }

    public boolean matches( PackageDeclaration pd ){
        if( isMatchAny() && pd == null ){
            return true;
        }
        if( constraint.test(pd)){
            return this.name.matches(pd.getNameAsString()) && annos.matches(pd);
        }
        return false;
    }

    public boolean isMatchAny(){
        try{
            return constraint.test(null) && this.annos.isMatchAny() && this.name.isMatchAny();
        } catch(Exception e){

        }
        return false;
    }
    @Override
    public PackageDeclaration firstIn(Node astStartNode, Predicate<PackageDeclaration> nodeMatchFn) {
        Optional<PackageDeclaration> opd = astStartNode.findFirst(PackageDeclaration.class, pd-> matches(pd) && nodeMatchFn.test(pd));
        if( opd.isPresent() ){
            return opd.get();
        }
        return null;
    }

    public Tokens parse( PackageDeclaration pd ){
        if(pd == null && isMatchAny() ){
            return new Tokens();
        }
        if( constraint.test(pd)){
            Tokens ts = name.parse(pd.getNameAsString());
            ts = annos.parseTo(pd, ts);
            return ts;
        }
        return null;
    }

    @Override
    public Select select(PackageDeclaration pd) {
        Tokens ts = parse( pd);
        if( ts == null ){
            return null;
        }
        return new Select(pd, ts);
    }

    @Override
    public Select  selectFirstIn(Node astNode) {
        return selectFirstIn(astNode, t->true);
    }

    /**
     * Select the first matching the prototype AND the selectMatchFn
     * @param astNode the start node
     * @param selectMatchFn the matching function on the select
     * @return the first matching Select or null if none found
     */
    public Select selectFirstIn(Node astNode, Predicate<Select>selectMatchFn) {
        //astNode.walk(PackageDeclaration.class, );
        Optional<PackageDeclaration> opd = astNode.findFirst( PackageDeclaration.class,
                (PackageDeclaration pd) -> {
                Select sel = select( pd);
                return ( sel != null  && selectMatchFn.test(sel));
            });
        if( opd.isPresent() ){
            return select(opd.get());
        }
        return null;
    }

    @Override
    public List<Select> listSelectedIn(Node astNode ){
        return listSelectedIn(astNode, t->true);
    }

    /**
     * Select the first matching the prototype AND the selectMatchFn
     * @param astNode the start node
     * @param selectMatchFn the matching function on the select
     * @return the first matching Select or null if none found
     */
    public List<Select> listSelectedIn(Node astNode, Predicate<Select>selectMatchFn) {
        List<Select> sel = new ArrayList<>();
        forEachIn(astNode, e -> {
            Select s = select(e);
            if( s != null && selectMatchFn.test(s)){
                sel.add(s);
            }
        });
        return sel;
    }

    @Override
    public <N extends Node> N forEachIn(N astNode, Predicate<PackageDeclaration> nodeMatchFn, Consumer<PackageDeclaration> nodeActionFn) {
        astNode.walk(PackageDeclaration.class, pd-> {
            if(matches(pd) && nodeMatchFn.test(pd)){
                nodeActionFn.accept(pd);
            }
        });
        return astNode;
    }

    @Override
    public PackageDeclaration draft(Translator translator, Map<String, Object> keyValues) {
        String name = null;
        Object pkgName = keyValues.get("$packageName");
        if( pkgName != null ){ //check for an override parameter
            keyValues.remove("$packageName");
            name = Stencil.of(pkgName.toString()).draft(translator, keyValues);
        } else{
            name = this.name.draft(translator, keyValues);
        }
        _anno._annos _as = this.annos.draft(translator, keyValues);

        PackageDeclaration pd = new PackageDeclaration();
        pd.setName(name);
        pd.setAnnotations(_as.ast());
        return pd;
    }

    @Override
    public $package $(String target, String $Name) {
        if( ! this.isMatchAny() ){
            this.name = this.name.$(target, $Name);
            this.annos = this.annos.$(target, $Name);
        }
        return this;
    }

    @Override
    public List<String> list$() {
        List<String> strs = this.annos.list$();
        strs.addAll( this.name.list$());
        return strs;
    }

    @Override
    public List<String> list$Normalized() {
        List<String> strs = this.annos.list$Normalized();
        strs.addAll( this.name.list$Normalized() );
        return strs.stream().distinct().collect(Collectors.toList());
    }

    /**
     * A Matched Selection result returned from matching a prototype $field
     * inside of some Node or _node
     */
    public static class Select implements $proto.selected,
            selectAst<PackageDeclaration> {

        public $tokens tokens;
        public PackageDeclaration packageDecl;

        public Select( PackageDeclaration pd, Tokens tokens){
            this.packageDecl = pd;
            this.tokens = $tokens.of(tokens);
        }

        @Override
        public $tokens tokens() {
            return tokens;
        }

        @Override
        public PackageDeclaration ast() {
            return packageDecl;
        }
    }
}