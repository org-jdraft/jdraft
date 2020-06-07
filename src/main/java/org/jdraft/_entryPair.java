package org.jdraft;

import com.github.javaparser.ast.Node;
import com.github.javaparser.ast.expr.*;
import com.github.javaparser.printer.PrettyPrinterConfiguration;
import org.jdraft.text.Text;

import java.util.Objects;
import java.util.function.Function;

/**
 * each name-value pair within an {@link _annoExpr}
 * i.e.
 * @A(key="value") (the "key="value"" part)
 *
 * NOTE: we also model the inferred/ hidden name (as "value") if it is not present
 * @A("val") ... (the key is inferred to be "value" and the value is the String "val")
 */
public final class _entryPair implements _java._node<MemberValuePair, _entryPair>,
        _java._withName<_entryPair>{

    public static final Function<String, _entryPair> PARSER = s-> _entryPair.of(s);

    public static _entryPair of(MemberValuePair mvp){
        return new _entryPair( mvp);
    }

    public static _entryPair of(SingleMemberAnnotationExpr se ){
        return new _entryPair( new MemberValuePair("value", se.getMemberValue()) );
    }
    public static _entryPair of(String name, int value){
        return of( new MemberValuePair(name, new IntegerLiteralExpr(value)));
    }

    public static _entryPair of(String name, boolean value){
        return of( new MemberValuePair(name, new BooleanLiteralExpr(value)));
    }

    public static _entryPair of(String name, char value){
        return of( new MemberValuePair(name, new CharLiteralExpr(value)));
    }

    public static _entryPair of(String name, float value){
        return of( new MemberValuePair(name, new DoubleLiteralExpr(value)));
    }

    public static _entryPair of(String name, double value){
        return of( new MemberValuePair(name, new DoubleLiteralExpr(value)));
    }

    public static _entryPair of(String name, long value){
        return of( new MemberValuePair(name, new LongLiteralExpr(value)));
    }

    public static _entryPair of(String name, _annoExpr _anno){
        return of( new MemberValuePair(name, _anno.ast()));
    }

    //arrays:
    public static _entryPair of(String name, int... value){
        return of( new MemberValuePair(name, _arrayInitExpr.of(value).ast()));
    }

    public static _entryPair of(String name, boolean... value){
        return of( new MemberValuePair(name, _arrayInitExpr.of(value).ast()));
    }

    public static _entryPair of(String name, char... value){
        return of( new MemberValuePair(name, _arrayInitExpr.of(value).ast()));
    }

    public static _entryPair of(String name, float... value){
        return of( new MemberValuePair(name, _arrayInitExpr.of(value).ast()));
    }

    public static _entryPair of(String name, double... value){
        return of( new MemberValuePair(name, _arrayInitExpr.of(value).ast()));
    }

    public static _entryPair of(String name, long... value){
        return of( new MemberValuePair(name, _arrayInitExpr.of(value).ast()));
    }

    public static _entryPair of(String name, _annoExpr... _anno){
        return of( new MemberValuePair(name, _arrayInitExpr.of(_anno).ast()));
    }

    public static _entryPair of(String name, String value){
        return of( new MemberValuePair(name, new StringLiteralExpr(value)));
    }

    public static _entryPair of(String name, Class value){
        return of( new MemberValuePair(name, new ClassExpr(_typeRef.of(value.getCanonicalName()).ast())));
    }

    public static _entryPair of(String name, Class... values){
        return of( new MemberValuePair(name, _arrayInitExpr.of(values).ast()));
    }

    public static _entryPair of(String s){
        return of( new String[]{s});
    }

    public static _entryPair of (String...str ){
        AnnotationExpr ae =
                _annoExpr.of( "@UNKNOWN("+ Text.combine( str)+")" ).ast() ;
                //StaticJavaParser.parseAnnotation( "@UNKNOWN("+ Text.combine( str)+")" );
        if( ae.isNormalAnnotationExpr() ){
            return new _entryPair(ae.asNormalAnnotationExpr().getPairs().get(0));
        }
        else{
            SingleMemberAnnotationExpr sma = (SingleMemberAnnotationExpr)ae;
            MemberValuePair mvp = new MemberValuePair();
            mvp.setValue( sma.getMemberValue() );
            mvp.setName("value");
            _entryPair _mv = new _entryPair(mvp);
            _mv.isValueOnly = true;
            return _mv;
        }
    }

    public static _feature._one<_entryPair, String> NAME = new _feature._one<>(_entryPair.class, String.class,
            _feature._id.NAME,
            a -> a.getName(),
            (_entryPair a, String name) -> a.setName(name), PARSER);

    public static _feature._one<_entryPair, _expr> VALUE = new _feature._one<>(_entryPair.class, _expr.class,
            _feature._id.VALUE,
            a->a.getValue(),
            (_entryPair a, _expr _e)-> a.setValue(_e), PARSER );

    public static _feature._features<_entryPair> FEATURES = _feature._features.of(_entryPair.class, NAME, VALUE);

    public boolean isValueOnly = false;

    public MemberValuePair mvp;

    public _feature._features<_entryPair> features(){
        return FEATURES;
    }

    public _entryPair(MemberValuePair mvp){
        this.mvp = mvp;
    }

    @Override
    public _entryPair copy() {
        return new _entryPair( this.mvp.clone() );
    }

    @Override
    public MemberValuePair ast() {
        return mvp;
    }

    public boolean isValueOnly(){
        return this.isValueOnly;
    }

    public String getName(){
        return this.mvp.getNameAsString();
    }

    public Node getNameNode() {
        return this.mvp.getName();
    }

    public _entryPair setName(String name){
        this.mvp.setName(name);
        return this;
    }

    public _expr getValue(){
        return _expr.of(this.mvp.getValue());
    }

    public _entryPair setValue(String... ex){
        this.mvp.setValue(Expr.of(ex));
        return this;
    }

    public _entryPair setValue(_expr _e){
        this.mvp.setValue(_e.ast());
        return this;
    }

    public _entryPair setValue(Expression e){
        this.mvp.setValue(e);
        return this;
    }

    public boolean isValue( String... ex){
        try {
            return Expr.equal(this.mvp.getValue(), Expr.of(ex));
        }catch(Exception e){
            return false;
        }
    }

    public boolean isValue( _expr _e){
        return Expr.equal(this.mvp.getValue(), _e.ast());
    }

    public boolean isValue( Expression e){
        return Expr.equal(this.mvp.getValue(), e);
    }

    public boolean isValue( boolean b){
        return isValue(Expr.of(b));
    }

    public boolean isValue( int i){
        return isValue(Expr.of(i));
    }

    public boolean isValue( char c){
        return isValue(Expr.of(c));
    }

    public boolean isValue( float f){
        return isValue(Expr.of(f));
    }

    public boolean isValue( long l){
        return isValue(Expr.of(l));
    }

    public boolean isValue( double d){
        return isValue(Expr.of(d));
    }

    /*
    @Override
    public boolean is(String... stringRep) {
        try{
            return of(stringRep).equals(this);
        } catch(Exception e){
            return false;
        }
    }
     */

    public boolean equals(Object o){
        if( o instanceof _entryPair){
            _entryPair ot = (_entryPair)o;

            boolean same = Objects.equals( ot.getName(), this.mvp.getNameAsString() )
                    && Objects.equals( ot.getValue().toString(), this.mvp.getValue().toString() );

            return same;
        }
        return false;
    }

    public int hashCode(){
        if( this.mvp.getNameAsString().equals("value") && this.isValueOnly ){
            return 31 * this.mvp.getValue().hashCode();
        }
        return 31 * this.mvp.hashCode();
    }

    public String toString(){
        if( this.mvp.getNameAsString().equals("value") && this.isValueOnly ){
            return mvp.getValue().toString();
        }
        return mvp.toString();
    }

    public String toString( PrettyPrinterConfiguration ppc ){
        if( this.mvp.getNameAsString().equals("value") && this.isValueOnly ){
            return mvp.toString();
        }
        return mvp.toString(ppc);
    }
}
