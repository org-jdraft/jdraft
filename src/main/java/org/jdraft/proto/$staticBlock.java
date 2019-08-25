package org.jdraft.proto;

import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.Node;
import com.github.javaparser.ast.PackageDeclaration;
import com.github.javaparser.ast.body.InitializerDeclaration;
import com.github.javaparser.ast.stmt.BlockStmt;
import com.github.javaparser.ast.stmt.Statement;
import org.jdraft.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Predicate;
import java.util.stream.Collectors;

/**
 * $proto on modelling the package declaration
 *
 * @see InitializerDeclaration
 */
public class $staticBlock implements $proto<_staticBlock, $staticBlock>, Template<_staticBlock> {

    public $stmt<BlockStmt> body;

    public Predicate<_staticBlock> constraint = t->true;

    public static $staticBlock of(){
        return new $staticBlock($stmt.of(BlockStmt.class, "$staticBlock$"), t->true);
    }


    public static $staticBlock of(String... bodyPattern ){
        String str =  Text.combine( bodyPattern );
        if( str.trim().startsWith("static" )){
            str = str.substring(str.indexOf("{")+1 );
            str = str.substring(0, str.indexOf("}") );
        }
        return new $staticBlock( $stmt.of( BlockStmt.class, str),  t->true );
    }

    public static $staticBlock of(Predicate<_staticBlock> matchFn ){
        return new $staticBlock( $stmt.of( BlockStmt.class, "$staticBlock$"), matchFn );
    }

    public static $staticBlock of(String str, Predicate<_staticBlock> matchFn ){

        if( str.trim().startsWith("static" )){
            str = str.substring(str.indexOf("{")+1 );
            str = str.substring(0, str.indexOf("}") );
        }
        return new $staticBlock( $stmt.of(BlockStmt.class, str),  matchFn );
    }


    public static $staticBlock of(Statement st){
        if( st instanceof BlockStmt ){
            return new $staticBlock( $stmt.blockStmt((BlockStmt)st), t->true );
        } else{
            return new $staticBlock( $stmt.blockStmt( new BlockStmt().addStatement( st) ), t->true );
        }
    }
    public static $staticBlock of(Expr.Command lambdaWithBody){
        Statement bdy = _lambda.from( Thread.currentThread().getStackTrace()[2]).getBody();
        return of(bdy);
    }

    public static <A extends Object> $staticBlock of (Consumer<A> lambdaWithBody){
        Statement bdy = _lambda.from( Thread.currentThread().getStackTrace()[2]).getBody();
        return of(bdy);
    }

    public static <A extends Object, B extends Object>  $staticBlock of(BiConsumer<A,B> lambdaWithBody ){
        Statement bdy = _lambda.from( Thread.currentThread().getStackTrace()[2]).getBody();
        return of(bdy);
    }

    public static <A extends Object, B extends Object,C extends Object>  $staticBlock of(Expr.TriConsumer<A,B,C> lambdaWithBody ){
        Statement bdy = _lambda.from( Thread.currentThread().getStackTrace()[2]).getBody();
        return of(bdy);
    }

    public static <A extends Object, B extends Object,C extends Object, D extends Object> $staticBlock of(Expr.QuadConsumer<A,B,C,D> lambdaWithBody ){
        Statement bdy = _lambda.from( Thread.currentThread().getStackTrace()[2]).getBody();
        return of(bdy);
    }

    public $staticBlock( $stmt<BlockStmt> $body,Predicate<_staticBlock> constraint){
        //the pattern must be a valid package name
        this.body = $body;
        this.constraint = constraint;
    }

    @Override
    public $staticBlock and(Predicate<_staticBlock> constraint) {
        this.constraint = this.constraint.and(constraint);
        return null;
    }

    @Override
    public $staticBlock hardcode$(Translator translator, Tokens kvs) {
        this.body = this.body.hardcode$(translator, kvs);
        return this;
    }

    @Override
    public boolean match(Node candidate) {
        if( candidate instanceof InitializerDeclaration  ){
            InitializerDeclaration pd = (InitializerDeclaration)candidate;
            return matches(pd);
        }
        return false;
    }

    public boolean matches(String... staticBlock){
        try{
            return matches(Ast.staticBlock(staticBlock));
        } catch(Exception e){
            return false;
        }
    }

    public boolean matches(InitializerDeclaration staticBlock){
        if( staticBlock == null ){
            return isMatchAny();
        }

        return matches(_staticBlock.of(staticBlock));
    }

    public boolean matches( _staticBlock staticBlock ){
        if( staticBlock == null ){
            return isMatchAny();
        }
        if( constraint.test(staticBlock)){
            return this.body.matches(staticBlock.ast().getBody());
        }
        return false;
    }

    public boolean isMatchAny(){
        try{
            return constraint.test(null) &&
                    this.body.constraint.test(null) &&
                    this.body.stmtStencil.isMatchAny();
        } catch(Exception e){

        }
        return false;
    }
    @Override
    public _staticBlock firstIn(Node astStartNode, Predicate<_staticBlock> nodeMatchFn) {
        Optional<InitializerDeclaration> opd = astStartNode.findFirst(InitializerDeclaration.class, pd-> matches(pd) && nodeMatchFn.test(_staticBlock.of(pd)));
        if( opd.isPresent() ){
            return _staticBlock.of(opd.get());
        }
        return null;
    }

    public Tokens parse (_staticBlock _sb ){
        if( constraint.test(_sb) ){
            return body.select( _sb.ast().getBody()).tokens().asTokens();
        }
        return null;
    }

    public Tokens parse( InitializerDeclaration pd ){
        if(pd == null && isMatchAny() ){
            return new Tokens();
        }
        return parse( _staticBlock.of(pd));
    }

    @Override
    public Select select(_staticBlock _sb) {
        Tokens ts = parse( _sb);
        if( ts == null ){
            return null;
        }
        return new Select(_sb, ts);
    }

    public Select select(InitializerDeclaration pd) {
        Tokens ts = parse( pd);
        if( ts == null ){
            return null;
        }
        return new Select(pd, ts);
    }

    @Override
    public Select selectFirstIn(Node astNode) {
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
        Optional<InitializerDeclaration> opd = astNode.findFirst( InitializerDeclaration.class,
                (InitializerDeclaration pd) -> {
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
    public <N extends Node> N forEachIn(N astNode, Predicate<_staticBlock> nodeMatchFn, Consumer<_staticBlock> nodeActionFn) {
        astNode.walk(InitializerDeclaration.class, pd-> {
            if(matches(pd) && nodeMatchFn.test( _staticBlock.of(pd))){
                nodeActionFn.accept(_staticBlock.of(pd));
            }
        });
        return astNode;
    }

    @Override
    public _staticBlock draft(Translator translator, Map<String, Object> keyValues) {
        if( keyValues.isEmpty() && isMatchAny() ){
            return new _staticBlock(new InitializerDeclaration());
        }
        Statement body = null;
        Object staticBlock = keyValues.get("$staticBlock");
        if( staticBlock != null ){ //check for an override parameter
            keyValues.remove("$staticBlock");
            body = Stmt.block( Stencil.of(staticBlock.toString()).draft(translator, keyValues) );
        } else{
            body = this.body.draft(translator, keyValues);
        }
        return _staticBlock.of(body);
    }

    @Override
    public $staticBlock $(String target, String $Name) {
        if( ! this.isMatchAny() ){
            this.body = this.body.$(target, $Name);
        }
        return this;
    }

    @Override
    public List<String> list$() {
        List<String> strs = this.body.list$();
        strs.addAll( this.body.list$());
        return strs;
    }

    @Override
    public List<String> list$Normalized() {
        List<String> strs = this.body.list$Normalized();
        strs.addAll( this.body.list$Normalized() );
        return strs.stream().distinct().collect(Collectors.toList());
    }

    /**
     * A Matched Selection result returned from matching a prototype $field
     * inside of some Node or _node
     */
    public static class Select implements selected,
            selectAst<InitializerDeclaration>, select_java<_staticBlock> {

        public $tokens tokens;
        public _staticBlock _sb;

        public Select( InitializerDeclaration id, Tokens tokens){
            this._sb = _staticBlock.of(id);
            this.tokens = $tokens.of(tokens);
        }

        public Select( _staticBlock _sb, Tokens tokens){
            this._sb = _sb;
            this.tokens = $tokens.of(tokens);
        }

        @Override
        public $tokens tokens() {
            return tokens;
        }

        @Override
        public InitializerDeclaration ast() {
            return _sb.ast();
        }

        @Override
        public _staticBlock _node() {
            return _sb;
        }
    }
}