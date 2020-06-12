package org.jdraft;

import com.github.javaparser.ast.Node;
import com.github.javaparser.ast.expr.*;
import com.github.javaparser.printer.PrettyPrinterConfiguration;
import org.jdraft.text.Text;

import java.util.Objects;
import java.util.function.Function;
import java.util.function.Predicate;

/**
 * each name-value pair within an {@link _anno}
 * i.e.
 * @A(key="value") (the "key="value"" part)
 *
 * NOTE: we also model the inferred/ hidden name (as "value") if it is not present
 * @A("val") ... (the key is inferred to be "value" and the value is the String "val")
 */
public final class _annoEntryPair implements _tree._node<MemberValuePair, _annoEntryPair>,
        _java._withName<_annoEntryPair>{

    public static final Function<String, _annoEntryPair> PARSER = s-> _annoEntryPair.of(s);

    public static _annoEntryPair of(MemberValuePair mvp){
        return new _annoEntryPair( mvp);
    }

    public static _annoEntryPair of(SingleMemberAnnotationExpr se ){
        return new _annoEntryPair( new MemberValuePair("value", se.getMemberValue()) );
    }
    public static _annoEntryPair of(String name, int value){
        return of( new MemberValuePair(name, new IntegerLiteralExpr(value)));
    }

    public static _annoEntryPair of(String name, boolean value){
        return of( new MemberValuePair(name, new BooleanLiteralExpr(value)));
    }

    public static _annoEntryPair of(String name, char value){
        return of( new MemberValuePair(name, new CharLiteralExpr(value)));
    }

    public static _annoEntryPair of(String name, float value){
        return of( new MemberValuePair(name, new DoubleLiteralExpr(value)));
    }

    public static _annoEntryPair of(String name, double value){
        return of( new MemberValuePair(name, new DoubleLiteralExpr(value)));
    }

    public static _annoEntryPair of(String name, long value){
        return of( new MemberValuePair(name, new LongLiteralExpr(value)));
    }

    public static _annoEntryPair of(String name, _anno _anno){
        return of( new MemberValuePair(name, _anno.ast()));
    }

    //arrays:
    public static _annoEntryPair of(String name, int... value){
        return of( new MemberValuePair(name, _arrayInitExpr.of(value).ast()));
    }

    public static _annoEntryPair of(String name, boolean... value){
        return of( new MemberValuePair(name, _arrayInitExpr.of(value).ast()));
    }

    public static _annoEntryPair of(String name, char... value){
        return of( new MemberValuePair(name, _arrayInitExpr.of(value).ast()));
    }

    public static _annoEntryPair of(String name, float... value){
        return of( new MemberValuePair(name, _arrayInitExpr.of(value).ast()));
    }

    public static _annoEntryPair of(String name, double... value){
        return of( new MemberValuePair(name, _arrayInitExpr.of(value).ast()));
    }

    public static _annoEntryPair of(String name, long... value){
        return of( new MemberValuePair(name, _arrayInitExpr.of(value).ast()));
    }

    public static _annoEntryPair of(String name, _anno... _anno){
        return of( new MemberValuePair(name, _arrayInitExpr.of(_anno).ast()));
    }

    public static _annoEntryPair of(String name, String value){
        return of( new MemberValuePair(name, new StringLiteralExpr(value)));
    }

    public static _annoEntryPair of(String name, Class value){
        return of( new MemberValuePair(name, new ClassExpr(_typeRef.of(value.getCanonicalName()).ast())));
    }

    public static _annoEntryPair of(String name, Class... values){
        return of( new MemberValuePair(name, _arrayInitExpr.of(values).ast()));
    }

    public static _annoEntryPair of(String s){
        return of( new String[]{s});
    }

    public static _annoEntryPair of (String...str ){
        AnnotationExpr ae =
                _anno.of( "@UNKNOWN("+ Text.combine( str)+")" ).ast() ;
                //StaticJavaParser.parseAnnotation( "@UNKNOWN("+ Text.combine( str)+")" );
        if( ae.isNormalAnnotationExpr() ){
            return new _annoEntryPair(ae.asNormalAnnotationExpr().getPairs().get(0));
        }
        else{
            SingleMemberAnnotationExpr sma = (SingleMemberAnnotationExpr)ae;
            MemberValuePair mvp = new MemberValuePair();
            mvp.setValue( sma.getMemberValue() );
            mvp.setName("value");
            _annoEntryPair _mv = new _annoEntryPair(mvp);
            _mv.isValueOnly = true;
            return _mv;
        }
    }

    public static _feature._one<_annoEntryPair, String> NAME = new _feature._one<>(_annoEntryPair.class, String.class,
            _feature._id.NAME,
            a -> a.getName(),
            (_annoEntryPair a, String name) -> a.setName(name), PARSER);

    public static _feature._one<_annoEntryPair, _expr> VALUE = new _feature._one<>(_annoEntryPair.class, _expr.class,
            _feature._id.VALUE,
            a->a.getValue(),
            (_annoEntryPair a, _expr _e)-> a.setValue(_e), PARSER );

    public static _feature._features<_annoEntryPair> FEATURES = _feature._features.of(_annoEntryPair.class,  PARSER, NAME, VALUE);

    public boolean isValueOnly = false;

    public MemberValuePair mvp;

    public _feature._features<_annoEntryPair> features(){
        return FEATURES;
    }

    public _annoEntryPair(MemberValuePair mvp){
        this.mvp = mvp;
    }

    @Override
    public _annoEntryPair copy() {
        return new _annoEntryPair( this.mvp.clone() );
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

    public _annoEntryPair setName(String name){
        this.mvp.setName(name);
        return this;
    }

    public _expr getValue(){
        return _expr.of(this.mvp.getValue());
    }

    public _annoEntryPair setValue(String... ex){
        this.mvp.setValue(Expr.of(ex));
        return this;
    }

    public _annoEntryPair setValue(_expr _e){
        this.mvp.setValue(_e.ast());
        return this;
    }

    public _annoEntryPair setValue(Expression e){
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

    public boolean isValue(Predicate<_expr> _matchFn){
        return _matchFn.test( getValue() );
    }

    public <_IC extends _expr> boolean isValue( Class<_IC> valueImpl, Predicate<_IC> _matchFn){
        if( valueImpl.isAssignableFrom( getValue().getClass() )){
            return _matchFn.test( (_IC)getValue() );
        }
        return false;
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
        if( o instanceof _annoEntryPair){
            _annoEntryPair ot = (_annoEntryPair)o;

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
            return mvp.getValue().toString(ppc);
        }
        return mvp.toString(ppc);
    }
}
