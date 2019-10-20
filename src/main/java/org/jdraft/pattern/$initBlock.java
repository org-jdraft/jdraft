package org.jdraft.pattern;

import com.github.javaparser.ast.Node;
import com.github.javaparser.ast.body.InitializerDeclaration;
import com.github.javaparser.ast.stmt.BlockStmt;
import com.github.javaparser.ast.stmt.Statement;
import org.jdraft.*;

import java.util.*;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Predicate;
import java.util.stream.Collectors;

/**
 * $proto on modelling the package declaration
 *
 * @see InitializerDeclaration
 */
public class $initBlock implements $pattern<_initBlock, $initBlock>, $pattern.$java<_initBlock,$initBlock>,
        Template<_initBlock>, $class.$part, $enum.$part, $member<_initBlock, $initBlock>{

    public Class<_initBlock> _modelType(){
        return _initBlock.class;
    }

    /** contents of the body */
    public $body body;

    /**
     * true means MUST BE STATIC,
     * null means can be static or non-static,
     * false means must be NON-STATIC
     */
    public Boolean isStatic = null;

    public Predicate<_initBlock> constraint = t->true;

    public static $initBlock of(){
        return new $initBlock($body.of(), null, t->true);
    }

    public static $initBlock of(String bodyPattern ){
        return of( new String[]{bodyPattern});
    }


    public static $initBlock.Or or( InitializerDeclaration... _protos ){
        $initBlock[] arr = new $initBlock[_protos.length];
        for(int i=0;i<_protos.length;i++){
            arr[i] = $initBlock.of( _protos[i]);
        }
        return or(arr);
    }

    public static $initBlock.Or or( $initBlock...$tps ){
        return new $initBlock.Or($tps);
    }

    public static $initBlock as(_initBlock _ib){
        return as( _ib.ast());
    }

    public static $initBlock as(InitializerDeclaration astId){
        //String body = astId.getBody().toString();
        $initBlock $ib = new $initBlock( $body.as(astId.getBody()), astId.isStatic(), t->true );
        return $ib;
    }

    public static $initBlock as(String... bodyPattern ){
        InitializerDeclaration id = Ast.initBlock(bodyPattern);
        return as(id);
    }

    public static $initBlock as(Ex.Command lambdaWithBody){
        Statement bdy = _lambda.from( Thread.currentThread().getStackTrace()[2]).getBody();
        return as(bdy);
    }

    public static <A extends Object> $initBlock as (Consumer<A> lambdaWithBody){
        Statement bdy = _lambda.from( Thread.currentThread().getStackTrace()[2]).getBody();
        return as(bdy);
    }

    public static <A extends Object, B extends Object> $initBlock as(BiConsumer<A,B> lambdaWithBody ){
        Statement bdy = _lambda.from( Thread.currentThread().getStackTrace()[2]).getBody();
        return as(bdy);
    }

    public static <A extends Object, B extends Object,C extends Object> $initBlock as(Ex.TriConsumer<A,B,C> lambdaWithBody ){
        Statement bdy = _lambda.from( Thread.currentThread().getStackTrace()[2]).getBody();
        return as(bdy);
    }

    public static <A extends Object, B extends Object,C extends Object, D extends Object> $initBlock as(Ex.QuadConsumer<A,B,C,D> lambdaWithBody ){
        Statement bdy = _lambda.from( Thread.currentThread().getStackTrace()[2]).getBody();
        return as(bdy);
    }

    public static $initBlock as(Statement st){
        if( st instanceof BlockStmt ){
            //return new $initBlock( $stmt.blockStmt((BlockStmt)st), null, t->true );
            return new $initBlock( $body.as((BlockStmt)st), null, t->true );
        } else{
            //return new $initBlock( $stmt.blockStmt( new BlockStmt().addStatement( st) ), null, t->true );
            return new $initBlock( $body.as( new BlockStmt().addStatement( st) ), null, t->true );
        }
    }

    public static $initBlock of( _initBlock _ib ){
        return of( _ib.ast());
    }

    public static $initBlock of( InitializerDeclaration id ){
        //$initBlock $ib = new $initBlock( $stmt.of( BlockStmt.class, id.getBody().toString()), id.isStatic(), t->true );
        $initBlock $ib = new $initBlock( $body.of( id), id.isStatic(), t->true );
        return $ib;
    }

    public static $initBlock of(String... bodyPattern ){
        InitializerDeclaration id = Ast.initBlock(bodyPattern);
        $initBlock $ib = new $initBlock( $body.of( id ), id.isStatic(), t->true );
        return $ib;
    }

    public static $initBlock of( Predicate<_initBlock> matchFn ){
        return new $initBlock( $body.of(), null, matchFn );
    }

    public static $initBlock of(String str, Predicate<_initBlock> matchFn ){
        return of(str).$and(matchFn);
    }

    public static $initBlock of(Statement st){
        if( st instanceof BlockStmt ){
            //return new $initBlock( $stmt.blockStmt((BlockStmt)st), null, t->true );
            return new $initBlock( $body.of((BlockStmt)st), null, t->true );
        } else{
            //return new $initBlock( $stmt.blockStmt( new BlockStmt().addStatement( st) ), null, t->true );
            return new $initBlock( $body.of( new BlockStmt().addStatement( st) ), null, t->true );
        }
    }

    public static $initBlock of(Ex.Command lambdaWithBody){
        Statement bdy = _lambda.from( Thread.currentThread().getStackTrace()[2]).getBody();
        return of(bdy);
    }

    public static <A extends Object> $initBlock of (Consumer<A> lambdaWithBody){
        Statement bdy = _lambda.from( Thread.currentThread().getStackTrace()[2]).getBody();
        return of(bdy);
    }

    public static <A extends Object, B extends Object> $initBlock of(BiConsumer<A,B> lambdaWithBody ){
        Statement bdy = _lambda.from( Thread.currentThread().getStackTrace()[2]).getBody();
        return of(bdy);
    }

    public static <A extends Object, B extends Object,C extends Object> $initBlock of(Ex.TriConsumer<A,B,C> lambdaWithBody ){
        Statement bdy = _lambda.from( Thread.currentThread().getStackTrace()[2]).getBody();
        return of(bdy);
    }

    public static <A extends Object, B extends Object,C extends Object, D extends Object> $initBlock of(Ex.QuadConsumer<A,B,C,D> lambdaWithBody ){
        Statement bdy = _lambda.from( Thread.currentThread().getStackTrace()[2]).getBody();
        return of(bdy);
    }

    private $initBlock(){
        this.body = $body.of();
        this.isStatic = null;
    }
    //public $initBlock($stmt<BlockStmt> $body, Boolean isStatic, Predicate<_initBlock> constraint){
    public $initBlock($body $body, Boolean isStatic, Predicate<_initBlock> constraint){
        //the pattern must be a valid package name
        this.body = $body;
        this.isStatic = isStatic;
        this.constraint = constraint;
    }

    @Override
    public $initBlock $and(Predicate<_initBlock> constraint) {
        this.constraint = this.constraint.and(constraint);
        return null;
    }

    public $initBlock setStatic(){
        return setStatic(true);
    }

    public $initBlock setStatic(Boolean toSet){
        this.isStatic = toSet;
        return this;
    }

    @Override
    public $initBlock hardcode$(Translator translator, Tokens kvs) {
        this.body = this.body.hardcode$(translator, kvs);
        return this;
    }

    public String toString(){
         if( isMatchAny() ){
             return "$initBlock{ $ANY$ }";
         }
         StringBuilder sb = new StringBuilder();
         if( this.isStatic != null && this.isStatic ){
             sb.append( "$initBlock{ static " );
         } else{
             sb.append( "$initBlock{ " );
         }
         sb.append( Text.indent( this.body.toString() ) );
         sb.append("}");
         return sb.toString();
    }

    @Override
    public boolean match(Node candidate) {
        if( candidate instanceof InitializerDeclaration  ){
            InitializerDeclaration pd = (InitializerDeclaration)candidate;
            return matches(pd);
        }
        return false;
    }

    public boolean matches(String... initBlock){
        try{
            return matches(Ast.staticBlock(initBlock));
        } catch(Exception e){
            return false;
        }
    }

    public boolean matches(InitializerDeclaration initBlock){
        if( initBlock == null ){
            return isMatchAny();
        }
        return matches(_initBlock.of(initBlock));
    }

    public boolean matches( _initBlock staticBlock ){
        if( staticBlock == null ){
            return isMatchAny();
        }
        if( constraint.test(staticBlock)){
            if( this.isStatic == null ) { //we dont care
                return this.body.matches(staticBlock);
                //return this.body.matches(staticBlock.ast().getBody());
            }
            //make sure we check static exist and body
            return this.isStatic == staticBlock.isStatic()
                    && this.body.matches(staticBlock);
        }
        return false;
    }

    public boolean isMatchAny(){
        try{
            return constraint.test(null) &&
                    this.isStatic == null &&
                    this.body.constraint.test(null) &&
                    this.body.isMatchAny();
                    //this.body.stmtStencil.isMatchAny();
        } catch(Exception e){

        }
        return false;
    }
    @Override
    public _initBlock firstIn(Node astStartNode, Predicate<_initBlock> nodeMatchFn) {
        Optional<InitializerDeclaration> opd = astStartNode.findFirst(InitializerDeclaration.class, pd-> matches(pd) && nodeMatchFn.test(_initBlock.of(pd)));
        if( opd.isPresent() ){
            return _initBlock.of(opd.get());
        }
        return null;
    }

    public Tokens parse (_initBlock _sb ){
        if( constraint.test(_sb) ){
            return body.select( _sb ).tokens().asTokens();
        }
        return null;
    }

    public Tokens parse( InitializerDeclaration pd ){
        if(pd == null && isMatchAny() ){
            return new Tokens();
        }
        return parse( _initBlock.of(pd));
    }

    @Override
    public Select select(_initBlock _ib) {
        Tokens ts = parse( _ib);
        if( ts == null ){
            return null;
        }
        return new Select(_ib, ts);
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
    public <N extends Node> N forEachIn(N astNode, Predicate<_initBlock> nodeMatchFn, Consumer<_initBlock> nodeActionFn) {
        astNode.walk(InitializerDeclaration.class, pd-> {
            if(matches(pd) && nodeMatchFn.test( _initBlock.of(pd))){
                nodeActionFn.accept(_initBlock.of(pd));
            }
        });
        return astNode;
    }

    @Override
    public _initBlock draft(Translator translator, Map<String, Object> keyValues) {
        if( keyValues.isEmpty() && isMatchAny() ){
            return new _initBlock(new InitializerDeclaration());
        }
        Statement body = null;
        Object staticBlock = keyValues.get("$initBlock");
        if( staticBlock != null ){ //check for an override parameter
            keyValues.remove("$initBlock");
            //body = $body.of( Stencil.of(staticBlock.toString()).draft(translator, keyValues) );
            body = Stmt.blockStmt( Stencil.of(staticBlock.toString()).draft(translator, keyValues) );
        } else{
            body = this.body.draft(translator, keyValues).ast();
        }
        Object isStatic = keyValues.get("$static");
        if( isStatic != null && isStatic.equals(false) ){
            return _initBlock.of(body);
        }
        //probably a static block
        return _initBlock.of(body).setStatic();
    }

    @Override
    public $initBlock $(String target, String $paramName) {
        if( ! this.isMatchAny() ){
            this.body = this.body.$(target, $paramName);
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
     * An Or entity that can match against any of the $pattern instances provided
     * NOTE: template features (draft/fill) are suppressed.
     */
    public static class Or extends $initBlock{

        final List<$initBlock>ors = new ArrayList<>();

        public Or($initBlock...$as){
            super();
            Arrays.stream($as).forEach($a -> ors.add($a) );
        }

        @Override
        public $initBlock hardcode$(Translator translator, Tokens kvs) {
            ors.forEach( $a -> $a.hardcode$(translator, kvs));
            return this;
        }

        @Override
        public String toString(){
            StringBuilder sb = new StringBuilder();
            sb.append("$initBlock.Or{");
            sb.append(System.lineSeparator());
            ors.forEach($a -> sb.append( Text.indent($a.toString()) ) );
            sb.append("}");
            return sb.toString();
        }

        /**
         *
         * @param astNode
         * @return
         */
        public $initBlock.Select select(InitializerDeclaration astNode){
            $initBlock $a = whichMatch(astNode);
            if( $a != null ){
                return $a.select(astNode);
            }
            return null;
        }

        public boolean isMatchAny(){
            return false;
        }

        /**
         * Return the underlying $anno that matches the InitBlock or null if none of the match
         * @param ae
         * @return
         */
        public $initBlock whichMatch(InitializerDeclaration ae){
            Optional<$initBlock> orsel  = this.ors.stream().filter( $p-> $p.match(ae) ).findFirst();
            if( orsel.isPresent() ){
                return orsel.get();
            }
            return null;
        }
    }

    /**
     * A Matched Selection result returned from matching a prototype $field
     * inside of some Node or _node
     */
    public static class Select implements selected,
            selectAst<InitializerDeclaration>, select_java<_initBlock> {

        public $tokens tokens;
        public _initBlock initBlock;

        public Select( InitializerDeclaration id, Tokens tokens){
            this.initBlock = _initBlock.of(id);
            this.tokens = $tokens.of(tokens);
        }

        public Select(_initBlock initBlock, Tokens tokens){
            this.initBlock = initBlock;
            this.tokens = $tokens.of(tokens);
        }

        @Override
        public $tokens tokens() {
            return tokens;
        }

        public boolean isStatic(){ return this.initBlock.isStatic(); }

        @Override
        public InitializerDeclaration ast() {
            return initBlock.ast();
        }

        @Override
        public _initBlock _node() {
            return initBlock;
        }
    }
}