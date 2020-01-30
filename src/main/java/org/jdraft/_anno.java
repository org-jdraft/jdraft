package org.jdraft;

import java.lang.annotation.Annotation;
import java.util.*;
import java.util.function.Consumer;
import java.util.function.Predicate;

import com.github.javaparser.ast.Node;
import com.github.javaparser.ast.NodeList;
import com.github.javaparser.ast.body.BodyDeclaration;
import com.github.javaparser.ast.expr.*;
import com.github.javaparser.ast.nodeTypes.NodeWithAnnotations;
import com.github.javaparser.ast.type.Type;

/**
 * Instance of a single @ annotation
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
 * @see _annos which represents ALL annotations applied to an AnnotatedEntity
 * @author Eric
 */
public final class _anno
        implements _expression <AnnotationExpr, _anno>, _named<_anno> {

    public static _anno of( String anno ){        
        return of( new String[]{anno} );
    }

    public static _anno of(){
        return new _anno( new MarkerAnnotationExpr() );
    }

    public static _anno of( String... annotation ) {
        return new _anno( Ast.anno( annotation ) );
    }

    /**
     * Look through the anonymous Object to find some annotated entity
     * and extract & model the first annotation as a _draft _anno
     * @param anonymousObject an anonymous Object containing an annotated entity
     * @return the _anno _draft object
     */
    public static _anno of( Object anonymousObject ){
        StackTraceElement ste = Thread.currentThread().getStackTrace()[2];
        ObjectCreationExpr oce = Ex.newEx(ste);
        NodeList<BodyDeclaration<?>> bds = oce.getAnonymousClassBody().get();
        BodyDeclaration<?> bd = bds.stream().filter(b -> b.getAnnotations().isNonEmpty() ).findFirst().get();
        return of( bd.getAnnotation(0) );
    }

    public static _anno of( Class<? extends Annotation> annotationClass){
        return of( new MarkerAnnotationExpr(annotationClass.getSimpleName()));
    }
    public static _anno of( AnnotationExpr astAnno ) {
        return new _anno( astAnno );
    }
    
    private AnnotationExpr astAnno;

    public _anno( AnnotationExpr astAnno ) {
        this.astAnno = astAnno;
    }

    @Override
    public _anno copy() {
        return new _anno( this.astAnno.clone() );
    }

    @Override
    public String getName() {
        return this.astAnno.getNameAsString();
    }

    @Override
    public _anno name( String name ){
        this.astAnno.setName(name);
        return this;
    }
    
    public _anno setName( String name ) {
        this.astAnno.setName( name );
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
     * @KeyValues(key=1,value=2) //a keyValue Annotation Type
     * }</PRE>
     * @return 
     */
    public boolean isKeyValueAnnotation(){
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
    public Map<String, Expression> getKeyValuesMap(){
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
        return isValue( "value", Ex.of(value));
    }
    public boolean isValue(boolean[] value){
        return isValue( "value", Ex.of(value));
    }

    public boolean isValue(double[] value){
        return isValue( "value", Ex.of(value));
    }
    public boolean isValue( float[] value){
        return isValue( "value", Ex.of(value));
    }

    public boolean isValue(int[] value){
        return isValue( "value", Ex.of(value));
    }

    public boolean isValue(char value){
        return isValue( "value", Ex.of(value));
    }
    public boolean isValue(boolean value){
        return isValue( "value", Ex.of(value));
    }

    public boolean isValue(double value){
        return isValue( "value", Ex.of(value));
    }
    public boolean isValue(float value){
        return isValue( "value", Ex.of(value));
    }

    public boolean isValue( String value){
        return isValue("value", value);
    }

    public boolean isValue(int value){
        return isValue( "value", Ex.of(value));
    }

    public boolean isValue(String name, char[] value){
        return isValue( name, Ex.of(value));
    }
    public boolean isValue(String name, boolean[] value){
        return isValue( name, Ex.of(value));
    }

    public boolean isValue(String name, double[] value){
        return isValue( name, Ex.of(value));
    }
    public boolean isValue(String name, float[] value){
        return isValue( name, Ex.of(value));
    }

    public boolean isValue(String name, int[] value){
        return isValue( name, Ex.of(value));
    }

    public boolean isValue(String name, char value){
        return isValue( name, Ex.of(value));
    }
    public boolean isValue(String name, boolean value){
        return isValue( name, Ex.of(value));
    }

    public boolean isValue(String name, double value){
        return isValue( name, Ex.of(value));
    }
    public boolean isValue(String name, float value){
        return isValue( name, Ex.of(value));
    }

    public boolean isValue( String name, String value){
        return isValue(name, Ex.stringLiteralEx(value));
    }

    public boolean isValue(String name, int value){
        return isValue( name, Ex.of(value));
    }

    public boolean isValue(String name, Expression value){
        Expression e = this.getValue(name);
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
        return hasValue( Ex.of(i));
    }

    /**
     * does the anno contain ANY attribute that contains this value?
     * @param c the char expression value
     * @return 
     */
    public boolean hasValue( char c){
        return hasValue( Ex.of(c));
    }    
    
    /**
     * does the anno contain ANY attribute that contains this value?
     * @param f the float expression value
     * @return 
     */
    public boolean hasValue( float f){
        return hasValue( Ex.of(f));
    }

    /**
     * does the anno contain ANY attribute that contains this value?
     * @param s the string literal
     * @return 
     */
    public boolean hasValue( String s){
        return hasValue( Ex.stringLiteralEx(s));
    }
    
    /**
     * does the anno contain ANY attribute that contains this value?
     * @param l long expression value
     * @return 
     */
    public boolean hasValue( long l){
        return hasValue( Ex.of(l));
    }
    
    /**
     * does the anno contain ANY attribute that contains this value?
     * @param b the boolean expression value
     * @return 
     */
    public boolean hasValue( boolean b){
        return hasValue( Ex.of(b));
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
    public boolean hasAttr( String attrKeyValue ){
        try{
            AssignExpr ae = Ex.assignEx(attrKeyValue);
            String name = ae.getTarget().toString();
            return hasAttr( name, ae.getValue());
        } catch (Exception e){
            return false;
        }
    }
    
    /**
     * does the anno contain an attribute with this name and value?
     * @param attrName
     * @param astExpr
     * @return 
     */
    public boolean hasAttr( String attrName, Expression astExpr){
        List<String> keys = listKeys();
        for(int i=0;i<keys.size(); i++){
            if( keys.get(i).equals(attrName) ){
                return getValue(i).equals(astExpr) ;
            }
        } 
        //if the attrName is "value" 
        if(attrName.equals("value") && !this.hasKeys() && this.hasValues()){
            return getValue(0).equals(astExpr);
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
    public boolean hasAttr( String attrName, Predicate<Expression> astExprMatchFn){
        List<String> keys = listKeys();
        for(int i=0;i<keys.size(); i++){
            if( keys.get(i).equals(attrName) ){
                return astExprMatchFn.test( getValue(i) );
            }
        } 
        //if the attrName is "value" 
        if(attrName.equals("value") && !this.hasKeys() && this.hasValues()){
            return astExprMatchFn.test( getValue(0) );
        }
        return false;
    }
    
    public boolean hasKeys(){
        return this.astAnno.isNormalAnnotationExpr() &&
            this.astAnno.asNormalAnnotationExpr().getPairs().size() > 0;
    }
    
    public <E extends Expression> void forValues( Class<E> expressionClass, Consumer<E> astExprActionFn ) {
        listValues( expressionClass ).stream().forEach(astExprActionFn );
    }

    public <E extends Expression> void forValues(
            Class<E> expressionClass, Predicate<E> astExprMatchFn, Consumer<E> astExprActionFn ) {
        listValues(expressionClass, astExprMatchFn ).forEach(astExprActionFn );
    }

    public Expression getValue( String name ) {
        if( this.astAnno instanceof NormalAnnotationExpr ) {
            NormalAnnotationExpr n = (NormalAnnotationExpr)this.astAnno;
            Optional<MemberValuePair> om = n.getPairs().stream()
                    .filter( m -> m.getNameAsString().equals( name ) ).findFirst();
            if( om.isPresent() ) {
                return om.get().getValue();
            }
        }
        if( this.astAnno instanceof SingleMemberAnnotationExpr && name.equals("value") ){
            return getValue(0);
        }
        return null;
    }

    @Override
    public boolean is( AnnotationExpr astExpr ){
        try {
            return _anno.of(astExpr).equals( this );
        }
        catch( Exception e ) {
            return false;
        }
    }

    @Override
    public boolean is( String... str ) {
        try {
            return is( Ast.anno( str ) );
        }
        catch( Exception e ) {
        }
        return false;
    }

    public Object get(_java.Component component){
        if( component == _java.Component.NAME ){
            return this.getName();
        }
        if( component == _java.Component.KEY_VALUES ){
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
            m.put(_java.Component.KEY_VALUES, nae.getPairs() );
        } else if( this.astAnno instanceof SingleMemberAnnotationExpr){
            SingleMemberAnnotationExpr se = (SingleMemberAnnotationExpr)this.astAnno;
            m.put(_java.Component.KEY_VALUES, new MemberValuePair("value", se.getMemberValue() ) );
        }
        return m;
    }

    public _anno removeAttrs() {
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

    public _anno addAttr( String attrNameValue) {
        try{
            AssignExpr ae = Ex.assignEx(attrNameValue);
            return addAttr( ae.getTarget().toString(), ae.getValue() );
        }catch(Exception e){
            throw new _jdraftException("Unable to parse Attr Name value \""+ attrNameValue+"\"");
        }        
    }
    
    public _anno addAttr( String key, char c ) {
        return addAttr( key, Ex.of( c ) );
    }

    public _anno addAttr( String key, boolean b ) {
        return addAttr( key, Ex.of( b ) );
    }

    public _anno addAttr( String key, int value ) {
        return addAttr( key, Ex.of( value ) );
    }

    public _anno addAttr( String key, long value ) {
        return addAttr( key, Ex.of( value ) );
    }

    public _anno addAttr( String key, float f ) {
        return addAttr( key, Ex.of( f ) );
    }

    public _anno addAttr( String key, double d ) {
        return addAttr( key, Ex.of( d ) );
    }

    public _anno addAttr( String key, Expression astExpr ) {
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

    public _anno addAttr( String key, String value ) {
        return addAttr(key, Ex.stringLiteralEx(value));
    }

    public _anno setValue( String key, char c ) {
        return setValue( key, Ex.of( c ) );
    }

    public _anno setValue( String key, boolean b ) {
        return addAttr( key, Ex.of( b ) );
    }

    public _anno setValue( String key, int value ) {
        return addAttr( key, Ex.of( value ) );
    }

    public _anno setValue( String key, long value ) {
        return addAttr( key, Ex.of( value ) );
    }

    public _anno setValue( String key, float f ) {
        return addAttr( key, Ex.of( f ) );
    }

    public _anno setValue( String key, double d ) {
        return addAttr( key, Ex.of( d ) );
    }

    public _anno setValue( String name, String expression ) {
        return setValue( name, Ex.stringLiteralEx( expression ) );
    }

    public _anno removeAttr( String name ) {
        if( this.astAnno instanceof NormalAnnotationExpr ) {
            NormalAnnotationExpr nae = (NormalAnnotationExpr)this.astAnno;
            nae.getPairs().removeIf( mvp -> mvp.getNameAsString().equals( name ) );
            return this;
        }
        //cant removeIn what is not there
        return this;
    }

    public _anno removeAttr( int index ) {
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

    public _anno setValue( String name, Expression e ) {
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

    public _anno setValue( int index, String stringLiteral ) {
        return setValue( index, Ex.stringLiteralEx( stringLiteral ) );
    }
    
    public _anno setValue( int index, int intLiteral) {
        return setValue( index, Ex.of( intLiteral ) );
    }

    public _anno setValue( int index, boolean boolLiteral) {
        return setValue( index, Ex.of( boolLiteral ) );
    }
    
    public _anno setValue( int index, char charLiteral) {
        return setValue( index, Ex.of( charLiteral ) );
    }
    
    public _anno setValue( int index, float floatLiteral) {
        return setValue( index, Ex.of( floatLiteral ) );
    }
    
    public _anno setValue( int index, double doubleLiteral) {
        return setValue( index, Ex.of( doubleLiteral ) );
    }
    
    public _anno setValue( int index, Expression value ) {
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

    public Expression getValue( int index ) {
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
            hm.put( "value", Ex.hash(this.astAnno.asSingleMemberAnnotationExpr().getMemberValue()));
            return hm.hashCode();
        }        
        Map<String,Integer> hm = new HashMap<>();
        this.astAnno.asNormalAnnotationExpr().getPairs().forEach(p -> hm.put(p.getNameAsString(), Ex.hash( p.getValue()) ) );
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
        final _anno other = (_anno)obj;

        return Ex.equivalent(astAnno, other.astAnno);
    }

    @Override
    public AnnotationExpr ast() {
        return this.astAnno;
    }

    /**
     * _model of an entity that may be annotated with multiple Annotations
     * this includes:
     * <UL>
     * {@link _type}
     * {@link _field}
     * {@link _method}
     * {@link _constructor}
     * {@link _parameter}
     * {@link _annotation._entry}
     * {@link _constant}
     * </UL>
     *
     * @author Eric
     *
     * @param <_HA> the container type (that has Annos)
     */
    public interface _hasAnnos<_HA extends _hasAnnos>
        extends _mrJava {
        
        /**
         * @return the annos
         */
        _annos getAnnos();
        
        /**
         * gets the anno at the index
         * @param index
         * @return 
         */
        default _anno getAnno( int index ) {
            return getAnnos().get( index );
        }

        /**
         * replace the annotations with the _annos provided
         * @param _as
         * @return
         */
        default _HA replace(_annos _as ) {
            this.getAnnos().clear().add( _as.list().toArray( new _anno[ 0 ] ) );
            return (_HA)this;
        }

        /**
         * apply a function to all annos
         * @param _annoActionFn
         * @return the modified T
         */
        default _HA forAnnos(Consumer<_anno> _annoActionFn ){
            getAnnos().forEach(_annoActionFn);
            return (_HA) this;
        }

        /**
         * Selectively apply a function to annos
         * @param _annoMatchFn
         * @param _annoActionFn
         * @return 
         */
        default _HA forAnnos(Predicate<_anno> _annoMatchFn, Consumer<_anno> _annoActionFn ){
            getAnnos().forEach(_annoMatchFn, _annoActionFn);
            return (_HA) this;
        }

        /**
         * Gets the FIRST annotation that matches the _annoMatchFn predicate
         *
         * @param _annoMatchFn
         * @return the first matching {@link _anno}, or null if none query
         */
        default _anno getAnno( Predicate<_anno> _annoMatchFn ) {
            return getAnnos().first( _annoMatchFn );
        }

        /**
         * Gets the FIRST _anno that matches the annotationClass
         *
         * @param annotationClass the annotationClass
         * @return the first annotation that is of the annotationClass or null
         */
        default _anno getAnno( Class<? extends Annotation> annotationClass ) {
            return getAnnos().first( annotationClass );
        }

        /**
         * Gets the FIRST _anno that matches the annoName
         *
         * @param annoName the NAME of the anno
         * @return the first _anno that has the NAME, or null
         */
        default _anno getAnno( String annoName ) {
            if( annoName.startsWith("@")){
                return getAnnos().first(annoName.substring(1));
            }
            return getAnnos().first( annoName );
        }

        /**
         *
         * @param annotationClass
         * @return
         */
        default boolean hasAnno( Class<? extends Annotation> annotationClass ) {
            return getAnno( annotationClass ) != null;
        }

        /**
         * Do we have an anno that has this name?
         * @param name the name of the anno
         * @return 
         */
        default boolean hasAnno( String name ) {
            return getAnno( name ) != null;
        }

        default boolean hasAnno( Predicate<_anno> _annoMatchFn ) {
            return getAnno( _annoMatchFn ) != null;
        }

        default List<_anno> listAnnos() {
            return getAnnos().list();
        }

        default List<_anno> listAnnos( Predicate<_anno> _annMatchFn ) {
            return getAnnos().list( _annMatchFn );
        }

        default List<_anno> listAnnos( String annoName ) {
            return getAnnos().list( annoName );
        }

        default List<_anno> listAnnos(Class<? extends Annotation> annotationClass ) {
            return getAnnos().list( annotationClass );
        }

        default _HA anno(List<AnnotationExpr> astAnnoList ){
            astAnnoList.forEach( a -> getAnnos().add(a) );
            return (_HA)this;
        }

        /**
         * annotate with the ANNOTATIONS and return the _annotated
         *
         * @param _anns ANNOTATIONS to to
         * @return the annotated entity
         */
        default _HA anno(_anno... _anns ) {
            getAnnos().add(_anns );
            return (_HA)this;
        }

        /**
         * annotate with the ANNOTATIONS and return the model
         *
         * @param annoClasses
         * @return
         */
        default _HA anno(Class<? extends Annotation>... annoClasses ) {
            getAnnos().add(annoClasses );
            return (_HA)this;
        }

        /**
         * accept one or more ANNOTATIONS
         *
         * @param annotations
         * @return
         */
        default _HA anno(String... annotations ) {
            getAnnos().add( annotations );
            return (_HA)this;
        }

        /**
         * remove the annotation and return the modified T
         * @param astAnn the annotation 
         * @return 
         */
        default _HA removeAnno(AnnotationExpr astAnn ){
            getAnnos().remove(astAnn);
            return (_HA)this;
        }
        
        /**
         * remove the annotation and return the modified T
         * @param _a
         * @return the modified T
         */
        default _HA removeAnno(_anno _a ){
            return removeAnno( _a.ast() );            
        }

        /**
         * removeAll all _anno that accept the _annoMatchFn
         *
         * @param _annoMatchFn
         * @return
         */
        default _HA removeAnnos(Predicate<_anno> _annoMatchFn ) {
            getAnnos().remove( _annoMatchFn );
            return (_HA)this;
        }

        /**
         * 
         * @return 
         */
        default boolean hasAnnos() {
            return !getAnnos().isEmpty();
        }

        /**
         * removeIn all ANNOTATIONS that are of the annotationClass
         *
         * @param annotationClass
         * @return
         */
        default _HA removeAnnos(Class<? extends Annotation> annotationClass ) {
            getAnnos().remove( annotationClass );
            return (_HA)this;
        }

        /**
         * removeAll all ANNOTATIONS that have the annoName
         *
         * @param annoNames
         * @return
         */
        default _HA removeAnnos(String... annoNames ) {
            getAnnos().remove( annoNames );
            return (_HA)this;
        }

        /**
         * removeIn all ANNOTATIONS
         *
         * @param _annosToRemove
         * @return
         */
        default _HA removeAnnos(List<_anno> _annosToRemove ) {
            getAnnos().remove(_annosToRemove.toArray( new _anno[ 0 ] ) );
            return (_HA)this;
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
