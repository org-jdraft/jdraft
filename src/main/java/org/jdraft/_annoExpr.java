package org.jdraft;

import java.lang.annotation.Annotation;
import java.util.*;
import java.util.function.BiPredicate;
import java.util.function.Consumer;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import com.github.javaparser.StaticJavaParser;
import com.github.javaparser.ast.Node;
import com.github.javaparser.ast.NodeList;
import com.github.javaparser.ast.body.BodyDeclaration;
import com.github.javaparser.ast.expr.*;
import com.github.javaparser.ast.nodeTypes.NodeWithAnnotations;
import com.github.javaparser.ast.type.Type;
import com.github.javaparser.printer.PrettyPrinterConfiguration;
import org.jdraft.text.Text;

/**
 * Instance of a single @ annotation (i.e. a "Reference" to an {@link _annotation} (
 * i.e. (as applied to a field "int a;")
 * <UL>
 * <LI> @base int a; : "naked" annotation</LI>
 * <LI> @singleValue("VALUE") int a; : "VALUE" annotation</LI>
 * <LI> @keyValue(key="VALUE",anotherKey=VALUE) int a; : "key-VALUE pair"
 * annotation</LI>
 * </UL>
 *
 * <UL>
 * <LI>{@link _class} i.e. @Deprecated class C{...}
 * <LI>{@link _enum} i.e. @Deprecated enum E{...}
 * <LI>{@link _interface} i.e. @Deprecated interface I{...}
 * <LI>{@link _annotation} i.e. @Deprecated @interface A{...}
 * <LI>{@link _field} i.e. @nonnull String NAME;
 * <LI>{@link _method} i.e. @Deprecated String toString(){...}
 * <LI>{@link _constant} @Deprecated E(2)
 * <LI>{@link _parameter} (@nonNull Object VALUE)
 * </UL>
 * <PRE>{@code
 * @name
 * @name("value")
 * @name(key="value")
 * @name(key1="value1", key2="value2")
 * }</PRE>
 *
 * @see _annoExprs which represents ALL annotations applied to an AnnotatedEntity
 * @author Eric
 */
public final class _annoExpr
        implements _expr<AnnotationExpr, _annoExpr>, _java._withName<_annoExpr>, _java._multiPart<AnnotationExpr, _annoExpr> {

    public static _annoExpr of(String anno ){
        return of( new String[]{anno} );
    }

    public static _annoExpr of(){
        return new _annoExpr( new MarkerAnnotationExpr() );
    }

    public static _annoExpr of(String... annotation ) {
        return new _annoExpr( Ast.anno( annotation ) );
    }

    /**
     * Look through the anonymous Object to find some annotated entity
     * and extract & model the first annotation as a _draft _anno
     * @param anonymousObject an anonymous Object containing an annotated entity
     * @return the _anno _draft object
     */
    public static _annoExpr of(Object anonymousObject ){
        StackTraceElement ste = Thread.currentThread().getStackTrace()[2];
        ObjectCreationExpr oce = Exprs.newEx(ste);
        NodeList<BodyDeclaration<?>> bds = oce.getAnonymousClassBody().get();
        BodyDeclaration<?> bd = bds.stream().filter(b -> b.getAnnotations().isNonEmpty() ).findFirst().get();
        return of( bd.getAnnotation(0) );
    }

    public static _annoExpr of(Class<? extends Annotation> annotationClass){
        return of( new MarkerAnnotationExpr(annotationClass.getSimpleName()));
    }
    public static _annoExpr of(AnnotationExpr astAnno ) {
        return new _annoExpr( astAnno );
    }
    
    private AnnotationExpr astAnno;

    public _annoExpr(AnnotationExpr astAnno ) {
        this.astAnno = astAnno;
    }

    @Override
    public _annoExpr copy() {
        return new _annoExpr( this.astAnno.clone() );
    }

    @Override
    public String getName() {
        return this.astAnno.getNameAsString();
    }

    public Name getNameNode(){ return this.astAnno.getName(); }

    @Override
    public _annoExpr setName(String name ){
        this.astAnno.setName(name);
        return this;
    }

    /**
     * Is this _anno a "marker" annotation (i.e. with know value / or keyValues)
     * i.e. <PRE>{@code
     * @Deprecated //a "Marker" annotation type
     * 
     * //...as apposed to:
     * @Generated("2/15/2018") //a singleMember annotation type
     * 
     * @KeyValues(key=1,value=2) //a keyValue Annotation Type
     * }</PRE>
     * @return 
     */
    public boolean isMarker(){
        return this.astAnno.isMarkerAnnotationExpr();
    }
    
    /**
     * i.e. <PRE>{@code
     * @Deprecated //a "Marker" annotation type
     * 
     * //...as apposed to:
     * @Generated("2/15/2018") //a singleMember annotation type
     * 
     * @KeyValues(key=1,value=2) //a keyValue Annotation Type
     * }</PRE>
     * 
     * @return 
     */
    public boolean isSingleMember(){
        return this.astAnno.isSingleMemberAnnotationExpr();
    }
    
    /**
     * i.e. <PRE>{@code
     * @Deprecated //a "Marker" annotation type
     * 
     * //...as apposed to:
     * @Generated("2/15/2018") //a singleMember annotation type
     * 
     * @KeyValues(key=1,value=2) //a "PairedMembers" Annotation Type
     * }</PRE>
     * @return 
     */
    public boolean isPairedMembers(){
        return this.astAnno.isNormalAnnotationExpr();
    }
    
    @Override
    public boolean isNamed( String name ) {
        return this.astAnno.getName().asString().equals( name );
    }

    public boolean isInstance( Class<?> clazz ) {
        String str = this.astAnno.getNameAsString();
        return str.equals( clazz.getCanonicalName() ) || str.equals( clazz.getSimpleName() );
    }

    /**
     * Perform some action on all matching pairs
     * @param _actionFn the action to take on all matching candidate pairs
     * @return yourself
     */
    public _annoExpr forPairs( Consumer<_pair> _actionFn){
        return forPairs(t-> true, _actionFn);
    }

    /**
     * Perform some action on all matching pairs
     * @param _matchFn the function to match candidate pairs
     * @param _actionFn the action to take on all matching canididate pairs
     * @return yourself
     */
    public _annoExpr forPairs( Predicate<_pair> _matchFn, Consumer<_pair> _actionFn){
        listPairs(_matchFn).forEach(_actionFn);
        return this;
    }

    /**
     * Does this anno have a specific member value
     * @param memberValueMatchFn
     * @return
     */
    public boolean hasPair(Predicate<_pair> memberValueMatchFn){
        return listPairs().stream().anyMatch(memberValueMatchFn);
    }

    /**
     * Does this anno have a specific member value
     * @param memberValueMatchFn
     * @return
     */
    public boolean hasPair(BiPredicate<String, _expr> memberValueMatchFn){
        return listPairs().stream().anyMatch(m -> memberValueMatchFn.test( m.getName(), m.getValue()));
    }

    /**
     *
     * @param pairMatchFn
     * @return
     */
    public List<_pair> listPairs(Predicate<_pair> pairMatchFn){
        return listPairs().stream().filter(pairMatchFn).collect(Collectors.toList());
    }

    /**
     *
     * @return
     */
    public List<_pair> listPairs(){
        List<_pair> _mvs = new ArrayList<>();
        if( this.astAnno.isSingleMemberAnnotationExpr()){
            //infer the that "name" is value for a singleMemberAnnotationExpr
            _mvs.add( new _pair(new MemberValuePair("value", this.astAnno.asSingleMemberAnnotationExpr().getMemberValue())));
        }
        else if( this.astAnno.isNormalAnnotationExpr()){
            this.astAnno.asNormalAnnotationExpr().getPairs().forEach(p-> _mvs.add( new _pair(p) ));
        }
        return _mvs;
    }

    public List<String> listKeys() {
        if( this.astAnno instanceof NormalAnnotationExpr ) {
            NormalAnnotationExpr n = (NormalAnnotationExpr)this.astAnno;
            List<String> keys = new ArrayList<>();
            n.getPairs().forEach( e -> keys.add( e.getNameAsString() ) );
            return keys;
        }
        if( this.astAnno instanceof SingleMemberAnnotationExpr ){
            List<String> arr = new ArrayList<>();
            arr.add("value");
            return arr;
        }
        return new ArrayList<>();
    }

    public List<Expression> listValues() {
        return listValues( t -> true );
    }

    public List<Expression> listValues( Predicate<Expression> astExprMatchFn ) {
        return listValues(Expression.class, astExprMatchFn );
    }

    public <E extends Expression> List<E> listValues( Class<E> expressionClass ) {
        return listValues( expressionClass, t -> true );
    }

    /**
     * Builds a Map of Keys-> values
     * @return
     */
    public Map<String, Expression> getPairsMap(){
        Map<String, Expression> keyValuesMap = new HashMap<>();
        if( this.astAnno instanceof MarkerAnnotationExpr ){
            return keyValuesMap;
        }
        if( this.astAnno instanceof SingleMemberAnnotationExpr){
            keyValuesMap.put("value", this.astAnno.asSingleMemberAnnotationExpr().getMemberValue());
            return keyValuesMap;
        }
        NormalAnnotationExpr n = (NormalAnnotationExpr)this.astAnno;
        n.getPairs().forEach(p -> keyValuesMap.put(p.getNameAsString(), p.getValue() ) );
        return keyValuesMap;
    }

    public boolean isValue(char[] value){
        return isPair( "value", Exprs.of(value));
    }
    public boolean isValue(boolean[] value){
        return isPair( "value", Exprs.of(value));
    }

    public boolean isValue(double[] value){
        return isPair( "value", Exprs.of(value));
    }
    public boolean isValue( float[] value){
        return isPair( "value", Exprs.of(value));
    }

    public boolean isValue(int[] value){
        return isPair( "value", Exprs.of(value));
    }

    public boolean isValue(char value){
        return isPair( "value", Exprs.of(value));
    }
    public boolean isValue(boolean value){
        return isPair( "value", Exprs.of(value));
    }

    public boolean isValue(double value){
        return isPair( "value", Exprs.of(value));
    }
    public boolean isValue(float value){
        return isPair( "value", Exprs.of(value));
    }

    public boolean isValue( String value){
        return isPair("value", value);
    }

    public boolean isValue(int value){
        return isPair( "value", Exprs.of(value));
    }

    public boolean isPair(String name, char[] value){
        return isPair( name, Exprs.of(value));
    }
    public boolean isPair(String name, boolean[] value){
        return isPair( name, Exprs.of(value));
    }

    public boolean isPair(String name, double[] value){
        return isPair( name, Exprs.of(value));
    }
    public boolean isPair(String name, float[] value){
        return isPair( name, Exprs.of(value));
    }

    public boolean isPair(String name, int[] value){
        return isPair( name, Exprs.of(value));
    }

    public boolean isPair(String name, char value){
        return isPair( name, Exprs.of(value));
    }
    public boolean isPair(String name, boolean value){
        return isPair( name, Exprs.of(value));
    }

    public boolean isPair(String name, double value){
        return isPair( name, Exprs.of(value));
    }
    public boolean isPair(String name, float value){
        return isPair( name, Exprs.of(value));
    }

    public boolean isPair(String name, String value){
        return isPair(name, Exprs.stringLiteralEx(value));
    }

    public boolean isPair(String name, int value){
        return isPair( name, Exprs.of(value));
    }

    public boolean isPair(String name, Expression value){
        Expression e = this.getPairValue(name);
        if( e != null ){
            return Objects.equals(e, value);
        }
        return false;
    }

    public <E extends Expression> List<E> listValues( Class<E> expressionClass,
                                                      Predicate<E> astExprMatchFn ) {
        List<E> values = new ArrayList<>();
        if( this.astAnno instanceof MarkerAnnotationExpr ){
            return values;
        }
        if( this.astAnno instanceof NormalAnnotationExpr ) {
            NormalAnnotationExpr n = (NormalAnnotationExpr)this.astAnno;
            n.getPairs().forEach(a -> {
                if( expressionClass.isAssignableFrom( expressionClass ) && astExprMatchFn.test( (E)a.getValue() ) ) {
                    values.add( (E)a.getValue() );
                }
            } );
        }
        else {
            SingleMemberAnnotationExpr s = (SingleMemberAnnotationExpr)this.astAnno;
            if( expressionClass.isAssignableFrom( expressionClass ) && astExprMatchFn.test( (E)s.getMemberValue() ) ) {
                values.add( (E)s.getMemberValue() );
            }
        }
        return values;
    }

    public void forValues( Consumer<Expression> astExprActionFn ) {
        listValues().forEach(astExprActionFn );
    }

    /**
     * does the annotation contain ANY attribute that contains this value
     * @param astExpr
     * @return 
     */
    public boolean hasValue(Expression astExpr){
        return listValues().stream().anyMatch(e -> e.equals(astExpr) );
    }

    /**
     * does the anno contain ANY attribute that contains this value?
     * @param i the integer value of a expression value
     * @return 
     */
    public boolean hasValue( int i){
        return hasValue( Exprs.of(i));
    }

    /**
     * does the anno contain ANY attribute that contains this value?
     * @param c the char expression value
     * @return 
     */
    public boolean hasValue( char c){
        return hasValue( Exprs.of(c));
    }    
    
    /**
     * does the anno contain ANY attribute that contains this value?
     * @param f the float expression value
     * @return 
     */
    public boolean hasValue( float f){
        return hasValue( Exprs.of(f));
    }

    /**
     * does the anno contain ANY attribute that contains this value?
     * @param s the string literal
     * @return 
     */
    public boolean hasValue( String s){
        return hasValue( Exprs.stringLiteralEx(s));
    }
    
    /**
     * does the anno contain ANY attribute that contains this value?
     * @param l long expression value
     * @return 
     */
    public boolean hasValue( long l){
        return hasValue( Exprs.of(l));
    }
    
    /**
     * does the anno contain ANY attribute that contains this value?
     * @param b the boolean expression value
     * @return 
     */
    public boolean hasValue( boolean b){
        return hasValue( Exprs.of(b));
    }    
    
    /**
     * does the anno contain ANY attribute that contains this value?
     * @param astExprMatchFn
     * @return 
     */
    public boolean hasValue( Predicate<Expression> astExprMatchFn){
        return listValues().stream().anyMatch(astExprMatchFn );
    }
    
    /**
     * Does this anno have an attribute that can be described by the key / value 
     * represented in the <CODE>attrKeyValue</CODE>
     * i.e. <PRE>{@code
     * _anno _a = _anno.of("a(x=1)");
     * assertTrue( _a.hasAttr("x = 1") );
     * assertFalse( _a.hasAttr("x = 2") );
     * assertFalse(_a.hasAttr("a = 1") );
     * }</PRE>
     * @param attrKeyValue
     * @return 
     */
    public boolean hasPair(String attrKeyValue ){
        try{
            AssignExpr ae = Exprs.assignEx(attrKeyValue);
            String name = ae.getTarget().toString();
            return hasPair( name, ae.getValue());
        } catch (Exception e){
            return false;
        }
    }

    public boolean hasPair(_pair _mv){
        return hasPair(_mv.getName(), _mv.getValue().ast());
    }

    public boolean hasPair(MemberValuePair mvp){
        return hasPair(_pair.of(mvp) );
    }

    public boolean hasPair(String name, int value){
        return hasPair( name, Exprs.of(value));
    }

    public boolean hasPair(String name, long value){
        return hasPair( name, Exprs.of(value));
    }


    public boolean hasPair(String name, char value){
        return hasPair( name, Exprs.of(value));
    }

    public boolean hasPair(String name, boolean value){
        return hasPair( name, Exprs.of(value));
    }

    public boolean hasPair(String name, float value){
        return hasPair( name, Exprs.of(value));
    }

    public boolean hasPair(String name, double value){
        return hasPair( name, Exprs.of(value));
    }

    /**
     * does the anno contain an attribute with this name and value?
     * @param attrName
     * @param astExpr
     * @return 
     */
    public boolean hasPair(String attrName, Expression astExpr){
        List<String> keys = listKeys();
        for(int i=0;i<keys.size(); i++){
            if( keys.get(i).equals(attrName) ){
                return getPairValue(i).equals(astExpr) ;
            }
        } 
        //if the attrName is "value" 
        if(attrName.equals("value") && !this.hasKeys() && this.hasValues()){
            return getPairValue(0).equals(astExpr);
        }
        return false;
    }

    /**
     * Does the anno contain an attribute with this name matching the expressionMatchFn 
     * 
     * @param attrName the attribute name
     * @param astExprMatchFn expression matching function
     * @return 
     */
    public boolean hasPair(String attrName, Predicate<Expression> astExprMatchFn){
        List<String> keys = listKeys();
        for(int i=0;i<keys.size(); i++){
            if( keys.get(i).equals(attrName) ){
                return astExprMatchFn.test( getPairValue(i) );
            }
        } 
        //if the attrName is "value" 
        if(attrName.equals("value") && !this.hasKeys() && this.hasValues()){
            return astExprMatchFn.test( getPairValue(0) );
        }
        return false;
    }

    public boolean hasPairs(_pair...mvs){
        for(int i=0;i<mvs.length; i++){
            if( ! hasPair(mvs[i])){
                return false;
            }
        }
        return true;
    }

    public boolean hasPairs(MemberValuePair...mvs){
        for(int i=0;i<mvs.length; i++){
            if( ! hasPair(mvs[i])){
                return false;
            }
        }
        return true;
    }

    /**
     * Does the anno contain the member values (EXACTLY THESE)
     * @param mvs
     * @return
     */
    public boolean isPairs(MemberValuePair...mvs){
        return isPairs( Arrays.stream(mvs).map(mv -> _pair.of(mv)).collect(Collectors.toList()).toArray(new _pair[0]));
    }

    public boolean isPairs(_pair...mvs){
        List<_pair> tmvs = listPairs();
        if( mvs.length == tmvs.size() ){
            for(int i=0;i<mvs.length;i++){
                if( ! hasPair(mvs[i])){
                    return false;
                }
            }
            return true;
        }
        return false;
    }

    public boolean isPairs(String... keyValuePairs){
        _annoExpr _a = _annoExpr.of("@"+this.getName()+"("+Text.combine(keyValuePairs)+")");

        List<_pair> mvs = _a.listPairs();

        //System.out.println( mvs );
        List<_pair> tmvs = listPairs();
        //System.out.println( tmvs );
        if( mvs.size() == tmvs.size() ){
            for(int i=0;i<mvs.size();i++){
                if( ! hasPair(mvs.get(i))){
                    return false;
                }
            }
            return true;
        }
        return false;
        //return mvs.containsAll(tmvs) && tmvs.containsAll(mvs);
    }

    public boolean hasKeys(){
        return this.astAnno.isNormalAnnotationExpr() &&
            this.astAnno.asNormalAnnotationExpr().getPairs().size() > 0;
    }

    public boolean hasKeys(String...keys){
        List<String> ks = Arrays.stream(keys).collect(Collectors.toList());
        return this.listKeys().containsAll(ks);
    }

    
    public <E extends Expression> void forValues( Class<E> expressionClass, Consumer<E> astExprActionFn ) {
        listValues( expressionClass ).stream().forEach(astExprActionFn );
    }

    public <E extends Expression> void forValues(
            Class<E> expressionClass, Predicate<E> astExprMatchFn, Consumer<E> astExprActionFn ) {
        listValues(expressionClass, astExprMatchFn ).forEach(astExprActionFn );
    }

    public Expression getPairValue(String name ) {
        if( this.astAnno instanceof NormalAnnotationExpr ) {
            NormalAnnotationExpr n = (NormalAnnotationExpr)this.astAnno;
            Optional<MemberValuePair> om = n.getPairs().stream()
                    .filter( m -> m.getNameAsString().equals( name ) ).findFirst();
            if( om.isPresent() ) {
                return om.get().getValue();
            }
        }
        if( this.astAnno instanceof SingleMemberAnnotationExpr && name.equals("value") ){
            return getPairValue(0);
        }
        return null;
    }

    @Override
    public boolean is( AnnotationExpr astExpr ){
        try {
            return _annoExpr.of(astExpr).equals( this );
        }
        catch( Exception e ) {
            return false;
        }
    }

    public Object get(_java.Component component){
        if( component == _java.Component.NAME ){
            return this.getName();
        }
        if( component == _java.Component.PAIRS){
            if( this.astAnno instanceof NormalAnnotationExpr ){
                NormalAnnotationExpr nae = (NormalAnnotationExpr)this.astAnno;
                return nae.getPairs();
            } else if( this.astAnno instanceof SingleMemberAnnotationExpr){
                SingleMemberAnnotationExpr se = (SingleMemberAnnotationExpr)this.astAnno;
                return new MemberValuePair("value", se.getMemberValue() );
            }
        }
        return null;
    }

    @Override
    public Map<_java.Component,Object> components(){
        Map<_java.Component,Object> m = new HashMap<>();
        m.put(_java.Component.NAME, this.getName() );
        if( this.astAnno instanceof NormalAnnotationExpr ){
            NormalAnnotationExpr nae = (NormalAnnotationExpr)this.astAnno;
            m.put(_java.Component.PAIRS, nae.getPairs() );
        } else if( this.astAnno instanceof SingleMemberAnnotationExpr){
            SingleMemberAnnotationExpr se = (SingleMemberAnnotationExpr)this.astAnno;
            m.put(_java.Component.PAIRS, new MemberValuePair("value", se.getMemberValue() ) );
        }
        return m;
    }

    public _annoExpr removePairs() {
        if( this.astAnno instanceof MarkerAnnotationExpr ) {
            return this;
        }
        if( !this.astAnno.getParentNode().isPresent() ) {
            throw new _jdraftException( "Cannot change attrs of annotation with no parent" );
        }
        MarkerAnnotationExpr m = new MarkerAnnotationExpr( this.getName() );
        this.astAnno.getParentNode().get().replace(astAnno, m );
        this.astAnno = m;
        return this;
    }

    public _annoExpr addPair(String key, char c ) {
        return addPair( key, Exprs.of( c ) );
    }

    public _annoExpr addPair(String key, Class c ) {
        return addPair( key, Exprs.classEx( c ) );
    }

    public _annoExpr addPair(String key, boolean b ) {
        return addPair( key, Exprs.of( b ) );
    }

    public _annoExpr addPair(String key, int value ) {
        return addPair( key, Exprs.of( value ) );
    }

    public _annoExpr addPair(String key, long value ) {
        return addPair( key, Exprs.of( value ) );
    }

    public _annoExpr addPair(String key, float f ) {
        return addPair( key, Exprs.of( f ) );
    }

    public _annoExpr addPair(String key, Class...classes ) {
        return addPair( _pair.of(key, classes) );
    }

    public _annoExpr addPair(String key, double d ) {
        return addPair( key, Exprs.of( d ) );
    }

    public _annoExpr addPair(String key, char... cs ) {
        return addPair( _pair.of(key, cs ) );
    }

    public _annoExpr addPair(String key, boolean... bs ) {
        return addPair( _pair.of(key, bs ) );
    }

    public _annoExpr addPair(String key, int... is ) {
        return addPair( _pair.of(key, is ) );
    }

    public _annoExpr addPair(String key, long... ls ) {
        return addPair( _pair.of(key, ls ) );
    }

    public _annoExpr addPair(String key, float... fs ) {
        return addPair( _pair.of(key, fs ) );
    }

    public _annoExpr addPair(String key, double... ds ) {
        return addPair( _pair.of(key, ds ) );
    }

    public _annoExpr addPair(String key, _annoExpr... es ) {
        return addPair( _pair.of(key, es ) );
    }

    public _annoExpr addPair(_pair _p) {
        if( this.astAnno instanceof NormalAnnotationExpr ) {
            NormalAnnotationExpr n = (NormalAnnotationExpr)this.astAnno;
            n.getPairs().add(_p.mvp);
            return this;
        }
        else {
            NodeList<MemberValuePair> nl = new NodeList<>();
            if( this.astAnno instanceof SingleMemberAnnotationExpr ){
                nl.add( new MemberValuePair("value", this.astAnno.asSingleMemberAnnotationExpr().getMemberValue()) );
            }
            nl.add(_p.mvp);
            NormalAnnotationExpr n = new NormalAnnotationExpr( this.astAnno.getName(), nl );
            astAnno.replace( n ); //this will set the parent pointer if necessary
            this.astAnno = n; //this will update the local reference
        }
        return this;
    }

    public _annoExpr addPair(String key, Expression astExpr ) {
        if( this.astAnno instanceof NormalAnnotationExpr ) {
            NormalAnnotationExpr n = (NormalAnnotationExpr)this.astAnno;
            n.addPair(key, astExpr );
            return this;
        }
        else {
            NodeList<MemberValuePair> nl = new NodeList<>();
            nl.add(new MemberValuePair( key, astExpr ) );
            NormalAnnotationExpr n = new NormalAnnotationExpr( this.astAnno.getName(), nl );
            astAnno.replace( n ); //this will set the parent pointer if necessary
            this.astAnno = n; //this will update the local reference
        }
        return this;
    }

    public _annoExpr addPair(String key, String value ) {
        return addPair(key, Exprs.stringLiteralEx(value));
    }

    public _annoExpr setPairValue(String key, char c ) {
        return setPairValue( key, Exprs.of( c ) );
    }

    public _annoExpr setPairValue(String key, boolean b ) {
        return setPairValue( key, Exprs.of( b ) );
    }

    public _annoExpr setPairValue(String key, int value ) {
        return setPairValue( key, Exprs.of( value ) );
    }

    public _annoExpr setPairValue(String key, long value ) {
        return setPairValue( key, Exprs.of( value ) );
    }

    public _annoExpr setPairValue(String key, float f ) {
        return setPairValue( key, Exprs.of( f ) );
    }

    public _annoExpr setPairValue(String key, double d ) {
        return setPairValue( key, Exprs.of( d ) );
    }

    public _annoExpr setPairValue(String name, String expression ) {
        return setPairValue( name, Exprs.stringLiteralEx( expression ) );
    }

    public _annoExpr removePairs( Predicate<_pair> _matchFn){
        listPairs(_matchFn).forEach(p -> this.removePair(p));
        return this;
    }


    public _annoExpr removePair(_pair _p ){

        if( this.astAnno.isSingleMemberAnnotationExpr() && _p.getName().equals("value")){
            //infer the that "name" is value for a singleMemberAnnotationExpr
            Expression mvpe = this.astAnno.asSingleMemberAnnotationExpr().getMemberValue();
            if( Exprs.equal( _p.getValue().ast(), mvpe )) {
                MarkerAnnotationExpr mae = new MarkerAnnotationExpr(this.astAnno.getName());
                if( this.astAnno.getParentNode().isPresent() ){
                    this.astAnno.getParentNode().get().replace( this.astAnno, mae);
                }
                this.astAnno = mae;
            }
            //_mvs.add( new _pair(new MemberValuePair("value", this.astAnno.asSingleMemberAnnotationExpr().getMemberValue())));
        }
        else if( this.astAnno.isNormalAnnotationExpr()){
            this.astAnno.asNormalAnnotationExpr().getPairs().remove( _p.ast() );
            if( this.astAnno.asNormalAnnotationExpr().getPairs().isEmpty() ){
                //ned to create a MarkerAnnoExpr
                MarkerAnnotationExpr mae = new MarkerAnnotationExpr(this.astAnno.getName());
                if( this.astAnno.getParentNode().isPresent() ){
                    this.astAnno.getParentNode().get().replace( this.astAnno, mae);
                }
                this.astAnno = mae;
            }
        }
        return this;
    }
    public _annoExpr removePair(String name ) {
        if( this.astAnno instanceof NormalAnnotationExpr ) {
            NormalAnnotationExpr nae = (NormalAnnotationExpr)this.astAnno;
            nae.getPairs().removeIf( mvp -> mvp.getNameAsString().equals( name ) );
            return this;
        } if( this.astAnno instanceof SingleMemberAnnotationExpr && name.equals("value")){

        }
        //cant removeIn what is not there
        return this;
    }

    public _annoExpr removePair(int index ) {
        if( this.astAnno instanceof NormalAnnotationExpr ) {
            NormalAnnotationExpr nae = (NormalAnnotationExpr)this.astAnno;
            nae.getPairs().remove( index );
            if( nae.getPairs().isEmpty() ) {
                MarkerAnnotationExpr mae = new MarkerAnnotationExpr( getName() );
                nae.getParentNode().get().replace( nae, mae );
                this.astAnno = mae;
            }
            return this;
        }
        if( index == 0 && this.astAnno instanceof SingleMemberAnnotationExpr ) {
            MarkerAnnotationExpr mae = new MarkerAnnotationExpr( this.astAnno.getNameAsString() );
            this.astAnno.getParentNode().get().replace(this.astAnno, mae );
            this.astAnno = mae;
        }
        //cant removeIn what is not there
        return this;
    }

    public _annoExpr setPairs(List<_pair> pairs){
        this.removePairs();
        pairs.forEach(p -> addPair(p));
        return this;
    }

    public _annoExpr addPairs(List<_pair> pairs ){
        pairs.forEach(p -> addPair(p));
        return this;
    }

    public _annoExpr setPairValue(String name, Expression e ) {
        this.listKeys().contains( name );
        if( this.astAnno instanceof NormalAnnotationExpr ) {
            NormalAnnotationExpr nae = (NormalAnnotationExpr)this.astAnno;
            Optional<MemberValuePair> omvp
                    = nae.getPairs().stream().filter( mvp -> mvp.getNameAsString().equals( name ) ).findFirst();
            if( omvp.isPresent() ) {
                omvp.get().setValue( e );
            }
            else {
                //not found, add it
                nae.addPair( name, e );
            }
            return this;
        }
        NormalAnnotationExpr nae = new NormalAnnotationExpr();
        nae.setName(this.astAnno.getNameAsString() );
        nae.addPair( name, e );

        this.astAnno.getParentNode().get().replace(this.astAnno, nae );
        this.astAnno = nae;
        return this;
    }

    public _annoExpr setPairValue(int index, String stringLiteral ) {
        return setPairValue( index, Exprs.stringLiteralEx( stringLiteral ) );
    }
    
    public _annoExpr setPairValue(int index, int intLiteral) {
        return setPairValue( index, Exprs.of( intLiteral ) );
    }

    public _annoExpr setPairValue(int index, boolean boolLiteral) {
        return setPairValue( index, Exprs.of( boolLiteral ) );
    }
    
    public _annoExpr setPairValue(int index, char charLiteral) {
        return setPairValue( index, Exprs.of( charLiteral ) );
    }
    
    public _annoExpr setPairValue(int index, float floatLiteral) {
        return setPairValue( index, Exprs.of( floatLiteral ) );
    }
    
    public _annoExpr setPairValue(int index, double doubleLiteral) {
        return setPairValue( index, Exprs.of( doubleLiteral ) );
    }
    
    public _annoExpr setPairValue(int index, Expression value ) {
        if( index == 0 && this.astAnno instanceof MarkerAnnotationExpr ) {
            MarkerAnnotationExpr ma = (MarkerAnnotationExpr)this.astAnno;
            SingleMemberAnnotationExpr sv = new SingleMemberAnnotationExpr( ma.getName(), value );
            if( this.astAnno.getParentNode().isPresent() ) {
                this.astAnno.getParentNode().get().replace( ma, sv );
                this.astAnno = sv;
            }
            else {
                throw new _jdraftException( "cannot add VALUE to annotation with no parent" );
            }
        }
        if( this.astAnno instanceof NormalAnnotationExpr ) {
            NormalAnnotationExpr n = (NormalAnnotationExpr)this.astAnno;
            n.getPairs().get( index ).setValue( value );
            return this;
        }
        if( this.astAnno instanceof SingleMemberAnnotationExpr ) {
            if( index == 0 ) {
                SingleMemberAnnotationExpr sv = (SingleMemberAnnotationExpr)this.astAnno;
                sv.setMemberValue( value );
                return this;
            }
        }
        throw new _jdraftException( "No Values at index " + index + " in annotation " + this.toString() );
    }

    //if the impl is anything other than a marker annotation expression
    //it has values
    public boolean hasValues() {
        return !(this.astAnno instanceof MarkerAnnotationExpr);
    }

    public Expression getPairValue(int index ) {
        if( !this.hasValues() ) {
            throw new _jdraftException( "No Values on Marker annotation " + this.toString() );
        }
        if( this.astAnno instanceof SingleMemberAnnotationExpr ) {
            if( index == 0 ) {
                SingleMemberAnnotationExpr sv = (SingleMemberAnnotationExpr)this.astAnno;
                return sv.getMemberValue();
            }
            throw new _jdraftException( "No Values at index " + index + " in annotation " + this.toString() );
        }
        NormalAnnotationExpr n = (NormalAnnotationExpr)this.astAnno;
        return n.getPairs().get( index ).getValue();
    }

    @Override
    public String toString() {
        return this.astAnno.toString();
    }

    @Override
    public int hashCode() {
        //if the annotation is the
        if( this.astAnno == null){
            return 0;
        }
        String name = this.astAnno.getNameAsString();
        int idx = name.indexOf('.');
        if( idx > 0){
            name = name.substring(name.lastIndexOf('.')+1);
        }
        if( this.astAnno instanceof MarkerAnnotationExpr ){
            return 31 * name.hashCode() + 15;
        }
        if( this.astAnno instanceof SingleMemberAnnotationExpr){            
            Map<String,Integer> hm = new HashMap<>();
            hm.put( "value", Exprs.hash(this.astAnno.asSingleMemberAnnotationExpr().getMemberValue()));
            return hm.hashCode();
        }        
        Map<String,Integer> hm = new HashMap<>();
        this.astAnno.asNormalAnnotationExpr().getPairs().forEach(p -> hm.put(p.getNameAsString(), Exprs.hash( p.getValue()) ) );
        return hm.hashCode();
    }

    @Override
    public boolean equals( Object obj ) {
        if( this == obj ) {
            return true;
        }
        if( obj == null ) {
            return false;
        }
        if( getClass() != obj.getClass() ) {
            return false;
        }
        final _annoExpr other = (_annoExpr)obj;

        return Exprs.equal(astAnno, other.astAnno);
    }

    @Override
    public AnnotationExpr ast() {
        return this.astAnno;
    }

    /**
     * each name-value pair within annotations
     * i.e.
     * @A(key="value") (the "key="value"" part)
     *
     * NOTE: we also model the inferred/ hidden name (as "value") if it is not present
     * @A("val") ... (the key is inferred to be "value" and the value is the String "val")
     */
    public static class _pair implements _java._node<MemberValuePair, _pair>,
            _java._withName<_pair>{

        public static _pair of(MemberValuePair mvp){
            return new _pair( mvp);
        }

        public static _pair of( String name, int value){
            return of( new MemberValuePair(name, new IntegerLiteralExpr(value)));
        }

        public static _pair of( String name, boolean value){
            return of( new MemberValuePair(name, new BooleanLiteralExpr(value)));
        }

        public static _pair of( String name, char value){
            return of( new MemberValuePair(name, new CharLiteralExpr(value)));
        }

        public static _pair of( String name, float value){
            return of( new MemberValuePair(name, new DoubleLiteralExpr(value)));
        }

        public static _pair of( String name, double value){
            return of( new MemberValuePair(name, new DoubleLiteralExpr(value)));
        }

        public static _pair of( String name, long value){
            return of( new MemberValuePair(name, new LongLiteralExpr(value)));
        }

        public static _pair of( String name, _annoExpr _anno){
            return of( new MemberValuePair(name, _anno.ast()));
        }


        //arrays:
        public static _pair of( String name, int... value){
            return of( new MemberValuePair(name, _arrayInitExpr.of(value).ast()));
        }

        public static _pair of( String name, boolean... value){
            return of( new MemberValuePair(name, _arrayInitExpr.of(value).ast()));
        }

        public static _pair of( String name, char... value){
            return of( new MemberValuePair(name, _arrayInitExpr.of(value).ast()));
        }

        public static _pair of( String name, float... value){
            return of( new MemberValuePair(name, _arrayInitExpr.of(value).ast()));
        }

        public static _pair of( String name, double... value){
            return of( new MemberValuePair(name, _arrayInitExpr.of(value).ast()));
        }

        public static _pair of( String name, long... value){
            return of( new MemberValuePair(name, _arrayInitExpr.of(value).ast()));
        }

        public static _pair of( String name, _annoExpr... _anno){
            return of( new MemberValuePair(name, _arrayInitExpr.of(_anno).ast()));
        }

        public static _pair of( String name, String value){
            return of( new MemberValuePair(name, new StringLiteralExpr(value)));
        }

        public static _pair of( String name, Class value){
            return of( new MemberValuePair(name, new ClassExpr(_typeRef.of(value.getCanonicalName()).ast())));
        }

        public static _pair of( String name, Class... values){
            return of( new MemberValuePair(name, _arrayInitExpr.of(values).ast()));
        }


        public static _pair of( String s){
            return of( new String[]{s});
        }

        public static _pair of (String...str ){
            AnnotationExpr ae = StaticJavaParser.parseAnnotation( "@A("+Text.combine( str)+")" );
            if( ae.isNormalAnnotationExpr() ){
                return new _pair(ae.asNormalAnnotationExpr().getPairs().get(0));
            }
            else{
                SingleMemberAnnotationExpr sma = (SingleMemberAnnotationExpr)ae;
                MemberValuePair mvp = new MemberValuePair();
                mvp.setValue( sma.getMemberValue() );
                mvp.setName("value");
                _pair _mv = new _pair(mvp);
                _mv.isValueOnly = true;
                return _mv;
            }
        }

        public boolean isValueOnly = false;

        public MemberValuePair mvp;

        public _pair(MemberValuePair mvp){
            this.mvp = mvp;
        }

        @Override
        public _pair copy() {
            return new _pair( this.mvp.clone() );
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
            //System.out.println( "NAME" + this.mvp.getName() );
            return this.mvp.getName();
        }

        public _pair setName(String name){
            this.mvp.setName(name);
            return this;
        }

        public _expr getValue(){
            return _expr.of(this.mvp.getValue());
        }

        public _pair setValue(String... ex){
            this.mvp.setValue(Exprs.of(ex));
            return this;
        }

        public _pair setValue(_expr _e){
            this.mvp.setValue(_e.ast());
            return this;
        }

        public _pair setValue(Expression e){
            this.mvp.setValue(e);
            return this;
        }


        public boolean isValue( String... ex){
            try {
                return Exprs.equal(this.mvp.getValue(), Exprs.of(ex));
            }catch(Exception e){
                return false;
            }
        }

        public boolean isValue( _expr _e){
            return Exprs.equal(this.mvp.getValue(), _e.ast());
        }

        public boolean isValue( Expression e){
            return Exprs.equal(this.mvp.getValue(), e);
        }


        public boolean isValue( boolean b){
            return isValue(Exprs.of(b));
        }

        public boolean isValue( int i){
            return isValue(Exprs.of(i));
        }

        public boolean isValue( char c){
            return isValue(Exprs.of(c));
        }

        public boolean isValue( float f){
            return isValue(Exprs.of(f));
        }

        public boolean isValue( long l){
            return isValue(Exprs.of(l));
        }

        public boolean isValue( double d){
            return isValue(Exprs.of(d));
        }

        @Override
        public boolean is(String... stringRep) {
            try{
                return of(stringRep).equals(this);
            } catch(Exception e){
                return false;
            }
        }

        public boolean equals(Object o){
            if( o instanceof _pair){
                _pair ot = (_pair)o;

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

    /**
     * Patches the NodeWithAnnotations interface "IN" to Type.
     * 
     * There is a tension between what we want to represent in the class hierarchy
     * and what Generics allow (in abstract base classes like Type and their 
     * derived classes with Generics)
     * 
     * As far as how this effects the draft API
     * 
     * @param <T> 
     */
    public static class Type_annoPatch <T extends Type>
        implements NodeWithAnnotations {

        //this can be any type of Type
        final T type;
        
        public Type_annoPatch(T type){
            this.type = type;
        }
        
        @Override
        public NodeList getAnnotations() {
            return type.getAnnotations();
        }

        @Override
        public Node setAnnotations(NodeList annotations) {
            return type.setAnnotations(annotations);
        }

        @Override
        public void tryAddImportToParentCompilationUnit(Class clazz) {
            type.tryAddImportToParentCompilationUnit(clazz);
        }
    }
}
