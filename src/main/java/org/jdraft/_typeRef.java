package org.jdraft;

import java.util.*;
import java.util.function.Function;
import java.util.function.Predicate;

import com.github.javaparser.ast.Node;
import com.github.javaparser.ast.NodeList;
import com.github.javaparser.ast.expr.AnnotationExpr;
import com.github.javaparser.ast.expr.CharLiteralExpr;
import com.github.javaparser.ast.nodeTypes.NodeWithType;
import com.github.javaparser.ast.type.*;
import org.jdraft.text.Stencil;
import org.jdraft.text.Text;

import static org.jdraft.Types.of;

/**
 * Model of a Java TYPE Declaration (i.e. String, int, List<Boolean>)
 * wraps an AST {@link Type} implementation
 *
 * @param <T> the Type implementation (i.e. ReferenceType)
 */
public final class _typeRef<T extends Type>
        implements _tree._node<Type, _typeRef>, _annos._withAnnos<_typeRef> {

    public static final Function<String, _typeRef> PARSER = s-> _typeRef.of(s);

    /** Void type used in */
    public static _typeRef VOID = of( new VoidType() );

    /** Var type (awaiting inference/resolution) */
    public static _typeRef VAR = of( new VarType() );

    /**
     * Type used when none is provided (i.e. when I write the lambda parameter with no type)
     * i.e. "a" in:<PRE>
     * a -> System.out.println(a);
     * i.e. "b" in:
     * (b) -> max(b, getWidth());
     * </PRE>
     */
    public static _typeRef UNKNOWN = of( new UnknownType( ) );

    /**
     *
     * @param t
     * @return
     */
    public static _typeRef of( java.lang.reflect.AnnotatedType t){
        return new _typeRef( Types.of( t ) );
    }

    /**
     *
     * @param t
     * @return
     */
    public static _typeRef of( java.lang.reflect.Type t){
        return new _typeRef( Types.of( t ) );
    }

    /**
     *
     * @param clazz
     * @return
     */
    public static _typeRef of( Class clazz ) {
        return new _typeRef( Types.of( clazz ) );
    }


    /**
     *
     * @param string
     * @return
     */
    public static _typeRef of( String string ) {
        return new _typeRef( Types.of( string ) );
    }

    public _annos getAnnos(){
        /** this is a hack... because right now accessing the annotations from the Type AST is a mess frought with danger
         * often when there ARE annotations that are applied at the wrong level... it
         * s not JavaParsers fault, but rather the sheer ambiguity of how Annotaitons can be applied to BOTH
         * return types AND/OR entities like MethodDeclarations like:
         * <PRE>
         * @NotNull A doSomething(){...}
         * </PRE>
         *
         * here the @NotNull annotation COULD be assigned to the return type A
         * -OR-
         * @NotNull COULD be assigned to the method doSomething()
         *
         * anyways it stinks that you are given back a "Synthetic" _annos
         * that cannot Really be manipulated
         *
         * TODO Make an Implementation of _hasAnnos that effectively unifies JUST Annotations on Types
         * so this can be fixed (i.e. I can return an implementation of _annos that allows you to manipulate it
         * as if you were directly manipulating the AST on the Type... but it's really just an abstraction layer
         * on top
         */
        Type t = getErasedType(this.node);
        List<AnnotationExpr> aes = new ArrayList<>();
        t.walk(AnnotationExpr.class, a->aes.add(a));
        return _annos.of(aes);
    }

    public _feature._features<_typeRef> features(){
        return FEATURES;
    }

    /**
     * Does the Main Type itself have annotations
     * NOTE: the Generics CAN have annotations,
     * <PRE>
     *     List<@NonNull String> l;
     * </PRE>
     * and we dont mean those... but rather
     * <PRE>
     * @NonNull List<String> l;
     * </PRE>
     * this is why we need to strip off the Generics before looking
     * @return
     */
    public boolean hasAnnos(){
        return getErasedType(this.node).findFirst(AnnotationExpr.class).isPresent();
    }

    /**
     *
     * @param t
     * @return
     */
    public static _typeRef of( Type t ) {
        return new _typeRef( t );
    }

    /**
     * Tests the equality of Types, this will make sure
     * @param a
     * @param b
     * @return
     */
    public static boolean equals(_typeRef a, _typeRef b){
        if(a == b ){
            return true;
        }
        if( a==null || b == null ){
            return false;
        }
        return Types.equal( a.node(), b.node());
    }

    /** The underlying AST type */
    private T node;

    public _typeRef( T t ) {
        this.node = t;
    }

    @Override
    public Type node() {
        return node;
    }
    
    /**
     * Unknown type is used for lambdas where the type may not be directly defined
     * <PRE>
     * for example:
     * (o)-> System.out.println(o);
     * 
     * ...as apposed to explicitly defined parameter type
     * (OutputStream o) -> System.out.println( o );
     * </PRE>
     * @return 
     */ 
    public boolean isUnknownType(){
        return node.isUnknownType();
    }

    public boolean isTypeParameter(){
        return this.node.isTypeParameter();
    }

    public boolean isUsingDiamondOperator(){
        return node.isClassOrInterfaceType()
                && node.asClassOrInterfaceType().isUsingDiamondOperator();
    }

    public boolean isReferenceType(){
        return node.isReferenceType();
    }
    public boolean isClassOrInterfaceType(){
        return node.isClassOrInterfaceType();
    }

    public boolean isBoxedType(){
        return node.isClassOrInterfaceType() && node.asClassOrInterfaceType().isBoxedType();
    }

    public boolean isPrimitiveType() {
        return node.isPrimitiveType();
    }

    public boolean isPrimitiveType(PrimitiveType pt){
        if( isPrimitiveType() ){
            PrimitiveType prim = node.asPrimitiveType();
            return Types.equal( pt, prim);
        }
        return false;
    }

    public boolean isPrimitiveType(Predicate<PrimitiveType> pt){
        if( isPrimitiveType() ){
            PrimitiveType prim = node.asPrimitiveType();
            return pt.test(prim);
        }
        return false;
    }

    public Type getElementType(){
        return this.node.getElementType();
    }

    /**
     * <UL>
     * <LI>"?" as in "List<?>"
     * <LI>"? extends T" as in "List<? extends T>"
     * <LI>"? super T" as in "List<? super T>"
     * </UL>
     * @return
     */
    public boolean isWildcardType() {
        return node.isWildcardType();
    }

    /**
     * i.e.
     * <CODE>
     * _typeRef _tr = _typeRef.of("? extends Serializable");
     * assertTrue( _tr.isWildcardExtendsType(Serializable.class));
     * </CODE>
     * @param wildcardExtendsClass
     * @return
     */
    public boolean isWildcardExtends( Class wildcardExtendsClass ){
        return isWildcardExtends( t-> Types.equal(t, Types.of(wildcardExtendsClass)));
    }

    /**
     * i.e.
     * "? extends Serializable"
     *
     * @param wildcardExtendsType
     * @return
     */
    public boolean isWildcardExtends( Predicate<ReferenceType> wildcardExtendsType){
        if( isWildcardType() && this.node.asWildcardType().getExtendedType().isPresent()){
            return wildcardExtendsType.test( this.node.asWildcardType().getExtendedType().get() );
        }
        return false;
    }

    /**
     * i.e.
     * <CODE>
     * _typeRef _tr = _typeRef.of("? super Serializable");
     * assertTrue( _tr.isWildcardSuperType(Serializable.class));
     * </CODE>
     * @param wildcardSuperClass
     * @return
     */
    public boolean isWildcardSuper( Class wildcardSuperClass ){
        return isWildcardSuper( t-> Types.equal(t, Types.of(wildcardSuperClass)));
    }

    /**
     * i.e.
     * "? extends Serializable"
     *
     * @param wildcardSuperType
     * @return
     */
    public boolean isWildcardSuper( Predicate<ReferenceType> wildcardSuperType){
        if( isWildcardType() && this.node.asWildcardType().getSuperType().isPresent()){
            return wildcardSuperType.test( this.node.asWildcardType().getSuperType().get() );
        }
        return false;
    }

    /**
     * <UL>
     * <LI>"?" as in "List<?>"
     * <LI>"? extends T" as in "List<? extends T>"
     * <LI>"? super T" as in "List<? super T>"
     * </UL>
     * @return
     */
    public boolean isWildcardType(Predicate<WildcardType> wtp){
        if( isWildcardType() ){
           WildcardType wct = node.asWildcardType();
            return wtp.test(wct);
        }
        return false;
    }

    public boolean isIntersectionType(_typeRef _tr){
        return isIntersectionType( it-> it.getElements().stream().anyMatch(t-> Types.equal(t, _tr.node())) );
    }

    /**
     * Is this class one of the type
     * @param c
     * @return
     */
    public boolean isIntersectionType(Class c){
        Type targ = Types.of(c);
        return isIntersectionType( it-> it.getElements().stream().anyMatch(t-> Types.equal(t, targ)) );
    }

    public boolean isIntersectionType() {
        return node.isIntersectionType();
    }

    public boolean isIntersectionType(Predicate<IntersectionType> pit){
        if( isIntersectionType() ){
            IntersectionType it = node.asIntersectionType();
            return pit.test(it);
        }
        return false;
    }

    public boolean isUnionType( Predicate<UnionType> put ){
        if( isUnionType() ){
            UnionType ut = node.asUnionType();
            return put.test(ut);
        }
        return false;
    }

    public boolean isUnionType(_typeRef _tr){
        return isUnionType( ut-> ut.getElements().stream().anyMatch(t-> Types.equal(t, _tr.node())) );
    }

    /**
     * Is this class one of the type
     * @param targ
     * @return
     */
    public boolean isUnionType(Type targ){
        return isUnionType( ut-> ut.getElements().stream().anyMatch(t-> Types.equal(t, targ)) );
    }

    /**
     * Is this class one of the type
     * @param c
     * @return
     */
    public boolean isUnionType(Class c){
        return isUnionType(Types.of(c));
    }

    /**
     *
     * @return
     */
    public boolean isUnionType() {
        return node.isUnionType();
    }

    /**
     * Does this type Represent Any Generic Type?
     * @return
     */
    public boolean isGenericType(){
        return node.isClassOrInterfaceType() && node.asClassOrInterfaceType().getTypeArguments().isPresent() ||
                node.isArrayType() && node.asArrayType().getElementType().isClassOrInterfaceType() && node.asArrayType().getElementType().asClassOrInterfaceType().getTypeArguments().isPresent() ;
    }

    /**
     * Gets the Type without Type Arguments
     * <PRE>
     * i.e.
     * _typeRef _tr = _typeRef.of( "Map<String,Integer>" );
     * assertEquals( _typeRef.of("Map"), _tr.getErasedType());
     * </PRE>
     * @return a _typeRef with generics erased
     */
    public _typeRef getErasedType(){
        return _typeRef.of( getErasedType(this.node));
    }

    /**
     * Returns a corresponding _typeRef stripped of:
     * <UL>
     * <LI>annotations
     * <LI>array brackets
     * <LI>type arguments
     * </UL>
     * @return
     */
    public _typeRef getBaseType(){
        return _typeRef.of(getBaseType( this.node));
    }

    public static Type getElementType( Type type){
        if( type.isArrayType() ){
            return getElementType(type.getElementType());
        }
        return type;
    }

    /**
     * Prints the array dimensions
     * @param level
     * @return
     */
    private static String dims(int level){
        String dims = "";
        for(int i=0;i<level;i++){
            dims+="[]";
        }
        return dims;
    }

    public static boolean isGenericType(Type type){
        return ( type.isClassOrInterfaceType() && type.asClassOrInterfaceType().getTypeArguments().isPresent() ) ||
                type.isArrayType() && type.asArrayType().getElementType().isClassOrInterfaceType() &&
                        type.asArrayType().getElementType().asClassOrInterfaceType().getTypeArguments().isPresent();
    }

    public static Type getErasedType( Type type ){
        if( type.getClass() == UnknownType.class || type.getClass() ==TypeParameter.class || type.getClass() == WildcardType.class){
            return type;
        }
        String st = type.toString(Print.PRINT_NO_TYPE_PARAMETERS );
        try {
            return Types.of(st);
        }catch(Exception e){
            //System.out.println(type.getClass()+" "+type);
            throw new _jdraftException("unable to get erased type " + type, e);
        }
    }

    public static Type getBaseType(Type type ){
        Type t = getErasedType(getElementType(type));
        //now remove annotations
        return Types.of( t.toString(Print.PRINT_NO_ANNOTATIONS_OR_COMMENTS));
    }

    /**
     * VarTypes are types that are awaiting type inference
     * Examples:
     * <ol>
     * <li><b>var</b> a = 1;</li>
     * <li><b>var</b> a = new ArrayList&lt;String>();</li>
     * </ol>
     * @return true if the type is a varType
     */
    public boolean isVarType(){
        return node instanceof VarType;
    }

    public boolean isArrayType() {
        return node.isArrayType();
    }

    public boolean isVoid() {
        return node.isVoidType();
    }

    public int getArrayDimensions() {
        return node.getArrayLevel();
    }

    public NodeList<Type> getTypeArguments(){
        if( node.isClassOrInterfaceType() && node.asClassOrInterfaceType().getTypeArguments().isPresent()){
            return node.asClassOrInterfaceType().getTypeArguments().get();
        }
        return new NodeList<Type>();
    }

    @Override
    public String toString() {
        return this.node.toString();
    }

    @Override
    public int hashCode() {
        return Deconstructed.of(this.node.toString() ).normalize().hashCode();
    }

    public _typeRef copy(){
        return of(this.node.clone());
    }


    public boolean is(java.lang.reflect.Type t){
        if( t.getTypeName().startsWith("[") ){
            //primitive arrays
            /** ARRG we have to handle Array Types returns because they return "[Z", "[B", ,"[S, "[F"... for typeName()*/
            return equals(_typeRef.of(t.toString()));
        }
        //otherwise, this will maintain the generics etc.
        return equals(_typeRef.of(t.getTypeName()));
    }

    public boolean is(Class expectedType ){
        try{
            return of( expectedType ).equals(this);
        }
        catch( Exception e) {
            return false;
        }
    }

    @Override
    public boolean is( String... type  ){
        Stencil st = Stencil.of(type);
        if( st.isMatchAny() ){
            return true;
        }
        if( st.isFixedText() ) {
            try {
                return of(Text.combine(type)).equals(this);
            } catch (Exception e) {
                return false;
            }
        }
        return st.matches(this.toString(Print.PRINT_NO_COMMENTS));
    }


    @Override
    public boolean is( Type typeDecl ){
        try{
            return of( typeDecl ).equals(this);
        }
        catch( Exception e) {
            return false;
        }
    }

    public static _feature._one<_typeRef, _annos> ANNOS = new _feature._one<>(_typeRef.class, _annos.class,
            _feature._id.ANNOS,
            a -> a.getAnnos(),
            (_typeRef t, _annos _ae) -> t.setAnnos(_ae), PARSER);

    public static _feature._one<_typeRef, Type> TYPE = new _feature._one<>(_typeRef.class, Type.class,
            _feature._id.TYPE,
            a -> a.node(),
            (_typeRef p, Type t) -> p.setType(t), PARSER);

    public static _feature._features<_typeRef> FEATURES = _feature._features.of(_typeRef.class,  PARSER, ANNOS, TYPE);

    /**
     * Replace the underlying node within the AST (if this node has a parent)
     * and return this (now pointing to the new node)
     * @param replaceNode the node instance to swap in for the old node that this facade was pointing to
     * @return the modified this (now pointing to the replaceNode which was swapped into the AST)
     */
    public _typeRef replace(Type replaceNode){
        this.node.replace(replaceNode);
        this.node = (T)replaceNode;
        return this;
    }

    public _typeRef setType(Type t){
        this.node.replace(t);
        return this;
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
        final _typeRef<?> other = (_typeRef<?>)obj;
        if( this.node == other.node){
            return true; // two _typeRef s pointing to the same ast Type
        }
        //union types can equal one another (
        if( this.node instanceof UnionType && other.node instanceof UnionType){
            Set<ReferenceType> rts = new HashSet<>();
            rts.addAll( ((UnionType) this.node).getElements());
            Set<ReferenceType> ots = new HashSet<>();
            ots.addAll( ((UnionType) other.node).getElements());
            if( rts.size() != ots.size() ){
                return false;
            }
            return  rts.stream().allMatch(r -> ots.stream().anyMatch(o-> Types.equal( o, r) ) );
        }
        if( this.isPrimitiveType() && other.isPrimitiveType() ) {
            return Objects.equals(this.node, other.node);
        }
        Deconstructed td = Deconstructed.of(this.node.toString() );
        Deconstructed od = Deconstructed.of(other.node.toString() );
        return td.equals( od );
    }

    /**
     * returns a normalized String version of this _typeRef
     * 
     * When we normalize, we take any"Fully qualified" parts and convert them to
     * non fully qualified...
     * i.e.
     * <PRE>
     * assertEqauls( _typeRef.of("java.util.List<java.util.Map>").normalize(), 
     *     "List<Map>");
     * </PRE>
     * @return 
     */
    public String normalized(){
        return Normalizer.of( this.node.toString() );
    }

    public interface _withType<N extends Node, _WT extends _tree._node> extends _tree._node<N, _WT> {

        /**
         * @return they type
         */
        default _typeRef getType(){
            return of(((NodeWithType) node()).getType());
        }

        /**
         * Gets the element type of the type (i.e. if the type is an Array type)
         * @return the array element type
         */
        default _typeRef getElementType() {
            return of(getType().node().getElementType());
        }

        /**
         * Set the TYPE and return the modified entity
         * @param t the _typeRef object
         * @return the modified entity after setting the TYPE
         */
        default _WT setType(Type t){
            ((NodeWithType) node()).setType(t);
            return (_WT)this;
        }

        /**
         *
         * @return
         */
        default boolean isArrayType(){
            return getType().isArrayType();
        }

        /**
         * set the TYPE and return
         * @param _tr
         * @return
         */
        default _WT setType(_typeRef _tr) {
            return setType(_tr.node());
        }

        /**
         * Set the TYPE and return the modified entity
         * @param typeRef the String representation of the TYPE
         * @return the modified entity after setting the TYPE
         */
        default _WT setType(String typeRef) {
            return setType(Types.of(typeRef));
        }

        /**
         * Set the TYPE and return the modified entity
         * @param clazz the class of the TYPE to set
         * @return the modified entity after setting the TYPE
         */
        default _WT setType(Class clazz) {
            return setType(Types.of(clazz.getCanonicalName()));
        }

        /**
         * is the type void
         * @return true if void
         */
        default boolean isVoid() {
            return getType().node().isVoidType();
        }

        /**
         * Is the TYPE of this entity the same as represented where the _typeRef TYPE
         * @param type the _typeRef representation of the TYPE
         * @return true if the TYPE is the same
         */
        default boolean isType(Type type) {
            return Types.equal( getType().node(), type);
        }

        /**
         * Is the TYPE of this entity the same as represented where the _typeRef TYPE
         * @param type the _typeRef representation of the TYPE
         * @return true if the TYPE is the same
         */
        default boolean isType(_typeRef type) {
            return getType().equals(type);
        }

        /**
         * Is the TYPE of this entity the same as represented where the _typeRef TYPE
         * @param clazz the Class representation of the TYPE
         * @return true if the TYPE is the same
         */
        default boolean isType(Class clazz) {
            try {
                return isType(clazz.getCanonicalName()) || isType(clazz.getSimpleName());
            } catch (Exception e) {
            }
            return false;
        }

        /**
         * Does the type of this named typed comply with the lambda
         * @param typeMatchFn lambda matcher function
         * @return true if the type
         */
        default boolean isType(Predicate<_typeRef> typeMatchFn) {
            return typeMatchFn.test(getType());
        }

        /**
         * Is the TYPE of this entity the same as represented where the String TYPE
         * @param type the String representation of the TYPE
         * @return true if the TYPE is the same
         */
        default boolean isType(String type) {
            try {
                return isType(Types.of(type));
            } catch (Exception e) {
            }
            return false;
        }
    }

    /**
     * NOTE :  THIS IS ALLOWING US TO ENTER A WORLD OF PAIN, 
     * since the _annos that we return is a COPY and if we MUTATE IT
     * it will not have the changes reflected in the ubnderlying code
     * 
     * We did it because, well Type in the (JavaParser) AST has Annotations
     * but does not implement NodeWithAnnotations
     * 
     * THANKFULLY type use annotations are exceedingly rare
     * @return 

    @Override
    public _annos getAnnos() {
        _annos _as = _annos.of();
        _as.add(this.astType.getAnnotations().toArray(new AnnotationExpr[0]));
        return _as;        
    }
    */

    private static class Normalizer{
        /**
         * When we create a Local Class and ask for it's name, it will have
         * this weird "$#$" qualifier, where # is some number...
         * Here is an example:
         * <PRE>
         * draft.java._classTest$1$Hoverboard
         * </PRE>
         * ...well we want to identify these patterns and convert them into dots
         * draft.java._classTest.Hoverboard
         */
        public static final String LOCAL_CLASS_NAME_PACKAGE_PATTERN = "\\$?\\d+\\$";
        
        private static String normalize( String s ) {
            if( s.length() == 0 ){
                return s;
            }
            /*
            look for the tell-tale local anonymous package pattern $#$ wheren # is a number... like "$1$" in :
            "draft.java._classTest$1$Hoverboard"
            ...and replace it with a '.'
            */
            s = s.replaceAll(LOCAL_CLASS_NAME_PACKAGE_PATTERN, ".");
            int idx = s.lastIndexOf( '.' );
            if( idx < 0 ) {
                return s;
            }
            return s.substring( idx + 1 );
        }
        
        public static String of( String str ) {
            String currentToken = "";
            StringBuilder sb = new StringBuilder();
            for( int i = 0; i < str.length(); i++ ) {
                char c = str.charAt( i );
                switch( c ) {
                    case '<':
                    case '&':
                    case '>':
                    case '|':
                    case ',':   
                        sb.append( normalize(currentToken) );  //normalize previous token                      
                        sb.append( c );
                        currentToken = "";
                        break;
                    default:
                        currentToken += c;
                        break;
                }
            }
            sb.append( normalize(currentToken) );            
            return sb.toString();
        }        
    }

    /** we "partsMap" the TYPE into symbols and tokens to check for equality */
    private static class Deconstructed {

        /**
         * When we create a Local Class and ask for it's name, it will have
         * this weird "$#$" qualifier, where # is some number...
         * Here is an example:
         * <PRE>
         * draft.java._classTest$1$Hoverboard
         * </PRE>
         * ...well we want to identify these patterns and convert them into dots
         * draft.java._classTest.Hoverboard
         */
        public static final String LOCAL_CLASS_NAME_PACKAGE_PATTERN = "\\$?\\d+\\$";

        public List<Character> symbols;
        public List<String> tokens;


        public static Deconstructed of( String str ) {
            List<Character> symbols = new ArrayList<>();
            List<String> tokens = new ArrayList<>();

            StringBuilder sb = new StringBuilder();
            for( int i = 0; i < str.length(); i++ ) {
                char c = str.charAt( i );
                switch( c ) {
                    case '<':
                    case '&':
                    case '>':
                    case '|':
                    case ',':
                        symbols.add( c );
                        if( sb.length() > 0 ) {
                            String tok = sb.toString().trim();
                            if( tok.length() > 0 ){
                                tokens.add( tok );
                            }
                            sb.delete( 0, sb.length() );
                        }
                        break;
                    default:
                        sb.append( c );
                        break;
                }
            }
            if( sb.length() > 0 ) {
                tokens.add( sb.toString() );
            }
            return new Deconstructed( symbols, tokens );
        }

        /** normalize fully qualified names used as tokens to Simple Names */
        public Deconstructed normalize(){
            for(int i=0;i<tokens.size();i++){
                tokens.set( i, normalize(tokens.get(i)) );
            }
            return this;
        }

        private static String normalize( String s ) {

            /*
            look for the tell-tale local anonymous package pattern $#$ wheren # is a number... like "$1$" in :
            "draft.java._classTest$1$Hoverboard"
            ...and replace it with a '.'
            */
            s = s.replaceAll(LOCAL_CLASS_NAME_PACKAGE_PATTERN, ".");
            int idx = s.lastIndexOf( '.' );
            if( idx < 0 ) {
                return s;
            }
            return s.substring( idx + 1 );
        }

        public Deconstructed( List<Character> symbols, List<String> tokens ) {
            this.symbols = symbols;
            this.tokens = tokens;
        }

        @Override
        public int hashCode() {
            int hash = 5;
            List<String>toks = new ArrayList<>();
            this.tokens.forEach( t-> toks.add( normalize(t )) );
            hash = 29 * hash + Objects.hashCode( this.symbols );
            hash = 29 * hash + Objects.hashCode( toks );
            return hash;
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
            final Deconstructed other = (Deconstructed)obj;
            return equals( other );
        }

        public boolean equals( Deconstructed od ) {
            //the broken up symbols must be the same
            if( !this.symbols.equals( od.symbols ) ) {
                return false;
            }
            if( this.tokens.size() != od.tokens.size() ) {
                return false;
            }
            for( int i = 0; i < this.tokens.size(); i++ ) {
                if( !this.tokens.get( i ).equals( od.tokens.get( i ) ) ) {
                    String tn = normalize( this.tokens.get( i ) );
                    String on = normalize( od.tokens.get( i ) );
                    if( !tn.equals( on ) ) {
                        return false;
                    }
                }
            }
            return true;
        }
    }
}
