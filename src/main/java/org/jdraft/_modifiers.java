package org.jdraft;

import com.github.javaparser.ast.NodeList;
import com.github.javaparser.ast.nodeTypes.NodeWithModifiers;
import com.github.javaparser.ast.Modifier;
import java.util.*;

/**
 * I find the default AST implementation (the EnumSet<Modifier>)
 * cumbersome to use in practice
 * this tries to make it more palatable from a Program dev API perspective
 *
 * @author Eric
 */
public final class _modifiers implements _java {

    /** Making the internal AST modifiers more accessible */
    public static final Modifier PUBLIC = Modifier.publicModifier();
    public static final Modifier PRIVATE = Modifier.privateModifier();
    public static final Modifier PROTECTED = Modifier.protectedModifier();
    public static final Modifier STATIC = Modifier.staticModifier();
    public static final Modifier FINAL = Modifier.finalModifier();
    public static final Modifier ABSTRACT = Modifier.abstractModifier();
    public static final Modifier SYNCHRONIZED = Modifier.synchronizedModifier();
    public static final Modifier STRICT = Modifier.strictfpModifier();
    public static final Modifier VOLATILE = Modifier.volatileModifier();
    public static final Modifier TRANSIENT = Modifier.transientModifier();
    public static final Modifier NATIVE = Modifier.nativeModifier();

    private final NodeWithModifiers node;

    public static _modifiers of( NodeWithModifiers nm ) {
        return new _modifiers( nm );
    }

    public static _modifiers of() {
        return new _modifiers();
    }

    public NodeWithModifiers astHolder() {
        return node;
    }

    //this will handle
    public static _modifiers of( String... mods ) {
        _modifiers _ms = new _modifiers();

        for( int i = 0; i < mods.length; i++ ) {
            String[] strs = mods[ i ].split( " " );
            for( int j = 0; j < strs.length; j++ ) {
                if( strs[ j ].trim().length() > 0 ) {
                    _ms.set( strs[ j ] );
                }
            }
        }
        return _ms;
    }

    public static _modifiers of( com.github.javaparser.ast.Modifier... mods ) {
        _modifiers _ms = new _modifiers();

        for( int i = 0; i < mods.length; i++ ) {
            _ms.set(mods[i]);
        }
        return _ms;
    }

    public NodeList<Modifier> ast() {
        return node.getModifiers();
    }

    public _modifiers( NodeWithModifiers nm ) {
        this.node = nm;
    }

    private _modifiers() {
        this.node = Ast.field( "int dummyParent;" );
    }

    public _modifiers set( String... keywords ) {
        Arrays.stream( keywords ).forEach( kw -> set( kw ) );
        return this;
    }

    public _modifiers set( String keyword ) {
        com.github.javaparser.ast.Modifier m = KEYWORD_TO_ENUM_MAP.get( keyword );
        if( m == null ) {
            throw new IllegalArgumentException( "invalid modifier keyword \"" + keyword + "\"" );
        }
        node.setModifier( m.getKeyword(), true );
        return this;
    }

    public _modifiers unset( String keyword ) {
        com.github.javaparser.ast.Modifier m = KEYWORD_TO_ENUM_MAP.get( keyword );
        if( m == null ) {
            throw new IllegalArgumentException( "invalid modifier keyword \"" + keyword + "\"" );
        }
        node.setModifier( m.getKeyword(), false );
        return this;
    }

    public _modifiers set(com.github.javaparser.ast.Modifier.Keyword keyword ) {
        if( keyword.equals( Modifier.Keyword.PUBLIC) ||
            keyword.equals( Modifier.Keyword.PROTECTED ) ||
            keyword.equals( Modifier.Keyword.PRIVATE ) ) {// || mod == Modifier.DEFAULT ) {
            setDefaultAccess();
        }
        node.setModifier( keyword, true );
        return this;
    }
    public _modifiers set( com.github.javaparser.ast.Modifier mod ) {
        if( mod.equals( Modifier.publicModifier()) ||
                mod.equals( Modifier.privateModifier() ) ||
                mod.equals(Modifier.protectedModifier() ) ) {// || mod == Modifier.DEFAULT ) {
            setDefaultAccess();
        }
        node.setModifier( mod.getKeyword(), true );
        return this;
    }

    public _modifiers unset( com.github.javaparser.ast.Modifier mod ) {
        node.setModifier( mod.getKeyword(), false );
        return this;
    }


    @Override
    public int hashCode() {
        return node.getModifiers().hashCode();
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
        final _modifiers other = (_modifiers)obj;
        return this.node.getModifiers().equals( other.node.getModifiers() );
    }

    /**
     *
     * @return
     */
    public boolean isEmpty(){
        return this.node.getModifiers().isEmpty();
    }

    /**
     *
     * @param mods
     * @return
     */
    public boolean is( String... mods ) {
        try {
            _modifiers _ms = _modifiers.of( mods );
            return this.equals( _ms );
        }
        catch( Exception e ) {

        }
        return false;
    }

    /**
     *
     * @return
     */
    public boolean isPublic() {
        return this.node.hasModifier( Modifier.Keyword.PUBLIC );
    }

    /**
     *
     * @return
     */
    public boolean isPrivate() {
        return this.node.hasModifier( Modifier.Keyword.PRIVATE );
    }

    /**
     *
     * @return
     */
    public boolean isProtected() {
        return this.node.hasModifier( Modifier.Keyword.PROTECTED );
    }

    /**
     *
     * @return
     */
    public boolean isPackagePrivate(){
        return !isPublic() && ! isProtected() && ! isPrivate();
    }
    
    /**
     * Is this entity a Java 8+ default method (i.e. on an interface)?
     * //HMMM SHOULD I ALSO CHECK IF ITS A METHOD ON AN INTERFACE / INFERRED
     * @return
     */
    public boolean isDefault() {
        return this.node.hasModifier( Modifier.Keyword.DEFAULT );
    }

    /**
     * Sets the modifier to (interface default) i.e. a Java 8+ default method
     * applied to an interface
     * @return the modified MODIFIERS
     */
    public _modifiers setDefault() {
        this.node.setModifier(Modifier.Keyword.DEFAULT, true );
        return this;
    }

    /**
     *
     * @return
     */
    public _modifiers setDefaultAccess() {
        NodeList<Modifier> ms = this.node.getModifiers();
        this.node.removeModifier( Modifier.Keyword.PUBLIC, Modifier.Keyword.PROTECTED, Modifier.Keyword.PRIVATE);

        return this;
    }

    /**
     *
     * @return
     */
    public boolean isAbstract() {
        return this.node.hasModifier( Modifier.Keyword.ABSTRACT );
    }

    public boolean isFinal() {
        return this.node.hasModifier( Modifier.Keyword.FINAL );
    }

    public boolean isVolatile() {
        return this.node.hasModifier( Modifier.Keyword.VOLATILE );
    }

    public boolean isTransient() {
        return this.node.hasModifier( Modifier.Keyword.TRANSIENT );
    }

    public boolean isSynchronized() {
        return this.node.hasModifier( Modifier.Keyword.SYNCHRONIZED );
    }

    public boolean isStrict() {
        return this.node.hasModifier( Modifier.Keyword.STRICTFP );
    }

    public boolean isStatic() {
        return this.node.hasModifier( Modifier.Keyword.STATIC );
    }

    public boolean isNative() {
        return this.node.hasModifier( Modifier.Keyword.NATIVE );
    }

    public _modifiers setPublic() {
        this.setDefaultAccess().set( Modifier.Keyword.PUBLIC );
        return this;
    }

    public _modifiers setProtected() {
        return this.setDefaultAccess().set( Modifier.Keyword.PROTECTED );
    }

    public _modifiers setPrivate() {
        return this.setDefaultAccess().set( Modifier.Keyword.PRIVATE );
    }

    public _modifiers setStatic() {
        set( Modifier.Keyword.STATIC );
        return this;
    }

    public _modifiers setFinal() {
        set( Modifier.Keyword.FINAL );
        return this;
    }

    public _modifiers setSynchronized() {
        set( Modifier.Keyword.SYNCHRONIZED );
        return this;
    }

    public _modifiers setAbstract() {
        set( Modifier.Keyword.ABSTRACT );
        return this;
    }

    public _modifiers setNative() {
        set( Modifier.Keyword.NATIVE );
        return this;
    }

    public _modifiers setTransient() {
        set( Modifier.Keyword.TRANSIENT );
        return this;
    }

    public _modifiers setVolatile() {
        set( Modifier.Keyword.VOLATILE );
        return this;
    }

    public _modifiers setStrictFP() {
        set( Modifier.Keyword.STRICTFP );
        return this;
    }

    public _modifiers setAbstract( boolean toSet ) {
        this.node.setModifier( Modifier.Keyword.ABSTRACT, toSet );
        return this;
    }

    public _modifiers setStatic( boolean toSet ) {
        this.node.setModifier( Modifier.Keyword.STATIC, toSet );
        return this;
    }

    public _modifiers setFinal( boolean toSet ) {
        this.node.setModifier( Modifier.Keyword.FINAL, toSet );
        return this;
    }

    public _modifiers setSynchronized( boolean toSet ) {
        this.node.setModifier( Modifier.Keyword.SYNCHRONIZED, toSet );
        return this;
    }

    public _modifiers setNative( boolean toSet ) {
        this.node.setModifier( Modifier.Keyword.NATIVE, toSet );
        return this;
    }

    public _modifiers setTransient( boolean toSet ) {
        this.node.setModifier( Modifier.Keyword.TRANSIENT, toSet );
        return this;
    }

    public _modifiers setVolatile( boolean toSet ) {
        this.node.setModifier( Modifier.Keyword.VOLATILE, toSet );
        return this;
    }

    public _modifiers setStrictFp( boolean toSet ) {
        this.node.setModifier( Modifier.Keyword.STRICTFP, toSet );
        return this;
    }

    public _modifiers clear() {
        this.node.getModifiers().clear();
        return this;
    }

    @Override
    public String toString() {

        String[] strs = this.asKeywords();
        StringBuilder sb = new StringBuilder();
        for( int i = 0; i < strs.length; i++ ) {
            if( i > 0 ) {
                sb.append( " " );
            }
            sb.append( strs[ i ] );
        }
        return sb.toString();
    }

    public String[] asKeywords() {
        List<String> strs = new ArrayList<>();
        if( node != null && node.getModifiers() != null ) {
            this.node.getModifiers().forEach( i -> {
                strs.add( ((Modifier)i).getKeyword().asString() );
            } );
        }
        return strs.toArray( new String[ 0 ] );
    }

    public static String[] getKeywords( int mods ) {
        if( mods == 0 ) {
            return new String[ 0 ];
        }
        int count = Integer.bitCount( mods );
        String[] keywords = new String[ count ];
        int theMods = mods;
        for( int i = 0; i < count; i++ ) {
            //bithacks: isolate the rightmost bit
            int nextBit = theMods & -theMods;
            keywords[ i ] = (BIT_TO_KEYWORD_MAP.get( nextBit ));

            //bithacks: turn off the rightmost bit
            theMods = theMods & (theMods - 1);
        }
        return keywords;
    }

    private String bitsToKeywordsString( int mods ) {
        StringBuilder sb = new StringBuilder();
        boolean first = true;
        int theMods = mods;
        while( theMods != 0 ) {
            //bithacks: isolate the rightmost bit
            int nextBit = theMods & -theMods;

            if( !first ) {
                sb.append( " " );
            }
            sb.append( BIT_TO_KEYWORD_MAP.get( nextBit ) );

            //bithacks: turn off the rightmost bit
            theMods = theMods & (theMods - 1);
            first = false;
        }
        if( sb.length() > 0 ) {
            sb.append( " " );
        }
        return sb.toString();
    }

    /**
     * does this _modifiers contain all of the modifiers
     * @param mods the modifiers to check
     * @return true IFF this _modifiers contains all of the mods in NodeList<Modifier>
     */
    public boolean containsAll( Collection<Modifier> mods ) {
        return this.node.getModifiers().containsAll(mods);
    }
    
    /**
     * does this _modifiers contain ANY of the modifiers 
     * @param mods the modifiers to check
     * @return true IFF this _modifiers contains any of the modifiers in the list
     */
    public boolean containsAny( Collection<Modifier> mods ){
        Optional<Modifier> om = 
            mods.stream().filter( m-> this.node.hasModifier(m.getKeyword()) ).findFirst();
        if( om.isPresent() ){
            return true;
        }
        return false;
    }
    
    public static final Map<String, Integer> KEYWORD_TO_BIT_MAP = new HashMap<String, Integer>();
    public static final Map<Integer, String> BIT_TO_KEYWORD_MAP = new HashMap<Integer, String>();
    public static final Map<Integer, com.github.javaparser.ast.Modifier> BIT_TO_ENUM_MAP = new HashMap<>();
    public static final Map<String, com.github.javaparser.ast.Modifier> KEYWORD_TO_ENUM_MAP = new HashMap<>();
    public static final Map<com.github.javaparser.ast.Modifier, String> ENUM_TO_KEYWORD_MAP = new HashMap<>();


    static {
        KEYWORD_TO_BIT_MAP.put( "public", java.lang.reflect.Modifier.PUBLIC );
        BIT_TO_KEYWORD_MAP.put( java.lang.reflect.Modifier.PUBLIC, "public" );
        BIT_TO_ENUM_MAP.put( java.lang.reflect.Modifier.PUBLIC, com.github.javaparser.ast.Modifier.publicModifier() );
        ENUM_TO_KEYWORD_MAP.put( com.github.javaparser.ast.Modifier.publicModifier(), "public" );
        KEYWORD_TO_ENUM_MAP.put( "public", com.github.javaparser.ast.Modifier.publicModifier() );

        KEYWORD_TO_BIT_MAP.put( "protected", java.lang.reflect.Modifier.PROTECTED );
        BIT_TO_KEYWORD_MAP.put( java.lang.reflect.Modifier.PROTECTED, "protected" );
        BIT_TO_ENUM_MAP.put( java.lang.reflect.Modifier.PROTECTED, com.github.javaparser.ast.Modifier.protectedModifier() );
        ENUM_TO_KEYWORD_MAP.put( com.github.javaparser.ast.Modifier.protectedModifier(), "protected" );
        KEYWORD_TO_ENUM_MAP.put( "protected", com.github.javaparser.ast.Modifier.protectedModifier() );

        KEYWORD_TO_BIT_MAP.put( "private", java.lang.reflect.Modifier.PRIVATE );
        BIT_TO_KEYWORD_MAP.put( java.lang.reflect.Modifier.PRIVATE, "private" );
        BIT_TO_ENUM_MAP.put( java.lang.reflect.Modifier.PRIVATE, com.github.javaparser.ast.Modifier.privateModifier() );
        KEYWORD_TO_ENUM_MAP.put( "private", com.github.javaparser.ast.Modifier.privateModifier() );
        ENUM_TO_KEYWORD_MAP.put( com.github.javaparser.ast.Modifier.privateModifier(), "private" );

        KEYWORD_TO_BIT_MAP.put( "static", java.lang.reflect.Modifier.STATIC );
        BIT_TO_KEYWORD_MAP.put( java.lang.reflect.Modifier.STATIC, "static" );
        BIT_TO_ENUM_MAP.put( java.lang.reflect.Modifier.STATIC, com.github.javaparser.ast.Modifier.staticModifier() );
        KEYWORD_TO_ENUM_MAP.put( "static", com.github.javaparser.ast.Modifier.staticModifier() );
        ENUM_TO_KEYWORD_MAP.put( com.github.javaparser.ast.Modifier.staticModifier(), "static" );

        KEYWORD_TO_BIT_MAP.put( "synchronized", java.lang.reflect.Modifier.SYNCHRONIZED );
        BIT_TO_KEYWORD_MAP.put( java.lang.reflect.Modifier.SYNCHRONIZED, "synchronized" );
        BIT_TO_ENUM_MAP.put( java.lang.reflect.Modifier.SYNCHRONIZED, com.github.javaparser.ast.Modifier.synchronizedModifier() );
        KEYWORD_TO_ENUM_MAP.put( "synchronized", com.github.javaparser.ast.Modifier.synchronizedModifier() );
        ENUM_TO_KEYWORD_MAP.put( com.github.javaparser.ast.Modifier.synchronizedModifier(), "synchronized" );

        KEYWORD_TO_BIT_MAP.put( "abstract", java.lang.reflect.Modifier.ABSTRACT );
        BIT_TO_KEYWORD_MAP.put( java.lang.reflect.Modifier.ABSTRACT, "abstract" );
        BIT_TO_ENUM_MAP.put( java.lang.reflect.Modifier.ABSTRACT, com.github.javaparser.ast.Modifier.abstractModifier() );
        KEYWORD_TO_ENUM_MAP.put( "abstract", com.github.javaparser.ast.Modifier.abstractModifier() );
        ENUM_TO_KEYWORD_MAP.put( com.github.javaparser.ast.Modifier.abstractModifier(), "abstract" );

        KEYWORD_TO_BIT_MAP.put( "final", java.lang.reflect.Modifier.FINAL );
        BIT_TO_KEYWORD_MAP.put( java.lang.reflect.Modifier.FINAL, "final" );
        BIT_TO_ENUM_MAP.put( java.lang.reflect.Modifier.FINAL, com.github.javaparser.ast.Modifier.finalModifier() );
        KEYWORD_TO_ENUM_MAP.put( "final", com.github.javaparser.ast.Modifier.finalModifier() );
        ENUM_TO_KEYWORD_MAP.put( com.github.javaparser.ast.Modifier.finalModifier(), "final" );

        KEYWORD_TO_BIT_MAP.put( "native", java.lang.reflect.Modifier.NATIVE );
        BIT_TO_KEYWORD_MAP.put( java.lang.reflect.Modifier.NATIVE, "native" );
        BIT_TO_ENUM_MAP.put( java.lang.reflect.Modifier.NATIVE, com.github.javaparser.ast.Modifier.nativeModifier() );
        KEYWORD_TO_ENUM_MAP.put( "native", com.github.javaparser.ast.Modifier.nativeModifier() );
        ENUM_TO_KEYWORD_MAP.put( com.github.javaparser.ast.Modifier.nativeModifier(), "native" );

        KEYWORD_TO_BIT_MAP.put( "transient", java.lang.reflect.Modifier.TRANSIENT );
        BIT_TO_KEYWORD_MAP.put( java.lang.reflect.Modifier.TRANSIENT, "transient" );
        BIT_TO_ENUM_MAP.put( java.lang.reflect.Modifier.TRANSIENT, com.github.javaparser.ast.Modifier.transientModifier() );
        KEYWORD_TO_ENUM_MAP.put( "transient", com.github.javaparser.ast.Modifier.transientModifier() );
        ENUM_TO_KEYWORD_MAP.put( com.github.javaparser.ast.Modifier.transientModifier(), "transient" );

        KEYWORD_TO_BIT_MAP.put( "volatile", java.lang.reflect.Modifier.VOLATILE );
        BIT_TO_KEYWORD_MAP.put( java.lang.reflect.Modifier.VOLATILE, "volatile" );
        BIT_TO_ENUM_MAP.put( java.lang.reflect.Modifier.VOLATILE, com.github.javaparser.ast.Modifier.volatileModifier() );
        KEYWORD_TO_ENUM_MAP.put( "volatile", com.github.javaparser.ast.Modifier.volatileModifier() );

        KEYWORD_TO_BIT_MAP.put( "strictfp", java.lang.reflect.Modifier.STRICT );
        BIT_TO_KEYWORD_MAP.put( java.lang.reflect.Modifier.STRICT, "strictfp" );
        BIT_TO_ENUM_MAP.put( java.lang.reflect.Modifier.STRICT, com.github.javaparser.ast.Modifier.strictfpModifier() );
        KEYWORD_TO_ENUM_MAP.put( "strictfp", com.github.javaparser.ast.Modifier.strictfpModifier() );
        ENUM_TO_KEYWORD_MAP.put( com.github.javaparser.ast.Modifier.strictfpModifier(), "strictfp" );

        //ANY_FOR DEFAULT INTERFACES
        KEYWORD_TO_BIT_MAP.put( "default", 1 << 12 );
        BIT_TO_KEYWORD_MAP.put( 1 << 12, "default" );
        //BIT_TO_ENUM_MAP.put( 1 << 12, com.github.javaparser.ast.Modifier );
        //KEYWORD_TO_ENUM_MAP.put( "default", com.github.javaparser.ast.Modifier.DEFAULT );
    }
    
    /**
     *
     * @author Eric
     * @param <_HM> the target TYPE
     */
    public interface _hasModifiers<_HM extends _hasModifiers>
        extends _java {
        
        /**
         * gets the explicitly set modifiers for the node
         * @return the explicitly set modifiers
         */
        _modifiers getModifiers();

        
        /**
         * Returns the Effective Modifiers as a bitMask
         * @return an int representing the BitMask of the modifiers
         */
        default int modifiersAsBitMask(){
            NodeList<Modifier> effective = getEffectiveModifiers();
            int bitMask = 0;
            for(int i=0;i<effective.size();i++){
                bitMask |= KEYWORD_TO_BIT_MAP.get( effective.get(i).getKeyword().asString() );
            }
            return bitMask;            
        } 
        
        /**
         * Set the modifiers based on the keywords passed in
         * @param mods
         * @return
         */
        default _HM modifiers(String... mods ) {
            getModifiers().set( mods );
            return (_HM)this;
        }
        
        default _HM modifiers(NodeList<Modifier> mods ){
            getModifiers().node.setModifiers(mods);
            return (_HM)this;
        }

        /**
         * Some modifiers are implied and not always printed in the source code...
         *
         * for example:
         * <PRE>
         * //THESE INTERFACES SHOULD BE (SEMANTICALLY) EQUAL:
         * interface I{
         *     int x = 100;
         * }
         * interface I{
         *     public static final int x = 100;
         * }
         * // both  of the above compile and they are semantically the same
         * // (they produce the same .class file)
         *
         * // because an initialized field (a) on an interface
         * // has implied modifiers of "public static final"
         * // but they can be omitted...
         * // we need to calculate the implied modifiers on entities to
         * // test (at the model level) whether two entities are in fact
         * // semantically equal when they aren't syntactically equal
         * </PRE>
         *
         * we can verify this is true by running this test :
         * <PRE>
         *     public @interface A{
         *         int a = 100;
         *     }
         *
         *     public @interface B{
         *         public static final int a = 100;
         *     }
         *
         *     public static void main(String[] args) throws NoSuchFieldException {
         *             assertEquals( B.class.getField("a").getModifiers(),
         *                  A.class.getField("a").getModifiers() );
         *     }
         * </PRE>
         * this requires us to check the container... i.e. if I am looking at
         * a _field that implements _hasModified, I check if the "container" (parent)
         * of the _field is an interface or _annotationType and return the implied modifiers
         * "public static final"
         *
         * Returns the effective modifiers associated with this entity
         * "effective modifiers" is created based on the
         * "explicit" modifiers (i.e. the ones spelled out, i.e. returned from getModifiers()"
         * and the "implied" modifiers that is calculated based on the context
         * for example, if we define a field "x" on an interface:
         *
         * <PRE>interface I { int x = 100; }</PRE>
         *
         * "x" has NO explicit modifiers, but "x" DOES have IMPLIED modifiers...
         * technically the modifiers on x are:
         *
         * <PRE><B>public static final</B> int x = 100;</PRE>
         *
         * ...but they are implied and not explicit, this will return the UNION of
         * the explicit and implied modifiers
         *
         * By default, this will only get the modifiers
         * @return EnumSet of modifiers
         */
        default NodeList<Modifier> getEffectiveModifiers(){

            return getModifiers().node.getModifiers(); //.getModifiers();
        }

        /**
         * Gets the "explicit" modifiers associated with the node
         * (those explicitly spelled out in code).
         *
         * NOTE: implied modifiers are also important
         *
         * @param mods
         * @return
         */
        default _HM modifiers(_modifiers mods ) {
            return replace( mods );
        }

        default _HM replace(_modifiers _ms ) {
            getModifiers().clear().set( _ms.asKeywords() );
            return (_HM)this;
        }
    }

    /**
     * entity with possible final modifier
     * @param <_HF>
     */
    public interface _hasFinal<_HF extends _hasFinal> {

        boolean isFinal();

        default _HF setFinal(){
            return setFinal(true);
        }

        _HF setFinal(boolean toSet );
    }

    /**
     * Entity with possible static modifier
     * @param <_HS>
     */
    public interface _hasStatic<_HS extends _hasStatic>  extends _hasModifiers<_HS> {

        boolean isStatic();

        default _HS setStatic(){
            return setStatic(true);
        }

        default _HS setStatic(boolean toSet ){
            _node n = (_node)this;
            NodeWithModifiers nwm = (NodeWithModifiers)n.ast();
            nwm.setModifier(Modifier.Keyword.STATIC, toSet);
            return (_HS)this;
        }
    }

    /**
     * Entity with possible synchronized modifier
     * @param <_HS>
     */
    public interface _hasSynchronized<_HS extends _hasSynchronized> extends _hasModifiers<_HS> {

        default boolean isSynchronized(){
            return getModifiers().node.hasModifier(Modifier.Keyword.SYNCHRONIZED );
        }

        default _HS setSynchronized(){
            return setSynchronized(true);
        }

        default _HS setSynchronized(boolean toSet ){
            _node n = (_node)this;
            NodeWithModifiers nwm = (NodeWithModifiers)n.ast();
            nwm.setModifier(Modifier.Keyword.SYNCHRONIZED, toSet);
            return (_HS)this;
        }
    }

    /**
     * Entity with possible abstract modifier
     * @param <_HA>
     */
    public interface _hasAbstract<_HA extends _hasAbstract> extends _hasModifiers<_HA> {

        boolean isAbstract();

        default _HA setAbstract(){
            return setAbstract(true);
        }

        default _HA setAbstract(boolean toSet ){
            _node n = (_node)this;
            NodeWithModifiers nwm = (NodeWithModifiers)n.ast();
            nwm.setModifier(Modifier.Keyword.ABSTRACT, toSet);
            return (_HA)this;
        }
    }

    /**
     * Entity with possible volatile modifier
     * @param <_HV>
     */
    public interface _hasVolatile<_HV extends _hasVolatile> extends _hasModifiers<_HV> {

        default boolean isVolatile(){
            return getModifiers().node.hasModifier(Modifier.Keyword.VOLATILE );
        }

        default _HV setVolatile(){
            return setVolatile(true);
        }

        default _HV setVolatile(boolean toSet ){
           _node n = (_node)this;
            NodeWithModifiers nwm = (NodeWithModifiers)n.ast();
            nwm.setModifier(Modifier.Keyword.VOLATILE, toSet);
            return (_HV)this;
        }
    }

    /**
     * Entity with possible native modifier
     * @param <_HN>
     */
    public interface _hasNative<_HN extends _hasNative>  extends _hasModifiers<_HN> {

        default boolean isNative(){
            return getModifiers().node.hasModifier(Modifier.Keyword.NATIVE );
        }

        default _HN setNative(){
            return setNative(true);
        }

        default _HN setNative(boolean toSet ){
            _node n = (_node)this;
            NodeWithModifiers nwm = (NodeWithModifiers)n.ast();
            nwm.setModifier(Modifier.Keyword.NATIVE, toSet);
            return (_HN)this;
        }
    }

    /**
     * Entity with possible transient modifier
     * @param <_HT>
     */
    public interface _hasTransient<_HT extends _hasTransient>  extends _hasModifiers<_HT> {

        default boolean isTransient(){
            return getModifiers().node.hasModifier(Modifier.Keyword.TRANSIENT );
        }

        default _HT setTransient(){
            return setTransient(true);
        }

        default _HT setTransient(boolean toSet ){
            _node n = (_node)this;
            NodeWithModifiers nwm = (NodeWithModifiers)n.ast();
            nwm.setModifier(Modifier.Keyword.TRANSIENT, toSet);
            return (_HT)this;
        }
    }

    /**
     * Entity with possible strictFp modifier
     * @param <_HS>
     */
    public interface _hasStrictFp<_HS extends _hasStrictFp>  extends _hasModifiers<_HS> {

        default boolean isStrictFp(){
            return getModifiers().node.hasModifier(Modifier.Keyword.STRICTFP );
        }

        default _HS setStrictFp(){
            return setStrictFp(true);
        }

        default _HS setStrictFp(boolean toSet ){
            _node n = (_node)this;
            NodeWithModifiers nwm = (NodeWithModifiers)n.ast();
            nwm.setModifier(Modifier.Keyword.STRICTFP, toSet);
            return (_HS)this;
        }
    }
}
