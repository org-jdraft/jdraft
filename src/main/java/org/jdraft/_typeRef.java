package org.jdraft;

import java.util.*;

import com.github.javaparser.ast.NodeList;
import com.github.javaparser.ast.expr.AnnotationExpr;
import com.github.javaparser.ast.type.*;

import org.jdraft.text.Text;

/**
 * Model of a Java TYPE Declaration (i.e. String, int, List<Boolean>)
 * wraps an AST {@link Type} implementation
 *
 * @param <T> the Type implementation (i.e. ReferenceType)
 */
public final class _typeRef<T extends Type>
        implements _java._compoundNode<Type, _typeRef>, _anno._hasAnnos<_typeRef> {

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
        return new _typeRef( Ast.typeRef( t ) );
    }

    /**
     *
     * @param t
     * @return
     */
    public static _typeRef of( java.lang.reflect.Type t){
        return new _typeRef( Ast.typeRef( t ) );
    }

    /**
     *
     * @param clazz
     * @return
     */
    public static _typeRef of( Class clazz ) {
        return new _typeRef( Ast.typeRef( clazz ) );
    }

    /**
     *
     * @param string
     * @return
     */
    public static _typeRef of( String string ) {
        return new _typeRef( Ast.typeRef( string ) );
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
        Type t = getErasedType(this.astType);
        List<AnnotationExpr> aes = new ArrayList<>();
        t.walk(AnnotationExpr.class, a->aes.add(a));
        return _annos.of(aes);
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
        return getErasedType(this.astType).findFirst(AnnotationExpr.class).isPresent();
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
        return Ast.typesEqual( a.ast(), b.ast());
    }

    /** The underlying AST type */
    private final T astType;

    public _typeRef( T t ) {
        this.astType = t;
    }

    @Override
    public Type ast() {
        return astType;
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
        return astType.isUnknownType();
    }

    public boolean isPrimitive() {
        return astType.isPrimitiveType();
    }

    public Type getElementType(){
        return this.astType.getElementType();
    }

    public boolean isWildcard() {
        return astType.isWildcardType();
    }

    public boolean isIntersectionType() {
        return astType.isIntersectionType();
    }

    public boolean isUnionType() {
        return astType.isUnionType();
    }

    /**
     * Does this type Represent Any Generic Type?
     * @return
     */
    public boolean isGenericType(){
        return astType.isClassOrInterfaceType() && astType.asClassOrInterfaceType().getTypeArguments().isPresent() ||
                astType.isArrayType() && astType.asArrayType().getElementType().isClassOrInterfaceType() && astType.asArrayType().getElementType().asClassOrInterfaceType().getTypeArguments().isPresent() ;
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
        return _typeRef.of( getErasedType(this.astType));
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
        return _typeRef.of(getBaseType( this.astType ));
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
        //System.out.println("BEFORE \""+type.getClass()+"\"" );
        //System.out.println("BEFORE THE ERASED TYPE \""+type.toString(Ast.PRINT_NO_TYPE_PARAMETERS )+"\"" );

        String st = type.toString(Ast.PRINT_NO_TYPE_PARAMETERS );
        //System.out.println("THE ERASED TYPE "+st );
        try {
            return Ast.typeRef(st);
        }catch(Exception e){
            System.out.println(type.getClass()+" "+type);
            throw new _jdraftException("GOT HERE", e);
            //return type;
        }
    }

    public static Type getBaseType(Type type ){
        Type t = getErasedType(getElementType(type));
        //now remove annotations
        return Ast.typeRef( t.toString(Ast.PRINT_NO_ANNOTATIONS_OR_COMMENTS));
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
        return astType instanceof VarType;
    }

    public boolean isArrayType() {
        return astType.isArrayType();
    }

    public boolean isVoid() {
        return astType.isVoidType();
    }

    public int getArrayDimensions() {
        return astType.getArrayLevel();
    }

    public NodeList<Type> getTypeArguments(){
        if( astType.isClassOrInterfaceType() && astType.asClassOrInterfaceType().getTypeArguments().isPresent()){
            return astType.asClassOrInterfaceType().getTypeArguments().get();
        }
        return new NodeList<Type>();
    }

    @Override
    public String toString() {
        return this.astType.toString();
    }

    @Override
    public int hashCode() {
        return Deconstructed.of(this.astType.toString() ).normalize().hashCode();
    }

    public _typeRef copy(){
        return of(this.astType.clone());
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
        try{
            return of( Text.combine(type) ).equals(this);
        }
        catch( Exception e) {
            return false;
        }
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

    @Override
    public Map<_java.Component, Object> components( ) {
        Map<_java.Component, Object> parts = new HashMap<>();
        parts.put( _java.Component.AST_TYPE, this.astType);
        parts.put( _java.Component.ARRAY_LEVEL, this.astType.getArrayLevel());
        parts.put( _java.Component.ELEMENT_TYPE, this.astType.getElementType());
        return parts;
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
        if( this.astType == other.astType){
            return true; // two _typeRef s pointing to the same ast Type
        }
        if( this.isPrimitive() && other.isPrimitive() ) {
            return Objects.equals(this.astType, other.astType );
        }
        Deconstructed td = Deconstructed.of(this.astType.toString() );
        Deconstructed od = Deconstructed.of(other.astType.toString() );
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
        return Normalizer.of( this.astType.toString() );        
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
