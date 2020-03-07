package org.jdraft.prototype;

import com.github.javaparser.ast.Node;
import com.github.javaparser.ast.expr.ArrayAccessExpr;
import com.github.javaparser.ast.expr.Expression;

import org.jdraft.*;
import org.jdraft.text.Template;
import org.jdraft.text.Text;
import org.jdraft.text.Tokens;
import org.jdraft.text.Translator;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class $arrayAccess
        implements $prototype.$node<ArrayAccessExpr, _arrayAccess, $arrayAccess>,
        $selector.$node<_arrayAccess, $arrayAccess>,
        $expr<ArrayAccessExpr, _arrayAccess, $arrayAccess>,
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

    public static $arrayAccess of(Predicate<_arrayAccess> matchFn) {
        return new $arrayAccess(matchFn);
    }

    public Predicate<_arrayAccess> predicate = (a)->true;

    public $expr name = $e.of();
    public $expr index = $e.of();

    public $arrayAccess() { }

    public $arrayAccess(ArrayAccessExpr e) {
        this.name = $e.of(e.getName());
        this.index = $e.of(e.getIndex());
    }

    public $arrayAccess(_arrayAccess _aa) {
        this.name = $e.of(_aa.getName());
        this.index = $e.of(_aa.getIndex());
    }

    public Predicate<_arrayAccess> getPredicate(){
        return this.predicate;
    }


    public boolean isMatchAny(){
        if(this.name.isMatchAny() && this.index.isMatchAny() ){
            try{
                return this.predicate.test(null);
            }catch(Exception e){}
        }
        return false;
    }

    public $arrayAccess(Predicate<_arrayAccess> predicate) {
        super();
        $and(predicate);
    }

    public _arrayAccess instance(String... s) {
        return _arrayAccess.of(s);
    }

    public boolean matches(Node node) {
        return select(node) != null;
    }

    public boolean matches(_java._domain _j) {
        return _j instanceof _arrayAccess && matches((_arrayAccess) _j);
    }

    public Selected select(String... str){
        return select(Text.combine(str));
    }

    public Selected select(String str){
        try{
            return select(Ex.arrayAccessEx(str));
        } catch(Exception e){
            return null;
        }
    }

    public Selected select(Node n) {
        if (n instanceof ArrayAccessExpr) {
            return select(_arrayAccess.of( (ArrayAccessExpr)n) );
        }
        return null;
    }

    public Selected select(_arrayAccess _aa) {
        if (this.predicate.test(_aa)) {
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
            return new Selected( _aa, ts);
        }
        return null;
    }

    public $arrayAccess $and(Predicate<_arrayAccess> _matchFn){
        this.predicate = this.predicate.and(_matchFn);
        return this;
    }

    public $arrayAccess $not(Predicate<_arrayAccess> _matchFn){
        this.predicate = this.predicate.and(_matchFn.negate());
        return this;
    }

    public $arrayAccess $(String target, String $name){
        this.name.$(target, $name);
        this.index.$(target, $name);
        return this;
    }

    public List<String> list$(){
        List<String> strs = new ArrayList<>();
        strs.addAll( this.name.list$() );
        strs.addAll( this.index.list$() );
        return strs;
    }

    public List<String> list$Normalized(){
        List<String> strs = new ArrayList<>();
        strs.addAll( this.name.list$Normalized() );
        strs.addAll( this.index.list$Normalized() );
        return strs.stream().distinct().collect(Collectors.toList());
    }

    public $arrayAccess $name( $expr $e){
        this.name = $e;
        return this;
    }

    public $arrayAccess $name( _expression _e){
        this.name = $e.of(_e);
        return this;
    }

    public $arrayAccess $name( Expression astE){
        this.name = $e.of(astE);
        return this;
    }

    public $arrayAccess $name( String... astE){
        this.name = $e.of(astE);
        return this;
    }

    public $arrayAccess $index( $expr $i ){
        this.index = $i;
        return this;
    }

    public $arrayAccess $index( _expression _e){
        this.index = $e.of(_e);
        return this;
    }

    public $arrayAccess $index( int index ) {
        this.index = $e.of(_int.of(index));
        return this;
    }

    public $arrayAccess $index( Expression astE){
        this.index = $e.of(astE);
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

    /*
    public Map<String, $proto> $mapTokens(_arrayAccess _aa) {
        Map<String, $proto> tokens = new HashMap<>();
        this.name = $e.of(_aa.getName());
        this.index = $e.of(_aa.getIndex());

        tokens.put("name", this.name);
        tokens.put("index", this.index);

        System.out.println( tokens );
        return tokens;
    }
     */

    public String toString() {
        return "$arrayAccess{"+ System.lineSeparator()+
                    "    "+this.name.toString()+System.lineSeparator()+
                    "    "+this.index.toString()+System.lineSeparator()+
                "}";
    }

    /**
     * This makes it easier to NOT have to do silly things with generics on the outside
     */
    public static class Selected extends Select<_arrayAccess>{
        public Selected(_arrayAccess _node, Tokens tokens) {
            super(_node, tokens);
        }
    }
}

