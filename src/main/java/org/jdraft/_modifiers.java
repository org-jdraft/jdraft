package org.jdraft;

import com.github.javaparser.ast.Modifier;
import com.github.javaparser.ast.NodeList;
import com.github.javaparser.ast.nodeTypes.NodeWithModifiers;
import org.jdraft.text.Text;

import java.util.*;
import java.util.function.Function;

/**
 * Representation of the source of a combination / listing of Java Modifiers
 * i.e.<PRE>
 *     "public static final"
 * </PRE>
 *
 * @author Eric
 */
public final class _modifiers implements _java._set<Modifier, _modifier, _modifiers> {

    public static final Function<String, _modifiers> PARSER = s-> _modifiers.of(s);

    private final NodeWithModifiers node;

    public static _modifiers of(NodeWithModifiers nm ) {
        return new _modifiers( nm );
    }

    public static _modifiers of() {
        return new _modifiers();
    }

    public NodeWithModifiers astHolder() {
        return node;
    }

    //this will handle
    public static _modifiers of(String... mods ) {
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

    public _modifiers copy(){
        return _modifiers.of( this.node );
    }

    public static _modifiers of(List<Modifier> mods){
        _modifiers _ms = new _modifiers();

        for( int i = 0; i < mods.size(); i++ ) {
            _ms.set(mods.get(i));
        }
        return _ms;
    }

    public static _modifiers of(_modifier... mods ) {
        _modifiers _ms = new _modifiers();

        for( int i = 0; i < mods.length; i++ ) {
            _ms.set(mods[i].mod);
        }
        return _ms;
    }

    public static _modifiers of(Modifier... mods ) {
        _modifiers _ms = new _modifiers();

        for( int i = 0; i < mods.length; i++ ) {
            _ms.set(mods[i]);
        }
        return _ms;
    }

    public NodeList<Modifier> ast() {
        return node.getModifiers();
    }

    public static _feature._many<_modifiers, _modifier> MODIFIERS = new _feature._many<>(_modifiers.class, _modifier.class,
            _feature._id.MODIFIERS,
            _feature._id.MODIFIER,
            a -> a.list(),
            (_modifiers a, List<_modifier> _ms) -> a.set(_ms), PARSER, s->_modifier.of(s));

    public static _feature._meta<_modifiers> META = _feature._meta.of(_modifiers.class, MODIFIERS );

    public _modifiers(NodeWithModifiers nm ) {
        this.node = nm;
    }

    private _modifiers() {
        this.node = Ast.fieldDeclaration( "int dummyParent;" );
    }

    public _modifiers set(String... keywords ) {
        Arrays.stream( keywords ).forEach( kw -> set( kw ) );
        return this;
    }

    public _modifiers set(String keyword ) {
        Modifier m = Modifiers.MODS_KEYWORD_TO_ENUM_MAP.get( keyword );
        if( m == null ) {
            throw new IllegalArgumentException( "invalid modifier keyword \"" + keyword + "\"" );
        }
        node.setModifier( m.getKeyword(), true );
        return this;
    }

    public _modifiers unset(String keyword ) {
        Modifier m = Modifiers.MODS_KEYWORD_TO_ENUM_MAP.get( keyword );
        if( m == null ) {
            throw new IllegalArgumentException( "invalid modifier keyword \"" + keyword + "\"" );
        }
        node.setModifier( m.getKeyword(), false );
        return this;
    }

    public _modifiers set(Modifier.Keyword keyword ) {
        if( keyword.equals( Modifier.Keyword.PUBLIC) ||
            keyword.equals( Modifier.Keyword.PROTECTED ) ||
            keyword.equals( Modifier.Keyword.PRIVATE ) ) {// || mod == Modifier.DEFAULT ) {
            setDefaultAccess();
        }
        node.setModifier( keyword, true );
        return this;
    }
    public _modifiers set(Modifier mod ) {
        if( mod.equals( Modifier.publicModifier()) ||
                mod.equals( Modifier.privateModifier() ) ||
                mod.equals(Modifier.protectedModifier() ) ) {// || mod == Modifier.DEFAULT ) {
            setDefaultAccess();
        }
        node.setModifier( mod.getKeyword(), true );
        return this;
    }

    public _modifiers unset(Modifier mod ) {
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

    @Override
    public List<_modifier> list() {
        List<_modifier> mods = new ArrayList<>();
        this.node.getModifiers().forEach(m -> mods.add( _modifier.of((Modifier)m)) );
        return mods;
    }

    public NodeList<Modifier> listAstElements(){
        return this.node.getModifiers();
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

    public _modifiers setDefault( boolean b){
        this.node.setModifier(Modifier.Keyword.DEFAULT, b );
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

    public _modifiers setAbstract(boolean toSet ) {
        this.node.setModifier( Modifier.Keyword.ABSTRACT, toSet );
        return this;
    }

    public _modifiers setStatic(boolean toSet ) {
        this.node.setModifier( Modifier.Keyword.STATIC, toSet );
        return this;
    }

    public _modifiers setFinal(boolean toSet ) {
        this.node.setModifier( Modifier.Keyword.FINAL, toSet );
        return this;
    }

    public _modifiers setSynchronized(boolean toSet ) {
        this.node.setModifier( Modifier.Keyword.SYNCHRONIZED, toSet );
        return this;
    }

    public _modifiers setNative(boolean toSet ) {
        this.node.setModifier( Modifier.Keyword.NATIVE, toSet );
        return this;
    }

    public _modifiers setTransient(boolean toSet ) {
        this.node.setModifier( Modifier.Keyword.TRANSIENT, toSet );
        return this;
    }

    public _modifiers setVolatile(boolean toSet ) {
        this.node.setModifier( Modifier.Keyword.VOLATILE, toSet );
        return this;
    }

    public _modifiers setStrictFp(boolean toSet ) {
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

    public int asInt(){
        int modBits = 0;
        if( node != null && node.getModifiers() != null ) {
            NodeList<Modifier> ms = this.node.getModifiers();
            for(int i=0;i<ms.size();i++){

                modBits = modBits | Modifiers.MODS_KEYWORD_TO_BIT_MAP.get( ms.get(i).getKeyword().asString() );
            }
        }
        return modBits;
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
            keywords[ i ] = (Modifiers.MODS_BIT_TO_KEYWORD_MAP.get( nextBit ));

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
            sb.append( Modifiers.MODS_BIT_TO_KEYWORD_MAP.get( nextBit ) );

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

    /**
     *
     * @see _variablesExpr
     *
     * @see _type
     *     @see _class
     *     @see _enum
     *     @see _interface
     *     @see _annotation
     *
     *
     * @see
     * @see _method
     * @see _field
     *
     *
     * @author Eric
     * @param <_HM> the target TYPE
     */
    public interface _withModifiers<_HM extends _withModifiers>
        extends _java._domain {

        default boolean isExplicitModifiers( int bitMask){
            return getModifiersAsBitMask() == bitMask;
        }

        default boolean isExplicitModifiers( _modifiers _ms){
            return getModifiersAsBitMask() == _ms.asInt();
        }

        default boolean isEffectiveModifiers( _modifiers _ms){
            return _modifiers.of(getEffectiveModifiers()).asInt() == _ms.asInt();
        }

        default boolean isEffectiveModifiers( int bitMask){
            return _modifiers.of(getEffectiveModifiers()).asInt() == bitMask;
        }

        /**
         * gets the explicitly set modifiers for the node
         * @return the explicitly set modifiers
         */
        _modifiers getModifiers();

        /**
         * Returns the Effective Modifiers as a bitMask
         * @return an int representing the BitMask of the modifiers
         */
        default int getModifiersAsBitMask(){
            NodeList<Modifier> effective = getEffectiveModifiers();
            int bitMask = 0;
            for(int i=0;i<effective.size();i++){
                bitMask |= Modifiers.MODS_KEYWORD_TO_BIT_MAP.get( effective.get(i).getKeyword().asString() );
            }
            return bitMask;
        }

        /**
         * Set the modifiers based on the keywords passed in
         * @param mods
         * @return
         */
        default _HM setModifiers(String... mods) {
            String ms = Text.combineTrim(mods);
            String[] mms = ms.split(" ");
            getModifiers().set(mms);
            return (_HM)this;
        }

        /**
         *
         * @param mods
         * @return
         */
        default _HM setModifiers(NodeList<Modifier> mods){
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
         * @param _mods
         * @return
         */
        default _HM setModifiers(_modifiers _mods) {
            //return setModifiers( mods );
            getModifiers().clear().set( _mods.asKeywords() );
            return (_HM)this;
        }
    }

    /**
     * entity with possible final modifier
     * @param <_WF>
     */
    public interface _withFinal<_WF extends _withFinal> {

        boolean isFinal();

        default _WF setFinal(){
            return setFinal(true);
        }

        _WF setFinal(boolean toSet);
    }

    /**
     * Entity with possible static modifier
     * @param <_WS>
     */
    public interface _withStatic<_WS extends _withStatic>  extends _withModifiers<_WS> {

        boolean isStatic();

        default _WS setStatic(){
            return setStatic(true);
        }

        default _WS setStatic(boolean toSet){
            _java._node n = (_java._node)this;
            NodeWithModifiers nwm = (NodeWithModifiers)n.ast();
            nwm.setModifier(Modifier.Keyword.STATIC, toSet);
            return (_WS)this;
        }
    }

    /**
     * Entity with possible synchronized modifier
     * @param <_WS>
     */
    public interface _withSynchronized<_WS extends _withSynchronized> extends _withModifiers<_WS> {

        default boolean isSynchronized(){
            return getModifiers().node.hasModifier(Modifier.Keyword.SYNCHRONIZED );
        }

        default _WS setSynchronized(){
            return setSynchronized(true);
        }

        default _WS setSynchronized(boolean toSet){
            _java._node n = (_java._node)this;
            NodeWithModifiers nwm = (NodeWithModifiers)n.ast();
            nwm.setModifier(Modifier.Keyword.SYNCHRONIZED, toSet);
            return (_WS)this;
        }
    }

    /**
     * Entity with possible abstract modifier
     * @param <_WA>
     */
    public interface _withAbstract<_WA extends _withAbstract> extends _withModifiers<_WA> {

        boolean isAbstract();

        default _WA setAbstract(){
            return setAbstract(true);
        }

        default _WA setAbstract(boolean toSet){
            _java._node n = (_java._node)this;
            NodeWithModifiers nwm = (NodeWithModifiers)n.ast();
            nwm.setModifier(Modifier.Keyword.ABSTRACT, toSet);
            return (_WA)this;
        }
    }

    /**
     * Entity with possible volatile modifier
     * @param <_HV>
     */
    public interface _withVolatile<_HV extends _withVolatile> extends _withModifiers<_HV> {

        default boolean isVolatile(){
            return getModifiers().node.hasModifier(Modifier.Keyword.VOLATILE );
        }

        default _HV setVolatile(){
            return setVolatile(true);
        }

        default _HV setVolatile(boolean toSet){
           _java._node n = (_java._node)this;
            NodeWithModifiers nwm = (NodeWithModifiers)n.ast();
            nwm.setModifier(Modifier.Keyword.VOLATILE, toSet);
            return (_HV)this;
        }
    }

    /**
     * Entity with possible native modifier
     * @param <_HN>
     */
    public interface _withNative<_HN extends _withNative>  extends _withModifiers<_HN> {

        default boolean isNative(){
            return getModifiers().node.hasModifier(Modifier.Keyword.NATIVE );
        }

        default _HN setNative(){
            return setNative(true);
        }

        default _HN setNative(boolean toSet){
            _java._node n = (_java._node)this;
            NodeWithModifiers nwm = (NodeWithModifiers)n.ast();
            nwm.setModifier(Modifier.Keyword.NATIVE, toSet);
            return (_HN)this;
        }
    }

    /**
     * Entity with possible transient modifier
     * @param <_HT>
     */
    public interface _withTransient<_HT extends _withTransient>  extends _withModifiers<_HT> {

        default boolean isTransient(){
            return getModifiers().node.hasModifier(Modifier.Keyword.TRANSIENT );
        }

        default _HT setTransient(){
            return setTransient(true);
        }

        default _HT setTransient(boolean toSet){
            _java._node n = (_java._node)this;
            NodeWithModifiers nwm = (NodeWithModifiers)n.ast();
            nwm.setModifier(Modifier.Keyword.TRANSIENT, toSet);
            return (_HT)this;
        }
    }

    /**
     * Entity with possible strictFp modifier
     * @param <_HS>
     */
    public interface _withStrictFp<_HS extends _withStrictFp>  extends _withModifiers<_HS> {

        default boolean isStrictFp(){
            return getModifiers().node.hasModifier(Modifier.Keyword.STRICTFP );
        }

        default _HS setStrictFp(){
            return setStrictFp(true);
        }

        default _HS setStrictFp(boolean toSet){
            _java._node n = (_java._node)this;
            NodeWithModifiers nwm = (NodeWithModifiers)n.ast();
            nwm.setModifier(Modifier.Keyword.STRICTFP, toSet);
            return (_HS)this;
        }
    }
}
