package org.jdraft.pattern;

import com.github.javaparser.ast.expr.ArrayAccessExpr;
import org.jdraft._arrayAccessExpr;
import org.jdraft._expr;
import org.jdraft.text.Tokens;
import org.jdraft.text.Translator;

import java.util.Map;
import java.util.function.Predicate;

/**
 * boolean literal pattern within code
 */
public class $arrayAccess extends $ex<ArrayAccessExpr, _arrayAccessExpr, $arrayAccess> {

    //match ANY boolean literal (any literal true or false)
    public static $arrayAccess of(){
        return new $arrayAccess();
    }

    public static $arrayAccess of(String...code){
        return of( _arrayAccessExpr.of(code));
    }

    public static $arrayAccess of(ArrayAccessExpr b){
        return of( _arrayAccessExpr.of(b));
    }

    public static $arrayAccess of(_arrayAccessExpr _a){
        $arrayAccess $ds = of(_a);
        return $ds;
    }

    public static $arrayAccess off(Predicate<_arrayAccessExpr> matchFn){
        return of().$and(matchFn);
    }

    public $arrayAccess(){
        super(ArrayAccessExpr.class, null);
    }

    public boolean matches(_arrayAccessExpr _b){
        return select(_b) != null;
    }

    public $ex name = $ex.any();
    public $ex index = $ex.any();

    public $arrayAccess $name( $ex $e){
        this.name = $e;
        return this;
    }

    public $arrayAccess $index( $ex $i ){
        this.index = $i;
        return this;
    }

    public Select<ArrayAccessExpr, _arrayAccessExpr> select(_expr _e){
        if( _e instanceof _arrayAccessExpr){

            _arrayAccessExpr _aa = (_arrayAccessExpr)_e;

            if( this.constraint.test(_aa)){
                //this seems wrong
                /*
                if( this.exprStencil != null ) {
                    Tokens ts = this.exprStencil.parse(_aa.aae.toString());
                    if (ts == null) {
                        return null;
                    }
                }
                */
                Tokens all = new Tokens();
                Select s = this.name.select(_aa.getExpression());
                if( s == null ){
                    return null;
                }
                all.putAll( s.tokens );
                s = this.index.select(_aa.getIndex());
                if( s == null ){
                    return null;
                }
                all.putAll(s.tokens);
                return new $ex.Select<ArrayAccessExpr, _arrayAccessExpr>(_aa, all);
            }
        }
        return null;
    }

    /**
     * construct and return a new T given the tokens
     *
     * @param keyValues alternating key, and values
     * @return
     */
    public _arrayAccessExpr draft(Translator translator, Map<String,Object> keyValues ){
        ArrayAccessExpr aae =  new ArrayAccessExpr();
        aae.setName( name.draft(translator, keyValues).ast() );
        aae.setIndex( index.draft(translator, keyValues).ast());
        return _arrayAccessExpr.of(aae );
        //return super.draft(translator, Tokens.of(keyValues));
    }

    public String toString(){
        return "$arrayAccess{"+System.lineSeparator()
                +"    "+this.exprStencil.toString()+System.lineSeparator()+
                "}";
    }

}
