package org.jdraft.pattern;

import com.github.javaparser.ast.expr.ArrayAccessExpr;
import org.jdraft._arrayAccess;
import org.jdraft._expression;
import org.jdraft.text.Tokens;
import org.jdraft.text.Translator;

import java.util.Map;
import java.util.function.Predicate;

/**
 * boolean literal pattern within code
 */
public class $arrayAccess extends $ex<ArrayAccessExpr, _arrayAccess, $arrayAccess> {

    //match ANY boolean literal (any literal true or false)
    public static $arrayAccess of(){
        return new $arrayAccess();
    }

    public static $arrayAccess of(String...code){
        return of( _arrayAccess.of(code));
    }

    public static $arrayAccess of(ArrayAccessExpr b){
        return of( _arrayAccess.of(b));
    }

    public static $arrayAccess of(_arrayAccess _a){
        $arrayAccess $ds = of(_a);
        return $ds;
    }

    public static $arrayAccess off(Predicate<_arrayAccess> matchFn){
        return of().$and(matchFn);
    }

    public $arrayAccess(){
        super(ArrayAccessExpr.class, null);
    }

    public boolean matches(_arrayAccess _b){
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

    public Select<ArrayAccessExpr, _arrayAccess> select( _expression _e){
        if( _e instanceof _arrayAccess){

            _arrayAccess _aa = (_arrayAccess)_e;

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
                Select s = this.name.select(_aa.getName());
                if( s == null ){
                    return null;
                }
                all.putAll( s.tokens );
                s = this.index.select(_aa.getIndex());
                if( s == null ){
                    return null;
                }
                all.putAll(s.tokens);
                return new $ex.Select<ArrayAccessExpr, _arrayAccess>(_aa, all);
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
    public _arrayAccess draft(Translator translator, Map<String,Object> keyValues ){
        ArrayAccessExpr aae =  new ArrayAccessExpr();
        aae.setName( name.draft(translator, keyValues).ast() );
        aae.setIndex( index.draft(translator, keyValues).ast());
        return _arrayAccess.of(aae );
        //return super.draft(translator, Tokens.of(keyValues));
    }

    public String toString(){
        return "$arrayAccess{"+System.lineSeparator()
                +"    "+this.exprStencil.toString()+System.lineSeparator()+
                "}";
    }

}
