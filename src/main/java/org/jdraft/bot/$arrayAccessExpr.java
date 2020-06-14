package org.jdraft.bot;

import com.github.javaparser.ast.Node;
import com.github.javaparser.ast.expr.ArrayAccessExpr;
import com.github.javaparser.ast.expr.Expression;

import org.jdraft.*;
import org.jdraft.text.*;

import java.util.*;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * $bot for inspecting or mutating an {@link ArrayAccessExpr} or {@link _arrayAccessExpr}
 * which are {@link _expr}s
 */
public class $arrayAccessExpr
        implements $bot.$node<ArrayAccessExpr, _arrayAccessExpr, $arrayAccessExpr>,
        $selector.$node<_arrayAccessExpr, $arrayAccessExpr>,
        $expr<ArrayAccessExpr, _arrayAccessExpr, $arrayAccessExpr>,
        Template<_arrayAccessExpr> {

    public static $arrayAccessExpr of() {
        return new $arrayAccessExpr();
    }

    public static $arrayAccessExpr of(ArrayAccessExpr aae) {
        return new $arrayAccessExpr(aae);
    }

    public static $arrayAccessExpr of(_arrayAccessExpr _aa) {
        return new $arrayAccessExpr(_aa);
    }

    public static $arrayAccessExpr of(String ...code) {
        return new $arrayAccessExpr(_arrayAccessExpr.of(code));
    }

    public static $arrayAccessExpr.Or or($arrayAccessExpr...$aas){
        return new Or($aas);
    }


    /**
     * An Or entity that can match against any of some number of $arrayAccess instances
     * NOTE: this can be used as a selector but NOT as a Template
     */
    public static class Or extends $arrayAccessExpr {

        public List<$arrayAccessExpr> $arrayAccessExprBots = new ArrayList<>();

        private Or($arrayAccessExpr...bots){
            Arrays.stream(bots).forEach(b-> $arrayAccessExprBots.add(b));
        }

        public boolean isMatchAny(){
            return false;
        }

        public boolean matches(ArrayAccessExpr candidate){
            return select(candidate) != null;
        }

        public $arrayAccessExpr.Or copy(){
            $arrayAccessExpr.Or $copy = $arrayAccessExpr.or();
            $copy.$and(this.predicate);
            $copy.name = ($expr)this.name.copy();
            $copy.index = ($expr)this.index.copy();
            this.$arrayAccessExprBots.forEach( ($a) -> $copy.$arrayAccessExprBots.add($a.copy()));
            return $copy;
        }

        /**
         * Return the underlying $arrayAccess that matches the _arrayAccess
         * (or null if none of the $arrayAccess match the candidate _arrayAccess)
         * @param candidate
         * @return
         */
        public $arrayAccessExpr whichMatch(_arrayAccessExpr candidate){
            Optional<$arrayAccessExpr> orsel  = this.$arrayAccessExprBots.stream().filter($p-> $p.matches(candidate) ).findFirst();
            if( orsel.isPresent() ){
                return orsel.get();
            }
            return null;
        }

        public Tokens parse(_arrayAccessExpr candidate){
            $arrayAccessExpr $a = whichMatch(candidate);
            if( $a != null) {
                Select s = $a.select(candidate);
                if( s != null ){
                    return s.tokens;
                }
            }
            return null;
        }

        /**
         *
         * @param _a
         * @return
         */
        public Select<_arrayAccessExpr> select(_arrayAccessExpr _a){
            Select commonSelect = super.select(_a);
            if(  commonSelect == null){
                return null;
            }
            $arrayAccessExpr $whichBot = whichMatch(_a);
            if( $whichBot == null ){
                return null;
            }
            Select whichSelect = $whichBot.select(_a);
            if(!commonSelect.tokens.isConsistent(whichSelect.tokens)){
                return null;
            }
            whichSelect.tokens.putAll(commonSelect.tokens);
            return whichSelect;
        }

        public String toString(){
            StringBuilder sb = new StringBuilder();
            sb.append( "$arrayAccess.Or{").append(System.lineSeparator());
            for(int i = 0; i<this.$arrayAccessExprBots.size(); i++){
                sb.append( Text.indent( this.$arrayAccessExprBots.get(i).toString()) );
            }
            sb.append("}");
            return sb.toString();
        }
    }/* Or */

    public Predicate<_arrayAccessExpr> predicate = (a)->true;

    public $expr name = $expr.of();
    public $expr index = $expr.of();

    /**
     * Build and return a new mutable copy of this bot
     * @return
     */
    public $arrayAccessExpr copy(){
        $arrayAccessExpr $aa = of( ).$and(this.predicate.and(t->true) );
        $aa.name = ($expr)this.name.copy();
        $aa.index = ($expr)this.index.copy();
        return $aa;
    }

    public $arrayAccessExpr() { }

    public $arrayAccessExpr(ArrayAccessExpr aae) {
        this.name = $expr.of(aae.getName());
        this.index = $expr.of(aae.getIndex());
    }

    public $arrayAccessExpr(_arrayAccessExpr _aa) {
        this.name = $expr.of(_aa.getExpression());
        this.index = $expr.of(_aa.getIndex());
    }

    public boolean isMatchAny(){
        if(this.name.isMatchAny() && this.index.isMatchAny()){
            try{
                return this.predicate.test(null);
            }catch(Exception e){
            }
        }
        return false;
    }

    public $arrayAccessExpr $not( $arrayAccessExpr... $sels ){
        return $not( t-> Stream.of($sels).anyMatch($s -> (($bot)$s).matches(t) ) );
    }

    public $arrayAccessExpr $(String target, String $name){
        $listBots().forEach(b -> b.$(target, $name));
        return this;
    }

    @Override
    public $arrayAccessExpr $hardcode(Translator translator, Map<String, Object> keyValues) {
        return this.$hardcode(translator, Tokens.of(keyValues));
    }

    public List<String> $list(){
        List<String> strs = new ArrayList<>();
        $listBots().forEach(b -> strs.addAll( b.$list() ));
        return strs;
    }

    public List<String> $listNormalized(){
        List<String> strs = new ArrayList<>();
        $listBots().forEach(b -> strs.addAll( b.$list() ));
        return strs.stream().distinct().collect(Collectors.toList());
    }

    public List<$bot> $listBots(){
        List<$bot> parts = new ArrayList<>();
        parts.add(name);
        parts.add(index);
        return parts;
    }

    @Override
    public $arrayAccessExpr $hardcode(Translator translator, Tokens kvs) {
        this.name.$hardcode(translator, kvs);
        this.index.$hardcode(translator, kvs);
        return this;
    }

    public Predicate<_arrayAccessExpr> getPredicate(){
        return this.predicate;
    }

    public $arrayAccessExpr setPredicate(Predicate<_arrayAccessExpr> predicate){
        this.predicate = predicate;
        return this;
    }

    public boolean matches(String str){
        return select(str) != null;
    }

    public boolean matches(_java._domain _j) {
        return _j instanceof _arrayAccessExpr && matches((_arrayAccessExpr) _j);
    }

    public Select<_arrayAccessExpr> select(String... str){
        try{
            return select(Expr.arrayAccessExpr(Text.combine(str)));
        } catch(Exception e){
            return null;
        }
    }

    public Select<_arrayAccessExpr> select(Node n) {
        if (n instanceof ArrayAccessExpr) {
            return select(_arrayAccessExpr.of( (ArrayAccessExpr)n) );
        }
        return null;
    }

    public Select<_arrayAccessExpr> select(_arrayAccessExpr _aa) {
        if (getPredicate() == null || getPredicate().test(_aa)) {

            Tokens ts = new Tokens();
            Select s = this.name.select(_aa.getExpression());
            if( s == null) {
                return null;
            }
            ts.putAll(s.tokens);

            s = this.index.select(_aa.getIndex());
            if( s == null) {
                return null;
            }
            ts.putAll(s.tokens);

            return new Select( _aa, ts);

        }
        return null;
    }

    public $arrayAccessExpr $name(){
        this.name = $expr.of();
        return this;
    }

    public $arrayAccessExpr $name($expr $e){
        this.name = $e;
        return this;
    }

    public $arrayAccessExpr $name(_expr _e){
        this.name = $expr.of(_e);
        return this;
    }

    public $arrayAccessExpr $name(Expression astE){
        this.name = $expr.of(astE);
        return this;
    }

    public $arrayAccessExpr $name(String... astE){
        this.name = $e.of(astE);
        return this;
    }

    public $arrayAccessExpr $index(Class<? extends _expr>... exprClasses){
        this.index = $e.of(exprClasses);
        return this;
    }

    public $arrayAccessExpr $index($expr $i ){
        this.index = $i;
        return this;
    }

    public $arrayAccessExpr $index(_expr _e){
        this.index = $expr.of(_e);
        return this;
    }

    public $arrayAccessExpr $index(int index ) {
        this.index = $expr.of(_intExpr.of(index));
        return this;
    }

    public $arrayAccessExpr $index(Expression astE){
        this.index = $expr.of(astE);
        return this;
    }

    public $arrayAccessExpr $index(){
        this.index = $expr.of();
        return this;
    }

    public $arrayAccessExpr $index(String... astE){
        this.index = $e.of(astE);
        return this;
    }

    @Override
    public _arrayAccessExpr draft(Translator translator, Map<String, Object> keyValues) {
        _arrayAccessExpr _aa = _arrayAccessExpr.of();
        _aa.setName( (_expr) this.name.draft(translator, keyValues));
        _aa.setIndex( (_expr) this.index.draft(translator, keyValues));
        return _aa;
    }

    public _arrayAccessExpr instance(ArrayAccessExpr astNode) {
        return _arrayAccessExpr.of(astNode);
    }

    public String toString() {
        return "$arrayAccess{"+ System.lineSeparator()+
                "    "+this.name.toString()+System.lineSeparator()+
                "    "+this.index.toString()+System.lineSeparator()+
                "}";
    }


}

