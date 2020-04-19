package org.jdraft.bot;

import com.github.javaparser.ast.Node;
import com.github.javaparser.ast.expr.ArrayAccessExpr;
import com.github.javaparser.ast.expr.Expression;

import org.jdraft.*;
import org.jdraft.text.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Predicate;

/**
 * Bot for interfacing with {@link ArrayAccessExpr} or {@link _arrayAccess} {@link _expression}s
 */
public class $arrayAccess
        implements $bot.$node<ArrayAccessExpr, _arrayAccess, $arrayAccess>,
        $bot.$multiBot<ArrayAccessExpr, _arrayAccess, $arrayAccess>,
        $selector.$node<_arrayAccess, $arrayAccess>,
        $expression<ArrayAccessExpr, _arrayAccess, $arrayAccess>,
        Template<_arrayAccess> {

    public static $arrayAccess of() {
        return new $arrayAccess();
    }

    public static $arrayAccess of(ArrayAccessExpr aae) {
        return new $arrayAccess(aae);
    }

    public static $arrayAccess of(_arrayAccess  _aa) {
        return new $arrayAccess(_aa);
    }

    public static $arrayAccess of(String ...code) {
        return new $arrayAccess(_arrayAccess.of(code));
    }

    public Predicate<_arrayAccess> predicate = (a)->true;

    public $expression name = $expression.of();
    public $expression index = $expression.of();

    /**
     * Build and return a new mutable copy of this bot
     * @return
     */
    public $arrayAccess copy(){
        $arrayAccess $aa = of( ).$and(this.predicate.and(t->true) );
        $aa.name = ($expression)this.name.copy();
        $aa.index = ($expression)this.index.copy();
        return $aa;
    }

    public $arrayAccess() { }

    public $arrayAccess(ArrayAccessExpr e) {
        this.name = $expression.of(e.getName());
        this.index = $expression.of(e.getIndex());
    }

    public $arrayAccess(_arrayAccess _aa) {
        this.name = $expression.of(_aa.getName());
        this.index = $expression.of(_aa.getIndex());
    }

    public List<$bot> $listBots(){
        List<$bot> parts = new ArrayList<>();
        parts.add(name);
        parts.add(index);
        return parts;
    }

    @Override
    public $arrayAccess $hardcode(Translator translator, Tokens kvs) {
        this.name.$hardcode(translator, kvs);
        this.index.$hardcode(translator, kvs);
        return this;
    }

    public Predicate<_arrayAccess> getPredicate(){
        return this.predicate;
    }

    public $arrayAccess setPredicate( Predicate<_arrayAccess> predicate){
        this.predicate = predicate;
        return this;
    }

    public boolean matches(String str){
        return select(str) != null;
    }

    public boolean matches(_java._domain _j) {
        return _j instanceof _arrayAccess && matches((_arrayAccess) _j);
    }

    public Select<_arrayAccess> select(String... str){
        try{
            return select(Expressions.arrayAccessEx(Text.combine(str)));
        } catch(Exception e){
            return null;
        }
    }

    public Select<_arrayAccess> select(Node n) {
        if (n instanceof ArrayAccessExpr) {
            return select(_arrayAccess.of( (ArrayAccessExpr)n) );
        }
        return null;
    }

    public Select<_arrayAccess> select(_arrayAccess _aa) {
        if (getPredicate() == null || getPredicate().test(_aa)) {

            Tokens ts = new Tokens();
            Select s = this.name.select(_aa.getName());
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

    public $arrayAccess $name(){
        this.name = $expression.of();
        return this;
    }

    public $arrayAccess $name( $expression $e){
        this.name = $e;
        return this;
    }

    public $arrayAccess $name( _expression _e){
        this.name = $expression.of(_e);
        return this;
    }

    public $arrayAccess $name( Expression astE){
        this.name = $expression.of(astE);
        return this;
    }

    public $arrayAccess $name( String... astE){
        this.name = $e.of(astE);
        return this;
    }

    public $arrayAccess $index(Class<? extends _expression>... exprClasses){
        this.index = $e.of(exprClasses);
        return this;
    }

    public $arrayAccess $index( $expression $i ){
        this.index = $i;
        return this;
    }

    public $arrayAccess $index( _expression _e){
        this.index = $expression.of(_e);
        return this;
    }

    public $arrayAccess $index( int index ) {
        this.index = $expression.of(_int.of(index));
        return this;
    }

    public $arrayAccess $index( Expression astE){
        this.index = $expression.of(astE);
        return this;
    }

    public $arrayAccess $index(){
        this.index = $expression.of();
        return this;
    }

    public $arrayAccess $index( String... astE){
        this.index = $e.of(astE);
        return this;
    }

    @Override
    public _arrayAccess draft(Translator translator, Map<String, Object> keyValues) {
        _arrayAccess _aa = _arrayAccess.of();
        _aa.setName( (_expression) this.name.draft(translator, keyValues));
        _aa.setIndex( (_expression) this.index.draft(translator, keyValues));
        return _aa;
    }

    public _arrayAccess instance(ArrayAccessExpr astNode) {
        return _arrayAccess.of(astNode);
    }

    public String toString() {
        return "$arrayAccess{"+ System.lineSeparator()+
                "    "+this.name.toString()+System.lineSeparator()+
                "    "+this.index.toString()+System.lineSeparator()+
                "}";
    }

}

