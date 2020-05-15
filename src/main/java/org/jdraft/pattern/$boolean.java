package org.jdraft.pattern;

import com.github.javaparser.ast.expr.BooleanLiteralExpr;
import org.jdraft._booleanExpr;
import org.jdraft.text.Tokens;
import org.jdraft.text.Translator;

import java.util.Map;
import java.util.function.Predicate;

/**
 * boolean literal pattern within code
 */
public class $boolean extends $ex<BooleanLiteralExpr, _booleanExpr, $boolean> {

    //match ANY boolean literal (any literal true or false)
    public static $boolean of(){
        return new $boolean();
    }

    public static $boolean of(String...code){
        return of( _booleanExpr.of(code));
    }

    public static $boolean of( BooleanLiteralExpr b){
        return of( _booleanExpr.of(b));
    }

    public static $boolean of(boolean b){
        return new $boolean(b);
    }

    public static $ex<BooleanLiteralExpr, _booleanExpr, $ex> or($boolean... $bs){
        return $ex.or($bs);
    }

    public static $boolean of(_booleanExpr _b){
        $boolean $ds = of(_b.getValue());
        return $ds;
    }

    public static $boolean off(Predicate<_booleanExpr> booleanMatchFn){
        return of().$and(booleanMatchFn);
    }

    public $boolean (){
        super(BooleanLiteralExpr.class, null);
    }

    public $boolean (boolean b){
        super(BooleanLiteralExpr.class, ""+b);
    }

    public boolean matches( boolean b){
        return select(_booleanExpr.of(b)) != null;
    }

    public boolean matches(_booleanExpr _b){
        return select(_b) != null;
    }

    /**
     * construct and return a new T given the tokens
     *
     * @param keyValues alternating key, and values
     * @return
     */
    public _booleanExpr draft(Translator translator, Map<String,Object> keyValues ){
        return super.draft(Translator.DEFAULT_TRANSLATOR, Tokens.of(keyValues));
    }

    public String toString(){
        return "$boolean{"+System.lineSeparator()
                +"    "+this.exprStencil.toString()+System.lineSeparator()+
                "}";
    }

}
