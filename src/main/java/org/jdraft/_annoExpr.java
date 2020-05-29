package org.jdraft;

import java.lang.annotation.Annotation;
import java.util.*;
import java.util.function.*;
import java.util.stream.Collectors;

import com.github.javaparser.ast.Node;
import com.github.javaparser.ast.NodeList;
import com.github.javaparser.ast.body.BodyDeclaration;
import com.github.javaparser.ast.expr.*;
import com.github.javaparser.ast.nodeTypes.NodeWithAnnotations;
import com.github.javaparser.ast.type.Type;
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
 * <LI>{@link _param} (@nonNull Object VALUE)
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
        implements _expr<AnnotationExpr, _annoExpr>, _java._withName<_annoExpr>, _java._node<AnnotationExpr, _annoExpr> {

    public static _annoExpr of(String anno ){
        return of( new String[]{anno} );
    }

    public static _annoExpr of(){
        return new _annoExpr( new MarkerAnnotationExpr() );
    }

    public static _annoExpr of(String... annotation ) {
        return new _annoExpr( Ast.annotationExpr( annotation ) );
    }

    /**
     * Look through the anonymous Object to find some annotated entity
     * and extract & model the first annotation as a _draft _anno
     * @param anonymousObject an anonymous Object containing an annotated entity
     * @return the _anno _draft object
     */
    public static _annoExpr of(Object anonymousObject ){
        StackTraceElement ste = Thread.currentThread().getStackTrace()[2];
        ObjectCreationExpr oce = Expr.newExpr(ste);
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

    public _annoExpr setName(_name _n){
        this.astAnno.setName( _n.toString() );
        return this;
    }

    @Override
    public _annoExpr setName(String name ){
        this.astAnno.setName(name);
        return this;
    }

    /**
     * gets the ith _entryPair
     * @param index
     * @return
     */
    public _entryPair getPair(int index ){
        return this.listPairs().get(index);
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
    public _annoExpr forPairs( Consumer<_entryPair> _actionFn){
        return forPairs(t-> true, _actionFn);
    }

    /**
     * Perform some action on all matching pairs
     * @param _matchFn the function to match candidate pairs
     * @param _actionFn the action to take on all matching canididate pairs
     * @return yourself
     */
    public _annoExpr forPairs(Predicate<_entryPair> _matchFn, Consumer<_entryPair> _actionFn){
        listPairs(_matchFn).forEach(_actionFn);
        return this;
    }

    /**
     * Does this anno have a specific member value
     * @param memberValueMatchFn
     * @return
     */
    public boolean hasPair(Predicate<_entryPair> memberValueMatchFn){
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
    public List<_entryPair> listPairs(Predicate<_entryPair> pairMatchFn){
        return listPairs().stream().filter(pairMatchFn).collect(Collectors.toList());
    }

    /**
     *
     * @return
     */
    public List<_entryPair> listPairs(){
        List<_entryPair> _mvs = new ArrayList<>();
        if( this.astAnno.isSingleMemberAnnotationExpr()){
            //infer the that "name" is value for a singleMemberAnnotationExpr
            _mvs.add( new _entryPair(new MemberValuePair("value", this.astAnno.asSingleMemberAnnotationExpr().getMemberValue())));
        }
        else if( this.astAnno.isNormalAnnotationExpr()){
            this.astAnno.asNormalAnnotationExpr().getPairs().forEach(p-> _mvs.add( new _entryPair(p) ));
        }
        return _mvs;
    }

    public static final Function<String, _annoExpr> PARSER = s-> _annoExpr.of(s);

    public static _feature._one<_annoExpr, String> NAME = new _feature._one<>(_annoExpr.class, String.class,
            _feature._id.NAME,
            a -> a.getName(),
            (_annoExpr a, String name) -> a.setName(name),
            PARSER );

    public static _feature._many<_annoExpr, _entryPair> ENTRY_PAIRS = new _feature._many<>(_annoExpr.class, _entryPair.class,
            _feature._id.ANNO_EXPR_ENTRY_PAIRS, _feature._id.ANNO_EXPR_ENTRY_PAIR,
            a->a.listPairs(),
            (_annoExpr a, List<_entryPair> pairs)-> a.setPairs(pairs),
            PARSER, s-> _entryPair.of(s));

    public static _feature._meta<_annoExpr> META = _feature._meta.of(_annoExpr.class, NAME, ENTRY_PAIRS);

    public boolean hasPair(String name, char value){
        return hasPair( name, Expr.of(value));
    }
    public boolean hasPair(String name, boolean value){
        return hasPair( name, Expr.of(value));
    }

    public boolean hasPair(String name, double value){
        return hasPair( name, Expr.of(value));
    }
    public boolean hasPair(String name, float value){
        return hasPair( name, Expr.of(value));
    }

    public boolean hasPair(String name, String value){
        return hasPair(name, Expr.stringExpr(value));
    }

    /*
    public boolean hasPair(String name, int value){
        return hasPair( name, Exprs.of(value));
    }
     */

    public boolean hasPair(String name, Expression value){
        Expression e = this.getPairValue(name);
        if( e != null ){
            return Objects.equals(e, value);
        }
        return false;
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
            AssignExpr ae = Expr.assignExpr(attrKeyValue);
            String name = ae.getTarget().toString();
            return hasPair( name, ae.getValue());
        } catch (Exception e){
            return false;
        }
    }

    public boolean hasPair(_entryPair _mv){
        return hasPair(_mv.getName(), _mv.getValue().ast());
    }

    public boolean hasPair(MemberValuePair mvp){
        return hasPair(_entryPair.of(mvp) );
    }

    public boolean hasPair(String name, int value){
        return hasPair( name, Expr.of(value));
    }

    public boolean hasPair(String name, long value){
        return hasPair( name, Expr.of(value));
    }

    public boolean hasPair(String name, char[] value){
        return hasPair( name, Expr.of(value));
    }
    public boolean hasPair(String name, boolean[] value){
        return hasPair( name, Expr.of(value));
    }

    public boolean hasPair(String name, double[] value){
        return hasPair( name, Expr.of(value));
    }
    public boolean hasPair(String name, float[] value){
        return hasPair( name, Expr.of(value));
    }

    public boolean hasPair(String name, int[] value){
        return hasPair( name, Expr.of(value));
    }

    /**
     * Does the anno contain an attribute with this name matching the expressionMatchFn 
     * 
     * @param attrName the attribute name
     * @param astExprMatchFn expression matching function
     * @return 
     */
    public boolean hasPair(String attrName, Predicate<Expression> astExprMatchFn){
        return !this.listPairs( (_entryPair p)-> Objects.equals(p.getName(), attrName) && astExprMatchFn.test(p.mvp.getValue())).isEmpty();
    }

    public boolean hasPairs(_entryPair...mvs){
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
        return isPairs( Arrays.stream(mvs).map(mv -> _entryPair.of(mv)).collect(Collectors.toList()).toArray(new _entryPair[0]));
    }

    public boolean isPairs(_entryPair...mvs){
        List<_entryPair> tmvs = listPairs();
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

        List<_entryPair> mvs = _a.listPairs();

        //System.out.println( mvs );
        List<_entryPair> tmvs = listPairs();
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

    /*
    public Object get(_java.Feature feature){
        if( feature == _java.Feature.NAME ){
            return this.getName();
        }
        if( feature == _java.Feature.ANNO_EXPR_ENTRY_PAIRS){
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
    */
    /*
    public Map<_java.Feature,Object> features(){
        Map<_java.Feature,Object> m = new HashMap<>();
        m.put(_java.Feature.NAME, this.getName() );
        if( this.astAnno instanceof NormalAnnotationExpr ){
            NormalAnnotationExpr nae = (NormalAnnotationExpr)this.astAnno;
            m.put(_java.Feature.ANNO_EXPR_ENTRY_PAIRS, nae.getPairs() );
        } else if( this.astAnno instanceof SingleMemberAnnotationExpr){
            SingleMemberAnnotationExpr se = (SingleMemberAnnotationExpr)this.astAnno;
            m.put(_java.Feature.ANNO_EXPR_ENTRY_PAIRS, new MemberValuePair("value", se.getMemberValue() ) );
        }
        return m;
    }
     */

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
        return addPair( key, Expr.of( c ) );
    }

    public _annoExpr addPair(String key, Class c ) {
        return addPair( key, Expr.classExpr( c ) );
    }

    public _annoExpr addPair(String key, boolean b ) {
        return addPair( key, Expr.of( b ) );
    }

    public _annoExpr addPair(String key, int value ) {
        return addPair( key, Expr.of( value ) );
    }

    public _annoExpr addPair(String key, long value ) {
        return addPair( key, Expr.of( value ) );
    }

    public _annoExpr addPair(String key, float f ) {
        return addPair( key, Expr.of( f ) );
    }

    public _annoExpr addPair(String key, Class...classes ) {
        return addPair( _entryPair.of(key, classes) );
    }

    public _annoExpr addPair(String key, double d ) {
        return addPair( key, Expr.of( d ) );
    }

    public _annoExpr addPair(String key, char... cs ) {
        return addPair( _entryPair.of(key, cs ) );
    }

    public _annoExpr addPair(String key, boolean... bs ) {
        return addPair( _entryPair.of(key, bs ) );
    }

    public _annoExpr addPair(String key, int... is ) {
        return addPair( _entryPair.of(key, is ) );
    }

    public _annoExpr addPair(String key, long... ls ) {
        return addPair( _entryPair.of(key, ls ) );
    }

    public _annoExpr addPair(String key, float... fs ) {
        return addPair( _entryPair.of(key, fs ) );
    }

    public _annoExpr addPair(String key, double... ds ) {
        return addPair( _entryPair.of(key, ds ) );
    }

    public _annoExpr addPair(String key, _annoExpr... es ) {
        return addPair( _entryPair.of(key, es ) );
    }

    public _annoExpr addPair(_entryPair _p) {
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
        return addPair(key, Expr.stringExpr(value));
    }

    public _annoExpr setPairValue(String key, char c ) {
        return setPairValue( key, Expr.of( c ) );
    }

    public _annoExpr setPairValue(String key, boolean b ) {
        return setPairValue( key, Expr.of( b ) );
    }

    public _annoExpr setPairValue(String key, int value ) {
        return setPairValue( key, Expr.of( value ) );
    }

    public _annoExpr setPairValue(String key, long value ) {
        return setPairValue( key, Expr.of( value ) );
    }

    public _annoExpr setPairValue(String key, float f ) {
        return setPairValue( key, Expr.of( f ) );
    }

    public _annoExpr setPairValue(String key, double d ) {
        return setPairValue( key, Expr.of( d ) );
    }

    public _annoExpr setPairValue(String name, String expression ) {
        return setPairValue( name, Expr.stringExpr( expression ) );
    }

    public _annoExpr removePairs( Predicate<_entryPair> _matchFn){
        listPairs(_matchFn).forEach(p -> this.removePair(p));
        return this;
    }


    public _annoExpr removePair(_entryPair _p ){

        if( this.astAnno.isSingleMemberAnnotationExpr() && _p.getName().equals("value")){
            //infer the that "name" is value for a singleMemberAnnotationExpr
            Expression mvpe = this.astAnno.asSingleMemberAnnotationExpr().getMemberValue();
            if( Expr.equal( _p.getValue().ast(), mvpe )) {
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

    public _annoExpr setPairs(List<_entryPair> pairs){
        this.removePairs();
        pairs.forEach(p -> addPair(p));
        return this;
    }

    public _annoExpr addPairs(List<_entryPair> pairs ){
        pairs.forEach(p -> addPair(p));
        return this;
    }

    public _annoExpr setPairValue(String name, Expression e ) {
        if( name == "value" || name == null ){
            List<_entryPair> _eps = listPairs(p-> p.getName() == null || p.getName().equals("value"));
            forPairs(p-> p.getName() == null || p.getName().equals("value"), p-> p.setValue(e.clone()));
        }
        return this;
    }

    public _annoExpr setPairValue(int index, String stringLiteral ) {
        return setPairValue( index, Expr.stringExpr( stringLiteral ) );
    }
    
    public _annoExpr setPairValue(int index, int intLiteral) {
        return setPairValue( index, Expr.of( intLiteral ) );
    }

    public _annoExpr setPairValue(int index, boolean boolLiteral) {
        return setPairValue( index, Expr.of( boolLiteral ) );
    }
    
    public _annoExpr setPairValue(int index, char charLiteral) {
        return setPairValue( index, Expr.of( charLiteral ) );
    }
    
    public _annoExpr setPairValue(int index, float floatLiteral) {
        return setPairValue( index, Expr.of( floatLiteral ) );
    }
    
    public _annoExpr setPairValue(int index, double doubleLiteral) {
        return setPairValue( index, Expr.of( doubleLiteral ) );
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
            hm.put( "value", Expr.hash(this.astAnno.asSingleMemberAnnotationExpr().getMemberValue()));
            return hm.hashCode();
        }        
        Map<String,Integer> hm = new HashMap<>();
        this.astAnno.asNormalAnnotationExpr().getPairs().forEach(p -> hm.put(p.getNameAsString(), Expr.hash( p.getValue()) ) );
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

        return Expr.equal(astAnno, other.astAnno);
    }

    @Override
    public AnnotationExpr ast() {
        return this.astAnno;
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
