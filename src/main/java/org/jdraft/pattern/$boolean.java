package org.jdraft.pattern;

import com.github.javaparser.ast.expr.BooleanLiteralExpr;
import org.jdraft._boolean;

/**
 * boolean literal pattern within code
 */
public class $boolean extends $ex<BooleanLiteralExpr, _boolean> {

    //match ANY boolean literal (any literal true or false)
    public static $boolean of(){
        return new $boolean();
    }

    public static $boolean of(String...code){
        return of( _boolean.of(code));
    }

    public static $boolean of( BooleanLiteralExpr b){
        return of( _boolean.of(b));
    }

    public static $boolean of(boolean b){
        return new $boolean(b);
    }

    public static $boolean of(_boolean _b){
        $boolean $ds = of(_b.getValue());
        return $ds;
    }

    public $boolean (){
        super(BooleanLiteralExpr.class, "$bool$");
    }

    public $boolean (boolean b){
        super(BooleanLiteralExpr.class, ""+b);
    }

    public String toString(){

        return "$boolean{"+System.lineSeparator()
                +"    "+this.exprStencil.toString()+System.lineSeparator()+
                "}";
    }

}
