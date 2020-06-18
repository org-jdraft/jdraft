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
 * @see _annos which represents ALL annotations applied to an Annotated Entity
 * @author Eric
 */
public final class _anno
        implements _expr<AnnotationExpr, _anno>, _java._withName<_anno>, _tree._node<AnnotationExpr, _anno> {

    public static _anno of(String anno ){
        return of( new String[]{anno} );
    }

    public static _anno of(){
        return new _anno( new MarkerAnnotationExpr() );
    }

    public static _anno of(String... annotation ) {
        return new _anno( Ast.annotationExpr( annotation ) );
    }

    /**
     * Look through the anonymous Object to find some annotated entity
     * and extract & model the first annotation as a _draft _anno
     * @param anonymousObject an anonymous Object containing an annotated entity
     * @return the _anno _draft object
     */
    public static _anno of(Object anonymousObject ){
        StackTraceElement ste = Thread.currentThread().getStackTrace()[2];
        ObjectCreationExpr oce = Expr.newExpr(ste);
        NodeList<BodyDeclaration<?>> bds = oce.getAnonymousClassBody().get();
        BodyDeclaration<?> bd = bds.stream().filter(b -> b.getAnnotations().isNonEmpty() ).findFirst().get();
        return of( bd.getAnnotation(0) );
    }

    public _feature._features<_anno> features(){
        return FEATURES;
    }

    public static _anno of(Class<? extends Annotation> annotationClass){
        return of( new MarkerAnnotationExpr(annotationClass.getSimpleName()));
    }
    public static _anno of(AnnotationExpr astAnno ) {
        return new _anno( astAnno );
    }
    
    private AnnotationExpr node;

    public _anno(AnnotationExpr node) {
        this.node = node;
    }

    @Override
    public _anno copy() {
        return new _anno( this.node.clone() );
    }

    @Override
    public String getName() {
        return this.node.getNameAsString();
    }

    public Name getNameNode(){ return this.node.getName(); }

    public _anno setName(_name _n){
        this.node.setName( _n.toString() );
        return this;
    }

    @Override
    public _anno setName(String name ){
        this.node.setName(name);
        return this;
    }

    /**
     * gets the ith _entryPair
     * @param index
     * @return
     */
    public _annoEntryPair getEntryPair(int index ){
        return this.listEntryPairs().get(index);
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
        return this.node.isMarkerAnnotationExpr();
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
        return this.node.isSingleMemberAnnotationExpr();
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
        return this.node.isNormalAnnotationExpr();
    }
    
    @Override
    public boolean isNamed( String name ) {
        return this.node.getName().asString().equals( name );
    }

    public boolean isInstance( Class<?> clazz ) {
        String str = this.node.getNameAsString();
        return str.equals( clazz.getCanonicalName() ) || str.equals( clazz.getSimpleName() );
    }

    /**
     * Perform some action on all matching pairs
     * @param _actionFn the action to take on all matching candidate pairs
     * @return yourself
     */
    public _anno toEntryPairs(Consumer<_annoEntryPair> _actionFn){
        return toEntryPairs(t-> true, _actionFn);
    }

    /**
     * Perform some action on all matching pairs
     * @param _matchFn the function to match candidate pairs
     * @param _actionFn the action to take on all matching canididate pairs
     * @return yourself
     */
    public _anno toEntryPairs(Predicate<_annoEntryPair> _matchFn, Consumer<_annoEntryPair> _actionFn){
        listEntryPairs(_matchFn).forEach(_actionFn);
        return this;
    }

    /**
     * Perform some action on all matching pairs
     * @param _actionFn the action to take on all matching candidate pairs
     * @return yourself
     */
    public List<_annoEntryPair> forEntryPairs(Consumer<_annoEntryPair> _actionFn){
        return forEntryPairs(t-> true, _actionFn);
    }

    /**
     * Perform some action on all matching pairs
     * @param _matchFn the function to match candidate pairs
     * @param _actionFn the action to take on all matching canididate pairs
     * @return yourself
     */
    public List<_annoEntryPair> forEntryPairs(Predicate<_annoEntryPair> _matchFn, Consumer<_annoEntryPair> _actionFn){
        List<_annoEntryPair> lep = listEntryPairs(_matchFn);
        lep.forEach(_actionFn);
        return lep;
    }


    /**
     * Does this anno have a specific member value
     * @param memberValueMatchFn
     * @return
     */
    public boolean hasEntryPair(Predicate<_annoEntryPair> memberValueMatchFn){
        return listEntryPairs().stream().anyMatch(memberValueMatchFn);
    }

    /**
     * Does this anno have a specific member value
     * @param memberValueMatchFn
     * @return
     */
    public boolean hasEntryPair(BiPredicate<String, _expr> memberValueMatchFn){
        return listEntryPairs().stream().anyMatch(m -> memberValueMatchFn.test( m.getName(), m.getValue()));
    }

    /**
     *
     * @param pairMatchFn
     * @return
     */
    public List<_annoEntryPair> listEntryPairs(Predicate<_annoEntryPair> pairMatchFn){
        return listEntryPairs().stream().filter(pairMatchFn).collect(Collectors.toList());
    }

    /**
     *
     * @return
     */
    public List<_annoEntryPair> listEntryPairs(){
        List<_annoEntryPair> _mvs = new ArrayList<>();
        if( this.node.isSingleMemberAnnotationExpr()){
            //infer the that "name" is value for a singleMemberAnnotationExpr
            _annoEntryPair _ep = new _annoEntryPair(new MemberValuePair("value", this.node.asSingleMemberAnnotationExpr().getMemberValue()));
            _ep.isValueOnly =true;
            _mvs.add(_ep);
        }
        else if( this.node.isNormalAnnotationExpr()){
            this.node.asNormalAnnotationExpr().getPairs().forEach(p-> _mvs.add( new _annoEntryPair(p) ));
        }
        return _mvs;
    }

    public static final Function<String, _anno> PARSER = s-> _anno.of(s);

    public static _feature._one<_anno, String> NAME = new _feature._one<>(_anno.class, String.class,
            _feature._id.NAME,
            a -> a.getName(),
            (_anno a, String name) -> a.setName(name),
            PARSER );

    /*
    public static _feature._one<_anno, _annoEntryPairs> ENTRY_PAIRS = new _feature._one<>(_anno.class, _annoEntryPairs.class,
            _feature._id.ENTRY_PAIRS,
            a-> a.getEntryPairs(),
            (_anno a, _annoEntryPairs ps)-> a.setEntryPairs(ps), PARSER );
    */

    public static _feature._many<_anno, _annoEntryPair> ENTRY_PAIRS = new _feature._many<>(_anno.class, _annoEntryPair.class,
            _feature._id.ENTRY_PAIRS, _feature._id.ENTRY_PAIR,
            a->a.listEntryPairs(),
            (_anno a, List<_annoEntryPair> pairs)-> a.setEntryPairs(pairs),
            PARSER, s-> _annoEntryPair.of(s))
            .setOrdered(false); //the order of the entry pairs { @A(a=1,b=2) == @A(b=2,a=1)} isn't semantically important

    public static _feature._features<_anno> FEATURES = _feature._features.of(_anno.class, PARSER, NAME, ENTRY_PAIRS);

    public boolean hasEntryPair(String name, char value){
        return hasEntryPair( name, Expr.of(value));
    }
    public boolean hasEntryPair(String name, boolean value){
        return hasEntryPair( name, Expr.of(value));
    }

    public boolean hasEntryPair(String name, double value){
        return hasEntryPair( name, Expr.of(value));
    }
    public boolean hasEntryPair(String name, float value){
        return hasEntryPair( name, Expr.of(value));
    }

    public boolean hasEntryPair(String name, String value){
        return hasEntryPair(name, Expr.stringLiteralExpr(value));
    }

    public boolean hasEntryPair(String name, Expression value){
        Expression e = this.getEntryValueExpression(name);
        if( e != null ){
            return Objects.equals(e, value);
        }
        return false;
    }

    public _annoEntryPairs getEntryPairs(){
        return _annoEntryPairs.of(this.node);
    }

    public _anno setEntryPairs(_annoEntryPairs _aeps){
        if( _aeps == null || _aeps.isEmpty() ){
            if( this.node instanceof MarkerAnnotationExpr){
                //its already done
            }
            else {
                MarkerAnnotationExpr mae = new MarkerAnnotationExpr(this.node.getName());
                this.node.replace(mae);
                this.node = mae;
            }
            return this;
        } else{
            if( _aeps.isValueOnly() ){
                SingleMemberAnnotationExpr sae = new SingleMemberAnnotationExpr(this.node.getName(), _aeps.getAt(0).getValue().node());
                this.node.replace(sae);
                this.node = sae;
            } else{
                NormalAnnotationExpr nae = new NormalAnnotationExpr(this.node.getName(), _aeps.astList());
                this.node.replace(nae);
                this.node = nae;
            }
            return this;
        }
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
    public boolean hasEntryPair(String attrKeyValue ){
        try{
            AssignExpr ae = Expr.assignExpr(attrKeyValue);
            String name = ae.getTarget().toString();
            return hasEntryPair( name, ae.getValue());
        } catch (Exception e){
            return false;
        }
    }

    public boolean hasEntryPair(_annoEntryPair _mv){
        return hasEntryPair(_mv.getName(), _mv.getValue().node());
    }

    public boolean hasEntryPair(MemberValuePair mvp){
        return hasEntryPair(_annoEntryPair.of(mvp) );
    }

    public boolean hasEntryPair(String name, int value){
        return hasEntryPair( name, Expr.of(value));
    }

    public boolean hasEntryPair(String name, long value){
        return hasEntryPair( name, Expr.of(value));
    }

    public boolean hasEntryPair(String name, char[] value){
        return hasEntryPair( name, Expr.arrayInitializerExpr(value));
    }
    public boolean hasEntryPair(String name, boolean[] value){
        return hasEntryPair( name, Expr.arrayInitializerExpr(value));
    }

    public boolean hasEntryPair(String name, double[] value){
        return hasEntryPair( name, Expr.arrayInitializerExpr(value));
    }
    public boolean hasEntryPair(String name, float[] value){
        return hasEntryPair( name, Expr.arrayInitializerExpr(value));
    }

    public boolean hasEntryPair(String name, int[] value){
        return hasEntryPair( name, Expr.arrayInitializerExpr(value));
    }

    /**
     * Does the anno contain an attribute with this name matching the expressionMatchFn 
     * 
     * @param attrName the attribute name
     * @param astExprMatchFn expression matching function
     * @return 
     */
    public boolean hasEntryPair(String attrName, Predicate<Expression> astExprMatchFn){
        return !this.listEntryPairs( (_annoEntryPair p)-> Objects.equals(p.getName(), attrName) && astExprMatchFn.test(p.node.getValue())).isEmpty();
    }

    public boolean hasEntryPairs(_annoEntryPair...mvs){
        for(int i=0;i<mvs.length; i++){
            if( ! hasEntryPair(mvs[i])){
                return false;
            }
        }
        return true;
    }

    public boolean hasEntryPairs(MemberValuePair...mvs){
        for(int i=0;i<mvs.length; i++){
            if( ! hasEntryPair(mvs[i])){
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
    public boolean isEntryPairs(MemberValuePair...mvs){
        return isEntryPairs( Arrays.stream(mvs).map(mv -> _annoEntryPair.of(mv)).collect(Collectors.toList()).toArray(new _annoEntryPair[0]));
    }

    public boolean isEntryPairs(_annoEntryPair...mvs){
        List<_annoEntryPair> tmvs = listEntryPairs();
        if( mvs.length == tmvs.size() ){
            for(int i=0;i<mvs.length;i++){
                if( ! hasEntryPair(mvs[i])){
                    return false;
                }
            }
            return true;
        }
        return false;
    }

    public boolean isEntryPairs(String... keyValuePairs){
        _anno _a = _anno.of("@"+this.getName()+"("+Text.combine(keyValuePairs)+")");

        List<_annoEntryPair> mvs = _a.listEntryPairs();

        //System.out.println( mvs );
        List<_annoEntryPair> tmvs = listEntryPairs();
        //System.out.println( tmvs );
        if( mvs.size() == tmvs.size() ){
            for(int i=0;i<mvs.size();i++){
                if( ! hasEntryPair(mvs.get(i))){
                    return false;
                }
            }
            return true;
        }
        return false;
        //return mvs.containsAll(tmvs) && tmvs.containsAll(mvs);
    }

    public Expression getEntryValueExpression(String name ) {
        if( this.node instanceof NormalAnnotationExpr ) {
            NormalAnnotationExpr n = (NormalAnnotationExpr)this.node;
            Optional<MemberValuePair> om = n.getPairs().stream()
                    .filter( m -> m.getNameAsString().equals( name ) ).findFirst();
            if( om.isPresent() ) {
                return om.get().getValue();
            }
        }
        if( this.node instanceof SingleMemberAnnotationExpr && name.equals("value") ){
            return getEntryValueExpression(0);
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

    public _anno removeEntryPairs() {
        if( this.node instanceof MarkerAnnotationExpr ) {
            return this;
        }
        if( !this.node.getParentNode().isPresent() ) {
            throw new _jdraftException( "Cannot change attrs of annotation with no parent" );
        }
        MarkerAnnotationExpr m = new MarkerAnnotationExpr( this.getName() );
        this.node.getParentNode().get().replace(node, m );
        this.node = m;
        return this;
    }

    public _anno addEntryPair(String key, char c ) {
        return addEntryPair( key, Expr.of( c ) );
    }

    public _anno addEntryPair(String key, Class c ) {
        return addEntryPair( key, Expr.classExpr( c ) );
    }

    public _anno addEntryPair(String key, boolean b ) {
        return addEntryPair( key, Expr.of( b ) );
    }

    public _anno addEntryPair(String key, int value ) {
        return addEntryPair( key, Expr.of( value ) );
    }

    public _anno addEntryPair(String key, long value ) {
        return addEntryPair( key, Expr.of( value ) );
    }

    public _anno addEntryPair(String key, float f ) {
        return addEntryPair( key, Expr.of( f ) );
    }

    public _anno addEntryPair(String key, Class...classes ) {
        return addEntryPair( _annoEntryPair.of(key, classes) );
    }

    public _anno addEntryPair(String key, double d ) {
        return addEntryPair( key, Expr.of( d ) );
    }

    public _anno addEntryPair(String key, char... cs ) {
        return addEntryPair( _annoEntryPair.of(key, cs ) );
    }

    public _anno addEntryPair(String key, boolean... bs ) {
        return addEntryPair( _annoEntryPair.of(key, bs ) );
    }

    public _anno addEntryPair(String key, int... is ) {
        return addEntryPair( _annoEntryPair.of(key, is ) );
    }

    public _anno addEntryPair(String key, long... ls ) {
        return addEntryPair( _annoEntryPair.of(key, ls ) );
    }

    public _anno addEntryPair(String key, float... fs ) {
        return addEntryPair( _annoEntryPair.of(key, fs ) );
    }

    public _anno addEntryPair(String key, double... ds ) {
        return addEntryPair( _annoEntryPair.of(key, ds ) );
    }

    public _anno addEntryPair(String key, _anno... es ) {
        return addEntryPair( _annoEntryPair.of(key, es ) );
    }

    public _anno addEntryPair(_annoEntryPair _p) {
        if( this.node instanceof NormalAnnotationExpr ) {
            NormalAnnotationExpr n = (NormalAnnotationExpr)this.node;
            n.getPairs().add(_p.node);
            return this;
        }
        else {
            NodeList<MemberValuePair> nl = new NodeList<>();
            if( this.node instanceof SingleMemberAnnotationExpr ){
                nl.add( new MemberValuePair("value", this.node.asSingleMemberAnnotationExpr().getMemberValue()) );
            }
            nl.add(_p.node);
            NormalAnnotationExpr n = new NormalAnnotationExpr( this.node.getName(), nl );
            node.replace( n ); //this will set the parent pointer if necessary
            this.node = n; //this will update the local reference
        }
        return this;
    }

    public _anno addEntryPair(String key, Expression astExpr ) {
        if( this.node instanceof NormalAnnotationExpr ) {
            NormalAnnotationExpr n = (NormalAnnotationExpr)this.node;
            n.addPair(key, astExpr );
            return this;
        }
        else {
            NodeList<MemberValuePair> nl = new NodeList<>();
            nl.add(new MemberValuePair( key, astExpr ) );
            NormalAnnotationExpr n = new NormalAnnotationExpr( this.node.getName(), nl );
            node.replace( n ); //this will set the parent pointer if necessary
            this.node = n; //this will update the local reference
        }
        return this;
    }

    public _anno addEntryPair(String key, String value ) {
        return addEntryPair(key, Expr.stringLiteralExpr(value));
    }

    public _anno setEntryPairValue(String key, char c ) {
        return setEntryPairValue( key, Expr.of( c ) );
    }

    public _anno setEntryPairValue(String key, boolean b ) {
        return setEntryPairValue( key, Expr.of( b ) );
    }

    public _anno setEntryPairValue(String key, int value ) {
        return setEntryPairValue( key, Expr.of( value ) );
    }

    public _anno setEntryPairValue(String key, long value ) {
        return setEntryPairValue( key, Expr.of( value ) );
    }

    public _anno setEntryPairValue(String key, float f ) {
        return setEntryPairValue( key, Expr.of( f ) );
    }

    public _anno setEntryPairValue(String key, double d ) {
        return setEntryPairValue( key, Expr.of( d ) );
    }

    public _anno setEntryPairValue(String name, String expression ) {
        return setEntryPairValue( name, Expr.stringLiteralExpr( expression ) );
    }

    public _anno removeEntryPairs(Predicate<_annoEntryPair> _matchFn){
        listEntryPairs(_matchFn).forEach(p -> this.removeEntryPair(p));
        return this;
    }


    public _anno removeEntryPair(_annoEntryPair _p ){

        if( this.node.isSingleMemberAnnotationExpr() && _p.getName().equals("value")){
            //infer the that "name" is value for a singleMemberAnnotationExpr
            Expression mvpe = this.node.asSingleMemberAnnotationExpr().getMemberValue();
            if( Expr.equal( _p.getValue().node(), mvpe )) {
                MarkerAnnotationExpr mae = new MarkerAnnotationExpr(this.node.getName());
                if( this.node.getParentNode().isPresent() ){
                    this.node.getParentNode().get().replace( this.node, mae);
                }
                this.node = mae;
            }
            //_mvs.add( new _pair(new MemberValuePair("value", this.astAnno.asSingleMemberAnnotationExpr().getMemberValue())));
        }
        else if( this.node.isNormalAnnotationExpr()){
            this.node.asNormalAnnotationExpr().getPairs().remove( _p.node() );
            if( this.node.asNormalAnnotationExpr().getPairs().isEmpty() ){
                //ned to create a MarkerAnnoExpr
                MarkerAnnotationExpr mae = new MarkerAnnotationExpr(this.node.getName());
                if( this.node.getParentNode().isPresent() ){
                    this.node.getParentNode().get().replace( this.node, mae);
                }
                this.node = mae;
            }
        }
        return this;
    }
    public _anno removeEntryPair(String name ) {
        if( this.node instanceof NormalAnnotationExpr ) {
            NormalAnnotationExpr nae = (NormalAnnotationExpr)this.node;
            nae.getPairs().removeIf( mvp -> mvp.getNameAsString().equals( name ) );
            return this;
        } if( this.node instanceof SingleMemberAnnotationExpr && name.equals("value")){

        }
        //cant removeIn what is not there
        return this;
    }

    public _anno removeEntryPair(int index ) {
        if( this.node instanceof NormalAnnotationExpr ) {
            NormalAnnotationExpr nae = (NormalAnnotationExpr)this.node;
            nae.getPairs().remove( index );
            if( nae.getPairs().isEmpty() ) {
                MarkerAnnotationExpr mae = new MarkerAnnotationExpr( getName() );
                nae.getParentNode().get().replace( nae, mae );
                this.node = mae;
            }
            return this;
        }
        if( index == 0 && this.node instanceof SingleMemberAnnotationExpr ) {
            MarkerAnnotationExpr mae = new MarkerAnnotationExpr( this.node.getNameAsString() );
            this.node.getParentNode().get().replace(this.node, mae );
            this.node = mae;
        }
        //cant removeIn what is not there
        return this;
    }

    public _anno setEntryPairs(List<_annoEntryPair> pairs){
        this.removeEntryPairs();
        pairs.forEach(p -> addEntryPair(p));
        return this;
    }

    public _anno addEntryPairs(List<_annoEntryPair> pairs ){
        pairs.forEach(p -> addEntryPair(p));
        return this;
    }

    public _anno setEntryPairValue(String name, Expression e ) {
        if( name == "value" || name == null ){
            List<_annoEntryPair> _eps = listEntryPairs(p-> p.getName() == null || p.getName().equals("value"));
            toEntryPairs(p-> p.getName() == null || p.getName().equals("value"), p-> p.setValue(e.clone()));
        }
        return this;
    }

    public _anno setEntryPairValue(int index, String stringLiteral ) {
        return setEntryPairValue( index, Expr.stringLiteralExpr( stringLiteral ) );
    }
    
    public _anno setEntryPairValue(int index, int intLiteral) {
        return setEntryPairValue( index, Expr.of( intLiteral ) );
    }

    public _anno setEntryPairValue(int index, boolean boolLiteral) {
        return setEntryPairValue( index, Expr.of( boolLiteral ) );
    }
    
    public _anno setEntryPairValue(int index, char charLiteral) {
        return setEntryPairValue( index, Expr.of( charLiteral ) );
    }
    
    public _anno setEntryPairValue(int index, float floatLiteral) {
        return setEntryPairValue( index, Expr.of( floatLiteral ) );
    }
    
    public _anno setEntryPairValue(int index, double doubleLiteral) {
        return setEntryPairValue( index, Expr.of( doubleLiteral ) );
    }
    
    public _anno setEntryPairValue(int index, Expression value ) {
        if( index == 0 && this.node instanceof MarkerAnnotationExpr ) {
            MarkerAnnotationExpr ma = (MarkerAnnotationExpr)this.node;
            SingleMemberAnnotationExpr sv = new SingleMemberAnnotationExpr( ma.getName(), value );
            if( this.node.getParentNode().isPresent() ) {
                this.node.getParentNode().get().replace( ma, sv );
                this.node = sv;
            }
            else {
                throw new _jdraftException( "cannot add VALUE to annotation with no parent" );
            }
        }
        if( this.node instanceof NormalAnnotationExpr ) {
            NormalAnnotationExpr n = (NormalAnnotationExpr)this.node;
            n.getPairs().get( index ).setValue( value );
            return this;
        }
        if( this.node instanceof SingleMemberAnnotationExpr ) {
            if( index == 0 ) {
                SingleMemberAnnotationExpr sv = (SingleMemberAnnotationExpr)this.node;
                sv.setMemberValue( value );
                return this;
            }
        }
        throw new _jdraftException( "No Values at index " + index + " in annotation " + this.toString() );
    }

    //if the impl is anything other than a marker annotation expression
    //it has values
    public boolean hasValues() {
        return !(this.node instanceof MarkerAnnotationExpr);
    }

    public Expression getEntryValueExpression(int index ) {
        if( !this.hasValues() ) {
            throw new _jdraftException( "No Values on Marker annotation " + this.toString() );
        }
        if( this.node instanceof SingleMemberAnnotationExpr ) {
            if( index == 0 ) {
                SingleMemberAnnotationExpr sv = (SingleMemberAnnotationExpr)this.node;
                return sv.getMemberValue();
            }
            throw new _jdraftException( "No Values at index " + index + " in annotation " + this.toString() );
        }
        NormalAnnotationExpr n = (NormalAnnotationExpr)this.node;
        return n.getPairs().get( index ).getValue();
    }

    @Override
    public String toString() {
        return this.node.toString();
    }

    @Override
    public int hashCode() {
        //if the annotation is the
        if( this.node == null){
            return 0;
        }
        String name = this.node.getNameAsString();
        int idx = name.indexOf('.');
        if( idx > 0){
            name = name.substring(name.lastIndexOf('.')+1);
        }
        if( this.node instanceof MarkerAnnotationExpr ){
            return 31 * name.hashCode() + 15;
        }
        if( this.node instanceof SingleMemberAnnotationExpr){
            Map<String,Integer> hm = new HashMap<>();
            hm.put( "value", Expr.hash(this.node.asSingleMemberAnnotationExpr().getMemberValue()));
            return hm.hashCode();
        }        
        Map<String,Integer> hm = new HashMap<>();
        this.node.asNormalAnnotationExpr().getPairs().forEach(p -> hm.put(p.getNameAsString(), Expr.hash( p.getValue()) ) );
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

        return Expr.equal(node, other.node);
    }

    @Override
    public AnnotationExpr node() {
        return this.node;
    }

    public _anno replace(AnnotationExpr ae){
        this.node.replace(ae);
        this.node = ae;
        return this;
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
